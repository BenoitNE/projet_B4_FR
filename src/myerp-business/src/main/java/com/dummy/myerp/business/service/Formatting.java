package com.dummy.myerp.business.service;

import java.util.Arrays;

public class Formatting {

    public String getSequenceNumber (String lastValueSequenceEcritureComptable){
        String [] sequenceNumber = {"0","0","0","0","0"};

        for (int i=0; i < lastValueSequenceEcritureComptable.length(); i++){
            if (i == 0) {
                sequenceNumber[sequenceNumber.length-1] = String.valueOf(lastValueSequenceEcritureComptable
                        .charAt(lastValueSequenceEcritureComptable.length()-1));
            }
            else {
                sequenceNumber[sequenceNumber.length-1-i] = String.valueOf(lastValueSequenceEcritureComptable
                        .charAt(lastValueSequenceEcritureComptable.length()-1-i));
            }
        }

        return (Arrays.toString(sequenceNumber)).replace("[","")
                .replace("]","")
                .replace(",","")
                .replace(" ","");
    }

    

    public static void main(String[] args) {

        String code = "46";
        Formatting formatting= new Formatting();

        System.out.println("le code est "+formatting.getSequenceNumber(code));

    }

}
