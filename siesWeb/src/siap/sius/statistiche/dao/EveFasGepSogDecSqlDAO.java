package siap.sius.statistiche.dao;

import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.sius.depositodecreto.model.DepositoDecretoModel;
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
 * Title: EveFasGepSogDecSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO estensione della EveFasGepSogSqlDAO che aggiunge alla JOIN di tabelle già
 * rappresentate dall'Ancestor anche la tabella DEPOSITO_DECRETO.
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

public class EveFasGepSogDecSqlDAO extends EveFasGepSogSqlDAO {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public EveFasGepSogDecSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	/**
	 * Restituisce la parte dello statement Sql "Select .... FROM .."
	 */
	protected String getSqlQuery() {
		String lStatement = getSqlQuerySelect() + ", " + getSqlQueryDepositoDecreto() + ", "
				+ getSqlQueryEsperto() + getSqlQueryFromJoinDepDec() + getSqlQueryJoin("03");

		return lStatement;
	}

	protected String getSqlQueryFromJoinDepDec() {
		String lStatement = new String("");

		lStatement += " FROM DEPOSITO_DECRETO DD join EVENTO E" + " on (DD.ID_EVENTO_GENERATO = E.ID_EVENTO)";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		EveFasGepSogModel lAncestorModel = (EveFasGepSogModel) super.getModel();
		EveFasGepSogProvModel lModel = new EveFasGepSogProvModel(lAncestorModel, getDepositoDecretoModel());
		return lModel;
	};

	private DepositoDecretoModel getDepositoDecretoModel() throws DAOException {
		// Lettura dei campi dalla tabella DEPOSITO_DECRETO_PC

		DepositoDecretoModel lDecreto = new DepositoDecretoModel();
		lDecreto.setIdDepositoDecreto(getBigDecimal("ID_DEPOSITO_DECRETO"));
		lDecreto.setAnnoS72(getBigDecimal("ANNO_S72"));
		lDecreto.setNumS72(getBigDecimal("NUM_S72"));
		lDecreto.setDataDeposito(getDate("DATA_DEPOSITO"));
		lDecreto.setCodTipoControlloEsecuzione(getString("TIPO_CONTROLLO_ESECUZIONE"));

		return lDecreto;
	}

	private String setOrderbyAnnoNum() {
		String lStatement = " ORDER BY  ANNO_S72 ASC, NUM_S72 ASC";
		return lStatement;
	}

	/**
	 * Il metodo restituisce la parte dello statement di select che elenca i campi della tabella
	 * DEPOSITO_DECRETO.
	 * 
	 * @return
	 */
	protected String getSqlQueryDepositoDecreto() {
		String lStatement = new String("");

		lStatement += " DD.ID_DEPOSITO_DECRETO, " + "DD.COD_MAGISTRATO, " + "DD.ANNO_S72, " + "DD.NUM_S72, "
				+ "DD.DATA_DEPOSITO, DD.TIPO_CONTROLLO_ESECUZIONE ";
		return lStatement;
	}

	// variabile flag utilizzata per comporre la condizione di filtro sulla select
	private boolean lInserito = false;

	/**
	 * Il metodo prepara le condizioni di ricerca nello statement in preparazione in base al contenuto del
	 * model di ricerca passato come argomento. La ricerca può essere di 2 tipi: per estremi dell'ordinanza
	 * espressi in intervallo di ANNOS72/NUMS72; per intervallo di DATA_DEPOSITO dell'Ordinanza.
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
			lAppoggio = " DD.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
			lCondizioni += setAND(lAppoggio);
		}

		// Condizione sul magistrato
		if (aModel.getCodMagistrato() != null && aModel.getCodMagistrato().length() > 0
				&& aModel.getCodMagistrato().compareTo("-") != 0) {
			if (aModel.getCodMagistrato().compareTo("Tutti") != 0
					&& aModel.getCodMagistrato().compareTo("Nessuno") != 0) {
				lAppoggio = " DD.COD_MAGISTRATO = '" + aModel.getCodMagistrato() + "'";
				lCondizioni += setAND(lAppoggio);
			} else {
				if (aModel.getCodMagistrato().compareTo("Tutti") == 0) {
					lAppoggio = " DD.COD_MAGISTRATO IS NOT NULL ";
					lCondizioni += setAND(lAppoggio);
				}
				if (aModel.getCodMagistrato().compareTo("Nessuno") == 0) {
					lAppoggio = " DD.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO IS NULL AND MR.MAG_COD_MAGISTRATO IS NULL ";
					lCondizioni += setAND(lAppoggio);
				}
			}

		} else if (aModel.getCodEsperto() != null && aModel.getCodEsperto().intValue() >= 0) {
			if (aModel.getCodEsperto().intValue() == 9999) {
				lAppoggio = " DD.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO IS NOT NULL ";
				lCondizioni += setAND(lAppoggio);
				lAppoggio = " E.DATA_EMISSIONE >= (MR.DATA_INIZIO - 1) ";
				lCondizioni += setAND(lAppoggio);
				lAppoggio = "  (MR.DATA_FINE IS NULL OR (MR.DATA_FINE -1) > E.DATA_EMISSIONE) ";
				lCondizioni += setAND(lAppoggio);
			} else {
				lAppoggio = " DD.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO = " + aModel.getCodEsperto();
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

		// Condizioni sul Tipo Decreto
		if (aModel.getTipoDecreto().equalsIgnoreCase(ICostantiStatistiche.TUTTI_DECRETI)) {
			// Nessun Filtro ulteriore per Tutti i Decreti
			// lAppoggio = "  ";
			// lCondizioni += setAND (lAppoggio);
		} else if (aModel.getTipoDecreto().equalsIgnoreCase(ICostantiStatistiche.INAMMISSIBILITA)) {
			lAppoggio = "DD.COD_TIPO_DECRETO = '03' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getTipoDecreto().equalsIgnoreCase(ICostantiStatistiche.INCOMPETENZA)) {
			lAppoggio = "DD.COD_TIPO_DECRETO = '04' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getTipoDecreto().equalsIgnoreCase(ICostantiStatistiche.NDPNLP)) {
			lAppoggio = "DD.COD_TIPO_DECRETO = '29' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getTipoDecreto().equalsIgnoreCase(ICostantiStatistiche.REVOCA)) {
			lAppoggio = "DD.COD_TIPO_DECRETO = 'RV' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getTipoDecreto().equalsIgnoreCase(ICostantiStatistiche.ALTRI_DECRETI)) {
			lAppoggio = "DD.COD_TIPO_DECRETO NOT IN ( '03', '04', '29', 'RV') ";
			lCondizioni += setAND(lAppoggio);
		}

		lCondizioni += setCondizioniDecreto(aModel);

		// Infine la WHERE
		if (lInserito)
			lCondizioni = " WHERE " + lCondizioni;

		return lCondizioni;
	}

	protected String setCondizioniDecreto(RicercaOrdinanzaModel aModel) {
		// Condizione resituita
		String lCondizioni = new String("");
		// Stringa di appoggio usata per la preparazione della singola condizione
		String lAppoggio;

		if (aModel.isTipoIntervalloRicercaXEstremiProvvedimento()) {
			if (aModel.getAnnoIniziale() != null && aModel.getNumIniziale() != null) {
				if (aModel.getAnnoFinale() != null && aModel.getNumFinale() != null) {
					// AnnoIniziale = AnnoFinale
					if (aModel.getAnnoIniziale().compareTo(aModel.getAnnoFinale()) == 0) {
						lAppoggio = " DD.ANNO_S72 = " + aModel.getAnnoIniziale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DD.NUM_S72 <= " + aModel.getNumFinale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DD.NUM_S72 >= " + aModel.getNumIniziale();
						lCondizioni += setAND(lAppoggio);
					} else {
						lAppoggio = " DD.ANNO_S72 > " + aModel.getAnnoIniziale();
						lAppoggio += " AND  DD.ANNO_S72 < " + aModel.getAnnoFinale();
						lAppoggio = "(" + lAppoggio + ")";
						lAppoggio += " OR (DD.ANNO_S72 = " + aModel.getAnnoFinale() + " AND"
								+ " DD.NUM_S72 <= " + aModel.getNumFinale() + ")";
						lAppoggio += " OR (DD.ANNO_S72 = " + aModel.getAnnoIniziale() + " AND"
								+ " DD.NUM_S72 >= " + aModel.getNumIniziale() + ")";
						lAppoggio = "(" + lAppoggio + ")";
						lCondizioni += setAND(lAppoggio);
					}
				} else {
					// Solo limite inferiore
					lAppoggio = " DD.ANNO_S72 >= " + aModel.getAnnoIniziale();
					lCondizioni += setAND(lAppoggio);
					lAppoggio = " DD.NUM_S72 >= " + aModel.getNumIniziale();
				}
			}
		} else if (aModel.isRicercaXDateDeposito()) {
			if (aModel.getDataDepositoIniziale() != null) {
				lAppoggio = " TO_CHAR(DD.DATA_DEPOSITO,'YYYYMMDD') >='"
						+ DateUtils.getDateToString(aModel.getDataDepositoIniziale(), "yyyyMMdd") + "'";
				lCondizioni += setAND(lAppoggio);
			}
			if (aModel.getDataDepositoFinale() != null) {
				lAppoggio = " TO_CHAR(DD.DATA_DEPOSITO,'YYYYMMDD') <='"
						+ DateUtils.getDateToString(aModel.getDataDepositoFinale(), "yyyyMMdd") + "'";
				lCondizioni += setAND(lAppoggio);
			}
		}
		// Tipo controllo esecuzione.
		if (aModel.getTipiControlliEsecuzione() != null) {
			if (aModel.getTipiControlliEsecuzione().length > 1
					&& aModel.getTipiControlliEsecuzione()[0] != null
					&& aModel.getTipiControlliEsecuzione()[1] != null) {
				lAppoggio = "( DD.TIPO_CONTROLLO_ESECUZIONE = 'E' OR DD.TIPO_CONTROLLO_ESECUZIONE = 'T' ) ";
				lCondizioni += setAND(lAppoggio);
			} else if (aModel.getTipiControlliEsecuzione().length == 1) {
				lAppoggio = " DD.TIPO_CONTROLLO_ESECUZIONE = '" + aModel.getTipiControlliEsecuzione()[0]
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

	/**
	 * Il metodo prepara lo statment sql che effettua la ricerca in base alle condizioni espresse dal model di
	 * ricerca passato come argomento.
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaProcSiusXProvvedimenti(RicercaOrdinanzaModel aModel) throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ricercaProcSiusXDecreti(): inizio ");

		String lStatement = getSqlQuery();

		lStatement += setCondizioni(aModel);
		lStatement += setOrderbyAnnoNum();

		// lStatement += setOrder();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ricercaProcSiusXDecreti(): fine ");

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

		// Condizione sull'ufficio
		if (aModel.getCodUfficioInserimento() != null
				&& aModel.getCodUfficioInserimento().trim().length() > 0) {
			lAppoggio = " DD.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
			lCondizioni += setAND(lAppoggio);
		}

		// Condizione sul magistrato
		if (aModel.getCodMagistrato() != null && aModel.getCodMagistrato().length() > 0
				&& aModel.getCodMagistrato().compareTo("-") != 0) {
			if (aModel.getCodMagistrato().compareTo("Tutti") != 0
					&& aModel.getCodMagistrato().compareTo("Nessuno") != 0) {
				lAppoggio = " DD.COD_MAGISTRATO = '" + aModel.getCodMagistrato() + "'";
				lCondizioni += setAND(lAppoggio);
			} else {
				if (aModel.getCodMagistrato().compareTo("Tutti") == 0) {
					lAppoggio = " DD.COD_MAGISTRATO IS NOT NULL ";
					lCondizioni += setAND(lAppoggio);
				}
				if (aModel.getCodMagistrato().compareTo("Nessuno") == 0) {
					lAppoggio = " DD.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO IS NULL AND MR.MAG_COD_MAGISTRATO IS NULL ";
					lCondizioni += setAND(lAppoggio);
				}
			}

		} else if (aModel.getCodEsperto() != null && aModel.getCodEsperto().intValue() >= 0) {
			if (aModel.getCodEsperto().intValue() == 9999) {
				lAppoggio = " DD.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO IS NOT NULL ";
				lCondizioni += setAND(lAppoggio);
				lAppoggio = " E.DATA_EMISSIONE >= (MR.DATA_INIZIO - 1) ";
				lCondizioni += setAND(lAppoggio);
				lAppoggio = "  (MR.DATA_FINE IS NULL OR (MR.DATA_FINE -1) > E.DATA_EMISSIONE) ";
				lCondizioni += setAND(lAppoggio);
			} else {
				lAppoggio = " DD.COD_MAGISTRATO IS NULL AND ESP.ID_ESPERTO = " + aModel.getCodEsperto();
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

		// Condizioni sul Tipo Decreto
		if (aModel.getTipoDecreto().equalsIgnoreCase(ICostantiStatistiche.TUTTI_DECRETI)) {
			// Nessun Filtro ulteriore per Tutti i Decreti
			// lAppoggio = "  ";
			// lCondizioni += setAND (lAppoggio);
		} else if (aModel.getTipoDecreto().equalsIgnoreCase(ICostantiStatistiche.INAMMISSIBILITA)) {
			lAppoggio = "DD.COD_TIPO_DECRETO = '03' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getTipoDecreto().equalsIgnoreCase(ICostantiStatistiche.INCOMPETENZA)) {
			lAppoggio = "DD.COD_TIPO_DECRETO = '04' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getTipoDecreto().equalsIgnoreCase(ICostantiStatistiche.NDPNLP)) {
			lAppoggio = "DD.COD_TIPO_DECRETO = '29' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getTipoDecreto().equalsIgnoreCase(ICostantiStatistiche.REVOCA)) {
			lAppoggio = "DD.COD_TIPO_DECRETO = 'RV' ";
			lCondizioni += setAND(lAppoggio);
		} else if (aModel.getTipoDecreto().equalsIgnoreCase(ICostantiStatistiche.ALTRI_DECRETI)) {
			lAppoggio = "DD.COD_TIPO_DECRETO NOT IN ( '03', '04', '29', 'RV') ";
			lCondizioni += setAND(lAppoggio);
		}

		lCondizioni += setCondizioniDecreto(aModel);

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
	private String setCondizioniDecreto(RicercaProvvedimentoModel aModel) {
		// Condizione resituita
		String lCondizioni = new String("");
		// Stringa di appoggio usata per la preparazione della singola condizione
		String lAppoggio;

		if (aModel.isTipoIntervalloRicercaXEstremiProvvedimento()) {
			if (aModel.getAnnoIniziale() != null && aModel.getNumIniziale() != null) {
				if (aModel.getAnnoFinale() != null && aModel.getNumFinale() != null) {
					// AnnoIniziale = AnnoFinale
					if (aModel.getAnnoIniziale().compareTo(aModel.getAnnoFinale()) == 0) {
						lAppoggio = " DD.ANNO_S72 = " + aModel.getAnnoIniziale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DD.NUM_S72 <= " + aModel.getNumFinale();
						lCondizioni += setAND(lAppoggio);
						lAppoggio = " DD.NUM_S72 >= " + aModel.getNumIniziale();
						lCondizioni += setAND(lAppoggio);
					} else {
						lAppoggio = " DD.ANNO_S72 > " + aModel.getAnnoIniziale();
						lAppoggio += " AND  DD.ANNO_S72 < " + aModel.getAnnoFinale();
						lAppoggio = "(" + lAppoggio + ")";
						lAppoggio += " OR (DD.ANNO_S72 = " + aModel.getAnnoFinale() + " AND"
								+ " DD.NUM_S72 <= " + aModel.getNumFinale() + ")";
						lAppoggio += " OR (DD.ANNO_S72 = " + aModel.getAnnoIniziale() + " AND"
								+ " DD.NUM_S72 >= " + aModel.getNumIniziale() + ")";
						lAppoggio = "(" + lAppoggio + ")";
						lCondizioni += setAND(lAppoggio);
					}
				} else {
					// Solo limite inferiore
					lAppoggio = " DD.ANNO_S72 >= " + aModel.getAnnoIniziale();
					lCondizioni += setAND(lAppoggio);
					lAppoggio = " DD.NUM_S72 >= " + aModel.getNumIniziale();
				}
			}
		} else if (aModel.isRicercaXDateDeposito()) {
			if (aModel.getDataDepositoIniziale() != null) {
				lAppoggio = " TO_CHAR(DD.DATA_DEPOSITO,'YYYYMMDD') >='"
						+ DateUtils.getDateToString(aModel.getDataDepositoIniziale(), "yyyyMMdd") + "'";
				lCondizioni += setAND(lAppoggio);
			}
			if (aModel.getDataDepositoFinale() != null) {
				lAppoggio = " TO_CHAR(DD.DATA_DEPOSITO,'YYYYMMDD') <='"
						+ DateUtils.getDateToString(aModel.getDataDepositoFinale(), "yyyyMMdd") + "'";
				lCondizioni += setAND(lAppoggio);
			}
		}
		// Tipo controllo esecuzione.
		if (aModel.getTipiControlliEsecuzione() != null) {
			if (aModel.getTipiControlliEsecuzione().length > 1
					&& aModel.getTipiControlliEsecuzione()[0] != null
					&& aModel.getTipiControlliEsecuzione()[1] != null) {
				lAppoggio = "( DD.TIPO_CONTROLLO_ESECUZIONE = 'E' OR DD.TIPO_CONTROLLO_ESECUZIONE = 'T' ) ";
				lCondizioni += setAND(lAppoggio);
			} else if (aModel.getTipiControlliEsecuzione().length == 1) {
				lAppoggio = " DD.TIPO_CONTROLLO_ESECUZIONE = '" + aModel.getTipiControlliEsecuzione()[0]
						+ "' ";
				lCondizioni += setAND(lAppoggio);
			}
		}

		return lCondizioni;
	}

}