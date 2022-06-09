package com.tuks.cos.assigment;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random random = new Random();
        long seed = Long.parseLong("-2070160575309264405");
        System.out.println(seed);
        random.setSeed(seed);
        PopController popController = new PopController(random);
        Chromosome answer =popController.evaluationAnswer();
        popController.shutdown();
        String output = answer.toString();
        System.out.println(output);
        SimplyExpression simplyExpression = new SimplyExpression(output);
        System.out.println(simplyExpression.simply());
        System.out.println(answer.getAnswer(100));


    }
}
