package com.example.jithin.trackdown;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

public class Home extends Fragment {

    RelativeLayout no_con;
    ScrollView con;
    LinearLayout refresh;
    Participant P = new Participant();
    long Game_Start = P.Game_Start_Time;
    TextView hour;
    TextView minute;
    TextView second;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home, container,
                false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        con=rootView.findViewById(R.id.internet_connection);
        no_con=rootView.findViewById(R.id.no_internet_connection);
        hour=rootView.findViewById(R.id.hour);
        minute=rootView.findViewById(R.id.minute);
        second=rootView.findViewById(R.id.second);


        if(isInternetAvailable("8.8.8.8", 53, 1000)){
            no_con.setVisibility(View.GONE);
            con.setVisibility(View.VISIBLE);
            CountDown();
        }else{
            con.setVisibility(View.GONE);
            no_con.setVisibility(View.VISIBLE);
        }


        refresh = rootView.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isInternetAvailable("8.8.8.8", 53, 1000)){
                    no_con.setVisibility(View.GONE);
                    con.setVisibility(View.VISIBLE);
                    CountDown();
                }else{
                    con.setVisibility(View.GONE);
                    no_con.setVisibility(View.VISIBLE);
                }
            }
        });


        return rootView;


    }

    private void CountDown(){

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

        }catch (Exception e){
            e.printStackTrace();
        }

        //Toast.makeText(this, ""+ Current, Toast.LENGTH_LONG).show();

        long time_milli=(Game_Start-Current)*1000;

        new CountDownTimer(time_milli, 1000) {
            public void onTick(long millisUntilFinished) {

                // decompose difference into days, hours, minutes and seconds
                int weeks = (int) ((millisUntilFinished / 1000) /
                        604800);
                int days = (int) ((millisUntilFinished / 1000) / 86400);
                int hours = (int) (((millisUntilFinished / 1000) - (days
                        * 86400)) / 3600);
                int minutes = (int) (((millisUntilFinished / 1000) - ((days
                        * 86400) + (hours * 3600))) / 60);
                int seconds = (int) ((millisUntilFinished / 1000) % 60);

                String h="00" +hours,m="00" +minutes,s="00" +seconds;

                hour.setText(h.substring(h.length()-2));
                minute.setText(m.substring(m.length()-2));
                second.setText(s.substring(s.length()-2));
            }

            public void onFinish() {

                ((MainActivity) getActivity()).SelectLevel(1650);

            }
        }.start();

    }

    public boolean isInternetAvailable(String address, int port, int timeoutMs) {
        try {
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress(address, port);

            sock.connect(sockaddr, timeoutMs); // This will block no more than timeoutMs
            sock.close();

            return true;

        } catch (IOException e) { return false; }
    }



}
