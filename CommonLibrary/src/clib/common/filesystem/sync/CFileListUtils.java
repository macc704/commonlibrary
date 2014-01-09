package clib.common.filesystem.sync;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import clib.common.filesystem.CDirectory;
import clib.common.filesystem.CFileSystem;

public class CFileListUtils {

	public static List<CFileListDifference> compare(CFileList master,
			CFileList copy) {
		List<CFileListDifference> differences = new ArrayList<CFileListDifference>();
		List<String> remainingPaths = copy.getPaths();
		for (String path : master.getPaths()) {
			if (!copy.hasPath(path)) {
				differences.add(new CFileListDifference(
						CFileListDifference.Kind.CREATED, path));
			} else {
				String masterSha1 = master.getSha1(path);
				String copySha1 = copy.getSha1(path);
				if (copySha1.equals(masterSha1)) {
					// OK
				} else {
					differences.add(new CFileListDifference(
							CFileListDifference.Kind.UPDATED, path));
				}
				remainingPaths.remove(path);
			}
		}
		// copyに残ったものが削除するもの
		for (String path : remainingPaths) {
			differences.add(new CFileListDifference(
					CFileListDifference.Kind.REMOVED, path));
		}
		return differences;
	}

	@Test
	public static void main(String[] args) {
		CDirectory base = CFileSystem.getExecuteDirectory().findDirectory(
				"testsrc/clib/common/filesystem/sync");
		CFileList test1 = new CFileList(base.findFile("list1.txt"));
		CFileList test2 = new CFileList(base.findFile("list2.txt"));
		System.out.println(compare(test2, test1));
		System.out.println(compare(test1, test2));
	}
}
