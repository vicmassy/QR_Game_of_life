package sample;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.problem.AbstractProblem;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Problem extends AbstractProblem {

    public static int DEFAULT_SIZE = 65;

    private BinaryVariable goalGrid;
    private Map<Pair<Double, Double>, BinaryVariable> population;

    public Problem() {
        super(1, 2);
        population = new HashMap<>();
    }

    public void setGoalGrid(Cell[][] grid) {
        this.goalGrid = new BinaryVariable(625);
        for(int i = 0; i < grid.length; ++i) {
            for(int j = 0; j < grid.length; ++j) {
                goalGrid.set(i*25+j, grid[i][j].getState());
            }
        }
    }

    public BinaryVariable getCandidate(double objective1, double objective2) {
        return population.get(new Pair<>(objective1, objective2));
    }

    private BinaryVariable initializeChromosome() {
        BinaryVariable chromosome = new BinaryVariable(DEFAULT_SIZE*DEFAULT_SIZE);
        Random random = new Random();
        for(int i = 0; i < DEFAULT_SIZE; ++i) {
            for (int j = 0; j < DEFAULT_SIZE; ++j) {
                if (i >= ((DEFAULT_SIZE-25)/2) && i < ((DEFAULT_SIZE-25)/2+25) && j >= ((DEFAULT_SIZE-25)/2) && j < ((DEFAULT_SIZE-25)/2+25)) {
                    chromosome.set(i*DEFAULT_SIZE+j, false);
                }
                else {
                    chromosome.set(i * DEFAULT_SIZE + j, random.nextBoolean());
                }
            }
        }
        return chromosome;
    }

    private BinaryVariable ToBinaryVariable(Cell[][] cells) {
        BinaryVariable binaryVariable = new BinaryVariable(625);
        for(int i = ((DEFAULT_SIZE-25)/2); i < ((DEFAULT_SIZE-25)/2+25); ++i) {
            for (int j = ((DEFAULT_SIZE-25)/2); j < ((DEFAULT_SIZE-25)/2+25); ++j) {
                binaryVariable.set((i-((DEFAULT_SIZE-25)/2))*25+(j-((DEFAULT_SIZE-25)/2)), cells[i][j].getState());
            }
        }
        return binaryVariable;
    }

    //TODO: Reading a QR Code
    /*public static String readQRCode(String filePath, String charset, Map hintMap)
            throws FileNotFoundException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
                hintMap);
        return qrCodeResult.getText();
    }*/


    @Override
    public Solution newSolution() {
        Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives());
        solution.setVariable(0, initializeChromosome());
        return solution;
    }

    @Override
    public void evaluate(Solution solution) {
        BinaryVariable chromosome = (BinaryVariable) solution.getVariable(0);
        solution.setObjective(0, chromosome.cardinality());
        Board candidate = new Board(chromosome, DEFAULT_SIZE, DEFAULT_SIZE);
        int minDistance = DEFAULT_SIZE*DEFAULT_SIZE;
        BinaryVariable minChromosome = new BinaryVariable(DEFAULT_SIZE*DEFAULT_SIZE);
        int currentDistance;
        for(int i =0; i < 500; ++i) {
            candidate.update();
            currentDistance = ToBinaryVariable(candidate.getGrid()).hammingDistance(goalGrid);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                minChromosome = chromosome.copy();
            }
        }
        solution.setObjective(1, minDistance);

        population.put(new Pair<>((double)minChromosome.cardinality(), (double)minDistance), chromosome);
    }
}
