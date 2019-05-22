package infovis.scatterplot;

import java.util.ArrayList;
import java.util.Iterator;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseController implements MouseListener, MouseMotionListener {

	private Model model = null;
	private View view = null;
	private static final Data Null = null;
	private Data scatter_data 	= null;
	private ArrayList<Data> rectangleDataPoints  = new ArrayList<Data>();
	private int x = 0;
	private int y = 0;
	private int markerX = 0;
	private int markerY = 0;

	private int initX = 0;
	private int initY = 0;
	private int currentX = 0;
	private int currentY = 0;
	private double mouseX  = 0;
	private double mouseY  = 0;

	public void mouseClicked(MouseEvent arg0) {
		view.setSelect(false);
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}
	//Listener to when the mouse is pressed, the pointer's location will determine the coordinate of marker grid
	public void mousePressed(MouseEvent arg0) {
		//Iterator<Data> iter = model.iterator();
		//view.getMarkerRectangle().setRect(x,y,w,h);
		//view.repaint();
		int x = arg0.getX();
		int y = arg0.getY();
		markerX = x;
		markerY = y;

		view.setMarker(x, y);
		view.setSelect(false);
		view.repaint();
	}
	//Setting up the marker grid's dimension to 0,0 so it doesn't show
	public void mouseReleased(MouseEvent arg0) {
		view.setMarkerDimension(0,0);
		view.setMarker(0,0);
	}
	//Function to when mouse is dragged and therefore will set the marker grid's area
	public void mouseDragged(MouseEvent arg0) {
		//currentX = arg0.getX();
		//currentY = arg0.getY();
		
		int x = arg0.getX();
		int y = arg0.getY();
		int i = x - markerX;
		int j = y - markerY;
		//Pointer at upper left
		if(i < 0 && j < 0) {
			view.setMarker(x, y);
			int w = markerX - x;
			int h = markerY - y;
			view.setMarkerDimension(h, w);
		}
		//Pointer at lowe left
		if(i < 0 && j > 0) {
			view.setMarker(x, markerY);
			int w = markerX - x;
			int h = y - markerY;
			view.setMarkerDimension(h,w);
		}
		//Pointer at upper right
		if(i > 0 && j < 0) {
			view.setMarker(markerX, y);
			int w = x - markerX;
			int h = markerY - y;
			view.setMarkerDimension(h, w);
		}
		//Pointer at lower right
		if(i > 0 && j > 0) {
			view.setMarkerDimension(j, i);
		}

		view.setSelect(true);
		rectangleDataPoints = get2Position();

		if(rectangleDataPoints != null) {
			System.out.println("");
			for (Data rectangleData: rectangleDataPoints) {
				ArrayList<Data> startData = view.newDataList();
				for (Data newd : startData) {
					if (newd.getLabel() == rectangleData.getLabel()) {
						newd.setColor(Color.GREEN);
					}
				}
			}
		}
		view.repaint();
	}

	public void mouseMoved(MouseEvent arg0) {
	}

	public void setModel(Model model) {
		this.model  = model;	
	}

	public void setView(View view) {
		this.view  = view;
	}

	public Model getModel() {
		return this.model;
	}
	//Function to compute the position of mouse's pointer
	private Data getPosition() {
		Data startData = null;
		Iterator<Data> iter = view.iterator();
		while (iter.hasNext()) {	
		  Data data =  iter.next();
		  double dx = data.getShape().getX();
		  double dy = data.getShape().getY();
		  
		  if(view.getMarkerRectangle().contains(dx, dy)) {
			  startData = data;  
		  }
			
		}
		return startData;
	}

	private ArrayList<Data> get2Position(){
	 	ArrayList<Data> startData = view.newDataList();
		ArrayList<Data> rectangleData  = new ArrayList<Data>();
	 	
		for (Data newd : startData) {
			  double dx = newd.getShape().getX();
			  double dy = newd.getShape().getY();

			  if(view.getMarkerRectangle().contains(dx, dy)) {
				  rectangleData.add(newd);
			  }
		}
		return rectangleData;
	}

}