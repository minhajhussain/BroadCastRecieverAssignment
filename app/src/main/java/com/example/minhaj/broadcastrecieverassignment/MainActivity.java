package com.example.minhaj.broadcastrecieverassignment;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //////////Constraints
    private EditText text;
    private TextView textToShow;
    private TextView batteryText;

    //////////////Broadcast




    private IntentFilter filterLow =  new IntentFilter(Intent.ACTION_BATTERY_LOW);
    private IntentFilter filterOkay = new IntentFilter(Intent.ACTION_BATTERY_OKAY);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Switch btnswitch = (Switch) findViewById(R.id.switch1);

        //////////For swtiching wifi
        btnswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true)
                {
                    WifiManager wifiOn = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiOn.setWifiEnabled(true);
                }
                else
                {
                    WifiManager wifiOff = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiOff.setWifiEnabled(false);
                }
            }
        });
        textToShow =(TextView) findViewById(R.id.saveText);
    }

        public void saveInfo(View view)
        {
            SharedPreferences sharedPreferences = getSharedPreferences("saveInfo",MODE_PRIVATE);
            text= (EditText) findViewById(R.id.editText);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Text",text.getText().toString());

            editor.apply();

            Snackbar.make(view,"Saved",Snackbar.LENGTH_SHORT).show();

        }

        public void showInfo(View view)
        {
            SharedPreferences sharedPreferences = getSharedPreferences("saveInfo",MODE_PRIVATE);
            String text= sharedPreferences.getString("Text","");

            textToShow.setText(text);


        }



    ////////For notification

    public void btnNotification(View view)
    {
        NotificationCompat.Builder notification = (NotificationCompat.Builder) new NotificationCompat.Builder(MainActivity.this)
                .setContentTitle("Notification").setContentText("Notification Clicked").setSmallIcon(R.mipmap.ic_launcher);

        Notification mynotification = notification.build();

        //Notification Manager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,mynotification);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            batteryText.setText(String.valueOf(level) + "%");

        }
    };

    @Override
    protected void onResume()
    {
        this.registerReceiver(receiver,filterLow);
        this.registerReceiver(receiver,filterOkay);


        super.onResume();
    }

    @Override
    protected void onPause(){
        this.unregisterReceiver(receiver);

        super.onPause();
    }


}
