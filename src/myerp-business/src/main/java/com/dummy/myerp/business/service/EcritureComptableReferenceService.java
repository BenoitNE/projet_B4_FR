package com.dummy.myerp.business.service;

import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;

public class EcritureComptableReferenceService {
    public Integer getYearNow (){
        LocalDate now = LocalDate.now();
        Integer year = Integer.valueOf(now.getYear());

        return year;
    }

    public String getNewSequenceNumber (Integer lastValueSequenceEcritureComptable) throws NotFoundException {
        String[] sequenceNumber = {"0", "0", "0", "0", "0"};
        lastValueSequenceEcritureComptable = lastValueSequenceEcritureComptable + 1;
        String lastValueSequence = String.valueOf(lastValueSequenceEcritureComptable);
        try {
            for (int i = 0; i < lastValueSequence.length(); i++) {
                if (i == 0) {
                    sequenceNumber[sequenceNumber.length - 1] = String.valueOf(lastValueSequence
                            .charAt(lastValueSequence.length() - 1));
                } else {
                    sequenceNumber[sequenceNumber.length - 1 - i] = String.valueOf(lastValueSequence
                            .charAt(lastValueSequence.length() - 1 - i));
                }
            }
            return (Arrays.toString(sequenceNumber)).replace("[", "")
                    .replace("]", "")
                    .replace(",", "")
                    .replace(" ", "");
        }catch (Exception e) {
            throw new NotFoundException("La séquence n'a pas pu être générée");
        }
    }
}
