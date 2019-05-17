package com.example.proje;


import android.graphics.Color;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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

    private static BarChart bar;
    private static PieChart pie;
    RequestQueue queue;
    String url;
    static Integer values[] = new Integer[7];
    static int chartNum = 0;


    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chart, container, false);
        bar = view.findViewById(R.id.barChart);
        pie = view.findViewById(R.id.pieChart);
        getDatas();
        return view;
    }

    private void getDatas(){
        JSONObject jsonobj = new JSONObject();
        try {
            jsonobj.put("request", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        url = "http://40.117.95.148:9080/checkLog";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response);
                            for(int i=0; i<7; i++){
                                int x = i+1;
                                values[i]=Integer.parseInt(response.getString("Day_" + x));
                            }
                            String[] texts = new String[5];
                            texts[0] = response.getString("Jumping");
                            texts[1] = response.getString("Walking");
                            texts[2] = response.getString("Up");
                            texts[3] = response.getString("Down");
                            texts[4] = response.getString("Running");
                            fillTextViews(texts);
                            getChart(chartNum);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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

    public static void getChart(int chartNum) throws InterruptedException {
        if(chartNum==0){
            List<BarEntry> barEntries = new ArrayList<>();
            for(int i=0; i<7; i++){
                barEntries.add(new BarEntry(i,values[i]));
            }
            BarDataSet barDataSet = new BarDataSet(barEntries, "Steps");
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            BarData barData = new BarData(barDataSet);
            barData.setBarWidth(0.9f);
            bar.setVisibility(View.VISIBLE);
            pie.setVisibility(View.INVISIBLE);
            bar.animateY(2000);
            bar.setData(barData);
            bar.setFitBars(true);
            bar.getAxisLeft().setDrawGridLines(false);
            bar.getXAxis().setDrawGridLines(false);
            XAxis xAxis = bar.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Day 7","Day 6","Day 5","Day 4","Day 3", "Day 2", "Day 1"}));
            Description description = new Description();
            description.setText("Steps Per Day");
            bar.setDragEnabled(false);
            bar.setScaleEnabled(false);
            bar.setDescription(description);
            bar.invalidate();
        }
        else if(chartNum==1){
            List<PieEntry> pieEntries = new ArrayList<>();
            for(int i=0; i<7; i++){
                pieEntries.add(new PieEntry(values[i],"Day"+i+1));
            }
            pie.setEntryLabelColor(Color.BLACK);
            PieDataSet pieDataSet = new PieDataSet(pieEntries, "Steps");
            pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            pieDataSet.setValueTextColor(Color.BLACK);
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData pieData = new PieData(pieDataSet);
            pieData.setValueTextColor(Color.BLACK);
            pie.setData(pieData);
            bar.setVisibility(View.INVISIBLE);
            pie.setVisibility(View.VISIBLE);
            pie.animateXY(2000,2000);
            Description description = new Description();
            description.setText("Steps Per Day");
            pie.setDescription(description);
            pie.invalidate();
        }

    }

    private void fillTextViews(String[] texts){
        ChartScreen.jumpText.setText(texts[0] + " Times Jump");
        ChartScreen.walkText.setText(texts[1] + " Times Walk");
        ChartScreen.stairsText.setText(texts[2] + " Times Stairs Up\n" + texts[3] + " Times Stairs Down");
        ChartScreen.runText.setText(texts[4] + " Times Run");
    }

}
