package com.example.bluetooth_finder;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    TextView statustextView;
    Button searchButton;
    ArrayList<String> bluetoothdevices = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    BluetoothAdapter bluetoothAdapter;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("info",action);
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                statustextView.setText("Finished");
                searchButton.setEnabled(true);
            }
            else if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE));
                String deviceString="";
                if(name==null||name.equals("")){
                    deviceString=("Address:"+address+" rssi:"+rssi);
                }
                else{
                    deviceString=("Name:"+name+" rssi"+rssi);
                }
                if(!bluetoothdevices.contains(deviceString)){
                    bluetoothdevices.add(deviceString);
                }
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };


    public void buttonClicked(View view){
        statustextView.setText("Searching..");
        searchButton.setEnabled(false);
        bluetoothdevices.clear();
        bluetoothAdapter.startDiscovery();

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = findViewById(R.id.listView);
        statustextView = findViewById(R.id.statusTextview);
        searchButton = findViewById(R.id.searchButton);

        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();

        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,bluetoothdevices);
        listview.setAdapter(arrayAdapter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);

    }
}
