package siap.siep.penapresunta.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.penapresunta.model.PenaPresuntaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: PenaPresuntaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PenaPresunta</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class PenaPresuntaDAO extends TableDAO
{
	public PenaPresuntaDAO (Connection con)
	{
			 super(con);
			 setTable("PENA_PRESUNTA");

			 //Settare la Sequence e i campi chiave
                         setSequenceField("ID_PENA_PRESUNTA","PEN_PRE_SEQ");
			 setField("ID_PENA_PRESUNTA", BIG_DECIMAL);
			 setField("DATA_INIZIO", DATE);
			 setField("DATA_FINE", DATE);
			 setField("NUM_ANNI_RECLUSIONE", BIG_DECIMAL);
			 setField("NUM_MESI_RECLUSIONE", BIG_DECIMAL);
			 setField("NUM_GIORNI_RECLUSIONE", BIG_DECIMAL);
			 setField("IMPORTO_MULTA", BIG_DECIMAL);
			 setField("NUM_ANNI_ARRESTO", BIG_DECIMAL);
			 setField("NUM_MESI_ARRESTO", BIG_DECIMAL);
			 setField("NUM_GIORNI_ARRESTO", BIG_DECIMAL);
			 setField("IMPORTO_AMMENDA", BIG_DECIMAL);
			 setField("DIES_A_QUO", STRING);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
			 setField("DATA_FINE_RECLUSIONE", DATE);
			 setField("DATA_INIZIO_ARRESTO", DATE);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdPenaPresunta() 		throws DAOException	 { return getBigDecimal("ID_PENA_PRESUNTA"); }
			public Date 					 getDataInizio() 		throws DAOException	 { return getDate("DATA_INIZIO"); }
			public Date 					 getDataFine() 		throws DAOException	 { return getDate("DATA_FINE"); }
			public BigDecimal 		 getNumAnniReclusione() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_RECLUSIONE"); }
			public BigDecimal 		 getNumMesiReclusione() 		throws DAOException	 { return getBigDecimal("NUM_MESI_RECLUSIONE"); }
			public BigDecimal 		 getNumGiorniReclusione() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_RECLUSIONE"); }
			public BigDecimal 		 getImportoMulta() 		throws DAOException	 { return getBigDecimal("IMPORTO_MULTA"); }
			public BigDecimal 		 getNumAnniArresto() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_ARRESTO"); }
			public BigDecimal 		 getNumMesiArresto() 		throws DAOException	 { return getBigDecimal("NUM_MESI_ARRESTO"); }
			public BigDecimal 		 getNumGiorniArresto() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_ARRESTO"); }
			public BigDecimal 		 getImportoAmmenda() 		throws DAOException	 { return getBigDecimal("IMPORTO_AMMENDA"); }
			public String 				 getDiesAQuo() 		throws DAOException	 { return getString("DIES_A_QUO"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
			public BigDecimal 		 getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
			public Date 					 getDataFineReclusione() 		throws DAOException	 { return getDate("DATA_FINE_RECLUSIONE"); }
			public Date 					 getDataInizioArresto() 		throws DAOException	 { return getDate("DATA_INIZIO_ARRESTO"); }


  //
  // METODI SET()
  //

			public void  	 setIdPenaPresunta(BigDecimal aValore ) 			 { setBigDecimal("ID_PENA_PRESUNTA", aValore); }
			public void  	 setDataInizio(Date aValore ) 			 { setDate("DATA_INIZIO", aValore); }
			public void  	 setDataFine(Date aValore ) 			 { setDate("DATA_FINE", aValore); }
			public void  	 setNumAnniReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_RECLUSIONE", aValore); }
			public void  	 setNumMesiReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_RECLUSIONE", aValore); }
			public void  	 setNumGiorniReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_RECLUSIONE", aValore); }
			public void  	 setImportoMulta(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_MULTA", aValore); }
			public void  	 setNumAnniArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_ARRESTO", aValore); }
			public void  	 setNumMesiArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_ARRESTO", aValore); }
			public void  	 setNumGiorniArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_ARRESTO", aValore); }
			public void  	 setImportoAmmenda(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_AMMENDA", aValore); }
			public void  	 setDiesAQuo(String aValore ) 			 { setString("DIES_A_QUO", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
			public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
			public void  	 setDataFineReclusione(Date aValore ) 			 { setDate("DATA_FINE_RECLUSIONE", aValore); }
			public void  	 setDataInizioArresto(Date aValore ) 			 { setDate("DATA_INIZIO_ARRESTO", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new PenaPresuntaModel(
								 getIdPenaPresunta() ,
								 getDataInizio() ,
								 getDataFine() ,
								 getNumAnniReclusione() ,
								 getNumMesiReclusione() ,
								 getNumGiorniReclusione() ,
								 getImportoMulta() ,
								 getNumAnniArresto() ,
								 getNumMesiArresto() ,
								 getNumGiorniArresto() ,
								 getImportoAmmenda() ,
								 getDiesAQuo() ,
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getFasSieIdFascicoloSiep() ,
								 getDataFineReclusione() ,
								 getDataInizioArresto()
								);
		}


	 public void 	 setDAOFromModel(PenaPresuntaModel aModel) throws DAOException
  		{
				 setIdPenaPresunta( aModel.getIdPenaPresunta() );
				 setDataInizio( aModel.getDataInizio() );
				 setDataFine( aModel.getDataFine() );
				 setNumAnniReclusione( aModel.getNumAnniReclusione() );
				 setNumMesiReclusione( aModel.getNumMesiReclusione() );
				 setNumGiorniReclusione( aModel.getNumGiorniReclusione() );
				 setImportoMulta( aModel.getImportoMulta() );
				 setNumAnniArresto( aModel.getNumAnniArresto() );
				 setNumMesiArresto( aModel.getNumMesiArresto() );
				 setNumGiorniArresto( aModel.getNumGiorniArresto() );
				 setImportoAmmenda( aModel.getImportoAmmenda() );
				 setDiesAQuo( aModel.getDiesAQuo() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
				 setDataFineReclusione( aModel.getDataFineReclusione() );
				 setDataInizioArresto( aModel.getDataInizioArresto() );
		}


	 public void 	 setDAOFromModelForUpdate(PenaPresuntaModel aModel) throws DAOException
  		{
				 setIdPenaPresunta( aModel.getIdPenaPresunta() );
				 setDataInizio( aModel.getDataInizio() );
				 setDataFine( aModel.getDataFine() );
				 setNumAnniReclusione( aModel.getNumAnniReclusione() );
				 setNumMesiReclusione( aModel.getNumMesiReclusione() );
				 setNumGiorniReclusione( aModel.getNumGiorniReclusione() );
				 setImportoMulta( aModel.getImportoMulta() );
				 setNumAnniArresto( aModel.getNumAnniArresto() );
				 setNumMesiArresto( aModel.getNumMesiArresto() );
				 setNumGiorniArresto( aModel.getNumGiorniArresto() );
				 setImportoAmmenda( aModel.getImportoAmmenda() );
				 setDiesAQuo( aModel.getDiesAQuo() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
				 setDataFineReclusione( aModel.getDataFineReclusione() );
				 setDataInizioArresto( aModel.getDataInizioArresto() );
		 setCondizioneUpdate(aModel.getIdPenaPresunta());
		}


	public void setCondizione(PenaPresuntaModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_PENA_PRESUNTA = " + key );
		 }
       public void setCondizioneUpdatebyIdFascicolo(BigDecimal key)
 			 {
	 setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + key );
		 }
}
