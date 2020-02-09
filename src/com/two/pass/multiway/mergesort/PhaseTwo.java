package com.two.pass.multiway.mergesort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class PhaseTwo {
	static long time = 0;
	static int itertion = 0;
	private static int maximum_record = 40;

	public static String mergeSort(ArrayList<String> sublist) {
		long itertionStart = System.currentTimeMillis();
		ArrayList<String> mergedFiles = new ArrayList<String>();
		System.lineSeparator();
		int read1 = 0;
		int read2 = 0;
		int write = 0;
		for (int k = 0; k < sublist.size(); k = k + 2) {
			String list12 = System.getProperty("user.dir") + System.getProperty("file.separator") + "buffer"
					+ System.getProperty("file.separator") + itertion + "-sublist-" + k + "_" + (k + 1);

			try {
				BufferedReader out1 = new BufferedReader(new FileReader(sublist.get(k)));
				BufferedReader out2 = null;

				if (k + 1 < sublist.size())
					out2 = new BufferedReader(new FileReader(sublist.get(k + 1)));

				BufferedWriter bw = new BufferedWriter(new FileWriter(list12));

				String record1 = null;
				String record2 = null;
				if (out2 != null) {
					while (true) {

						if (record1 == null) {
							record1 = out1.readLine();
							if (read1 == maximum_record && record1 != null) {
								++PhaseOne.icount;
								read1 = 0;
							}

							++read1;

						}
						if (record2 == null) {
							record2 = out2.readLine();
							if (read2 == maximum_record && record2 != null) {
								++PhaseOne.icount;
								read1 = 0;
							}

							++read1;
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

						if (write == maximum_record) {
							++PhaseOne.ocount;
							write = 0;
						}
						bw.newLine();

					}
				} else {

					while ((record1 = out1.readLine()) != null) {
						bw.write(record1);
						++write;
						if (write == maximum_record) {
							++PhaseOne.ocount;
							write = 0;
						}
						bw.newLine();
					}
				}

				bw.close();

				mergedFiles.add(list12);

				out1.close();
				// out2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		time = time + (System.currentTimeMillis() - itertionStart);
		System.out.println("iteration " + itertion + " in phase 2 merging time taken for this iteration is "
				+ (System.currentTimeMillis() - itertionStart) + "ms" + "(" + "~approx "
				+ (System.currentTimeMillis() - itertionStart) / 1000.0 + "sec)");
		for (String f : sublist) {
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
