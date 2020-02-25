package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.StringUtils;
import siap.dao.SIAPSqlDAO;
import siap.siep.modulocumulo.model.ReatoCumuloModel;

/**
 * <p>
 * Title: ReatoCumuloSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Reato_Cumulo
 * </p>
 * 
 * @version 4.0
 */
public class ReatoCumuloSqlDAO extends SIAPSqlDAO {

	public ReatoCumuloSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaReatoCumulo(ReatoCumuloModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	/**
	 * Ricerca tutti i reati associati a un titolo
	 * 
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaReatiCumuloByIdTitolo(BigDecimal aIdTitolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	/**
	 * 
	 * @param aKey
	 * @param aKeyCum
	 * @throws DAOException
	 */
	public void ricercaReatiCumNoCircostanzaByIdTitolo(BigDecimal aIdTitolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND PROGR_CIRCOSTANZA = 1 "; // I record con PROGR_CIRCOSTANZA = 1 sono reati e non
												// circostanze
		lSql += " AND FLAG_STATO != 'C' "; // Non vanno presi i Reati Cancellati o Annullati
		lSql += " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	public void ricercaCircostanzeReatoCumByReatoTitoloCum(BigDecimal aProgrReato, BigDecimal aKeyTitolo)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND PROGR_REATO = " + aProgrReato;
		lSql += " AND PROGR_CIRCOSTANZA != 1"; // I record con PROGR_CIRCOSTANZA > di 1 sono circostanze e non
												// reati
		lSql += " AND FLAG_STATO != 'C' "; // Non vanno presi i ReatiCircostanza Cancellati o Annullati
		lSql += " AND TIT_ID_TITOLO_CUMULATO = " + aKeyTitolo;
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	/**
	 * 
	 * @param aModel
	 * @throws DAOException
	 * @deprecated Da capire cosa fa
	 */
	public void ricercaReatiCumulo(ReatoCumuloModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneCount(aModel);
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	public void ricercaReatoCumuloByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	/**
	 * Restituisce il numero di fascicoli siep che hanno un reato che ricade nei criteri indicati nel model
	 * Qui nel cumulo non sembra essere molto utile
	 * 
	 * @deprecated da capire a che serve
	 */
	public void getCountReatiCumulo(ReatoCumuloModel aModel) throws DAOException {
		String lStatement = "SELECT COUNT (a.ID_FASCICOLO_SIEP) howmanyrecords ";

		lStatement += "  FROM (SELECT DISTINCT ID_FASCICOLO_SIEP "
				+ " FROM FASCICOLO_SIEP FASC, REATO_CUMULO, SOGGETTO SOGG, UFFICIO UFF, CG_REF_CODES DESCR_TIPO_UFF, COMUNE DESCR_COM_UFF";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIEP = REATO_CUMULO.FAS_SIE_ID_FASCICOLO_SIEP ";
		lStatement += "  AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO  ";
		lStatement += "  AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO ";
		lStatement += "  AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += "  AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE ";
		lStatement += "  AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";

		lStatement += setCondizioneCount(aModel);
		lStatement += " ) a";

		setStatement(lStatement);

	}

	/**
	 * Restituisce il Max Progressivo attuale (PROGR_REATO) per i reati associati al titolo
	 * 
	 * @param aIdTitolo
	 * @return - Il max Progressivo
	 * @throws DAOException
	 */
	public BigDecimal getProgressivoReatoCumulo(BigDecimal aIdTitolo) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(PROGR_REATO) aMAX";
		lStatement += " FROM REATO_CUMULO";
		lStatement += " WHERE TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;

		setStatement(lStatement);

		this.start();

		BigDecimal lProgressivo = null;
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			lProgressivo = this.getBigDecimal("aMAX");

		this.stop();

		if (lProgressivo == null)
			lProgressivo = new BigDecimal(0);

		return lProgressivo;
	}

	/**
	 * Restituisce il max progressivo circostanza (PROGR_CIRCOSTANZA) per il reato con progressivo indicato
	 * (aProgReato)
	 * 
	 * @param aProgReato
	 * @param aKeyFas
	 * @return
	 * @throws DAOException
	 */
	public BigDecimal getProgressivoCircostanzaCumulo(BigDecimal aProgReato, BigDecimal aIdTitolo)
			throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(PROGR_CIRCOSTANZA) aMAX";
		lStatement += " FROM REATO_CUMULO";
		lStatement += " WHERE PROGR_REATO = " + aProgReato;
		lStatement += " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;

		setStatement(lStatement);

		this.start();

		BigDecimal lProgressivo = null;
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			lProgressivo = this.getBigDecimal("aMAX");

		this.stop();

		if (lProgressivo == null)
			lProgressivo = new BigDecimal(0);

		return lProgressivo;
	}

	// =================================================================================================
	// Ricerca per Tit_Id_Titolo_Cumulato in Join con RICHPM_REATO_CUM
	// =================================================================================================
	public void ricercaReatiCumNoCircostanzaByIdTitoloRichGE(BigDecimal aIdTitolo, BigDecimal aIdRichGe)
			throws DAOException {
		String lSql = getSqlQueryJoinRichiestaGE();

		lSql += " AND PROGR_CIRCOSTANZA = 1 "; // I record con PROGR_CIRCOSTANZA = 1 sono reati e non
												// circostanze
		lSql += " AND FLAG_STATO != 'C' "; // Non vanno presi i Reati Cancellati o Annullati
		lSql += " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;
		lSql += " AND RICHPM_REATO_CUM.RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aIdRichGe;
		lSql += " AND RICHPM_REATO_CUM.REA_ID_REATO_CUMULO = ID_REATO_CUM ";
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	// Stringa della QUERY in Join con RICHPM_REATO_CUM
	protected String getSqlQueryJoinRichiestaGE() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_REATO_CUM, "
				+ "COD_TIPO_REATO, DECOTIPOREATO.RV_MEANING DESCTIPOREATO, " + "DATA_REATO, "
				+ "PROGR_NUMERO_MANUALE, " + "PROGR_REATO, " + "PROGR_CIRCOSTANZA, " + "DATA_INIZIO, "
				+ "ANNO_INIZIO, " + "MESE_INIZIO, " + "GIORNO_INIZIO, " + "DATA_FINE, " + "ANNO_FINE, "
				+ "MESE_FINE, " + "GIORNO_FINE, "
				+ "COD_PERIODO_CONSUMAZIONE, DECOPERCONS.RV_MEANING DESCPECONS, " + "DESC_LUOGO, "
				+ "COD_FONTE, DECOFONTE.RV_MEANING DESCFONTE, " + "ANNO_FONTE, " + "NUMERO_FONTE, "
				+ "ARTICOLO, " + "COD_SOTTONUMERAZIONE, DECOSOTTONUM.RV_MEANING DESCSOTTONUM, " + "COMMA, "
				+ "COMMA_QUALIFICANTE, DECCOMMAQUAL.RV_MEANING DESCCOMMAQUALIFICANTE, " + "LETTERA, "
				+ "NUMERO, " + "COD_TIPO_PENA_DETENTIVA, DECOPENADET.RV_MEANING DESCPENADET, " + "NUM_ANNI, "
				+ "NUM_MESI, " + "NUM_GIORNI, "
				+ "COD_TIPO_SANZIONE, DECTIPOSANZIONE.RV_MEANING DESCTIPOSANZIONE, " + "SANZIONE_PECUNIARIA, "
				+ "FLAG_ERGASTOLO, " + "ANNI_ISOLAMENTO_DIURNO, " + "MESI_ISOLAMENTO_DIURNO, "
				+ "GIORNI_ISOLAMENTO_DIURNO, " +

				"ID_CONTINUAZIONE_REATO_CUM, " + "TIPO_CONTINUAZIONE_REATO, " + "KEY_REATO_NSC, " + "NOTE, " +

				"FLAG_STATO, " + "MOTIVO_MODIFICA, " + "TIT_ID_TITOLO_CUMULATO, " + "ID_REATO_ORIGINE, " +

				"COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO ";

		lStatement += " FROM REATO_CUMULO, RICHPM_REATO_CUM ";
		lStatement += " , CG_REF_CODES DECOTIPOREATO, CG_REF_CODES DECOPERCONS ";
		lStatement += " , CG_REF_CODES DECOSOTTONUM, CG_REF_CODES DECOPENADET, CG_REF_CODES DECOFONTE ";
		lStatement += " , CG_REF_CODES DECTIPOSANZIONE ";
		lStatement += " , CG_REF_CODES DECCOMMAQUAL ";
		lStatement += " WHERE ";
		lStatement += " DECOTIPOREATO.RV_DOMAIN='TIPO_REATO' AND DECOTIPOREATO.RV_LOW_VALUE=REATO_CUMULO.COD_TIPO_REATO ";
		lStatement += " AND DECOPERCONS.RV_DOMAIN='PERIODO_CONSUMAZIONE' AND DECOPERCONS.RV_LOW_VALUE=REATO_CUMULO.COD_PERIODO_CONSUMAZIONE ";
		lStatement += " AND DECOSOTTONUM.RV_DOMAIN='SOTTONUMERAZIONE' AND DECOSOTTONUM.RV_LOW_VALUE=REATO_CUMULO.COD_SOTTONUMERAZIONE ";
		lStatement += " AND DECOPENADET.RV_DOMAIN='TIPO_PENA_DETENTIVA' AND DECOPENADET.RV_LOW_VALUE=REATO_CUMULO.COD_TIPO_PENA_DETENTIVA ";
		lStatement += " AND DECOFONTE.RV_DOMAIN='FONTE' AND DECOFONTE.RV_LOW_VALUE=REATO_CUMULO.COD_FONTE ";
		lStatement += " AND DECTIPOSANZIONE.RV_DOMAIN='TIPO_SANZIONE' AND DECTIPOSANZIONE.RV_LOW_VALUE=REATO_CUMULO.COD_TIPO_SANZIONE ";
		lStatement += " AND DECCOMMAQUAL.RV_DOMAIN='SOTTONUMERAZIONE' AND DECCOMMAQUAL.RV_LOW_VALUE=REATO_CUMULO.COMMA_QUALIFICANTE ";

		return lStatement;
	}

	// ==========================================================================================================

	/**
	 * 
	 * @return
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_REATO_CUM, "
				+ "COD_TIPO_REATO, DECOTIPOREATO.RV_MEANING DESCTIPOREATO, " + "DATA_REATO, "
				+ "PROGR_NUMERO_MANUALE, " + "PROGR_REATO, " + "PROGR_CIRCOSTANZA, " + "DATA_INIZIO, "
				+ "ANNO_INIZIO, " + "MESE_INIZIO, " + "GIORNO_INIZIO, " + "DATA_FINE, " + "ANNO_FINE, "
				+ "MESE_FINE, " + "GIORNO_FINE, "
				+ "COD_PERIODO_CONSUMAZIONE, DECOPERCONS.RV_MEANING DESCPECONS, " + "DESC_LUOGO, "
				+ "COD_FONTE, DECOFONTE.RV_MEANING DESCFONTE, " + "ANNO_FONTE, " + "NUMERO_FONTE, "
				+ "ARTICOLO, " + "COD_SOTTONUMERAZIONE, DECOSOTTONUM.RV_MEANING DESCSOTTONUM, " + "COMMA, "
				+ "COMMA_QUALIFICANTE, DECCOMMAQUAL.RV_MEANING DESCCOMMAQUALIFICANTE, " + "LETTERA, "
				+ "NUMERO, " + "COD_TIPO_PENA_DETENTIVA, DECOPENADET.RV_MEANING DESCPENADET, " + "NUM_ANNI, "
				+ "NUM_MESI, " + "NUM_GIORNI, "
				+ "COD_TIPO_SANZIONE, DECTIPOSANZIONE.RV_MEANING DESCTIPOSANZIONE, " + "SANZIONE_PECUNIARIA, "
				+ "FLAG_ERGASTOLO, " + "ANNI_ISOLAMENTO_DIURNO, " + "MESI_ISOLAMENTO_DIURNO, "
				+ "GIORNI_ISOLAMENTO_DIURNO, " +

				"ID_CONTINUAZIONE_REATO_CUM, " + "TIPO_CONTINUAZIONE_REATO, " + "KEY_REATO_NSC, " + "NOTE, " +

				"FLAG_STATO, " + "MOTIVO_MODIFICA, " + "TIT_ID_TITOLO_CUMULATO, " + "ID_REATO_ORIGINE, " +

				"COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO ";

		lStatement += " FROM REATO_CUMULO ";
		lStatement += " , CG_REF_CODES DECOTIPOREATO, CG_REF_CODES DECOPERCONS ";
		lStatement += " , CG_REF_CODES DECOSOTTONUM, CG_REF_CODES DECOPENADET, CG_REF_CODES DECOFONTE ";
		lStatement += " , CG_REF_CODES DECTIPOSANZIONE ";
		lStatement += " , CG_REF_CODES DECCOMMAQUAL ";
		lStatement += " WHERE ";
		lStatement += " DECOTIPOREATO.RV_DOMAIN='TIPO_REATO' AND DECOTIPOREATO.RV_LOW_VALUE=REATO_CUMULO.COD_TIPO_REATO ";
		lStatement += " AND DECOPERCONS.RV_DOMAIN='PERIODO_CONSUMAZIONE' AND DECOPERCONS.RV_LOW_VALUE=REATO_CUMULO.COD_PERIODO_CONSUMAZIONE ";
		lStatement += " AND DECOSOTTONUM.RV_DOMAIN='SOTTONUMERAZIONE' AND DECOSOTTONUM.RV_LOW_VALUE=REATO_CUMULO.COD_SOTTONUMERAZIONE ";
		lStatement += " AND DECOPENADET.RV_DOMAIN='TIPO_PENA_DETENTIVA' AND DECOPENADET.RV_LOW_VALUE=REATO_CUMULO.COD_TIPO_PENA_DETENTIVA ";
		lStatement += " AND DECOFONTE.RV_DOMAIN='FONTE' AND DECOFONTE.RV_LOW_VALUE=REATO_CUMULO.COD_FONTE ";
		lStatement += " AND DECTIPOSANZIONE.RV_DOMAIN='TIPO_SANZIONE' AND DECTIPOSANZIONE.RV_LOW_VALUE=REATO_CUMULO.COD_TIPO_SANZIONE ";
		lStatement += " AND DECCOMMAQUAL.RV_DOMAIN='SOTTONUMERAZIONE' AND DECCOMMAQUAL.RV_LOW_VALUE=REATO_CUMULO.COMMA_QUALIFICANTE ";

		return lStatement;
	}

	/**
	 * Restituisce il Max Id Continuazione Reato per il titolo passato in input
	 * 
	 * @param idFas
	 * @return
	 * @throws DAOException
	 */
	public BigDecimal GetMaxIdContinuazione(BigDecimal idTitolo) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(ID_CONTINUAZIONE_REATO_CUM) aMAX";
		lStatement += " FROM REATO_CUMULO";
		lStatement += " WHERE TIT_ID_TITOLO_CUMULATO = " + idTitolo;

		setStatement(lStatement);

		this.start();

		BigDecimal maxId = null;
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			maxId = this.getBigDecimal("aMAX");

		this.stop();

		if (maxId == null)
			maxId = new BigDecimal(0);

		return maxId;
	}

	//
	// METODO GET MODEL()
	//

	public GenericModel getModel() throws DAOException {
		ReatoCumuloModel aModel = new ReatoCumuloModel();

		aModel.setIdReatoCum(getBigDecimal("ID_REATO_CUM"));
		aModel.setCodTipoReato(getString("COD_TIPO_REATO"));
		aModel.setDescrTipoReato(getString("DESCTIPOREATO"));
		aModel.setDataReato(getDate("DATA_REATO"));
		aModel.setProgrNumeroManuale(getString("PROGR_NUMERO_MANUALE"));
		aModel.setProgrReato(getBigDecimal("PROGR_REATO"));
		aModel.setProgrCircostanza(getBigDecimal("PROGR_CIRCOSTANZA"));

		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setAnnoInizio(getBigDecimal("ANNO_INIZIO"));
		aModel.setMeseInizio(getBigDecimal("MESE_INIZIO"));
		aModel.setGiornoInizio(getBigDecimal("GIORNO_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setAnnoFine(getBigDecimal("ANNO_FINE"));
		aModel.setMeseFine(getBigDecimal("MESE_FINE"));
		aModel.setGiornoFine(getBigDecimal("GIORNO_FINE"));
		aModel.setCodPeriodoConsumazione(getString("COD_PERIODO_CONSUMAZIONE"));
		aModel.setDescrPeriodoConsumazioneCum(getString("DESCPECONS"));
		aModel.setDescLuogo(getString("DESC_LUOGO"));

		aModel.setCodFonte(getString("COD_FONTE"));
		aModel.setDescrFonte(getString("DESCFONTE"));
		aModel.setAnnoFonte(getBigDecimal("ANNO_FONTE"));
		aModel.setNumeroFonte(getString("NUMERO_FONTE"));
		aModel.setCodSottonumerazione(getString("COD_SOTTONUMERAZIONE"));
		aModel.setDescrSottonumerazione(getString("DESCSOTTONUM"));
		aModel.setComma(getString("COMMA"));
		aModel.setCommaQualificante(getString("COMMA_QUALIFICANTE"));
		aModel.setDescrCommaQualificante(getString("DESCCOMMAQUALIFICANTE"));
		aModel.setLettera(getString("LETTERA"));
		aModel.setNumero(getString("NUMERO"));
		aModel.setArticolo(getString("ARTICOLO"));

		aModel.setCodTipoPenaDetentiva(getString("COD_TIPO_PENA_DETENTIVA"));
		aModel.setDescrTipoPenaDetentiva(getString("DESCPENADET"));
		aModel.setNumAnni(getBigDecimal("NUM_ANNI"));
		aModel.setNumMesi(getBigDecimal("NUM_MESI"));
		aModel.setNumGiorni(getBigDecimal("NUM_GIORNI"));
		aModel.setCodTipoSanzione(getString("COD_TIPO_SANZIONE"));
		aModel.setDescrTipoSanzione(getString("DESCTIPOSANZIONE"));
		aModel.setSanzionePecuniaria(getBigDecimal("SANZIONE_PECUNIARIA"));
		aModel.setFlagErgastolo(getString("FLAG_ERGASTOLO"));
		aModel.setNumAnniIsolamentoDiurno(getBigDecimal("ANNI_ISOLAMENTO_DIURNO"));
		aModel.setNumMesiIsolamentoDiurno(getBigDecimal("MESI_ISOLAMENTO_DIURNO"));
		aModel.setNumGiorniIsolamentoDiurno(getBigDecimal("GIORNI_ISOLAMENTO_DIURNO"));

		aModel.setIdContinuazioneReatoCum(getBigDecimal("ID_CONTINUAZIONE_REATO_CUM"));
		aModel.setTipoContinuazioneReato(getString("TIPO_CONTINUAZIONE_REATO"));
		aModel.setKeyReatoNsc(getBigDecimal("KEY_REATO_NSC"));
		aModel.setNote(getString("NOTE"));

		aModel.setFlagStato(getString("FLAG_STATO"));
		aModel.setMotivoModificaNote(getString("MOTIVO_MODIFICA"));
		aModel.setTitIdTitoloCumulato(getBigDecimal("TIT_ID_TITOLO_CUMULATO"));
		aModel.setIdReatoOrigine(getBigDecimal("ID_REATO_ORIGINE"));

		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));

		aModel.calcolaStringaConsumazioneCum();

		return aModel;
	}

	/**
	 * Imposta la condizione di ricerca per IdTitolo e PROGR_REATO
	 * 
	 * @param aModel
	 * @return
	 */
	public String setCondizione(ReatoCumuloModel aModel) {
		String lCondizioni = new String("");

		if (aModel.getTitIdTitoloCumulato() != null) {
			lCondizioni += " AND TIT_ID_TITOLO_CUMULATO=" + aModel.getTitIdTitoloCumulato();
		}
		if (aModel.getProgrReato() != null) {
			lCondizioni += " AND PROGR_REATO=" + aModel.getProgrReato();
		}

		return lCondizioni;
	}

	/**
	 * Imposta le condizioni di ricerca per la select count distinct
	 * 
	 * @param aRm
	 * @return
	 * @deprecated da verificare se significativa
	 */
	public String setCondizioneCount(ReatoCumuloModel aRm) {

		String lCondizioni = new String();

		if (!aRm.getCodFonte().equals("") && !aRm.getCodFonte().equals("-")) {
			lCondizioni += " AND REATO_CUMULO.COD_FONTE = '" + StringUtils.convertSqlString(aRm.getCodFonte())
					+ "'";
		}

		if (aRm.getAnnoFonte() != null && !aRm.getAnnoFonte().toString().equals("")) {
			lCondizioni += " AND REATO_CUMULO.ANNO_FONTE = '" + aRm.getAnnoFonte() + "'";
		}

		if (!aRm.getNumeroFonte().equals("")) {
			lCondizioni += " AND REATO_CUMULO.NUMERO_FONTE = '"
					+ StringUtils.convertSqlString(aRm.getNumeroFonte()) + "'";
		}

		if (!aRm.getCodSottonumerazione().equals("") && !aRm.getCodSottonumerazione().equals("-")) {
			lCondizioni += " AND REATO_CUMULO.COD_SOTTONUMERAZIONE = '"
					+ StringUtils.convertSqlString(aRm.getCodSottonumerazione()) + "'";
		}

		if (!aRm.getComma().equals("")) {
			lCondizioni += " AND REATO_CUMULO.COMMA = '" + StringUtils.convertSqlString(aRm.getComma()) + "'";
		}

		if (!aRm.getCommaQualificante().equals("") && !aRm.getCommaQualificante().equals("-")) {
			lCondizioni += " AND REATO_CUMULO.COMMA_QUALIFICANTE = '"
					+ StringUtils.convertSqlString(aRm.getCommaQualificante()) + "'";
		}

		if (!aRm.getLettera().equals("")) {
			lCondizioni += " AND REATO_CUMULO.LETTERA = '" + StringUtils.convertSqlString(aRm.getLettera())
					+ "'";
		}

		if (!aRm.getNumero().equals("")) {
			lCondizioni += " AND REATO_CUMULO.NUMERO = '" + StringUtils.convertSqlString(aRm.getNumero())
					+ "'";
		}

		if (!aRm.getArticolo().equals("")) {
			lCondizioni += " AND REATO_CUMULO.ARTICOLO = '" + StringUtils.convertSqlString(aRm.getArticolo())
					+ "'";
		}

		if (!aRm.getCodUfficioInserimento().equals("")) {
			lCondizioni += " AND FASC.COD_UFFICIO_INSERIMENTO = '"
					+ StringUtils.convertSqlString(aRm.getCodUfficioInserimento()) + "'";
		}

		// if (aRm.getFasSieIdFascicoloSiep()!= null)
		// {
		// lCondizioni += " AND REATO_CUMULO.TIT_ID_TITOLO_CUMULATO = '"+aRm.getTitIdTitoloCumulato() + "'";
		// }

		if (aRm.getProgrReato() != null) {
			lCondizioni += " AND REATO_CUMULO.PROGR_REATO = '" + aRm.getProgrReato() + "'";
		}

		if (aRm.getFlagStato() != null) {
			lCondizioni += " AND REATO_CUMULO.FLAG_STAO = '" + aRm.getFlagStato() + "'";
		}

		// if (aRm.getCumIdCumulo()!= null)
		// {
		// lCondizioni += " AND REATO_CUMULO.CUM_ID_CUMULO = '"+aRm.getCumIdCumulo() + "'";
		// }

		if (aRm.getMotivoModificaNote() != null) {
			lCondizioni += " AND REATO_CUMULO.MOTIVO_MODIFICA = '" + aRm.getMotivoModificaNote() + "'";
		}

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_REATO_CUM = " + aKey;
	}

	public String setOrder() {
		return " ORDER BY PROGR_REATO, PROGR_CIRCOSTANZA";
	}

	public String setOrderReato() {
		return " ORDER BY PROGR_REATO ";
	}

	/**
	 * Metodo per ricercare il massimo progressivo dei reati associati ad un fascicolo.
	 * 
	 * @param idFas
	 * @return
	 * @throws DAOException
	 */
	public int getMaxProgrReato(BigDecimal idTitolo) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(PROGR_REATO) aMAX";
		lStatement += " FROM REATO_CUMULO";
		lStatement += " WHERE TIT_ID_TITOLO_CUMULATO = " + idTitolo;

		setStatement(lStatement);

		this.start();

		int maxId = 0;
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			maxId = this.getInt("aMAX");

		this.stop();

		return maxId;
	}

}