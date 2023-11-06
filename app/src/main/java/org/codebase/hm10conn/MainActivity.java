package org.codebase.hm10conn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private BluetoothHandler bluetoothHandler;
    private LineChart graph;
    private LineDataSet dataSet;
    private LineData lineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button connectButton = findViewById(R.id.connectButton);
        graph = findViewById(R.id.graph);

        // Check and request Location permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        }

        // Initialize the BluetoothHandler
//        bluetoothHandler = new BluetoothHandler(this);

        connectButton.setOnClickListener(v -> {
            // Replace with your HM-10 connection logic
            bluetoothHandler.connectToHM10();
        });

//        // Initialize the graph
//        dataSet = new LineDataSet(null, "Data");
//        lineData = new LineData(dataSet);
//        graph.setData(lineData);
//
//        // Customize the graph appearance
//        dataSet.setDrawCircles(false);
//        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        dataSet.setCubicIntensity(0.2f);
//        dataSet.setDrawValues(false);
//        graph.invalidate();


        LineChart lineChart = findViewById(R.id.graph);

        // Sample data for the chart
        LineDataSet dataSet = new LineDataSet(getData(), "Sample Data");
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // Customize the chart appearance (optional)
        dataSet.setColors(new int[]{R.color.black}, this);
        dataSet.setDrawCircles(true);
        dataSet.setDrawValues(true);
        lineChart.getDescription().setEnabled(false);

        // Refresh the chart
        lineChart.invalidate();

    }

    private ArrayList<Entry> getData() {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 50f));
        entries.add(new Entry(2, 80f));
        entries.add(new Entry(3, 60f));
        entries.add(new Entry(4, 75f));
        // Add more data points as needed

        return entries;
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, continue with Bluetooth
            } else {
                // Location permission denied, handle accordingly
            }
        }
    }

    // Update the graph with new data
    public void updateGraph(float xValue, float yValue) {
        // Add a new entry to the data set and refresh the chart
        lineData.addEntry(new Entry(xValue, yValue), 0);
        graph.notifyDataSetChanged();
        graph.invalidate();
    }

}