package com.two.pass.multiway.mergesort;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileService {
	private int readCount;
	private int writeCount;

	public void readInputFile(final FileChannel fileChannel, final ByteBuffer inputBuffer) {
		inputBuffer.clear();
		try {
			final int bytesRead = fileChannel.read(inputBuffer);
			readCount = readCount + bytesRead / Constants.BLOCK_SIZE;
			if (bytesRead % Constants.BLOCK_SIZE != 0) {
				readCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		inputBuffer.flip();
	}

	public int getReadCount() {
		return readCount;
	}

	public void writeOutputFile(final FileChannel fileChannel, final ByteBuffer outputBuffer) {
		try {
			final int byteWrites = fileChannel.write(outputBuffer);
			writeCount = writeCount + byteWrites / Constants.BLOCK_SIZE;
			if (byteWrites % Constants.BLOCK_SIZE != 0) {
				writeCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		outputBuffer.clear();
	}

	public int getWriteCount() {
		return writeCount;
	}
}
