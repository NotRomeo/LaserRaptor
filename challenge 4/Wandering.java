package testpac;

import lejos.hardware.motor.Motor;
import lejos.robotics.subsumption.Behavior;

public class Wandering implements Behavior{

	private boolean suppressed = false;
	
	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		suppressed = false;
		Motor.D.backward();
		Motor.A.backward();
		while(!suppressed)
			Thread.yield();
		// cross the line
		for(int i=0; i < 100; i++)
		{
			Motor.D.backward();
			Motor.A.backward();
		}
		// clean up
		Motor.D.stop(true);
		Motor.A.stop(true);
	}

	@Override
	public void suppress() {
		suppressed = true;
		
	}

}
