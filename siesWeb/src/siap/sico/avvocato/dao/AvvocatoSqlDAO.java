package siap.sico.avvocato.dao;

/**
* <p>Title: AvvocatoSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.avvocato.model.AvvocatoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;


public class AvvocatoSqlDAO extends SIAPSqlDAO
{
  public AvvocatoSqlDAO (Connection con)
  {
   super(con);
  }

	// 19/03/2010 Rework per Normalizzazione COMBO dei FORI in tutte le fasi di gestione Avvocati.
	public void ricercaForo()	 throws DAOException
  {
    String lStatement = new String("");
    lStatement += " select distinct(FORO) from AVVOCATO  where FLAG_VISUALIZZA=1";
    lStatement += " order by FORO asc";
    setStatement(lStatement);
  }

	// 19/03/2010 Rework per Normalizzazione COMBO dei FORI in tutte le fasi di gestione Avvocati.
  public GenericModel getModelForo() throws DAOException
  {
    AvvocatoModel aModel = new  AvvocatoModel();
    //Inserire le opportune set delle descrizioni!
    aModel.setForo(getString("FORO"));
    return aModel;
  }
  
}
