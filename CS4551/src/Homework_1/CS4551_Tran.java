package Homework_1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import setupImageProject.Image;

public class CS4551_Tran
{

	public static boolean MainMain(Scanner input, Image image, String filename) 
	{
		System.out.println("Main Menu-----------------------------------\r\n" + 
				"1. Conversion to Gray - scale Image (24bits -> 8bits)\r\n" + 
				"2. Conversion to N-level Image \r\n" + 
				"3. Conversion to 8-bit Indexed Color Image using Uniform Color Quantization(24bits -> 8bits)\r\n" + 
				"4. Conversion to 8-bit Indexed Color Image using [your selected method](24bits->8bits)\r\n" + 
				"5. Quit");
		
		System.out.print("Please enter the task number [1 - 5]: ");
		String option = input.next();
//		String option = "3";
		System.out.println();
		
		boolean isRunning = true;
		switch(option) 
		{
			case "1":
			{
				System.out.println("Conversion to Gray - scale Image (24bits -> 8bits):");
				Image GrayScaleImage = GrayScaleConversion(image);
				GrayScaleImage.write2PPM("grayscale.ppm");
				GrayScaleImage.display();
				System.out.println();
				
				isRunning = true;
			}break;
			
			case "2":
			{
				System.out.println("Conversion to N-level Image:");
				String nLevelS = null;
				
				boolean isAInput = false;
				do 
				{
					System.out.print("Please enter the N-level [2 (1 bit), 4 (2 bit), 8 (3 bit), 16 (4 bit)]: ");
					nLevelS = input.next();
					
					if(nLevelS.equals("2") || nLevelS.equals("4") || 
							nLevelS.equals("8") || nLevelS.equals("16"))
					{
						isAInput = true;
					}
				}
				while(!isAInput);
				
				int nLevel = Integer.parseInt(nLevelS);
//				int nLevel = Integer.parseInt("4");
				Image NLevelImage = NLevelConversion(image, nLevel);
				Image NLevelErrorDiffusionImage = NLevelErrorDiffusionConversion(image, nLevel);

				NLevelImage.write2PPM("Output1.ppm");
				NLevelImage.display();
				
				NLevelErrorDiffusionImage.write2PPM("Output2.ppm");
				NLevelErrorDiffusionImage.display();
				
				System.out.println();
				isRunning = true;
			}break;
			
			case "3":
			{
				System.out.println("Conversion to 8-bit Indexed Color Image using Uniform Color Quantization(24bits -> 8bits):");
				
				Image UCQ = UniformColorQuantization(image, filename);
				UCQ.write2PPM(filename + "-QT8.ppm");
				UCQ.display();
				
				System.out.println();
				isRunning = true;
			}break;
			
			case "4":
			{
				System.out.println("Conversion to 8-bit Indexed Color Image using [your selected method](24bits->8bits):");
				
				System.out.println();
				isRunning = true;
			}break;
			
			case "5":
			{
				System.out.println();
				System.out.println("Quiting Program...\n");
				isRunning = false;
			}break;
			
			default:
			{
				System.out.println("Invalid option\n");
				isRunning = true;
			}break;
		}
		
		return isRunning;
	}
	
	private static Image UniformColorQuantization(Image image, String filename)
	{
		final int MAX_BIT_VALUE = 256;
		
		System.out.println("index\tred\tgreen\tblue");
		System.out.println("----------------------------");

		Map<Integer, int[]> lookUpTable = new HashMap<>();
		for(int index = 0; index < MAX_BIT_VALUE; index++) 
		{
			int redIndex = (0xff & (index & 224)) >> 5;
			int greenIndex = (0xff & (index & 28)) >> 2;
			int blueIndex = (0xff & (index & 3));
			
			int red = (redIndex * 32) + 16;
			int green = (greenIndex * 32) + 16;
			int blue = (blueIndex * 64) + 32;
		
			lookUpTable.put(index, new int[] {red, green, blue});
			
			System.out.println(index + "\t" + red + "\t" + green + "\t" + blue);
		}
		
		Image indexImage = new Image(image.getW(), image.getH());
		for(int y = 0; y < image.getH(); y++) 
		{
			for(int x = 0; x < image.getW(); x++) 
			{
				int[] rgb = new int[3];
				image.getPixel(x, y, rgb);
				
				int redIndex = SegmentIndex(MAX_BIT_VALUE, 8, rgb[0]);
				int greenIndex = SegmentIndex(MAX_BIT_VALUE, 8, rgb[1]);
				int blueIndex = SegmentIndex(MAX_BIT_VALUE, 4, rgb[2]);
				
				int colorIndex = 0xFF & ((redIndex << 5) | (greenIndex << 2) | blueIndex);
				
				indexImage.setPixel(x, y, new int[]{colorIndex, colorIndex, colorIndex});
			}
		}
		
		Image indexColorImage = new Image(image.getW(), image.getH());
		for(int y = 0; y < image.getH(); y++) 
		{
			for(int x = 0; x < image.getW(); x++) 
			{
				int[] rgb = new int[3];
				indexImage.getPixel(x, y, rgb);
				indexColorImage.setPixel(x, y, lookUpTable.get(rgb[0]));
			}
		}

		indexImage.write2PPM(filename+"-index.ppm");
		return indexColorImage;
	}

	private static int SegmentIndex(int bitValue, int segment, int colorValue)
	{
		int indexResult = 0;
		int redSegmentInterval = bitValue / segment;
		while(indexResult < segment) 
		{
			int increment = ((indexResult + 1) * redSegmentInterval);
			if (colorValue < increment) 
			{
				break;
			}
			indexResult++;
		}
		return indexResult;
	}

	private static Image NLevelErrorDiffusionConversion(Image image, int nLevel)
	{
//		nLevel = 2;
		
		final int BIT_VALUE = 256;
		final double GRAY_VALUE = BIT_VALUE - 1;
		double grayInterval = GRAY_VALUE / (nLevel - 1.0);
		double grayCount = 0;
		ArrayList<Double> grayValues = new ArrayList<>();
		grayValues.add(grayCount);
		while(grayCount < GRAY_VALUE) 
		{
			grayCount = grayCount + grayInterval;
			grayValues.add(grayCount);
		}
		
		Image grayScaleImage = GrayScaleConversion(image);
		
		Image NLevelImage = new Image(grayScaleImage.getW(), grayScaleImage.getH());
		for(int y = 0; y < image.getH(); y++) 
		{
			for(int x = 0; x < image.getW(); x++) 
			{
				int[] rgb = new int[3];
				grayScaleImage.getPixel(x, y, rgb);

				double grayValue = rgb[0];
				
				for(int i = 1; i < grayValues.size(); i++) 
				{
					if(rgb[0] < grayValues.get(i)) 
					{
						double highGrayValue = grayValues.get(i);
						double lowGrayValue = grayValues.get(i - 1);
						
						double lowBound = Math.abs(lowGrayValue - rgb[0]);
						double highBound = Math.abs(highGrayValue - rgb[0]);
						
						// the Bounds that has the lower number means that the gray pixal value 
						// is closer to one of the n_level values
						if(lowBound < highBound) 
						{
							grayValue = lowGrayValue;
						}
						else 
						{
							grayValue = highGrayValue;
						}
						
						break;
					}
				}
				
//				if(newGrayValue < 127) 
//				{
//					newGrayValue = 0;
//				}
//				else 
//				{
//					newGrayValue = 255;
//				}
				
				int newGrayValue = (int) Math.round(grayValue);
				int[] newRGB = new int[] {newGrayValue, newGrayValue, newGrayValue};
				NLevelImage.setPixel(x, y, newRGB);
				
				double error = (rgb[0] - grayValue);
				
				//right pixel
				int xOffset = (x + 1);
				int yOffset = y;
				if(xOffset < grayScaleImage.getW()) 
				{
					filterWeight(grayScaleImage, xOffset, yOffset, error, 7.0 , 16.0);
				}
				
				//right diagonal pixel
				xOffset = (x + 1);
				yOffset = (y + 1);
				if(xOffset < grayScaleImage.getW() && yOffset < grayScaleImage.getH()) 
				{
					filterWeight(grayScaleImage, xOffset, yOffset, error, 1.0 , 16.0);
				}
				
				//bottom pixel
				xOffset = x;
				yOffset = (y + 1);
				if(yOffset < grayScaleImage.getH()) 
				{
					filterWeight(grayScaleImage, xOffset, yOffset, error, 5.0 , 16.0);
				}
				
				//left diagonal pixel
				xOffset = (x - 1);
				yOffset = (y + 1);
				if(xOffset >= 0 && xOffset < grayScaleImage.getW() && yOffset < grayScaleImage.getH()) 
				{
					filterWeight(grayScaleImage, xOffset, yOffset, error, 3.0 , 16.0);
				}
			}
		}
		return NLevelImage;
	}

	private static void filterWeight(Image grayScaleImage, int xOffset, int yOffset, double error, double num, double dem)
	{
		int[] newRGB = new int[3];
		grayScaleImage.getPixel(xOffset, yOffset, newRGB);
		
		double errorDiffusionD = newRGB[0] + (error * (num / dem));
		int errorDiffusion = (int) Math.round(errorDiffusionD);
		
		newRGB[0] = errorDiffusion;
		newRGB[1] = errorDiffusion;
		newRGB[2] = errorDiffusion;
		
		grayScaleImage.setPixel(xOffset, yOffset, newRGB);
	}

	private static Image NLevelConversion(Image image, int nLevel)
	{
		//nLevel = 8;
		
		final int BIT_VALUE = 256;
		final int GRAY_VALUE = BIT_VALUE - 1;
		double grayInterval = GRAY_VALUE / (nLevel - 1.0);
		double grayCount = 0;
		ArrayList<Integer> grayValues = new ArrayList<>();
		grayValues.add((int)grayCount);
		while(grayCount < GRAY_VALUE) 
		{
			grayCount = grayCount + grayInterval;
			grayValues.add((int)Math.floor(grayCount));
		}
		
		Image grayScaleImage = GrayScaleConversion(image);
		
		Image NLevelImage = new Image(grayScaleImage.getW(), grayScaleImage.getH());
		for(int y = 0; y < image.getH(); y++) 
		{
			for(int x = 0; x < image.getW(); x++) 
			{
				int[] rgb = new int[3];
				grayScaleImage.getPixel(x, y, rgb);

				int newGrayValue = rgb[0];
				for(int i = 1; i < grayValues.size(); i++) 
				{
					if(rgb[0] < grayValues.get(i)) 
					{
						int highGrayValue = grayValues.get(i);
						int lowGrayValue = grayValues.get(i - 1);
						
						int highBound = Math.abs(highGrayValue - rgb[0]);
						int lowBound = Math.abs(lowGrayValue - rgb[0]);
						
						// the Bounds that has the lower number means that the gray pixal value 
						// is closer to one of the n_level values
						if(lowBound < highBound) 
						{
							newGrayValue = lowGrayValue;
						}
						else 
						{
							newGrayValue = highGrayValue;
						}
						
						break;
					}
				}
				
				rgb[0] = newGrayValue;
				rgb[1] = newGrayValue;
				rgb[2] = newGrayValue;
				
				NLevelImage.setPixel(x, y, rgb);
//				image.setPixel(x, y, rgb);
			}
		}
		return NLevelImage;
	}
  
	private static Image GrayScaleConversion(Image image)
	{
		Image newImage = new Image(image.getW(), image.getH());
	    
		for(int y = 0; y < image.getH(); y++) 
		{
			for(int x = 0; x < image.getW(); x++) 
			{
				int[] rgb = new int[3];
				image.getPixel(x, y, rgb);
				
				double GrayD = Math.round(0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]);
				int Gray = (int) GrayD;
				
				if(Gray > 255)
				{
					Gray = 255;
				}
				
				if(Gray < 0)
				{
					Gray = 0;
				}
				
				rgb[0] = Gray;
				rgb[1] = Gray;
				rgb[2] = Gray;
				
				newImage.setPixel(x, y, rgb);
			}
		}
		return newImage;
	}

	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		System.out.println(System.getProperty("user.dir"));
		Image image = new Image("src\\Homework_1\\Ducky.ppm");
//		Image image = new Image("src\\Homework_1\\Buildings.ppm");
//		Image image = new Image("src\\Homework_1\\Citynight.ppm");
		
		String filename = args[0].replace(".ppm", "");
		while(MainMain(input, image, filename)) {}

		input.close();
	}

}
