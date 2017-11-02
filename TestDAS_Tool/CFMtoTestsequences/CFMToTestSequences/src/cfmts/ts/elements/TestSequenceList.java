package cfmts.ts.elements;

import java.util.ArrayList;


public class TestSequenceList {

	 ArrayList<TestSequence> testSequencesList;
     
     public TestSequenceList() {
    	 testSequencesList = new ArrayList<TestSequence>();
     }

	public ArrayList<TestSequence> getTestSequences() {
		return testSequencesList;
	}

	public void setTestItems(ArrayList<TestSequence> testSequencesList) {
		this.testSequencesList = testSequencesList;
	}
	
	public void add(TestSequence testSequence) {
		this.testSequencesList.add(testSequence);
	}

}
