package br.ufc.great.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import com.google.gson.Gson;

import java.lang.reflect.Method;

import br.ufc.great.ControlUtils;
import br.ufc.great.TestCase;
import br.ufc.great.TestSequences;
import br.ufc.great.annotations.ControlContext;
import br.ufc.great.annotations.ControlContextGroup;

@Aspect
public class ContextAspect {

    @Pointcut("@annotation(controlContextAnnotation)")
    public void controlContextGroupPointcut(ControlContextGroup controlContextAnnotation){}
    
    @Pointcut("@annotation(controlContextAnnotation)")
    public void controlContextPointcut(ControlContext controlContextAnnotation){}

    @Pointcut("execution(* *(..))")
    public void atExecution(){}
    
    @Around("controlContextPointcut(controlContextAnnotation) && atExecution()")
    public Object contextAdvice(ProceedingJoinPoint joinPoint, ControlContext controlContextAnnotation) throws Throwable {	
    	
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
    					// TODO - Capturar o tipo de retorno e verificar se é do tipo bolean.
    					// Invalidar item da sequência caso seja diferente.
    					// Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    					String contextName = controlContextAnnotation.contextName();
    					
    					if (currentTestCase.containsContext(contextName)) {
    						setReturnValue(true);
    					} else {
    						setReturnValue(false);
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

    @Around("controlContextGroupPointcut(controlContextAnnotation) && atExecution()")
    public Object contextGroupAdvice(ProceedingJoinPoint joinPoint, ControlContextGroup controlContextAnnotation) throws Throwable {	
    	
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
    					Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//    					String newValue = currentTestCase.getContextStatevalue(controlContextAnnotation.contextName());
    					
    					String contextGroup = controlContextAnnotation.contextGroupName();
    					String newValue = currentTestCase.getContextGroupValue(contextGroup);
    					if (!newValue.equals("UNDEFINED")) {
    						changeReturnValue(newValue, method.getReturnType().getSimpleName().toString());
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

//    @After("annotationPointCutDefinition(controlContextAnnotation) && atExecution()")
//    public void printNewLine(JoinPoint pointcut, ControlContext controlContextAnnotation){
//        System.out.print("\n\r");
//    }
    
    // ------------------------------------------------------------
    // TODO - DEBUG
//    public TestCase getTestCase() {
//        Path currentRelativePath = Paths.get("");
//        String fileRelativePath = "\\control\\src\\main\\java\\br\\ufc\\great\\assets\\test_sequence.json";
//        String filePath = currentRelativePath.toAbsolutePath().toString() + fileRelativePath;
//        return ControlUtils.getTestItemFromJson(filePath);
//    }
   
}