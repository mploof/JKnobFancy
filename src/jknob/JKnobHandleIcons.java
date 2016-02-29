package jknob;

import javax.swing.ImageIcon;

/**
 * This is a convenience class for retrieving default handle icons
 * @author Michael
 *
 */
public class JKnobHandleIcons {
	
		private static String smallGreenDot = "10px-Green_pog.png";
		private static String smallBlueDot = "15px-Blue_pog.png";
		private static String medBlueDot = "20px-Blue_pog.png";		
		private static String largeBlueDot = "25px-Blue_pog.png";
		
		public static ImageIcon smallGreenDot(){
			return new ImageIcon(JKnobHandleIcons.class.getResource(smallGreenDot));
		}
		
		public static ImageIcon smallBlueDot(){
			return new ImageIcon(JKnobHandleIcons.class.getResource(smallBlueDot));
		}
		
		public static ImageIcon medBlueDot(){
			return new ImageIcon(JKnobHandleIcons.class.getResource(medBlueDot));
		}
		
		public static ImageIcon largeBlueDot(){
			return new ImageIcon(JKnobHandleIcons.class.getResource(largeBlueDot));
		}	
}
