import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.subsumption.Behavior;

public class LineFollowerAlexxx implements Behavior
{
	private static ColorSampleExample cse = new ColorSampleExample();
	private int BasicSpeed = 300;
	private int maximumSpeed = 550;
	private double PGain = 300;
	private double DGain = 2000;
	private float middleValue;
	
	private Gyrosensors gyrosensor = new Gyrosensors();
	private float[] oldRotation = gyrosensor.getRotation();
	private int countGyroTurns = 0;
	
	private boolean suppressed = false;
	
	public LineFollowerAlexxx()
	{
		
	}
	public boolean takeControl()
	{
		return true;
	}
	
	public void suppress()
	{
		suppressed = true;
	}
	
	public void action()
	{
		suppressed = false;
		Motor.A.forward();
		Motor.D.forward();
		while (!suppressed)
		{
			Thread.yield();
		}
		Motor.A.stop();
		Motor.D.stop();
	}
	
	public void followLine()
	{
		double formerError = 0.0;
		while(countGyroTurns <= 5)
		{
			int speed = 0;
			double reading = 0.0;
			float[] result =cse.redSample();
			for(float number : result)
				reading = number;
			
			double error = middleValue - reading;
			speed = (int) (7*(PGain * error + DGain * (error - formerError))); 
			formerError = error;
			
			int rightSpeed = BasicSpeed + speed;
			int leftSpeed = BasicSpeed - speed;
			
			//Now we need the speeds to have a value between their max and min values
			if (leftSpeed < 0)
				leftSpeed = 0;
			else if(leftSpeed > maximumSpeed)
				leftSpeed = maximumSpeed;
			
			if (rightSpeed < 0)
				rightSpeed = 0;
			else if(rightSpeed > maximumSpeed)
				rightSpeed = maximumSpeed;
			
			// get Gyrosensor data
			float[] newRotation = gyrosensor.getRotation();
			if(corner(newRotation, oldRotation)) {
				countGyroTurns++;
				oldRotation = newRotation;
				System.out.println("new Corner!");
			}
			
			Motor.D.setSpeed(rightSpeed);
			Motor.A.setSpeed(leftSpeed);
			
			Motor.D.backward();
			Motor.A.backward();
		}
	}
	
	// get Gyrosensor degrees and when changed about 90 degrees return true otherwise false
	private static boolean corner(float[] newRotation, float[] oldRotation) {
		System.out.println("Array Length: " + newRotation.length);
		
		for(float x : newRotation) {
			System.out.println("new: " + x);
		}
		for(float x : oldRotation) {
			System.out.println("old: " + x);
		}
		return newRotation[0] > oldRotation[0]+70 || 
			   newRotation[0] < oldRotation[0]-70;
	}
	
	public void autoCalibrate()
	{
		Button.waitForAnyPress();
		float sum = (float) 0.0;
		for(int times = 0; times < 100; times++)
		{
			float[] midValue = cse.redSample();
			double measured = 0.0;
			for(float midVal : midValue)
				measured = midVal;
			sum += measured;
		}
		this.middleValue = sum/100;
	}
}