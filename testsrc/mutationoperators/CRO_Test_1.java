package mutationoperators;

import static org.junit.Assert.assertEquals;
import mutationoperators.cro.CRO;

import org.junit.Test;

import utils.MethodTest;

public class CRO_Test_1 extends MethodTest {

	public CRO_Test_1() {
		super(new CRO());
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int value; " + 
				"Bar childLeft = null; " +
				"Bar childRight = null; " +
				"public Bar(){value = 0;} " +
				"public Bar(int v){value = v;} " +
				"public Bar(Bar left){childLeft = left;} " +
				"public Bar(Bar left, Bar right){childLeft = left; childRight = right;} " +
				"public Bar(int v, Bar left){value = v; childLeft = left} " +
				"public Bar(int v, Bar left, Bar right){value = v; childLeft = left, childRight = right;} " +
				"public int getValue(){return value} ";
	}

	@Test
	public void testCRO_LocalReplacement1() {
		int diff = compareMatches("Bar Temp = new Bar();", "Bar Temp = new Bar(1);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCRO_LocalReplacement2() {
		int diff = compareMatches("Bar Temp = new Bar();", "Bar Temp = new Bar(1);");
		assertEquals(1, diff);
	}

	@Test
	public void testCRO_SameArgumentNumber1() {
		int diff = compareMatches("childRight = new Bar(); Bar Temp = new Bar(1);", "childRight = new Bar(); Bar Temp = new Bar(childRight);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCRO_SameArgumentNumber2() {
		int diff = compareMatches("childRight = new Bar(); Bar Temp = new Bar(childRight, childRight);", "childRight = new Bar(); Bar Temp = new Bar(42, childRight);");
		assertEquals(1, diff);
	}

}
