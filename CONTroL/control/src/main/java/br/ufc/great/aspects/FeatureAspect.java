
package br.ufc.great.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import br.ufc.great.ControlUtils;
import br.ufc.great.TestCase;
import br.ufc.great.TestSequences;
import br.ufc.great.annotations.ControlFeature;

@Aspect
public class FeatureAspect {

    @Pointcut("@annotation(controlFeatureAnnotation)")
    public void controlFeaturePointcut(ControlFeature controlFeatureAnnotation){}

    @Pointcut("execution(* *(..))")
    public void atExecution(){}

    @Around("controlFeaturePointcut(controlFeatureAnnotation) && atExecution()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, ControlFeature controlFeatureAnnotation) throws Throwable {	
    	
    	RunnableWithReturn worker = new RunnableWithReturn() {
    		public void run() {
    			TestSequences testSequences = ControlUtils.getTestSequences();
    			
    			try {
					setReturnValue(joinPoint.proceed());
					
					testSequences.aquireLock();
					
					if (!testSequences.isFinished()) {
						TestCase currentTestCase = testSequences.getCurrentTestCase();
						String feature = controlFeatureAnnotation.feature();
						
						if ( (boolean) getReturnValue() ) {
							currentTestCase.addActualFeatures(feature);							
						} else {
							currentTestCase.removeActualFeatures(feature);
						}
						
					}
				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
					testSequences.releaseLock();
				}
    		}
    	};
    	
    	Thread t = new Thread(worker);
    	t.start();
    	t.join();
    	
    	return worker.getReturnValue();
    }
}