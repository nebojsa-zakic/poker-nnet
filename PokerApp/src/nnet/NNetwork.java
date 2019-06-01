package nnet;

import java.io.IOException;
import java.time.Instant;
import java.util.Scanner;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

public class NNetwork {
	public static final String NNET_LOCATION = "C:\\Users\\user777\\Desktop\\poker.nnet";
	static Scanner in;
	
	static NeuralNetwork<BackPropagation> neuralNetwork;
	
	public static void main(String[] args) throws IOException {
		in = new Scanner(System.in);
		menu();
	}

	public static void menu() {
		menuPrint();
		selectChoice();
	}
	
	public static void menuPrint() {
		System.out.println("1. Nova mreza");
		System.out.println("2. Ucitaj mrezu");
		System.out.println("3. Test mreze");
		System.out.println("4. Rucni test");
		System.out.println("5. Treniraj ucitanu mrezu");
		System.out.println("0. Izlaz ");
	}
	
	public static void selectChoice() {
		int choice = in.nextInt();
		
		switch(choice) {
			case 1: createNew(); break;
			case 2: loadNetwork(); break;
			case 3: try {
				testFromSet(neuralNetwork, getDataSet());
			} catch (IOException e) {
				e.printStackTrace();
			} break;
			case 4: 
				in.nextLine();
				System.out.println("Unesite ruku.");
				testRow(neuralNetwork, new DataSetRow(HandNormalizer.parseLine(in.nextLine())), true);
				break;
			case 5:
				learn();
				break;
			case 0:
				in.close();
				System.exit(0);
		}
		
		menu();
	}
	
	public static void learn() {
		System.out.println("Zapoceto (" + Instant.now() + ")");
		int max = 101;

                try {
                    DataSet loadedSet = getDataSet();
                
                    for (int i = 0; i < max; i++) {

                        neuralNetwork.learn(loadedSet);

                       // System.out.println("Cuvanje trenutnog stanja " + (i + 1) + "/" + max + " (" + Instant.now() + ")");
                        if (i % 100 == 0) {
                            neuralNetwork.save(NNET_LOCATION);
                        }
                    }
                
                } catch (IOException e) {        
                    e.printStackTrace();
        	}

		System.out.println("Zavrseno (" + Instant.now() + ")");
	}
	
	public static void testRow(NeuralNetwork<BackPropagation> neuralNetwork, DataSetRow dsrw) {
		neuralNetwork.setInput(dsrw.getInput());
		neuralNetwork.calculate();
		System.out.println(neuralNetwork.getOutput()[0] + " " + neuralNetwork.getOutput()[1]);
	}
	
        public static double[] evaluate(DataSetRow dsrw) {
            neuralNetwork.setInput(dsrw.getInput());
            neuralNetwork.calculate();
            
            return neuralNetwork.getOutput();
        }
        
	public static void testRow(NeuralNetwork<BackPropagation> neuralNetwork, DataSetRow dsrw, boolean noInput) {
		neuralNetwork.setInput(dsrw.getInput());
		neuralNetwork.calculate();
		if (!noInput) {
			System.out.println(neuralNetwork.getOutput()[0] + " " + neuralNetwork.getOutput()[1] * 100000);
		} else {
			System.out.println(neuralNetwork.getOutput()[0] + " " + neuralNetwork.getOutput()[1] * 100000);
		}
	}
	
	public static void testFromSet(NeuralNetwork<BackPropagation> neuralNetwork, DataSet ds) {
		for (DataSetRow dsrw: ds.getRows()) {
			testRow(neuralNetwork, dsrw);
		}
	} 
	
	public static void createNew() {
		neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 17, 4, 2);
		MomentumBackpropagation learnRule = (MomentumBackpropagation)neuralNetwork.getLearningRule();
		Config.set(learnRule);
	}
	
	public static void loadNetwork() {
		neuralNetwork = NeuralNetwork.createFromFile(NNET_LOCATION);
		MomentumBackpropagation learnRule = (MomentumBackpropagation)neuralNetwork.getLearningRule();
		Config.set(learnRule);
	}
	
	public static DataSet getDataSet() throws IOException {
		return HandNormalizer.loadTrainingData("C:\\Users\\user777\\Desktop\\poker_data.txt");
	}
}

class Config {
	private static int maxIterations = 100;
	private static double learningRate = 0.5;
	private static double maxError = 0.00001;
	
	static void set(BackPropagation learnRule) {
		learnRule.setMaxError(maxError);
		learnRule.setLearningRate(learningRate);
		learnRule.setMaxIterations(maxIterations);
	}
}