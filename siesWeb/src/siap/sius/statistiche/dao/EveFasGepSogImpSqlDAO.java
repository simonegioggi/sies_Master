package siap.sius.statistiche.dao;

import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.statistiche.action.ICostantiStatistiche;
import siap.sius.statistiche.model.EveFasGepSogModel;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaOrdinanzaModel;
import siap.sius.statistiche.model.RicercaProvvedimentoModel;

/**
 * <p>
 * Title: EveFasGepSogImpSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO estensione della EveFasGepSogSqlDAO che aggiunge alla JOIN di tabelle già
 * rappresentate dall'Ancestor anche la tabella IMPUGNAZIONE.
 * </p>
 * *
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */

public class EveFasGepSogImpSqlDAO extends EveFasGepSogSqlDAO {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public EveFasGepSogImpSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	/**
	 * Restituisce la parte dello statement Sql "Select .... FROM .."
	 */
	protected String getSqlQuery() {
		String lStatement = getSqlQuerySelectNoDocAll() + ", " + getSqlQueryImpugnazione()
				+ getSqlQueryFromJoinImpugnazione() + getSqlQueryJoinNoDocAll();

		return lStatement;
	}

	protected String getSqlQueryFromJoinImpugnazione() {
		String lStatement = new String("");

		lStatement += " FROM IMPUGNAZIONE IM left outer join DEPOSITO_DECRETO DD on (IM.DEP_DEC_ID_DEPOSITO_DECRETO = DD.ID_DEPOSITO_DECRETO)";

		lStatement += " left outer join DEPOSITO_ORDINANZA_PC DO on (IM.DEP_OPID_DEPOSITO_ORDINANZA_PC = DO.ID_DEPOSITO_ORDINANZA_PC)";
		lStatement += " left outer join DEPOSITO_SENTENZA DS on (IM.DEP_ID_DEPOSITO_SENTENZA = DS.ID_DEPOSITO_SENTENZA)";
		lStatement += " join EVENTO E on (DD.ID_EVENTO_GENERATO = E.ID_EVENTO OR DO.ID_EVENTO_GENERATO = E.ID_EVENTO OR DS.ID_EVENTO_GENERATO = E.ID_EVENTO)";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		EveFasGepSogModel lAncestorModel = (EveFasGepSogModel) super.getModelNoDocAll();
		EveFasGepSogProvModel lModel = new EveFasGepSogProvModel(lAncestorModel, getImpugnazioneModel());
		return lModel;
	};

	private ImpugnazioneModel getImpugnazioneModel() throws DAOException {
		// Lettura dei campi dalla tabella DEPOSITO_ORDINANZA_PC
		ImpugnazioneModel lImpugnazione = new ImpugnazioneModel();

		lImpugnazione.setIdImpugnazione(getBigDecimal("ID_IMPUGNAZIONE"));
		lImpugnazione.setAnnoS7(getBigDecimal("ANNO_S7"));
		lImpugnazione.setProgrS7(getBigDecimal("PROGR_S7"));

		if (findColumn("COD_TIPO_IMPUGNAZIONE")) {
			lImpugnazione.setCodTipoImpugnazione(getString("COD_TIPO_IMPUGNAZIONE"));
		}

		lImpugnazione.setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA"));
		lImpugnazione.setFlagAnnullamento(getString("FLAG_ANNULLAMENTO"));
		return lImpugnazione;
	}

	private String setOrderbyAnnoNum() {
		String lStatement = " ORDER BY  ANNO_S7 ASC, PROGR_S7 ASC";
		return lStatement;
	}

	/**
	 * Il metodo restituisce la parte dello statement di select che elenca i campi della tabella IMPUGNAZIONE.
	 *
	 * @return
	 */
	protected String getSqlQueryImpugnazione() {
		String lStatement = new String("");

		lStatement += " IM.ID_IMPUGNAZIONE, " + "IM.ANNO_S7, " + "IM.PROGR_S7, "
				+ "IM.COD_TIPO_IMPUGNAZIONE, " + "IM.DATA_ARRIVO_CANCELLERIA, " + "IM.FLAG_ANNULLAMENTO ";
		return lStatement;
	}

	// variabile flag utilizzata per comporre la condizione di filtro sulla select
	private boolean lInserito = false;

	/**
	 * Il metodo prepara le condizioni di ricerca nello statement in preparazione in base al contenuto del
	 * model di ricerca passato come argomento. La ricerca può essere di 2 tipi: per estremi dell'impugnazione
	 * espressi in intervallo di ANNOS7/PROGRS7; per intervallo di DATA_ARRIVO_CANCELLERIA dell'Ordinanza.
	 *
	 * @param aModel
	 * @return
	 */

	protected String setCondizioni(RicercaOrdinanzaModel aModel) {
		lInserito = false;
		// Condizione di WHERE resituita
		String lCondizioni = new String("");
		// Stringa di appoggio usata per la preparazione della singola condizione
		String lAppoggio;

		// Condizione sull'ufficio
		if (aModel.getCodUfficioInserimento() != null
				&& aModel.getCodUfficioInserimento().trim().length() > 0) {
			lAppoggio = " IM.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
			lCondizioni += setAND(lAppoggio);
		}

		// Tipo Impugnazione
		if (aModel.getCodTipoImpugnazione() != null && aModel.getCodTipoImpugnazione().trim().length() > 0) {
			lAppoggio = " IM.COD_TIPO_IMPUGNAZIONE = '" + aModel.getCodTipoImpugnazione() + "'";
			lCondizioni += setAND(lAppoggio);
		}

		// Condizioni sullo Stato di Validazione
		if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI)) {
			lAppoggio = " IM.FLAG_ANNULLAMENTO = 'S' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_ANNULLATI)) {
			lAppoggio = " (IM.FLAG_ANNULLAMENTO IS NULL OR IM.FLAG_ANNULLAMENTO <> 'S' )";
			lCondizioni += setAND(lAppoggio);
		}

		lCondizioni += setCondizioniIntervallo(aModel);

		// Infine la WHERE
		if (lInserito)
			lCondizioni = " WHERE " + lCondizioni;

		return lCondizioni;
	}

	protected String setCondizioniIntervallo(RicercaOrdinanzaModel aModel) {
		// Condizione resituita
		String lCondizioni = new String("");
		// Stringa di appoggio usata per la preparazione della singola condizione
		String lAppoggio;

		if (aModel.isTipoIntervalloRicercaXEstremiProvvedimento()) {
			if (aModel.getAnnoIniziale() != null && aModel.getNumIniziale() != null) {
				if (aModel.getAnnoFinale() != null && aModel.getNumFinale() != null) {
					// AnnoIniziale = AnnoFinale
					if (aModel.getAnnoIniziale().compareTo(aModel.getAnnoFinale()) == 0) {
						lAppoggio = " IM.ANNO_S7 = " + aModel.getAnnoIniziale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " IM.PROGR_S7 <= " + aModel.getNumFinale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " IM.PROGR_S7 >= " + aModel.getNumIniziale();
						lCondizioni += setAND(lAppoggio);
					} else {
						lAppoggio = " IM.ANNO_S7 > " + aModel.getAnnoIniziale();
						lAppoggio += " AND  IM.ANNO_S7 < " + aModel.getAnnoFinale();
						lAppoggio = "(" + lAppoggio + ")";
						lAppoggio += " OR (IM.ANNO_S7 = " + aModel.getAnnoFinale() + " AND"
								+ " IM.PROGR_S7 <= " + aModel.getNumFinale() + ")";
						lAppoggio += " OR (IM.ANNO_S7 = " + aModel.getAnnoIniziale() + " AND"
								+ " IM.PROGR_S7 >= " + aModel.getNumIniziale() + ")";
						lAppoggio = "(" + lAppoggio + ")";
						lCondizioni += setAND(lAppoggio);
					}
				} else {
					// Solo limite inferiore
					lAppoggio = " IM.ANNO_S7 >= " + aModel.getAnnoIniziale();
					lCondizioni += setAND(lAppoggio);
					lAppoggio = " IM.PROGR_S7 >= " + aModel.getNumIniziale();
				}
			}
		} else if (aModel.isRicercaXDateArrivoCancelleria()) {
			if (aModel.getDataArrivoInCancelleriaIniziale() != null) {
				lAppoggio = " TO_CHAR(IM.DATA_ARRIVO_CANCELLERIA,'YYYYMMDD') >='"
						+ DateUtils.getDateToString(aModel.getDataArrivoInCancelleriaIniziale(), "yyyyMMdd")
						+ "'";
				lCondizioni += setAND(lAppoggio);
			}
			if (aModel.getDataArrivoInCancelleriaFinale() != null) {
				lAppoggio = " TO_CHAR(IM.DATA_ARRIVO_CANCELLERIA,'YYYYMMDD') <='"
						+ DateUtils.getDateToString(aModel.getDataArrivoInCancelleriaFinale(), "yyyyMMdd")
						+ "'";
				lCondizioni += setAND(lAppoggio);
			}
		}

		return lCondizioni;
	}

	private String setAND(String aCondizioni) {
		if (lInserito)
			aCondizioni = " AND " + aCondizioni;

		lInserito = true;

		return aCondizioni;
	}

	/**
	 * Il metodo prepara lo statment sql che effettua la ricerca in base alle condizioni espresse dal model di
	 * ricerca passato come argomento.
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaProcSiusXProvvedimenti(RicercaOrdinanzaModel aModel) throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ricercaProcSiusXImpugnazione(): inizio ");

		String lStatement = getSqlQuery();

		lStatement += setCondizioni(aModel);
		lStatement += setOrderbyAnnoNum();

		// lStatement += setOrder();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ricercaProcSiusXImpugnazione(): fine ");

		setStatement(lStatement);
	}

	protected String getSqlQueryImpEve() {
		String lStatement = "SELECT " + getSqlQueryImpugnazione() + "," + getSqlQueryEvento()
				+ getSqlQueryFromJoinImpugnazione();

		return lStatement;
	}

	/**
	 * Valorizza le condizioni di filtro in base al contenuto del model ImpugnazioneModel passato.
	 *
	 * @param aModel
	 */
	public String setCondizioneImpEve(ImpugnazioneModel aModel) {
		String lCondizioni = new String("");
		String lAppoggio = new String("");
		lInserito = false;

		if (aModel != null) {
			if (aModel.getAnnoS7() != null)
				lAppoggio = " ANNO_S7 = " + aModel.getAnnoS7();
			lCondizioni += setAND(lAppoggio);

			if (aModel.getProgrS7() != null)
				lAppoggio = " PROGR_S7 = " + aModel.getProgrS7();
			lCondizioni += setAND(lAppoggio);

			if (aModel.getCodUfficioInserimento() != null
					&& aModel.getCodUfficioInserimento().trim().length() > 0)
				lAppoggio = " IM.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
			lCondizioni += setAND(lAppoggio);

			// Aggiunto IM su COD_UFFICIO_INSERIMENTO perchè sulle nuove versioni
			// Oracle rilancia nome colonna ambiguo
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Condizione -> " + lCondizioni);

			// Infine la WHERE
			if (lInserito)
				lCondizioni = " WHERE " + lCondizioni;

		}
		return lCondizioni;
	}

	/**
	 * Prepara lo statement per la query di ricerca Impugnazione in join con Evento.
	 *
	 * @param aModel
	 * @throws DAOException
	 */

	public void ricercaImpugnazioneEvento(ImpugnazioneModel aModel) throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ricercaImpugnazioneEvento(): inizio ");

		String lStatement = getSqlQueryImpEve();

		lStatement += setCondizioneImpEve(aModel);

		// lStatement += setOrder();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ricercaImpugnazioneEvento(): fine ");

		setStatement(lStatement);
	}

	/**
	 * Restituisce EveFasGepSogProvModel in cui vengono valorizzati l'Evento e l'Impugnazione.
	 *
	 * @return
	 * @throws DAOException
	 */

	public GenericModel getModelImpEve() throws DAOException {
		EveFasGepSogModel lAncestorModel = new EveFasGepSogModel(getEventoModel());

		EveFasGepSogProvModel lModel = new EveFasGepSogProvModel(lAncestorModel, getImpugnazioneModel());

		return lModel;
	};

	// private boolean findColumn(String aValue) {
	// try {
	// mRs.findColumn(aValue);
	// } catch (Exception sqex) {
	// return false;
	// }
	// return true;
	// }

	/**
	 * MEV10-s3: aggiunto metodo
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaProcSiusXProvvedimenti(RicercaProvvedimentoModel aModel) throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ricercaProcSiusXProvvedimenti(): inizio ");

		String lStatement = getSqlQuery();

		lStatement += setCondizioni(aModel);
		lStatement += setOrderbyAnnoNum();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ricercaProcSiusXProvvedimenti(): fine ");

		setStatement(lStatement);
	}

	/**
	 * MEV10-s3: aggiunto metodo
	 *
	 * @param aModel
	 * @return String
	 */
	private String setCondizioni(RicercaProvvedimentoModel aModel) {
		lInserito = false;
		// Condizione di WHERE resituita
		String lCondizioni = new String("");
		// Stringa di appoggio usata per la preparazione della singola condizione
		String lAppoggio;

		// Condizione sull'ufficio
		if (aModel.getCodUfficioInserimento() != null
				&& aModel.getCodUfficioInserimento().trim().length() > 0) {
			lAppoggio = " IM.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
			lCondizioni += setAND(lAppoggio);
		}

		// Tipo Impugnazione
		if (aModel.getCodTipoImpugnazione() != null && aModel.getCodTipoImpugnazione().trim().length() > 0) {
			lAppoggio = " IM.COD_TIPO_IMPUGNAZIONE = '" + aModel.getCodTipoImpugnazione() + "'";
			lCondizioni += setAND(lAppoggio);
		}

		// Condizioni sullo Stato di Validazione
		if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI)) {
			lAppoggio = " IM.FLAG_ANNULLAMENTO = 'S' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_ANNULLATI)) {
			lAppoggio = " (IM.FLAG_ANNULLAMENTO IS NULL OR IM.FLAG_ANNULLAMENTO <> 'S' )";
			lCondizioni += setAND(lAppoggio);
		}

		lCondizioni += setCondizioniIntervallo(aModel);

		// Infine la WHERE
		if (lInserito)
			lCondizioni = " WHERE " + lCondizioni;

		return lCondizioni;
	}

	/**
	 * MEV10-s3: aggiunto metodo
	 *
	 * @param aModel
	 * @return String
	 */
	private String setCondizioniIntervallo(RicercaProvvedimentoModel aModel) {
		// Condizione resituita
		String lCondizioni = new String("");
		// Stringa di appoggio usata per la preparazione della singola condizione
		String lAppoggio;

		if (aModel.isTipoIntervalloRicercaXEstremiProvvedimento()) {
			if (aModel.getAnnoIniziale() != null && aModel.getNumIniziale() != null) {
				if (aModel.getAnnoFinale() != null && aModel.getNumFinale() != null) {
					// AnnoIniziale = AnnoFinale
					if (aModel.getAnnoIniziale().compareTo(aModel.getAnnoFinale()) == 0) {
						lAppoggio = " IM.ANNO_S7 = " + aModel.getAnnoIniziale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " IM.PROGR_S7 <= " + aModel.getNumFinale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " IM.PROGR_S7 >= " + aModel.getNumIniziale();
						lCondizioni += setAND(lAppoggio);
					} else {
						lAppoggio = " IM.ANNO_S7 > " + aModel.getAnnoIniziale();
						lAppoggio += " AND  IM.ANNO_S7 < " + aModel.getAnnoFinale();
						lAppoggio = "(" + lAppoggio + ")";
						lAppoggio += " OR (IM.ANNO_S7 = " + aModel.getAnnoFinale() + " AND"
								+ " IM.PROGR_S7 <= " + aModel.getNumFinale() + ")";
						lAppoggio += " OR (IM.ANNO_S7 = " + aModel.getAnnoIniziale() + " AND"
								+ " IM.PROGR_S7 >= " + aModel.getNumIniziale() + ")";
						lAppoggio = "(" + lAppoggio + ")";
						lCondizioni += setAND(lAppoggio);
					}
				} else {
					// Solo limite inferiore
					lAppoggio = " IM.ANNO_S7 >= " + aModel.getAnnoIniziale();
					lCondizioni += setAND(lAppoggio);
					lAppoggio = " IM.PROGR_S7 >= " + aModel.getNumIniziale();
				}
			}
		} else if (aModel.isRicercaXDateArrivoCancelleria()) {
			if (aModel.getDataArrivoInCancelleriaIniziale() != null) {
				lAppoggio = " TO_CHAR(IM.DATA_ARRIVO_CANCELLERIA,'YYYYMMDD') >='"
						+ DateUtils.getDateToString(aModel.getDataArrivoInCancelleriaIniziale(), "yyyyMMdd")
						+ "'";
				lCondizioni += setAND(lAppoggio);
			}
			if (aModel.getDataArrivoInCancelleriaFinale() != null) {
				lAppoggio = " TO_CHAR(IM.DATA_ARRIVO_CANCELLERIA,'YYYYMMDD') <='"
						+ DateUtils.getDateToString(aModel.getDataArrivoInCancelleriaFinale(), "yyyyMMdd")
						+ "'";
				lCondizioni += setAND(lAppoggio);
			}
		}

		return lCondizioni;
	}

}