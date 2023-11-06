package org.codebase.hm10conn;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import java.util.UUID;

public class BluetoothHandler {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private Context context;
    private MainActivity mainActivity;

    private static final UUID HM10_SERVICE_UUID = UUID.fromString("your_service_uuid");
    private static final UUID HM10_CHARACTERISTIC_UUID = UUID.fromString("your_characteristic_uuid");

    public BluetoothHandler(MainActivity activity) {
        context = activity;
        mainActivity = activity;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void connectToHM10() {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice("HM10_DEVICE_ADDRESS"); // Replace with your HM-10's MAC address
        bluetoothGatt = device.connectGatt(context, false, gattCallback);
    }

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // Device connected, discover services.
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // Device disconnected, handle as needed.
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Discover the services and characteristics.
                BluetoothGattService service = gatt.getService(HM10_SERVICE_UUID);
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(HM10_CHARACTERISTIC_UUID);

                // Enable notifications for the characteristic if required.
                gatt.setCharacteristicNotification(characteristic, true);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            // Process the data and update the graph.
            if (data.length >= 2) {
                float xValue = (float) data[0];
                float yValue = (float) data[1];
                mainActivity.updateGraph(xValue, yValue);
            }
        }
    };
}

