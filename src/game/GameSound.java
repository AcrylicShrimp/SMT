package game;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class GameSound {
	
	public class ClipPair {
		public Clip clip;
		public boolean loop;
	}
	
	private ArrayList<ClipPair> clip_list;
	
	public GameSound() {
		this.clip_list = new ArrayList<>();
	}
	
	public void update() {
		for(Iterator<ClipPair> clip_iterator = this.clip_list.iterator() ; clip_iterator.hasNext() ; ) {
			ClipPair clip = clip_iterator.next();
			
			if(!clip.clip.isRunning()) {
				
				if(clip.loop) {
					clip.clip.setMicrosecondPosition(0);
					clip.clip.start();
				} else {
					clip.clip.close();
					clip.clip = null;
					clip_iterator.remove();
				}
			}
		}
	}
	
	public Clip make_sound(String sound_path, float volume) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(sound_path)));
			((FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(volume);
			
			return clip;
		} catch (Exception e) {
		}
		
		return null;
	}
	
	public Clip play_sound(String sound_path, float volume, boolean loop) {
		ClipPair clip_pair = new ClipPair();
		
		try {
			clip_pair.clip = AudioSystem.getClip();
			clip_pair.clip.open(AudioSystem.getAudioInputStream(new File(sound_path)));
			((FloatControl)clip_pair.clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(volume);
			clip_pair.clip.start();
			
			clip_pair.loop = loop;
			
			this.clip_list.add(clip_pair);
			
			return clip_pair.clip;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void stop_sound_all() {
		for(ClipPair clip : this.clip_list) {
			clip.clip.stop();
			clip.clip.close();
			clip.clip = null;
		}
		
		this.clip_list.clear();
	}
}