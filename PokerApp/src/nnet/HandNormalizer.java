/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nnet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 *
 * @author Nebojsa
 */
public class HandNormalizer {
    final static double MAX = 100000;
    
    static DataSet loadTrainingData(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
		DataSet trainingSet = new DataSet(17, 2);

		try {
			String line;
			int iter = 0;
			int max = 99999;
			
			while ((line = reader.readLine()) != null && (iter++) < max) {
                                System.out.println(line);
				double trainValues[] = parseLine(line);
				
				double expectedValue[] = new double[2];    
                                
                                expectedValue[0] = Double.parseDouble(line.split(" ")[17]);
                                expectedValue[1] = Double.parseDouble(line.split(" ")[18]) / MAX;

                                trainingSet.add(new DataSetRow(trainValues, expectedValue));
			}
		} finally {
			reader.close();
		}
		return trainingSet;
    }
    
    public static double[] parseLine(String line) {
        String[] values = line.split(" ");
        double[] result = new double[17];
        
        for (int i = 0; i < 14; i += 2) {
            result[i] = readCardValue(values[i]);
            result[i + 1] = readCardSymbol(values[i + 1]);
        }
        
        System.out.println(line);
        
        result[14] = Double.parseDouble(values[14]) / MAX;
        result[15] = Double.parseDouble(values[15]) / MAX;
        result[16] = Double.parseDouble(values[16]) / MAX;
        
        return result;
    }
    
    static double readCardValue(String num) {        
        switch(num) {
            case "K":
                return 0.014/0.015;
            case "Q":
                return 0.013/0.015;
            case "J":
                return 0.012/0.015;
            case "A":
                return 0.015/0.015;
            default:
                try {
                    return Double.parseDouble(num) / 15;
                } catch(Exception e) {
                    e.printStackTrace();
                    return 0;
                }
        }
    }

    static double readCardSymbol(String type) {
        switch(type) {
            case "0": 
                return 0;
            case "H":
                return 0.25;
            case "T":
                return 0.5;
            case "C":
                return 0.75;
            case "P":
                return 1;
            default:
                return 0.5;
        }
    }
}
   