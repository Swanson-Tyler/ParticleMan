
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

public class Particle {
   Random rand = new Random();
   public int x;
   public int y;
   public double dx;
   public double dy;
   public int dx2;
   public int dy2;
   public double size;
   public int life;
   public Color color;
   public float avgX;
   public float avgY;
   public boolean isDead;
   
   
   public Particle(int x, int y, double dx, double dy, double size, int life, Color c, float avgX, float avgY ){
	      this.x = x;
	      this.y = y;
	      this.dx = dx;
	      this.dy = dy;
	      this.size = size;
	      this.life = life;
	      this.color = c;
	      this.avgX = avgX;
	      this.avgY = avgY;
	      this.isDead = false;
	      
	    
	   }
   public void setX(int x){
	   this.x = x;
	   
   }
   public void setY(int y){
	   this.y = y;
   }
   public void setDx(int dx){
	   this.dx = dx;
   }
   public void setDy(int dy){
	   this.dy = dy;
   }
   public void setSize(int size){
	   this.size = size;
   }
   public void setLife(int life){
	   this.life = life;
   }
   public int getLife(){
	   return life;
   }
   public void setColor(Color color){
	   this.color = color;
   }
   public void setAvgX(float avgX){
	   this.avgX = avgX;
   }
   public void setAvgY(float avgY){
	   this.avgY = avgY;
   }
   public boolean isDead(){
	   return isDead;
   }

   public boolean update(){
	   	  int varX = (int) (rand.nextInt(2) - 1);
	   	  int varY = (int) (rand.nextInt(2) - 1);
	   	  
	      x += dx + (int) varX;
	      y += dy + (int) varY;
	   
	      
	      
	      
	      
	      life-=30;
	      
	      if((life <= 0) || (size <= 0))
	         return true;
	      return false;
	   }

   public void render(Graphics2D g2d, Color c){
	      
	   
	      g2d.setColor(c);
	      
	      g2d.fillOval(x,y,(int) size,(int) size);
	      
	   }
  

}
