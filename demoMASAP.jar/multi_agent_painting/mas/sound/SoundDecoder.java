package multi_agent_painting.mas.sound;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.math.MathException;
import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

import tools.appControl.Logger;

/**
 * @author Ludovic Lefoulon
 * @version 2 By Sylvain Le Leuch
 * 
 * This Class receive the data from the player, or the
 * recorder. It constructs the fft, and build HashMap 
 * for musical agents. 
 * 
 *  
 */
public class SoundDecoder{

	// This value is the sample rate value of the incoming byte array
	// It's the frequency which serve to discreet the signal
	// ex : 44100 Hz
	private float sampleRate;
	// This is the buffer size for sound file lecture
	//private byte[] data;
	// 
	private HashMap<Double, Double> frequencyMap;
	 
	/**
	 * This builder takes buffer size into parameter
	 */
	public SoundDecoder(float sampleRate){
		this.sampleRate = sampleRate;
		this.frequencyMap = new HashMap<Double, Double>();
	}
	
	/**
	 * Method to obtain the frequency graph of the current buffer, after the fft
	 * It computes frequency and corresponding amplitude from complex's table of the fft.
	 * 
	 * Formulas are : 
	 * amplitude = squareroot (realPart^2 + imaginaryPart^2 )
	 * frequency = positionInTable * sampleRate / bufferSize
	 * 
	 * @param fft
	 * @return
	 */
	private synchronized void computeFrequencyGraph(Complex[] fft){
		
		// Complex's part
		double imaginaryPart;
		double realPart;
		// Amplitude of a frequency
		Double amplitude = 0.0;
		// Frequency
		Double frequency = 0.0;
		
		int i = 0;
		for(i = 1 ; i < fft.length ; i++){
			
				// Compute the squared imaginary part
				imaginaryPart = Math.pow( fft[i].getImaginary(), 2.0);
				// Compute the squared real part
				realPart = Math.pow( fft[i].getReal(), 2.0);
				// Compute the module of the complex
				amplitude = Math.sqrt( imaginaryPart + realPart);
				// Compute the frequency
				frequency = i*44100.0/4096;
				// Add frequency and corresponding amplitude into the map
				frequencyMap.put(frequency, amplitude);
		}
		
	}
	
	/**
	 * This public method ust be called at each 
	 * @param dataToManipulate
	 * @return 
	 */
	public HashMap<Double, Double> dataToFFT(byte[] dataToManipulate){
		
		frequencyMap = new HashMap<Double, Double>();
		// Creation of a fft object
		FastFourierTransformer fastFourierTransformer = new FastFourierTransformer();
		
		// 
		ArrayList<Complex[]> result = new ArrayList<Complex[]>();
		
		int lengthOfDataToManipulate=dataToManipulate.length;
		int lengthOfDataForFFT;
		if(lengthOfDataToManipulate%2 !=0){
			lengthOfDataForFFT = lengthOfDataToManipulate+1;
		}
		else
			lengthOfDataForFFT = lengthOfDataToManipulate;
		
		double[] dataForFFT = new double[lengthOfDataForFFT];
		for(int i = 0;i < lengthOfDataToManipulate;++i){
			dataForFFT[i]=dataToManipulate[i];
		}
		
		try{
			result.add(fastFourierTransformer.transform2(dataForFFT));
			computeFrequencyGraph(result.get(result.size() - 1));
		}
		catch(MathException me){
			Logger.critical("Erreur : "+me.getStackTrace().toString());
		}
		
		return this.frequencyMap;
	}
	
}
