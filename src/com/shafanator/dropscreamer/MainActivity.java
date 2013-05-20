package com.shafanator.dropscreamer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

  
public class MainActivity extends Activity implements SensorEventListener, OnItemSelectedListener{
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private MediaPlayer mp;
    private Intent i;
    private AdView adView;
    public static int scream;
    private int start;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), mSensorManager.SENSOR_DELAY_GAME);
        
        //make and use dropdown
        makeDropdown();
        
        scream = R.raw.ascream;
        start= scream;
        String beep = R.raw.beep + "";
        String glass = R.raw.glass + "";
        
        
        mp = MediaPlayer.create(this, scream);
        i = new Intent(this, Drop.class);
        
        PowerManager mgr = (PowerManager)this.getSystemService(Context.POWER_SERVICE); 
        WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock"); 
        wakeLock.acquire();
        startService(i);
        Log.e("beep", beep);
        Log.e("Glass", glass);
        Log.e("Scream", scream+"");
        
        
		
	}
	private void makeDropdown(){
		Spinner spinner = (Spinner) findViewById(R.id.scream_picker);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.scream_picker, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		Log.e("id", id +"");
		scream = (int) (start + id) ;
		mp = MediaPlayer.create(this, (int) (scream));
		startService(i);
		
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

	 protected void onResume() {
	    super.onResume();
	    mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), mSensorManager.SENSOR_DELAY_GAME);
	    
	 }
	public void start(View view){
		Log.e("test", "press");
		mp.start();
		startService(i);
		
	
		
		
	}
	public void end(View view)
	{
		
		stopService(i);
		Log.e("stop","stop");
		
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		 //Log.e("x", String.valueOf(event.values[0]));
	     //Log.e("y", String.valueOf(event.values[1]));
	     //Log.e("z", String.valueOf(event.values[2]));
	     
	     if(Math.abs(event.values[0]) < 1.0 && Math.abs(event.values[1]) < 1.0 && Math.abs(event.values[2]) < 1.0)
	     {
	    	
	 		mp.start();
	 		Log.e("x", String.valueOf(event.values[0]));
		    Log.e("y", String.valueOf(event.values[1]));
		    Log.e("z", String.valueOf(event.values[2]));
		   
		   
	 		
	     }
		
	}
	
	

	
}
