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
	private double markerOffset = 0;
	private double markerSizeX;
	private double markerSizeY;
	Color markerColor = new Color(150, 150, 150, 127);
	private Rectangle2D overviewRect = new Rectangle2D.Double();
	private double overviewScale = 0.2;
	private double overviewInitPos = 50;
	private double overviewSizeX;
	private double overviewSizeY;
	
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
		g2D.scale(scale, scale);
		paintDiagram(g2D);
		
		// Overview rectangle
		overviewScale = 0.2/scale;
		overviewSizeX = getWidth();
		overviewSizeY = getHeight();
		
		Graphics2D overview = (Graphics2D) g;
		overview.scale(overviewScale, overviewScale);
		overviewRect = new Rectangle2D.Double(0, 0, overviewSizeX, overviewSizeY);
		overview.setColor(Color.darkGray);
		overview.translate(overviewInitPos, overviewInitPos);
		overview.draw(overviewRect);
		paintDiagram(overview);
		
		// Marker rectangle
		markerSizeX = getWidth()/scale;
		markerSizeY = getHeight()/scale;
		
		Graphics2D markerView = (Graphics2D) g;
		marker = new Rectangle2D.Double(markerOffset, markerOffset, markerSizeX, markerSizeY);
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
		marker.setRect(x, y, 16, 10);
	}
	public Rectangle2D getMarker(){
		return marker;
	}
	public boolean markerContains(int x, int y){
		return marker.contains(x, y);
	}
}
 