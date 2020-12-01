package fhs.cs.stepcounter;
/***
 * Static methods to do things with 2d arrays.
 * 
 * @author David
 *
 */
public class ArrayHelper {

	/***
	 * Return the column at colIndex for data[][]
	 * 
	 * @param data
	 *            a 2d double array
	 * @param colIndex
	 *            index of column to be extracted
	 * @return a 1d array with column data or null if colIndex is out of bounds
	 */
	public static double[] extractColumn(double[][] data, int colIndex) {
		if (invalidCol(data, colIndex))
			return null;

		double[] col = new double[data.length];

		for (int i = 0; i < data.length; i++) {
			col[i] = data[i][colIndex];
		}

		return col;
	}

	/***
	 * Return an array from startrow to endrow of the column data at colIndex
	 * 
	 * @param data
	 *            the 2d double array to extract from
	 * @param colIndex
	 *            index of column to extract data from
	 * @param startrow
	 *            starting row to extract data
	 * @param endrow
	 *            ending row to extract data (note: this row is included in the
	 *            data)
	 * @return returns a 1d array of data from data[][] between startrow and
	 *         endrow from colIndex
	 */
	public static double[] extractPartialColumn(double[][] data, int colIndex, int startrow, int endrow) {
		if (invalidCol(data, colIndex) || invalidRow(data, startrow) || invalidRow(data, endrow) || startrow > endrow)
			return null;
		double[] col = new double[endrow - startrow + 1];

		for (int i = 0; i < col.length; i++) {
			col[i] = data[startrow + i][colIndex];
		}

		return col;
	}

	/***
	 * Extract multiple columns from data.
	 * 
	 * @param data
	 *            the 2d array to extract from
	 * @param colStartIndex
	 *            starting column index
	 * @param numberOfColumns
	 *            total number of columns to extract
	 * @return a new double[][] containing extracted data from data
	 */
	public static double[][] extractColumns(double[][] data, int colStartIndex, int numberOfColumns) {
		if (invalidCol(data, colStartIndex) || invalidCol(data, colStartIndex + numberOfColumns - 1))
			return null;

		double[][] col = new double[data.length][numberOfColumns];

		for (int i = 0; i < data.length; i++) {
			for (int j = colStartIndex; j < colStartIndex + numberOfColumns; j++)
				col[i][j - colStartIndex] = data[i][j];
		}

		return col;
	}

	/***
	 * Extract multiple columns from data between startrow and endrow.
	 * 
	 * @param data
	 *            the 2d array to extract from
	 * @param colStartIndex
	 *            starting column index
	 * @param numberOfColumns
	 *            total number of columns to extract
	 * @param startrow
	 *            starting row to include
	 * @param endrow
	 *            ending row to include
	 * @return a new double[][] containing data from data.
	 */
	public static double[][] extractPartialColumns(double[][] data, int colStartIndex, int numberOfColumns,
			int startrow, int endrow) {

		if (invalidCol(data, colStartIndex) || invalidCol(data, colStartIndex + numberOfColumns - 1)
				|| invalidRow(data, startrow) || invalidRow(data, endrow) || endrow > startrow)
			return null;

		double[][] col = new double[endrow - startrow + 1][numberOfColumns];

		for (int i = 0; i < col.length; i++) {
			for (int j = colStartIndex; j < colStartIndex + numberOfColumns; j++)
				col[i][j - colStartIndex] = data[startrow + i][j];
		}

		return col;
	}

	/***
	 * Extract columns whose indexes are contained in cols[].
	 * 
	 * @param data
	 *            array to extract data from.
	 * @param cols
	 *            list of column indexes to extract data from
	 * @return a new double[][] containing data from the columns listed in
	 *         cols[].
	 */
	public static double[][] extractColumns(double[][] data, int[] cols) {
		if (cols.length == 0)
			return null;

		double[][] col = new double[data.length][cols.length];

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < cols.length; j++) {
				int colIndex = cols[j];

				if (invalidCol(data, colIndex))
					return null;

				col[i][j] = data[i][colIndex];
			}
		}

		return col;
	}

	/***
	 * return a transposed copy of input array.
	 * 
	 * @param arr
	 *            array to transpose
	 * @return returns a transposed copy of input array.
	 */
	public static double[][] transposeArray(double[][] arr) {
		double[][] out = new double[arr[0].length][arr.length];

		for (int r = 0; r < arr.length; r++) {
			for (int c = 0; c < arr[0].length; c++) {
				out[c][r] = arr[r][c];
			}
		}

		return out;
	}

	/***
	 * display the entire contents of arr
	 * 
	 * @param arr
	 */
	public static void display(double[][] arr) {
		display(arr, arr.length);
	}

	/***
	 * display first n rows of arr
	 * 
	 * @param arr
	 *            array to display
	 * @param n
	 *            number of rows to display
	 */
	public static void display(double[][] arr, int n) {
		System.out.println("Rows: " + arr.length + " Cols: " + arr[0].length);

		for (int r = 0; r < n; r++) {
			for (int c = 0; c < arr[0].length; c++) {
				System.out.print(arr[r][c] + " ");
			}
			System.out.println();
		}
	}

	/***
	 * Combine an array of objects into a string with a joining string. The
	 * opposite of "split"
	 * 
	 * @param arr
	 *            array of objects to join
	 * @param joiningString
	 *            string inserted between each element's .toString() to form
	 *            final return string
	 * @return a string consisting of each element of arr's .toString()
	 *         separated by joiningString
	 */
	public static String join(Object[] arr, String joiningString) {
		StringBuilder b = new StringBuilder();

		for (int i = 0; i < arr.length - 1; i++) {
			b.append(arr[i].toString() + joiningString);
		}

		b.append(arr[arr.length - 1]);

		return b.toString();
	}

	public static double[] downSample(double[] data, int saveEveryNth) {
		double[] output = new double[data.length / saveEveryNth];

		for (int i = 0; i < output.length; i++) {
			output[i] = data[i * saveEveryNth];
		}

		return output;
	}

	/***
	 * Checks if colIndex is out of bounds in data[][]
	 * 
	 * @param arr
	 *            2d array to check bounds for
	 * @param colIndex
	 *            index to check if out of bounds
	 * @return true iff colIndex is a valid column index for data.
	 */
	public static boolean invalidCol(double[][] arr, int colIndex) {
		return (colIndex < 0 || colIndex >= arr.length);
	}

	/***
	 * Checks if rowIndex is out of bounds in data[][]
	 * 
	 * @param arr
	 *            2d array to check bounds for
	 * @param rowIndex
	 *            index to check if out of bounds
	 * @return true iff rowIndex is a valid row index for data.
	 */
	public static boolean invalidRow(double[][] arr, int rowIndex) {
		return (rowIndex < 0 || rowIndex >= arr[0].length);
	}

	/***
	 * Combine several 1d arrays together into a 2d array as if each 1d array was a column.
	 * @param columns the 1d arrays to combine
	 * @return a 2d array with each parameter as a column in the output
	 */
	public static double[][] combineAsColumns(double[]...columns) {
		// TODO: should we make a fresh copy?
		return transposeArray(columns);
	}
}