package hha.main;

import hha.robot.R;
import hha.robot.R.layout;
import hha.robot.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		// Show the Up button in the action bar.
		Button sendButton = (Button)findViewById(R.id.send_btn);
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(About.this);  
		        builder.setTitle("感谢您提出的宝贵意见！");  
		        builder.setPositiveButton("好的", new EmptyListener());  
		        AlertDialog ad = builder.create();  
		        ad.show();  
			}
		});
	}
	
	 //空的监听类  
    private class EmptyListener implements DialogInterface.OnClickListener{  
   
        @Override  
        public void onClick(DialogInterface dialog, int which) {  
        	About.this.finish();
        }  
    }  

}
