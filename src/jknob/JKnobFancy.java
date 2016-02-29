package jknob;

//Imports for the GUI classes.
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;

/**
* JFancyKnob.java - 
*   A knob component.  The knob can be rotated by dragging 
*   a spot on the knob around in a circle.
*   The knob will report its position in radians when asked.
*
* @author Michael ploof
* @author Dickinson College
* @version 12/4/2000
*/

@SuppressWarnings("serial")
public class JKnobFancy extends JComponent{

	ImageIcon backgroundIcon;
	float scale;
	
	protected int trackRadius = 65;		// Radius of circle the handles will trace	
	protected Point center;
	protected ImageIcon handleIcon;
	protected List<JKnobHandle> handles = new ArrayList<JKnobHandle>();
			
	public class JKnobHandle{
		
		ImageIcon icon;
		String iconName;
		JKnobFancy thisKnob;
		int radius;
		
		private double theta;
		private boolean pressedOnSpot;
				
		JKnobHandle(double theta, ImageIcon icon, JKnobFancy thisKnob){
			init(theta, icon, thisKnob);
		}
		
		private void init(double theta, ImageIcon icon, JKnobFancy thisKnob){
			this.theta = theta;
			this.icon = icon;
			this.pressedOnSpot = false;
			this.thisKnob = thisKnob;
			this.radius = this.icon.getIconWidth() / 2;
		}

		public ImageIcon getIcon() {
			return icon;
		}

		public void setIcon(ImageIcon icon) {
			this.icon = icon;
		}

		public boolean isPressedOnSpot() {
			return pressedOnSpot;
		}

		public void setPressedOnSpot(boolean pressedOnSpot) {
			this.pressedOnSpot = pressedOnSpot;
		}
		
		public void setAngle(double theta) {
			this.theta = theta;
		}
		
		/**
		* Get the current angular position of the knob.
		*
		* @return the current angular position of the knob.
		*/
		public double getAngle() {
			return theta;
		}	

		public void setAngleDeg(double deg) {
			this.theta = Math.toRadians(deg);
		}
		 
		public double getAngleDeg(){
			double tempTheta = theta >= 0 ? theta : 2 * Math.PI + theta; 
			double ret = Math.toDegrees(tempTheta);			
			return ret;
		}

		
		 /**
		  * Determine if the mouse click was on the spot or
		  * not.  If it was return true, otherwise return 
		  * false.
		  *
		  * @return true if x,y is on the spot and false if not.
		  */
		 private boolean isOnSpot(Point pt) {
			return (pt.distance(getSpotCenter()) < this.radius);
		 }
		
		 /** 
		  * Calculate the x, y coordinates of the center of the spot.
		  *
		  * @return a Point containing the x,y position of the center
		  *         of the spot.
		  */ 
		 protected Point getSpotCenter() {
		
			// Calculate the center point of the spot RELATIVE to the
			// center of the of the circle.
		
			int r = thisKnob.trackRadius - this.radius;
		
			int xcp = (int)(r * Math.cos(theta));
			int ycp = (int)(r * Math.sin(theta));
		
			// Adjust the center point of the spot so that it is offset
			// from the center of the circle.  This is necessary because
			// 0,0 is not actually the center of the circle, it is  the 
		     // upper left corner of the component!
			int xc = center.x + xcp;
			int yc = center.y - ycp;
		
			// Create a new Point to return since we can't  
			// return 2 values!
			return new Point(xc,yc);
		 }
		 
		 /**
		  * Calculate the x, y coordinates of the point on the edge of the handle's
		  * radius that is closes to the center of rotation
		  * 
		  * @return a Point containing the x,y position of the point on the handle
		  * 	closest to the center of ration
		  */
		 protected Point getCenterEdgePoint(){
			 double tempTheta = this.getAngle();
			 int xOffset = (int)(this.radius * Math.cos(tempTheta));
			 int yOffset = (int)(this.radius * Math.sin(tempTheta));
			 Point center = this.getSpotCenter();
			 return new Point(center.x - xOffset, center.y + yOffset);			 
		 }

		public int getRadius() {
			return radius;
		}
		
		
	}
	
	 /**
	  * No-arg constructor that initializes the position
	  * of the knob to 0 degrees (right).
	  */
	public JKnobFancy(Point2D relCenter, float relTrackRadius, ImageIcon backgroundIcon, int backgroundWidth, ImageIcon handleIcon) {
		this(0, relCenter, relTrackRadius, backgroundIcon, backgroundWidth, handleIcon);
	 }
	 
	 /**
	  * 
	  * @param initDeg the initial angle of the pre-populated first handle
	  * @param relCenter the center point around which the handles will rotate as a fractional values relative to the overall size of the background image
	  * @param relTrackRadius the relative radius as a fraction of the overall width of the background image
	  * @param backgroundIcon the IconImage for the background
	  * @param backgroundWidth the width of the background. The background image will be scaled proportionally to fit this value
	  * @param handleIcon the IconImage for the handles
	  */	  
	 public JKnobFancy(double initDeg, Point2D relCenter, float relTrackRadius, ImageIcon backgroundIcon, int backgroundWidth, ImageIcon handleIcon) {
		 init(initDeg, relCenter, relTrackRadius, backgroundIcon, backgroundWidth, handleIcon);	 
	 }
	 
	 /**
	  * Empty public constructor available so the object may be instantiated before being properly initialized
	  */
	 public JKnobFancy(){
		 
	 }
	 
	 /**
	  * 
	  * @param initDeg the initial angle of the pre-populated first handle
	  * @param relCenter the center point around which the handles will rotate as a fractional values relative to the overall size of the background image
	  * @param relTrackRadius the relative radius as a fraction of the overall width of the background image
	  * @param backgroundIcon the IconImage for the background
	  * @param backgroundWidth the width of the background. The background image will be scaled proportionally to fit this value
	  * @param handleIcon the IconImage for the handles
	  */
	 public void init(double initDeg, Point2D relCenter, float relTrackRadius, ImageIcon backgroundIcon, int backgroundWidth, ImageIcon handleIcon){
		 
		 handles.add(new JKnobHandle(Math.toRadians(initDeg), handleIcon, this));		
			
			this.backgroundIcon = backgroundIcon;		
			this.scale = (float) backgroundWidth / (float) backgroundIcon.getIconWidth();
			this.center = new Point((int)(relCenter.getX() * backgroundIcon.getIconWidth() * scale), (int)(relCenter.getY() * backgroundIcon.getIconHeight() * scale));			
			this.trackRadius = (int)(relTrackRadius * backgroundWidth);
			
			addMouseListener(new MouseAdapter() {
				 /**
				  * When the mouse button is pressed, the dragging of the
				  * spot will be enabled if the button was pressed over
				  * the spot.
				  *
				  * @param e reference to a MouseEvent object describing
				  *          the mouse press.
				  */
				 @Override
				 public void mousePressed(MouseEvent e) {
				
					Point mouseLoc = e.getPoint();
					boolean alreadySelected = false;
					for(JKnobHandle thisHandle : handles){
						// This prevents multiple spots from being simultaneously selected
						if(alreadySelected){
							thisHandle.pressedOnSpot = false;
						}
						else{
							thisHandle.pressedOnSpot = thisHandle.isOnSpot(mouseLoc);
						}
						if(thisHandle.pressedOnSpot){
							alreadySelected = true;
						}
					}		
				 }
				
				 /**
				  * When the button is released, the dragging of the spot
				  * is disabled.
				  *
				  * @param e reference to a MouseEvent object describing
				  *          the mouse release.
				  */
				 @Override
				 public void mouseReleased(MouseEvent e) {
					for(JKnobHandle thisHandle : handles){
						thisHandle.pressedOnSpot = false;
					}		
				 }
			});
			addMouseMotionListener(new MouseMotionAdapter() {
				 /**
				  * Compute the new angle for the spot and repaint the 
				  * knob.  The new angle is computed based on the new
				  * mouse position.
				  *
				  * @param e reference to a MouseEvent object describing
				  *          the mouse drag.
				  */
				 @Override
				 public void mouseDragged(MouseEvent e) {
					 
					for(JKnobHandle thisHandle : handles){
						if (thisHandle.pressedOnSpot) {
							
						    int mx = e.getX();
						    int my = e.getY();
					
						    // Compute the x, y position of the mouse RELATIVE
						    // to the center of the knob.
						    int mxp = mx - center.x;
						    int myp = center.y - my;
					
						    // Compute the new angle of the knob from the
						    // new x and y position of the mouse.  
						    // Math.atan2(...) computes the angle at which
						    // x,y lies from the positive y axis with cw rotations
						    // being positive and ccw being negative.
						    thisHandle.setAngle(Math.atan2(myp, mxp));
					
						    repaint();
						}
					}
				 }
			});
	 }
	 
	 /**
	  * Adds a new handle to the knob
	  * @param initDeg starting position of the new handle in degrees
	  * @param icon ImageIcon to use for the new handle
	  */
	 public void addHandle(double initDeg, ImageIcon icon){
		 handles.add(new JKnobHandle(Math.toRadians(initDeg), icon, this));
	 }
	 
	 /**
	  * Adds a new handle to the knob. This uses the handle icon that
	  * was set in the knob constructor
	  * @param initDeg starting position of the new handle in degrees
	  */ 
	 public void addHandle(double initDeg){
		 handles.add(new JKnobHandle(Math.toRadians(initDeg), this.handleIcon, this));
	 }
	 
	 /**
	  * 
	  * @param which the element of the handle list that should be returned
	  * @return a the selected handle
	  */
	 public JKnobHandle getHandle(int which){
		 return handles.get(which);
	 }
	
	 /**
	  * Paint the JKnob on the graphics context given.  The knob
	  * is a filled circle with a small filled circle offset 
	  * within it to show the current angular position of the 
	  * knob.
	  *
	  * @param g The graphics context on which to paint the knob.
	  */
	 public void paint(Graphics g) {	
		 
		// Draw background
		g.drawImage(backgroundIcon.getImage(), 0, 0, (int)Math.round(backgroundIcon.getIconWidth()*scale), (int)Math.round(backgroundIcon.getIconHeight()*scale), this);
				
		// Draw handles
		for(int i = 0; i < handles.size(); i ++){
			JKnobHandle thisHandle = handles.get(i);
			
			// Find the center of the handle
			Point pt = thisHandle.getSpotCenter();
			int xc = (int)pt.getX();
			int yc = (int)pt.getY();			
			
			ImageIcon thisIcon = thisHandle.getIcon();
			g.drawImage(thisIcon.getImage(), xc-thisIcon.getIconWidth()/2, yc-thisIcon.getIconHeight()/2, thisIcon.getIconWidth(), thisIcon.getIconHeight(), this);					
		}		
	 }
	
	 /**
	  * Return the ideal size that the knob would like to be.
	  *
	  * @return the preferred size of the JKnob.
	  */
	 public Dimension getPreferredSize() {
		 return new Dimension(Math.round(backgroundIcon.getIconWidth()*scale), Math.round(backgroundIcon.getIconHeight()*scale));
	 }
	
	 /**
	  * Return the minimum size that the knob would like to be.
	  * This is the same size as the preferred size so the
	  * knob will be of a fixed size.
	  *
	  * @return the minimum size of the JKnob.
	  */
	 public Dimension getMinimumSize() {
		 return new Dimension(backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());
	 }
}
