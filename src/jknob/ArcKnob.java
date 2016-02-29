package jknob;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class ArcKnob extends JKnobFancy{

	private int initHandles;
	private Color arcColor;
	private double arcStartDeg;
	private double arcSizeDeg;
	
	/**
	 * Empty constructor so the ArcKnob may be instantiated before being full initialized
	 */
	public ArcKnob(){
		this.initHandles = 0;
		setDefaultArcColor();
	}
	
	/**
	 * Three handle constructor
	 * @param initDeg0
	 * @param handleIcon0
	 * @param initDeg1
	 * @param handleIcon1
	 * @param initDeg2
	 * @param handleIcon2
	 * @param relCenter
	 * @param relTrackRadius
	 * @param backgroundIcon
	 * @param backgroundWidth
	 */
	public ArcKnob(double initDeg0, ImageIcon handleIcon0, double initDeg1, ImageIcon handleIcon1, double initDeg2,ImageIcon handleIcon2, 
			Point2D relCenter, float relTrackRadius, ImageIcon backgroundIcon, int backgroundWidth) {
		this.init(initDeg0, handleIcon0, initDeg1, handleIcon1, initDeg2, handleIcon2, relCenter, relTrackRadius, backgroundIcon, backgroundWidth);		
	}
	
	/**
	 * Two handle constructor
	 * @param initDeg0
	 * @param handleIcon0
	 * @param initDeg1
	 * @param handleIcon1
	 * @param relCenter
	 * @param relTrackRadius
	 * @param backgroundIcon
	 * @param backgroundWidth
	 */
	public ArcKnob(double initDeg0, ImageIcon handleIcon0, double initDeg1, ImageIcon handleIcon1, 
			Point2D relCenter, float relTrackRadius, ImageIcon backgroundIcon, int backgroundWidth) {
		this.init(initDeg0, handleIcon0, initDeg1, handleIcon1, relCenter, relTrackRadius, backgroundIcon, backgroundWidth);		
	}
	
	public void init(double initDeg0, ImageIcon handleIcon0, double initDeg1, ImageIcon handleIcon1, double initDeg2,ImageIcon handleIcon2, Point2D relCenter, float relTrackRadius, ImageIcon backgroundIcon, int backgroundWidth) {
		super.init(initDeg0, relCenter, relTrackRadius, backgroundIcon, backgroundWidth, handleIcon0);		
		this.addHandle(initDeg1, handleIcon1);
		this.addHandle(initDeg2, handleIcon2);
		this.initHandles = 3;
		setDefaultArcColor();
	}
	
	public void init(double initDeg0, ImageIcon handleIcon0, double initDeg1, ImageIcon handleIcon1, Point2D relCenter, float relTrackRadius, ImageIcon backgroundIcon, int backgroundWidth) {
		super.init(initDeg0, relCenter, relTrackRadius, backgroundIcon, backgroundWidth, handleIcon0);
		this.addHandle(initDeg1, handleIcon1);
		this.initHandles = 2;
		setDefaultArcColor();
	}
	
	private void setDefaultArcColor(){
		arcColor = new Color(255, 0, 255, 120);
	}
	
	public void setArcColor(Color color){
		arcColor = color;
	}
	
	public Color getArcColor(){
		return arcColor;
	}
	
	public double getArcStartDeg(){
		return arcStartDeg;		
	}
	
	public double getArcSizeDeg(){
		return arcSizeDeg;		
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		// Set transparent magenta fill for arc
		g2.setColor(arcColor);
		
		for(JKnobHandle thisHandle : handles){
			g2.setStroke(new BasicStroke(3));
			g2.draw(new Line2D.Float(center, thisHandle.getCenterEdgePoint()));
		}
		
		// Draw arc
		int startHandle = 0;
		int stopHandle = 0;
		if(this.initHandles == 3){
			startHandle = 1;
			stopHandle = 2;
		}
		else if(this.initHandles == 2){
			startHandle = 0;
			stopHandle = 1;
		}
		if(handles.size() >= 2 && initHandles >= 2){
			arcStartDeg = (int)handles.get(startHandle).getAngleDeg();
			arcSizeDeg = (int)handles.get(stopHandle).getAngleDeg() - (int)handles.get(startHandle).getAngleDeg(); 
			arcSizeDeg = arcSizeDeg < 0 ? 360 + arcSizeDeg : arcSizeDeg;			
			int handleDia = this.getHandle(0).getRadius() * 2;
			g2.fillArc(center.x - trackRadius + handleDia, center.y - trackRadius + handleDia, (trackRadius - handleDia) * 2, (trackRadius - handleDia) * 2, 
					(int)arcStartDeg, (int)arcSizeDeg);
		}
	}
}
