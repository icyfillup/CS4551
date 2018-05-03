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
	
//			option = input.next();	
			option = "1";
			System.out.println();
		}
		while(!(option.contains("1") || option.contains("2") || option.contains("3")));
		
		if(option.equals("1")) 
		{
			BlockBasedMotionCompensation(input);
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

	public static void BlockBasedMotionCompensation(Scanner input) 
	{
		Image targetImage = new Image("./IDB/Walk_100.ppm");
		Image referenceImage = new Image("./IDB/Walk_010.ppm");
		System.out.println();
		
		String nS = null;
		do
		{
			System.out.print("Select block size n (8, 16, or 24): ");
	
//			n = input.next();	
			nS = "8";
			System.out.println();
		}
		while(!(nS.contains("8") || nS.contains("16") || nS.contains("24")));
		
		String pS = null;
		do
		{
			System.out.print("Select search window p (4, 8, 12, or 16): ");
	
//			p = input.next();	
			pS = "4";
			System.out.println();
		}
		while(!(pS.contains("4") || pS.contains("8") || pS.contains("12") || pS.contains("16")));
		
		int n = Integer.parseInt(nS);
		int p = Integer.parseInt(pS);
		
		Image residualImage = new Image(targetImage.getW(), targetImage.getH());
		int minError = 255;
		int maxError = 0;
		
		for(int worldY = 0; worldY < targetImage.getH() / n; worldY++) 
		{
			for(int worldX = 0; worldX < targetImage.getW() / n; worldX++) 
			{
				int targetBlockX = worldX * n;
				int targetBlockY = worldY * n;
				
				double bestMatchedReferenceBlockMSD = Double.MAX_VALUE; 
				int bestMatchedReferenceBlockX = 0; 
				int bestMatchedReferenceBlockY = 0;
				
				for(int yOffset = 0; yOffset < (p * 2) + 1;  yOffset++) 
				{
					int referenceBlockY = (targetBlockY - p) + yOffset;
					for(int xOffset = 0; xOffset < (p * 2) + 1;  xOffset++) 
					{
						int referenceBlockX = (targetBlockX - p) + xOffset;
						double MSD = 0;
						for(int y = 0; y < n; y++) 
						{
							int targetPixelY = targetBlockY + y;
							int referencePixelY = referenceBlockY + y;
							
							if(targetPixelY < 0 || targetPixelY >= targetImage.getH()) continue;
							if(referencePixelY < 0 || referencePixelY >= referenceImage.getH()) continue;

							for(int x = 0; x < n; x++) 
							{
								int targetPixelX = targetBlockX + x;
								int referencePixelX = referenceBlockX + x;
								
								if(targetPixelX < 0 || targetPixelX >= targetImage.getW()) continue;
								if(referencePixelX < 0 || referencePixelX >= referenceImage.getW()) continue;
			
								int[] targetPixelRGB = new int[3];
								int[] referencePixelRGB = new int[3];
								
								targetImage.getPixel(targetPixelX, 
													 targetPixelY, 
													 targetPixelRGB);
								
								referenceImage.getPixel(referencePixelX, 
														referencePixelY, 
														referencePixelRGB);
								
								int targetGrayPixel = 
										(int) Math.round(0.299 * targetPixelRGB[0] + 0.587 * targetPixelRGB[1] + 0.114 * targetPixelRGB[2]);
								int referenceGrayPixel = 
										(int) Math.round(0.299 * referencePixelRGB[0] + 0.587 * referencePixelRGB[1] + 0.114 * referencePixelRGB[2]);
								
								MSD += Math.pow(targetGrayPixel - referenceGrayPixel, 2);
							}
						}
						
						MSD = (1.0/(n*n)) * MSD;
						
						if(MSD < bestMatchedReferenceBlockMSD) 
						{
							bestMatchedReferenceBlockMSD = MSD;	
							bestMatchedReferenceBlockX = referenceBlockX;
							bestMatchedReferenceBlockY = referenceBlockY;
						}
					}
				}
				
				int motionX = targetBlockX - bestMatchedReferenceBlockX;
				int motionY = targetBlockY - bestMatchedReferenceBlockY;
				
				for(int y = 0; y < n; y++) 
				{
					int targetPixelY = targetBlockY + y;
					int referencePixelY = bestMatchedReferenceBlockY + y;
					
					if(targetPixelY < 0 || targetPixelY >= targetImage.getH()) continue;
					if(referencePixelY < 0 || referencePixelY >= referenceImage.getH()) continue;
					
					for(int x = 0; x < n; x++) 
					{
						int targetPixelX = targetBlockX + x;
						int referencePixelX = bestMatchedReferenceBlockX + x;
						
						if(targetPixelX < 0 || targetPixelX >= targetImage.getW()) continue;
						if(referencePixelX < 0 || referencePixelX >= referenceImage.getW()) continue;

						int[] targetPixelRGB = new int[3];
						int[] referencePixelRGB = new int[3];
						
						targetImage.getPixel(targetPixelX, 
											 targetPixelY, 
											 targetPixelRGB);
						
						referenceImage.getPixel(referencePixelX, 
												referencePixelY, 
												referencePixelRGB);
						
						int targetGrayPixel = 
								(int) Math.round(0.299 * targetPixelRGB[0] + 0.587 * targetPixelRGB[1] + 0.114 * targetPixelRGB[2]);
						int referenceGrayPixel = 
								(int) Math.round(0.299 * referencePixelRGB[0] + 0.587 * referencePixelRGB[1] + 0.114 * referencePixelRGB[2]);
						
						int[] residualPixelRGB = new int[3];
						
						int residualGrayPixel = Math.abs(targetGrayPixel - referenceGrayPixel);
//						int residualGrayPixel = referenceGrayPixel;
						
						residualPixelRGB[0] = residualGrayPixel;
						residualPixelRGB[1] = residualGrayPixel;
						residualPixelRGB[2] = residualGrayPixel;
						
						if(residualGrayPixel < minError) minError = residualGrayPixel;
						if(residualGrayPixel > maxError) maxError = residualGrayPixel;
						
						if(residualGrayPixel < maxError && residualGrayPixel > minError) 
						{
							System.out.println();
						}
						
						residualImage.setPixel(targetBlockX + x, targetBlockY + y, residualPixelRGB);
					}	
				}
				
//				System.out.println("x: " + bestMatchedReferenceBlockX + ", y: " + bestMatchedReferenceBlockY);
//				System.out.println("Best Matched Macro Block MSD: " + bestMatchedReferenceBlockMSD);
//				System.out.println();
			}	
		}
		
//		for(int y = 0; y < residualImage.getH(); y++) 
//		{
//			for(int x = 0; x < residualImage.getW(); x++) 
//			{
//				int[] residualPixelRGB = new int[3];
//				residualImage.getPixel(x, y, residualPixelRGB);
//				
//				residualPixelRGB[0] = (int)(((double)(residualPixelRGB[0] - minError) / (double)(maxError - minError)) * 255.0);
//				residualPixelRGB[1] = (int)(((double)(residualPixelRGB[1] - minError) / (double)(maxError - minError)) * 255.0);
//				residualPixelRGB[2] = (int)(((double)(residualPixelRGB[2] - minError) / (double)(maxError - minError)) * 255.0);
//				
//				residualImage.setPixel(x, y, residualPixelRGB);
//			}	
//		}
		
//		targetImage.display();
//		referenceImage.display();
		residualImage.display();
		System.out.println();
	}
	
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		
		while(MainMenu(input)) {}
		
		input.close();
		System.exit(0);
	}
}
