import java.io.File;
import java.util.List;

public class Test1 {

	static int a;
	int b;
	boolean d;
	String str = "100";
	String str2;
	Test1 test;
	String s3;
	
	Test1 (){
		str2 = "Bla";
	}
	
	Test1 (String args){
		str2 = args;
	}
	
	public int getX(){
		return b;
	}
	
	public void checkForSpace(int c){
		str = "Test";
		if((Test1.a + b) > c){
			if(d){
				this.getX();
				System.out.println(this.str);
				this.test.str.length();
			}
		}
	}
	
	public boolean meth(Test1 arg){
		return arg.b > 2;
	}
	
	public boolean meth(Test1 arg, Test1 arg2){
		return true;
	}
	
	public static void main(String[] args){
		Test1 t1;
		Object t2, t3;
		t1 = new Test1();
		t2 = t1;
		t3 = t1;
		
		Object o = (Object) t1;
		
		t1.getX();
		
		boolean b1 = (t2 == t3);
		boolean b2 = (t2.equals(t3));
	
	}

}
