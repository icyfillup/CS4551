package Spring_Break_2018_Extra_Credit;


public class CS4551_Tran
{

	public static void main(String[] args)
	{
		System.out.println(args[0]);
		
		Image originalImage = new Image(args[0]);
		
//##############		Read and resize the input image
		
		int widthPadding = ((originalImage.getW() % 8) == 0) ? originalImage.getW() : originalImage.getW() + 8 - (originalImage.getW() % 8);
		int heightPadding = ((originalImage.getH() % 8) == 0) ? originalImage.getH() : originalImage.getH() + 8 - (originalImage.getH() % 8);
		
		Image paddedImage = new Image(widthPadding, heightPadding);
		
		for(int j = 0; j < originalImage.getH(); j++) 
		{
			for(int i = 0; i < originalImage.getW(); i++) 
			{
				int[] rgb = new int[3];
				originalImage.getPixel(i, j, rgb);
				paddedImage.setPixel(i, j, rgb);
			}
		}
		
//################		Color space transformation and Subsampling
		
		double[][] Y = new double[paddedImage.getH()][paddedImage.getW()];
		double[][] Cb = new double[paddedImage.getH()][paddedImage.getW()];
		double[][] Cr = new double[paddedImage.getH()][paddedImage.getW()];
		
		for(int j = 0; j < paddedImage.getH(); j++) 
		{
			for(int i = 0; i < paddedImage.getW(); i++) 
			{
				int[] RGB = new int[3];
				paddedImage.getPixel(i, j, RGB);
				
				double tempY = (RGB[0] * 0.2990) + (RGB[1] * 0.5870) + (RGB[2] * 0.1140);
				double tempCb = (RGB[0] * -0.1687) + (RGB[1] * -0.3313) + (RGB[2] * 0.5000);
				double tempCr = (RGB[0] * 0.5000) + (RGB[1] * -0.4187) + (RGB[2] * -0.0813);
				
				tempY = Math.floor(tempY);
				tempCb = Math.floor(tempCb * 10) / 10.0;
				tempCr = Math.floor(tempCr * 10) / 10.0;
				
				Y[j][i] = (tempY - 128);
				Cb[j][i] = (tempCb - 0.5);
				Cr[j][i] = (tempCr - 0.5);
			}
		}
		
		int subSamplingWidth = (paddedImage.getW() * 2) / 4;
		int subSamplingHeight = (paddedImage.getH() * 2) / 4;
		
		subSamplingWidth = ((subSamplingWidth % 8) == 0) ? subSamplingWidth : subSamplingWidth + 8 - (subSamplingWidth % 8);
		subSamplingHeight = ((subSamplingHeight % 8) == 0) ? subSamplingHeight : subSamplingHeight + 8 - (subSamplingHeight % 8);
		
		double[][] subSampleCb = new double[subSamplingHeight][subSamplingWidth];
		double[][] subSampleCr = new double[subSamplingHeight][subSamplingWidth];
		
		for(int indexY = 0; indexY < Y.length; indexY = indexY + 2) 
		{
			int subSampleIndexY = indexY / 2;
			for(int indexX = 0; indexX < Y[0].length; indexX = indexX + 2) 
			{
				int subSampleIndexX = indexX / 2;
				
				subSampleCb[subSampleIndexY][subSampleIndexX] = 
						(Cb[indexY][indexX] * 1.0/4.0) + 
						(Cb[indexY][indexX + 1] * 1.0/4.0) + 
						(Cb[indexY + 1][indexX] * 1.0/4.0) + 
						(Cb[indexY + 1][indexX + 1] * 1.0/4.0);
				
				subSampleCr[subSampleIndexY][subSampleIndexX] = 
						(Cr[indexY][indexX] * 1.0/4.0) + 
						(Cr[indexY][indexX + 1] * 1.0/4.0) + 
						(Cr[indexY + 1][indexX] * 1.0/4.0) + 
						(Cr[indexY + 1][indexX + 1] * 1.0/4.0);
			}	
		}
//		 Y, subSampleCb, and subSampleCr can now be use
		System.out.println();
		
//################		Inverse Color space transformation and Supersampling
		
		for(int indexY = 0; indexY < Y.length; indexY = indexY + 2) 
		{
			int subSampleIndexY = indexY / 2;
			for(int indexX = 0; indexX < Y[0].length; indexX = indexX + 2) 
			{
				int subSampleIndexX = indexX / 2;
				
				subSampleCb[subSampleIndexY][subSampleIndexX] = 
						(Cb[indexY][indexX] * 1.0/4.0) + 
						(Cb[indexY][indexX + 1] * 1.0/4.0) + 
						(Cb[indexY + 1][indexX] * 1.0/4.0) + 
						(Cb[indexY + 1][indexX + 1] * 1.0/4.0);
				
				subSampleCr[subSampleIndexY][subSampleIndexX] = 
						(Cr[indexY][indexX] * 1.0/4.0) + 
						(Cr[indexY][indexX + 1] * 1.0/4.0) + 
						(Cr[indexY + 1][indexX] * 1.0/4.0) + 
						(Cr[indexY + 1][indexX + 1] * 1.0/4.0);
			}	
		}
		
//################		Remove Padding and Display the image
		
		int originalHeight = 0;
		for(int j = paddedImage.getH() - 1; j >= 0; j--) 
		{
			originalHeight = j;
			boolean isOriginalHeight = false;
			for(int i = 0; i < paddedImage.getW(); i++) 
			{
				int[] rgb = new int[3];
				paddedImage.getPixel(i, j, rgb);
				if(rgb[0] != 0 || rgb[1] != 0 || rgb[2] != 0) 
				{
					isOriginalHeight = true;
					break;
				}
			}
			
			if(isOriginalHeight) 
			{
				//plus one to size from the index
				originalHeight++;
				break;
			}
		}
		
		int originalWidth = 0;
		for(int i = paddedImage.getW() - 1; i >= 0; i--) 
		{
			originalWidth = i;
			boolean isOriginalWidth= false;
			for(int j = 0; j < paddedImage.getH(); j++) 
			{
				int[] rgb = new int[3];
				paddedImage.getPixel(i, j, rgb);
				if(rgb[0] != 0 || rgb[1] != 0 || rgb[2] != 0) 
				{
					isOriginalWidth = true;
					break;
				}
			}
			
			if(isOriginalWidth) 
			{
				//plus one to size from the index
				originalWidth++;
				break;
			}
		}
		
		System.exit(0);
	}

}
