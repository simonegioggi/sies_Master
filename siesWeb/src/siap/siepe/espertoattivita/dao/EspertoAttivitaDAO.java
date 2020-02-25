package siap.siepe.espertoattivita.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siepe.espertoattivita.model.EspertoAttivitaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: EspertoAttivitaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella EspertoAttivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class EspertoAttivitaDAO extends TableDAO
{
	public EspertoAttivitaDAO (Connection con)
	{
			 super(con);
			 setTable("ESPERTO_ATTIVITA");

			 //I campi chiave sono più di uno perchè la chiave
                   // della tabella è composta
                   setFieldKey("ESP_ID_ESPERTO",BIG_DECIMAL);
                   setFieldKey("ATT_ID_ATTIVITA", BIG_DECIMAL);
                   setFieldKey("DATA_INIZIO", DATE);

			 setField("DATA_INIZIO", DATE);
			 setField("DATA_FINE", DATE);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 setField("ESP_ID_ESPERTO", BIG_DECIMAL);
			 setField("ATT_ID_ATTIVITA", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public Date 					 getDataInizio() 		throws DAOException	 { return getDate("DATA_INIZIO"); }
			public Date 					 getDataFine() 		throws DAOException	 { return getDate("DATA_FINE"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
			public BigDecimal 		 getEspIdEsperto() 		throws DAOException	 { return getBigDecimal("ESP_ID_ESPERTO"); }
			public BigDecimal 		 getAttIdAttivita() 		throws DAOException	 { return getBigDecimal("ATT_ID_ATTIVITA"); }


  //
  // METODI SET()
  //

			public void  	 setDataInizio(Date aValore ) 			 { setDate("DATA_INIZIO", aValore); }
			public void  	 setDataFine(Date aValore ) 			 { setDate("DATA_FINE", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
			public void  	 setEspIdEsperto(BigDecimal aValore ) 			 { setBigDecimal("ESP_ID_ESPERTO", aValore); }
			public void  	 setAttIdAttivita(BigDecimal aValore ) 			 { setBigDecimal("ATT_ID_ATTIVITA", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new EspertoAttivitaModel(
								 getDataInizio() ,
								 getDataFine() ,
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getEspIdEsperto() ,
								 getAttIdAttivita()
								);
		}


	 public void 	 setDAOFromModel(EspertoAttivitaModel aModel) throws DAOException
  		{
				 setDataInizio( aModel.getDataInizio() );
				 setDataFine( aModel.getDataFine() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setEspIdEsperto( aModel.getEspIdEsperto() );
				 setAttIdAttivita( aModel.getAttIdAttivita() );
		}


	 public void 	 setDAOFromModelForUpdate(EspertoAttivitaModel aModel) throws DAOException
  		{
				 setDataInizio( aModel.getDataInizio() );
				 setDataFine( aModel.getDataFine() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setEspIdEsperto( aModel.getEspIdEsperto() );
				 setAttIdAttivita( aModel.getAttIdAttivita() );
	// Da definire la condizione di update
      //setCondizioneUpdate(aModel.getIdEspertoAttivita());
		}


	public void setCondizione(EspertoAttivitaModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }

      public void setCondizioneByIdAttivita(BigDecimal key)
      {
         setCondition(" ATT_ID_ATTIVITA = " + key );
      }


	public void setCondizioneUpdate(BigDecimal key)
      {
	 setCondition(" ID_ESPERTO_ATTIVITA = " + key );
      }


      public void setCondizioneByKey(EspertoAttivitaModel aModel)
      {
         String lCondizioni = new String();

         lCondizioni = " ESP_ID_ESPERTO = " + aModel.getEspIdEsperto();
         lCondizioni = " ATT_ID_ATTIVITA = " + aModel.getAttIdAttivita();
         lCondizioni = " DATA_INIZIO = " + aModel.getDataInizio();


         setCondition(lCondizioni);
      }

      public void setCondizioneChiusura(EspertoAttivitaModel aModel)
      {
         String lCondizioni = new String();
         lCondizioni = " ESP_ID_ESPERTO = " + aModel.getEspIdEsperto();
         lCondizioni += " AND ATT_ID_ATTIVITA = "+ aModel.getAttIdAttivita();
         lCondizioni += " AND DATA_FINE IS NULL" ;

         setCondition(lCondizioni);

      }

      public void setDAOFromModelXChiusura(EspertoAttivitaModel aModel) throws DAOException
      {
         setDataFine( aModel.getDataFine() );
         setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
         setDataAggiornamento( aModel.getDataAggiornamento() );
         setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
         setCondizioneChiusura(aModel);
      }

}
