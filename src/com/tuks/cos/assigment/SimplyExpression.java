package com.tuks.cos.assigment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SimplyExpression {
    private String expression;

    public SimplyExpression(String expression) {
        this.expression = expression.replace(" ","");
    }

    public String simply(){
        boolean changed;
        do {
            String newExpression =simply("("+expression+")+(0)");
            changed = (newExpression.equals(expression));
            expression=newExpression;
        }while (!changed);
        return expression;
    }

    private String simply(String input){
        //(x)+(4)
        if (input.charAt(0)!='(')
            return input;

        String lhs = removeBrackets(input);

        input = input.substring(lhs.length()+2);
        char op = input.charAt(0);

        input = input.substring(1);
        String rhs = removeBrackets(input);

        lhs = simply(lhs);
        rhs = simply(rhs);

        String output;

        if (op == '+'){
            output =simplyAddition(lhs,rhs);
        }else if (op == '-'){
            output =simplySubtraction(lhs,rhs);
        }else if (op == '*'){
            output =simplyMultiplication(lhs,rhs);
        }else{
            output =simplyDivision(lhs,rhs);
        }

        return output;
    }

    private String simplyMultiplication(String lhs, String rhs) {
        String output ="";
        boolean isLhsNum = isNumber(lhs);
        boolean isRhsNum = isNumber(rhs);
        if(isLhsNum && isRhsNum){
            output = Double.toString(Double.parseDouble(lhs)*Double.parseDouble(rhs));
            if (output.charAt(0)!='-' ){
                output = "+"+output;
            }
            return output;
        }
        else if(lhs.equals("0")||rhs.equals("0")||lhs.equals("0.0")||rhs.equals("0.0")){
            return "+0.0";
        }

        boolean isLhsOneNum = isOneNumber(lhs);
        boolean isRhsOneNum = isOneNumber(rhs);
        if(isLhsOneNum && isRhsOneNum){
            char lhsSign = lhs.charAt(0);
            if (lhsSign !='+' && lhsSign !='-'){
                lhsSign='+';
            }
            else{
                lhs = lhs.substring(1);
            }

            char rhsSign = rhs.charAt(0);
            if (rhsSign !='+' && rhsSign !='-'){
                rhsSign='+';
            }
            else{
                rhs = rhs.substring(1);
            }

            if (lhsSign == rhsSign){
                output = "+";
            }
            else{
                output = "-";
            }

            if (rhs.equals("1")||rhs.equals("1.0")){
                output += lhs;
            }
            else if (lhs.equals("1")||lhs.equals("1.0")){
                output += rhs;
            }
            else if(isLhsNum){
                String number ="";
                for (char letter:rhs.toCharArray()) {
                    if (letter =='*')
                        break;
                    number +=letter;
                }
                if (isNumber(number)){
                    double total = Double.parseDouble(number)*Double.parseDouble(lhs);
                    rhs = rhs.substring(number.length()+1);
                    output += total + "*" + rhs;
                }else {
                    output += lhs+"*"+rhs;
                }
            }
            else if(isRhsNum){
                String number ="";
                for (char letter:lhs.toCharArray()) {
                    if (letter =='*')
                        break;
                    number +=letter;
                }
                if (isNumber(number)){
                    double total = Double.parseDouble(number)*Double.parseDouble(rhs);
                    lhs = lhs.substring(number.length()+1);
                    output += total + "*" + lhs;
                }else {
                    output += rhs + "*" + lhs;
                }
            }
            else if (rhs.compareTo(lhs)<0){
                output += rhs+'*'+lhs;
            }
            else if (rhs.compareTo(lhs)>=0){
                output += lhs+'*'+rhs;
            }
        }
        else if(isLhsOneNum){
            List<String> numbers = splitString(rhs);
            for (String number: numbers) {
                int indexOfSymbol = rhs.indexOf(number);
                char temp;
                do {
                    indexOfSymbol = indexOfSymbol - 1;
                    temp = rhs.charAt(indexOfSymbol);
                } while (temp != '+' && temp != '-');
                number=temp+number;
                output+=simplyMultiplication(lhs,number);
            }
        }
        else if(isRhsOneNum){
            List<String> numbers = splitString(lhs);
            for (String number: numbers) {
                int indexOfSymbol = lhs.indexOf(number);
                char temp;
                do {
                    indexOfSymbol = indexOfSymbol - 1;
                    temp = lhs.charAt(indexOfSymbol);
                } while (temp != '+' && temp != '-');
                number=temp+number;
                output+=simplyMultiplication(number,rhs);
            }
        }
        else{
            List<String> rhsNumbers = splitString(rhs);
            List<String> lhsNumbers = splitString(lhs);
            for (String rhsNumber:rhsNumbers) {
                int indexOfSymbol = rhs.indexOf(rhsNumber);
                char temp;
                do {
                    indexOfSymbol = indexOfSymbol - 1;
                    temp = rhs.charAt(indexOfSymbol);
                } while (temp != '+' && temp != '-');
                rhsNumber=temp+rhsNumber;
                for (String lhsNumber:lhsNumbers) {
                    int indexOfSymbol2 = lhs.indexOf(lhsNumber);
                    char temp2;
                    do {
                        indexOfSymbol2 = indexOfSymbol2 - 1;
                        temp2 = lhs.charAt(indexOfSymbol2);
                    } while (temp2 != '+' && temp2 != '-');
                    lhsNumber=temp2+lhsNumber;
                    output+=simplyMultiplication(lhsNumber,rhsNumber);
                }
            }
        }

        return output;
    }

    private String simplyDivision(String lhs, String rhs) {
        String output ="";
        boolean isLhsNum = isNumber(lhs);
        boolean isRhsNum = isNumber(rhs);
        if (isLhsNum && isRhsNum && Double.parseDouble(rhs)!=0){
            output = Double.toString(Double.parseDouble(lhs)/Double.parseDouble(rhs));
            if (output.charAt(0)!='-' ){
                output = "+"+output;
            }
            return output;
        }
        else if(isLhsNum && Double.parseDouble(lhs)==0){
            return "+0.0";
        }
        else if(isRhsNum && (Double.parseDouble(rhs)==0 || Double.parseDouble(rhs)==1 || Double.parseDouble(rhs)==-1)){
            if(Double.parseDouble(rhs)==-1)
                return simplyMultiplication(lhs,"-1");
            else
                return lhs;
        }
        else if(isLhsNum && (Double.parseDouble(lhs)==1 ||Double.parseDouble(lhs)==-1)){
            return "("+lhs+")/("+rhs+")";
        }
        else if(lhs.equals(rhs)){
            return "+1.0";
        }

        boolean isLhsOneNum = isOneNumber(lhs);
        boolean isRhsOneNum = isOneNumber(rhs);
        if (isLhsOneNum && isRhsOneNum){
            char lhsSign = lhs.charAt(0);
            if (lhsSign!='-'&&lhsSign!='+'){
                lhsSign='+';
            }
            else{
                lhs=lhs.substring(1);
            }

            char rhsSign = rhs.charAt(0);
            if (rhsSign!='-'&&rhsSign!='+'){
                rhsSign='+';
            }
            else{
                rhs=rhs.substring(1);
            }

            if (lhs.equals(rhs)){
                if(lhsSign==rhsSign){
                    return "+1.0";
                }
                else{
                    return "-1.0";
                }
            }
        }
        else if(isLhsOneNum){
            char lhsSign = lhs.charAt(0);
            if (lhsSign!='-'&&lhsSign!='+'){
                lhsSign ='+';
            }
            else{
                lhs =lhs.substring(1);
            }

            char rhsSign = rhs.charAt(0);
            if (rhsSign!='-'&&rhsSign!='+'){
                rhsSign ='+';
            }
            else{
                rhs =rhs.substring(1);
            }

            return '('+lhs+")/("+rhs+')';
        }

        return '('+lhs+")/("+rhs+')';
    }

    private String simplyAddition(String lhs, String rhs) {
        String output;
        boolean isLhsNum = isNumber(lhs);
        boolean isRhsNum = isNumber(rhs);
        if(isLhsNum && isRhsNum){
            output = Double.toString(Double.parseDouble(lhs)+Double.parseDouble(rhs));
            if (output.charAt(0)!='-' ){
                output = "+"+output;
            }
            return output;
        }
        else{
            if (rhs.charAt(0)=='-'||rhs.charAt(0)=='+')
                output = lhs+rhs;
            else
                output = lhs+'+'+rhs;
            if (lhs.charAt(0)!='-'&&lhs.charAt(0)!='+')
                output = '+'+output;
        }
        List<String> numbers = splitString(output);

        double sign;
        int indexOfSymbol;
        char temp;
        HashMap<String,Double> signAndNumbers = new HashMap<>();
        for (String number: numbers){

            if(!number.equals("0")) {
                indexOfSymbol = output.indexOf(number);
                do {
                    indexOfSymbol = indexOfSymbol - 1;
                    temp = output.charAt(indexOfSymbol);
                } while (temp != '+' && temp != '-');
                sign = Integer.parseInt(output.charAt(indexOfSymbol) + "1");
                output = output.substring(1 + number.length());

                List<String> objects = Arrays.asList(number.split("\\*"));
                if (objects.size()>=2 &&isNumber(objects.get(0))){
                    sign *=Double.parseDouble(objects.get(0));
                    boolean frist=false;
                    boolean second=false;
                    for (String object: objects) {
                        if(!frist) {
                            frist=true;
                        }else if(!second){
                            number = object;
                            second=true;
                        }else {
                            number += '*' + object;
                        }
                    }
                }
                String newKey = number;

                for (String key:signAndNumbers.keySet()) {
                    if (sideEqual(key,newKey)){
                        newKey=key;
                        break;
                    }
                }
                if (newKey!=number) {
                    signAndNumbers.replace(newKey, (signAndNumbers.get(newKey) + sign));
                } else {
                    signAndNumbers.put(newKey, sign);
                }
            }
        }
        output ="";
        if (signAndNumbers.values().stream().allMatch(x->x==0)){
            return "+0.0";
        }

        double total=0;
        for (String keys: signAndNumbers.keySet()) {
            if (isNumber(keys)) {
                total +=Double.parseDouble(simplyMultiplication(keys,Double.toString(signAndNumbers.get(keys))));
            }
            else{
                output += simplyMultiplication(keys,Double.toString(signAndNumbers.get(keys)));
            }
        }

        if (total>0)
            output ="+"+total+output;
        else if (total!=0)
            output =total+output;

        return output;
    }

    private String simplySubtraction(String lhs, String rhs) {
        String output="";
        boolean isLhsNum = isNumber(lhs);
        boolean isRhsNum = isNumber(rhs);
        if(isLhsNum && isRhsNum){
            output = Double.toString(Double.parseDouble(lhs)-Double.parseDouble(rhs));
            if (output.charAt(0)!='-' ){
                output = "+"+output;
            }
            return output;
        }
        else{
            output = lhs;
            if (isOneNumber(rhs)) {
                if (rhs.charAt(0) == '-')
                    output += '+' + rhs.substring(1);
                else if (rhs.charAt(0) == '+')
                    output += '-' + rhs.substring(1);
                else
                    output += '-' + rhs;
            }else{
                List<String> numbers = splitString(rhs);
                for (String number:numbers) {
                    int indexOfSymbol = rhs.indexOf(number);
                    char temp;
                    do {
                        indexOfSymbol = indexOfSymbol - 1;
                        temp = rhs.charAt(indexOfSymbol);
                    } while (temp != '+' && temp != '-');
                    number=temp+number;
                    if (number.charAt(0) == '-')
                        output += '+' + number.substring(1);
                    else if (number.charAt(0) == '+')
                        output += '-' + number.substring(1);
                    else
                        output += '-' + number;
                }

            }
            if (lhs.charAt(0)!='-'&&lhs.charAt(0)!='+')
                output = '+'+output;
        }
        List<String> numbers = splitString(output);

        int sign;
        int indexOfSymbol;
        char temp;
        HashMap<String,Integer> signAndNumbers = new HashMap<>();
        for (String number: numbers){
            if(!number.equals("0")) {
                indexOfSymbol = output.indexOf(number);
                do {
                    indexOfSymbol = indexOfSymbol - 1;
                    temp = output.charAt(indexOfSymbol);
                } while (temp != '+' && temp != '-');
                sign = Integer.parseInt(output.charAt(indexOfSymbol) + "1");
                output = output.substring(1 + number.length());

                List<String> objects = Arrays.asList(number.split("\\*"));
                if (objects.size()>=2 &&isNumber(objects.get(0))){
                    sign *=Double.parseDouble(objects.get(0));
                    boolean frist=false;
                    boolean second=false;
                    for (String object: objects) {
                        if(!frist) {
                            frist=true;
                        }else if(!second){
                            number = object;
                            second=true;
                        }else {
                            number += '*' + object;
                        }
                    }
                }
                String newKey = number;

                for (String key:signAndNumbers.keySet()) {
                    if (sideEqual(key,newKey)){
                        newKey=key;
                        break;
                    }
                }
                if (newKey!=number) {
                    signAndNumbers.replace(newKey, (signAndNumbers.get(newKey) + sign));
                } else {
                    signAndNumbers.put(newKey, sign);
                }
            }
        }
        output ="";
        if (signAndNumbers.values().stream().allMatch(x->x==0)){
            return "+0.0";
        }

        double total=0;
        for (String keys: signAndNumbers.keySet()) {
            if (isNumber(keys)) {
                total +=Double.parseDouble(simplyMultiplication(keys,Double.toString(signAndNumbers.get(keys))));
            }
            else{
                output += simplyMultiplication(keys,Double.toString(signAndNumbers.get(keys)));
            }
        }

        if (total>0)
            output ="+"+total+output;
        else if (total!=0)
            output =total+output;

        return output;
    }

    private List<String> splitString(String output) {
        List<String> numbersWithOutPlus= Arrays.asList(output.split("\\+"));
        List<String> numbersWithOutMinus = new ArrayList<>();
        for (String number:numbersWithOutPlus) {
            numbersWithOutMinus.addAll(Arrays.asList(number.split("-")));
        }
        numbersWithOutMinus.removeIf(x->x.isEmpty());
        return numbersWithOutMinus;
    }

    private boolean isOneNumber(String pase) {
        return splitString(pase).size()==1;
    }

    private String removeBrackets(String input){
        int numberOfBrackets=0;
        char[] characters = input.toCharArray();
        String output="";
        for (char character: characters) {
            if (character =='('){
                numberOfBrackets++;
                if (numberOfBrackets!=1)
                    output+=character;
            } else if(character ==')'){
                numberOfBrackets--;
                if (numberOfBrackets!=0)
                    output+=character;
            } else{
                output+=character;
            }

            if (numberOfBrackets == 0)
                break;
        }
        return output;
    }

    private boolean isNumber(String lhs) {
        try {
            Double.parseDouble(lhs);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private boolean sideEqual(String lhs, String rhs) {
        if (lhs.equals(rhs)){
            return true;
        }else if (lhs.contains("*") && rhs.contains("*")){
            char lhsSinged = lhs.charAt(0);
            if (lhsSinged == '-' ||lhsSinged == '+'){
                lhs =lhs.substring(1);
            }else{
                lhsSinged ='+';
            }

            char rhsSinged = lhs.charAt(0);
            if (rhsSinged == '-' ||rhsSinged == '+'){
                rhs =rhs.substring(1);
            }else{
                rhsSinged ='+';
            }
            if (lhsSinged!=rhsSinged){
                return false;
            }

            List<String> lhsNumbers = Arrays.asList(lhs.split("\\*"));
            List<String> rhsNumbers = Arrays.asList(rhs.split("\\*"));
            for (String rhsNumber:rhsNumbers) {
                if (!isNumber(rhsNumber) && !lhsNumbers.contains(rhsNumber)){
                    return false;
                }
            }
            for (String lhsNumber:lhsNumbers) {
                if (!isNumber(lhsNumber) && !rhsNumbers.contains(lhsNumber)){
                    return false;
                }
            }

            return true;
        }else{
            return false;
        }
    }

    private boolean isFactor(String expression){
        List<String> number = splitString(expression);
        return true;
    }
}
