package siap.sius.prescrizione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sius.prescrizione.model.PrescrizioneModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: PrescrizioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Prescrizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class PrescrizioneDAO extends SIAPTableDAO
{
	/**
   * Costruttore di classe con attributi.
   * <p>
   * @param con Connesione al dbase.
   */
  public PrescrizioneDAO (Connection con)
	{
    super(con);
    setTable("PRESCRIZIONE");

    //Settare la Sequence e i campi chiave
    setFieldKey("ID_PRESCRIZIONE", BIG_DECIMAL);
    setSequenceField("ID_PRESCRIZIONE","PRE_SEQ");

    setField("ID_PRESCRIZIONE", BIG_DECIMAL);
    setField("COD_TIPO_PRESCRIZIONE", STRING);
    setField("COD_LUOGO_AFFIDAMENTO", STRING);
    setField("COD_UFF_MAGISTRATO_COMPETENTE", STRING);
    setField("COD_LUOGO_AUTORIZZATO", STRING);
    setField("ID_CSSA_COMPETENTE", BIG_DECIMAL);
    setField("DESCR_MANSIONE_LAVORATIVA", STRING);
    setField("DESCR_LUOGO_LAVORO", STRING);
    setField("COD_PROVINCIA_AUTORIZZATA", STRING);
    setField("ORA_USCITA_ABITAZIONE", STRING);
    setField("ORA_RIENTRO_ABITAZIONE", STRING);
    setField("AUTORITA_COMPETENTE_CONTROLLO", STRING);
    setField("NUM_VOLTE_CONTROLLO", BIG_DECIMAL);
    setField("DESCR_ALTRA_PRESCRIZIONE", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("EVE_ID_EVENTO", BIG_DECIMAL);
    setField("DESCR_COMUNITA_TERAPEUTICA", STRING);
    setField("PROGR_PRESCRIZIONE",BIG_DECIMAL);
    setField("DESCR_PRESCRIZIONE_1", STRING);
    setField("DESCR_PRESCRIZIONE_2", STRING);
    setField("DESCR_PRESCRIZIONE_3", STRING);
	}


  //
  // METODI GET()
  //
  public BigDecimal 		 getIdPrescrizione() 		          throws DAOException	 { return getBigDecimal("ID_PRESCRIZIONE"); }
  public String 				 getCodTipoPrescrizione() 		    throws DAOException	 { return getString("COD_TIPO_PRESCRIZIONE"); }
  public String 				 getCodLuogoAffidamento() 		    throws DAOException	 { return getString("COD_LUOGO_AFFIDAMENTO"); }
  public String 				 getCodUffMagistratoCompetente() 	throws DAOException	 { return getString("COD_UFF_MAGISTRATO_COMPETENTE"); }
  public String 				 getCodLuogoAutorizzato() 		    throws DAOException	 { return getString("COD_LUOGO_AUTORIZZATO"); }
  public BigDecimal			 getIdCssaCompetente() 		        throws DAOException	 { return getBigDecimal("ID_CSSA_COMPETENTE"); }
  public String 				 getDescrMansioneLavorativa() 		throws DAOException	 { return getString("DESCR_MANSIONE_LAVORATIVA"); }
  public String 				 getDescrLuogoLavoro() 		        throws DAOException	 { return getString("DESCR_LUOGO_LAVORO"); }
  public String 				 getCodProvinciaAutorizzata() 		throws DAOException	 { return getString("COD_PROVINCIA_AUTORIZZATA"); }
  public String 				 getOraUscitaAbitazione() 		    throws DAOException	 { return getString("ORA_USCITA_ABITAZIONE"); }
  public String 				 getOraRientroAbitazione() 		    throws DAOException	 { return getString("ORA_RIENTRO_ABITAZIONE"); }
  public String 				 getAutoritaCompetenteControllo() throws DAOException	 { return getString("AUTORITA_COMPETENTE_CONTROLLO"); }
  public BigDecimal 		 getNumVolteControllo() 		      throws DAOException	 { return getBigDecimal("NUM_VOLTE_CONTROLLO"); }
  public String 				 getDescrAltraPrescrizione() 		  throws DAOException	 { return getString("DESCR_ALTRA_PRESCRIZIONE"); }
  public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 					 getDataInserimento() 		        throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 				 getCodUfficioInserimento() 		  throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 				 getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 					 getDataAggiornamento() 		      throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal 		 getEveIdEve() 	                  throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }
//  public BigDecimal 		 getDepOpidDepositoOrdinanzaPc() 	throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }
  public String 				 getDescrComunitaTerapeutica() 		throws DAOException	 { return getString("DESCR_COMUNITA_TERAPEUTICA"); }
  public BigDecimal      getProgrPrescrizione()           throws DAOException  { return getBigDecimal("PROGR_PRESCRIZIONE"); }
  
  public String 				 getDescrPrescrizione1() 		throws DAOException	 { return getString("DESCR_PRESCRIZIONE_1"); }
  public String 				 getDescrPrescrizione2() 		throws DAOException	 { return getString("DESCR_PRESCRIZIONE_2"); }
  public String 				 getDescrPrescrizione3() 		throws DAOException	 { return getString("DESCR_PRESCRIZIONE_3"); }
  
  //
  // METODI SET()
  //
  public void  	 setIdPrescrizione(BigDecimal aValore ) 			      { setBigDecimal("ID_PRESCRIZIONE", aValore); }
  public void  	 setCodTipoPrescrizione(String aValore ) 			      { setString("COD_TIPO_PRESCRIZIONE", aValore); }
  public void  	 setCodLuogoAffidamento(String aValore ) 			      { setString("COD_LUOGO_AFFIDAMENTO", aValore); }
  public void  	 setCodUffMagistratoCompetente(String aValore ) 		{ setString("COD_UFF_MAGISTRATO_COMPETENTE", aValore); }
  public void  	 setCodLuogoAutorizzato(String aValore ) 			      { setString("COD_LUOGO_AUTORIZZATO", aValore); }
  public void  	 setIdCssaCompetente(BigDecimal aValore ) 			    { setBigDecimal("ID_CSSA_COMPETENTE", aValore); }
  public void  	 setDescrMansioneLavorativa(String aValore ) 			  { setString("DESCR_MANSIONE_LAVORATIVA", aValore); }
  public void  	 setDescrLuogoLavoro(String aValore ) 			        { setString("DESCR_LUOGO_LAVORO", aValore); }
  public void  	 setCodProvinciaAutorizzata(String aValore ) 			  { setString("COD_PROVINCIA_AUTORIZZATA", aValore); }
  public void  	 setOraUscitaAbitazione(String aValore ) 			      { setString("ORA_USCITA_ABITAZIONE", aValore); }
  public void  	 setOraRientroAbitazione(String aValore ) 			    { setString("ORA_RIENTRO_ABITAZIONE", aValore); }
  public void  	 setAutoritaCompetenteControllo(String aValore ) 	  { setString("AUTORITA_COMPETENTE_CONTROLLO", aValore); }
  public void  	 setNumVolteControllo(BigDecimal aValore ) 			    { setBigDecimal("NUM_VOLTE_CONTROLLO", aValore); }
  public void  	 setDescrAltraPrescrizione(String aValore ) 			  { setString("DESCR_ALTRA_PRESCRIZIONE", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 			  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			            { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 			    { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 			{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 			          { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 			  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setEveIdEve(BigDecimal aValore )                   { setBigDecimal("EVE_ID_EVENTO", aValore); }
  public void 	 setDescrComunitaTerapeutica( String aValore ) 		  { setString("DESCR_COMUNITA_TERAPEUTICA", aValore); }
  public void    setProgrPrescrizione( BigDecimal aValore )         { setBigDecimal("PROGR_PRESCRIZIONE", aValore); }
  public void 	 setDescrPrescrizione1( String aValore ) 		  { setString("DESCR_PRESCRIZIONE_1", aValore); }
  public void 	 setDescrPrescrizione2( String aValore ) 		  { setString("DESCR_PRESCRIZIONE_2", aValore); }
  public void 	 setDescrPrescrizione3( String aValore ) 		  { setString("DESCR_PRESCRIZIONE_3", aValore); }

	public GenericModel getModel() throws DAOException
  {
    return new PrescrizioneModel(getIdPrescrizione() ,
                                 getCodTipoPrescrizione() ,
                                 "",
                                 getCodLuogoAffidamento() ,
                                 "",
                                 getCodUffMagistratoCompetente() ,
                                 "",
                                 getCodLuogoAutorizzato() ,
                                 "",
                                 getIdCssaCompetente() ,
                                 "",
                                 getDescrMansioneLavorativa() ,
                                 getDescrLuogoLavoro() ,
                                 getCodProvinciaAutorizzata() ,
                                 "",
                                 getOraUscitaAbitazione() ,
                                 getOraRientroAbitazione() ,
                                 getAutoritaCompetenteControllo() ,
                                 getNumVolteControllo() ,
                                 getDescrAltraPrescrizione() ,
                                 getCodOperatoreInserimento() ,
                                 getDataInserimento() ,
                                 getCodUfficioInserimento() ,
                                 "",
                                 getCodOperatoreAggiornamento() ,
                                 getDataAggiornamento() ,
                                 getCodUfficioAggiornamento() ,
                                 "",
                                 getEveIdEve(),
                                 getDescrComunitaTerapeutica(),
                                 getProgrPrescrizione(), 
                                 getDescrPrescrizione1(),
                                 getDescrPrescrizione2(),
                                 getDescrPrescrizione3());
		}


	 /**
    * Imposta i dati del DAO con quelli contenuti nel model
    * e passati come argomento.
    * <p>
    * @param aModel model con i valori da passare al DAO.
    * @throws DAOException propaga l'errore di eccezione.
    */
   public void setDAOFromModel(PrescrizioneModel aModel) throws DAOException
   {
     setIdPrescrizione( aModel.getIdPrescrizione() );
     setCodTipoPrescrizione( aModel.getCodTipoPrescrizione() );
     setCodLuogoAffidamento( aModel.getCodLuogoAffidamento() );
     setCodUffMagistratoCompetente( aModel.getCodUffMagistratoCompetente() );
     setCodLuogoAutorizzato( aModel.getCodLuogoAutorizzato() );
     setIdCssaCompetente( aModel.getIdCssaCompetente() );
     setDescrMansioneLavorativa( aModel.getDescrMansioneLavorativa() );
     setDescrLuogoLavoro( aModel.getDescrLuogoLavoro() );
     setCodProvinciaAutorizzata( aModel.getCodProvinciaAutorizzata() );
     setOraUscitaAbitazione( aModel.getOraUscitaAbitazione() );
     setOraRientroAbitazione( aModel.getOraRientroAbitazione() );
     setAutoritaCompetenteControllo( aModel.getAutoritaCompetenteControllo() );
     setNumVolteControllo( aModel.getNumVolteControllo() );
     setDescrAltraPrescrizione( aModel.getDescrAltraPrescrizione() );
     setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
     setDataInserimento( aModel.getDataInserimento() );
     setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setEveIdEve( aModel.getEveIdEve() );
     setDescrComunitaTerapeutica( aModel.getDescrComunitaTerapeutica() );
     setProgrPrescrizione( aModel.getProgrPrescrizione() );
     setDescrPrescrizione1( aModel.getDescrPrescrizione1() );
     setDescrPrescrizione2( aModel.getDescrPrescrizione2() );
     setDescrPrescrizione3( aModel.getDescrPrescrizione3() );
    
  }

	 /**
    * Imposta i dati del DAO con quelli contenuti nel model
    * e passati come argomento, per la fase di update.
    * <p>
    * @param aModel model con i valori da passare al DAO.
    * @throws DAOException propaga l'errore di eccezione.
    */
  public void setDAOFromModelForUpdate(PrescrizioneModel aModel) throws DAOException
  {
    setIdPrescrizione( aModel.getIdPrescrizione() );
    setCodTipoPrescrizione( aModel.getCodTipoPrescrizione() );
    setCodLuogoAffidamento( aModel.getCodLuogoAffidamento() );
    setCodUffMagistratoCompetente( aModel.getCodUffMagistratoCompetente() );
    setCodLuogoAutorizzato( aModel.getCodLuogoAutorizzato() );
    setIdCssaCompetente( aModel.getIdCssaCompetente() );
    setDescrMansioneLavorativa( aModel.getDescrMansioneLavorativa() );
    setDescrLuogoLavoro( aModel.getDescrLuogoLavoro() );
    setCodProvinciaAutorizzata( aModel.getCodProvinciaAutorizzata() );
    setOraUscitaAbitazione( aModel.getOraUscitaAbitazione() );
    setOraRientroAbitazione( aModel.getOraRientroAbitazione() );
    setAutoritaCompetenteControllo( aModel.getAutoritaCompetenteControllo() );
    setNumVolteControllo( aModel.getNumVolteControllo() );
    setDescrAltraPrescrizione( aModel.getDescrAltraPrescrizione() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setEveIdEve( aModel.getEveIdEve() );
    setDescrComunitaTerapeutica(aModel.getDescrComunitaTerapeutica());
    setProgrPrescrizione(aModel.getProgrPrescrizione());
    setDescrPrescrizione1( aModel.getDescrPrescrizione1() );
    setDescrPrescrizione2( aModel.getDescrPrescrizione2() );
    setDescrPrescrizione3( aModel.getDescrPrescrizione3() );
    
    setCondizioneUpdate(aModel.getIdPrescrizione());
  }

  /**
   * Imposta le condizioni di filtro.
   * <p>
   * @param aModel
   */
	public void setCondizione(PrescrizioneModel aModel)
  {
    String lCondizioni = new String();
    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

	/**
   * Imposta la condizio di update per la chiave.
   * <p>
   * @param key chiave soggetta ad update.
   */
  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_PRESCRIZIONE = " + key );
  }

  /**
   * Imposta la condizione per l'id del deposito ordinanza.
   * <p>
   * @param aKey chiave del deposito ordinanza.
   */
  public void setCondizioneByDepOrdPC( BigDecimal aKey )
  {
    setCondition( " EVE_ID_EVENTO = " + aKey );
  }
}