package com.two.pass.multiway.mergesort;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class TpmmsService {
	static String fileName = "";
	static int fileSize = -1;
	int totalTuples = -1;
	int totalBlocks = -1;
	static int totalFills = -1;
	int mainMemoryBlocks = -1;
	private static StringBuilder subListFilePath;
	private StringBuilder filePath;
	static File file;
	static int sortReadCount = -1;
	static int sortWriteCount = -1;

	public void fileDetails(String fileName) {
		TpmmsService.fileName = fileName;
		subListFilePath = new StringBuilder("./subListFiles/");
		filePath = new StringBuilder("./inputFiles/");
		filePath.append(fileName);
		file = new File(filePath.toString());
		fileSize = (int) file.length();
		System.out.println("File Name : " + fileName);
		System.out.println("Total Number of Tuples : " + fileSize / Constants.TUPLE_SIZE);
		totalBlocks = getTotalBlocks(fileSize, Constants.BLOCK_SIZE);
		System.out.println("Total Number of Blocks : " + totalBlocks);
		totalFills = getTotalFills(fileSize, Constants.MAIN_MEMORY_SIZE);
		System.out.println("Total Main Memory Fills : " + totalFills);
		mainMemoryBlocks = getMainMemoryBlocks(Constants.MAIN_MEMORY_SIZE, Constants.BLOCK_SIZE);
		System.out.println("Total Main Memory Blocks : " + mainMemoryBlocks);
		tpmms();
		System.out.println("-------------------------------------");
	}

	public static int tpmms() {
		try {
			final FileChannel inputFileChannel = FileChannel.open(Paths.get(file.getPath()),
					EnumSet.of(StandardOpenOption.READ));
			final ByteBuffer inputBuffer = ByteBuffer.allocate(Constants.MAIN_MEMORY_SIZE);
			final FileService fileService = new FileService();

			for (int run = 0; run < totalFills - 1; run++) {
				fileService.readInputFile(inputFileChannel, inputBuffer);
				QuickSort.sort(0, Constants.MAIN_MEMORY_SIZE - Constants.TUPLE_SIZE, inputBuffer);
				write(inputBuffer, fileService, run);
			}

			if (fileSize % Constants.MAIN_MEMORY_SIZE != 0) {
				fileService.readInputFile(inputFileChannel, inputBuffer);
				QuickSort.sort(0,
						((fileSize % Constants.MAIN_MEMORY_SIZE) / Constants.TUPLE_SIZE * Constants.TUPLE_SIZE)
								- Constants.TUPLE_SIZE,
						inputBuffer);
				write(inputBuffer, fileService, totalFills - 1);
			} else {
				fileService.readInputFile(inputFileChannel, inputBuffer);
				QuickSort.sort(0, Constants.MAIN_MEMORY_SIZE - Constants.TUPLE_SIZE, inputBuffer);
				write(inputBuffer, fileService, totalFills - 1);
			}

			if (inputFileChannel.isOpen()) {
				try {
					inputFileChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			sortReadCount = fileService.getReadCount();
			sortWriteCount = fileService.getWriteCount();
			System.out.println(fileName + " - Total Blocks Read : " + sortReadCount);
			System.out.println(fileName + " - Total Blocks Write : " + sortWriteCount);

		} catch (FileNotFoundException fnfException) {
			System.err.println("File not exits: " + fnfException.getMessage());
			return 0;
		} catch (IOException ioException) {
			System.err.println("Erro while reading: " + ioException.getMessage());
			return 0;
		}

		return 0;// sortReadCount + sortWriteCount;
	}

	private static void write(final ByteBuffer inputBuffer, final FileService fileService, final int run)
			throws FileNotFoundException, IOException {
		final String sublistFileName = subListFilePath.toString() + fileName + "_" + run + ".txt";
		final FileChannel outputFileChannel = FileChannel.open(Files.createFile(Paths.get(sublistFileName)),
				EnumSet.of(StandardOpenOption.WRITE));
		fileService.writeOutputFile(outputFileChannel, inputBuffer);
		if (outputFileChannel.isOpen()) {
			try {
				outputFileChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static int getTotalNoOfTuples(final int fileSize, final int tupleSize) {
		return fileSize / tupleSize;
	}

	public static long getNoOfTuplesPerBlock(final long blockSize, final int tupleSize) {
		return blockSize / tupleSize;
	}

	public static int getTotalBlocks(final int fileSize, final int blockSize) {
		return (int) Math.ceil((double) fileSize / blockSize);
	}

	public static int getMainMemoryBlocks(final int mainMemorySize, final int blockSize) {
		return mainMemorySize / blockSize;
	}

	public static int getTotalFills(final int fileSize, final int mainMemorySize) {
		return (int) Math.ceil((double) fileSize / mainMemorySize);
	}

	public static int getMemorySizeInMB() {
		return (int) (Runtime.getRuntime().totalMemory() / (1024 * 1024));
	}

}
