package siap.siep.posizione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: PosizioneGiuridicaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class PosizioneGiuridicaDAO extends SIAPTableDAO
{
  public PosizioneGiuridicaDAO (Connection con)
  {
    super(con);

    setTable("POSIZIONE_GIURIDICA");

    setSequenceField("ID_POSIZIONE_GIURIDICA", "POS_GIU_SEQ");

    setFieldKey("ID_POSIZIONE_GIURIDICA", BIG_DECIMAL);

    setField("ID_POSIZIONE_GIURIDICA", BIG_DECIMAL);
    setField("COD_POSIZIONE_GIURIDICA", STRING);
    setField("DATA_INIZIO", DATE);
    setField("DATA_FINE", DATE);
    setField("COD_POSIZIONE_PROCESSUALE", STRING);
    setField("NOTE", STRING);
    setField("LUOGO_PROVA_AFFIDAMENTO", STRING);
    setField("LUOGO_LAVORO_SEMILIBERTA", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
    setField("ID_EVENTO_RIFERIMENTO", BIG_DECIMAL);
    
    setField("LUOGO_ESPIAZIONE", STRING);
    setField("AUTORITA_COMPETENTE", STRING);
    setField("AUTORITA_COMPETENTE_SEDE", STRING);
    setField("AUTORITA_COMPETENTE_INDIRIZZO", STRING);
    setField("COD_MASCHERA", STRING); 
    setField("ALT_CAU_ID_ALTRA_CAUSA", BIG_DECIMAL);
    
  }

  //
  // METODI GET()
  //
  public BigDecimal getIdPosizioneGiuridica() 	throws DAOException	      { return getBigDecimal("ID_POSIZIONE_GIURIDICA"); }
  public String getCodPosizioneGiuridica() 	throws DAOException	          { return getString("COD_POSIZIONE_GIURIDICA"); }
  public Date 	getDataInizio() 		throws DAOException	                  { return getDate("DATA_INIZIO"); }
  public Date 	getDataFine() 		        throws DAOException	            { return getDate("DATA_FINE"); }
  public String getCodPosizioneProcessuale() 	throws DAOException	        { return getString("COD_POSIZIONE_PROCESSUALE"); }
  public String getNote() 		        throws DAOException	                { return getString("NOTE"); }
  public String getLuogoProvaAffidamento() 		throws DAOException	        { return getString("LUOGO_PROVA_AFFIDAMENTO"); }
  public String getLuogoLavoroSemiliberta() 		throws DAOException	      { return getString("LUOGO_LAVORO_SEMILIBERTA"); }
  public String getCodOperatoreInserimento() 	throws DAOException	        { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 	getDataInserimento() 		throws DAOException	              { return getDate("DATA_INSERIMENTO"); }
  public String getCodUfficioInserimento() 	throws DAOException	          { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String getCodOperatoreAggiornamento() 	throws DAOException	      { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 	getDataAggiornamento() 		throws DAOException	            { return getDate("DATA_AGGIORNAMENTO"); }
  public String getCodUfficioAggiornamento() 	throws DAOException	        { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal getFasSieIdFascicoloSiep() 	throws DAOException	      { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public BigDecimal getIdEventoRiferimento() 	throws DAOException	        { return getBigDecimal("ID_EVENTO_RIFERIMENTO"); }  

  public String getLuogoEspiazione() 	throws DAOException	        { return getString("LUOGO_ESPIAZIONE"); }  
  public String getAutoritaCompetente() 	throws DAOException	        { return getString("AUTORITA_COMPETENTE"); }  
  public String getAutoritaCompetenteSede() 	throws DAOException	        { return getString("AUTORITA_COMPETENTE_SEDE"); }  
  public String getAutoritaCompetenteIndirizzo() 	throws DAOException	        { return getString("AUTORITA_COMPETENTE_INDIRIZZO"); }  
  public String getCodMaschera() 	throws DAOException	        { return getString("COD_MASCHERA"); }  
  public BigDecimal getAltCauIdAltraCausa() 	throws DAOException	        { return getBigDecimal("ALT_CAU_ID_ALTRA_CAUSA"); }  
  

  //
  // METODI SET()
  //
  public void setIdPosizioneGiuridica(BigDecimal aValore ) 	    { setBigDecimal("ID_POSIZIONE_GIURIDICA", aValore); }
  public void setCodPosizioneGiuridica(String aValore ) 	      { setString("COD_POSIZIONE_GIURIDICA", aValore); }
  public void setDataInizio(Date aValore ) 			                { setDate("DATA_INIZIO", aValore); }
  public void setDataFine(Date aValore ) 			                  { setDate("DATA_FINE", aValore); }
  public void setCodPosizioneProcessuale(String aValore ) 	    { setString("COD_POSIZIONE_PROCESSUALE", aValore); }
  public void setNote(String aValore ) 			                    { setString("NOTE", aValore); }
  public void setLuogoProvaAffidamento(String aValore ) 			  { setString("LUOGO_PROVA_AFFIDAMENTO", aValore); }
  public void setLuogoLavoroSemiliberta(String aValore ) 			  { setString("LUOGO_LAVORO_SEMILIBERTA", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 	    { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 		            { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 	      { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 	  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 		          { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 	    { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setFasSieIdFascicoloSiep(BigDecimal aValore ) 	  { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void setIdEventoRiferimento(BigDecimal aValore ) 	    { setBigDecimal("ID_EVENTO_RIFERIMENTO", aValore); }

  public void setLuogoEspiazione(String aValore) {setString("LUOGO_ESPIAZIONE", aValore); }
  public void setAutoritaCompetente(String aValore) {setString("AUTORITA_COMPETENTE", aValore); }
  public void setAutoritaCompetenteSede(String aValore) {setString("AUTORITA_COMPETENTE_SEDE", aValore); }
  public void setAutoritaCompetenteIndirizzo(String aValore) {setString("AUTORITA_COMPETENTE_INDIRIZZO", aValore); }
  public void setCodMaschera(String aValore) {setString("COD_MASCHERA", aValore); }
  public void setAltCauIdAltraCausa(BigDecimal aValore) {setBigDecimal("ALT_CAU_ID_ALTRA_CAUSA", aValore); }
  
  public GenericModel getModel() throws DAOException
  {
    return new PosizioneGiuridicaModel(
                                       getIdPosizioneGiuridica() ,
                                       getCodPosizioneGiuridica() ,
                                       "",
                                       getDataInizio() ,
                                       getDataFine() ,
                                       getCodPosizioneProcessuale() ,
                                       "",
                                       getNote() ,
                                       getLuogoProvaAffidamento() ,
                                       getLuogoLavoroSemiliberta() ,
                                       getCodOperatoreInserimento() ,
                                       getDataInserimento() ,
                                       getCodUfficioInserimento() ,
                                       "",
                                       getCodOperatoreAggiornamento() ,
                                       getDataAggiornamento() ,
                                       getCodUfficioAggiornamento() ,
                                       "",
                                       getFasSieIdFascicoloSiep(),
                                       getIdEventoRiferimento(),
                                       "",
                                       getLuogoEspiazione(),
                                       getAutoritaCompetente(),
                                       getAutoritaCompetenteSede(),
                                       getAutoritaCompetenteIndirizzo(),
                                       getCodMaschera(),
                                       getAltCauIdAltraCausa()
                                      );
  }

  public void setDAOFromModel(PosizioneGiuridicaModel aModel) throws DAOException
  {
    setIdPosizioneGiuridica( aModel.getIdPosizioneGiuridica() );
    setCodPosizioneGiuridica( aModel.getCodPosizioneGiuridica() );
    setDataInizio( aModel.getDataInizio() );
    setDataFine( aModel.getDataFine() );
    setCodPosizioneProcessuale( aModel.getCodPosizioneProcessuale() );
    setNote( aModel.getNote() );
    setLuogoProvaAffidamento( aModel.getLuogoProvaAffidamento() );
    setLuogoLavoroSemiliberta( aModel.getLuogoLavoroSemiliberta() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setIdEventoRiferimento( aModel.getIdEventoRiferimento() );

    setLuogoEspiazione( aModel.getLuogoEspiazione() );
    setAutoritaCompetente( aModel.getAutoritaCompetente() );
    setAutoritaCompetenteSede( aModel.getAutoritaCompetenteSede() );
    setAutoritaCompetenteIndirizzo( aModel.getAutoritaCompetenteIndirizzo() );
    setCodMaschera( aModel.getCodMaschera() );
    setAltCauIdAltraCausa(aModel.getAltCauIdAltraCausa());

  }

  public void setDAOFromModelForUpdate(PosizioneGiuridicaModel aModel) throws DAOException
  {
    //setIdPosizioneGiuridica( aModel.getIdPosizioneGiuridica() );
    setCodPosizioneGiuridica( aModel.getCodPosizioneGiuridica() );
    setDataInizio( aModel.getDataInizio() );
    setDataFine( aModel.getDataFine() );
    setCodPosizioneProcessuale( aModel.getCodPosizioneProcessuale() );
    //setNote( aModel.getNote() );
    setLuogoProvaAffidamento( aModel.getLuogoProvaAffidamento() );
    setLuogoLavoroSemiliberta( aModel.getLuogoLavoroSemiliberta() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    //setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    //setIdEventoRiferimento( aModel.getIdEventoRiferimento() );
    setCodMaschera(aModel.getCodMaschera());
    setAltCauIdAltraCausa(aModel.getAltCauIdAltraCausa());
    setCondizioneUpdate(aModel.getIdPosizioneGiuridica());
  }

  public void setCondizione(PosizioneGiuridicaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_POSIZIONE_GIURIDICA = " + key );
  }

  public void setCondizioneUpdateCampoLegatoAltraCausa(BigDecimal key)
  {
    setCondition(" ALT_CAU_ID_ALTRA_CAUSA = " + key );
  }
  
  public void setCondizioneIdFascicolo(BigDecimal key)
  {
    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + key );
  }

  public void setOrdinamentoPerIdPosizione()
  {
    setCondition(" ORDER BY ID_POSIZIONE_GIURIDICA ASC" );
  }
  public void setCondizioneIdFascicoloOrderDataInserimento(BigDecimal aIdFascicolo)
  {
    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo  + " ORDER BY DATA_INSERIMENTO ASC");
  }


}
