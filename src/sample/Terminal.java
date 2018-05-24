package sample;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;

import java.io.File;

public class Terminal {

    public static void main(String[] args) {
        Board b = FileHandler.loadFromFile(new File("theXenotext.qr"), 25);
        Cell[][] grid = b.getGrid();
        for (Cell[] aGrid : grid) {
            for (int j = 0; j < grid.length; ++j) {
                System.out.print(aGrid[j].getState() ?  "X " : "  ");
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------");

        Problem problem = new Problem();
        problem.setGoalGrid(grid);
        NondominatedPopulation result = new Executor()
                .withProblem(problem)
                .withAlgorithm("NSGAII")
                .distributeOnAllCores()
                .withMaxEvaluations(30000)
                .run();

        //display the results
        System.out.format("Objective1  Objective2%n");

        for (Solution solution : result) {
            System.out.println(solution.getObjective(0) + "      " + solution.getObjective(1));
            BinaryVariable binaryVariable = problem.getCandidate(solution.getObjective(0), solution.getObjective(1));
            for(int i = ((Problem.DEFAULT_SIZE-25)/2); i < ((Problem.DEFAULT_SIZE-25)/2+25); ++i) {
                for (int j = ((Problem.DEFAULT_SIZE-25)/2); j < ((Problem.DEFAULT_SIZE-25)/2+25); ++j) {
                    System.out.print(binaryVariable.get(i*25+j)? "X " : "  ");
                }
                System.out.println();
            }
            System.out.println("---------------------------------------------------");
        }
    }

}
