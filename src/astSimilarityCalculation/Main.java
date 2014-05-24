package astSimilarityCalculation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class Main {

	/**
	 * @param args
	 */
	private static String A;
	private static List<String> B;
	private static int len;
		
	static {
		A = "";
		B = new ArrayList<String>();
	}

	@Test
	public static void main(String[] args) {
        MainFrame mf = new MainFrame();
        while(true)
        {
        	if(!mf.isVisible())
        	{
        		break;
        	}
        }
//	    
//		long time1 = System.currentTimeMillis();
//		// 读文件
//		len = Integer.parseInt(args[0]);
//		A = FileToString.readFile(args[1] + "/A.cpp");
//		for (int i = 0; i < len; i++) {
//			B.add(FileToString.readFile(args[1] + "/B" + (i) + ".cpp"));
//			// B.add(FileToString.readFile(args[1]+"/B ("+(i+1)+").cpp"));
//		}

//		GetAstSimilarity gas = new GetAstSimilarity();
//		gas.SimilarityCalculation(A, B);
//		// 获取序号和相似度
//	//	System.out.println(gas.getSimilarity()+" "+gas.getIndex());
//		Integer sim = (int)(gas.getSimilarity()*100);
//		Integer idx = gas.getIndex();
//        try {
//			FileWriter fw = new FileWriter(args[1]+"/Similarity.out");
//			fw.write(sim.toString()+" "+idx.toString()+"\n");
//			fw.close();
//		} catch (IOException e) {
//			System.out.println("file writer FAILED");
//		}
//        long time2 = System.currentTimeMillis();
      //  System.out.println("using "+(time2-time1)+"ms");
	}

}
