package siap.sige.impugnazione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collection;
import java.util.Vector;

import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
* <p>Title: ImpugnazioneSigeSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Impugnazione Sige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ImpugnazioneSigeSqlDAO extends SqlDAO
{
  public ImpugnazioneSigeSqlDAO (Connection con)
  {
     super(con);
  }

  //
  // METODO RICERCA()
  //
  public void ricercaImpugnazioneSige( ImpugnazioneSigeModel  aModel)	 throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " " + setCondizione(aModel);
    setStatement(lSql);
  }

  public void ricercaImpugnazioneByKey( BigDecimal aKey)	 throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByKey(aKey);
    setStatement(lSql);
  }

  public void ricercaImpugnazioneByIdProvvedimentoSige(BigDecimal aKeyProvvedimentoSige) throws DAOException
  {
    String lSql = getSqlQueryPerJoin();

    lSql += " " + setCondizioniByIdProvvedimentoSige(aKeyProvvedimentoSige);

    setStatement(lSql);
  }

  public void ricercaImpugnazioneByIdProvvedimentoSigeNoJoin(BigDecimal aKeyProvvedimentoSige) throws DAOException
  {
    String lSql = getSqlQueryByIdProvvedimentoSige();

    lSql += " " + setCondizioniByIdProvvedimentoSige(aKeyProvvedimentoSige);

    setStatement(lSql);
  }
  
  public void ricercaImpugnazioniAccolteByIdProvvedimentoSige (BigDecimal aKeyProvvedimentoSige) throws DAOException
  {
    String lSql = getSqlQueryByIdProvvedimentoSige();

    lSql += " " + setCondizioniByIdProvvedimentoSige(aKeyProvvedimentoSige);
    lSql += " and COD_TENORE_DECISIONE = '10'";

    setStatement(lSql);
  }
  
  
  
  public void ricercaImpugnazioniAnnullateByProv( BigDecimal aIdProv )	 throws DAOException
  {

	  String lSql = getSqlQuery();
      // PROVVEDIMENTO SIGE
      lSql += " WHERE IMP.PROVV_ID_PROVVEDIMENTO_SIGE = " + aIdProv;

    lSql += " AND (IMP.DATA_ANNULLAMENTO IS NOT NULL)";
    lSql += " ORDER BY IMP.DATA_INSERIMENTO ASC ";
    setStatement(lSql);
  }

  public void ricercaImpugnazioniDelProvvedimento( BigDecimal aIdProv ) throws DAOException
  {
    String lSql = getSqlQuery();
      
      // PROVVEDIMENTO SIGE
      lSql += " WHERE IMP.PROVV_ID_PROVVEDIMENTO_SIGE = " + aIdProv;

      //lSql += " AND (IMP.DATA_ANNULLAMENTO IS NOT NULL)";
    lSql += " ORDER BY IMP.DATA_INSERIMENTO ASC ";
    setStatement(lSql);
  }

  public void ricercaImpugnazioniByIdProvvTipoImp( BigDecimal aIdProv, String aTipoImp ) throws DAOException
  {
	  //da rifare per soluzione non unica
    String lSql = getSqlQuery();
      
      // PROVVEDIMENTO SIGE
      lSql += " WHERE IMP.PROVV_ID_PROVVEDIMENTO_SIGE = " + aIdProv;
      lSql += " AND IMP.COD_TIPO_IMPUGNAZIONE = '" +aTipoImp+ "'";

    lSql += " ORDER BY IMP.DATA_RICORSO DESC ";
    setStatement(lSql);
  }
  
  public void ricercaImpugnazioniByIdProvvTipoImpPerEsitoDecisione( BigDecimal aIdProv, String aTipoImp ) throws DAOException
  {
	  //da rifare per soluzione non unica
    String lSql = getSqlQuery();
      
      // PROVVEDIMENTO SIGE
      lSql += " WHERE IMP.PROVV_ID_PROVVEDIMENTO_SIGE = " + aIdProv;
      lSql += " AND IMP.COD_TIPO_IMPUGNAZIONE = '" +aTipoImp+ "'";
      lSql += " AND IMP.FLAG_ANNULLAMENTO is NULL or UPPER(IMP.FLAG_ANNULLAMENTO) <> 'S'";

    lSql += " ORDER BY IMP.DATA_INSERIMENTO ASC ";
    setStatement(lSql);
  }
  
  
  

  public void ricercaImpugnazioniByIdProvv( BigDecimal aIdProv ) throws DAOException
  {
	  //Cerca le impugnazioni non annullate del provvedimento
    String lSql = getSqlQuery();
      
      // PROVVEDIMENTO SIGE
      lSql += " WHERE IMP.PROVV_ID_PROVVEDIMENTO_SIGE = " + aIdProv;
      lSql += " AND IMP.FLAG_ANNULLAMENTO IS NULL  ";

    lSql += " ORDER BY IMP.DATA_INSERIMENTO ASC ";
    setStatement(lSql);
  }

  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += " SELECT ID_IMPUGNAZIONE_SIGE, ANNO_S7, PROGR_S7, COD_TIPO_IMPUGNAZIONE, TIPO_IMPUGNAZIONE.RV_MEANING DESCR_TIPO_IMPUGNAZIONE, "+
                  " SOGGETTO_IMPUGNANTE, SOGGETTO_IMPUGNANTE.RV_MEANING DESCR_SOGGETTO_IMPUGNANTE, DATA_RICORSO, DATA_ANNOTAZIONE, "+
                  " ANNOTAZIONE, DATA_ARRIVO_CANCELLERIA, DATA_TRASMISSIONE_ATTI, COD_AUTORITA_DESTINATARIA, "+
                  " AUTORITA_DESTINATARIA.RV_MEANING DESCR_AUTORITA_DESTINATARIA, DATA_DECISIONE, COD_TENORE_DECISIONE, "+
                  " TENORE_DECISIONE.RV_MEANING DESCR_TENORE_DECISIONE, DATA_RESTITUZIONE_ATTI, COD_OPERATORE_INSERIMENTO, "+
                  " DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO, COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO, "+
                  " PROVV_ID_PROVVEDIMENTO_SIGE, FLAG_ANNULLAMENTO, DATA_ANNULLAMENTO, MOTIVO_ANNULLAMENTO, FLAG_SOSP_ESEC, PROVV_ID_PROVV_GENERATO, "+
                   //@emma 13072018 intervento post COLLAUDO 11.2
                  " CONV_RICORSO_IN_CASS, ID_OPPOSIZIONE_CONV_RICORSO, FLAG_VALIDAZIONE_ESITO " ;

              lStatement += " FROM IMPUGNAZIONE_SIGE IMP JOIN CG_REF_CODES TIPO_IMPUGNAZIONE ON (TIPO_IMPUGNAZIONE.RV_DOMAIN = 'TIPO_RICORSO_SIGE' AND TIPO_IMPUGNAZIONE.RV_LOW_VALUE = IMP.COD_TIPO_IMPUGNAZIONE) ";
              lStatement += " LEFT OUTER JOIN CG_REF_CODES SOGGETTO_IMPUGNANTE ON (SOGGETTO_IMPUGNANTE.RV_DOMAIN ='SOGGETTO_IMPUGNANTE_SIGE' AND SOGGETTO_IMPUGNANTE.RV_LOW_VALUE = IMP.SOGGETTO_IMPUGNANTE) ";
              lStatement += " JOIN CG_REF_CODES AUTORITA_DESTINATARIA ON (AUTORITA_DESTINATARIA.RV_DOMAIN = 'TIPO_UFFICIO' AND AUTORITA_DESTINATARIA.RV_LOW_VALUE = IMP.COD_AUTORITA_DESTINATARIA) ";
              lStatement += " LEFT OUTER JOIN CG_REF_CODES TENORE_DECISIONE ON (TENORE_DECISIONE.RV_DOMAIN = 'TENORE_DECISIONE_RICORSO_SIGE' AND TENORE_DECISIONE.RV_LOW_VALUE = IMP.COD_TENORE_DECISIONE) ";


    return lStatement;
  }

  //
  // METODO GETMODEL()
  //
  public GenericModel getModel() throws DAOException
  {
    ImpugnazioneSigeModel aModel = new ImpugnazioneSigeModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdImpugnazioneSige(getBigDecimal("ID_IMPUGNAZIONE_SIGE") );
    aModel.setAnnoS7(getBigDecimal("ANNO_S7") );
    aModel.setProgrS7(getBigDecimal("PROGR_S7") );
    aModel.setCodTipoImpugnazione(getString("COD_TIPO_IMPUGNAZIONE") );
    aModel.setDescrTipoImpugnazione(getString("DESCR_TIPO_IMPUGNAZIONE") );
    aModel.setSoggettoImpugnante(getString("SOGGETTO_IMPUGNANTE") );
    aModel.setDescrSoggettoImpugnante(getString("DESCR_SOGGETTO_IMPUGNANTE") );
    aModel.setDataRicorso(getDate("DATA_RICORSO") );
    aModel.setDataAnnotazione(getDate("DATA_ANNOTAZIONE") );
    aModel.setAnnotazione(getString("ANNOTAZIONE") );
    aModel.setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA") );
    aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI") );
    aModel.setCodAutoritaDestinataria(getString("COD_AUTORITA_DESTINATARIA") );
    aModel.setDescrAutoritaDestinataria(getString("DESCR_AUTORITA_DESTINATARIA") );
    aModel.setDataDecisione(getDate("DATA_DECISIONE") );
    aModel.setCodTenoreDecisione(getString("COD_TENORE_DECISIONE") );
    aModel.setDescrTenoreDecisione(getString("DESCR_TENORE_DECISIONE") );
    aModel.setDataRestituzioneAtti(getDate("DATA_RESTITUZIONE_ATTI") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    aModel.setDescrUfficioInserimento("");
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    aModel.setDescrUfficioAggiornamento("");
    aModel.setProvvIdProvvedimentoSige(getBigDecimal("PROVV_ID_PROVVEDIMENTO_SIGE") );
    aModel.setFlagAnnullamento(getString("FLAG_ANNULLAMENTO") );
    aModel.setDataAnnullamento(getDate("DATA_ANNULLAMENTO"));
    aModel.setMotivoAnnullamento(getString("MOTIVO_ANNULLAMENTO"));
    aModel.setFlagSospEsec(getString("FLAG_SOSP_ESEC"));
    aModel.setIdProvvedimentoGenerato(super.getBigDecimal("PROVV_ID_PROVV_GENERATO"));
    aModel.setConvRicorsoInCass(getString("CONV_RICORSO_IN_CASS"));
    aModel.setIdOpposizioneConvRicorso(getBigDecimal("ID_OPPOSIZIONE_CONV_RICORSO") );
    //@emma 12072018 intervento post COLLAUDO 11.2 
    try {
    if(getString("FLAG_VALIDAZIONE_ESITO")!=null)
    	aModel.setFlagValidazioneEsito(getString("FLAG_VALIDAZIONE_ESITO"));
    }catch (Exception e){
    	aModel.setFlagValidazioneEsito("");
    }
    
    return aModel;
  }

  public String  setCondizione(ImpugnazioneSigeModel aModel)
  {
    String lCondizioni = new String();
    return lCondizioni;
  }

  public String setCondizioniByKey(BigDecimal aKey)
  {
    String lCondizioni = new String();

    lCondizioni += " WHERE ID_IMPUGNAZIONE_SIGE = '"+aKey+"' ";
    return lCondizioni;
  }

  public String setCondizioniByIdProvvedimentoSige(BigDecimal aKeyProvvedimentoSige)
  {
    String lCondizioni = new String();

    lCondizioni += " WHERE PROVV_ID_PROVVEDIMENTO_SIGE = "+aKeyProvvedimentoSige+" ";
    return lCondizioni;
  }

  public void ricercaImpugnazioneByIdEvento( BigDecimal aIdEvento) throws DAOException
  {
    String lSql = getSqlQueryPerJoin();
    lSql += " " + setCondizioniByIdEvento(aIdEvento);
    setStatement(lSql);
  }
  
  public void ricercaImpugnazioneByIdEventoTipoImp( BigDecimal aIdEvento, String aTipo) throws DAOException
  {
    String lSql = getSqlQueryPerJoin();
    lSql += " " + setCondizioniByIdEvento(aIdEvento);
    setStatement(lSql);
  }

  protected String getSqlQueryPerJoin()
  {
    String lStatement = new String("");

    lStatement += " SELECT IMP.ID_IMPUGNAZIONE_SIGE, IMP.ANNO_S7, IMP.PROGR_S7, IMP.COD_TIPO_IMPUGNAZIONE, "+
                  " TIPO_IMPUGNAZIONE.RV_MEANING DESCR_TIPO_IMPUGNAZIONE, IMP.SOGGETTO_IMPUGNANTE, "+
                  " SOGGETTO_IMPUGNANTE.RV_MEANING DESCR_SOGGETTO_IMPUGNANTE, IMP.DATA_RICORSO, IMP.DATA_ANNOTAZIONE, "+
                  " IMP.ANNOTAZIONE, IMP.DATA_ARRIVO_CANCELLERIA, IMP.DATA_TRASMISSIONE_ATTI, IMP.COD_AUTORITA_DESTINATARIA, "+
                  " AUTORITA_DESTINATARIA.RV_MEANING DESCR_AUTORITA_DESTINATARIA, IMP.DATA_DECISIONE, IMP.COD_TENORE_DECISIONE, "+
                  " TENORE_DECISIONE.RV_MEANING DESCR_TENORE_DECISIONE, IMP.DATA_RESTITUZIONE_ATTI, IMP.COD_OPERATORE_INSERIMENTO, "+
                  " IMP.DATA_INSERIMENTO, IMP.COD_UFFICIO_INSERIMENTO, IMP.COD_OPERATORE_AGGIORNAMENTO, IMP.DATA_AGGIORNAMENTO, "+
                  " IMP.COD_UFFICIO_AGGIORNAMENTO, IMP.PROVV_ID_PROVVEDIMENTO_SIGE, " +
                  " IMP.FLAG_ANNULLAMENTO, IMP.DATA_ANNULLAMENTO, IMP.MOTIVO_ANNULLAMENTO, IMP.FLAG_SOSP_ESEC, IMP.PROVV_ID_PROVV_GENERATO, "+
                  " CONV_RICORSO_IN_CASS, ID_OPPOSIZIONE_CONV_RICORSO " ;
                  
    lStatement += " FROM IMPUGNAZIONE_SIGE IMP JOIN CG_REF_CODES TIPO_IMPUGNAZIONE ON (TIPO_IMPUGNAZIONE.RV_DOMAIN = 'TIPO_RICORSO_SIGE' AND TIPO_IMPUGNAZIONE.RV_LOW_VALUE = IMP.COD_TIPO_IMPUGNAZIONE) ";
    lStatement += " LEFT OUTER JOIN CG_REF_CODES SOGGETTO_IMPUGNANTE ON (SOGGETTO_IMPUGNANTE.RV_DOMAIN ='SOGGETTO_IMPUGNANTE_SIGE' AND SOGGETTO_IMPUGNANTE.RV_LOW_VALUE = IMP.SOGGETTO_IMPUGNANTE) ";
    lStatement += " JOIN CG_REF_CODES AUTORITA_DESTINATARIA ON (AUTORITA_DESTINATARIA.RV_DOMAIN = 'TIPO_UFFICIO' AND AUTORITA_DESTINATARIA.RV_LOW_VALUE = IMP.COD_AUTORITA_DESTINATARIA) ";
    lStatement += " JOIN CG_REF_CODES TENORE_DECISIONE ON (TENORE_DECISIONE.RV_DOMAIN = 'TENORE_DECISIONE_RICORSO_SIGE' AND TENORE_DECISIONE.RV_LOW_VALUE = IMP.COD_TENORE_DECISIONE) ";
    return lStatement;
  }

  protected String getSqlQueryByIdProvvedimentoSige()
  {
    String lStatement = new String("");

    lStatement += " SELECT  ID_IMPUGNAZIONE_SIGE, ANNO_S7, PROGR_S7, COD_TIPO_IMPUGNAZIONE, SOGGETTO_IMPUGNANTE, DATA_RICORSO, "+
    		" DATA_ANNOTAZIONE, ANNOTAZIONE, DATA_ARRIVO_CANCELLERIA, DATA_TRASMISSIONE_ATTI, COD_AUTORITA_DESTINATARIA, "+
    		" DATA_DECISIONE, COD_TENORE_DECISIONE, DATA_RESTITUZIONE_ATTI, COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, "+
    		" COD_UFFICIO_INSERIMENTO, COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO, "+
    		" null as DESCR_TIPO_IMPUGNAZIONE, null as DESCR_SOGGETTO_IMPUGNANTE, null as DESCR_AUTORITA_DESTINATARIA, null as DESCR_TENORE_DECISIONE, "+
    		" PROVV_ID_PROVVEDIMENTO_SIGE, FLAG_ANNULLAMENTO, DATA_ANNULLAMENTO, MOTIVO_ANNULLAMENTO, FLAG_SOSP_ESEC, PROVV_ID_PROVV_GENERATO, "+
    		" CONV_RICORSO_IN_CASS, ID_OPPOSIZIONE_CONV_RICORSO " ;
    
    lStatement += " FROM IMPUGNAZIONE_SIGE ";
    return lStatement;
  }
  
  public String setCondizioniByIdEvento( BigDecimal aIdEvento)
  {
    String lCondizioni = new String();
     lCondizioni +=    " , EVENTO EVE, PROVVEDIMENTO_SIGE PROVV ";
      lCondizioni += "WHERE EVE.ID_EVENTO = '"+aIdEvento+"' ";
      lCondizioni +=    "AND EVE.ID_EVENTO = PROVV.ID_EVENTO_GENERATO AND PROVV.ID_PROVVEDIMENTO_SIGE = IMP.PROVV_ID_PROVVEDIMENTO_SIGE ";

    lCondizioni +=      "AND (IMP.DATA_ANNULLAMENTO IS NULL)";

    return lCondizioni;
  }


 /**
  * Calcola il Massimo Progressivo relativo ad un certo ufficio e all'anno in corso.
  * Il massimo progressivo rappresenta anche l'ultimo progressivo inserito all'intenro
  * dell'ufficio trattato.
  * <p>
  * @param aImpugnazioneModel istanza model del'Impugnazione.
  * @throws DAOException propaga l'errore di eccezione.
  */
  public void  getProgressivoImpugnazioneSige(ImpugnazioneSigeModel aImpModel)
  throws DAOException
  {
    String lStatement = new String();

    lStatement += " SELECT MAX(PROGR_S7) aMAX";
    lStatement += " FROM IMPUGNAZIONE_SIGE IMP";
    lStatement += " WHERE IMP.ANNO_S7 = " + aImpModel.getAnnoS7();
    lStatement += " AND IMP.COD_UFFICIO_INSERIMENTO = " + aImpModel.getCodUfficioInserimento();

    setStatement( lStatement );
  }

  public String getDataRicorso(BigDecimal aIdEve)
    throws DAOException
  {
    String retNum = " ";
    String lStatement = "";

      lStatement = "SELECT ID_PROVVEDIMENTO_SIGE, TO_CHAR(DATA_RICORSO,'dd-mm-yyyy') as DATA_RICORSO FROM PROVVEDIMENTO_SIGE LEFT OUTER JOIN IMPUGNAZIONE_SIGE ON PROVV_ID_PROVVEDIMENTO_SIGE = ID_PROVVEDIMENTO_SIGE WHERE ID_EVENTO_GENERATO = " + aIdEve;

      if (!lStatement.equals("")){
      setStatement( lStatement );
      this.start();
      if( this.next() )
      {
        retNum = this.getString("DATA_RICORSO") ;
      }
    }
    return retNum;
  }

  public BigDecimal getIdProvvedimentoSige(BigDecimal aIdImpugnazione)
  throws DAOException
{
  BigDecimal retNum = new BigDecimal(0);
  String lStatement = "";

    lStatement = "SELECT PROVV_ID_PROVVEDIMENTO_SIGE FROM IMPUGNAZIONE_SIGE WHERE ID_IMPUGNAZIONE_SIGE = " + aIdImpugnazione;

    if (!lStatement.equals("")){
    setStatement( lStatement );
    this.start();
    if( this.next() )
    {
      retNum = this.getBigDecimal("PROVV_ID_PROVVEDIMENTO_SIGE") ;
    }
  }
  return retNum;
}
  public Collection <String>getDateRicorsi(BigDecimal aIdEve)
    throws DAOException
  {
    Collection <String>aDate = new Vector<String>();
    String lStatement = "";

      lStatement = "SELECT ID_PROVVEDIMENTO_SIGE, TO_CHAR(DATA_RICORSO,'dd-mm-yyyy') as DATA_RICORSO FROM PROVVEDIMENTO_SIGE LEFT OUTER JOIN IMPUGNAZIONE_SIGE ON PROVV_ID_PROVVEDIMENTO_SIGE = ID_PROVVEDIMENTO_SIGE WHERE ID_EVENTO_GENERATO = " + aIdEve;

      if (!lStatement.equals("")){
      setStatement( lStatement );
      this.start();
      while (this.next() )
      {
        String aDataRicorso = this.getString("DATA_RICORSO");
        if (aDataRicorso!=null)
        {
          aDate.add(aDataRicorso) ;
        }
      }
    }
    return aDate;
  }

}
