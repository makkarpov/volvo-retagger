package ru.makkarpov.retagger;

import helliker.id3.ID3v2Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;

public class Recoder {
	private boolean useNumbers = false;
	private boolean useHashes = false;
	private boolean flatten = true;
	private int curFile = 1;
	
	private ReplaceFile names = new ReplaceFile();
	private ReplaceFile tags = new ReplaceFile();
	
	public Recoder() {
		names.load("names");
		tags.load("tags");
	}
	
	public void setUseNumbers(boolean use) {
		useNumbers = use;
		curFile = 1;
	}
	
	public void setUseHashes(boolean use) {
		useHashes = use;
	}

	private String hash(String s) {
		try {
			String h = new BigInteger(MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"))).toString(16).toUpperCase() + "0000000";
			return h.substring(1, 9);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getClass().getCanonicalName();
		}
	}
	
	public void parseDirectory(File dir, File out, EncodingListener listener) throws Exception {
	    if (!dir.isDirectory()) {
	        System.out.println("Skipping " + dir + ": not a directory");
        }

		File[] list = dir.listFiles();
		for (File f: list) {
		    if (!f.isDirectory()) {
		        System.out.println("Skipping " + f + ": not a directory");
            }

			File[] list2 = f.listFiles();
			for (File f1: list2) {
				if (!f1.getName().endsWith(".mp3")) continue;

				String newName;
				if (useHashes) newName = hash(f.getName() + f1.getName()) + ".mp3";
				else if (useNumbers) newName = String.format("%04d.mp3", curFile++);
				else newName = names.replace(f.getName() + " - " + f1.getName()) + ".mp3";

				File target = new File(out, newName);
				parseFile(f1, f.getName(), target, listener);
			}
		}
	}

	public static Boolean copyFile(File source, File dest) {
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			int nLength;
			byte[] buf = new byte[8000];
			while (true) {
				nLength = is.read(buf);
				if (nLength < 0) {
					break;
				}
				os.write(buf, 0, nLength);
			}
			return true;
		} catch (IOException ex) {

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception ex) {
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (Exception ex) {
				}
			}
		}
		return false;
	}
	
	private void parseFile(File in, String dirName, File out, EncodingListener listener) throws Exception {
		String songName = in.getName();
		if (songName.toLowerCase().endsWith(".mp3"))
			songName = songName.substring(0, songName.length() - 3);
		
		listener.setSong(dirName, songName);
		
		if (out.exists() && !out.delete())
            throw new IOException("Failed to delete file: " + out);

        Files.copy(in.toPath(), out.toPath());
		 		
		ID3v2Tag tag1 = new ID3v2Tag(out, 0);
		tag1.removeTag();
		
		RandomAccessFile raf = new RandomAccessFile(out, "rw");
		
		Rus_ID3v1 tag = new Rus_ID3v1();
		tag.setArtist(tags.replace(dirName));
		tag.setTitle(tags.replace(songName));
		tag.setAlbum("");
		tag.setComment("");
		tag.write(raf);
		
		raf.close();
	}
}
