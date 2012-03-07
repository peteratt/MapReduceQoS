package scheduler;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class GraphPlotter {
	
	// graph's filename. Saved in JPEG under project's root folder
	private String fileName;
	// graph's title
	private String graphTitle;
	
	
	public GraphPlotter(String fileName, String graphTitle){
		this.fileName = fileName;
		this.graphTitle = graphTitle;
	}
	
	/**
	 * Plots a default bars chart with the requested parameters and saves it in the project's root folder
	 * 
	 * @param xAxis		name of the x axis
	 * @param yAxis		name of the y axis
	 * @param values	table containing the labels for each element in x axis and their values
	 */
	public void plotBarsGraph(String xAxis, String yAxis, Hashtable<String,Double> values){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String xAxisLabel;
		double xAxisValue;
		
		Set<String> xAxisSet = values.keySet();
		Iterator<String> xAxisIterator = xAxisSet.iterator();
		
		while(xAxisIterator.hasNext()){
			xAxisLabel = xAxisIterator.next();
			xAxisValue = values.get(xAxisLabel);
			dataset.setValue(xAxisValue, yAxis, xAxisLabel);
		}
		
		JFreeChart chart = ChartFactory.createBarChart(graphTitle, xAxis, yAxis, dataset, 
				PlotOrientation.VERTICAL, false, true, false);
        try {
            ChartUtilities.saveChartAsJPEG(new File("./"+fileName+".jpg"), chart, 500, 300);
        } catch (IOException e) {
            System.err.println("Error creating graph");
        }
	}
	
	/**
	 * Plots a default pie graph with the given table
	 * 
	 * @param values		table containing the different elements and their values
	 */
	public void plotPieGraph(Hashtable<String,Integer> values){
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		String label;
		int labelValue;
		
		Set<String> labelSet = values.keySet();
		Iterator<String> labelIterator = labelSet.iterator();
		
		while(labelIterator.hasNext()){
			label = labelIterator.next();
			labelValue = values.get(label);
			pieDataset.setValue(label, labelValue);
		}
		
        JFreeChart chart = ChartFactory.createPieChart(graphTitle, pieDataset, true, true, false);
        
        try {
            ChartUtilities.saveChartAsJPEG(new File("./"+fileName+".jpg"), chart, 500, 300);
        } catch (Exception e) {
            System.out.println("Error creating graph");
        }
	}
}
