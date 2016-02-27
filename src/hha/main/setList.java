package hha.main;

import hha.robot.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class setList extends Activity {
	private Button returnButton;
	private SeekBar seekBar = null;
	private TextView description = null;
	private RadioButton hanyuRadio;
	private RadioButton yueyuRadio;
	private CheckBox debugmode;
	private CheckBox networkmode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set);

		returnButton = (Button) findViewById(R.id.returnbutton);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		description = (TextView) findViewById(R.id.percenttextView);
		hanyuRadio = (RadioButton) findViewById(R.id.hanyuRadioButton);
		yueyuRadio = (RadioButton) findViewById(R.id.yueyuRadioButton);
		debugmode = (CheckBox) findViewById(R.id.CheckBoxDebug);
		networkmode = (CheckBox) findViewById(R.id.CheckBoxNetwork);

		hanyuRadio.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppData.RecognizableLanguage = 0;
			}
		});
		yueyuRadio.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppData.RecognizableLanguage = 1;
			}
		});
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setList.this.finish();
			}
		});

		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			/**
			 * 拖动条停止拖动的时候调用
			 */
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			/**
			 * 拖动条开始拖动的时候调用
			 */
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			/**
			 * 拖动条进度改变的时候调用
			 */
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				description.setText("当前音量：" + progress + "%");
				AppData.SystemAudio = progress;
			}
		});

		debugmode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				AppData.DebugMode = arg1;
			}
		});
		networkmode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				AppData.NetworkMode = arg1;
			}
		});
		seekBar.setProgress(AppData.SystemAudio);
		description.setText("当前音量：" + AppData.SystemAudio + "%");
		if (AppData.RecognizableLanguage == 0)
			hanyuRadio.setChecked(true);
		else
			yueyuRadio.setChecked(true);
		debugmode.setChecked(AppData.DebugMode);
		networkmode.setChecked(AppData.NetworkMode);		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		seekBar.setProgress(AppData.SystemAudio);
		description.setText("当前音量：" + AppData.SystemAudio + "%");
		if (AppData.RecognizableLanguage == 0)
			hanyuRadio.setChecked(true);
		else
			yueyuRadio.setChecked(true);
		debugmode.setChecked(AppData.DebugMode);
		networkmode.setChecked(AppData.NetworkMode);	
	}
}