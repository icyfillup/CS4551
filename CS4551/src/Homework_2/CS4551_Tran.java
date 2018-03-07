package Homework_2;

import java.util.Scanner;

public class CS4551_Tran
{
	public static void  AliasingRadialCirclePattern(int M, int N, int K) 
	{
		Image radialCircle = new Image(512, 512);

		int centerX = radialCircle.getW() / 2;
		int centerY = radialCircle.getH() / 2;
		
		
		for(int increment = 1; increment <= 5; increment++) 
		{
			int radiusSize = N * increment;
			
			for(int thicc = 0; thicc < M; thicc++) 
			{
				int thiccnessX = centerX + thicc;
				int thiccnessY = centerY + thicc;
				
				for(int deg = 0; deg <= 90; deg++) 
				{
					int[] rgb = new int[] {255, 255, 255};
					float radian = (float) ((Math.PI / 180.0) * (float)deg);
					
					float Quad1X = (float) (thiccnessX + (radiusSize * Math.cos(radian)));
					float Quad1Y = (float) (thiccnessY + (radiusSize * Math.sin(radian)));
					radialCircle.setPixel((int)Math.floor(Quad1X), (int)Math.floor(Quad1Y), rgb);
					
					int Quad2X = (int)Math.floor(centerX - Math.abs(centerX - Quad1X));
					int Quad2Y = (int)Math.floor(Quad1Y);
					radialCircle.setPixel(Quad2X, Quad2Y, rgb);
					
					int Quad3X = (int)Math.floor(centerX - Math.abs(centerX - Quad1X));
					int Quad3Y = (int)Math.floor(centerY - Math.abs(centerY - Quad1Y));
					radialCircle.setPixel(Quad3X, Quad3Y, rgb);
					
					int Quad4X = (int)Math.floor(Quad1X);
					int Quad4Y = (int)Math.floor(centerY - Math.abs(centerY - Quad1Y));
					radialCircle.setPixel(Quad4X, Quad4Y, rgb);
				}	
			}
		}
		
		
		radialCircle.display();
		
		System.out.println();
	}
	
	public static void AliasingCircle(Scanner input)
	{
		boolean UserInputDebugMode = true;
		if(UserInputDebugMode) 
		{
			int[] testingInput = new int[] {3, 20, 2};
//			int[] testingInput = new int[] {1, 20, 4};
//			int[] testingInput = new int[] {3, 20, 2};
//			int[] testingInput = new int[] {3, 20, 4};
//			int[] testingInput = new int[] {5, 40, 2};
//			int[] testingInput = new int[] {5, 40, 4};
			
			AliasingRadialCirclePattern(testingInput[0], testingInput[1], testingInput[2]); 
		}
		else 
		{
			// error checking for thickness
			String thiccnessS = "";
			do
			{
				System.out.print("Enter Thiccness of circle: ");
				thiccnessS = input.next();

				if (!thiccnessS.matches("[0-9]+"))
				{
					System.out.println("Invalid Input\n");
				}
			}
			while (!thiccnessS.matches("[0-9]+"));

			// error checking for distance
			String distanceS = "";
			do
			{
				System.out.print("Enter distance betweens circles: ");
				distanceS = input.next();

				if (!distanceS.matches("[0-9]+"))
				{
					System.out.println("Invalid Input\n");
				}
			}
			while (!distanceS.matches("[0-9]+"));

			// error checking for resize
			String resizeS = "";
			do
			{
				System.out.print("Enter resize value(2, 4, 8, 16): ");
				resizeS = input.next();

				if (!resizeS.matches("[0-9]+") || 
					!(resizeS.equals("2") || resizeS.equals("4") || 
					 resizeS.equals("8") || resizeS.equals("16")))
				{
					System.out.println("Invalid Input\n");
				}
			}
			while (!resizeS.matches("[0-9]+") || 
					!(resizeS.equals("2") || resizeS.equals("4") || 
					resizeS.equals("8") || resizeS.equals("16")));

			int thiccness = Integer.parseInt(thiccnessS);
			int distance = Integer.parseInt(distanceS);
			int resize = Integer.parseInt(resizeS);
			
			AliasingRadialCirclePattern(thiccness, distance, resize); 	
		}
		
		
	}

	public static boolean MainMenu(Scanner input)
	{
		System.out.println("Main Menu----------------------------");
		System.out.println("1. Aliasing");
		System.out.println("2. Dictionary Coding");
		System.out.println("3. Quit\n");
		System.out.print("Please enter the task number [1-3]: ");

		// String option = input.next();
		String option = "1";

		System.out.println();
		System.out.println();

		boolean isRunning = true;
		switch (option)
		{
			case "1":
			{
				isRunning = true;
				System.out.println("Aliasing");

				AliasingCircle(input);

			}
			break;

			case "2":
			{
				isRunning = true;
				System.out.println("Dictionary Coding");

			}
			break;

			case "3":
			{
				isRunning = false;
				System.out.println("Quit\n");
			}
			break;

			default:
			{
				isRunning = true;
				System.out.println("Invalid input.");
			}
			break;
		}

		return isRunning;
	}

	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		while (MainMenu(input))
		{}

		input.close();
		System.exit(0);
	}

}
