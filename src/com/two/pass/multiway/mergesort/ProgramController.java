package com.two.pass.multiway.mergesort;

import java.util.ArrayList;
import java.util.List;

public class ProgramController {
	static String fileName1 = Constants.INPUT_PATH + Constants.INPUT_FILE1;
	static String fileName2 = Constants.INPUT_PATH + Constants.INPUT_FILE2;

	public static void main(String[] args) throws InterruptedException {

		PhaseOne tpmms = new PhaseOne();
		tpmms.emptyBuffer();
		List<String> T1 = tpmms.sortTuple("T1", fileName1);
		System.out.println("Size T1 : " + T1.size());
		List<String> T2 = tpmms.sortTuple("T2", fileName2);
		System.out.println("Size T2 : " + T2.size());
		System.out.println("Totalllll Time to sort the T1 and T2 is " + tpmms.sortTime + "ms " + "(" + "~approx "
				+ tpmms.sortTime / 1000.0 + "sec)");
		System.out.println("Total number of records " + PhaseOne.record_count);
		System.out.println("Number of Blocks " + tpmms.currentBlock);
		System.out.println(
				"Total number of blocks used to store the Tuples is " + (PhaseOne.record_count / Constants.MAX_RECORD));
		ArrayList<String> sortedSublist = new ArrayList<String>();
		sortedSublist.addAll(T1);
		sortedSublist.addAll(T2);
		Long time = Long.parseLong(PhaseTwo.mergeSort(sortedSublist));
		System.out.println("Total Time taken to merge is " + time + "ms" + "(" + "~approx" + time / 1000.0 + "sec)");
		System.out.println("Total time taken for phase 1 and Phase 2 is " + (time + tpmms.sortTime) + "ms" + "("
				+ "~approx " + (time + tpmms.sortTime) / 1000.0 + "sec)");
		System.out.println("Total number of I/O's for sorting");
		System.out.println("Input blocks : " + tpmms.getInputCount());
		System.out.println("Output blocks : " + tpmms.getOutputCount());
		System.out.println("Total no of input and output block for sorting"
				+ (tpmms.getInputCount() + tpmms.getOutputCount()));

	}

}
