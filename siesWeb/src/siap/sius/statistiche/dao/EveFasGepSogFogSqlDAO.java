package siap.sius.statistiche.dao;

import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.sius.statistiche.action.ICostantiStatistiche;
import siap.sius.statistiche.model.EveFasGepSogModel;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaOrdinanzaModel;
import siap.sius.statistiche.model.RicercaProvvedimentoModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: EveFasGepSogFogSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO estensione della EveFasGepSogSqlDAO che gestisce la ricerca per
 * "Foglio Complementare", dato registrato nella tabella DOCUMENTO_ALLEGATO.
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

public class EveFasGepSogFogSqlDAO extends EveFasGepSogSqlDAO {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public EveFasGepSogFogSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	/**
	 * Restituisce la parte dello statement Sql "Select .... FROM .."
	 */
	protected String getSqlQuery() {
		String lStatement = getSqlQuerySelect() + getSqlQueryFromDocJoinEve()
				+ getSqlQueryJoinNoDocAll();

		return lStatement;
	}

	// STUB: forse non ce n'è bisogno!!
	public GenericModel getModel() throws DAOException {
		EveFasGepSogModel lAncestorModel = (EveFasGepSogModel) super.getModel();
		EveFasGepSogProvModel lModel = null;
		lModel = new EveFasGepSogProvModel(lAncestorModel);
		return lModel;
	}

	protected String getSqlQueryFromDocJoinEve() {
		String lStatement = new String("");

		lStatement += " FROM DOCUMENTO_ALLEGATO DA ";
		lStatement += " join EVENTO E on (DA.EVE_ID_EVENTO = E.ID_EVENTO)";

		return lStatement;
	}

	protected String getSqlQueryFromDocJoinLogTrasEsec() {
		String lStatement = new String("");

		lStatement += " join LOG_TRASFERIMENTO_ESECUZIONE LTE on (DA.EVE_ID_EVENTO = LTE.CHIAVE_SIES)";

		return lStatement;
	}

	protected String getSqlQueryFromDocJoinCodUnivMap() {
		String lStatement = new String("");

		lStatement += " ,CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO, CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO ";
		lStatement += " ,CG_REF_CODES DESCR_COD_PROCEDIMENTO, CODICI_UNIVOCI_MAPPATI CUM ";
		return lStatement;
	}

	private String setOrderbyAnnoNum() {
		String lStatement = " ORDER BY ANNO_FOGLIO_COMPLEMENTARE ASC, PROGR_FOGLIO_COMPLEMENTARE ASC";
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
		lInserito = false;

		// Condizione fissa per filtrare nella tabella DOCUMENTO_ALLEGATO
		// solo i Fogli Complementari.
		lCondizioni += setAND(" DA.COD_TIPO_DOCUMENTO = '06'");

		// Condizione sui Fogli Complementari (Trasmessi, Trasmessi con Errore,
		// Iscritti Manualmente, Da Trasmettere)
		if (aModel.getModalitaRicerca() != null) {
			if (aModel.getModalitaRicerca().equals(
					ICostantiStatistiche.RICERCA_FC_TRASMESSI)) {
				lAppoggio = " DA.DATA_TRASMISSIONE IS NOT NULL ";
				lCondizioni += setAND(lAppoggio);
			} else if (aModel.getModalitaRicerca().equals(
					ICostantiStatistiche.RICERCA_FC_TRASMESSI_CON_ERRORE)) {
				lAppoggio = " E.KEY_ESEC_NSC IS NULL AND LTE.CHIAVE_NSC = 0 AND DA.CODI_MOTIVAZIONE_NON_INVIO IS NULL ";
				lAppoggio += " AND LTE.DATA_OPERAZIONE = (SELECT MAX(DATA_OPERAZIONE) FROM LOG_TRASFERIMENTO_ESECUZIONE L WHERE L.CHIAVE_SIES = LTE.CHIAVE_SIES) ";
				lCondizioni += setAND(lAppoggio);
			} else if (aModel.getModalitaRicerca().equals(
					ICostantiStatistiche.RICERCA_FC_ISCRITTI_MANUALMENTE)) {
				lAppoggio = " DA.CODI_MOTIVAZIONE_NON_INVIO IS NOT NULL ";
				lCondizioni += setAND(lAppoggio);
			} else if (aModel.getModalitaRicerca().equals(
					ICostantiStatistiche.RICERCA_FC_DA_TRASMETTERE)) {
				lAppoggio = " E.KEY_ESEC_NSC IS NULL ";
				lAppoggio += " AND DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' ";
				lAppoggio += " AND E.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
				lAppoggio += " AND NVL(E.COD_MOTIVO, '-') = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' ";
				lAppoggio += " AND G.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND G.COD_OGGETTO_PROCEDIMENTO||E.COD_MOTIVO||E.COD_ESITO = CUM.ID_CONTENUTO||CUM.ID_OGGETTO||CUM.ID_ESITO ";
				lAppoggio += " AND E.ID_EVENTO NOT IN (SELECT LTE.CHIAVE_SIES FROM LOG_TRASFERIMENTO_ESECUZIONE LTE) ";
				lCondizioni += setAND(lAppoggio);
			}
		}

		// Condizione sull'ufficio
		if (aModel.getCodUfficioInserimento() != null
				&& aModel.getCodUfficioInserimento().trim().length() > 0) {
			lAppoggio = " DA.COD_UFFICIO_INSERIMENTO = '"
					+ aModel.getCodUfficioInserimento() + "'";
			lCondizioni += setAND(lAppoggio);
		}

		// Condizioni sullo Stato di Validazione

		if (aModel.getStatoValidazione().equalsIgnoreCase(
				ICostantiStatistiche.ANNULLATI)) {
			lAppoggio = " DA.FLAG_DOCUMENTO_REGISTRATO = 'A' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getStatoValidazione().equalsIgnoreCase(
				ICostantiStatistiche.NON_ANNULLATI)) {
			lAppoggio = " (DA.FLAG_DOCUMENTO_REGISTRATO IS NULL OR DA.FLAG_DOCUMENTO_REGISTRATO <> 'A' )";
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
			if (aModel.getAnnoIniziale() != null
					&& aModel.getNumIniziale() != null) {
				if (aModel.getAnnoFinale() != null
						&& aModel.getNumFinale() != null) {
					// AnnoIniziale = AnnoFinale
					if (aModel.getAnnoIniziale().compareTo(
							aModel.getAnnoFinale()) == 0) {
						lAppoggio = " DA.ANNO_FOGLIO_COMPLEMENTARE = "
								+ aModel.getAnnoIniziale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DA.PROGR_FOGLIO_COMPLEMENTARE <= "
								+ aModel.getNumFinale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DA.PROGR_FOGLIO_COMPLEMENTARE >= "
								+ aModel.getNumIniziale();
						lCondizioni += setAND(lAppoggio);
					} else {
						lAppoggio = " DA.ANNO_FOGLIO_COMPLEMENTARE > "
								+ aModel.getAnnoIniziale();
						lAppoggio += " AND  DA.ANNO_FOGLIO_COMPLEMENTARE < "
								+ aModel.getAnnoFinale();
						lAppoggio = "(" + lAppoggio + ")";
						lAppoggio += " OR (DA.ANNO_FOGLIO_COMPLEMENTARE = "
								+ aModel.getAnnoFinale() + " AND"
								+ " DA.PROGR_FOGLIO_COMPLEMENTARE <= "
								+ aModel.getNumFinale() + ")";
						lAppoggio += " OR (DA.ANNO_FOGLIO_COMPLEMENTARE = "
								+ aModel.getAnnoIniziale() + " AND"
								+ " DA.PROGR_FOGLIO_COMPLEMENTARE >= "
								+ aModel.getNumIniziale() + ")";
						lAppoggio = "(" + lAppoggio + ")";
						lCondizioni += setAND(lAppoggio);
					}
				} else {
					// Solo limite inferiore
					lAppoggio = " DA.ANNO_FOGLIO_COMPLEMENTARE >= "
							+ aModel.getAnnoIniziale();
					lCondizioni += setAND(lAppoggio);
					lAppoggio = " DA.PROGR_FOGLIO_COMPLEMENTARE >= "
							+ aModel.getNumIniziale();
				}
			}
		} else if (aModel.isRicercaXDateEmissione()) {
			if (aModel.getDataEmissioneIniziale() != null) {
				lAppoggio = " TO_CHAR(DA.DATA_EMISSIONE,'YYYYMMDD') >='"
						+ DateUtils.getDateToString(
								aModel.getDataEmissioneIniziale(), "yyyyMMdd") + "'";
				lCondizioni += setAND(lAppoggio);
			}
			if (aModel.getDataEmissioneFinale() != null) {
				lAppoggio = " TO_CHAR(DA.DATA_EMISSIONE,'YYYYMMDD') <='"
						+ DateUtils.getDateToString(aModel.getDataEmissioneFinale(), "yyyyMMdd") + "'";
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
	 * Il metodo prepara lo statment sql che effettua la ricerca in base alle
	 * condizioni espresse dal model di ricerca passato come argomento.
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaProcSiusXProvvedimenti(RicercaOrdinanzaModel aModel)
			throws DAOException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(
				"" + getClass().getName()
						+ " .ricercaProcSiusXImpugnazione(): inizio ");

		String lStatement = "";

		lStatement = getSqlQuery();

		if (aModel.getModalitaRicerca() != null
				&& aModel.getModalitaRicerca().equals(
						ICostantiStatistiche.RICERCA_FC_TRASMESSI_CON_ERRORE)) {
			lStatement += getSqlQueryFromDocJoinLogTrasEsec();
		}

		if (aModel.getModalitaRicerca() != null
				&& aModel.getModalitaRicerca().equals(
						ICostantiStatistiche.RICERCA_FC_DA_TRASMETTERE)) {
			lStatement += getSqlQueryFromDocJoinCodUnivMap();
		}

		lStatement += setCondizioni(aModel);
		lStatement += setOrderbyAnnoNum();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(
				"" + getClass().getName()
						+ " .ricercaProcSiusXImpugnazione(): fine ");

		setStatement(lStatement);
	}

	/**
	 * MEV10-s3: aggiunto metodo
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaProcSiusXProvvedimenti(RicercaProvvedimentoModel aModel) throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ricercaProcSiusXProvvedimenti(): inizio ");

		String lStatement = "";

		lStatement = getSqlQuery();

		if (aModel.getModalitaRicerca() != null
				&& aModel.getModalitaRicerca().equals(ICostantiStatistiche.RICERCA_FC_TRASMESSI_CON_ERRORE)) {
			lStatement += getSqlQueryFromDocJoinLogTrasEsec();
		}

		if (aModel.getModalitaRicerca() != null
				&& aModel.getModalitaRicerca().equals(ICostantiStatistiche.RICERCA_FC_DA_TRASMETTERE)) {
			lStatement += getSqlQueryFromDocJoinCodUnivMap();
		}

		lStatement += setCondizioni(aModel);
		lStatement += setOrderbyAnnoNum();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
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
		lInserito = false;

		// Condizione fissa per filtrare nella tabella DOCUMENTO_ALLEGATO
		// solo i Fogli Complementari.
		lCondizioni += setAND(" DA.COD_TIPO_DOCUMENTO = '06'");

		// Condizione sui Fogli Complementari (Trasmessi, Trasmessi con Errore, Iscritti Manualmente, Da
		// Trasmettere)
		if (aModel.getModalitaRicerca() != null) {
			if (aModel.getModalitaRicerca().equals(ICostantiStatistiche.RICERCA_FC_TRASMESSI)) {
				lAppoggio = " DA.DATA_TRASMISSIONE IS NOT NULL ";
				lCondizioni += setAND(lAppoggio);
			} else if (aModel.getModalitaRicerca().equals(
					ICostantiStatistiche.RICERCA_FC_TRASMESSI_CON_ERRORE)) {
				lAppoggio = " E.KEY_ESEC_NSC IS NULL AND LTE.CHIAVE_NSC = 0 AND DA.CODI_MOTIVAZIONE_NON_INVIO IS NULL ";
				lAppoggio += " AND LTE.DATA_OPERAZIONE = (SELECT MAX(DATA_OPERAZIONE) FROM LOG_TRASFERIMENTO_ESECUZIONE L WHERE L.CHIAVE_SIES = LTE.CHIAVE_SIES) ";
				lCondizioni += setAND(lAppoggio);
			} else if (aModel.getModalitaRicerca().equals(
					ICostantiStatistiche.RICERCA_FC_ISCRITTI_MANUALMENTE)) {
				lAppoggio = " DA.CODI_MOTIVAZIONE_NON_INVIO IS NOT NULL ";
				lCondizioni += setAND(lAppoggio);
			} else if (aModel.getModalitaRicerca().equals(ICostantiStatistiche.RICERCA_FC_DA_TRASMETTERE)) {
				lAppoggio = " E.KEY_ESEC_NSC IS NULL ";
				lAppoggio += " AND DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' ";
				lAppoggio += " AND E.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
				lAppoggio += " AND NVL(E.COD_MOTIVO, '-') = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' ";
				lAppoggio += " AND G.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND G.COD_OGGETTO_PROCEDIMENTO||E.COD_MOTIVO||E.COD_ESITO = CUM.ID_CONTENUTO||CUM.ID_OGGETTO||CUM.ID_ESITO ";
				lAppoggio += " AND E.ID_EVENTO NOT IN (SELECT LTE.CHIAVE_SIES FROM LOG_TRASFERIMENTO_ESECUZIONE LTE) ";
				lCondizioni += setAND(lAppoggio);
			}
		}

		// Condizione sull'ufficio
		if (aModel.getCodUfficioInserimento() != null
				&& aModel.getCodUfficioInserimento().trim().length() > 0) {
			lAppoggio = " DA.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
			lCondizioni += setAND(lAppoggio);
		}

		// Condizioni sullo Stato di Validazione

		if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI)) {
			lAppoggio = " DA.FLAG_DOCUMENTO_REGISTRATO = 'A' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_ANNULLATI)) {
			lAppoggio = " (DA.FLAG_DOCUMENTO_REGISTRATO IS NULL OR DA.FLAG_DOCUMENTO_REGISTRATO <> 'A' )";
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
						lAppoggio = " DA.ANNO_FOGLIO_COMPLEMENTARE = " + aModel.getAnnoIniziale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DA.PROGR_FOGLIO_COMPLEMENTARE <= " + aModel.getNumFinale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DA.PROGR_FOGLIO_COMPLEMENTARE >= " + aModel.getNumIniziale();
						lCondizioni += setAND(lAppoggio);
					} else {
						lAppoggio = " DA.ANNO_FOGLIO_COMPLEMENTARE > " + aModel.getAnnoIniziale();
						lAppoggio += " AND  DA.ANNO_FOGLIO_COMPLEMENTARE < " + aModel.getAnnoFinale();
						lAppoggio = "(" + lAppoggio + ")";
						lAppoggio += " OR (DA.ANNO_FOGLIO_COMPLEMENTARE = " + aModel.getAnnoFinale() + " AND"
								+ " DA.PROGR_FOGLIO_COMPLEMENTARE <= " + aModel.getNumFinale() + ")";
						lAppoggio += " OR (DA.ANNO_FOGLIO_COMPLEMENTARE = " + aModel.getAnnoIniziale()
								+ " AND" + " DA.PROGR_FOGLIO_COMPLEMENTARE >= " + aModel.getNumIniziale()
								+ ")";
						lAppoggio = "(" + lAppoggio + ")";
						lCondizioni += setAND(lAppoggio);
					}
				} else {
					// Solo limite inferiore
					lAppoggio = " DA.ANNO_FOGLIO_COMPLEMENTARE >= " + aModel.getAnnoIniziale();
					lCondizioni += setAND(lAppoggio);
					lAppoggio = " DA.PROGR_FOGLIO_COMPLEMENTARE >= " + aModel.getNumIniziale();
				}
			}
		} else if (aModel.isRicercaXDateEmissione()) {
			if (aModel.getDataEmissioneIniziale() != null) {
				lAppoggio = " TO_CHAR(DA.DATA_EMISSIONE,'YYYYMMDD') >='"
						+ DateUtils.getDateToString(aModel.getDataEmissioneIniziale(), "yyyyMMdd") + "'";
				lCondizioni += setAND(lAppoggio);
			}
			if (aModel.getDataEmissioneFinale() != null) {
				lAppoggio = " TO_CHAR(DA.DATA_EMISSIONE,'YYYYMMDD') <='"
						+ DateUtils.getDateToString(aModel.getDataEmissioneFinale(), "yyyyMMdd") + "'";
				lCondizioni += setAND(lAppoggio);
			}
		}

		return lCondizioni;
	}

}