import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;


public class ImageSnapListener extends MediaListenerAdapter {
	
	/* The video stream index, used to ensure we display frames from one 
	 * and only one video stream from the media container.
	 */

    int mVideoStreamIndex = -1;
    
	final String outputFilePrefix = "images/frame_";
	
    // Time of last frame write
	private static long mLastPtsWrite = Global.NO_PTS;
	
    long microSecBetweenFrames;
    
    int counter = 1;
    
    double secondsBetweenFrames;
    
    
    
    
    /*
     * -------------------Constructor--------------------------------------*
     */
	public ImageSnapListener(double secondsBetweenFrames){
		
		this.secondsBetweenFrames = secondsBetweenFrames;
	
		microSecBetweenFrames = (long)(Global.DEFAULT_PTS_PER_SECOND * secondsBetweenFrames);
	}
	/*
	 * 	-------------------------------------------------------------------*
	 */

    public void onVideoPicture(IVideoPictureEvent event) {

        if (event.getStreamIndex() != mVideoStreamIndex) {

            // if the selected video stream id is not yet set, go ahead and select this lucky video stream
            if (mVideoStreamIndex == -1){
            	
                mVideoStreamIndex = event.getStreamIndex();
            // no need to show frames from this video stream

            }else{

                return;
            }
        }

        // if uninitialized, back date mLastPtsWrite to get the very first frame

        if (mLastPtsWrite == Global.NO_PTS)

            mLastPtsWrite = event.getTimeStamp() - microSecBetweenFrames;

        // if it's time to write the next frame

        if (event.getTimeStamp() - mLastPtsWrite >= microSecBetweenFrames) {
            
        	//run edge detection on the BufferedImage that the event received
            EdgeDetector e = new EdgeDetector(convertToType( event.getImage(), BufferedImage.TYPE_INT_ARGB ));
            dumpImageToFile(e.getProduct());

            // update last write time

            mLastPtsWrite += microSecBetweenFrames;

        }
        
    }

   
    //method to save image
    private void dumpImageToFile(BufferedImage image) {

        try {

            String outputFilename = outputFilePrefix + counter + ".png";
            counter++;
            
            ImageIO.write(image, "png", new File(outputFilename));

            

        }

        catch (IOException e) {

            e.printStackTrace();

           

        }

    }
    
    //method to convert bufferedImage to intARGB or whatever you want
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

