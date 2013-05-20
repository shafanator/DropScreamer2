package com.shafanator.dropscreamer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Drop extends Service implements SensorEventListener{
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private MediaPlayer mp;
	private Camera camera;
	private int cameraId = 0;
	private long time_since_last_cycle;
	private long end_of_cycle;
	private boolean just_run;
	public Drop() {
	}
	public void onCreate() {
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener((SensorEventListener) this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), mSensorManager.SENSOR_DELAY_GAME);
        mp = MediaPlayer.create(this, R.raw.scream);
        camera = null;
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
        	camera = Camera.open(); 
        }
        end_of_cycle=0;
        
	}
	public void onDestroy(){
		
		//stopSelf();
		Log.e("des", "des");
		System.exit(0);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
		
		
	}
	 @Override
	 public int onStartCommand(Intent intent, int flags, int startId) {
		 Log.i("LocalService", "Received start id " + startId + ": " + intent);
	        // We want this service to continue running until it is explicitly
	        // stopped, so return sticky.
		 PowerManager mgr = (PowerManager)this.getSystemService(Context.POWER_SERVICE); 
	     WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock"); 
	     wakeLock.acquire();
	     return START_STICKY;
	 }
	 
	 
	 public void onSensorChanged(SensorEvent event) {
		 //Log.e("x", String.valueOf(event.values[0]));
	     //Log.e("y", String.valueOf(event.values[1]));
	     //Log.e("z", String.valueOf(event.values[2]));
		 time_since_last_cycle = end_of_cycle - System.currentTimeMillis();
	     if(Math.abs(event.values[0]) < 1.0 && Math.abs(event.values[1]) < 1.0 && Math.abs(event.values[2]) < 1.0)
	     {
	    	
	 		mp.start();
	 		Log.e("x", String.valueOf(event.values[0]));
		    Log.e("y", String.valueOf(event.values[1]));
		    Log.e("z", String.valueOf(event.values[2]));
		    if(camera!=null){
		    	Parameters p = camera.getParameters();
		    	p.setFlashMode(Parameters.FLASH_MODE_TORCH);
		    	camera.setParameters(p);
		    	camera.startPreview();
		    	for(int x = 0; x<=2; x++){
		    		p.setFlashMode(Parameters.FLASH_MODE_TORCH);
			    	camera.setParameters(p);
		    		p.setFlashMode(Parameters.FLASH_MODE_OFF);
		    		camera.setParameters(p);
		    		
		    	}
		    	camera.stopPreview();
		    	//camera.release();
		    	end_of_cycle = System.currentTimeMillis();
		    	just_run=true;
		    }
		   
	 		
	     }
	     if(Math.abs(event.values[2]) > 7.0 && just_run)
	     {
	    	 Log.e("surv","surv");
	    	 just_run=false;
	    	 ///do something after fal
	    	 /*try {
				InputStream input = new URL("http://api.remix.bestbuy.com/v1/products(mobileOperatingSystem=android&salePrice%3E200)?pageSize=100&show=name&apiKey=dh68f47cme85ppe32rnrafb5&format=json").openStream();
				Map<String, String> map = new gson().fromJson(new InputStreamReader(input, "UTF-8"), new TypeToken<Map<String,String>>(){}.getType());
				Random random = new Random();
				List<String> keys = new ArrayList<String>(map.keySet());
				String randomKey = keys.get(random.nextInt(keys.size()) );
				String value = map.get(randomKey);
				Log.e("phone", value);
	    	 } catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	     }
	 }
	 
		
	

	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
