package org.greencubes.versions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

import org.greencubes.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;

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
			dir = new File(args[0]);
			directoryUrl = dir.toURI();
			versionFile.createNewFile();
			FileWriter fw = new FileWriter(versionFile);
			JSONObject json = new JSONObject();
			JSONArray jsonList = new JSONArray();
			make(dir, fw, jsonList);
			fw.close();
			json.put("files", jsonList);
			for(int arg = 1; arg < args.length - 1; arg += 2) { // Put parameters to resulting json
				json.put(args[arg], args[arg + 1]);
			}
			fw = new FileWriter(new File("version.js"));
			json.writeWithIdent(fw);
			fw.close();
			System.out.println("Done.");
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}	
	}
	
	private static void make(File file, FileWriter fw, JSONArray jsonList) throws IOException {
		for(File f : file.listFiles()) {
			if(f.isDirectory()) {
				make(f, fw, jsonList);
				continue;
			}
			String filename = Util.getRelativeName(f, directoryUrl);
			try {
				String hash = Util.getMD5Checksum(f);
				fw.append(filename).append(';').append(hash).append('\n');
				JSONObject joFileInfo = new JSONObject();
				joFileInfo.put("length", file.length());
				joFileInfo.put("name", filename);
				joFileInfo.put("hash", hash);
				jsonList.put(joFileInfo);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
