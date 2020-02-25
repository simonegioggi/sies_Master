package siap.siep.notiziareato.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.notiziareato.model.NotiziaReatoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: NotiziaReatoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella NotiziaReato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class NotiziaReatoDAO extends TableDAO
{
	public NotiziaReatoDAO (Connection con)
	{
			 super(con);
			 setTable("NOTIZIA_REATO");

			 //Settare la Sequence e i campi chiave
       setSequenceField("ID_NOTIZIA_REATO", "NOT_REA_SEQ");

       setFieldKey("ID_NOTIZIA_REATO",BIG_DECIMAL);
			 setField("ID_NOTIZIA_REATO", BIG_DECIMAL);
			 setField("PROGR_NOTIZIA", STRING);
			 setField("DATA_PERVENIMENTO", DATE);
			 setField("ACQUISIZIONE_DIRETTA", STRING);
			 setField("DATA_FATTO", DATE);
			 setField("COD_FONTE", STRING);
			 setField("TIPO_FONTE", STRING);
			 setField("COD_COMUNE_FONTE", STRING);
			 setField("NUM_REG_AUTORITA", STRING);
			 setField("LUOGO_PROVENIENZA", STRING);
			 setField("DATA_ACQUISIZIONE", DATE);
			 setField("NUMERO_RICEVUTA", STRING);
			 setField("DESCRIZIONE_FONTE", STRING);
       setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 //modifiche per integrazione REGE-SIES			  
			 setField("DATA_ARRESTO", DATE); 
			 setField("DATA_FERMO", DATE); 
			 setField("FLAG_ARRESTATO", STRING); 
			 setField("FLAG_FOTOSEGNALATO", STRING);
			 setField("DATA_FOTO", DATE);
			 setField("COD_AUTORITA_FOTO", STRING);
			 setField("COD_COMUNE_FOTO", STRING);
			 setField("FAS_ID_FASCICOLO_SIGE", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdNotiziaReato() 		throws DAOException	 { return getBigDecimal("ID_NOTIZIA_REATO"); }
			public String 				 getProgrNotizia() 		throws DAOException	 { return getString("PROGR_NOTIZIA"); }
			public Date 					 getDataPervenimento() 		throws DAOException	 { return getDate("DATA_PERVENIMENTO"); }
			public String 				 getAcquisizioneDiretta() 		throws DAOException	 { return getString("ACQUISIZIONE_DIRETTA"); }
			public Date 					 getDataFatto() 		throws DAOException	 { return getDate("DATA_FATTO"); }
			public String 				 getCodFonte() 		throws DAOException	 { return getString("COD_FONTE"); }
			public String 				 getTipoFonte() 		throws DAOException	 { return getString("TIPO_FONTE"); }
			public String 				 getCodComuneFonte() 		throws DAOException	 { return getString("COD_COMUNE_FONTE"); }
			public String 				 getNumRegAutorita() 		throws DAOException	 { return getString("NUM_REG_AUTORITA"); }
			public String 				 getLuogoProvenienza() 		throws DAOException	 { return getString("LUOGO_PROVENIENZA"); }
			public Date 					 getDataAcquisizione() 		throws DAOException	 { return getDate("DATA_ACQUISIZIONE"); }
			public String 				 getNumeroRicevuta() 		throws DAOException	 { return getString("NUMERO_RICEVUTA"); }
			public String 				 getDescrizioneFonte() 		throws DAOException	 { return getString("DESCRIZIONE_FONTE"); }
      public BigDecimal 		 getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
			//modifiche per integrazione REGE-SIES
			public String 				getFlagArrestato() 			throws DAOException	 { return getString("FLAG_ARRESTATO"); }
			public String 				getFlagFotosegnalato() 		throws DAOException	 { return getString("FLAG_FOTOSEGNALATO"); }
			public Date 				getDataArresto() 			throws DAOException	 { return getDate("DATA_ARRESTO"); }
			public Date 				getDataFermo() 				throws DAOException	 { return getDate("DATA_FERMO"); }
			public Date 				getDataFoto()				throws DAOException	 {return getDate("DATA_FOTO");}
			public String				getCodAutoritaFoto()			throws DAOException	 {return getString("COD_AUTORITA_FOTO");}
			public String				getCodComuneFoto() 			throws DAOException	 {return getString("COD_COMUNE_FOTO");}
			public BigDecimal 		 getFasIdFascicoloSige() 		throws DAOException	 { return getBigDecimal("FAS_ID_FASCICOLO_SIGE"); }


  //
  // METODI SET()
  //

			public void  	 setIdNotiziaReato(BigDecimal aValore ) 			 { setBigDecimal("ID_NOTIZIA_REATO", aValore); }
			public void  	 setProgrNotizia(String aValore ) 			 { setString("PROGR_NOTIZIA", aValore); }
			public void  	 setDataPervenimento(Date aValore ) 			 { setDate("DATA_PERVENIMENTO", aValore); }
			public void  	 setAcquisizioneDiretta(String aValore ) 			 { setString("ACQUISIZIONE_DIRETTA", aValore); }
			public void  	 setDataFatto(Date aValore ) 			 { setDate("DATA_FATTO", aValore); }
			public void  	 setCodFonte(String aValore ) 			 { setString("COD_FONTE", aValore); }
			public void  	 setTipoFonte(String aValore ) 			 { setString("TIPO_FONTE", aValore); }
			public void  	 setCodComuneFonte(String aValore ) 			 { setString("COD_COMUNE_FONTE", aValore); }
			public void  	 setNumRegAutorita(String aValore ) 			 { setString("NUM_REG_AUTORITA", aValore); }
			public void  	 setLuogoProvenienza(String aValore ) 			 { setString("LUOGO_PROVENIENZA", aValore); }
			public void  	 setDataAcquisizione(Date aValore ) 			 { setDate("DATA_ACQUISIZIONE", aValore); }
			public void  	 setNumeroRicevuta(String aValore ) 			 { setString("NUMERO_RICEVUTA", aValore); }
			public void  	 setDescrizioneFonte(String aValore ) 			 { setString("DESCRIZIONE_FONTE", aValore); }
      public void		 setFasSieIdFascicoloSiep(BigDecimal aValore ) 		             { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
			 //modifiche per integrazione REGE-SIES
			public void  	 setFlagArrestato(String aValore) 		{setString("FLAG_ARRESTATO", aValore); }
			public void  	 setFlagFotosegnalato(String aValore) 	{setString("FLAG_FOTOSEGNALATO", aValore); }
			public void  	 setDataArresto(Date aValore) 			{setDate("DATA_ARRESTO", aValore); }
			public void  	 setDataFermo(Date aValore) 			{setDate("DATA_FERMO", aValore); }
			public void 	 setDataFoto(Date aValore)				{setDate("DATA_FOTO", aValore);}
			public void 	 setCodAutoritaFoto(String aValore)		{setString("COD_AUTORITA_FOTO", aValore);}
			public void 	 setCodComuneFoto(String aValore) 			{setString("COD_COMUNE_FOTO", aValore);}
			public void		 setFasIdFascicoloSige(BigDecimal aValore ) 		             { setBigDecimal("FAS_ID_FASCICOLO_SIGE", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new NotiziaReatoModel(
								 getIdNotiziaReato() ,
								 getProgrNotizia() ,
								 getDataPervenimento() ,
								 getAcquisizioneDiretta() ,
								 getDataFatto() ,
								 getCodFonte() ,
								 "",
								 getTipoFonte() ,
								 getCodComuneFonte() ,
								 "",
								 getNumRegAutorita() ,
								 getLuogoProvenienza() ,
								 getDataAcquisizione() ,
								 getNumeroRicevuta() ,
								 getDescrizioneFonte() ,
                 getFasSieIdFascicoloSiep(),
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento(),
								 getDataArresto(),
								 getDataFermo(),
								 getFlagFotosegnalato(),
								 getFlagArrestato(),
								 getDataFoto(),
								 getCodAutoritaFoto(),
								 getCodComuneFoto(),
								 getFasIdFascicoloSige()
								 );
		}


	 public void 	 setDAOFromModel(NotiziaReatoModel aModel) throws DAOException
  		{
				 setIdNotiziaReato( aModel.getIdNotiziaReato() );
				 setProgrNotizia( aModel.getProgrNotizia() );
				 setDataPervenimento( aModel.getDataPervenimento() );
				 setAcquisizioneDiretta( aModel.getAcquisizioneDiretta() );
				 setDataFatto( aModel.getDataFatto() );
				 setCodFonte( aModel.getCodFonte() );
				 setTipoFonte( aModel.getTipoFonte() );
				 setCodComuneFonte( aModel.getCodComuneFonte() );
				 setNumRegAutorita( aModel.getNumRegAutorita() );
				 setLuogoProvenienza( aModel.getLuogoProvenienza() );
				 setDataAcquisizione( aModel.getDataAcquisizione() );
				 setNumeroRicevuta( aModel.getNumeroRicevuta() );
				 setDescrizioneFonte( aModel.getDescrizioneFonte() );
         setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );				 				 
				 //modifiche per integrazione REGE-SIES
				 setDataArresto(aModel.getDataArresto());
				 setDataFermo(aModel.getDataFermo());
				 setFlagFotosegnalato(aModel.getFlagFotosegnalato());
				 setFlagArrestato(aModel.getFlagArrestato());
				 setDataFoto(aModel.getDataFoto());
				 setCodAutoritaFoto(aModel.getCodAutoritaFoto());
				 setCodComuneFoto(aModel.getCodComuneFoto());
				 setFasIdFascicoloSige( aModel.getFasIdFascicoloSige() );
				
		}


	 public void 	 setDAOFromModelForUpdate(NotiziaReatoModel aModel) throws DAOException
  		{
				 //setIdNotiziaReato( aModel.getIdNotiziaReato() );
				 setProgrNotizia( aModel.getProgrNotizia() );
				 setDataPervenimento( aModel.getDataPervenimento() );
				 setAcquisizioneDiretta( aModel.getAcquisizioneDiretta() );
				 setDataFatto( aModel.getDataFatto() );
				 setCodFonte( aModel.getCodFonte() );
				 setTipoFonte( aModel.getTipoFonte() );
				 setCodComuneFonte( aModel.getCodComuneFonte() );
         setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
				 setNumRegAutorita( aModel.getNumRegAutorita() );
				 setLuogoProvenienza( aModel.getLuogoProvenienza() );
				 setDataAcquisizione( aModel.getDataAcquisizione() );
				 setNumeroRicevuta( aModel.getNumeroRicevuta() );
				 setDescrizioneFonte( aModel.getDescrizioneFonte() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );		 
		 	//modifiche per integrazione REGE-SIES
			setFlagArrestato(aModel.getFlagArrestato());
			setFlagFotosegnalato(aModel.getFlagFotosegnalato());
			setDataArresto(aModel.getDataArresto());
			setDataFermo(aModel.getDataFermo());
			setDataFoto(aModel.getDataFoto());
			setCodAutoritaFoto(aModel.getCodAutoritaFoto());
			setCodComuneFoto(aModel.getCodComuneFoto());
			setCondizioneUpdate(aModel.getIdNotiziaReato());
			setFasIdFascicoloSige( aModel.getFasIdFascicoloSige() );
		}


	public void setCondizione(NotiziaReatoModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_NOTIZIA_REATO = " + key );
		 }

}
