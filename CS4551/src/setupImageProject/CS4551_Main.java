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
    Image img_ = new Image(args[0]);
    for(int y = 0; y < img_.getH(); y++) 
    {
    	for(int x = 0; x < img_.getW(); x++) 
        {
//    		img_.displayPixelValue(x, y);
    		int[] rgb = new int[3];
    		img_.getPixel(x, y, rgb);
    		
    		//rgb[0] = 0;
    		rgb[1] = 0;
    		rgb[2] = 0;
    		
    		img_.setPixel(x, y, rgb);
        }
    	if(y > img_.getH()/2)
    		break;
    }
    img_.write2PPM("src/setupImageProject/image/out.ppm");

    Image img = new Image("src/setupImageProject/image/out.ppm");
    img.display();
    
    System.out.println("--Good Bye--");
  }

	public static void usage()
	{
		System.out.println("\nUsage: java CS4551_Main [input_ppm_file]\n");
	}
}
