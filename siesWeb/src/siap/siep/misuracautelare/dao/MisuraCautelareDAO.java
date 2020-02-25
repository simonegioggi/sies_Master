package siap.siep.misuracautelare.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>Title: MisuraCautelareDAO</p>
 * <p>Description: Classe DAO che rappresenta la tabella MisuraCautelare</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class MisuraCautelareDAO extends SIAPTableDAO
{
	public MisuraCautelareDAO (Connection con)
	{
    super(con);
    setTable("MISURA_CAUTELARE");

    //Settare la Sequence e i campi chiave
    setSequenceField("ID_MISURA_CAUTELARE", "MIS_CAU_SEQ");
    setFieldKey("ID_MISURA_CAUTELARE", BIG_DECIMAL);

    setField("ID_MISURA_CAUTELARE", BIG_DECIMAL);
    setField("COD_TIPO_MISURA", STRING);
    setField("DATA_INIZIO", DATE);
    setField("DATA_FINE", DATE);
    setField("DATA_EMISSIONE_ORDINANZA", DATE);
    setField("NUM_ANNI", BIG_DECIMAL);
    setField("NUM_MESI", BIG_DECIMAL);
    setField("NUM_GIORNI", BIG_DECIMAL);
    setField("GIORNI", BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
    setField("EVE_ID_EVENTO", BIG_DECIMAL);
    setField("FLAG_COMPUTABILE", STRING);
    setField("FLAG_MODIFICA_MANUALE", STRING);
    //nuovi
    setField("COD_MOTIVO_NON_COMPUTABILE", STRING);
//modifica relativa al tipo istituto
    setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
    //setField("COD_TIPO_ISTITUTO_DETENZIONE", STRING);
    setField("ALTRO_LUOGO_DETENZIONE", STRING);
    //setField("COD_LUOGO_DETENZIONE", STRING);
    setField("NUM_RIFER", STRING);
    setField("COD_TIPO_UFFICIO_RIFER", STRING);
    setField("COD_LUOGO_UFFICIO_RIFER", STRING);
    setField("DATA_FUNGIBILITA", DATE);
    setField("NOTE", STRING);
    
    //misure cautelari computabili e non computabili 
	 setField("ANNO_FASC_BDMC", BIG_DECIMAL);
	 setField("NUME_FASC_BDMC", BIG_DECIMAL);
	 setField("ANNO_RGNR", BIG_DECIMAL);
	 setField("NUMERO_RGNR", BIG_DECIMAL);
	 setField("ANNO_REG_GEN", BIG_DECIMAL);
	 setField("NUMERO_REG_GEN", BIG_DECIMAL);
	 setField("TIPO_UFFICIO_REG_GEN", STRING);
	 setField("AUTORITA_EMITTENTE", STRING);
	 setField("AUTORITA_EMITTENTE_LUOGO", STRING);
	 setField("AUTORITA_COMPETENTE", STRING);
	 setField("AUTORITA_COMPETENTE_SEDE", STRING);
	 setField("AUTORITA_COMPETENTE_INDIRIZZO", STRING);
	 setField("ANNO_RIFER", BIG_DECIMAL);
	 setField("CODICE_UFFICIO_PM_SEDE", STRING);
	 setField("POS_GIU_ID_POSIZIONE_GIURIDICA", BIG_DECIMAL);

	}

  //
  // METODI GET()
  //
  public BigDecimal 		 getIdMisuraCautelare() 		throws DAOException	 { return getBigDecimal("ID_MISURA_CAUTELARE"); }
  public String 				 getCodTipoMisura() 		throws DAOException	 { return getString("COD_TIPO_MISURA"); }
  public Date 					 getDataInizio() 		throws DAOException	 { return getDate("DATA_INIZIO"); }
  public Date 					 getDataFine() 		throws DAOException	 { return getDate("DATA_FINE"); }
  public Date 					 getDataEmissioneOrdinanza() 		throws DAOException	 { return getDate("DATA_EMISSIONE_ORDINANZA"); }
  public BigDecimal 		 getNumAnni() 		throws DAOException	 { return getBigDecimal("NUM_ANNI"); }
  public BigDecimal 		 getNumMesi() 		throws DAOException	 { return getBigDecimal("NUM_MESI"); }
  public BigDecimal 		 getNumGiorni() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI"); }
  public BigDecimal 		 getGiorni() 			throws DAOException	 { return getBigDecimal("GIORNI"); }
  public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal 		 getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public BigDecimal 		 getEveIdEvento() 		throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }
  public String 		 getFlagComputabile() 		throws DAOException	 { return getString("FLAG_COMPUTABILE"); }
  public String 		 getFlagModificaManuale() 	throws DAOException	 { return getString("FLAG_MODIFICA_MANUALE"); }
  //nuovi
  public String 				 getCodMotivoNonComputabile() 		throws DAOException	 { return getString("COD_MOTIVO_NON_COMPUTABILE"); }
//modifica relativa al tipo istituto
  public String                                  getIstDetIdIstitutoDetenzione()          throws DAOException	 { return getString("IST_DET_ID_ISTITUTO_DETENZIONE"); }
//  public String 				 getCodTipoIstitutoDetenzione() 		throws DAOException	 { return getString("COD_TIPO_ISTITUTO_DETENZIONE"); }
  public String 				 getAltroLuogoDetenzione() 		throws DAOException	 { return getString("ALTRO_LUOGO_DETENZIONE"); }
//  public String 				 getCodLuogoDetenzione() 		throws DAOException	 { return getString("COD_LUOGO_DETENZIONE"); }
  public String 				 getNumRifer() 		throws DAOException	 { return getString("NUM_RIFER"); }
  public String 				 getCodTipoUfficioRifer() 		throws DAOException	 { return getString("COD_TIPO_UFFICIO_RIFER"); }
  public String 				 getCodLuogoUfficioRifer() 		throws DAOException	 { return getString("COD_LUOGO_UFFICIO_RIFER"); }
  public Date 					 getDataFungibilita() 		throws DAOException	 { return getDate("DATA_FUNGIBILITA"); }
  public String 				 getNote() 		throws DAOException	 { return getString("NOTE"); }

	//misure cautelari computabili e non computabili 
	public BigDecimal 		 getAnnoFascBdmc() 		throws DAOException	 { return getBigDecimal("ANNO_FASC_BDMC"); } 
	public BigDecimal 		 getNumeFascBdmc() 		throws DAOException	 { return getBigDecimal("NUME_FASC_BDMC"); } 
	public BigDecimal 		 getAnnoRgnr() 		throws DAOException	 { return getBigDecimal("ANNO_RGNR"); } 
	public BigDecimal 		 getNumeroRgnr() 		throws DAOException	 { return getBigDecimal("NUMERO_RGNR"); } 
	public BigDecimal 		 getAnnoRegGen() 		throws DAOException	 { return getBigDecimal("ANNO_REG_GEN"); } 
	public BigDecimal 		 getNumeroRegGen() 		throws DAOException	 { return getBigDecimal("NUMERO_REG_GEN"); } 
	public String 				 getTipoUfficioRegGen() 		throws DAOException	 { return getString("TIPO_UFFICIO_REG_GEN"); } 
	public String 				 getAutoritaEmittente() 		throws DAOException	 { return getString("AUTORITA_EMITTENTE"); } 
	public String 				 getAutoritaEmittenteLuogo() 		throws DAOException	 { return getString("AUTORITA_EMITTENTE_LUOGO"); } 
	public String 				 getAutoritaCompetente() 		throws DAOException	 { return getString("AUTORITA_COMPETENTE"); } 
	public String 				 getAutoritaCompetenteSede() 		throws DAOException	 { return getString("AUTORITA_COMPETENTE_SEDE"); } 
	public String 				 getAutoritaCompetenteIndirizzo() 		throws DAOException	 { return getString("AUTORITA_COMPETENTE_INDIRIZZO"); } 
	public BigDecimal 		 getAnnoRifer() 		throws DAOException	 { return getBigDecimal("ANNO_RIFER"); } 
	public String 				 getCodiceUfficioPmSede() 		throws DAOException	 { return getString("CODICE_UFFICIO_PM_SEDE"); } 
	public BigDecimal 		 getPosGiuIdPosizioneGiuridica() 		throws DAOException	 { return getBigDecimal("POS_GIU_ID_POSIZIONE_GIURIDICA"); } 

  //
  // METODI SET()
  //
  public void  	 setIdMisuraCautelare(BigDecimal aValore ) 			 { setBigDecimal("ID_MISURA_CAUTELARE", aValore); }
  public void  	 setCodTipoMisura(String aValore ) 			 { setString("COD_TIPO_MISURA", aValore); }
  public void  	 setDataInizio(Date aValore ) 			 { setDate("DATA_INIZIO", aValore); }
  public void  	 setDataFine(Date aValore ) 			 { setDate("DATA_FINE", aValore); }
  public void  	 setDataEmissioneOrdinanza(Date aValore ) 			 { setDate("DATA_EMISSIONE_ORDINANZA", aValore); }
  public void  	 setNumAnni(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI", aValore); }
  public void  	 setNumMesi(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI", aValore); }
  public void  	 setNumGiorni(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI", aValore); }
  public void  	 setGiorni(BigDecimal aValore ) 			 { setBigDecimal("GIORNI", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void  	 setEveIdEvento(BigDecimal aValore ) 			 { setBigDecimal("EVE_ID_EVENTO", aValore); }
  public void  	 setFlagComputabile(String aValore ) 			 { setString("FLAG_COMPUTABILE", aValore); }
  public void  	 setFlagModificaManuale(String aValore ) 			 { setString("FLAG_MODIFICA_MANUALE", aValore); }
  //nuovi
  public void  	 setCodMotivoNonComputabile(String aValore ) 			 { setString("COD_MOTIVO_NON_COMPUTABILE", aValore); }
//modifica relativa al tipo istituto
  public void  setIstDetIdIstitutoDetenzione(String aValore )         	{  setString("IST_DET_ID_ISTITUTO_DETENZIONE",  aValore); }
//  public void  	 setCodTipoIstitutoDetenzione(String aValore ) 			 { setString("COD_TIPO_ISTITUTO_DETENZIONE", aValore); }
  public void  	 setAltroLuogoDetenzione(String aValore ) 			 { setString("ALTRO_LUOGO_DETENZIONE", aValore); }
//  public void  	 setCodLuogoDetenzione(String aValore ) 			 { setString("COD_LUOGO_DETENZIONE", aValore); }
  public void  	 setNumRifer(String aValore ) 			 { setString("NUM_RIFER", aValore); }
  public void  	 setCodTipoUfficioRifer(String aValore ) 			 { setString("COD_TIPO_UFFICIO_RIFER", aValore); }
  public void  	 setCodLuogoUfficioRifer(String aValore ) 			 { setString("COD_LUOGO_UFFICIO_RIFER", aValore); }
  public void  	 setDataFungibilita(Date aValore ) 			 { setDate("DATA_FUNGIBILITA", aValore); }
  public void  	 setNote(String aValore ) 			 { setString("NOTE", aValore); }

	public void  	 setAnnoFascBdmc(BigDecimal aValore ) 			 { setBigDecimal("ANNO_FASC_BDMC", aValore); } 
	public void  	 setNumeFascBdmc(BigDecimal aValore ) 			 { setBigDecimal("NUME_FASC_BDMC", aValore); } 
	public void  	 setAnnoRgnr(BigDecimal aValore ) 			 { setBigDecimal("ANNO_RGNR", aValore); } 
	public void  	 setNumeroRgnr(BigDecimal aValore ) 			 { setBigDecimal("NUMERO_RGNR", aValore); } 
	public void  	 setAnnoRegGen(BigDecimal aValore ) 			 { setBigDecimal("ANNO_REG_GEN", aValore); } 
	public void  	 setNumeroRegGen(BigDecimal aValore ) 			 { setBigDecimal("NUMERO_REG_GEN", aValore); } 
	public void  	 setTipoUfficioRegGen(String aValore ) 			 { setString("TIPO_UFFICIO_REG_GEN", aValore); } 
	public void  	 setAutoritaEmittente(String aValore ) 			 { setString("AUTORITA_EMITTENTE", aValore); } 
	public void  	 setAutoritaEmittenteLuogo(String aValore ) 			 { setString("AUTORITA_EMITTENTE_LUOGO", aValore); } 
	public void  	 setAutoritaCompetente(String aValore ) 			 { setString("AUTORITA_COMPETENTE", aValore); } 
	public void  	 setAutoritaCompetenteSede(String aValore ) 			 { setString("AUTORITA_COMPETENTE_SEDE", aValore); } 
	public void  	 setAutoritaCompetenteIndirizzo(String aValore ) 			 { setString("AUTORITA_COMPETENTE_INDIRIZZO", aValore); } 
	public void  	 setAnnoRifer(BigDecimal aValore ) 			 { setBigDecimal("ANNO_RIFER", aValore); } 
	public void  	 setCodiceUfficioPmSede(String aValore ) 			 { setString("CODICE_UFFICIO_PM_SEDE", aValore); } 
	public void  	 setPosGiuIdPosizioneGiuridica(BigDecimal aValore ) 			 { setBigDecimal("POS_GIU_ID_POSIZIONE_GIURIDICA", aValore); } 

	
	public GenericModel getModel() throws DAOException
  {
    return new MisuraCautelareModel(
            getIdMisuraCautelare()
            , getCodTipoMisura()
            , ""
            , getDataInizio()
            , getDataFine()
            , getDataEmissioneOrdinanza()
            , getNumAnni()
            , getNumMesi()
            , getNumGiorni()
            , getGiorni()
            
            , getCodOperatoreInserimento()
            , getDataInserimento()
            , getCodUfficioInserimento()
            , ""
            , getCodOperatoreAggiornamento()
            , getDataAggiornamento()
            , getCodUfficioAggiornamento()
            , ""
            , getFasSieIdFascicoloSiep()
            , getEveIdEvento()
            
            , getFlagComputabile()
            , getFlagModificaManuale()
            , "" 
            , getCodMotivoNonComputabile()
            , ""
            , getIstDetIdIstitutoDetenzione()
            , getAltroLuogoDetenzione()

            //
            , getNumRifer()
            , getCodTipoUfficioRifer()
            , ""
            , getCodLuogoUfficioRifer()
            , ""
            , getDataFungibilita()
            , getNote()
            //modifica relativa al tipo istituto
            , null
            
			 ,getAnnoFascBdmc()  
			 ,getNumeFascBdmc()  
			 ,getAnnoRgnr()  
			 ,getNumeroRgnr()  
			 ,getAnnoRegGen()  
			 ,getNumeroRegGen()  
			 ,getTipoUfficioRegGen()  
			 ,getAutoritaEmittente()  
			 ,getAutoritaEmittenteLuogo()  
			 ,getAutoritaCompetente()  
			 ,getAutoritaCompetenteSede()  
			 ,getAutoritaCompetenteIndirizzo()  
			 ,getAnnoRifer()  
			 ,getCodiceUfficioPmSede()  
			 , ""
			 ,getPosGiuIdPosizioneGiuridica()  
			 , null
                    								);
  }

  public void setDAOFromModel(MisuraCautelareModel aModel) throws DAOException
  {
    setIdMisuraCautelare( aModel.getIdMisuraCautelare() );
    setCodTipoMisura( aModel.getCodTipoMisura() );
    setDataInizio( aModel.getDataInizio() );
    setDataFine( aModel.getDataFine() );
    setDataEmissioneOrdinanza(aModel.getDataEmissioneOrdinanza());
    setNumAnni( aModel.getNumAnni() );
    setNumMesi( aModel.getNumMesi() );
    setNumGiorni( aModel.getNumGiorni() );
    setGiorni( aModel.getGiorni() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setEveIdEvento( aModel.getEveIdEvento() );
    setFlagComputabile(aModel.getFlagComputabile());
    setFlagModificaManuale(aModel.getFlagModificaManuale());
    //nuovi
    setCodMotivoNonComputabile( aModel.getCodMotivoNonComputabile() );
   //modifica relativa al tipo istituto
    setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
   // setCodTipoIstitutoDetenzione( aModel.getCodTipoIstitutoDetenzione() );
    setAltroLuogoDetenzione( aModel.getAltroLuogoDetenzione() );
   // setCodLuogoDetenzione( aModel.getCodLuogoDetenzione() );
    setNumRifer( aModel.getNumRifer() );
    setCodTipoUfficioRifer( aModel.getCodTipoUfficioRifer() );
    setCodLuogoUfficioRifer( aModel.getCodLuogoUfficioRifer() );
    setDataFungibilita( aModel.getDataFungibilita() );
    setNote( aModel.getNote() );
    
	 setAnnoFascBdmc( aModel.getAnnoFascBdmc() );  
	 setNumeFascBdmc( aModel.getNumeFascBdmc() );  
	 setAnnoRgnr( aModel.getAnnoRgnr() );  
	 setNumeroRgnr( aModel.getNumeroRgnr() );  
	 setAnnoRegGen( aModel.getAnnoRegGen() );  
	 setNumeroRegGen( aModel.getNumeroRegGen() );  
	 setTipoUfficioRegGen( aModel.getTipoUfficioRegGen() );  
	 setAutoritaEmittente( aModel.getAutoritaEmittente() );  
	 setAutoritaEmittenteLuogo( aModel.getAutoritaEmittenteLuogo() );  
	 setAutoritaCompetente( aModel.getAutoritaCompetente() );  
	 setAutoritaCompetenteSede( aModel.getAutoritaCompetenteSede() );  
	 setAutoritaCompetenteIndirizzo( aModel.getAutoritaCompetenteIndirizzo() );  
	 setAnnoRifer( aModel.getAnnoRifer() );  
	 setCodiceUfficioPmSede( aModel.getCodiceUfficioPmSede() );  
	 setPosGiuIdPosizioneGiuridica( aModel.getPosGiuIdPosizioneGiuridica() );  
    
  }

  public void setDAOFromModelForUpdate(MisuraCautelareModel aModel) throws DAOException
  {
    // setIdMisuraCautelare( aModel.getIdMisuraCautelare() );
    setCodTipoMisura( aModel.getCodTipoMisura() );
    setDataInizio( aModel.getDataInizio() );
    setDataFine( aModel.getDataFine() );
    setDataEmissioneOrdinanza(aModel.getDataEmissioneOrdinanza());
    setNumAnni( aModel.getNumAnni() );
    setNumMesi( aModel.getNumMesi() );
    setNumGiorni( aModel.getNumGiorni() );
    setGiorni( aModel.getGiorni() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    //setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setEveIdEvento( aModel.getEveIdEvento() );
    setFlagComputabile(aModel.getFlagComputabile());
    setFlagModificaManuale(aModel.getFlagModificaManuale());

    //nuovi
    setCodMotivoNonComputabile( aModel.getCodMotivoNonComputabile() );
  //modifica relativa al tipo istituto
    setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
    //setCodTipoIstitutoDetenzione( aModel.getCodTipoIstitutoDetenzione() );
    setAltroLuogoDetenzione( aModel.getAltroLuogoDetenzione() );
    //setCodLuogoDetenzione( aModel.getCodLuogoDetenzione() );
    setNumRifer( aModel.getNumRifer() );
    setCodTipoUfficioRifer( aModel.getCodTipoUfficioRifer() );
    setCodLuogoUfficioRifer( aModel.getCodLuogoUfficioRifer() );
    setDataFungibilita( aModel.getDataFungibilita() );
    setNote( aModel.getNote() );

	 setAnnoFascBdmc( aModel.getAnnoFascBdmc() );  
	 setNumeFascBdmc( aModel.getNumeFascBdmc() );  
	 setAnnoRgnr( aModel.getAnnoRgnr() );  
	 setNumeroRgnr( aModel.getNumeroRgnr() );  
	 setAnnoRegGen( aModel.getAnnoRegGen() );  
	 setNumeroRegGen( aModel.getNumeroRegGen() );  
	 setTipoUfficioRegGen( aModel.getTipoUfficioRegGen() );  
	 setAutoritaEmittente( aModel.getAutoritaEmittente() );  
	 setAutoritaEmittenteLuogo( aModel.getAutoritaEmittenteLuogo() );  
	 setAutoritaCompetente( aModel.getAutoritaCompetente() );  
	 setAutoritaCompetenteSede( aModel.getAutoritaCompetenteSede() );  
	 setAutoritaCompetenteIndirizzo( aModel.getAutoritaCompetenteIndirizzo() );  
	 setAnnoRifer( aModel.getAnnoRifer() );  
	 setCodiceUfficioPmSede( aModel.getCodiceUfficioPmSede() );  
	 setPosGiuIdPosizioneGiuridica( aModel.getPosGiuIdPosizioneGiuridica() );  

    setCondizioneUpdate(aModel.getIdMisuraCautelare());
  }

  public void setCondizione(MisuraCautelareModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

	public void setCondizioneInCorsoComputabile(BigDecimal aIdFascicolo)
  {
    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = "+aIdFascicolo+" AND DATA_FINE IS NULL AND FLAG_COMPUTABILE = 'S'");
  }

	public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_MISURA_CAUTELARE = " + key );
  }
	
  public void setCondizioneDeleteByIdPosGiuridica(BigDecimal key)
  {
	setCondition(" POS_GIU_ID_POSIZIONE_GIURIDICA = " + key );
  }
  
  public void setCondizioneByFascicoloSiep (BigDecimal aIdFascicolo)
  {
    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = "+aIdFascicolo);
  }
}
