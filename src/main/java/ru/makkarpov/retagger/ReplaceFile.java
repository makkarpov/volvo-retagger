package ru.makkarpov.retagger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ReplaceFile {
	private HashMap<Character, Character> replaces = new HashMap<Character, Character>();
	
	public void load(String name) {
		File f = new File(name + "_replacements.txt");
		
		if (!f.exists()) return;
		
		System.out.println("Found replacements for " + name);
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
			
			replaces.clear();
			
			while (true) {
				String line = br.readLine();
				if (line == null) break;
				
				replaces.put(line.charAt(0), line.charAt(1));
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public String replace(String s) {
		char[] chars = s.toCharArray();
		for (Character c: replaces.keySet()) {
			char f = c.charValue();
			char t = replaces.get(c).charValue();
			for (int i = 0; i < chars.length; i++)
				if (chars[i] == f) chars[i] = t;
		}
		return new String(chars);
	}
}
