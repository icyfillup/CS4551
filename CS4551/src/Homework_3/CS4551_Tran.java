package Homework_3;

import java.util.Scanner;

public class CS4551_Tran
{
	private static int[][] LuminanceQuantizationTable()
	{
		int[][] result = new int[][] {{4, 4, 4, 8, 8, 16, 16, 32},
										{4, 4, 4, 8, 8, 16, 16, 32},
										{4, 4, 8, 8, 16, 16, 32, 32},
										{8, 8, 8, 16, 16, 32, 32, 32},
										{8, 8, 16, 16, 32, 32, 32, 32},
										{16, 16, 16, 32, 32, 32, 32, 32},
										{16, 16, 32, 32, 32, 32, 32, 32},
										{32, 32, 32, 32, 32, 32, 32, 32}};
										
		return result;
	}
	
	private static int[][] ChrominanceQuantizationTable()
	{
		int[][] result = new int[][] {{8, 8, 8, 16, 32, 32, 32, 32},
										{8, 8, 8, 16, 32, 32, 32, 32},
										{8, 8, 16, 32, 32, 32, 32, 32},
										{16, 16, 32, 32, 32, 32, 32, 32},
										{32, 32, 32, 32, 32, 32, 32, 32},
										{32, 32, 32, 32, 32, 32, 32, 32},
										{32, 32, 32, 32, 32, 32, 32, 32},
										{32, 32, 32, 32, 32, 32, 32, 32},};
										
		return result;
	}
	
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
	
	private static Image RemoveImagePadding(Image paddedImage) 
	{
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
		
		Image newImage = new Image(originalWidth, originalHeight);
		
		for(int y = 0; y < newImage.getH(); y++) 
		{
			for(int x = 0; x < newImage.getW(); x++)
			{
				int[] rgb = new int[3];
				paddedImage.getPixel(x, y, rgb);
				newImage.setPixel(x, y, rgb);
			}	
		}
		
		return newImage;
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

	private static double[][] DctConversion(double[][] sourceChannel)
	{
		assert(sourceChannel.length % 8 == 0);
		assert(sourceChannel[0].length % 8 == 0);
		
		double[][] inverseY = new double[sourceChannel.length][sourceChannel[0].length];
		for(int j = 0; j < sourceChannel.length/8; j++) 
		{
			for(int i = 0; i < sourceChannel[0].length/8; i++) 
			{
				double[][] sourceSample = new double[8][8];
				for(int y = 0; y < sourceSample.length; y++) 
				{
					for(int x = 0; x < sourceSample[0].length; x++) 
					{
						sourceSample[y][x] = sourceChannel[(j * 8) + y][(i * 8) + x];
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
		
		return inverseY;
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
				
				double min = -1 * Math.pow(2, 10);
				double max = Math.pow(2, 10);
				
				if(dctCoeff[y][x] < min) 
				{
					dctCoeff[y][x] = min;
				}
				else if(dctCoeff[y][x] > max) 
				{
					dctCoeff[y][x] = max;
				}
				
			}
		}
		
		return dctCoeff;
	}
	
	private static void QuantizeSample(double[][] inverseY, double[][] inverseCb, double[][] inverseCr, int nLevel)
	{
		int[][] yQuantizationTable = LuminanceQuantizationTable();
		int[][] cbcrChrominanceTable = ChrominanceQuantizationTable();
		
		for(int u = 0; u < inverseY.length; u++) 
		{
			for(int v = 0; v < inverseY[0].length; v++) 
			{
				double yPrime = yQuantizationTable[u % 8][v % 8] * Math.pow(2, nLevel);
				inverseY[u][v] = (inverseY[u][v] / yPrime);
			}
		}
		
		for(int u = 0; u < inverseCb.length; u++) 
		{
			for(int v = 0; v < inverseCb[0].length; v++) 
			{
				double cbPrime = cbcrChrominanceTable[u % 8][v % 8] * Math.pow(2, nLevel);
				inverseCb[u][v] = (inverseCb[u][v] / cbPrime);
			}
		}
		
		for(int u = 0; u < inverseCr.length; u++) 
		{
			for(int v = 0; v < inverseCr[0].length; v++) 
			{
				double crPrime = cbcrChrominanceTable[u % 8][v % 8] * Math.pow(2, nLevel);
				inverseCr[u][v] = (inverseCr[u][v] / crPrime);
			}
		}
	}
	
	private static void DeQuantizeSample(double[][] inverseY, double[][] inverseCb, double[][] inverseCr, int nLevel)
	{
		int[][] yQuantizationTable = LuminanceQuantizationTable();
		int[][] cbcrChrominanceTable = ChrominanceQuantizationTable();
		
		for(int u = 0; u < inverseY.length; u++) 
		{
			for(int v = 0; v < inverseY[0].length; v++) 
			{
				double yPrime = yQuantizationTable[u % 8][v % 8] * Math.pow(2, nLevel);
				inverseY[u][v] = inverseY[u][v] * yPrime;
			}
		}
		
		for(int u = 0; u < inverseCb.length; u++) 
		{
			for(int v = 0; v < inverseCb[0].length; v++) 
			{
				double cbPrime = cbcrChrominanceTable[u % 8][v % 8] * Math.pow(2, nLevel);
				inverseCb[u][v] = inverseCb[u][v] * cbPrime;
			}
		}
		
		for(int u = 0; u < inverseCr.length; u++) 
		{
			for(int v = 0; v < inverseCr[0].length; v++) 
			{
				double crPrime = cbcrChrominanceTable[u % 8][v % 8] * Math.pow(2, nLevel);
				inverseCr[u][v] = inverseCr[u][v] * crPrime;
			}
		}
	}

	private static double[][] InverseDctCalculation(double[][] dctCoeff)
	{
		assert(dctCoeff.length % 8 == 0);
		assert(dctCoeff[0].length % 8 == 0);
		
		double [][] sourceChannel = new double[dctCoeff.length][dctCoeff[0].length];
		
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
				sourceChannel[j][i] = sum;
			}
		}
		return sourceChannel;
	}

	private static double[][] InverseDctConversion(double[][] InverseChannel)
	{
		double[][] newChannel = new double[InverseChannel.length][InverseChannel[0].length];
		for(int j = 0; j < InverseChannel.length/8; j++) 
		{
			for(int i = 0; i < InverseChannel[0].length/8; i++) 
			{
				double[][] dctCoeff = new double[8][8];
				for(int y = 0; y < dctCoeff.length; y++) 
				{
					for(int x = 0; x < dctCoeff[0].length; x++) 
					{
						dctCoeff[y][x] = InverseChannel[(j * 8) + y][(i * 8) + x];
					}	
				}
				
				double[][] sourceSample = InverseDctCalculation(dctCoeff);
				for(int y = 0; y < sourceSample.length; y++) 
				{
					for(int x = 0; x < sourceSample[0].length; x++) 
					{
						newChannel[(j * 8) + y][(i * 8) + x] = sourceSample[y][x];
					}	
				}
			}
		}
		
		return newChannel;
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
		
		Scanner input = new Scanner(System.in);
		String n = null;
		do
		{
			System.out.print("Select compression quality between 0 - 5: ");
			n = input.next();	
//			n = "0";
		}
		while(!(n.contains("0") || n.contains("1") || n.contains("2") || 
				n.contains("3") || n.contains("4") || n.contains("5")));
		
		int nLevel = Integer.parseInt(n);
		
		
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
		
		double[][] inverseY = DctConversion(Y);
		double[][] inverseCb = DctConversion(subSampleCb);
		double[][] inverseCr = DctConversion(subSampleCr);
		
//################		Quantization		
		
		QuantizeSample(inverseY, inverseCb, inverseCr, nLevel);
		
		
//################		De-quantization		
		
		DeQuantizeSample(inverseY, inverseCb, inverseCr, nLevel);
		
//################		Inverse DCT

		double[][] newY = InverseDctConversion(inverseY);
		double[][] newSubSampleCb = InverseDctConversion(inverseCb);
		double[][] newSubSampleCr = InverseDctConversion(inverseCr);
		
//################		Inverse Color space transformation and Supersampling

		Image newPaddedImage = InverseColorSpaceTransformationSubsampling(newY, newSubSampleCb, newSubSampleCr);
		newPaddedImage.display();
//################		Remove Padding and Display the image
		
		Image newImage = RemoveImagePadding(newPaddedImage);
		newImage.display();
		
		input.close();
		System.exit(0);
	}
}
