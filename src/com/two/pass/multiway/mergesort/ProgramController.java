package com.two.pass.multiway.mergesort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ProgramController {
	static String fileName1 = Constants.INPUT_PATH + Constants.INPUT_FILE1;
	static String fileName2 = Constants.INPUT_PATH + Constants.INPUT_FILE2;

	public static void main(String[] args) throws InterruptedException {
		System.out
		.println("****************************Cleaning Directory*********************************************");
		buildBlockDirectory();
		buildOutputDirectory();
		System.out.println("Diretory Cleaned");
		System.out.println("****************************TPMMS Console*********************************************");
		System.gc();
		System.out.println("Memory Size :  " + getMemorySize());
		System.out.println("Tuple Size : " + Constants.TUPLE_SIZE);
		PhaseOne phaseOne = new PhaseOne();
		System.out.println("****************************Phase 1 for T1*********************************************");
		List<String> T1 = phaseOne.sortTuple("T1", fileName1);
		int recordCount1 = phaseOne.getRecordCount();
		System.out.println("Records in T1 : " + recordCount1);
		System.out.println("Block for T1 : " + (recordCount1 * Constants.TUPLE_SIZE) / Constants.BLOCK_SIZE);
		System.out.println("****************************Phase 1 for T2*********************************************");
		List<String> T2 = phaseOne.sortTuple("T2", fileName2);
		int recordCount2 = phaseOne.getRecordCount() - recordCount1;
		System.out.println("Records in T2 : " + recordCount2);
		System.out.println("Block for T2 : " + (recordCount2 * Constants.TUPLE_SIZE) / Constants.BLOCK_SIZE);
		System.out.println("****************************Phase 1 Overview*********************************************");
		System.out.println("Total number of records " + phaseOne.getRecordCount());
		System.out.println("Total number of Block " + (phaseOne.getRecordCount() * Constants.TUPLE_SIZE) / Constants.BLOCK_SIZE);
		int phaseOneDiskIO = (2 * phaseOne.getRecordCount() * Constants.TUPLE_SIZE) / Constants.BLOCK_SIZE;
		System.out.println("Sorted Disk IO " + phaseOneDiskIO);
		System.gc();
		System.out.println("****************************Phase 2*********************************************");
		PhaseTwo phaseTwo = new PhaseTwo(T1, T2);
		phaseTwo.performMergeSort();
		System.out.println("Phase 2 Time : " + phaseTwo.getMergeTtime() + "ms" + " ("
				+ phaseTwo.getMergeTtime() / 1000.0 + " sec)");
		System.out.println("Total Number of I/O :" + phaseTwo.getTupleCount1() + phaseTwo.getTupleCount2());
		System.out.println("****************************Output Overview*********************************************");
		System.out.println(phaseTwo.getReadCount() + phaseTwo.getWriteCount());
		System.out.println(
				"Total time Phase 1 & Phase 2 : " + (phaseTwo.getMergeTtime() + phaseOne.getSortingTime()) + "ms");
		System.out.println("Total time Phase 1 & Phase 2 : "
				+ ((phaseTwo.getMergeTtime() + phaseOne.getSortingTime()) / 1000.0) + " sec");
		System.out.println("Total no. of Write Operation in Phase 2 : " + phaseTwo.getWrite());
		buildOutputDirectory(phaseTwo.getOutputPath());
		// buildBlockDirectory();
	}

	private static void buildOutputDirectory(String outputPath) {
		try {
			File outputDir = new File(Constants.OUTPUT_PATH);
			if (!outputDir.exists()) {
				System.out.println("Output Directory Created : " + outputDir.mkdir());
			}
			BufferedReader br = new BufferedReader(new FileReader(outputPath));
			FileWriter writer = new FileWriter(Constants.OUTPUT_PATH + "output.txt", true);
			String temp;

			while ((temp = br.readLine()) != null) {
				writer.write(temp + "\n");
				writer.flush();
			}
			br.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void buildOutputDirectory() {
		File outputDir = new File(Constants.OUTPUT_PATH);
		if (!outputDir.exists()) {
			System.out.println("Output Directory Created : " + outputDir.mkdir());
		} else if (outputDir.isFile()) {
		} else {
			String fileList[] = outputDir.list();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].trim().length() >= 1) {
					File currentBlockFiles = new File(outputDir.getPath(), fileList[i]);
					currentBlockFiles.delete();
				}
			}
			System.out.println("Output Directory Deleted :- " + outputDir.delete());
			System.out.println("Output Directory Created :- " + outputDir.mkdir());
		}
	}

	public static void buildBlockDirectory() {
		File deleteBlocks = new File(Constants.BLOCK_PATH);
		if (!deleteBlocks.exists()) {
			System.out.println("Block Directory Created : " + deleteBlocks.mkdir());
		} else if (deleteBlocks.isFile()) {
		} else {
			String fileList[] = deleteBlocks.list();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].trim().length() >= 1) {
					File currentBlockFiles = new File(deleteBlocks.getPath(), fileList[i]);
					currentBlockFiles.delete();
				}
			}
			System.out.println("Block Directory Deleted :- " + deleteBlocks.delete());
			System.out.println("Block Directory Created :- " + deleteBlocks.mkdir());
		}
	}

	private static int getMemorySize() {
		return (int) (Runtime.getRuntime().totalMemory() / (1024 * 1024));
	}

}
