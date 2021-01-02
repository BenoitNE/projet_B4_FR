package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import com.dummy.myerp.business.impl.connection.AbstractBusinessManager;
import com.dummy.myerp.business.impl.connection.TransactionManager;
import com.dummy.myerp.business.service.EcritureComptableService;
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

    /**
     * Instanciation de la classe Service
     */
    EcritureComptableService ecritureComptableService = new EcritureComptableService();

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


    private EcritureComptable ecritureComptable1 () {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(7);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2021/00122");
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));
        return vEcritureComptable;
    }

    private EcritureComptable ecritureComptable2 (){
            EcritureComptable vEcritureComptable;
            vEcritureComptable = new EcritureComptable();
            vEcritureComptable.setId(8);
            vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
            vEcritureComptable.setDate(new Date());
            vEcritureComptable.setLibelle("Libelle");
            vEcritureComptable.setReference("AC-2021/00033");
            vEcritureComptable.getListLigneEcriture()
                    .add(new LigneEcritureComptable(new CompteComptable(3), null, new BigDecimal(123), null));
            vEcritureComptable.getListLigneEcriture()
                    .add(new LigneEcritureComptable(new CompteComptable(4), null, null, new BigDecimal(123)));
            return vEcritureComptable;
        }

    private EcritureComptable ecritureComptable3 (){
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(9);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2021/00122");
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        return vEcritureComptable;
    }



    /*-------------------------- checkEcritureComptable Test --------------------------------*/

    @Test
    public void checkEcritureComptable() throws ParseException, FunctionalException, NotFoundException {
        EcritureComptable vEcritureComptable1 = ecritureComptable1();
            when (this.comptabiliteDaoMock.getEcritureComptableByRef(vEcritureComptable1.getReference()))
                   .thenReturn(vEcritureComptable1);
        Assert.assertTrue(manager.checkEcritureComptable(vEcritureComptable1));
    }

    @Test
    public void checkEcritureComptableUnit() throws FunctionalException, ParseException {
        EcritureComptable vEcritureComptable = ecritureComptable1();
        Assert.assertTrue(manager.checkEcritureComptableUnit(vEcritureComptable));
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable = ecritureComptable1();
        vEcritureComptable.setReference("AC-2021/33");// Violation de regexp = "\\w{1,5}-\\d{4}/\\d{5}"
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable = ecritureComptable1();
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null,
                        new BigDecimal(123456), null)); // Ligne d'écriture comptable déséquilirer;
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable = ecritureComptable3();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG5CodeJournal() throws FunctionalException, ParseException {
        EcritureComptable vEcritureComptable = ecritureComptable1();
        vEcritureComptable.setJournal(new JournalComptable("BE", "Achat"));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG5Date() throws FunctionalException, ParseException {
        EcritureComptable vEcritureComptable = ecritureComptable1();
        vEcritureComptable.setReference("AC-2007/00023");
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test
    public void checkEcritureComptableContext() throws NotFoundException, FunctionalException {
        EcritureComptable vEcritureComptable1 = ecritureComptable1();
        when(this.comptabiliteDaoMock.getEcritureComptableByRef(vEcritureComptable1.getReference()))
                .thenReturn(vEcritureComptable1);
        Assert.assertTrue(manager.checkEcritureComptableContext(vEcritureComptable1));
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableContextRG6() throws NotFoundException, FunctionalException, ParseException {
        EcritureComptable vEcritureComptable1 = ecritureComptable1();
        EcritureComptable vEcritureComptable2 = ecritureComptable2();
        vEcritureComptable2.setReference("AC-2021/00122"); // Référence identique à ecritureComptable1
        when(this.comptabiliteDaoMock.getEcritureComptableByRef(vEcritureComptable1.getReference()))
                .thenReturn(vEcritureComptable2);
        manager.checkEcritureComptableContext(vEcritureComptable1);

    }
    /*-------------------------- addReference Test --------------------------------*/
    @Test
    public void addReferenceWhenSequenceEcritureComptableForYearNowDoesntExist () throws NotFoundException, FunctionalException {
        EcritureComptable vEcritureComptable = ecritureComptable1();
        try {
            when (this.comptabiliteDaoMock.getLastValueSequenceEcritureComptableForYear(vEcritureComptable.getJournal().getCode(),
                    ecritureComptableService.getYearNow()))
                    .thenReturn(null);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        manager.addReference(vEcritureComptable);
        Assert.assertEquals("AC-2021/00001",vEcritureComptable.getReference());
    }

    @Test
    public void addReferenceWhenSequenceEcritureComptableForYearNowExist () throws NotFoundException, FunctionalException {
        EcritureComptable vEcritureComptable = ecritureComptable1();
        SequenceEcritureComptable vSequenceEcritureComptable = new SequenceEcritureComptable();
        vSequenceEcritureComptable.setJournalCode("AC");
        vSequenceEcritureComptable.setAnnee(2021);
        vSequenceEcritureComptable.setDerniereValeur(89);
        try {
            when (this.comptabiliteDaoMock.getLastValueSequenceEcritureComptableForYear(vEcritureComptable.getJournal().getCode(),
                    ecritureComptableService.getYearNow())).thenReturn(vSequenceEcritureComptable);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        manager.addReference(vEcritureComptable);
        Assert.assertEquals("AC-2021/00090",vEcritureComptable.getReference());
    }

    @Test(expected = FunctionalException.class)
    public void addReferenceWhenSequenceEcritureComptableException () throws FunctionalException {
        EcritureComptable vEcritureComptable = null;
        manager.addReference(vEcritureComptable);
    }



}
