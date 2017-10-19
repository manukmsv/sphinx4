package edu.cmu.sphinix.app;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.frontend.util.AudioFileDataSource;

import java.awt.event.*;
import java.io.*;
import java.net.URL;

public class FileChooserExample extends JFrame implements ActionListener {

	private static final String ACOUSTIC_MODEL_PATH = "resource:/edu/cmu/sphinx/models/en-us/en-us";
	private static final String DICTIONARY_PATH = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
	private static final String MODEL_PATH = "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin";
	// private static final String AUDIO_URL =
	// "/edu/cmu/sphinx/demo/aligner/10001-90210-01803.wav";
	MediaToText mediaToText = new MediaToText();
	JMenuBar mb;
	JMenu file;
	JMenuItem open;
	JMenuItem save;
	JTextArea ta;

	FileChooserExample() {
		open = new JMenuItem("Open File");
		open.addActionListener(this);
		save = new JMenuItem("Save");
		save.addActionListener(this);
		file = new JMenu("File");
		file.add(open);
		file.add(save);
		mb = new JMenuBar();
		mb.setBounds(0, 0, 800, 20);
		mb.add(file);
		ta = new JTextArea(800, 800);
		ta.setBounds(0, 20, 800, 800);
		add(mb);
		add(ta);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == open) {
			JFileChooser fc = new JFileChooser();
			int i = fc.showOpenDialog(this);
			if (i == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				String filepath = f.getPath();
				try {
					// -----------------------------------------------------
					//String t = mediaToText.MediaText(ACOUSTIC_MODEL_PATH, DICTIONARY_PATH, MODEL_PATH, filepath);
					Configuration configuration = new Configuration();

			        // Load model from the jar
			        configuration.setAcousticModelPath(ACOUSTIC_MODEL_PATH);

			        // You can also load model from folder
			        // configuration.setAcousticModelPath("file:en-us");

			        configuration.setDictionaryPath(DICTIONARY_PATH);
			        configuration.setLanguageModelPath(MODEL_PATH);

			        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
			        //InputStream stream = new FileInputStream(filepath);
			        // ----
			        // AudioFileDataSource dataSource = (AudioFileDataSource) cm.lookup("audioFileDataSource");
			        AudioFileDataSource dataSource = null;
			        URL audioFileURL = new URL("file:" + filepath);
			        
			        if(filepath.endsWith(".mp3") || filepath.endsWith(".MP3")) {
			        	InputStream audioSrc = new FileInputStream(filepath);
			        	//add buffer for mark/reset support
			        	InputStream bufferedIn = new BufferedInputStream(audioSrc);
				        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
				        AudioFormat baseFormat = audioStream.getFormat();
				        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
		                        baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
		                AudioInputStream decodedAudioStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
		                dataSource.setInputStream(decodedAudioStream, null);
			        } else {
			        	dataSource.setAudioFile( audioFileURL, null);
			        }
	                // ----
			        
			        
			        //recognizer.startRecognition(stream);
			        String TEXT = "";
			        SpeechResult result;
			        while ((result = recognizer.getResult()) != null) {
			        	TEXT += result.getHypothesis()+" ";
			        }
			        recognizer.stopRecognition();
					// -----------------------------------------------------
					ta.setText(TEXT);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} else if (e.getSource() == save) {
			System.out.println("SAve");
			JFileChooser saveFile = new JFileChooser();
			int i = saveFile.showSaveDialog(this);
			if (i == JFileChooser.APPROVE_OPTION) {
				// We'll be making a mytmp.txt file, write in there, then move it to
				// the selected
				// file. This takes care of clearing that file, should there be
				// content in it.
				File targetFile = saveFile.getSelectedFile();

				try {
					if (!targetFile.exists()) {
						targetFile.createNewFile();
					}

					FileWriter fw = new FileWriter(targetFile);

					fw.write(ta.getText());
					fw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}
	}

	public static void main(String[] args) {
		FileChooserExample om = new FileChooserExample();
		om.setSize(500, 500);
		om.setLayout(null);
		om.setVisible(true);
		om.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private static AudioInputStream convertToPCM(AudioInputStream audioInputStream)
    {
        AudioFormat m_format = audioInputStream.getFormat();

        if ((m_format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) &&
            (m_format.getEncoding() != AudioFormat.Encoding.PCM_UNSIGNED))
        {
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                m_format.getSampleRate(), 16,
                m_format.getChannels(), m_format.getChannels() * 2,
                m_format.getSampleRate(), m_format.isBigEndian());
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
    }

    return audioInputStream;
}
}