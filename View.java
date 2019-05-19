package infovis.scatterplot;

import infovis.debug.Debug;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;


import javax.swing.JPanel;

public class View extends JPanel {
	    private Model model = null;
	    private Rectangle2D markerRectangle = new Rectangle2D.Double(0,0,0,0); 
	    private Rectangle2D rectangle = new Rectangle2D.Double(100, 0, 0, 0);   
	 	private Rectangle2D sRectangle = new Rectangle2D.Double(0, 0, 20, 20);   
	 	private double mouseX  = 0;
		private double mouseY  = 0;
		private int MarkerH = 0;
	 	private int MarkerW = 0;
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

			double dimension = model.getDimension():
			rectangle.setRect(100, 0 , dimension * 100, dimension * 100);
			g2D.draw(rectangle);
			
			Graphics2D marker = (Graphics2D)g.create();
			markerRectangle.setColor(Color.LIGHT_GRAY);
			markerRectangle.draw(markerRectangle);

			int rxv = 0, ry = 0;
			int cx = 100, cy = 0;
			ArrayList <Range> range = model.getRanges();
			sRectangle = new Rectangle2D.Double(0,0, 5, 5); 		
			double rect_height = chartRect.getHeight();
			double rect_width  = chartRect.getWidth();

			if  (select == false) {
				
				new_make_scatter();
				
			}

			for (int i = 0; i< model.getDim(); i++) {
	
				for (int j = 0; j< model.getDim(); j++) {
					double x_max =range.get(j).getMax();
					double x_min =range.get(j).getMin();
					double y_max =range.get(i).getMax();
					double y_min =range.get(i).getMin();

						for (Data d : model.getList()){
							double Vi = ((d.getValue(i) - y_min)) / (y_max - y_min);
							double Vj = ((d.getValue(j) - x_min)) / (x_max - x_min);
							double Pi = (Vi + i) * (rect_width)/(model.getDim());
							double Pj = (Vj + j) * (rect_height)/(model.getDim());
							int data_id = d.getId();
							String data_lable = d.getLabel();
							ScatRect 		 = new Rectangle2D.Double(100+Pi,Pj,3, 3);
							newDataList.add(new Data(d.getValues(),data_id, data_lable,ScatRect));
						}
					}
			
					}

	        for (String l : model.getLabels()) {
				Debug.print(l);
				Debug.print(",  ");
				Debug.println("");
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
	        
			
		}

		public void setMarkerDimension(int h, int w) {
			this.MarkerH = h;
			this.MarkerW = w;
		}

		public void setMarker (int x, int y) {
			this.mouseX = x;
			this.mouseY = y;
		}

		private ArrayList<Data> newDataList  = new ArrayList<Data>();

		public void setModel(Model model) {
			this.model = model;
		}
}
