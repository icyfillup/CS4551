package Homework_4;

import java.util.Scanner;

public class CS4551_Tran
{
	public static boolean MainMenu(Scanner input) 
	{
		boolean isRunning = true;
		String option = null;
		do
		{
			System.out.print("Main Menu-----------------------------------\r\n" + 
							 "1.Block Based Motion Compensation \r\n" + 
							 "2.Removing Moving Objects\r\n" + 
							 "3.Quit\r\n\n" + 
					 		 "Please enter the task number [1-3]: ");
	
			option = input.next();	
//			n = "0";
			System.out.println();
		}
		while(!(option.contains("1") || option.contains("2") || option.contains("3")));
		
		if(option.equals("1")) 
		{
			
		}
		else if(option.equals("2")) 
		{
			
		}
		else if(option.equals("3")) 
		{
			isRunning = false;
		}
		
		return isRunning;
	}
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		
		while(MainMenu(input)) {}
		
		input.close();
		System.exit(0);
	}
}
