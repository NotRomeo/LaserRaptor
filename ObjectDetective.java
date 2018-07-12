package testpac;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class ObjectDetective {

	Gyrosensors rotate;
	UltrasonicSensor sound;
	DetectRedBlue colours;
	private int BasicSpeed = 300;
	private int MaximumSpeed = 550;
	double distanceToPillar = 0.0;
	
	// Create constructor
	public ObjectDetective(ColorSampleExample cse) 
	{
		Button.waitForAnyPress();
		sound = new UltrasonicSensor();
		rotate = new Gyrosensors();
		colours = new DetectRedBlue(cse);
	}
	public void scanForPosition()
	{
		double topLimit = 1.5;
		distanceToPillar = 0.0;
		//System.out.println("Scanning");
		Motor.A.setSpeed(BasicSpeed - 100);
		rotate.resetGyro();
		
		boolean found = false;
		boolean turnedAround = false;
		
		while(!found && !turnedAround)
		{
			float thisAngle = 0;
			float[] angle = rotate.getRotation();
			for(float x: angle)
			{
				thisAngle = x;
			}
			if(thisAngle <= -360)
			{
				turnedAround = true;
//				System.out.println(distanceToPillar);
				Motor.A.stop(true);
				if(distanceToPillar == 0.0)
					wander();
			}
			else
			{
				float[] distance = sound.getSoundSample();
				for(float x: distance)
				{
					if(x < (float)topLimit && x > 0.0)
					{
						distanceToPillar = x;
						found = true;
					}
				}
				if(!found)
					Motor.A.forward();
			}
		}
		Motor.A.stop(true);
		approach();
	}
	
	public void wander()
	{
		//System.out.println("Wandering now");
		Motor.D.setSpeed(BasicSpeed);
		Motor.A.setSpeed(MaximumSpeed);
		for(int i = 30000; i > 0; i--)
		{
			Motor.A.forward();
			Motor.D.forward();
		}
		Motor.A.stop(true);
		Motor.D.stop(true);
		rotate.resetGyro();
		scanForPosition();
	}
	
	public void approach()
	{
		//System.out.println("Approaching");
		Motor.A.setSpeed(BasicSpeed);
		Motor.D.setSpeed(BasicSpeed);
		
		while(distanceToPillar > 0.08)
		{
			if(distanceToPillar > 1.5)
			{
				Motor.A.stop(true);
				Motor.D.stop(true);
				scanForPosition();
				break;
			}
			else
			{
				Motor.A.forward();
				Motor.D.forward();
				float[] measure = sound.getSoundSample();
				for(float x : measure)
				{
					distanceToPillar = (double)x;
				}	
			}
		}
		Motor.A.stop(true);
		Motor.D.stop(true);
	}
	
	public void toPillar()
	{
		scanForPosition();
		System.out.println("Found one");
		Delay.msDelay(3000);
	}
}