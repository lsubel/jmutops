package mutationoperators;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

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
		this.prefixFile = new File("C:" + File.separator + "Users" + File.separator + "sheak" + File.separator + "Desktop" + File.separator + "Bachelorarbeit" + File.separator + "Repository" + File.separator + "Code" + File.separator + "jMutOps" + File.separator + "testsrc" + File.separator + "resource" + File.separator + "prefix"  + File.separator + "Foo.java");
		this.postfixFile = new File("C:" + File.separator + "Users" + File.separator + "sheak" + File.separator + "Desktop" + File.separator + "Bachelorarbeit" + File.separator + "Repository" + File.separator + "Code" + File.separator + "jMutOps" + File.separator + "testsrc" + File.separator + "resource" + File.separator + "postfix"  + File.separator + "Foo.java");
		
		this.extractor = new ExtractorForTests();
		
		this.jmutops = new JMutOps();
		this.jmutops.addResultListener(extractor);
		this.jmutops.checkFiles(prefixFile, postfixFile);
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
