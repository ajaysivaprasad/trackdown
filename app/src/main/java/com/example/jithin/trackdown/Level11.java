package com.example.jithin.trackdown;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class Level11 extends Fragment {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    Participant P = new Participant();
    FloatingActionButton go;
    TextView lno;
    EditText For;
    ProgressBar spinner;
    String folder_path=Environment.getExternalStorageDirectory() + File.separator+"TrackDown";
    String file_path1=Environment.getExternalStorageDirectory() + File.separator+"TrackDown" + File.separator+"track.txt";
    String file_path2=Environment.getExternalStorageDirectory() + File.separator+"TrackDown" + File.separator+"down.pdf";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fold, container,
                false);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("LEVEL", MODE_PRIVATE).edit();
        editor.putInt("level", 1132);
        editor.apply();

        spinner = (ProgressBar)rootView.findViewById(R.id.progressBar1);

        lno = (TextView) rootView.findViewById(R.id.text);
        lno.setText("Level 11");







        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

        File folder = new File(folder_path);
        File file1 = new File(file_path1);
        File file2 = new File(file_path2);

        if(!folder.exists()){

            adb.setCancelable(false);
            adb.setTitle("Unfortunately, Track Down has stopped");
            adb.setMessage("Due to FolderNotFoundException\nPath: /Internal Storage/TrackDown");
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity) getActivity()).finishapp();
                    return;
                }
            });
            adb.show();
        }else if(!file1.exists()){

            if(!file2.exists()) {
                adb.setCancelable(false);
                adb.setTitle("Unfortunately, Track Down has stopped");
                adb.setMessage("Due to FileNotFoundException\nPath: /Internal Storage/TrackDown/track.txt");
                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) getActivity()).finishapp();
                        return;
                    }
                });
                adb.show();
            }else{

                adb.setCancelable(false);
                adb.setTitle("Unfortunately, Level 11 has completed");
                adb.setMessage("Click Ok to go to next level");
                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new upload().execute();
                        return;
                    }
                });
                adb.show();



                /*if(currenttime()){
                    ((MainActivity) getActivity()).SelectLevel(121);
                }else{
                    Toast.makeText(getActivity(),"Network Error!", Toast.LENGTH_SHORT).show();
                }*/
            }
        }else if(!file2.exists()){

            adb.setCancelable(false);
            adb.setTitle("Unfortunately, Track Down has stopped");
            adb.setMessage("Due to FileNotFoundException\nPath: /Internal Storage/TrackDown/down.pdf");
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity) getActivity()).finishapp();
                    return;
                }
            });
            adb.show();

        }else{
            adb.setCancelable(false);
            adb.setTitle("Unfortunately, Track Down has stopped");
            adb.setMessage("Due to FileFoundException\nPath: /Internal Storage/TrackDown/track.txt");
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity) getActivity()).finishapp();
                    return;
                }
            });
            adb.show();
        }


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

            P.SetTime(getActivity(),11,Current);

            //Toast.makeText(getApplicationContext(),""+Current, Toast.LENGTH_LONG).show();


            long time = Current-P.GetTime(getActivity(),10);
            // decompose difference into days, hours, minutes and seconds

            int days = (int) (time / 86400);
            int hours = (int) ((time - (days
                    * 86400)) / 3600);
            int minutes = (int) ((time - ((days
                    * 86400) + (hours * 3600))) / 60);
            int seconds = (int) (time % 60);

            String h="00" +hours,m="00" +minutes,s="00" +seconds;

            String TimeTaken=h.substring(h.length()-2)+":"+m.substring(m.length()-2)+":"+s.substring(s.length()-2);
            DatabaseReference participant_myRef = database.getReference("Participants").child(P.Participant_No).child("Game").child("Level 11");
            participant_myRef.setValue(TimeTaken + " - " + Current);

            DatabaseReference participant_level = database.getReference("Participants").child(P.Participant_No).child("CurrentLevel");
            participant_level.setValue("12");


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
                ((MainActivity) getActivity()).SelectLevel(1296);
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
