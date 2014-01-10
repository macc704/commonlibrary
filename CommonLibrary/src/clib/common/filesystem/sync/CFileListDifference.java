package clib.common.filesystem.sync;

import java.io.Serializable;

public class CFileListDifference implements Serializable {

	private static final long serialVersionUID = 1L;

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
