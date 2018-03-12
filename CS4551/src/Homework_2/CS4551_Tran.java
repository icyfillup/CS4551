package Homework_2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CS4551_Tran
{
	public static final boolean UserInputDebugMode = true;
	
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
			int radiusSize = N * increment;
			
			for(int thicc = 0; thicc < M; thicc++) 
			{
				int thiccnessX = centerX + thicc;
				int thiccnessY = centerY + thicc;
				
				for(float deg = 0; deg <= 90; deg += 0.001) 
				{
					float radian = (float) ((Math.PI / 180.0) * (float)deg);
					
					int Quad1X = (int) Math.round(thiccnessX + (radiusSize * Math.cos(radian)));
					int Quad1Y = (int) Math.round(thiccnessY + (radiusSize * Math.sin(radian)));
					boolean isQuad1OutOfBound = isCircleOutOfBound(radialCircle, Quad1X, Quad1Y);
					
					if(!isQuad1OutOfBound) 
					{
						int Quad2X = (int)Math.round(centerX - Math.abs(centerX - Quad1X));
						int Quad2Y = (int)Math.round(Quad1Y);
						boolean isQuad2OutOfBound = isCircleOutOfBound(radialCircle, Quad2X, Quad2Y);
						
						if(!isQuad2OutOfBound) 
						{
							int Quad3X = (int)Math.round(centerX - Math.abs(centerX - Quad1X));
							int Quad3Y = (int)Math.round(centerY - Math.abs(centerY - Quad1Y));
							boolean isQuad3OutOfBound = isCircleOutOfBound(radialCircle, Quad3X, Quad3Y);	
							
							if(!isQuad3OutOfBound) 
							{
								int Quad4X = (int)Math.round(Quad1X);
								int Quad4Y = (int)Math.round(centerY - Math.abs(centerY - Quad1Y));
								boolean isQuad4OutOfBound = isCircleOutOfBound(radialCircle, Quad4X, Quad4Y);
								
								expand = expand && !isQuad1OutOfBound && !isQuad2OutOfBound && !isQuad3OutOfBound && !isQuad4OutOfBound;
					
								if(expand) 
								{
									radialCircle.setPixel(Quad1X, Quad1Y, black);
									radialCircle.setPixel(Quad2X, Quad2Y, black);
									radialCircle.setPixel(Quad3X, Quad3Y, black);
									radialCircle.setPixel(Quad4X, Quad4Y, black);	
								}
							}
							else 
								expand = false;
						}
						else
							expand = false;
					}
					else
						expand = false;
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
				setFliter2(originalCircle, resizeFilter2Circle, 
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
	
	private static void setFliter2(Image originalCircle, Image resizeFilter1Circle, 
			int xOriginal, int yOriginal, 
			int xResize, int yResize,
			int[] rgb)
	{
		float[] weightRGB = new float[] {0, 0, 0};
		
		int topY = yOriginal - 1;
		
		int topLeftX = xOriginal - 1;
		int topCenterX = xOriginal;
		int topRightX = xOriginal + 1;
		fillInPixelWeightFilter(originalCircle, weightRGB, topLeftX, topY, 1.0/16.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, topCenterX, topY, 2.0/16.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, topRightX, topY, 1.0/16.0);

		int centerY = yOriginal;
		
		int leftX = xOriginal - 1;
		int centerX = xOriginal;
		int rightX = xOriginal + 1;
		fillInPixelWeightFilter(originalCircle, weightRGB, leftX, centerY, 2.0/16.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, centerX, centerY, 4.0/16.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, rightX, centerY, 2.0/16.0);
		
		int bottomY = yOriginal + 1;
		
		int bottomLeftX = xOriginal - 1;
		int bottomCenterX = xOriginal;
		int bottomRightX = xOriginal + 1;
		fillInPixelWeightFilter(originalCircle, weightRGB, bottomLeftX, bottomY, 1.0/16.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, bottomCenterX, bottomY, 2.0/16.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, bottomCenterX, bottomY, 1.0/16.0);
	
		int[] newWeightRGB = new int[] {Math.round(weightRGB[0]), 
										Math.round(weightRGB[1]), 
										Math.round(weightRGB[2])};
		
		resizeFilter1Circle.setPixel(xResize, yResize, newWeightRGB);
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
		fillInPixelWeightFilter(originalCircle, weightRGB, topLeftX, topY, 1.0/9.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, topCenterX, topY, 1.0/9.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, topRightX, topY, 1.0/9.0);

		int centerY = yOriginal;
		
		int leftX = xOriginal - 1;
		int centerX = xOriginal;
		int rightX = xOriginal + 1;
		fillInPixelWeightFilter(originalCircle, weightRGB, leftX, centerY, 1.0/9.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, centerX, centerY, 1.0/9.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, rightX, centerY, 1.0/9.0);
		
		int bottomY = yOriginal + 1;
		
		int bottomLeftX = xOriginal - 1;
		int bottomCenterX = xOriginal;
		int bottomRightX = xOriginal + 1;
		fillInPixelWeightFilter(originalCircle, weightRGB, bottomLeftX, bottomY, 1.0/9.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, bottomCenterX, bottomY, 1.0/9.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, bottomCenterX, bottomY, 1.0/9.0);
	
		int[] newWeightRGB = new int[] {Math.round(weightRGB[0]), 
										Math.round(weightRGB[1]), 
										Math.round(weightRGB[2])};
		
		resizeFilter1Circle.setPixel(xResize, yResize, newWeightRGB);
	}

	private static void fillInPixelWeightFilter(Image circle, float[] weightRGB, int x, int y, double weight)
	{
		if(!isCircleOutOfBound(circle, x, y)) 
		{
			int[] oldRGB = new int[3];
			circle.getPixel(x, y, oldRGB);
			
			if(oldRGB[0] != 0)
				System.out.println();
			weightRGB[0] += oldRGB[0] * weight;
			weightRGB[1] += oldRGB[1] * weight;
			weightRGB[2] += oldRGB[2] * weight;
		}
	}

	public static void AliasingCirclePattern(Scanner input)
	{
		if(UserInputDebugMode) 
		{
//			int[] testingInput = new int[] {1, 20, 2};
//			int[] testingInput = new int[] {1, 20, 4};
//			int[] testingInput = new int[] {3, 20, 2};
//			int[] testingInput = new int[] {3, 20, 4};
//			int[] testingInput = new int[] {5, 40, 2};
			int[] testingInput = new int[] {5, 40, 4};
			
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
	
	public static void EncodeDenoteFileMessage(Scanner input) 
	{
		String fileName = null;
		if(UserInputDebugMode) 
		{
//			fileName = "LZW_test1.txt";
//			fileName = "LZW_test2.txt";
			fileName = "LZW_test3.txt";
//			fileName = "LZW_test4.txt";
		}
		else 
		{
			System.out.print("Enter filename: ");
			fileName = input.next();
		}
		
        String line = null;
        try {
        	String tempLine = null;

        	FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((tempLine = bufferedReader.readLine()) != null)
                line = tempLine;

            bufferedReader.close();
            fileReader.close();
            
            Set<String> initSymbols = new HashSet<>();
            List<Integer> encodedMessage = encoder(line, initSymbols);
            String decodedMessage = decoder(encodedMessage, initSymbols);
            
            if(UserInputDebugMode) checkDebugMessage(line, decodedMessage);
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
            // ex.printStackTrace();
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");                  
            // ex.printStackTrace();
        }
        
        System.out.println();
	}

	private static void checkDebugMessage(String line, String decodedMessage)
	{
		for(int index = 0; index < line.length(); index++) 
		{
			if(line.charAt(index) != decodedMessage.charAt(index)) 
			{
				System.out.println("not the same");
				break;
			}
				
		}
		System.out.println();
	}

	private static List<Integer> encoder(String line, Set<String> initSymbols)
	{
		//line = "ababababab";
		
		for(char symbol: line.toCharArray())
			initSymbols.add("" + symbol);
		
		List<String> dictionary = new ArrayList<>(initSymbols);
		
		String tempPastSymbols = "";
		List<Integer> encodedMessage = new ArrayList<>();
		for(char symbol: line.toCharArray()) 
		{
			String stream = tempPastSymbols + symbol;
			
			if(dictionary.contains(stream)) 
			{
				tempPastSymbols = stream;
			}
			else 
			{
				dictionary.add(stream);
				encodedMessage.add(dictionary.indexOf(tempPastSymbols));
				tempPastSymbols = "" + symbol;
			}
		}
		encodedMessage.add(dictionary.indexOf(tempPastSymbols));
		
		return encodedMessage;
	}

	private static String decoder(List<Integer> encodedMessage, Set<String> initSymbols)
	{
		List<String> dictionary = new ArrayList<>(initSymbols);
		
		String decodedMessage = "";
		int currentIndex;
		for(int encodedMessageIndex = 0; encodedMessageIndex < encodedMessage.size(); encodedMessageIndex++) 
		{
			currentIndex = encodedMessage.get(encodedMessageIndex);
			String currentEntry = dictionary.get(currentIndex);
			decodedMessage = decodedMessage + currentEntry;
			
			if(encodedMessageIndex + 1 < encodedMessage.size()) 
			{
				if(encodedMessage.get(encodedMessageIndex + 1) < dictionary.size()) 
				{
					String futureEntry = dictionary.get(encodedMessage.get(encodedMessageIndex + 1));
					
					dictionary.add(currentEntry + futureEntry.charAt(0));
				}
				else 
				{
					dictionary.add(currentEntry + currentEntry.charAt(0));
				}	
			}
			
		}
		
		/*
		int currentIndex = encodedMessage.get(0);
		String currentWord = dictionary.get(currentIndex);
		
		String decodedMessage = currentWord;
		
		int previousIndex = currentIndex;
		
		for(int encodedMessageIndex = 1; encodedMessageIndex < encodedMessage.size(); encodedMessageIndex++) 
		{
			currentIndex = encodedMessage.get(encodedMessageIndex);
			currentWord = dictionary.get(currentIndex);
			
			decodedMessage = decodedMessage + currentWord;
			
			String stream = dictionary.get(previousIndex) + dictionary.get(currentIndex).charAt(0);
			dictionary.add(stream);
			
			previousIndex = currentIndex;
		}
		*/
		return decodedMessage;
	}

	public static boolean MainMenu(Scanner input)
	{
		System.out.println("Main Menu----------------------------");
		System.out.println("1. Aliasing");
		System.out.println("2. Dictionary Coding");
		System.out.println("3. Quit\n");
		System.out.print("Please enter the task number [1-3]: ");

		// String option = input.next();
		String option = "2";

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

				EncodeDenoteFileMessage(input);
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
