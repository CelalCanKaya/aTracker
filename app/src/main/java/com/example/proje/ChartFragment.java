package com.example.proje;


import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    private BarChart bar;
    RequestQueue queue;
    String url;
    Integer values[] = new Integer[7];

    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chart, container, false);
        bar = view.findViewById(R.id.barChart);
        //getDatas();
        //ALTTAKİ SATIR SİLİNECEK
        try {
            getChart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void getDatas(){
        JSONObject jsonobj = new JSONObject();
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        url = "http://40.117.95.148:9080/predict";
        try {
            jsonobj.put("x_axis", "1");
            jsonobj.put("y_axis", "2");
            jsonobj.put("z_axis", "3");
            System.out.println(jsonobj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            for(int i=0; i<7; i++){
                                int x = i+1;
                                values[i]=Integer.parseInt(response.getString("Day_" + x));
                            }
                            //getChart();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } /*catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR");
                    }
                }
        );
        queue.add(objectRequest);
    }

    private void getChart() throws InterruptedException {
        List<BarEntry> barEntries = new ArrayList<>();
        for(int i=0; i<7; i++){
            barEntries.add(new BarEntry(i,i));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Steps");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        bar.setVisibility(View.VISIBLE);
        bar.animateY(2000);
        bar.setData(barData);
        bar.setFitBars(true);
        bar.getAxisLeft().setDrawGridLines(false);
        bar.getXAxis().setDrawGridLines(false);
        XAxis xAxis = bar.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Mon","Tue","Wed","Thu","Fri", "Sat", "Sun"}));
        Description description = new Description();
        description.setText("Steps Per Day");
        bar.setDragEnabled(false);
        bar.setScaleEnabled(false);
        bar.setDescription(description);
        bar.invalidate();
    }
}
