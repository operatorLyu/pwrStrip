package com.example.fileread;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BatteryUtil {
    private static final String TAG = "BatteryUtil";
    public static BatteryInfo update(){
        String sys_path="/sys/class/power_supply/battery/uevent";
        String data=new String();
        BatteryInfo info=new BatteryInfo();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("cat " + sys_path); // 此处进行读操作
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line ;
            while (null != (line = br.readLine())) {
                data=data+"\n"+line;
                //Log.d(TAG, "update: "+line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!data.isEmpty()){
            info.status=statusParse(data);
            info.capacity=capacityParse(data);
            info.voltage=voltageParse(data);
            info.current=currentParse(data);
            info.charge=chargeParse(data);
            //Log.d(TAG, "update: voltage"+info.voltage);
            //Log.d(TAG, "update: current"+info.current);
        }

        return info;
    }

    public static String statusParse(String data){
        String status=null;
        Pattern pattern=Pattern.compile(".*STATUS=([a-zA-Z]+)");
        Matcher m=pattern.matcher(data);
        if(m.find()){
            status=m.group(1);
        }
        return status;
    }

    public static int capacityParse(String data){
        int capacity=0;
        Pattern pattern=Pattern.compile(".*CAPACITY=(\\d+)");
        Matcher m=pattern.matcher(data);
        if(m.find()){
            capacity=Integer.parseInt(m.group(1));
        }
        return capacity;
    }

    public static double voltageParse(String data){
        double voltage=0;
        Pattern pattern=Pattern.compile(".*VOLTAGE_NOW=(\\d+)");
        Matcher m=pattern.matcher(data);
        if(m.find()){
            voltage=Integer.parseInt(m.group(1))/1000*0.001;
        }
        return voltage;     //in Volt
    }

    public static double currentParse(String data){
        double current=0;
        Pattern pattern=Pattern.compile(".*CURRENT_NOW=((-)?\\d+)");
        Matcher m=pattern.matcher(data);
        if(m.find()){
            current=Integer.parseInt(m.group(1))*0.001;
        }
        return current;     //in mA
    }

    public static int chargeParse(String data){
        int charge=0;
        Pattern pattern=Pattern.compile(".*CHARGE_COUNTER=(\\d+)");
        Matcher m=pattern.matcher(data);
        if(m.find()){
            charge=Integer.parseInt(m.group(1))/1000;
        }
        return charge;
    }
}

