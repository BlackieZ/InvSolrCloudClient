package com.valueclickbrands.solr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class FileUtil {
	private static Logger logger = Logger.getLogger(FileUtil.class);
	
	public static HashMap<String, String> getSymbol(String _file) {
		logger.info("Read Symbol--->"+_file);
		HashMap<String, String> symbolWords = new HashMap<String, String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(_file),"UTF-8"));
//			BufferedReader br = new BufferedReader(new FileReader(new File(
//					_file)));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] data = line.toLowerCase().trim().split(":");
				if (data.length == 2) {
					symbolWords.put(data[0], data[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return symbolWords;
	}

	public static HashSet<String> getPhrase(String _file) {
		logger.info("Read Phrase--->"+_file);
		HashSet<String> stopWords = new HashSet<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(_file),"UTF-8"));
//			BufferedReader br = new BufferedReader(new FileReader(new File(
//					_file)));
			String line = null;
			while ((line = br.readLine()) != null) {
				stopWords.add(line.toLowerCase().trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return stopWords;
	}

	public static HashSet<String> getStopWords(String _file) {
		logger.info("Read StopWords--->"+_file);
		HashSet<String> stopWords = new HashSet<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(_file),"UTF-8"));
//			BufferedReader br = new BufferedReader(new FileReader(new File(
//					_file)));
			String line = null;
			while ((line = br.readLine()) != null) {
				stopWords.add(line.toLowerCase().trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return stopWords;
	}

	public static HashMap<String, String> getStemmingDic(String _file) {
		logger.info("Read StemmingDic--->"+_file);
		HashMap<String, String> stemming = new HashMap<String, String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(_file),"UTF-8"));
//			BufferedReader br = new BufferedReader(new FileReader(new File(
//					_file)));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.contains(":")) {
					String[] linects = line.split(":");
					if (linects.length == 2) {
						String words = linects[1].trim().toLowerCase();
						String oriword = linects[0].trim().toLowerCase();
						if (words.contains(" ")) {
							String[] value = words.split(" ");
							for (int i = 0; i < value.length; i++) {
								stemming.put(value[i], oriword);
							}
						} else
							stemming.put(words, oriword);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return stemming;
	}
	
	public static void writeDUMPFile(String file,List<String[]> data){
		logger.info("Write File--->"+file);
		CSVWriter writer = null;
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file),Charset.forName("UTF-8"));
			writer = new CSVWriter(out);
			for(String[] row:data){
				writer.writeNext(row);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(writer!=null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static List<String[]> readDUMPFile(String file){
		logger.info("Read File--->"+file);
		CSVReader reader = null;
		List<String[]> data = new ArrayList<String[]>();
		try {
			reader = new CSVReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String[] nextLine = null;
			while ((nextLine = reader.readNext()) != null) {
				data.add(nextLine);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}
}
