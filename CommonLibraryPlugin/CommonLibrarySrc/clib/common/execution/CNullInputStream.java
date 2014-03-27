package clib.common.execution;

import java.io.IOException;
import java.io.InputStream;

public class CNullInputStream extends InputStream {

	public static CNullInputStream INSTANCE = new CNullInputStream();

	private CNullInputStream() {
	}

	@Override
	public int read() throws IOException {
		return 0;
	}

}
