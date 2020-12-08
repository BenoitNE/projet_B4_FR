package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;


public class ComptabiliteManagerImplTest {


    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    @Mock
    private ComptabiliteDao comptabiliteDaoMock;

    @Mock
    private DaoProxy daoProxyMock;

    @Mock
    private TransactionManager transactionManagerMock;

 /*    Initialisation et configuration de Mockito pour la mise en place des tests*/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(this.daoProxyMock.getComptabiliteDao()).thenReturn(this.comptabiliteDaoMock);
        AbstractBusinessManager.configure(null, this.daoProxyMock, this.transactionManagerMock);
    }

    @Test
    public void checkEcritureComptableUnit() throws FunctionalException, ParseException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new SimpleDateFormat("yyyy").parse("2020"));
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2020/00124");
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    /*-------------------------- addReference Test --------------------------------*/

    @Test
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

        SequenceEcritureComptable vSequenceEcritureComptable = new SequenceEcritureComptable();
        vSequenceEcritureComptable.setJournalCode("AC");
        vSequenceEcritureComptable.setAnnee(2019);
        vSequenceEcritureComptable.setDerniereValeur(89);

        try {
            when (this.comptabiliteDaoMock.getLastValueSequenceEcritureComptableForYear("AC", 2019))
                    .thenReturn(vSequenceEcritureComptable);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        manager.addReference(vEcritureComptable);

        Assert.assertEquals("AC-2020/00001",vEcritureComptable.getReference());

    }

    @Test
    public void addReferenceWhenSequenceEcritureComptableForYearNowExist () throws NotFoundException {
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

        SequenceEcritureComptable vSequenceEcritureComptable = new SequenceEcritureComptable();
        vSequenceEcritureComptable.setJournalCode("AC");
        vSequenceEcritureComptable.setAnnee(2020);
        vSequenceEcritureComptable.setDerniereValeur(89);

        try {
            when (this.comptabiliteDaoMock.getLastValueSequenceEcritureComptableForYear("AC", 2020))
                    .thenReturn(vSequenceEcritureComptable);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        manager.addReference(vEcritureComptable);

        Assert.assertEquals("AC-2020/00090",vEcritureComptable.getReference());

    }

    @Test (expected = NotFoundException.class)
    public void addReferenceException() throws NotFoundException {
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

        SequenceEcritureComptable vSequenceEcritureComptable = new SequenceEcritureComptable();
        vSequenceEcritureComptable.setJournalCode("AC");
        vSequenceEcritureComptable.setAnnee(2020);
        vSequenceEcritureComptable.setDerniereValeur(895667546);

        try {
            when (this.comptabiliteDaoMock.getLastValueSequenceEcritureComptableForYear("AC", 2020))
                    .thenReturn(vSequenceEcritureComptable);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        manager.addReference(vEcritureComptable);
        System.out.println(vEcritureComptable.getReference());
    }



}
