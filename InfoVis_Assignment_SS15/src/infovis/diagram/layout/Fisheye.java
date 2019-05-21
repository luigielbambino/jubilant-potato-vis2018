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
	
	public int pFocusX;
	public int pFocusY;
	

	public void setMouseCoords(int x, int y, View view) {
		// TODO Auto-generated method stub
		
		// get focus
		pFocusX=(int)(x / view.getScale() - view.getTranslateX()); // Focus point in X
		pFocusY=(int)(y / view.getScale() - view.getTranslateY()); // Focus point in Y
	}

	public Model transform(Model model, View view) {
		// TODO Auto-generated method stub
		
		double dNormX, dNormY, dMaxX, dMaxY, qNormX, qNormY; // normal  coordinates and distance from focus to border of the window
		double pfyeX,pfyeY, gpx, gpy, sGeomX, sGeomY, qFishX, qFishY, sFishX, sFishY; // fisheye coordinates, 
		
		int d = 2; // distortion factor
		int ratio = 3;
		
		int windowWith = view.getWidth(); // Boundary in X
		int windowHeight = view.getHeight(); // Boundary in Y
		
		int length  =  model.getVertices().size(); 
		for(int i=0; i<length;i++){
			
			double sNormX = 40; // vertex width
			double sNormY = sNormX / ratio; // vertex height
			double initX = model.getVertices().get(i).getCenterX();
			double initY = model.getVertices().get(i).getCenterY();
			
			// mapping position
			if(initX > pFocusX){dMaxX = windowWith - pFocusX;
			}else{dMaxX = 0 - pFocusX;}
			if(initY > pFocusY){dMaxY = windowHeight - pFocusY;
			}else{ dMaxY = 0 - pFocusY;}
			
			dNormX = initX - pFocusX;
			dNormY = initY - pFocusY;
			gpx = (d + 1) * (dNormX / dMaxX) / (d * (dNormX / dMaxX) + 1);
			gpy = (d + 1) * (dNormY / dMaxY) / (d * (dNormY / dMaxY) + 1);
			pfyeX = gpx * dMaxX + pFocusX;
			pfyeY = gpy * dMaxY + pFocusX ;
			
			qNormX = initX + sNormX / 2;
			qNormY = initY + sNormY / 2;
			if(qNormX > pFocusX){dMaxX = windowWith - pFocusX;
			}else{dMaxX = -pFocusX;}
			if(qNormY > pFocusY){dMaxY = windowHeight - pFocusY;
			}else{dMaxY = -pFocusY;}
			
			dNormX = qNormX - pFocusX;
			dNormY = qNormY - pFocusY;
			
			
			// Mapping size
			qFishX = pFocusX + dMaxX * (d+1) * (dNormX/dMaxX) / (d * (dNormX/dMaxX) + 1);
			qFishY = pFocusX + dMaxY * (d+1) * (dNormY/dMaxY) / (d * (dNormY/dMaxY) + 1);
			sGeomX = 2 * (int)(qFishX - pfyeX);
			sGeomY = 2 * (int)(qFishY - pfyeY);
			
			// assign sFish according to ratio
			if(sGeomX < sGeomY){
				sFishX = sGeomX;
				sFishY = sGeomX / ratio;
			}else{
				sFishX = sGeomY * ratio;
				sFishY= sGeomY;
			}
			
			// apply distortions
			model.getVertices().get(i).setX(pfyeX-(sFishX/2));
			model.getVertices().get(i).setY(pfyeY-(sFishY/2));
			model.getVertices().get(i).setWidth(sFishX);
			model.getVertices().get(i).setHeight(sFishY);
		}
		
		return model;
	}
	
}
