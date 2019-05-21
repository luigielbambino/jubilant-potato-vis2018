package infovis.paracoords;

import infovis.scatterplot.Data;
import infovis.scatterplot.Model;
import infovis.scatterplot.Range;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class View extends JPanel {
	private Model model = null;
	private static final Data Null = null;
	private Line2D.Double parallelLine = new Line2D.Double(100, 100, 100, 600);
	private Line2D.Double dLine = new Line2D.Double(0, 0, 0, 0);
	private Rectangle2D markerRectangle = new Rectangle2D.Double(0, 0, 0, 0);
	private int markerWidth = 0;
	private int markerHeight = 0;
	private int mouseX = 0;
	private int mouseY = 0;
	private ArrayList<Data> dAttributes = new ArrayList<Data>();
	private double x1 = 100;
	private double y1 = 100;
	private double x2 = x1;
	private double y2 = 500;
	private boolean reset = true;
	private ArrayList<Data> dList = new ArrayList<Data>();
	private double [][] dValues;
	private boolean drawLines;

	@Override
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.clearRect(0, 0, getWidth(), getHeight());

		if(drawLines == true) {
			makeLines();
			drawLines = false;
		}

		markerRectangle.setRect(mouseX, mouseY, markerWidth, markerHeight);
		Color color = Color.GRAY;
		Color c = null;
		g2D.setColor(c);
		g2D.draw(markerRectangle);

		if(reset) {
			makeAttributes();
			reset = false;
		}
		for(int j = 0; j < dAttributes.size(); j++) {
			Data newData = null;
			newData = this.dAttributes.get(j);
			String label = newData.getLabel();
			int strX = (int)(newData.getLine().getX1());
			int strY = (int)(newData.getLine().getY1());
			g2D.setColor(Color.BLUE);
        	g2D.drawString(label, strX-20, strY);
        	g2D.setColor(newData.getColor());
			g2D.draw(newData.getLine());
		}
		Data newd = Null;
		for (int i = 0; i < dList.size(); i++) {
			newd = dList.get(i);
			dValues = newd.getXY();
			double pX1 = 0;
			double pX2 = 0;
			double pY1 = 0;
			double pY2 = 0;
			for(int j = 0; j < model.getDim() - 1; j++) {
				pX1 = dValues[j][0];
				pY1 = dValues[j][1] + 100;
				pX2 = dValues[j + 1][0];
				pY2 = dValues[j + 1][1] + 100;
				dLine.setLine(pX1, pY1, pX2, pY2);
				g2D.setColor(newd.getColor());
				g2D.draw(dLine);
			}
		}
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void makeLines() {
		ArrayList<Range> range = model.getRanges();
		for(Data d : model.getList()) {
			double x1Line = 100;
			double dValues[][] = new double[d.getValues().length][d.getValues().length];
			int dataID = d.getID();
			String dLabel = d.getLabel();

			for(int i = 0; i < model.getDim(); i++) {
				double max = range.get(i).getMax();
				double min = range.get(i).getMin();
				double vi = ((d.getValue(i) - min)) / (max - min);
				double yP = (vi) * (2800) / (model.getDim());
				double xP = x1Line;
				dValues[i][0] = xP;
				dValues[i][1] = yP;
				x1Line += 100;
			}
			dList.add(new Data(dValues, dataID, dLabel));
		}
	}

	public Rectangle2D getMarkerRectangle() {
		return markerRectangle;
	}

	public void setMarkerDimension(int h, int w) {
		this.markerHeight = h;
		this.markerWidth = w;
	}

	public void setMarker (int x, int y) {
		this.mouseX = x;
		this.mouseY = y;
	}

	public void drawLines(boolean b) {
		this.drawLines = b;
	}

	public void makeAttributes() {
		for(String l : model.getLabels()) {
			x2 = x1;
			int dataID = Model.generateNewID();
			dAttributes.add(new Data(dataID, x1, y1, x2, y2, l));
			x1 += 100;
		}
	}

	public void setLine(double x) {
		x1 = x;
	}

	public ArrayList<Data> getDataAttribute() {
		return dAttributes;
	}

	public ArrayList<Data> getDataList() {
		return dList;
	}

	public double[][] getDataValues() {
		return dValues;
	}
	
}
