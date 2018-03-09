package Homework_2;

import java.util.Scanner;

public class CS4551_Tran
{
	public static boolean isCircleOutOfBound(Image circle, int X, int Y) 
	{
		boolean result = true;
		if(X < circle.getW() && X >= 0 && Y < circle.getH() && Y >= 0)
		{
			result = false;
		}
		
		return result;
	}
	
	public static Image RadialCirclePattern(int M, int N) 
	{
		Image radialCircle = new Image(512, 512);
		int[] white = new int[] {255, 255, 255};
		for(int y = 0; y < radialCircle.getH(); y++) 
		{
			for(int x = 0; x < radialCircle.getW(); x++) 
			{
				radialCircle.setPixel(x, y, white);
			}
		}

		int centerX = radialCircle.getW() / 2;
		int centerY = radialCircle.getH() / 2;
		int[] black = new int[] {0, 0, 0};
		
		int increment = 1;
		boolean expand = true;
		while(expand) 
		{
			expand = false;
			int radiusSize = N * increment;
			
			for(int thicc = 0; thicc < M; thicc++) 
			{
				int thiccnessX = centerX + thicc;
				int thiccnessY = centerY + thicc;
				
				for(float deg = 0; deg <= 90; deg += 0.01) 
				{
					float radian = (float) ((Math.PI / 180.0) * (float)deg);
					
					int Quad1X = (int) Math.round(thiccnessX + (radiusSize * Math.cos(radian)));
					int Quad1Y = (int) Math.round(thiccnessY + (radiusSize * Math.sin(radian)));
					boolean isQuad1OutOfBound = isCircleOutOfBound(radialCircle, Quad1X, Quad1Y);
					if(!isQuad1OutOfBound)
						radialCircle.setPixel(Quad1X, Quad1Y, black);
					
					int Quad2X = (int)Math.round(centerX - Math.abs(centerX - Quad1X));
					int Quad2Y = (int)Math.round(Quad1Y);
					boolean isQuad2OutOfBound = isCircleOutOfBound(radialCircle, Quad2X, Quad2Y);
					if(!isQuad2OutOfBound)
						radialCircle.setPixel(Quad2X, Quad2Y, black);
					
					int Quad3X = (int)Math.round(centerX - Math.abs(centerX - Quad1X));
					int Quad3Y = (int)Math.round(centerY - Math.abs(centerY - Quad1Y));
					boolean isQuad3OutOfBound = isCircleOutOfBound(radialCircle, Quad3X, Quad3Y);
					if(!isQuad3OutOfBound)
						radialCircle.setPixel(Quad3X, Quad3Y, black);
					
					int Quad4X = (int)Math.round(Quad1X);
					int Quad4Y = (int)Math.round(centerY - Math.abs(centerY - Quad1Y));
					boolean isQuad4OutOfBound = isCircleOutOfBound(radialCircle, Quad4X, Quad4Y);
					if(!isQuad4OutOfBound)
						radialCircle.setPixel(Quad4X, Quad4Y, black);
					
					expand = expand || !isQuad1OutOfBound || !isQuad2OutOfBound || !isQuad3OutOfBound || !isQuad4OutOfBound;
				}	
			}
			
			increment++;
		}
		
		return radialCircle;
	}
	
	public static void ResizeCirclePattern(Image originalCircle, int K)
	{
		Image resizeNoFilterCircle = new Image(originalCircle.getW() / K, originalCircle.getH() / K);
		Image resizeFilter1Circle = new Image(originalCircle.getW() / K, originalCircle.getH() / K);
		Image resizeFilter2Circle = new Image(originalCircle.getW() / K, originalCircle.getH() / K);
		
		int[] white = new int[] {255, 255, 255};
		for(int y = 0; y < resizeNoFilterCircle.getH(); y++) 
		{
			for(int x = 0; x < resizeNoFilterCircle.getW(); x++) 
			{
				resizeNoFilterCircle.setPixel(x, y, white);
				resizeFilter1Circle.setPixel(x, y, white);
				resizeFilter2Circle.setPixel(x, y, white);
			}
		}
		
		for(int y = 0; y < resizeNoFilterCircle.getH(); y++) 
		{
			int originalCircleOffsetY = y * K;
			for(int x = 0; x < resizeNoFilterCircle.getW(); x++) 
			{
				int originalCircleOffsetX = x * K;
				
				int[] rgb = new int[3];
				originalCircle.getPixel(originalCircleOffsetX, originalCircleOffsetY, rgb);
				resizeNoFilterCircle.setPixel(x, y, rgb);
				setFliter1(originalCircle, resizeFilter1Circle, 
							originalCircleOffsetX, originalCircleOffsetY, 
							x, y, rgb);
			}
		}
		
		originalCircle.display();
		resizeNoFilterCircle.display();
		resizeFilter1Circle.display();
		resizeFilter2Circle.display();
		System.out.println();
	}
	
	private static void setFliter1(Image originalCircle, Image resizeFilter1Circle, 
									int xOriginal, int yOriginal, 
									int xResize, int yResize,
									int[] rgb)
	{
		float[] weightRGB = new float[] {0, 0, 0};
		
		int topY = yOriginal - 1;
		
		int topLeftX = xOriginal - 1;
		int topCenterX = xOriginal;
		int topRightX = xOriginal + 1;
		fillInPixelWeightFilter1(originalCircle, weightRGB, topLeftX, topY);
		fillInPixelWeightFilter1(originalCircle, weightRGB, topCenterX, topY);
		fillInPixelWeightFilter1(originalCircle, weightRGB, topRightX, topY);

		int centerY = yOriginal;
		
		int leftX = xOriginal - 1;
		int centerX = xOriginal;
		int rightX = xOriginal + 1;
		fillInPixelWeightFilter1(originalCircle, weightRGB, leftX, centerY);
		fillInPixelWeightFilter1(originalCircle, weightRGB, centerX, centerY);
		fillInPixelWeightFilter1(originalCircle, weightRGB, rightX, centerY);
		
		int bottomY = yOriginal + 1;
		
		int bottomLeftX = xOriginal - 1;
		int bottomCenterX = xOriginal;
		int bottomRightX = xOriginal + 1;
		fillInPixelWeightFilter1(originalCircle, weightRGB, bottomLeftX, bottomY);
		fillInPixelWeightFilter1(originalCircle, weightRGB, bottomCenterX, bottomY);
		fillInPixelWeightFilter1(originalCircle, weightRGB, bottomCenterX, bottomY);
	
		int[] newWeightRGB = new int[] {Math.round(weightRGB[0]), 
										Math.round(weightRGB[1]), 
										Math.round(weightRGB[2])};
		
		resizeFilter1Circle.setPixel(xResize, yResize, newWeightRGB);
	}

	private static void fillInPixelWeightFilter1(Image circle, float[] weightRGB, int x, int y)
	{
		if(!isCircleOutOfBound(circle, x, y)) 
		{
			int[] oldRGB = new int[3];
			circle.getPixel(x, y, oldRGB);
			
			if(oldRGB[0] != 0)
				System.out.println();
			weightRGB[0] += oldRGB[0] * (1.0/9.0);
			weightRGB[1] += oldRGB[1] * (1.0/9.0);
			weightRGB[2] += oldRGB[2] * (1.0/9.0);
		}
	}

	public static void AliasingCirclePattern(Scanner input)
	{
		boolean UserInputDebugMode = true;
		if(UserInputDebugMode) 
		{
			int[] testingInput = new int[] {1, 20, 2};
//			int[] testingInput = new int[] {1, 20, 4};
//			int[] testingInput = new int[] {3, 20, 2};
//			int[] testingInput = new int[] {3, 20, 4};
//			int[] testingInput = new int[] {5, 40, 2};
//			int[] testingInput = new int[] {5, 40, 4};
			
			Image radialCircle = RadialCirclePattern(testingInput[0], testingInput[1]); 
			ResizeCirclePattern(radialCircle, testingInput[2]);
			radialCircle.display();
			
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
			
			Image radialCircle = RadialCirclePattern(thiccness, distance);
			ResizeCirclePattern(radialCircle, resize);
			radialCircle.display();
		}
		
		System.out.println();
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

				AliasingCirclePattern(input);

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
