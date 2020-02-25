package siap.sico.storicosoggetto.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.storicosoggetto.model.StoricoSoggettoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: StoricoSoggettoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella StoricoSoggetto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class StoricoSoggettoDAO extends TableDAO
{
	public StoricoSoggettoDAO (Connection con)
	{
			 super(con);
			 setTable("storico_soggetto");

			 //Settare la Sequence e i campi chiave

			 setField("PROGRESSIVO_STORICO", BIG_DECIMAL);
			 setField("DATA_VARIAZIONE", DATE);
			 setField("ID_SOGGETTO_VARIATO", BIG_DECIMAL);
			 setField("COD_FISCALE", STRING);
			 setField("COD_CS", STRING);
			 setField("COD_AFIS", STRING);
			 setField("COGNOME", STRING);
			 setField("NOME", STRING);
			 setField("ANNO_NASCITA", BIG_DECIMAL);
			 setField("DATA_NASCITA", DATE);
			 setField("DATA_NASCITA_PRESUNTA", STRING);
			 setField("COD_COMUNE_NASCITA", STRING);
			 setField("COD_PROVINCIA_NASCITA", STRING);
			 setField("COD_STATO_NASCITA", STRING);
			 setField("DESC_COMUNE_NASCITA_ESTERO", STRING);
			 setField("NAZIONALITA", STRING);
			 setField("PATERNITA", STRING);
			 setField("COGNOME_MADRE", STRING);
			 setField("NOME_MADRE", STRING);
			 setField("SESSO", STRING);
			 setField("ATTO_NASCITA", STRING);
			 setField("NOTE", STRING);
			 setField("COD_COMUNE_CASELLARIO", STRING);
			 setField("FLAG_PRESENZA_FASCICOLO", STRING);
			 setField("MESE_NASCITA", BIG_DECIMAL);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
			 setField("ID_SOGGETTO_NUOVO", BIG_DECIMAL);
			 setField("FAS_SIE_ID_FASCICOLO_SIUS", BIG_DECIMAL);
			 setField("ETA_PRESUNTA_ANNI", BIG_DECIMAL);
			 setField("ETA_PRESUNTA_MESI", BIG_DECIMAL);

	}


		//
		// METODI GET()
		//
		public BigDecimal 		 	 getProgressivoStorico() 			throws DAOException	 { return getBigDecimal("PROGRESSIVO_STORICO"); }
		public Date 				 getDataVariazione() 				throws DAOException	 { return getDate("DATA_VARIAZIONE"); }
		public BigDecimal 			 getIdSoggettoVariato() 			throws DAOException	 { return getBigDecimal("ID_SOGGETTO_VARIATO"); }
		public String 				 getCodFiscale() 					throws DAOException	 { return getString("COD_FISCALE"); }
		public String 				 getCodCs() 						throws DAOException	 { return getString("COD_CS"); }
		public String 				 getCodAfis() 						throws DAOException	 { return getString("COD_AFIS"); }
		public String 				 getCognome() 						throws DAOException	 { return getString("COGNOME"); }
		public String 				 getNome() 							throws DAOException	 { return getString("NOME"); }
		public BigDecimal 		 	 getAnnoNascita() 					throws DAOException	 { return getBigDecimal("ANNO_NASCITA"); }
		public Date 				 getDataNascita() 					throws DAOException	 { return getDate("DATA_NASCITA"); }
		public String 				 getDataNascitaPresunta() 			throws DAOException	 { return getString("DATA_NASCITA_PRESUNTA"); }
		public String 				 getCodComuneNascita() 				throws DAOException	 { return getString("COD_COMUNE_NASCITA"); }
		public String 				 getCodProvinciaNascita() 			throws DAOException	 { return getString("COD_PROVINCIA_NASCITA"); }
		public String 				 getCodStatoNascita() 				throws DAOException	 { return getString("COD_STATO_NASCITA"); }
		public String 				 getDescComuneNascitaEstero() 		throws DAOException	 { return getString("DESC_COMUNE_NASCITA_ESTERO"); }
		public String 				 getNazionalita() 					throws DAOException	 { return getString("NAZIONALITA"); }
		public String 				 getPaternita() 					throws DAOException	 { return getString("PATERNITA"); }
		public String 				 getCognomeMadre() 					throws DAOException	 { return getString("COGNOME_MADRE"); }
		public String 				 getNomeMadre() 					throws DAOException	 { return getString("NOME_MADRE"); }
		public String 				 getSesso() 						throws DAOException	 { return getString("SESSO"); }
		public String 				 getAttoNascita() 					throws DAOException	 { return getString("ATTO_NASCITA"); }
		public String 				 getNote() 							throws DAOException	 { return getString("NOTE"); }
		public String 				 getCodComuneCasellario() 			throws DAOException	 { return getString("COD_COMUNE_CASELLARIO"); }
		public String 				 getFlagPresenzaFascicolo() 		throws DAOException	 { return getString("FLAG_PRESENZA_FASCICOLO"); }
		public BigDecimal 			 getMeseNascita() 					throws DAOException	 { return getBigDecimal("MESE_NASCITA"); }
		public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
		public Date 				 getDataInserimento() 				throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
		public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
		public String 				 getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
		public Date 				 getDataAggiornamento() 			throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
		public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
		public BigDecimal 			 getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
		public BigDecimal 			 getIdSoggettoNuovo() 				throws DAOException	 { return getBigDecimal("ID_SOGGETTO_NUOVO"); }
		public BigDecimal 		 	 getFasSieIdFascicoloSius() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIUS"); }
		public BigDecimal 		 	 getEtapresuntaAnni() 				throws DAOException	 { return getBigDecimal("ETA_PRESUNTA_ANNI"); }
		public BigDecimal 		 	 getEtaPresuntaMesi() 				throws DAOException	 { return getBigDecimal("ETA_PRESUNTA_MESI"); }
  
		//
		// METODI SET()
		//
		public void  	 setProgressivoStorico(BigDecimal aValore ) 		 { setBigDecimal("PROGRESSIVO_STORICO", aValore); }
		public void  	 setDataVariazione(Date aValore ) 					 { setDate("DATA_VARIAZIONE", aValore); }
		public void  	 setIdSoggettoVariato(BigDecimal aValore ) 			 { setBigDecimal("ID_SOGGETTO_VARIATO", aValore); }
		public void  	 setCodFiscale(String aValore ) 					 { setString("COD_FISCALE", aValore); }
		public void  	 setCodCs(String aValore ) 							 { setString("COD_CS", aValore); }
		public void  	 setCodAfis(String aValore ) 						 { setString("COD_AFIS", aValore); }
		public void  	 setCognome(String aValore ) 						 { setString("COGNOME", aValore); }
		public void  	 setNome(String aValore ) 							 { setString("NOME", aValore); }
		public void  	 setAnnoNascita(BigDecimal aValore ) 				 { setBigDecimal("ANNO_NASCITA", aValore); }
		public void  	 setDataNascita(Date aValore ) 			 		     { setDate("DATA_NASCITA", aValore); }
		public void  	 setDataNascitaPresunta(String aValore ) 			 { setString("DATA_NASCITA_PRESUNTA", aValore); }
		public void  	 setCodComuneNascita(String aValore ) 			 	 { setString("COD_COMUNE_NASCITA", aValore); }
		public void  	 setCodProvinciaNascita(String aValore ) 			 { setString("COD_PROVINCIA_NASCITA", aValore); }
		public void  	 setCodStatoNascita(String aValore ) 			 	 { setString("COD_STATO_NASCITA", aValore); }
		public void  	 setDescComuneNascitaEstero(String aValore ) 		 { setString("DESC_COMUNE_NASCITA_ESTERO", aValore); }
		public void  	 setNazionalita(String aValore ) 					 { setString("NAZIONALITA", aValore); }
		public void  	 setPaternita(String aValore ) 						 { setString("PATERNITA", aValore); }
		public void  	 setCognomeMadre(String aValore ) 					 { setString("COGNOME_MADRE", aValore); }
		public void  	 setNomeMadre(String aValore ) 						 { setString("NOME_MADRE", aValore); }
		public void  	 setSesso(String aValore ) 							 { setString("SESSO", aValore); }
		public void  	 setAttoNascita(String aValore ) 					 { setString("ATTO_NASCITA", aValore); }
		public void  	 setNote(String aValore ) 							 { setString("NOTE", aValore); }
		public void  	 setCodComuneCasellario(String aValore ) 			 { setString("COD_COMUNE_CASELLARIO", aValore); }
		public void  	 setFlagPresenzaFascicolo(String aValore ) 			 { setString("FLAG_PRESENZA_FASCICOLO", aValore); }
		public void  	 setMeseNascita(BigDecimal aValore ) 				 { setBigDecimal("MESE_NASCITA", aValore); }
		public void  	 setCodOperatoreInserimento(String aValore ) 		 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
		public void  	 setDataInserimento(Date aValore ) 					 { setDate("DATA_INSERIMENTO", aValore); }
		public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
		public void  	 setCodOperatoreAggiornamento(String aValore ) 		 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
		public void  	 setDataAggiornamento(Date aValore ) 			 	 { setDate("DATA_AGGIORNAMENTO", aValore); }
		public void  	 setCodUfficioAggiornamento(String aValore ) 		 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
		public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 		 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
		public void  	 setIdSoggettoNuovo(BigDecimal aValore ) 			 { setBigDecimal("ID_SOGGETTO_NUOVO", aValore); }
		public void  	 setFasSieIdFascicoloSius(BigDecimal aValore ) 		 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIUS", aValore); }
		public void  	 setEtaPresuntaAnni(BigDecimal aValore ) 			 { setBigDecimal("ETA_PRESUNTA_ANNI", aValore); }
		public void  	 setEtaPresuntaMesi(BigDecimal aValore ) 			 { setBigDecimal("ETA_PRESUNTA_MESI", aValore); }

	public GenericModel getModel() throws DAOException
  			 {
 				 return new StoricoSoggettoModel(
								 getProgressivoStorico() ,
								 getDataVariazione() ,
								 getIdSoggettoVariato() ,
								 getCodFiscale() ,
								 "",
								 getCodCs() ,
								 "",
								 getCodAfis() ,
								 "",
								 getCognome() ,
								 getNome() ,
								 getAnnoNascita() ,
								 getDataNascita() ,
								 getDataNascitaPresunta() ,
								 getCodComuneNascita() ,
								 "",
								 getCodProvinciaNascita() ,
								 "",
								 getCodStatoNascita() ,
								 "",
								 getDescComuneNascitaEstero() ,
								 getNazionalita() ,
								 getPaternita() ,
								 getCognomeMadre() ,
								 getNomeMadre() ,
								 getSesso() ,
								 getAttoNascita() ,
								 getNote() ,
								 getCodComuneCasellario() ,
								 "",
								 getFlagPresenzaFascicolo() ,
								 getMeseNascita() ,
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getFasSieIdFascicoloSiep() ,
								 getIdSoggettoNuovo(),
								 getFasSieIdFascicoloSius(),
								 getEtapresuntaAnni(),
								 getEtaPresuntaMesi()
								);
		}


	 public void 	 setDAOFromModel(StoricoSoggettoModel aModel) throws DAOException
  		{
				 setProgressivoStorico( aModel.getProgressivoStorico() );
				 setDataVariazione( aModel.getDataVariazione() );
				 setIdSoggettoVariato( aModel.getIdSoggettoVariato() );
				 setCodFiscale( aModel.getCodFiscale() );
				 setCodCs( aModel.getCodCs() );
				 setCodAfis( aModel.getCodAfis() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setAnnoNascita( aModel.getAnnoNascita() );
				 setDataNascita( aModel.getDataNascita() );
				 setDataNascitaPresunta( aModel.getDataNascitaPresunta() );
				 setCodComuneNascita( aModel.getCodComuneNascita() );
				 setCodProvinciaNascita( aModel.getCodProvinciaNascita() );
				 setCodStatoNascita( aModel.getCodStatoNascita() );
				 setDescComuneNascitaEstero( aModel.getDescComuneNascitaEstero() );
				 setNazionalita( aModel.getNazionalita() );
				 setPaternita( aModel.getPaternita() );
				 setCognomeMadre( aModel.getCognomeMadre() );
				 setNomeMadre( aModel.getNomeMadre() );
				 setSesso( aModel.getSesso() );
				 setAttoNascita( aModel.getAttoNascita() );
				 setNote( aModel.getNote() );
				 setCodComuneCasellario( aModel.getCodComuneCasellario() );
				 setFlagPresenzaFascicolo( aModel.getFlagPresenzaFascicolo() );
				 setMeseNascita( aModel.getMeseNascita() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
				 setIdSoggettoNuovo( aModel.getIdSoggettoNuovo() );
				 setFasSieIdFascicoloSius( aModel.getFasSieIdFascicoloSius() );
				 setEtaPresuntaAnni( aModel.getEtaPresuntaAnni() );
				 setEtaPresuntaMesi( aModel.getEtaPresuntaMesi() );

		}


	 public void setDAOFromModelForUpdate(StoricoSoggettoModel aModel) throws DAOException
  		{
				 setProgressivoStorico( aModel.getProgressivoStorico() );
				 setDataVariazione( aModel.getDataVariazione() );
				 setIdSoggettoVariato( aModel.getIdSoggettoVariato() );
				 setCodFiscale( aModel.getCodFiscale() );
				 setCodCs( aModel.getCodCs() );
				 setCodAfis( aModel.getCodAfis() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setAnnoNascita( aModel.getAnnoNascita() );
				 setDataNascita( aModel.getDataNascita() );
				 setDataNascitaPresunta( aModel.getDataNascitaPresunta() );
				 setCodComuneNascita( aModel.getCodComuneNascita() );
				 setCodProvinciaNascita( aModel.getCodProvinciaNascita() );
				 setCodStatoNascita( aModel.getCodStatoNascita() );
				 setDescComuneNascitaEstero( aModel.getDescComuneNascitaEstero() );
				 setNazionalita( aModel.getNazionalita() );
				 setPaternita( aModel.getPaternita() );
				 setCognomeMadre( aModel.getCognomeMadre() );
				 setNomeMadre( aModel.getNomeMadre() );
				 setSesso( aModel.getSesso() );
				 setAttoNascita( aModel.getAttoNascita() );
				 setNote( aModel.getNote() );
				 setCodComuneCasellario( aModel.getCodComuneCasellario() );
				 setFlagPresenzaFascicolo( aModel.getFlagPresenzaFascicolo() );
				 setMeseNascita( aModel.getMeseNascita() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
				 setIdSoggettoNuovo( aModel.getIdSoggettoNuovo() );
				 setFasSieIdFascicoloSius( aModel.getFasSieIdFascicoloSius() );
				 setEtaPresuntaAnni( aModel.getEtaPresuntaAnni() );
				 setEtaPresuntaMesi( aModel.getEtaPresuntaMesi() );

		}


	public void setCondizione(StoricoSoggettoModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
  {
	  setCondition(" ID_storico_soggetto = " + key );
  }

  public void setDAOFromModelSoggetto(SoggettoModel aSoggetto ) throws DAOException
  {
      setIdSoggettoVariato(aSoggetto.getIdSoggetto());
      if (aSoggetto.getDataAggiornamento()==null )
      {
        setDataVariazione(aSoggetto.getDataInserimento());
      }else{
        setDataVariazione(aSoggetto.getDataAggiornamento());
      }
      setCodFiscale(aSoggetto.getCodFiscale());
      setCodCs(aSoggetto.getCodCs());
      setCodAfis(aSoggetto.getCodAfis());
      setCognome(aSoggetto.getCognome());
      setNome(aSoggetto.getNome());
      setAnnoNascita(aSoggetto.getAnnoNascita());
      setDataNascita(aSoggetto.getDataNascita());
      setMeseNascita(aSoggetto.getMeseNascita());
      setDataNascitaPresunta(aSoggetto.getDataNascitaPresunta());
      setCodComuneNascita(aSoggetto.getCodComuneNascita());
      setCodProvinciaNascita(aSoggetto.getCodProvinciaNascita());
      setCodComuneCasellario(aSoggetto.getCodComuneCasellario());
      setCodStatoNascita(aSoggetto.getCodStatoNascita());
      setDescComuneNascitaEstero(aSoggetto.getDescComuneNascitaEstero());
      setNazionalita(aSoggetto.getNazionalita());
      setPaternita(aSoggetto.getPaternita());
      setCognomeMadre(aSoggetto.getCognomeMadre());
      setNomeMadre(aSoggetto.getNomeMadre());
      setSesso(aSoggetto.getSesso());
      setAttoNascita(aSoggetto.getAttoNascita());
      setNote(aSoggetto.getNote());
      setCodOperatoreInserimento(aSoggetto.getCodOperatoreAggiornamento());
      setDataInserimento(aSoggetto.getDataAggiornamento());
      setCodUfficioInserimento(aSoggetto.getCodUfficioAggiornamento());
      setMeseNascita(aSoggetto.getMeseNascita());
      setEtaPresuntaAnni(aSoggetto.getEtaPresuntaAnni());
      setEtaPresuntaMesi(aSoggetto.getEtaPresuntaMesi());
  }
}