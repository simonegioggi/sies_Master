package siap.regesies.regeavvocato.dao;

import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.regesies.regeavvocato.model.RegeAvvocatoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: RegeAvvocatoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RegeAvvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RegeAvvocatoDAO extends SIAPTableDAO
{
	public RegeAvvocatoDAO (Connection con)
	{
			 super(con);
			 setTable("rege_avvocato");

			 //Settare la Sequence e i campi chiave

			 setField("ID_FILE", STRING);
			 setField("PROGR_AVVOCATO", BIG_DECIMAL);
			 setField("COGNOME", STRING);
			 setField("NOME", STRING);
			 setField("FORO", STRING);
			 setField("COD_TIPO_AVVOCATO", STRING);
			 setField("DATA_INIZIO_VALIDITA", DATE);
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
			public int        		 getProgrAvvocato() 		throws DAOException	 { return getInt("PROGR_AVVOCATO"); }
			public String 				 getCognome() 		throws DAOException	 { return getString("COGNOME"); }
			public String 				 getNome() 		throws DAOException	 { return getString("NOME"); }
			public String 				 getForo() 		throws DAOException	 { return getString("FORO"); }
			public String 				 getCodTipoAvvocato() 		throws DAOException	 { return getString("COD_TIPO_AVVOCATO"); }
			public Date 					 getDataInizioValidita() 		throws DAOException	 { return getDate("DATA_INIZIO_VALIDITA"); }
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
			public void  	 setProgrAvvocato(int aValore ) 			 { setInt("PROGR_AVVOCATO", aValore); }
			public void  	 setCognome(String aValore ) 			 { setString("COGNOME", aValore); }
			public void  	 setNome(String aValore ) 			 { setString("NOME", aValore); }
			public void  	 setForo(String aValore ) 			 { setString("FORO", aValore); }
			public void  	 setCodTipoAvvocato(String aValore ) 			 { setString("COD_TIPO_AVVOCATO", aValore); }
			public void  	 setDataInizioValidita(Date aValore ) 			 { setDate("DATA_INIZIO_VALIDITA", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new RegeAvvocatoModel(
								 getIdFile() ,
								 getProgrAvvocato() ,
								 getCognome() ,
								 getNome() ,
								 getForo() ,
								 getCodTipoAvvocato() ,
								 "",
								 getDataInizioValidita() ,
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento()
								);
		}


	 public void 	 setDAOFromModel(RegeAvvocatoModel aModel) throws DAOException
  		{
				 setIdFile( aModel.getIdFile() );
				 setProgrAvvocato( aModel.getProgrAvvocato() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setForo( aModel.getForo() );
				 setCodTipoAvvocato( aModel.getCodTipoAvvocato() );
				 setDataInizioValidita( aModel.getDataInizioValidita() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
		}


	 public void 	 setDAOFromModelForUpdate(RegeAvvocatoModel aModel) throws DAOException
  		{
				 setIdFile( aModel.getIdFile() );
				 setProgrAvvocato( aModel.getProgrAvvocato() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setForo( aModel.getForo() );
				 setCodTipoAvvocato( aModel.getCodTipoAvvocato() );
				 setDataInizioValidita( aModel.getDataInizioValidita() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
		     setCondizioneUpdate(aModel.getIdFile(), aModel.getProgrAvvocato());
		}


	public void setCondizione(RegeAvvocatoModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(String key, int aProgr)
 			 {
          setCondition(" ID_FILE = '" + key +"' AND PROGR_AVVOCATO = " + aProgr);
		 }
/* Setta la condizione per la cancellazione
   * @param aModel
   */
  public void setCondizioneDelete(String aKey)
  {
    String lCondizioni = new String();
    lCondizioni = " ID_FILE = '" +aKey + "'";

    setCondition(lCondizioni);
  }


}
