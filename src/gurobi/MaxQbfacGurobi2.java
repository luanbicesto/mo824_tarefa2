package gurobi;
import gurobi.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
public class MaxQbfacGurobi2 {
  private static int _size;  
  private static Double A[][];  
  public static void main(String[] args) {


    try {
      readInput("instances/qbf060");
        
      GRBEnv env = new GRBEnv();
      GRBModel model = new GRBModel(env);
      
      model.set(GRB.DoubleParam.TimeLimit, 1800);
      
      //Creating variables
      GRBVar[] variables = new GRBVar[_size];
      for(int i = 0; i < _size; i++) {
          variables[i] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "x" + Integer.toString(i));
      }
      
      //Add objective function
      GRBQuadExpr objectiveExpr = new GRBQuadExpr();
      for(int i = 0; i < _size; i++) {
          for(int j = 0; j < _size; j++) {
              objectiveExpr.addTerm(A[i][j], variables[i], variables[j]);
          }
      }
      model.setObjective(objectiveExpr, GRB.MAXIMIZE);
      
      //Add adjacency constraint
      GRBLinExpr adjacencyConstraint;
      for(int i = 0; i < _size-1; i++) {
          adjacencyConstraint = new GRBLinExpr();
          adjacencyConstraint.addTerm(1.0, variables[i]);
          adjacencyConstraint.addTerm(1.0, variables[i+1]);
          model.addConstr(adjacencyConstraint, GRB.LESS_EQUAL, 1.0, "c" + Integer.toString(i));
      }
      
      // Optimize model
      model.optimize();
      
      System.out.println("Obj: " + model.get(GRB.DoubleAttr.ObjVal));
      
      // Dispose of model and environment
      model.dispose();
      env.dispose();
      
    } catch(GRBException e) {
        e.printStackTrace();
    } catch(Exception ex) {
        ex.printStackTrace();
    }
  }
  
  
  
  protected static Integer readInput(String filename) throws IOException {

		Reader fileInst = new BufferedReader(new FileReader(filename));
		StreamTokenizer stok = new StreamTokenizer(fileInst);

		stok.nextToken();
		_size = (int) stok.nval;
		A = new Double[_size][_size];

		for (int i = 0; i < _size; i++) {
			for (int j = i; j < _size; j++) {
				stok.nextToken();
				A[i][j] = stok.nval;				
				if (j>i)
					A[j][i] = 0.0;
			}
		}

		return _size;

	}
}
