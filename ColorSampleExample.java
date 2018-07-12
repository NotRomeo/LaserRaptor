package testpac;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

class ColorSampleExample {
	// Use either the red or the rgb names in this example.
	// For convenience we have given both
	EV3ColorSensor colorSensor;
	SampleProvider redProvider;
	SampleProvider rgbProvider;
	float[] redSample;
	float[] rgbSample;

	// Create constructor
	public ColorSampleExample() {
		// Set the sensor to the right port (example port 2)
		colorSensor = new EV3ColorSensor(SensorPort.S1);
		// Note that the redSample will contain 1 value in the array while
		// the rgbSample will contain 3, the red, green, and blue value.
		// The red mode version
		redProvider = colorSensor.getRedMode();
		redSample = new float[redProvider.sampleSize()];
		// The rgb mode version
		rgbProvider = colorSensor.getRGBMode();
		rgbSample = new float[rgbProvider.sampleSize()];
	}

	// The red mode version
	public float[] redSample() {
		redProvider.fetchSample(redSample, 0);
		return redSample;
	}

	// The rgb mode version
	public float[] rgbSample() {
		rgbProvider.fetchSample(rgbSample, 0);
		return rgbSample;
	}
}