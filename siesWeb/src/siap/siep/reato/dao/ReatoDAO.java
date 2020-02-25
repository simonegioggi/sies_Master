package siap.siep.reato.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.reato.model.ReatoModel;
import siap.sige.reato.model.ReatoSentenzaSigeModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
 
/**
* <p>Title: ReatoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Reato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ReatoDAO extends SIAPTableDAO
{
	public ReatoDAO (Connection con)
	{
    super(con);
    setTable("REATO");

    setSequenceField("ID_REATO", "REA_SEQ");

    setFieldKey("ID_REATO", BIG_DECIMAL);

    setField("ID_REATO", BIG_DECIMAL);
    setField("COD_TIPO_REATO", STRING);
    setField("DATA_REATO", DATE);
    setField("PROGR_NUMERO_MANUALE", STRING);
    setField("PROGR_REATO", BIG_DECIMAL);
    setField("PROGR_CIRCOSTANZA", BIG_DECIMAL);
    setField("DATA_INIZIO", DATE);
    setField("ANNO_INIZIO", BIG_DECIMAL);
    setField("MESE_INIZIO", BIG_DECIMAL);
    setField("GIORNO_INIZIO", BIG_DECIMAL);
    setField("DATA_FINE", DATE);
    setField("ANNO_FINE", BIG_DECIMAL);
    setField("MESE_FINE", BIG_DECIMAL);
    setField("GIORNO_FINE", BIG_DECIMAL);
    setField("COD_PERIODO_CONSUMAZIONE", STRING);
    setField("DESC_LUOGO", STRING);
    setField("COD_FONTE", STRING);
    setField("ANNO_FONTE", BIG_DECIMAL);
    setField("NUMERO_FONTE", STRING);
    setField("COD_SOTTONUMERAZIONE", STRING);
    setField("COMMA", STRING);
    setField("LETTERA", STRING);
    setField("NUMERO", STRING);
    setField("ARTICOLO", STRING);
    setField("NOTE", STRING);
    setField("COD_TIPO_PENA_DETENTIVA", STRING);
    setField("NUM_ANNI", BIG_DECIMAL);
    setField("NUM_MESI", BIG_DECIMAL);
    setField("NUM_GIORNI", BIG_DECIMAL);
    setField("SANZIONE_PECUNIARIA", BIG_DECIMAL);
    setField("FLAG_ERGASTOLO", STRING);
    setField("DATA_INIZIO_ISOLAMENTO_DIURNO", DATE);
    setField("DATA_FINE_ISOLAMENTO_DIURNO", DATE);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
    setField("COD_TIPO_SANZIONE", STRING);
    setField("ANNI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
    setField("MESI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
    setField("GIORNI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
    setField("ID_CONTINUAZIONE_REATO", BIG_DECIMAL);
    setField("TIPO_CONTINUAZIONE_REATO", STRING);
    setField("KEY_REATO_NSC", BIG_DECIMAL);
    //***************************************
    //Federica - a9-rr-078
    //aggiunto campo Comma-Qualificante 
    setField("COMMA_QUALIFICANTE", STRING);
    //***************************************

	}


  //
  // METODI GET()
  //

  public BigDecimal	getIdReato() 					throws DAOException	{ return getBigDecimal("ID_REATO"); }
  public String 	getCodTipoReato() 				throws DAOException	{ return getString("COD_TIPO_REATO"); }
  public Date 		getDataReato() 					throws DAOException	{ return getDate("DATA_REATO"); }
  public String 	getProgrNumeroManuale() 		throws DAOException	{ return getString("PROGR_NUMERO_MANUALE"); }
  public BigDecimal getProgrReato() 				throws DAOException	{ return getBigDecimal("PROGR_REATO"); }
  public BigDecimal getProgrCircostanza() 			throws DAOException	{ return getBigDecimal("PROGR_CIRCOSTANZA"); }
  public Date 		getDataInizio() 				throws DAOException	{ return getDate("DATA_INIZIO"); }
  public BigDecimal getAnnoInizio() 				throws DAOException	{ return getBigDecimal("ANNO_INIZIO"); }
  public BigDecimal	getMeseInizio() 				throws DAOException	{ return getBigDecimal("MESE_INIZIO"); }
  public BigDecimal getGiornoInizio() 				throws DAOException	{ return getBigDecimal("GIORNO_INIZIO"); }
  public Date 		getDataFine() 					throws DAOException	{ return getDate("DATA_FINE"); }
  public BigDecimal getAnnoFine() 					throws DAOException	{ return getBigDecimal("ANNO_FINE"); }
  public BigDecimal getMeseFine() 					throws DAOException	{ return getBigDecimal("MESE_FINE"); }
  public BigDecimal getGiornoFine() 				throws DAOException	{ return getBigDecimal("GIORNO_FINE"); }
  public String 	getCodPeriodoConsumazione() 	throws DAOException	{ return getString("COD_PERIODO_CONSUMAZIONE"); }
  public String 	getDescLuogo() 					throws DAOException	{ return getString("DESC_LUOGO"); }
  public String 	getCodFonte() 					throws DAOException	{ return getString("COD_FONTE"); }
  public BigDecimal getAnnoFonte() 					throws DAOException	{ return getBigDecimal("ANNO_FONTE"); }
  public String 	getNumeroFonte() 				throws DAOException	{ return getString("NUMERO_FONTE"); }
  public String 	getCodSottonumerazione() 		throws DAOException	{ return getString("COD_SOTTONUMERAZIONE"); }
  public String 	getComma() 						throws DAOException	{ return getString("COMMA"); }
  public String 	getLettera() 					throws DAOException	{ return getString("LETTERA"); }
  public String 	getNumero() 					throws DAOException	{ return getString("NUMERO"); }
  public String 	getArticolo() 					throws DAOException	{ return getString("ARTICOLO"); }
  public String 	getNote() 						throws DAOException	{ return getString("NOTE"); }
  public String 	getCodTipoPenaDetentiva() 		throws DAOException	{ return getString("COD_TIPO_PENA_DETENTIVA"); }
  public BigDecimal getNumAnni() 					throws DAOException	{ return getBigDecimal("NUM_ANNI"); }
  public BigDecimal getNumMesi() 					throws DAOException	{ return getBigDecimal("NUM_MESI"); }
  public BigDecimal getNumGiorni() 					throws DAOException	{ return getBigDecimal("NUM_GIORNI"); }
  public BigDecimal getSanzionePecuniaria() 		throws DAOException	{ return getBigDecimal("SANZIONE_PECUNIARIA"); }
  public String 	getFlagErgastolo() 				throws DAOException	{ return getString("FLAG_ERGASTOLO"); }
  public Date 		getDataInizioIsolamentoDiurno() throws DAOException	{ return getDate("DATA_INIZIO_ISOLAMENTO_DIURNO"); }
  public Date 		getDataFineIsolamentoDiurno() 	throws DAOException	{ return getDate("DATA_FINE_ISOLAMENTO_DIURNO"); }
  public String 	getCodOperatoreInserimento() 	throws DAOException	{ return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 		getDataInserimento() 			throws DAOException	{ return getDate("DATA_INSERIMENTO"); }
  public String 	getCodUfficioInserimento() 		throws DAOException	{ return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 	getCodOperatoreAggiornamento() 	throws DAOException	{ return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 		getDataAggiornamento() 			throws DAOException	{ return getDate("DATA_AGGIORNAMENTO"); }
  public String 	getCodUfficioAggiornamento() 	throws DAOException	{ return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal getFasSieIdFascicoloSiep() 		throws DAOException	{ return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public String 	getCodTipoSanzione() 			throws DAOException	{ return getString("COD_TIPO_SANZIONE"); }
  public BigDecimal getNumAnniIsolamentoDiurno() 	throws DAOException	{ return getBigDecimal("ANNI_ISOLAMENTO_DIURNO"); }
  public BigDecimal getNumMesiIsolamentoDiurno() 	throws DAOException	{ return getBigDecimal("MESI_ISOLAMENTO_DIURNO"); }
  public BigDecimal getNumGiorniIsolamentoDiurno() 	throws DAOException	{ return getBigDecimal("GIORNI_ISOLAMENTO_DIURNO"); }
  public BigDecimal getIdContinuazioneReato() 		throws DAOException	{ return getBigDecimal("ID_CONTINUAZIONE_REATO"); }
  public String 	getTipoContinuazioneReato() 	throws DAOException	{ return getString("TIPO_CONTINUAZIONE_REATO"); }
  public BigDecimal getKeyReatoNsc()    			throws DAOException { return getBigDecimal("KEY_REATO_NSC"); }
  //***************************************
  //Federica - a9-rr-078
  //aggiunto campo Comma-Qualificante 
  public String 	getCommaQualificante() 			throws DAOException	{ return getString("COMMA_QUALIFICANTE"); }
  //***************************************  

  //
  // METODI SET()
  //

  public void setIdReato(BigDecimal aValore ) 			        { setBigDecimal("ID_REATO", aValore); }
  public void setCodTipoReato(String aValore ) 			        { setString("COD_TIPO_REATO", aValore); }
  public void setDataReato(Date aValore ) 			            { setDate("DATA_REATO", aValore); }
  public void setProgrNumeroManuale(String aValore ) 			{ setString("PROGR_NUMERO_MANUALE", aValore); }
  public void setProgrReato(BigDecimal aValore ) 			    { setBigDecimal("PROGR_REATO", aValore); }
  public void setProgrCircostanza(BigDecimal aValore ) 			{ setBigDecimal("PROGR_CIRCOSTANZA", aValore); }
  public void setDataInizio(Date aValore ) 			            { setDate("DATA_INIZIO", aValore); }
  public void setAnnoInizio(BigDecimal aValore ) 			    { setBigDecimal("ANNO_INIZIO", aValore); }
  public void setMeseInizio(BigDecimal aValore ) 			    { setBigDecimal("MESE_INIZIO", aValore); }
  public void setGiornoInizio(BigDecimal aValore ) 			    { setBigDecimal("GIORNO_INIZIO", aValore); }
  public void setDataFine(Date aValore ) 			            { setDate("DATA_FINE", aValore); }
  public void setAnnoFine(BigDecimal aValore ) 			        { setBigDecimal("ANNO_FINE", aValore); }
  public void setMeseFine(BigDecimal aValore ) 			        { setBigDecimal("MESE_FINE", aValore); }
  public void setGiornoFine(BigDecimal aValore ) 			    { setBigDecimal("GIORNO_FINE", aValore); }
  public void setCodPeriodoConsumazione(String aValore ) 		{ setString("COD_PERIODO_CONSUMAZIONE", aValore); }
  public void setDescLuogo(String aValore ) 			        { setString("DESC_LUOGO", aValore); }
  public void setCodFonte(String aValore ) 			            { setString("COD_FONTE", aValore); }
  public void setAnnoFonte(BigDecimal aValore ) 			    { setBigDecimal("ANNO_FONTE", aValore); }
  public void setNumeroFonte(String aValore ) 			        { setString("NUMERO_FONTE", aValore); }
  public void setCodSottonumerazione(String aValore ) 			{ setString("COD_SOTTONUMERAZIONE", aValore); }
  public void setComma(String aValore ) 			            { setString("COMMA", aValore); }
  public void setLettera(String aValore ) 			            { setString("LETTERA", aValore); }
  public void setNumero(String aValore ) 			            { setString("NUMERO", aValore); }
  public void setArticolo(String aValore ) 			            { setString("ARTICOLO", aValore); }
  public void setNote(String aValore ) 			                { setString("NOTE", aValore); }
  public void setCodTipoPenaDetentiva(String aValore ) 			{ setString("COD_TIPO_PENA_DETENTIVA", aValore); }
  public void setNumAnni(BigDecimal aValore ) 			        { setBigDecimal("NUM_ANNI", aValore); }
  public void setNumMesi(BigDecimal aValore ) 			        { setBigDecimal("NUM_MESI", aValore); }
  public void setNumGiorni(BigDecimal aValore ) 			    { setBigDecimal("NUM_GIORNI", aValore); }
  public void setSanzionePecuniaria(BigDecimal aValore ) 		{ setBigDecimal("SANZIONE_PECUNIARIA", aValore); }
  public void setFlagErgastolo(String aValore ) 			    { setString("FLAG_ERGASTOLO", aValore); }
  public void setDataInizioIsolamentoDiurno(Date aValore ) 		{ setDate("DATA_INIZIO_ISOLAMENTO_DIURNO", aValore); }
  public void setDataFineIsolamentoDiurno(Date aValore ) 		{ setDate("DATA_FINE_ISOLAMENTO_DIURNO", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 		{ setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 			    { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 		{ setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 	{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 			    { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 		{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setFasSieIdFascicoloSiep(BigDecimal aValore ) 	{ setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void setCodTipoSanzione(String aValore ) 			    { setString("COD_TIPO_SANZIONE", aValore); }
  public void setNumAnniIsolamentoDiurno(BigDecimal aValore ) 	{ setBigDecimal("ANNI_ISOLAMENTO_DIURNO", aValore); }
  public void setNumMesiIsolamentoDiurno(BigDecimal aValore ) 	{ setBigDecimal("MESI_ISOLAMENTO_DIURNO", aValore); }
  public void setNumGiorniIsolamentoDiurno(BigDecimal aValore )	{ setBigDecimal("GIORNI_ISOLAMENTO_DIURNO", aValore); }
  public void setIdContinuazioneReato(BigDecimal aValore )      { setBigDecimal("ID_CONTINUAZIONE_REATO", aValore); }
  public void setTipoContinuazioneReato(String aValore )        { setString("TIPO_CONTINUAZIONE_REATO", aValore); }
  public void setKeyReatoNsc(BigDecimal aValore )               { setBigDecimal("KEY_REATO_NSC", aValore); }
  //***************************************
  //Federica - a9-rr-078
  //aggiunto campo Comma-Qualificante 
  public void setCommaQualificante(String aValore ) 			{ setString("COMMA_QUALIFICANTE", aValore); }
  //***************************************    

  
  public GenericModel getModel() throws DAOException
  {
    return new ReatoModel(
                           getIdReato() ,
                           getCodTipoReato() ,
                           "",
                           getDataReato() ,
                           getProgrNumeroManuale() ,
                           getProgrReato() ,
                           getProgrCircostanza() ,
                           getDataInizio() ,
                           getAnnoInizio() ,
                           getMeseInizio() ,
                           getGiornoInizio() ,
                           getDataFine() ,
                           getAnnoFine() ,
                           getMeseFine() ,
                           getGiornoFine() ,
                           getCodPeriodoConsumazione() ,
                           "",
                           getDescLuogo() ,
                           getCodFonte() ,
                           "",
                           getAnnoFonte() ,
                           getNumeroFonte() ,
                           getCodSottonumerazione() ,
                           "",
                           getComma() ,
                           getLettera() ,
                           getNumero() ,
                           getArticolo() ,
                           getNote() ,
                           getCodTipoPenaDetentiva() ,
                           "",
                           getNumAnni() ,
                           getNumMesi() ,
                           getNumGiorni() ,
                           getSanzionePecuniaria() ,
                           getFlagErgastolo() ,
                           getDataInizioIsolamentoDiurno() ,
                           getDataFineIsolamentoDiurno() ,
                           getCodOperatoreInserimento() ,
                           getDataInserimento() ,
                           getCodUfficioInserimento() ,
                           "",
                           getCodOperatoreAggiornamento() ,
                           getDataAggiornamento() ,
                           getCodUfficioAggiornamento() ,
                           "",
                           getFasSieIdFascicoloSiep(),
           				   getCodTipoSanzione(),
                           "",
                           getNumAnniIsolamentoDiurno(),
                           getNumMesiIsolamentoDiurno(),
                           getNumGiorniIsolamentoDiurno(),
                           getIdContinuazioneReato(),
                           getTipoContinuazioneReato(),
                           getKeyReatoNsc(),
                           //***************************************
                           //Federica - a9-rr-078
                           //aggiunto campo Comma-Qualificante 
                           getCommaQualificante(), 
                           ""
                           //***************************************    
                         ); 
  }

  public void setDAOFromModel(ReatoModel aModel) throws DAOException
  {
    setIdReato						( aModel.getIdReato() );
    setCodTipoReato					( aModel.getCodTipoReato() );
    setDataReato					( aModel.getDataReato() );
    setProgrNumeroManuale			( aModel.getProgrNumeroManuale() );
    setProgrReato					( aModel.getProgrReato() );
    setProgrCircostanza				( aModel.getProgrCircostanza() );
    setDataInizio					( aModel.getDataInizio() );
    setAnnoInizio					( aModel.getAnnoInizio() );
    setMeseInizio					( aModel.getMeseInizio() );
    setGiornoInizio					( aModel.getGiornoInizio() );
    setDataFine						( aModel.getDataFine() );
    setAnnoFine						( aModel.getAnnoFine() );
    setMeseFine						( aModel.getMeseFine() );
    setGiornoFine					( aModel.getGiornoFine() );
    setCodPeriodoConsumazione		( aModel.getCodPeriodoConsumazione() );
    setDescLuogo					( aModel.getDescLuogo() );
    setCodFonte						( aModel.getCodFonte() );
    setAnnoFonte					( aModel.getAnnoFonte() );
    setNumeroFonte					( aModel.getNumeroFonte() );
    setCodSottonumerazione			( aModel.getCodSottonumerazione() );
    setComma						( aModel.getComma() );
    setLettera						( aModel.getLettera() );
    setNumero						( aModel.getNumero() );
    setArticolo						( aModel.getArticolo() );
    setNote							( aModel.getNote() );
    setCodTipoPenaDetentiva			( aModel.getCodTipoPenaDetentiva() );
    setNumAnni						( aModel.getNumAnni() );
    setNumMesi						( aModel.getNumMesi() );
    setNumGiorni					( aModel.getNumGiorni() );
    setSanzionePecuniaria			( aModel.getSanzionePecuniaria() );
    setFlagErgastolo				( aModel.getFlagErgastolo() );
    setDataInizioIsolamentoDiurno	( aModel.getDataInizioIsolamentoDiurno() );
    setDataFineIsolamentoDiurno		( aModel.getDataFineIsolamentoDiurno() );
    setCodOperatoreInserimento		( aModel.getCodOperatoreInserimento() );
    setDataInserimento				( aModel.getDataInserimento() );
    setCodUfficioInserimento		( aModel.getCodUfficioInserimento() );
    //setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    //setDataAggiornamento( aModel.getDataAggiornamento() );
    //setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFasSieIdFascicoloSiep		( aModel.getFasSieIdFascicoloSiep() );
    setCodTipoSanzione				( aModel.getCodTipoSanzione() );
    setNumAnniIsolamentoDiurno		( aModel.getNumAnniIsolamentoDiurno());
    setNumMesiIsolamentoDiurno		( aModel.getNumMesiIsolamentoDiurno());
    setNumGiorniIsolamentoDiurno	( aModel.getNumGiorniIsolamentoDiurno());
    setIdContinuazioneReato			( aModel.getIdContinuazioneReato());
    setTipoContinuazioneReato		( aModel.getTipoContinuazioneReato());
    setKeyReatoNsc					( aModel.getKeyReatoNsc() );
    //***************************************
    //Federica - a9-rr-078
    //aggiunto campo Comma-Qualificante 
    setCommaQualificante			( aModel.getCommaQualificante() );
    //***************************************    
 }
 
  public void setDAOFromModelForUpdate(ReatoModel aModel) throws DAOException
  {
    //setIdReato( aModel.getIdReato() );
    setCodTipoReato					( aModel.getCodTipoReato() );
    //setDataReato( aModel.getDataReato() );
    setProgrNumeroManuale			( aModel.getProgrNumeroManuale() );
    //setProgrReato( aModel.getProgrReato() );
    //setProgrCircostanza( aModel.getProgrCircostanza() );
    setDataInizio					( aModel.getDataInizio() );
    setAnnoInizio					( aModel.getAnnoInizio() );
    setMeseInizio					( aModel.getMeseInizio() );
    setGiornoInizio					( aModel.getGiornoInizio() );
    setDataFine						( aModel.getDataFine() );
    setAnnoFine						( aModel.getAnnoFine() );
    setMeseFine						( aModel.getMeseFine() );
    setGiornoFine					( aModel.getGiornoFine() );
    setCodPeriodoConsumazione		( aModel.getCodPeriodoConsumazione() );
    setDescLuogo					( aModel.getDescLuogo() );
    setCodFonte						( aModel.getCodFonte() );
    setAnnoFonte					( aModel.getAnnoFonte() );
    setNumeroFonte					( aModel.getNumeroFonte() );
    setCodSottonumerazione			( aModel.getCodSottonumerazione() );
    setComma						( aModel.getComma() );
    setLettera						( aModel.getLettera() );
    setNumero						( aModel.getNumero() );
    setArticolo						( aModel.getArticolo() );
    setNote							( aModel.getNote() );
    //setCodTipoPenaDetentiva( aModel.getCodTipoPenaDetentiva() );
    //setNumAnni( aModel.getNumAnni() );
    //setNumMesi( aModel.getNumMesi() );
    //setNumGiorni( aModel.getNumGiorni() );
    //setSanzionePecuniaria( aModel.getSanzionePecuniaria() );
    //setFlagErgastolo( aModel.getFlagErgastolo() );
    //setDataInizioIsolamentoDiurno( aModel.getDataInizioIsolamentoDiurno() );
    //setDataFineIsolamentoDiurno( aModel.getDataFineIsolamentoDiurno() );
    setCodOperatoreAggiornamento	( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento			( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento		( aModel.getCodUfficioAggiornamento() );
    //setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setCodTipoSanzione				( aModel.getCodTipoSanzione() );
    setNumAnniIsolamentoDiurno		( aModel.getNumAnniIsolamentoDiurno());
    setNumMesiIsolamentoDiurno		( aModel.getNumMesiIsolamentoDiurno());
    setNumGiorniIsolamentoDiurno	( aModel.getNumGiorniIsolamentoDiurno());
    setIdContinuazioneReato			( aModel.getIdContinuazioneReato());
    setTipoContinuazioneReato		( aModel.getTipoContinuazioneReato());    
    setKeyReatoNsc					( aModel.getKeyReatoNsc());
    //***************************************
    //Federica - a9-rr-078
    //aggiunto campo Comma-Qualificante 
    setCommaQualificante			( aModel.getCommaQualificante() );
    //***************************************    
   
    setCondizioneUpdate(aModel.getIdReato());
  }

public void setDAOFromModelForUpdateUlterioriNorme(ReatoModel aModel) throws DAOException
  {
    //setIdReato( aModel.getIdReato() );
    setCodTipoReato					( aModel.getCodTipoReato() );
    //setDataReato( aModel.getDataReato() );
    setProgrNumeroManuale			( aModel.getProgrNumeroManuale() );
    //setProgrReato( aModel.getProgrReato() );
    //setProgrCircostanza( aModel.getProgrCircostanza() );
    setDataInizio					( aModel.getDataInizio() );
    setAnnoInizio					( aModel.getAnnoInizio() );
    setMeseInizio					( aModel.getMeseInizio() );
    setGiornoInizio					( aModel.getGiornoInizio() );
    setDataFine						( aModel.getDataFine() );
    setAnnoFine						( aModel.getAnnoFine() );
    setMeseFine						( aModel.getMeseFine() );
    setGiornoFine					( aModel.getGiornoFine() );
    setCodPeriodoConsumazione		( aModel.getCodPeriodoConsumazione() );
    setDescLuogo					( aModel.getDescLuogo() );
    //setCodFonte( aModel.getCodFonte() );
    //setAnnoFonte( aModel.getAnnoFonte() );
    //setNumeroFonte( aModel.getNumeroFonte() );
    //setCodSottonumerazione( aModel.getCodSottonumerazione() );
    //setComma( aModel.getComma() );
    //setLettera( aModel.getLettera() );
    //setNumero( aModel.getNumero() );
    //setArticolo( aModel.getArticolo() );
    setNote							( aModel.getNote() );
    //setCodTipoPenaDetentiva( aModel.getCodTipoPenaDetentiva() );
    //setNumAnni( aModel.getNumAnni() );
    //setNumMesi( aModel.getNumMesi() );
    //setNumGiorni( aModel.getNumGiorni() );
    //setSanzionePecuniaria( aModel.getSanzionePecuniaria() );
    //setFlagErgastolo( aModel.getFlagErgastolo() );
    //setDataInizioIsolamentoDiurno( aModel.getDataInizioIsolamentoDiurno() );
    //setDataFineIsolamentoDiurno( aModel.getDataFineIsolamentoDiurno() );
    setCodOperatoreAggiornamento	( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento			( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento		( aModel.getCodUfficioAggiornamento() );
    //setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    //setCodTipoSanzione( aModel.getCodTipoSanzione() );
    //setNumAnniIsolamentoDiurno(aModel.getNumAnniIsolamentoDiurno());
    //setNumMesiIsolamentoDiurno(aModel.getNumMesiIsolamentoDiurno());
    //setNumGiorniIsolamentoDiurno(aModel.getNumGiorniIsolamentoDiurno());
    //setIdContinuazioneReato(aModel.getIdContinuazioneReato());
    //setTipoContinuazioneReato(aModel.getTipoContinuazioneReato());    
    //***************************************
    //Federica - a9-rr-078
    //aggiunto campo Comma-Qualificante 
    //setCommaQualificante			( aModel.getCommaQualificante() );
    //***************************************    
    
    setCondizioneUpdate(aModel.getIdReato());
  }

public void setDAOFromModelForUpdateNonPrimaNorma(ReatoModel aModel) throws DAOException
  {
    //setIdReato( aModel.getIdReato() );
    //setCodTipoReato( aModel.getCodTipoReato() );
    //setDataReato( aModel.getDataReato() );
    //setProgrNumeroManuale( aModel.getProgrNumeroManuale() );
    //setProgrReato( aModel.getProgrReato() );
    //setProgrCircostanza( aModel.getProgrCircostanza() );
    //setDataInizio( aModel.getDataInizio() );
    //setAnnoInizio( aModel.getAnnoInizio() );
    //setMeseInizio( aModel.getMeseInizio() );
    //setGiornoInizio( aModel.getGiornoInizio() );
    //setDataFine( aModel.getDataFine() );
    //setAnnoFine( aModel.getAnnoFine() );
    //setMeseFine( aModel.getMeseFine() );
    //setGiornoFine( aModel.getGiornoFine() );
    //setCodPeriodoConsumazione( aModel.getCodPeriodoConsumazione() );
    //setDescLuogo( aModel.getDescLuogo() );
    setCodFonte( aModel.getCodFonte() );
    setAnnoFonte( aModel.getAnnoFonte() );
    setNumeroFonte( aModel.getNumeroFonte() );
    setCodSottonumerazione( aModel.getCodSottonumerazione() );
    setComma( aModel.getComma() );
    setLettera( aModel.getLettera() );
    setNumero( aModel.getNumero() );
    setArticolo( aModel.getArticolo() );
    //setNote( aModel.getNote() );
    //setCodTipoPenaDetentiva( aModel.getCodTipoPenaDetentiva() );
    //setNumAnni( aModel.getNumAnni() );
    //setNumMesi( aModel.getNumMesi() );
    //setNumGiorni( aModel.getNumGiorni() );
    //setSanzionePecuniaria( aModel.getSanzionePecuniaria() );
    //setFlagErgastolo( aModel.getFlagErgastolo() );
    //setDataInizioIsolamentoDiurno( aModel.getDataInizioIsolamentoDiurno() );
    //setDataFineIsolamentoDiurno( aModel.getDataFineIsolamentoDiurno() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    //setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    //setCodTipoSanzione( aModel.getCodTipoSanzione() );
    //setNumAnniIsolamentoDiurno(aModel.getNumAnniIsolamentoDiurno());
    //setNumMesiIsolamentoDiurno(aModel.getNumMesiIsolamentoDiurno());
    //setNumGiorniIsolamentoDiurno(aModel.getNumGiorniIsolamentoDiurno());
    //setIdContinuazioneReato(aModel.getIdContinuazioneReato());
    //setTipoContinuazioneReato(aModel.getTipoContinuazioneReato());    
    //***************************************
    //Federica - a9-rr-078
    //aggiunto campo Comma-Qualificante 
    setCommaQualificante			( aModel.getCommaQualificante() );
    //***************************************    

    setCondizioneUpdate(aModel.getIdReato());
  }
  
  public void setDAOFromModelForUpdateKeyReatoNsc( ReatoModel  aModel ) throws DAOException
  {
      setKeyReatoNsc(aModel.getKeyReatoNsc());
  }

	public void selCondizione(ReatoModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

	public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition (" ID_REATO="+ key);
  }

  	public void setCondizione_CancellazioneACatena(BigDecimal aProgReato,BigDecimal aCodFascicolo)
  {
    setCondition (" PROGR_REATO="+ aProgReato+" AND FAS_SIE_ID_FASCICOLO_SIEP="+aCodFascicolo);
  }
  	
  	public void setCondizione_Continuazioni(ReatoModel aModel)
  {
  		String lCondizioni = "";
  		
  		lCondizioni += " FAS_SIE_ID_FASCICOLO_SIEP = "+aModel.getFasSieIdFascicoloSiep();
  		
  		if (aModel.getProgrReato() != null) {
  			
           lCondizioni += " AND PROGR_REATO = "+aModel.getProgrReato();
         }
  		
  		setCondition (lCondizioni);
  }

    /**
     * 
     * @param aModel
     */
    public void setCondizione_CancellaContinuazione(ReatoModel aModel)
    {
      String lCondizioni = "";
      
      lCondizioni = "ID_CONTINUAZIONE_REATO = (select A.ID_CONTINUAZIONE_REATO from reato A where ";
      
      lCondizioni += " FAS_SIE_ID_FASCICOLO_SIEP = "+aModel.getFasSieIdFascicoloSiep();
      
      lCondizioni += " AND PROGR_REATO = "+aModel.getProgrReato();
      
      lCondizioni += " AND PROGR_CIRCOSTANZA = "+aModel.getProgrCircostanza();
      
      lCondizioni += ")";
      
      lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = "+aModel.getFasSieIdFascicoloSiep();
      
      setCondition (lCondizioni);
    }
    	
 	public void setCondizioneIdSiep(BigDecimal aKey)
 	{
 		String lCondizioni = "FAS_SIE_ID_FASCICOLO_SIEP =" + aKey;
 		setCondition (lCondizioni);
 	}
  	
  	public void setCondizione_CancellazioneACatena(ReatoSentenzaSigeModel aReato) throws DAOException
    {
  		if (aReato.getProgrReato() == null || aReato.getFasSigeSenId() == null )
  			throw new DAOException("errore nella cancellazione: PROGR_REATO o FAS_SIGE_SEN_ID mancanti !");
      
  		setCondition (" PROGR_REATO=" + aReato.getProgrReato() + " AND ID_REATO IN (SELECT REA_ID_REATO FROM REATO_SENTENZA_SIGE WHERE FAS_SIGE_SEN_ID = " + aReato.getFasSigeSenId() + ")");
    }
   
}
