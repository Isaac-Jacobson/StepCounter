package fhs.cs.stepcounter.dataexplorer;
import java.util.HashMap;

/***
 * Represents a data file containing sensor data from a step-counting trial
 * along with meta-data including information about the subject, how many steps
 * the file represents, and so on.
 * 
 * @author David
 */
public class DataFile {
	private CSVData data;
	private HashMap<String, String> metaData;

	public DataFile(CSVData csvdata) {
		this.data = csvdata;
		metaData = new HashMap<String, String>();
	}

	/**
	 * Return an array of all keys to the meta-data map. This allows someone to
	 * see what they can request with the getMetaData() method.
	 * 
	 * @return string array of all keys to pieces of metadata.
	 */
	public String[] getAttributes() {
		return metaData.keySet().toArray(new String[0]);
	}

	/**
	 * Wrapper for metadata map. Returns the value corresponding to input key.
	 * 
	 * @return value corresponding to input key or null if entry not present.
	 */
	public String getMetaData(String key) {
		return metaData.get(key);
	}

	/**
	 * return csvData object
	 * 
	 * @return csvDat object
	 */
	public CSVData getData() {
		return data;
	}

	/**
	 * Wrapper for metadata map. Add new key-value pair to metadata. Returns
	 * true if successful, false if empty input.
	 * 
	 * @param key
	 *            data description (e.g. "phone_model" or "steps")
	 * @param value
	 *            value for that attribute (e.g. "iPhone 6" or "22")
	 * @return true if successful, false if empty input
	 */
	public boolean addMetaData(String key, String value) {
		if (key == null || value == null || key.equals("") || value.equals(""))
			return false;
		metaData.put(key, value);
		return true;
	}

	/**
	 * Copy all entries from input map to this object's attribute map
	 * 
	 * @param metadata2
	 */
	public void addAttributes(HashMap<String, String> metadata2) {
		for (String key : metadata2.keySet()) {
			metaData.put(key, metadata2.get(key));
		}
	}
	
	public String toString() {
		return mapToString("\n", ": ");
	}
	
	private String mapToString(String nextItem, String keyValSeparator) {
		StringBuilder b = new StringBuilder();
		for (String key : metaData.keySet()) {
			String val = metaData.get(key);
			b.append(key + keyValSeparator + val + nextItem);
		}
		
		return b.toString();
	}
}