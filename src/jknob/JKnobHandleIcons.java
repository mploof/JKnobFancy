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
			return getImageIcon(smallGreenDot);
		}
		
		public static ImageIcon smallBlueDot(){
			return getImageIcon(smallBlueDot);
		}
		
		public static ImageIcon medBlueDot(){
			return getImageIcon(medBlueDot);
		}
		
		public static ImageIcon largeBlueDot(){
			return getImageIcon(largeBlueDot);
		}	
		
		public static ImageIcon getImageIcon(String fileName){
			return new ImageIcon(new JKnobHandleIcons().getClass().getClassLoader().getResource("images/" + fileName));
		}
}
