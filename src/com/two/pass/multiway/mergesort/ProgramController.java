package com.two.pass.multiway.mergesort;

import java.io.File;
import java.util.List;

public class ProgramController {
	static String fileName1 = Constants.INPUT_PATH + Constants.INPUT_FILE1;
	static String fileName2 = Constants.INPUT_PATH + Constants.INPUT_FILE2;

	public static void main(String[] args) throws InterruptedException {
		cleanDirectory();
		System.out.println("Memory Size :  " + getMemorySize());
		PhaseOne tpmms = new PhaseOne();
		List<String> T1 = tpmms.sortTuple("T1", fileName1);
		List<String> T2 = tpmms.sortTuple("T2", fileName2);
		PhaseTwo phaseTwo = new PhaseTwo(T1, T2);
		Long time = Long.parseLong(phaseTwo.performMergeSort());
		System.out.println("Total number of records " + tpmms.getRecordCount());
		System.out.println("Block Used :  " + tpmms.currentBlock);
		System.out.println("Sorting Time for T1 and T2 : " + tpmms.sortTime / 1000.0 + " seconds approx");
		System.out.println("Sorting Time for T1 and T2 is " + tpmms.sortTime + " ms");
		System.out.println("Phase 2 Time : " + time + "ms" + "(" + "~approx" + time / 1000.0 + "sec)");
		System.out.println("Total time Phase 1 & Phase 2 : " + (time + tpmms.sortTime) + "ms" + "(" + "~approx "
				+ (time + tpmms.sortTime) / 1000.0 + "sec)");
		System.out.println("Total number of I/O's for sorting");
		System.out.println("Input blocks : " + tpmms.getInputCount());
		System.out.println("Output blocks : " + tpmms.getOutputCount());
		System.out.println(
				"Total no of input and output block for sorting" + (tpmms.getInputCount() + tpmms.getOutputCount()));

	}

	public static void cleanDirectory() {
		System.out
				.println("****************************Cleaning Directory*********************************************");
		File deleteBlocks = new File(Constants.BLOCK_PATH);
		if (!deleteBlocks.exists()) {
			System.out.println("Block Directory Created : " + deleteBlocks.mkdir());

		} else if (deleteBlocks.isFile()) {
			System.out.println("Output Directory Deleted : " + deleteBlocks.delete());
			System.out.println("Output Directory Created : " + deleteBlocks.mkdir());
		} else {
			String fileList[] = deleteBlocks.list();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].trim().length() >= 1) {
					File currentBlockFiles = new File(deleteBlocks.getPath(), fileList[i]);
					currentBlockFiles.delete();
				}
			}
			System.out.println("Output Directory Deleted :- " + deleteBlocks.delete());
			System.out.println("Output Directory Created :- " + deleteBlocks.mkdir());
		}
		File deleteOutput = new File(Constants.OUTPUT_PATH);
		if (deleteOutput.exists()) {
			System.out.println("Output Directory Deleted : " + deleteOutput.delete());
			System.out.println("Output Directory Created : " + deleteOutput.mkdir());
		}
		System.out.println("Diretory Cleaned");
		System.out.println("****************************TPMMS Console*********************************************");
	}

	private static int getMemorySize() {
		return (int) (Runtime.getRuntime().totalMemory() / (1024 * 1024));
	}

}
