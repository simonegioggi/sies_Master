package siap.regesies.regesoggetto.dao;

import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.regesies.regesoggetto.model.RegeSoggettoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: RegeSoggettoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RegeSoggetto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RegeSoggettoDAO extends SIAPTableDAO
{
	public RegeSoggettoDAO (Connection con)
	{
			 super(con);
			 setTable("rege_soggetto");

			 //Settare la Sequence e i campi chiave

			 setField("ID_FILE", STRING);
			 setField("FLAG_TIPO_SOGG", STRING);
			 setField("COD_FISCALE", STRING);
			 setField("COD_CS", STRING);
			 setField("COD_AFIS", STRING);
			 setField("COGNOME", STRING);
			 setField("NOME", STRING);
			 setField("ANNO_NASCITA", INT);
			 setField("DATA_NASCITA", DATE);
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
			 setField("DENO_SOGG", STRING);
			 setField("RAGI_SOGG", STRING);
			 setField("NOME_RAPP_LEGA", STRING);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
	}


  //
  // METODI GET()
  //

			public String 				 getIdFile() 		throws DAOException	 { return getString("ID_FILE"); }
			public String 				 getFlagTipoSogg() 		throws DAOException	 { return getString("FLAG_TIPO_SOGG"); }
			public String 				 getCodFiscale() 		throws DAOException	 { return getString("COD_FISCALE"); }
			public String 				 getCodCs() 		throws DAOException	 { return getString("COD_CS"); }
			public String 				 getCodAfis() 		throws DAOException	 { return getString("COD_AFIS"); }
			public String 				 getCognome() 		throws DAOException	 { return getString("COGNOME"); }
			public String 				 getNome() 		throws DAOException	 { return getString("NOME"); }
			public int 		 getAnnoNascita() 		throws DAOException	 { return getInt("ANNO_NASCITA"); }
			public Date 					 getDataNascita() 		throws DAOException	 { return getDate("DATA_NASCITA"); }
			public String 				 getCodComuneNascita() 		throws DAOException	 { return getString("COD_COMUNE_NASCITA"); }
			public String 				 getCodProvinciaNascita() 		throws DAOException	 { return getString("COD_PROVINCIA_NASCITA"); }
			public String 				 getCodStatoNascita() 		throws DAOException	 { return getString("COD_STATO_NASCITA"); }
			public String 				 getDescComuneNascitaEstero() 		throws DAOException	 { return getString("DESC_COMUNE_NASCITA_ESTERO"); }
			public String 				 getNazionalita() 		throws DAOException	 { return getString("NAZIONALITA"); }
			public String 				 getPaternita() 		throws DAOException	 { return getString("PATERNITA"); }
			public String 				 getCognomeMadre() 		throws DAOException	 { return getString("COGNOME_MADRE"); }
			public String 				 getNomeMadre() 		throws DAOException	 { return getString("NOME_MADRE"); }
			public String 				 getSesso() 		throws DAOException	 { return getString("SESSO"); }
			public String 				 getAttoNascita() 		throws DAOException	 { return getString("ATTO_NASCITA"); }
			public String 				 getNote() 		throws DAOException	 { return getString("NOTE"); }
			public String 				 getDenoSogg() 		throws DAOException	 { return getString("DENO_SOGG"); }
			public String 				 getRagiSogg() 		throws DAOException	 { return getString("RAGI_SOGG"); }
			public String 				 getNomeRappLega() 		throws DAOException	 { return getString("NOME_RAPP_LEGA"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }


  //
  // METODI SET()
  //

			public void  	 setIdFile(String aValore ) 			 { setString("ID_FILE", aValore); }
			public void  	 setFlagTipoSogg(String aValore ) 			 { setString("FLAG_TIPO_SOGG", aValore); }
			public void  	 setCodFiscale(String aValore ) 			 { setString("COD_FISCALE", aValore); }
			public void  	 setCodCs(String aValore ) 			 { setString("COD_CS", aValore); }
			public void  	 setCodAfis(String aValore ) 			 { setString("COD_AFIS", aValore); }
			public void  	 setCognome(String aValore ) 			 { setString("COGNOME", aValore); }
			public void  	 setNome(String aValore ) 			 { setString("NOME", aValore); }
			public void  	 setAnnoNascita(int aValore ) 			 { setInt("ANNO_NASCITA", aValore); }
			public void  	 setDataNascita(Date aValore ) 			 { setDate("DATA_NASCITA", aValore); }
			public void  	 setCodComuneNascita(String aValore ) 			 { setString("COD_COMUNE_NASCITA", aValore); }
			public void  	 setCodProvinciaNascita(String aValore ) 			 { setString("COD_PROVINCIA_NASCITA", aValore); }
			public void  	 setCodStatoNascita(String aValore ) 			 { setString("COD_STATO_NASCITA", aValore); }
			public void  	 setDescComuneNascitaEstero(String aValore ) 			 { setString("DESC_COMUNE_NASCITA_ESTERO", aValore); }
			public void  	 setNazionalita(String aValore ) 			 { setString("NAZIONALITA", aValore); }
			public void  	 setPaternita(String aValore ) 			 { setString("PATERNITA", aValore); }
			public void  	 setCognomeMadre(String aValore ) 			 { setString("COGNOME_MADRE", aValore); }
			public void  	 setNomeMadre(String aValore ) 			 { setString("NOME_MADRE", aValore); }
			public void  	 setSesso(String aValore ) 			 { setString("SESSO", aValore); }
			public void  	 setAttoNascita(String aValore ) 			 { setString("ATTO_NASCITA", aValore); }
			public void  	 setNote(String aValore ) 			 { setString("NOTE", aValore); }
			public void  	 setDenoSogg(String aValore ) 			 { setString("DENO_SOGG", aValore); }
			public void  	 setRagiSogg(String aValore ) 			 { setString("RAGI_SOGG", aValore); }
			public void  	 setNomeRappLega(String aValore ) 			 { setString("NOME_RAPP_LEGA", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new RegeSoggettoModel(
								 getIdFile() ,
								 getFlagTipoSogg() ,
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
								 getDenoSogg() ,
								 getRagiSogg() ,
								 getNomeRappLega() ,
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento()
								);
		}


	 public void 	 setDAOFromModel(RegeSoggettoModel aModel) throws DAOException
  		{
				 setIdFile( aModel.getIdFile() );
				 setFlagTipoSogg( aModel.getFlagTipoSogg() );
				 setCodFiscale( aModel.getCodFiscale() );
				 setCodCs( aModel.getCodCs() );
				 setCodAfis( aModel.getCodAfis() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setAnnoNascita( aModel.getAnnoNascita() );
				 setDataNascita( aModel.getDataNascita() );
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
				 setDenoSogg( aModel.getDenoSogg() );
				 setRagiSogg( aModel.getRagiSogg() );
				 setNomeRappLega( aModel.getNomeRappLega() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
		}


	 public void 	 setDAOFromModelForUpdate(RegeSoggettoModel aModel) throws DAOException
  		{
				 setIdFile( aModel.getIdFile() );
				 setFlagTipoSogg( aModel.getFlagTipoSogg() );
				 setCodFiscale( aModel.getCodFiscale() );
				 setCodCs( aModel.getCodCs() );
				 setCodAfis( aModel.getCodAfis() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setAnnoNascita( aModel.getAnnoNascita() );
				 setDataNascita( aModel.getDataNascita() );
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
				 setDenoSogg( aModel.getDenoSogg() );
				 setRagiSogg( aModel.getRagiSogg() );
				 setNomeRappLega( aModel.getNomeRappLega() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
		     setCondizioneUpdate(aModel.getIdFile());
		}


    public void setCondizione(RegeSoggettoModel aModel)
    {
      String lCondizioni = new String();

      boolean lInserito = false;
      if (lInserito)
        setCondition(lCondizioni);
    }

    public void setCondizioneUpdate(String key)
    {
      setCondition(" ID_FILE = '" + key + "'");
    }

    /* Setta la condizione per la cancellazione di tutti i reati
     * @param aModel
     */
    public void setCondizioneDelete(String aKey)
    {
      String lCondizioni = new String();
      lCondizioni = " ID_FILE = '" + aKey + "'";

      setCondition(lCondizioni);
    }

}
