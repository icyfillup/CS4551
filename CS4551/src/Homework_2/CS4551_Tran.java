package Homework_2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CS4551_Tran
{
	public static final boolean UserInputDebugMode = false;

	public static void AliasingCirclePattern(Scanner input)
	{
		int hello = 1;
		if (UserInputDebugMode)
		{
			// int[] testingInput = new int[] {1, 20, 2};
			// int[] testingInput = new int[] {1, 20, 4};
			// int[] testingInput = new int[] {3, 20, 2};
			// int[] testingInput = new int[] {3, 20, 4};
			// int[] testingInput = new int[] {5, 40, 2};
			int[] testingInput = new int[] { 5, 40, 4 };

			Image radialCircle = Image.RadialCirclePattern(testingInput[0], testingInput[1]);
			Image.ResizeCirclePattern(radialCircle, testingInput[2]);
		}
		else
		{
			// error checking for thickness
			String thiccnessS = "";
			do
			{
				System.out.print("Enter Thiccness of circle: ");
				thiccnessS = input.next();

				if (!thiccnessS.matches("[0-9]+"))
				{
					System.out.println("Invalid Input\n");
				}
			}
			while (!thiccnessS.matches("[0-9]+"));

			// error checking for distance
			String distanceS = "";
			do
			{
				System.out.print("Enter distance betweens circles: ");
				distanceS = input.next();

				if (!distanceS.matches("[0-9]+"))
				{
					System.out.println("Invalid Input\n");
				}
			}
			while (!distanceS.matches("[0-9]+"));

			// error checking for resize
			String resizeS = "";
			do
			{
				System.out.print("Enter resize value(2, 4, 8, 16): ");
				resizeS = input.next();

				if (!resizeS.matches("[0-9]+")
						|| !(resizeS.equals("2") || resizeS.equals("4") || resizeS.equals("8") || resizeS.equals("16")))
				{
					System.out.println("Invalid Input\n");
				}
			}
			while (!resizeS.matches("[0-9]+")
					|| !(resizeS.equals("2") || resizeS.equals("4") || resizeS.equals("8") || resizeS.equals("16")));

			int thiccness = Integer.parseInt(thiccnessS);
			int distance = Integer.parseInt(distanceS);
			int resize = Integer.parseInt(resizeS);

			Image radialCircle = Image.RadialCirclePattern(thiccness, distance);
			Image.ResizeCirclePattern(radialCircle, resize);
		}

		// System.out.println();
	}

	public static void EncodeDecodeFileMessage(Scanner input)
	{
		String fileName = null;
		if (UserInputDebugMode)
		{
			// fileName = "LZW_test1.txt";
			// fileName = "LZW_test2.txt";
			// fileName = "LZW_test3.txt";
			fileName = "LZW_test4.txt";
		}
		else
		{
			System.out.print("Enter filename: ");
			fileName = input.next();
		}

		String[] outputFileResult = new String[1];
		outputFileResult[0] = fileName + " Results:\n\n";

		String line = null;
		try
		{
			String tempLine = null;

			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((tempLine = bufferedReader.readLine()) != null)
				line = tempLine;

			bufferedReader.close();
			fileReader.close();

			outputFileResult[0] += "Original Text:\n" + line + "\n\n";

			Set<String> initSymbols = new HashSet<>();
			List<Integer> encodedMessage = encoder(line, initSymbols, outputFileResult);
			String decodedMessage = decoder(encodedMessage, initSymbols, outputFileResult);

			if (UserInputDebugMode) checkDebugMessage(line, decodedMessage);

			String[] filenameSpliter = fileName.split("\\.");
			write2TXT(filenameSpliter[0] + "_output.txt", outputFileResult[0]);
			System.out.println(outputFileResult[0]);
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("Unable to open file '" + fileName + "'");
			// ex.printStackTrace();
		}
		catch (IOException ex)
		{
			System.out.println("Error reading file '" + fileName + "'");
			// ex.printStackTrace();
		}

		System.out.println();
	}

	private static void checkDebugMessage(String line, String decodedMessage)
	{
		for (int index = 0; index < line.length(); index++)
		{
			if (line.charAt(index) != decodedMessage.charAt(index))
			{
				System.out.println("not the same");
				break;
			}

		}
		System.out.println();
	}

	private static List<Integer> encoder(String line, Set<String> initSymbols, String[] outputFileResult)
	{
		// line = "abbaabbaababbaaaabaabba";
		for (char symbol : line.toCharArray())
			initSymbols.add("" + symbol);

		List<String> dictionary = new ArrayList<>(initSymbols);

		String s = "";
		List<Integer> encodedMessage = new ArrayList<>();
		for (char c : line.toCharArray())
		{
			if (dictionary.contains(s + c))
			{
				s = s + c;
			}
			else
			{
				encodedMessage.add(dictionary.indexOf(s));

				if (dictionary.size() < Math.pow(2, 8))
				{
					dictionary.add(s + c);
				}

				s = "" + c;
			}
		}
		encodedMessage.add(dictionary.indexOf(s));

		outputFileResult[0] += "Index\tEntry\n-------------\n";
		for (int index = 0; index < dictionary.size(); index++)
		{
			String code = dictionary.get(index);
			outputFileResult[0] += index + "\t " + code + "\n";
		}
		outputFileResult[0] += "\n\nEncoded Text:\n";
		for (int index = 0; index < encodedMessage.size(); index++)
		{
			int encodedIndex = encodedMessage.get(index);
			outputFileResult[0] += encodedIndex + " ";
		}
		outputFileResult[0] += "\n\n";

		return encodedMessage;
	}

	private static String decoder(List<Integer> encodedMessage, Set<String> initSymbols, String[] outputFileResult)
	{
		List<String> dictionary = new ArrayList<>(initSymbols);

		String decodedMessage = "";
		int currentIndex;
		for (int encodedMessageIndex = 0; encodedMessageIndex < encodedMessage.size(); encodedMessageIndex++)
		{
			currentIndex = encodedMessage.get(encodedMessageIndex);
			String currentEntry = dictionary.get(currentIndex);
			decodedMessage = decodedMessage + currentEntry;

			if (encodedMessageIndex + 1 < encodedMessage.size())
			{
				if (dictionary.size() < Math.pow(2, 8))
				{
					if (encodedMessage.get(encodedMessageIndex + 1) < dictionary.size())
					{
						String futureEntry = dictionary.get(encodedMessage.get(encodedMessageIndex + 1));

						dictionary.add(currentEntry + futureEntry.charAt(0));
					}
					else
					{
						dictionary.add(currentEntry + currentEntry.charAt(0));
					}
				}
			}
		}

		outputFileResult[0] += "Decoded Text:\n";
		outputFileResult[0] += decodedMessage + "\n\n";

		double bitPerSymbolBeforeCompression = 8.0;
		double dataSizeBeforeCompression = bitPerSymbolBeforeCompression * decodedMessage.length();

		int bitPerSymbolAfterCompression = 0;
		while (Math.pow(2, bitPerSymbolAfterCompression) < dictionary.size())
			bitPerSymbolAfterCompression++;

		double dataSizeAfterCompression = bitPerSymbolAfterCompression * encodedMessage.size();

		double compressionRatio = dataSizeBeforeCompression / dataSizeAfterCompression;
		outputFileResult[0] += "Compression Ratio:	" + compressionRatio;

		return decodedMessage;
	}

	public static void write2TXT(String fileName, String outputFileResult)
	// wrrite the image data in img to a PPM file
	{
		FileOutputStream fos = null;
		PrintWriter dos = null;

		try
		{
			fos = new FileOutputStream(fileName);
			dos = new PrintWriter(fos);

			System.out.println("Writing the Compression output into " + fileName + "...");

			dos.print(outputFileResult);

			dos.close();
			fos.close();

			System.out.println("Wrote into " + fileName + " Successfully.");

		} // try
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}

	public static boolean MainMenu(Scanner input)
	{
		System.out.println("Main Menu----------------------------");
		System.out.println("1. Aliasing");
		System.out.println("2. Dictionary Coding");
		System.out.println("3. Quit\n");
		System.out.print("Please enter the task number [1-3]: ");

		String option = input.next();
		// String option = "1";

		System.out.println();
		System.out.println();

		boolean isRunning = true;
		switch (option)
		{
			case "1":
			{
				isRunning = true;
				System.out.println("Aliasing");

				AliasingCirclePattern(input);
			}
				break;

			case "2":
			{
				isRunning = true;
				System.out.println("Dictionary Coding");

				EncodeDecodeFileMessage(input);
			}
				break;

			case "3":
			{
				isRunning = false;
				System.out.println("Quit\n");
			}
				break;

			default:
			{
				isRunning = true;
				System.out.println("Invalid input.");
			}
				break;
		}

		return isRunning;
	}

	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		while (MainMenu(input))
		{}

		input.close();
		System.exit(0);
	}

}
