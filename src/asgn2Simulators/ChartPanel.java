package asgn2Simulators;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

@SuppressWarnings("serial")
public class ChartPanel extends JPanel {
	
	private String title;
	
	TimeSeries vehTotal = new TimeSeries("Total Vehicles");
	TimeSeries parkedTotal = new TimeSeries("Parked Vehicles");
	TimeSeries carTotal = new TimeSeries("Total Cars"); 
	TimeSeries scarTotal = new TimeSeries("Small Cars");
	TimeSeries mcTotal = new TimeSeries("MotorCycles");
	TimeSeries disatisfied = new TimeSeries("Disatisfied Customers");
	TimeSeries archived = new TimeSeries("Archived Vehicles");
	TimeSeries queue = new TimeSeries("Queued Vehicles");

	public ChartPanel(String title) {
		super();
		this.title = title;
		final TimeSeriesCollection dataset = createTimeSeriesData(); 
        JFreeChart chart = createChart(dataset);
        this.add(new org.jfree.chart.ChartPanel(chart), BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout());
        this.add(btnPanel, BorderLayout.SOUTH);
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
		tsc.addSeries(mcTotal);
		tsc.addSeries(scarTotal);
		tsc.addSeries(queue);
		tsc.addSeries(archived);
		tsc.addSeries(disatisfied);
		return tsc; 
	}
	
	public void addData(String str){
		String data [] = str.split(":");
		String que = "";
		for (int i = 0; i < data[23].length(); i++){
			char c = data[23].charAt(i);
			if (!Character.isDigit(c)){
				break;
			}
			que += data[23].charAt(i);
		}
		data[23]=data[23];
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(2014,0,1,6,Integer.parseInt(data[0]));
        Date timePoint = cal.getTime();
        
		vehTotal.add(new Minute(timePoint),Integer.parseInt(data[2]));
		parkedTotal.add(new Minute(timePoint),Integer.parseInt(data[5]));
		carTotal.add(new Minute(timePoint),Integer.parseInt(data[8]));
		scarTotal.add(new Minute(timePoint),Integer.parseInt(data[11]));
		mcTotal.add(new Minute(timePoint),Integer.parseInt(data[14]));
		disatisfied.add(new Minute(timePoint),Integer.parseInt(data[17]));
		archived.add(new Minute(timePoint),Integer.parseInt(data[20]));
		queue.add(new Minute(timePoint),Integer.parseInt(que));
		
	}
	
    /**
     * Helper method to deliver the Chart - currently uses default colours and auto range 
     * @param dataset TimeSeriesCollection for plotting 
     * @returns chart to be added to panel 
     */
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            title, "hh:mm", "Vehicles", dataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setAutoRange(true);
        return result;
    }

}
