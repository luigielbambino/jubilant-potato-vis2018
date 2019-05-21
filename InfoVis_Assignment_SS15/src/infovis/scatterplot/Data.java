package infovis.scatterplot;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import com.sun.org.apache.regexp.internal.recompile;

public class Data{
	private double [] values;
	private double [][] XY;
	private Color color = Color.BLACK;
	private String label = "";
	RectangularShape shape;
	public static final double standardH = 5;
	public static final double standardW = 5;
	private int id = 0;
	private double launch = 0;
	private double capacity = 0;
	private double PS = 0;
	private double speed = 0;
	private double veloMax = 0;
	private double mass = 0;
	private double acceleration = 0;

	private Line2D.Double lineData = new Line2D.Double(0, 0, 0, 0);
	private Line2D.Double lineAttribute	= new Line2D.Double(0, 0, 0, 0);

	public Data(double[] values, String label) {
		super();
		this.values = values;
		this.label = label;
		this.shape = new Rectangle2D.Double(100, 100, standardW, standardH);
		this.id = Model.generateNewID();
		this.label = label;
		this.launch = values[0];
		this.capacity = values[1];
		this.PS = values[2];
		this.speed = values[3];
		this.veloMax = values[4];
		this.mass = values[5];
		this.acceleration = values[6];
	}

	public Data(double[] values, int dataID, String label, RectangularShape shape) {
		super();
		this.values = values;
		this.label = label;
		this.shape = shape;
		this.id = dataID;
		this.launch = values[0];
		this.capacity = values[1];
		this.PS = values[2];
		this.speed = values[3];
		this.veloMax = values[4];
		this.mass = values[5];
		this.acceleration = values[6];
	}

	public Data(double[] values, Color color, String label) {
		super();
		this.values = values;
		this.color = color;
		this.label = label;
	}

	public Data (double[] values) {
		this.values = values;
	}

	public Data(int aID, double x1, double y1, double x2, double y2, String aName) {
		super();
		this.id = aID;
		this.label = aName;
		this.lineAttribute.setLine(x1, y1, x2, y2);	
	}

	public Data(double[][] values, int dataID, String label ) {
		super();
		this.label = label;
		this.id = dataID;
		this.XY = values;
		this.setColor(Color.RED);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}
	public int getDimension(){
		 return values.length;
	}
	public double getValue(int index){
		return values[index];
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(label);
		stringBuffer.append('[');
		for (double value : values) {
			stringBuffer.append(value);
			stringBuffer.append(',');
		}
		
		stringBuffer.append(']');
	return stringBuffer.toString();
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return label;
	}

	public double getLaunch() {
		return launch;
	}

	public double getCapacity() {
		return capacity;
	}

	public double getPS() {
		return PS;
	}

	public double getSpeed() {
		return speed;
	}

	public double getVeloMax() {
		return veloMax;
	}

	public double getMass() {
		return mass;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public boolean contains (double x, double y) {
		return shape.contains(x, y);
	}

	public void paint(Graphics2D g2D) {
		g2D.setColor(color);
		g2D.fill(shape);
		g2D.setColor(Color.GRAY);
		g2D.draw(shape);
	}

	public RectangularShape getShape() {
		return shape;
	}

	public void setShape(RectangularShape shape) {
		this.shape = shape;
	}

	public Line2D.Double getLine(){
		
		return this.lineAttribute;
		
	}

	public void changeLine(double x1, double y1, double x2, double y2) {
		this.lineAttribute.setLine(x1, y1, x2, y2);
	}

	public double[][] getXY() {
		return XY;
	}

	public void setX (int index, double x) {
		XY[index][0] = x;
	}

	public void setY(int index, double y) {
		XY[index][1] = y;
	}

}
