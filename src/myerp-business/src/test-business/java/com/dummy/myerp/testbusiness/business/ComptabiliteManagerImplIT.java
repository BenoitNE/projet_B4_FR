package com.dummy.myerp.testbusiness.business;


import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.testbusiness.business.BusinessTestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ComptabiliteManagerImplIT extends BusinessTestCase  {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    @Test
    public void getListCompteComptable() {
        List<CompteComptable> compteComptableList = manager.getListCompteComptable();
        Assert.assertNotNull(compteComptableList);
    }

   /* @Test
    public void getListJournalComptable() {
        List <JournalComptable> journalComptableList = manager.getListJournalComptable();
        Assert.assertNotNull(journalComptableList);
    }*/

    @Test
    public void getListEcritureComptable(){
        List <EcritureComptable> ecritureComptableList = manager.getListEcritureComptable();
        Assert.assertNotNull(ecritureComptableList);
    }

    @Test
    public void addReferenceWhenSequenceEcritureComptableForYearNowExist () throws NotFoundException, ParseException, FunctionalException {
        try {
            manager.insertSequenceEcritureComptable(2020, 33, "VE");
        }catch (Exception e){
            manager.updateSequenceEcritureComptable(2020, 33,"VE");
        }

        /* Ecréture comptable avec une séquence d'écriture comptable qui existe pour l'année en cours */
        EcritureComptable vEcritureComptable2;
        vEcritureComptable2 = new EcritureComptable();
        vEcritureComptable2.setId(1);
        vEcritureComptable2.setReference("VE-2020/00007");
        vEcritureComptable2.setJournal(new JournalComptable("VE", "addReferenceWhenSequenceEcritureComptableForYearNowExist Test"));
        vEcritureComptable2.setDate(new Date());
        vEcritureComptable2.setLibelle("addReferenceWhenSequenceEcritureComptableForYearNowDoesntExist Test");
        vEcritureComptable2.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(1234),
                null));
        vEcritureComptable2.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(1234)));
        manager.insertEcritureComptable(vEcritureComptable2);

        EcritureComptable vEcritureComptable = manager.getEcritureComptableById(1);

        manager.addReference(vEcritureComptable);
        Assert.assertEquals("VE-2020/00034", manager.getEcritureComptableById(1).getReference());
    }

    @Test
    public void addReferenceWhenSequenceEcritureComptableForYearNowDoesntExist () throws NotFoundException, ParseException, FunctionalException {
        // Ecréture comptable avec une séquence d'écriture comptable qui n'existe pas pour l'année en cours
        String sDate = "2019-12-30 00:00:00";
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDate);

        EcritureComptable vEcritureComptable1;
        vEcritureComptable1 = new EcritureComptable();
        vEcritureComptable1.setId(1);
        vEcritureComptable1.setReference("AC-2019/00007");
        vEcritureComptable1.setJournal(new JournalComptable("AC", "addReferenceWhenSequenceEcritureComptableForYearNowDoesntExist Test"));
        vEcritureComptable1.setDate(date);
        vEcritureComptable1.setLibelle("addReferenceWhenSequenceEcritureComptableForYearNowDoesntExist Test");
        vEcritureComptable1.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(1234),
                null));
        vEcritureComptable1.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(1234)));
        manager.insertEcritureComptable(vEcritureComptable1);

        EcritureComptable vEcritureComptable = manager.getEcritureComptableById(1);

        manager.addReference(vEcritureComptable);
        Assert.assertEquals("AC-2020/00001", manager.getEcritureComptableById(1).getReference());
        manager.deleteSequenceEcritureComptable(2020, "AC");
    }

    @Test
    public void updateEcritureComptable () throws NotFoundException, FunctionalException {
        EcritureComptable vEcritureComptable = manager.getEcritureComptableById(-1);
        vEcritureComptable.setLibelle("new libelle");
        manager.updateEcritureComptable(vEcritureComptable);
        Assert.assertEquals("new libelle", vEcritureComptable.getLibelle());
    }

    @Test
    public void deleteEcritureComptable() throws NotFoundException, ParseException, FunctionalException {
        /* Ecriture comptable pour tester la suppresssion */
        EcritureComptable vEcritureComptable3;
        vEcritureComptable3 = new EcritureComptable();
        vEcritureComptable3.setId(1);
        vEcritureComptable3.setReference("OD-2020/00023");
        vEcritureComptable3.setJournal(new JournalComptable("OD", "addReferenceWhenSequenceEcritureComptableForYearNowExist Test"));
        vEcritureComptable3.setDate(new Date());
        vEcritureComptable3.setLibelle("Ecriture comptable à supprimer Test");
        vEcritureComptable3.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(1234),
                null));
        vEcritureComptable3.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(1234)));
        manager.insertEcritureComptable(vEcritureComptable3);

        List<EcritureComptable> ecritureComptableList = manager.getListEcritureComptable();
        for (EcritureComptable ecritureComptable : ecritureComptableList) {
            Assert.assertNotEquals(java.util.Optional.of(1), ecritureComptable.getId());
        }
    }
    @Test
    public void getEcritureComptableById () throws NotFoundException {
        EcritureComptable vEcritureComptable = manager.getEcritureComptableById(-2);
        Assert.assertEquals("TMA Appli Xxx",vEcritureComptable.getLibelle());
    }
    @Test(expected = FunctionalException.class)
    public void addReferenceExeption() throws FunctionalException, ParseException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(1);
        vEcritureComptable.setJournal(new JournalComptable("KZ", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                new BigDecimal(123)));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                new BigDecimal(123)));
        manager.addReference(vEcritureComptable);
    }
    @Test
    public void checkEcritureComptableContext() throws ParseException, FunctionalException {
        EcritureComptable vEcritureComptable3;
        vEcritureComptable3 = new EcritureComptable();
        vEcritureComptable3.setId(1);
        vEcritureComptable3.setReference("OD-2020/00023");
        vEcritureComptable3.setJournal(new JournalComptable("OD", "addReferenceWhenSequenceEcritureComptableForYearNowExist Test"));
        vEcritureComptable3.setDate(new Date());
        vEcritureComptable3.setLibelle("Ecriture comptable à supprimer Test");
        vEcritureComptable3.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(1234),
                null));
        vEcritureComptable3.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(1234)));
        manager.insertEcritureComptable(vEcritureComptable3);

        manager.checkEcritureComptableContext(vEcritureComptable3);

    }

    @Test
    public void checkEcritureComptable() throws ParseException, FunctionalException {
        EcritureComptable vEcritureComptable3;
        vEcritureComptable3 = new EcritureComptable();
        vEcritureComptable3.setId(1);
        vEcritureComptable3.setReference("OD-2020/00012");
        vEcritureComptable3.setJournal(new JournalComptable("OD", "addReferenceWhenSequenceEcritureComptableForYearNowExist Test"));
        vEcritureComptable3.setDate(new Date());
        vEcritureComptable3.setLibelle("Ecriture comptable à supprimer Test");
        vEcritureComptable3.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(1234),
                null));
        vEcritureComptable3.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(1234)));
        manager.insertEcritureComptable(vEcritureComptable3);
        manager.checkEcritureComptable(vEcritureComptable3);
    }

    @After
    public void deleteEcritureComptableId1() {
        manager.deleteEcritureComptable(1);
    }
}
