package siap.siep.penacumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.penacumulo.model.PenaCumuloModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
* <p>Title: PenaCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella PenaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class PenaCumuloSqlDAO extends SqlDAO
{
  public PenaCumuloSqlDAO (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //

  public void ricercaPenaCumulo( PenaCumuloModel  aModel)  throws DAOException
  {
    String lSql = getSqlQuery();
  
    lSql += " " + setCondizione(aModel);
    setStatement(lSql);
  }

  public void ricercaPenaCumuloByKey( BigDecimal aKey)   throws DAOException
  {
    String lSql = getSqlQuery();
  
    lSql += " " + setCondizioniByKey(aKey);
    setStatement(lSql);
  }

  public void ricercaPenaCumuloByIdCumulo(BigDecimal aKey)   throws DAOException
  {
    String lSql = getSqlQuery();
    
    lSql +=  " AND CUM_ID_CUMULO = " + aKey;
    lSql +=  " ORDER BY DATA_INSERIMENTO DESC";
    setStatement(lSql);
  }

  /**
   * Imposta lo statement di ricerca dell'ultimo record PENA_CUMULO inserito e 
   * validato associato al fascicolo in input.
   * @param aIdFascicolo id del fascicolo
   * @throws DAOException
   */
  public void ricercaUltimaPenaCumuloByIdFascicolo(BigDecimal aIdFascicolo)   throws DAOException
  {
    String lStatement = new String("");
    lStatement += " SELECT " +
                  " pena.ID_PENA_CUMULO, pena.CUM_ID_CUMULO, pena.COD_TIPO_PENA_DETENTIVA,  "+
                  " PENA_DET.RV_MEANING DESCR_PENA_DETENTIVA, "+
                  " pena.NUM_ANNI_RECLUSIONE, pena.NUM_MESI_RECLUSIONE, pena.NUM_GIORNI_RECLUSIONE, "+
                  " pena.IMPORTO_MULTA, "+
                  " pena.NUM_ANNI_ARRESTO, pena.NUM_MESI_ARRESTO, pena.NUM_GIORNI_ARRESTO, "+
                  " pena.IMPORTO_AMMENDA, "+
                  " pena.NUM_GIORNI_LIB_ANTICIPATA, "+
                  // 20/05/2014	- Nuova L.A. - decreto 2013/146	-                
                  " pena.NUM_GIORNI_LIB_ANTICIPATA_LA, "+
                  " pena.NUM_GIORNI_LIB_ANTICIPATA_SPE, "+
                  " pena.NUM_GIORNI_LIB_ANTICIPATA_INT, "+  
                  // End                  
                  " pena.NUM_GIORNI_RIDUZIONE_PENA, "+ //DL92/2014                   
                  " pena.DATA_DECORRENZA_PENA, "+
                  " pena.MOTIVAZIONI, "+
                  " pena.NUM_ANNI_RECLUSIONE_SOSP, "+
                  " pena.NUM_MESI_RECLUSIONE_SOSP, "+
                  " pena.NUM_GIORNI_RECLUSIONE_SOSP, "+
                  " pena.NUM_ANNI_ARRESTO_SOSP, "+
                  " pena.NUM_MESI_ARRESTO_SOSP, "+
                  " pena.NUM_GIORNI_ARRESTO_SOSP, "+
                  " pena.ESTREMI_ORDINANZA, "+
                  " pena.FLAG_ERGASTOLO, "+
                  " pena.NOTE, "+
                  " pena.COD_OPERATORE_INSERIMENTO, pena.DATA_INSERIMENTO, pena.COD_UFFICIO_INSERIMENTO, "+
                  " pena.COD_OPERATORE_AGGIORNAMENTO, pena.DATA_AGGIORNAMENTO, pena.COD_UFFICIO_AGGIORNAMENTO, "+
                  " pena.NUM_ANNI_ISOLAMENTO_DIURNO, "+
                  " pena.NUM_MESI_ISOLAMENTO_DIURNO, "+
                  " pena.NUM_GIORNI_ISOLAMENTO_DIURNO, "+
                  " pena.MISURA_SICUREZZA, "+
                  " pena.PENA_ACCESSORIA ";
    lStatement += " FROM PENA_CUMULO pena, CUMULO, CG_REF_CODES PENA_DET ";
    lStatement += " WHERE CUM_ID_CUMULO = ID_CUMULO ";
    lStatement += "   AND CUMULO.FAS_SIE_ID_FASCICOLO_SIEP = "+ aIdFascicolo;
    lStatement += "   AND CUMULO.FLAG_VALIDATO = 'S' ";
    lStatement += "   AND (PENA_DET.RV_DOMAIN = 'FLAG_ERGASTOLO' AND PENA_DET.RV_LOW_VALUE = pena.COD_TIPO_PENA_DETENTIVA) ";
    lStatement += " ORDER BY CUMULO.DATA_INSERIMENTO DESC ";
    
    setStatement(lStatement);
  }
   
  /**
   * Imposta lo statement di ricerca del record PENA_CUMULO associato al
   * provvedimento passato in input.
   * @param aIdEvento id dell'evento del provvedimento di cumulo
   * @throws DAOException
   */
  public void ricercaPenaCumuloByIdEvento(BigDecimal aIdEvento ,BigDecimal aIdFascicolo)   throws DAOException
  {
    String lStatement = new String("");
    lStatement += " SELECT " +
                  " pena.ID_PENA_CUMULO, pena.CUM_ID_CUMULO, pena.COD_TIPO_PENA_DETENTIVA,  "+
                  " null DESCR_PENA_DETENTIVA, "+
                  " pena.NUM_ANNI_RECLUSIONE, pena.NUM_MESI_RECLUSIONE, pena.NUM_GIORNI_RECLUSIONE, "+
                  " pena.IMPORTO_MULTA, "+
                  " pena.NUM_ANNI_ARRESTO, pena.NUM_MESI_ARRESTO, pena.NUM_GIORNI_ARRESTO, "+
                  " pena.IMPORTO_AMMENDA, "+
                  " pena.NUM_GIORNI_LIB_ANTICIPATA, "+
                  // 20/05/2014	- Nuova L.A. - decreto 2013/146	-                
                  " pena.NUM_GIORNI_LIB_ANTICIPATA_LA, "+
                  " pena.NUM_GIORNI_LIB_ANTICIPATA_SPE, "+
                  " pena.NUM_GIORNI_LIB_ANTICIPATA_INT, "+  
                  // End                   
                  " pena.NUM_GIORNI_RIDUZIONE_PENA, "+ //DL92/2014 
                  " pena.DATA_DECORRENZA_PENA, "+
                  " pena.MOTIVAZIONI, "+
                  " pena.NUM_ANNI_RECLUSIONE_SOSP, "+
                  " pena.NUM_MESI_RECLUSIONE_SOSP, "+
                  " pena.NUM_GIORNI_RECLUSIONE_SOSP, "+
                  " pena.NUM_ANNI_ARRESTO_SOSP, "+
                  " pena.NUM_MESI_ARRESTO_SOSP, "+
                  " pena.NUM_GIORNI_ARRESTO_SOSP, "+
                  " pena.ESTREMI_ORDINANZA, "+
                  " pena.FLAG_ERGASTOLO, "+
                  " pena.NOTE, "+
                  " pena.COD_OPERATORE_INSERIMENTO, pena.DATA_INSERIMENTO, pena.COD_UFFICIO_INSERIMENTO, "+
                  " pena.COD_OPERATORE_AGGIORNAMENTO, pena.DATA_AGGIORNAMENTO, pena.COD_UFFICIO_AGGIORNAMENTO, "+
                  " pena.NUM_ANNI_ISOLAMENTO_DIURNO, "+
                  " pena.NUM_MESI_ISOLAMENTO_DIURNO, "+
                  " pena.NUM_GIORNI_ISOLAMENTO_DIURNO, "+
                  " pena.MISURA_SICUREZZA, "+
                  " pena.PENA_ACCESSORIA ";
    lStatement += " FROM PENA_CUMULO pena, CUMULO ";
    //lStatement += " WHERE CUM_ID_CUMULO = ID_CUMULO ";
    lStatement += " WHERE cumulo.FAS_SIE_ID_FASCICOLO_SIEP = "+ aIdFascicolo;
    lStatement += "   AND CUMULO.EVE_ID_EVENTO = "+ aIdEvento;
    lStatement += "   AND CUM_ID_CUMULO = ID_CUMULO ";
//    lStatement += "   AND CUMULO.FLAG_VALIDATO = 'S' ";
//    lStatement += " ORDER BY CUMULO.DATA_INSERIMENTO DESC ";
    
    setStatement(lStatement);
  }  

  /**
   * 
   * @return
   */
  protected String getSqlQuery()
  {
    String lStatement = new String("");
    lStatement += " SELECT " +
                  " ID_PENA_CUMULO, "+
                  " COD_TIPO_PENA_DETENTIVA, PENA_DET.RV_MEANING DESCR_PENA_DETENTIVA, "+
                  " NUM_ANNI_RECLUSIONE, "+
                  " NUM_MESI_RECLUSIONE, "+
                  " NUM_GIORNI_RECLUSIONE, "+
                  " IMPORTO_MULTA, "+
                  " NUM_ANNI_ARRESTO, "+
                  " NUM_MESI_ARRESTO, "+
                  " NUM_GIORNI_ARRESTO, "+
                  " IMPORTO_AMMENDA, "+
                  " DATA_DECORRENZA_PENA, "+
                  " MOTIVAZIONI, "+
                  " NUM_ANNI_RECLUSIONE_SOSP, "+
                  " NUM_MESI_RECLUSIONE_SOSP, "+
                  " NUM_GIORNI_RECLUSIONE_SOSP, "+
                  " NUM_ANNI_ARRESTO_SOSP, "+
                  " NUM_MESI_ARRESTO_SOSP, "+
                  " NUM_GIORNI_ARRESTO_SOSP, "+
                  " ESTREMI_ORDINANZA, "+
                  " FLAG_ERGASTOLO, "+
                  " NOTE, "+
                  " COD_OPERATORE_INSERIMENTO, "+
                  " DATA_INSERIMENTO, "+
                  " COD_UFFICIO_INSERIMENTO, "+
                  " COD_OPERATORE_AGGIORNAMENTO, "+
                  " DATA_AGGIORNAMENTO, "+
                  " COD_UFFICIO_AGGIORNAMENTO, "+
                  " CUM_ID_CUMULO, "+
                  " NUM_ANNI_ISOLAMENTO_DIURNO, "+
                  " NUM_MESI_ISOLAMENTO_DIURNO, "+
                  " NUM_GIORNI_ISOLAMENTO_DIURNO, "+
                  " MISURA_SICUREZZA, "+
                  " PENA_ACCESSORIA, "+
                  " NUM_GIORNI_LIB_ANTICIPATA, "+
    // 20/05/2014	- Nuova L.A. - decreto 2013/146	-                
                  " NUM_GIORNI_LIB_ANTICIPATA_LA, "+
                  " NUM_GIORNI_LIB_ANTICIPATA_SPE, "+
                  " NUM_GIORNI_LIB_ANTICIPATA_INT," +
                  " NUM_GIORNI_RIDUZIONE_PENA  ";
   // End  
    lStatement += " FROM PENA_CUMULO, CG_REF_CODES PENA_DET";
    lStatement += " WHERE PENA_DET.RV_DOMAIN = 'FLAG_ERGASTOLO' AND PENA_DET.RV_LOW_VALUE = COD_TIPO_PENA_DETENTIVA";

    return lStatement;
  }

  //
  // METODO GETMODEL()
  //

  public GenericModel getModel() throws DAOException
  {
    PenaCumuloModel aModel = new  PenaCumuloModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdPenaCumulo(getBigDecimal("ID_PENA_CUMULO") );
    aModel.setCodTipoPenaDetentiva(getString("COD_TIPO_PENA_DETENTIVA") );
    aModel.setDescrTipoPenaDetentiva(getString("DESCR_PENA_DETENTIVA") );
    aModel.setNumAnniReclusione(getBigDecimal("NUM_ANNI_RECLUSIONE") );
    aModel.setNumMesiReclusione(getBigDecimal("NUM_MESI_RECLUSIONE") );
    aModel.setNumGiorniReclusione(getBigDecimal("NUM_GIORNI_RECLUSIONE") );
    aModel.setImportoMulta(getBigDecimal("IMPORTO_MULTA") );
    aModel.setNumAnniArresto(getBigDecimal("NUM_ANNI_ARRESTO") );
    aModel.setNumMesiArresto(getBigDecimal("NUM_MESI_ARRESTO") );
    aModel.setNumGiorniArresto(getBigDecimal("NUM_GIORNI_ARRESTO") );
    aModel.setImportoAmmenda(getBigDecimal("IMPORTO_AMMENDA") );
    aModel.setDataDecorrenzaPena(getDate("DATA_DECORRENZA_PENA") );
    aModel.setMotivazioni(getString("MOTIVAZIONI") );
    aModel.setNumAnniReclusioneSosp(getBigDecimal("NUM_ANNI_RECLUSIONE_SOSP") );
    aModel.setNumMesiReclusioneSosp(getBigDecimal("NUM_MESI_RECLUSIONE_SOSP") );
    aModel.setNumGiorniReclusioneSosp(getBigDecimal("NUM_GIORNI_RECLUSIONE_SOSP") );
    aModel.setNumAnniArrestoSosp(getBigDecimal("NUM_ANNI_ARRESTO_SOSP") );
    aModel.setNumMesiArrestoSosp(getBigDecimal("NUM_MESI_ARRESTO_SOSP") );
    aModel.setNumGiorniArrestoSosp(getBigDecimal("NUM_GIORNI_ARRESTO_SOSP") );
    aModel.setEstremiOrdinanza(getString("ESTREMI_ORDINANZA") );
    aModel.setFlagErgastolo(getString("FLAG_ERGASTOLO") );
    aModel.setNote(getString("NOTE") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setCumIdCumulo(getBigDecimal("CUM_ID_CUMULO") );

    aModel.setNumAnniIsolamentoDiurno(getBigDecimal("NUM_ANNI_ISOLAMENTO_DIURNO") );
    aModel.setNumMesiIsolamentoDiurno(getBigDecimal("NUM_MESI_ISOLAMENTO_DIURNO") );
    aModel.setNumGiorniIsolamentoDiurno(getBigDecimal("NUM_GIORNI_ISOLAMENTO_DIURNO") );

    aModel.setMisuraSicurezza(getString("MISURA_SICUREZZA") );
    aModel.setPenaAccessoria(getString("PENA_ACCESSORIA") );
    aModel.setNumGiorniLibAnticipata(getBigDecimal("NUM_GIORNI_LIB_ANTICIPATA") );
// 20/05/2014	- Nuova L.A. - decreto 2013/146	- 
    aModel.setNumGiorniLibAnticipataLA(getBigDecimal("NUM_GIORNI_LIB_ANTICIPATA_LA") );
    aModel.setNumGiorniLibAnticipataSPE(getBigDecimal("NUM_GIORNI_LIB_ANTICIPATA_SPE") );
    aModel.setNumGiorniLibAnticipataINT(getBigDecimal("NUM_GIORNI_LIB_ANTICIPATA_INT") );
    // DL 92/2014
    aModel.setNumGiorniRiduzionePena (getBigDecimal("NUM_GIORNI_RIDUZIONE_PENA") );
    
    return aModel;
  }


  public String  setCondizione(PenaCumuloModel aModel)
  {
    String lCondizioni = new String();
    
    //boolean lInserito = false;
    return lCondizioni;
  }


  public String setCondizioniByKey(BigDecimal aKey)
  {
    return " AND ID_PENA_CUMULO = " + aKey;
  }
}
