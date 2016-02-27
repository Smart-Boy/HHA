package hha.xf;


//构造方法 Reader(android.content.Context context),此处传入activity的this
//开始读 bool start(String str);成功返回true，失败返回false
//取消读 cancel(); 暂停pause();  恢复(从暂停中)resume();

import android.content.SharedPreferences;
import com.iflytek.cloud.*;
import hha.main.AppData;
import hha.main.MainActivity;
import android.os.Bundle;

public class Reader {
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 默认发音人
    private String voicer = "xiaoyan";

    //主要对象
    private SpeechSynthesizer mTts = null;

    //用于构造mTts的对象
	private InitListener il = new InitListener(){

        @Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
                MainActivity.main.showTip("初始化失败,错误码："+code);
        	} else {
				// 初始化成功，之后可以调用startSpeaking方法
        		// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
        		// 正确的做法是将onCreate中的startSpeaking调用移至这里
        		setParam();
			}
		}
	};

	//mTts阅读时需要使用的对象，各种重写的方法可以添加
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {
            MainActivity.main.getPlayer().play(false,null);
        }

        @Override
        public void onSpeakBegin()   {

        }

        @Override
        public void onSpeakPaused() {

        }

		//percent为播放进度0~100,beginPos为播放音频在文本中开始位置,endPos表示播放音频在文本中结束位置.
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {

        }


        @Override
        public void onSpeakResumed(){

        }

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub

		}
    };

    //构造方法
    public Reader(android.content.Context context){
    	mTts = SpeechSynthesizer.createSynthesizer(context,il);

    }

    //开始合成
    public boolean start(String str){
    	int code = mTts.startSpeaking(str, mTtsListener);
    	if(code==0) return true;
    	else return false;
    }

    //取消合成
    public void cancel(){
    	mTts.stopSpeaking();
    }

    //暂停合成
    public void pause(){
    	mTts.pauseSpeaking();
    }

    //继续合成
    public void resume(){
    	mTts.resumeSpeaking();
    }

	private void setParam(){
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 根据合成引擎设置相应参数
		if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			// 设置在线合成发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
			//设置合成语速
			mTts.setParameter(SpeechConstant.SPEED, "50");
			//设置合成音调
			mTts.setParameter(SpeechConstant.PITCH, "50");
			//设置合成音量
			mTts.setParameter(SpeechConstant.VOLUME, "50");
		}else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
			// 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
			mTts.setParameter(SpeechConstant.VOICE_NAME, "");
			/**
			 * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
			 * 开发者如需自定义参数，请参考在线合成参数设置
			 */
		}
		//设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		// mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		// mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
	}
}
