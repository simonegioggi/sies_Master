package siap.sius.statistiche.dao;

import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
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
 * Title: EveFasGepSogSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO estensione della EveFasGepSogSqlDAO che aggiunge alla JOIN di tabelle già
 * rappresentate dall'Ancestor anche la tabella DEPOSITO_ORDINANZA_PC.
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

public class EveFasGepSogOrdSqlDAO extends EveFasGepSogSqlDAO {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public EveFasGepSogOrdSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	/**
	 * Restituisce la parte dello statement Sql "Select .... FROM .."
	 */
	protected String getSqlQuery() {
		String lStatement = getSqlQuerySelect() + ", " + getSqlQueryDepositoOrdinanzaPc() + ", "
				+ getSqlQueryEsperto() + getSqlQueryFromJoinDepOrd() + getSqlQueryJoin("02");

		return lStatement;
	}

	protected String getSqlQueryFromJoinDepOrd() {
		String lStatement = new String("");

		lStatement += " FROM DEPOSITO_ORDINANZA_PC DO join EVENTO E"
				+ " on (DO.ID_EVENTO_GENERATO = E.ID_EVENTO)";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {

		EveFasGepSogModel lAncestorModel = (EveFasGepSogModel) super.getModel();
		EveFasGepSogProvModel lModel = new EveFasGepSogProvModel(lAncestorModel,
				getDepositoOrdinanzaPcModel());
		return lModel;
	};

	private DepositoOrdinanzaPcModel getDepositoOrdinanzaPcModel() throws DAOException {
		// Lettura dei campi dalla tabella DEPOSITO_ORDINANZA_PC

		DepositoOrdinanzaPcModel lOrdinanza = new DepositoOrdinanzaPcModel();
		lOrdinanza.setIdDepositoOrdinanzaPc(getBigDecimal("ID_DEPOSITO_ORDINANZA_PC"));
		lOrdinanza.setAnnoS3(getBigDecimal("ANNO_S3"));
		lOrdinanza.setNumS3(getBigDecimal("NUM_S3"));
		lOrdinanza.setDataDeposito(getDate("DATA_DEPOSITO"));
		lOrdinanza.setCodTipoControlloEsecuzione(getString("TIPO_CONTROLLO_ESECUZIONE"));

		return lOrdinanza;
	}

	/**
	 * Il metodo restituisce la parte dello statement di select che elenca i campi della tabella
	 * DEPOSITO_ORDINANZA_PC.
	 * 
	 * @return
	 */
	protected String getSqlQueryDepositoOrdinanzaPc() {
		String lStatement = new String("");

		lStatement += " DO.ID_DEPOSITO_ORDINANZA_PC, " + "DO.COD_MAGISTRATO, " + "DO.ANNO_S3, "
				+ "DO.NUM_S3, " + "DO.DATA_DEPOSITO, DO.TIPO_CONTROLLO_ESECUZIONE ";
		return lStatement;
	}

	private String setOrderbyAnnoNum() {
		String lStatement = " ORDER BY ANNO_S3 ASC, NUM_S3 ASC";
		return lStatement;
	}

	// variabile flag utilizzata per comporre la condizione di filtro sulla select
	private boolean lInserito = false;

	/**
	 * Il metodo prepara le condizioni di ricerca nello statement in preparazione in base al contenuto del
	 * model di ricerca passato come argomento. La ricerca può essere di 2 tipi: per estremi dell'ordinanza
	 * espressi in intervallo di ANNOS3/NUMS3; per intervallo di DATA_DEPOSITO dell'Ordinanza.
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

		// Condizione Ricerca Ordinanze Prive di Foglio Complementare
		if (aModel.getModalitaRicerca() != null) {
			if (aModel.getModalitaRicerca().equals(ICostantiStatistiche.RICERCA_ORDINANZE_PRIVE_DI_FC)) {
				lAppoggio = " DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' ";
				lAppoggio += " AND E.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
				lAppoggio += " AND NVL(E.COD_MOTIVO, '-') = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' ";
				lAppoggio += " AND G.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND G.COD_OGGETTO_PROCEDIMENTO||E.COD_MOTIVO||E.COD_ESITO = CUM.ID_CONTENUTO||CUM.ID_OGGETTO||CUM.ID_ESITO ";
				lAppoggio += " AND E.ID_EVENTO IN (SELECT DISTINCT DAA.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO DAA ";
				lAppoggio += "                     MINUS ";
				lAppoggio += "                     SELECT DISTINCT DAAA.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO DAAA ";
				lAppoggio += "                     WHERE DAAA.COD_TIPO_DOCUMENTO = '06')";
				lCondizioni += setAND(lAppoggio);
			}
		}

		// Condizione sull'ufficio
		if (aModel.getCodUfficioInserimento() != null
				&& aModel.getCodUfficioInserimento().trim().length() > 0) {
			lAppoggio = " DO.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
			lCondizioni += setAND(lAppoggio);
		}

		// Condizione sul magistrato
		if (aModel.getCodMagistrato() != null && aModel.getCodMagistrato().length() > 0
				&& aModel.getCodMagistrato().compareTo("-") != 0) {
			if (aModel.getCodMagistrato().compareTo("Tutti") != 0
					&& aModel.getCodMagistrato().compareTo("Nessuno") != 0) {
				lAppoggio = " DO.COD_MAGISTRATO = '" + aModel.getCodMagistrato() + "'";
				lCondizioni += setAND(lAppoggio);
			} else {
				if (aModel.getCodMagistrato().compareTo("Tutti") == 0) {
					lAppoggio = " DO.COD_MAGISTRATO IS NOT NULL ";
					lCondizioni += setAND(lAppoggio);
				}
				if (aModel.getCodMagistrato().compareTo("Nessuno") == 0) {
					lAppoggio = " DO.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO IS NULL AND MR.MAG_COD_MAGISTRATO IS NULL ";
					lCondizioni += setAND(lAppoggio);
				}
			}

		} else if (aModel.getCodEsperto() != null && aModel.getCodEsperto().intValue() >= 0) {
			if (aModel.getCodEsperto().intValue() == 9999) {
				lAppoggio = " DO.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO IS NOT NULL ";
				lCondizioni += setAND(lAppoggio);
				lAppoggio = " E.DATA_EMISSIONE >= (MR.DATA_INIZIO - 1) ";
				lCondizioni += setAND(lAppoggio);
				lAppoggio = "  (MR.DATA_FINE IS NULL OR (MR.DATA_FINE -1) > E.DATA_EMISSIONE) ";
				lCondizioni += setAND(lAppoggio);
			} else {
				lAppoggio = " DO.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO = " + aModel.getCodEsperto();
				lCondizioni += setAND(lAppoggio);
				lAppoggio = " E.DATA_EMISSIONE >= (MR.DATA_INIZIO - 1) ";
				lCondizioni += setAND(lAppoggio);
				lAppoggio = "  (MR.DATA_FINE IS NULL OR (MR.DATA_FINE -1) > E.DATA_EMISSIONE) ";
				lCondizioni += setAND(lAppoggio);
			}
		}

		// Condizioni sullo Stato di Validazione
		if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI)) {
			lAppoggio = " E.FLAG_DOCUMENTO_REGISTRATO = 'A' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.VALIDATI)) {
			lAppoggio = "E.FLAG_DOCUMENTO_REGISTRATO <> 'A' AND DA.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_VALIDATI)) {
			lAppoggio = "E.FLAG_DOCUMENTO_REGISTRATO <> 'A' AND  DA.FLAG_DOCUMENTO_REGISTRATO <> 'S' ";
			lCondizioni += setAND(lAppoggio);
		}

		lCondizioni += setCondizioniOrdinanza(aModel);

		// Infine la WHERE
		if (lInserito)
			lCondizioni = " WHERE " + lCondizioni;

		return lCondizioni;
	}

	protected String setCondizioniOrdinanza(RicercaOrdinanzaModel aModel) {
		// Condizione resituita
		String lCondizioni = new String("");
		// Stringa di appoggio usata per la preparazione della singola condizione
		String lAppoggio;

		if (aModel.isTipoIntervalloRicercaXEstremiProvvedimento()) {
			if (aModel.getAnnoIniziale() != null && aModel.getNumIniziale() != null) {
				if (aModel.getAnnoFinale() != null && aModel.getNumFinale() != null) {
					// AnnoIniziale = AnnoFinale
					if (aModel.getAnnoIniziale().compareTo(aModel.getAnnoFinale()) == 0) {
						lAppoggio = " DO.ANNO_S3 = " + aModel.getAnnoIniziale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DO.NUM_S3 <= " + aModel.getNumFinale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DO.NUM_S3 >= " + aModel.getNumIniziale();
						lCondizioni += setAND(lAppoggio);
					} else {
						lAppoggio = " DO.ANNO_S3 > " + aModel.getAnnoIniziale();
						lAppoggio += " AND DO.ANNO_S3 < " + aModel.getAnnoFinale();
						lAppoggio = "(" + lAppoggio + ")";
						lAppoggio += " OR (DO.ANNO_S3 = " + aModel.getAnnoFinale() + " AND"
								+ " DO.NUM_S3 <= " + aModel.getNumFinale() + ")";
						lAppoggio += " OR (DO.ANNO_S3 = " + aModel.getAnnoIniziale() + " AND"
								+ " DO.NUM_S3 >= " + aModel.getNumIniziale() + ")";
						lAppoggio = "(" + lAppoggio + ")";
						lCondizioni += setAND(lAppoggio);
					}
				} else {
					// Solo limite inferiore
					lAppoggio = " DO.ANNO_S3 >= " + aModel.getAnnoIniziale();
					lCondizioni += setAND(lAppoggio);
					lAppoggio = " DO.NUM_S3 >= " + aModel.getNumIniziale();
				}
			}
		} else if (aModel.isRicercaXDateDeposito()) {
			if (aModel.getDataDepositoIniziale() != null) {
				lAppoggio = " TO_CHAR(DO.DATA_DEPOSITO,'YYYYMMDD') >='"
						+ DateUtils.getDateToString(aModel.getDataDepositoIniziale(), "yyyyMMdd") + "'";
				lCondizioni += setAND(lAppoggio);
			}
			if (aModel.getDataDepositoFinale() != null) {
				lAppoggio = " TO_CHAR(DO.DATA_DEPOSITO,'YYYYMMDD') <='"
						+ DateUtils.getDateToString(aModel.getDataDepositoFinale(), "yyyyMMdd") + "'";
				lCondizioni += setAND(lAppoggio);
			}
		}

		// tipo controllo esecuzione
		if (aModel.getTipiControlliEsecuzione() != null) {
			if (aModel.getTipiControlliEsecuzione().length > 1
					&& aModel.getTipiControlliEsecuzione()[0] != null
					&& aModel.getTipiControlliEsecuzione()[1] != null) {
				lAppoggio = "( DO.TIPO_CONTROLLO_ESECUZIONE = 'E' OR DO.TIPO_CONTROLLO_ESECUZIONE = 'T' ) ";
				lCondizioni += setAND(lAppoggio);
			} else if (aModel.getTipiControlliEsecuzione().length == 1) {
				lAppoggio = " DO.TIPO_CONTROLLO_ESECUZIONE = '" + aModel.getTipiControlliEsecuzione()[0]
						+ "' ";
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

	protected String getSqlQueryFromDocJoinCodUnivMap() {
		String lStatement = new String("");

		lStatement += " ,CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO, CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO ";
		lStatement += " ,CG_REF_CODES DESCR_COD_PROCEDIMENTO, CODICI_UNIVOCI_MAPPATI CUM ";
		return lStatement;
	}

	/**
	 * Il metodo prepara lo statment sql che effettua la ricerca in base alle condizioni espresse dal model di
	 * ricerca passato come argomento.
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaProcSiusXProvvedimenti(RicercaOrdinanzaModel aModel) throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ricercaProcSiusXProvvedimenti(): inizio ");

		String lStatement = getSqlQuery();

		// Ricerca Ordinanze Prive di Foglio Complementare
		if (aModel.getModalitaRicerca() != null
				&& aModel.getModalitaRicerca().equals(ICostantiStatistiche.RICERCA_ORDINANZE_PRIVE_DI_FC)) {
			lStatement += getSqlQueryFromDocJoinCodUnivMap();
		}

		lStatement += setCondizioni(aModel);
		lStatement += setOrderbyAnnoNum();
		// lStatement += setOrdinamento(aModel);

		// lStatement += setOrder();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ricercaProcSiusXProvvedimenti(): fine ");

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

		String lStatement = getSqlQuery();

		// Ricerca Provvedimenti Privi di Foglio Complementare
		if (aModel.getModalitaRicerca() != null
				&& aModel.getModalitaRicerca().equals(ICostantiStatistiche.RICERCA_PROVVEDIMENTI_PRIVI_DI_FC)) {
			lStatement += getSqlQueryFromDocJoinCodUnivMap();
		}

		lStatement += setCondizioni(aModel);
		lStatement += " UNION ";

		// MEV10-s3:
		lStatement += getSqlQuerySent();

		// Ricerca Provvedimenti Privi di Foglio Complementare
		if (aModel.getModalitaRicerca() != null
				&& aModel.getModalitaRicerca().equals(ICostantiStatistiche.RICERCA_PROVVEDIMENTI_PRIVI_DI_FC)) {
			lStatement += getSqlQueryFromDocJoinCodUnivMap();
		}

		lStatement += setCondizioniSent(aModel);

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

		// Condizione Ricerca Provvedimenti Prive di Foglio Complementare
		if (aModel.getModalitaRicerca() != null) {
			if (aModel.getModalitaRicerca().equals(ICostantiStatistiche.RICERCA_PROVVEDIMENTI_PRIVI_DI_FC)) {
				lAppoggio = " DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' ";
				lAppoggio += " AND E.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
				lAppoggio += " AND NVL(E.COD_MOTIVO, '-') = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' ";
				lAppoggio += " AND G.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND G.COD_OGGETTO_PROCEDIMENTO||E.COD_MOTIVO||E.COD_ESITO = CUM.ID_CONTENUTO||CUM.ID_OGGETTO||CUM.ID_ESITO ";
				lAppoggio += " AND E.ID_EVENTO IN (SELECT DISTINCT DAA.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO DAA ";
				lAppoggio += "                     MINUS ";
				lAppoggio += "                     SELECT DISTINCT DAAA.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO DAAA ";
				lAppoggio += "                     WHERE DAAA.COD_TIPO_DOCUMENTO = '06')";
				lCondizioni += setAND(lAppoggio);
			}
		}

		// Condizione sull'ufficio
		if (aModel.getCodUfficioInserimento() != null
				&& aModel.getCodUfficioInserimento().trim().length() > 0) {
			lAppoggio = " DO.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
			lCondizioni += setAND(lAppoggio);
		}

		// Condizione sul magistrato
		if (aModel.getCodMagistrato() != null && aModel.getCodMagistrato().length() > 0
				&& aModel.getCodMagistrato().compareTo("-") != 0) {
			if (aModel.getCodMagistrato().compareTo("Tutti") != 0
					&& aModel.getCodMagistrato().compareTo("Nessuno") != 0) {
				lAppoggio = " DO.COD_MAGISTRATO = '" + aModel.getCodMagistrato() + "'";
				lCondizioni += setAND(lAppoggio);
			} else {
				if (aModel.getCodMagistrato().compareTo("Tutti") == 0) {
					lAppoggio = " DO.COD_MAGISTRATO IS NOT NULL ";
					lCondizioni += setAND(lAppoggio);
				}
				if (aModel.getCodMagistrato().compareTo("Nessuno") == 0) {
					lAppoggio = " DO.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO IS NULL AND MR.MAG_COD_MAGISTRATO IS NULL ";
					lCondizioni += setAND(lAppoggio);
				}
			}

		} else if (aModel.getCodEsperto() != null && aModel.getCodEsperto().intValue() >= 0) {
			if (aModel.getCodEsperto().intValue() == 9999) {
				lAppoggio = " DO.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO IS NOT NULL ";
				lCondizioni += setAND(lAppoggio);
				lAppoggio = " E.DATA_EMISSIONE >= (MR.DATA_INIZIO - 1) ";
				lCondizioni += setAND(lAppoggio);
				lAppoggio = "  (MR.DATA_FINE IS NULL OR (MR.DATA_FINE -1) > E.DATA_EMISSIONE) ";
				lCondizioni += setAND(lAppoggio);
			} else {
				lAppoggio = " DO.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO = " + aModel.getCodEsperto();
				lCondizioni += setAND(lAppoggio);
				lAppoggio = " E.DATA_EMISSIONE >= (MR.DATA_INIZIO - 1) ";
				lCondizioni += setAND(lAppoggio);
				lAppoggio = "  (MR.DATA_FINE IS NULL OR (MR.DATA_FINE -1) > E.DATA_EMISSIONE) ";
				lCondizioni += setAND(lAppoggio);
			}
		}

		// Condizioni sullo Stato di Validazione
		if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI)) {
			lAppoggio = " E.FLAG_DOCUMENTO_REGISTRATO = 'A' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.VALIDATI)) {
			lAppoggio = "E.FLAG_DOCUMENTO_REGISTRATO <> 'A' AND DA.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_VALIDATI)) {
			lAppoggio = "E.FLAG_DOCUMENTO_REGISTRATO <> 'A' AND  DA.FLAG_DOCUMENTO_REGISTRATO <> 'S' ";
			lCondizioni += setAND(lAppoggio);
		}

		lCondizioni += setCondizioniProvvedimento(aModel);

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
	private String setCondizioniProvvedimento(RicercaProvvedimentoModel aModel) {
		// Condizione resituita
		String lCondizioni = new String("");
		// Stringa di appoggio usata per la preparazione della singola condizione
		String lAppoggio;

		if (aModel.isTipoIntervalloRicercaXEstremiProvvedimento()) {
			if (aModel.getAnnoIniziale() != null && aModel.getNumIniziale() != null) {
				if (aModel.getAnnoFinale() != null && aModel.getNumFinale() != null) {
					// AnnoIniziale = AnnoFinale
					if (aModel.getAnnoIniziale().compareTo(aModel.getAnnoFinale()) == 0) {
						lAppoggio = " DO.ANNO_S3 = " + aModel.getAnnoIniziale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DO.NUM_S3 <= " + aModel.getNumFinale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DO.NUM_S3 >= " + aModel.getNumIniziale();
						lCondizioni += setAND(lAppoggio);
					} else {
						lAppoggio = " DO.ANNO_S3 > " + aModel.getAnnoIniziale();
						lAppoggio += " AND DO.ANNO_S3 < " + aModel.getAnnoFinale();
						lAppoggio = "(" + lAppoggio + ")";
						lAppoggio += " OR (DO.ANNO_S3 = " + aModel.getAnnoFinale() + " AND"
								+ " DO.NUM_S3 <= " + aModel.getNumFinale() + ")";
						lAppoggio += " OR (DO.ANNO_S3 = " + aModel.getAnnoIniziale() + " AND"
								+ " DO.NUM_S3 >= " + aModel.getNumIniziale() + ")";
						lAppoggio = "(" + lAppoggio + ")";
						lCondizioni += setAND(lAppoggio);
					}
				} else {
					// Solo limite inferiore
					lAppoggio = " DO.ANNO_S3 >= " + aModel.getAnnoIniziale();
					lCondizioni += setAND(lAppoggio);
					lAppoggio = " DO.NUM_S3 >= " + aModel.getNumIniziale();
				}
			}
		} else if (aModel.isRicercaXDateDeposito()) {
			if (aModel.getDataDepositoIniziale() != null) {
				lAppoggio = " TO_CHAR(DO.DATA_DEPOSITO,'YYYYMMDD') >='"
						+ DateUtils.getDateToString(aModel.getDataDepositoIniziale(), "yyyyMMdd") + "'";
				lCondizioni += setAND(lAppoggio);
			}
			if (aModel.getDataDepositoFinale() != null) {
				lAppoggio = " TO_CHAR(DO.DATA_DEPOSITO,'YYYYMMDD') <='"
						+ DateUtils.getDateToString(aModel.getDataDepositoFinale(), "yyyyMMdd") + "'";
				lCondizioni += setAND(lAppoggio);
			}
		}

		// tipo controllo esecuzione
		if (aModel.getTipiControlliEsecuzione() != null) {
			if (aModel.getTipiControlliEsecuzione().length > 1
					&& aModel.getTipiControlliEsecuzione()[0] != null
					&& aModel.getTipiControlliEsecuzione()[1] != null) {
				lAppoggio = "( DO.TIPO_CONTROLLO_ESECUZIONE = 'E' OR DO.TIPO_CONTROLLO_ESECUZIONE = 'T' ) ";
				lCondizioni += setAND(lAppoggio);
			} else if (aModel.getTipiControlliEsecuzione().length == 1) {
				lAppoggio = " DO.TIPO_CONTROLLO_ESECUZIONE = '" + aModel.getTipiControlliEsecuzione()[0]
						+ "' ";
				lCondizioni += setAND(lAppoggio);
			}
		}

		return lCondizioni;
	}

	/**
	 * MEV10-s3: aggiunto metodo
	 * 
	 * @return String
	 */
	protected String getSqlQuerySent() {
		String lStatement = getSqlQuerySelect() + ", " + getSqlQueryDepositoSentenza() + ", "
				+ getSqlQueryEsperto() + getSqlQuerySentFromJoinDepOrd() + getSqlQueryJoin("01");

		return lStatement;
	}

	/**
	 * MEV10-s3: aggiunto metodo
	 * 
	 * @return String
	 */
	protected String getSqlQueryDepositoSentenza() {
		String lStatement = new String("");

		lStatement += " DO.ID_DEPOSITO_SENTENZA, " + "DO.COD_MAGISTRATO, " + "DO.ANNO_SENTENZA, "
				+ "DO.NUM_SENTENZA, " + "DO.DATA_DEPOSITO, NULL ";
		return lStatement;
	}

	/**
	 * MEV10-s3: aggiunto metodo
	 * 
	 * @param aModel
	 * @return String
	 */
	private String setCondizioniSent(RicercaProvvedimentoModel aModel) {
		lInserito = false;

		// Condizione di WHERE resituita
		String lCondizioni = new String("");
		// Stringa di appoggio usata per la preparazione della singola condizione
		String lAppoggio;

		// Condizione Ricerca Provvedimenti Privi di Foglio Complementare
		if (aModel.getModalitaRicerca() != null) {
			if (aModel.getModalitaRicerca().equals(ICostantiStatistiche.RICERCA_PROVVEDIMENTI_PRIVI_DI_FC)) {
				lAppoggio = " DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' ";
				lAppoggio += " AND E.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
				lAppoggio += " AND NVL(E.COD_MOTIVO, '-') = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' ";
				lAppoggio += " AND G.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
				lAppoggio += " AND G.COD_OGGETTO_PROCEDIMENTO||E.COD_MOTIVO||E.COD_ESITO = CUM.ID_CONTENUTO||CUM.ID_OGGETTO||CUM.ID_ESITO ";
				lAppoggio += " AND E.ID_EVENTO IN (SELECT DISTINCT DAA.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO DAA ";
				lAppoggio += "                     MINUS ";
				lAppoggio += "                     SELECT DISTINCT DAAA.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO DAAA ";
				lAppoggio += "                     WHERE DAAA.COD_TIPO_DOCUMENTO = '06')";
				lCondizioni += setAND(lAppoggio);
			}
		}

		// Condizione sull'ufficio
		if (aModel.getCodUfficioInserimento() != null
				&& aModel.getCodUfficioInserimento().trim().length() > 0) {
			lAppoggio = " DO.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
			lCondizioni += setAND(lAppoggio);
		}

		// Condizione sul magistrato
		if (aModel.getCodMagistrato() != null && aModel.getCodMagistrato().length() > 0
				&& aModel.getCodMagistrato().compareTo("-") != 0) {
			if (aModel.getCodMagistrato().compareTo("Tutti") != 0
					&& aModel.getCodMagistrato().compareTo("Nessuno") != 0) {
				lAppoggio = " DO.COD_MAGISTRATO = '" + aModel.getCodMagistrato() + "'";
				lCondizioni += setAND(lAppoggio);
			} else {
				if (aModel.getCodMagistrato().compareTo("Tutti") == 0) {
					lAppoggio = " DO.COD_MAGISTRATO IS NOT NULL ";
					lCondizioni += setAND(lAppoggio);
				}
				if (aModel.getCodMagistrato().compareTo("Nessuno") == 0) {
					lAppoggio = " DO.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO IS NULL AND MR.MAG_COD_MAGISTRATO IS NULL ";
					lCondizioni += setAND(lAppoggio);
				}
			}

		} else if (aModel.getCodEsperto() != null && aModel.getCodEsperto().intValue() >= 0) {
			if (aModel.getCodEsperto().intValue() == 9999) {
				lAppoggio = " DO.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO IS NOT NULL ";
				lCondizioni += setAND(lAppoggio);
				lAppoggio = " E.DATA_EMISSIONE >= (MR.DATA_INIZIO - 1) ";
				lCondizioni += setAND(lAppoggio);
				lAppoggio = "  (MR.DATA_FINE IS NULL OR (MR.DATA_FINE -1) > E.DATA_EMISSIONE) ";
				lCondizioni += setAND(lAppoggio);
			} else {
				lAppoggio = " DO.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO = " + aModel.getCodEsperto();
				lCondizioni += setAND(lAppoggio);
				lAppoggio = " E.DATA_EMISSIONE >= (MR.DATA_INIZIO - 1) ";
				lCondizioni += setAND(lAppoggio);
				lAppoggio = "  (MR.DATA_FINE IS NULL OR (MR.DATA_FINE -1) > E.DATA_EMISSIONE) ";
				lCondizioni += setAND(lAppoggio);
			}
		}

		// Condizioni sullo Stato di Validazione
		if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI)) {
			lAppoggio = " E.FLAG_DOCUMENTO_REGISTRATO = 'A' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.VALIDATI)) {
			lAppoggio = "E.FLAG_DOCUMENTO_REGISTRATO <> 'A' AND DA.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_VALIDATI)) {
			lAppoggio = "E.FLAG_DOCUMENTO_REGISTRATO <> 'A' AND  DA.FLAG_DOCUMENTO_REGISTRATO <> 'S' ";
			lCondizioni += setAND(lAppoggio);
		}

		lCondizioni += setCondizioniSentProvvedimento(aModel);

		// Infine la WHERE
		if (lInserito)
			lCondizioni = " WHERE " + lCondizioni;

		return lCondizioni;
	}

	/**
	 * MEV10-s3: aggiunto metodo
	 * 
	 * @return Sring
	 */
	protected String getSqlQuerySentFromJoinDepOrd() {
		String lStatement = new String("");

		lStatement += " FROM DEPOSITO_SENTENZA DO join EVENTO E"
				+ " on (DO.ID_EVENTO_GENERATO = E.ID_EVENTO)";
		return lStatement;
	}

	/**
	 * MEV10-s3: aggiunto metodo
	 * 
	 * @param aModel
	 * @return String
	 */
	private String setCondizioniSentProvvedimento(RicercaProvvedimentoModel aModel) {
		// Condizione resituita
		String lCondizioni = new String("");
		// Stringa di appoggio usata per la preparazione della singola condizione
		String lAppoggio;

		if (aModel.isTipoIntervalloRicercaXEstremiProvvedimento()) {
			if (aModel.getAnnoIniziale() != null && aModel.getNumIniziale() != null) {
				if (aModel.getAnnoFinale() != null && aModel.getNumFinale() != null) {
					// AnnoIniziale = AnnoFinale
					if (aModel.getAnnoIniziale().compareTo(aModel.getAnnoFinale()) == 0) {
						lAppoggio = " DO.ANNO_SENTENZA = " + aModel.getAnnoIniziale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DO.NUM_SENTENZA <= " + aModel.getNumFinale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DO.NUM_SENTENZA >= " + aModel.getNumIniziale();
						lCondizioni += setAND(lAppoggio);
					} else {
						lAppoggio = " DO.ANNO_SENTENZA > " + aModel.getAnnoIniziale();
						lAppoggio += " AND DO.ANNO_SENTENZA < " + aModel.getAnnoFinale();
						lAppoggio = "(" + lAppoggio + ")";
						lAppoggio += " OR (DO.ANNO_SENTENZA = " + aModel.getAnnoFinale() + " AND"
								+ " DO.NUM_SENTENZA <= " + aModel.getNumFinale() + ")";
						lAppoggio += " OR (DO.ANNO_SENTENZA = " + aModel.getAnnoIniziale() + " AND"
								+ " DO.NUM_SENTENZA >= " + aModel.getNumIniziale() + ")";
						lAppoggio = "(" + lAppoggio + ")";
						lCondizioni += setAND(lAppoggio);
					}
				} else {
					// Solo limite inferiore
					lAppoggio = " DO.ANNO_SENTENZA >= " + aModel.getAnnoIniziale();
					lCondizioni += setAND(lAppoggio);
					lAppoggio = " DO.NUM_SENTENZA >= " + aModel.getNumIniziale();
				}
			}
		} else if (aModel.isRicercaXDateDeposito()) {
			if (aModel.getDataDepositoIniziale() != null) {
				lAppoggio = " TO_CHAR(DO.DATA_DEPOSITO,'YYYYMMDD') >='"
						+ DateUtils.getDateToString(aModel.getDataDepositoIniziale(), "yyyyMMdd") + "'";
				lCondizioni += setAND(lAppoggio);
			}
			if (aModel.getDataDepositoFinale() != null) {
				lAppoggio = " TO_CHAR(DO.DATA_DEPOSITO,'YYYYMMDD') <='"
						+ DateUtils.getDateToString(aModel.getDataDepositoFinale(), "yyyyMMdd") + "'";
				lCondizioni += setAND(lAppoggio);
			}
		}

		// tipo controllo esecuzione
		if (aModel.getTipiControlliEsecuzione() != null) {
			if (aModel.getTipiControlliEsecuzione().length > 1
					&& aModel.getTipiControlliEsecuzione()[0] != null
					&& aModel.getTipiControlliEsecuzione()[1] != null) {
				lAppoggio = "( DO.TIPO_CONTROLLO_ESECUZIONE = 'E' OR DO.TIPO_CONTROLLO_ESECUZIONE = 'T' ) ";
				lCondizioni += setAND(lAppoggio);
			} else if (aModel.getTipiControlliEsecuzione().length == 1) {
				lAppoggio = " DO.TIPO_CONTROLLO_ESECUZIONE = '" + aModel.getTipiControlliEsecuzione()[0]
						+ "' ";
				lCondizioni += setAND(lAppoggio);
			}
		}

		return lCondizioni;
	}

}