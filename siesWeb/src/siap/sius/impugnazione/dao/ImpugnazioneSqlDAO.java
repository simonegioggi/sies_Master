package siap.sius.impugnazione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collection;
import java.util.Vector;

import siap.sius.impugnazione.model.ImpugnazioneModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ImpugnazioneSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Impugnazione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ImpugnazioneSqlDAO extends SqlDAO {

	public ImpugnazioneSqlDAO(Connection con) {
		super(con);
	}

	/**
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
//	public void ricercaImpugnazione(ImpugnazioneModel aModel) throws DAOException {
//		String lSql = getSqlQuery();
//		lSql += " " + setCondizione(aModel);
//		setStatement(lSql);
//	}

	public void ricercaImpugnazioneByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	/**
	 * 
	 * @param aKeyDepositoDecreto
	 * @throws DAOException
	 */
	public void ricercaImpugnazioneByIdDepositoDecreto(BigDecimal aKeyDepositoDecreto) throws DAOException {
		String lSql = getSqlQueryPerJoin();

		lSql += " " + setCondizioniByIdDepositoDecreto(aKeyDepositoDecreto);

		setStatement(lSql);
	}

	/**
	 * 
	 * @param aKeyDepositoOrdinanza
	 * @throws DAOException
	 */
	public void ricercaImpugnazioneByIdDepositoOrdinanza(BigDecimal aKeyDepositoOrdinanza)
			throws DAOException {
		String lSql = getSqlQueryPerJoin();

		lSql += " " + setCondizioniByIdDepositoOrdinanza(aKeyDepositoOrdinanza);

		setStatement(lSql);
	}

	/**
	 * 
	 * @param aKeyDepositoSentenza
	 * @throws DAOException
	 */
	public void ricercaImpugnazioneByIdDepositoSentenza(BigDecimal aKeyDepositoSentenza) throws DAOException {
		String lSql = getSqlQueryPerJoin();

		lSql += " " + setCondizioniByIdDepositoSentenza(aKeyDepositoSentenza);

		setStatement(lSql);
	}

	/**
	 * Ricerca le Impugnazione con data_annullamento valorizzata
	 * 
	 * @param aIdProv
	 *            - ID_DEPOSITO_ORDINANZA_PC o ID_DEPOSITO_DECRETO
	 * @param aTipoProv
	 * @throws DAOException
	 */
	public void ricercaImpugnazioniAnnullateByProv(BigDecimal aIdProv, String aTipoProv) throws DAOException {
		String lSql = getSqlQuery();
		if (aTipoProv.equals("03")) {
			// ORDINANZA
			lSql += " WHERE IMP.DEP_OPID_DEPOSITO_ORDINANZA_PC = " + aIdProv;
		} else {
			// DECRETO
			lSql += " WHERE IMP.DEP_DEC_ID_DEPOSITO_DECRETO = " + aIdProv;
		}

		lSql += " AND (IMP.DATA_ANNULLAMENTO IS NOT NULL)";

		lSql += " ORDER BY IMP.DATA_INSERIMENTO ASC ";

		setStatement(lSql);
	}

	/**
	 * Ricerca le impugnazioni collegate al
	 * 
	 * @param aIdProv
	 *            - ID_DEPOSITO_ORDINANZA_PC o ID_DEPOSITO_DECRETO
	 * @param aTipoProv
	 * @throws DAOException
	 */
	public void ricercaImpugnazioniDelProvvedimento(BigDecimal aIdProv, String aTipoProv,
			String[] aTipoImpugnazione, String aFlagAnnullate) throws DAOException {
		String lSql = getSqlQuery();
		if (aTipoProv.equals("03")) {
			// ORDINANZA
			lSql += " WHERE IMP.DEP_OPID_DEPOSITO_ORDINANZA_PC = " + aIdProv;
		} else if (aTipoProv.equals("01")) {
			// SENTENZA
			lSql += " WHERE IMP.DEP_ID_DEPOSITO_SENTENZA = " + aIdProv;
		} else {
			// DECRETO
			lSql += " WHERE IMP.DEP_DEC_ID_DEPOSITO_DECRETO = " + aIdProv;
		}
		// lSql += " AND (IMP.DATA_ANNULLAMENTO IS NOT NULL)";

		if (aTipoImpugnazione != null) {
			String lTipo = " AND IMP.COD_TIPO_IMPUGNAZIONE in ( ";
			for (int i = 0; i < aTipoImpugnazione.length; i++) {
				if (i > 0)
					lTipo += ",";

				lTipo += "'" + aTipoImpugnazione[i] + "'";
			}
			lTipo += " ) ";

			lSql += lTipo;
		}

		lSql += " ORDER BY IMP.DATA_INSERIMENTO ASC ";

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT ID_IMPUGNAZIONE, ANNO_S7, PROGR_S7, COD_TIPO_IMPUGNAZIONE, TIPO_IMPUGNAZIONE.RV_MEANING DESCR_TIPO_IMPUGNAZIONE, "
				+ " SOGGETTO_IMPUGNANTE, SOGGETTO_IMPUGNANTE.RV_MEANING DESCR_SOGGETTO_IMPUGNANTE, DATA_RICORSO, DATA_ANNOTAZIONE, "
				+ " ANNOTAZIONE, DATA_ARRIVO_CANCELLERIA, DATA_TRASMISSIONE_ATTI, COD_AUTORITA_DESTINATARIA, "
				+ " AUTORITA_DESTINATARIA.RV_MEANING DESCR_AUTORITA_DESTINATARIA, DATA_DECISIONE, COD_TENORE_DECISIONE, "
				+ " TENORE_DECISIONE.RV_MEANING DESCR_TENORE_DECISIONE, DATA_RESTITUZIONE_ATTI, "
				+ " COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO, "
				+ " COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO, "
				+ " DEP_OPID_DEPOSITO_ORDINANZA_PC, DEP_DEC_ID_DEPOSITO_DECRETO, "
				+ " FLAG_ANNULLAMENTO, DATA_ANNULLAMENTO, MOTIVO_ANNULLAMENTO, FLAG_SOSP_ESEC, DEP_ID_DEPOSITO_SENTENZA, DESCRIZIONE_ALTRO ";

		lStatement += " FROM IMPUGNAZIONE IMP JOIN CG_REF_CODES TIPO_IMPUGNAZIONE ON (TIPO_IMPUGNAZIONE.RV_DOMAIN = 'TIPO_RICORSO' AND TIPO_IMPUGNAZIONE.RV_LOW_VALUE = IMP.COD_TIPO_IMPUGNAZIONE) ";
		lStatement += " JOIN CG_REF_CODES SOGGETTO_IMPUGNANTE ON (SOGGETTO_IMPUGNANTE.RV_DOMAIN ='SOGGETTO_IMPUGNANTE' AND SOGGETTO_IMPUGNANTE.RV_LOW_VALUE = IMP.SOGGETTO_IMPUGNANTE) ";
		lStatement += " JOIN CG_REF_CODES AUTORITA_DESTINATARIA ON (AUTORITA_DESTINATARIA.RV_DOMAIN = 'TIPO_UFFICIO' AND AUTORITA_DESTINATARIA.RV_LOW_VALUE = IMP.COD_AUTORITA_DESTINATARIA) ";
		lStatement += " JOIN CG_REF_CODES TENORE_DECISIONE ON (TENORE_DECISIONE.RV_DOMAIN = 'TENORE_DECISIONE_RICORSO' AND TENORE_DECISIONE.RV_LOW_VALUE = IMP.COD_TENORE_DECISIONE) ";

		return lStatement;
	}

	/**
   * 
   */
	public GenericModel getModel() throws DAOException {
		ImpugnazioneModel aModel = new ImpugnazioneModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdImpugnazione(getBigDecimal("ID_IMPUGNAZIONE"));
		aModel.setAnnoS7(getBigDecimal("ANNO_S7"));
		aModel.setProgrS7(getBigDecimal("PROGR_S7"));
		aModel.setCodTipoImpugnazione(getString("COD_TIPO_IMPUGNAZIONE"));
		aModel.setDescrTipoImpugnazione(getString("DESCR_TIPO_IMPUGNAZIONE"));
		aModel.setSoggettoImpugnante(getString("SOGGETTO_IMPUGNANTE"));
		aModel.setDescrSoggettoImpugnante(getString("DESCR_SOGGETTO_IMPUGNANTE"));
		aModel.setDataRicorso(getDate("DATA_RICORSO"));
		aModel.setDataAnnotazione(getDate("DATA_ANNOTAZIONE"));
		aModel.setAnnotazione(getString("ANNOTAZIONE"));
		aModel.setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA"));
		aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
		aModel.setCodAutoritaDestinataria(getString("COD_AUTORITA_DESTINATARIA"));
		aModel.setDescrAutoritaDestinataria(getString("DESCR_AUTORITA_DESTINATARIA"));
		aModel.setDataDecisione(getDate("DATA_DECISIONE"));
		aModel.setCodTenoreDecisione(getString("COD_TENORE_DECISIONE"));
		aModel.setDescrTenoreDecisione(getString("DESCR_TENORE_DECISIONE"));
		aModel.setDataRestituzioneAtti(getDate("DATA_RESTITUZIONE_ATTI"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setDescrUfficioInserimento("");
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setDescrUfficioAggiornamento("");
		aModel.setDepOpidDepositoOrdinanzaPc(getBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC"));
		aModel.setDepDecIdDepositoDecreto(getBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO"));
		aModel.setFlagAnnullamento(getString("FLAG_ANNULLAMENTO"));
		aModel.setDataAnnullamento(getDate("DATA_ANNULLAMENTO"));
		aModel.setMotivoAnnullamento(getString("MOTIVO_ANNULLAMENTO"));
		aModel.setFlagSospEsec(getString("FLAG_SOSP_ESEC")); // 30/04/2007
		aModel.setDepIdDepositoSentenza(getBigDecimal("DEP_ID_DEPOSITO_SENTENZA"));
		aModel.setDescrizioneAltro(getString("DESCRIZIONE_ALTRO"));
		return aModel;
	}

	/**
	 * 
	 * @param aModel
	 * @return
	 */
//	public String setCondizione(ImpugnazioneModel aModel) {
//		String lCondizioni = new String();
//		boolean lInserito = false;
//		return lCondizioni;
//	}

	public String setCondizioniByKey(BigDecimal aKey) {
		String lCondizioni = new String();

		lCondizioni += " WHERE ID_IMPUGNAZIONE = '" + aKey + "' ";
		return lCondizioni;
	}

	public String setCondizioniByIdDepositoDecreto(BigDecimal aKeyDepositoDecreto) {
		String lCondizioni = new String();

		lCondizioni += " WHERE DEP_DEC_ID_DEPOSITO_DECRETO = " + aKeyDepositoDecreto + " ";
		return lCondizioni;
	}

	public String setCondizioniByIdDepositoOrdinanza(BigDecimal aKeyDepositoOrdinanza) {
		String lCondizioni = new String();

		lCondizioni += " WHERE DEP_OPID_DEPOSITO_ORDINANZA_PC = " + aKeyDepositoOrdinanza + " ";
		return lCondizioni;
	}

	public String setCondizioniByIdDepositoSentenza(BigDecimal aKeyDepositoSentenza) {
		String lCondizioni = new String();

		lCondizioni += " WHERE DEP_ID_DEPOSITO_SENTENZA = " + aKeyDepositoSentenza + " ";
		return lCondizioni;
	}

	/**
	 * 
	 * @param aIdEvento
	 * @param aTipo
	 *            - 01 o 02 o 03
	 * @param aTipoImpugnazione
	 *            - String[] eventuali COD_TIPO_IMPUGNAZIONE
	 * @param aFlagAnnullate
	 *            - S solo annullate, N solo non annullate, else tutte
	 * @throws DAOException
	 */
	public void ricercaImpugnazioneByIdEventoTipoProvv(BigDecimal aIdEvento, String aTipo,
			String[] aTipoImpugnazione, String aFlagAnnullate) throws DAOException {
		String lSql = getSqlQueryPerJoin();
		lSql += " " + setCondizioniByIdEventoTipoProvv(aIdEvento, aTipo, aTipoImpugnazione, aFlagAnnullate);
		setStatement(lSql);
	}

	/**
	 * Identica a getSqlQuery() ma i cami della query hanno il prefisso IMP
	 * 
	 * @return
	 */
	protected String getSqlQueryPerJoin() {
		String lStatement = new String("");

		lStatement += " SELECT IMP.ID_IMPUGNAZIONE, IMP.ANNO_S7, IMP.PROGR_S7, IMP.COD_TIPO_IMPUGNAZIONE, "
				+ " TIPO_IMPUGNAZIONE.RV_MEANING DESCR_TIPO_IMPUGNAZIONE, IMP.SOGGETTO_IMPUGNANTE, "
				+ " SOGGETTO_IMPUGNANTE.RV_MEANING DESCR_SOGGETTO_IMPUGNANTE, IMP.DATA_RICORSO, IMP.DATA_ANNOTAZIONE, "
				+ " IMP.ANNOTAZIONE, IMP.DATA_ARRIVO_CANCELLERIA, IMP.DATA_TRASMISSIONE_ATTI, IMP.COD_AUTORITA_DESTINATARIA, "
				+ " AUTORITA_DESTINATARIA.RV_MEANING DESCR_AUTORITA_DESTINATARIA, IMP.DATA_DECISIONE, IMP.COD_TENORE_DECISIONE, "
				+ " TENORE_DECISIONE.RV_MEANING DESCR_TENORE_DECISIONE, IMP.DATA_RESTITUZIONE_ATTI, IMP.COD_OPERATORE_INSERIMENTO, "
				+ " IMP.DATA_INSERIMENTO, IMP.COD_UFFICIO_INSERIMENTO, IMP.COD_OPERATORE_AGGIORNAMENTO, IMP.DATA_AGGIORNAMENTO, "
				+ " IMP.COD_UFFICIO_AGGIORNAMENTO, IMP.DEP_OPID_DEPOSITO_ORDINANZA_PC, IMP.DEP_DEC_ID_DEPOSITO_DECRETO, "
				+ " IMP.FLAG_ANNULLAMENTO, IMP.DATA_ANNULLAMENTO, IMP.MOTIVO_ANNULLAMENTO, IMP.FLAG_SOSP_ESEC, IMP.DEP_ID_DEPOSITO_SENTENZA, "
				+ " IMP.DESCRIZIONE_ALTRO ";

		lStatement += " FROM IMPUGNAZIONE IMP JOIN CG_REF_CODES TIPO_IMPUGNAZIONE ON (TIPO_IMPUGNAZIONE.RV_DOMAIN = 'TIPO_RICORSO' AND TIPO_IMPUGNAZIONE.RV_LOW_VALUE = IMP.COD_TIPO_IMPUGNAZIONE) ";
		lStatement += " JOIN CG_REF_CODES SOGGETTO_IMPUGNANTE ON (SOGGETTO_IMPUGNANTE.RV_DOMAIN ='SOGGETTO_IMPUGNANTE' AND SOGGETTO_IMPUGNANTE.RV_LOW_VALUE = IMP.SOGGETTO_IMPUGNANTE) ";
		lStatement += " JOIN CG_REF_CODES AUTORITA_DESTINATARIA ON (AUTORITA_DESTINATARIA.RV_DOMAIN = 'TIPO_UFFICIO' AND AUTORITA_DESTINATARIA.RV_LOW_VALUE = IMP.COD_AUTORITA_DESTINATARIA) ";
		lStatement += " JOIN CG_REF_CODES TENORE_DECISIONE ON (TENORE_DECISIONE.RV_DOMAIN = 'TENORE_DECISIONE_RICORSO' AND TENORE_DECISIONE.RV_LOW_VALUE = IMP.COD_TENORE_DECISIONE) ";

		return lStatement;
	}

	/**
	 * Metodo che completa la getSqlQuery() per settare le condizioni di ricerca per IdEvento Vengono esclusi
	 * i record con data annullamento valorizzata IMP.DATA_ANNULLAMENTO IS NULL
	 * 
	 * @param aIdEvento
	 * @param aTipoEvento
	 * @param aTipoImpugnazione
	 *            eventuale filtro su COD_TIPO_IMPUGNAZIONE
	 * @param aFlagAnnullate
	 *            - S - N - NULL
	 * @return
	 */
	private String setCondizioniByIdEventoTipoProvv(BigDecimal aIdEvento, String aTipoEvento,
			String[] aTipoImpugnazione, String aFlagAnnullate) {
		String lCondizioni = new String();
		if (aTipoEvento.equals("03")) {
			lCondizioni += " , EVENTO EVE, DEPOSITO_ORDINANZA_PC DOP ";
			lCondizioni += "WHERE EVE.ID_EVENTO = '" + aIdEvento + "' ";
			lCondizioni += "AND EVE.ID_EVENTO = DOP.ID_EVENTO_GENERATO AND DOP.ID_DEPOSITO_ORDINANZA_PC = IMP.DEP_OPID_DEPOSITO_ORDINANZA_PC ";
		} else if (aTipoEvento.equals("01")) {
			lCondizioni += " , EVENTO EVE, DEPOSITO_SENTENZA DS ";
			lCondizioni += "WHERE EVE.ID_EVENTO = '" + aIdEvento + "' ";
			lCondizioni += "AND EVE.ID_EVENTO = DS.ID_EVENTO_GENERATO AND DS.ID_DEPOSITO_SENTENZA = IMP.DEP_ID_DEPOSITO_SENTENZA ";
		} else {
			lCondizioni += " , EVENTO EVE, DEPOSITO_DECRETO DD ";
			lCondizioni += "WHERE EVE.ID_EVENTO = '" + aIdEvento + "' ";
			lCondizioni += "AND EVE.ID_EVENTO = DD.ID_EVENTO_GENERATO AND DD.ID_DEPOSITO_DECRETO = IMP.DEP_DEC_ID_DEPOSITO_DECRETO ";
		}

		if (aTipoImpugnazione != null) {
			String lTipo = " AND COD_TIPO_IMPUGNAZIONE in ( ";
			for (int i = 0; i < aTipoImpugnazione.length; i++) {
				if (i > 0)
					lTipo += ",";
				lTipo += "'" + aTipoImpugnazione[i] + "'";
			}
			lTipo += " ) ";

			lCondizioni += lTipo;
		}

		if ("S".equals(aFlagAnnullate))
			lCondizioni += "AND (IMP.DATA_ANNULLAMENTO IS NOT NULL)";
		else if ("N".equals(aFlagAnnullate))
			lCondizioni += "AND (IMP.DATA_ANNULLAMENTO IS NULL)";

		lCondizioni += " ORDER BY IMP.DATA_INSERIMENTO ";

		return lCondizioni;
	}

	/**
	 * Calcola il Massimo Progressivo relativo ad un certo ufficio e all'anno in corso. Il massimo progressivo
	 * rappresenta anche l'ultimo progressivo inserito all'intenro dell'ufficio trattato.
	 * <p>
	 * 
	 * @param aImpugnazioneModel
	 *            istanza model del'Impugnazione.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void getProgressivoImpugnazione(ImpugnazioneModel aImpModel) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(PROGR_S7) aMAX";
		lStatement += " FROM IMPUGNAZIONE IMP";
		lStatement += " WHERE IMP.ANNO_S7 = " + aImpModel.getAnnoS7();
		lStatement += " AND IMP.COD_UFFICIO_INSERIMENTO = " + aImpModel.getCodUfficioInserimento();

		setStatement(lStatement);
	}

	public String getDataRicorso(BigDecimal aIdEve, String aTipo) throws DAOException {
		String retNum = " ";
		String lStatement = "";

		if (aTipo.equals("03")) {
			lStatement = "SELECT ID_DEPOSITO_ordinanza_pc,TO_CHAR(DATA_RICORSO,'dd-mm-yyyy') as DATA_RICORSO FROM DEPOSITO_ordinanza_pc LEFT OUTER JOIN IMPUGNAZIONE ON DEP_opID_DEPOSITO_ordinanza_pc=ID_DEPOSITO_ordinanza_pc WHERE ID_EVENTO_GENERATO = "
					+ aIdEve;
		}
		if (aTipo.equals("02")) {
			lStatement = "SELECT ID_DEPOSITO_DECRETO,TO_CHAR(DATA_RICORSO,'dd-mm-yyyy') as DATA_RICORSO FROM DEPOSITO_DECRETO LEFT OUTER JOIN IMPUGNAZIONE ON DEP_DEC_ID_DEPOSITO_DECRETO=ID_DEPOSITO_DECRETO WHERE ID_EVENTO_GENERATO = "
					+ aIdEve;
		}
		if (aTipo.equals("01")) {
			lStatement = "SELECT ID_DEPOSITO_SENTENZA,TO_CHAR(DATA_RICORSO,'dd-mm-yyyy') as DATA_RICORSO FROM DEPOSITO_SENTENZA LEFT OUTER JOIN IMPUGNAZIONE ON DEP_ID_DEPOSITO_SENTENZA=ID_DEPOSITO_SENTENZA WHERE ID_EVENTO_GENERATO = "
					+ aIdEve;
		}
		if (!lStatement.equals("")) {
			setStatement(lStatement);
			this.start();
			if (this.next()) {
				retNum = this.getString("DATA_RICORSO");
			}
		}
		return retNum;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public Collection getDateRicorsi(BigDecimal aIdEve, String aTipo) throws DAOException {
		Collection aDate = new Vector();
		String lStatement = "";

		if (aTipo.equals("03")) {
			lStatement = "SELECT ID_DEPOSITO_ordinanza_pc,TO_CHAR(DATA_RICORSO,'dd-mm-yyyy') as DATA_RICORSO FROM DEPOSITO_ordinanza_pc LEFT OUTER JOIN IMPUGNAZIONE ON DEP_opID_DEPOSITO_ordinanza_pc=ID_DEPOSITO_ordinanza_pc WHERE ID_EVENTO_GENERATO = "
					+ aIdEve;
		}
		if (aTipo.equals("02")) {
			lStatement = "SELECT ID_DEPOSITO_DECRETO,TO_CHAR(DATA_RICORSO,'dd-mm-yyyy') as DATA_RICORSO FROM DEPOSITO_DECRETO LEFT OUTER JOIN IMPUGNAZIONE ON DEP_DEC_ID_DEPOSITO_DECRETO=ID_DEPOSITO_DECRETO WHERE ID_EVENTO_GENERATO = "
					+ aIdEve;
		}
		if (aTipo.equals("01")) {
			lStatement = "SELECT ID_DEPOSITO_SENTENZA,TO_CHAR(DATA_RICORSO,'dd-mm-yyyy') as DATA_RICORSO FROM DEPOSITO_SENTENZA LEFT OUTER JOIN IMPUGNAZIONE ON DEP_ID_DEPOSITO_SENTENZA=ID_DEPOSITO_SENTENZA WHERE ID_EVENTO_GENERATO = "
					+ aIdEve;
		}
		if (!lStatement.equals("")) {
			setStatement(lStatement);
			this.start();
			while (this.next()) {
				String aDataRicorso = this.getString("DATA_RICORSO");
				if (aDataRicorso != null) {
					aDate.add(aDataRicorso);
				}
			}
		}
		return aDate;
	}

}