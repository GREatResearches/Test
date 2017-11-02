package cfmts.example;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class Test_SatSolver {
	

	public static void main(String[] args) {
		//path "D:/Dropbox/Arquivos - GREat/Experimentos_qualificacao_SBSE/dados_experimento/LPSD_cnf.cnf"
		boolean status = false;
		String fileName = "testFile";
		//Passando as regras para o arquivo CNF
		creatingCNFFile(fileName);
		//Aplicando o SAT solver
		ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);
        // CNF filename is given on the command line 
        System.out.println("vai ler");
        try {
        	FileInputStream in = new FileInputStream("D:/"+fileName+".cnf");
        	
            IProblem problem = reader.parseInstance(in);
            System.out.println("Ok, leu o arquivo");
            if (problem.isSatisfiable()) {
            	System.out.println("Satisfiable !");
                System.out.println(reader.decode(problem.model()));		            	
            } else {
                System.out.println("Unsatisfiable !");
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        	System.out.println("File not found");
        } catch (ParseFormatException e) {
            // TODO Auto-generated catch block
        	System.out.println("Mal formatado");
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	System.out.println("Io error");
        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");      
        }
	}
	
	public static void creatingCNFFile(String fileName){
		//esse vetor vai guardar os identificados das fetures nao utilizadas para acrescentar no final do arquivo CNF
		//Isso porque o numero de variáveis do arquivo CNF deve ser compatível com o numero de features
		
		try {
	      /* Open the file */
	      FileOutputStream fos   = new FileOutputStream("D:/"+fileName+".cnf");
	      OutputStreamWriter osw = new OutputStreamWriter(fos);
	      BufferedWriter bw      = new BufferedWriter(osw);


	      int nVariables = 3;
	      int nClauses = 2;
	      //Indicando a quantidade de variáveis e clásulas
	      bw.write("p cnf "+ nVariables +" "+nClauses);
	      bw.newLine();
	      StringBuffer st;
	      //Regras And Group
	      
	  	  
	      //-x1 or x2 :: x1 requer x2
		  //-x1 or -x2 :: x1 exclui x2 => ou um ou outro
	      st = new StringBuffer();
	      st.append("1 ");
		  st.append("0");
		  bw.write(st.toString());
		  bw.newLine();
	      
		  // contexto true ->
		  // constrainsts ->
		  // uma variavel para cada contexto
		  
		  st.append("1 3 ");
		  st.append("0");
		  bw.write(st.toString());
		  
	      /* Close the file */
	      bw.close();
	    }catch (IOException e) {
	      e.printStackTrace();
	    }
	}
			
}
