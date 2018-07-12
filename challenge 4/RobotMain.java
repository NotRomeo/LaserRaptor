package testpac;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class RobotMain {

	public static void main(String[] args) {
		/*
		ColorSampleExample cse = new ColorSampleExample();
		PoleScanning laserRaptorPole = new PoleScanning(cse);
		for(int i = 0; i<6; i++)
		{
			laserRaptorPole.toPillar();
		}
		BetterMusic.GhostBusters();
		*/
		
		ColorSampleExample cse = new ColorSampleExample();
		Behavior laserRaptorFol = new LineFollowing(cse);
		PoleScanning laserRaptorPole = new PoleScanning(cse);
		Behavior laserRaptorGrab = new Grabbing(laserRaptorPole);
		Behavior wandering = new Wandering();
		Behavior [] behaviorArray = {wandering, laserRaptorFol, laserRaptorPole, laserRaptorGrab};
		Arbitrator arbitrator = new Arbitrator(behaviorArray);
	    arbitrator.go();
	    
		/*
		UltrasonicSensor test = new UltrasonicSensor();
		for(int i=0; i<10; i++)
		{
			Button.waitForAnyPress();
			float[] sample = test.getSoundSample();
			System.out.println(sample[0]);
		}*/
		//ObjectDetective sherlock = new ObjectDetective();
		//sherlock.findPillars();
	}
}
