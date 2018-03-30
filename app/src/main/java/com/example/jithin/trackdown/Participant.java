package com.example.jithin.trackdown;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Participant {



    public final String Participant_Id = "6aff9fb109e6ba43";
    public final String Participant_No = "p307";



public final long Game_Start_Time = 1507357800;


    public void SetTime(Context context,int n,long t){
        SharedPreferences.Editor editor = context.getSharedPreferences("LEVEL", MODE_PRIVATE).edit();
        switch (n){
            case 1:editor.putLong("T1",t);
                break;
            case 2:editor.putLong("T2",t);
                break;
            case 3:editor.putLong("T3",t);
                break;
            case 4:editor.putLong("T4",t);
                break;
            case 5:editor.putLong("T5",t);
                break;
            case 6:editor.putLong("T6",t);
                break;
            case 7:editor.putLong("T7",t);
                break;
            case 8:editor.putLong("T8",t);
                break;
            case 9:editor.putLong("T9",t);
                break;
            case 10:editor.putLong("T10",t);
                break;
            case 11:editor.putLong("T11",t);
                break;
            case 121:editor.putLong("T12a",t);
                break;
            case 122:editor.putLong("T12b",t);
                break;
            case 123:editor.putLong("T12c",t);
                break;
            case 13:editor.putLong("T13",t);
                break;
        }
        editor.apply();
    }
    public long GetTime(Context context, int n){
        SharedPreferences prefs = context.getSharedPreferences("LEVEL", MODE_PRIVATE);
        switch (n){
            case 1:return prefs.getLong("T1",0);
            case 2:return prefs.getLong("T2",0);
            case 3:return prefs.getLong("T3",0);
            case 4:return prefs.getLong("T4",0);
            case 5:return prefs.getLong("T5",0);
            case 6:return prefs.getLong("T6",0);
            case 7:return prefs.getLong("T7",0);
            case 8:return prefs.getLong("T8",0);
            case 9:return prefs.getLong("T9",0);
            case 10:return prefs.getLong("T10",0);
            case 11:return prefs.getLong("T11",0);
            case 121:return prefs.getLong("T12a",0);
            case 122:return prefs.getLong("T12b",0);
            case 123:return prefs.getLong("T12c",0);
            case 13:return prefs.getLong("T13",0);
        }
        return 0;
    }

}
