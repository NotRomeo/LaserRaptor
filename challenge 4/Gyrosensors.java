package testpac;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Gyrosensors {

	EV3GyroSensor gyroSensor;
	SampleProvider rotation;
	float[] sample;
	
	public Gyrosensors()
	{
		gyroSensor = new EV3GyroSensor(SensorPort.S2);
		rotation = gyroSensor.getAngleMode();
		sample = new float[rotation.sampleSize()];
	}
	
	public float[] getRotation()
	{
		rotation.fetchSample(sample, 0);
		return sample;
	}
	public void resetGyro() {
		gyroSensor.reset();
		Delay.msDelay(500);
	}
 }

