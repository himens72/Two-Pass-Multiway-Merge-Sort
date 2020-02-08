package com.two.pass.multiway.mergesort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class PhaseOne {
	static int icount = 0;
	static int ocount = 0;
	private static int maximum_record = 160;
	private static int record_count;
	private static int no_of_sublist;
	File buffer = null;
	File buffer1 = null;
	File buffer2 = null;
	long sortTime = 0;

	public PhaseOne() {
		buffer1 = new File(System.getProperty("user.dir") + "/buffer1");
		buffer2 = new File(System.getProperty("user.dir") + "/buffer2");
	}

	private void emptyBuffer() {
		buffer = new File(System.getProperty("user.dir") + "/buffer");
//		if (buffer.isFile())
//			buffer.delete();
//			buffer.mkdir();
//
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

	private ArrayList<String> sortRecord(String loc, String relation) {
		int i = 0;
		int data_count = 0;
		ArrayList<String> tmp = new ArrayList<String>();
		long begin = System.currentTimeMillis();
		try {
			BufferedReader br = new BufferedReader(new FileReader(loc));

			boolean run = true;
			while (run) {
				String record = null;
				ArrayList<String> subList = new ArrayList<String>();

				for (int k = 0; k < maximum_record && (record = br.readLine()) != null; k++) {
					subList.add(record);
					record_count++;
					++data_count;
					if (data_count == maximum_record) {
						data_count = 0;
						++icount;
						++ocount;
					}

				}

				Collections.sort(subList, new Comparator<String>() {
					public int compare(String o1, String o2) {
						return o1.substring(0, 18).compareTo(o2.substring(0, 18));
					}
				});

				String outputFile = System.getProperty("user.dir") + "/buffer/sublist-" + i;
				BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
				for (String s : subList) {
					out.write(s);
					out.newLine();
				}

				out.close();

				tmp.add(outputFile);

				if (record == null)
					break;
				i++;
			}
			// }
			no_of_sublist = tmp.size();
			sortTime = sortTime + (System.currentTimeMillis() - begin);
			System.out.println("Time take to sort relation " + relation + " is " + (System.currentTimeMillis() - begin)
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

	private void emptyBufferBD1() {
		// File buffer1 = new File(System.getProperty("user.dir") +
		// "/buffer/bagdiffer1");
//		if (buffer.isFile())
//			buffer.delete();
//			buffer.mkdir();
//
		if (!buffer1.exists()) {
			buffer1.mkdir();
		} else if (buffer1.isFile()) {
			buffer1.delete();
			buffer1.mkdir();
		}
		String intermediate[] = buffer1.list();
		for (String bufferLoctn : intermediate) {
			File buff = new File(buffer1.getPath(), bufferLoctn);
			buff.delete();
		}
	}

	private void emptyBufferBD2() {
//		File buffer2 = new File(System.getProperty("user.dir") + "/buffer/bagdiffer2");
//		if (buffer.isFile())
//			buffer.delete();
//			buffer.mkdir();
//
		if (!buffer2.exists()) {
			buffer2.mkdir();
		} else if (buffer2.isFile()) {
			buffer2.delete();
			buffer2.mkdir();
		}
		String intermediate[] = buffer2.list();
		for (String bufferLoctn : intermediate) {
			File buff = new File(buffer2.getPath(), bufferLoctn);
			buff.delete();
		}
	}

	public static void main(String[] args) {
		if ((args != null) && (args.length == 2)) {
			PhaseOne tpmms = new PhaseOne();
			tpmms.emptyBuffer();
			ArrayList<String> T1 = tpmms.sortRecord(args[0], "T1");
			// System.out.println("sorts the T1 separately");
			// ArrayList<String> T1 = tpmms.sortRecord(args[0],"T1");
			ArrayList<String> T = new ArrayList<String>();
			try {

				FileUtils.copyDirectory(tpmms.buffer, tpmms.buffer1, true);
				File intermediate[] = tpmms.buffer1.listFiles();
				for (File file : intermediate) {
					if (file.isFile()) {
						// System.out.println(file.getAbsolutePath());
						Collections.addAll(T, file.getAbsolutePath());
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			ArrayList<String> T2 = tpmms.sortRecord(args[1], "T2");
			System.out.println("Total Time to sort the T1 and T2 is " + tpmms.sortTime + "ms " + "(" + "~approx "
					+ tpmms.sortTime / 1000.0 + "sec)");
			System.out.println("Total number of records " + PhaseOne.record_count);
			System.out.println("Number of sublist " + PhaseOne.no_of_sublist);
			System.out.println("Total number of blocks used to store the Tuples is " + (PhaseOne.record_count / maximum_record));
			ArrayList<String> sortedSublist = new ArrayList<String>();
			sortedSublist.addAll(T1);
			sortedSublist.addAll(T2);
			Long time = Long.parseLong(PhaseTwo.mergeSort(sortedSublist));
			System.out
					.println("Total Time taken to merge is " + time + "ms" + "(" + "~approx" + time / 1000.0 + "sec)");
			System.out.println("Total time taken for phase 1 and Phase 2 is " + (time + tpmms.sortTime) + "ms" + "("
					+ "~approx " + (time + tpmms.sortTime) / 1000.0 + "sec)");
			System.out.println("Total number of I/O's for sorting");
			System.out.println("Input blocks : " + icount);
			System.out.println("Output blocks : " + ocount);
			System.out.println("Total no of input and output block for sorting" + (icount + ocount));
			System.out.println("BagDifference Count starts!!");
			long Bagstart = System.currentTimeMillis();
			BagDiffernce.mergeSort(T, 0);

			try {
				tpmms.bagDifference();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long BagStop = System.currentTimeMillis();
			System.out.format("BagDiffernce time %d ms ~approx %f sec\n", BagStop - Bagstart,
					(BagStop - Bagstart) / 1000.0);
			// System.out.format("Total time %d ms \nm",((BagStop-Bagstart)+time));
			System.out.format("Execution time for phase 1, phase2 and computing the bag difference is "
					+ ((BagStop - Bagstart) + time + (tpmms.sortTime)) + " ms" + "(" + "~approx "
					+ ((BagStop - Bagstart) + time + (tpmms.sortTime)) / 1000.0 + "sec)\n");
			System.out.println("Total number of I/O's for sorting and computing the bag Difference");
			System.out.println("Input blocks : " + icount);
			System.out.println("Output blocks : " + ocount);
			System.out.println("Total no of input and output block for sorting and computing the bag Difference "
					+ (icount + ocount));
		}
	}

	public void bagDifference() throws IOException {
//		Arraylist student_detail
		FileReader fr = new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator") + "buffer1"
				+ System.getProperty("file.separator") + "7-sublist-0_1");
		BufferedReader t1 = new BufferedReader(fr);
		Scanner scan = new Scanner(t1);
		FileReader fr1 = new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator") + "buffer"
				+ System.getProperty("file.separator") + "8-sublist-0_1");
		BufferedReader t2 = new BufferedReader(fr1);
		Scanner scan1 = new Scanner(t2);
		BufferedWriter out1 = new BufferedWriter(
				new FileWriter(System.getProperty("user.dir") + System.getProperty("file.separator") + "T1bg.txt"));
		BufferedWriter bag_diff = new BufferedWriter(new FileWriter(
				System.getProperty("user.dir") + System.getProperty("file.separator") + "bagdifference.txt"));
		String str = null;
		int count = 0;
		String studentID = null;
		String previousStudentID = null;
		while ((str = t1.readLine()) != null) {

			studentID = str.substring(0, 18);
			if (previousStudentID != null) {
				if (studentID.equalsIgnoreCase(previousStudentID)) {

					count++;
				} else {
					out1.write(previousStudentID + "," + (count + 1) + System.getProperty("line.separator"));
					out1.flush();
					count = 0;
				}
			}
			previousStudentID = studentID;
		}

		/*
		 * while((str=scan.next())!=null) {
		 * 
		 * studentID=str.substring(0, 8); String buff=null;
		 * if((buff=scan.nextLine())!=null) { if(buff.substring(0,
		 * 8).equals(studentID)){
		 * 
		 * ++count;
		 * 
		 * } else { if(count!=0) { ++count;
		 * System.out.println("bagdiffer"+studentID+count);
		 * out1.write(studentID+","+count+System.getProperty("line.separator"));
		 * out1.flush(); } count=0; break; }
		 * 
		 * } }
		 */
		scan.close();
		out1.close();
		BufferedWriter out2 = new BufferedWriter(
				new FileWriter(System.getProperty("user.dir") + System.getProperty("file.separator") + "Outbg.txt"));
		str = null;
		count = 0;
		studentID = null;
		previousStudentID = null;

		while ((str = t2.readLine()) != null) {

			studentID = str.substring(0, 8);
			if (previousStudentID != null) {
				if (studentID.equalsIgnoreCase(previousStudentID)) {

					count++;
				} else {
					out2.write(previousStudentID + "," + (count + 1) + System.getProperty("line.separator"));
					out2.flush();
					count = 0;
				}
			}
			previousStudentID = studentID;
		}

		/*
		 * while((str=scan1.next())!=null) {
		 * 
		 * studentID=str.substring(0, 8); String buff=null;
		 * if((buff=scan1.nextLine())!=null) { if(buff.substring(0,
		 * 8).equals(studentID)){
		 * 
		 * ++count;
		 * 
		 * } else { if(count!=0) { ++count;
		 * System.out.println("bagdiffer"+studentID+count);
		 * out2.write(studentID+","+count+System.getProperty("line.separator"));
		 * out2.flush(); } count=0; break; }
		 * 
		 * } }
		 */ scan1.close();
		out2.close();
		BufferedReader input1 = new BufferedReader(
				new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator") + "Outbg.txt"));
		BufferedReader input2 = new BufferedReader(
				new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator") + "T1bg.txt"));
		String stuid = "";
		String StudentDetails = "";
		System.out.println(" Bag Difference for T1 and T2 are");
		while ((StudentDetails = input2.readLine()) != null) {

			String StudentId = StudentDetails.substring(0, 8);
			while ((stuid = input1.readLine()) != null) {
				if (stuid.substring(0, 8).equalsIgnoreCase(StudentId)) {
					int differ = 0;
					String[] details = stuid.split(",");
					String[] detailsT1 = StudentDetails.split(",");
					int table1 = Integer.parseInt(detailsT1[1]);
					int table2 = Integer.parseInt(details[1]);
					int diff = table2 - table1;
					if (diff > 0) {
						differ = table1 - diff;
						if (differ > 0) {
							System.out.println(StudentId + "," + differ + "\n");
							bag_diff.write(StudentId + "," + differ);
							bag_diff.write(System.lineSeparator());
							bag_diff.flush();
							break;
						}
					}
				}
			}
		}
		input1.close();
		input2.close();
		bag_diff.close();
	}
}
