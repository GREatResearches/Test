
package br.ufc.great;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class TestSequences {
    private int currentPosTestCase;
    private int currentPosTestSequence;
    private TestCase currentTestCase;
    private ArrayList<TestCase> currentTestSequence;
	private ArrayList<ArrayList<TestCase>> testSequences;
	private boolean isCurrentSeqFinished;
	private boolean isFinished;
	private ReentrantLock lock;

	// TODO - Trocar para private.
	public TestSequences() {
    }
	
	public void initialize() {
		if (testSequences != null) {
			currentPosTestCase = 0;
			currentPosTestSequence = 0;
			isFinished = false;
			isCurrentSeqFinished = false;
	    	currentTestSequence = testSequences.get(0);
	    	currentTestCase = currentTestSequence.get(0);
	    	lock = new ReentrantLock();
		}
	}
	
	public void aquireLock() {
		lock.lock();
	}
	
	public void releaseLock() {
		lock.unlock();
	}
	
     public TestCase getCurrentTestCase() {
    	 return currentTestCase;
     }
     
     public ArrayList<TestCase> getCurrentTestSequence() {
    	 return currentTestSequence;
     }
	
	public void moveNextTestCase() {
		if ((currentPosTestCase + 1) == currentTestSequence.size()) {
			isCurrentSeqFinished = true;
		} else {
			TestCase previousTestCase = currentTestCase;	
			currentPosTestCase++;
			currentTestCase = currentTestSequence.get(currentPosTestCase);
			currentTestCase.setActualFeatureStatus(
					new ArrayList<String>(previousTestCase.getActualFeatureStatus())
			);
		}
	}
	
	public void moveNextTestSeq() {
		if ((currentPosTestSequence + 1) == testSequences.size()) {
			isFinished = true;
		} else {
			TestCase previousTestCase = currentTestCase;
			
			currentPosTestSequence++;
			currentTestSequence = testSequences.get(currentPosTestSequence);
			
			currentPosTestCase = 0;
			isCurrentSeqFinished = false;
			currentTestCase = currentTestSequence.get(currentPosTestCase);
			
			currentTestCase.setActualFeatureStatus(
					new ArrayList<String>(previousTestCase.getActualFeatureStatus())
			);
		}
	}
	
	public boolean isCurrentSeqFinished() {
		return isCurrentSeqFinished;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public void setTestSequences(ArrayList<ArrayList<TestCase>> testSequences) {
		this.testSequences = testSequences;
	}
	
}
