package scheduler;

import java.util.Hashtable;

public interface GraphPlotterInterface {

	/**
	 * Plots a default bars chart with the requested parameters and saves it in 
	 * the project's root folder.
	 * 
	 * @param xAxis		name of the x axis
	 * @param yAxis		name of the y axis
	 * @param values	table containing the labels for each element in x axis and their values
	 */
	public void plotBarsGraph(String xAxis, String yAxis, Hashtable<String,Double> values);
	/**
	 * Plots a default pie graph with the given table. The values must be integers and the sum
	 * of all of them must be 100.
	 * 
	 * @param values		table containing the different elements and their values
	 */
	public void plotPieGraph(Hashtable<String,Integer> values);
}
