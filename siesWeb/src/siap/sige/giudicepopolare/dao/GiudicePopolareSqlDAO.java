package siap.sige.giudicepopolare.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sige.giudicepopolare.model.GiudicePopolareModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
* <p>Title: GiudicePopolareSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella GiudicePopolare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/
public class GiudicePopolareSqlDAO extends SIAPSqlDAO
{
  public GiudicePopolareSqlDAO (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //
  public void ricercaGiudicePopolare(GiudicePopolareModel aModel)
  throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += "  " + setCondizione(aModel);
    lSql += " ORDER BY GP.COD_RUOLO DESC, COGNOME ASC, NOME ASC ";
    setStatement(lSql);
  }

  public void ricercaGiudicePopolareByKey(BigDecimal aKey)
  throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " WHERE " + setCondizioniByKey(aKey);
    setStatement(lSql);
  }

  public void ricercaGiudicePopolareByCodUfficio(String aCodUfficio) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " WHERE GP.COD_UFFICIO_APPARTENENZA = '" + aCodUfficio + "'";
    //lSql += " OR GP.COD_UFFICIO_APPARTENENZA IS NULL";
    lSql += " ORDER BY COGNOME";
    setStatement(lSql);
  }

  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
                     " ID_GIUDICE_POPOLARE, "+
                     " GP.COGNOME, "+
                     " GP.NOME, "+
                     " GP.INDIRIZZO, "+
                     " GP.COD_UFFICIO_APPARTENENZA, "+
                     " GP.DATA_INIZIO_VALIDITA, "+
                     " GP.DATA_FINE_VALIDITA, "+
                     " GP.COD_RUOLO, "+
                     " DISP.RV_MEANING AS DESCR_RUOLO, " +
                     " GP.COD_OPERATORE_INSERIMENTO, "+
                     " GP.DATA_INSERIMENTO, "+
                     " GP.COD_UFFICIO_INSERIMENTO, "+
                     " GP.COD_OPERATORE_AGGIORNAMENTO, "+
                     " GP.DATA_AGGIORNAMENTO, "+
                     " GP.COD_UFFICIO_AGGIORNAMENTO, "+
                     " GP.CODICE_FISCALE, "+
                     " GP.DATA_NASCITA, "+
                     " GP.COD_STATO_NASCITA, "+
                     " NAZIONE.RV_MEANING AS DESCR_STATO_NASCITA, " +
                     " GP.COD_COMUNE_NASCITA, "+
                     " COMUNE.DESCRIZIONE AS DESCR_COMUNE_NASCITA, " +
                     " GP.COMUNE_ESTERO_NASCITA, "+
                     " GP.SEZ_ID_SEZIONE, " +
                     " SEZIONE.DESCRIZIONE AS DESCR_SEZIONE, " +
                     " GP.COD_SESSO "+
                 " FROM GIUDICE_POPOLARE GP " +
                     " INNER JOIN CG_REF_CODES DISP ON DISP.RV_LOW_VALUE = GP.COD_RUOLO " +
                     " AND DISP.RV_DOMAIN = 'RUOLO_GIUDICE_POPOLARE' " +
                     " INNER JOIN CG_REF_CODES NAZIONE ON NAZIONE.RV_LOW_VALUE = GP.COD_STATO_NASCITA " +
                     " AND NAZIONE.RV_DOMAIN = 'NAZIONE' " +  
                     " LEFT OUTER JOIN COMUNE ON COMUNE.COD_COMUNE = GP.COD_COMUNE_NASCITA " +
                     " LEFT OUTER JOIN SEZIONE ON SEZIONE.ID_SEZIONE = GP.SEZ_ID_SEZIONE ";
    return lStatement;
  }
  //
  // METODO GETMODEL()
  //
  public GenericModel getModel()
  throws DAOException
  {
    GiudicePopolareModel aModel = new  GiudicePopolareModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdGiudicePopolare(getBigDecimal("ID_GIUDICE_POPOLARE") );
    aModel.setCognome(getString("COGNOME") );
    aModel.setNome(getString("NOME") );
    aModel.setIndirizzo(getString("INDIRIZZO") );
    aModel.setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA") );
    aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
    aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
    aModel.setCodRuolo(getString("COD_RUOLO"));
    aModel.setDescrRuolo(getString("DESCR_RUOLO"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setCodiceFiscale(getString("CODICE_FISCALE"));
    aModel.setDataNascita(getDate("DATA_NASCITA"));
    aModel.setCodStatoNascita(getString("COD_STATO_NASCITA"));
    aModel.setDescrStatoNascita(getString("DESCR_STATO_NASCITA"));
    aModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
    aModel.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
    aModel.setComuneEsteroNascita(getString("COMUNE_ESTERO_NASCITA"));
    aModel.setSezIdSezione(getBigDecimal("SEZ_ID_SEZIONE"));
    aModel.setDescrSezione(getString("DESCR_SEZIONE"));
    aModel.setCodSesso(getString("COD_SESSO"));
    
    return aModel;
  }

  public String setCondizione(GiudicePopolareModel aModel)
  {
    String lCondizioni = new String();
    boolean lInserito = false;

    // Imposta la condizione di filtro sul codice Ufficio di appartenenza.
    if((aModel.getCodUfficioAppartenenza()).length() != 0)
    {
      lCondizioni = " GP.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() +"'";
      lInserito = true;
    }

    // Imposta la condizione di filtro sul Cognome.
    if((aModel.getCognome()).length() != 0)
    {
      if(lInserito)
        lCondizioni += " AND ";
      lCondizioni += " COGNOME LIKE '" + aModel.getCognome() + "%'";

      lInserito = true;
    }

    // Imposta la condizione di filtro sul Nome.
    if((aModel.getNome()).length() != 0)
    {
      if(lInserito)
        lCondizioni += " AND ";
      lCondizioni += " NOME LIKE '" + aModel.getNome() + "%'";

      lInserito = true;
    }
    
    // Imposta condizione di filtro sulla sezione.
    
    // 1 - Condizione su occorrenze che non sono associati a sezioni.
    /* 
     * 20081030 - Commentato e da eliminare su richiesta del cliente
     * inqunato un giudicepopolare deve essere associato ad una sezione
     * Quando si avrà conferma dall'analista, si eliminerà parte di codice.
     *   
    if( aModel.getSezIdSezione() != null && 
        aModel.getSezIdSezione().compareTo(new BigDecimal("-1")) == 0 )
    {
      if(lInserito)
        lCondizioni += " AND ";
      lCondizioni += " SEZ_ID_SEZIONE IS NULL ";
      
      lInserito = true;
    }
    // 2 - Condizione su occorrenze che hanno una sezione associata. 
    else if( aModel.getSezIdSezione() != null ) 
    {
      if(lInserito)
        lCondizioni += " AND ";
      lCondizioni += " SEZ_ID_SEZIONE = " + aModel.getSezIdSezione();
      
      lInserito = true;
    }
    */
    // Condizione di filtro sulla sezione.
    if( aModel.getSezIdSezione() != null ) 
    {
      if(lInserito)
        lCondizioni += " AND ";
      
      lCondizioni += " SEZ_ID_SEZIONE = " + aModel.getSezIdSezione();
      
      lInserito = true;
    }    
    
    // Imposta condizione di filtro sulle date di validità.
    
    // 1 - Data Inizio Validità >= della data richiesta 
    if( aModel.getDataInizioValidita() != null )
    {
      if(lInserito)
        lCondizioni += " AND ";
      lCondizioni += " DATA_INIZIO_VALIDITA >= " 
        + " TO_DATE('"+DateUtils.getDateToString(aModel.getDataInizioValidita(), "dd/MM/yyyy")+"','DD/MM/YYYY')";
      
      lInserito = true;      
    }
    // 2 - Filtra per tutte le occorrenze che hanno la data di fine validità a null 
    if(aModel.getMessage() != null && 
       aModel.getMessage().equalsIgnoreCase("dataFineisNull"))
    {
      if(lInserito)
        lCondizioni += " AND ";
      lCondizioni += " DATA_FINE_VALIDITA IS NULL ";
      
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
    return " ID_GIUDICE_POPOLARE = " + aKey;
  }
}