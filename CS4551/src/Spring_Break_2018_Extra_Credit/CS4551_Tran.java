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
				
				
//				subSampleCb[subSampleIndexY][subSampleIndexX] = 
//						(Cb[indexY][indexX]);
//				
//				subSampleCr[subSampleIndexY][subSampleIndexX] = 
//						(Cr[indexY][indexX]);
			}	
		}
	}
	
	private static Image InverseColorSpaceTransformationSubsampling(double[][] Y, double[][] subSampleCb, double[][] subSampleCr) 
	{
		Image newPaddedImage = new Image(Y[0].length, Y.length);
		for(int indexY = 0; indexY < Y.length; indexY++) 
		{
			int subSampleIndexY = indexY / 2;
			for(int indexX = 0; indexX < Y[0].length; indexX++) 
			{
				int subSampleIndexX = indexX / 2;
				
				double newTempY = (Y[indexY][indexX] + 128);
				double newTempCb = (subSampleCb[subSampleIndexY][subSampleIndexX] + 0.5);
				double newTempCr = (subSampleCr[subSampleIndexY][subSampleIndexX] + 0.5);
				
				double R = (newTempY * 1.000) + (newTempCb * 0.0) + (newTempCr * 1.4020);
				double G = (newTempY * 1.000) + (newTempCb * -0.3441) + (newTempCr * -0.7141);
				double B = (newTempY * 1.000) + (newTempCb * 1.7720) + (newTempCr * 0.0);
				
				if(R >= 256) R = 255;
				if(G >= 256) G = 255;
				if(B >= 256) B = 255;
				
				if(R < 0) R = 0;
				if(G < 0) G = 0;
				if(B < 0) B = 0;
				
				assert(R < 256 && R >= 0);
				assert(G < 256 && G >= 0);
				assert(B < 256 && B >= 0);
				
				newPaddedImage.setPixel(indexX, indexY, new int[] {(int)R, (int)G, (int)B});
			}	
		}
		return newPaddedImage;
	}

	private static double[][] DctCalculation(double[][] sourceSample)
	{
		double [][] dctCoeff = new double[sourceSample.length][sourceSample[0].length];
		
		for(int y = 0; y < sourceSample.length; y++) 
		{
			for(int x = 0; x < sourceSample[0].length; x++) 
			{
				double sum = 0.0;
				for(int i = 0; i < sourceSample.length; i++) 
				{
					double firstDivide = ((2.0 * i + 1.0) * y * Math.PI) / 16.0;
					double firstCosine = Math.cos(firstDivide);
					
					for(int j = 0; j < sourceSample[0].length; j++) 
					{
						double secondDivide = ((2.0 * j + 1.0) * x * Math.PI) / 16.0;
						double secondCosine = Math.cos(secondDivide);
						
						sum = sum + (firstCosine * secondCosine * sourceSample[i][j]);
					}
				}		
				dctCoeff[y][x] = CoefficientScalar(x, y) * sum;
			}
		}
		
		return dctCoeff;
	}
	

	private static double[][] InverseDctCalculation(double[][] dctCoeff)
	{
		double [][] sourceSample = new double[dctCoeff.length][dctCoeff[0].length];
		
		for(int j = 0; j < dctCoeff.length; j++) 
		{
			for(int i = 0; i < dctCoeff[0].length; i++) 
			{
				double sum = 0.0;
				for(int u = 0; u < dctCoeff.length; u++) 
				{
					double firstDivide = ((2.0 * i + 1.0) * u * Math.PI) / 16;
					double firstCosine = Math.cos(firstDivide);
					for(int v = 0; v < dctCoeff[0].length; v++) 
					{
						double secondDivide = ((2.0 * j + 1.0) * v * Math.PI) / 16.0;
						double secondCosine = Math.cos(secondDivide);
						
						sum += CoefficientScalar(u, v) * firstCosine * secondCosine * dctCoeff[v][u];
					}
				}
				sourceSample[j][i] = sum;
			}
		}
		return sourceSample;
	}

	
	public static double CoefficientScalar(int x, int y) 
	{
		double c1 = (x == 0) ? Math.sqrt(2.0)/2.0 : 1.0;
		double c2 = (y == 0) ? Math.sqrt(2.0)/2.0 : 1.0;
		
		double result = (c1 * c2) / 4.0;
		return result;
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

//################		Discrete Cosine Transform
		
		assert(Y.length % 8 == 0);
		assert(Y[0].length % 8 == 0);
		
		double[][] inverseY = new double[Y.length][Y[0].length];
		for(int j = 0; j < Y.length/8; j++) 
		{
			for(int i = 0; i < Y[0].length/8; i++) 
			{
				double[][] sourceSample = new double[8][8];
				for(int y = 0; y < sourceSample.length; y++) 
				{
					for(int x = 0; x < sourceSample[0].length; x++) 
					{
						sourceSample[y][x] = Y[(j * 8) + y][(i * 8) + x];
					}	
				}
				
				double[][] dctCoeff = DctCalculation(sourceSample);
				for(int y = 0; y < dctCoeff.length; y++) 
				{
					for(int x = 0; x < dctCoeff[0].length; x++) 
					{
						inverseY[(j * 8) + y][(i * 8) + x] = dctCoeff[y][x];
					}	
				}
			}
		}

		System.out.println();
		
//################		Inverse DCT

		double[][] newY = new double[inverseY.length][inverseY[0].length];
		for(int j = 0; j < inverseY.length/8; j++) 
		{
			for(int i = 0; i < inverseY[0].length/8; i++) 
			{
				double[][] dctCoeff = new double[8][8];
				for(int y = 0; y < dctCoeff.length; y++) 
				{
					for(int x = 0; x < dctCoeff[0].length; x++) 
					{
						dctCoeff[y][x] = inverseY[(j * 8) + y][(i * 8) + x];
					}	
				}
				
				double[][] sourceSample = InverseDctCalculation(dctCoeff);
				for(int y = 0; y < sourceSample.length; y++) 
				{
					for(int x = 0; x < sourceSample[0].length; x++) 
					{
						newY[(j * 8) + y][(i * 8) + x] = sourceSample[y][x];
					}	
				}
			}
		}
		
//################		Inverse Color space transformation and Supersampling

		Image newPaddedImage = InverseColorSpaceTransformationSubsampling(newY, subSampleCb, subSampleCr);
		newPaddedImage.display();
		
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
