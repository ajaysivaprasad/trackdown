package com.example.jithin.trackdown;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class Level10 extends Fragment {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    Participant P = new Participant();
    RelativeLayout img;
    String url = "",url1="https://graph.facebook.com/",url2="/likes?summary=true&access_token=1938978109729614|9741c0aa9906a00f2f4d0b48cf51217b";
    String like = "0";
    FloatingActionButton fab;
    TextView no,likes,t,lno;
    String Q;
    ProgressBar spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fb, container,
                false);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("LEVEL", MODE_PRIVATE).edit();
        editor.putInt("level", 1074);
        editor.apply();

        spinner = (ProgressBar)rootView.findViewById(R.id.progressBar1);

        lno = (TextView) rootView.findViewById(R.id.text);
        lno.setText("Level 10");
        no = (TextView) rootView.findViewById(R.id.no);
        no.setText(P.Participant_No);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        t = (TextView) rootView.findViewById(R.id.sol);

        DatabaseReference myRef_a = database.getReference("Questions").child("l10").child("q");
        myRef_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Q = dataSnapshot.getValue(String.class);
                t.setText(Q + P.Participant_No + "-" + P.Participant_Id.substring(0,4));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        DatabaseReference fb = database.getReference("Participants").child(P.Participant_No).child("fb");
        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String f = dataSnapshot.getValue(String.class);
                if(f.equals("1")){

                    new upload().execute();

                    /*if(currenttime()){
                        ((MainActivity) getActivity()).SelectLevel(11);
                    }else{
                        Toast.makeText(getActivity(),"Network Error!", Toast.LENGTH_SHORT).show();
                    }*/

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


        likes=(TextView)rootView.findViewById(R.id.like);
        fab=(FloatingActionButton)rootView.findViewById(R.id.fab);
        img=(RelativeLayout) rootView.findViewById(R.id.img);

        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/13-levels-of-Track-Down-1991538814455323/"));
                startActivity(browserIntent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new GetContacts().execute();

            }
        });



        DatabaseReference myRef_title = database.getReference("Participants").child(P.Participant_No).child("Data").child("fb").child("fb_post_id");
        myRef_title.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                url = url1 + dataSnapshot.getValue(String.class) + url2;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
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

            P.SetTime(getActivity(),10,Current);

            //Toast.makeText(getApplicationContext(),""+Current, Toast.LENGTH_LONG).show();


            long time = Current-P.GetTime(getActivity(),9);
            // decompose difference into days, hours, minutes and seconds

            int days = (int) (time / 86400);
            int hours = (int) ((time - (days
                    * 86400)) / 3600);
            int minutes = (int) ((time - ((days
                    * 86400) + (hours * 3600))) / 60);
            int seconds = (int) (time % 60);

            String h="00" +hours,m="00" +minutes,s="00" +seconds;

            String TimeTaken=h.substring(h.length()-2)+":"+m.substring(m.length()-2)+":"+s.substring(s.length()-2);
            DatabaseReference participant_myRef = database.getReference("Participants").child(P.Participant_No).child("Game").child("Level 10");
            participant_myRef.setValue(TimeTaken + " - " + Current);

            DatabaseReference participant_level = database.getReference("Participants").child(P.Participant_No).child("CurrentLevel");
            participant_level.setValue("11");


        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    //JSONArray contacts = jsonObj.getJSONArray("contacts");
                        JSONObject c = jsonObj.getJSONObject("summary");

                        like = c.getString("total_count");

                } catch (Exception e) {
                }
            } else {
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            likes.setText("Number of likes = " + like);

            int l = Integer.parseInt(like);

            if(l >= 13){

                new upload().execute();

                /*if(currenttime()){
                    ((MainActivity) getActivity()).SelectLevel(11);
                }else{
                    Toast.makeText(getActivity(),"Network Error!", Toast.LENGTH_SHORT).show();
                }*/
            }else{
                Toast.makeText(getActivity(),"Sorry!", Toast.LENGTH_SHORT).show();
            }

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
                ((MainActivity) getActivity()).SelectLevel(1132);
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
