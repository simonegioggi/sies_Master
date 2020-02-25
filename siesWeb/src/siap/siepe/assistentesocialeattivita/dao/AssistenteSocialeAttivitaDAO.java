package siap.siepe.assistentesocialeattivita.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siepe.assistentesocialeattivita.model.AssistenteSocialeAttivitaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: AssistenteSocialeAttivitaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella AssistenteSocialeAttivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class AssistenteSocialeAttivitaDAO extends TableDAO
{
	public AssistenteSocialeAttivitaDAO (Connection con)
	{
         super(con);
         setTable("ASSISTENTE_SOCIALE_ATTIVITA");

         // La tabella è una tabella di relazioni e non ha indici
         // nè sequence associate.

         setField("DATA_INIZIO", DATE);
         setField("DATA_FINE", DATE);
         setField("COD_OPERATORE_INSERIMENTO", STRING);
         setField("DATA_INSERIMENTO", DATE);
         setField("COD_UFFICIO_INSERIMENTO", STRING);
         setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
         setField("DATA_AGGIORNAMENTO", DATE);
         setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
         setField("ASS_SOC_ID_ASS_SOCIALE", BIG_DECIMAL);
         setField("ATT_ID_ATTIVITA", BIG_DECIMAL);
	}


      //
      // METODI GET()
      //

      public Date 	getDataInizio() 		       throws DAOException	 { return getDate("DATA_INIZIO"); }
      public Date 	getDataFine() 		       throws DAOException	 { return getDate("DATA_FINE"); }
      public String 	getCodOperatoreInserimento() 	 throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
      public Date 	getDataInserimento() 		 throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
      public String 	getCodUfficioInserimento() 	 throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
      public String 	getCodOperatoreAggiornamento() throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
      public Date 	getDataAggiornamento() 		 throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
      public String 	getCodUfficioAggiornamento() 	 throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
      public BigDecimal  getAssSocIdAssSociale() 	 throws DAOException	 { return getBigDecimal("ASS_SOC_ID_ASS_SOCIALE"); }
      public BigDecimal  getAttIdAttivita() 		 throws DAOException	 { return getBigDecimal("ATT_ID_ATTIVITA"); }


      //
      // METODI SET()
      //

      public void  	 setDataInizio(Date aValore ) 			 { setDate("DATA_INIZIO", aValore); }
      public void  	 setDataFine(Date aValore ) 			       { setDate("DATA_FINE", aValore); }
      public void  	 setCodOperatoreInserimento(String aValore ) 	 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
      public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
      public void  	 setCodUfficioInserimento(String aValore ) 	 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
      public void  	 setCodOperatoreAggiornamento(String aValore ) 	 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
      public void  	 setDataAggiornamento(Date aValore ) 		 { setDate("DATA_AGGIORNAMENTO", aValore); }
      public void  	 setCodUfficioAggiornamento(String aValore ) 	 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
      public void  	 setAssSocIdAssSociale(BigDecimal aValore ) 	 { setBigDecimal("ASS_SOC_ID_ASS_SOCIALE", aValore); }
      public void  	 setAttIdAttivita(BigDecimal aValore ) 		 { setBigDecimal("ATT_ID_ATTIVITA", aValore); }


	public GenericModel getModel() throws DAOException
      {
         return new AssistenteSocialeAttivitaModel(
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
          getAssSocIdAssSociale() ,
          getAttIdAttivita()
         );
      }


	 public void setDAOFromModel(AssistenteSocialeAttivitaModel aModel) throws DAOException
       {
          setDataInizio( aModel.getDataInizio() );
          setDataFine( aModel.getDataFine() );
          setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
          setDataInserimento( aModel.getDataInserimento() );
          setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
          setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
          setDataAggiornamento( aModel.getDataAggiornamento() );
          setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
          setAssSocIdAssSociale( aModel.getAssSocIdAssSociale() );
          setAttIdAttivita( aModel.getAttIdAttivita() );
      }


      public void setDAOFromModelForUpdate(AssistenteSocialeAttivitaModel aModel) throws DAOException
      {
         setDataInizio( aModel.getDataInizio() );
         setDataFine( aModel.getDataFine() );
         setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
         setDataAggiornamento( aModel.getDataAggiornamento() );
         setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
         setAssSocIdAssSociale( aModel.getAssSocIdAssSociale() );
         setAttIdAttivita( aModel.getAttIdAttivita() );
         //setCondizioneUpdate(aModel.getIdAssistenteSocialeAttivita());
      }

      /**
       * Vengono valorizzati i campi da aggiornare sul record
       * assistente_sociale_attività da chiudere.
       * Il Model di scambio è quello da inserire poi, per questo i
       * campi sono invertiti.
       * @param aModel
       * @throws DAOException
       */
      public void setDAOFromModelForchiusura(AssistenteSocialeAttivitaModel aModel) throws DAOException
      {
         setDataFine(aModel.getDataInizio());
         setCodOperatoreAggiornamento(aModel.getCodOperatoreInserimento());
         setCodUfficioAggiornamento(aModel.getCodUfficioInserimento());
         setCondizioneChiudiAssXAtt(aModel);
      }


	public void setCondizione(AssistenteSocialeAttivitaModel aModel)
      {
         String lCondizioni = new String();

         boolean lInserito = false;
         if ( lInserito ) setCondition(lCondizioni);
      }

      /**
       * La funzione setta le condizioni per la ricerca di uno specifico Assistente Sociale
       *  legato ad una Attività specifica ed in stato attivo.
       *
       */
      public void setCondizioneAssAttivoXAtt(AssistenteSocialeAttivitaModel aModel)
      {
         String lCondizioni = new String();

         lCondizioni = " ATT_ID_ATTIVITA = " + aModel.getAttIdAttivita();
         lCondizioni += " AND ASS_SOC_ID_ASS_SOCIALE = " + aModel.getAssSocIdAssSociale();
         lCondizioni += " AND DATA_FINE IS NULL";
         setCondition(lCondizioni);
      }

      public void setCondizioneChiudiAssXAtt(AssistenteSocialeAttivitaModel aModel)
      {
         String lCondizioni = new String();

         lCondizioni = " ATT_ID_ATTIVITA = " + aModel.getAttIdAttivita();
         lCondizioni += "AND DATA_FINE IS NULL";
         setCondition(lCondizioni);
      }

	public void setCondizioneByIdAttivita(BigDecimal key)
      {
         setCondition(" ATT_ID_ATTIVITA = " + key );
      }

}
