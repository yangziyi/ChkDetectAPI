package org.jt.ae.service;

import net.sf.json.JSONArray;

import java.util.ArrayList;


public class testMain {
	public static void main(String[] args) {
		ArrayList<String> testList = new ArrayList<String>();
		
		testList.add("{\"a\":\"1\"},{\"b\":\"1\"},{\"c\":\"1\"}");
		System.out.println(testList);
		JSONArray json = JSONArray.fromObject(testList);
		System.out.println(json);
		System.out.println(json.length());
	}
}
