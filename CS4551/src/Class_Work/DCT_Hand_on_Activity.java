package Class_Work;

public class DCT_Hand_on_Activity
{
	public static void SetupDCTValues(double [][] A) 
	{
		A[0][0] = 139.0;
		A[0][1] = 144.0;
		A[0][2] = 149.0;
		A[0][3] = 153.0;
		A[0][4] = 155.0;
		A[0][5] = 155.0;
		A[0][6] = 155.0;
		A[0][7] = 155.0;
		
		A[1][0] = 144.0;
		A[1][1] = 151.0;
		A[1][2] = 153.0;
		A[1][3] = 156.0;
		A[1][4] = 159.0;
		A[1][5] = 156.0;
		A[1][6] = 156.0;
		A[1][7] = 156.0;
		
		A[2][0] = 150.0;
		A[2][1] = 155.0;
		A[2][2] = 160.0;
		A[2][3] = 163.0;
		A[2][4] = 158.0;
		A[2][5] = 156.0;
		A[2][6] = 156.0;
		A[2][7] = 156.0;
		
		A[3][0] = 159.0;
		A[3][1] = 161.0;
		A[3][2] = 162.0;
		A[3][3] = 160.0;
		A[3][4] = 160.0;
		A[3][5] = 159.0;
		A[3][6] = 159.0;
		A[3][7] = 159.0;
		
		A[4][0] = 159.0;
		A[4][1] = 160.0;
		A[4][2] = 161.0;
		A[4][3] = 162.0;
		A[4][4] = 162.0;
		A[4][5] = 155.0;
		A[4][6] = 155.0;
		A[4][7] = 155.0;
		
		A[5][0] = 161.0;
		A[5][1] = 161.0;
		A[5][2] = 161.0;
		A[5][3] = 161.0;
		A[5][4] = 160.0;
		A[5][5] = 157.0;
		A[5][6] = 157.0;
		A[5][7] = 157.0;
		
		A[6][0] = 162.0;
		A[6][1] = 162.0;
		A[6][2] = 161.0;
		A[6][3] = 163.0;
		A[6][4] = 162.0;
		A[6][5] = 157.0;
		A[6][6] = 157.0;
		A[6][7] = 157.0;
		
		A[7][0] = 162.0;
		A[7][1] = 162.0;
		A[7][2] = 161.0;
		A[7][3] = 161.0;
		A[7][4] = 163.0;
		A[7][5] = 158.0;
		A[7][6] = 158.0;
		A[7][7] = 158.0;
	}
	
	public static void DisplayDCTValues(double [][] A)
	{
		for(int i = 0; i < A.length; i++) 
		{
			for(int j = 0; j < A[0].length; j++) 
			{
				System.out.print(A[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public static double CoefficientScalar(int x, int y) 
	{
		double c1 = (x == 0) ? Math.sqrt(2.0)/2.0 : 1.0;
		double c2 = (y == 0) ? Math.sqrt(2.0)/2.0 : 1.0;
		
		double result = (c1 * c2) / 4.0;
		return result;
	}

	public static void ShiftDCTValues(double[][] A, int index)
	{
		for(int i = 0; i < A.length; i++) 
		{
			for(int j = 0; j < A[0].length; j++) 
			{
				A[i][j] = A[i][j] + index;
			}
		}
	}
	
	public static void main(String[] args)
	{
		double [][] A = new double[8][8];
		
		SetupDCTValues(A);
		
		ShiftDCTValues(A, -128);
		
		DisplayDCTValues(A);
		
		double [][] B = new double[8][8];
		int x = 0;
		int y = 0;
		double sum = 0.0;
		for(int i = 0; i < A.length; i++) 
		{
			double firstDivide = ((2.0 * i + 1.0) * y * Math.PI) / 16.0;
			double firstCosine = Math.cos(firstDivide);
			
			for(int j = 0; j < A[0].length; j++) 
			{
				double secondDivide = ((2.0 * j + 1.0) * x * Math.PI) / 16.0;
				double secondCosine = Math.cos(secondDivide);
				
				if(i == 4 && j == 4) 
				{
					System.out.println("hello");
				}
				
				
				sum = sum + (firstCosine * secondCosine * A[i][j]);
				System.out.println(sum);
				
			}
			
		}
		
		
		System.out.println(CoefficientScalar(x, y) * sum);
		
		
		
	}


}
