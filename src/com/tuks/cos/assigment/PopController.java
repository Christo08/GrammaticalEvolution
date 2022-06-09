package com.tuks.cos.assigment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class PopController {
    private List<Chromosome> populations;
    private List<Integer> xs;
    private List<Double> ys;
    private Random random;
    private ExecutorService searchExecutor;

    private final int populationsSize =100;
    private final int tormentSize = 4;
    private final int minNumberOfGens = 50;
    private final int numberOfSameFitness = 20;

    private final double mutationPreceding = 0.7;
    private final double reproductionPreceding = 0.3;
    private final double crossoverPreceding = 0;
    private final double recreatePreceding = 0.5;

    public PopController(Random random) {
        this.populations = new ArrayList<>();
        this.random = random;
        this.searchExecutor = Executors.newFixedThreadPool(4);

        for (int counter =0; counter<populationsSize; counter++){
            Chromosome newChromosome;
            do {
                newChromosome=new Chromosome(random);
            }while (hasChromose(newChromosome));
            populations.add(newChromosome);
        }

        xs = new ArrayList<>();
        ys = new ArrayList<>();
        getFitness();
    }

    private boolean hasChromose(Chromosome newChromosome) {
        boolean output = false;
        for (Chromosome oldChromosome: populations) {
            if (oldChromosome.toString().equals(newChromosome.toString())){
                output = true;
                break;
            }
        }
        return output;
    }

    public Chromosome selectAChromosome(){
        List<Chromosome> populations = new ArrayList<>();
        int position;
        for (int counter = 0; counter< tormentSize; counter++){
            position = random.nextInt(populationsSize);
            populations.add(this.populations.get(position));
        }

        Chromosome winner =populations.get(0);
        double bestFitness= Double.MAX_VALUE;
        double tempFitness;
        for (Chromosome chromosome: populations) {
            tempFitness = chromosome.getFitness(xs,ys);
            if(tempFitness<=bestFitness) {
                winner = chromosome;
                bestFitness=tempFitness;
            }
        }
        return winner;
    }

    public Chromosome evaluationAnswer(){
        List<Chromosome> newPopulations;
        Chromosome winner = null;
        double fitness= Double.MAX_VALUE;
        double oldFitness= Double.MAX_VALUE;
        int numberOfSameFitness =0;
        for (int counter=0; fitness!=0; counter++){
            newPopulations = new ArrayList<>();
            newPopulations.addAll(mutation((int) (populationsSize*mutationPreceding)));
            newPopulations.addAll(crossover((int) (populationsSize*crossoverPreceding)));
            newPopulations.addAll(reproduction((int) (populationsSize*reproductionPreceding)));
            populations = newPopulations;
            winner =bestChromosome();
            fitness =winner.getFitness(xs,ys);
            if (fitness!=oldFitness){
                oldFitness =fitness;
                numberOfSameFitness =0;
            }else{
                numberOfSameFitness++;
            }
            if(counter%100==0){
                System.out.println("recreate population");
                recreatePartOfThePopulations();
            }
            System.out.println("Gen "+counter+" fitness: "+fitness+" number of same fitness: "+numberOfSameFitness);
            if(numberOfSameFitness>=this.numberOfSameFitness && counter>=minNumberOfGens)
            {
                break;
            }
        }
        return winner;
    }

    public void getFitness(){
        try {
            File myObj = new File("fitnessCase.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] values = data.split(": ");
                xs.add(Integer.parseInt(values[0]));
                ys.add(Double.parseDouble(values[1]));
            }
            myReader.close();
        } catch ( FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void shutdown() {
        searchExecutor.shutdown();
    }

    private void recreatePartOfThePopulations() {
        List<Integer> positionToReplace = new ArrayList<>();
        int numberOfPosition = (int) (minNumberOfGens*recreatePreceding);
        int position;
        for (int counter=0; counter<numberOfPosition;counter++){
            do {
                position = random.nextInt(populationsSize);
            }while (positionToReplace.contains(position));
            positionToReplace.add(position);
        }
        for (int number: positionToReplace){
            populations.set(number,new Chromosome(random));
        }
    }

    private Chromosome bestChromosome() {

        AtomicReference<Chromosome> winner1 =new AtomicReference<>();
        AtomicReference<Double> bestFitness1= new AtomicReference<>(Double.MAX_VALUE);
        searchExecutor.submit(()->{
            double tempFitness1;
            int startOfBach1=0;
            int endOfBach1=populations.size()/4;
            for (int counter=startOfBach1; counter<endOfBach1; counter++) {
                tempFitness1 = populations.get(counter).getFitness(xs,ys);
                if(tempFitness1<= bestFitness1.get()) {
                    winner1.set(populations.get(counter));
                    bestFitness1.set(tempFitness1);
                }
            }
        });

        AtomicReference<Chromosome> winner2 =new AtomicReference<>();
        AtomicReference<Double> bestFitness2= new AtomicReference<>(Double.MAX_VALUE);
        searchExecutor.submit(()-> {
            double tempFitness2;
            int startOfBach2 = populations.size() / 4;
            int endOfBach2 = populations.size() / 2;
            for (int counter = startOfBach2; counter < endOfBach2; counter++) {
                tempFitness2 = populations.get(counter).getFitness(xs, ys);
                if (tempFitness2 <= bestFitness2.get()) {
                    winner2.set(populations.get(counter));
                    bestFitness2.set(tempFitness2);
                }
            }
        });

        AtomicReference<Chromosome> winner3 =new AtomicReference<>();
        AtomicReference<Double> bestFitness3= new AtomicReference<>(Double.MAX_VALUE);
        searchExecutor.submit(()-> {
            double tempFitness3;
            int startOfBach3 = populations.size() / 2;
            int endOfBach3 = (populations.size() / 4) * 3;
            for (int counter = startOfBach3; counter < endOfBach3; counter++) {
                tempFitness3 = populations.get(counter).getFitness(xs, ys);
                if (tempFitness3 <= bestFitness3.get()) {
                    winner3.set(populations.get(counter));
                    bestFitness3.set(tempFitness3);
                }
            }
        });

        AtomicReference<Chromosome> winner4 =new AtomicReference<>();
        AtomicReference<Double> bestFitness4= new AtomicReference<>(Double.MAX_VALUE);
        searchExecutor.submit(()-> {
            double tempFitness4;
            int startOfBach4 = (populations.size() / 4) * 3;
            int endOfBach4 = populations.size();
            for (int counter = startOfBach4; counter < endOfBach4; counter++) {
                tempFitness4 = populations.get(counter).getFitness(xs, ys);
                if (tempFitness4 <= bestFitness4.get()) {
                    winner4.set(populations.get(counter));
                    bestFitness4.set(tempFitness4);
                }
            }
        });

        while (winner1.get() ==null || winner2.get()==null || winner3.get()==null || winner4.get()==null){

        }
        Chromosome winner = winner1.get();
        double bestFitness= bestFitness1.get();
        if (bestFitness>=bestFitness2.get()){
            winner = winner2.get();
            bestFitness = bestFitness2.get();
        }
        if (bestFitness>=bestFitness3.get()){
            winner = winner3.get();
            bestFitness = bestFitness3.get();
        }
        if (bestFitness>=bestFitness4.get()){
            winner = winner4.get();
        }

        return winner;
    }

    private List<Chromosome> mutation(int numberOfMutations){
        List<Chromosome> output = new ArrayList<>();
        Chromosome temp ;
        for (int counter = 0; counter< numberOfMutations; counter++){
            temp = new Chromosome(selectAChromosome());
            temp.mutation();
            output.add(temp);
        }
        return output;
    }

    private List<Chromosome> crossover(int numberOfCrossover){
        List<Chromosome> output = new ArrayList<>();
        Chromosome temp1 ;
        String subString1;
        Chromosome temp2 ;
        String subString2;
        for (int counter = 0; counter< numberOfCrossover/2; counter++){
            System.out.println(counter);
            temp1 = new Chromosome(selectAChromosome());
            subString1 = temp1.getSubString();

            temp2 = new Chromosome(selectAChromosome());
            subString2 = temp2.getSubString();

            temp1.crossover(subString1, subString2);
            temp2.crossover(subString2, subString1);
            System.out.println(temp1);
            System.out.println(temp2);

            output.add(temp1);
            output.add(temp2);
        }
        return output;
    }

    private List<Chromosome> reproduction(int numberOfReproduction){
        List<Chromosome> output = new ArrayList<>();
        Chromosome temp ;
        for (int counter = 0; counter< numberOfReproduction; counter++){
            temp = selectAChromosome().reproduction();
            output.add(temp);
        }
        return output;
    }

}
