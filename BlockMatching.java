import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class BlockMatching extends JFrame {

	BufferedImage img1;
	BufferedImage img2;
  
    Vector[][] motionVector;
	public BlockMatching(BufferedImage img1, BufferedImage img2) throws IOException{
		motionVector = new Vector[img1.getWidth()][img1.getHeight()];
		this.img1 = img1;
		this.img2 = img2;
		

	}
	
	
	private void addMenu(){
		JMenu fileMenu = new JMenu( "File");
		
		JMenuItem readItem = new JMenuItem("Get Motion Vectors");
		readItem.addActionListener(new 
				ActionListener()
				{
					public void actionPerformed( ActionEvent event){
						
						try {
							chooseImages();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
			
				});
				
				
	
		
	fileMenu.add(readItem);
	
	
	

	
	
	JMenuItem exitItem = new JMenuItem("Exit");
	exitItem.addActionListener(new 
			ActionListener()
			{
				public void actionPerformed( ActionEvent event){
					System.exit(0);
					
				}
		
			});
	
	fileMenu.add(exitItem);
	
	JMenuBar menuBar = new JMenuBar();
	menuBar.add(fileMenu);
	this.setJMenuBar(menuBar);
	
}
	
	public Vector[][] chooseImages() throws IOException{
	
		Vector[][] b = runBlockMatching();
		return b;
		
	}
//method gets the average rgb 0-255 value given the intARGB 
	public int getAvgRGBval(int aRGB){
		Color c = new Color(aRGB);
		int red = c.getRed();
		int green = c.getGreen();
		int blue = c.getBlue();
		
		int avg = (red + green + blue) / 3;
		//System.out.println(avg);
		return avg;
		
	}
	
	public int[] getMotionVector(int x, int y, int xInc, int yInc, int width, int height){
		int result[] = new int[2];
		int test = 0;
		int same = 0;
		int right = 0;
		int left = 0;
		int up = 0;
		int down = 0;
		int upRight = 0;
		int upLeft = 0;
		int downRight = 0;
		int downLeft = 0;
		
		//variables to keep track of the difference in test block and corresponding blocks
		int sameDiff, rDiff, lDiff, uDiff, dDiff, uRDiff, uLDiff, dRDiff, dLDiff;
		int min = 1000000;
		//assume same block is the min
		String minString = "same";

		//get average color for test block
		if(((x + xInc) < width) && ((y + yInc) < height)){
			for(int i = x; i < x + xInc; i++){
				for(int j = y; j < y + yInc; j++){
					test+= getAvgRGBval(img1.getRGB(i, j));
					
				}
			}
		}
		//get average color for same block 
		if(((x + xInc) < width) && ((y + yInc) < height)){
				for(int i = x; i < x + xInc; i++){
					for(int j = y; j < y + yInc; j++){
						same+= getAvgRGBval(img2.getRGB(i, j));
						
					}
				}
				
				sameDiff = Math.abs(test - same);
				//set closest block to be same area originally
				min = sameDiff;
		}	
		//get average test color for right block 
				//if there is a right block within img dimensions
		if((x + (xInc * 2)) < width)  {
			for(int i = x + xInc; i < x + (xInc * 2); i++){
				for(int j = y; j < y + yInc; j++){
					right+= getAvgRGBval(img2.getRGB(i, j));
				
				}
			}
			rDiff = Math.abs(test - right);
			//check if it is closer than those checked so far
			if(rDiff < min){
				min = rDiff;
				minString = "r";
			}
		}	
		
		//get average test color for left block
		//if there is a left block within img dimensions
				if((x - xInc) > 0){
					for(int i = x - xInc; i < x ; i++){
						for(int j = y; j < y + yInc; j++){
							left+= getAvgRGBval(img2.getRGB(i, j));
						
						}
					}
					lDiff = Math.abs(test - left);
					//check if it is closer than those checked so far
					if(lDiff < min){
						min = lDiff;
						minString = "l";

					}

				}
				
		//get average test color for top block
		//if there is a top block within img dimensions
				if((y - yInc) > 0){
					for(int i = x; i < x + xInc; i++){
						for(int j = y - yInc; j < y; j++){
							up+= getAvgRGBval(img2.getRGB(i, j));
						
						}
					}
					uDiff = Math.abs(test - up);
					//check if it is closer than those checked so far
					if(uDiff < min){
						min = uDiff;
						minString = "u";

					}
				}
				
		//get average test color for bottom block
		//if there is a bottom block within img dimensions
				if((y + (yInc * 2)) < height){
					for(int i = x; i < x + xInc; i++){
						for(int j = y + yInc; j < y + (yInc * 2); j++){
							down+= getAvgRGBval(img2.getRGB(i, j));
						
						}
					}
					dDiff = Math.abs(test - down);
					//check if it is closer than those checked so far
					if(dDiff < min){
						min = dDiff;
						minString = "d";

					}
				}
				
		//get average test color for upper right block
		//if there is a upper right block within img dimensions
				if(((y - yInc) > 0) && ((x + (xInc * 2)) < width)){
					for(int i = x + xInc; i < x + (xInc * 2); i++){
						for(int j = y - yInc; j < y; j++){
							upRight+= getAvgRGBval(img2.getRGB(i, j));
						
						}
					}
					uRDiff = Math.abs(test - upRight);
					//check if it is closer than those checked so far
					if(uRDiff < min){
						min = uRDiff;
						minString = "uR";

					}
				}
				
			//get average test color for lower right block
			//if there is a upper lower block within img dimensions
					if(((y + (yInc * 2)) < height) && ((x + (xInc * 2)) < width)){
						for(int i = x + xInc; i < x + (xInc * 2); i++){
							for(int j = y + yInc; j < y + (yInc * 2); j++){
								downRight+= getAvgRGBval(img2.getRGB(i, j));
							
							}
						}
						dRDiff = Math.abs(test - downRight);
						//check if it is closer than those checked so far
						if(dRDiff < min){
							min = dRDiff;
							minString = "dR";

						}
					}
			//get average test color for lower left block
			//if there is a upper lower left within img dimensions
					if(((y + (yInc * 2)) < height) && ((x - xInc) > 0)){
						for(int i = x - xInc; i < x; i++){
							for(int j = y + yInc; j < y + (yInc * 2); j++){
								downLeft+= getAvgRGBval(img2.getRGB(i, j));
							
							}
						}
						dLDiff = Math.abs(test - downLeft);
						//check if it is closer than those checked so far
						if(dLDiff < min){
							min = dLDiff;
							minString = "dL";

						}
					}
			//get average test color for upper left block
			//if there is a upper upper left within img dimensions
					if(((y - yInc) > 0) && ((x - xInc) > 0)){
						for(int i = x - xInc; i < x; i++){
							for(int j = y - yInc; j < y ; j++){
								upLeft+= getAvgRGBval(img2.getRGB(i, j));
							
							}
						}
						uLDiff = Math.abs(test - upLeft);
						//check if it is closer than those checked so far
						if(uLDiff < min){
							min = uLDiff;
							minString = "uL";

						}
					}
					
					
			//Now we have our total color blocks for each of the 8 directions
			//So let's see which matches the best and return a motion vector
			if(minString.equals("r")){
				result[0] = 1;
				result[1] = 0;
				
				
			}else if(minString.equals("l")){
				result[0] = -1;
				result[1] = 0;
				
			}else if(minString.equals("u")){
				result[0] = 0;
				result[1] = -1;
				
			}else if(minString.equals("d")){
				result[0] = 0;
				result[1] = 1;
				
			}else if(minString.equals("uL")){
				result[0] = -1;
				result[1] = -1;
				
			}else if(minString.equals("uR")){
				result[0] = 1;
				result[1] = -1;
				
			}else if(minString.equals("dL")){
				result[0] = -1;
				result[1] = 1;
				
			}else if(minString.equals("dR")){
				result[0] = 1;
				result[1] = 1;
				
			}
					
			
		

		return result;
		
	}
	public Vector[][] runBlockMatching(){
		
				int xInc = 16;   //16 is the preferred box range
				int yInc = 16;  
				for(int i = 0; i < img1.getWidth() - xInc; i+= xInc){
					for(int j = 0; j < img1.getHeight() - yInc ; j+=yInc){
						
						
						int[] vectorResult = getMotionVector(i, j, xInc, yInc, img1.getWidth(), img1.getHeight());
						Vector v = new Vector(vectorResult[0], vectorResult[1]);
						for(int x = i; x < i + xInc; x++){
							for(int y = j; y < j + yInc; y++){
								motionVector[x][y] = v; 
							}
						}
					}
				}
				
		return motionVector;
	}
	
	
}
