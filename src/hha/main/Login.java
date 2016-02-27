package hha.main;

import hha.robot.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Login extends Activity {
	
//	private final static String FORGETPASSWORD = "forgetPassword?";
	
	Button loginButton=null;
	Button registButton;
	TextView forgetPasswoTextView;
	EditText username;
	EditText password;
	EditText password_again;
	boolean show_password_again = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		password_again = (EditText) findViewById(R.id.password_again);
		
		loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.main.getBot().getNet().Login(username.getText().toString(), password.getText().toString());
				Login.this.finish();
			}
		});
		
		registButton = (Button)findViewById(R.id.registerButton);
		registButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!show_password_again)
				{
					show_password_again = true;
					password_again.setVisibility(View.VISIBLE);
					return;
				}
				
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	
}

