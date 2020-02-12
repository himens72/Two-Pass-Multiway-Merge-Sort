package com.two.pass.multiway.mergesort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class UniqueChar {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(Constants.INPUT_PATH + Constants.INPUT_FILE1));
		String record = null;
		ArrayList<String> temp = new ArrayList<String>();
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		while ((record = br.readLine()) != null) {
			if (record.trim().length() > 0) {
				if (!temp.contains(record.substring(0, 8).trim())) {
					temp.add(record.substring(0, 8).trim());
					count1++;
				} else {
					count2++;
				}
			} else {
				count3++;
			}
			count4++;
		}
		System.out.println(count1);
		System.out.println(count2);
		System.out.println(count3);
		System.out.println(count4);
		System.out.println(temp.size());
		br.close();
	}
}
