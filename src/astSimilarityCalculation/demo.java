package astSimilarityCalculation;

import java.util.ArrayList;
import java.util.List;

public class demo {
    
    public void demoSimTest()
    {
    	// assuming the codes are in /data/simtest/
    	String A = "/data/simtest/A.cpp";
    	List<String> B = new ArrayList<String>();
    	B.add("/data/simtest/B0.cpp");
    	B.add("/data/simtest/B1.cpp");
    	B.add("/data/simtest/B2.cpp");
    	
    	String codeA = FileToString.readFile(A);
    	List<String> codeB = new ArrayList<String>();
    	for(int i = 0; i < B.size(); i++)
    	{
    		String fileLocation = B.get(i);
    		codeB.add(FileToString.readFile(fileLocation));//read the code from file
    	}
    	
    	GetAstSimilarity gas = new GetAstSimilarity(); 
    	gas.SimilarityCalculation(codeA, codeB);
    	// after calling the function "SimilarityCalculation", the result
    	// will stays in the object gas. 
    	List<Double> similarityList = gas.getSimList(); //get a list of result
    	for (int i = 0; i < similarityList.size(); i++)
    	{
    	    System.out.println("The similarity of the "+ (i+1) + "th code is " + similarityList.get(i));
    	}
    	
    	System.out.println("The highest result is " + gas.getSimilarity());
    	System.out.println("The highest result's index is " + gas.getIndex());
    }
}
