package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.testbusiness.business.BusinessTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ComptabiliteManagerImplIT extends BusinessTestCase {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    @Test
    public void checkGetListCompteComptable() {
        List<CompteComptable> compteComptableList = manager.getListCompteComptable();
        Assert.assertNotNull(compteComptableList);
    }
}
