package com.example.jithin.trackdown;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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


public class Level1 extends Fragment {

    String Q;
    Participant P = new Participant();
    TextView para_question,lno;
    EditText para_answer;
    FloatingActionButton go;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    ProgressBar spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.para_type, container,
                false);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("LEVEL", MODE_PRIVATE).edit();
        editor.putInt("level", 1650);
        editor.apply();

        spinner = (ProgressBar)rootView.findViewById(R.id.progressBar1);


        lno = (TextView) rootView.findViewById(R.id.text);
        lno.setText("Level 1");
        para_question = (TextView) rootView.findViewById(R.id.para_question);






        DatabaseReference myRef_content = database.getReference("Questions").child("l1").child("q");
        myRef_content.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Q = dataSnapshot.getValue(String.class);

                para_question.setText(Q);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });










        //para_question.setText("dI$ !s g0nNa b3 a v3rY DifF1cult l*ve7 4 uuuuuiuuuu l0ser$. BeTeter b@kkck 0uT b3f0Re y)0 6666 d00m%d. #inclde>stReam.h>F[pos].n3wnode->index =r;F[pos].newn0de->next ifF[pos].St@rt==NUL). 00lanmJisamakesaalamabanaloserwork. JLKD@@@gR3@t$tc0D3r0fTh320thC3ntUry.0n1y$tup. kutt@njlkitsmech@thiyanjon$n0w.");
        para_answer = (EditText) rootView.findViewById(R.id.para_answer);
        go = (FloatingActionButton) rootView.findViewById(R.id.fab);
        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                String s = para_question.getText().toString();
                s = s.replaceAll("\\s+", "");
                String s1 = para_answer.getText().toString();
                s1 = s1.replaceAll("\\s+", "");
                if (s.equals(s1) && s1.length() > 3) {

                    new upload().execute();

                    /*if (currenttime()) {

                        ((MainActivity) getActivity()).SelectLevel(2);
                    } else {
                        Toast.makeText(getActivity(), "Network Error!", Toast.LENGTH_SHORT).show();
                    }*/

                } else {
                    Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
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

            P.SetTime(getActivity(),1,Current);

            //Toast.makeText(getApplicationContext(),""+Current, Toast.LENGTH_LONG).show();


        long time = Current-P.Game_Start_Time;
                // decompose difference into days, hours, minutes and seconds

                int days = (int) (time / 86400);
                int hours = (int) ((time - (days
                        * 86400)) / 3600);
                int minutes = (int) ((time - ((days
                        * 86400) + (hours * 3600))) / 60);
                int seconds = (int) (time % 60);

                String h="00" +hours,m="00" +minutes,s="00" +seconds;

        String TimeTaken=h.substring(h.length()-2)+":"+m.substring(m.length()-2)+":"+s.substring(s.length()-2);
        DatabaseReference participant_myRef = database.getReference("Participants").child(P.Participant_No).child("Game").child("Level 01");
        participant_myRef.setValue(TimeTaken + " - " + Current);

            DatabaseReference participant_level = database.getReference("Participants").child(P.Participant_No).child("CurrentLevel");
        participant_level.setValue("2");



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
                ((MainActivity) getActivity()).SelectLevel(2659);
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
