package com.two.pass.multiway.mergesort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PhaseOne {
	static int inputCount = 0;
	public int getInputCount() {
		return inputCount;
	}

	public void setInputCount(int inputCount) {
		PhaseOne.inputCount = inputCount;
	}

	static int outputCount = 0;
	static int recordCount;
	public int getRecordCount() {
		return recordCount;
	}

	public static void setRecordCount(int recordCount) {
		PhaseOne.recordCount = recordCount;
	}

	static int blockCount;
	File buffer = null;
	long sortTime = 0;
	int currentBlock  = 0;
	public static int getBlockCount() {
		return blockCount;
	}

	public static void setBlockCount(int blockCount) {
		PhaseOne.blockCount = blockCount;
	}

	BufferedReader br;
	
	public int getOutputCount() {
		return outputCount;
	}

	public void setOutputCount(int outputCount) {
		PhaseOne.outputCount = outputCount;
	}

	public int getCurrentBlock() {
		return currentBlock;
	}

	public void setCurrentBlock(int currentBlock) {
		this.currentBlock = currentBlock;
	}

	public ArrayList<String> sortTuple(String tuple, String path) {
		if(tuple.equals("T2"))
			currentBlock++;
		int data_count = 0;
		ArrayList<String> temp = new ArrayList<>();
		long begin = System.currentTimeMillis();
		try {
			br = new BufferedReader(new FileReader(path));
			boolean run = true;
			while (run) {
				String record = null;
				ArrayList<String> subList = new ArrayList<>();

				for (int k = 0; k < Constants.MAX_RECORD && (record = br.readLine()) != null; k++) {
					subList.add(record);
					recordCount++;
					++data_count;
					if (data_count == Constants.MAX_RECORD) {
						data_count = 0;
						++inputCount;
						++outputCount;
					}
				}
				Collections.sort(subList, new Comparator<String>() {
					public int compare(String o1, String o2) {
						return o1.substring(0, 18).compareTo(o2.substring(0, 18));
					}
				});
				
				String outputFile = Constants.BLOCK_PATH+ "/Block-" + currentBlock;
				BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
				for (String s : subList) {
					out.write(s);
					out.newLine();
				}
				out.close();			
				temp.add(outputFile);

				if (record == null)
					break;
				currentBlock++;
			}
			setBlockCount(temp.size());
			sortTime = sortTime + (System.currentTimeMillis() - begin);
			System.out.println("Time take to sort relation " + tuple + " is " + (System.currentTimeMillis() - begin)
					+ "ms" + "(" + "~approx " + (System.currentTimeMillis() - begin) / 1000.0 + "sec)");
		} catch (FileNotFoundException e) {
			System.out.println("The File doesn't Exist : " + path);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return temp;
	}
}
