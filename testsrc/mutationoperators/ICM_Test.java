package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.icm.ICM;

import org.junit.Test;

import utils.MethodTest;

public class ICM_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new ICM();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return  "boolean b; " +
				"int i; " +
				"byte by; " +
				"short sh; " +
				"long l; " +
				"float f; " +
				"double d; ";
	}

	@Test
	public void testICM_boolean1() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.b = true; if(this.b) {System.out.println(\"END PROGRAM\");}";
		String post = "System.out.println(\"START PROGRAM\"); this.b = false; if(this.b) {System.out.println(\"END PROGRAM\");}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testICM_boolean2() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.b = false; if(this.b) {System.out.println(\"END PROGRAM\");}";
		String post = "System.out.println(\"START PROGRAM\"); this.b = true; if(this.b) {System.out.println(\"END PROGRAM\");}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_IntByteShort1() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.i = 1; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.i = 0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_IntByteShort2() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.by = -1; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.by = 1; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_IntByteShort3() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.sh = 5; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.sh = -1; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_IntByteShort4() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.i = 42; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.i = 43; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_IntByteShort5() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.i = -2; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.i = -1; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_IntByteShort6() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.i = 2; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.i = 3; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Long1() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.l = 1; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.l = 0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Long2() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.l = 0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.l = 1; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Long3() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.l = -1; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.l = 0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Long4() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.l = 3; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.l = 4; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Long5() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.l = (Long.MAX_VALUE - 1); System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.l = Long.MAX_VALUE; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop); 
	}
	
	@Test
	public void testICM_Long6() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.l = Long.MIN_VALUE; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.l = (Long.MIN_VALUE + 1); System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop); 
	}
	
	@Test
	public void testICM_Float1() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.f = 1.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.f = 0.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Float2() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.f = 2.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.f = 0.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Float3() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.f = 0.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.f = 1.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Float4() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.f = 42.4242; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.f = 1.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Float5() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.f = Float.MAX_VALUE; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.f = 1.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop)); 
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Float6() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.f = Float.MIN_VALUE; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.f = 1.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Double1() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.d = 1.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.d = 0.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Double2() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.d = 124.4214; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.d = 1.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Double3() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.d = -42.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.d = 1.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop); 
	}
	
	@Test
	public void testICM_Double4() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.d = Double.MAX_VALUE; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.d = 1.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_Double5() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); this.d = Double.MIN_VALUE; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); this.d = 1.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_NoMatching1() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); final double d2 = Double.MIN_VALUE; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		String post = "System.out.println(\"START PROGRAM\"); final double d2 = 1.0; System.out.println(\"VALUE: \" + this.i); System.out.println(\"END PROGRAM\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testICM_NoMatching2() { 
		String pre 	= "System.out.println(\"START PROGRAM\"); final boolean b2 = false; if(this.b) {System.out.println(\"END PROGRAM\");}";
		String post = "System.out.println(\"START PROGRAM\"); final boolean b2 = true; if(this.b) {System.out.println(\"END PROGRAM\");}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
