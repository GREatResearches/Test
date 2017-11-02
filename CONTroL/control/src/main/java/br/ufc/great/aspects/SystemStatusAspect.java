package br.ufc.great.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.ufc.great.ControlUtils;
import br.ufc.great.TestCase;
import br.ufc.great.TestSequences;
import br.ufc.great.annotations.ControlSystemStatus;

@Aspect
public class SystemStatusAspect {	
	@Pointcut("@annotation(systemStatusAnnotation)")
    public void controlSystemStatusPointcut(ControlSystemStatus systemStatusAnnotation){}

    @Pointcut("execution(* *(..))")
    public void atExecution(){}

    @Around("controlSystemStatusPointcut(systemStatusAnnotation) && atExecution()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, ControlSystemStatus systemStatusAnnotation) throws Throwable {
    	
    	RunnableWithReturn worker = new RunnableWithReturn() {
    		public void run() {
    			
    			TestSequences testSequences = ControlUtils.getTestSequences();
    			
    			try {
    				setReturnValue(joinPoint.proceed());
    				
    				testSequences.aquireLock();
        			
        			// O teste a priori é necessário para o caso da das sequências já terem chegado ao fim
    				// mas a execução do programa continuou.
    				if (!testSequences.isFinished()) {
    					
    					TestCase currentTestCase = testSequences.getCurrentTestCase();
    					currentTestCase.assertFeatures();
        				// Atualiza o próximo caso de teste com os statues de features do testee anterior.
        				testSequences.moveNextTestCase();
        				
        				if (testSequences.isCurrentSeqFinished()) {
        					// Atualiza a variável isFinshed caso todas as sequências tenham sido executadas.
    						// Atualiza o primeiro teste da próxima sequência com os status features de último teste da sequência anterior.
    						testSequences.moveNextTestSeq();
    						
    						if (testSequences.isFinished()){
    							//TODO - GENERATE TEST REPORT.
    	    					
    	    					System.out.println("GENERATING DUMMY REPORT...");
    	    					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
    	    					String result = gson.toJson(testSequences);
    	    					System.out.println("-------------------------------------------------------------------------------------------");
    	    					System.out.println(result);
    	    					System.out.println("-------------------------------------------------------------------------------------------");
    						}
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
