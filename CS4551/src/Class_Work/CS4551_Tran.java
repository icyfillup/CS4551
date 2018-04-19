package Class_Work;

public class CS4551_Tran
{
	public static void main(String[] args)
	{
		Image currentImage = new Image(args[0]);
		Image referenceImage = new Image(args[0]);
		
		int p = 5;
		int searchingAreaX = 100;
		int searchingAreaY = 100;
		int macroBlockSize = 16;
		
		for(int yOffset = 0; yOffset < (p * 2) + 1;  yOffset++) 
		{
			int currentMacroBlockY = (searchingAreaY - p) + yOffset;
			for(int xOffset = 0; xOffset < (p * 2) + 1;  xOffset++) 
			{
				int currentMacroBlockX = (searchingAreaX - p) + xOffset;
				double MSD = 0;
				for(int y = 0; y < macroBlockSize; y++) 
				{
					for(int x = 0; x < macroBlockSize; x++) 
					{
						int[] currentRGB = new int[3];
						int[] referenceRGB = new int[3];
						
						currentImage.getPixel(currentMacroBlockX + x, 
											  currentMacroBlockY + y, 
											  currentRGB);
						
						referenceImage.getPixel(searchingAreaX + x, 
												searchingAreaY + y, 
												referenceRGB);
						
						MSD += Math.pow(currentRGB[1] - referenceRGB[1], 2);
					}
				}
				
				MSD = (1.0/(16.0*16.0)) * MSD;
				
				System.out.println("x: " + currentMacroBlockX + ", y: " + currentMacroBlockY);
				System.out.println("MSD: " + MSD);
				System.out.println();
			}
		}
		
	}
}
