package astSimilarityCalculation;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.dom.ICodeReaderFactory;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTInitializer;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTProblem;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IASTTypeId;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.parser.CodeReader;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTExpressionStatement;
import org.eclipse.cdt.internal.core.parser.scanner2.FileCodeReaderFactory;
import org.eclipse.core.runtime.CoreException;

/**
 * 
 * @author badboy
 * @description 获取代码的抽象语法树
 * 
 */
public class getAST {
	public static IASTTranslationUnit getAst(String code) {
		IParserLogService log = new DefaultLogService();
		CodeReader reader = new CodeReader(code.toCharArray());
		Map definedSymbols = new HashMap();
		String[] includePaths = new String[0];
		IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
		ICodeReaderFactory readerFactory = FileCodeReaderFactory.getInstance();
		IASTTranslationUnit ast = null;
		try {
			ast = GPPLanguage.getDefault().getASTTranslationUnit(
					reader, info, readerFactory, null, log);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			System.out.println("getAST fail");
			e.printStackTrace();
		}
		return ast;
	}
	
	public class MyASTVisitor extends ASTVisitor {
		MyASTVisitor(){this.shouldVisitNames = true;}
		public int visit(IASTName node){return PROCESS_CONTINUE;}
		public int leave(IASTName node){return PROCESS_CONTINUE;}
	}

	public static ASTVisitor getVisitor() {
		ASTVisitor visitor = new ASTVisitor() {

			public int visit(IASTName name) {
				String prtName = name.toString();
				if (prtName.length() == 0)
					prtName = name.getRawSignature(); // use pre pre-processor
				// value
				System.out.println("Visiting name: " + prtName);

				// System.out.println("visiting the name property: "+name.getPropertyInParent().getName());

				// System.out.println("name's completionContext : "+
				// name.getCompletionContext().toString());

				return PROCESS_CONTINUE;
			}

			/*
			 * @Override public int visit(IASTName name) { // Looking only for
			 * references, not declarations if (name.isReference()) { IBinding b
			 * = name.resolveBinding(); IType type; try { type = (b instanceof
			 * IFunction) ? ((IFunction) b) .getType() : null; if (type != null)
			 * System.out.print("Referencing " + name + ", type " +
			 * ASTTypeUtil.getType(type)); } catch (DOMException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 * 
			 * } return ASTVisitor.PROCESS_CONTINUE; }
			 */
			public int visit(IASTStatement stmt) { // lots more
				String sig = stmt.getRawSignature();
				if (sig.length() > 0) {
					System.out.println("Visiting stmt: "
							+ stmt.getRawSignature());
					// System.out.println("visiting the stmt property: "+stmt.getPropertyInParent());
				} else if (stmt instanceof IASTCompoundStatement) {
					IASTCompoundStatement cstmt = (IASTCompoundStatement) stmt;
					IASTStatement[] stmts = cstmt.getStatements();
					System.out.println("Visiting compound stmt with stmts: "
							+ stmts.length);
					for (IASTStatement st : stmts) {
						String rawSig = st.getRawSignature();

						if (rawSig.length() == 0) {
							System.out.println("   ->" + st);
							if (st instanceof CASTExpressionStatement) {
								CASTExpressionStatement es = (CASTExpressionStatement) st;
								IASTExpression exp = es.getExpression();
								if (exp instanceof IASTBinaryExpression) {
									IASTBinaryExpression bexp = (IASTBinaryExpression) exp;

									System.out.println("    binary exp: "
											+ bexp.getOperand1() + " "
											+ bexp.getOperator() + " "
											+ bexp.getOperand2());
								}
								String expStr = exp.getRawSignature();
								IType type = exp.getExpressionType();
							}
						} else {
							System.out.println("   ->" + rawSig);
						}
					}
				}
				return PROCESS_CONTINUE;
			}

			public int visit(IASTDeclaration decl) {

				System.out.println("Visiting decl: " + decl.getRawSignature());
				return PROCESS_CONTINUE;
			}

			public int visit(IASTComment comm) {
				System.out.println("Visiting comm: " + comm.getRawSignature());
				return PROCESS_CONTINUE;
			}

			public int visit(IASTDeclarator dtor) {
				System.out.println("Visiting dtor: " + dtor.getRawSignature());
				return PROCESS_CONTINUE;
			}

			public int visit(IASTDeclSpecifier dspe) {
				System.out.println("Visiting dspe: " + dspe.getRawSignature());
				return PROCESS_CONTINUE;
			}

			public int visit(IASTEnumerator enmt) {
				System.out.println("Visiting enmt: " + enmt.getRawSignature());
				return PROCESS_CONTINUE;
			}

			public int visit(IASTExpression expr) {
				System.out.println(expr.getExpressionType().toString());
				System.out.println("Visiting expr: " + expr.getRawSignature());
				return PROCESS_CONTINUE;
			}

			public int visit(IASTInitializer init) {
				System.out.println("Visiting init: " + init.getRawSignature());
				return PROCESS_CONTINUE;
			}

			public int visit(IASTParameterDeclaration para) {
				System.out.println("Visiting para: " + para.getRawSignature());
				return PROCESS_CONTINUE;
			}

			public int visit(IASTProblem prob) {
				System.out.println("Visiting prob: " + prob.getRawSignature());
				return PROCESS_CONTINUE;
			}

			public int visit(IASTTranslationUnit tran) {
				System.out.println("Visiting tran: " + tran.getRawSignature());
				return PROCESS_CONTINUE;
			}

			public int visit(IASTTypeId type) {
				System.out.println("Visiting type: " + type.getRawSignature());
				return PROCESS_CONTINUE;
			}

			/*
			 * public int leave(IASTName name) {
			 * System.out.println("Leaving name " + name.getRawSignature());
			 * return ASTVisitor.PROCESS_CONTINUE; }
			 * 
			 * public int leave(IASTStatement stmt) {
			 * System.out.println(" Leaving stmt: " + stmt.getRawSignature());
			 * return PROCESS_CONTINUE; }
			 * 
			 * 
			 * public int leave(IASTDeclaration decl) {
			 * System.out.println("Leaving decl: " + decl.getRawSignature());
			 * return PROCESS_CONTINUE; }
			 * 
			 * public int leave(IASTComment comm) {
			 * System.out.println("Leaving comm: " + comm.getRawSignature());
			 * return PROCESS_CONTINUE; }
			 * 
			 * public int leave(IASTDeclarator dtor) {
			 * System.out.println("Leaving dtor: " + dtor.getRawSignature());
			 * return PROCESS_CONTINUE; }
			 * 
			 * public int leave(IASTDeclSpecifier dspe) {
			 * System.out.println("Leaving dspe: " + dspe.getRawSignature());
			 * return PROCESS_CONTINUE; }
			 * 
			 * public int leave(IASTEnumerator enmt) {
			 * System.out.println("Leaving enmt: " + enmt.getRawSignature());
			 * return PROCESS_CONTINUE; }
			 * 
			 * public int leave(IASTExpression expr) {
			 * System.out.println("Leaving expr: " + expr.getRawSignature());
			 * return PROCESS_CONTINUE; }
			 * 
			 * public int leave(IASTInitializer init) {
			 * System.out.println("Leaving init: " + init.getRawSignature());
			 * return PROCESS_CONTINUE; }
			 * 
			 * public int leave(IASTParameterDeclaration para) {
			 * System.out.println("Leaving para: " + para.getRawSignature());
			 * return PROCESS_CONTINUE; }
			 * 
			 * public int leave(IASTProblem prob) {
			 * System.out.println("Leaving prob: " + prob.getRawSignature());
			 * return PROCESS_CONTINUE; }
			 * 
			 * public int leave(IASTTranslationUnit tran) {
			 * System.out.println("Leaving tran: " + tran.getRawSignature());
			 * return PROCESS_CONTINUE; }
			 * 
			 * public int leave(IASTTypeId type) {
			 * System.out.println("Leaving type: " + type.getRawSignature());
			 * return PROCESS_CONTINUE; }
			 */
		};
		visitor.shouldVisitNames = true;
		visitor.shouldVisitDeclarations = true;
		visitor.shouldVisitStatements = true;
		visitor.shouldVisitComments = true;
		visitor.shouldVisitDeclarators = true;
		visitor.shouldVisitDeclSpecifiers = true;
		visitor.shouldVisitEnumerators = true;
		visitor.shouldVisitExpressions = true;
		visitor.shouldVisitInitializers = true;
		visitor.shouldVisitParameterDeclarations = true;
		visitor.shouldVisitProblems = true;
		visitor.shouldVisitTranslationUnit = true;
		visitor.shouldVisitTypeIds = true;
		return visitor;
	}
}
