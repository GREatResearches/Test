package cfmts.ts.elements;

import java.util.ArrayList;

public class TestSequence {

	   ArrayList<TestCase> testCases;
	     
	     public TestSequence() {
	    	 testCases = new ArrayList<TestCase>();
	     }

		public ArrayList<TestCase> getTestCases() {
			return testCases;
		}

		public void setTestItems(ArrayList<TestCase> testCases) {
			this.testCases = testCases;
		}
		
		public void addTestItem(TestCase testCase) {
			testCases.add(testCase);
		}
		
		public void removeTestItem(TestCase testCase) {
			testCases.remove(testCase);
		}
		
}
