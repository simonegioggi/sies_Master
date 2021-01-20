package siap.siep.fascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import siap.dao.SIAPSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.MinorMask;

/**
 * <p>
 * Title: FascicoloSiepSqlDAO
 * </p>
 * <p>
 * Description: Realizza Sql DAo del Fascicolo Siep
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class FascicoloSiepOnViewSqlDAO extends SIAPSqlDAO {

	public FascicoloSiepOnViewSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void getCountFascicoli(FascicoloSiepModel aModel) throws DAOException {
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM V_FASCICOLO_SIEP WHERE ";
		lStatement += " " + setCondizione(aModel);
		setStatement(lStatement);
	}

	public void ricercaFascicoloPaged(FascicoloSiepModel aModel, int aPage) throws DAOException {
		String lStatement = getFascicoloSqlQuery();
		String lPaginedStatement = new String("");

		lStatement += " " + setCondizione(aModel);
		lStatement += " " + setOrder();

		// lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
		// + " ) INNER ) WHERE rn between " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
		// + " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		// MEV_6: cambiata la paginazione
		lPaginedStatement = convertStatementToNewPaginedStatement(aPage, lStatement);
		setStatement(lPaginedStatement);
	}

	public void ricercaFascicoloMajorPaged(FascicoloSiepModel aModel, int aPage, String codUfficio,
			String tipoUfficioUtente) throws DAOException {
		String lStatement = getFascicoloMajorSqlQuery();
		String lPaginedStatement = new String("");
		// se l'utente accedere a SIEP - utenza PGCAP
		// deve avere visibilità dei Procedimenti inseriti dall'ufficio
		// Minorenne (PMM) a condizione che i soggetti associati siano
		// dei Minorenni
		if (tipoUfficioUtente != null && !tipoUfficioUtente.equals("") && tipoUfficioUtente.equals("PGCAP")) {
			lStatement += " " + setCondizionePGCAP(aModel);
		} else {
			lStatement += " " + setCondizione(aModel);
		}
		lStatement += " and vfs.id_fascicolo_siep = vse.FAS_SIE_ID_FASCICOLO_SIEP ";
		if (tipoUfficioUtente != null && !tipoUfficioUtente.equals("") && tipoUfficioUtente.equals("PGCAP")) {
			// nessuna condizione
		} else {
			lStatement += MinorMask.minorCondition("vse", "vfs", codUfficio);
		}
		lStatement += " " + setOrder();

		// lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
		// + " ) INNER ) WHERE rn between " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
		// + " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		// MEV_6: cambiata la paginazione
		lPaginedStatement = convertStatementToNewPaginedStatement(aPage, lStatement);
		setStatement(lPaginedStatement);
	}

	protected String getFascicoloSqlQuery() {
		String lStatement = new String();

		lStatement += "SELECT * FROM V_FASCICOLO_SIEP WHERE ";

		return lStatement;
	}

	protected String getFascicoloMajorSqlQuery() {
		String lStatement = new String();

		lStatement += "SELECT vfs.* FROM V_FASCICOLO_SIEP vfs, V_SOGGETTO_ETA vse WHERE ";

		return lStatement;
	}

	/**
	 * Esegue la ricerca dei fascicoli in base a "DESCR_TIPO_PROVVEDIMENTO", "DESCR_TIPO_AUTORITA_EMITTENTE" e
	 * "STATO_PROCEDIMENTO"
	 *
	 * @param strCodiceDistrettoUtente
	 */
	protected String getFascicoloSqlQueryRicercaProcSog(String strCodiceDistrettoUtente, int aPage,
			String strCodUfficioUtenteConnesso) {
		String lStatement = new String();

		if (aPage > 0) {
			lStatement += " SELECT   FASCICOLO_siep.id_fascicolo_siep, fascicolo_siep.chiave_anno, ";
			lStatement += " fascicolo_siep.chiave_ufficio, fascicolo_siep.chiave_progr, ";
			lStatement += " fascicolo_siep.flag_validato, fascicolo_siep.data_iscrizione, ";
			lStatement += " fascicolo_siep.data_aggiornamento, ";
			lStatement += " fascicolo_siep.data_irrevocabilita, ";
			lStatement += " fascicolo_siep.data_arrivo_atto, "; // Anna
			lStatement += " fascicolo_siep.cod_stato_fascicolo, soggetto.id_soggetto, ";
			lStatement += " soggetto.cognome, soggetto.nome, sentenza.id_sentenza, ";
			lStatement += " sentenza.data_provvedimento, ";
			lStatement += " luogo_emittente.descrizione descr_luogo_emittente, uff.cod_distretto, ";
			lStatement += " tipo_provvedimento.rv_meaning descr_tipo_provvedimento, ";
			lStatement += " tipo_autorita_emittente.rv_meaning descr_tipo_autorita_emittente, ";
			lStatement += " (c.rv_meaning || ' ' || TO_CHAR (a.DATA, 'dd-mm-yyyy')) stato_procedimento ";
		} else {
			lStatement += " SELECT count(*) HowManyRecords ";
		}

		lStatement += " FROM  ";
		lStatement += " fascicolo_siep, ";
		lStatement += " soggetto, ";
		lStatement += " sentenza, ";
		lStatement += " comune luogo_emittente, ";
		lStatement += " cg_ref_codes tipo_provvedimento, ";
		lStatement += " cg_ref_codes tipo_autorita_emittente, ";
		lStatement += " cg_ref_codes c, ";
		lStatement += " v_soggetto_eta vse, ";
		lStatement += " (SELECT cod_ufficio, cod_tipo_ufficio, cod_comune, cod_distretto ";
		lStatement += " FROM ufficio ";
		lStatement += " WHERE cod_distretto = '" + strCodiceDistrettoUtente + "') uff, ";
		lStatement += " (SELECT b.fas_sie_id_fascicolo_siep, b.cod_stato_procedimento, b.DATA ";
		lStatement += " FROM stato_procedimento b,max_stato_procedimento d ";
		lStatement += " WHERE b.cod_ufficio_inserimento in ";
		lStatement += " (SELECT cod_ufficio ";

		lStatement += " FROM ufficio ";
		lStatement += " WHERE cod_distretto = '" + strCodiceDistrettoUtente + "') ";
		lStatement += " and b.fas_sie_id_fascicolo_siep=d.fas_sie_id_fascicolo_siep ";
		lStatement += " and b.progressivo=d.sta_pro_progressivo) a ";
		lStatement += " WHERE  ";
		lStatement += " fascicolo_siep.sog_id_soggetto = soggetto.id_soggetto ";
		lStatement += " AND fascicolo_siep.sen_id_sentenza = sentenza.id_sentenza ";
		lStatement += " AND luogo_emittente.cod_comune = sentenza.cod_luogo_emittente ";
		lStatement += " AND uff.cod_ufficio = fascicolo_siep.chiave_ufficio ";

		// 09/2011 Introduzione filtro FLAG_VALIDATO per Ricerca Altre BDI (solo fascicoli validati)
		// Per segnalazione durante periodo garanzia
		lStatement += " AND (fascicolo_siep.chiave_ufficio = '" + strCodUfficioUtenteConnesso
				+ "' OR (fascicolo_siep.chiave_ufficio != '" + strCodUfficioUtenteConnesso
				+ "' AND fascicolo_siep.FLAG_VALIDATO = 'S'))";

		lStatement += " AND (tipo_provvedimento.rv_domain = 'TIPO_PROVVEDIMENTO' AND tipo_provvedimento.rv_low_value = sentenza.cod_tipo_provvedimento) ";
		lStatement += " AND (tipo_autorita_emittente.rv_domain = 'TIPO_UFFICIO' AND tipo_autorita_emittente.rv_low_value = sentenza.cod_tipo_autorita_emittente) ";
		lStatement += " AND c.rv_domain = 'STATO_PROCEDIMENTO' ";
		lStatement += " AND c.rv_low_value = NVL (a.cod_stato_procedimento, '-') ";
		lStatement += " AND fascicolo_siep.id_fascicolo_siep = a.fas_sie_id_fascicolo_siep(+) ";
		lStatement += " AND fascicolo_siep.id_fascicolo_siep = vse.fas_sie_id_fascicolo_siep ";

		return lStatement;
	}

	/**
	 * Esegue la ricerca dei fascicoli in base al super soggetto
	 *
	 * @param strCodiceDistrettoUtente
	 */
	protected String setCondizioneSuperSoggetto(SoggettoModel aModel, String strTipoRicerca, String major,
			String tipoUfficio, String campoDet) {

		String lCondizioni = new String();
		// 22/09/2010 Utilizzo di UPPER e toUpperCase per normalizzare il controllo di uguaglianza x
		// (PATERNITA, COGNOME_MADRE, ATTO_NASCITA, COD_AFIS)

		if (aModel.getAnnoNascita() != null)
			lCondizioni += " AND soggetto.ANNO_NASCITA = '" + aModel.getAnnoNascita() + "'";
		else
			lCondizioni += " AND soggetto.ANNO_NASCITA is null";

		// MEV_39: risolta casistica per unix (l'atto di nascita comprende il carattere ' --> NA'00 287
		if (aModel.getAttoNascita() != null && aModel.getAttoNascita().length() > 0)
			lCondizioni += " AND UPPER(soggetto.ATTO_NASCITA) = '"
					+ StringUtils.convertSqlString(aModel.getAttoNascita().toUpperCase()) + "'";
		else
			lCondizioni += " AND soggetto.ATTO_NASCITA is null";

		if (aModel.getCodAfis() != null && aModel.getCodAfis().length() > 0)
			lCondizioni += " AND UPPER(soggetto.COD_AFIS) = '" + aModel.getCodAfis().toUpperCase() + "'";
		else
			lCondizioni += " AND soggetto.COD_AFIS is null";

		if (aModel.getCodComuneNascita() != null && aModel.getCodComuneNascita().length() > 0)
			lCondizioni += " AND soggetto.COD_COMUNE_NASCITA = '" + aModel.getCodComuneNascita() + "'";
		else
			lCondizioni += " AND soggetto.COD_COMUNE_NASCITA is null";

		if (aModel.getCodCs() != null && aModel.getCodCs().length() > 0)
			lCondizioni += " AND soggetto.COD_CS = '"
					+ StringUtils.convertSqlString(aModel.getCodCs().toUpperCase()) + "'";
		else
			lCondizioni += " AND soggetto.COD_CS is null";

		if (aModel.getCodFiscale() != null && aModel.getCodFiscale().length() > 0)
			lCondizioni += " AND soggetto.COD_FISCALE = '" + aModel.getCodFiscale() + "'";
		else
			lCondizioni += " AND soggetto.COD_FISCALE is null";

		if (aModel.getCodProvinciaNascita() != null && aModel.getCodProvinciaNascita().length() > 0)
			lCondizioni += " AND soggetto.COD_PROVINCIA_NASCITA = '" + aModel.getCodProvinciaNascita() + "'";
		else
			lCondizioni += " AND soggetto.COD_PROVINCIA_NASCITA is null";

		if (aModel.getCodStatoNascita() != null && aModel.getCodStatoNascita().length() > 0)
			lCondizioni += " AND soggetto.COD_STATO_NASCITA = '" + aModel.getCodStatoNascita() + "'";
		else
			lCondizioni += " AND soggetto.COD_STATO_NASCITA is null";

		if (aModel.getCognome() != null && aModel.getCognome().length() > 0)
			// Paolo Cherubini 16/02/2012 b2/rr010 va in errore sulle lettere minuscole accentate aggiungo
			// upper
			lCondizioni += " AND upper(soggetto.COGNOME) = '"
					+ StringUtils.convertSqlString(aModel.getCognome()) + "'";
		else
			lCondizioni += " AND soggetto.COGNOME is null";

		if (aModel.getNome() != null && aModel.getNome().length() > 0)
			// Paolo Cherubini 16/02/2012 b2/rr010 va in errore sulle lettere minuscole accentate aggiungo
			// upper
			lCondizioni += " AND upper(soggetto.NOME) = '" + StringUtils.convertSqlString(aModel.getNome())
					+ "'";
		else
			lCondizioni += " AND soggetto.NOME is null";

		if (aModel.getDataNascita() != null)
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lCondizioni += " AND trunc(soggetto.DATA_NASCITA) = to_date('"
					+ DateUtils.getDateToString(aModel.getDataNascita(), "dd/MM/yyyy") + "','DD-MM-YYYY')";
		else
			lCondizioni += " AND soggetto.DATA_NASCITA is null";

		if (aModel.getDataNascitaPresunta() != null && aModel.getDataNascitaPresunta().length() > 0)
			lCondizioni += " AND soggetto.DATA_NASCITA_PRESUNTA = '" + aModel.getDataNascitaPresunta() + "'";
		else
			lCondizioni += " AND soggetto.DATA_NASCITA_PRESUNTA is null";

		if (aModel.getDescComuneNascitaEstero() != null && aModel.getDescComuneNascitaEstero().length() > 0)
			lCondizioni += " AND soggetto.DESC_COMUNE_NASCITA_ESTERO = '"
					+ StringUtils.convertSqlString(aModel.getDescComuneNascitaEstero()) + "'";
		else
			lCondizioni += " AND soggetto.DESC_COMUNE_NASCITA_ESTERO is null";

		if (aModel.getNazionalita() != null && aModel.getNazionalita().length() > 0)
			lCondizioni += " AND soggetto.NAZIONALITA = '" + aModel.getNazionalita() + "'";
		else
			lCondizioni += " AND soggetto.NAZIONALITA is null";

		if (aModel.getPaternita() != null && aModel.getPaternita().length() > 0)
			lCondizioni += " AND UPPER(soggetto.PATERNITA) = '"
					+ StringUtils.convertSqlString(aModel.getPaternita().toUpperCase()) + "'";
		else
			lCondizioni += " AND soggetto.PATERNITA is null";

		if (aModel.getCognomeMadre() != null && aModel.getCognomeMadre().length() > 0)
			lCondizioni += " AND UPPER(soggetto.COGNOME_MADRE) = '"
					+ StringUtils.convertSqlString(aModel.getCognomeMadre().toUpperCase()) + "'";
		else
			lCondizioni += " AND soggetto.COGNOME_MADRE is null";

		if (aModel.getNomeMadre() != null && aModel.getNomeMadre().length() > 0)
			lCondizioni += " AND UPPER(soggetto.NOME_MADRE) = '"
					+ StringUtils.convertSqlString(aModel.getNomeMadre().toUpperCase()) + "'";
		else
			lCondizioni += " AND soggetto.NOME_MADRE is null";

		if (aModel.getSesso() != null && aModel.getSesso().length() > 0)
			lCondizioni += " AND soggetto.SESSO = '" + aModel.getSesso() + "'";
		else
			lCondizioni += " AND soggetto.SESSO is null";

		if (aModel.getMeseNascita() != null)
			lCondizioni += " AND soggetto.MESE_NASCITA = " + aModel.getMeseNascita();
		else
			lCondizioni += " AND soggetto.MESE_NASCITA is null";

		if (aModel.getEtaPresuntaAnni() != null)
			lCondizioni += " AND soggetto.ETA_PRESUNTA_ANNI = " + aModel.getEtaPresuntaAnni();
		else
			lCondizioni += " AND soggetto.ETA_PRESUNTA_ANNI is null";

		if (aModel.getEtaPresuntaMesi() != null)
			lCondizioni += " AND soggetto.ETA_PRESUNTA_MESI = " + aModel.getEtaPresuntaMesi();
		else
			lCondizioni += " AND soggetto.ETA_PRESUNTA_MESI is null";

		// Ambrosino Supersoggetto -- serve per avere solo i soggetti dell'ufficio
		// nella lista di ActListaFascicoloPerSoggetto.java quando la ricerca
		// è per ufficio e non per distretto;

		if (StringUtils.checkValidValue(major)) {
			if (campoDet != null && !campoDet.equals("") && campoDet.equals("S") && tipoUfficio != null
					&& !tipoUfficio.equals("") && tipoUfficio.equals("PGCAP")) {
				// campoDet = Parametro utilizzato come discriminante nel caso di ufficio PGCAP,
				// per visualizzare i procedimenti di soggetti minorenni (solo nel caso di dettaglio)
				// pertanto non viene aggiunta la condizione soggetto >= 18
			} else {

				lCondizioni += MinorMask.minorCondition("vse", "fascicolo_siep", major);
			}
		}

		if (strTipoRicerca.equals("ufficio")) {
			if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
				// lCondizioni += " AND soggetto.COD_UFFICIO_INSERIMENTO = '"+
				// aModel.getCodUfficioInserimento()+"'";
				// lCondizioni += " AND soggetto.COD_UFFICIO_INSERIMENTO = fascicolo_siep.CHIAVE_UFFICIO ";
				lCondizioni += " AND (fascicolo_siep.CHIAVE_UFFICIO = '" + aModel.getCodUfficioInserimento()
						+ "' or exists ";
				lCondizioni += " (select 1 from ufficio_accorpato ua where fascicolo_siep.CHIAVE_UFFICIO = ua.COD_UFFICIO_NEW(+) and ua.COD_UFFICIO = '"
						+ aModel.getCodUfficioInserimento() + "'))";
			}
		}

		if (strTipoRicerca.equals("ufficio")) {
			if (aModel.getClassiFascicolo() != null && aModel.getClassiFascicolo().length > 0) {
				String[] lClassiFascicolo = aModel.getClassiFascicolo();

				for (int i = 0; i < lClassiFascicolo.length; i++) {
					if (i != 0) {
						lCondizioni += " OR ";
					} else {
						lCondizioni += " AND (";
					}

					int lTipoClasse = Integer.parseInt(lClassiFascicolo[i]);
					if (lTipoClasse > 1) {
						lCondizioni += " (CHIAVE_PROGR BETWEEN " + (lTipoClasse * 10000) + " AND "
								+ (lTipoClasse * 10000 + 9999) + " ) ";
					} else // TipoProgressivo =1
					{
						lCondizioni += " (CHIAVE_PROGR BETWEEN 1 AND " + (lTipoClasse * 10000 + 9999) + " ) ";
					}
				}

				lCondizioni += " ) ";

			} // Chiude if aModel.getClassiFascicolo()

		} // Chiude if strTipoRicerca.equals("ufficio")

		//
		// fine Ambrosino

		return lCondizioni;
	}

	/**
	 * Esegue la ricerca di un fascicolo
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicolo(FascicoloSiepModel aModel) throws DAOException {
		String lStatement = getFascicoloSqlQuery();
		lStatement += " " + setCondizione(aModel);
		lStatement += " " + setOrder();

		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca di un fascicolo
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicoloByKey(BigDecimal aKey) throws DAOException {
		String lStatement = getFascicoloSqlQuery();
		lStatement += " ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " " + setOrder();
		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca dei fascicoli per soggetto
	 *
	 * @param aModel
	 * @param aPage
	 * @param strTipoRicerca
	 * @param strCodiceDistrettoUtente
	 * @throws DAOException
	 */
	public void ricercaFascicoloPagedSoggetto(FascicoloSiepModel aModel, int aPage, String aCodUff,
			String strTipoRicerca, String strCodiceDistrettoUtente) throws DAOException {
		String lStatement = getFascicoloSqlQueryRicercaProcSog(strCodiceDistrettoUtente, aPage, aCodUff);
		String lPaginedStatement = new String("");

		lStatement += " " + setCondizioneSoggettoUffDistr(aModel, strTipoRicerca);
		lStatement += " " + setOrder();

		// lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
		// + " ) INNER ) WHERE rn between " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
		// + " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		// MEV_6: cambiata la paginazione
		lPaginedStatement = convertStatementToNewPaginedStatement(aPage, lStatement);
		setStatement(lPaginedStatement);
	}

	/**
	 * paolo super soggetto 22 luglio 2009 Esegue la ricerca dei fascicoli per Super soggetto
	 *
	 * @param aModel
	 * @param aPage
	 * @param strTipoRicerca
	 * @param strCodiceDistrettoUtente
	 * @throws DAOException
	 */
	public void getCountFascicoliSoggetto(FascicoloSiepModel aModel) throws DAOException {
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM V_FASCICOLO_SIEP WHERE ";

		lStatement += " " + setCondizioneSoggetto(aModel);

		setStatement(lStatement);
	}

	public void ricercaFascicoloOnViewPagedSuperSoggetti(SoggettoModel aModel, String aCodUfficio, int aPage,
			String strTipoRicerca, String strCodiceDistrettoUtente, String major, String tipoUfficio,
			String campoDet) throws DAOException {
		String lStatement = getFascicoloSqlQueryRicercaProcSog(strCodiceDistrettoUtente, aPage, aCodUfficio);
		String lPaginedStatement = new String("");
		lStatement += " " + setCondizioneSuperSoggetto(aModel, strTipoRicerca, major, tipoUfficio, campoDet);

		// Ambros --> Per la ricerca lato SIUS (parametro cod_distretto='sius') non vanno presi gli iscritti
		if (strTipoRicerca.equals("sius")) {
			lStatement += " and COD_STATO_FASCICOLO != '02'";
		}

		lStatement += " " + setOrder();

		// Ambros --> Per il conteggio di inner il parametro aPage=0 (e conto tutte le righe trovate);
		// Per la Query di ricerca SuperSogg aPage=lpagina(e monto 20 rihe alla volta )

		if (aPage == 0) {
			setStatement(lStatement);
		} else {
			// lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
			// + " ) INNER ) WHERE rn between " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
			// + " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
			// MEV_6: cambiata la paginazione
			lPaginedStatement = convertStatementToNewPaginedStatement(aPage, lStatement);
			setStatement(lPaginedStatement);
		}
	}

	public void getCountFascicoliSoggettoUfficio(FascicoloSiepModel aModel, String aCodUfficio)
			throws DAOException {
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM V_FASCICOLO_SIEP WHERE CHIAVE_UFFICIO = '"
				+ aCodUfficio + "' AND ";

		lStatement += " " + setCondizioneSoggetto(aModel);

		setStatement(lStatement);
	}

	public void ricercaFascicoloSoggetto(FascicoloSiepModel aModel) throws DAOException {
		String lStatement = getFascicoloSqlQuery();

		lStatement += " " + setCondizioneSoggetto(aModel);
		lStatement += " " + setOrder();

		setStatement(lStatement);
	}

	private String setCondizioneSoggetto(FascicoloSiepModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito=false;

		lCondizioni += "  ID_SOGGETTO = " + aModel.getSogIdSoggetto() + "";

		if ((aModel.getSenIdSentenza() != null)) {
			lCondizioni += "   AND ID_SENTENZA = " + aModel.getSenIdSentenza() + "";
		}
		if ((aModel.getIdFascicoloSiep() != null)) {
			lCondizioni += " AND ID_FASCICOLO_SIEP = " + aModel.getIdFascicoloSiep() + "";
		}
		if ((aModel.getChiaveAnno() != null)) {
			lCondizioni += " AND CHIAVE_ANNO = " + aModel.getChiaveAnno() + "";
		}
		if ((aModel.getChiaveProgr() != null)) {
			lCondizioni += " AND CHIAVE_PROGR = " + aModel.getChiaveProgr() + "";
		}

		return lCondizioni;
	}

	private String setCondizioneSoggettoUffDistr(FascicoloSiepModel aModel, String strTipoRicerca) {
		String lCondizioni = new String();

		lCondizioni += "  AND FASCICOLO_SIEP.SOG_ID_SOGGETTO = " + aModel.getSogIdSoggetto() + "";
		// usa come chiave di ricerca l'Ufficio (di apparteneza operatore)
		// se nella form di ricerca è stato scelto "Nell'Ufficio"
		if (strTipoRicerca != null) {
			if (strTipoRicerca.equals("ufficio")) {
				if ((aModel.getChiaveUfficio() != null)) {
					lCondizioni += " AND FASCICOLO_SIEP.CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "'";
				}
			}
		}

		return lCondizioni;
	}

	/**
	 * Setta le condizioni per la Ricerca
	 *
	 * @param aModel
	 * @return
	 */
	private String setCondizione(FascicoloSiepModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito=false;

		if (aModel.getCodUfficioInserimento() != null && !aModel.getCodUfficioInserimento().equals("")) {
			lCondizioni += "COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
		} else {
			lCondizioni += "CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "' ";
		}

		if ((aModel.getSenIdSentenza() != null)) {
			lCondizioni += " AND ID_SENTENZA = " + aModel.getSenIdSentenza() + "";
		}
		if ((aModel.getSogIdSoggetto() != null)) {
			lCondizioni += " AND ID_SOGGETTO = " + aModel.getSogIdSoggetto() + "";
		}
		if ((aModel.getIdFascicoloSiep() != null)) {
			lCondizioni += " AND ID_FASCICOLO_SIEP = " + aModel.getIdFascicoloSiep() + "";
		}
		if ((aModel.getChiaveAnno() != null)) {
			lCondizioni += " AND CHIAVE_ANNO = " + aModel.getChiaveAnno() + "";
		}
		if ((aModel.getChiaveProgr() != null)) {
			lCondizioni += " AND CHIAVE_PROGR = " + aModel.getChiaveProgr() + "";
		}
		// 26/06/2009 Introduzione filtro FLAG_VALIDATO per Ricerca Altre BDI (solo fascicoli validati)
		if ((aModel.getFlagValidato() != null) && aModel.getFlagValidato().length() > 0) {
			lCondizioni += " AND FLAG_VALIDATO = '" + aModel.getFlagValidato() + "'";
		}

		if ((aModel.getDataIscrizione() != null)) {
			lCondizioni += " AND DATA_ISCRIZIONE = TO_DATE('"
					+ DateUtils.getDateToString(aModel.getDataIscrizione(), "ddMMyyyy") + "', 'DDMMYYYY') ";
		}

		// Cerca i fascicoli a partire da una data
		if ((aModel.getDataIscrizioneIniziale() != null)) {
			lCondizioni += " AND ( DATA_ISCRIZIONE >= TO_DATE('"
					+ DateUtils.getDateToString(aModel.getDataIscrizioneIniziale(), "ddMMyyyy")
					+ "', 'DDMMYYYY')) ";
		}

		if ((aModel.getDataIscrizioneFinale() != null)) {
			lCondizioni += " AND ( DATA_ISCRIZIONE <= TO_DATE('"
					+ DateUtils.getDateToString(aModel.getDataIscrizioneFinale(), "ddMMyyyy")
					+ "', 'DDMMYYYY')) ";
		}

		// Cerca i fascicoli a partire da una coppia Progressivo/Anno
		if ((aModel.getChiaveAnnoIniziale() != null) && (aModel.getChiaveAnnoIniziale().intValue() >= 0)
				&& (aModel.getChiaveProgrIniziale() != null)
				&& (aModel.getChiaveProgrIniziale().intValue() >= 0)) {
			lCondizioni += " AND ( (CHIAVE_ANNO > " + aModel.getChiaveAnnoIniziale() + ")";
			lCondizioni += " OR (CHIAVE_ANNO = " + aModel.getChiaveAnnoIniziale() + " AND CHIAVE_PROGR >= "
					+ aModel.getChiaveProgrIniziale() + "))";
		}
		// Cerca i fascicoli fino ad una coppia Progressivo/Anno
		if ((aModel.getChiaveAnnoFinale() != null) && (aModel.getChiaveAnnoFinale().intValue() >= 0)
				&& (aModel.getChiaveProgrFinale() != null)
				&& (aModel.getChiaveProgrFinale().intValue() >= 0)) {
			// Nel caso non venga specificata la coppia di ricerca iniziale,
			// vengono cercati i fascicoli
			// a partire dal primo fascicolo dell'anno finale specificato
			if ((aModel.getChiaveAnnoIniziale() == null)
					|| (aModel.getChiaveAnnoIniziale().intValue() <= 0)
							&& (aModel.getChiaveProgrIniziale() == null)
					|| (aModel.getChiaveProgrIniziale().intValue() <= 0)) {
				lCondizioni += " AND ( (CHIAVE_ANNO > " + aModel.getChiaveAnnoFinale() + ")";
				lCondizioni += " OR (CHIAVE_ANNO = " + aModel.getChiaveAnnoFinale()
						+ " AND CHIAVE_PROGR >= 1))";
			}

			lCondizioni += " AND ( (CHIAVE_ANNO < " + aModel.getChiaveAnnoFinale() + ")";
			lCondizioni += " OR (CHIAVE_ANNO = " + aModel.getChiaveAnnoFinale() + " AND CHIAVE_PROGR <= "
					+ aModel.getChiaveProgrFinale() + "))";
		}
		if ((aModel.getFlagValidato() != null && aModel.getFlagValidato().equals("N"))) {
			lCondizioni += " AND (FLAG_VALIDATO ='N' OR FLAG_VALIDATO IS NULL)";
		}

		if (aModel.getClassiFascicolo() != null && aModel.getClassiFascicolo().length > 0) {
			String[] lClassiFascicolo = aModel.getClassiFascicolo();
			for (int i = 0; i < lClassiFascicolo.length; i++) {
				if (i != 0) {
					lCondizioni += " OR ";
				} else {
					lCondizioni += " AND (";
				}

				int lTipoClasse = Integer.parseInt(lClassiFascicolo[i]);
				if (lTipoClasse > 1) {
					lCondizioni += " (CHIAVE_PROGR BETWEEN " + (lTipoClasse * 10000) + " AND "
							+ (lTipoClasse * 10000 + 9999) + " ) ";
				} else // TipoProgressivo =1
				{
					lCondizioni += " (CHIAVE_PROGR BETWEEN 1 AND " + (lTipoClasse * 10000 + 9999) + " ) ";
				}
			}

			lCondizioni += " ) ";
		}

		return lCondizioni;
	}

	private String setOrder() {
		String lOrder = new String();

		lOrder = " ORDER BY CHIAVE_ANNO, CHIAVE_PROGR";

		return lOrder;
	}

	/**
	 *
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
		SoggettoModel lSoggetto = new SoggettoModel();
		SentenzaModel lSentenza = new SentenzaModel();

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setSogIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lFascicolo.setSenIdSentenza(getBigDecimal("ID_SENTENZA"));
		lFascicolo.setFlagValidato(getString("FLAG_VALIDATO"));
		lFascicolo.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		lFascicolo.setDescrStatoProcedimento(getString("STATO_PROCEDIMENTO"));
		lFascicolo.setCodDistretto(getString("COD_DISTRETTO"));
		lFascicolo.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		lFascicolo.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSentenza.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		lSentenza.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO"));
		lSentenza.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE"));
		lSentenza.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
		lFascicolo.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA"));
		lFascicolo.setDataArrivoAtto(getDate("DATA_ARRIVO_ATTO"));

		lFascicolo.setSoggetto(lSoggetto);
		lFascicolo.setSentenza(lSentenza);
		return lFascicolo;
	}

	/**
	 * Setta le condizioni per la Ricerca
	 *
	 * @param aModel
	 * @return
	 */
	private String setCondizionePGCAP(FascicoloSiepModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito=false;

		if (aModel.getCodUfficioInserimento() != null && !aModel.getCodUfficioInserimento().equals("")) {
			lCondizioni += "(COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento()
					+ "' or (nvl(vse.eta_ora, 18) < 18 and vfs.flag_validato = 'S') ) ";
		} else {
			lCondizioni += "(CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio()
					+ "' or (nvl(vse.eta_ora, 18) < 18 and vfs.flag_validato = 'S') ) ";
		}

		if ((aModel.getSenIdSentenza() != null)) {
			lCondizioni += " AND ID_SENTENZA = " + aModel.getSenIdSentenza() + "";
		}
		if ((aModel.getSogIdSoggetto() != null)) {
			lCondizioni += " AND ID_SOGGETTO = " + aModel.getSogIdSoggetto() + "";
		}
		if ((aModel.getIdFascicoloSiep() != null)) {
			lCondizioni += " AND ID_FASCICOLO_SIEP = " + aModel.getIdFascicoloSiep() + "";
		}
		if ((aModel.getChiaveAnno() != null)) {
			lCondizioni += " AND CHIAVE_ANNO = " + aModel.getChiaveAnno() + "";
		}
		if ((aModel.getChiaveProgr() != null)) {
			lCondizioni += " AND CHIAVE_PROGR = " + aModel.getChiaveProgr() + "";
		}
		// 26/06/2009 Introduzione filtro FLAG_VALIDATO per Ricerca Altre BDI (solo fascicoli validati)
		if ((aModel.getFlagValidato() != null) && aModel.getFlagValidato().length() > 0) {
			lCondizioni += " AND FLAG_VALIDATO = '" + aModel.getFlagValidato() + "'";
		}

		if ((aModel.getDataIscrizione() != null)) {
			lCondizioni += " AND DATA_ISCRIZIONE = TO_DATE('"
					+ DateUtils.getDateToString(aModel.getDataIscrizione(), "ddMMyyyy") + "', 'DDMMYYYY') ";
		}

		// Cerca i fascicoli a partire da una data
		if ((aModel.getDataIscrizioneIniziale() != null)) {
			lCondizioni += " AND ( DATA_ISCRIZIONE >= TO_DATE('"
					+ DateUtils.getDateToString(aModel.getDataIscrizioneIniziale(), "ddMMyyyy")
					+ "', 'DDMMYYYY')) ";
		}

		if ((aModel.getDataIscrizioneFinale() != null)) {
			lCondizioni += " AND ( DATA_ISCRIZIONE <= TO_DATE('"
					+ DateUtils.getDateToString(aModel.getDataIscrizioneFinale(), "ddMMyyyy")
					+ "', 'DDMMYYYY')) ";
		}

		// Cerca i fascicoli a partire da una coppia Progressivo/Anno
		if ((aModel.getChiaveAnnoIniziale() != null) && (aModel.getChiaveAnnoIniziale().intValue() >= 0)
				&& (aModel.getChiaveProgrIniziale() != null)
				&& (aModel.getChiaveProgrIniziale().intValue() >= 0)) {
			lCondizioni += " AND ( (CHIAVE_ANNO > " + aModel.getChiaveAnnoIniziale() + ")";
			lCondizioni += " OR (CHIAVE_ANNO = " + aModel.getChiaveAnnoIniziale() + " AND CHIAVE_PROGR >= "
					+ aModel.getChiaveProgrIniziale() + "))";
		}
		// Cerca i fascicoli fino ad una coppia Progressivo/Anno
		if ((aModel.getChiaveAnnoFinale() != null) && (aModel.getChiaveAnnoFinale().intValue() >= 0)
				&& (aModel.getChiaveProgrFinale() != null)
				&& (aModel.getChiaveProgrFinale().intValue() >= 0)) {
			// Nel caso non venga specificata la coppia di ricerca iniziale,
			// vengono cercati i fascicoli
			// a partire dal primo fascicolo dell'anno finale specificato
			if ((aModel.getChiaveAnnoIniziale() == null)
					|| (aModel.getChiaveAnnoIniziale().intValue() <= 0)
							&& (aModel.getChiaveProgrIniziale() == null)
					|| (aModel.getChiaveProgrIniziale().intValue() <= 0)) {
				lCondizioni += " AND ( (CHIAVE_ANNO > " + aModel.getChiaveAnnoFinale() + ")";
				lCondizioni += " OR (CHIAVE_ANNO = " + aModel.getChiaveAnnoFinale()
						+ " AND CHIAVE_PROGR >= 1))";
			}

			lCondizioni += " AND ( (CHIAVE_ANNO < " + aModel.getChiaveAnnoFinale() + ")";
			lCondizioni += " OR (CHIAVE_ANNO = " + aModel.getChiaveAnnoFinale() + " AND CHIAVE_PROGR <= "
					+ aModel.getChiaveProgrFinale() + "))";
		}
		if ((aModel.getFlagValidato() != null && aModel.getFlagValidato().equals("N"))) {
			lCondizioni += " AND (FLAG_VALIDATO ='N' OR FLAG_VALIDATO IS NULL)";
		}

		if (aModel.getClassiFascicolo() != null && aModel.getClassiFascicolo().length > 0) {
			String[] lClassiFascicolo = aModel.getClassiFascicolo();
			for (int i = 0; i < lClassiFascicolo.length; i++) {
				if (i != 0) {
					lCondizioni += " OR ";
				} else {
					lCondizioni += " AND (";
				}

				int lTipoClasse = Integer.parseInt(lClassiFascicolo[i]);
				if (lTipoClasse > 1) {
					lCondizioni += " (CHIAVE_PROGR BETWEEN " + (lTipoClasse * 10000) + " AND "
							+ (lTipoClasse * 10000 + 9999) + " ) ";
				} else // TipoProgressivo =1
				{
					lCondizioni += " (CHIAVE_PROGR BETWEEN 1 AND " + (lTipoClasse * 10000 + 9999) + " ) ";
				}
			}

			lCondizioni += " ) ";
		}

		return lCondizioni;
	}

}