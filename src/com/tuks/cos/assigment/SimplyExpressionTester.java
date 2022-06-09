package com.tuks.cos.assigment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class SimplyExpressionTester {

    private static HashMap<String,String> additionTestAndAnswer = new HashMap<>();
    private static HashMap<String,String> subtractionTestAndAnswer = new HashMap<>();
    private static HashMap<String,String> multiplicationTestAndAnswer = new HashMap<>();

    public static void main(String[] args) {
        getAdditionTests();
        getSubtractionTests();
        getMultiplicationTests();

        SimplyExpression simplyExpression;
        int counter=1;
        System.out.println("Addition test");
        for (String test: additionTestAndAnswer.keySet()) {
            simplyExpression = new SimplyExpression(test);
            boolean check = additionTestAndAnswer.get(test).equals(simplyExpression.simply());
            if (!check) {
                System.out.println("file: " + additionTestAndAnswer.get(test));
                System.out.println("output: " + simplyExpression.simply());
            }
            simplyExpression = new SimplyExpression(test);
            System.out.println(counter+". "+test+": "+(additionTestAndAnswer.get(test).equals(simplyExpression.simply())));
            counter++;
        }

        System.out.println("");
        System.out.println("Subtraction test");
        counter=1;
        for (String test: subtractionTestAndAnswer.keySet()) {
            simplyExpression = new SimplyExpression(test);
            boolean check = subtractionTestAndAnswer.get(test).equals(simplyExpression.simply());
            if (!check) {
                System.out.println("file: " + subtractionTestAndAnswer.get(test));
                System.out.println("output: " + simplyExpression.simply());
            }
            simplyExpression = new SimplyExpression(test);
            System.out.println(counter+". "+test+": "+(subtractionTestAndAnswer.get(test).equals(simplyExpression.simply())));
            counter++;
        }

        System.out.println("");
        System.out.println("Multiplication test");
        counter=1;
        for (String test: multiplicationTestAndAnswer.keySet()) {
            simplyExpression = new SimplyExpression(test);
            boolean check = multiplicationTestAndAnswer.get(test).equals(simplyExpression.simply());
            if (!check) {
                System.out.println("file: " + multiplicationTestAndAnswer.get(test));
                System.out.println("output: " + simplyExpression.simply());
            }
            simplyExpression = new SimplyExpression(test);
            System.out.println(counter+". "+test+": "+(multiplicationTestAndAnswer.get(test).equals(simplyExpression.simply())));
            counter++;
        }
    }

    public static void getAdditionTests(){
        try {
            File myObj = new File("C:\\Users\\User\\OneDrive\\tuks\\honors\\year 1\\semester 1\\COS 710\\GrammaticalEvolution\\Addition");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] values = data.split("=");
                additionTestAndAnswer.put(values[0],values[1]);
            }
            myReader.close();
        } catch ( FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void getSubtractionTests(){
        try {
            File myObj = new File("C:\\Users\\User\\OneDrive\\tuks\\honors\\year 1\\semester 1\\COS 710\\GrammaticalEvolution\\Subtraction");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] values = data.split("=");
                subtractionTestAndAnswer.put(values[0],values[1]);
            }
            myReader.close();
        } catch ( FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void getMultiplicationTests(){
        try {
            File myObj = new File("C:\\Users\\User\\OneDrive\\tuks\\honors\\year 1\\semester 1\\COS 710\\GrammaticalEvolution\\Multiplication");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] values = data.split("=");
                multiplicationTestAndAnswer.put(values[0],values[1]);
            }
            myReader.close();
        } catch ( FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
