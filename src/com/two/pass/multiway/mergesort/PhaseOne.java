package com.two.pass.multiway.mergesort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PhaseOne {
	QuickSort quickSort = new QuickSort();
	static int inputCount = 0;
	static int outputCount = 0;
	static int recordCount;
	static int blockCount;
	File buffer = null;
	long sortingTime = 0;
	public long getSortingTime() {
		return sortingTime;
	}

	public void setSortingTime(long sortingTime) {
		this.sortingTime = sortingTime;
	}

	int currentBlock = 0;
	BufferedReader br;

	public ArrayList<String> sortTuple(String tuple, String path) {
		if (tuple.equals("T2"))
			currentBlock++;
		long data_count = 0;
		ArrayList<String> temp = new ArrayList<>();
		long begin = System.currentTimeMillis();
		try {
			br = new BufferedReader(new FileReader(path));
			boolean run = true;
			long blockSize = (Constants.TOTAL_MEMORY / 1000); // Using 10% memory for reading data from disk
			while (run) {
				String record = null;
				ArrayList<String> subList = new ArrayList<>();

				while((record = br.readLine()) != null) {
					subList.add(record);
					recordCount++;
					++data_count;
					if (data_count == blockSize) {
						data_count = 0;
						++inputCount;
						++outputCount;
						break;
					}	
				}
				subList = quickSort.executeQuickSort(subList);

				String outputFile = Constants.BLOCK_PATH + "/Block-" + currentBlock;

				BufferedWriter write = new BufferedWriter(new FileWriter(outputFile));
				for (int i = 0; i < subList.size(); i++) {
					write.write(subList.get(i));
					write.newLine();
				}
				write.close();
				temp.add(outputFile);

				if (record == null)
					break;
				currentBlock++;
			}
			setBlockCount(temp.size());
			sortingTime += (System.currentTimeMillis() - begin);
			System.out.println("Time taken by Phase 1 for " + tuple + " : " + (System.currentTimeMillis() - begin)
					+ "ms ("  + (System.currentTimeMillis() - begin) / 1000.0 + "sec)");
		} catch (FileNotFoundException e) {
			System.out.println("The File doesn't Exist : " + path);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return temp;
	}

	public int getInputCount() {
		return inputCount;
	}

	public void setInputCount(int inputCount) {
		PhaseOne.inputCount = inputCount;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public static void setRecordCount(int recordCount) {
		PhaseOne.recordCount = recordCount;
	}

	public static int getBlockCount() {
		return blockCount;
	}

	public static void setBlockCount(int blockCount) {
		PhaseOne.blockCount = blockCount;
	}

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

}
