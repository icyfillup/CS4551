package Homework_4;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
			option = "2";
			System.out.println();
		}
		while(!(option.contains("1") || option.contains("2") || option.contains("3")));
		
		if(option.equals("1")) 
		{
			Image targetImage = null;
			String targetFileName = "";
			{
				boolean hasTargetFile = false;
				do
				{
					System.out.print("Enter Target Image File Name: ");
//					targetFileName = input.next();	
					targetFileName = "Walk_054.ppm";
					
					try 
					{
						targetImage = new Image(targetFileName);
						hasTargetFile = true;
					}
					catch(Exception e) 
					{
						hasTargetFile = false;
					}
				}
				while(!hasTargetFile);
				System.out.println();
			}

			Image referenceImage = null;
			String referenceFileName = "";
			{
				boolean hasReferenceFile = false;
				do
				{
					System.out.print("Enter Reference Image File Name: ");
//					referenceFileName = input.next();	
					referenceFileName = "Walk_050.ppm";
					
					try 
					{
						referenceImage = new Image(referenceFileName);
						hasReferenceFile = true;
					}
					catch(Exception e) 
					{
						hasReferenceFile = false;
					}
				}
				while(!hasReferenceFile);
				System.out.println();
			}
			
			String nS = null;
			do
			{
				System.out.print("Select block size n (8, 16, or 24): ");
		
//				nS = input.next();	
				nS = "8";
				System.out.println();
			}
			while(!(nS.contains("8") || nS.contains("16") || nS.contains("24")));
			System.out.println();
			
			String pS = null;
			do
			{
				System.out.print("Select search window p (4, 8, 12, or 16): ");
		
//				pS = input.next();	
				pS = "4";
				System.out.println();
			}
			while(!(pS.contains("4") || pS.contains("8") || pS.contains("12") || pS.contains("16")));
			System.out.println();
			
			int n = Integer.parseInt(nS);
			int p = Integer.parseInt(pS);
			
			int imageBlockWidth = targetImage.getW() / n;
			int imageBlockHeight = targetImage.getH() / n;
			Vector2i[][] motionVectorList = new Vector2i[imageBlockHeight][imageBlockWidth];
			
			Image residualImage = new Image(targetImage.getW(), targetImage.getH());
			BlockBasedMotionCompensation(targetImage, referenceImage, residualImage, motionVectorList, n, p);
			CreateMotionVectorTextFile(motionVectorList, residualImage, targetFileName, referenceFileName);
			residualImage.display();
			residualImage.write2PPM("error_" + targetFileName);
			
			System.out.println();
		}
		else if(option.equals("2")) 
		{
			int targetFrame = -1;
			do
			{
				System.out.print("Enter Target Frame Image number (19 ~ 179): ");
				try 
				{
//					targetFrame= Integer.parseInt(input.next());	
					targetFrame = 50;
				}
				catch(Exception e){}
				
				System.out.println();
			}
			while(!(targetFrame> 19 && targetFrame< 179));
			System.out.println();
			
			String targetFileName = "Walk_" + FrameCountFormatter(targetFrame) + ".ppm";
			String referenceFileName = "Walk_" + FrameCountFormatter(targetFrame - 2) + ".ppm";
			
			Image targetImage = new Image(targetFileName);
			Image referenceImage = new Image(referenceFileName);
			
			MovingObjectDetectionandRemoval(targetImage, referenceImage);
		}
		else if(option.equals("3")) 
		{
			isRunning = false;
		}
		
		return isRunning;
	}
	
	public static void MovingObjectDetectionandRemoval(Image targetImage, Image referenceImage) 
	{
		int n = 8;
		int p = 4;
		
		int imageBlockWidth = targetImage.getW() / n;
		int imageBlockHeight = targetImage.getH() / n;
		Vector2i[][] motionVectorList = new Vector2i[imageBlockHeight][imageBlockWidth];
		
		BlockBasedMotionCompensation(targetImage, referenceImage, 
									 new Image(targetImage.getW(), targetImage.getH()), 
									 motionVectorList, n, p);
		
		for(int blockOffsetY = 0; blockOffsetY < imageBlockHeight; blockOffsetY++) 
		{
			for(int blockOffsetX = 0; blockOffsetX < imageBlockWidth; blockOffsetX++) 
			{
				Vector2i mv = motionVectorList[blockOffsetY][blockOffsetX];
				if(mv.getX() != 0 && mv.getY() != 0) 
				{
					for(int y = 0; y < n; y++) 
					{
						for(int x = 0; x < n; x++) 
						{
							int[] targetPixelRGB = new int[3];
							targetImage.getPixel((blockOffsetX * n)+ x, 
												 (blockOffsetY * n) + y, 
												  targetPixelRGB);
							
							int targetGrayPixel = 
									(int) Math.round(0.299 * targetPixelRGB[0] + 0.587 * targetPixelRGB[1] + 0.114 * targetPixelRGB[2]);
							
							targetPixelRGB[0] = 200;
							targetPixelRGB[1] = targetGrayPixel;
							targetPixelRGB[2] = targetGrayPixel;
							
							targetImage.setPixel((blockOffsetX * n)+ x, 
												 (blockOffsetY * n) + y, 
												 targetPixelRGB);
							
						}
					}
				}
			}	
		}
		targetImage.display();
		System.out.println();
	}
	
	public static String FrameCountFormatter(int rawFrameNumber) 
	{
		String frameNumberS = String.valueOf(rawFrameNumber);
		int addZeroCount = 3 - frameNumberS.length();
		
		for(int i = 0; i < addZeroCount; i++) 
		{
			frameNumberS = "0" + frameNumberS;
		}
		
		return frameNumberS;
	}

	public static void BlockBasedMotionCompensation(Image targetImage, Image referenceImage, Image residualImage, 
													Vector2i[][] motionVectorList, int n, int p) 
	{
		int minError = 255;
		int maxError = 0;

		for(int imageBlockY = 0; imageBlockY < motionVectorList.length; imageBlockY++) 
		{
			for(int imageBlockX = 0; imageBlockX < motionVectorList[0].length; imageBlockX++) 
			{
				int targetBlockX = imageBlockX * n;
				int targetBlockY = imageBlockY * n;
				
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
				
				motionVectorList[imageBlockY][imageBlockX] = new Vector2i(targetBlockX - bestMatchedReferenceBlockX, targetBlockY - bestMatchedReferenceBlockY);
				
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
						residualPixelRGB[0] = residualGrayPixel;
						residualPixelRGB[1] = residualGrayPixel;
						residualPixelRGB[2] = residualGrayPixel;
						
						if(residualGrayPixel < minError) minError = residualGrayPixel;
						if(residualGrayPixel > maxError) maxError = residualGrayPixel;
						
						residualImage.setPixel(targetBlockX + x, targetBlockY + y, residualPixelRGB);
					}	
				}
			}	
		}
		
		for(int y = 0; y < residualImage.getH(); y++) 
		{
			for(int x = 0; x < residualImage.getW(); x++) 
			{
				int[] residualPixelRGB = new int[3];
				residualImage.getPixel(x, y, residualPixelRGB);
				
				residualPixelRGB[0] = (int)(((double)(residualPixelRGB[0] - minError) / (double)(maxError - minError)) * 255.0);
				residualPixelRGB[1] = (int)(((double)(residualPixelRGB[1] - minError) / (double)(maxError - minError)) * 255.0);
				residualPixelRGB[2] = (int)(((double)(residualPixelRGB[2] - minError) / (double)(maxError - minError)) * 255.0);
				
				residualImage.setPixel(x, y, residualPixelRGB);
			}	
		}
	}
	
	public static void CreateMotionVectorTextFile(Vector2i[][] motionVectorList, Image image, String targetName, String referenceName)
	{
		PrintWriter writer;
		try
		{
			writer = new PrintWriter("mv.txt", "UTF-8");
			
			int imageBlockX = motionVectorList[0].length;
			int imageBlockY = motionVectorList.length;
			
			writer.println("# Target image name: " + targetName);
			writer.println("# Reference image name: " + referenceName);
			writer.print("# Number of target macro block: " + imageBlockX + " x " + imageBlockY + " ");
			writer.println("(image size is " + image.getW() + " x " + image.getH() + ")");
			
			for(int y = 0 ; y < imageBlockY; y++) 
			{
				for(int x = 0 ; x < imageBlockX; x++) 
				{
					Vector2i mv = motionVectorList[y][x];
					writer.print("[" + mv.getX() + "," + mv.getY() +"]\t");
				}	
				writer.println();
			}
			
			writer.close();
		}
		catch (FileNotFoundException | UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		
		while(MainMenu(input)) {}
		
		input.close();
		System.exit(0);
	}
}

class Vector2i
{
	private int x;
	private int y;
	public Vector2i(int x, int y)
	{
		super();
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	@Override
	public String toString()
	{
		return "Vector2i [x=" + x + ", y=" + y + "]";
	}
}