package com.example.jithin.trackdown;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class Level12c extends Fragment {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    Participant P = new Participant();
    FloatingActionButton go;
    EditText answer;
    ImageView location;
    int no = 0;
    TextView question,lno;
    ProgressBar spinner;
    int a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.que, container,
                false);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("LEVEL", MODE_PRIVATE).edit();
        editor.putInt("level", 1476);
        editor.apply();

        spinner = (ProgressBar)rootView.findViewById(R.id.progressBar1);

        lno = (TextView) rootView.findViewById(R.id.text);
        lno.setText("Level 12.3");

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        go = (FloatingActionButton)rootView.findViewById(R.id.fab);
        answer = (EditText)rootView.findViewById(R.id.answer);
        question = (TextView)rootView.findViewById(R.id.que);
        no = 1;
        question.setText("Type a number between 11 and 99");

        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String s = answer.getText().toString();

                if (s.equals("")) {
                    Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                }else{

                int result = Integer.parseInt(s);
                switch (no) {
                    case 1:
                        if (result > 11 && result < 99) {
                            a1 = result;
                            no = 2;
                            question.setText("Type a number between 125 and 150");
                            answer.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        if (result > 125 && result < 150) {
                            a2 = result;
                            no = 3;
                            question.setText("Type a number between 89 and 144");
                            answer.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        if (result > 89 && result < 144) {
                            a3 = result;
                            no = 4;
                            question.setText("Type a number between 124 and 150");
                            answer.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 4:
                        if (result > 124 && result < 150) {
                            a4 = result;
                            no = 5;
                            question.setText("Type a number between 10 and 25");
                            answer.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 5:
                        if (result > 10 && result < 25) {
                            a5 = result;
                            no = 6;
                            question.setText("Type a number which is multiple of 11");
                            answer.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 6:
                        if (result % 11 == 0) {
                            a6 = result;
                            no = 7;
                            question.setText("Type a number which is divisible by 13");
                            answer.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 7:
                        if (result % 13 == 0) {
                            a7 = result;
                            no = 8;
                            question.setText("(21-13)^3/(5*3+1)");
                            answer.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 8:
                        if (result == 32) {
                            a8 = result;
                            no = 9;
                            question.setText("Type your rollnumber");
                            answer.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 9:
                        if (result > 0 && result < 999) {
                            a9 = result;
                            no = 10;
                            question.setText("Type your semester");
                            answer.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 10:
                        if (result > 0 && result < 9) {
                            a10 = result;
                            no = 11;
                            question.setText("Type first 2 digits of your mobile number");
                            answer.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 11:
                        if (result > 9 && result < 100) {
                            a11 = result;
                            no = 12;
                            question.setText("Type your birth year");
                            answer.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 12:
                        if (result > 1900 && result < 2020) {
                            a12 = result;
                            no = 13;
                            question.setText("Ans1-Ans2-Ans3+Ans4-Ans5+Ans6+Ans7-Ans8+Ans9-Ans10+Ans11+Ans12");
                            answer.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 13:

                        a13 = a1 - a2 - a3 + a4 - a5 + a6 + a7 - a8 + a9 - a10 + a11 + a12;

                        if (result == a13) {

                            new upload().execute();

                            /*if(currenttime()){
                                ((MainActivity) getActivity()).SelectLevel(13);
                            }else{
                                Toast.makeText(getActivity(),"Network Error!", Toast.LENGTH_SHORT).show();
                            }*/

                        } else {
                            Toast.makeText(getActivity(), "Sorry!", Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
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

            P.SetTime(getActivity(),123,Current);

            //Toast.makeText(getApplicationContext(),""+Current, Toast.LENGTH_LONG).show();


            long time = Current-P.GetTime(getActivity(),122);
            // decompose difference into days, hours, minutes and seconds

            int days = (int) (time / 86400);
            int hours = (int) ((time - (days
                    * 86400)) / 3600);
            int minutes = (int) ((time - ((days
                    * 86400) + (hours * 3600))) / 60);
            int seconds = (int) (time % 60);

            String h="00" +hours,m="00" +minutes,s="00" +seconds;

            String TimeTaken=h.substring(h.length()-2)+":"+m.substring(m.length()-2)+":"+s.substring(s.length()-2);
            DatabaseReference participant_myRef = database.getReference("Participants").child(P.Participant_No).child("Game").child("Level 12c");
            participant_myRef.setValue(TimeTaken + " - " + Current);
            DatabaseReference participant_level = database.getReference("Participants").child(P.Participant_No).child("CurrentLevel");
            participant_level.setValue("14");

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
                ((MainActivity) getActivity()).SelectLevel(1543);
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
