package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;

import siap.dao.SIAPTableDAO;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;

/**
* <p>Title: CircostanzaCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Circostanza_Cumulo</p>
* @version 1.0
*/

public class CircostanzaCumuloDAO extends SIAPTableDAO
{
	public CircostanzaCumuloDAO (Connection con)
	{
	    super(con);
	    setTable("CIRCOSTANZA_CUMULO");
	
	    setSequenceField("ID_CIRCOSTANZA_CUMULO", "CIRCOSTANZA_CUMULO_SEQ");
	
	    setFieldKey("ID_CIRCOSTANZA_CUMULO", BIG_DECIMAL);
	
	    setField("COD_TIPO_CIRCOSTANZA", STRING);
	    setField("COD_FONTE", STRING);
	    setField("ANNO_FONTE", BIG_DECIMAL);
	    setField("NUMERO_FONTE", STRING);
	    setField("ARTICOLO", STRING);
	    setField("COD_SOTTONUMERAZIONE", STRING);
	    setField("COMMA", STRING);
	    setField("COMMA_QUALIFICANTE", STRING);
	    setField("LETTERA", STRING);
	    setField("NUMERO", STRING);
	
	    setField("NOTE", STRING);
	    setField("FLAG_SENTENZA_APPLICAZ_PENA", STRING);
	   	setField("FLAG_GIUDIZIO_ABBREVIATO", STRING);
	   	setField("COD_BILANCIAMENTO_CIRCOSTANZE", STRING);
	   	setField("NOTE_BILANCIAMENTO", STRING);
	   	
	    setField("COD_OPERATORE_INSERIMENTO", STRING);
	    setField("DATA_INSERIMENTO", DATE);
	    setField("COD_UFFICIO_INSERIMENTO", STRING);
	    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
	    setField("DATA_AGGIORNAMENTO", DATE);
	    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
	    
	    setField("ID_CIRCOSTANZA_ORIGINE", BIG_DECIMAL);
	    setField("FLAG_STATO", STRING);
	    setField("MOTIVO_MODIFICA", STRING);
	    setField("TIT_ID_TITOLO_CUMULATO", BIG_DECIMAL);    

	}

  //
  // METODI GET()
  //

  public BigDecimal 	getIdCircostanzaCumulo() 			throws DAOException	{ return getBigDecimal("ID_CIRCOSTANZA_CUMULO"); }
  public String 		getCodTipoCircostanza() 			throws DAOException	{ return getString("COD_TIPO_CIRCOSTANZA"); }
  
  public String 		getCodFonte() 						throws DAOException	{ return getString("COD_FONTE"); }
  public BigDecimal 	getAnnoFonte() 						throws DAOException	{ return getBigDecimal("ANNO_FONTE"); }
  public String 		getNumeroFonte() 					throws DAOException	{ return getString("NUMERO_FONTE"); }
  public String 		getArticolo() 						throws DAOException	{ return getString("ARTICOLO"); }
  public String 		getCodSottonumerazione() 			throws DAOException	{ return getString("COD_SOTTONUMERAZIONE"); }
  public String 		getComma() 							throws DAOException	{ return getString("COMMA"); }
  public String 		getCommaQualificante() 				throws DAOException	{ return getString("COMMA_QUALIFICANTE"); }
  public String 		getLettera() 						throws DAOException	{ return getString("LETTERA"); }
  public String 		getNumero() 						throws DAOException	{ return getString("NUMERO"); }
  
  public String 		getNote() 							throws DAOException	{ return getString("NOTE"); }
  public String 		getFlagSentenzaApplicazPena() 		throws DAOException { return getString("FLAG_SENTENZA_APPLICAZ_PENA"); }
  public String 		getCodBilanciamentoCircostanze() 	throws DAOException	{ return getString("COD_BILANCIAMENTO_CIRCOSTANZE"); }  
  public String 		getFlagGiudizioAbbreviato() 		throws DAOException { return getString("FLAG_GIUDIZIO_ABBREVIATO"); }
  public String 		getNoteBilanciamento() 		    	throws DAOException	{ return getString("NOTE_BILANCIAMENTO");}
  
  public String 		getCodOperatoreInserimento() 		throws DAOException	{ return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 			getDataInserimento() 				throws DAOException	{ return getDate("DATA_INSERIMENTO"); }
  public String 		getCodUfficioInserimento() 			throws DAOException	{ return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 		getCodOperatoreAggiornamento() 		throws DAOException	{ return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 			getDataAggiornamento() 				throws DAOException	{ return getDate("DATA_AGGIORNAMENTO"); }
  public String 		getCodUfficioAggiornamento() 		throws DAOException	{ return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  
  public BigDecimal 	getIdCircostanzaOrigine()		throws DAOException	 { return getBigDecimal("ID_CIRCOSTANZA_ORIGINE"); }
  public String      	getFlagStato()                  throws DAOException  { return getString     ("FLAG_STATO"                 ); } 
  public String      	getMotivoModifica()             throws DAOException  { return getString     ("MOTIVO_MODIFICA"            ); } 
  public BigDecimal  	getTitIdTitoloCumulato()        throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"     ); }

  //
  // METODI SET()
  //

  public void setIdCircostanzaCumulo(BigDecimal aValore ) 			{ setBigDecimal("ID_CIRCOSTANZA_CUMULO", aValore); }
  public void setCodTipoCircostanza(String aValore ) 			    { setString("COD_TIPO_CIRCOSTANZA", aValore); }
  
  public void setCodFonte(String aValore ) 			                { setString("COD_FONTE", aValore); }
  public void setAnnoFonte(BigDecimal aValore ) 			        { setBigDecimal("ANNO_FONTE", aValore); }
  public void setNumeroFonte(String aValore ) 			            { setString("NUMERO_FONTE", aValore); }
  public void setArticolo(String aValore ) 			                { setString("ARTICOLO", aValore); }
  public void setCodSottonumerazione(String aValore ) 			    { setString("COD_SOTTONUMERAZIONE", aValore); }
  public void setComma(String aValore ) 			                { setString("COMMA", aValore); }
  public void setCommaQualificante(String aValore ) 				{ setString("COMMA_QUALIFICANTE", aValore); }
  public void setLettera(String aValore ) 			                { setString("LETTERA", aValore); }
  public void setNumero(String aValore ) 			                { setString("NUMERO", aValore); }
  
  public void setNote(String aValore ) 			                    { setString("NOTE", aValore); }
  public void setFlagSentenzaApplicazPena(String aValore) 		{ setString("FLAG_SENTENZA_APPLICAZ_PENA", aValore); }
  public void setCodBilanciamentoCircostanze(String aValore) 	{ setString("COD_BILANCIAMENTO_CIRCOSTANZE", aValore); }  
  public void setFlagGiudizioAbbreviato(String aValore) 		{ setString("FLAG_GIUDIZIO_ABBREVIATO", aValore); }
  public void setNoteBilanciamento(String aValore) 				{ setString("NOTE_BILANCIAMENTO", aValore);}
  
  public void setCodOperatoreInserimento(String aValore ) 			{ setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 			        { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 			{ setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 		{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 			        { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 			{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }

  public void setIdCircostanzaOrigine	(BigDecimal aValore ) 		{ setBigDecimal("ID_CIRCOSTANZA_ORIGINE",	 aValore); }
  public void setFlagStato              (String      aValore )   	{ setString     ("FLAG_STATO"                 , aValore); } 
  public void setMotivoModifica         (String      aValore )   	{ setString     ("MOTIVO_MODIFICA"            , aValore); } 
  public void setTitIdTitoloCumulato    (BigDecimal  aValore )   	{ setBigDecimal ("TIT_ID_TITOLO_CUMULATO"     , aValore); } 
  

  public GenericModel getModel() throws DAOException
  {
    return new CircostanzaCumuloModel(
                                 getIdCircostanzaCumulo() ,
                                 getCodTipoCircostanza() ,
                                 "",	// aDescrTipoCircostanza
                                 getCodFonte() ,
                                 "",	// aDescrFonte
                                 getAnnoFonte() ,
                                 getNumeroFonte() ,
                                 getArticolo() ,
                                 getCodSottonumerazione() ,
                                 "",	// aDescrSottonumerazione
                                 getComma() ,
                                 getCommaQualificante(), 
                                 "",	// DescCommaQualificante
                                 getLettera() ,
                                 getNumero() ,
                                 getNote() ,
                                 getCodOperatoreInserimento() ,
                                 getDataInserimento() ,
                                 getCodUfficioInserimento() ,
                                 "",	// DescUfficioInserimento
                                 getCodOperatoreAggiornamento() ,
                                 getDataAggiornamento() ,
                                 getCodUfficioAggiornamento() ,
                                 "", 	// DescUfficioAggiornamento
                                 getFlagSentenzaApplicazPena(),
                                 getCodBilanciamentoCircostanze(),
                                 "", 	// aDescrBilanciamentoCircostanze
                                 getFlagGiudizioAbbreviato(),
                                 getNoteBilanciamento(),
                                 getIdCircostanzaOrigine(),
                                 getFlagStato(),
                                 getMotivoModifica(),
                                 getTitIdTitoloCumulato()
                               );
  }


  public void setDAOFromModel(CircostanzaCumuloModel aModel) throws DAOException
  {	
    setIdCircostanzaCumulo	( aModel.getIdCircostanzaCumulo() );
    setCodTipoCircostanza	( aModel.getCodTipoCircostanza() );
    
    setCodFonte				( aModel.getCodFonte() );
    setAnnoFonte			( aModel.getAnnoFonte() );
    setNumeroFonte			( aModel.getNumeroFonte() );
    setArticolo				( aModel.getArticolo() );
    setCodSottonumerazione	( aModel.getCodSottonumerazione() );
    setComma				( aModel.getComma() );
    setCommaQualificante	( aModel.getCommaQualificante() );
    setLettera				( aModel.getLettera() );
    setNumero				( aModel.getNumero() );
    
    setNote					( aModel.getNote() );
    setCodOperatoreInserimento	( aModel.getCodOperatoreInserimento() );
    setDataInserimento			( aModel.getDataInserimento() );
    setCodUfficioInserimento	( aModel.getCodUfficioInserimento() );
    //setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    //setDataAggiornamento( aModel.getDataAggiornamento() );
    //setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFlagSentenzaApplicazPena		(aModel.getFlagSentenzaApplicazPena());
    setCodBilanciamentoCircostanze	(aModel.getCodBilanciamentoCircostanze());
    setFlagGiudizioAbbreviato		(aModel.getFlagGiudizioAbbreviato());
    setNoteBilanciamento			(aModel.getNoteBilanciamento());
    
    setIdCircostanzaOrigine ( aModel.getIdCircostanzaOrigine() );
    setFlagStato          	( aModel.getFlagStato()             );  
    setMotivoModifica       ( aModel.getMotivoModifica()        );  
    setTitIdTitoloCumulato  ( aModel.getTitIdTitoloCumulato()   );  
    
  }

  public void setDAOFromModelForUpdate(CircostanzaCumuloModel aModel) throws DAOException
  {
    setCodTipoCircostanza		( aModel.getCodTipoCircostanza() );
    setCodFonte					( aModel.getCodFonte() );
    
    setAnnoFonte				( aModel.getAnnoFonte() );
    setNumeroFonte				( aModel.getNumeroFonte() );
    setArticolo					( aModel.getArticolo() );
    setCodSottonumerazione		( aModel.getCodSottonumerazione() );
    setComma					( aModel.getComma() );
    setCommaQualificante		( aModel.getCommaQualificante() );
    setLettera					( aModel.getLettera() );
    setNumero					( aModel.getNumero() );
    
    setNote						( aModel.getNote() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento		( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento	( aModel.getCodUfficioAggiornamento() );

    setFlagSentenzaApplicazPena		(aModel.getFlagSentenzaApplicazPena());
    setCodBilanciamentoCircostanze	(aModel.getCodBilanciamentoCircostanze());
    setFlagGiudizioAbbreviato		(aModel.getFlagGiudizioAbbreviato());
    setNoteBilanciamento			(aModel.getNoteBilanciamento());
    
    setIdCircostanzaOrigine ( aModel.getIdCircostanzaOrigine() );
    setFlagStato          	( aModel.getFlagStato()             );  
    setMotivoModifica       ( aModel.getMotivoModifica()        );  
    setTitIdTitoloCumulato  ( aModel.getTitIdTitoloCumulato()   );

    setCondizioneUpdate				(aModel.getIdCircostanzaCumulo());
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_CIRCOSTANZA_CUMULO = " + key );
  }

  public void setCondizioneUpdateTitoloCumulato(BigDecimal aIdTitoloCumulo)
  {
	    setCondition(" TIT_ID_TITOLO_CUMULATO = " + aIdTitoloCumulo );
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
