package fhs.cs.stepcounter.dataexplorer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import fhs.cs.stepcounter.ArrayHelper;
import fhs.cs.stepcounter.interfaces.DataSetInterface;

public class CSVData implements DataSetInterface {
	private static boolean DEBUG = false;
	private static final String SAVE_SUFFIX = "-out";
	private static final String DELIMETER = ",";

	private double[][] rawData;
	private String[] headerNames;
	private String filepath;

	private CSVData(double[][] data, String[] headers, String filepath) {
		rawData = data;
		headerNames = headers;
		this.filepath = filepath;

		cleanStrings(headerNames);
	}

	private void cleanStrings(String[] headerNames) {
		for (int i = 0; i < headerNames.length; i++) {
			headerNames[i] = headerNames[i].toLowerCase().trim();
		}
	}

	public static CSVData createDataSet(String filepath, int linesToSkip) {
		debug("Reading file: " + filepath);

		String data = normalizeLineBreaks(readFileAsString(filepath));
		String[] lines = data.split("\n");

		debug("Reading " + lines.length + " total lines from file");
		debug("Using index " + (linesToSkip) + " as header row");

		String headerLine = lines[linesToSkip];
		debug("Headers: " + headerLine);

		String[] headers = headerLine.split(",");
		debug("Parsed header line into: " + headers.length + " total columns");

		int startColumn = 0;
		return createDataSet(filepath, linesToSkip + 1, headers, startColumn);
	}

	private static String normalizeLineBreaks(String s) {
		return s.replace("\r\n", "\n").replace('\r', '\n');
	}

	public static String readFileAsString(String filepath) {
		debug("Reading file: " + filepath);
		ClassLoader classLoader = DataFileSet.class.getClassLoader();
		File file = new File(classLoader.getResource(filepath).getFile());

		// Read File Content
		String content = "";
		try {
			content = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			System.err.println("FILE NOT FOUND: " + filepath);
			e.printStackTrace();
		}

		return content;
	}

	public static CSVData createDataSet(String filepath, int linesToSkip, String[] columnHeaders, int startColumn) {
		debug("Reading file: " + filepath);

		String data = normalizeLineBreaks(readFileAsString(filepath));
		String[] lines = data.split("\n");

		debug("Reading " + lines.length + " total lines from file");

		int numColumns = columnHeaders.length;
		debug("Reading " + numColumns + " total columns");

		int startRow = linesToSkip;

		// create storage for data
		double[][] numdata = new double[lines.length - linesToSkip][numColumns];

		for (int r = startRow; r < lines.length; r++) {
			String line = lines[r];
			String[] coords = line.split(",");

			for (int j = startColumn; j < numColumns; j++) {
				if (coords[j].endsWith("#"))
					coords[j] = coords[j].substring(0, coords[j].length() - 1);
				double val = 0;
				try {
					val = Double.parseDouble(coords[j]);
				} catch (Exception e) {
					System.err.println("Warning: invalid number format");
				}
				numdata[r - 1][j - startColumn] = val;
			}
		}
		
		return new CSVData(numdata, columnHeaders, filepath);
	}

	public double[][] getAllData() {
		return rawData;
	}

	public String[] getColumnNames() {
		return this.headerNames;
	}

	private String dataToString() {
		StringBuilder b = new StringBuilder();

		b.append(join(headerNames, ", "));
		for (int i = 0; i < rawData.length - 1; i++) {
			b.append(join(rawData[i], ", ") + "\n");
		}

		b.append(join(rawData[rawData.length - 1], ", "));

		return b.toString();
	}

	public void saveToFile(String path, String name) {
		String datastring = dataToString();

	}

	private String join(double[] data, String joiner) {
		StringBuilder b = new StringBuilder();

		for (int i = 0; i < data.length - 1; i++) {
			double o = data[i];

			b.append(("" + o) + joiner);
		}

		b.append(data[data.length - 1]);

		return b.toString();
	}

	private String join(Object[] data, String joiner) {
		StringBuilder b = new StringBuilder();

		for (int i = 0; i < data.length - 1; i++) {
			Object o = data[i];

			b.append(o.toString() + joiner);
		}

		b.append(data[data.length - 1]);

		return b.toString();
	}

	public static void writeDataToFile(String filePath, String data) {
		File outFile = new File(filePath);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
			writer.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void debug(String string) {
		if (DEBUG) {
			System.err.println(string);
		}
	}

	/***
	 * Return the index for column whose header is name. If no column name
	 * exactly matches the input, it returns the first column whose name
	 * contains the argument.
	 * 
	 * @param name
	 *            name of column to find index for
	 * @return the index corresponding to column named, name
	 */
	public int getIndexForColumn(String name) {
		name = name.toLowerCase();

		for (int i = 0; i < this.headerNames.length; i++) {
			if (headerNames[i].equalsIgnoreCase(name)) {
				return i;
			}
		}

		debug("No column name matches: " + name);
		debug("Searching for columns that contain the words from " + name);

		String[] words = name.split(" ");
		for (int i = 0; i < this.headerNames.length; i++) {
			//debug("CHECKING HEADER: " + headerNames[i]);
			int count = 0;
			for (String searchTerm : words) {
				//debug("**** TEST: " + headerNames[i] + " contains " + searchTerm);
				if (headerNames[i].contains(searchTerm)) {
					count++;
				}
			}
			if (count == words.length) {
				//debug("matched for column: " + headerNames[i]);
				return i;
			}
		}

		return -1;
	}

	@Override
	public double[] getColumn(String name) {
		int index = getIndexForColumn(name);
		if (index == -1)
			return null;

		return ArrayHelper.extractColumn(rawData, index);
	}

	@Override
	public double[][] getDataForColumns(String[] columnNames) {
		int[] cols = new int[columnNames.length];

		for (int i = 0; i < cols.length; i++) {
			cols[i] = getIndexForColumn(columnNames[i]);
			if (cols[i] == -1) {
				System.err.println("no column named: " + columnNames[i]);
				return null;
			}
		}

		return ArrayHelper.extractColumns(rawData, cols);
	}

	public int getNumberOfColumns() {
		return rawData[0].length;
	}
	
	public int getNumberOfRows() {
		return rawData.length;
	}
}