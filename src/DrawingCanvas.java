import java.awt.*;

import javax.swing.*;
public class DrawingCanvas extends Canvas{
	Canvas drawCan;
	Graphics graphics;
	JFrame frame;
	
    public DrawingCanvas(int x, int y) {
//    	drawCan = new Canvas();
//    	drawCan.setFocusable(false);
    	
        frame = new JFrame();
        frame.setFocusable(true);
        frame.setSize(x, y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.setVisible(true);
        
        //graphics = drawCan.getGraphics();
//        drawCan.setBackground(Color.WHITE);
//        
//        graphics.setColor(Color.black);
//        graphics.drawLine(40, 30, 330, 380);
//        graphics.drawRect(100, 300, 200, 400);
    }
 
    public void paint(Graphics graphics) {
    	//System.out.println("Painting the canvas...");
//        graphics.setColor(Color.black);
//        graphics.drawLine(40, 30, 330, 380);
 
       // graphics.
    }
    
    public void drawLine(double startX, double startY, double endX, double endY, Color color)
    {
    	
    	Graphics graphics = this.getGraphics();
    	graphics.setColor(color);
    	graphics.drawLine((int)startX, (int)startY, (int)endX, (int)endY);
    }
    
    public void drawText(String str, double x, double y, Color color)
    {
    	Graphics graphics = this.getGraphics();
    	graphics.setColor(color);
    	graphics.drawString(str, (int)x, (int)y);
    }
    
    public void drawLine(double startX, double startY, double endX, double endY, Color color, int thickness)
    {
    	Graphics2D g2D = (Graphics2D)this.getGraphics();
    	g2D.setStroke(new BasicStroke(thickness));
    	g2D.setColor(color);
    	g2D.drawLine((int)startX, (int)startY, (int)endX, (int)endY);
    }
    
    
    public void drawCircle(double x, double y, int diam, Color color)
    {
    	Graphics graphics = this.getGraphics();
    	graphics.setColor(color);
    	graphics.drawOval((int)x, (int)y, diam, diam);
    }
 

    
    
}
