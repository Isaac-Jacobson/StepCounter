package fhs.cs.stepcounter.tester;

import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import fhs.cs.stepcounter.NaiveStepCounter;
import fhs.cs.stepcounter.dataexplorer.CSVData;
import fhs.cs.stepcounter.dataexplorer.DataFile;
import fhs.cs.stepcounter.dataexplorer.DataFileSet;
import fhs.cs.stepcounter.interfaces.StepCounter;

public class StepCountTester {
	private static final String DATA_PATH = "Dobervich/";

	public static void main(String[] args) {
		StepCounter counter = new NaiveStepCounter();

		DataFileSet dataset = new DataFileSet();
		dataset.addDataFrom(DATA_PATH);
		System.out.println("Loaded " + dataset.size() + " files.");

		System.out.println("Filename                        | Algorithm steps | Actual steps | Error");
		for (int i = 0; i < dataset.size(); i++) {
			DataFile dataFile = dataset.get(i);
			int numSteps = Integer.parseInt(dataFile.getMetaData("steps"));

			CSVData csvdata = dataFile.getData();
			counter.loadData(csvdata);
			int calculatedSteps = counter.countSteps();

			// TODO: use printf instead to format it nicely
			System.out.printf("%-32s| %-16d| %-13d| %-13d\n", dataFile.getMetaData("filename"), calculatedSteps, numSteps,
					(numSteps - calculatedSteps));
		}
	}

	public static void graphData(double[][] data) {
		Plot2DPanel plot = new Plot2DPanel();

		// add a line plot to the PlotPanel
		plot.addLinePlot("y", data);

		// put the PlotPanel in a JFrame, as a JPanel
		JFrame frame = new JFrame("Results");
		frame.setSize(800, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);
	}
}