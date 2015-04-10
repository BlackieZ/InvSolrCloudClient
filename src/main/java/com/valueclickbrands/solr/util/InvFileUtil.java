package com.valueclickbrands.solr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;

public class InvFileUtil {
	private static Logger logger = Logger.getLogger(InvFileUtil.class);
	
	public static HashSet<String> getIgnoreCase(String _file) {
		logger.info("Read IgnoreCase--->"+_file);
		HashSet<String> ignoreCases = new HashSet<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(_file),"UTF-8"));
//			BufferedReader br = new BufferedReader(new FileReader(new File(
//					_file)));
			String line = null;
			while ((line = br.readLine()) != null) {
				ignoreCases.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return ignoreCases;
	}
	
	public static HashMap<String,String> getPharseSynonym(String _file){
		logger.info("Read PharseSynonym--->"+_file);
		HashMap<String,String> pharseSynonym = new HashMap<String,String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(_file),"UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] pharse = line.split(":");
				if(pharse.length==2){
					String[] synonyms = pharse[1].split(",");
					for(String synonym:synonyms){
						pharseSynonym.put(synonym.trim().toLowerCase(),pharse[0].toLowerCase().trim());
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
					e.printStackTrace();
				}
			}
		}
		return pharseSynonym;
	}
	
	public static void main(String[] args){
		HashMap<String,String> data = InvFileUtil.getPharseSynonym("C:\\work\\workspaces\\Perforce\\npan_SI-NPAN1_4079\\datateam\\Content Site Service\\Search\\CSSearch-Invest\\InvSearch\\src\\main\\resources\\PhraseSynonym.txt");
		System.out.println(data);
	}
}