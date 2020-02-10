package com.two.pass.multiway.mergesort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class PhaseTwo {
	static long time = 0;
	static int itertion = 0;

	public static String mergeSort(List<String> blockList) {
		long itertionStart = System.currentTimeMillis();
		ArrayList<String> mergedFiles = new ArrayList<String>();
		System.lineSeparator();
		int tupleCount1 = 0;
		int tupleCount2 = 0;
		int write = 0;
		for (int i = 0; i < blockList.size(); i = i + 2) {
			String currentMergeFile = Constants.BLOCK_PATH + itertion + "-Block-" + i + "_" + (i + 1);
			System.out.println(currentMergeFile);
			try {
				BufferedReader br1 = new BufferedReader(new FileReader(blockList.get(i)));
				BufferedReader br2 = null;

				if (i + 1 < blockList.size())
					br2 = new BufferedReader(new FileReader(blockList.get(i + 1)));

				BufferedWriter bw = new BufferedWriter(new FileWriter(currentMergeFile));

				String record1 = null;
				String record2 = null;
				if (br2 != null) {
					while (true) {
						if (record1 == null) {
							record1 = br1.readLine();
							if (tupleCount1 == Constants.MAX_RECORD && record1 != null) {
								++PhaseOne.inputCount;
								tupleCount1 = 0;
							}
							++tupleCount1;
						}
						if (record2 == null) {
							record2 = br2.readLine();
							if (tupleCount2 == Constants.MAX_RECORD && record2 != null) {
								++PhaseOne.outputCount;
								tupleCount1 = 0;
							}
							++tupleCount1;
						}

						if (record1 == null && record2 == null)
							break;

						if (record1 != null && record2 != null) {

							if (record1.substring(0, 18).compareToIgnoreCase(record2.substring(0, 18)) > 0) {
								bw.write(record2);
								++write;
								record2 = null;
							} else if (record1.substring(0, 18).compareToIgnoreCase(record2.substring(0, 18)) < 0) {
								bw.write(record1);
								++write;
								record1 = null;
							} else {
								bw.write(record1 + "\n");
								bw.write(record2);
								write = write + 2;
								record1 = null;
								record2 = null;
							}
						} else {

							if (record1 != null) {
								bw.write(record1);
								++write;
								record1 = null;
							} else {
								bw.write(record2);
								++write;
								record2 = null;
							}
						}
						if (write == Constants.MAX_RECORD) {
							++PhaseOne.outputCount;
							write = 0;
						}
						bw.newLine();
					}
				} else {
					while ((record1 = br1.readLine()) != null) {
						bw.write(record1);
						++write;
						if (write == Constants.MAX_RECORD) {
							++PhaseOne.outputCount;
							write = 0;
						}
						bw.newLine();
					}
				}
				bw.close();
				mergedFiles.add(currentMergeFile);
				br1.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		time = time + (System.currentTimeMillis() - itertionStart);
		System.out.println("iteration " + itertion + " in phase 2 merging time taken for this iteration is "
				+ (System.currentTimeMillis() - itertionStart) + "ms" + "(" + "~approx "
				+ (System.currentTimeMillis() - itertionStart) / 1000.0 + "sec)");
		for (String f : blockList) {
			File buff = new File(f);
			boolean b = buff.delete();
		}
		if (mergedFiles.size() > 1) {
			itertion++;
			return mergeSort(mergedFiles);
		} else {
			return new Long(time).toString();
		}
	}
}
