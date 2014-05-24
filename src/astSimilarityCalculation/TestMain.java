package astSimilarityCalculation;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)  
public class TestMain {

	private String len;
	private String loc;
	
	
	@Parameters
	public static Collection getParam(){
		//String[] args={"6","C:\\Users\\badboy\\Desktop\\毕业设计相关\\asttest\\ceshi"};
		//Object[][] obj={{"1","C:\\Users\\badboy\\Desktop\\7-Zip"}};
		Object[][] obj={{"14","C:\\Users\\badboy\\Desktop\\毕业论文测试"}};
		return Arrays.asList(obj);
	}
	public TestMain(String len,String loc){
		this.len=len;
		this.loc=loc;
	}
	
	@Test
	public void runTest(){
		System.out.println(len+" "+loc);
		Main.main(new String[]{len,loc});
	}
	public String getLen() {
		return len;
	}
	public void setLen(String len) {
		this.len = len;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	
}
