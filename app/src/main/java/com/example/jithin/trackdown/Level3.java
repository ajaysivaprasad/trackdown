package com.example.jithin.trackdown;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

public class Level3 extends Fragment {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    Participant P = new Participant();
    FloatingActionButton go;
    TextView q1,q2,q3,q4,q5,lno;
    EditText a1,a2,a3,a4,a5;
    String A1,A2,A3,A4,A5;
    ProgressBar spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.quiz, container,
                false);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("LEVEL", MODE_PRIVATE).edit();
        editor.putInt("level", 3053);
        editor.apply();

        spinner = (ProgressBar)rootView.findViewById(R.id.progressBar1);

        lno = (TextView) rootView.findViewById(R.id.text);
        lno.setText("Level 3");

        q1 = (TextView)rootView.findViewById(R.id.q1);
        q2 = (TextView)rootView.findViewById(R.id.q2);
        q3 = (TextView)rootView.findViewById(R.id.q3);
        q4 = (TextView)rootView.findViewById(R.id.q4);
        q5 = (TextView)rootView.findViewById(R.id.q5);
        a1 = (EditText)rootView.findViewById(R.id.a1);
        a2 = (EditText)rootView.findViewById(R.id.a2);
        a3 = (EditText)rootView.findViewById(R.id.a3);
        a4 = (EditText)rootView.findViewById(R.id.a4);
        a5 = (EditText)rootView.findViewById(R.id.a5);


        DatabaseReference myRef_q = database.getReference("Questions").child("l3").child("q");
        myRef_q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String s = dataSnapshot.getValue(String.class);

                String[] parts = s.split("----");
                q1.setText(parts[0]);
                q2.setText(parts[1]);
                q3.setText(parts[2]);
                q4.setText(parts[3]);
                q5.setText(parts[4]);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
        DatabaseReference myRef_a = database.getReference("Questions").child("l3").child("a");
        myRef_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String s = dataSnapshot.getValue(String.class);

                String[] parts = s.split("----");

                A1=parts[0];
                A2=parts[1];
                A3=parts[2];
                A4=parts[3];
                A5=parts[4];

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });



        go = (FloatingActionButton)rootView.findViewById(R.id.fab);

        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String s1=a1.getText().toString();
                s1=s1.replaceAll("\\s+","");
                s1=s1.toLowerCase();
                String s2=a2.getText().toString();
                s2=s2.replaceAll("\\s+","");
                s2=s2.toLowerCase();
                String s3=a3.getText().toString();
                s3=s3.replaceAll("\\s+","");
                s3=s3.toLowerCase();
                String s4=a4.getText().toString();
                s4=s4.replaceAll("\\s+","");
                s4=s4.toLowerCase();
                String s5=a5.getText().toString();
                s5=s5.replaceAll("\\s+","");
                s5=s5.toLowerCase();
                if(s1.equals(A1) && s2.equals(A2) && s3.equals(A3) && s4.equals(A4) && s5.equals(A5) && s1.length() > 0){

                    new upload().execute();

                    /*if(currenttime()){
                        ((MainActivity) getActivity()).SelectLevel(4);
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

            P.SetTime(getActivity(),3,Current);

            //Toast.makeText(getApplicationContext(),""+Current, Toast.LENGTH_LONG).show();


            long time = Current-P.GetTime(getActivity(),2);
            // decompose difference into days, hours, minutes and seconds

            int days = (int) (time / 86400);
            int hours = (int) ((time - (days
                    * 86400)) / 3600);
            int minutes = (int) ((time - ((days
                    * 86400) + (hours * 3600))) / 60);
            int seconds = (int) (time % 60);

            String h="00" +hours,m="00" +minutes,s="00" +seconds;

            String TimeTaken=h.substring(h.length()-2)+":"+m.substring(m.length()-2)+":"+s.substring(s.length()-2);
            DatabaseReference participant_myRef = database.getReference("Participants").child(P.Participant_No).child("Game").child("Level 03");
            participant_myRef.setValue(TimeTaken + " - " + Current);

            DatabaseReference participant_level = database.getReference("Participants").child(P.Participant_No).child("CurrentLevel");
            participant_level.setValue("4");

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
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
                ((MainActivity) getActivity()).SelectLevel(4305);
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
