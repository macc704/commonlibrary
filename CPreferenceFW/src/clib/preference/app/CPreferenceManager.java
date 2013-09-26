package clib.preference.app;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import clib.common.filesystem.CFile;
import clib.common.table.CCSVFileIO;
import clib.preference.model.CPreferenceRepository;
import clib.preference.model.ICPreferenceCategory;
import clib.preference.view.CPreferenceFrame;
import clib.view.windowmanager.CWindowCentraizer;

public class CPreferenceManager {

	private CFile file;

	private CPreferenceRepository repository = new CPreferenceRepository();
	private List<ICPreferenceCategory> categories = new ArrayList<ICPreferenceCategory>();

	public CPreferenceManager(CFile file) {
		this.file = file;
		loadFromFile();
	}

	public void putCategory(ICPreferenceCategory category) {
		category.setRepository(this.repository);
		categories.add(category);
		category.load();
	}

	public List<ICPreferenceCategory> getCategories() {
		return categories;
	}

	public void saveToFile() {
		String[][] data = new String[repository.size()][2];
		int i = 0;
		for (String key : repository.keys()) {
			String value = repository.get(key);
			data[i][0] = key;
			data[i][1] = value;
			i++;
		}
		CCSVFileIO.save(data, file);
	}

	public void loadFromFile() {
		repository.clear();
		String[][] data = CCSVFileIO.load(file);
		for (String[] line : data) {
			if (line.length == 2) {
				String key = line[0];
				String value = line[1];
				repository.put(key, value);
			}
		}
	}

	public void openPreferenceFrame() {
		CPreferenceFrame frame = new CPreferenceFrame(this);
		frame.setTitle("Preference");
		frame.setModal(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(true);
		frame.setBounds(100, 100, 400, 300);
		CWindowCentraizer.centerWindow(frame);

		for (ICPreferenceCategory category : categories) {
			category.load();
		}
		frame.setVisible(true);
	}
}
