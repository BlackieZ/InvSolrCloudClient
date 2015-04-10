package com.valueclickbrands.solr.model;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author Vanson Zou
 * @date Jan 29, 2015 
 */

public class TreeNode {

	private int id;
	private int pLid;
	private int has_children;
	private int weight;
	private int depth;
	private List<TreeNode> childrenList = new ArrayList<TreeNode>();
	private int rootId;
	private Node node;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getpLid() {
		return pLid;
	}
	public void setpLid(int pLid) {
		this.pLid = pLid;
	}
	public int getHas_children() {
		return has_children;
	}
	public void setHas_children(int has_children) {
		this.has_children = has_children;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public List<TreeNode> getChildrenList() {
		return childrenList;
	}
	
	public int getRootId() {
		return rootId;
	}
	public void setRootId(int rootId) {
		this.rootId = rootId;
	}
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	
	
	
	
	
}
