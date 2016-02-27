package hha.main;

import hha.chart.DateNotMatchValueException;
import hha.chart.MyChart;
import hha.chart.NotSetDateException;
import hha.chart.NotSetValueException;
import hha.chart.SameDateException;
import hha.robot.R;
import hha.util.ExitApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MenuList extends ListActivity {
	// public static final ColorStateList white = null;
	String[] map = new String[7];
	ToggleButton loginButton;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loginButton.setChecked(AppData.isLogined);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenulist);
		map[0] = "健康数据统计";
		map[1] = "备忘事件";
		map[2] = "查看健康建议";
		map[3] = "软件设置";
		map[4] = "关于我们";
		map[5] = "退出";
		map[6] = "测试";

		Button returnbutton = (Button) this.findViewById(R.id.returnbutton);
		loginButton = (ToggleButton) findViewById(R.id.login_btn);

		returnbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MenuList.this, MainActivity.class);
				MenuList.this.startActivity(intent);
			}
		});
		if (!AppData.NetworkMode)
			loginButton.setVisibility(View.GONE);
		else
			loginButton.setVisibility(View.VISIBLE);
		loginButton.setChecked(AppData.isLogined);
		loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!AppData.isLogined) {
					Intent intent = new Intent();
					intent.setClass(MenuList.this, Login.class);
					MenuList.this.startActivity(intent);
				} else {
					AppData.isLogined = false;
				}
			}
		});
		// loginButton.setOnCheckedChangeListener(new OnCheckedChangeListener()
		// {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		this.getListView().setCacheColorHint(Color.TRANSPARENT);
		this.getListView().setAdapter(new MyAdapter());
		// MenuList.this.getListView().setCacheColorHint(Color.TRANSPARENT);
		this.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0: {
					MyChart mChart = new MyChart(MenuList.this);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss");
					try {

						Date[] dates = new Date[4];
						try {
							dates[0] = sdf.parse("2014-6-17 00:00:00");
							dates[1] = sdf.parse("2014-6-18 00:00:00");
							dates[2] = sdf.parse("2014-6-19 00:00:00");
							dates[3] = sdf.parse("2014-6-20 00:00:00");
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mChart.setDate(dates);
					} catch (SameDateException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					mChart.setValue(new double[] { 10, 15, 20, 5 });
					mChart.setTitle("睡眠时间统计图");

					try {
						mChart.startActivity();
					} catch (NotSetDateException e) {
						e.printStackTrace();
					} catch (NotSetValueException e) {
						e.printStackTrace();
					} catch (DateNotMatchValueException e) {
						e.printStackTrace();
					}
					break;
				}

				case 1: {
					Intent intent = new Intent();
					intent.setClass(MenuList.this, Memo.class);
					MenuList.this.startActivity(intent);
					break;
				}
				case 2:
					break;
				case 3: {
					Intent intent = new Intent();
					intent.setClass(MenuList.this, setList.class);
					MenuList.this.startActivity(intent);
					break;
				}
				case 4: {
					Intent intent = new Intent();
					intent.setClass(MenuList.this, About.class);
					MenuList.this.startActivity(intent);
					break;
				}
				case 5:
					if (MainActivity.main.getBot() != null
							&& MainActivity.main.getBot().getDb() != null
							&& MainActivity.main.getBot().getDb().isInitDone()) {
						AppData.SaveData();
						MainActivity.main.getBot().getDb().SaveData();
						MainActivity.main.getBot().getDb().Close();
					}
					ExitApplication.getInstance().exit();
					System.exit(0);
					break;
				case 6:
					Test();
					break;
				default:
					return;
				}
			}

		});
	}

	public void Test() {
		MainActivity.main.getBot().setProperty("mode", "test");
		Intent intent = new Intent();
		intent.setClass(MenuList.this, MainActivity.class);
		MenuList.this.startActivity(intent);
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (AppData.DebugMode)
				return map.length;
			else
				return map.length - 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView text = new TextView(MenuList.this);
			text.setBackgroundResource(R.drawable.jiantou);
			text.setText(map[position]);
			text.setTextSize(37);
			text.setTextColor(Color.LTGRAY);
			text.setTag(position);
			return text;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
}
