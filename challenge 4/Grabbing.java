package testpac;

import lejos.hardware.motor.Motor;
import lejos.robotics.subsumption.Behavior;

public class Grabbing implements Behavior{
	
	UltrasonicSensor sound;
	PoleScanning scan;
	boolean grabbing = true;
	boolean suppressed = false;
	
	public Grabbing(Behavior laserRaptorPole)
	{
		this.scan = (PoleScanning) laserRaptorPole;
		sound = scan.ultra();
	}
	
	private void grab()
	{
		Motor.C.setSpeed(1200f);
		for (int i=0; i<28500; i++)
			Motor.C.forward();
		Motor.C.stop(true);
		scan.setPoleFound(false);
		suppress();
	}
	private void release()
	{
		Motor.C.setSpeed(1200f);
		for (int k=0; k<84000; k++)
			 Motor.C.backward();
		Motor.C.stop(true);
		suppress();
	}
	@Override
	public boolean takeControl() {
		float [] dist = sound.getSoundSample();
		return scan.poleFound() && dist[0] < 0.08;
	}
	@Override
	public void action() {
		suppressed = false;
		if(grabbing)
		{
			grab();
			grabbing = false;
		}
		else
		{
			release();
			grabbing = true;
		}
		while(!suppressed && Motor.C.isMoving())
			Thread.yield();
		Motor.C.stop(true);
		suppress();
	}
	@Override
	public void suppress() {
		suppressed = true;
		
	}
}
