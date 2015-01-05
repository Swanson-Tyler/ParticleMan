//***************************
//* Author: Tyler Swanson    
//*
//* Copyright 2014 
//*
//*****************************

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

 

public class ParticleMan extends JFrame{
	final String outputFilePrefix = "renders/frame_";
	int counter = 1,numFrames,currentFrame,renderFrames, maxFrames, particleCount;
	double secondsBetweenFrames,frameRate;
    private final JFileChooser chooser;
    private static final ArrayList<Particle> particlePool = new ArrayList<Particle>();
    private static final Particle[] particlePool2 = new Particle[2000000];
    
    //javax swing gui components
    ImageIcon i;
	JLabel j, frameNumLabel, colorLabel, particleSpeedValue, particleSpeedLabel, particleSizeValue, particleSizeLabel;
	JPanel frameNumPanel, colorPanel, particleSpeed, particleSize, caPanel, controlPanel;
	JTextField startingColor;
	JSlider particleSpeedSlider,particleSizeSlider;
	JScrollPane s;
	Border blackline;
	
    BufferedImage b;

	Timer timer;
	Random rand;
	Color[] particleColors;
    
   
	
	
	public ParticleMan( double frameRate){
		//set our initial particleCount to zero;
		particleCount = 0;
		
		//create directory to hold edge detected images
		File imagesFolder = new File("images");
		imagesFolder.mkdir();
		
		//create directory to hold rendered particle images
		File renderFolder = new File("renders");
		renderFolder.mkdir();
		
		//set up JFrame
		this.setTitle("ParticleMan Demo");
		this.setSize(1000,800);
		this.setBackground(Color.decode("#262526"));
		
		//add menu items at the top
		addMenu();
		
		
		//set up the control panel on left side
		setUpControls();
		
		//initialize and fill array of colors
		particleColors = new Color[255];
		setUpColors();
		
		//set up panel to show Rendered Images
		caPanel = new JPanel();
		caPanel.setSize(640, 360);
		
	
		//initialize renderFrames to however many images are currently 
		//in the folder to account for multiple renders
		
		renderFrames = checkRenderFolderSize("renders");
		
		//initial the label that tells us what frame we are currently at
		currentFrame = 0;
		frameNumLabel = new JLabel("Frame: " + currentFrame);
		this.add(frameNumLabel);
		
		//initialize more gui components
		j = new JLabel();
		b = new BufferedImage(640, 360, BufferedImage.TYPE_INT_ARGB);
		i = new ImageIcon(b);
		blackline = BorderFactory.createLineBorder(Color.black);

		
		//initialize random number generator for making particle movement more organic
		rand = new Random();
		
		//set a max number of frames we can render.
		numFrames = 100;
		
		//creates pop up window for you to choose a file from
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		
		//frameRate we render at
		this.frameRate = frameRate;
		
		
	}
	
	//Method to set up control panel on left side of gui
	private void setUpControls(){
		
		//control panel set up making it take 4 rows of components and one column
		controlPanel = new JPanel(new GridLayout(4,1));
		controlPanel.setSize(175, 800);
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		controlPanel.setBorder(raisedbevel);
		controlPanel.setBackground(Color.decode("#f2f2f2"));
		
		
		
		//set up speed controls
		particleSpeed = new JPanel();
		particleSpeed.setSize(200, 200);
		particleSpeed.setBackground(Color.decode("#f2f2f2"));
		
		particleSpeedLabel = new JLabel("Particle Speed");
		particleSpeedSlider = new JSlider(1, 10, 1);
		particleSpeedValue = new JLabel(String.valueOf(particleSpeedSlider.getValue()));

		//listener to see when slider has been changed
		particleSpeedSlider.addChangeListener(new ChangeListener()
		{
		

			@Override
			public void stateChanged(ChangeEvent e) {
				
				// update the label when the slider changes
				particleSpeedValue.setText(String.valueOf(particleSpeedSlider.getValue()));
			}
		});
		
		//add slider and labels to control panel
		particleSpeed.add(particleSpeedLabel);
		particleSpeed.add(particleSpeedSlider);
		particleSpeed.add(particleSpeedValue);
		controlPanel.add(particleSpeed);
		
		//Set up size controls
		particleSize = new JPanel();
		particleSize.setSize(200, 200);
		particleSize.setBackground(Color.decode("#f2f2f2"));

		
		particleSizeLabel = new JLabel("Particle Size");
		particleSizeSlider = new JSlider(1, 30, 2);
		particleSizeValue = new JLabel(String.valueOf(particleSizeSlider.getValue()));

	
		//listener for the particle size slider
		particleSizeSlider.addChangeListener(new ChangeListener()
		{
		

			@Override
			public void stateChanged(ChangeEvent e) {
				//change the label when slider changes
				particleSizeValue.setText(String.valueOf(particleSizeSlider.getValue()));
			}
		});
		//add sliders and labels to control panel
		particleSize.add(particleSizeLabel);
		particleSize.add(particleSizeSlider );
		particleSize.add(particleSizeValue);
		controlPanel.add(particleSize);
		
		//initialize color panel
		colorPanel = new JPanel();
		colorPanel.setSize(200, 200);
		colorPanel.setBackground(Color.decode("#f2f2f2"));
		colorLabel = new JLabel("Starting Color");
		startingColor = new JTextField("0xFF00FF00");
		startingColor.addActionListener(new ActionListener()
		{
			public void actionPerformed( ActionEvent event){
				//fills the color array with new values based on hex entered
				setUpColors();
				
			}
			
			
			
		});
		
		//add color panel to 
		colorPanel.add(colorLabel);
		colorPanel.add(startingColor);
		controlPanel.add(colorPanel);
		
		//Place holder for frame number panel , doesn't get added to the control panel yet
		 frameNumPanel = new JPanel();
		frameNumPanel.setSize(100, 150);
		frameNumLabel = new JLabel("Frame: "+ currentFrame);
		frameNumPanel.add(frameNumLabel);
		
		
		//finally add control panel to the JFrame
		this.add(controlPanel);

		
	}
	
	//Check the folder to see if there are any files in it
	private int checkRenderFolderSize(String folderName){
		int num = -1;
		File file = new File( folderName + "/frame_" + 1 + ".png");
		if(!file.exists())
			return 0; //return 0 if there aren't any files in the folder
		else{
			for(num = 2; num < 100000; num++){
				File file1 = new File(folderName + "/frame_" + num + ".png");
				if(!file1.exists()){
					return num-1; //if there are files in the folder, return how many
					
				}
				
			}
		}
		return num;
	}
	
	//Method to set up color array for particles to use
	private void setUpColors(){
		//parses hex and gets intARGB
		int hex =(int) Long.parseLong( startingColor.getText().substring( 2, startingColor.getText().length() ), 16 );
		
		//bit shift to get rgb values respectively
		int red = (hex >>> 16) & 0x000000FF;
		int green = (hex >>> 8 ) & 0x000000FF;
		int blue = (hex) & 0x000000FF;
		
		//how much to increment each color by 
		float rChange =  (float) (((float) red)/255.0);
		float bChange =  (float) (((float) blue)/255.0);
		float gChange =  (float) (((float) green)/255.0);
		

		double alpha = 200;
		
		//iterate through and fill color array
		for(int i = 0; i < 255; i++ ){
			red -= rChange;
			green -= gChange;
			blue -= bChange;
			
			alpha -= .5;
			Color c = new Color( (int) red, (int) green, (int) blue, (int) alpha);
			particleColors[254 - i] = c;
		}
	}
	
		//adds menu list at top
		private void addMenu(){
			JMenu fileMenu = new JMenu( "File");
			
			JMenuItem readItem = new JMenuItem("Read Video");
			readItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event){
					double fps = (double) (1 / frameRate); 
					File file = getFile();
					if(file!= null){
						readVideoFrames(file.getName(), fps);
					}
					
	
				}	
			});
			
					
		fileMenu.add(readItem);
		
		
		
		JMenuItem particleItem = new JMenuItem("Render Particle Motion");
		particleItem.addActionListener(new 
				ActionListener()
				{
					public void actionPerformed( ActionEvent event){
						
							
							Runnable run = new Runnable(){
								public void run(){
								try{
									openParticleImage();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								}
							};
							Thread thread = new Thread(run);
							thread.start();
						
					}
			
				});
		
		fileMenu.add(particleItem);
		JMenuItem writerItem = new JMenuItem("Write Video");
		writerItem.addActionListener(new 
				ActionListener()
				{
					public void actionPerformed( ActionEvent event){
						
							
							Runnable run = new Runnable(){
								public void run(){
									String stringResult1 = JOptionPane.showInputDialog("Input FPS: (Recommend below 24)");
									int framesPerSec = stringToInt(stringResult1);
	
									if(renderFrames == 0 || (renderFrames == -1)){
										JOptionPane.showMessageDialog(new JFrame(),"You need to render some frames first. It's the >Render Particle Motion option under >File");
									}else{
										File file = new File("renders/frame_1.png");
										int width = 1920;
										try {
											width = ImageIO.read(file).getWidth();
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										int height = 1080;
										try {
											height = ImageIO.read(file).getHeight();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										VideoWriter w = new VideoWriter("renders/", framesPerSec, renderFrames - 1, width, height );
									}
								}
							};
							Thread thread = new Thread(run);
							thread.start();
						
					}
			
				});
		
		fileMenu.add(writerItem);
		
		
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
	
	//converts string to int
	private int stringToInt(String stringNum){
		int i = Integer.parseInt(stringNum);
		return i;
	}
	
    
    //main method that does block matching, and creates the rendered frames
	public void openParticleImage() throws IOException{
		
		final Graphics2D g2d =(Graphics2D) b.createGraphics();
		g2d.setColor(Color.BLUE);
		BlockMatching block;
		BufferedImage img1;
		BufferedImage img2;
		
		for(int i = 0; i < numFrames + 1 ; i++){
			//create 2 consecutive files using our naming convention
			String filename1 = "images/frame_" + (i+1) + ".png";
			String filename2 = "images/frame_" + (i+2) + ".png";
			
			File file1 = new File(filename1);
			File file2 = new File(filename2);
			
			//if the files exist give the file paths
			if((file1.exists()) && (file2.exists())){
					//read them and turn them into BufferedImages
					img1 = ImageIO.read(file1);
					img2 = ImageIO.read(file2);
					
					//set up a double array of Vectors to hold a vector for every x,y of img1
					//we assume img1 and img2 have the same width and height because they come from the same original video
					Vector[][] motionVectors = new Vector[img1.getWidth()][img1.getHeight()];
					
					//initialize a new BlockMatching object that runs a motion estimation algorithm on img1, img2
					block = new BlockMatching(img1, img2);
					
					//run the chooseImages() method and set motionVectors equal to what is returned
					motionVectors = block.chooseImages();
					
					//traverse through the double array of vectors and get an average x and y value 
					//to assign to help guide the motion of the particles that may have been overlooked
					int x= 0;
					int y = 0;
					for(int m = 0 ; m < img1.getWidth(); m++){
						for(int n = 0 ; n < img1.getHeight(); n++){
							if(motionVectors[m][n] != null){
								x+= motionVectors[m][n].x;
								y+= motionVectors[m][n].y;
							}
						}
					}
					float avgX = (float) x / (float)(img1.getWidth() * img1.getHeight());
					float avgY = (float) y / (float)(img1.getWidth() * img1.getHeight());

					//run method that adds particles to the pool with the appropriate parameters
					createParticles(img1, motionVectors, avgX, avgY);
					
					//update and render particle pool
					update();
					render(g2d);
					
					//create image from BufferedImage b
					dumpImageToFile(b);
					
					//increment 
					renderFrames++;
					currentFrame++;
					
					//adds 
					frameNumLabel.setText("Frame: " + currentFrame);
					controlPanel.add(frameNumLabel);
					controlPanel.repaint();
					
					
					}
				} 
		
		System.out.println(renderFrames);
		
	}	
	
	//opens dialog to choose a file from
	private File getFile(){
		File file = null;
		if ( chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			file = chooser.getSelectedFile();
		}
		return file;
	}
 
	// sees if pixel is on an edge and creates a particle from it
	public void createParticles(BufferedImage img, Vector[][] motionVectors, float avgX, float avgY){
		//traverse through every pixel in the given BufferedImage
		for(int i = 0; i < img.getWidth(); i++){
			for(int j = 0; j < img.getHeight(); j++){
				
				//if it has an average rgb value of less than 200, meaning not white and either gray or black
				if(getAvgRGBval(img.getRGB(i, j)) < 200){
				    
					//if we don't have a motion vector associated with that pixel
					if(motionVectors[i][j] == null){
						
						//create a particle using the avgX and avgY motion vector we calculated and add it to the pool 
						
						//Particle p = new Particle(i,j,  (int) (avgX ) * particleSpeedSlider.getValue() ,(int) (avgY) * particleSpeedSlider.getValue() , particleSizeSlider.getValue() ,254, particleColors[254], avgX, avgY);
						//particlePool.add(p);
						Particle p = getFreeParticle(i,j,  (int) (avgX ) * particleSpeedSlider.getValue() ,(int) (avgY) * particleSpeedSlider.getValue() , particleSizeSlider.getValue() ,254, particleColors[254], avgX, avgY);
						particlePool2[particleCount] = p;
						particleCount++;
						
					}else{
						//we do have a motion vector so we use it when creating a particle and adding it to the pool 
						
						//Particle p = new Particle(i,j, motionVectors[i][j].x * particleSpeedSlider.getValue()  ,motionVectors[i][j].y * particleSpeedSlider.getValue(), particleSizeSlider.getValue() , 254 , particleColors[254], avgX, avgY);
						//particlePool.add(p);
						Particle p = getFreeParticle(i,j, motionVectors[i][j].x * particleSpeedSlider.getValue()  ,motionVectors[i][j].y * particleSpeedSlider.getValue(), particleSizeSlider.getValue() , 254 , particleColors[254], avgX, avgY);

						particlePool2[particleCount] = p;
						particleCount++;
					}
					
				}
			}
		}
	}

	//update each particle in the particlePool ArrayList
	public void update(){
	    //Array implementation using pooling  
		for(int i = 0; i < particleCount; i++){
			Particle p = particlePool2[i];
			if(p.update()){
				returnParticleToPool(p, i);
			}
		}
		
		
		
		
		//ArrayList implementation
		
		  /*
	      Iterator<Particle> it = particlePool.iterator();
	      while (it.hasNext()) {
	    	    Particle part = it.next();
	    	    if(part.update()) {
	    	       it.remove();
	    	    }
	    	  }
	    	  */
	   }
	
	//handles dead particles
	public void returnParticleToPool(Particle p, int currentIndex){
		//swap dead particle with last living particle in pool
		particlePool2[currentIndex] = particlePool2[particleCount - 1];
		particlePool2[particleCount - 1] = p;
		
		--particleCount;
		
	}
	//returns a free dead particle to use
	public Particle getFreeParticle(int x, int y, int xSpeed, int ySpeed, int size, int life,  Color c, float avgX, float avgY){
		
		Particle p;
		
		if(particlePool2[particleCount] == null){ //no dead particles yet, so we need to create a new particle;
			p = new Particle(x, y, xSpeed, ySpeed, size, life, c, avgX, avgY);
			
			
		}else{

			p = particlePool2[particleCount];
			p.setX(x);
			p.setY(y);
			p.setDx(xSpeed);
			p.setDy(ySpeed);
			p.setSize(size);
			p.setLife(life);
			p.setColor(c);
			p.setAvgX(avgX);
			p.setAvgY(avgY);
			
		}
		
		return p;
		
	}
	
	//render each particle in the particlePool ArrayList
	public void render(Graphics2D g2d){
		AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, b.getWidth(), b.getHeight());
		
		/
		//Array implementation using pooling
		for(int i = 0; i < particleCount; i++){
			if(particlePool2[i].getLife() >0){
				Color c = particleColors[particlePool2[i].getLife()];
				particlePool2[i].render(g2d, c);
			}
		}
		
		g2d.setStroke(new BasicStroke());
		for(int i = 0; i < particleCount; i++){
			if(particlePool2[i].getLife() >0){
				Color c = particleColors[particlePool2[i].getLife()];
				particlePool2[i].render(g2d, c);
			}
		}
		
		//ArrayList implementation
		/*
		for(int i = 0; i <= particlePool.size() - 1;i++){
			//get a color from our color array to render with
			 Color c = particleColors[particlePool.get(i).life];
	         particlePool.get(i).render(g2d,c);
	      }
		displayBufferedImage();
		
		*/
		
	   }
	//method to create a file from a BufferedImage
	 private String dumpImageToFile(BufferedImage image) {

	        try {

	            String outputFilename = outputFilePrefix + counter + ".png";
	            counter++;
	            
	            ImageIO.write(image, "png", new File(outputFilename));

	            return outputFilename;

	        }

	        catch (IOException e) {

	            e.printStackTrace();

	            return null;

	        }

	    }
	//Method to display our BufferedImage
	 public void displayBufferedImage(){
		    this.setLayout(new BorderLayout());
			j.setVisible(false);
			j.setIcon(i);
			j.setVisible(true);
			j.setBorder(blackline);
			
			caPanel.add(j);
			//this.add(controlPanel, BorderLayout.WEST);
			this.add(caPanel, BorderLayout.EAST);
			
			repaint();
			this.validate();
		}
	
	 //Gets the average rgb value from an intARGB
	public int getAvgRGBval(int aRGB){
		Color c = new Color(aRGB);
		int red = c.getRed();
		int green = c.getGreen();
		int blue = c.getBlue();
		
		int avg = (red + green + blue) / 3;
		
		return avg;
		
	}
	
	//Gets the BufferedImages from a chosen video and performs edge detection on them using the ImageSnapListener
    public void readVideoFrames(final String inputFilename, final double frameRate){

    	Thread thread = new Thread(){
    		public void run(){
    			try{
    			IMediaReader mediaReader = ToolFactory.makeReader(inputFilename);
    	        // stipulate that we want BufferedImages created in BGR 24bit color space

    	        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
    	        ImageSnapListener listener = new ImageSnapListener(frameRate);
    	        mediaReader.addListener(listener);
    	        
    	        
    	        // read out the contents of the media file and
    	        // dispatch events to the attached listener

    	        while (mediaReader.readPacket() == null); //Do nothing
    			}catch(Exception e){
    				
    			}
    	 

    	    }

    		
    	};
    	thread.start();
        
 

    }
        		

 

    

 

}
