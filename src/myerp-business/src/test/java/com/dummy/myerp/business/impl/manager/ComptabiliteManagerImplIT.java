package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.testbusiness.business.BusinessTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class ComptabiliteManagerImplIT extends BusinessTestCase {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    @Test
    public void getListCompteComptable() {
        List<CompteComptable> compteComptableList = manager.getListCompteComptable();
        Assert.assertNotNull(compteComptableList);
    }

    @Test
    public void getListJournalComptable() {
        List <JournalComptable> journalComptableList = manager.getListJournalComptable();
        Assert.assertNotNull(journalComptableList);
    }

    @Test
    public void getListEcritureComptable(){
        List <EcritureComptable> ecritureComptableList = manager.getListEcritureComptable();
        Assert.assertNotNull(ecritureComptableList);
    }

   /* @Test
    public void addReferenceWhenSequenceEcritureComptableForYearNowDoesntExist () throws NotFoundException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(1234)));

        manager.addReference(vEcritureComptable);
        Assert.assertEquals("AC-2020/00001", vEcritureComptable.getReference());
    }*/

    @Test
    public void insertEcritureComptable () throws ParseException, FunctionalException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("VE", "Vente"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Test");
        vEcritureComptable.setReference("VE-2020/00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(1234),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(1234)));

        manager.insertEcritureComptable(vEcritureComptable);
    }

    @Test
    public void updateEcritureComptable (){
        EcritureComptable vEcritureComptable = new EcritureComptable();
    }

    @Test
    public void getLastValueSequenceEcritureComptableForYear () throws NotFoundException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(1234)));

        Assert.assertEquals(40, manager.getLastValueSequenceEcritureComptableForYear(vEcritureComptable));
    }

    @Test
    public void getEcritureComptableById () throws NotFoundException {
        EcritureComptable vEcritureComptable = manager.getEcritureComptableById(-2);
        Assert.assertEquals("TMA Appli Xxx",vEcritureComptable.getLibelle());
    }


}
