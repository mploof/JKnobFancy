package test;

import java.awt.Container;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

import jknobfancy.JKnobFancy;
import jknobfancy.JKnobHandleIcons;

import java.awt.event.*;


@SuppressWarnings("serial")
public class TestKnob extends JComponent {

	public static void main(String[] args) {
		JFrame myFrame = new JFrame("JKnob Test method");
		
		Container thePane = myFrame.getContentPane();

		JKnobFancy knob = new JKnobFancy(160, new Point2D.Double(0.5, 0.5), 0.5, 
				JKnobHandleIcons.largeBlueDot(), 100, JKnobHandleIcons.smallGreenDot());
		knob.setCwDirection(false);
		knob.setMinPos(5);
		knob.setMaxPos(5);
		knob.setMinVal(0);
		knob.setMaxVal(150);
		knob.addMouseMotionListener(new MouseMotionAdapter() {
			 
			 @Override
			 public void mouseDragged(MouseEvent e) {
				knob.moveHandles(e);
				System.out.println("Knob val: " + knob.getHandle(0).getVal());				 
			 }
		});
		thePane.add(knob);

			myFrame.addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent e) {
	                System.exit(0);
	            }
	        });
	
		myFrame.pack();
		myFrame.setVisible(true);
   }

}

