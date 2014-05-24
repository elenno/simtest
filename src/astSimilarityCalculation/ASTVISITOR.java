package astSimilarityCalculation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.IASTInitializer;
import org.eclipse.cdt.core.dom.ast.IASTInitializerExpression;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTProblem;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IASTTypeId;
import org.eclipse.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator;


public class ASTVISITOR {
	
	public class HASH
	{
		private int level;
		private int father;
		private int totson;
		private double V;//���ֵ
		private double W;//���Ȩֵ
		private int hash;
		private String type;
		private String code;
		private int deep;
		private double infoValue;
		HASH()
		{
			this.level=0;
			this.father=-1;
			this.type="";
			this.code="";
			this.totson=1;
			this.V=1;
			this.W=1;
			this.infoValue=1;
			this.deep=1;
		}
		public void setNode(int _level,int _father,String _type,String _code)
        {
        	level=_level;
        	father=_father;
        	type=_type;
        	code=_code;
        }
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public int getFather() {
			return father;
		}
		public void setFather(int father) {
			this.father = father;
		}
		public int getTotson() {
			return totson;
		}
		public void setTotson(int totson) {
			this.totson = totson;
		}
		
		public int getHash() {
			return hash;
		}
		public void setHash(int hash) {
			this.hash = hash;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public int getDeep() {
			return deep;
		}
		public void setDeep(int deep) {
			this.deep = deep;
		}
		public double getInfoValue() {
			return infoValue;
		}
		public void setInfoValue(double infoValue) {
			this.infoValue = infoValue;
		}
		public double getV() {
			return V;
		}
		public void setV(double v) {
			V = v;
		}
		public double getW() {
			return W;
		}
		public void setW(double w) {
			W = w;
		}
	}
	
	public class FunctionMover {
		private Integer numDecl;
		private Integer numStmt;
		private List<String> funcCallList;
		private boolean vis = false;
		public Integer getNumDecl() {
			return numDecl;
		}
		public void setNumDecl(Integer numDecl) {
			this.numDecl = numDecl;
		}
		public Integer getNumStmt() {
			return numStmt;
		}
		public void setNumStmt(Integer numStmt) {
			this.numStmt = numStmt;
		}
		public FunctionMover(Integer _decl, Integer _stmt)
		{
			funcCallList = new ArrayList<String>();
			numDecl = _decl;
			numStmt = _stmt;
			vis = false;
		}
		public List<String> getFuncCallList() {
			return funcCallList;
		}
		public void setFuncCallList(List<String> funcCallList) {
			this.funcCallList = funcCallList;
		}
		public boolean isVis() {
			return vis;
		}
		public void setVis(boolean vis) {
			this.vis = vis;
		}
	}
	
	public class MyASTVisitor extends ASTVisitor {
        private ArrayList<Integer>[] sonList; //���ӽ���б�
		private int level;//����
        private HASH[] tree;//����ɵ���
        private int top;//curջ��ջ��
        private Integer[] cur;//ջ
        private int cnt;//��¼���Ľ����
        private int mini;
        private Integer curDecl = -1;
        private String curFunc = "";
        private Integer curStmt = -1;
        private List<String> structList = new ArrayList<String>();
        private Map<String, Integer> funcPosMap = new HashMap<String, Integer>();        
        private Map<String, FunctionMover> functionMap = new HashMap<String, FunctionMover>();
		private int totVertex;
        MyASTVisitor() {
			this.shouldVisitNames = true;
			this.shouldVisitDeclarations = true;
			this.shouldVisitStatements = true;
			this.shouldVisitComments = true;
			this.shouldVisitDeclarators = true;
			this.shouldVisitDeclSpecifiers = true;
			this.shouldVisitEnumerators = true;
			this.shouldVisitExpressions = true;
			this.shouldVisitInitializers = true;
			this.shouldVisitParameterDeclarations = true;
			this.shouldVisitProblems = true;
			this.shouldVisitTranslationUnit = true;
			this.shouldVisitTypeIds = true;
			this.level = 0;
			this.top=0;
			this.cnt=1;
			this.mini=0;
			this.tree=new HASH[10000];
			this.cur=new Integer[10000];
			for(int i=0;i<tree.length;i++)
			{
				this.tree[i]=new HASH();
				this.cur[i]=0;
			}
			tree[0].code="root";
			tree[0].type="root";
			reset();
		}
		
		private void reset() {
			functionMap.clear();
			structList.clear();
			funcPosMap.clear();
			curDecl = -1;
			curFunc = "";
			curStmt = -1;
			totVertex = 0;
		}

		public void setThings(String _type,String _code)
		{
			
			level++;
			tree[cnt].setNode(level, cur[top],_type ,_code);
			cur[++top]=cnt++;
		//	System.out.println(level);
		}
		public void unsetThings()
		{
			level--;
			top--;
			if(top<mini)
				mini=top;
		//	System.out.println(level);
		}
		public void print()
		{
			for(int i=0;i<cnt;i++)
			{
				//output+=i+":"+tree[i].type+"\n"+tree[i].code+"\n";
				System.out.println(i+":"+tree[i].type+"\n"+tree[i].code+" father="+tree[i].father);
			}
		}
		public void print(int root)
		{
			System.out.println("idx: "+root+"  "+tree[root].type+" "+tree[root].code);
			for(int i=0;i<sonList[root].size();i++)
			{
				print(sonList[root].get(i));
			}
		}
		public void print(ArrayList[] sonList1)
		{
			for(int i=0;i<sonList1.length;i++)
			{
				System.out.printf("%d: %d:",i,sonList1[i].size());
				for(int j=0;j<sonList1[i].size();j++)
				{
					System.out.printf("%d ",sonList1[i].get(j));
				}
				System.out.println("");
			}
		}
		
		public void addFunctionMover(String func, Integer decl, Integer stmt)
		{
		//	if (""== func || -1 == decl || -1 == stmt)
		//	{
		//		return;
		//	}
			functionMap.put(func, new FunctionMover(decl, stmt));
		}

		
		public void travel()//����������������������飬���������
		{
		    sonList=new ArrayList[cnt];
			for(int i=0;i<cnt;i++)
			{
				tree[i].hash=tree[i].type.hashCode();
			
			//	System.out.println(tree[i].type);
				sonList[i]=new ArrayList();
				int x=tree[i].father;
				if(x!=-1)
				{
					sonList[x].add(i);
				}
			}
			
			for(int i=1;i<cnt;i++)
			{
				int x=tree[i].father;
				double sc=0;
				for(int j=0,p;j<sonList[x].size();j++){
					p=sonList[x].get(j);
					sc+=tree[p].infoValue;
				}
				tree[i].W=tree[i].infoValue/sc;
				tree[i].V=tree[i].W*(sonList[x].size()-1+tree[x].V-1);//V(n)=W(n)*[S(n)+V(father)-1]  S(n)��n���ֵ���
			}
			for(int k=cnt-1;k>0;k--)//����ÿ��������ܹ��ж��ٸ������
			{
				int x=tree[k].father;
				tree[x].totson+=tree[k].totson;
				tree[x].deep=tree[x].deep<tree[k].deep+1?tree[k].deep+1:tree[x].deep;
			//	System.out.println(tree[k].totson);
			}
		}
		
		
		
		public int visit(IASTName name) {
			String prtName = name.toString();
			
			if (prtName.length() == 0)
				prtName = name.getRawSignature(); // use pre pre-processor
			if ("IASTCompositeTypeSpecifier.TYPE_NAME - IASTName for IASTCompositeTypeSpecifier".equals(name.getPropertyInParent().getName()))
			{
				structList.add(prtName);
			}
			// value
		//	System.out.println("name: "+name.getPropertyInParent().getName());
			setThings(name.getPropertyInParent().getName(),"Visiting name: " + prtName);
		//	System.out.println("Visiting name: " + prtName);
		//	System.out.println(name.getPropertyInParent().getName());
			return PROCESS_CONTINUE;
		}

		
		public int visit(IASTStatement stmt) { // lots more
			
		    if (stmt instanceof IASTExpressionStatement)
		    {
		    	if (stmt.getRawSignature().length() > 100)
		    	{
		    	//	System.out.println("Visiting expr: "+ stmt.getPropertyInParent() + stmt.getRawSignature());
		    		return PROCESS_SKIP;
		    	}		    	
		    }
		    if ("IASTFunctionDefinition.FUNCTION_BODY - Function Body for IASTFunctionDefinition".equals(stmt.getPropertyInParent().getName()))
		    {
		    	curStmt = cnt;
		    	addFunctionMover(curFunc,curDecl,curStmt);
		    //	curFunc = "";
		    //	curDecl = -1;
		    //	curStmt = -1;
		    }
		  //  System.out.println(stmt.getPropertyInParent().getName()+" "+"Visiting stmt: " + stmt.getRawSignature());
		    setThings(stmt.getPropertyInParent().getName(),
					"Visiting stmt: " + stmt.getRawSignature());
			return PROCESS_CONTINUE;
		}

		public int visit(IASTDeclaration decl) {
			
			if ("IASTTranslationUnit.OWNED_DECLARATION - IASTDeclaration for IASTTranslationUnit".equals(decl.getPropertyInParent().getName()))
		    { 
				curDecl = cnt; //设置当前函数块的序号		
		    }
			
		//	System.out.println("Visiting decl: " + decl.getRawSignature());
        //    System.out.println(decl.getPropertyInParent().getName());
			setThings(decl.getPropertyInParent().getName(),"Visiting decl: " + decl.getRawSignature());
			return PROCESS_CONTINUE;
		}

		public int visit(IASTComment comm) {
			setThings(comm.getPropertyInParent().getName(),"Visiting comm: " + comm.getRawSignature());
			//System.out.println("Visiting comm: " + comm.getRawSignature());
			
			return PROCESS_CONTINUE;
		}

		public int visit(IASTDeclarator dtor) {	
		//	System.out.println("Visiting dtor: " + dtor.getRawSignature());
		//	System.out.println(dtor.getPropertyInParent().getName()+" "+dtor.getName());
            if ("IASTFunctionDefinition.DECLARATOR - IASTFunctionDeclarator for IASTFunctionDefinition".equals(dtor.getPropertyInParent().getName()))
            {
            	curFunc = dtor.getName().toString();
            //	System.out.println(curFunc);
            //	System.out.println(dtor.getName());
            }
            setThings(dtor.getPropertyInParent().getName(),"Visiting dtor: " + dtor.getRawSignature());
			return PROCESS_CONTINUE;
		}

		public int visit(IASTDeclSpecifier dspe) {
			setThings(dspe.getPropertyInParent().getName(),"Visiting dspe: " + dspe.getRawSignature());
			//System.out.println("Visiting dspe: " + dspe.getRawSignature());
			//System.out.println(dspe.getPropertyInParent().getName());
			
			return PROCESS_CONTINUE;
		}

		public int visit(IASTEnumerator enmt) {
			setThings(enmt.getPropertyInParent().getName(),"Visiting enmt: " + enmt.getRawSignature());
			//System.out.println("Visiting enmt: " + enmt.getRawSignature());
			
			return PROCESS_CONTINUE;
		}

		public int visit(IASTExpression expr) {
			if("IASTFunctionCallExpression.FUNCTION_Name - IASTExpression (name) for IASTFunctionCallExpression".equals(expr.getPropertyInParent().getName()))
			{
				funcPosMap.put(expr.getRawSignature(), cnt);
				//System.out.println("expr:"+expr.getRawSignature());
			    functionMap.get(curFunc).getFuncCallList().add(expr.getRawSignature());	
			}
			
			setThings(expr.getPropertyInParent().getName(),"Visiting expr: " + expr.getRawSignature());
			//System.out.println("Visiting expr: " + expr.getPropertyInParent().getName());
			/*if(expr instanceof IASTBinaryExpression)
			{
				System.out.println("!!!!!!11");
				System.out.println("Visiting expr: "+ expr.getPropertyInParent() + expr.getRawSignature());
			}*/
			
			//System.out.println(expr.getPropertyInParent().getName());
			return PROCESS_CONTINUE;
		}

		public int visit(IASTInitializer init) {
			
			if(init instanceof IASTInitializerExpression)
			{
			//	System.out.println("!!!!!!11");
			//	System.out.println("Visiting init: "+ init.getPropertyInParent() + init.getRawSignature());
				if(init.getRawSignature().length() > 30)
				{
					return PROCESS_SKIP;
				}
			}
			//System.out.println("Visiting init: " + init.getRawSignature());
			//System.out.println(init.getPropertyInParent().getName());
			setThings(init.getPropertyInParent().getName(),"Visiting init: " + init.getRawSignature());
			return PROCESS_CONTINUE;
		}

		public int visit(IASTParameterDeclaration para) {
			setThings(para.getPropertyInParent().getName(),"Visiting para: " + para.getRawSignature());
			//System.out.println("Visiting para: " + para.getRawSignature());
			
			//System.out.println(para.getPropertyInParent().getName());
			return PROCESS_CONTINUE;
		}

		public int visit(IASTProblem prob) {
			setThings(prob.getPropertyInParent().getName(),"Visiting prob: " + prob.getRawSignature());
			//System.out.println("Visiting prob: " + prob.getRawSignature());
			
			return PROCESS_CONTINUE;
		}

		public int visit(IASTTranslationUnit tran) {
		//	setThings(tran.getPropertyInParent().getName(),"Visiting tran: " + tran.getRawSignature());
			//System.out.println("Visiting tran: " + tran.getRawSignature());
			return PROCESS_CONTINUE;
		}

		public int visit(IASTTypeId type) {
			setThings(type.getPropertyInParent().getName(),"Visiting type: " + type.getRawSignature());
		//	System.out.println("Visiting type: " + type.getRawSignature());
			
			return PROCESS_CONTINUE;
		}

		public int leave(IASTName name) {
			unsetThings();
		//	System.out.println("Leaving name " + name.getRawSignature());
			
			return PROCESS_CONTINUE;
		}

		public int leave(IASTStatement stmt) {
			unsetThings();
			
			//System.out.println(" Leaving stmt: " + stmt.getRawSignature());
			return PROCESS_CONTINUE;
		}

		public int leave(IASTDeclaration decl) {
			unsetThings();
			//System.out.println("Leaving decl: " + decl.getRawSignature());
			
			return PROCESS_CONTINUE;
		}

		public int leave(IASTComment comm) {
			unsetThings();
			
			//System.out.println("Leaving comm: " + comm.getRawSignature());
			return PROCESS_CONTINUE;
		}

		public int leave(IASTDeclarator dtor) {
			unsetThings();
		//	System.out.println("Leaving dtor: " + dtor.getRawSignature());

			return PROCESS_CONTINUE;
		}

		public int leave(IASTDeclSpecifier dspe) {
			unsetThings();
		//	System.out.println("Leaving dspe: " + dspe.getRawSignature());
			
			return PROCESS_CONTINUE;
		}

		public int leave(IASTEnumerator enmt) {
			unsetThings();
			
		//	System.out.println("Leaving enmt: " + enmt.getRawSignature());
			return PROCESS_CONTINUE;
		}

		public int leave(IASTExpression expr) {
			unsetThings();
			
		//	System.out.println("Leaving expr: " + expr.getRawSignature());
			
			return PROCESS_CONTINUE;
		}

		public int leave(IASTInitializer init) {
			unsetThings();
			
		//	System.out.println("Leaving init: " + init.getRawSignature());
			return PROCESS_CONTINUE;
		}

		public int leave(IASTParameterDeclaration para) {
			unsetThings();
			
		//	System.out.println("Leaving para: " + para.getRawSignature());
			return PROCESS_CONTINUE;
		}

		public int leave(IASTProblem prob) {
			unsetThings();
			
		//	System.out.println("Leaving prob: " + prob.getRawSignature());
			return PROCESS_CONTINUE;
		}

		public int leave(IASTTranslationUnit tran) {
		//	unsetThings();
			
		//	System.out.println("Leaving tran: " + tran.getRawSignature());
			return PROCESS_CONTINUE;
		}

		public int leave(IASTTypeId type) {
			unsetThings();
			
		//	System.out.println("Leaving type: " + type.getRawSignature());
			return PROCESS_CONTINUE;
		}

		public ArrayList<Integer>[] getSonList() {
			return sonList;
		}

		public void setSonList(ArrayList<Integer>[] sonList) {
			this.sonList = sonList;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public HASH[] getTree() {
			return tree;
		}

		public void setTree(HASH[] tree) {
			this.tree = tree;
		}

		public int getTop() {
			return top;
		}

		public void setTop(int top) {
			this.top = top;
		}

		public Integer[] getCur() {
			return cur;
		}

		public void setCur(Integer[] cur) {
			this.cur = cur;
		}

		public int getCnt() {
			return cnt;
		}

		public void setCnt(int cnt) {
			this.cnt = cnt;
		}

		public int getMini() {
			return mini;
		}

		public void setMini(int mini) {
			this.mini = mini;
		}

		public void printFuncMover() {
			for(Iterator<String> it=functionMap.keySet().iterator();it.hasNext();)
			{
				String key = it.next().toString();
				FunctionMover fm = functionMap.get(key);
				System.out.println(key+" "+fm.getNumDecl()+" "+fm.getNumStmt());
			    System.out.println(getTree()[fm.getNumDecl()].type+" "+getTree()[fm.getNumDecl()].code);
			    System.out.println(getTree()[fm.getNumStmt()].type+" "+getTree()[fm.getNumStmt()].code);
			}
		}

		public void removeStruct() {
			for(int i = 0; i < structList.size(); i++)
			{
				functionMap.remove(structList.get(i));
			}
		}

		public void moveFunction(String func) {
			//travel the tree  
			//IASTFunctionCallExpression.FUNCTION_Name - IASTExpression (name) for IASTFunctionCallExpression
		 //   System.out.println("move: "+func );
			FunctionMover fm = functionMap.get(func);
		    List<String> list = fm.getFuncCallList();
		    for(int i = 0; i < fm.getFuncCallList().size(); i++)
		    {
		    	String tmp = list.get(i);
		    //	System.out.println("call: "+tmp);
		    	FunctionMover f = functionMap.get(tmp);
		    	if(null == f)
		    	{
		    	    continue;
		    	}
		    	if(f.vis)
		    	{
		    		continue;
		    	}
		    	moveFunction(tmp);
		    	f.vis = true;
		    	functionMap.remove(tmp);
		    	functionMap.put(tmp, f);
		    }
		    if(!"main".equals(func))
		    {
		    	//todo move the sub tree
		    	//tree[fm.numStmt]
		        Integer curPos = funcPosMap.get(func);
		        if(curPos==null)
		        {
		        	System.out.println("null:"+func);
		        	return;
		        }
		        while(curPos!=-1)
		        {
		        	if("IASTCompoundStatement.NESTED_STATEMENT - nested IASTStatement for IASTCompoundStatement".equals(tree[curPos].type))
		        	{
		        		break;
		        	}
		        	curPos = tree[curPos].father;
		        }
		     //   System.out.println("curPos: "+curPos);
		     //   System.out.println("beforeMOve");
		     //   for(int j=0;j<sonList[0].size();j++)
		     //   {
		     //   	System.out.printf("%d ",sonList[0].get(j));
		     //   }
		     //   System.out.println("");
		        Integer curFather = tree[curPos].father;
		        Integer oldFather = tree[fm.numDecl].father;
		     //   System.out.println("curpos: "+curPos+"  cur: "+curFather+"  old: "+oldFather);
		     //   System.out.println("stmt: "+fm.numStmt+"  "+"decl: "+fm.numDecl);
		        sonList[curFather].remove(curPos);
		        //sonList[curFather].add(fm.numStmt);
		        for(int j=0;j<sonList[fm.numStmt].size();j++)
		        {
		        	sonList[curFather].add(sonList[fm.numStmt].get(j));
		        }
		        sonList[oldFather].remove(fm.numDecl);
		     //   System.out.println("afterMOve");
		     //   for(int j=0;j<sonList[0].size();j++)
		     //   {
		     //   	System.out.printf("%d ",sonList[0].get(j));
		     //   }
		     //   System.out.println("");
		       /* System.out.println("after move:");
		        for(int j=0;j<sonList[curFather].size();j++)
		        {
		        	System.out.printf("%d ",sonList[curFather].get(j));
		        }
		        System.out.println("");*/
		    }
		}

		public void removeNotCalledFunc() {
			//todo remove not called function
			for(Iterator<String> it=functionMap.keySet().iterator();it.hasNext();)
			{
				String key = it.next().toString();
				if("main".equals(key))
				{
					continue;
				}
				FunctionMover fm = functionMap.get(key);
			//	System.out.println("removeFunc:"+key);
			    if(!fm.vis)
			    {
			    	Integer father = tree[fm.numDecl].father;
			    	sonList[father].remove(fm.numDecl);
			    }
			}
		}
		public void countTotalVertex(int root)
		{
			totVertex++;
			//System.out.println(tree[root].type);
			//System.out.println(tree[root].code);
			for(int i=0;i<sonList[root].size();i++)
			{
				countTotalVertex(sonList[root].get(i));
			}
		}

		public int getTotVertex() {
			return totVertex;
		}

		public void setTotVertex(int totVertex) {
			this.totVertex = totVertex;
		}
	}

	private HASH[] t1,t2;
	private ArrayList<Integer>[] s1,s2;
    
	public double max_similarity(HASH[] tree1, HASH[] tree2,
			ArrayList[] sonList1, ArrayList[] sonList2, int tot1, int tot2) {
		t1=tree1.clone();
		t2=tree2.clone();
		s1=sonList1.clone();
		s2=sonList2.clone();
		double v1=0,v2=0;
		for(int i=0;i<tree1.length;i++){
			v1+=tree1[i].V;
		}
		for(int i=0;i<tree2.length;i++){
			v2+=tree2[i].V;
		}
		/*for(int i=0;i<sonList1.length;i++)
		{
			System.out.printf("%d: ",i);
			for(int j=0;j<sonList1[i].size();j++)
			{
				System.out.printf("%d ",sonList1[i].get(j));
			}
			System.out.println("");
		}*/
	
	//	System.out.println("v1=:"+v1+" v2=:"+v2);
	//	System.out.println("ans=:"+WeightTreeMatching(0,0)/AVG(tree1[0].V,tree2[0].V));
    //    return WeightTreeMatching(0,0)/AVG(v1,v2);
	//	System.out.println("vertex: "+tot1+" "+tot2);
	//	System.out.println("vertex2: "+t1[0].totson+" "+t2[0].totson);
		return SimpleTreeMatching(0,0)/AVG(tot1,tot2);  // 2*���ƥ������/(T1�����+T2�����)
	}

	public double WeightTreeMatching(int p1,int p2){
		if(t1[p1].hash!=t2[p2].hash){
			return 0;
		}
		int m=s1[p1].size();
		int n=s2[p2].size();
		int i,j;
		double[][] M=new double[m+1][n+1];
		double W;
		for(i=0;i<=m;i++)
		{
			M[i][0]=0;
		}
		for(j=0;j<=n;j++)
		{
			M[0][j]=0;
		}
		for(i=1;i<=m;i++)
		{
			for(j=1;j<=n;j++)
			{
				int p3=s1[p1].get(i-1);
				int p4=s2[p2].get(j-1);
				W=WeightTreeMatching(p3,p4);
				M[i][j]=max(M[i][j-1],M[i-1][j],M[i-1][j-1]+W);
			}
		}
		return M[m][n]+AVG(t1[m].V,t2[n].V);
	}

	public double SimpleTreeMatching(int p1, int p2) {    
		if(t1[p1].hash!=t2[p2].hash)//���������ĸ����ǲ�ͬ����ͬ
			return 0;
	
		int m=s1[p1].size();//����tree1[p1]���ӽ����
		int n=s2[p2].size();//����tree2[p2]���ӽ����
		int i,j;
		double[][] M=new double[m+1][n+1];
		double W;
		
		for(i=0;i<=m;i++)
		{
			M[i][0]=0;
		}
		for(j=0;j<=n;j++)
		{
			M[0][j]=0;
		}
		
		for(i=1;i<=m;i++)
		{
			for(j=1;j<=n;j++)
			{
				int p3=s1[p1].get(i-1);
				int p4=s2[p2].get(j-1);
				W=SimpleTreeMatching(p3,p4);
				M[i][j]=max(M[i][j-1],M[i-1][j],M[i-1][j-1]+W);
			}
		}
		return M[m][n]+1;	
	}
	
	
	
	
    public double AVG(double a,double b)
    {
    	return (a+b)/2.0;
    }
	public double max(double x, double y, double z) {
		double temp;
		if (x < y) {  
            temp = y;  
        } else  
            temp = x;  
        if (z > temp)  
            temp = z;  
        return temp;
	}

	public HASH[] getT1() {
		return t1;
	}

	public void setT1(HASH[] t1) {
		this.t1 = t1;
	}

	public HASH[] getT2() {
		return t2;
	}

	public void setT2(HASH[] t2) {
		this.t2 = t2;
	}

	public ArrayList<Integer>[] getS1() {
		return s1;
	}

	public void setS1(ArrayList<Integer>[] s1) {
		this.s1 = s1;
	}

	public ArrayList<Integer>[] getS2() {
		return s2;
	}

	public void setS2(ArrayList<Integer>[] s2) {
		this.s2 = s2;
	}	
}
