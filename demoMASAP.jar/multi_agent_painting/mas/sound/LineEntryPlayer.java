package multi_agent_painting.mas.sound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import tools.appControl.Logger;
import tools.appControl.StoppableRunnable;

/**
 * This class is the microphone sound player.
 * 
 * It will play the music on the sound card, and
 * also call a decoder to perform the fft.
 * 
 * The result will be stored inside an external HashMap, 
 * which is a parameter.
 * 
 * @author Sylvain Le Leuch, based on Ludovic Lefoulon draft version
 *
 */
public class LineEntryPlayer extends Thread implements StoppableRunnable{

	private volatile SoundThreadState threadState = SoundThreadState.PLAY;
	private boolean shouldStop = false, ready;
	private AudioFormat audioFormat = null;
	private TargetDataLine targetDataLine = null;
	private SourceDataLine sourceDataLine = null;
	private ByteArrayOutputStream byteArrayOutputStream = null;
	private ByteArrayInputStream byteArrayInputStream = null;
	private SoundDecoder decoder;
	private HashMap<Double, Double> map;
	
	public LineEntryPlayer(HashMap<Double, Double> map){
		super();
		this.map = map;
		this.ready = true;
	}

	public void run(){
		initSound();
		try {
			this.targetDataLine.open(audioFormat);
		} catch (LineUnavailableException e) {
			Logger.critical("run 3"+e.getMessage());
			e.printStackTrace();
		}

		// Start microphone lecture
		targetDataLine.start();
	
		byte[] soundData = new byte[4096];
		int nbReadByte;
		byteArrayOutputStream = new ByteArrayOutputStream();
		while(!shouldStop && this.threadState == SoundThreadState.PLAY){
			
			nbReadByte = targetDataLine.read(soundData,0,soundData.length);
			// FFT call and storage
			this.map.putAll(decoder.dataToFFT(soundData));
			// Writing on sound card
			byteArrayOutputStream.write(soundData,0,nbReadByte);
			
		}
		try {
			byteArrayOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		targetDataLine.close();
		targetDataLine.drain();
		try {
			listenToSample();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void over(){
		shouldStop = true;
	}

	// initialization part
	public void initSound(){
		// Initiate the sound format
		this.audioFormat = new AudioFormat(44100,16,2, true,true);
		//Initiate the decoder for fft
		this.decoder = new SoundDecoder(44100);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class,audioFormat);
		 if (!AudioSystem.isLineSupported(info)) {
	            System.err.println("Le son n'est pas supporté");
	            return;
	        }

		 try {
			targetDataLine = (TargetDataLine)AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	// Normally useless for our project, due to the fact that sound
	// listened is inside the room
	public void listenToSample() throws IOException{
		try {
			sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		byte[] toPlay = byteArrayOutputStream.toByteArray();
		byteArrayInputStream = new ByteArrayInputStream(toPlay);
		AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream,audioFormat,toPlay.length/audioFormat.getFrameSize());
		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,audioFormat);
		try {
			sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		sourceDataLine.start();
		byte[] toSpeaker = new byte[16384];
		int nbReadByte;

		while((nbReadByte = audioInputStream.read(toSpeaker, 0,toSpeaker.length)) != -1){
			if(nbReadByte > 0){
				sourceDataLine.write(toSpeaker,0,nbReadByte);
			}
		}
		sourceDataLine.close();
		sourceDataLine.drain();
	}

	@Override
	public void shutdown() {
		if(!this.shouldStop){
			this.shouldStop = true;
			this.ready = false;
		}
		
	}
	
	public void start() {
		if (this.ready) {
			new Thread(this).start();
		}
	}
	
	public void setState(SoundThreadState state){
		this.threadState = state;
	}
	
}

