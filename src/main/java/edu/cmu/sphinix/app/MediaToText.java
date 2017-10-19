package edu.cmu.sphinix.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sphinx.alignment.LongTextAligner;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechAligner;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.app.aligner.AlignerApp;
import edu.cmu.sphinx.app.transcriber.TranscriberApp;
import edu.cmu.sphinx.result.WordResult;

public class MediaToText {

	/*private static final String ACOUSTIC_MODEL_PATH =
            "resource:/edu/cmu/sphinx/models/en-us/en-us";
    private static final String DICTIONARY_PATH =
            "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
    private static final String MODEL_PATH = "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin";
    private static final String AUDIO_URL = "/edu/cmu/sphinx/demo/aligner/10001-90210-01803.wav";
    
    
    public static void main(String[] args) throws Exception {
    	MediaText(ACOUSTIC_MODEL_PATH, DICTIONARY_PATH, MODEL_PATH, AUDIO_URL);
        }*/
    
	public static String MediaText(String ACOUSTIC_MODEL_PATH, String DICTIONARY_PATH, String MODEL_PATH, String AUDIO_URL) throws MalformedURLException, IOException, URISyntaxException {
		
		String TEXT = "";	
		Configuration configuration = new Configuration();

        // Load model from the jar
        configuration
                .setAcousticModelPath(ACOUSTIC_MODEL_PATH);

        // You can also load model from folder
        // configuration.setAcousticModelPath("file:en-us");

        configuration
                .setDictionaryPath(DICTIONARY_PATH);
        configuration
                .setLanguageModelPath(MODEL_PATH);

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(
                configuration);
        //InputStream stream = TranscriberApp.class.getResourceAsStream(AUDIO_URL);
        InputStream stream = new FileInputStream(AUDIO_URL);
        SpeechResult result;
        recognizer.startRecognition(stream);

        while ((result = recognizer.getResult()) != null) {
        	TEXT += result.getHypothesis()+" ";
        }
        recognizer.stopRecognition();
        System.out.println(TEXT);
        
        return TEXT;
        
       
}
	
}
