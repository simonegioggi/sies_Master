package siap.siepe.attivita.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.siepe.attivita.model.AttivitaModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
* <p>Title: AttivitaSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Attivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class AttivitaSqlDAO extends SqlDAO
{
	 public AttivitaSqlDAO (Connection con)
			{
			 super(con);
			}


 //
  // METODO RICERCA()
  //


  public void ricercaAttivita( AttivitaModel  aModel)	 throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizione(aModel);
    setStatement(lSql);
  }


  public void ricercaAttivitaByKey( BigDecimal aKey)	 throws DAOException
		{
			 String lSql = getSqlQuery();

		 lSql += " " + setCondizioniByKey(aKey);
			 setStatement(lSql);
		}


  protected String getSqlQuery()
		{			 String lStatement = new String("");

			 lStatement += " SELECT " +
				 "ID_ATTIVITA, "+
				 "DATA_INIZIO, "+
				 "DATA_CHIUSURA, "+
				 "COD_TIPO_ATTIVITA, "+
				 "NOTE, "+
				 "DOC_BLOB, "+
				 "COD_OPERATORE_INSERIMENTO, "+
				 "DATA_INSERIMENTO, "+
				 "COD_UFFICIO_INSERIMENTO, "+
				 "COD_OPERATORE_AGGIORNAMENTO, "+
				 "DATA_AGGIORNAMENTO, "+
				 "COD_UFFICIO_AGGIORNAMENTO, "+
				 "FAS_SIE_ID_FAS_SIEPE, "+
				 "ASS_SOC_ID_ASS_SOCIALE, " +
                         "FLAG_DOCUMENTO_REGISTRATO, " +
                          "COD_ESITO_ATTIVITA, " +
                          "NOTA_CHIUSURA ";
			 lStatement += " FROM ATTIVITA";
			 //lStatement += " WHERE ";
			 return lStatement;		}


 //
  // METODO GETMODEL()
  //


  public GenericModel  	 getModel() throws DAOException
  {
    AttivitaModel aModel = new  AttivitaModel();

//Inserire le opportune set delle descrizioni!
    aModel.setIdAttivita(getBigDecimal("ID_ATTIVITA") );
    aModel.setDataInizio(getDate("DATA_INIZIO") );
    aModel.setDataChiusura(getDate("DATA_CHIUSURA") );
    aModel.setCodTipoAttivita(getString("COD_TIPO_ATTIVITA") );
    try
    {
      aModel.setDescrTipoAttivita(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoAttivitaSiepe(), aModel.getCodTipoAttivita()));
    }
    catch (Exception e)
    {
      throw new DAOException(e.toString());
    }
    aModel.setNote(getString("NOTE") );
    /* aModel.setDocBlob(getBlob("DOC_BLOB") ); */
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setFasSieIdFasSiepe(getBigDecimal("FAS_SIE_ID_FAS_SIEPE") );
    aModel.setAssSocIdAssSociale(getBigDecimal("ASS_SOC_ID_ASS_SOCIALE") );
    aModel.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));
    aModel.setCodEsitoAttivita(getString("COD_ESITO_ATTIVITA"));
    if (aModel.getCodEsitoAttivita() != null)
    {
       try
       {
          aModel.setDescrEsitoAttivita(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getEsitoAttivitaSiepe(), aModel.getCodEsitoAttivita()));
       }
       catch (Exception e)
       {
          throw new DAOException(e.toString());
       }
    }
    aModel.setNotaChiusura(getString("NOTA_CHIUSURA"));
    return aModel;
  }


  public String  setCondizione(AttivitaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;

    if (aModel.getFasSieIdFasSiepe() != null)
    {
      lCondizioni = " FAS_SIE_ID_FAS_SIEPE = " + aModel.getFasSieIdFasSiepe();
      lInserito = true;
    }
    if (aModel.getCodTipoAttivita().trim().length() > 0)
    {
      if(lInserito)
        lCondizioni += " AND";
      lCondizioni = " COD_TIPO_ATTIVITA = '" + aModel.getCodTipoAttivita() +"'";
      lInserito = true;
    }
    // Aggiungere qui le altre condizioni ....

    if(lInserito)
      lCondizioni = " WHERE " + lCondizioni;
    return lCondizioni;
    }


  public String setCondizioniByKey(BigDecimal aKey)
  {
    return " WHERE ID_ATTIVITA = " + aKey;
  }
}
