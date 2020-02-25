package siap.siep.circostanza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: CircostanzaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Circostanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class CircostanzaDAO extends SIAPTableDAO
{
	public CircostanzaDAO (Connection con)
	{
    super(con);
    setTable("CIRCOSTANZA");

    setSequenceField("ID_CIRCOSTANZA", "CIR_SEQ");

    setFieldKey("ID_CIRCOSTANZA", BIG_DECIMAL);

    setField("COD_TIPO_CIRCOSTANZA", STRING);
    setField("COD_FONTE", STRING);
    setField("ANNO_FONTE", BIG_DECIMAL);
    setField("NUMERO_FONTE", STRING);
    setField("COD_SOTTONUMERAZIONE", STRING);
    setField("COMMA", STRING);
    setField("LETTERA", STRING);
    setField("NUMERO", STRING);
    setField("ARTICOLO", STRING);
    setField("NOTE", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
    //Fabio - REWORK Bilanciamento Circostanze (a7-rr-165)
    setField("FLAG_SENTENZA_APPLICAZ_PENA", STRING);
   	setField("FLAG_GIUDIZIO_ABBREVIATO", STRING);
   	setField("COD_BILANCIAMENTO_CIRCOSTANZE", STRING);
   	//setField("PROGR_CIRCOSTANZE", BIG_DECIMAL);
   	setField("NOTE_BILANCIAMENTO", STRING);
    //***************************************
    //Federica - a9-rr-078
    //aggiunto campo Comma-Qualificante 
    setField("COMMA_QUALIFICANTE", STRING);
    //***************************************
  }

  //
  // METODI GET()
  //

  public BigDecimal 	getIdCircostanza() 					throws DAOException	{ return getBigDecimal("ID_CIRCOSTANZA"); }
  public String 		getCodTipoCircostanza() 			throws DAOException	{ return getString("COD_TIPO_CIRCOSTANZA"); }
  public String 		getCodFonte() 						throws DAOException	{ return getString("COD_FONTE"); }
  public BigDecimal 	getAnnoFonte() 						throws DAOException	{ return getBigDecimal("ANNO_FONTE"); }
  public String 		getNumeroFonte() 					throws DAOException	{ return getString("NUMERO_FONTE"); }
  public String 		getCodSottonumerazione() 			throws DAOException	{ return getString("COD_SOTTONUMERAZIONE"); }
  public String 		getComma() 							throws DAOException	{ return getString("COMMA"); }
  public String 		getLettera() 						throws DAOException	{ return getString("LETTERA"); }
  public String 		getNumero() 						throws DAOException	{ return getString("NUMERO"); }
  public String 		getArticolo() 						throws DAOException	{ return getString("ARTICOLO"); }
  public String 		getNote() 							throws DAOException	{ return getString("NOTE"); }
  public String 		getCodOperatoreInserimento() 		throws DAOException	{ return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 			getDataInserimento() 				throws DAOException	{ return getDate("DATA_INSERIMENTO"); }
  public String 		getCodUfficioInserimento() 			throws DAOException	{ return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 		getCodOperatoreAggiornamento() 		throws DAOException	{ return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 			getDataAggiornamento() 				throws DAOException	{ return getDate("DATA_AGGIORNAMENTO"); }
  public String 		getCodUfficioAggiornamento() 		throws DAOException	{ return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal 	getFasSieIdFascicoloSiep() 			throws DAOException	{ return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  //Fabio - REWORK Bilanciamento Circostanze (a7-rr-165)
  public String 		getFlagSentenzaApplicazPena() 		throws DAOException { return getString("FLAG_SENTENZA_APPLICAZ_PENA"); }
  public String 		getCodBilanciamentoCircostanze() 	throws DAOException	{ return getString("COD_BILANCIAMENTO_CIRCOSTANZE"); }  
  public String 		getFlagGiudizioAbbreviato() 		throws DAOException { return getString("FLAG_GIUDIZIO_ABBREVIATO"); }
  //public BigDecimal 	getProgrCircostanze() 		    	throws DAOException	{ return getBigDecimal("PROGR_CIRCOSTANZE");}
  public String 		getNoteBilanciamento() 		    	throws DAOException	{ return getString("NOTE_BILANCIAMENTO");}
  //***************************************
  //Federica - a9-rr-078
  //aggiunto campo Comma-Qualificante 
  public String 		getCommaQualificante() 				throws DAOException	{ return getString("COMMA_QUALIFICANTE"); }
  //***************************************  

  //
  // METODI SET()
  //

  public void setIdCircostanza(BigDecimal aValore ) 			    { setBigDecimal("ID_CIRCOSTANZA", aValore); }
  public void setCodTipoCircostanza(String aValore ) 			    { setString("COD_TIPO_CIRCOSTANZA", aValore); }
  public void setCodFonte(String aValore ) 			                { setString("COD_FONTE", aValore); }
  public void setAnnoFonte(BigDecimal aValore ) 			        { setBigDecimal("ANNO_FONTE", aValore); }
  public void setNumeroFonte(String aValore ) 			            { setString("NUMERO_FONTE", aValore); }
  public void setCodSottonumerazione(String aValore ) 			    { setString("COD_SOTTONUMERAZIONE", aValore); }
  public void setComma(String aValore ) 			                { setString("COMMA", aValore); }
  public void setLettera(String aValore ) 			                { setString("LETTERA", aValore); }
  public void setNumero(String aValore ) 			                { setString("NUMERO", aValore); }
  public void setArticolo(String aValore ) 			                { setString("ARTICOLO", aValore); }
  public void setNote(String aValore ) 			                    { setString("NOTE", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 			{ setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 			        { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 			{ setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 		{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 			        { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 			{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setFasSieIdFascicoloSiep(BigDecimal aValore ) 		{ setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
//Fabio - REWORK Bilanciamento Circostanze (a7-rr-165)
  public void setFlagSentenzaApplicazPena(String aValore) 		{ setString("FLAG_SENTENZA_APPLICAZ_PENA", aValore); }
  public void setCodBilanciamentoCircostanze(String aValore) 	{ setString("COD_BILANCIAMENTO_CIRCOSTANZE", aValore); }  
  public void setFlagGiudizioAbbreviato(String aValore) 		{ setString("FLAG_GIUDIZIO_ABBREVIATO", aValore); }
  //public void setProgrCircostanze(BigDecimal aValore) 			{ setBigDecimal("PROGR_CIRCOSTANZE", aValore);}
  public void setNoteBilanciamento(String aValore) 				{ setString("NOTE_BILANCIAMENTO", aValore);}
  //***************************************
  //Federica - a9-rr-078
  //aggiunto campo Comma-Qualificante 
  public void setCommaQualificante(String aValore ) 			{ setString("COMMA_QUALIFICANTE", aValore); }
  //***************************************    


	public GenericModel getModel() throws DAOException
  {
    return new CircostanzaModel(
                                 getIdCircostanza() ,
                                 getCodTipoCircostanza() ,
                                 "",
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
                                 getCodOperatoreInserimento() ,
                                 getDataInserimento() ,
                                 getCodUfficioInserimento() ,
                                 "",
                                 getCodOperatoreAggiornamento() ,
                                 getDataAggiornamento() ,
                                 getCodUfficioAggiornamento() ,
                                 "",
                                 getFasSieIdFascicoloSiep(),
                                 getFlagSentenzaApplicazPena(),
                                 getCodBilanciamentoCircostanze(),
                                 "",
                                 getFlagGiudizioAbbreviato(),
                                 getNoteBilanciamento(),
                                 //***************************************
                                 //Federica - a9-rr-078
                                 //aggiunto campo Comma-Qualificante 
                                 getCommaQualificante(), 
                                 ""
                                 //***************************************    
                               );
		}


  public void setDAOFromModel(CircostanzaModel aModel) throws DAOException
	{
    setIdCircostanza( aModel.getIdCircostanza() );
    setCodTipoCircostanza( aModel.getCodTipoCircostanza() );
    setCodFonte( aModel.getCodFonte() );
    setAnnoFonte( aModel.getAnnoFonte() );
    setNumeroFonte( aModel.getNumeroFonte() );
    setCodSottonumerazione( aModel.getCodSottonumerazione() );
    setComma( aModel.getComma() );
    setLettera( aModel.getLettera() );
    setNumero( aModel.getNumero() );
    setArticolo( aModel.getArticolo() );
    setNote( aModel.getNote() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    //setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    //setDataAggiornamento( aModel.getDataAggiornamento() );
    //setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );

    //  Fabio - REWORK Bilanciamento Circostanze (a7-rr-165)
    setFlagSentenzaApplicazPena(aModel.getFlagSentenzaApplicazPena());
    setCodBilanciamentoCircostanze(aModel.getCodBilanciamentoCircostanze());
    setFlagGiudizioAbbreviato(aModel.getFlagGiudizioAbbreviato());
    //setProgrCircostanze(aModel.getProgrCircostanze());
    setNoteBilanciamento(aModel.getNoteBilanciamento());
    //***************************************
    //Federica - a9-rr-078
    //aggiunto campo Comma-Qualificante 
    setCommaQualificante			( aModel.getCommaQualificante() );
    //***************************************    
  }

  public void setDAOFromModelForUpdate(CircostanzaModel aModel) throws DAOException
  {
    //setIdCircostanza( aModel.getIdCircostanza() );
    setCodTipoCircostanza( aModel.getCodTipoCircostanza() );
    setCodFonte( aModel.getCodFonte() );
    setAnnoFonte( aModel.getAnnoFonte() );
    setNumeroFonte( aModel.getNumeroFonte() );
    setCodSottonumerazione( aModel.getCodSottonumerazione() );
    setComma( aModel.getComma() );
    setLettera( aModel.getLettera() );
    setNumero( aModel.getNumero() );
    setArticolo( aModel.getArticolo() );
    setNote( aModel.getNote() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    //setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    
    //  Fabio - REWORK Bilanciamento Circostanze (a7-rr-165)
    setFlagSentenzaApplicazPena(aModel.getFlagSentenzaApplicazPena());
    setCodBilanciamentoCircostanze(aModel.getCodBilanciamentoCircostanze());
    setFlagGiudizioAbbreviato(aModel.getFlagGiudizioAbbreviato());
    //setProgrCircostanze(aModel.getProgrCircostanze());
    setNoteBilanciamento(aModel.getNoteBilanciamento());
    //***************************************
    //Federica - a9-rr-078
    //aggiunto campo Comma-Qualificante 
    setCommaQualificante( aModel.getCommaQualificante() );
    //***************************************    

    setCondizioneUpdate(aModel.getIdCircostanza());
  }

  public void setCondizione(CircostanzaModel aModel)
  {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
  }

	public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_CIRCOSTANZA = " + key );
  }

	public void setCondizioneUpdateFascicolo(BigDecimal aIdFascicolo)
	  {
	    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo );
	  }
	
	public void setCondizioneCircBilanciamento(BigDecimal aIdFascicolo)
	  {
	    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo + " AND COD_FONTE = '25' AND ARTICOLO = '442' ");
	  }
	
	public void setCondizioneUpdateFascicoloSige(BigDecimal aIdFascSenSige)
	{
	    setCondition(" ID_CIRCOSTANZA IN (SELECT CIR_ID_CIRCOSTANZA FROM CIRCOSTANZA_SENTENZA_SIGE WHERE FAS_SIGE_SEN_ID = " + aIdFascSenSige + ")");

	}

	
	
	
}
