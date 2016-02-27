package hha.xf;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.iflytek.cloud.*;
import hha.main.MainActivity;

public class NetRobot {

	Context mainContext;
	MainActivity mainActivity;
	SpeechUnderstander su = null;
	public NetRobot(MainActivity main) {
		// TODO Auto-generated constructor stub
		mainActivity = main;
		mainContext = main.getApplicationContext();
	}

	public int BeginSpeechUnderstand()
	{
		setParam();
		return su.startUnderstanding(sul);
	}

	public int EndSpeechUnderstand() {
		su.stopUnderstanding();
		return 0;
	}

	private InitListener il = new InitListener() {
		@Override
		public void onInit(int code) {
			// TODO Auto-generated method stub
			if (code == ErrorCode.SUCCESS) {
				mainActivity.button.setEnabled(true);
				mainActivity.Welcome();
			} else {
				mainActivity.showTip("Error:" + code);
			}
		}
	};



	private SpeechUnderstanderListener sul = new SpeechUnderstanderListener() {

		@Override
		public void onVolumeChanged(int v, byte[] data)   {
			mainActivity.showTip("onVolumeChanged:" + v);
		}

		@Override
		public void onError(SpeechError speechError) {
			mainActivity.showTip("onError:" + speechError.getErrorDescription());
		}

		@Override
		public void onEndOfSpeech()   {
			mainActivity.showTip("onEndOfSpeech");
		}

		@Override
		public void onBeginOfSpeech() {
			mainActivity.showTip("onBeginOfSpeech");
		}

		@Override
		public void onResult(final UnderstanderResult result) {
//			mainActivity.runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
					if (null != result) {
						SaxParseService saxParseService = new SaxParseService();
						Data data = null;
						try {
							String string = result.getResultString();
							mainActivity.ShowTextOnUIThread(string);
							data = saxParseService.getData(string);
							mainActivity.GetReturnData(data);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}



		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3){
			// TODO Auto-generated method stub

		}
	};

	public void InitXF() {
		SpeechUtility.createUtility(mainContext, SpeechConstant.APPID +"=53227870");
		if (SpeechUtility.getUtility().queryAvailableEngines() != null
				&& SpeechUtility.getUtility().queryAvailableEngines().length > 0) {
			mainActivity.showTip("讯飞语音可以使用");
		}
		else {
			mainActivity.showTip("讯飞初始化失败");
		}
		su = SpeechUnderstander.createUnderstander(mainContext, il);
	}

	private void setParam() {
		String lang =  "mandarin";
		if (lang.equals("en_us")) {
			// 设置语言
			su.setParameter(SpeechConstant.LANGUAGE, "en_us");
		}else {
			// 设置语言
			su.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			su.setParameter(SpeechConstant.ACCENT, lang);
		}
		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		su.setParameter(SpeechConstant.VAD_BOS, "4000");

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		su.setParameter(SpeechConstant.VAD_EOS, "1000");

		// 设置标点符号，默认：1（有标点）
		su.setParameter(SpeechConstant.ASR_PTT, "1");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		// su.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		// su.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/sud.wav");
	}

}
