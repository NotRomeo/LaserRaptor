package Assignment2;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;

public class TouchSampleExample {
	EV3TouchSensor touch;
	float[] sample;

	// Create constructor
	public TouchSampleExample() {
		// Set the sensor to the right port (example port 1)
		touch = new EV3TouchSensor(SensorPort.S1);
		// Initialize the array with the size of 1 sample.
		// A float is used since the value can be decimal
		sample = new float[touch.sampleSize()];
	}

	// The touch sensor is either pressed or not, hence the boolean return type
	public boolean checkTouched() {
		// This gets a sample from the sensor and stores it in sample
		touch.fetchSample(sample, 0);
		// Now we want to check whether the
		// retrieved sample says the sensor is pressed or not
		if (sample[0] == 1) {
			return true;
		}
		return false;
	}
}
