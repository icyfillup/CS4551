All the programming done for this assignment was on a windows machine. 
If you can, compile and run this java program on a windows computer. 
This program was never tested on a IOS machine.

I am going to assume that this program is going to be run on a windows computer and 
that it has the java and javac program path attached to the environment variables

--------------------------------------------------------------------------------------

Before running the java program, put all the java file into one directory.
In the same directory, put all the .ppm image files you want to process.

Open the command line in that directory.

To compile all java file, type 
	javac *.java

To run the java program, type 
	java CS4551_Tran [image_input_filename]
		ex. java CS4551_Tran ducky.ppm
		
An image file from the java program will be outputted 
in the same directory where the java program was ran.
	
--------------------------------------------------------------------------------------

[NOTE]: the [image_input_filename] can either be a relative filepath or an absolute filepath. Depending on where the image file come from, the output image file can be in a different location. For instance, if image_input_filename is "C:/My Document/ducky.ppm", then the output image file will be at "C:/My Document/ducky_1.ppm".

[Recommend]: the [image_input_filename] should be a image filename inside the java program directory where you put all my java files in. Doing so will help the program output image files in the same directory.