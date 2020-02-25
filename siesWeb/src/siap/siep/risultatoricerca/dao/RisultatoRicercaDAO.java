package siap.siep.risultatoricerca.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.risultatoricerca.model.RisultatoRicercaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: RisultatoRicercaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RisultatoRicerca</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RisultatoRicercaDAO extends TableDAO
{
	public RisultatoRicercaDAO (Connection con)
	{
		 super(con);
		 setTable("RISULTATO_RICERCA");

		 //Settare la Sequence e i campi chiave
		 setSequenceField("ID_RICERCA", "RIS_RIC_SEQ");

		 setField("ID_RICERCA", BIG_DECIMAL);
		 setField("COD_UFFICIO", STRING);
		 setField("ID_FASCICOLO_SIEP", BIG_DECIMAL);
		 setField("CHIAVE_ANNO", BIG_DECIMAL);
		 setField("CHIAVE_PROGR", BIG_DECIMAL);
		 setField("COGNOME", STRING);
		 setField("NOME", STRING);
		 setField("LUOGO_NASCITA", STRING);
		 setField("DATA_NASCITA", DATE);
		 setField("DATA_REATO", DATE);
		 setField("DATA_FINE_PENA", DATE);
		 setField("NUM_ANNI_PENA_RES", BIG_DECIMAL);
		 setField("NUM_MESI_PENA_RES", BIG_DECIMAL);
		 setField("NUM_GIORNI_PENA_RES", BIG_DECIMAL);
		 setField("COD_POSIZIONE_GIURIDICA", STRING);
		 setField("DESCR_POSIZIONE_GIURIDICA", STRING);
		 setField("COD_UTENTE", STRING);
		 setField("NAZIONALITA", STRING);
	}


	    //
	    // METODI GET()
	    //
		public BigDecimal 		 	 getIdRicerca() 				throws DAOException	 { return getBigDecimal("ID_RICERCA"); }
		public String 				 getCodUfficio() 				throws DAOException	 { return getString("COD_UFFICIO"); }
		public BigDecimal 		 	 getIdFascicoloSiep() 			throws DAOException	 { return getBigDecimal("ID_FASCICOLO_SIEP"); }
		public BigDecimal 		 	 getChiaveAnno() 				throws DAOException	 { return getBigDecimal("CHIAVE_ANNO"); }
		public BigDecimal 		 	 getChiaveProgr() 				throws DAOException	 { return getBigDecimal("CHIAVE_PROGR"); }
		public String 				 getCognome() 					throws DAOException	 { return getString("COGNOME"); }
		public String 				 getNome() 						throws DAOException	 { return getString("NOME"); }
		public String 				 getLuogoNascita() 				throws DAOException	 { return getString("LUOGO_NASCITA"); }
		public Date 				 getDataNascita() 				throws DAOException	 { return getDate("DATA_NASCITA"); }
		public Date 				 getDataReato() 				throws DAOException	 { return getDate("DATA_REATO"); }
		public Date 				 getDataFinePena() 				throws DAOException	 { return getDate("DATA_FINE_PENA"); }
		public BigDecimal 		 	 getNumAnniPenaRes() 			throws DAOException	 { return getBigDecimal("NUM_ANNI_PENA_RES"); }
		public BigDecimal 		 	 getNumMesiPenaRes() 			throws DAOException	 { return getBigDecimal("NUM_MESI_PENA_RES"); }
		public BigDecimal 		 	 getNumGiorniPenaRes() 			throws DAOException	 { return getBigDecimal("NUM_GIORNI_PENA_RES"); }
		public String 				 getCodPosizioneGiuridica() 	throws DAOException	 { return getString("COD_POSIZIONE_GIURIDICA"); }
		public String 				 getDescrPosizioneGiuridica() 	throws DAOException	 { return getString("DESCR_POSIZIONE_GIURIDICA"); }
		public String 				 getCodUtente() 				throws DAOException	 { return getString("COD_UTENTE"); }
		public String 				 getNazionalita() 				throws DAOException	 { return getString("NAZIONALITA"); }

		//
		// METODI SET()
		//
		public void  	 setIdRicerca(BigDecimal aValore ) 					 { setBigDecimal("ID_RICERCA", aValore); }
		public void  	 setCodUfficio(String aValore ) 			 		 { setString("COD_UFFICIO", aValore); }
		public void  	 setIdFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("ID_FASCICOLO_SIEP", aValore); }
		public void  	 setChiaveAnno(BigDecimal aValore ) 			 	 { setBigDecimal("CHIAVE_ANNO", aValore); }
		public void  	 setChiaveProgr(BigDecimal aValore ) 			 	 { setBigDecimal("CHIAVE_PROGR", aValore); }
		public void  	 setCognome(String aValore ) 			 			 { setString("COGNOME", aValore); }
		public void  	 setNome(String aValore ) 			 				 { setString("NOME", aValore); }
		public void  	 setLuogoNascita(String aValore ) 			 		 { setString("LUOGO_NASCITA", aValore); }
		public void  	 setDataNascita(Date aValore ) 			 			 { setDate("DATA_NASCITA", aValore); }
		public void  	 setDataReato(Date aValore ) 			 			 { setDate("DATA_REATO", aValore); }
		public void  	 setDataFinePena(Date aValore ) 			 		 { setDate("DATA_FINE_PENA", aValore); }
		public void  	 setNumAnniPenaRes(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_PENA_RES", aValore); }
		public void  	 setNumMesiPenaRes(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_PENA_RES", aValore); }
		public void  	 setNumGiorniPenaRes(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_PENA_RES", aValore); }
		public void  	 setCodPosizioneGiuridica(String aValore ) 			 { setString("COD_POSIZIONE_GIURIDICA", aValore); }
		public void  	 setDescrPosizioneGiuridica(String aValore ) 		 { setString("DESCR_POSIZIONE_GIURIDICA", aValore); }
		public void 	 setCodUtente(String aValore) 	               	 	 { setString("COD_UTENTE", aValore); }
		public void 	 setNazionalita(String aValore) 	               	 { setString("COD_UTENTE", aValore); }

	public GenericModel getModel() throws DAOException
  	{
	 return new RisultatoRicercaModel(
					 getIdRicerca() ,
					 getCodUfficio() ,
					 "",
					 getIdFascicoloSiep() ,
					 getChiaveAnno() ,
					 getChiaveProgr() ,
					 getCognome() ,
					 getNome() ,
					 getLuogoNascita() ,
					 getDataNascita() ,
					 getDataReato() ,
					 getDataFinePena() ,
					 getNumAnniPenaRes() ,
					 getNumMesiPenaRes() ,
					 getNumGiorniPenaRes() ,
					 getCodPosizioneGiuridica() ,
					 getDescrPosizioneGiuridica(),
					 getCodUtente(),
					 getNazionalita()
					);
	}


	 public void setDAOFromModel(RisultatoRicercaModel aModel) throws DAOException
  	 {
		 setIdRicerca( aModel.getIdRicerca() );
		 setCodUfficio( aModel.getCodUfficio() );
		 setIdFascicoloSiep( aModel.getIdFascicoloSiep() );
		 setChiaveAnno( aModel.getChiaveAnno() );
		 setChiaveProgr( aModel.getChiaveProgr() );
		 setCognome( aModel.getCognome() );
		 setNome( aModel.getNome() );
		 setLuogoNascita( aModel.getLuogoNascita() );
		 setDataNascita( aModel.getDataNascita() );
		 setDataReato( aModel.getDataReato() );
		 setDataFinePena( aModel.getDataFinePena() );
		 setNumAnniPenaRes( aModel.getNumAnniPenaRes() );
		 setNumMesiPenaRes( aModel.getNumMesiPenaRes() );
		 setNumGiorniPenaRes( aModel.getNumGiorniPenaRes() );
		 setCodPosizioneGiuridica( aModel.getCodPosizioneGiuridica() );
		 setDescrPosizioneGiuridica( aModel.getDescrPosizioneGiuridica() );
		 setCodUtente(aModel.getCodUtente());
		 setNazionalita(aModel.getNazionalita());
	 }


	 public void setDAOFromModelForUpdate(RisultatoRicercaModel aModel) throws DAOException
  	 {
		 setIdRicerca( aModel.getIdRicerca() );
		 setCodUfficio( aModel.getCodUfficio() );
		 setIdFascicoloSiep( aModel.getIdFascicoloSiep() );
		 setChiaveAnno( aModel.getChiaveAnno() );
		 setChiaveProgr( aModel.getChiaveProgr() );
		 setCognome( aModel.getCognome() );
		 setNome( aModel.getNome() );
		 setLuogoNascita( aModel.getLuogoNascita() );
		 setDataNascita( aModel.getDataNascita() );
		 setDataReato( aModel.getDataReato() );
		 setDataFinePena( aModel.getDataFinePena() );
		 setNumAnniPenaRes( aModel.getNumAnniPenaRes() );
		 setNumMesiPenaRes( aModel.getNumMesiPenaRes() );
		 setNumGiorniPenaRes( aModel.getNumGiorniPenaRes() );
		 setCodPosizioneGiuridica( aModel.getCodPosizioneGiuridica() );
		 setDescrPosizioneGiuridica( aModel.getDescrPosizioneGiuridica() );
		 setCodUtente(aModel.getCodUtente());
		 setNazionalita(aModel.getNazionalita());

		 setCondizioneUpdate(aModel.getIdRicerca());
	 }


	public void setCondizione(RisultatoRicercaModel aModel)
	{
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
	}


	public void setCondizioneUpdate(BigDecimal key)
    {
		setCondition(" ID_RICERCA = " + key );
	}

}
