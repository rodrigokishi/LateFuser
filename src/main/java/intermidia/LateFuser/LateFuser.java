package intermidia.LateFuser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class LateFuser 
{
    public static void main( String[] args ) throws Exception
    {
    	//Count how many different segmentations are being fed as input
    	int segmentationQty = args.length - 1;
    	
    	//An arraylist of integer pairs for each modality. 
    	ArrayList<ArrayList<int[]>> segmList = new ArrayList<ArrayList<int[]>>();
    	
    	//Read an input for each modality
    	for(int i = 0; i < segmentationQty; i++)
    	{
    		FileReader fileReader = new FileReader(new File(args[i]));
    		BufferedReader bufferedReader = new BufferedReader(fileReader);
    		
    		
    		segmList.add(new ArrayList<int[]>());
    		
    		//Fills the segmentation arraylist for a modality
    		while(bufferedReader.ready())
    		{
    			String line[] = bufferedReader.readLine().split(",\\s");
    			segmList.get(segmList.size() -1).add(
    					new int[]
    					{
    							Integer.parseInt(line[0]),
    							Integer.parseInt(line[1])
    					}
    					);    			 
    		}    		
    		bufferedReader.close();
    	}
    	int shotArraySize = segmList.get(0).get(segmList.get(0).size() - 1)[1];    	
    	
    	//Create and initialize boundary assignment matrix
    	int boundaryAssignmentMatrix[][] = new int[segmentationQty][shotArraySize];
    	for(int i = 0; i < segmentationQty; i++)
    	{
    		for(int j = 0; j < shotArraySize; j++)
    		{
    			boundaryAssignmentMatrix[i][j] = 0;
    		}
    	}
    	
    	//Fills the boundary assignment matrix 
    	for(int i = 0; i < segmentationQty; i++)
    	{
    		for(int[] boundaries : segmList.get(i))
    		{
    			boundaryAssignmentMatrix[i][boundaries[0] - 1] = 1;
    		}
    	}
    	
    	//Prints the assignment matrix
/*    	for(int i = 0; i < segmentationQty; i++)
    	{
    		for(int j = 0; j < shotArraySize; j++)
    		{
    			System.out.print(boundaryAssignmentMatrix[i][j] + " ");
    		}
    		System.out.println();
    	}
    	System.out.println();*/
    	
    	
    	//Performs a consensual decision
    	FileWriter fileWriter = new FileWriter(new File(args[args.length - 1]));
    	for(int j = 0; j < shotArraySize; j++)
    	{
    		int agreementCount = 0;
    		for(int i = 0; i < segmentationQty; i++)
    		{
    			agreementCount += boundaryAssignmentMatrix[i][j]; 
    		}
    		//If more than half predictions agree on that boundary
    		if(agreementCount > segmentationQty/2)
    		{
    			if(j == 0)
    			{
    				fileWriter.write((j + 1) + "");
    			}
    			else
    			{
    				fileWriter.write(", " + j + "\n" + (j + 1));
    			}
    		}
    	}
    	fileWriter.write(", " + shotArraySize);
    	fileWriter.close();
    }
}
