package clib.common.filesystem.sync;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import clib.common.filesystem.CDirectory;
import clib.common.filesystem.CFile;
import clib.common.filesystem.CFileElement;
import clib.common.filesystem.CFileSystem;

public class CFileList {

	public static final String DELIMITER = ":";

	private Map<String, String> map = new LinkedHashMap<String, String>();

	public CFileList(CDirectory basedir) {
		build(basedir);
	}

	public CFileList(CFile file) {
		unserialize(file);
	}

	public List<String> getPaths() {
		return new ArrayList<String>(map.keySet());
	}

	public boolean hasPath(String path) {
		return map.containsKey(path);
	}

	public String getSha1(String path) {
		return map.get(path);
	}

	public void build(CDirectory basedir) {
		map.clear();
		build0(basedir, basedir);
	}

	private void build0(CDirectory basedir, CDirectory dir) {
		for (CFileElement child : dir.getChildren()) {
			if (child.getNameByString().startsWith(".")) {
				continue;
			}
			if (child.isFile()) {
				String sha1 = CDigest.getSha1((CFile) child);
				String relativePath = child.getRelativePath(basedir).toString();
				map.put(relativePath, sha1);
			} else {
				build0(basedir, (CDirectory) child);
			}
		}
	}

	public void serialize(CFile file) {
		file.saveText("");
		for (String key : map.keySet()) {
			String sha1 = map.get(key);
			file.appendText(key + DELIMITER + sha1);
		}
	}

	public void unserialize(CFile file) {
		map.clear();
		for (String line : file.loadTextAsList()) {
			String[] tokens = line.split(DELIMITER);
			map.put(tokens[0], tokens[1]);
		}
	}

	@Test
	public static void main(String[] args) throws Exception {
		CDirectory dir = CFileSystem.getExecuteDirectory().findDirectory("src");
		CFileList list = new CFileList(dir);

		CFile out = CFileSystem.getExecuteDirectory().findOrCreateFile(
				"list2.txt");
		list.serialize(out);
	}
}
