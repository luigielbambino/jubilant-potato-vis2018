package infovis.paracoords;

import infovis.scatterplot.Data;
import infovis.scatterplot.Model;
import infovis.scatterplot.Range;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Shape;
import javax.swing.JPanel;

public class View extends JPanel {
	
	public int xOffset = 100;
	private int yOffset = 80;
	public int distance = 100;
	public int xInitPos = 0;
	public int xEndPos = 0;
	public int newX = 0;
	public int pInitX = 0;
	public int pInitY = 0;
	public int pEndX = 0;
	public int pEndY = 0;
	public int[] order = new int[] { 0, 1, 2, 3, 4, 5, 6 };
	public int moveAxis = -2;
	public ArrayList<Integer> list = new ArrayList<Integer>();
	private Model model = null;
	public boolean init = true;
	private int length = 400;
	private int border = 5;
	private Line2D.Double line;	

	@Override
	public void paint(Graphics g) {
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.clearRect(0, 0, getWidth(), getHeight());
		
		if (init == true) {
			for (int i = 0; i < order.length; i++) {
				list.add(order[i]);
			}
			init = false;
		}
		
		if(moveAxis==-1){
			for (int i = 0; i < model.getLabels().size(); i++) {
				g2D.setStroke(new BasicStroke(3));
				g2D.setColor(Color.darkGray);
				g.drawLine(xOffset + i * distance, yOffset, xOffset + i * distance, yOffset + length);
			}
		}else{
			for (int i = 0; i < model.getLabels().size(); i++) {
				g2D.setStroke(new BasicStroke(3));
				if (list.get(i)==moveAxis) {
					g2D.setColor(Color.blue);
					g.drawLine(newX, yOffset, newX, yOffset + length);
				} else {
					g2D.setColor(Color.darkGray);
					g.drawLine(xOffset + i * distance, yOffset, xOffset + i * distance, yOffset + length);
				}
			}
		}
		
		if(moveAxis==-1){
			for (int i = 0; i < model.getLabels().size(); i++) {
				int width = g.getFontMetrics().stringWidth(model.getLabels().get(list.get(i)));
				g2D.setColor(Color.darkGray);
				g.drawString(model.getLabels().get(list.get(i)), i * distance + xOffset - width / 2, 60);
			}
		}else{
			for (int i = 0; i < model.getLabels().size(); i++) {
				int width = g.getFontMetrics().stringWidth(model.getLabels().get(list.get(i)));
				g2D.setStroke(new BasicStroke(3));
				if (list.get(i)==moveAxis) {
				g2D.setColor(Color.blue);
					g.drawString(model.getLabels().get(list.get(i)), newX - width / 2, 60);
				} else {
					g2D.setColor(Color.darkGray);
					g.drawString(model.getLabels().get(list.get(i)), i * distance + xOffset - width / 2, 60);
				}
			}
		}
		
		for (int i = 0; i < model.getList().size(); i++) {

			double x1 = 0, y1 = 0;
			double xPoint[] = new double[model.getLabels().size()];
			double yPoint[] = new double[model.getLabels().size()];

			boolean check = false;
			for (int j = 0; j < model.getLabels().size(); j++) {
				double rangeLow = model.getRanges().get(list.get(j)).getMin();
				double rangeHigh = model.getRanges().get(list.get(j)).getMax();
				double initValue = model.getList().get(i).getValue(list.get(j));

				y1 = yOffset + (length - border)
						- (length - 2 * border) * (initValue - rangeLow) / (rangeHigh - rangeLow);
				
				if (list.get(j) == moveAxis) {
					x1 = newX;
				} else {
					x1 = xOffset + j * distance;
				}

				xPoint[j] = x1;
				yPoint[j] = y1;
			}
			for (int j = 0; j < model.getLabels().size() - 1; j++) {
				line = new Line2D.Double(xPoint[j], yPoint[j], xPoint[j + 1], yPoint[j + 1]);
				if (line.intersectsLine(pInitX, pInitY, pEndX, pEndY)
						|| line.intersectsLine(pInitX, pEndY, pEndX, pInitY)) {
					check = true;
				}
			}
			if (check == true) {
				for (int j = 0; j < model.getLabels().size() - 1; j++) {
					line = new Line2D.Double(xPoint[j], yPoint[j], xPoint[j + 1], yPoint[j + 1]);
					g2D.setColor(Color.green);
					g2D.setStroke(new BasicStroke(1));
					g2D.draw(line);
				}
			} else {
				for (int j = 0; j < model.getLabels().size() - 1; j++) {
					line = new Line2D.Double(xPoint[j], yPoint[j], xPoint[j + 1], yPoint[j + 1]);
					g2D.setColor(Color.darkGray);
					g2D.setStroke(new BasicStroke(1));
					g2D.draw(line);
				}
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
	
}
