package ar.edu.unlam.soa.sacudeme;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends Activity {

	private MediaPlayer mPlayer;
	private int currentSong = 0;
	Button b5;
	int seg = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_second);
		b5 = (Button) findViewById(R.id.button5);
		
		Intent intent = getIntent();
		int number = intent.getIntExtra("BUTTON NUMBER", 1);
		
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText(String.valueOf(number));
		
		if (number == 1) {
			mPlayer = MediaPlayer.create(SecondActivity.this, R.raw.sonido2);
			currentSong = R.raw.sonido2;
			mPlayer.start();
		} else if (number == 2) {
			mPlayer = MediaPlayer.create(SecondActivity.this, R.raw.sonido3);
			currentSong = R.raw.sonido3;
			mPlayer.start();
		} else if (number == 3) {
			mPlayer = MediaPlayer.create(SecondActivity.this, R.raw.sonido4);
			currentSong = R.raw.sonido4;
			mPlayer.start();
		}
		
		b5.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				seg = mPlayer.getCurrentPosition();
				mPlayer.pause();
			}
		});
	}
	
	protected void onPause() {
		super.onPause();
		if (mPlayer.isPlaying()) {
			mPlayer.pause();
		}
	}
	
	protected void onResume() {
		super.onResume();
		if(mPlayer == null) {
			mPlayer = MediaPlayer.create(SecondActivity.this, currentSong);
		}
		
		mPlayer.start();
	}
	
	protected void onStop() {
		super.onStop();
		
		if (mPlayer.isPlaying()) {
			mPlayer.stop();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_second,
					container, false);
			return rootView;
		}
	}

}
