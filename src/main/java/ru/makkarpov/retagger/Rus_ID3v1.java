package ru.makkarpov.retagger;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Rus_ID3v1 {
	protected String album = "";
	protected String artist = "";
	protected String comment = "";
	protected String title = "";
	protected String year = "";
	protected byte genre = -1;

	private final String ENCODING = "CP-1252";
	
	public Rus_ID3v1()
	{
	}

	private static String truncate(String s, int len) {
		s = s.trim();
		return s.length() > len ? s.substring(0, len) : s;
	}

	public Rus_ID3v1(RandomAccessFile paramRandomAccessFile) throws IOException {
		read(paramRandomAccessFile);
	}

	public void setAlbum(String paramString) {
		this.album = truncate(paramString, 30);
	}

	public String getAlbum() {
		return this.album;
	}

	public void setArtist(String paramString) {
		this.artist = truncate(paramString, 30);
	}

	public String getArtist() {
		return this.artist;
	}

	public void setComment(String paramString) {
		this.comment = truncate(paramString, 30);
	}

	public String getComment() {
		return this.comment;
	}

	public void setGenre(byte paramByte) {
		this.genre = paramByte;
	}

	public byte getGenre() {
		return this.genre;
	}

	public int getSize() {
		return 128;
	}

	public void setTitle(String paramString) {
		this.title = truncate(paramString, 30);
	}

	public String getTitle() {
		return this.title;
	}

	public void setYear(String paramString) {
		this.year = truncate(paramString, 4);
	}

	public String getYear() {
		return this.year;
	}

	public void delete(RandomAccessFile paramRandomAccessFile) throws IOException {
		if (seek(paramRandomAccessFile))
			paramRandomAccessFile.setLength(paramRandomAccessFile.length() - 128L);
	}

	public boolean equals(Object paramObject)
	{
		if (!(paramObject instanceof Rus_ID3v1))
			return false;
		Rus_ID3v1 localID3v1 = (Rus_ID3v1)paramObject;
		if (!this.album.equals(localID3v1.album))
			return false;
		if (!this.artist.equals(localID3v1.artist))
			return false;
		if (!this.comment.equals(localID3v1.comment))
			return false;
		if (this.genre != localID3v1.genre)
			return false;
		if (!this.title.equals(localID3v1.title))
			return false;
		if (!this.year.equals(localID3v1.year))
			return false;
		return super.equals(paramObject);
	}

	public void read(RandomAccessFile paramRandomAccessFile) throws IOException {
		byte[] arrayOfByte = new byte[30];
		if (!seek(paramRandomAccessFile)) return;
		paramRandomAccessFile.read(arrayOfByte, 0, 30);
		this.title = new String(arrayOfByte, 0, 30, ENCODING).trim();
		paramRandomAccessFile.read(arrayOfByte, 0, 30);
		this.artist = new String(arrayOfByte, 0, 30, ENCODING).trim();
		paramRandomAccessFile.read(arrayOfByte, 0, 30);
		this.album = new String(arrayOfByte, 0, 30, ENCODING).trim();
		paramRandomAccessFile.read(arrayOfByte, 0, 4);
		this.year = new String(arrayOfByte, 0, 4, ENCODING).trim();
		paramRandomAccessFile.read(arrayOfByte, 0, 30);
		this.comment = new String(arrayOfByte, 0, 30, ENCODING).trim();
		paramRandomAccessFile.read(arrayOfByte, 0, 1);
		this.genre = arrayOfByte[0];
	}

	public boolean seek(RandomAccessFile paramRandomAccessFile) throws IOException {
		byte[] arrayOfByte = new byte[3];
		paramRandomAccessFile.seek(paramRandomAccessFile.length() - 128L);
		paramRandomAccessFile.read(arrayOfByte, 0, 3);
		String str = new String(arrayOfByte, 0, 3);
		return str.equals("TAG");
	}

	public String toString()
	{
		String str = "ID3v1 " + getSize() + "\n";
		str = str + "Title = " + this.title + "\n";
		str = str + "Artist = " + this.artist + "\n";
		str = str + "Album = " + this.album + "\n";
		str = str + "Comment = " + this.comment + "\n";
		str = str + "Year = " + this.year + "\n";
		str = str + "Genre = " + this.genre + "\n";
		return str;
	}

	public void write(RandomAccessFile paramRandomAccessFile) throws IOException {
		byte[] arrayOfByte = new byte[128];
		byte[] strBuf;
		int j = 3;
		delete(paramRandomAccessFile);
		paramRandomAccessFile.seek(paramRandomAccessFile.length());
		arrayOfByte[0] = 'T';
		arrayOfByte[1] = 'A';
		arrayOfByte[2] = 'G';
		String str;

		str = truncate(this.title, 30);
		strBuf = str.getBytes("ISO-8859-5");
		System.arraycopy(strBuf, 0, arrayOfByte, j, strBuf.length);
		j += 30;

		str = truncate(this.artist, 30);
		strBuf = str.getBytes("ISO-8859-5");
		System.arraycopy(strBuf, 0, arrayOfByte, j, strBuf.length);
		j += 30;

		str = truncate(this.album, 30);
		strBuf = str.getBytes("ISO-8859-5");
		System.arraycopy(strBuf, 0, arrayOfByte, j, strBuf.length);
		j += 30;

		str = truncate(this.year, 4);
		strBuf = str.getBytes("ISO-8859-5");
		System.arraycopy(strBuf, 0, arrayOfByte, j, strBuf.length);
		j += 4;

		str = truncate(this.comment, 30);
		strBuf = str.getBytes("ISO-8859-5");
		System.arraycopy(strBuf, 0, arrayOfByte, j, strBuf.length);
		j += 30;

		arrayOfByte[j] = this.genre;
		paramRandomAccessFile.write(arrayOfByte);
	}
	
	public void writeRaw(RandomAccessFile paramRandomAccessFile) throws IOException {
		byte[] arrayOfByte = new byte[128];
		byte[] strBuf;
		int j = 3;
		delete(paramRandomAccessFile);
		paramRandomAccessFile.seek(paramRandomAccessFile.length());
		arrayOfByte[0] = 'T';
		arrayOfByte[1] = 'A';
		arrayOfByte[2] = 'G';
		String str;

		str = truncate(this.title, 30);
		for (int i = 0; i < str.length(); i++)
			arrayOfByte[j+i] = (byte) str.charAt(i);
		j += 30;

		str = truncate(this.artist, 30);
		for (int i = 0; i < str.length(); i++)
			arrayOfByte[j+i] = (byte) str.charAt(i);
		j += 30;

		str = truncate(this.album, 30);
		for (int i = 0; i < str.length(); i++)
			arrayOfByte[j+i] = (byte) str.charAt(i);
		j += 30;

		str = truncate(this.year, 4);
		for (int i = 0; i < str.length(); i++)
			arrayOfByte[j+i] = (byte) str.charAt(i);
		j += 4;

		str = truncate(this.comment, 30);
		for (int i = 0; i < str.length(); i++)
			arrayOfByte[j+i] = (byte) str.charAt(i);
		j += 30;

		arrayOfByte[j] = this.genre;
		paramRandomAccessFile.write(arrayOfByte);
	}
}