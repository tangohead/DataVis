
public class DataPoint {
	private double position;
	private double angle;
	private double sonarDistance;
	
	public DataPoint(double position, double angle, double sonarDistance)
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
