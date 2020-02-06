package com.two.pass.multiway.mergesort;

import java.io.File;



public class ProgramExecution {
	public static final int TUPLE_SIZE = 100;
	public static final int BLOCK_SIZE = (4096 / TUPLE_SIZE) * TUPLE_SIZE;
	public static final int MAIN_MEMORY_SIZE = (((int) (Runtime.getRuntime().totalMemory() * 0.5) / BLOCK_SIZE)
			* BLOCK_SIZE);

	public static void main(String[] args) {
		System.out.println("Main Memory Allocated " + (int) (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + "MB");
		System.out.println("Start Time : " + System.nanoTime());
		TpmmsService tpmms_Service = new TpmmsService();
		tpmms_Service.fileDetails("sample.txt");
		System.out.println("End Time : " + System.nanoTime());
		System.gc();
	}
}
