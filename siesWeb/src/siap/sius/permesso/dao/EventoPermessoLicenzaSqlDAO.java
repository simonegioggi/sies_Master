package siap.sius.permesso.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.sius.permesso.model.EventoPermessoLicenzaModel;

/**
 * EventoPermessoLicenzaSqlDAO - Classe SqlDAO che rappresenta la tabella EventoPermessoLicenza
 *
 * @version 1.0
 */
public class EventoPermessoLicenzaSqlDAO extends SqlDAO {

	public EventoPermessoLicenzaSqlDAO(Connection aCon) {

		super(aCon);
	}

	//
	// METODO RICERCA()
	//
	/*
	 * public void ricercaEventoPermessoLicenza( EventoPermessoLicenzaModel aModel) throws DAOException {
	 * String lSql = getSqlQuery(); lSql += " " + setCondizione(aModel); setStatement(lSql); }
	 */
	public void ricercaEventoPermessoLicenzaByKey(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaEventoPermessoLicenzaByKeyLicLib(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKeyLicLib(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {

		String lStatement = new String("");
		lStatement += " SELECT " + " ID_EVENTO_PERMESSO_LICENZA, " + " COD_TIPO_EVENTO, "
				+ " TE.RV_MEANING AS DESCR_TIPO_EVENTO, " + " DATA_SEGNALAZIONE, " + " DESCR_EVENTO, "
				+ " MITTENTE_SEGNALAZIONE, " + " COD_TIPO_CONSEGUENZA, "
				+ " TC.RV_MEANING AS DESCR_TIPO_CONSEGUENZA, " + " DESCR_CONSEGUENZE, "
				+ " COD_OPERATORE_INSERIMENTO, " + " COD_UFFICIO_INSERIMENTO, " + " DATA_INSERIMENTO, "
				+ " COD_OPERATORE_AGGIORNAMENTO, " + " COD_UFFICIO_AGGIORNAMENTO, " + " DATA_AGGIORNAMENTO, "
				+ " COD_UFFICIO_INSERIMENTO, " + " COD_OPERATORE_AGGIORNAMENTO, "
				+ " LIC_ID_LICENZA_LIBANTICIPATA " + " FROM " + " EVENTO_PERMESSO_LICENZA EPL "
				+ " INNER JOIN CG_REF_CODES TE ON TE.RV_LOW_VALUE = EPL.COD_TIPO_EVENTO "
				+ " AND TE.RV_DOMAIN = 'TIPO_EVENTO_PERMESSO_LICENZA'  "
				+ " INNER JOIN CG_REF_CODES TC ON TC.RV_LOW_VALUE = EPL.COD_TIPO_CONSEGUENZA "
				+ " AND TC.RV_DOMAIN = 'TIPO_CONSEGUENZA' ";
		// lStatement += " WHERE ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {

		EventoPermessoLicenzaModel lModel = new EventoPermessoLicenzaModel();

		// Inserire le opportune set delle descrizioni!
		lModel.setIdEventoPermessoLicenza(getBigDecimal("ID_EVENTO_PERMESSO_LICENZA"));
		lModel.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
		lModel.setDescrTipoEvento(getString("DESCR_TIPO_EVENTO"));
		lModel.setDataSegnalazione(getDate("DATA_SEGNALAZIONE"));
		lModel.setDescrEvento(getString("DESCR_EVENTO"));
		lModel.setMittenteSegnalazione(getString("MITTENTE_SEGNALAZIONE"));
		lModel.setCodTipoConseguenza(getString("COD_TIPO_CONSEGUENZA"));
		lModel.setDescrTipoConseguenza(getString("DESCR_TIPO_CONSEGUENZA"));
		lModel.setDescrConseguenze(getString("DESCR_CONSEGUENZE"));
		lModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		lModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		lModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		lModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		lModel.setCodUfficioInserimento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		lModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		lModel.setLicIdLicenzaLibAnticipata(getBigDecimal("LIC_ID_LICENZA_LIBANTICIPATA"));

		return lModel;
	}

	/*
	 * public String setCondizione(EventoPermessoLicenzaModel aModel) { String lCondizioni = new String();
	 * 
	 * boolean lInserito = false;
	 * 
	 * return lCondizioni; }
	 */

	public String setCondizioniByKey(BigDecimal aKey) {

		return " WHERE ID_EVENTO_PERMESSO_LICENZA = " + aKey;
	}

	public String setCondizioniByKeyLicLib(BigDecimal aKey) {

		return " WHERE LIC_ID_LICENZA_LIBANTICIPATA = " + aKey;
	}

}