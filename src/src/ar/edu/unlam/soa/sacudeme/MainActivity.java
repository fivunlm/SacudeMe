package ar.edu.unlam.soa.sacudeme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener, OnSeekBarChangeListener {

	private SensorManager mSensorManager = null;
	private Sensor mAccelerometer = null;
	private TextView mTxtX = null;
	private TextView mTxtY = null;
	private TextView mTxtZ = null;
	private TextView mTxtMsg = null;
	private SeekBar  mSeekBarSernsibility = null;
	
	private float mSensibility = 2f;
	private SoundPool mSoundPool;
	private Map<Integer,Integer> mSoundPoolMap = new HashMap<Integer, Integer>();
	private int mStreamID;
	
	private static String TAG = "MainActivity";
	
	private class Tuple {
		private float mFirst;
		private float mSecond;
		private float mThird;
		
		public Tuple(float first, float second, float third) {
			super();
			this.mFirst = first;
			this.mSecond = second;
			this.mThird = third;
		}

		public float getFirst() {
			return mFirst;
		}

		public float getThird() {
			return mThird;
		}

		public float getSecond() {
			return this.mSecond;
		}
		
		
	}
	private List<Tuple> mMotion = new ArrayList<Tuple>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

		mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 100);
		mSoundPoolMap.put(1, mSoundPool.load(this, R.raw.sonido2, 1));
		mSoundPoolMap.put(2, mSoundPool.load(this, R.raw.sonido3, 1));
		mSoundPoolMap.put(3, mSoundPool.load(this, R.raw.sonido4, 1));
		
		mSeekBarSernsibility = (SeekBar) findViewById(R.id.seekBarSensibility);
		mSeekBarSernsibility.setOnSeekBarChangeListener(this);
		mTxtMsg = (TextView) findViewById(R.id.txtMsg);
		mTxtMsg.setText(String.format("%.2f ", mSensibility));
	}


	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
				
		mTxtX = (TextView) findViewById(R.id.txtX);
		mTxtY = (TextView) findViewById(R.id.txtY);
		mTxtZ = (TextView) findViewById(R.id.txtZ);
		
		float x = event.values[0] > mSensibility ? event.values[0] : 0;
		float y = event.values[1] > mSensibility ? event.values[1] : 0;
		float z = event.values[2] > mSensibility ? event.values[2] : 0;
		
		// The phone stopped, check kind of movement 
		if( x == 0 && y == 0 && z == 0){
			if(mMotion.size() > 0 ){
				proccessMotions();
				mMotion.clear();
			}
		}
		else {
			mMotion.add(new Tuple(x, y, z));
		}
		
		
		mTxtX.setText(Float.toString(x));
		mTxtY.setText(Float.toString(y));
		mTxtZ.setText(Float.toString(z));
	}

	private void proccessMotions() {
		
		AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float leftVolume = curVolume/maxVolume;
        float rightVolume = curVolume/maxVolume;
        int priority = 1;
        int no_loop = 0;
        float normal_playback_rate = 1f;
		
		Log.d(TAG, "---------------------------------------------------");
		int xC = 0;
		int yC = 0;
		int zC = 0;
		float x = 0;
		float y = 0;
		float z = 0;
		
		for(Tuple tuple : mMotion){
			Log.d(TAG, String.format("(%f,%f,%f)", tuple.getFirst(), tuple.getSecond(), tuple.getThird()));
			x = tuple.getFirst();
			y = tuple.getSecond();
			z = tuple.getThird();
			
			if( x > y && x > z)
				xC++;
			if( y > x && y > z )
				yC++;
			if( z > y && z > x )
				zC++;
		}
		
		if( xC > yC && xC > zC ){
			Log.d(TAG, "EN X");
			Toast.makeText(getApplicationContext(), "EN X", Toast.LENGTH_SHORT).show();
			mSoundPool.stop(mStreamID);
			mStreamID = mSoundPool.play(mSoundPoolMap.get(1), leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
		}
		if( yC > xC && yC > zC ){
			Log.d(TAG, "EN Y");
			Toast.makeText(getApplicationContext(), "EN Y", Toast.LENGTH_SHORT).show();
			mSoundPool.stop(mStreamID);
			mStreamID = mSoundPool.play(mSoundPoolMap.get(2), leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
		}
		if( zC > yC && zC > xC ){
			Log.d(TAG, "EN Z");
			Toast.makeText(getApplicationContext(), "EN Z", Toast.LENGTH_SHORT).show();
			mSoundPool.stop(mStreamID);
			mStreamID = mSoundPool.play(mSoundPoolMap.get(3), leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
		}
		Log.d(TAG, "---------------------------------------------------");
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		mSensibility =  (float) seekBar.getMax() - progress;
		mTxtMsg.setText(String.format("%.2f ", mSensibility));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

}

