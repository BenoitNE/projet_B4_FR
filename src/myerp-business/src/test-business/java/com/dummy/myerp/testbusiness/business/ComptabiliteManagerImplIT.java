package com.dummy.myerp.testbusiness.business;


import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.testbusiness.business.BusinessTestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ComptabiliteManagerImplIT extends BusinessTestCase  {

    ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    private EcritureComptable ecritureComptable1 () {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(1);
        vEcritureComptable.setReference("VE-2021/00007");
        vEcritureComptable.setJournal(new JournalComptable("VE", "journal test"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("libelle test");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(1234),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(1234)));
        return vEcritureComptable;
    }

    @Before
    public void insertEcritureComptable() throws ParseException, FunctionalException {
        manager.insertEcritureComptable(ecritureComptable1());
    }

    @Test
    public void getListCompteComptable() {
        List<CompteComptable> compteComptableList = manager.getListCompteComptable();
        Assert.assertNotNull(compteComptableList);
    }

    @Test
    public void getListEcritureComptable(){
        List <EcritureComptable> ecritureComptableList = manager.getListEcritureComptable();
        Assert.assertNotNull(ecritureComptableList);
    }

    @Test
    public void addReferenceWhenSequenceEcritureComptableForYearNowExist () throws NotFoundException, ParseException, FunctionalException {
        manager.insertSequenceEcritureComptable(2021, 33, "VE");
        EcritureComptable vEcritureComptable = manager.getEcritureComptableById(1);
        manager.addReference(vEcritureComptable);
        Assert.assertEquals("VE-2021/00034", manager.getEcritureComptableById(1).getReference());
    }


    @Test
    public void addReferenceWhenSequenceEcritureComptableForYearNowDoesntExist () throws NotFoundException, ParseException, FunctionalException {
        EcritureComptable vEcritureComptable = manager.getEcritureComptableById(1);
        vEcritureComptable.setReference("VE-2019/00089");
        manager.addReference(vEcritureComptable);
        Assert.assertEquals("VE-2021/00001", vEcritureComptable.getReference());
    }

    @Test
    public void updateEcritureComptable () throws NotFoundException, FunctionalException {
        EcritureComptable vEcritureComptable = manager.getEcritureComptableById(1);
        vEcritureComptable.setLibelle("new libelle");
        manager.updateEcritureComptable(vEcritureComptable);
        Assert.assertEquals("new libelle", vEcritureComptable.getLibelle());
    }

    @Test(expected = FunctionalException.class)
    public void addReferenceExeption() throws FunctionalException, ParseException {
        EcritureComptable vEcritureComptable = null;
        manager.addReference(vEcritureComptable);
    }

    @Test
    public void deleteEcritureComptable() throws NotFoundException, ParseException, FunctionalException {
        manager.deleteEcritureComptable(1);
        List<EcritureComptable> ecritureComptableList = manager.getListEcritureComptable();
        for (EcritureComptable ecritureComptable : ecritureComptableList) {
            Assert.assertNotEquals(java.util.Optional.of(1), ecritureComptable.getId());
        }
    }

    @Test
    public void getEcritureComptableById () throws NotFoundException {
        EcritureComptable vEcritureComptable = manager.getEcritureComptableById(1);
        Assert.assertEquals("libelle test",vEcritureComptable.getLibelle());
    }

    @Test
    public void checkEcritureComptableContext() throws ParseException, FunctionalException, NotFoundException {
        EcritureComptable vEcritureComptable = manager.getEcritureComptableById(1);
        Assert.assertTrue(manager.checkEcritureComptableContext(vEcritureComptable));
    }

    @After
    public void deleteEcritureComptableId1() {
        manager.deleteEcritureComptable(1);
        manager.deleteSequenceEcritureComptable(2021,"VE");
    }
}
