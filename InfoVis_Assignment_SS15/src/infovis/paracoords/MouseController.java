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
	public Shape currentShape = null;
	
	private static final Data Null = null;
	
	private int r = 5;
	private int initAxes;

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		int x=e.getX();
		view.xInitPos = x - r;
		view.xEndPos = x + r;
		view.newX = x;
		
		for(int i = 0; i< model.getLabels().size();i++){
			if(view.xOffset + i * view.distance > view.xInitPos && view.xOffset + i * view.distance < view.xEndPos) {
				view.moveAxis = view.list.get(i);
			}
		}
		view.repaint();
		initAxes=(x-50)/110;
	}

	public void mouseReleased(MouseEvent e) {
		view.moveAxis=-1;
		int x=e.getX();
		int finalAxes=(x-50)/110;
		
		if(finalAxes<initAxes){
			int temp=view.list.get(initAxes);
			view.list.remove(initAxes);
			view.list.add(finalAxes+1, temp);
		}else{
			int temp=view.list.get(initAxes);
			view.list.remove(initAxes);
			view.list.add(finalAxes, temp);
		}
		view.repaint();

	}

	public void mouseDragged(MouseEvent e) {
		int x=e.getX();
		view.newX=x;
		view.repaint();
	}

	public void mouseMoved(MouseEvent e) {
		int x=e.getX();
		int y=e.getY();
		
		view.pInitX = x - r;
		view.pInitY = y - r;
		view.pEndX = x + r;
		view.pEndY = y + r;
		
		view.repaint();
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

}
