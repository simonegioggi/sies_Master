package siap.sius.collaboratore.dao;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.sius.collaboratore.model.CollaboratoreModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
* <p>Title: CollaboratoreSqlDAO</p>
* <p>Description: Classe SqlDAO di interfaccia alla tabella COLLA.COLLABORATORE.</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* <p> La classe implementa le funzioni per accedere ai dati su questa tabella.
* @version 2.4
*/

public class CollaboratoreSqlDAO extends SqlDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public CollaboratoreSqlDAO (Connection con)
    {
		super(con);
    }


       //
       // METODO RICERCA()
       //
       public void ricercaCollaboratore( CollaboratoreModel  aModel) throws DAOException
       {
          String lSql = getSqlQuery();

          lSql += " " + setCondizione(aModel);
          lSql += " " + setOrder();

          setStatement(lSql);
       }

       protected String getSqlQuery()
       {
          String lStatement = new String("");

          // Vengono letti tutti i campi tranne ID_FASCICOLO_SIUS
          lStatement += " SELECT " +
              "ID_COLLABORATORE, "+      
              "UFFICIO, "+  
              "DATA_INIZIO, "+
              "DATA_FINE, "+
              "COD_OPERATORE_INSERIMENTO, "+  
              "COD_UFFICIO_INSERIMENTO, "+  
              "DATA_INSERIMENTO, "+
              "COD_OPERATORE_AGGIORNAMENTO, "+  
              "COD_UFFICIO_AGGIORNAMENTO, "+  
              "DATA_AGGIORNAMENTO ";
         
              lStatement += " FROM COLLA.COLLABORATORE ";
              return lStatement;
      }

      //
      // METODO GETMODEL()
      //
      public GenericModel getModel() throws DAOException
      {
        CollaboratoreModel aModel = new CollaboratoreModel();
        aModel.setIdCollaboratore(getBigDecimal("ID_COLLABORATORE"));
        aModel.setCodUfficio( getString("UFFICIO"));
        aModel.setDataInizio(getDate("DATA_INIZIO"));
        aModel.setDataFine(getDate("DATA_FINE"));
        aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
        aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
        aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
        aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
        aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
        aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
        
        //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        //siesLogger.debug("Data 1: " + DateUtils.getDateToString(aModel.getDataInizio(),"dd/MM/yyyy"));  
  	  	//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  	  	//siesLogger.debug("Data 2: " + DateUtils.getDateToString(aModel.getDataFine(),"dd/MM/yyyy"));      
        
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("CollaboratoreModel: " + aModel);  
        return aModel;    
    }
   

      public String setCondizione(CollaboratoreModel aModel)
      {
         String lCondizioni = new String();

         boolean lInserito = false;


         if (aModel.getIdFascicoloSius() != null && aModel.getCodUfficio().trim().length() > 0)
         {
        	// Ricerca per ID_FASCICOLO_SIUS
            lInserito = true;
            lCondizioni = " ID_FASCICOLO_SIUS = COLLA.COLL.cripta('" +aModel.getCodUfficio() + "'," + aModel.getIdFascicoloSius()  + ")" ;
         }
         else
             if (aModel.getIdCollaboratore() != null)
             {
            	 // Ricerca per ID_COLLABORATORE (chiave)
                 lInserito = true;
                 lCondizioni = " ID_COLLABORATORE = '" +  aModel.getIdCollaboratore() + "' ";
              }
         if (lInserito)
            lCondizioni = " WHERE " + lCondizioni;

         return lCondizioni;
      }

      // Setta l'ordinamento dalla più recente alla meno recente
      public String setOrder()
      {
         String lOrdering = " ORDER BY DATA_FINE DESC" ;
         return lOrdering;
      }

}