package mutationoperators;

import static org.junit.Assert.assertEquals;
import mutationoperators.cro.CRO;

import org.junit.Test;

import utils.MethodTest;

public class CRO_Test_SameClass extends MethodTest {

	public CRO_Test_SameClass() {
		super(new CRO());
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int value; " + 
				"Foo childLeft = null; " +
				"Foo childRight = null; " +
				"public Foo(){value = 0;} " +
				"public Foo(int v){value = v;} " +
				"public Foo(Foo left){childLeft = left;} " +
				"public Foo(Foo left, Foo right){childLeft = left; childRight = right;} " +
				"public Foo(int v, Foo left){value = v; childLeft = left;} " +
				"public Foo(int v, Foo left, Foo right){value = v; childLeft = left; childRight = right;} " +
				"public int getValue(){return value;} ";
	}

	@Test
	public void testCRO_LocalReplacement1() {
		int diff = compareMatches("Foo Temp = new Foo();", "Foo Temp = new Foo(1);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCRO_LocalReplacement2() {
		int diff = compareMatches("Foo Temp = new Foo();", "Foo Temp = new Foo(1, null);");
		assertEquals(1, diff);
	}

	@Test
	public void testCRO_SameArgumentNumber1() {
		int diff = compareMatches("childRight = new Foo(); Foo Temp = new Foo(1);", "childRight = new Foo(); Foo Temp = new Foo(childRight);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCRO_SameArgumentNumber2() {
		int diff = compareMatches("childRight = new Foo(); Foo Temp = new Foo(childRight, childRight);", "childRight = new Foo(); Foo Temp = new Foo(42, childRight);");
		assertEquals(1, diff);
	}

}
