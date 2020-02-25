package siap.siep.statis.dao;

/**
* <p>Title: StatoFascicoloResSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella StatoFascicoloRes</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.sql.Connection;

import siap.siep.statis.model.StatoFascicoloResModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

public class StatoFascicoloResSqlDAO extends SqlDAO {

	public StatoFascicoloResSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaStatoFascicoloRes(StatoFascicoloResModel aModel) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "COD_STATO_FASCICOLO, " + "DESCRIZIONE ";
		lStatement += " FROM STATOFASCICOLORES";
		lStatement += " WHERE ";

		lStatement += " " + setCondizioni(aModel);
		setStatement(lStatement);
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		StatoFascicoloResModel aModel = new StatoFascicoloResModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setCodStatoFascicolo(getInteger("COD_STATO_FASCICOLO"));
		aModel.setDescrizione(getString("DESCRIZIONE"));
		return aModel;
	}

	public void selCondizione(StatoFascicoloResModel aModel) {
		// String lCondizioni = new String();
		// boolean lInserito = false;
	}

	public String setCondizioni(StatoFascicoloResModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

}