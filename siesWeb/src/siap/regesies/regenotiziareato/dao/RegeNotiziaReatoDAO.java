package siap.regesies.regenotiziareato.dao;

import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: RegeNotiziaReatoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RegeNotiziaReato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RegeNotiziaReatoDAO extends SIAPTableDAO
{
	public RegeNotiziaReatoDAO (Connection con)
	{
			 super(con);
			 setTable("rege_notizia_reato");

			 //Settare la Sequence e i campi chiave

			 setField("ID_FILE", STRING);
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
			public String 		 getProgrNotizia() 		throws DAOException	 { return getString("PROGR_NOTIZIA"); }
			public Date 					 getDataPervenimento() 		throws DAOException	 { return getDate("DATA_PERVENIMENTO"); }
			public String 				 getAcquisizioneDiretta() 		throws DAOException	 { return getString("ACQUISIZIONE_DIRETTA"); }
			public Date 					 getDataFatto() 		throws DAOException	 { return getDate("DATA_FATTO"); }
			public String 				 getCodFonte() 		throws DAOException	 { return getString("COD_FONTE"); }
			public String 				 getTipoFonte() 		throws DAOException	 { return getString("TIPO_FONTE"); }
			public String 				 getCodComuneFonte() 		throws DAOException	 { return getString("COD_COMUNE_FONTE"); }
			public String 		 getNumRegAutorita() 		throws DAOException	 { return getString("NUM_REG_AUTORITA"); }
			public String 				 getLuogoProvenienza() 		throws DAOException	 { return getString("LUOGO_PROVENIENZA"); }
			public Date 					 getDataAcquisizione() 		throws DAOException	 { return getDate("DATA_ACQUISIZIONE"); }
			public String  		 getNumeroRicevuta() 		throws DAOException	 { return getString("NUMERO_RICEVUTA"); }
			public String 				 getDescrizioneFonte() 		throws DAOException	 { return getString("DESCRIZIONE_FONTE"); }
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
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new RegeNotiziaReatoModel(
								 getIdFile() ,
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
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento()
								);
		}


	 public void 	 setDAOFromModel(RegeNotiziaReatoModel aModel) throws DAOException
  		{
				 setIdFile( aModel.getIdFile() );
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
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
		}


	 public void 	 setDAOFromModelForUpdate(RegeNotiziaReatoModel aModel) throws DAOException
  		{
				 setIdFile( aModel.getIdFile() );
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
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );

		     setCondizioneUpdate(aModel.getIdFile(),aModel.getProgrNotizia() );
		}


    public void setCondizione(RegeNotiziaReatoModel aModel)
    {
      String lCondizioni = new String();

      boolean lInserito = false;
      if (lInserito)
        setCondition(lCondizioni);
    }

    public void setCondizioneUpdate(String key, String aProgr)
    {
      setCondition(" ID_FILE = '" + key + "' AND PROGR_NOTIZIA = '" + aProgr+"'");
    }

    /* Setta la condizione per la cancellazione
     * @param aModel
     */
    public void setCondizioneDelete(String aKey)
    {
      String lCondizioni = new String();
      lCondizioni = " ID_FILE = '" + aKey + "'";

      setCondition(lCondizioni);
    }


}
