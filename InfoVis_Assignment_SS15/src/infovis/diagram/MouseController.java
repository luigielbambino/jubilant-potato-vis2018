 package infovis.diagram;

import infovis.debug.Debug;
import infovis.diagram.elements.DrawingEdge;
import infovis.diagram.elements.Edge;
import infovis.diagram.elements.Element;
import infovis.diagram.elements.GroupingRectangle;
import infovis.diagram.elements.None;
import infovis.diagram.elements.Vertex;
import infovis.diagram.layout.Fisheye;
import java.awt.geom.Rectangle2D;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MouseController implements MouseListener,MouseMotionListener {
	 private Model model;
	 private View view;
	 private Element selectedElement = new None();
	 private double mouseOffsetX;
	 private double mouseOffsetY;
	 private boolean edgeDrawMode = false;
	 private DrawingEdge drawingEdge = null;
	 private boolean fisheyeMode;
	 private GroupingRectangle groupRectangle;
	 
	 private boolean hitOverview = false; // overview
	 private boolean hitMarker = false; // marker
	 
	 private Fisheye fisheye = new Fisheye(); // fisheye
	 private Model fishModel; //fisheye
	 
	/*
	 * Getter And Setter
	 */
	 public Element getSelectedElement(){
		 return selectedElement;
	 }
    public Model getModel() {
		return model;
	}
	public void setModel(Model diagramModel) {
		this.model = diagramModel;
	}
	public View getView() {
		return view;
	}
	public void setView(View diagramView) {
		this.view = diagramView;
	}
	/*
     * Implements MouseListener
     */
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		double scale = view.getScale();
		
		
		
		if (e.getButton() == MouseEvent.BUTTON3){
			/*
			 * add grouped elements to the model
			 */
			Vertex groupVertex = (Vertex)getElementContainingPosition(x/scale,y/scale);
			for (Iterator<Vertex> iter = groupVertex.getGroupedElements().iteratorVertices();iter.hasNext();){
				model.addVertex(iter.next());
			}
			for (Iterator<Edge> iter = groupVertex.getGroupedElements().iteratorEdges();iter.hasNext();){
				model.addEdge(iter.next());
			}
			/*
			 * remove elements
			 */
			List<Edge> edgesToRemove = new ArrayList<Edge>();
			for (Iterator<Edge> iter = model.iteratorEdges(); iter.hasNext();){
				Edge edge = iter.next();
				if (edge.getSource() == groupVertex || edge.getTarget() == groupVertex){
					edgesToRemove.add(edge);
				}
			}
			model.removeEdges(edgesToRemove);
			model.removeElement(groupVertex);
			
		}
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		double scale = view.getScale();
		
		int xOver=(int) (view.overviewX * view.overviewScale * scale);
		int yOver=(int) (view.overviewY * view.overviewScale * scale);
		int wOver=(int) (view.overviewWidth * view.overviewScale * scale);
		int hOver=(int) (view.overviewHeight * view.overviewScale * scale);
		
		int xMark=(int) (view.markerX * view.overviewScale * scale);
		int yMark=(int) (view.markerY * view.overviewScale * scale);
		int wMark=(int) (view.markerWidth * view.overviewScale * scale);
		int hMark=(int) (view.markerHeight * view.overviewScale * scale);
		
		if(x>xOver && x<xOver+wOver && y>yOver && y<yOver+hOver){
			hitOverview=true;
		}
		
		if(x>xMark && x<xMark+wMark && y>yMark && y<yMark+hMark){
			hitMarker=true;
			System.out.println("hitMarker: " + hitMarker);
		}
		
		System.out.println("marker: " + xMark + "-" + yMark +"-" + wMark + "-" + hMark);
		
		
	   if (edgeDrawMode){
			drawingEdge = new DrawingEdge((Vertex)getElementContainingPosition(x/scale,y/scale));
			model.addElement(drawingEdge);
		} else if (fisheyeMode){
			/*
			 * do handle interactions in fisheye mode
			 */
			view.repaint();
		} else {
			
			selectedElement = getElementContainingPosition(x/scale,y/scale);
			/*
			 * calculate offset
			 */
			mouseOffsetX = x - selectedElement.getX() * scale ;
			mouseOffsetY = y - selectedElement.getY() * scale ;	
		}
		
	}
	public void mouseReleased(MouseEvent arg0){
		int x = arg0.getX();
		int y = arg0.getY();
		
		hitOverview=false; // overview
		hitMarker=false; // marker
		
		if (drawingEdge != null){
			Element to = getElementContainingPosition(x, y);
			model.addEdge(new Edge(drawingEdge.getFrom(),(Vertex)to));
			model.removeElement(drawingEdge);
			drawingEdge = null;
		}
		if (groupRectangle != null){
		    Model groupedElements = new Model();
			for (Iterator<Vertex> iter = model.iteratorVertices(); iter.hasNext();) {
				Vertex vertex = iter.next();
				if (groupRectangle.contains(vertex.getShape().getBounds2D())){
					Debug.p("Vertex found");
					groupedElements.addVertex(vertex);	
				}
			}
			if (!groupedElements.isEmpty()){
				model.removeVertices(groupedElements.getVertices());
				
				Vertex groupVertex = new Vertex(groupRectangle.getCenterX(),groupRectangle.getCenterX());
				groupVertex.setColor(Color.ORANGE);
				groupVertex.setGroupedElements(groupedElements);
				model.addVertex(groupVertex);
				
				List<Edge> newEdges = new ArrayList(); 
				for (Iterator<Edge> iter = model.iteratorEdges(); iter.hasNext();) {
					Edge edge =  iter.next();
				    if (groupRectangle.contains(edge.getSource().getShape().getBounds2D()) 
				    	&& groupRectangle.contains(edge.getTarget().getShape().getBounds2D())){
				    		groupVertex.getGroupedElements().addEdge(edge);
                            Debug.p("add Edge to groupedElements");	
                            //iter.remove(); // Warum geht das nicht!
				    } else if (groupRectangle.contains(edge.getSource().getShape().getBounds2D())){
				    	groupVertex.getGroupedElements().addEdge(edge);
				    	newEdges.add(new Edge(groupVertex,edge.getTarget()));
				    } else if (groupRectangle.contains(edge.getTarget().getShape().getBounds2D())){
				    	groupVertex.getGroupedElements().addEdge(edge);
				    	newEdges.add(new Edge(edge.getSource(),groupVertex));
				    }
				}
				model.addEdges(newEdges);
				model.removeEdges(groupedElements.getEdges());
			}
			model.removeElement(groupRectangle);
			groupRectangle = null;
		}
		view.repaint();
	}
	
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		double scale = view.getScale();
		
		if(hitMarker==true){
			hitOverview=false;
			view.updateMarker(x, y);
			view.updateTranslation(x, y);
		}
		
		if(hitOverview==true){
			view.updateOverview(x, y);
		}
		
		/*
		 * Aufgabe 1.2
		 */
		if (fisheyeMode){
			/*
			 * handle fisheye mode interactions
			 */
			fisheye.setMouseCoords(x, y, view); // Fisheye. Normal coords
			fishModel=new Model();
			for(int i=0;i<model.getVertices().size();i++){
				fishModel.addVertex(new Vertex(model.getVertices().get(i)));
			}
			fishModel=fisheye.transform(fishModel, view);
			view.setModel(fishModel);
			
			view.repaint();
		} else if (edgeDrawMode){
			drawingEdge.setX(e.getX());
			drawingEdge.setY(e.getY());
		}else if(selectedElement != null){
			selectedElement.updatePosition((e.getX()-mouseOffsetX)/scale, (e.getY()-mouseOffsetY) /scale);
		}
		view.repaint();
	}
	public void mouseMoved(MouseEvent e) {
	}
	public boolean isDrawingEdges() {
		return edgeDrawMode;
	}
	public void setDrawingEdges(boolean drawingEdges) {
		this.edgeDrawMode = drawingEdges;
	}
	
	public void setFisheyeMode(boolean b) {
		fisheyeMode = b;
		if (b){
			Debug.p("new Fisheye Layout");
			/*
			 * handle fish eye initial call
			 */
			view.repaint();
		} else {
			Debug.p("new Normal Layout");
			view.setModel(model);
			view.repaint();
		}
	}
	
	/*
	 * private Methods
	 */
	private Element getElementContainingPosition(double x,double y){
		Element currentElement = new None();
		Iterator<Element> iter = getModel().iterator();
		while (iter.hasNext()) {
		  Element element =  iter.next();
		  if (element.contains(x, y)) currentElement = element;  
		}
		return currentElement;
	}
	
    
}
