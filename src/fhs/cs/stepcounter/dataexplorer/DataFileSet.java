package fhs.cs.stepcounter.dataexplorer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/***
 * This class loads csv data and corresponding metadata files from specified
 * paths.
 * 
 * @author David
 *
 */
public class DataFileSet {
	private static final boolean DEBUG = false;
	private static final String DEFAULT_METADATA_FILE = "data_description.txt";
	private static final String FILE_SEPARATION_SRING = "*********";
	private static final String NEW_ATTRIBUTE_STRING = "-";
	private static final String NEW_ATTRIBUTE_END_STRING = ":";
	private ArrayList<DataFile> dataFiles;

	public DataFileSet() {
		dataFiles = new ArrayList<DataFile>();
	}

	public void addDataFrom(String path) {
		addDataFrom(path, DEFAULT_METADATA_FILE);
	}

	public void addDataFrom(String path, String nameOfMetaDataFile) {
		path = cleanPath(path);

		// ********** load metadata file ***********
		debug("Loading data from: " + path);
		String metadatafile = readFileAsString(path + nameOfMetaDataFile);
		if (metadatafile == null || metadatafile.equals("")) {
			System.err.println("error loading: " + (path + nameOfMetaDataFile));
			return;
		}

		// ********** parse metadata file ***********
		String[] lines = metadatafile.split("\n");
		debug("Metadata file has " + lines.length + " lines");

		// ********** find indexes for file divisions ********
		ArrayList<Integer> fileDivisionIndexes = new ArrayList<Integer>();
		int linenum = 0;
		fileDivisionIndexes.add(-1);
		for (String line : lines) {
			if (line.startsWith(FILE_SEPARATION_SRING)) {
				if (linenum == 0) {
					fileDivisionIndexes.clear();
					fileDivisionIndexes.add(0);
				} else {
					fileDivisionIndexes.add(linenum);
				}
			}
			linenum++;
		}
		if (fileDivisionIndexes.get(fileDivisionIndexes.size() - 1) != lines.length - 1) {
			fileDivisionIndexes.add(lines.length);
		}

		// ********* load each file *************
		for (int i = 0; i < fileDivisionIndexes.size() - 1; i++) {
			int start = fileDivisionIndexes.get(i);
			int end = fileDivisionIndexes.get(i + 1);

			HashMap<String, String> metadata = getMetaDataFor(start + 1, end - 1, lines);
			debug(
					"Processing file: " + metadata.get("filename") + " with " + metadata.size() + " attributes.");
			processFile(path, metadata);
		}
	}

	private HashMap<String, String> getMetaDataFor(int start, int end, String[] lines) {
		HashMap<String, String> metadata = new HashMap<String, String>();

		for (int i = start; i <= end; i++) {
			String line = lines[i];

			if (line.startsWith(NEW_ATTRIBUTE_STRING)) {
				int attributeStart = line.indexOf(NEW_ATTRIBUTE_STRING);
				int attributeEnd = line.indexOf(NEW_ATTRIBUTE_END_STRING);
				if (start == -1 || end == -1 || end <= start) {
					System.err.println("Bad format on line: " + i);
					System.err.println(line);
					continue;
				}

				String key = line.substring(attributeStart + 1, attributeEnd).trim();
				String value = line.substring(attributeEnd + 1).trim();

				if (!key.equals("")) {
					debug("adding attribute: " + key + " : " + value);
					metadata.put(key, value);
				}
			}
		}

		return metadata;
	}

	private void processFile(String path, HashMap<String, String> metadata) {
		String filename = metadata.get("filename");
		String steps = metadata.get("steps");

		if (filename == null) {
			System.err.println("no filename attribute");
			return;
		}
		if (steps == null) {
			System.err.println("no steps attribute");
			return;
		}

		// load csv file.
		CSVData csvdata = CSVData.createDataSet(path + filename, 0);

		// create data object.
		DataFile datafile = new DataFile(csvdata);
		datafile.addAttributes(metadata);

		// add it to list.
		dataFiles.add(datafile);
	}

	private String cleanPath(String path) {
		path = path.trim();
		if (!path.endsWith("/"))
			path = path + "/";
		return path;
	}

	public int size() {
		return this.dataFiles.size();
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

	public DataFile get(int i) {
		if (i < 0 || i >= dataFiles.size())
			return null;
		return dataFiles.get(i);
	}
	
	private static void debug(String string) {
		if (DEBUG) {
			System.err.println(string);
		}
	}
}