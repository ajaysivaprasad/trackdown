package com.example.jithin.trackdown;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Level13 extends Fragment {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    Participant P = new Participant();
    FloatingActionButton go;
    EditText password;
    ImageView audio;
    String Q,G;
    TextView lno,f,t;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.last, container,
                false);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("LEVEL", MODE_PRIVATE).edit();
        editor.putInt("level", 1543);
        editor.apply();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffcc0000")));

        lno = (TextView) rootView.findViewById(R.id.text);
        lno.setText("Level 13");

        f=(TextView)rootView.findViewById(R.id.sol);
        t=(TextView)rootView.findViewById(R.id.sol1);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        DatabaseReference myRef_g = database.getReference("Participants").child(P.Participant_No).child("Data").child("Gmail");
        myRef_g.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                G = dataSnapshot.getValue(String.class);
                f.setText("\nFrom : "+G);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });


        DatabaseReference myRef_a = database.getReference("Questions").child("l15").child("q");
        myRef_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Q = dataSnapshot.getValue(String.class);
                t.setText(Q);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });


        return rootView;
    }

}
