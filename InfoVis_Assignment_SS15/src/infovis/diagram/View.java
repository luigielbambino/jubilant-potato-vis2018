package infovis.diagram;

import infovis.diagram.elements.Element;
import infovis.diagram.elements.Vertex;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class View extends JPanel {
	private Model model = null;
	private Color color = Color.BLUE;
	private double scale = 1;
	private double translateX = 0;
	private double translateY = 0;
	
	private Rectangle2D marker = new Rectangle2D.Double();
	Color markerColor = new Color(0, 0, 255, 50);
	public double markerX = 0;
	public double markerY = 0;
	public int markerWidth;
	public int markerHeight;

	public Rectangle2D overviewRect = new Rectangle2D.Double();
	public double overviewScale = 4;
	private double overViewTranslateX = 0;
	private double overViewTranslateY = 0;

	public Rectangle2D overViewTop = new Rectangle2D.Double();
	Color topColor = new Color(100, 100, 100, 120);
	
	
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
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.clearRect(0, 0, getWidth(), getHeight());

		g2D.scale(scale, scale);
		g2D.translate(-translateX, -translateY);
		paintDiagram(g2D, model);

		// Overview rectangle & top overview Rectangle
		g2D.scale(1, 1);
		g2D.setStroke(new BasicStroke((float) (1 / scale)));
		double overviewTopY = translateY + overViewTranslateY;
		double overviewTopHeight = 15 / scale;

		double totalScale = overviewScale * scale;
		double width = getWidth() / totalScale;
		double height = getHeight() / totalScale;
		double x = getWidth() / scale - width + translateX + overViewTranslateX;
		double y = overviewTopY + overviewTopHeight;

		overViewTop.setRect(x, overviewTopY, width, overviewTopHeight);
		g2D.setColor(topColor);
		g2D.fill(overViewTop);
		g2D.draw(overViewTop);

		overviewRect.setRect(x, y, width, height);
		g2D.draw(overviewRect);
		g2D.setPaint(Color.white);
		g2D.fill(overviewRect);

		// draw a scaled model into the overview rectangle
		g2D.translate(x, y); // translate to overview rectangle's coordinate system
		Model scaledModel = new Model();
		for (Element element : model.getElements()) {
			Vertex vertex = (Vertex) element;
			Vertex newElement = new Vertex(vertex.getX() / totalScale, vertex.getY() / totalScale,
					vertex.getWidth() / totalScale, vertex.getHeight() / totalScale);
			scaledModel.addElement((Element) newElement);
		}
		paintDiagram(g2D, scaledModel);
		g2D.translate(-x, -y); // translate back to original cooordinate system

		// Marker rectangle
		markerWidth = (int) (getWidth() / scale);
		markerHeight = (int) (getHeight() / scale);

		double differenceX = (translateX / scale) / overviewScale;
		double differenceY = (translateY / scale) / overviewScale;
		double markerWidth = overviewRect.getWidth() / this.scale;
		double markerHeight = overviewRect.getHeight() / this.scale;
		double markerX = overviewRect.getX() + differenceX;
		double markerY = overviewRect.getY() + differenceY;
		marker = new Rectangle2D.Double(markerX, markerY, markerWidth, markerHeight);
		g2D.setColor(markerColor);
		g2D.draw(marker);
		g2D.fill(marker);

	}

	private void paintDiagram(Graphics2D g2D, Model model) {
		for (Element element : model.getElements()) {
			element.paint(g2D);
		}
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public double getScale() {
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

	public double getOverviewTranslateX() {
		return overViewTranslateX;
	}

	public void setOverviewTranslateX(double x) {
		overViewTranslateX = x;
	}

	public double getOverviewTranslateY() {
		return overViewTranslateY;
	}

	public void setOverviewTranslateY(double y) {
		overViewTranslateY = y;
	}

	public void updateTranslation(double x, double y) {
		setTranslateX(x);
		setTranslateY(y);
	}

	public void updateMarker(int x, int y) {
		marker.setRect(x, y, 16, 10);
	}

	public Rectangle2D getMarker() {
		return marker;
	}

	public boolean markerContains(int x, int y) {
		return marker.contains(x, y);
	}
}
