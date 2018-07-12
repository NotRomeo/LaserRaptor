package testpac;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.subsumption.Behavior;

public class LineFollowing implements Behavior {

	ColorSampleExample cse;
	private int baseSpeed = 150;
	private int maxSpeed = 300;
	private double PGain = 300;
	private double DGain = 2000;
	private float thrivePoint;
	private float whiteValue;
	private float blackValue;
	private boolean suppressed = false;

	// Create constructor
	public LineFollowing(ColorSampleExample cse) {
			this.cse = cse;
			calibrate();
		}

	private void calibrate() {
		System.out.println("press to calibrate white value");
		Button.waitForAnyPress();
		float sum =  0f;
		for(int times = 0; times < 100; times++)
		{
			float[] midValue = cse.redSample();
			double measured = 0.0;
			for(float midVal : midValue)
				measured = midVal;
			sum += measured;
		}
		whiteValue = sum/100;
		System.out.println("white value calibrated");
		System.out.println("press to calibrate the black value");
		Button.waitForAnyPress();
		float sum2 =  0f;
		for(int times = 0; times < 100; times++)
		{
			float[] midValue = cse.redSample();
			double measured = 0.0;
			for(float midVal : midValue)
				measured = midVal;
			sum2 += measured;
		}
		blackValue = sum2/100;
		System.out.println("black value calibrate");
		thrivePoint = ((whiteValue + blackValue)/2);
	}


	@Override
	public boolean takeControl() {
		double sample = 0.0;
		float [] result = cse.redSample();
		for (float number : result)
			sample = number;
		return (thrivePoint - 0.1) < sample && (thrivePoint + 0.1) > sample;
	}

	@Override
	public void action() {
		double lastError = 0;
		double value = 2;
		suppressed = false;
		while(!suppressed) {
			int power = 0;
			double sample = 0.0;
			float [] result = cse.redSample();
			for (float number : result)
				sample = number;
			double error = thrivePoint - sample;
			if(Math.abs(whiteValue - error) < Math.abs(error - thrivePoint))
				value = 30;
			else if(Math.abs(error - blackValue) < Math.abs(thrivePoint - error))
				value = 30;
			power = (int) (value * (PGain * error + DGain * (error - lastError)));
			lastError = error;
			
			int leftSpeed = baseSpeed - power;
			int rightSpeed = baseSpeed + power;
			leftSpeed = Math.max(leftSpeed, 0);
			leftSpeed = Math.min(leftSpeed, maxSpeed);
			rightSpeed = Math.max(rightSpeed, 0);
			rightSpeed = Math.min(rightSpeed, maxSpeed);
			Motor.A.setSpeed(leftSpeed);
			Motor.D.setSpeed(rightSpeed);
			Motor.A.backward();
			Motor.D.backward();
			value = 2;
			Thread.yield();
		}
		/*
		// continue at least for 1.5 seconds in case the line was lost, otherwise go wander
		for(int i = 0; i<1500; i++)
		{
			int power = 0;
			double sample = 0.0;
			float [] result = cse.redSample();
			for (float number : result)
				sample = number;
			double error = thrivePoint - sample;
			if(Math.abs(whiteValue - error) < Math.abs(error - thrivePoint))
				value = 30;
			else if(Math.abs(error - blackValue) < Math.abs(thrivePoint - error))
				value = 30;
			power = (int) (value*(PGain * error + DGain * (error - lastError)));
			lastError = error;
			
			int leftSpeed = baseSpeed - power;
			int rightSpeed = baseSpeed + power;
			leftSpeed = Math.max(leftSpeed, 0);
			leftSpeed = Math.min(leftSpeed, maxSpeed);
			rightSpeed = Math.max(rightSpeed, 0);
			rightSpeed = Math.min(rightSpeed, maxSpeed);
			Motor.A.setSpeed(leftSpeed);
			Motor.D.setSpeed(rightSpeed);
			Motor.A.backward();
			Motor.D.backward();
			value = 2;
			Thread.yield();
		}
		*/
		// cleanup
		suppress();
		Motor.D.stop(true);
		Motor.A.stop(true);
		
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
}
