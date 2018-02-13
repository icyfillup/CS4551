package Homework_1;

import java.util.Scanner;

public class CS4551_Tran
{

	public static boolean MainMain(Scanner input) 
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
	
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		
		while(MainMain(input)) {}

		input.close();
	}

}
