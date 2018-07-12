package testpac;

import lejos.robotics.subsumption.Behavior;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;

public class PoleScanning implements Behavior{
	
	UltrasonicSensor sound;
	Gyrosensors rotate;
	DetectRedBlue colours;
	private boolean poleFound = false;
	private boolean suppressed = false;
	private int BasicSpeed = 300;
	private int MaximumSpeed = 550;
	private double distanceToPillar = 0.0;
	
	public PoleScanning(ColorSampleExample cse)
	{
		System.out.println("press to start the ultrasonic sensor");
		Button.waitForAnyPress();
		sound = new UltrasonicSensor();
		rotate = new Gyrosensors();
		colours = new DetectRedBlue(cse);
	}
	
	@Override
	public boolean takeControl() {
		float [] dist = sound.getSoundSample();
		return dist[0] < 0.25 && !poleFound;
	}

	@Override
	public void suppress() {
		suppressed = true;
		
	}
	@Override
	public void action()
	{
		suppressed = false;
		System.out.println("PillarSearch");
		toPillar();
		while (!suppressed)
		{
			Thread.yield();
		}
		//cleanup
		Motor.A.stop(true);
		Motor.D.stop(true);
	}
	
	public UltrasonicSensor ultra()
	{
		return sound;
	}
	public boolean poleFound()
	{
		return poleFound;
	}
	public void setPoleFound(boolean found)
	{
		poleFound = found;
	}
	//
	public void scanForPosition()
	{
		double topLimit = 1.5;
		distanceToPillar = 0.0;
		System.out.println("Scanning");
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
					Motor.A.backward();
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
			Motor.A.backward();
			Motor.D.backward();
		}
		Motor.A.stop(true);
		Motor.D.stop(true);
		rotate.resetGyro();
		scanForPosition();
	}
	
	public void approach()
	{
		System.out.println("Approaching");
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
				Motor.A.backward();
				Motor.D.backward();
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
		distanceToPillar = 0.25;
		approach();
		String colour = colours.redOrBlue();
		if(colour.equals("RED"))
		{
			// drive left
			 for(int i=0; i< 2000; i++)
			 {	
				 Motor.D.setSpeed(300);
				 Motor.A.setSpeed(100);
				 Motor.D.backward();
				 Motor.A.backward();
			 } 
		}
		//drive right
		else if(colour.equals("BLUE"))
		{
			for(int i=0; i< 2000; i++)
			 {	
				 Motor.D.setSpeed(100);
				 Motor.A.setSpeed(300);
				 Motor.D.backward();
				 Motor.A.backward();
			 }
		}
		Motor.A.stop(true);
		Motor.D.stop(true);
		poleFound = true;
		System.out.println("Found one");
		suppress();
	}
}
