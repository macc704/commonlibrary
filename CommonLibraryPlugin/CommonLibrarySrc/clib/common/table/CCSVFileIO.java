/*
 * CCSVFileReader.java
 * Created on 2011/11/12
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.common.table;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import clib.common.filesystem.CFile;

/**
 * @author macchan
 */
public class CCSVFileIO {

	public static String[][] load(CFile file) {
		try {
			String text = file.loadText();
			CSVParser parser = new CSVParser(new StringReader(text));
			String[][] table = parser.getAllValues();
			if (table == null) {
				return new String[0][0];
			}
			return table;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static List<String[]> loadAsList(CFile file) {
		String[][] table = load(file);
		List<String[]> newTable = new ArrayList<String[]>();
		for (String[] record : table) {
			newTable.add(record);
		}
		return newTable;
	}

	public static List<List<String>> loadAsListList(CFile file) {
		String[][] table = load(file);
		List<List<String>> newTable = new ArrayList<List<String>>();
		for (String[] record : table) {
			newTable.add(Arrays.asList(record));
		}
		return newTable;
	}

	public static void save(String[][] table, CFile file) {
		StringWriter writer = new StringWriter();
		CSVPrinter printer = new CSVPrinter(writer);
		printer.println(table);
		file.saveText(writer.toString());
	}

	public static void saveByList(List<String[]> table, CFile file) {
		save(table, file);
	}

	public static void save(List<String[]> table, CFile file) {
		int size = table.size();
		String[][] array = new String[size][];
		for (int i = 0; i < size; i++) {
			array[i] = table.get(i);
		}
		save(array, file);
	}

	public static void saveByListList(List<List<String>> table, CFile file) {
		List<String[]> newTable = new ArrayList<String[]>();
		for (List<String> record : table) {
			String[] array = new String[record.size()];
			record.toArray(array);
			newTable.add(array);
		}
		saveByList(newTable, file);
	}
}
