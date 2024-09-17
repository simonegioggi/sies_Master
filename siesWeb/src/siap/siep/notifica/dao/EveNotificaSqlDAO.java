package siap.siep.notifica.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.siep.notifica.model.NotificaModel;

/**
 * EveNotificaSqlDAO - Classe SqlDAO che rappresenta la tabella Notifica join Evento per ricavare il motivo
 * Evento nella lista degli atti richiesti.
 *
 * @version 1.0
 */

public class EveNotificaSqlDAO extends NotificaSqlDAO {

	public EveNotificaSqlDAO(Connection con) {

		super(con);
	}

	public void ricercaEveNotificaByFascicoloSius(BigDecimal aKey, String aTipoEvento) throws DAOException {

		String lStatement = getSqlQuery();
		lStatement += " AND E.FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;

		if (aTipoEvento != null)
			lStatement += " AND E.COD_TIPO_EVENTO = '" + aTipoEvento + "'";

		lStatement += setOrder();

		setStatement(lStatement);
	}

	private String setOrder() {

		String lCondizioni = new String(" ORDER BY DATA_INVIO DESC, EVE_ID_EVENTO ");

		return lCondizioni;
	}

	protected String getSqlQuery() {

		String lStatement = new String("");

		lStatement += "SELECT CODMOV.RV_MEANING MOTIVO_EVE, N.ID_NOTIFICA ID_NOTIFICA, "
				+ "N.COD_TIPO_NOTIFICA COD_TIPO_NOTIFICA, COD_NOT.RV_MEANING TIP_NOT, "
				+ "N.DATA_AVVENUTA_NOTIFICA DATA_AVVENUTA_NOTIFICA, N.DATA_INVIO DATA_INVIO, "
				+ "N.COD_ESITO COD_ESITO, COD_ESI.RV_MEANING ESITO, N.NOTE NOTE, "
				+ "N.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO, "
				+ "N.DATA_INSERIMENTO DATA_INSERIMENTO, "
				+ "N.COD_UFFICIO_INSERIMENTO COD_UFFICIO_INSERIMENTO, "
				+ "N.CODICE_OPERATORE_AGGIORNAMENTO CODICE_OPERATORE_AGGIORNAMENTO, "
				+ "N.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO, "
				+ "N.COD_UFFICIO_AGGIORNAMENTO COD_UFFICIO_AGGIORNAMENTO, "
				+ "N.EVE_ID_EVENTO EVE_ID_EVENTO, "
				+ "N.AUT_EST_ID_AUTORITA_ESTERNA AUT_EST_ID_AUTORITA_ESTERNA, "
				+ "N.SOG_ID_SOGGETTO SOG_ID_SOGGETTO, "
				+ "N.AVV_ID_AVVOCATO_FASCICOLO_SIEP AVV_ID_AVVOCATO_FASCICOLO_SIEP, "
				+ "N.AVV_ID_AVVOCATO_FASCICOLO_SIUS AVV_ID_AVVOCATO_FASCICOLO_SIUS, "
				+ "N.AVV_ID_AVVOCATO_FASCICOLO_SIGE AVV_ID_AVVOCATO_FASCICOLO_SIGE, "
				+ "N.SOLLECITO SOLLECITO, CSS_ID_CSSA,N.CUR_ID_CURATORE CUR_ID_CURATORE, "
				+ "N.UFF_COD_UFFICIO UFF_COD_UFFICIO, E.FLAG_DOCUMENTO_REGISTRATO FLAG, "
				// modifica relativa al tipo istituto
				+ "N.IST_DET_ID_ISTITUTO_DETENZIONE, N.ID_PARTE_UDIENZA, "
				// Ticket#202405170122 - errore nella ricerca di fascicoli SIUS in cui è presente una
				// richiesta istruttoria
				+ "N.ID_CIVILMENTE_OBBLIGATO " // aggiunto campo in estrazione
				+ "FROM NOTIFICA N, CG_REF_CODES COD_NOT, "
				+ "CG_REF_CODES COD_ESI, EVENTO E, CG_REF_CODES CODMOV "
				+ "WHERE E.ID_EVENTO = N.EVE_ID_EVENTO "
				+ "AND COD_NOT.RV_DOMAIN = 'TIPO_NOTIFICA' AND COD_NOT.RV_LOW_VALUE = N.COD_TIPO_NOTIFICA "
				+ "AND COD_ESI.RV_DOMAIN = 'ESITO_NOTIFICA' AND COD_ESI.RV_LOW_VALUE = N.COD_ESITO "
				+ "AND E.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {

		NotificaModel aModel;
		aModel = (NotificaModel) super.getModel();
		aModel.setDescrizione(this.getString("MOTIVO_EVE"));
		aModel.setFlagDocRegistrato(this.getString("FLAG"));
		return aModel;
	}

	/*****************************************************************************
	 * Recupera la data invio del Certificato Casellario Giudiziario
	 ****************************************************************************/
	public Date getDataInvioCertCasellario(BigDecimal aFascKey, String aTipoEvento, String aCodMotivo)
			throws DAOException {

		Date dataInvio = null;
		String lStatement = new String("");

		lStatement += " SELECT ";
		lStatement += " MAX(DATA_INVIO) DATA_INVIO ";
		lStatement += " FROM NOTIFICA N, CG_REF_CODES COD_NOT,";
		lStatement += " CG_REF_CODES COD_ESI, ";
		lStatement += " EVENTO E, CG_REF_CODES CODMOV ";
		lStatement += " WHERE E.ID_EVENTO = N.EVE_ID_EVENTO ";
		lStatement += " AND COD_NOT.RV_DOMAIN = 'TIPO_NOTIFICA' AND COD_NOT.RV_LOW_VALUE = N.COD_TIPO_NOTIFICA";
		lStatement += " AND COD_ESI.RV_DOMAIN = 'ESITO_NOTIFICA' AND COD_ESI.RV_LOW_VALUE = N.COD_ESITO ";
		lStatement += " AND E.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";

		lStatement += " AND E.FAS_SIU_ID_FASCICOLO_SIUS = " + aFascKey;

		if (aTipoEvento != null)
			lStatement += " AND E.COD_TIPO_EVENTO = '" + aTipoEvento + "'";
		if (aCodMotivo != null)
			lStatement += " AND E.COD_MOTIVO = '" + aCodMotivo + "'";

		setStatement(lStatement);

		this.start();

		if (this.next() && (this.getDate("DATA_INVIO") != null))
			dataInvio = this.getDate("DATA_INVIO");

		this.stop();

		return dataInvio;
	}

}