package clib.common.filesystem.sync;

public class CFileListDifference {
	public enum Kind {
		CREATED, UPDATED, REMOVED
	};

	private Kind kind;
	private String path;

	public CFileListDifference(Kind command, String path) {
		this.kind = command;
		this.path = path;
	}

	public Kind getCommand() {
		return kind;
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return kind.toString() + ":" + path;
	}
}
