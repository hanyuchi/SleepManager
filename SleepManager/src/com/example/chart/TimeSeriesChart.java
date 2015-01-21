package com.example.chart;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.afree.chart.ChartFactory;
import org.afree.chart.AFreeChart;
import org.afree.chart.axis.DateAxis;

import com.example.chart.ChartView;
import com.example.dblayout.DatabaseHandler;
import com.example.dblayout.TrackDataData;

import org.afree.chart.plot.XYPlot;
import org.afree.chart.renderer.xy.XYItemRenderer;
import org.afree.chart.renderer.xy.XYSplineRenderer;
import org.afree.data.time.Second;
import org.afree.data.time.TimeSeries;
import org.afree.data.time.TimeSeriesCollection;
import org.afree.data.xy.XYDataset;
import org.afree.graphics.SolidColor;
import org.afree.ui.RectangleInsets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

@SuppressLint("ViewConstructor")
public class TimeSeriesChart extends ChartView {
	private static int year = 0;
	private static int month = 0;
	private static int day = 0;
	private static int startId = 0;
	private static int endId = 0;
	private static DatabaseHandler db;
	private static Context context = null;

    @SuppressWarnings("static-access")
	public TimeSeriesChart(int year, int month, int day, int startId, int endId, Context context) {	
        super(context);
        this.year = year;
        this.month = month + 1;
        this.day = day;
        this.startId = startId;
        this.endId = endId;
        this.context = context;
        final AFreeChart chart = createChart(createDataset());
        setChart(chart);
    }

	private static AFreeChart createChart(XYDataset dataset) {
        AFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Your Sleep Quality",  // title
            "Date & Time",             // x-axis label
            "Sleep Data",   // y-axis label
            dataset,            // data
            true,               // create legend?
            true,               // generate tooltips?
            false               // generate URLs?
        );

        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaintType(new SolidColor(Color.LTGRAY));
        plot.setDomainGridlinePaintType(new SolidColor(Color.WHITE));
        plot.setRangeGridlinePaintType(new SolidColor(Color.WHITE));
        plot.setAxisOffset(new RectangleInsets(5.0, 1.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYSplineRenderer) {
        	XYSplineRenderer renderer = (XYSplineRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
        return chart;
    }

    private static XYDataset createDataset() {
    	TimeSeriesCollection dataset = new TimeSeriesCollection();
    	TimeSeries s1 = new TimeSeries("You Sleep Quality");
    	db = new DatabaseHandler(context);
    	Log.d("START", String.valueOf(endId));
    	Log.d("END", String.valueOf(startId));
    	for(int i = endId; i <= startId; i++) {
    		List<TrackDataData> tl = db.getAllTrackData(i);
        	//Log.d("COUNT", String.valueOf(tl.size()));
        	Iterator<TrackDataData> itr = tl.iterator();
        	//set graph using data from database
        	int lastHour = -1;
        	int lastMinute = -1;
        	int lastSecond = -1;
        	float sum;
        	if(tl.size() == 0) {
        		continue;
        	} else {
        		sum = tl.get(0).getAcceleration();
        	}
        	int count = 1;
        	int flag = 1;
        	while(itr.hasNext()) {
        		TrackDataData td = itr.next();    		
        		if(td.getHour() == lastHour
        		&& td.getMinute() == lastMinute
        		&& td.getSecond() == lastSecond) {
        			sum += td.getAcceleration();
        			count++;
        		} else if(lastSecond != -1) { 
        			Log.d("Data", String.valueOf(flag++));
        			s1.add(new Second(lastSecond, lastMinute, lastHour, day, month, year), sum/count);
        			Log.d("LastSecond", String.valueOf(lastSecond));
        			Log.d("SUM", String.valueOf(sum));
        			Log.d("CNT", String.valueOf(count));
        			Log.d("AVE", String.valueOf(sum/count));
        			//Log.d("LastMinute", String.valueOf(lastMinute));
        			//Log.d("LastHour", String.valueOf(lastHour));
        			sum = td.getAcceleration();
        			count = 1;
        		}
        		lastHour = td.getHour();
    			lastMinute = td.getMinute();
    			lastSecond = td.getSecond();
        	}
        	Log.d("LData", String.valueOf(flag++));
        	s1.add(new Second(lastSecond, lastMinute, lastHour, day, month, year), sum/count);
        	Log.d("lastSecond", String.valueOf(lastSecond));
        	Log.d("SUM", String.valueOf(sum));
    		Log.d("CNT", String.valueOf(count));
        	Log.d("AVE", String.valueOf(sum/count));
    		//Log.d("lastMinute", String.valueOf(lastMinute));
    		//Log.d("lastHour", String.valueOf(lastHour));   
    	}
    	
        dataset.addSeries(s1);
        return dataset;
    }
}
