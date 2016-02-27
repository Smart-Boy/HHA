package hha.util.music;

import hha.main.MainActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.os.Environment;

public class LocalSoundPlayer {

	private SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
	private Map<String, Integer> soundPoolMap = new HashMap<String, Integer>();
	MainActivity main;
	MediaPlayer mp = new MediaPlayer();
	boolean is_pre = true;

	Queue<SoundEffectTask> preSound = new LinkedList<SoundEffectTask>();
	Queue<SoundEffectTask> sufSound = new LinkedList<SoundEffectTask>();

	public LocalSoundPlayer(MainActivity _main) {
		super();
		this.main = _main;
		mp.setOnCompletionListener(listener);
	}

	public void Add(String name, Map<String, Integer> setting) {
		SoundEffectTask task = new SoundEffectTask(name, setting);
		if (setting.get("pre") == 1) {
			preSound.offer(task);
			System.out.println("add pre");
		}
		if (setting.get("suf") == 1) {
			sufSound.offer(task);
			System.out.println("add suf");
		}

	}

	private OnCompletionListener listener = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			System.out.println("play completion");
			play();
		}

	};

	int loop;
	String ansString;
	private String item;

	private void play() {
		SoundEffectTask task = null;
		if (is_pre) {
			task = preSound.peek();
		} else {
			task = sufSound.peek();
		}
		if (task == null) {
			if (is_pre) {
				System.out.println("don't have a task");
				MainActivity.main.getReader().start(ansString);
			}
			return;
		}
		if (task.setting.get("back") == 0) {
			MediaPlay(task.name, task.setting);
			if (loop == -1) {
				if (is_pre) {
					preSound.poll();
				} else {
					sufSound.poll();
				}
			}
		} else {
			SoundPoolPlay(task.name, task.setting);
			if (is_pre) {
				preSound.poll();
			} else {
				sufSound.poll();
			}
			play();
		}

	}

	public void play(boolean is_pre, String ansString) {
		this.ansString = ansString;
		this.is_pre = is_pre;
		play();
	}

	public void MediaPlay(String name, Map<String, Integer> setting) {
		// MediaPlayerLoad(name);
		loop = setting.get("loop");
		System.out.println("MediaPlay");
		if (loop == -1)
			return;
		try {
			mp.reset();
			mp.setOnCompletionListener(listener);
			mp.setDataSource(main.getFileStreamPath("audio") + "/" + name
					+ ".ogg");
			System.out.println(main.getFileStreamPath("audio") + "/" + name
					+ ".ogg");
			// mp.setDataSource(am.openFd("audio/" + name + ".ogg")
			// .getFileDescriptor());
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.setLooping(false);
			mp.prepare();
			mp.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loop--;
		setting.put("loop", loop);
	}

	public void playUrl(String url) {

		try {
			mp.reset();
			mp.setDataSource(url);
			mp.prepare();
			mp.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void SoundPoolLoad() {
		String[] names = null;
		File f = main.getFileStreamPath("audio");
		names = f.list();
		if (names==null) return;
		for (String item : names) {
			if (item.endsWith(".ogg")) {
				System.out.println(f.getPath() + "/" + item);
				String name = item.split("\\.")[0];
				soundPoolMap.put(name,
						soundPool.load(f.getPath() + "/" + item, 1));
			}
		}
	}

	public void SoundPoolPlay(String name, Map<String, Integer> setting) {
		System.out.println("SoundPoolPlay");
		soundPool.play(soundPoolMap.get(name), 1, 1, 0, setting.get("loop"),
				(float) (setting.get("rate") * 0.01));
	}

	public void SoundPoolStop() {
		soundPool.autoPause();
	}

	public void Resume() {
		soundPool.autoResume();
	}

	public void stop() {
		// TODO Auto-generated method stub
		mp.stop();
	}
}
