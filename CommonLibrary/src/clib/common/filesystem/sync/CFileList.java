package clib.common.filesystem.sync;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import clib.common.filesystem.CDirectory;
import clib.common.filesystem.CFile;
import clib.common.filesystem.CFileElement;
import clib.common.filesystem.CFileFilter;
import clib.common.filesystem.CFileSystem;

/*
 * ファイルパスの集合を表現するクラス 
 * パス，SHA1ハッシュのマップを持つ．
 * 作成時，デフォルトで.から始まるファイルをはじくようになっている．
 */
public class CFileList implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String DELIMITER = ":";

	/* Path, SHA1のリスト */
	private Map<String, String> map = new LinkedHashMap<String, String>();

	public CFileList(CDirectory basedir) {
		build(basedir, CFileFilter.IGNORE_BY_NAME_FILTER(".*"));
	}

	public CFileList(CDirectory basedir, CFileFilter filter) {
		build(basedir, filter);
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

	public void build(CDirectory basedir, CFileFilter filter) {
		map.clear();
		build0(basedir, basedir, filter);
	}

	private void build0(CDirectory basedir, CDirectory dir, CFileFilter filter) {
		for (CFileElement child : dir.getChildren()) {
			if (!filter.accept(child)) {
				continue;
			}
			if (child.isFile()) {
				String sha1 = CDigest.getSha1((CFile) child);
				String relativePath = child.getRelativePath(basedir).toString();
				map.put(relativePath, sha1);
			} else {
				build0(basedir, (CDirectory) child, filter);
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
		// CDirectory dir =
		// CFileSystem.getExecuteDirectory().findDirectory("src");
		CDirectory dir = CFileSystem.getExecuteDirectory().findDirectory(
				"testdata/filelist");
		{
			CFileList list = new CFileList(dir);
			CFile out = CFileSystem.getExecuteDirectory().findOrCreateFile(
					"list1.txt");
			list.serialize(out);
		}

		{
			CFileList list = new CFileList(dir, CFileFilter.ALL_ACCEPT_FILTER());
			CFile out = CFileSystem.getExecuteDirectory().findOrCreateFile(
					"list2.txt");
			list.serialize(out);
		}

		{
			CFileList list = new CFileList(dir,
					CFileFilter.IGNORE_BY_NAME_FILTER(".*", "*.class", "*.xml"));
			CFile out = CFileSystem.getExecuteDirectory().findOrCreateFile(
					"list3.txt");
			list.serialize(out);
		}
	}
}
