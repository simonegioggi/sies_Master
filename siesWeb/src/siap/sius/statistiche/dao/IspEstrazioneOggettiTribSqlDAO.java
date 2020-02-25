package siap.sius.statistiche.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sius.esperto.model.EspertoModel;
import siap.sius.statistiche.model.IspEstrazioneOggettiModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

public class IspEstrazioneOggettiTribSqlDAO extends SqlDAO {
    public IspEstrazioneOggettiTribSqlDAO (Connection con)
    {
      super(con);
    }

    //
    // METODO RICERCA()
    //

    public void ricercaOggettiPriviDiOccorrenza(String aTipoUfficio, String aCodMagistrato, String aFasSiusChiaveUfficio) throws DAOException
    {
      String lSql = new String("");
      lSql  = "SELECT " +
              "COD_OGGETTO, " +
              "NULL AS GEN_PRID_GENERALE_PROCEDIMENTO, " + 
              "NULL AS FAS_SIU_DATA_DEFINIZIONE, " + 
              "NULL AS TEN_DATA_FINE, " + 
              "NULL AS COD_ESITO_STATISTICA, " + 
              "NULL AS COD_ESITO_TENORE, " + 
              "NULL AS FAS_SIU_ID_FASCICOLO_SIUS, " + 
              "NULL AS COD_MAGISTRATO,DEFINITO, " + 
              "NULL AS DEP_OPID_DEPOSITO_ORDINANZA_PC, " + 
              "NULL AS COD_OGGETTO_PROCEDIMENTO, " + 
              "NULL AS FAS_SIU_CHIAVE_PROGR, " + 
              "NULL AS FAS_SIU_CHIAVE_UFFICIO, " + 
              "NULL AS FAS_SIU_COD_STATO_FASCICOLO, " + 
              "NULL AS TEN_DATA_INS, " + 
              "NULL AS DATA_DEPOSITO, " + 
              "NULL AS TEN_DATA, " + 
              "NULL AS FAS_SIU_CHIAVE_ANNO, " + 
              "NULL AS DEP_DEC_ID_DEPOSITO_DECRETO, " + 
              "NULL AS FAS_SIU_DATA_ISCRIZIONE " + 
              "FROM " +
              "ISP_MOTIVO_OGGETTO " +
              "AND  " + 
              "COD_MOTIVO NOT IN  " + 
              "( " + 
                  "SELECT " + 
                  "COD_OGGETTO_TENORE " + 
                  "FROM " + 
                  "ISP_ESTRAZIONE_OGGETTI_TRIB  " + 
                  "WHERE "; 
      if (aCodMagistrato == null) {
          lSql += " COD_MAGISTRATO IS NULL ";
      } else {
          lSql += " COD_MAGISTRATO = '" + aCodMagistrato + "' ";
      }
      lSql +=     "AND FAS_SIUS_CHIAVE_UFFICIO = '" + aFasSiusChiaveUfficio + "' ";
      lSql +=     "AND TIPO_UFFICIO = '" + aTipoUfficio + "' ";
      lSql += ")";

      setStatement(lSql);
    }
/*
    public void ricercaOggettiStatisticaMagistrati(String aCodMagistrato, String aFasSiusChiaveUfficio, Vector<String> aCodOggetti) throws DAOException
    {
      String lSql = new String("");
      lSql  = "SELECT " +
              "COD_OGGETTO, " +
              "FAS_SIU_DATA_DEFINIZIONE, " + 
              "NULL AS TEN_DATA_FINE, " + 
              "NULL AS COD_ESITO_STATISTICA, " + 
              "NULL AS COD_ESITO_TENORE, " + 
              "NULL AS FAS_SIU_ID_FASCICOLO_SIUS, " + 
              "NULL AS COD_MAGISTRATO,DEFINITO, " + 
              "NULL AS DEP_OPID_DEPOSITO_ORDINANZA_PC, " + 
              "NULL AS COD_OGGETTO_PROCEDIMENTO, " + 
              "NULL AS FAS_SIU_CHIAVE_PROGR, " + 
              "NULL AS FAS_SIU_CHIAVE_UFFICIO, " + 
              "NULL AS FAS_SIU_COD_STATO_FASCICOLO, " + 
              "NULL AS TEN_DATA_INS, " + 
              "NULL AS DATA_DEPOSITO, " + 
              "NULL AS TEN_DATA, " + 
              "NULL AS FAS_SIU_CHIAVE_ANNO, " + 
              "NULL AS DEP_DEC_ID_DEPOSITO_DECRETO, " + 
              "NULL AS FAS_SIU_DATA_ISCRIZIONE " + 
              "FROM " +
              "ISP_MOTIVO_OGGETTO " +
              "AND  " + 
              "COD_MOTIVO NOT IN  " + 
              "( " + 
                  "SELECT " + 
                  "COD_OGGETTO_TENORE " + 
                  "FROM " + 
                  "ISP_ESTRAZIONE_OGGETTI_TRIB  " + 
                  "WHERE "; 
      if (aCodMagistrato == null) {
          lSql += " COD_MAGISTRATO IS NULL ";
      } else {
          lSql += " COD_MAGISTRATO = '" + aCodMagistrato + "' ";
      }
      lSql +=     "AND FAS_SIUS_CHIAVE_UFFICIO = '" + aFasSiusChiaveUfficio + "' ";
      lSql +=     "AND TIPO_UFFICIO = '" + aTipoUfficio + "' ";
      lSql += ")";

      setStatement(lSql);
    }
*/
    protected String getSqlQuery()
    {
      String lStatement = new String("");
     
      return lStatement;
    }


    //
    // METODO GETMODEL()
    //
    public GenericModel getModel() throws DAOException
    {
        IspEstrazioneOggettiModel aModel = new  IspEstrazioneOggettiModel();

        //Popola il Model con i valori della tabella

        // In questo MODEL il valore del COD_OGGETTO rimpiazza il valore di COD_OGGETTO_TENORE
        aModel.setCodOggettoTenore(getString("COD_OGGETTO"));                               
        aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS")); 
        aModel.setFasSiuChiaveAnno(getBigDecimal("FAS_SIU_CHIAVE_ANNO")); 
        aModel.setFasSiuChiaveUfficio(getString("FAS_SIU_CHIAVE_UFFICIO")); 
        aModel.setFasSiuChiaveProgr(getBigDecimal("FAS_SIU_CHIAVE_PROGR")); 
        aModel.setFasSiuCodStatoFascicolo(getString("FAS_SIU_COD_STATO_FASCICOLO")); 
        aModel.setFasSiuDataIscrizione(getDate("FAS_SIU_DATA_ISCRIZIONE")); 
        aModel.setFasSiuDataDefinizione(getDate("FAS_SIU_DATA_DEFINIZIONE")); 
        aModel.setCodEsitoTenore(getString("COD_ESITO_TENORE")); 
        aModel.setCodMagistrato(getString("COD_MAGISTRATO")); 
        aModel.setGenPridGeneraleProcedimento(getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO")); 
        aModel.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO")); 
        aModel.setDepOpidDepositoOrdinanzaPc(getBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC")); 
        aModel.setDepDecIdDepositoDecreto(getBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO")); 
        aModel.setTenData(getDate("TEN_DATA")); 
        aModel.setTenDataFine(getDate("TEN_DATA_FINE")); 
        aModel.setTenDataIns(getDate("TEN_DATA_INS")); 
        aModel.setCodEsitoStatistica(getString("COD_ESITO_STATISTICA")); 
        aModel.setDataDeposito(getDate("DATA_DEPOSITO")); 
        aModel.setDefinito(getString("DEFINITO")); 
        
      
      return aModel;
    }

    public String  setCondizione(EspertoModel aModel)
    {
      String lCondizioni = new String();
      boolean lInserito = false;

      // Imposta la condizione di filtro sul codice Ufficio di appartenenza.
      if((aModel.getCodUfficioAppartenenza()).length() != 0)
      {
        lCondizioni = " COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() +"'";
        lInserito = true;
      }

      // Imposta la condizione di filtro sul Cognome
      if((aModel.getCognome()).length() != 0)
      {
        if(lInserito)
          lCondizioni += " AND";
        lCondizioni += " COGNOME LIKE '" + aModel.getCognome() + "%'";

        lInserito = true;
      }

      // Imposta la condizione di filtro sul nome
      if((aModel.getNome()).length() != 0)
      {
        if(lInserito)
          lCondizioni += " AND";
        lCondizioni += " NOME LIKE '" + aModel.getNome() + "%'";

        lInserito = true;
      }

      if(lInserito)
        lCondizioni = " WHERE " + lCondizioni;

      return lCondizioni;
    }

    /**
     * Metodo che imposta il filtro di condizione con l'id
     * <p>
     * @param aKey BigDecimal id esperto.
     * @return String stringa di ritorno con la condizione.
     */
    public String setCondizioniByKey(BigDecimal aKey)
    {
      return " ID_ESPERTO = " + aKey;
    }
}
