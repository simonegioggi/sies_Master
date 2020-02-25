package siap.sius.impugnazione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sius.impugnazione.model.ImpugnazioneModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: ImpugnazioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Impugnazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ImpugnazioneDAO extends TableDAO
{
  public ImpugnazioneDAO (Connection con)
  {
    super(con);
    setTable("IMPUGNAZIONE");

    //Settare la Sequence e i campi chiave

    setSequenceField("ID_IMPUGNAZIONE", "IMP_SEQ");
    setFieldKey("ID_IMPUGNAZIONE", BIG_DECIMAL);
    setField("ID_IMPUGNAZIONE", BIG_DECIMAL);
    setField("ANNO_S7", BIG_DECIMAL);
    setField("PROGR_S7", BIG_DECIMAL);
    setField("COD_TIPO_IMPUGNAZIONE", STRING);
    setField("SOGGETTO_IMPUGNANTE", STRING);
    setField("DATA_RICORSO", DATE);
    setField("DATA_ANNOTAZIONE", DATE);
    setField("ANNOTAZIONE", STRING);
    setField("DATA_ARRIVO_CANCELLERIA", DATE);
    setField("DATA_TRASMISSIONE_ATTI", DATE);
    setField("COD_AUTORITA_DESTINATARIA", STRING);
    setField("DATA_DECISIONE", DATE);
    setField("COD_TENORE_DECISIONE", STRING);
    setField("DATA_RESTITUZIONE_ATTI", DATE);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("DEP_OPID_DEPOSITO_ORDINANZA_PC", BIG_DECIMAL);
    setField("DEP_DEC_ID_DEPOSITO_DECRETO", BIG_DECIMAL);
// Nuovi campi 19-04-2005
    setField("FLAG_ANNULLAMENTO", STRING);
    setField("DATA_ANNULLAMENTO", DATE);
    setField("MOTIVO_ANNULLAMENTO", STRING);
    setField("FLAG_SOSP_ESEC", STRING);  // Nuovo campo 30-04-2007
    setField("DEP_ID_DEPOSITO_SENTENZA", BIG_DECIMAL);
    setField("DESCRIZIONE_ALTRO", STRING);
  }

  //
  // METODI GET()
  //
  public BigDecimal 		 getIdImpugnazione() 			throws DAOException	 { return getBigDecimal("ID_IMPUGNAZIONE"); }
  public BigDecimal 		 getAnnoS7() 					throws DAOException	 { return getBigDecimal("ANNO_S7"); }
  public BigDecimal 		 getProgrS7() 					throws DAOException	 { return getBigDecimal("PROGR_S7"); }
  public String 			 getCodTipoImpugnazione() 		throws DAOException	 { return getString("COD_TIPO_IMPUGNAZIONE"); }
  public String 			 getSoggettoImpugnante() 		throws DAOException	 { return getString("SOGGETTO_IMPUGNANTE"); }
  public Date 				 getDataRicorso() 				throws DAOException	 { return getDate("DATA_RICORSO"); }
  public Date 				 getDataAnnotazione() 			throws DAOException	 { return getDate("DATA_ANNOTAZIONE"); }
  public String 			 getAnnotazione() 				throws DAOException	 { return getString("ANNOTAZIONE"); }
  public Date 				 getDataArrivoCancelleria() 	throws DAOException	 { return getDate("DATA_ARRIVO_CANCELLERIA"); }
  public Date 				 getDataTrasmissioneAtti() 		throws DAOException	 { return getDate("DATA_TRASMISSIONE_ATTI"); }
  public String 			 getCodAutoritaDestinataria() 	throws DAOException	 { return getString("COD_AUTORITA_DESTINATARIA"); }
  public Date 				 getDataDecisione() 			throws DAOException	 { return getDate("DATA_DECISIONE"); }
  public String 			 getCodTenoreDecisione() 		throws DAOException	 { return getString("COD_TENORE_DECISIONE"); }
  public Date 				 getDataRestituzioneAtti() 		throws DAOException	 { return getDate("DATA_RESTITUZIONE_ATTI"); }
  public String 			 getCodOperatoreInserimento() 	throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 				 getDataInserimento() 			throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 			 getCodUfficioInserimento() 	throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 			 getCodOperatoreAggiornamento() throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 				 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 			 getCodUfficioAggiornamento() 	throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal 		 getDepOpidDepositoOrdinanzaPc()throws DAOException	 { return getBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC"); }
  public BigDecimal 		 getDepDecIdDepositoDecreto() 	throws DAOException	 { return getBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO"); }
  public String 		     getFlagAnnullamento() 			throws DAOException	 { return getString("FLAG_ANNULLAMENTO"); }
  public Date 			     getDataAnnullamento() 			throws DAOException	 { return getDate("DATA_ANNULLAMENTO"); }
  public String 		     getMotivoAnnullamento()		throws DAOException	 { return getString("MOTIVO_ANNULLAMENTO"); }
  public String 		     getFlagSospEsec() 		    	throws DAOException	 { return getString("FLAG_SOSP_ESEC"); }    // 30/04/2007
  public BigDecimal 		 getDepIdDepositoSentenza() 	throws DAOException	 { return getBigDecimal("DEP_ID_DEPOSITO_SENTENZA"); }
  public String 		     getDescrizioneAltro() 		    throws DAOException	 { return getString("DESCRIZIONE_ALTRO"); }
  
  //
  // METODI SET()
  //
  public void  	 setIdImpugnazione(BigDecimal aValore ) 				 { setBigDecimal("ID_IMPUGNAZIONE", aValore); }
  public void  	 setAnnoS7(BigDecimal aValore ) 						 { setBigDecimal("ANNO_S7", aValore); }
  public void  	 setProgrS7(BigDecimal aValore ) 			 			 { setBigDecimal("PROGR_S7", aValore); }
  public void  	 setCodTipoImpugnazione(String aValore ) 				 { setString("COD_TIPO_IMPUGNAZIONE", aValore); }
  public void  	 setSoggettoImpugnante(String aValore ) 				 { setString("SOGGETTO_IMPUGNANTE", aValore); }
  public void  	 setDataRicorso(Date aValore ) 							 { setDate("DATA_RICORSO", aValore); }
  public void  	 setDataAnnotazione(Date aValore ) 						 { setDate("DATA_ANNOTAZIONE", aValore); }
  public void  	 setAnnotazione(String aValore ) 						 { setString("ANNOTAZIONE", aValore); }
  public void  	 setDataArrivoCancelleria(Date aValore ) 				 { setDate("DATA_ARRIVO_CANCELLERIA", aValore); }
  public void  	 setDataTrasmissioneAtti(Date aValore ) 				 { setDate("DATA_TRASMISSIONE_ATTI", aValore); }
  public void  	 setCodAutoritaDestinataria(String aValore ) 			 { setString("COD_AUTORITA_DESTINATARIA", aValore); }
  public void  	 setDataDecisione(Date aValore ) 						 { setDate("DATA_DECISIONE", aValore); }
  public void  	 setCodTenoreDecisione(String aValore ) 				 { setString("COD_TENORE_DECISIONE", aValore); }
  public void  	 setDataRestituzioneAtti(Date aValore ) 			 	 { setDate("DATA_RESTITUZIONE_ATTI", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			 			 { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 			 	 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 			 		 { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setDepOpidDepositoOrdinanzaPc(BigDecimal aValore ) 	 { setBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC", aValore); }
  public void  	 setDepDecIdDepositoDecreto(BigDecimal aValore ) 		 { setBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO", aValore); }
  public void  	 setFlagAnnullamento(String aValore ) 					 { setString("FLAG_ANNULLAMENTO", aValore); }
  public void  	 setDataAnnullamento(Date aValore ) 			 		 { setDate("DATA_ANNULLAMENTO", aValore); }
  public void  	 setMotivoAnnullamento(String aValore ) 				 { setString("MOTIVO_ANNULLAMENTO", aValore); }
  public void  	 setFlagSospEsec(String aValore ) 			   			 { setString("FLAG_SOSP_ESEC", aValore); }    // 30/04/2007
  public void  	 setDepIdDepositoSentenza(BigDecimal aValore ) 			 { setBigDecimal("DEP_ID_DEPOSITO_SENTENZA", aValore); }
  public void  	 setDescrizioneAltro(String aValore ) 			   		 { setString("DESCRIZIONE_ALTRO", aValore); }
  
  public GenericModel getModel() throws DAOException
  {
    return new ImpugnazioneModel(
                  getIdImpugnazione() ,
                  getAnnoS7() ,
                  getProgrS7() ,
                  getCodTipoImpugnazione() ,
                  "",
                  getSoggettoImpugnante() ,
                  "",
                  getDataRicorso() ,
                  getDataAnnotazione() ,
                  getAnnotazione() ,
                  getDataArrivoCancelleria() ,
                  getDataTrasmissioneAtti() ,
                  getCodAutoritaDestinataria() ,
                  "",
                  getDataDecisione() ,
                  getCodTenoreDecisione() ,
                  "",
                  getDataRestituzioneAtti() ,
                  getCodOperatoreInserimento() ,
                  getDataInserimento() ,
                  getCodUfficioInserimento() ,
                  "",
                  getCodOperatoreAggiornamento() ,
                  getDataAggiornamento() ,
                  getCodUfficioAggiornamento() ,
                  "",
                  getDepOpidDepositoOrdinanzaPc() ,
                  getDepDecIdDepositoDecreto(),
                  getFlagAnnullamento(),
                  getDataAnnullamento(),
                  getMotivoAnnullamento(),
                  getFlagSospEsec(),
                  getDepIdDepositoSentenza(),
                  getDescrizioneAltro());
            }



  public void 	 setDAOFromModel(ImpugnazioneModel aModel) throws DAOException
  {
    setIdImpugnazione( aModel.getIdImpugnazione() );
    setAnnoS7( aModel.getAnnoS7() );
    setProgrS7( aModel.getProgrS7() );
    setCodTipoImpugnazione( aModel.getCodTipoImpugnazione() );
    setSoggettoImpugnante( aModel.getSoggettoImpugnante() );
    setDataRicorso( aModel.getDataRicorso() );
    setDataAnnotazione( aModel.getDataAnnotazione() );
    setAnnotazione( aModel.getAnnotazione() );
    setDataArrivoCancelleria( aModel.getDataArrivoCancelleria() );
    setDataTrasmissioneAtti( aModel.getDataTrasmissioneAtti() );
    setCodAutoritaDestinataria( aModel.getCodAutoritaDestinataria() );
    setDataDecisione( aModel.getDataDecisione() );
    setCodTenoreDecisione( aModel.getCodTenoreDecisione() );
    setDataRestituzioneAtti( aModel.getDataRestituzioneAtti() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setDepOpidDepositoOrdinanzaPc( aModel.getDepOpidDepositoOrdinanzaPc() );
    setDepDecIdDepositoDecreto( aModel.getDepDecIdDepositoDecreto() );
    setFlagAnnullamento(aModel.getFlagAnnullamento());
    setDataAnnullamento(aModel.getDataAnnullamento());
    setMotivoAnnullamento(aModel.getMotivoAnnullamento());
    setFlagSospEsec(aModel.getFlagSospEsec());  // 30/04/2007
    setDepIdDepositoSentenza( aModel.getDepIdDepositoSentenza() );
    setDescrizioneAltro(aModel.getDescrizioneAltro());
}


  public void 	 setDAOFromModelForUpdate(ImpugnazioneModel aModel) throws DAOException
  {
    setIdImpugnazione( aModel.getIdImpugnazione() );
    setAnnoS7( aModel.getAnnoS7() );
    setProgrS7( aModel.getProgrS7() );
    setCodTipoImpugnazione( aModel.getCodTipoImpugnazione() );
    setSoggettoImpugnante( aModel.getSoggettoImpugnante() );
    setDataRicorso( aModel.getDataRicorso() );
    setDataAnnotazione( aModel.getDataAnnotazione() );
    setAnnotazione( aModel.getAnnotazione() );
    setDataArrivoCancelleria( aModel.getDataArrivoCancelleria() );
    setDataTrasmissioneAtti( aModel.getDataTrasmissioneAtti() );
    setCodAutoritaDestinataria( aModel.getCodAutoritaDestinataria() );
    setDataDecisione( aModel.getDataDecisione() );
    setCodTenoreDecisione( aModel.getCodTenoreDecisione() );
    setDataRestituzioneAtti( aModel.getDataRestituzioneAtti() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setDepOpidDepositoOrdinanzaPc( aModel.getDepOpidDepositoOrdinanzaPc() );
    setDepDecIdDepositoDecreto( aModel.getDepDecIdDepositoDecreto() );
    setFlagAnnullamento(aModel.getFlagAnnullamento());
    setDataAnnullamento(aModel.getDataAnnullamento());
    setMotivoAnnullamento(aModel.getMotivoAnnullamento());
    setFlagSospEsec(aModel.getFlagSospEsec());  // 30/04/2007
    setDepIdDepositoSentenza(aModel.getDepIdDepositoSentenza());
    setDescrizioneAltro(aModel.getDescrizioneAltro()); 

    setCondizioneUpdate(aModel.getIdImpugnazione());
  }


  public void setCondizione(ImpugnazioneModel aModel)
  {
    String lCondizioni = new String();
    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }


  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_IMPUGNAZIONE = " + key );
  }
}
