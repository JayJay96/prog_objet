package multi_agent_painting.mas.sound;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import tools.appControl.Logger;
import tools.appControl.StoppableRunnable;

/**
 * This class is the file sound player.
 * 
 * It will play the music on the sound card, and
 * also call a decoder to perform the fft.
 * 
 *  The result will be stored inside an external HashMap, 
 *  which is a parameter.
 * 
 * @author Sylvain Le Leuch, based on Ludovic Lefoulon draft version
 *
 */
public class FilePlayer implements StoppableRunnable{
	
	private volatile SoundThreadState threadState = SoundThreadState.PLAY;
	private boolean shutdown = false, ready;
	private boolean shouldStop;
	private File musicToRead;
	private AudioFileFormat audioFileFormat;
	private float sampleRate;
	// This is the buffer size for sound file lecture
	private byte[] data;
	private SoundDecoder decoder;
	private HashMap<Double, Double> map;
	 
	/**
	 * This builder takes buffer size into parameter
	 */
	@Deprecated
	public FilePlayer( File music){
		this.shouldStop = false;
		this.musicToRead = music;
		try{
			this.audioFileFormat=AudioSystem.getAudioFileFormat(music);
			this.sampleRate = audioFileFormat.getFormat().getSampleRate();
			this.decoder = new SoundDecoder(sampleRate);
		}
		catch(Exception e){
			Logger.critical(" FilePlayer 2 " +e.getMessage());
		}
	}
	
	/**
	 * This builder takes buffer size into parameter
	 * and also the HashMap into it will write
	 */
	public FilePlayer(File music, HashMap<Double, Double> map){
		this.shouldStop = false;
		this.musicToRead = music;
		this.map = map;
		this.ready = true;
		try{
			this.audioFileFormat=AudioSystem.getAudioFileFormat(music);
			this.sampleRate = audioFileFormat.getFormat().getSampleRate();
			this.decoder = new SoundDecoder(sampleRate);
		}
		catch(Exception e){
			Logger.critical(" FilePlayer " +e.getMessage());
		}
	}
	
	public void nextstep(SourceDataLine line, AudioInputStream toPlay, AudioFormat decodedFormat) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		if(line != null){
			line.open(decodedFormat);
			
			this.data = new byte[4096];
			
			line.start();
	
			int nbBytesRead = 4096;
		
		
			while(!shouldStop && nbBytesRead != -1){
						
				if(this.threadState == SoundThreadState.PLAY){
					
					nbBytesRead = toPlay.read(data, 0, data.length);
					// Writing on sound card
					line.write(data, 0, nbBytesRead);
					
					// make FFT and store result into the map
					this.map.putAll(decoder.dataToFFT(data));
				}
				
			} // end While
		}
		line.drain();
		line.stop();
		line.close();
		
	}
	
	
	@SuppressWarnings("unused")
	public void run(){
		AudioInputStream toPlay = null;
		SourceDataLine line = null;
		AudioFormat decodedFormat = null;
		
		AudioInputStream streamFromFile;
		try {
			streamFromFile = AudioSystem.getAudioInputStream(this.musicToRead);
			
			AudioFormat baseFormat = streamFromFile.getFormat();
			
			decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,baseFormat.getSampleRate(),
					16,baseFormat.getChannels(),
					baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(),
					false);
			
			toPlay = AudioSystem.getAudioInputStream(decodedFormat,streamFromFile);
			
			
			DataLine.Info info = new DataLine.Info(SourceDataLine.class,decodedFormat);
			line = (SourceDataLine)AudioSystem.getLine(info);
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		
		
		this.shouldStop = false;

		while(!this.shutdown){
			Logger.info("## State : " + this.threadState);
			
			while (this.threadState != SoundThreadState.PLAY) {
				Thread.yield();
			}
			
			try{
				int i =0;
				nextstep(line, toPlay, decodedFormat);;
				toPlay.close();
			}
			
			catch(Exception e){
				System.out.println(e.getStackTrace());
				Logger.critical("run 1 "+e.getStackTrace().toString());
			}
			// Be sure that file is closed even if an error occurs
			
			finally{
				// Only if the file exist and is assigned
				if(toPlay != null) { 
					try { toPlay.close(); } catch(IOException ioe){
						Logger.critical("run 2"+ioe.getStackTrace().toString());
						
					}// end catch
				}// end If
			}// end Finally
		}// end While
	}// function
	
	public void musicOver(){
		this.shouldStop = true;
	}

	@Override
	public void shutdown() {
		if(!this.shutdown){
			this.shutdown = true;
			this.ready = false;
		}
		
	}
	
	public void start() {
		if (this.ready) {
			Logger.critical("Starting file sound player ");
			new Thread(this).start();
		}
	}
	
	public void setFile(File f){
		this.musicToRead = f;
	}
	
	public void setMap(HashMap<Double, Double> map){
		this.map = map;
	}
	
	public void setState(SoundThreadState state){
		this.threadState = state;
	}

}
