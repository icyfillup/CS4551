package setupImageProject;

/*******************************************************
 * CS4551 Multimedia Software Systems @ Author: Elaine Kang
 *******************************************************/

//
// Template Code - demonstrate how to use Image class

public class CS4551_Main
{
	public static void main(String[] args)
  {
	// if there is no commandline argument, exit the program
    if(args.length != 1)
    {
      usage();
      System.exit(1);
    }

    System.out.println("--Welcome to Multimedia Software System--");

    // Create an Image object with the input PPM file name.
    // Display it and write it into another PPM file.
    Image img = new Image(args[0]);
    img.display();
    img.write2PPM("Image/out.ppm");

    System.out.println("--Good Bye--");
  }

	public static void usage()
	{
		System.out.println("\nUsage: java CS4551_Main [input_ppm_file]\n");
	}
}
