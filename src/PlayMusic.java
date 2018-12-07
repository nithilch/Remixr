// Nithil Chigullapally 300288453
// Joel Calo 300279569

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlayMusic {

	static File soundFile = new File(".\\outputs\\songToPlay.wav");
	static AudioInputStream audioIn;
	static Clip clip;
	static long clipTime;
	
	//Opens an input stream for the sound file to be played and plays it.
	public static void play() {
		try {
			audioIn = AudioSystem.getAudioInputStream(soundFile);
	    	clip = AudioSystem.getClip();
	    	clip.open(audioIn);
	    	clip.setMicrosecondPosition(clipTime);
	    	clip.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Pauses the current song and takes note of the time the song was paused.
	public static void pause() {
		try {
			clipTime = clip.getMicrosecondPosition();
			System.out.println("Paused at " + clipTime);
			clip.stop();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Resets the play time and stops the song.
	public static void stop() {
		try {
			setClipTimetoZero();
			clip.stop();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Sets the play time to 0;
	public static void setClipTimetoZero() {
		clipTime = 0;
	}

	
	
}
