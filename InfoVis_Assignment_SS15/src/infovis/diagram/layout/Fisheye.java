package infovis.diagram.layout;

import infovis.debug.Debug;
import infovis.diagram.Model;
import infovis.diagram.View;
import infovis.diagram.elements.Edge;
import infovis.diagram.elements.Vertex;

import java.util.Iterator;

/*
 * 
 */

public class Fisheye implements Layout{
	
	public int focusX;
	public int focusY;
	

	public void setMouseCoords(int x, int y, View view) {
		// TODO Auto-generated method stub
		focusX=(int)(x/view.getScale()-view.getTranslateX());
		focusY=(int)(y/view.getScale()-view.getTranslateY());
	}

	public Model transform(Model model, View view) {
		// TODO Auto-generated method stub
		
		double dMaxX, dMaxY, dNormX, dNormY, dNorX,dNorY;
		double fishX,fishY;
		int d=2;
		int boundaryX=view.getWidth();
		int boundaryY=view.getHeight();	
		//System.out.println(boundaryX+"+"+boundaryY);
		
		int length=model.getVertices().size();
		for(int i=0; i<length;i++){
			
			double width=60;
			double height=20;

			
			double oldX=model.getVertices().get(i) .getCenterX();
			double oldY=model.getVertices().get(i).getCenterY();
			
			if(oldX>focusX){
				dMaxX=boundaryX-focusX;
			}else{
				dMaxX=0-focusX;
			}
			dNormX=oldX-focusX;
			
			if(oldY>focusY){
				dMaxY=boundaryY-focusY;
			}else{ 
				dMaxY=0-focusY;
			}
			dNormY=oldY-focusY;
		
			fishX=focusX+dMaxX*(d+1)*(dNormX/dMaxX)/(d*(dNormX/dMaxX)+1);
			fishY=focusY+dMaxY*(d+1)*(dNormY/dMaxY)/(d*(dNormY/dMaxY)+1);
			
			double qNormX=oldX+width/2;
			double qNormY=oldY+height/2;
			
			if(qNormX>focusX){
				dMaxX=boundaryX-focusX;
			}else{
				dMaxX=0-focusX;
			}
			dNormX=qNormX-focusX;
			
			if(qNormY>focusY){
				dMaxY=boundaryY-focusY;
			}else{ 
				dMaxY=0-focusY;
			}
			dNormY=qNormY-focusY;
			
			double qfishX=focusX+dMaxX*(d+1)*(dNormX/dMaxX)/(d*(dNormX/dMaxX)+1);
			double qfishY=focusY+dMaxY*(d+1)*(dNormY/dMaxY)/(d*(dNormY/dMaxY)+1);
			
			
			double geomX=2*Math.abs(qfishX-fishX);
			double geomY=2*Math.abs(qfishY-fishY);
			
			double sGeomX,sGeomY;
			
			if(geomX<geomY){
				sGeomX=geomX;
				sGeomY=geomX/3;
			}else{
				sGeomX=geomY*3;
				sGeomY=geomY;
			}
			
			model.getVertices().get(i).setX(fishX-0.5*sGeomX);
			model.getVertices().get(i).setY(fishY-0.5*sGeomY);
			model.getVertices().get(i).setWidth(sGeomX);
			model.getVertices().get(i).setHeight(sGeomY);
			
		}
		
		return model;
		//return null;
	}
	
}
