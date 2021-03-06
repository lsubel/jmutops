package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.ConstructorTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.ipc.IPC;

import org.junit.Test;


public class IPC_Test extends ConstructorTest {

	MutationOperator mutop;
	
	
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new IPC();
		this.addMutationOperatorToTest(mutop);
	}
	
	@Override
	protected String getExtends() {
		return "RootClass";
	}

	@Override
	protected String getImplements() {
		return "";
	}
	
	@Override
	protected String getOtherClassContent() {
		return  "public Foo(){this.size = 1;} " +
				"public Foo(int s, Object o){this.size = s; this.value = o;} ";
	}

	@Override
	protected void initializeContextFiles() {
		String fileContentRootClass = 
				"int size;" +
				"Object value; " +
				"public RootClass(){this.size = 0;} " +
				"public RootClass(int i){this.size = i;} " +
				"public RootClass(int i, Object o){this.size = i; this.value = o;} ";
		addContextSourceFile("RootClass", surroundWithClass("RootClass", fileContentRootClass));
	}
	
	@Test
	public void testIPC_Method1(){
		String args = "int i";
		String pre 	= "super(i); System.out.println(this.size);";
		String post	= "System.out.println(this.size);";
		HashMap<String, Integer> resultMap = compareMatches(args, pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testIPC_Method2(){
		String args = "Object o";
		String pre 	= "super(1); System.out.println(this.size);";
		String post	= "System.out.println(this.size);";
		HashMap<String, Integer> resultMap = compareMatches(args, pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testIPC_Method3(){
		String args = "int i";
		String pre 	= "super(i); System.out.println(this.size);";
		String post	= "System.out.println(this.size);";
		HashMap<String, Integer> resultMap = compareMatches(args, pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testIPC_NoMatch1(){
		String args = "int i";
		String pre 	= "super(); System.out.println(this.size);";
		String post	= "System.out.println(this.size);";
		HashMap<String, Integer> resultMap = compareMatches(args, pre, post);
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testIPC_NoMatch2(){
		String args = "int i";
		String pre 	= "super(); System.out.println(this.size);";
		String post	= "super(i); System.out.println(this.size);";
		HashMap<String, Integer> resultMap = compareMatches(args, pre, post);
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
