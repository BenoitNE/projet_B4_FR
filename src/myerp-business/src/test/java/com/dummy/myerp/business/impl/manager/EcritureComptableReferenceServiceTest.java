package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.service.EcritureComptableReferenceService;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Test;


public class EcritureComptableReferenceServiceTest {

    private EcritureComptableReferenceService referenceService = new EcritureComptableReferenceService();


    @Test
    public void getNewSequenceNumber() throws NotFoundException {
        Integer lastValueSequenceEcritureComptable = 33;
        Assert.assertEquals("00034", referenceService.getNewSequenceNumber(lastValueSequenceEcritureComptable));
    }

    @Test(expected = NotFoundException.class)
    public void getNewSequenceNumberError() throws NotFoundException{
        Integer lastValueSequenceEcritureComptable = 5345698;
        referenceService.getNewSequenceNumber(lastValueSequenceEcritureComptable);
    }

}
