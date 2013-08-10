package mutationoperators;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.io.File;

import jmutops.JMutOps;

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
			this.jmutops.addResultListener(extractor);
			this.jmutops.checkFiles(prefixFile, postfixFile);
		} catch (Exception e) {
			fail("Could not find file.");
		}
	}

	@Test
	public void testMutationOperatorProperties() {
		for(MutationOperator mutop: extractor.mutationOperatorList){
			MutationOperatorProperty prop = mutop.mutopproperty;
			
			assertNotNull(prop.getCategory());
			
			assertNotNull(prop.getLevel());
			
			assertNotNull(prop.getDescription());
			assertNotSame("", prop.getDescription());
			
			assertNotNull(prop.getFullname());
			assertNotSame("", prop.getFullname());
			
			assertNotNull(prop.getShortname());
			assertNotSame("", prop.getShortname());
		}
	}

}
