package ru.makkarpov.retagger;

import helliker.id3.ID3v2Tag;

import java.io.File;
import java.io.PrintStream;
import java.io.RandomAccessFile;

public class RetaggerMain implements EncodingListener {
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			new GUIMain();
			return;
		}

		new RetaggerMain(args);
	}
	
	private RetaggerMain(String[] args) throws Exception {
		System.out.println("VOLVO tags encoder by makkarpov");
		
		// Process all directories

		Recoder rcd = new Recoder();
		
		for (String s: args) {
			if (s.equals("--cp866")) {
				System.setOut(new PrintStream(System.out, true, "CP866"));
				continue;
			}
			
			if (s.equals("--numbers")) {
				rcd.setUseNumbers(true);
				continue;
			}
			
			if (s.equals("--hashes")) {
				rcd.setUseNumbers(true);
				continue;
			}
			
			File f = new File(s);
			if (!f.exists() || !f.isDirectory()) {
				System.out.println(s + ": omitting, does not exists or not a directory.");
				continue;
			}
			
			File out = new File(s + "_retagged");
			
			rcd.parseDirectory(f, out, this);
		}
		
		System.out.println("Completed.");
	}

	@Override
	public void setSong(String artist, String title) {
		System.out.println(">> " + artist + " - " + title);
	}
}
