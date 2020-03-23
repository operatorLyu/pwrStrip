package com.example.fileread;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class Retrival extends BroadcastReceiver {
    private static final String TAG = "Retrival";
    private MsgPipe msgPipe;

    @Override
    public void onReceive(Context context, Intent intent){
        Log.d(TAG, "onReceive: getBroadcast");
        BatteryInfo newInfo=BatteryUtil.update();
        int power=(int)(newInfo.voltage*newInfo.current);
        msgPipe.getMsg(power);
        Log.d(TAG, "onReceive: power="+power);
        AlarmManager alarmMgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent newIntent=new Intent("RETRIVAL");
        if(intent.getBooleanExtra("ifStop",true)){
            return;
        }
        else{
            newIntent.putExtra("ifStop",false);
        }
        PendingIntent alarmIntent=PendingIntent.getBroadcast(context,0,newIntent,0);
        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+100,alarmIntent);
        Log.d(TAG, "onReceive: sendBrodcast");
    }

    interface MsgPipe{
        public void getMsg(int power);
    }

    public void setMsgPipe(MsgPipe pipe){
        this.msgPipe=pipe;
    }
}
