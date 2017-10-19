package edu.cmu.sphinix.app;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class FileChooserExample extends JFrame implements ActionListener {
	
	private static final String ACOUSTIC_MODEL_PATH = "resource:/edu/cmu/sphinx/models/en-us/en-us";
    private static final String DICTIONARY_PATH = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
    private static final String MODEL_PATH = "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin";
    //private static final String AUDIO_URL = "/edu/cmu/sphinx/demo/aligner/10001-90210-01803.wav";
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
					//ta.setText("PLEASE WAIT...");
					String t = mediaToText.MediaText(ACOUSTIC_MODEL_PATH, DICTIONARY_PATH, MODEL_PATH, filepath);
					ta.setText(t);
					/*BufferedReader br = new BufferedReader(new FileReader(filepath));
					String s1 = "", s2 = "";
					while ((s1 = br.readLine()) != null) {
						s2 += s1 + "\n";
					}
					ta.setText(s2);
					br.close();*/
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
}