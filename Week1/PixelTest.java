
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;





public class PixelTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int w = 100;
		int h = 100;
		int type = BufferedImage.TYPE_INT_ARGB;
		int  a, r, g, b;
		Color p;

		BufferedImage image = new BufferedImage(w, h, type);



		for(int x = 0; x < w; x++) {
		    for(int y = 0; y < h; y++) {
//		    	 a = (int)(1); //alpha
		    	 r = (int)(Math.random()*256); //red
		    	 g = (int)(Math.random()*256); //green
		    	 b = (int)(Math.random()*256); //blue
		//    	p = (a<<24) | (r<<16) | (g<<8) | b;
		    	 p = new Color(r,g,b); // opaque color
		    	 // can also use floats like with Maple example
		    	image.setRGB(x, y, p.getRGB());
		        
		    }
		};
		try {
		ImageIO.write(image, "png", new File("test.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("All Done "+System.currentTimeMillis());
	}

}
