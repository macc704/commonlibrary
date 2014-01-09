package clib.common.filesystem.sync;

public class CFileListDifference {
	public enum Kind {
		CREATED, UPDATED, REMOVED
	};

	private Kind kind;
	private String path;

	public CFileListDifference(Kind kind, String path) {
		this.kind = kind;
		this.path = path;
	}

	public Kind getKind() {
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
