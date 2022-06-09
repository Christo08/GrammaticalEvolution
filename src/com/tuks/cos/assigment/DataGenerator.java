package com.tuks.cos.assigment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    public static void main(String[] args) {
        Random random = new Random();

        List<Integer> xs = new ArrayList<>();
        List<Double> ys = new ArrayList<>();
        for (int counter =0; counter<201; counter++){
            xs.add(counter-100);
            ys.add(function(counter-100));
        }

        try {
            File myObj = new File("fitnessCase.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

            FileWriter myWriter = new FileWriter("fitnessCase.txt",true);
            for (int counter =0; counter<xs.size(); counter++){
                myWriter.write(xs.get(counter)+": "+ys.get(counter)+"\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    static double function(int x){
        return (x*x+1);
    }
}
