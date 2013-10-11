package mutationoperators.methodlevel;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.io.File;

import jmutops.JMutOps;
import mutationoperators.MutationOperator;

import org.junit.Before;
import org.junit.Test;

import utils.ExtractorForTests;

public class MutationOperatorTest {

	JMutOps jmutops;
	
	ExtractorForTests extractor;
	
	File prefixFile;
	
	File postfixFile;
	
	@Before
	public void setUp() throws Exception {
		try {
			this.prefixFile = File.createTempFile("tmp", "java");  
			this.postfixFile = File.createTempFile("tmp2", "java");
			
			this.extractor = new ExtractorForTests();
			
			this.jmutops = new JMutOps();
			this.jmutops.addEventListener(extractor);
			this.jmutops.checkFiles(prefixFile, postfixFile);
		} catch (Exception e) {
			fail("Could not find file.");
		}
	}

	@Test
	public void testMutationOperatorProperties() {
		for(MutationOperator mutop: extractor.mutationOperatorList){
			
			assertNotNull(mutop.getCategory());
			
			assertNotNull(mutop.getLevel());
			
			assertNotNull(mutop.getDescription());
			assertNotSame("", mutop.getDescription());
			
			assertNotNull(mutop.getFullname());
			assertNotSame("", mutop.getFullname());
			
			assertNotNull(mutop.getShortname());
			assertNotSame("", mutop.getShortname());
		}
	}

}
