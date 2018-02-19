package Homework_1;

import java.util.Scanner;

public class CS4551_Tran
{

	public static boolean MainMain(Scanner input, Image image) 
	{
		System.out.println("Main Menu-----------------------------------\r\n" + 
				"1. Conversion to Gray - scale Image (24bits -> 8bits)\r\n" + 
				"2. Conversion to N-level Image \r\n" + 
				"3. Conversion to 8-bit Indexed Color Image using Uniform Color Quantization(24bits -> 8bits)\r\n" + 
				"4. Quit");
		
		System.out.print("Please enter the task number [1 - 4]: ");
		String option = input.next();
//		String option = "2";
		System.out.println();
		
		boolean isRunning = true;
		switch(option) 
		{
			case "1":
			{
				System.out.println("Conversion to Gray - scale Image (24bits -> 8bits):");
				Image GrayScaleImage = image.GrayScaleConversion();
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
				Image NLevelImage = image.NLevelConversion(nLevel);
				Image NLevelErrorDiffusionImage = image.NLevelErrorDiffusionConversion(nLevel);

				NLevelImage.display();
				NLevelErrorDiffusionImage.display();
				
				System.out.println();
				isRunning = true;
			}break;
			
			case "3":
			{
				System.out.println("Conversion to 8-bit Indexed Color Image using Uniform Color Quantization(24bits -> 8bits):");
				
				Image UCQ = image.UniformColorQuantization();
				UCQ.display();
				
				System.out.println();
				isRunning = true;
			}break;
			
			case "4":
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
	
	public static void main(String[] args)
	{
		System.out.println();	
		System.out.println();
		
		if(args.length == 0) 
		{
			System.out.println("No input file argument. Quitting program...");
		}
		else 
		{
			Scanner input = new Scanner(System.in);
			try 
			{
				Image image = new Image(args[0]);
				while(MainMain(input, image)) {}
				input.close();
			}
			catch(Exception e) 
			{
				System.out.println("Cannot find input file. Quitting program...");
				input.close();
			}
			
		}
		
		System.exit(0);
	}

}
