package com.tuks.cos.assigment;

import java.lang.reflect.Array;
import java.util.*;

public class Chromosome {
    /*
    <expr> =  <var> | (<expr> <op> <expr>)
    <op> = + | - | *
    <var> = 1 | 2 | 3 | 4 | 5 | 6 | 7| 8 | 9 | x
    */

    private String octets="";
    private Random random;
    private int x;

    public Chromosome(Random random) {
        this.random =random;
        octets = createExpr(5);
    }

    public Chromosome(Chromosome oldChromosome){
        this.random = oldChromosome.random;
        this.octets = oldChromosome.octets;
    }

    public double getAnswer(int x){
        Queue<Integer> numbers = getAllIntegers(octets);
        this.x = x;
        return evaluateExpr(numbers);
    }

    public void mutation(){
        String newOctets =createExpr(5);
        List<String> numbers = Arrays.asList(octets.split(" "));
        int startIndex = random.nextInt(numbers.size());
        while(numbers.get(startIndex).equals("2") || numbers.get(startIndex).isEmpty()){
            startIndex = random.nextInt(numbers.size());
        }
        if (numbers.get(startIndex+1).equals("2")){
            startIndex= startIndex-1;
        }

        addNewNode(newOctets, startIndex);
    }

    public Chromosome reproduction(){
        return new Chromosome(this);
    }

    public String getSubString(){
        List<String> octet = Arrays.asList(octets.split(" "));
        int position;
        String output;
        do {
            position=random.nextInt(octet.size());
        }while (octet.get(position).equals("2"));

        if (octet.get(position+1).equals("2")){
            int exprNumber =strToBinary(octet.get(position-1));
            if (exprNumber%2==0){
                output= octet.get(position-1);
                output+=" "+octet.get(position)+" "+octet.get(position+1);
                return output;
            }else{
                position = position-1;
            }
        }

        if (position==0)
            return octets;

        int exprNumber =strToBinary(octet.get(position));
        int numberOfTwoNeed;
        if (exprNumber%2==0){
            numberOfTwoNeed =2;
        }else{
            numberOfTwoNeed=3;
        }
        output= octet.get(position);
        String num;
        for (int counter=position+1; counter<octet.size(); counter++){
            num=octet.get(counter);
            if (num.equals("2")){
                numberOfTwoNeed--;
            }
            else if (!octet.get(counter+1).equals("2")){
                exprNumber =strToBinary(octet.get(counter));
                if (exprNumber%2==1){
                    numberOfTwoNeed+=3;
                }
            }
            output+=" "+num;
            if (numberOfTwoNeed==1)
                break;
        }

        return output;
    }

    public double getFitness(List<Integer> xs, List<Double> ys) {
        long fitness =0;
        double answer;
        for (int counter=0; counter < xs.size(); counter++) {
            answer = getAnswer(xs.get(counter));
            fitness += Math.sqrt(Math.pow((ys.get(counter)-answer),2));
        }
        return fitness;
    }

    public void crossover(String oldString, String newString) {
        int startingPosition =octets.indexOf(oldString);
        int endingPosition =startingPosition+oldString.length();
        String newOctets =octets.substring(0,startingPosition);
        newOctets += newString;
        newOctets += octets.substring(endingPosition);
        octets = newOctets;
    }

    @Override
    public String toString() {
        String output =getStringForExpr(this.getAllIntegers(octets));
        return output;
    }

    private void addNewNode(String newNodes,int startingIndex){
        List<String> numbers = Arrays.asList(octets.split(" "));
        List<String> finalOctets = new ArrayList<>();
        int endingIndex = getLastIndex(numbers, startingIndex);

        for (int counter =0; counter<startingIndex; counter++){
            finalOctets.add(numbers.get(counter));
        }
        finalOctets.addAll(Arrays.asList(newNodes.split(" ")));
        for (int counter =endingIndex; counter<numbers.size(); counter++){
            finalOctets.add(numbers.get(counter));
        }
        octets="";
        for (String stringOctets:finalOctets) {
            octets=octets+stringOctets+" ";
        }
    }

    private String getStringForExpr(Queue<Integer> numbers) {
        int number = numbers.poll();
        if(number%2 == 0){
            return getStringVar(numbers);
        } else {
            return getStringOp(numbers);
        }
    }

    private String getStringOp(Queue<Integer> numbers) {
        int number = numbers.poll();
        String value1 = getStringForExpr(numbers);
        String value2 = getStringForExpr(numbers);
        if (number%3 == 0){
            return "("+value1+") + ("+value2+")";
        }else if (number%3 == 1){
            return "("+value1+") - ("+value2+")";
        }else {
            return "("+value1+") * ("+value2+")";
        }/* else{
            return "("+value1+") / ("+value2+")";
        }*/
    }

    private String getStringVar(Queue<Integer> numbers) {
        int number = numbers.poll();
        //1 | 2 | 3 | 4 | 5 | 6 | 7| 8 | 9 | x
        if(number%15 < 10
        )
            return Integer.toString(((number%10)+1));
        else
            return "x";
    }

    private int getLastIndex(List<String> numbers, int startIndex) {
       int lastIndex = 0;
       int numberOfEnds =1;
       for (int counter = startIndex; counter<numbers.size();counter++){
            if((counter+1)>=(numbers.size()-1)){
                lastIndex = (numbers.size()-1);
                break;
            }
            if(numbers.get(counter+1).equals("2")){
                numberOfEnds--;
            } else if(strToBinary(numbers.get(counter))%2==1){
                numberOfEnds += 2;
            }
            if (numberOfEnds == 0){
                lastIndex = (counter+1);
                break;
            }
       }
       return (lastIndex+1);
    }

    private double evaluateExpr(Queue<Integer> numbers){
        int num = numbers.poll();
        double output;
        if (num%2 == 0){
            output = evaluateVar(numbers);
        } else{
            output = evaluateOP(numbers);
        }
        return output;
    }

    private double evaluateOP(Queue<Integer> numbers) {
        int num = numbers.poll();
        double output;
        double value1 = evaluateExpr(numbers);
        double value2 = evaluateExpr(numbers);
        if (num%3 == 0){
            output = value1+value2;
        }else if (num%3 == 1){
            output = value1-value2;
        }else {
            output = value1*value2;
        }
        return output;
    }

    private int evaluateVar(Queue<Integer> numbers) {
        int number = numbers.poll();
        int output;
        if(number%12 <= 8)
            output= (((number%10)+1));
        else
            output= x;
        return output;
    }

    private String createExpr(int numberNodes) {
        int randomNumber = random.nextInt(256);
        String output;
        if (randomNumber%2==0 || numberNodes==0){
            if(randomNumber%2!=0){
                output = Integer.toBinaryString((randomNumber+1))+" ";
            } else{
                output = Integer.toBinaryString(randomNumber)+" ";
            }
            output =output + createVar();
        } else {
            output = Integer.toBinaryString(randomNumber)+" ";
            output =output + createOp() + createExpr((numberNodes-1)) + createExpr((numberNodes-1));
        }
        return  output;
    }

    private String createOp() {
        int randomNumber = random.nextInt(256);
        return Integer.toBinaryString(randomNumber)+" 2 ";
    }

    private String createVar() {
        int randomNumber = random.nextInt(256);
        return Integer.toBinaryString(randomNumber)+" 2 ";
    }

    private Queue<Integer> getAllIntegers(String string)
    {
        List<String> octets = Arrays.asList(string.split(" "));
        Queue<Integer> output = new LinkedList<>();
        for (String octet: octets) {
            if(!octet.equals("2"))
                output.add(strToBinary(octet));
        }
        return output;
    }

    private int strToBinary(String octet)
    {
        String reverseString = reverse(octet);
        int number =0;
        for (int counter =0; counter<reverseString.length(); counter++){
            number += (int) (Math.pow(2,counter)*Integer.parseInt(String.valueOf(reverseString.charAt(counter))));
        }
        return number;
    }

    private String reverse(String input)
    {
        char[] a = input.toCharArray();
        int l, r = 0;
        r = a.length - 1;

        for (l = 0; l < r; l++, r--)
        {
            // Swap values of l and r
            char temp = a[l];
            a[l] = a[r];
            a[r] = temp;
        }
        return String.valueOf(a);
    }
}
