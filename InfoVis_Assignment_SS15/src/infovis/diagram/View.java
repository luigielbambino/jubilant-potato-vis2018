package infovis.diagram;

import infovis.diagram.elements.Element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import java.awt.BasicStroke;
import java.awt.Stroke;

import javax.swing.JPanel;


public class View extends JPanel{
	private Model model = null;
	private Color color = Color.BLUE;
	private double scale = 1;
	private double translateX= 0;
	private double translateY=0;
	private Rectangle2D marker = new Rectangle2D.Double();
	Color markerColor = new Color(150, 150, 150, 127);
	private Rectangle2D overviewRect = new Rectangle2D.Double();
	private Rectangle2D dataRect = new Rectangle2D.Double();
	
	public int overviewX = 40; // overview
	public int overviewY = 20; // overview
	public double overviewScale = 0.2;
	public double overviewWidth;
	public double overviewHeight;
	
	public double markerX = 0; // marker
	public double markerY = 0; // marker
	public int markerWidth; //marker
	public int markerHeight; // marker
	
	
	
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	
	public void paint(Graphics g) {
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.clearRect(0, 0, getWidth(), getHeight());
		//g2D.translate(translateX * scale, translateY * scale);
		//g2D.scale(scale, scale);
		//paintDiagram(g2D);
		
		Graphics2D data = (Graphics2D) g;
		data.scale(scale, scale);
		dataRect.setRect(0, 0, overviewWidth, overviewHeight);
		data.setColor(Color.darkGray);
		data.translate(translateX, translateY);
		data.draw(dataRect);
		paintDiagram(data);
		
		
		// Overview rectangle
		overviewScale = 0.2/scale;
		overviewWidth = getWidth();
		overviewHeight = getHeight();
		
		Graphics2D overview = (Graphics2D) g;
		overview.scale(overviewScale, overviewScale);
		overviewRect.setRect(0, 0, overviewWidth, overviewHeight);
		overview.setColor(Color.darkGray);
		overview.translate(overviewX, overviewY);
		overview.draw(overviewRect);
		paintDiagram(overview);
				
		// Marker rectangle
		markerWidth = (int) (getWidth() / scale);
		markerHeight = (int) (getHeight() / scale);
		
		Graphics2D markerView = (Graphics2D) g;
		marker.setRect(markerX, markerY, markerWidth, markerHeight);
		markerView.setColor(markerColor);
		markerView.fill(marker);
		markerView.draw(marker);
		
	}
	
	private void paintDiagram(Graphics2D g2D){
		for (Element element: model.getElements()){
			element.paint(g2D);
		}
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	public double getScale(){
		return scale;
	}
	public double getTranslateX() {
		return translateX;
	}
	public void setTranslateX(double translateX) {
		this.translateX = translateX;
	}
	public double getTranslateY() {
		return translateY;
	}
	public void setTranslateY(double tansslateY) {
		this.translateY = tansslateY;
	}
	public void updateTranslation(double x, double y){
		setTranslateX(x);
		setTranslateY(y);
	}	
	public void updateMarker(int x, int y){
		//marker.setRect(x, y, markerWidth, markerHeight);
		markerX= (int) (x / overviewScale / scale);
		markerY= (int) (y / overviewScale / scale);
	}
	public Rectangle2D getMarker(){
		return marker;
	}
	public boolean markerContains(int x, int y){
		return marker.contains(x, y);
	}
	public void updateOverview(int x, int y) { // overview
		overviewX = (int) (x / overviewScale / scale);
		overviewY = (int) (y / overviewScale / scale);
	}
}
 