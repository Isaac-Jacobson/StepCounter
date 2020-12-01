package fhs.cs.stepcounter.interfaces;

import fhs.cs.stepcounter.dataexplorer.CSVData;

public interface StepCounter {
	
	/***
	 * Return the number of steps represented by the data in CSVData object.
	 * 
	 * @param data
	 *            a CSVData object which is a wrapper for the raw sensor data.
	 *            Extract the specific data you want using the
	 *            .getDataForColumns method. You can specify column names to get
	 *            a 2d array of the data.
	 * @return the number of steps represented by the data. 
	 */
	public int countSteps();

	public void loadData(CSVData csvdata);
}