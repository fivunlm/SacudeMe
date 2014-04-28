package ar.edu.unlam.soa.sacudeme;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager mSensorManager = null;
	private Sensor mAccelerometer = null;
	private TextView mTxtX = null;
	private TextView mTxtY = null;
	private TextView mTxtZ = null;
	private float mLastX, mLastY, mLastZ;
	private boolean mInitialized = false;
	private float NOISE = 2;
	Button b1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);

		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		
		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mTxtX.setText("0.0");
			mTxtY.setText("0.0");
			mTxtZ.setText("0.0");
			mInitialized = true;
		} else {
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE)
				deltaX = (float) 0.0;
			else
				startSecondActivity(1);
			if (deltaY < NOISE)
				deltaY = (float) 0.0;
			if (deltaZ < NOISE)
				deltaZ = (float) 0.0;
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mTxtX.setText(Float.toString(deltaX));
			mTxtY.setText(Float.toString(deltaY));
			mTxtZ.setText(Float.toString(deltaZ));
			
		}
	}
	
	private void startSecondActivity(int buttonNum) {
		Intent intent = new Intent(this, SecondActivity.class);
		intent.putExtra("BUTTON NUMBER", buttonNum);
		startActivity(intent);
	}
}

