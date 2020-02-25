package siap.sius.cancassfascsius.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: CancAssFascSiusDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella CancAssFascSius</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class CancAssFascSiusDAO extends TableDAO
{
	public CancAssFascSiusDAO (Connection con)
	{
         super(con);
         setTable("CANC_ASS_FASC_SIUS");

         //Settare la Sequence e i campi chiave

         setField("COD_CANCELLERIA_ASSEGNATARIA", STRING);
         setField("COD_UFFICIO", STRING);
         setField("FAS_SIUS_ID_FASCICOLO_SIUS", BIG_DECIMAL);
         setField("COD_STATO_PROCEDIMENTO", STRING);
         setField("DESCR_STATO_PROCEDIMENTO", STRING);
         setField("DATA_INIZIO", DATE);
         setField("DATA_FINE", DATE);
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

      public String 	getCodCancelleriaAssegnataria() 	throws DAOException	 { return getString("COD_CANCELLERIA_ASSEGNATARIA"); }
      public String 	getCodUfficio() 		            throws DAOException	 { return getString("COD_UFFICIO"); }
      public BigDecimal getFasSiusIdFascicoloSius() 		throws DAOException	 { return getBigDecimal("FAS_SIUS_ID_FASCICOLO_SIUS"); }
      public String 	getCodStatoProcedimento() 		throws DAOException	 { return getString("COD_STATO_PROCEDIMENTO"); }
      public String 	getDescrStatoProcedimento() 		throws DAOException	 { return getString("DESCR_STATO_PROCEDIMENTO"); }
      public Date 	getDataInizio() 		            throws DAOException	 { return getDate("DATA_INIZIO"); }
      public Date 	getDataFine() 		            throws DAOException	 { return getDate("DATA_FINE"); }
      public String 	getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
      public Date 	getDataInserimento() 		      throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
      public String 	getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
      public String 	getCodOperatoreAggiornamento()      throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
      public Date 	getDataAggiornamento() 		      throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
      public String 	getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }

      //
      // METODI SET()
      //

      public void  	setCodCancelleriaAssegnataria(String aValore ) 	{ setString("COD_CANCELLERIA_ASSEGNATARIA", aValore); }
      public void  	setCodUfficio(String aValore ) 			{ setString("COD_UFFICIO", aValore); }
      public void  	setFasSiusIdFascicoloSius(BigDecimal aValore ) 	{ setBigDecimal("FAS_SIUS_ID_FASCICOLO_SIUS", aValore); }
      public void  	setCodStatoProcedimento(String aValore ) 		{ setString("COD_STATO_PROCEDIMENTO", aValore); }
      public void  	setDescrStatoProcedimento(String aValore ) 	{ setString("DESCR_STATO_PROCEDIMENTO", aValore); }
      public void  	setDataInizio(Date aValore ) 			      { setDate("DATA_INIZIO", aValore); }
      public void  	setDataFine(Date aValore ) 			      { setDate("DATA_FINE", aValore); }
      public void  	setCodOperatoreInserimento(String aValore )     { setString("COD_OPERATORE_INSERIMENTO", aValore); }
      public void  	setDataInserimento(Date aValore ) 			{ setDate("DATA_INSERIMENTO", aValore); }
      public void  	setCodUfficioInserimento(String aValore ) 	{ setString("COD_UFFICIO_INSERIMENTO", aValore); }
      public void  	setCodOperatoreAggiornamento(String aValore ) 	{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
      public void  	setDataAggiornamento(Date aValore ) 		{ setDate("DATA_AGGIORNAMENTO", aValore); }
      public void  	setCodUfficioAggiornamento(String aValore ) 	{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }


      /**
       * La funzione costruisce il model della CancAssFascSius a partire dal record corrente in tabella.
       * @return CancelleriaAssegnatariaModel
       * @throws DAOException
       */

	public GenericModel getModel() throws DAOException
      {
         return new CancAssFascSiusModel(
                getCodCancelleriaAssegnataria() ,
                "",
                getCodUfficio() ,
                "",
                getFasSiusIdFascicoloSius() ,
                getCodStatoProcedimento() ,
                getDescrStatoProcedimento(),
                getDataInizio() ,
                getDataFine() ,
                getCodOperatoreInserimento() ,
                getDataInserimento() ,
                getCodUfficioInserimento() ,
                "",
                getCodOperatoreAggiornamento() ,
                getDataAggiornamento() ,
                getCodUfficioAggiornamento() ,
                ""
                );
      }

      /**
       * Funzione di inserimento: inserisce in tabella i dati ricevuti dal model passato come argomento.
       * @param aModel: CancAssFascSiusModel
       * @throws DAOException
       */
      public void 	 setDAOFromModel(CancAssFascSiusModel aModel) throws DAOException
      {
         setCodCancelleriaAssegnataria( aModel.getCodCancelleriaAssegnataria() );
         setCodUfficio( aModel.getCodUfficio() );
         setFasSiusIdFascicoloSius( aModel.getFasSiusIdFascicoloSius() );
         setCodStatoProcedimento( aModel.getCodStatoProcedimento() );
         setDescrStatoProcedimento( aModel.getDescrStatoProcedimento() );
         setDataInizio( aModel.getDataInizio() );
         setDataFine( aModel.getDataFine() );
         setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
         setDataInserimento( aModel.getDataInserimento() );
         setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
         setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
         setDataAggiornamento( aModel.getDataAggiornamento() );
         setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      }




      /**
       * Funzione di aggiornamento: aggiorna alcuni dei campi in tabella con
       * i dati ricevuti dal model passato come argomento.
       * @param aModel: CancAssFascSiusModel
       * @throws DAOException
       */
	 public void 	 setDAOFromModelForUpdate(CancAssFascSiusModel aModel) throws DAOException
       {
          setCodCancelleriaAssegnataria( aModel.getCodCancelleriaAssegnataria() );
          setCodUfficio( aModel.getCodUfficio() );
          setFasSiusIdFascicoloSius( aModel.getFasSiusIdFascicoloSius() );
          setCodStatoProcedimento( aModel.getCodStatoProcedimento() );
          setDescrStatoProcedimento( aModel.getDescrStatoProcedimento() );
          setDataInizio( aModel.getDataInizio() );
          setDataFine( aModel.getDataFine() );
          setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
          setDataAggiornamento( aModel.getDataAggiornamento() );
          setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
         // setCondizioneUpdate(aModel.getIdCancAssFascSius());
      }


	public void setCondizione(CancAssFascSiusModel aModel)
      {
         String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
      }

      /**
       * La funzione prepara la condizione per individuare univocamente l'occorenza attiva per uno specifico Fascicolo SIUS.
       * @param key: ID Fascicolo SIUS.
       */
      public void setCondizioneUpdate(BigDecimal key)
      {
          setCondition(" DATA_FINE IS NULL AND FAS_SIUS_ID_FASCICOLO_SIUS = " + key );
      }


      /**
       * La funzione prepara la condizione per individuare univocamente l'ultima occorrenza chiusa nella tabella FAS_SIUS_ID_FASCICOLO_SIUS da riattivare.
       * La riattivazione consiste nel valorizzare a null la DATA DI CHIUSURA e l'ultima occorrenza per uno specifico Fascicolo viene individuato dalla DATA_AGGIORNAMENTO più recente.
       * @param key: ID Fascicolo SIUS.
       */
      public void setCondizioneRiattivazione(BigDecimal key)
      {
          setCondition(" FAS_SIUS_ID_FASCICOLO_SIUS = " + key + " AND DATA_AGGIORNAMENTO = (SELECT MAX(DATA_AGGIORNAMENTO) FROM CANC_ASS_FASC_SIUS WHERE FAS_SIUS_ID_FASCICOLO_SIUS = " + key + ")");
      }


}
