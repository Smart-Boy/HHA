package hha.main;

import hha.robot.R;
import hha.robot.R.layout;
import hha.robot.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Suggest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggest);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.suggest, menu);
		return true;
	}

}
