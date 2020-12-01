package fhs.cs.stepcounter.dataexplorer;

import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.math.plot.Plot2DPanel;

import fhs.cs.stepcounter.NaiveStepCounter;
import processing.core.PApplet;

public class TestDataExplorer {
	private static final String DATA_PATH = "alejandro";

	public static void main(String[] args) {
		DataFileSet dataset = new DataFileSet();

		dataset.addDataFrom(DATA_PATH);
		System.out.println("Loaded " + dataset.size() + " files.");

		for (int i = 0; i < dataset.size(); i++) {
			System.out.println("Displaying File #" + i);
			System.out.println("\n\n");
			System.out.println();

			DataFile currentDataFile = dataset.get(i);
			System.out.println(currentDataFile);

			CSVData csvdata = currentDataFile.getData();

			// ------------ Fetch data to graph --------------------------------------
			NaiveStepCounter counter = new NaiveStepCounter(csvdata);
			
			double[] data1 = counter.getDataForGraphing();
			Plot2DPanel plot = new Plot2DPanel();

			// ------------ Add data to plot to the PlotPanel --------------------------
			plot.addLinePlot("magnitudes", data1);
			
			// --== Uncomment 2 lines below to graph your predicted step points ==--
			// double[][] stepVals = buildStepPointArray(counter);
			// plot.addScatterPlot("steps", stepVals);

			// put the PlotPanel in a JFrame, as a JPanel
			JFrame frame = new JFrame(currentDataFile.getMetaData("filename"));
			frame.setBounds(600, 50, 800, 600);
			// frame.setSize(800, 600);
			frame.setContentPane(plot);
			frame.setVisible(true);
			
		}
	}
	
	private static double[][] buildStepPointArray(NaiveStepCounter counter) {
		int steps = counter.countSteps();
		int[] stepIndexes = counter.getStepIndexes();
		double[] data1 = counter.getDataForGraphing();
		
		double[][] stepVals = new double[steps][2];
		for (int j = 0; j < stepVals.length; j++) {
			stepVals[j][0] = stepIndexes[j];
			stepVals[j][1] = data1[stepIndexes[j]];
		}
		
		return stepVals;
	}
}