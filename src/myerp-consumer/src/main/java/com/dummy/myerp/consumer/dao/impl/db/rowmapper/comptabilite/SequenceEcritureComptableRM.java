package com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite;

import org.springframework.jdbc.core.RowMapper;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link RowMapper} de {@link SequenceEcritureComptable}
 */
public class SequenceEcritureComptableRM implements RowMapper<SequenceEcritureComptable> {

    @Override
    public SequenceEcritureComptable mapRow(ResultSet resultSet, int i) throws SQLException {
        SequenceEcritureComptable vBean = new SequenceEcritureComptable();
        vBean.setAnnee(resultSet.getInt("annee"));
        vBean.setDerniereValeur(resultSet.getInt("derniere_valeur"));
        vBean.setJournalCode(resultSet.getString("journal_code"));
        return vBean;
    }
}
