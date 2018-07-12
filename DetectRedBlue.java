package testpac;

public class DetectRedBlue 
{
	private ColorSampleExample cse;
	
	public DetectRedBlue(ColorSampleExample colour)
	{
		cse = colour;
	}
	
	public String redOrBlue()
	{
		float[] rgb = cse.rgbSample();
		if(rgb[0] > rgb[2])
		{
			return "RED";
		}
		else if(rgb[2] >rgb[0])
		{
			return "BLUE";
		}
		else
			return null;
	}
}