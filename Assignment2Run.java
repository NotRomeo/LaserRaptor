package Assignment2;

public class Assignment2Run 
{
	public static void main(String[] args)
	{
		LineFollower bot = new LineFollower();
		bot.autoCalibrate();
		bot.followLine();
	}
}
