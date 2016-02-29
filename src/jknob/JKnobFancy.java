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
*   A knob component. The knob can be rotated by dragging 
*   a spot on the knob around in a circle. The positions
*   of the handles may reported in degrees or radians by
*   getting a handle and calling the appropriate method.
*
* @author Michael ploof
*/

@SuppressWarnings("serial")
public class JKnobFancy extends JComponent{

	/**
	 * Dimension of the knob's background image
	 */
	Dimension backgroundSize = new Dimension();
	/**
	 * Icon used for the knob background
	 */
	ImageIcon backgroundIcon;
	/**
	 * The scale by which the background ImageIcon is scaled
	 * when it is drawn.
	 */
	float scale;
	
	/**
	 * Relative radius as a percent (0.0-1.0) of the overall width of the background image
	 */
	protected double relTrackRadius;
	/**
	 * Radius of the circle along which the handles will move
	 */
	protected int trackRadius;	
	/**
	 * Pixel location of the center of the handle track
	 */
	protected Point center;
	/**
	 * The handle icon passed to the knob constructor. Different handle
	 * icons may be used for subsequently added handles, but this icon 
	 * will be used if none is specified.
	 */
	protected ImageIcon defaultHandleIcon;
	/**
	 * List containing all handles currently located on the knob object
	 */
	protected List<JKnobHandle> handles = new ArrayList<JKnobHandle>();
			
	/**
	 * This class describes handle objects that may be positioned 
	 * on the JKnobFancy object. 
	 */
	public class JKnobHandle{
		
		/**
		 * The handle icon
		 */
		ImageIcon icon;
		/**
		 * Reference to the knob object on which the handle is located
		 */
		JKnobFancy thisKnob;
		/**
		 * Radius of clickable handle area
		 */
		int radius;						
		/**
		 * Handle location in radians
		 */
		private double theta;			
		/**
		 * Whether the handle is currently clicked
		 */
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
		
		/**
		 * @param theta the new handle angular position in radians
		 */
		public void setAngle(double theta) {
			this.theta = theta;
		}
		
		/**
		* @return the current angular position of the handle in radians
		*/
		public double getAngle() {
			return theta;
		}	

		/**
		 * @param deg the new handle angular position in degrees
		 */
		public void setAngleDeg(double deg) {
			this.theta = Math.toRadians(deg);
		}
		
		/**
		* @return the current angular position of the handle in degrees
		*/
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

			return new Point(xc,yc);
		 }
		 
		 /**
		  * Calculate the x, y coordinates of the point on the edge of the handle's
		  * radius that is closes to the center of rotation. This is useful if drawing
		  * a line from the center of the handle's track to the edge of the handle icon.
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

		/**
		 * @return The radius in pixels of the clickable handle area
		 */
		public int getRadius() {
			return radius;
		}		
	}
	
	 /**
	  * No initial location constructor that initializes the position
	  * of the knob to 0 degrees (right).
	  */
	public JKnobFancy(Point2D relCenter, float relTrackRadius, ImageIcon backgroundIcon, int backgroundWidth, ImageIcon handleIcon) {
		this(0, relCenter, relTrackRadius, backgroundIcon, backgroundWidth, handleIcon);
	 }
	 
	 /*** 
	  * @param initDeg the initial angle of the pre-populated first handle
	  * @param relCenter the center point around which the handles will rotate as fractional values relative to the overall size of the background image.
	  * 	<br><br>For instance, if the original background image is 100px W x 400 px H and the center of rotation should be at 50px, 100px, this parameter
	  * 	would be "new Point(0.5, 0,25)". The point of locating the center of rotation in this manner is to ensure that the center of rotation is always
	  * 	in the same place on the background image regardless of how it is scaled based upon the backgroundWidth parameter.<br><br>
	  * @param relTrackRadius the relative radius as a percent (0.0-1.0) of the overall width of the background image
	  * @param backgroundIcon the IconImage for the background
	  * @param backgroundWidth the width of the background. The background image will be scaled proportionally to fit this value
	  * @param handleIcon the default IconImage for the handles
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
	  * See notes for {@link #JKnobFancy(double, Point2D, float, ImageIcon, int, ImageIcon)}
	  */	  
	 public void init(double initDeg, Point2D relCenter, float relTrackRadius, ImageIcon backgroundIcon, int backgroundWidth, ImageIcon handleIcon){
		 		 
		this.defaultHandleIcon = handleIcon;			
		this.backgroundIcon = backgroundIcon;		
		this.setWidth(backgroundWidth);		 
		this.center = new Point((int)(relCenter.getX() * backgroundIcon.getIconWidth() * scale), (int)(relCenter.getY() * backgroundIcon.getIconHeight() * scale));
		this.relTrackRadius = relTrackRadius;
		this.setTrackRadius();
		
		handles.add(new JKnobHandle(Math.toRadians(initDeg), this.defaultHandleIcon, this));
		
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
	  * Sets the knob background image width. The background image is always scaled with
	  * width / height proportionality, so this will cause the the width of the image
	  * to change to accommodate the requested width.
	  * @param backgroundWidth width in pixels
	  */
	 public void setWidth(int backgroundWidth){		
		this.scale = (float) backgroundWidth / (float) backgroundIcon.getIconWidth();
		int height = (int)(backgroundIcon.getIconHeight() * this.scale);
		this.backgroundSize.setSize(backgroundWidth, height);
		repaint();
	 }
	 
	 /**
	  * Sets the knob background image height. The background image is always scaled with
	  * width / height proportionality, so this will cause the the width of the image
	  * to change to accommodate the requested height.
	  * @param backgroundHeight
	  */
	 public void setHeight(int backgroundHeight){		 
		 this.scale = (float) backgroundHeight / (float) backgroundIcon.getIconHeight();
		 int width = (int)(backgroundIcon.getIconWidth() * this.scale);
		 backgroundSize.setSize(width, backgroundHeight);		 
		 repaint();
	 }
	 
	 /**
	  * @return Dimension of the current background image size 
	  */
	 public Dimension getBackgroundSize(){
		 return backgroundSize;
	 }
	 
	 /**
	  * @param relTrackRadius relative radius as a percent (0.0-1.0) of the overall width of the background image
	  */
	 public void setTrackRadius(){
		 this.trackRadius = (int)(this.relTrackRadius * backgroundSize.getWidth());
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
		 handles.add(new JKnobHandle(Math.toRadians(initDeg), this.defaultHandleIcon, this));
	 }
	 
	 /**
	  * Retrieves a handle object currently located on the knob
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
