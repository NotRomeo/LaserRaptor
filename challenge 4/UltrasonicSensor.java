package testpac;

import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class UltrasonicSensor {
	EV3UltrasonicSensor sound;
	SampleProvider soundSample;
	float[] sample;
	
	public UltrasonicSensor()
	{
		sound = new EV3UltrasonicSensor(SensorPort.S3);
		soundSample = sound.getDistanceMode();
		sample = new float[sound.sampleSize()];		
	}
	public float[] getSoundSample()
	{
		sound.fetchSample(sample, 0);
		return sample;
	}
}
