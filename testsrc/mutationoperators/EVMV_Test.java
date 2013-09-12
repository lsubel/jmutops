package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.emvm.EMVM;

import org.junit.Test;

import utils.ClassTest;

public class EVMV_Test extends ClassTest {

	MutationOperator mutop;
	
	@Override 
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new EMVM();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return  "private final boolean BOOL_CONST = true; " +
				"int i; " +
				"byte by; " +
				"short sh; " +
				"long l; " +
				"float f; " +
				"double d; ";
	}

	@Test
	public void testEVMV_boolean1() { 
		String pre 	= "private boolean b1 = true;";
		String post = "private boolean b1 = false;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_boolean2() { 
		String pre 	= "private boolean b1 = BOOL_CONST;";
		String post = "private boolean b1 = false;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_int1() { 
		String pre 	= "private int i1 = 12414;";
		String post = "private int i1 = 0;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_int2() { 
		String pre 	= "private final int i1 = -12414;";
		String post = "private final int i1 = 0;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_short1() { 
		String pre 	= "private short sh1 = 42;";
		String post = "private short sh1 = 0;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_short2() { 
		String pre 	= "private final short sh1 = -41;";
		String post = "private final short sh1 = 0;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_byte1() { 
		String pre 	= "private byte by1 = 12;";
		String post = "private byte by1 = 0;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testEVMV_byte2() { 
		String pre 	= "private final byte by1 = -1;";
		String post = "private final byte by1 = 0;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_long1() { 
		String pre 	= "private long l1 = 11241242;";
		String post = "private long l1 = 0;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_long2() { 
		String pre 	= "private final long l1 = -1234567890;";
		String post = "private final long l1 = 0;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_float1() { 
		String pre 	= "private float f1 = 12.3456789;";
		String post = "private float f1 = 0.0;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testEVMV_float2() { 
		String pre 	= "private final float f1 = -12.3456789;";
		String post = "private final float f1 = 0.0;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_double1() { 
		String pre 	= "private double d1 = 12.34567890123456789;";
		String post = "private double d1 = 0.0;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testEVMV_double2() { 
		String pre 	= "private final double d1 = -98765.43210;";
		String post = "private final double d1 = 0.0;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_char1() { 
		String pre 	= "private char c1 = 'l';";
		String post = "private char c1 = '\u0000';";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_char2() { 
		String pre 	= "private final char c1 = 's';";
		String post = "private final char c1 = '\u0000';";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_object1() { 
		String pre 	= "private String s1 = \"MESSAGE\";";
		String post = "private String s1 = null;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEVMV_object2() { 
		String pre 	= "private Object o1 = new Integer(42);";
		String post = "private Object o1 = null;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
