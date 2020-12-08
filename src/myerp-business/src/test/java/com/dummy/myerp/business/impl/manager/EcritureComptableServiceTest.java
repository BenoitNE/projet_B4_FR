package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.service.EcritureComptableService;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;


public class EcritureComptableServiceTest {

    private EcritureComptableService referenceService = new EcritureComptableService();


    @Test
    public void getNewSequenceNumber() throws NotFoundException {
        Integer lastValueSequenceEcritureComptable = 33;
        Assert.assertEquals("00034", referenceService.getNewSequenceNumber(lastValueSequenceEcritureComptable));
    }

    @Test(expected = NotFoundException.class)
    public void getNewSequenceNumberException() throws NotFoundException{
        Integer lastValueSequenceEcritureComptable = 5345698;
        referenceService.getNewSequenceNumber(lastValueSequenceEcritureComptable);
    }

    @Test
    public void getJournalCodeByReference(){
        Assert.assertEquals("AC", referenceService.getJournalCodeByReference("AC-2020/00031"));
    }

    @Test
    public void  getDateByReference() throws ParseException {
        Assert.assertEquals("2020", referenceService.getDateByReference("AC-2020/00031"));
    }

}
