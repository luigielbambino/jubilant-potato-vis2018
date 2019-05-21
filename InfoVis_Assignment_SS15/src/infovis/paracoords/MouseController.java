package infovis.paracoords;

import infovis.scatterplot.Data;
import infovis.scatterplot.Model;
import infovis.paracoords.View;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class MouseController implements MouseListener, MouseMotionListener {
	private View view = null;
	private Model model = null;
	Shape currentShape = null;
	private static final Data Null = null;

	private int markerX = 0;
	private int markerY = 0;
	private boolean markerReady = true;

	private ArrayList<Data> dAttributes = new ArrayList<Data>();
	private ArrayList<Data> dList = new ArrayList<Data>();
	private double[][] dValues;
	Data drag = null;
	Data replace = null;

	private double y1 = 0;
	private double y2 = 0;
	private double x1 = 0;
	private double x2 = 0;
	private int mDirection = 0;
	private int mDirectionData = 0;
	private int index = 0;
	private double indexX = 0;
	private double leftX = 0;
	private double rightX = 0;
	private boolean change = false;

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Data dPoints = null;
		for(int i = 0; i < view.getDataList().size(); i++) {
			dPoints = view.getDataList().get(i);
			dPoints.setColor(Color.GREEN);
		}
		view.repaint();
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		view.setMarkerDimension(0, 0);
		view.setMarker(0, 0);
		int x = e.getX();
		int y = e.getY();
		this.dAttributes = view.getDataAttribute();

		for(int i = 0; i < this.dAttributes.size(); i++) {
			double getX1;
			double getX2;
			double getY1;
			double getY2;
			getX1 = this.dAttributes.get(i).getLine().getX1();
			getX2 = this.dAttributes.get(i).getLine().getX2();
			getY1 = this.dAttributes.get(i).getLine().getY1();
			getY2 = this.dAttributes.get(i).getLine().getY2();

			if (Line2D.ptSegDist(getX1, getY1, getX2, getY2, x, y) == 0.0) {
				System.out.println(getX1);
				drag = this.dAttributes.get(i);
				indexX = getX1;
				index = i;
				this.x1 = getX1;
				this.x2 = getX2;
				this.y1 = getY1;
				this.y2 = getY2;

				if(i == 0) {
					rightX = this.dAttributes.get(i + 1).getLine().getX1();
					leftX = 0;
				}
					else if(i == this.dAttributes.size() - 1) {
						rightX = this.dAttributes.get(i).getLine().getX1() + 100;
						leftX = this.dAttributes.get(i - 1).getLine().getX1();
					}
						else {
							rightX = this.dAttributes.get(i + 1).getLine().getX1();
							leftX = this.dAttributes.get(i - 1).getLine().getX1();
						}
						System.out.println("[L]: " + leftX + "[R]: " + rightX );
						markerReady = false;
					}
					else {
						markerX = x;
						markerY = y;
						view.setMarker(x, y);
					}
		}
		view.repaint();
	}

	public void mouseReleased(MouseEvent e) {
		markerReady = true;
		drag = null;

	}

	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		dValues = view.getDataValues();

		if(drag != null) {
			Data dPoints = null;
			Data lines = null;
			drag.changeLine(x, y1, x, y2);
			this.dAttributes = view.getDataAttribute();

			if(x < indexX) {
				mDirection = -1;
				mDirectionData = 1;
			}
				else {
					mDirection = 1;
					mDirectionData = -1;
				}
				for(int i = 0; i < this.dAttributes.size(); i++) {
					lines = this.dAttributes.get(i);
					double getX1;
					double getX2;
					double getY1;
					double getY2;
					getX1 = lines.getLine().getX1();
					getX2 = lines.getLine().getX2();
					getY1 = lines.getLine().getY1();
					getY2 = lines.getLine().getY2();

					if((getX1 == leftX & getX1 > x) || (getX1 == rightX & getX1 < x)) {
						change = true;
						lines.setColor(Color.BLUE);
						replace = lines;
						double indexXData[][];
						System.out.println(replace.getLine().getX1());
						for(int j = 0; j < view.getDataList().size(); j++) {
						dPoints = view.getDataList().get(j);
						indexXData = dPoints.getXY();
							if (indexXData[index + mDirection][0] == replace.getLine().getX1()) {
								double lineY1 = indexXData[index + mDirection][1];
								double lineY2 = indexXData[index][1];
								dPoints.setX(index, x1);
								dPoints.setY(index, lineY1);
								dPoints.setX(index + mDirection, x);
								dPoints.setY(index + mDirection, lineY1);
							}
						}
						replace.changeLine(x1, y1, x2, y2);
						this.dAttributes.set(i, drag);
						this.dAttributes.set(i + mDirectionData, replace);
						x1 = drag.getLine().getX1();
						x2 = drag.getLine().getX2();
						y1 = drag.getLine().getY1();
						y2 = drag.getLine().getY2();

						System.out.println("End.");
					}
					else {
						change = false;
					}
					if(change) {
						System.out.println(i);
						if (i == 0) {
							leftX = 0;
							rightX = this.dAttributes.get(i + 1).getLine().getX1();
						}
						else if(i == this.dAttributes.size() - 1) {
							leftX = this.dAttributes.get(i - 1).getLine().getX1();
							rightX = this.dAttributes.get(i).getLine().getX1() + 100;
						}
							else {
								leftX = this.dAttributes.get(i - 1).getLine().getX1();
								rightX = this.dAttributes.get(i + 1).getLine().getX1();
							}
					}
					if ( (x < indexX)) {
						mDirection = -1;
						mDirectionData = 1;
						System.out.println("[L]");
					}
						else {
							mDirection = 1;
							mDirectionData = -1;
							System.out.println("[R]");
						}
					
					double indexXData[][];
					
					for (int i = 0; i < view.getDataList().size(); i++) {
						dPoints = view.getDataList().get(i);
						indexXData = dPoints.getXY();
						if(indexXData[index][0] == indexX) {
							dPoints.setX(index, x);
						}
					}
					indexX = x;
				}
			int w1 = x - markerX;
			int h1 = y - markerY;

			if(markerReady) {
				Data points = null;

				if(w1 < 0  && h1 < 0) {
					view.setMarker(x, y);
					int w = markerX - x;
					int h = markerY - y;

					view.setMarkerDimension(h, w);
					drawLine(h, w);
				}
				if(w1 < 0 && h1 > 0) {
					view.setMarker(x, markerY);
					int w = markerX - x;
					int h = y - markerY;

					view.setMarkerDimension(h, w);
					drawLine(h, w);
				}
				if(w1 > 0 && h1 < 0) {
					view.setMarker(markerX, y);
					int w = x - markerX;
					int h = markerY - y;

					view.setMarkerDimension(h, w);
					drawLine(h, w);
				}
				if(w1 > 0 && h1 > 0) {
					int w = w1;
					int h = h1;
					view.setMarkerDimension(h, w);
					drawLine(h, w);
				}
			}
		}
		view.repaint();
	}

	public void mouseMoved(MouseEvent e) {

	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void drawLine(int h, int w) {
		Data dPoints = null;
		double indexXData[][];
		boolean changeLineColor = false;
		for (int i = 0; i < view.getDataList().size(); i++) {
			dPoints = view.getDataList().get(i);
			indexXData = dPoints.getXY();
			Attributes:
			for(int j = 0; j < indexXData.length - 1; j++) {
				double pX1 = indexXData[j][0];
				double pY1 = indexXData[j][1] + 100;
				double pX2 = indexXData[j + 1][0];
				double pY2 = indexXData[j+1][1] + 100;
				Line2D.Double lineTest = new Line2D.Double(pX1, pY1, pX2, pY2);
				if(lineTest.intersects(view.getMarkerRectangle().getX(), view.getMarkerRectangle().getY(), w, h)) {
					changeLineColor = true;
					break Attributes;
				}
				else {
					changeLineColor = false;
				}
			}
			if(changeLineColor) {
				dPoints.setColor(Color.GRAY);
			}
				else {
					dPoints.setColor(Color.RED);
				}
		}
	}

}
