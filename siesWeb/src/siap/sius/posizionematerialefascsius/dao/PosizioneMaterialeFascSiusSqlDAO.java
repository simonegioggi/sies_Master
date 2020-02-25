package siap.sius.posizionematerialefascsius.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
* <p>Title: PosizioneMaterialeFascSiusSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella PosizioneMaterialeFasc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

  public class PosizioneMaterialeFascSiusSqlDAO extends SqlDAO
  {
    public PosizioneMaterialeFascSiusSqlDAO(Connection con)
    {
      super(con);
    }


    //
    // METODO RICERCA()
    //


    public void ricercaPosizioneMaterialeFasc(PosizioneMaterialeFascModel aModel) throws
        DAOException
    {
      String lSql = getSqlQuery();

      lSql += " " + setCondizione(aModel);
      lSql += " " + setOrder();

      setStatement(lSql);
    }

    public void ricercaPosizioneMaterialeFascAttivaXFas(BigDecimal aIdFascicolo) throws
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
          "PF.COD_POSIZIONE_MATERIALE, " +
          "PF.COD_UFFICIO, " +
          "PF.FAS_SIUS_ID_FASCICOLO_SIUS, " +
          "PF.COD_STATO_PROCEDIMENTO, " +
          "PF.DESCR_STATO_PROCEDIMENTO, " +
          "PF.DATA_INIZIO, " +
          "PF.DATA_FINE, " +
          "PF.COD_OPERATORE_INSERIMENTO, " +
          "PF.DATA_INSERIMENTO, " +
          "PF.COD_UFFICIO_INSERIMENTO, " +
          "PF.COD_OPERATORE_AGGIORNAMENTO, " +
          "PF.DATA_AGGIORNAMENTO, " +
          "PF.COD_UFFICIO_AGGIORNAMENTO, "+
          "P.DESC_POSIZIONE_MATERIALE";

      lStatement += " FROM POSIZIONE_MATERIALE_FASC_SIUS PF";
      lStatement += " JOIN POSIZIONE_MATERIALE P ON P.COD_UFFICIO = PF.COD_UFFICIO";
      lStatement += " AND P.COD_POSIZIONE_MATERIALE = PF.COD_POSIZIONE_MATERIALE";

      //lStatement += " WHERE ";
      return lStatement;
    }


 //
  // METODO GETMODEL()
  //


  public GenericModel getModel() throws DAOException
  {
    PosizioneMaterialeFascModel aModel = new PosizioneMaterialeFascModel(PosizioneMaterialeFascModel.POSIZIONE_MATERIALE_FASCICOLO_SIUS);

    //Inserire le opportune set delle descrizioni!
    aModel.setCodPosizioneMateriale(getString("COD_POSIZIONE_MATERIALE"));
    aModel.setDescrPosizioneMateriale(getString("DESC_POSIZIONE_MATERIALE"));
    aModel.setCodUfficio(getString("COD_UFFICIO"));
    aModel.setFasSieIdFascicoloSiep (getBigDecimal("FAS_SIUS_ID_FASCICOLO_SIUS"));
    aModel.setCodStatoProcedimento(getString("COD_STATO_PROCEDIMENTO"));
    aModel.setDescrStatoProcedimento(getString("DESCR_STATO_PROCEDIMENTO"));
    if (aModel.getDescrStatoProcedimento() == null || aModel.getDescrStatoProcedimento().trim().length() < 1)
      aModel.setDescrStatoProcedimento(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getStatoProcedimento(), aModel.getCodStatoProcedimento() ));
    aModel.setDataInizio(getDate("DATA_INIZIO"));
    aModel.setDataFine(getDate("DATA_FINE"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
    return aModel;
  }


  public String setCondizione(PosizioneMaterialeFascModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;


    if (aModel.getCodPosizioneMateriale() != null && aModel.getCodPosizioneMateriale().trim().length() > 0)
    {
      lInserito = true;
      lCondizioni = " COD_POSIZIONE_MATERIALE = '" + aModel.getCodPosizioneMateriale().trim() + "'"; ;
    }
    if (aModel.getCodUfficio() != null && aModel.getCodUfficio().trim().length() > 0)
    {
      if (lInserito)
        lCondizioni += " AND";

      lInserito = true;
      lCondizioni += " COD_UFFICIO = '" + aModel.getCodUfficio().trim() + "'"; ;
    }

    if (aModel.getFasSieIdFascicoloSiep() != null )
    {
      if (lInserito)
        lCondizioni += " AND";

      lInserito = true;
      lCondizioni += " FAS_SIUS_ID_FASCICOLO_SIUS = " + aModel.getFasSieIdFascicoloSiep();
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
