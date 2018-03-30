package com.example.jithin.trackdown;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.telecom.TelecomManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class Level5 extends Fragment {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    Participant P = new Participant();
    MediaPlayer mp;
    FloatingActionButton go;
    EditText password;
    String A;
    TextView lno;
    ProgressBar spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.empty, container,
                false);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("LEVEL", MODE_PRIVATE).edit();
        editor.putInt("level", 5210);
        editor.apply();

        spinner = (ProgressBar)rootView.findViewById(R.id.progressBar1);

        lno = (TextView) rootView.findViewById(R.id.text);
        lno.setText("Level 5");

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        DatabaseReference myRef_a = database.getReference("Questions").child("l5").child("a");
        myRef_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                A = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });


        AudioManager audio = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);

        //mp = MediaPlayer.create(getActivity(),R.raw.audio);
        //mp.setLooping(true);
        //mp.start();


        password = (EditText)rootView.findViewById(R.id.password);

        go = (FloatingActionButton)rootView.findViewById(R.id.fab);

        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String s=password.getText().toString();
                s=s.replaceAll("\\s+","");
                if(s.equals(A) && s.length() > 3){

                    new upload().execute();

                    /*if(currenttime()){
                        mp.stop();
                        ((MainActivity) getActivity()).SelectLevel(6);
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

    @Override
    public void onPause() {
        super.onPause();
        mp.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mp = MediaPlayer.create(getActivity(),R.raw.audio);
        mp.setLooping(true);
        mp.start();
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

            P.SetTime(getActivity(),5,Current);

            //Toast.makeText(getApplicationContext(),""+Current, Toast.LENGTH_LONG).show();


            long time = Current-P.GetTime(getActivity(),4);
            // decompose difference into days, hours, minutes and seconds

            int days = (int) (time / 86400);
            int hours = (int) ((time - (days
                    * 86400)) / 3600);
            int minutes = (int) ((time - ((days
                    * 86400) + (hours * 3600))) / 60);
            int seconds = (int) (time % 60);

            String h="00" +hours,m="00" +minutes,s="00" +seconds;

            String TimeTaken=h.substring(h.length()-2)+":"+m.substring(m.length()-2)+":"+s.substring(s.length()-2);
            DatabaseReference participant_myRef = database.getReference("Participants").child(P.Participant_No).child("Game").child("Level 05");
            participant_myRef.setValue(TimeTaken + " - " + Current);

            DatabaseReference participant_level = database.getReference("Participants").child(P.Participant_No).child("CurrentLevel");
            participant_level.setValue("6");


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
                mp.stop();
                ((MainActivity) getActivity()).SelectLevel(6295);
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
