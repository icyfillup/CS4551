package Class_Work;

/*******************************************************
 CS4551 Multimedia Software Systems
 @ Author: Elaine Kang

 This image class is for a 24bit RGB image only.
 *******************************************************/

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import javax.imageio.stream.FileImageInputStream;

// A wrapper class of BufferedImage
// Provide a couple of utility functions such as reading from and writing to PPM file

public class Image
{
	private BufferedImage img;
	private String fileName; // Input file name
	private int pixelDepth = 3; // pixel depth in byte

	public Image(int w, int h)
	// create an empty image with w(idth) and h(eight)
	{
		fileName = "";
		img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		System.out.println("Created an empty image with size " + w + "x" + h);
	}

	public Image(String fn)
	// Create an image and read the data from the file
	{
		fileName = fn;
		readPPM(fileName);
		System.out.println("Created an image from " + fileName + " with size " + getW() + "x" + getH());
	}

	public int getW()
	{
		return img.getWidth();
	}

	public int getH()
	{
		return img.getHeight();
	}

	public int getSize()
	// return the image size in byte
	{
		return getW() * getH() * pixelDepth;
	}

	public void setPixel(int x, int y, byte[] rgb)
	// set byte rgb values at (x,y)
	{
		int pix = 0xff000000 | ((rgb[0] & 0xff) << 16) | ((rgb[1] & 0xff) << 8) | (rgb[2] & 0xff);
		img.setRGB(x, y, pix);
	}

	public void setPixel(int x, int y, int[] irgb)
	// set int rgb values at (x,y)
	{
		byte[] rgb = new byte[3];

		for (int i = 0; i < 3; i++)
			rgb[i] = (byte) irgb[i];

		setPixel(x, y, rgb);
	}

	public void getPixel(int x, int y, byte[] rgb)
	// retreive rgb values at (x,y) and store in the byte array
	{
		int pix = img.getRGB(x, y);

		rgb[2] = (byte) pix;
		rgb[1] = (byte) (pix >> 8);
		rgb[0] = (byte) (pix >> 16);
	}

	public void getPixel(int x, int y, int[] rgb)
	// retreive rgb values at (x,y) and store in the int array
	{
		int pix = img.getRGB(x, y);

		byte b = (byte) pix;
		byte g = (byte) (pix >> 8);
		byte r = (byte) (pix >> 16);

		// converts singed byte value (~128-127) to unsigned byte value (0~255)
		rgb[0] = (int) (0xFF & r);
		rgb[1] = (int) (0xFF & g);
		rgb[2] = (int) (0xFF & b);
	}

	public void displayPixelValue(int x, int y)
	// Display rgb pixel in unsigned byte value (0~255)
	{
		int pix = img.getRGB(x, y);

		byte b = (byte) pix;
		byte g = (byte) (pix >> 8);
		byte r = (byte) (pix >> 16);

		System.out.println(
				"RGB Pixel value at (" + x + "," + y + "):" + (0xFF & r) + "," + (0xFF & g) + "," + (0xFF & b));
	}

	public void readPPM(String fileName)
	// read a data from a PPM file
	{
		File fIn = null;
		FileImageInputStream fis = null;

		try
		{
			fIn = new File(fileName);
			fis = new FileImageInputStream(fIn);

			System.out.println("Reading " + fileName + "...");

			// read Identifier
			if (!fis.readLine().equals("P6"))
			{
				System.err.println("This is NOT P6 PPM. Wrong Format.");
				System.exit(0);
			}

			// read Comment line
			String commentString = fis.readLine();

			// read width & height
			String[] WidthHeight = fis.readLine().split(" ");
			int width = Integer.parseInt(WidthHeight[0]);
			int height = Integer.parseInt(WidthHeight[1]);

			// read maximum value
			int maxVal = Integer.parseInt(fis.readLine());

			if (maxVal != 255)
			{
				System.err.println("Max val is not 255");
				System.exit(0);
			}

			// read binary data byte by byte and save it into BufferedImage object
			int x, y;
			byte[] rgb = new byte[3];
			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			for (y = 0; y < getH(); y++)
			{
				for (x = 0; x < getW(); x++)
				{
					rgb[0] = fis.readByte();
					rgb[1] = fis.readByte();
					rgb[2] = fis.readByte();
					setPixel(x, y, rgb);
				}
			}

			fis.close();

			System.out.println("Read " + fileName + " Successfully.");

		} // try
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}

	public void write2PPM(String fileName)
	// wrrite the image data in img to a PPM file
	{
		FileOutputStream fos = null;
		PrintWriter dos = null;

		try
		{
			fos = new FileOutputStream(fileName);
			dos = new PrintWriter(fos);

			System.out.println("Writing the Image buffer into " + fileName + "...");

			// write header
			dos.print("P6" + "\n");
			dos.print("#CS451" + "\n");
			dos.print(getW() + " " + getH() + "\n");
			dos.print(255 + "\n");
			dos.flush();

			// write data
			int x, y;
			byte[] rgb = new byte[3];
			for (y = 0; y < getH(); y++)
			{
				for (x = 0; x < getW(); x++)
				{
					getPixel(x, y, rgb);
					fos.write(rgb[0]);
					fos.write(rgb[1]);
					fos.write(rgb[2]);

				}
				fos.flush();
			}
			dos.close();
			fos.close();

			System.out.println("Wrote into " + fileName + " Successfully.");

		} // try
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
	
//My code below///////////////////////////////////////////////////////////////////////////////////////	

	public void setFileName(String filename) 
	{
		fileName = filename;
	}
	
	public void display()
	// display the image on the screen
	{
		// Use a label to display the image
		// String title = "Image Name - " + fileName;
		String title = fileName;
		JFrame frame = new JFrame(title);
		JLabel label = new JLabel(new ImageIcon(img));
		frame.add(label, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public Image GrayScaleConversion()
	{
		Image grayScaleImage = new Image(getW(), getH());

		for (int y = 0; y < getH(); y++)
		{
			for (int x = 0; x < getW(); x++)
			{
				int[] rgb = new int[3];
				getPixel(x, y, rgb);

				double GrayD = Math.round(0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]);
				int Gray = (int) GrayD;

				if (Gray > 255)
				{
					Gray = 255;
				}

				if (Gray < 0)
				{
					Gray = 0;
				}

				rgb[0] = Gray;
				rgb[1] = Gray;
				rgb[2] = Gray;

				grayScaleImage.setPixel(x, y, rgb);
			}
		}

		String[] splitFilePath = fileName.split("\\\\");
		String newFileName = fileName.replace(splitFilePath[splitFilePath.length - 1], "grayscale.ppm");
		grayScaleImage.write2PPM(newFileName);

		return grayScaleImage;
	}

	public Image NLevelConversion(int nLevel)
	{
		// nLevel = 8;

		final int BIT_VALUE = 256;
		final int GRAY_VALUE = BIT_VALUE - 1;
		double grayInterval = GRAY_VALUE / (nLevel - 1.0);
		double grayCount = 0;
		ArrayList<Integer> grayValues = new ArrayList<>();
		grayValues.add((int) grayCount);
		while (grayCount < GRAY_VALUE)
		{
			grayCount = grayCount + grayInterval;
			grayValues.add((int) Math.floor(grayCount));
		}

		Image grayScaleImage = GrayScaleConversion();

		Image NLevelImage = new Image(grayScaleImage.getW(), grayScaleImage.getH());
		for (int y = 0; y < getH(); y++)
		{
			for (int x = 0; x < getW(); x++)
			{
				int[] rgb = new int[3];
				grayScaleImage.getPixel(x, y, rgb);

				int newGrayValue = rgb[0];
				for (int i = 1; i < grayValues.size(); i++)
				{
					if (rgb[0] < grayValues.get(i))
					{
						int highGrayValue = grayValues.get(i);
						int lowGrayValue = grayValues.get(i - 1);

						int highBound = Math.abs(highGrayValue - rgb[0]);
						int lowBound = Math.abs(lowGrayValue - rgb[0]);

						// the Bounds that has the lower number means that the gray pixal value
						// is closer to one of the n_level values
						if (lowBound < highBound)
						{
							newGrayValue = lowGrayValue;
						}
						else
						{
							newGrayValue = highGrayValue;
						}

						break;
					}
				}

				rgb[0] = newGrayValue;
				rgb[1] = newGrayValue;
				rgb[2] = newGrayValue;

				NLevelImage.setPixel(x, y, rgb);
				// image.setPixel(x, y, rgb);
			}
		}

		String[] splitFilePath = fileName.split("\\\\");
		String newFileName = fileName.replace(splitFilePath[splitFilePath.length - 1], "Output1.ppm");
		NLevelImage.write2PPM(newFileName);

		return NLevelImage;
	}

	public Image NLevelErrorDiffusionConversion(int nLevel)
	{
		// nLevel = 2;

		final int BIT_VALUE = 256;
		final int GRAY_VALUE = BIT_VALUE - 1;
		double grayInterval = GRAY_VALUE / (nLevel - 1.0);
		double grayCount = 0;
		ArrayList<Integer> grayValues = new ArrayList<>();
		while (grayCount < BIT_VALUE)
		{
			grayValues.add((int) grayCount);
			grayCount = grayCount + grayInterval;
		}

		Image grayScaleImage = GrayScaleConversion();

		Image NLevelErrorDiffusionImage = new Image(grayScaleImage.getW(), grayScaleImage.getH());
		for (int y = 0; y < getH(); y++)
		{
			for (int x = 0; x < getW(); x++)
			{
				int[] rgb = new int[3];
				grayScaleImage.getPixel(x, y, rgb);

				double grayValue = rgb[0];

				for (int i = 1; i < grayValues.size(); i++)
				{
					int grayPart = grayValues.get(i);
					if (rgb[0] <= grayPart)
					{
						double highGrayPart = grayValues.get(i);
						double lowGrayPart = grayValues.get(i - 1);

						double lowBound = Math.abs(lowGrayPart - rgb[0]);
						double highBound = Math.abs(highGrayPart - rgb[0]);

						// the Bounds that has the lower number means that the gray pixal value
						// is closer to one of the n_level values
						if (lowBound < highBound)
						{
							grayValue = lowGrayPart;
						}
						else
						{
							grayValue = highGrayPart;
						}

						break;
					}
				}

				// if(grayValue < 127)
				// {
				// grayValue = 0;
				// }
				// else
				// {
				// grayValue = 255;
				// }

				int newGrayValue = (int) Math.round(grayValue);
				int[] newRGB = new int[] { newGrayValue, newGrayValue, newGrayValue };
				NLevelErrorDiffusionImage.setPixel(x, y, newRGB);

				double error = (rgb[0] - newRGB[0]);

				// right pixel
				int xOffset = (x + 1);
				int yOffset = y;
				if (xOffset < grayScaleImage.getW())
				{
					filterWeight(grayScaleImage, xOffset, yOffset, error, 7.0, 16.0);
				}

				// right diagonal pixel
				xOffset = (x + 1);
				yOffset = (y + 1);
				if (xOffset < grayScaleImage.getW() && yOffset < grayScaleImage.getH())
				{
					filterWeight(grayScaleImage, xOffset, yOffset, error, 1.0, 16.0);
				}

				// bottom pixel
				xOffset = x;
				yOffset = (y + 1);
				if (yOffset < grayScaleImage.getH())
				{
					filterWeight(grayScaleImage, xOffset, yOffset, error, 5.0, 16.0);
				}

				// left diagonal pixel
				xOffset = (x - 1);
				yOffset = (y + 1);
				if (xOffset >= 0 && xOffset < grayScaleImage.getW() && yOffset < grayScaleImage.getH())
				{
					filterWeight(grayScaleImage, xOffset, yOffset, error, 3.0, 16.0);
				}
			}
		}

		String[] splitFilePath = fileName.split("\\\\");
		String newFileName = fileName.replace(splitFilePath[splitFilePath.length - 1], "Output2.ppm");
		NLevelErrorDiffusionImage.write2PPM(newFileName);
		return NLevelErrorDiffusionImage;
	}

	private static void filterWeight(Image grayScaleImage, int xOffset, int yOffset, double error, double num,
			double dem)
	{
		int[] newRGB = new int[3];
		grayScaleImage.getPixel(xOffset, yOffset, newRGB);

		double errorDiffusionD = newRGB[0] + (error * (num / dem));
		int errorDiffusion = (int) Math.round(errorDiffusionD);

		newRGB[0] = errorDiffusion;
		newRGB[1] = errorDiffusion;
		newRGB[2] = errorDiffusion;

		grayScaleImage.setPixel(xOffset, yOffset, newRGB);
	}

	public Image UniformColorQuantization()
	{
		final int MAX_BIT_VALUE = 256;

		System.out.println("index\tred\tgreen\tblue");
		System.out.println("----------------------------");

		Map<Integer, int[]> lookUpTable = new HashMap<>();
		for (int index = 0; index < MAX_BIT_VALUE; index++)
		{
			int redIndex = (0xff & (index & 224)) >> 5;
			int greenIndex = (0xff & (index & 28)) >> 2;
			int blueIndex = (0xff & (index & 3));

			int red = (redIndex * 32) + 16;
			int green = (greenIndex * 32) + 16;
			int blue = (blueIndex * 64) + 32;

			lookUpTable.put(index, new int[] { red, green, blue });

			System.out.println(index + "\t" + red + "\t" + green + "\t" + blue);
		}

		Image indexImage = new Image(getW(), getH());
		for (int y = 0; y < getH(); y++)
		{
			for (int x = 0; x < getW(); x++)
			{
				int[] rgb = new int[3];
				getPixel(x, y, rgb);

				int redIndex = SegmentIndex(MAX_BIT_VALUE, 8, rgb[0]);
				int greenIndex = SegmentIndex(MAX_BIT_VALUE, 8, rgb[1]);
				int blueIndex = SegmentIndex(MAX_BIT_VALUE, 4, rgb[2]);

				int colorIndex = 0xFF & ((redIndex << 5) | (greenIndex << 2) | blueIndex);

				indexImage.setPixel(x, y, new int[] { colorIndex, colorIndex, colorIndex });
			}
		}

		Image indexColorImage = new Image(getW(), getH());
		for (int y = 0; y < getH(); y++)
		{
			for (int x = 0; x < getW(); x++)
			{
				int[] rgb = new int[3];
				indexImage.getPixel(x, y, rgb);
				indexColorImage.setPixel(x, y, lookUpTable.get(rgb[0]));
			}
		}

		indexImage.write2PPM(fileName.replace(".ppm", "") + "-index.ppm");
		indexColorImage.write2PPM(fileName.replace(".ppm", "") + "-QT8.ppm");
		return indexColorImage;
	}

	private static int SegmentIndex(int bitValue, int segment, int colorValue)
	{
		int indexResult = 0;
		int redSegmentInterval = bitValue / segment;
		while (indexResult < segment)
		{
			int increment = ((indexResult + 1) * redSegmentInterval);
			if (colorValue < increment)
			{
				break;
			}
			indexResult++;
		}
		return indexResult;
	}
	
	public static boolean isCircleOutOfBound(Image circle, int X, int Y)
	{
		boolean result = true;
		if (X < circle.getW() && X >= 0 && Y < circle.getH() && Y >= 0)
		{
			result = false;
		}

		return result;
	}

	public static Image RadialCirclePattern(int M, int N)
	{
		Image radialCircle = new Image(512, 512);
		int[] white = new int[] { 255, 255, 255 };
		for (int y = 0; y < radialCircle.getH(); y++)
		{
			for (int x = 0; x < radialCircle.getW(); x++)
			{
				radialCircle.setPixel(x, y, white);
			}
		}

		int centerX = radialCircle.getW() / 2;
		int centerY = radialCircle.getH() / 2;
		int[] black = new int[] { 0, 0, 0 };

		int increment = 1;
		boolean expand = true;
		while (expand)
		{
			int radiusSize = N * increment;

			for (int thicc = 0; thicc < M; thicc++)
			{
				int thiccnessX = centerX + thicc;
				int thiccnessY = centerY + thicc;

				for (float deg = 0; deg <= 90; deg += 0.001)
				{
					float radian = (float) ((Math.PI / 180.0) * (float) deg);

					int Quad1X = (int) Math.round(thiccnessX + (radiusSize * Math.cos(radian)));
					int Quad1Y = (int) Math.round(thiccnessY + (radiusSize * Math.sin(radian)));
					boolean isQuad1OutOfBound = isCircleOutOfBound(radialCircle, Quad1X, Quad1Y);

					if (!isQuad1OutOfBound)
					{
						int Quad2X = (int) Math.round(centerX - Math.abs(centerX - Quad1X));
						int Quad2Y = (int) Math.round(Quad1Y);
						boolean isQuad2OutOfBound = isCircleOutOfBound(radialCircle, Quad2X, Quad2Y);

						if (!isQuad2OutOfBound)
						{
							int Quad3X = (int) Math.round(centerX - Math.abs(centerX - Quad1X));
							int Quad3Y = (int) Math.round(centerY - Math.abs(centerY - Quad1Y));
							boolean isQuad3OutOfBound = isCircleOutOfBound(radialCircle, Quad3X, Quad3Y);

							if (!isQuad3OutOfBound)
							{
								int Quad4X = (int) Math.round(Quad1X);
								int Quad4Y = (int) Math.round(centerY - Math.abs(centerY - Quad1Y));
								boolean isQuad4OutOfBound = isCircleOutOfBound(radialCircle, Quad4X, Quad4Y);

								expand = expand && !isQuad1OutOfBound && !isQuad2OutOfBound && !isQuad3OutOfBound
										&& !isQuad4OutOfBound;

								if (expand)
								{
									radialCircle.setPixel(Quad1X, Quad1Y, black);
									radialCircle.setPixel(Quad2X, Quad2Y, black);
									radialCircle.setPixel(Quad3X, Quad3Y, black);
									radialCircle.setPixel(Quad4X, Quad4Y, black);
								}
							}
							else
								expand = false;
						}
						else
							expand = false;
					}
					else
						expand = false;
				}
			}

			increment++;
		}

		String radialFileName = "Radial_Circle.ppm";
		radialCircle.setFileName(radialFileName);
		radialCircle.write2PPM(radialFileName);
		radialCircle.display();
		
		return radialCircle;
	}

	public static void ResizeCirclePattern(Image originalCircle, int K)
	{
		Image resizeNoFilterCircle = new Image(originalCircle.getW() / K, originalCircle.getH() / K);
		Image resizeFilter1Circle = new Image(originalCircle.getW() / K, originalCircle.getH() / K);
		Image resizeFilter2Circle = new Image(originalCircle.getW() / K, originalCircle.getH() / K);

		int[] white = new int[] { 255, 255, 255 };
		for (int y = 0; y < resizeNoFilterCircle.getH(); y++)
		{
			for (int x = 0; x < resizeNoFilterCircle.getW(); x++)
			{
				resizeNoFilterCircle.setPixel(x, y, white);
				resizeFilter1Circle.setPixel(x, y, white);
				resizeFilter2Circle.setPixel(x, y, white);
			}
		}

		for (int y = 0; y < resizeNoFilterCircle.getH(); y++)
		{
			int originalCircleOffsetY = y * K;
			for (int x = 0; x < resizeNoFilterCircle.getW(); x++)
			{
				int originalCircleOffsetX = x * K;

				int[] rgb = new int[3];
				originalCircle.getPixel(originalCircleOffsetX, originalCircleOffsetY, rgb);
				resizeNoFilterCircle.setPixel(x, y, rgb);
				setFliter1(originalCircle, resizeFilter1Circle, originalCircleOffsetX, originalCircleOffsetY, x, y,
						rgb);
				setFliter2(originalCircle, resizeFilter2Circle, originalCircleOffsetX, originalCircleOffsetY, x, y,
						rgb);
			}
		}

		String noFilterFileName = "Resize_No-Filter_Radial_Circle.ppm";
		String filter1FileName = "Resize_Filter-1_Radial_Circle.ppm";
		String filter2FileName = "Resize_Filter-2_Radial_Circle.ppm";
		
		resizeNoFilterCircle.setFileName(noFilterFileName);
		resizeFilter1Circle.setFileName(filter1FileName);
		resizeFilter2Circle.setFileName(filter2FileName);
		
		resizeNoFilterCircle.write2PPM(noFilterFileName);
		resizeFilter1Circle.write2PPM(filter1FileName);
		resizeFilter2Circle.write2PPM(filter2FileName);
		
		resizeNoFilterCircle.display();
		resizeFilter1Circle.display();
		resizeFilter2Circle.display();
		
		
//		System.out.println();
	}

	private static void setFliter2(Image originalCircle, Image resizeFilter1Circle, int xOriginal, int yOriginal,
			int xResize, int yResize, int[] rgb)
	{
		float[] weightRGB = new float[] { 0, 0, 0 };

		int topY = yOriginal - 1;

		int topLeftX = xOriginal - 1;
		int topCenterX = xOriginal;
		int topRightX = xOriginal + 1;
		fillInPixelWeightFilter(originalCircle, weightRGB, topLeftX, topY, 1.0 / 16.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, topCenterX, topY, 2.0 / 16.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, topRightX, topY, 1.0 / 16.0);

		int centerY = yOriginal;

		int leftX = xOriginal - 1;
		int centerX = xOriginal;
		int rightX = xOriginal + 1;
		fillInPixelWeightFilter(originalCircle, weightRGB, leftX, centerY, 2.0 / 16.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, centerX, centerY, 4.0 / 16.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, rightX, centerY, 2.0 / 16.0);

		int bottomY = yOriginal + 1;

		int bottomLeftX = xOriginal - 1;
		int bottomCenterX = xOriginal;
		int bottomRightX = xOriginal + 1;
		fillInPixelWeightFilter(originalCircle, weightRGB, bottomLeftX, bottomY, 1.0 / 16.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, bottomCenterX, bottomY, 2.0 / 16.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, bottomCenterX, bottomY, 1.0 / 16.0);

		int[] newWeightRGB = new int[] { Math.round(weightRGB[0]), Math.round(weightRGB[1]), Math.round(weightRGB[2]) };

		resizeFilter1Circle.setPixel(xResize, yResize, newWeightRGB);
	}

	private static void setFliter1(Image originalCircle, Image resizeFilter1Circle, int xOriginal, int yOriginal,
			int xResize, int yResize, int[] rgb)
	{
		float[] weightRGB = new float[] { 0, 0, 0 };

		int topY = yOriginal - 1;

		int topLeftX = xOriginal - 1;
		int topCenterX = xOriginal;
		int topRightX = xOriginal + 1;
		fillInPixelWeightFilter(originalCircle, weightRGB, topLeftX, topY, 1.0 / 9.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, topCenterX, topY, 1.0 / 9.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, topRightX, topY, 1.0 / 9.0);

		int centerY = yOriginal;

		int leftX = xOriginal - 1;
		int centerX = xOriginal;
		int rightX = xOriginal + 1;
		fillInPixelWeightFilter(originalCircle, weightRGB, leftX, centerY, 1.0 / 9.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, centerX, centerY, 1.0 / 9.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, rightX, centerY, 1.0 / 9.0);

		int bottomY = yOriginal + 1;

		int bottomLeftX = xOriginal - 1;
		int bottomCenterX = xOriginal;
		int bottomRightX = xOriginal + 1;
		fillInPixelWeightFilter(originalCircle, weightRGB, bottomLeftX, bottomY, 1.0 / 9.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, bottomCenterX, bottomY, 1.0 / 9.0);
		fillInPixelWeightFilter(originalCircle, weightRGB, bottomCenterX, bottomY, 1.0 / 9.0);

		int[] newWeightRGB = new int[] { Math.round(weightRGB[0]), Math.round(weightRGB[1]), Math.round(weightRGB[2]) };

		resizeFilter1Circle.setPixel(xResize, yResize, newWeightRGB);
	}

	private static void fillInPixelWeightFilter(Image circle, float[] weightRGB, int x, int y, double weight)
	{
		if (!isCircleOutOfBound(circle, x, y))
		{
			int[] oldRGB = new int[3];
			circle.getPixel(x, y, oldRGB);

			if (oldRGB[0] != 0)
				System.out.println();
			weightRGB[0] += oldRGB[0] * weight;
			weightRGB[1] += oldRGB[1] * weight;
			weightRGB[2] += oldRGB[2] * weight;
		}
	}


} // Image class
