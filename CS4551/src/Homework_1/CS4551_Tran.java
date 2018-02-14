package Homework_1;

import java.util.Scanner;

import setupImageProject.Image;

public class CS4551_Tran
{

	public static boolean MainMain(Scanner input, Image image) 
	{
		System.out.println("Main Menu-----------------------------------\r\n" + 
				"1. Conversion to Gray - scale Image (24bits -> 8bits)\r\n" + 
				"2. Conversion to N-level Image \r\n" + 
				"3. Conversion to 8-bit Indexed Color Image using Uniform Color Quantization(24bits -> 8bits)\r\n" + 
				"4. Conversion to 8-bit Indexed Color Image using [your selected method](24bits->8bits)\r\n" + 
				"5. Quit");
		
		System.out.print("Please enter the task number [1 - 5]: ");
		String option = input.next();
		System.out.println();
		
		boolean isRunning = true;
		switch(option) 
		{
			case "1":
			{
				System.out.println("Conversion to Gray - scale Image (24bits -> 8bits):");
				GrayScaleConversion(image);
				System.out.println();
				isRunning = true;
			}break;
			
			case "2":
			{
				System.out.println("Conversion to N-level Image:");
				
				System.out.println();
				isRunning = true;
			}break;
			
			case "3":
			{
				System.out.println("Conversion to 8-bit Indexed Color Image using Uniform Color Quantization(24bits -> 8bits):");
				
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
	
	private static void GrayScaleConversion(Image image)
	{
		for(int y = 0; y < image.getH(); y++) 
		{
			for(int x = 0; x < image.getW(); x++) 
			{
				int[] rgb = new int[3];
				image.getPixel(x, y, rgb);
				
				int Gray = (int) Math.round(0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]);
				rgb[0] = Gray;
				rgb[1] = Gray;
				rgb[2] = Gray;
				
				image.setPixel(x, y, rgb);
			}
		}
		
		image.write2PPM("src\\Homework_1\\image\\out.ppm");

	    Image img = new Image("src\\Homework_1\\image\\out.ppm");
	    img.display();
	}

	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		System.out.println(System.getProperty("user.dir"));
		Image image = new Image("src\\Homework_1\\image\\Ducky.ppm");
		
		while(MainMain(input, image)) {}

		input.close();
	}

}
