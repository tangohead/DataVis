import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.swing.JFrame;


public class RobotDataVisualiser{
	final static int MAX_WIDTH = 1200;
	final static int MAX_HEIGHT = 400;
	
	final static int X_OFFSET = 30;
	
	static int X_PAD; 
	final static int ROBOT_LINE_OFFSET = 300;
	final static int WALL_Y_PAD = 4;
	final static int WALL_OFFSET = 300;

	final static int DOT_WIDTH = 3;
	
	public static void main(String[] args) throws InterruptedException
	{
		try {
			ArrayList<DataPoint> data = CSVReader("CS313Group1SPoutput.csv");
			ArrayList<Double> avgSonar = MovAvg3(data);
			ArrayList<Double> avgSonar5Pt = MovAvg5(data);
			
			DrawingCanvas d = new DrawingCanvas(MAX_WIDTH, MAX_HEIGHT);

			
			X_PAD = (int)Math.floor((MAX_WIDTH - (X_OFFSET*2)) / (data.size()));
			//X_PAD = 10;
			System.out.println("[DEBUG]: XPAD "+ X_PAD);
			for(int i = 1; i < data.size(); i++)
			{
				
				drawRobotLine(d, data.get(i-1).getPosition(), data.get(i).getPosition(), Color.DARK_GRAY);
				
				
				drawSonarLine(d, data.get(i-1).getPosition(), data.get(i).getPosition(), data.get(i-1).getSonarDistance(), data.get(i).getSonarDistance(), Color.RED);
				
				drawSonarLine(d, data.get(i-1).getPosition(), data.get(i).getPosition(), avgSonar.get(i-1), avgSonar.get(i), Color.BLACK);
				
				drawSonarLine(d, data.get(i-1).getPosition(), data.get(i).getPosition(), avgSonar5Pt.get(i-1), avgSonar5Pt.get(i), Color.GREEN);
				
				System.out.println("[DEBUG]: " + i % (data.size() * 0.1));
				if(i % (int)(data.size() * 0.1) == 0)
				{
					d.drawLine(data.get(i).getPosition() * X_PAD + X_OFFSET, ROBOT_LINE_OFFSET, 
							data.get(i).getPosition() * X_PAD + X_OFFSET, WALL_OFFSET - avgSonar5Pt.get(i)*WALL_Y_PAD, Color.BLUE);
					
					d.drawLine(data.get(i).getPosition() * X_PAD + X_OFFSET, ROBOT_LINE_OFFSET, data.get(i).getPosition() * X_PAD + X_OFFSET - 7, ROBOT_LINE_OFFSET - 15, Color.BLUE);
					d.drawLine(data.get(i).getPosition() * X_PAD + X_OFFSET, ROBOT_LINE_OFFSET, data.get(i).getPosition() * X_PAD + X_OFFSET + 7, ROBOT_LINE_OFFSET - 15, Color.BLUE);
					
					d.drawLine(data.get(i).getPosition() * X_PAD + X_OFFSET, WALL_OFFSET - avgSonar5Pt.get(i)*WALL_Y_PAD, data.get(i).getPosition() * X_PAD + X_OFFSET - 7, WALL_OFFSET - avgSonar5Pt.get(i)*WALL_Y_PAD + 15, Color.BLUE);
					d.drawLine(data.get(i).getPosition() * X_PAD + X_OFFSET, WALL_OFFSET - avgSonar5Pt.get(i)*WALL_Y_PAD, data.get(i).getPosition() * X_PAD + X_OFFSET + 7, WALL_OFFSET - avgSonar5Pt.get(i)*WALL_Y_PAD + 15, Color.BLUE);
					
					d.drawText(data.get(i).getPosition() + "cm", data.get(i).getPosition() * X_PAD + X_OFFSET - 10, ROBOT_LINE_OFFSET + 20, Color.BLUE);
					d.drawText(avgSonar5Pt.get(i) + "cm", data.get(i).getPosition() * X_PAD + X_OFFSET + 2, ROBOT_LINE_OFFSET - 40, Color.BLUE);
				}
				Thread.sleep(75);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR]: Could not parse Data CSV - " + e.getMessage());
		}
		
		
		//d.paint();
		
	}
	
	private static void drawSonarLine(DrawingCanvas d, double robotPos, double robotPos2, double sonarDist, double sonarDist2, Color color)
	{
		d.drawCircle(robotPos2 * X_PAD + X_OFFSET, WALL_OFFSET - sonarDist2 *WALL_Y_PAD, DOT_WIDTH, color);
		d.drawLine(robotPos * X_PAD + X_OFFSET, WALL_OFFSET - sonarDist*WALL_Y_PAD, 
					robotPos2 * X_PAD + X_OFFSET, WALL_OFFSET - sonarDist2*WALL_Y_PAD, color);
	}
	
	private static void drawRobotLine(DrawingCanvas d, double robotPos, double robotPos2, Color color)
	{
		d.drawCircle(robotPos2 * X_PAD + X_OFFSET, ROBOT_LINE_OFFSET-2, DOT_WIDTH, color);
		d.drawLine(robotPos * X_PAD + X_OFFSET, ROBOT_LINE_OFFSET, 
				robotPos2 * X_PAD + X_OFFSET, ROBOT_LINE_OFFSET, color, 4);
	}
	
	private static ArrayList<DataPoint> CSVReader(String filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
		String line = "";
		ArrayList<DataPoint> data = new ArrayList<DataPoint>();
		
		while((line = br.readLine()) != null)
		{
			System.out.println("[READ]: " + line);
			String[] csvLine = line.split(",");
			data.add(new DataPoint(Double.parseDouble(csvLine[0]), 
									Double.parseDouble(csvLine[1]), 
									Double.parseDouble(csvLine[2])));
		}
		
		return data;
		
	}
	
	private static ArrayList<Double> MovAvg3(ArrayList<DataPoint> data)
	{
		ArrayList<Double> avg = new ArrayList<Double>();
		
		for(int i = 0; i < data.size(); i++)
		{
			double val = 0;
			if(i == 0)
			{
				val = (data.get(i).getSonarDistance() + data.get(i+1).getSonarDistance())/2;
			}
			else if(i == data.size() - 1)
			{
				val = (data.get(i).getSonarDistance() + data.get(i-1).getSonarDistance())/2;
			}
			else
			{
				val = (data.get(i).getSonarDistance() + data.get(i-1).getSonarDistance() + data.get(i+1).getSonarDistance())/3;
			}
			avg.add(val);		
		}
		
		return avg;
	}
	
	private static ArrayList<Double> MovAvg5(ArrayList<DataPoint> data)
	{
		ArrayList<Double> avg = new ArrayList<Double>();
		
		for(int i = 0; i < data.size(); i++)
		{
			double val = 0;
			if(i == 0)
			{
				val = (data.get(i).getSonarDistance() + data.get(i+1).getSonarDistance() + data.get(i+2).getSonarDistance())/3;
			}
			else if(i == 1)
			{
				val = ( data.get(i-1).getSonarDistance() + data.get(i).getSonarDistance() + data.get(i+1).getSonarDistance() + data.get(i+2).getSonarDistance())/4;
			}
			else if(i == data.size() - 1)
			{
				val = (data.get(i).getSonarDistance() + data.get(i-1).getSonarDistance() + data.get(i-2).getSonarDistance())/3;
			}
			else if(i == data.size() - 2)
			{
				val = (data.get(i+1).getSonarDistance() + data.get(i).getSonarDistance() + data.get(i-1).getSonarDistance() + data.get(i-2).getSonarDistance())/4;
			}
			else
			{
				val = (data.get(i).getSonarDistance() + data.get(i-1).getSonarDistance() + data.get(i+1).getSonarDistance()+ data.get(i-2).getSonarDistance() + data.get(i+2).getSonarDistance())/5;
			}
			avg.add(val);		
		}
		
		return avg;
	}

}

class DrawingCanvas extends Canvas{
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

class DataPoint {
	private double position;
	private double angle;
	private double sonarDistance;
	
	DataPoint(double position, double angle, double sonarDistance)
	{
		this.position = position;
		this.angle = angle;
		this.sonarDistance = sonarDistance;
	}
	
	public double getPosition() {
		return position;
	}
	public double getAngle() {
		return angle;
	}
	public double getSonarDistance() {
		return sonarDistance;
	}
}


