package com.example.jithin.trackdown;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;

import static android.content.Context.MODE_PRIVATE;


public class Level2 extends Fragment{

    String A;
    EditText box_answer;
    FloatingActionButton go;
    int a=0;
    OrientationEventListener myOrientationEventListener;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    Participant P = new Participant();
    ImageView img_animation;
    TextView sol,lno;
    TranslateAnimation animation;
    boolean y1=false,y2=false;
    float ya1=0,ya2=0;
    ProgressBar spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.box, container,
                false);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("LEVEL", MODE_PRIVATE).edit();
        editor.putInt("level", 2659);
        editor.apply();

        spinner = (ProgressBar)rootView.findViewById(R.id.progressBar1);

        lno = (TextView) rootView.findViewById(R.id.text);
        lno.setText("Level 2");

        DatabaseReference myRef_content = database.getReference("Questions").child("l2").child("a");
        myRef_content.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                A = dataSnapshot.getValue(String.class);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });



        img_animation = (ImageView) rootView.findViewById(R.id.c);
        sol = (TextView) rootView.findViewById(R.id.sol);

        ViewTreeObserver vto=img_animation.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
                                          @Override public void onGlobalLayout(){
                                              int [] location = new int[2];
                                              img_animation.getLocationOnScreen(location);
                                              ya1 = location[1];
                                              y1=true;
                                              loc(ya1,ya2);
                                              img_animation.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                          }
                                      });
        ViewTreeObserver vt=sol.getViewTreeObserver();
        vt.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override public void onGlobalLayout(){
                int [] location = new int[2];
                sol.getLocationOnScreen(location);
                ya2 = location[1];
                y2=true;
                loc(ya1,ya2);
                sol.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });



        myOrientationEventListener
                = new OrientationEventListener(getActivity(), SensorManager.SENSOR_DELAY_NORMAL){

            @Override
            public void onOrientationChanged(int arg0) {

                if(arg0>140 && arg0<220){
                    if(a==0){
                        animation.setFillAfter(true);
                        img_animation.startAnimation(animation);
                        a=1;
                    }
                }else{
                    img_animation.clearAnimation();
                    a=0;
                }
            }};


        if (myOrientationEventListener.canDetectOrientation()){
            myOrientationEventListener.enable();
        }
        else{
            Toast.makeText(getActivity(),"Cannot Detect!", Toast.LENGTH_LONG).show();
        }


        box_answer = (EditText)rootView.findViewById(R.id.box_answer);
        go = (FloatingActionButton)rootView.findViewById(R.id.fab);

        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String s=box_answer.getText().toString();
                s=s.replaceAll("\\s+","");
                s=s.toLowerCase();
                if(s.equals(A) && s.length() > 2){

                    new upload().execute();

                    /*if(currenttime()){
                        myOrientationEventListener.disable();
                        ((MainActivity) getActivity()).SelectLevel(3);
                    }else{
                        Toast.makeText(getActivity(),"Network Error!", Toast.LENGTH_SHORT).show();
                    }*/

                }else{
                    Toast.makeText(getActivity(),"Sorry!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return rootView;
    }

    public boolean currenttime(){

        long Current=0;

        try {


            String url = "https://time.is/Unix_time_now";
            Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            String[] tags = new String[]{
                    "div[id=time_section]",
                    "div[id=clock0_bg]"
            };
            Elements elements = doc.select(tags[0]);
            for (int i = 0; i < tags.length; i++) {
                elements = elements.select(tags[i]);
            }

            Current=Long.parseLong(elements.text());

            P.SetTime(getActivity(),2,Current);

            //Toast.makeText(getApplicationContext(),""+Current, Toast.LENGTH_LONG).show();


            long time = Current-P.GetTime(getActivity(),1);
            // decompose difference into days, hours, minutes and seconds

            int days = (int) (time / 86400);
            int hours = (int) ((time - (days
                    * 86400)) / 3600);
            int minutes = (int) ((time - ((days
                    * 86400) + (hours * 3600))) / 60);
            int seconds = (int) (time % 60);

            String h="00" +hours,m="00" +minutes,s="00" +seconds;

            String TimeTaken=h.substring(h.length()-2)+":"+m.substring(m.length()-2)+":"+s.substring(s.length()-2);
            DatabaseReference participant_myRef = database.getReference("Participants").child(P.Participant_No).child("Game").child("Level 02");
            participant_myRef.setValue(TimeTaken + " - " + Current);
            DatabaseReference participant_level = database.getReference("Participants").child(P.Participant_No).child("CurrentLevel");
            participant_level.setValue("3");

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
    public void loc(float ya1,float ya2){
        if(y1 && y2){
            animation = new TranslateAnimation(0.0f, 0.0f, 0.0f,-(ya1 - ya2));
            animation.setDuration(700);
            animation.setFillAfter(true);
        }
    }

    private class upload extends AsyncTask<Void, Void, Void> {

        int a = 1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            spinner.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            if (currenttime()) {

                a=1;
                myOrientationEventListener.disable();
                ((MainActivity) getActivity()).SelectLevel(3053);
            } else {
                a=0;
                //Toast.makeText(getActivity(), "Network Error!", Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(a==0) {
                Toast.makeText(getActivity(), "Network Error!", Toast.LENGTH_SHORT).show();
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                spinner.setVisibility(View.GONE);
            }

        }

    }


}
