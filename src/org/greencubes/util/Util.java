package org.greencubes.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;

public class Util {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat fileDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	public static void close(InputStream is) {
		if(is != null)
			try {
				is.close();
			} catch(IOException e) {
			}
	}

	public static void close(OutputStream os) {
		if(os != null)
			try {
				os.close();
			} catch(IOException e) {}
	}

	public static void disconnect(HttpURLConnection uc) {
		if(uc != null)
			uc.disconnect();
	}

	public static void close(Reader r) {
		if(r != null)
			try {
				r.close();
			} catch(IOException e) {
			}
	}
	
	public static String readFile(File f) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader in = null;
		try { 
			in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String s;
			while((s = in.readLine()) != null) {
				if(sb.length() == 0)
					sb.append('\n');
				sb.append(s);
			}
		} finally {
			Util.close(in);
		}
		return sb.toString();
	}
	
	public static void writeFile(String str, File f) throws IOException {
		if(!f.exists()) {
			if(!f.getParentFile().exists() && !f.getParentFile().mkdirs())
				throw new IOException("Unable to create dirs for file");
			if(!f.createNewFile())
				throw new IOException("Unable to create file");
		}
		FileWriter w = new FileWriter(f);
		w.write(str);
		w.close();
	}
	
	private static byte[] createChecksum(File file) throws Exception {
		InputStream fis = new FileInputStream(file);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do {
			numRead = fis.read(buffer);
			if(numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while(numRead != -1);
		fis.close();
		return complete.digest();
	}

	public static String getMD5Checksum(File file) throws Exception {
		byte[] b = createChecksum(file);
		String result = "";
		for(int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
	
	public final static String join(String[] split) {
		return join(split, "");
	}
	
	public final static String join(String[] split, String glue) {
		return join(split, glue, 0);
	}
	
	public final static String join(String[] split, String glue, int start) {
		return join(split, glue, start, split.length - 1);
	}
	
	public static String join(String[] split, String glue, int start, int end) {
		if(split.length == 0)
			return "";
		start = start >= split.length ? split.length - 1 : start;
		end = end >= split.length ? split.length - 1 : end;
		int length = glue.length() * (end - start);
		for(int i = start; i <= end; ++i)
			length += split[i].length();
		StringBuilder sb = new StringBuilder(length);
		boolean set = false;
		for(int i = start; i <= end; ++i) {
			if(set)
				sb.append(glue);
			sb.append(split[i]);
			set = true;
		}
		return sb.toString();
	}
	
	public static String getRelativeName(File file, URI directory) throws IOException {
		URI fUri = file.toURI();
		return directory.relativize(fUri).getPath();
	}
}
