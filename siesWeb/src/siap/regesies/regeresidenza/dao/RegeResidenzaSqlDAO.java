package siap.regesies.regeresidenza.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.regesies.regeresidenza.model.RegeResidenzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: RegeResidenzaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella RegeResidenza
 * </p>
 */
public class RegeResidenzaSqlDAO extends SIAPSqlDAO {

	public RegeResidenzaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaRegeResidenza(String aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioni(aKey);
		setStatement(lSql);
	}

	public void ricercaRegeResidenzaByKey(String aKey, String aTipo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey, aTipo);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT" + " ID_FILE," + " COD_STATO," + " DESCR_STATO.RV_MEANING DESCR_STATO,"
				+ " REGE_RESIDENZA.COD_PROVINCIA," + " DESCR_PROVINCIA.RV_MEANING DESCR_PROVINCIA,"
				+ " REGE_RESIDENZA.COD_COMUNE," + " COMUNE.DESCRIZIONE DESCR_COMUNE," + " REGE_RESIDENZA.CAP,"
				+ " INDIRIZZO," + " DESC_COMUNE_ESTERO," + " COD_TIPO_RESIDENZA,"
				+ " DESCR_TIPO_RESIDENZA.RV_MEANING DESCR_TIPO_RESIDENZA," + " COD_OPERATORE_INSERIMENTO,"
				+ " DATA_INSERIMENTO," + " COD_UFFICIO_INSERIMENTO," + " COD_OPERATORE_AGGIORNAMENTO,"
				+ " DATA_AGGIORNAMENTO," + " COD_UFFICIO_AGGIORNAMENTO";
		lStatement += " FROM REGE_RESIDENZA, CG_REF_CODES DESCR_STATO, CG_REF_CODES DESCR_PROVINCIA,";
		lStatement += " COMUNE, CG_REF_CODES DESCR_TIPO_RESIDENZA";
		lStatement += " WHERE (DESCR_STATO.RV_DOMAIN = 'NAZIONE' AND DESCR_STATO.RV_LOW_VALUE = REGE_RESIDENZA.COD_STATO)";
		lStatement += " AND (DESCR_PROVINCIA.RV_DOMAIN = 'PROVINCIA' AND DESCR_PROVINCIA.RV_LOW_VALUE = REGE_RESIDENZA.COD_PROVINCIA)";
		lStatement += " AND (COMUNE.COD_COMUNE = REGE_RESIDENZA.COD_COMUNE)";
		lStatement += " AND (DESCR_TIPO_RESIDENZA.RV_DOMAIN = 'TIPO_RESIDENZA' AND DESCR_TIPO_RESIDENZA.RV_LOW_VALUE = REGE_RESIDENZA.COD_TIPO_RESIDENZA)";

		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		RegeResidenzaModel aModel = new RegeResidenzaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdFile(getString("ID_FILE"));
		aModel.setCodStato(getString("COD_STATO"));
		aModel.setDescrStato(getString("DESCR_STATO"));
		aModel.setCodProvincia(getString("COD_PROVINCIA"));
		aModel.setDescrProvincia(getString("DESCR_PROVINCIA"));
		aModel.setCodComune(getString("COD_COMUNE"));
		aModel.setDescrComune(getString("DESCR_COMUNE"));
		aModel.setCap(getString("CAP"));
		aModel.setIndirizzo(getString("INDIRIZZO"));
		aModel.setCodTipoResidenza(getString("COD_TIPO_RESIDENZA"));
		aModel.setDescrTipoResidenza(getString("DESCR_TIPO_RESIDENZA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setDescComuneEstero(getString("DESC_COMUNE_ESTERO"));
		aModel.calcolaStringaResidenza();
		return aModel;
	}

	public String setCondizione(RegeResidenzaModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioni(String aKey) {

		return " AND ID_File = '" + aKey + "'";
	}

	public String setCondizioniByKey(String aKey, String aTipo) {
		return " AND ID_File = '" + aKey + "' AND COD_TIPO_RESIDENZA = '" + aTipo + "'";
	}

}