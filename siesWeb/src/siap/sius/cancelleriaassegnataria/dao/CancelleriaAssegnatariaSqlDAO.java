package siap.sius.cancelleriaassegnataria.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: CancelleriaAssegnatariaSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella CancelleriaAssegnataria</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class CancelleriaAssegnatariaSqlDAO extends SIAPSqlDAO
{

	 public CancelleriaAssegnatariaSqlDAO (Connection con)
       {
          super(con);
       }

  /**
   * Funzione di ricerca.
   * La funzione imposta la Query di ricerca valorizzando il filtro dai dati contenuti nel model.
   * @param aModel
   * @throws DAOException
   */
  public void ricercaCancelleriaAssegnataria( CancelleriaAssegnatariaModel  aModel)	 throws DAOException
  {
     String lSql = getSqlQuery();

     lSql += " " + setCondizione(aModel);
     setStatement(lSql);
  }

  /**
   * La funzione prepara la query di ricerca sulla tabella CANCELLERIA_ASSEGNATARIA in join con UFFICIO e COMUNE al fine di ricavare
   * la descrizione dell'Ufficio di riferimento.
   * @return
   */
  protected String getSqlQuery()
  {
     String lStatement = new String("");

     lStatement += " SELECT " +
            "COD_CANCELLERIA_ASSEGNATARIA, "+
             "CA.COD_UFFICIO COD_UFFICIO, "+
             "DESC_CANCELLERIA_ASSEGNATARIA, "+
             "CA.COD_OPERATORE_INSERIMENTO, "+
             "CA.DATA_INSERIMENTO, "+
             "CA.COD_UFFICIO_INSERIMENTO, "+
             "CA.COD_OPERATORE_AGGIORNAMENTO, "+
             "CA.DATA_AGGIORNAMENTO, "+
             "CA.COD_UFFICIO_AGGIORNAMENTO, "+
             "U.COD_COMUNE COD_COMUNE, "+
             "U.COD_TIPO_UFFICIO COD_TIPO_UFFICIO, "+
             "U.COD_PROVINCIA COD_PROVINCIA, "+
             "C.DESCRIZIONE DESC_COMUNE";

      lStatement += " FROM CANCELLERIA_ASSEGNATARIA CA";
      lStatement += " JOIN UFFICIO U ON CA.COD_UFFICIO = U.COD_UFFICIO";
      lStatement += " LEFT OUTER JOIN COMUNE C ON U.COD_COMUNE = C.COD_COMUNE";

      return lStatement;
   }

  /**
   * La funzione legge i dati dalla tabella in un model.
   * @return
   * @throws DAOException
   */
  public GenericModel  	 getModel() throws DAOException
  {
     CancelleriaAssegnatariaModel aModel = new  CancelleriaAssegnatariaModel();
     // Valorizzazione del model con i dati letti dal DB
     aModel.setCodCancelleriaAssegnataria(getString("COD_CANCELLERIA_ASSEGNATARIA") );
     aModel.setCodUfficio(getString("COD_UFFICIO") );
     aModel.setDescCancelleriaAssegnataria(getString("DESC_CANCELLERIA_ASSEGNATARIA") );
     aModel.setDescrUfficio( DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoUfficio(),   getString("COD_TIPO_UFFICIO")) + " di " + getString("DESC_COMUNE") + " (" + getString("COD_PROVINCIA") + ")");
     aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
     aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
     aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
     aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
     aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
     aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
     return aModel;
   }

   /**
    * La funzione stabilisce il filtro di ricerca in base ai
    * dati passati attraverso il model.
    * @param aModel
    * @return
    */
   public String  setCondizione(CancelleriaAssegnatariaModel aModel)
   {
      String lCondizioni = new String();

      boolean lInserito = false;

      if(aModel.getCodCancelleriaAssegnataria() != null && aModel.getCodCancelleriaAssegnataria().trim().length() > 0)
      {
                 lInserito = true;
                 lCondizioni = " COD_CANCELLERIA_ASSEGNATARIA = '" + aModel.getCodCancelleriaAssegnataria().trim() + "'";;
      }
      if(aModel.getCodUfficio() != null && aModel.getCodUfficio().trim().length() > 0)
      {
         if (lInserito)
            lCondizioni += " AND";

         lInserito = true;
         lCondizioni += " CA.COD_UFFICIO = '" + aModel.getCodUfficio().trim() + "'";;
      }
      if(aModel.getDescCancelleriaAssegnataria() != null && aModel.getDescCancelleriaAssegnataria().trim().length() > 0)
      {
         if (lInserito)
            lCondizioni += " AND";

         lInserito = true;
         lCondizioni += " DESC_CANCELLERIA_ASSEGNATARIA LIKE '%" + aModel.getDescCancelleriaAssegnataria().trim() + "%'";
      }

      // Aggiungere le altre condizioni ....

      if (lInserito)
         lCondizioni = " WHERE " + lCondizioni;

      return lCondizioni;
   }
}
