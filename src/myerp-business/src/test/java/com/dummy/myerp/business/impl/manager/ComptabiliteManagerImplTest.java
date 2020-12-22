package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
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

    /*-------------------------- checkEcritureComptable Test --------------------------------*/

    @Test
    public void checkEcritureComptable() throws ParseException, FunctionalException, NotFoundException {
        EcritureComptable vEcritureComptable1;
        vEcritureComptable1 = new EcritureComptable();
        vEcritureComptable1.setId(7);
        vEcritureComptable1.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable1.setDate(new Date());
        vEcritureComptable1.setLibelle("Libelle");
        vEcritureComptable1.setReference("AC-2020/00122");
        vEcritureComptable1.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable1.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));

        EcritureComptable vEcritureComptable2;
        vEcritureComptable2 = new EcritureComptable();
        vEcritureComptable2.setId(7);
        vEcritureComptable2.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable2.setDate(new Date());
        vEcritureComptable2.setLibelle("Libelle");
        vEcritureComptable2.setReference("AC-2020/00033");
        vEcritureComptable2.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(3), null, new BigDecimal(123), null));
        vEcritureComptable2.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(4), null, null, new BigDecimal(123)));

            when (this.comptabiliteDaoMock.getEcritureComptableByRef(vEcritureComptable1.getReference()))
                   .thenReturn(vEcritureComptable2);

        manager.checkEcritureComptable(vEcritureComptable1);

    }

    @Test
    public void checkEcritureComptableUnit() throws FunctionalException, ParseException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
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

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG5CodeJournal() throws FunctionalException, ParseException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("KG-2020/00023");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG5Date() throws FunctionalException, ParseException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2003/00023");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test
    public void checkEcritureComptableContextRG6() throws NotFoundException, FunctionalException, ParseException {
        EcritureComptable vEcritureComptable1;
        vEcritureComptable1 = new EcritureComptable();
        vEcritureComptable1.setId(7);
        vEcritureComptable1.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable1.setDate(new Date());
        vEcritureComptable1.setLibelle("Libelle");
        vEcritureComptable1.setReference("AC-2020/00122");
        vEcritureComptable1.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable1.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));

        EcritureComptable vEcritureComptable2;
        vEcritureComptable2 = new EcritureComptable();
        vEcritureComptable2.setId(7);
        vEcritureComptable2.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable2.setDate(new Date());
        vEcritureComptable2.setLibelle("Libelle");
        vEcritureComptable2.setReference("AC-2020/00033");
        vEcritureComptable2.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable2.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));

        when(this.comptabiliteDaoMock.getEcritureComptableByRef(vEcritureComptable1.getReference()))
                .thenReturn(vEcritureComptable2);

        manager.checkEcritureComptableContext(vEcritureComptable1);

    }
    /*-------------------------- addReference Test --------------------------------*/

    @Test
    public void addReferenceWhenSequenceEcritureComptableForYearNowDoesntExist () throws NotFoundException, FunctionalException {
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
            when (this.comptabiliteDaoMock.getLastValueSequenceEcritureComptableForYear("AC", 2018))
                    .thenReturn(vSequenceEcritureComptable);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        manager.addReference(vEcritureComptable);
        Assert.assertEquals("AC-2020/00001",vEcritureComptable.getReference());

    }

    @Test
    public void addReferenceWhenSequenceEcritureComptableForYearNowExist () throws NotFoundException, FunctionalException {
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



}
