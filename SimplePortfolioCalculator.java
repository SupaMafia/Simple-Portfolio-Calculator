/*
 * Date: 2021-8-1.
 * File Name: Simple Portfolio Calculator.java
 * Version: 0.1
 * Author: Weikang Ke
 */

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * A program that calculates optimum portfolio for either maximizing return or minimizing risk. A simple implementation of Modern portfolio theory.
 */

public class SimplePortfolioCalculator {
    public static void main(String[] args) {

        //input indices, take index Price A, index B
        String filename0 = "Indices.txt";
        Scanner inputStream0 = null;
        try {
            inputStream0 = new Scanner(new FileInputStream(filename0));
        } catch (FileNotFoundException e) {
            System.out.println("File " + filename0 + " was not found, or could not be opened");
            System.exit(0);
        }
        //read total number of dates for indices
        int dateNum = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename0))) {
            while (reader.readLine() != null) dateNum++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Total number of date is: " + dateNum);
        // read data into array: indexData
        double[][] indexData = new double[2][dateNum]; //create original data array
        for (int i = 0; i < dateNum; i++) { //takes data in indices into array
            double date = inputStream0.nextDouble();
            indexData[0][i] = inputStream0.nextDouble();
            indexData[1][i] = inputStream0.nextDouble();
            String line = inputStream0.nextLine(); // go the next line
        }
        inputStream0.close(); //close inputStream
        /*System.out.println("Array length: " + indexData.length); //check the functioning of input method
        for (int i = 0; i < dateNum; i++) {
            System.out.println("line of index: " + i + " index A: " + indexData[0][i] + " index B: " + indexData[1][i]);
        }
         */

        //calculate daily return A and daily excess return B
        double[][] dailyReturn = new double[2][dateNum - 1]; //create daily return data array, length is shorter by 1 line due to the nature of calculation
        for (int i = 0; i < dateNum - 1; i++) {
            dailyReturn[0][i] = (indexData[0][i] - indexData[0][i + 1]) / indexData[0][i + 1];
            dailyReturn[1][i] = (indexData[1][i] - indexData[1][i + 1]) / indexData[1][i + 1];
            if (i == dateNum - 1) {
                break;
            }
        }
        /*for (int i = 0; i < dateNum - 1; i++) {//check function
            System.out.println("daily return of index A: " + dailyReturn[0][i] + " daily return of index B: " + dailyReturn[1][i]);
        }
         */

        //calculate daily mean return of A and daily mean return of B
        double sumReturnA = 0;
        for (int i = 0; i < dateNum - 1; i++) {
            sumReturnA += dailyReturn[0][i];
        }
        double dailyMeanReturnA = sumReturnA / (dateNum - 1);
        double sumReturnB = 0;
        for (int i = 0; i < dateNum - 1; i++) {
            sumReturnB += dailyReturn[1][i];
        }
        double dailyMeanReturnB = sumReturnB / (dateNum - 1);
        //System.out.println("Daily Mean Return of A is =" + dailyMeanReturnA + " Daily Mean Return of B is =" + dailyMeanReturnB);

        // Take input of Bank Deposit Rate (year rate) to calculate Daily Risk Free Rate
        System.out.println("Enter Bank Deposit Rate (yearly,): ___% (Nordea SE currently has deposit rate at 0.000%)");
        double depositRate = 0;
        int count = 0;
        do {
            try {
                Scanner in0 = new Scanner(System.in);
                depositRate = in0.nextDouble();
                System.out.println("deposite rate=" + depositRate);
                break;
            } catch (InputMismatchException e0) {
                System.out.println("Non-numeric values are not allowed, Please  re enter");
            } finally {
                count++;
            }
        } while (count < 10);
        double riskFreeRate = Math.pow((1 + depositRate), (0.0027397260273973)) - 1;
        //System.out.println("Daily Risk Free Rate = " + riskFreeRate);

        //calculate Daily Mean Excess Return of A and B
        double meanExcessReturnA = dailyMeanReturnA - riskFreeRate;
        double meanExcessReturnB = dailyMeanReturnB - riskFreeRate;
        //System.out.println("Daily Mean Excess Return of A =" + meanExcessReturnA + " Daily Mean Excess Return of B =" + meanExcessReturnB);

        //calculate Standard Deviation of A and B
        double stdDevA;
        double sodA; //Square of difference (x-x bar)^2
        double sosodA = 0; //Sum of square of difference
        for (int i = 0; i < dateNum - 1; i++) {
            double diffA = dailyReturn[0][i] - dailyMeanReturnA;
            sodA = Math.pow(diffA, 2);
            sosodA += sodA;
        }
        stdDevA = Math.sqrt(sosodA / (dateNum - 1)); //(dateNum -1) as n, and n by formula (population)
        double stdDevB;
        double sodB; //Square of difference (x-x bar)^2
        double sosodB = 0; //Sum of square of difference
        for (int i = 0; i < dateNum - 1; i++) {
            double diffB = dailyReturn[1][i] - dailyMeanReturnB;
            sodB = Math.pow(diffB, 2);
            sosodB += sodB;
        }
        stdDevB = Math.sqrt(sosodB / (dateNum - 1)); //(dateNum -1) as n, and n by formula (population)
        //System.out.println("Std.Dev. of A =" + stdDevA + " Std.Dev. of B =" + stdDevB);

        //Calculate sharpe ratio A and B
        double sharpeA = meanExcessReturnA / stdDevA;
        double sharpeB = meanExcessReturnB / stdDevB;
        System.out.println("Sharpe ratio of A =" + sharpeA + " Sharpe ratio of B =" + sharpeB);

        //Calculate covariance of Daily Excess Return of A and B
        double pod; //product of difference
        double sopod = 0; //sum of product of difference
        for (int i = 0; i < dateNum - 1; i++) {
            double diffA = dailyReturn[0][i] - dailyMeanReturnA;
            double diffB = dailyReturn[1][i] - dailyMeanReturnB;
            pod = diffA * diffB;
            sopod += pod;
        }
        double covAB = sopod / (dateNum - 1); //(dateNum -1) as n, and n by formula (population)
        //System.out.println("Covariance of return = " + covAB);

        //Calculate correlation coefficient of Daily Excess Return of A and B
        double corAB = 0;
        double varA = sosodA / (dateNum - 1);
        double varB = sosodB / (dateNum - 1);
        corAB = covAB / (varA * varB);
        //System.out.println("Correlation coefficient of return is =" + corAB);

        //calculate Maximum Sharpe-ratio Portfolio
        double msWeightA = (meanExcessReturnA * varB - meanExcessReturnB * covAB) / (meanExcessReturnB * varA + meanExcessReturnA * varB - (meanExcessReturnA + meanExcessReturnB) * covAB);
        double msWeightB = 1 - msWeightA;
        System.out.println("For Maximum Sharpe-ratio Portfolio, Weight of index A =" + msWeightA * 100 + "%" + " Weight of index B =" + msWeightB * 100 + "%");

        //Calculate Minimum Variance Portfolio
        double mvWeightA = (varB - covAB) / (varA + varB - 2 * covAB); //find local minimum
        double mvw0 = mvWeightA - 1; //to check if it is local minimum of the variance function
        double mvw1 = mvWeightA + 1;
        double ck0 = 2 * (varA * mvw0 - varB * (1 - mvw0) - 2 * covAB * mvw0 + covAB);
        double ck1 = 2 * (varA * mvw1 - varB * (1 - mvw1) - 2 * covAB * mvw1 + covAB);
        if (ck0 < 0 && ck1 > 0) {
            double mvWeightB = 1 - mvWeightA;
            System.out.println("For minimum Variance Portfolio (check with graph), Weight of index A =" + mvWeightA * 100 + "%" + " Weight of index B =" + mvWeightB * 100 + "%");
        } else {
            System.out.println("There is no minimum Variance Portfolio, please verify through graphes or check inputs");
        }
    }
}
