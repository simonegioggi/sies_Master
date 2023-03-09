package siap.siep.notifica.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sico.evento.model.EventoModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: NotificaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Notifica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class NotificaDAO extends SIAPTableDAO
{
  public NotificaDAO (Connection con)
  {
     super(con);
     setTable("NOTIFICA");

		 //Settare la Sequence e i campi chiave
     setFieldKey("ID_NOTIFICA",BIG_DECIMAL);

     setSequenceField("ID_NOTIFICA","NOT_SEQ");

     setField("ID_NOTIFICA", BIG_DECIMAL);
     setField("COD_TIPO_NOTIFICA", STRING);
     setField("COD_UFF_UEPE_USSM_SS", STRING);
     setField("COD_UFF_UDS_UDSM", STRING);
     setField("COD_UFF_TDS_TDSM", STRING);
     setField("DATA_AVVENUTA_NOTIFICA", DATE);
     setField("DATA_INVIO", DATE);
     setField("COD_ESITO", STRING);
     setField("NOTE", STRING);
     setField("COD_OPERATORE_INSERIMENTO", STRING);
     setField("DATA_INSERIMENTO", DATE);
     setField("COD_UFFICIO_INSERIMENTO", STRING);
     setField("CODICE_OPERATORE_AGGIORNAMENTO", STRING);
     setField("DATA_AGGIORNAMENTO", DATE);
     setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
     setField("EVE_ID_EVENTO", BIG_DECIMAL);
     setField("AUT_EST_ID_AUTORITA_ESTERNA", BIG_DECIMAL);
     setField("SOG_ID_SOGGETTO", BIG_DECIMAL);
     setField("AVV_ID_AVVOCATO_FASCICOLO_SIEP", BIG_DECIMAL);
     setField("AVV_ID_AVVOCATO_FASCICOLO_SIUS", BIG_DECIMAL);
     setField("UFF_COD_UFFICIO", STRING);
     setField("SOLLECITO", BIG_DECIMAL);
     setField("CSS_ID_CSSA", BIG_DECIMAL);
     //modifica relativa al tipo istituto
     setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
     setField("AUT_EST_ID_AUTORITA_EST_DELEG", BIG_DECIMAL);
     setField("AVV_ID_AVVOCATO_FASCICOLO_SIGE", BIG_DECIMAL);
     setField("CUR_ID_CURATORE", BIG_DECIMAL);
     setField("ID_PARTE_UDIENZA", BIG_DECIMAL);
     setField("FLAG_NOTIFICA_VIA_FAX", BIG_DECIMAL);
     setField("ID_CIVILMENTE_OBBLIGATO", BIG_DECIMAL); //MEV_2023-13 
  }

  //
  // METODI GET()
  //
  public BigDecimal 		 getIdNotifica() 					 throws DAOException	    { return getBigDecimal("ID_NOTIFICA"); }
  public String 	  		 getCodTipoNotifica() 				 throws DAOException	    { return getString("COD_TIPO_NOTIFICA"); }
  public String 	  		 getCodUffUepeUssmSS() 				 throws DAOException	    { return getString("COD_UFF_UEPE_USSM_SS"); }
  public String 	  		 getCodUffUdsUdsm() 				 throws DAOException	    { return getString("COD_UFF_UDS_UDSM"); }
  public String 	  		 getCodUffTdsTdsm() 				 throws DAOException	    { return getString("COD_UFF_TDS_TDSM"); }
  public Date 	    		 getDataAvvenutaNotifica() 			 throws DAOException	    { return getDate("DATA_AVVENUTA_NOTIFICA"); }
  public Date 		       	 getDataInvio() 					 throws DAOException	    { return getDate("DATA_INVIO"); }
  public String 		   	 getCodEsito() 						 throws DAOException	    { return getString("COD_ESITO"); }
  public String 			 getNote() 							 throws DAOException	    { return getString("NOTE"); }
  public String 			 getCodOperatoreInserimento() 		 throws DAOException	    { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 			     getDataInserimento() 				 throws DAOException	    { return getDate("DATA_INSERIMENTO"); }
  public String 			 getCodUfficioInserimento() 		 throws DAOException	    { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 			 getCodiceOperatoreAggiornamento() 	 throws DAOException	    { return getString("CODICE_OPERATORE_AGGIORNAMENTO"); }
  public Date 			     getDataAggiornamento() 		     throws DAOException	    { return getDate("DATA_AGGIORNAMENTO"); }
  public String 			 getCodUfficioAggiornamento() 		 throws DAOException	    { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal 		 getEveIdEvento() 		             throws DAOException	    { return getBigDecimal("EVE_ID_EVENTO"); }
  public BigDecimal 		 getAutEstIdAutoritaEsterna() 		 throws DAOException	    { return getBigDecimal("AUT_EST_ID_AUTORITA_ESTERNA"); }
  public BigDecimal 		 getAvvIdAvvocatoFascicoloSiep() 	 throws DAOException	    { return getBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SIEP"); }
  public BigDecimal 		 getAvvIdAvvocatoFascicoloSius() 	 throws DAOException	    { return getBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SIUS"); }
  public BigDecimal 		 getSogIdSoggetto() 				 throws DAOException	    { return getBigDecimal("SOG_ID_SOGGETTO"); }
  public String 		     getUffCodUfficio() 				 throws DAOException	    { return getString("UFF_COD_UFFICIO"); }
  public BigDecimal 		 getSollecito() 		    		 throws DAOException	    { return getBigDecimal("SOLLECITO"); }
  public BigDecimal 		 getCssIdCssa() 		    		 throws DAOException	    { return getBigDecimal("CSS_ID_CSSA"); }
  //modifica relativa al tipo istituto
  public String 		     getIstDetIdIstitutoDetenzione() 	 throws DAOException	    { return getString("IST_DET_ID_ISTITUTO_DETENZIONE"); }
  public BigDecimal          getAutEstIdAutoritaEstDeleg()       throws DAOException        { return getBigDecimal("AUT_EST_ID_AUTORITA_EST_DELEG"); }
  public BigDecimal 		 getAvvIdAvvocatoFascicoloSige() 	 throws DAOException	    { return getBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SIGE"); }
  public BigDecimal 		 getCurIdCuratore() 				 throws DAOException	    { return getBigDecimal("CUR_ID_CURATORE"); }
  public BigDecimal 		 getFlagNotificaViaFax() 			 throws DAOException	    { return getBigDecimal("FLAG_NOTIFICA_VIA_FAX"); }
  public BigDecimal 		 getIdParteUdienza() 				 throws DAOException	    { return getBigDecimal("ID_PARTE_UDIENZA"); }
  
  // MEV_2023-13 
  public BigDecimal          getIdCivilmenteObbligato()          throws DAOException        { return getBigDecimal("ID_CIVILMENTE_OBBLIGATO"); }
  
  //
  // METODI SET()
  //
  public void setIdNotifica(BigDecimal aValore ) 			          { setBigDecimal("ID_NOTIFICA", aValore); }
  public void setCodTipoNotifica(String aValore ) 			          { setString("COD_TIPO_NOTIFICA", aValore); }
  public void setCodUffUepeUssmSS(String aValore ) 			          { setString("COD_UFF_UEPE_USSM_SS", aValore); }
  public void setCodUffUdsUdsm(String aValore ) 			          { setString("COD_UFF_UDS_UDSM", aValore); }
  public void setCodUffTdsTdsm(String aValore ) 			          { setString("COD_UFF_TDS_TDSM", aValore); }
  public void setDataAvvenutaNotifica(Date aValore ) 			      { setDate("DATA_AVVENUTA_NOTIFICA", aValore); }
  public void setDataInvio(Date aValore ) 			                  { setDate("DATA_INVIO", aValore); }
  public void setCodEsito(String aValore ) 			                  { setString("COD_ESITO", aValore); }
  public void setNote(String aValore ) 			                      { setString("NOTE", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 			  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 			          { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 			  { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodiceOperatoreAggiornamento(String aValore ) 	      { setString("CODICE_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 			          { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 		  { setString("CODICE_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 			  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setEveIdEvento(BigDecimal aValore ) 			          { setBigDecimal("EVE_ID_EVENTO", aValore); }
  public void setAutEstIdAutoritaEsterna(BigDecimal aValore ) 		  { setBigDecimal("AUT_EST_ID_AUTORITA_ESTERNA", aValore); }
  public void setSogIdSoggetto(BigDecimal aValore ) 			      { setBigDecimal("SOG_ID_SOGGETTO", aValore); }
  public void setAvvIdAvvocatoFascicoloSiep(BigDecimal aValore )      { setBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SIEP", aValore); }
  public void setAvvIdAvvocatoFascicoloSius(BigDecimal aValore )      { setBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SIUS", aValore); }
  public void setUffCodUfficio(String aValore ) 			          { setString("UFF_COD_UFFICIO", aValore); }
  public void setSollecito(BigDecimal aValore ) 			          { setBigDecimal("SOLLECITO", aValore); }
  public void setCssIdCssa(BigDecimal aValore ) 			          { setBigDecimal("CSS_ID_CSSA", aValore); }
  //modifica relativa al tipo istituto
  public void setIstDetIdIstitutoDetenzione(String aValore ) 		  { setString("IST_DET_ID_ISTITUTO_DETENZIONE", aValore); }
  public void setAutEstIdAutoritaEstDeleg(BigDecimal aValore )        { setBigDecimal("AUT_EST_ID_AUTORITA_EST_DELEG", aValore); }
  public void setAvvIdAvvocatoFascicoloSige(BigDecimal aValore )      { setBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SIGE", aValore); }
  public void setCurIdCuratore(BigDecimal aValore )					  { setBigDecimal("CUR_ID_CURATORE", aValore); }
  public void setFlagNotificaViaFax(BigDecimal aValore )			  { setBigDecimal("FLAG_NOTIFICA_VIA_FAX", aValore); }
  public void setIdParteUdienza(BigDecimal aValore )				  { setBigDecimal("ID_PARTE_UDIENZA", aValore); }
  // MEV_2023-13 
  public void setIdCivilmenteObbligato (BigDecimal aValore )           { setBigDecimal("ID_CIVILMENTE_OBBLIGATO", aValore); }   
  
	public GenericModel getModel() throws DAOException
  {
	  return new NotificaModel(
								 getIdNotifica() ,
								 getCodTipoNotifica() ,
								 getCodUffUepeUssmSS() ,
								 getCodUffUdsUdsm() ,
								 getCodUffTdsTdsm() ,
								 "",
								 getDataAvvenutaNotifica() ,
								 getDataInvio() ,
								 getCodEsito() ,
								 "",
								 getNote() ,
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodiceOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getEveIdEvento() ,
								 getAutEstIdAutoritaEsterna() ,
								 getSogIdSoggetto() ,getAvvIdAvvocatoFascicoloSiep() ,
								 getAvvIdAvvocatoFascicoloSius() ,
								 getAvvIdAvvocatoFascicoloSige() ,
								 getUffCodUfficio(),
								 getSollecito(),
								 getCssIdCssa(),
                 null,
                //modifica relativa al tipo istituto
                 getIstDetIdIstitutoDetenzione(),
                 null,
                 getCurIdCuratore(),
				 getFlagNotificaViaFax(),
                 getIdParteUdienza()
                 , null  //MEV_2023-13 
								);
		}

    public void setDAOFromModel(NotificaModel aModel) throws DAOException
    {
      setIdNotifica( aModel.getIdNotifica() );
      setCodTipoNotifica( aModel.getCodTipoNotifica() );
      setCodUffUepeUssmSS( aModel.getCodUffUepeUssmSS() );
      setCodUffUdsUdsm( aModel.getCodUffUdsUdsm() );
      setCodUffTdsTdsm( aModel.getCodUffTdsTdsm() );
      setDataAvvenutaNotifica( aModel.getDataAvvenutaNotifica() );
      setDataInvio( aModel.getDataInvio() );
      setCodEsito( aModel.getCodEsito() );
      setNote( aModel.getNote() );
      setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
      setDataInserimento( aModel.getDataInserimento() );
      setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
      setCodiceOperatoreAggiornamento( aModel.getCodiceOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setEveIdEvento( aModel.getEveIdEvento() );
      setAutEstIdAutoritaEsterna( aModel.getAutEstIdAutoritaEsterna() );
      setSogIdSoggetto( aModel.getSogIdSoggetto() );
      setAvvIdAvvocatoFascicoloSiep( aModel.getAvvIdAvvocatoFascicoloSiep() );
      setAvvIdAvvocatoFascicoloSius( aModel.getAvvIdAvvocatoFascicoloSius() );
      setAvvIdAvvocatoFascicoloSige( aModel.getAvvIdAvvocatoFascicoloSige() );
      setUffCodUfficio( aModel.getUffCodUfficio() );
      setSollecito( aModel.getSollecito() );
      setCssIdCssa( aModel.getCssIdCssa() );
      //modifica relativa al tipo istituto
      setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
      setAutEstIdAutoritaEstDeleg( aModel.getAutEstIdAutoritaEstDeleg() );
      setCurIdCuratore( aModel.getCurIdCuratore() );
      setFlagNotificaViaFax (aModel.getFlagNotificaViaFax());
      setIdParteUdienza( aModel.getIdParteUdienza() );
      // MEV_2023-13 
      setIdCivilmenteObbligato ( aModel.getIdCivilmenteObbligato() );
   }

  public void setDAOFromModelForUpdate(NotificaModel aModel) throws DAOException
  {
     setIdNotifica( aModel.getIdNotifica() );
     setCodTipoNotifica( aModel.getCodTipoNotifica() );
     setCodUffUepeUssmSS( aModel.getCodUffUepeUssmSS() );
     setCodUffUdsUdsm( aModel.getCodUffUdsUdsm() );
     setCodUffTdsTdsm( aModel.getCodUffTdsTdsm() );
     setDataAvvenutaNotifica( aModel.getDataAvvenutaNotifica() );
     setDataInvio( aModel.getDataInvio() );
     setCodEsito( aModel.getCodEsito() );
     setNote( aModel.getNote() );
     setCodiceOperatoreAggiornamento( aModel.getCodiceOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setEveIdEvento( aModel.getEveIdEvento() );
     setAutEstIdAutoritaEsterna( aModel.getAutEstIdAutoritaEsterna() );
     setSogIdSoggetto( aModel.getSogIdSoggetto() );
     setAvvIdAvvocatoFascicoloSiep( aModel.getAvvIdAvvocatoFascicoloSiep() );
     setAvvIdAvvocatoFascicoloSius( aModel.getAvvIdAvvocatoFascicoloSius() );
     setAvvIdAvvocatoFascicoloSige( aModel.getAvvIdAvvocatoFascicoloSige() );
     setUffCodUfficio( aModel.getUffCodUfficio() );
     setSollecito( aModel.getSollecito() );
     setCssIdCssa( aModel.getCssIdCssa() );
     setCondizioneUpdate(aModel.getIdNotifica());
     //modifica relativa al tipo istituto
     setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
     setAutEstIdAutoritaEstDeleg( aModel.getAutEstIdAutoritaEstDeleg() );
     setCurIdCuratore( aModel.getCurIdCuratore() );
     setFlagNotificaViaFax (aModel.getFlagNotificaViaFax());
     // MEV_2023-13 
     setIdCivilmenteObbligato ( aModel.getIdCivilmenteObbligato() );
  }

  // Update del SOG_ID_SOGGETTO per le NOTIFICHE legate ad EVENTI collegati ad un FASCICOLO SIUS.
  public void setDAOFromModelForUpdateIdSoggetto(EventoModel aModel, BigDecimal aIdSoggetto) throws DAOException
  {
    setSogIdSoggetto(aIdSoggetto);
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    selCondizioneUpdateXIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
  }

  // Update del SOG_ID_SOGGETTO per le NOTIFICHE collegate ad un FASCICOLO SIUS.
  public void setDAOFromModelForUpdateIdSoggetto(FascicoloSiusModel aFascicoloUnificante, FascicoloSiusModel aFascicoloUnificato ) throws DAOException
  {
    setSogIdSoggetto(aFascicoloUnificante.getSogIdSoggetto());
    setDataAggiornamento( aFascicoloUnificante.getDataAggiornamento() );
    setCodUfficioAggiornamento( aFascicoloUnificante.getCodUfficioAggiornamento() );
    setCodOperatoreAggiornamento( aFascicoloUnificante.getCodOperatoreAggiornamento() );
    selCondizioneUpdateXIdFascicoloSiusIdSoggetto(aFascicoloUnificato.getIdFascicoloSius(), aFascicoloUnificato.getSogIdSoggetto());
  }

	public void setCondizione(NotificaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

	public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_NOTIFICA = " + key );
  }
	
	public void setCondizioneAvvIdAvvocatoFascicoloSiep(BigDecimal key)
  {
    setCondition(" AVV_ID_AVVOCATO_FASCICOLO_SIEP = " + key );
  }
	
	public void setCondizioneEvento(BigDecimal key)
  {
    setCondition(" EVE_ID_EVENTO = " + key );
  }

  public void selCondizioneUpdateXIdFascicoloSius(BigDecimal key)
  {
    setCondition(" EVE_ID_EVENTO in (select id_evento from evento, fascicolo_sius where fascicolo_sius.id_fascicolo_sius = " + key +" and evento.fas_siu_id_fascicolo_sius = fascicolo_sius.id_fascicolo_sius)");
  }

  public void selCondizioneUpdateXIdFascicoloSiusIdSoggetto(BigDecimal aIdFascicoloSius, BigDecimal aIdSoggetto )
  {
    setCondition(" EVE_ID_EVENTO in (select id_evento from evento, fascicolo_sius where fascicolo_sius.id_fascicolo_sius = " + aIdFascicoloSius +" and notifica.sog_id_soggetto = " + aIdSoggetto +" and evento.fas_siu_id_fascicolo_sius = fascicolo_sius.id_fascicolo_sius)");
  }
  
  /**
   * Setta le condizioni di update per i record di Notifiche legati ad un Soggetto e ad un Provvedimento SIGE legato ad un Fascicolo SIGE.
   * @param aIdFascicoloSige
   * @param aIdSoggetto
   */
  public void selCondizioneUpdateXIdFascicoliSigeIdSoggetto(BigDecimal aIdFascicoloSige, BigDecimal aIdSoggetto )
  {
	String lCondizione = " SOG_ID_SOGGETTO = " + aIdSoggetto;
	lCondizione += " and EVE_ID_EVENTO in ( select ID_EVENTO_GENERATO from PROVVEDIMENTO_SIGE WHERE FAS_ID_FASCICOLO_SIGE = " + aIdFascicoloSige + ") ";
    setCondition(lCondizione);
  }

  public void selCondizioneDeleteByIdParteUdienza(BigDecimal aIdParteUdienza)
  {
    setCondition(" ID_PARTE_UDIENZA = "+ aIdParteUdienza);
  }

}