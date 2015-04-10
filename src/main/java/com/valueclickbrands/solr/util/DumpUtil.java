package com.valueclickbrands.solr.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.valueclickbrands.solr.model.Node;
import com.valueclickbrands.solr.model.Tag;

public class DumpUtil {
	private String dumpFilePath;

	public void setDumpFilePath(String dumpFilePath) {
		this.dumpFilePath = dumpFilePath;
	}


	public HashMap<Integer, Node> readNodeData() {
		List<String[]> nodeData = FileUtil.readDUMPFile(dumpFilePath
				+ File.separator +  "name");
		HashMap<Integer, Node> nodeMap = new HashMap<Integer, Node>();
		for (String[] arr : nodeData) {
			try {
				Node node = new Node(arr);
				nodeMap.put(node.getNid(), node);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return nodeMap;
	}
	
	public HashMap<String,String> getAliasData(){
		List<String[]> aliasData = FileUtil.readDUMPFile(dumpFilePath
				+ File.separator + "name");
		HashMap<String,String> aliasMap = new HashMap<String,String>();
		for (String[] arr : aliasData) {
			aliasMap.put(arr[0], arr[1]);
		}
		return aliasMap;
	}
	public HashMap<String,List<Tag>> getTagData(){
		List<String[]> tagData = FileUtil.readDUMPFile(dumpFilePath
				+ File.separator +  "name");
		HashMap<String,List<Tag>> tagMap = new HashMap<String,List<Tag>>();
		for (String[] arr : tagData) {
			Tag tag = new Tag();
			String key = arr[1]+"_"+arr[2];
			if(tagMap.containsKey(key)){
				tagMap.get(key).add(tag);
			}else{
				List<Tag> list = new ArrayList<Tag>();
				list.add(tag);
				tagMap.put(key, list);
			}
		}
		return tagMap;
	}
	
}
