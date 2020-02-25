package siap.sius.cancassfascsius.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
* <p>Title: CancAssFascSiusSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella CancAssFascSius</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class CancAssFascSiusSqlDAO extends SqlDAO
{
	 public CancAssFascSiusSqlDAO (Connection con)
       {
          super(con);
       }


       //
       // METODO RICERCA()
       //
       public void ricercaCancAssFascSius( CancAssFascSiusModel  aModel) throws DAOException
       {
          String lSql = getSqlQuery();

          lSql += " " + setCondizione(aModel);
          lSql += " " + setOrder();

          setStatement(lSql);
       }

       public void ricercaCancAssFascSiusAttivaXFas(BigDecimal aIdFascicolo) throws
       DAOException
       {
         String lSql = getSqlQuery();

         lSql += "  WHERE FAS_SIUS_ID_FASCICOLO_SIUS= " + aIdFascicolo;
         lSql += " AND DATA_FINE IS NULL";

         setStatement(lSql);
       }



       protected String getSqlQuery()
       {
          String lStatement = new String("");

          lStatement += " SELECT " +
              "CF.COD_CANCELLERIA_ASSEGNATARIA, "+
              "CF.COD_UFFICIO, "+
              "CF.FAS_SIUS_ID_FASCICOLO_SIUS, "+
              "CF.COD_STATO_PROCEDIMENTO, "+
              "CF.DESCR_STATO_PROCEDIMENTO, "+
              "CF.DATA_INIZIO, "+
              "CF.DATA_FINE, "+
              "CF.COD_OPERATORE_INSERIMENTO, "+
              "CF.DATA_INSERIMENTO, "+
              "CF.COD_UFFICIO_INSERIMENTO, "+
              "CF.COD_OPERATORE_AGGIORNAMENTO, "+
              "CF.DATA_AGGIORNAMENTO, "+
              "CF.COD_UFFICIO_AGGIORNAMENTO, "+
              "CA.DESC_CANCELLERIA_ASSEGNATARIA ";

              lStatement += " FROM CANC_ASS_FASC_SIUS CF";
              lStatement += " JOIN CANCELLERIA_ASSEGNATARIA CA ON CA.COD_UFFICIO = CF.COD_UFFICIO";
              lStatement += " AND CA.COD_CANCELLERIA_ASSEGNATARIA = CF.COD_CANCELLERIA_ASSEGNATARIA";

              //lStatement += " WHERE ";
              return lStatement;
      }


      //
      // METODO GETMODEL()
      //
      public GenericModel  	 getModel() throws DAOException
      {
         CancAssFascSiusModel aModel = new  CancAssFascSiusModel();

         aModel.setCodCancelleriaAssegnataria(getString("COD_CANCELLERIA_ASSEGNATARIA") );
         aModel.setDescCancelleriaAssegnataria(getString("DESC_CANCELLERIA_ASSEGNATARIA") );
         aModel.setCodUfficio(getString("COD_UFFICIO") );
         aModel.setDescrUfficio(getString("DESC_CANCELLERIA_ASSEGNATARIA") );
         aModel.setFasSiusIdFascicoloSius(getBigDecimal("FAS_SIUS_ID_FASCICOLO_SIUS") );
         aModel.setCodStatoProcedimento(getString("COD_STATO_PROCEDIMENTO") );
         aModel.setDescrStatoProcedimento(getString("DESCR_STATO_PROCEDIMENTO") );
         aModel.setDataInizio(getDate("DATA_INIZIO") );
         aModel.setDataFine(getDate("DATA_FINE") );
         aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
         aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
         aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
         aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
         aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
         aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
         return aModel;
      }


      public String setCondizione(CancAssFascSiusModel aModel)
      {
         String lCondizioni = new String();

         boolean lInserito = false;


         if (aModel.getCodCancelleriaAssegnataria() != null && aModel.getCodCancelleriaAssegnataria().trim().length() > 0)
         {
            lInserito = true;
            lCondizioni = " COD_CANCELLERIA_ASSEGNATARIA = '" + aModel.getCodCancelleriaAssegnataria().trim() + "'"; ;
         }
         if (aModel.getCodUfficio() != null && aModel.getCodUfficio().trim().length() > 0)
         {
            if (lInserito)
               lCondizioni += " AND";

            lInserito = true;
            lCondizioni += " COD_UFFICIO = '" + aModel.getCodUfficio().trim() + "'"; ;
         }

         if (aModel.getFasSiusIdFascicoloSius() != null )
         {
            if (lInserito)
               lCondizioni += " AND";

            lInserito = true;
            lCondizioni += " FAS_SIUS_ID_FASCICOLO_SIUS = " + aModel.getFasSiusIdFascicoloSius();
         }

         // Aggiungere le altre condizioni ....

         if (lInserito)
            lCondizioni = " WHERE " + lCondizioni;

         return lCondizioni;
      }

      // Setta l'ordinamento dalla più recente alla meno recente
      public String setOrder()
      {
         String lOrdering = " ORDER BY DATA_AGGIORNAMENTO DESC" ;
         return lOrdering;
      }

}
