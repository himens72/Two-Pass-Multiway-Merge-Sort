package com.two.pass.multiway.mergesort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

public class PhaseTwo {
	static long time = 0;
	static int itertion = 0;
	static String currentMergeFile = "";
	static List<String> listOfFiles;

	public PhaseTwo(List<String> T1, List<String> T2) {
		listOfFiles = new ArrayList<>();
		listOfFiles.addAll(T1);
		listOfFiles.addAll(T2);
	}

	public String performMergeSort() {
		return mergeSort(listOfFiles);
	}

	public static String mergeSort(List<String> blockList) {
		long itertionStart = System.currentTimeMillis();
		ArrayList<String> mergedFiles = new ArrayList<>();
		System.lineSeparator();
		int tupleCount1 = 1;
		int tupleCount2 = 1;
		int write = 1;
		System.out.println("BLOCK SIZE + " +blockList.size());
		for (int i = 0; i < blockList.size(); i = i + 2) {
			currentMergeFile = Constants.BLOCK_PATH + itertion + "-Block-" + i + "_" + (i + 1);
			try {
				BufferedReader br1 = new BufferedReader(new FileReader(blockList.get(i)));
				BufferedReader br2 = null;
				if (i + 1 < blockList.size())
					br2 = new BufferedReader(new FileReader(blockList.get(i + 1)));

				BufferedWriter bw = new BufferedWriter(new FileWriter(currentMergeFile));
				String tuple1 = null;
				String tuple2 = null;
				String currentTuple = "";
				if (br2 != null) {
					while (true) {
						if (tuple1 == null) {
							tuple1 = br1.readLine();
							tupleCount1++;
						}
						if (tuple2 == null) {
							tuple2 = br2.readLine();
							tupleCount2++;
						}
						if (tuple1 == null && tuple2 == null) {
							break;
						}
//						System.out.println(i+ "------->" +currentTuple);
						if (tuple1 != null && tuple2 != null) {
							if (currentTuple.trim().length() > 0) {
								String id1 = tuple1.substring(0, 8);
								String date1 = tuple1.substring(8, 18);
								String id2 = tuple2.substring(0, 8);
								String date2 = tuple2.substring(8, 18);
								if (id1.equals(id2)) {
									if (currentTuple.substring(0, 8).equals(id1)) {
										if (date1.compareToIgnoreCase(date2) > 0) { // Condition is true when date1 is
											// latest
											if (tuple1.substring(8, 18)
													.compareToIgnoreCase(currentTuple.substring(8, 18)) > 0) {
												currentTuple = tuple1;
											}

										} else if (date1.compareToIgnoreCase(date2) < 0) { // Condition is true when
											if (tuple2.substring(8, 18)
													.compareToIgnoreCase(currentTuple.substring(8, 18)) > 0) {
												currentTuple = tuple2;
											}
										} else { // Condition is true when date1 and date 2 is same
											currentTuple = tuple1;
										}
										tuple1 = null;
										tuple2 = null;
									} else {
										//Systen.out.println(currentTuple);
										bw.write(currentTuple);
										bw.newLine();
										if (date1.compareToIgnoreCase(date2) > 0) { // Condition is true when date1 is
											// latest
											currentTuple = tuple1;
										} else if (date1.compareToIgnoreCase(date2) < 0) { // Condition is true when
											// date2
											// is latest
											currentTuple = tuple2;
										} else { // Condition is true when date1 and date 2 is same
											currentTuple = tuple1;
										}
										tuple1 = null;
										tuple2 = null;
										write++;
									}
								} else {
									String currentId = currentTuple.substring(0, 8);
									String currentDate = currentTuple.substring(8, 18);
									if (currentId.equals(id1)) {
										if (date1.compareToIgnoreCase(currentDate) > 0) {
											currentTuple = tuple1;
										}
										tuple1 = null;
									} else if (currentId.equals(id2)) {
										if (date2.compareToIgnoreCase(currentDate) > 0) {
											currentTuple = tuple2;
										}
										tuple2 = null;
									} else {
										//Systen.out.println(currentTuple);
										bw.write(currentTuple);
										bw.newLine();
										if (tuple1.substring(0, 18).compareToIgnoreCase(tuple2.substring(0, 18)) > 0) {
											currentTuple = tuple2;
											tuple2 = null;
										} else if (tuple1.substring(0, 18)
												.compareToIgnoreCase(tuple2.substring(0, 18)) < 0) {
											currentTuple = tuple1;
											tuple1 = null;
										}
									}
								}
							} else { // This Block is executed once Time When Merge take place 1st Time
								String id1 = tuple1.substring(0, 8);
								String date1 = tuple1.substring(8, 18);
								String id2 = tuple2.substring(0, 8);
								String date2 = tuple2.substring(8, 18);
								if (id1.equals(id2)) {
									if (date1.compareToIgnoreCase(date2) > 0) { // Condition is true when date1 is
										// latest
										//if (tuple1.substring(0, 18).compareToIgnoreCase(currentTuple.substring(0, 18)) > 0) {
											currentTuple = tuple1;
										//}
									} else if (date1.compareToIgnoreCase(date2) < 0) { // Condition is true when date2
										// is
										// latest
										//if (tuple2.substring(0, 18).compareToIgnoreCase(currentTuple.substring(0, 18)) > 0) {
											currentTuple = tuple2;
										//}
									} else { // Condition is true when date1 and date 2 is same
										currentTuple = tuple1;
									}
									tuple1 = null;
									tuple2 = null;
								} else {
									if (tuple1.substring(0, 18).compareToIgnoreCase(tuple2.substring(0, 18)) > 0) {
										currentTuple = tuple2;
										tuple2 = null;
									} else if (tuple1.substring(0, 18)
											.compareToIgnoreCase(tuple2.substring(0, 18)) < 0) {
										currentTuple = tuple1;
										tuple1 = null;
									}
								}

							}
						} else {
							if (tuple1 != null) {
								if(currentTuple.trim().length() > 0) {
									if (currentTuple.substring(0, 8).equals(tuple1.substring(0, 8))) {
										if (tuple1.substring(0, 18).compareToIgnoreCase(currentTuple.substring(0, 18)) > 0) {
											currentTuple = tuple1;
										}
										tuple1 = null;
									} else {
										//Systen.out.println("****> " +currentTuple);
										bw.write(currentTuple);
										bw.newLine();
										++write;
										currentTuple = tuple1;
										tuple1 = null;
									}	
								} else {
									currentTuple = tuple1;
									tuple1 = null;
								}
								
							} else  {
								if(currentTuple.trim().length() > 0) {
									if (currentTuple.substring(0, 8).equals(tuple2.substring(0, 8))) {
										if (tuple2.substring(0, 18).compareToIgnoreCase(currentTuple.substring(0, 18)) > 0) {
											currentTuple = tuple2;
										}
										tuple2 = null;
									} else {
										//Systen.out.println("--> " +currentTuple);
										bw.write(currentTuple);
										bw.newLine();
										++write;
										currentTuple = tuple2;
										tuple2 = null;
									}	
								} else {
									currentTuple = tuple2;
									tuple2 = null;
								}
								
							}
						}

					}
				} else {
					while ((tuple1 = br1.readLine()) != null) {
						if(currentTuple.trim().length() > 0) {
							if (currentTuple.substring(0, 8).equals(tuple1.substring(0, 8))) {
								if (tuple1.substring(0, 18).compareToIgnoreCase(currentTuple.substring(0, 18)) > 0) {
									currentTuple = tuple1;
								}
							} else {
								//Systen.out.println(currentTuple);
								bw.write(currentTuple);
								bw.newLine();
								++write;
								currentTuple = tuple1;
							}
						} else {
							currentTuple = tuple1;
						}
					}	
				}
				//Systen.out.println("End " + currentTuple);
				//Systen.out.println("--------------------->");
				bw.write(currentTuple);
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
			System.out.println("Current Output FIle : " + currentMergeFile);
			return new Long(time).toString();
		}
	}
}
