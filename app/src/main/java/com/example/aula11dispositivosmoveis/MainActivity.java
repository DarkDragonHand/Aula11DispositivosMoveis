package com.example.aula11dispositivosmoveis;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart = findViewById(R.id.lineChart);

        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.getDescription().setEnabled(false);

        lineChart.setTouchEnabled(true);

        lineChart.setScaleEnabled(true);
        lineChart.setDragEnabled(true);

        lineChart.setPinchZoom(true);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://ghelfer.net/la/weather.json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String dados = new String(response);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                ArrayList<Entry> values = new ArrayList<>();

                try {
                    JSONObject res = new JSONObject(dados);
                    JSONArray array = res.getJSONArray("weather");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject json = array.getJSONObject(i);

                        String temp = json.get("temperature").toString();

                        values.add(new Entry(i, Float.parseFloat(temp)));
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                // create a dataset and give it a type
                LineDataSet set1;
                set1 = new LineDataSet(values, "Temperaturas");

                set1.setDrawIcons(false);

                // draw dashed line
                set1.enableDashedLine(10f, 5f, 0f);

                // black lines and points
                set1.setColor(Color.BLACK);
                set1.setCircleColor(Color.BLACK);

                // line thickness and point size
                set1.setLineWidth(1f);
                set1.setCircleRadius(3f);

                // draw points as solid circles
                set1.setDrawCircleHole(false);

                // customize legend entry
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set1.setFormSize(15.f);

                // text size of values
                set1.setValueTextSize(9f);

                dataSets.add(set1);

                LineData lineData = new LineData(dataSets);
                lineChart.setData(lineData);
                lineChart.invalidate();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
