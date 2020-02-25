package siap.siep.archiviazione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.archiviazione.model.ArchiviazioneModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: ArchiviazioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Archiviazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ArchiviazioneDAO extends TableDAO
{
	public ArchiviazioneDAO (Connection con)
	{
			 super(con);
			 setTable("ARCHIVIAZIONE");

       setSequenceField("ID_ARCHIVIAZIONE","ARC_SEQ");
       setFieldKey("ID_ARCHIVIAZIONE", BIG_DECIMAL);

			 setField("ID_ARCHIVIAZIONE", BIG_DECIMAL);
			 setField("COD_TIPO_PROVVEDIMENTO", STRING);
			 setField("DATA_EMISSIONE", DATE);
			 setField("DATA_RICEZIONE", DATE);
			 setField("ANNO_NOTA", BIG_DECIMAL);
			 setField("NUM_NOTA", STRING);
			 setField("COD_PROVVEDIMENTO", STRING);
			 setField("ANNO_PROVVEDIMENTO", BIG_DECIMAL);
			 setField("NUM_PROVVEDIMENTO", STRING);
			 setField("COD_TIPO_PROVVEDIMENTO_ARC", STRING);
			 setField("DATA_DEFINIZIONE", DATE);
			 setField("COD_OGGETTO_DEFINIZIONE", STRING);
			 setField("COD_TIPO_EMITTENTE", STRING);
			 setField("COD_TIPO_AUTORITA_EMITTENTE", STRING);
			 setField("COD_LUOGO_EMITTENTE", STRING);
			 setField("INDIRIZZO_EMITTENTE", STRING);
			 setField("ALTRA_AUTORITA", STRING);
			 setField("NOTE", STRING);
			 setField("FLAG_ANNULLAMENTO", STRING);
			 setField("DATA_ANNULLAMENTO", DATE);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
			 setField("EVE_ID_EVENTO", BIG_DECIMAL);
			 setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
			 setField("CSS_ID_CSSA", BIG_DECIMAL);
// 02-04-2015
			 setField("CHIAVE_ANNO", BIG_DECIMAL);
			 setField("CHIAVE_PROGR", BIG_DECIMAL);
// 22-06-2015
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdArchiviazione() 		throws DAOException	 { return getBigDecimal("ID_ARCHIVIAZIONE"); }
			public String 				 getCodTipoProvvedimento() 		throws DAOException	 { return getString("COD_TIPO_PROVVEDIMENTO"); }
			public Date 					 getDataEmissione() 		throws DAOException	 { return getDate("DATA_EMISSIONE"); }
			public Date 					 getDataRicezione() 		throws DAOException	 { return getDate("DATA_RICEZIONE"); }
			public BigDecimal 		 getAnnoNota() 		throws DAOException	 { return getBigDecimal("ANNO_NOTA"); }
			public String 				 getNumNota() 		throws DAOException	 { return getString("NUM_NOTA"); }
			public String 				 getCodProvvedimento() 		throws DAOException	 { return getString("COD_PROVVEDIMENTO"); }
			public BigDecimal 		 getAnnoProvvedimento() 		throws DAOException	 { return getBigDecimal("ANNO_PROVVEDIMENTO"); }
			public String 				 getNumProvvedimento() 		throws DAOException	 { return getString("NUM_PROVVEDIMENTO"); }
			public String 				 getCodTipoProvvedimentoArc() 		throws DAOException	 { return getString("COD_TIPO_PROVVEDIMENTO_ARC"); }
			public Date 					 getDataDefinizione() 		throws DAOException	 { return getDate("DATA_DEFINIZIONE"); }
			public String 				 getCodOggettoDefinizione() 		throws DAOException	 { return getString("COD_OGGETTO_DEFINIZIONE"); }
			public String 				 getCodTipoEmittente() 		throws DAOException	 { return getString("COD_TIPO_EMITTENTE"); }
			public String 				 getCodTipoAutoritaEmittente() 		throws DAOException	 { return getString("COD_TIPO_AUTORITA_EMITTENTE"); }
			public String 				 getCodLuogoEmittente() 		throws DAOException	 { return getString("COD_LUOGO_EMITTENTE"); }
			public String 				 getIndirizzoEmittente() 		throws DAOException	 { return getString("INDIRIZZO_EMITTENTE"); }
			public String 				 getAltraAutorita() 		throws DAOException	 { return getString("ALTRA_AUTORITA"); }
			public String 				 getNote() 		throws DAOException	 { return getString("NOTE"); }
			public String 				 getFlagAnnullamento() 		throws DAOException	 { return getString("FLAG_ANNULLAMENTO"); }
			public Date 					 getDataAnnullamento() 		throws DAOException	 { return getDate("DATA_ANNULLAMENTO"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public BigDecimal 		 getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
			public BigDecimal 		 getEveIdEvento() 		throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }
			public String 				 getIstDetIdIstitutoDetenzione() 		throws DAOException	 { return getString("IST_DET_ID_ISTITUTO_DETENZIONE"); }
			public BigDecimal 		 getCssIdCssa() 		throws DAOException	 { return getBigDecimal("CSS_ID_CSSA"); }

			public BigDecimal 		 getChiaveAnno() 		throws DAOException	 { return getBigDecimal("CHIAVE_ANNO"); }
			public BigDecimal 		 getChiaveProgr() 		throws DAOException	 { return getBigDecimal("CHIAVE_PROGR"); }

			public String 			 getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 			 getDataAggiornamento() 			throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 			 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
			
  //
  // METODI SET()
  //

			public void  	 setIdArchiviazione(BigDecimal aValore ) 			 { setBigDecimal("ID_ARCHIVIAZIONE", aValore); }
			public void  	 setCodTipoProvvedimento(String aValore ) 			 { setString("COD_TIPO_PROVVEDIMENTO", aValore); }
			public void  	 setDataEmissione(Date aValore ) 			 { setDate("DATA_EMISSIONE", aValore); }
			public void  	 setDataRicezione(Date aValore ) 			 { setDate("DATA_RICEZIONE", aValore); }
			public void  	 setAnnoNota(BigDecimal aValore ) 			 { setBigDecimal("ANNO_NOTA", aValore); }
			public void  	 setNumNota(String aValore ) 			 { setString("NUM_NOTA", aValore); }
			public void  	 setCodProvvedimento(String aValore ) 			 { setString("COD_PROVVEDIMENTO", aValore); }
			public void  	 setAnnoProvvedimento(BigDecimal aValore ) 			 { setBigDecimal("ANNO_PROVVEDIMENTO", aValore); }
			public void  	 setNumProvvedimento(String aValore ) 			 { setString("NUM_PROVVEDIMENTO", aValore); }
			public void  	 setCodTipoProvvedimentoArc(String aValore ) 			 { setString("COD_TIPO_PROVVEDIMENTO_ARC", aValore); }
			public void  	 setDataDefinizione(Date aValore ) 			 { setDate("DATA_DEFINIZIONE", aValore); }
			public void  	 setCodOggettoDefinizione(String aValore ) 			 { setString("COD_OGGETTO_DEFINIZIONE", aValore); }
			public void  	 setCodTipoEmittente(String aValore ) 			 { setString("COD_TIPO_EMITTENTE", aValore); }
			public void  	 setCodTipoAutoritaEmittente(String aValore ) 			 { setString("COD_TIPO_AUTORITA_EMITTENTE", aValore); }
			public void  	 setCodLuogoEmittente(String aValore ) 			 { setString("COD_LUOGO_EMITTENTE", aValore); }
			public void  	 setIndirizzoEmittente(String aValore ) 			 { setString("INDIRIZZO_EMITTENTE", aValore); }
			public void  	 setAltraAutorita(String aValore ) 			 { setString("ALTRA_AUTORITA", aValore); }
			public void  	 setNote(String aValore ) 			 { setString("NOTE", aValore); }
			public void  	 setFlagAnnullamento(String aValore ) 			 { setString("FLAG_ANNULLAMENTO", aValore); }
			public void  	 setDataAnnullamento(Date aValore ) 			 { setDate("DATA_ANNULLAMENTO", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
			public void  	 setEveIdEvento(BigDecimal aValore ) 			 { setBigDecimal("EVE_ID_EVENTO", aValore); }
			public void  	 setIstDetIdIstitutoDetenzione(String aValore ) 			 { setString("IST_DET_ID_ISTITUTO_DETENZIONE", aValore); }
			public void  	 setCssIdCssa(BigDecimal aValore ) 			 { setBigDecimal("CSS_ID_CSSA", aValore); }
			
			public void  	 setChiaveAnno(BigDecimal aValore ) 			 { setBigDecimal("CHIAVE_ANNO", aValore); }
			public void  	 setChiaveProgr(BigDecimal aValore ) 			 { setBigDecimal("CHIAVE_PROGR", aValore); }
			
			public void  	 setCodOperatoreAggiornamento(String aValore ) 	 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 	 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }

	public GenericModel getModel() throws DAOException
  			 {
 				 return new ArchiviazioneModel(
								 getIdArchiviazione() ,
								 getCodTipoProvvedimento() ,
								 "",
								 getDataEmissione() ,
								 getDataRicezione() ,
								 getAnnoNota() ,
								 getNumNota() ,
								 getCodProvvedimento() ,
								 "",
								 getAnnoProvvedimento() ,
								 getNumProvvedimento() ,
								 getCodTipoProvvedimentoArc() ,
								 "",
								 getDataDefinizione() ,
								 getCodOggettoDefinizione() ,
								 "",
								 getCodTipoEmittente() ,
								 "",
								 getCodTipoAutoritaEmittente() ,
								 "",
								 getCodLuogoEmittente() ,
								 "",
								 getIndirizzoEmittente() ,
								 getAltraAutorita() ,
								 getNote() ,
								 getFlagAnnullamento() ,
								 getDataAnnullamento() ,
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getFasSieIdFascicoloSiep() ,
								 getEveIdEvento() ,
								 getIstDetIdIstitutoDetenzione() ,
								 getCssIdCssa() ,
								 
								 getChiaveAnno() ,
								 getChiaveProgr(),
								 
								 getCodOperatoreAggiornamento(),
								 getDataAggiornamento(),
								 getCodUfficioAggiornamento() 
								);
		}


	 public void 	 setDAOFromModel(ArchiviazioneModel aModel) throws DAOException
  		{
				 setIdArchiviazione( aModel.getIdArchiviazione() );
				 setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
				 setDataEmissione( aModel.getDataEmissione() );
				 setDataRicezione( aModel.getDataRicezione() );
				 setAnnoNota( aModel.getAnnoNota() );
				 setNumNota( aModel.getNumNota() );
				 setCodProvvedimento( aModel.getCodProvvedimento() );
				 setAnnoProvvedimento( aModel.getAnnoProvvedimento() );
				 setNumProvvedimento( aModel.getNumProvvedimento() );
				 setCodTipoProvvedimentoArc( aModel.getCodTipoProvvedimentoArc() );
				 setDataDefinizione( aModel.getDataDefinizione() );
				 setCodOggettoDefinizione( aModel.getCodOggettoDefinizione() );
				 setCodTipoEmittente( aModel.getCodTipoEmittente() );
				 setCodTipoAutoritaEmittente( aModel.getCodTipoAutoritaEmittente() );
				 setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
				 setIndirizzoEmittente( aModel.getIndirizzoEmittente() );
				 setAltraAutorita( aModel.getAltraAutorita() );
				 setNote( aModel.getNote() );
				 setFlagAnnullamento( aModel.getFlagAnnullamento() );
				 setDataAnnullamento( aModel.getDataAnnullamento() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
				 setEveIdEvento( aModel.getEveIdEvento() );
				 setIstDetIdIstitutoDetenzione( aModel.getIstDetIdIstitutoDetenzione() );
				 setCssIdCssa( aModel.getCssIdCssa() );
//	02-04-2015	
				 setChiaveAnno(aModel.getChiaveAnno() );
				 setChiaveProgr(aModel.getChiaveProgr() );
		}


	 public void 	 setDAOFromModelForUpdate(ArchiviazioneModel aModel) throws DAOException
  		{
				 setIdArchiviazione( aModel.getIdArchiviazione() );
				 setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
				 setDataEmissione( aModel.getDataEmissione() );
				 setDataRicezione( aModel.getDataRicezione() );
				 setAnnoNota( aModel.getAnnoNota() );
				 setNumNota( aModel.getNumNota() );
				 setCodProvvedimento( aModel.getCodProvvedimento() );
				 setAnnoProvvedimento( aModel.getAnnoProvvedimento() );
				 setNumProvvedimento( aModel.getNumProvvedimento() );
				 setCodTipoProvvedimentoArc( aModel.getCodTipoProvvedimentoArc() );
				 setDataDefinizione( aModel.getDataDefinizione() );
				 setCodOggettoDefinizione( aModel.getCodOggettoDefinizione() );
				 setCodTipoEmittente( aModel.getCodTipoEmittente() );
				 setCodTipoAutoritaEmittente( aModel.getCodTipoAutoritaEmittente() );
				 setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
				 setIndirizzoEmittente( aModel.getIndirizzoEmittente() );
				 setAltraAutorita( aModel.getAltraAutorita() );
				 setNote( aModel.getNote() );
				 setFlagAnnullamento( aModel.getFlagAnnullamento() );
				 setDataAnnullamento( aModel.getDataAnnullamento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
				 setEveIdEvento( aModel.getEveIdEvento() );
				 setIstDetIdIstitutoDetenzione( aModel.getIstDetIdIstitutoDetenzione() );
				 setCssIdCssa( aModel.getCssIdCssa() );
// 02-04-2015
				 setChiaveAnno(aModel.getChiaveAnno() );
				 setChiaveProgr(aModel.getChiaveProgr() );
//  22-06-2015
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
//				 
		     setCondizioneUpdate(aModel.getIdArchiviazione());
		}


	public void setCondizione(ArchiviazioneModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_ARCHIVIAZIONE = " + key );
		 }

}
