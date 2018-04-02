package Spring_Break_2018_Extra_Credit;



public class CS4551_Tran
{
	private static Image CreatePaddedImage(Image originalImage) 
	{
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
		
		return paddedImage;
	}
	
	private static void ColorSpaceTransformationSubsampling(Image paddedImage, double[][] Y, double[][] subSampleCb, double[][] subSampleCr) 
	{
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
				
//				tempY = Math.floor(tempY);
//				tempCb = Math.floor(tempCb * 10) / 10.0;
//				tempCr = Math.floor(tempCr * 10) / 10.0;
				
				assert(tempY <= 255 & tempY >= 0);
				assert(tempCb <= 127.5 & tempCb >= -127.5);
				assert(tempCr <= 127.5 & tempCr >= -127.5);
				
				Y[j][i] = (tempY - 128);
				Cb[j][i] = (tempCb - 0.5);
				Cr[j][i] = (tempCr - 0.5);
				
				assert(Y[j][i] <= 127 & Y[j][i] >= -128);
				assert(Cb[j][i] <= 127 & Cb[j][i] >= -128);
				assert(Cb[j][i] <= 127 & Cb[j][i] >= -128);
				
			}
		}
		
		/* check if the matrix calculation was incorrect
		Image testPaddedImage = new Image(paddedImage.getW(), paddedImage.getH());
		
		for(int j = 0; j < paddedImage.getH(); j++) 
		{
			for(int i = 0; i < paddedImage.getW(); i++) 
			{
				double newTempY = (Y[j][i] + 128);
				double newTempCb = (Cb[j][i] + 0.5);
				double newTempCr = (Cr[j][i] + 0.5);
				
				double R = (newTempY * 1.000) + (newTempCb * 0.0) + (newTempCr * 1.4020);
				double G = (newTempY * 1.000) + (newTempCb * -0.3441) + (newTempCr * -0.7141);
				double B = (newTempY * 1.000) + (newTempCb * 1.7720) + (newTempCr * 0.0);
				
				R = Math.round(R);
				G = Math.round(G);
				B = Math.round(B);
				
				testPaddedImage.setPixel(i, j, new int[] {(int)R, (int)G, (int)B});
			}
		}
		
		testPaddedImage.display();
		 */	
		for(int indexY = 0; indexY < Y.length; indexY = indexY + 2) 
		{
			int subSampleIndexY = indexY / 2;
			for(int indexX = 0; indexX < Y[0].length; indexX = indexX + 2) 
			{
				int subSampleIndexX = indexX / 2;
				
//				subSampleCb[subSampleIndexY][subSampleIndexX] = 
//						(Cb[indexY][indexX] * 1.0/4.0) + 
//						(Cb[indexY][indexX + 1] * 1.0/4.0) + 
//						(Cb[indexY + 1][indexX] * 1.0/4.0) + 
//						(Cb[indexY + 1][indexX + 1] * 1.0/4.0);
//				
//				subSampleCr[subSampleIndexY][subSampleIndexX] = 
//						(Cr[indexY][indexX] * 1.0/4.0) + 
//						(Cr[indexY][indexX + 1] * 1.0/4.0) + 
//						(Cr[indexY + 1][indexX] * 1.0/4.0) + 
//						(Cr[indexY + 1][indexX + 1] * 1.0/4.0);
				
				
				subSampleCb[subSampleIndexY][subSampleIndexX] = 
						(Cb[indexY][indexX]);
				
				subSampleCr[subSampleIndexY][subSampleIndexX] = 
						(Cr[indexY][indexX]);
				
				
			}	
		}
		
//		Image newPaddedImage = new Image(Y[0].length, Y.length);
//		
//		for(int indexY = 0; indexY < subSampleCb.length; indexY++) 
//		{
//			for(int indexX = 0; indexX < subSampleCb[0].length; indexX++) 
//			{
//				if(Y.length > (indexY * 2) && Y[0].length > (indexX * 2) ) 
//				{
//					double newTempY = (Y[indexY * 2][indexX * 2] + 128);
//					double newTempCb = (subSampleCb[indexY][indexX] + 0.5);
//					double newTempCr = (subSampleCr[indexY][indexX] + 0.5);
//					
//					double R = (newTempY * 1.000) + (newTempCb * 0.0) + (newTempCr * 1.4020);
//					double G = (newTempY * 1.000) + (newTempCb * -0.3441) + (newTempCr * -0.7141);
//					double B = (newTempY * 1.000) + (newTempCb * 1.7720) + (newTempCr * 0.0);
//					
//					newPaddedImage.setPixel(indexX, indexY, new int[] {(int)R, (int)G, (int)B});	
//				}
//			}	
//		}
//
//		newPaddedImage.display();
//		System.out.println();
	}
	
	public static void main(String[] args)
	{
		System.out.println(args[0]);
		
		Image originalImage = new Image(args[0]);
		
//##############		Read and resize the input image

		Image paddedImage = CreatePaddedImage(originalImage);
		
//################		Color space transformation and Subsampling
		int subSamplingWidth = (paddedImage.getW() * 2) / 4;
		int subSamplingHeight = (paddedImage.getH() * 2) / 4;
		
		subSamplingWidth = ((subSamplingWidth % 8) == 0) ? subSamplingWidth : subSamplingWidth + 8 - (subSamplingWidth % 8);
		subSamplingHeight = ((subSamplingHeight % 8) == 0) ? subSamplingHeight : subSamplingHeight + 8 - (subSamplingHeight % 8);
		
		double[][] Y = new double[paddedImage.getH()][paddedImage.getW()];
		double[][] subSampleCb = new double[subSamplingHeight][subSamplingWidth];
		double[][] subSampleCr = new double[subSamplingHeight][subSamplingWidth];
		
		ColorSpaceTransformationSubsampling(paddedImage, Y, subSampleCb, subSampleCr);	
		
//################		Inverse Color space transformation and Supersampling
		//maybe change subsamplingCb and subsamplingCr to to Cb and Cr size 
		Image newPaddedImage = new Image(Y[0].length, Y.length);
		
		for(int indexY = 0; indexY < subSampleCb.length; indexY++) 
		{
			for(int indexX = 0; indexX < subSampleCb[0].length; indexX++) 
			{
				if(Y.length > (indexY * 2) && Y[0].length > (indexX * 2) ) 
				{
					double newTempCb = (subSampleCb[indexY][indexX] + 0.5);
					double newTempCr = (subSampleCr[indexY][indexX] + 0.5);
					
					{
						int topLeftX = (indexX * 2), topLeftY = (indexY * 2); 
						double newTempY = (Y[topLeftY][topLeftX] + 128);
						double R = (newTempY * 1.000) + (newTempCb * 0.0) + (newTempCr * 1.4020);
						double G = (newTempY * 1.000) + (newTempCb * -0.3441) + (newTempCr * -0.7141);
						double B = (newTempY * 1.000) + (newTempCb * 1.7720) + (newTempCr * 0.0);
						newPaddedImage.setPixel(topLeftX, topLeftY, new int[] {(int)R, (int)G, (int)B});	
							
					}
					
					{
						int topRightX = (indexX * 2) + 1, topRightY = (indexY * 2); 
						double newTempY = (Y[topRightY][topRightX] + 128);
						double R = (newTempY * 1.000) + (newTempCb * 0.0) + (newTempCr * 1.4020);
						double G = (newTempY * 1.000) + (newTempCb * -0.3441) + (newTempCr * -0.7141);
						double B = (newTempY * 1.000) + (newTempCb * 1.7720) + (newTempCr * 0.0);
						newPaddedImage.setPixel(topRightX, topRightY, new int[] {(int)R, (int)G, (int)B});		
					}
					
					{
						int bottomLeftX = (indexX * 2), bottomLeftY = (indexY * 2) + 1; 
						double newTempY = (Y[bottomLeftY][bottomLeftX] + 128);
						double R = (newTempY * 1.000) + (newTempCb * 0.0) + (newTempCr * 1.4020);
						double G = (newTempY * 1.000) + (newTempCb * -0.3441) + (newTempCr * -0.7141);
						double B = (newTempY * 1.000) + (newTempCb * 1.7720) + (newTempCr * 0.0);
						newPaddedImage.setPixel(bottomLeftX, bottomLeftY, new int[] {(int)R, (int)G, (int)B});		
					}
					
					{
						int bottomRightX = (indexX * 2) + 1, bottomRightY = (indexY * 2) + 1; 
						double newTempY = (Y[bottomRightY][bottomRightX] + 128);
						double R = (newTempY * 1.000) + (newTempCb * 0.0) + (newTempCr * 1.4020);
						double G = (newTempY * 1.000) + (newTempCb * -0.3441) + (newTempCr * -0.7141);
						double B = (newTempY * 1.000) + (newTempCb * 1.7720) + (newTempCr * 0.0);
						newPaddedImage.setPixel(bottomRightX, bottomRightY, new int[] {(int)R, (int)G, (int)B});		
					}
				}
				
			}	
		}

		newPaddedImage.display();
		System.out.println();
//################		Remove Padding and Display the image
		
		int originalHeight = 0;
		for(int j = newPaddedImage.getH() - 1; j >= 0; j--) 
		{
			originalHeight = j;
			boolean isOriginalHeight = false;
			for(int i = 0; i < newPaddedImage.getW(); i++) 
			{
				int[] rgb = new int[3];
				newPaddedImage.getPixel(i, j, rgb);
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
		for(int i = newPaddedImage.getW() - 1; i >= 0; i--) 
		{
			originalWidth = i;
			boolean isOriginalWidth= false;
			for(int j = 0; j < newPaddedImage.getH(); j++) 
			{
				int[] rgb = new int[3];
				newPaddedImage.getPixel(i, j, rgb);
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
