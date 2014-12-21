package org.greencubes.versions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

import org.greencubes.util.Util;

public class Main {

	private static File dir;
	private static URI directoryUrl;
	
	public static void main(String[] args) {
		if(args.length == 0) {
			System.err.println("Please, specify directory!");
			System.exit(1);
		}
		File versionFile = new File("version.md5");
		try {
			dir = new File(Util.join(args, " "));
			directoryUrl = dir.toURI();
			versionFile.createNewFile();
			FileWriter fw = new FileWriter(versionFile);
			make(dir, fw);
			fw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}
	
	private static void make(File dir, FileWriter fw) throws IOException {
		for(File f : dir.listFiles()) {
			if(f.isDirectory()) {
				make(f, fw);
				continue;
			}
			String filename = Util.getRelativeName(f, directoryUrl);
			try {
				String hash = Util.getMD5Checksum(f);
				fw.append(filename).append(';').append(hash).append('\n');
				//System.out.println(filename + ";" + hash);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
