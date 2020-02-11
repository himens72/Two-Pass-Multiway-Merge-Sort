package com.two.pass.multiway.mergesort;

public class Constants {

	public static final int TUPLE_SIZE = 100;
	public static final int BLOCK_SIZE = (4096 / TUPLE_SIZE) * TUPLE_SIZE;
	public static final int TOTAL_MEMORY = ((int) (Runtime.getRuntime().totalMemory() * 0.5));
	public static final int MEMORY_SIZE = (TOTAL_MEMORY / BLOCK_SIZE) * BLOCK_SIZE;
	public static final int MAX_RECORD = 40;
	public static final String INPUT_PATH = "./inputfiles/";
	public static final String BLOCK_PATH = "./blocks/";
	public static final String OUTPUT_PATH = "./outputfiles/";
	public static final String INPUT_FILE1 = "sample3.txt";
	public static final String INPUT_FILE2 = "sample4.txt";
	public static final String T1 = "T1";
	public static final String T2 = "T2";

}
