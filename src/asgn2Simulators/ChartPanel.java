package asgn2Simulators;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;

@SuppressWarnings("serial")
public class ChartPanel extends JPanel {
	
	private TimeSeries vehTotal = new TimeSeries("Total Vehicles");
	private TimeSeries parkedTotal = new TimeSeries("Parked Vehicles");
	private TimeSeries carTotal = new TimeSeries("Total Cars"); 
	private TimeSeries scarTotal = new TimeSeries("Small Cars");
	private TimeSeries mcTotal = new TimeSeries("MotorCycles");
	private TimeSeries queue = new TimeSeries("Queued Vehicles");
	private TimeSeries archived = new TimeSeries("Archived Vehicles");
	private TimeSeries disatisfied = new TimeSeries("Disatisfied Customers");
	private DefaultCategoryDataset finalData;
	
	private org.jfree.chart.ChartPanel mainChart;
	private org.jfree.chart.ChartPanel finalChart;
	
	private String title;
	private final String unitsLabel = "Vehicles";
	private final int total = 0;
	private final int dis = 1; 
	private boolean main;
	
	

	public ChartPanel(String title) {
		super();
		this.title = title;
		main = true;
		final TimeSeriesCollection dataset = createTimeSeriesData(); 
        JFreeChart chart = createChart(dataset);
        mainChart = new org.jfree.chart.ChartPanel(chart);
        
        finalData = new DefaultCategoryDataset();
        createFinalChart();
        
        this.add(mainChart, BorderLayout.CENTER);
	}
	
	
    /**
     * Private method creates the dataset. Lots of hack code in the 
     * middle, but you should use the labelled code below  
	 * @return collection of time series for the plot 
	 */
	private TimeSeriesCollection createTimeSeriesData() {
		TimeSeriesCollection tsc = new TimeSeriesCollection(); 
		
		//Collection
		tsc.addSeries(vehTotal);
		tsc.addSeries(parkedTotal);
		tsc.addSeries(carTotal);
		tsc.addSeries(scarTotal);
		tsc.addSeries(mcTotal);
		tsc.addSeries(queue);
		tsc.addSeries(archived);
		tsc.addSeries(disatisfied);
		return tsc; 
	}
	
	
	public void addData(String str){
		String data [] = str.split(":");
		String que = "";
		final int timeValue = 0;
		final int vehTotalValue = 2;
		final int parkedValue = 5;
		final int carsValue = 8;
		final int smallCarValue = 11;
		final int motorCycleValue = 14;
		final int disatisfiedValue = 17;
		final int archivedValue = 20;
		final int queueValue = 23;
		
		for (int i = timeValue; i < data[queueValue].length(); i++){
			char c = data[queueValue].charAt(i);
			if (!Character.isDigit(c)){
				break;
			}
			que += data[queueValue].charAt(i);
		}
		data[queueValue]=data[queueValue];
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(2014,timeValue,1,6,Integer.parseInt(data[timeValue]));
        Date timePoint = cal.getTime();
        
		vehTotal.add(new Minute(timePoint),Integer.parseInt(data[vehTotalValue]));
		parkedTotal.add(new Minute(timePoint),Integer.parseInt(data[parkedValue]));
		carTotal.add(new Minute(timePoint),Integer.parseInt(data[carsValue]));
		scarTotal.add(new Minute(timePoint),Integer.parseInt(data[smallCarValue]));
		mcTotal.add(new Minute(timePoint),Integer.parseInt(data[motorCycleValue]));
		disatisfied.add(new Minute(timePoint),Integer.parseInt(data[disatisfiedValue]));
		archived.add(new Minute(timePoint),Integer.parseInt(data[archivedValue]));
		queue.add(new Minute(timePoint),Integer.parseInt(que));
		
		finalData.clear();
    	finalData.setValue(vehTotal.getMaxY(), ("Total Vehicles "+vehTotal.getMaxY()), "");
		finalData.setValue(disatisfied.getMaxY(), ("Disatisifed Customers "+disatisfied.getMaxY()),"");
	}
	
	
	public void clearData(){
		vehTotal.clear();
		parkedTotal.clear();
		carTotal.clear();
		scarTotal.clear();
		mcTotal.clear();
		disatisfied.clear();
		archived.clear();
		queue.clear();
		finalData.clear();
	}
	
	
    /**
     * Helper method to deliver the Chart - currently uses default colours and auto range 
     * @param dataset TimeSeriesCollection for plotting 
     * @returns chart to be added to panel 
     */
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            title, "hh:mm", unitsLabel, dataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setAutoRange(true);
        
        result.getXYPlot().getRendererForDataset(dataset).setSeriesPaint(0, Color.BLACK);
        result.getXYPlot().getRendererForDataset(dataset).setSeriesPaint(1, Color.BLUE);
        result.getXYPlot().getRendererForDataset(dataset).setSeriesPaint(2, Color.CYAN);
        result.getXYPlot().getRendererForDataset(dataset).setSeriesPaint(3, Color.GRAY);
        result.getXYPlot().getRendererForDataset(dataset).setSeriesPaint(4, Color.DARK_GRAY);
        result.getXYPlot().getRendererForDataset(dataset).setSeriesPaint(5, Color.YELLOW);
        result.getXYPlot().getRendererForDataset(dataset).setSeriesPaint(6, Color.GREEN);
        result.getXYPlot().getRendererForDataset(dataset).setSeriesPaint(7, Color.RED);
        return result;
    }
    
    
    /**
     * Helper method to deliver the Chart - currently uses default colours and auto range 
     * @param dataset TimeSeriesCollection for plotting 
     * @returns chart to be added to panel 
     */
    private void createFinalChart() {
    	
        final JFreeChart result = ChartFactory.createBarChart((title + " Overview"),
        		"", unitsLabel, finalData, PlotOrientation.HORIZONTAL, true, true, false);
        
        CategoryPlot plot = (CategoryPlot) result.getPlot();

        plot.getRendererForDataset(finalData).setSeriesPaint(total, Color.BLACK);
        plot.getRendererForDataset(finalData).setSeriesPaint(dis, Color.RED);
        
        finalChart = new org.jfree.chart.ChartPanel(result);
    }
    
    
    public void switchGraph(){
    	if (main){
    		switchToFinal();
    	}else if (!main){
    		switchToMain();
    	}
		repaint();
    }
    
    
    private void switchToFinal(){
    	main = false;
    	this.remove(mainChart);
		this.add(finalChart, BorderLayout.CENTER);
    }
    
    
    private void switchToMain(){
    	main = true;
		this.remove(finalChart);
		this.add(mainChart, BorderLayout.CENTER);
    }

}
