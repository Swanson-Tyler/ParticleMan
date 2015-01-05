import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IVideoPicture;


public class VideoWriter {
	int count = 1;
	int fps;
	int numFrames;
	int width;
	int height;
	String folderName;
	final String OUTPUT_NAME = "product";
	
	public VideoWriter(String folderName, int fps, int numFrames, int width, int height){
		this.width = width;
		this.height = height;
		this.numFrames = numFrames;
		this.fps = fps;
		this.folderName = folderName;
		createVideo(folderName, fps);
		
	}
	public void createVideo(String folderName, int fps){
		IMediaWriter writer = ToolFactory.makeWriter("product.mov");
		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, width,height);
		long startTime = System.nanoTime();
		for(int i = 0; i < numFrames; i++){
			String filePath = folderName + "frame_" + (i + 1) + ".png";
			BufferedImage image = convertToType(getImageFromFile(filePath), BufferedImage.TYPE_3BYTE_BGR);
			writer.encodeVideo(0, image, System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
			try {
				    Thread.sleep((long) (1000 / fps));

	
				}catch (InterruptedException e) {						
				    // ignore

				}

		}	
		writer.close();
	}
	public BufferedImage getImageFromFile(String filename){
		BufferedImage image;
		
        try {
            // try to read from file in working directory
            File file = new File(filename);
            if (file.isFile()) {
                image = ImageIO.read(file);
            }

            // now try to read from file in same directory as this .class file
            else {
                URL url = getClass().getResource(filename);
                if (url == null) { url = new URL(filename); }
                image = ImageIO.read(url);
            }
            
        }
        catch (IOException e) {
            // e.printStackTrace();
            throw new RuntimeException("Could not open file: " + filename);
        }
        return image;
    }
	
	 public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
	    	
	        BufferedImage image;
	
	        // if the source image is already the target type, return the source image
	
	        if (sourceImage.getType() == targetType) {
	
	            image = sourceImage;
	
	        }
	
	        // otherwise create a new image of the target type and draw the new image
	
	        else {
	
	            image = new BufferedImage(sourceImage.getWidth(),
	
	                 sourceImage.getHeight(), targetType);
	
	            image.getGraphics().drawImage(sourceImage, 0, 0, null);
	
	        }
	
	 
	       
	        return image;

	         
	
	    }

		
	
}
