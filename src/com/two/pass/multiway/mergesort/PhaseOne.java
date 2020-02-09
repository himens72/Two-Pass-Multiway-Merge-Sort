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
	static int record_count;
	static int no_of_sublist;
	File buffer = null;
	long sortTime = 0;
	int currentBlock  = 0;
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

	public void emptyBuffer() {
		buffer = new File(Constants.BLOCK_PATH);
		if (!buffer.exists()) {
			buffer.mkdir();
		} else if (buffer.isFile()) {
			buffer.delete();
			buffer.mkdir();
		}
		String intermediate[] = buffer.list();
		for (String bufferLoctn : intermediate) {
			File buff = new File(buffer.getPath(), bufferLoctn);
			buff.delete();
		}
	}

	public ArrayList<String> sortTuple(String tuple, String path) {
		if(tuple.equals("T2"))
			currentBlock++;
		int data_count = 0;
		ArrayList<String> tmp = new ArrayList<String>();
		long begin = System.currentTimeMillis();
		try {
			br = new BufferedReader(new FileReader(path));
			boolean run = true;
			while (run) {
				String record = null;
				ArrayList<String> subList = new ArrayList<String>();

				for (int k = 0; k < Constants.MAX_RECORD && (record = br.readLine()) != null; k++) {
					subList.add(record);
					record_count++;
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
				tmp.add(outputFile);

				if (record == null)
					break;
				currentBlock++;
			}
			no_of_sublist = tmp.size();
			sortTime = sortTime + (System.currentTimeMillis() - begin);
			System.out.println("Time take to sort relation " + tuple + " is " + (System.currentTimeMillis() - begin)
					+ "ms" + "(" + "~approx " + (System.currentTimeMillis() - begin) / 1000.0 + "sec)");
		} catch (FileNotFoundException e) {
			System.out.println("The file not found");
			System.exit(1);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		return tmp;
	}
	
	public static void main(String[] args) throws InterruptedException {
		if ((args != null) && (args.length == 2)) {
			PhaseOne tpmms = new PhaseOne();
			tpmms.emptyBuffer();
			ArrayList<String> T1 = tpmms.sortTuple(args[0], "T1");
			System.out.println("Size T1 : "  +T1.size());
			ArrayList<String> T2 = tpmms.sortTuple(args[1], "T2");
			System.out.println("Size T2 : "  +T2.size());
			System.out.println("Totalllll Time to sort the T1 and T2 is " + tpmms.sortTime + "ms " + "(" + "~approx "
					+ tpmms.sortTime / 1000.0 + "sec)");
			System.out.println("Total number of records " + PhaseOne.record_count);
			System.out.println("Number of sublist " + PhaseOne.no_of_sublist);
			System.out.println("Total number of blocks used to store the Tuples is " + (PhaseOne.record_count / Constants.MAX_RECORD));
			ArrayList<String> sortedSublist = new ArrayList<String>();
			sortedSublist.addAll(T1);
			sortedSublist.addAll(T2);
			Long time = Long.parseLong(PhaseTwo.mergeSort(sortedSublist));
			System.out
					.println("Total Time taken to merge is " + time + "ms" + "(" + "~approx" + time / 1000.0 + "sec)");
			System.out.println("Total time taken for phase 1 and Phase 2 is " + (time + tpmms.sortTime) + "ms" + "("
					+ "~approx " + (time + tpmms.sortTime) / 1000.0 + "sec)");
			System.out.println("Total number of I/O's for sorting");
			System.out.println("Input blocks : " + inputCount);
			System.out.println("Output blocks : " + outputCount);
			System.out.println("Total no of input and output block for sorting" + (inputCount + outputCount));
		}
	}
}
