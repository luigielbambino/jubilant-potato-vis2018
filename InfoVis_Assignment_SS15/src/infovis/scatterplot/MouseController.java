package infovis.scatterplot;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseController implements MouseListener, MouseMotionListener {

	private Model model = null;
	private View view = null;
	private static final Data Null = null;
	private Data scatter_data 	= null;
	private ArrayList<Data> rectangleData  = new ArrayList<Data>();
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
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		//Iterator<Data> iter = model.iterator();
		//view.getMarkerRectangle().setRect(x,y,w,h);
		//view.repaint();
		x = arg0.getX();
		y = arg0.getY();
		markerX = x;
		markerY = y;

		view.setMarkerPosition(x, y);
		view.selectMouse(false);
		view.repaint();
	}

	public void mouseReleased(MouseEvent arg0) {
		view.markerDimension(0,0);
		view.markerPosition(0,0);
	}

	public void mouseDragged(MouseEvent arg0) {
		currentX = arg0.getX();
		currentY = arg0.getY();
		
		int i = x - markerX;
		int j = y - markerY;
		
		if(i < 0 && j < 0) {
			view.setMarkerPosition(x, y);
			int w = markerX - x;
			int h = y - markerY;
			view.setMarkerDimension(h, w);
		}

		if(i > 0 && j < 0) {
			view.setMarkerPosition(markerX, y);
			int w = x - markerX;
			int h = markerY - y:
			view.setMarkerDimension(h, w);
		}

		if(i > 0 && j > 0) {
			view.setMarkerDimension(j, i);
		}

		view.selectMouse(true);
		rectangleDataPoints = get2Position();
		if(rectangleDataPoints != null) {
			System.out.println("")
			for (Data rectangleData: rectangleDataPoints) {
				ArrayList<Data> startData = view.updateList();
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

	private Data getPosition(){
		Data startData = null;
		Iterator<Data> iter = view.iterator();
		while (iter.hasNext()) {	
		  Data data =  iter.next();
		  double dx = data.getShape().getX();
		  double dy = data.getShape().getY();
		  
		  if (view.getMarkerRectangle().contains(dx, dy)) {
			  startData = data;  
		  }
			
		}
		return startData;
	}

	private ArrayList<Data> get2Position(){
	 	ArrayList<Data> startData = view.get_new_List();
		ArrayList<Data> rectangleData  = new ArrayList<Data>();
	 	
		for (Data newd : startData) {
			  double dx = newd.getShape().getX();
			  double dy = newd.getShape().getY();

			  if (view.getMarkerRectangle().contains(dx, dy)) {
				  rectangleData.add(newd);
			  }
		}
		return rectangleData;
	}

}
