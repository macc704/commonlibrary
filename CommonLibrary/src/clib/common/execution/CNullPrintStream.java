package clib.common.execution;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class CNullPrintStream extends PrintStream {
	public static CNullPrintStream INSTANCE = new CNullPrintStream();

	private CNullPrintStream() {
		super(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
			}
		});
	}
}
