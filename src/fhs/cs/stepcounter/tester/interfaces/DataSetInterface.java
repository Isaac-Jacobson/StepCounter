package fhs.cs.stepcounter.interfaces;

public interface DataSetInterface {
	public String[] getColumnNames();
	public double[] getColumn(String name);
	public double[][] getDataForColumns(String[] columnNames);
}