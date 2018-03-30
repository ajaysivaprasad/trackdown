package com.example.jithin.trackdown;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class MainActivity extends AppCompatActivity {

    static boolean CurrentlyRunning= false;

    String name="";
    int level,Clevel=0,position;
    String android_id;
    Participant P = new Participant();
    String title="",content="",display="",notification="";
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference noti_myRef = database.getReference("Notification").child("All");
    FragmentManager fragmentManager = getFragmentManager();
    Home h = new Home();
    Level1 l1 = new Level1();
    Level2 l2 = new Level2();
    Level3 l3 = new Level3();
    Level4 l4 = new Level4();
    Level5 l5 = new Level5();
    Level6 l6 = new Level6();
    Level7 l7 = new Level7();
    Level8 l8 = new Level8();
    Level9 l9 = new Level9();
    Level10 l10 = new Level10();
    Level11 l11 = new Level11();
    Level12a l12a = new Level12a();
    Level12b l12b = new Level12b();
    Level12c l12c = new Level12c();
    Level13 l13 = new Level13();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if(android_id.equals(P.Participant_Id)){
            SharedPreferences prefs = getSharedPreferences("LEVEL", MODE_PRIVATE);
            level=prefs.getInt("level",7000);
            SelectLevel(level);

        }else {
            ImageView noshare = (ImageView)findViewById(R.id.noshare);
            noshare.setVisibility(View.VISIBLE);
            //finish();
        }

        DatabaseReference databaseRef = database.getReference("Participants");
        databaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange (DataSnapshot dataSnapshot) {

                position = 0;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // parse the snapshot to your local model
                    User user = postSnapshot.getValue(User.class);

                    // access your desired field
                    name = user.CurrentLevel;

                    if(Integer.parseInt(name)>Clevel)
                    {
                        position++;
                    }
                }
            }

            @Override
            public void onCancelled (DatabaseError databaseError) {

            }
        });



        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {

                    if(CurrentlyRunning) {
                        Toast.makeText(MainActivity.this, "Internet Connected", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if(CurrentlyRunning) {
                        Toast.makeText(MainActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    }
                    }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        DatabaseReference myRef_d = database.getReference("Participants").child(P.Participant_No).child("Data").child("Device Id");
        myRef_d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!P.Participant_Id.equals(dataSnapshot.getValue(String.class))){
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        DatabaseReference myRef_title = database.getReference("Notification").child("Current").child("Title");
        myRef_title.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                title = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        DatabaseReference myRef_content = database.getReference("Notification").child("Current").child("Content");
        myRef_content.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                content = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        DatabaseReference myRef = database.getReference("Notification").child("Current").child("Display");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                display = dataSnapshot.getValue(String.class);

                if(display.equals("yes")){
                    notification(title,content);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        noti_myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                notification = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void notification(String title1,String content1){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(title1);
        adb.setCancelable(false);
        adb.setMessage(content1);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                return;
            }
        });
        if(CurrentlyRunning) {
            adb.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.notification) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Notifications");
            adb.setCancelable(false);
            adb.setMessage(notification);
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    return;
                }
            });
            adb.show();
        }else if (id == R.id.leader) {

            new leveldownload().execute();

        }
        return true;
    }
    public void onStart() {
        super.onStart();
        CurrentlyRunning= true;
    }
    public void onStop() {
        super.onStop();
        CurrentlyRunning= false;
    }
    public void SelectLevel(int l){

        SharedPreferences prefs = getSharedPreferences("lead", MODE_PRIVATE);
        int m;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (l){
            case 7000:Clevel = 0;
                fragmentTransaction.replace(R.id.layout, h);fragmentTransaction.commit();
                break;
            case 1650:Clevel = 1;
                fragmentTransaction.replace(R.id.layout, l1);fragmentTransaction.commit();
                break;
            case 2659:Clevel = 2;
                m=prefs.getInt("l2",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l2", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l2);fragmentTransaction.commit();
                break;
            case 3053:Clevel = 3;
                m=prefs.getInt("l3",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l3", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l3);fragmentTransaction.commit();
                break;
            case 4305:Clevel = 4;
                m=prefs.getInt("l4",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l4", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l4);fragmentTransaction.commit();
                break;
            case 5210:Clevel = 5;
                m=prefs.getInt("l5",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l5", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l5);fragmentTransaction.commit();
                break;
            case 6295:Clevel = 6;
                m=prefs.getInt("l6",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l6", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l6);fragmentTransaction.commit();
                break;
            case 7678:Clevel = 7;
                m=prefs.getInt("l7",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l7", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l7);fragmentTransaction.commit();
                break;
            case 8493:Clevel = 8;
                m=prefs.getInt("l8",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l8", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l8);fragmentTransaction.commit();
                break;
            case 9192:Clevel = 9;
                m=prefs.getInt("l9",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l9", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l9);fragmentTransaction.commit();
                break;
            case 1074:Clevel = 10;
                m=prefs.getInt("l10",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l10", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l10);fragmentTransaction.commit();
                break;
            case 1132:Clevel = 11;
                m=prefs.getInt("l11",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l11", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l11);fragmentTransaction.commit();
                break;
            case 1296:Clevel = 12;
                m=prefs.getInt("l12",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l12", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l12a);fragmentTransaction.commit();
                break;
            case 1343:Clevel = 13;
                m=prefs.getInt("l13",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l13", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l12b);fragmentTransaction.commit();
                break;
            case 1476:Clevel = 14;
                m=prefs.getInt("l14",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l14", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l12c);fragmentTransaction.commit();
                break;
            case 1543:Clevel = 15;
                m=prefs.getInt("l15",0);
                if(m==0){
                    SharedPreferences.Editor editor = this.getSharedPreferences("lead", MODE_PRIVATE).edit();
                    editor.putInt("l15", 1);
                    editor.apply();
                    new levelupload().execute();
                }
                fragmentTransaction.replace(R.id.layout, l13);fragmentTransaction.commit();
                break;
        }
    }


    public void finishapp(){
        this.finish();
    }

    private class levelupload extends AsyncTask<Void, Void, Void> {

        String jsonStr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall("https://us-central1-leaderboard-7ab49.cloudfunctions.net/leaberboard?level="+(Clevel-1)+"&operation=add");

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


        }

    }

    private class leveldownload extends AsyncTask<Void, Void, Void> {

        String jsonStr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall("https://us-central1-leaderboard-7ab49.cloudfunctions.net/leaberboard?level="+Clevel+"&operation=return");

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {

                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("My Id : " + P.Participant_No);
                adb.setCancelable(false);

                if (Clevel < 1) {
                    adb.setMessage("Game not started");
                } else {
                    switch (Clevel) {
                        case 12:
                            adb.setMessage(jsonStr.replaceAll("\\s+", "") + " Participant(s) completed Level " + 12.1);
                            break;
                        case 13:
                            adb.setMessage(jsonStr.replaceAll("\\s+", "") + " Participant(s) completed Level " + 12.2);
                            break;
                        case 14:
                            adb.setMessage(jsonStr.replaceAll("\\s+", "") + " Participant(s) completed Level " + 12.3);
                            break;
                        case 15:
                            adb.setMessage("After completing this final level, please wait for the announcement of Winners");
                            break;
                        default:
                            adb.setMessage(jsonStr.replaceAll("\\s+", "") + " Participant(s) completed Level " + Clevel);
                    }

                }

                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        return;
                    }
                });
                adb.show();

            }catch (Exception e){

            }
        }

    }

}
