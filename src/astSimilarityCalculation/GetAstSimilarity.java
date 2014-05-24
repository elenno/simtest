package astSimilarityCalculation;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;

public class GetAstSimilarity {
    private double similarity;//相似度
    private int index;//最相似的那个序号
    private List<Double> simList;
    public void SimilarityCalculation(String codeA,List<String> codeB){
    	simList = new ArrayList<Double>();
    	
    	IASTTranslationUnit astA=getAST.getAst(codeA);
    	  	
    	ASTVISITOR astvisitor=new ASTVISITOR();
		ASTVISITOR.MyASTVisitor vis1=astvisitor.new MyASTVisitor();
		astA.accept(vis1);

		
		vis1.travel();
	//	vis1.print(0);
		
		vis1.removeStruct();
		vis1.moveFunction("main");
		vis1.removeNotCalledFunc();
		vis1.countTotalVertex(0);
	//	vis1.print(vis1.getSonList());
	//	vis1.printFuncMover();
	//	vis1.print();
	//	vis1.print(0);
		double max=0.0;
		int idx=0;
		for(int i=0;i<codeB.size();i++)
        {
        	IASTTranslationUnit ast2=getAST.getAst(codeB.get(i));
        	ASTVISITOR.MyASTVisitor vis2=astvisitor.new MyASTVisitor();
        	ast2.accept(vis2);
        	vis2.travel();
        	vis2.removeStruct();
    		vis2.moveFunction("main");
    		vis2.removeNotCalledFunc();
        //	vis2.print();
    		vis2.countTotalVertex(0);
        	double sim=astvisitor.max_similarity(vis1.getTree(),vis2.getTree(),vis1.getSonList(),vis2.getSonList(),vis1.getTotVertex(),vis2.getTotVertex());
       // 	System.out.println("第"+(i+1)+"份代码与待检代码的相似度为 "+sim);
        	simList.add(sim);
        	if(max<sim)
        	{
        		max=sim;
        		idx=i;
        	}
        }
	    this.similarity=max;
	    this.index=idx;
    }
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public List<Double> getSimList() {
		return simList;
	}
	public void setSimList(List<Double> simList) {
		this.simList = simList;
	}
	
}
