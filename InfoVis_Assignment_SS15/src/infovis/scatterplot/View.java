package infovis.scatterplot;

import infovis.debug.Debug;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

public class View extends JPanel {
	    private Model model = null;
	    private Rectangle2D markerRectangle = new Rectangle2D.Double(0,0,0,0); 
	    private Rectangle2D rectangle = new Rectangle2D.Double(100, 0, 0, 0);   
	 	private Rectangle2D sRectangle = new Rectangle2D.Double(0, 0, 20, 20);
	 	private ArrayList<Data> newDataList  = new ArrayList<Data>();
	 	private double chartWidth = 700;
	 	private double chartHeight = 700;
		private int markerWidth = 0;
		private int markerHeight = 0;
	 	private int mouseX  = 0;
	 	private int mouseY  = 0;
	 	private boolean select = false;

		public Rectangle2D getMarkerRectangle() {
			return markerRectangle;
		}
		 
		@Override
		public void paint(Graphics g) {

			Graphics2D g2D = (Graphics2D) g;

			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2D.clearRect(0, 0, getWidth(), getHeight());

			double dimension = model.getDim();
			rectangle.setRect(100, 0 , dimension * 100, dimension * 100);
			g2D.draw(rectangle);
			
			//Setting up the mouse marker (rectangle area)
			//The x and y coordinates are defined by mouseX and mouseY to markerWidth and markerHeight
			//The marker's grid color is also defined here
			markerRectangle.setRect(mouseX, mouseY, markerWidth, markerHeight);
			Color color = Color.GRAY;
			Color c = null;
			g2D.setColor(c);
			g2D.draw(markerRectangle);
			//Meant to be the measurement to draw the labels from the grid
			int rx = 0, ry = 0;
			int cx = 100, cy = 0;
			//Get the labels of the chart
	        for (String l : model.getLabels()) {
				Debug.print(l);
				Debug.print(",  ");
				Debug.println("");
				//Setting up the distance and color of labels from scatterplot chart
				g2D.setColor(Color.BLACK);
				rx += 100;
				g2D.drawString(l, rx + 20, ry + 15);
				g2D.drawLine(rx, ry, rx, 700);
				cy += 100;
				g2D.drawLine(cx, cy, 800, cy);
				g2D.drawString(l, cx - 50, cy - 50);
			}
			for (Range range : model.getRanges()) {
				Debug.print(range.toString());
				Debug.print(",  ");
				Debug.println("");
			}
			for (Data d : model.getList()) {
				Debug.print(d.toString());
				Debug.println("");
			}
			for (Data newd : newDataList) {
				g2D.setColor(newd.getColor());
				g2D.fill(newd.getShape());
			}
	        if  (select == false) {
				newScatter();
			}
			
		}
		//To get the marker grid's weight and height
		public void setMarkerDimension(int h, int w) {
			this.markerWidth = w;
			this.markerHeight = h;
		}
		//To set the marker's position
		public void setMarker (int x, int y) {
			this.mouseX = x;
			this.mouseY = y;
		}

		ArrayList<Data> newDataList() {
			return newDataList;
		}

		public void setModel(Model model) {
			this.model = model;
		}

		public void setSelect (boolean b) {
			select = b;
		}

		public boolean getSelect() {
			return select;
		}

		public Iterator<Data> iterator() {
			return newDataList.iterator();
		}
		//FUnction that works to compute new scatter whenever a new area/dimension of marker is set
		public void newScatter() {
			ArrayList <Range> range = model.getRanges();
			sRectangle = new Rectangle2D.Double(0,0, 5, 5); 		
			double rectangleH = rectangle.getHeight();
			double rectangleW  = rectangle.getWidth();

			for (int i = 0; i < model.getDim(); i++) {
				for (int j = 0; j < model.getDim(); j++) {
					double xMax = range.get(j).getMax();
					double xMin = range.get(j).getMin();
					double yMax = range.get(i).getMax();
					double yMin = range.get(i).getMin();
						for (Data d : model.getList()) {
							double Vi = ((d.getValue(i) - yMin)) / (yMax - yMin);
							double Vj = ((d.getValue(j) - xMin)) / (xMax - xMin);
							double Pi = (Vi + i) * (rectangleW) / (model.getDim());
							double Pj = (Vj + j) * (rectangleH) / (model.getDim());
							int dataID = d.getID();
							String dataLabel = d.getLabel();
							sRectangle = new Rectangle2D.Double(100 + Pi, Pj, 3, 3);
							newDataList.add(new Data(d.getValues(), dataID, dataLabel, sRectangle));
						}
					}
				}
		}

		public void setRect(double height, double width) {
			chartWidth = width;
			chartHeight = height;
		}

}