package siap.siep.posizionemateriale.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.siep.posizionemateriale.model.PosizioneMaterialeModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: PosizioneMaterialeSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella PosizioneMateriale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

  public class PosizioneMaterialeSqlDAO extends SIAPSqlDAO
  {
    public PosizioneMaterialeSqlDAO (Connection con)
    {
      super(con);
    }

    //
    // METODO RICERCA()
    //
    public void ricercaPosizioneMateriale( PosizioneMaterialeModel  aModel)	 throws DAOException
    {
      String lSql = getSqlQuery();
  
      lSql += " " + setCondizione(aModel);
      lSql += " " + setOrderDescrizione();	// 11/03/2010
      
      setStatement(lSql);
    }

    protected String getSqlQuery()
    {
      String lStatement = new String("");

      lStatement += " SELECT " +
                    "COD_POSIZIONE_MATERIALE, "+
                    "P.COD_UFFICIO COD_UFFICIO, "+
                    "DESC_POSIZIONE_MATERIALE, "+
                    "COD_OPERATORE_INSERIMENTO, "+
                    "DATA_INSERIMENTO, "+
                    "COD_UFFICIO_INSERIMENTO, "+
                    "COD_OPERATORE_AGGIORNAMENTO, "+
                    "DATA_AGGIORNAMENTO, "+
                    "COD_UFFICIO_AGGIORNAMENTO, "+
                    "U.COD_COMUNE COD_COMUNE, "+
                    "U.COD_TIPO_UFFICIO COD_TIPO_UFFICIO, "+
                    "U.COD_PROVINCIA COD_PROVINCIA, "+
                    "C.DESCRIZIONE DESC_COMUNE, " +
                    "P.DATA_FINE_VALIDITA DFV";
      
      lStatement += " FROM POSIZIONE_MATERIALE P";
      lStatement += " JOIN UFFICIO U ON P.COD_UFFICIO = U.COD_UFFICIO";
      lStatement += " LEFT OUTER JOIN COMUNE C ON U.COD_COMUNE = C.COD_COMUNE";

      return lStatement;
    }


    //
    // METODO GETMODEL()
    //
    
    public GenericModel  getModel() throws DAOException
    {
      PosizioneMaterialeModel aModel = new  PosizioneMaterialeModel();

      //Inserire le opportune set delle descrizioni!
      aModel.setCodPosizioneMateriale(getString("COD_POSIZIONE_MATERIALE") );
      aModel.setCodUfficio(getString("COD_UFFICIO") );
      aModel.setDescrUfficio( DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoUfficio(),   getString("COD_TIPO_UFFICIO")) + " di " + getString("DESC_COMUNE") + " (" + getString("COD_PROVINCIA") + ")");
      aModel.setDescPosizioneMateriale(  getString("DESC_POSIZIONE_MATERIALE") );
      aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
      aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
      aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
      //aModel.setDescrUfficioInserimento(getString("") );
      aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
      aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
      aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
      // aModel.setDescrUfficioAggiornamento(getString("") );
      aModel.setDataFineValidita( getDate("DFV") );
      
      return aModel;
    }

    public String  setCondizione(PosizioneMaterialeModel aModel)
    {
      String lCondizioni = new String();

      boolean lInserito = false;

      if(aModel.getCodPosizioneMateriale() != null && aModel.getCodPosizioneMateriale().trim().length() > 0)
      {
        lInserito = true;
        lCondizioni = " COD_POSIZIONE_MATERIALE = '" + aModel.getCodPosizioneMateriale().trim() + "'";
      }
      
      if(aModel.getCodUfficio() != null && aModel.getCodUfficio().trim().length() > 0)
      {
        if (lInserito)
          lCondizioni += " AND";

        lInserito = true;
        lCondizioni += " P.COD_UFFICIO = '" + aModel.getCodUfficio().trim() + "'";
      }

      if(aModel.getDescPosizioneMateriale() != null && aModel.getDescPosizioneMateriale().trim().length() > 0)
      {
        if (lInserito)
          lCondizioni += " AND";

        lInserito = true;
        lCondizioni += " DESC_POSIZIONE_MATERIALE LIKE '%" + aModel.getDescPosizioneMateriale().trim() + "%'";
      }

      if(   aModel.getFiltroDataValidita() != null 
         && aModel.getFiltroDataValidita().trim().length() > 0
         && !"2".equals( aModel.getFiltroDataValidita() ) // FiltroDataValidita = 2 ==> Visualizza Tutte le Posizioni Materiali  
         )
      {
        if (lInserito)
          lCondizioni += " AND";

        lInserito = true;
        
        // FiltroDataValidita = 0 ==> Visualizza Solo Posizioni Materiali Valide
        if( "0".equals( aModel.getFiltroDataValidita() ) )
        {
          lCondizioni += " (P.DATA_FINE_VALIDITA IS NULL OR P.DATA_FINE_VALIDITA > SYSDATE)";
        }
        // FiltroDataValidita = 1 ==> Visualizza Solo Posizioni Materiali NON Valide
        if( "1".equals( aModel.getFiltroDataValidita() ) )
        {
          lCondizioni += " (P.DATA_FINE_VALIDITA IS NOT NULL OR P.DATA_FINE_VALIDITA <= SYSDATE)";
        }
       }
      // Aggiungere le altre condizioni ....

      if (lInserito)
        lCondizioni = " WHERE " + lCondizioni;

      return lCondizioni;
    }
    
    private String setOrderDescrizione()
    {
      String lOrder = new String();
      lOrder = " ORDER BY desc_posizione_materiale ASC";
      return lOrder;
    }
    
}
