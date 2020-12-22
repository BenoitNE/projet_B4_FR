package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.service.EcritureComptableService;
import com.dummy.myerp.model.bean.comptabilite.*;

import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Set;


/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================


    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }

   /**
    * Instanciation de la classe Service
    */
    EcritureComptableService ecritureComptableService = new EcritureComptableService();

    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }

    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) throws FunctionalException {

            try {
                addReferenceWhenSequenceEcritureComptableForYearNowExist(pEcritureComptable);

            } catch (Exception e1) {
                try {
                addReferenceWhenSequenceEcritureComptableForYearNowDoesNotExist(pEcritureComptable);
                } catch (Exception e2){
                    throw new FunctionalException("La référence n'a pas pu être générée");
            }

        }
    }

    private void addReferenceWhenSequenceEcritureComptableForYearNowDoesNotExist(EcritureComptable pEcritureComptable) {
        // 2. S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
        //    2.1. Utiliser le numéro 1
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
        String referenceEcritureComptable = pEcritureComptable.getJournal().getCode() + "-" + ecritureComptableService.getYearNow() + "/00001";

        // 4.  Enregistrer (insert) la valeur de la séquence en persitance
        getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(ecritureComptableService.getYearNow(),
                1, pEcritureComptable.getJournal().getCode());

        // 3.  Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5)
        pEcritureComptable.setReference(referenceEcritureComptable);
        getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
    }

    private void addReferenceWhenSequenceEcritureComptableForYearNowExist(EcritureComptable pEcritureComptable) throws NotFoundException {
        // 1. Remonter depuis la persitance la dernière valeur de la séquence du journal pour l'année de l'écriture
        //    (table sequence_ecriture_comptable)
        SequenceEcritureComptable sequenceEcritureComptable = getDaoProxy().getComptabiliteDao()
                .getLastValueSequenceEcritureComptableForYear(
                pEcritureComptable.getJournal().getCode(), ecritureComptableService.getYearNow());

        //    2.2. Utiliser la dernière valeur + 1
        // 4.  Enregistrer (update) la valeur de la séquence en persitance
        getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(sequenceEcritureComptable.getAnnee(),
            sequenceEcritureComptable.getDerniereValeur()+1, pEcritureComptable.getJournal().getCode());

        String referenceEcritureComptable = pEcritureComptable.getJournal().getCode() + "-" + ecritureComptableService.getYearNow() + "/" +
            ecritureComptableService.getNewSequenceNumber(sequenceEcritureComptable.getDerniereValeur());

        // 3.  Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5)
        pEcritureComptable.setReference(referenceEcritureComptable);
        getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException, ParseException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */

    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException, ParseException {


        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            throw new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.",
                                          new ConstraintViolationException(
                                              "L'écriture comptable ne respecte pas les contraintes de validation",
                                              vViolations));
        }

        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
        }

        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        //      avec un montant au débit et un montant au crédit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2
            || vNbrCredit < 1
            || vNbrDebit < 1) {
            throw new FunctionalException(
                "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
        }

        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
        if(!pEcritureComptable.getJournal().getCode()
                .equals(ecritureComptableService.getJournalCodeByReference(pEcritureComptable.getReference()))){
            throw new FunctionalException("Le code journal ne correspond pas à celui de la reférence.");
        }

        if(ecritureComptableService.getYearFromDate(pEcritureComptable.getDate())!=(ecritureComptableService.getDateByReference(pEcritureComptable.getReference()))){
            throw new FunctionalException("L'année dans la référence ne correspond pas à celle de l'écriture.");
        }
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                    pEcritureComptable.getReference());

                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                    || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
                }
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException, ParseException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * @param id
     * @return
     * @throws NotFoundException
     */
    public EcritureComptable getEcritureComptableById(Integer id) throws NotFoundException {
        return getDaoProxy().getComptabiliteDao().getEcritureComptable(id);
    }

    /**
     *
     * @param annee
     * @param derniereValeur
     * @param journalCode
     */
    public void insertSequenceEcritureComptable(int annee, int derniereValeur, String journalCode){
        getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(annee, derniereValeur, journalCode);
    }

    /**
     *
     * @param annee
     * @param derniereValeur
     * @param journalCode
     */
    public void updateSequenceEcritureComptable(int annee, int derniereValeur, String journalCode){
        getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(annee, derniereValeur, journalCode);
    }

    /**
     *
     * @param annee
     * @param journalCode
     */
    public void deleteSequenceEcritureComptable(int annee, String journalCode){
        getDaoProxy().getComptabiliteDao().deleteSequenceEcritureComptable(annee,journalCode);
    }


}
