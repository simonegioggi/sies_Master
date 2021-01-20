package siap.sico.soggetto.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.util.MinorMask;

/**
 * <p>
 * Title: SoggettoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Soggetto
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
public class SoggettoSqlDAO extends SIAPSqlDAO {

	public SoggettoSqlDAO(Connection con) {
		super(con);
	}

	public void getCountSoggetti(SoggettoModel aModel, String aCodDistretto) throws DAOException {

		String lStatement = "SELECT COUNT(*) HowManyRecords " + " FROM SOGGETTO " +

				" WHERE  COD_UFFICIO_INSERIMENTO IN (SELECT cod_ufficio FROM UFFICIO WHERE COD_DISTRETTO='"
				+ aCodDistretto + "') ";

		lStatement += this.setCondizione(aModel);

		setStatement(lStatement);
	}

	// Paolo cherubini x SuperSoggetto
	public void getCountFascicoliPerSoggetto(BigDecimal aIdSoggetto) throws DAOException {
		String lStatement = "SELECT (conta_siep + conta_sige + conta_sius) HowManyRecords "
				+ "FROM (SELECT COUNT (*) conta_siep FROM fascicolo_siep "
				+ "WHERE fascicolo_siep.sog_id_soggetto = " + aIdSoggetto + ") siep, "
				+ "(SELECT COUNT (*) conta_sige FROM fascicolo_sige "
				+ "WHERE fascicolo_sige.sog_id_soggetto = " + aIdSoggetto + ") sige, "
				+ "(SELECT COUNT (*) conta_sius FROM fascicolo_sius "
				+ "WHERE fascicolo_sius.sog_id_soggetto = " + aIdSoggetto + ") sius ";
		setStatement(lStatement);
	}

	// fine Paolo x SuperSoggetto

	public void getEtaSoggetto(BigDecimal aIdSoggetto) throws DAOException {
		String lStatement = "select t.eta_ora from V_SOGGETTO_ETA t " + "WHERE t.COD_SOGGETTO = "
				+ aIdSoggetto + "";
		setStatement(lStatement);
	}

	public void ricercaSoggettoByKey(BigDecimal aKey) throws DAOException {
		String lStatement = new String(getSoggettoSqlQuery());

		lStatement += " AND ID_SOGGETTO = " + aKey;
		setStatement(lStatement);
	}

	public void ricercaSuperSoggetto(String TipoRicerca, String strCodUfficioUtenteConnesso,
			SoggettoModel aModel, String StrFascicolo, String strCodDistrettoUtenteConnesso) {
		ricercaSuperSoggetto(TipoRicerca, strCodUfficioUtenteConnesso, aModel, StrFascicolo,
				strCodDistrettoUtenteConnesso, "", false, "");
	}

	/**
	 * creata da Paolo cherubini 29/07/2009 Esegue la ricerca dei fascicoli in base al super soggetto
	 *
	 * @param soggetto
	 *            model
	 */
	public void ricercaSuperSoggetto(String TipoRicerca, String strCodUfficioUtenteConnesso,
			SoggettoModel aModel, String StrFascicolo, String strCodDistrettoUtenteConnesso,
			String majorOffice, boolean fromDetail, String tipoUfficio) {
		String lStatement = new String(
				getSuperSoggettoSqlQuery(StrFascicolo, strCodDistrettoUtenteConnesso, majorOffice));

		// 09/2011 Introduzione filtro FLAG_VALIDATO per Ricerca Altre BDI (solo fascicoli validati)
		// Per segnalazione durante periodo garanzia

		if (StrFascicolo.compareTo("FASCICOLO_SIEP") == 0) {
			lStatement += " AND (CHIAVE_UFFICIO = '" + strCodUfficioUtenteConnesso
					+ "' OR (CHIAVE_UFFICIO != '" + strCodUfficioUtenteConnesso
					+ "' AND FLAG_VALIDATO = 'S'))";
		}

		if (aModel.getAnnoNascita() != null)
			lStatement += " AND soggetto.ANNO_NASCITA = '" + aModel.getAnnoNascita() + "'";
		else
			lStatement += " AND soggetto.ANNO_NASCITA is null";

		// MEV_39: risolta casistica per unix (l'atto di nascita comprende il carattere ' --> NA'00 287
		if (aModel.getAttoNascita() != null && aModel.getAttoNascita().length() > 0)
			lStatement += " AND soggetto.ATTO_NASCITA = '"
					+ StringUtils.convertSqlString(aModel.getAttoNascita()) + "'";
		else
			lStatement += " AND soggetto.ATTO_NASCITA is null";

		if (aModel.getCodAfis() != null && aModel.getCodAfis().length() > 0)
			lStatement += " AND soggetto.COD_AFIS = '" + aModel.getCodAfis() + "'";
		else
			lStatement += " AND soggetto.COD_AFIS is null";

		if (aModel.getCodComuneNascita() != null && aModel.getCodComuneNascita().length() > 0)
			lStatement += " AND soggetto.COD_COMUNE_NASCITA = '" + aModel.getCodComuneNascita() + "'";
		else
			lStatement += " AND soggetto.COD_COMUNE_NASCITA is null";

		if (aModel.getCodCs() != null && aModel.getCodCs().length() > 0)
			lStatement += " AND soggetto.COD_CS = '" + aModel.getCodCs() + "'";
		else
			lStatement += " AND soggetto.COD_CS is null";

		if (aModel.getCodFiscale() != null && aModel.getCodFiscale().length() > 0)
			lStatement += " AND soggetto.COD_FISCALE = '" + aModel.getCodFiscale() + "'";
		else
			lStatement += " AND soggetto.COD_FISCALE is null";

		if (aModel.getCodProvinciaNascita() != null && aModel.getCodProvinciaNascita().length() > 0)
			lStatement += " AND soggetto.COD_PROVINCIA_NASCITA = '" + aModel.getCodProvinciaNascita() + "'";
		else
			lStatement += " AND soggetto.COD_PROVINCIA_NASCITA is null";

		if (aModel.getCodStatoNascita() != null && aModel.getCodStatoNascita().length() > 0)
			lStatement += " AND soggetto.COD_STATO_NASCITA = '" + aModel.getCodStatoNascita() + "'";
		else
			lStatement += " AND soggetto.COD_STATO_NASCITA is null";

		if (aModel.getCognome() != null && aModel.getCognome().length() > 0)
			// Paolo Cherubini 16/02/2012 b2/rr010 va in errore sulle lettere minuscole accentate aggiungo
			// upper
			lStatement += " AND upper(soggetto.COGNOME) = '"
					+ StringUtils.convertSqlString(aModel.getCognome()) + "'";
		else
			lStatement += " AND soggetto.COGNOME is null";

		if (aModel.getNome() != null && aModel.getNome().length() > 0)
			// Paolo Cherubini 16/02/2012 b2/rr010 va in errore sulle lettere minuscole accentate aggiungo
			// upper
			lStatement += " AND upper(soggetto.NOME) = '" + StringUtils.convertSqlString(aModel.getNome())
					+ "'";
		else
			lStatement += " AND soggetto.NOME is null";

		if (aModel.getDataNascita() != null)
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lStatement += " AND trunc(soggetto.DATA_NASCITA) = to_date('"
					+ DateUtils.getDateToString(aModel.getDataNascita(), "dd/MM/yyyy") + "','DD-MM-YYYY')";
		else
			lStatement += " AND soggetto.DATA_NASCITA is null";

		if (aModel.getDataNascitaPresunta() != null && aModel.getDataNascitaPresunta().length() > 0)
			lStatement += " AND soggetto.DATA_NASCITA_PRESUNTA = '" + aModel.getDataNascitaPresunta() + "'";
		else
			lStatement += " AND soggetto.DATA_NASCITA_PRESUNTA is null";

		if (aModel.getEtaPresuntaAnni() != null)
			lStatement += " AND soggetto.ETA_PRESUNTA_ANNI = " + aModel.getEtaPresuntaAnni();
		else
			lStatement += " AND soggetto.ETA_PRESUNTA_ANNI is null";

		if (aModel.getEtaPresuntaMesi() != null)
			lStatement += " AND soggetto.ETA_PRESUNTA_MESI = " + aModel.getEtaPresuntaMesi();
		else
			lStatement += " AND soggetto.ETA_PRESUNTA_MESI is null";

		if (aModel.getDescComuneNascitaEstero() != null && aModel.getDescComuneNascitaEstero().length() > 0)
			lStatement += " AND upper(soggetto.DESC_COMUNE_NASCITA_ESTERO) = '"
					+ StringUtils.convertSqlString(aModel.getDescComuneNascitaEstero().toUpperCase()) + "'";
		else
			lStatement += " AND soggetto.DESC_COMUNE_NASCITA_ESTERO is null";

		if (aModel.getNazionalita() != null && aModel.getNazionalita().length() > 0)
			lStatement += " AND soggetto.NAZIONALITA = '" + aModel.getNazionalita() + "'";
		else
			lStatement += " AND soggetto.NAZIONALITA is null";

		if (aModel.getPaternita() != null && aModel.getPaternita().length() > 0)
			lStatement += " AND upper(soggetto.PATERNITA) = '"
					+ StringUtils.convertSqlString(aModel.getPaternita().toUpperCase()) + "'";
		else
			lStatement += " AND soggetto.PATERNITA is null";

		if (aModel.getCognomeMadre() != null && aModel.getCognomeMadre().length() > 0)
			lStatement += " AND upper(soggetto.COGNOME_MADRE) = '"
					+ StringUtils.convertSqlString(aModel.getCognomeMadre().toUpperCase()) + "'";
		else
			lStatement += " AND soggetto.COGNOME_MADRE is null";

		if (aModel.getNomeMadre() != null && aModel.getNomeMadre().length() > 0)
			lStatement += " AND upper(soggetto.NOME_MADRE) = '"
					+ StringUtils.convertSqlString(aModel.getNomeMadre().toUpperCase()) + "'";
		else
			lStatement += " AND soggetto.NOME_MADRE is null";

		if (aModel.getSesso() != null && aModel.getSesso().length() > 0)
			lStatement += " AND soggetto.SESSO = '" + aModel.getSesso() + "'";
		else
			lStatement += " AND soggetto.SESSO is null";

		if (aModel.getMeseNascita() != null)
			lStatement += " AND soggetto.MESE_NASCITA = " + aModel.getMeseNascita();
		else
			lStatement += " AND soggetto.MESE_NASCITA is null";

		// Il cod. Ufficio presente in "CodUfficioInserimento" di Soggetto e di Fascicolo, e "Chiave_Ufficio"
		// in condizioni normali hanno tutti lo stesso valore

		if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0)
			lStatement += " AND soggetto.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento()
					+ "'";

		if (StringUtils.checkValidValue(majorOffice)) {
			// se il tipo ufficio delll'utente connesso è PGCAP e provengo
			// dal dettaglio, devo poter vedere anche i fascicoli validati di
			// soggetti minorenni iscritti da altri uffici, pertanto non aggiungo
			// il filtro sull'età del soggetto
			if (fromDetail && tipoUfficio != null && !tipoUfficio.equals("") && tipoUfficio.equals("PGCAP")) {
				// lStatement += MinorMask.minorConditionPGCAP("vse", StrFascicolo, majorOffice);
			} else {
				lStatement += MinorMask.minorCondition("vse", StrFascicolo, majorOffice);
			}
		}

		setStatement(lStatement);
	}

	/**
	 * Ricerca i soggetti omonimi per il model passato
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaSoggettoOmonimo(SoggettoModel aModel) throws DAOException {
		String lStatement = new String(getSoggettoSqlQuery());

		lStatement += " " + setCondizioneOmonimi(aModel);

		lStatement += setOrder();

		setStatement(lStatement);
	}

	/**
	 * Ricerca Soggetto
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaSoggetto(SoggettoModel aModel) throws DAOException {
		String lStatement = new String(getSoggettoSqlQuery());

		lStatement += " " + setCondizione(aModel);

		// Ufficio per la prima query
		if (aModel.getCodUfficioInserimento() != null) {
			lStatement += " AND COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
		}

		/*
		 * --- 28/04 Union con altri soggetti di altri uffici con fascicolo validato... lStatement
		 * +=" UNION ";
		 *
		 * lStatement += getSoggettoFascicoloSqlQuery(); lStatement += " " + setCondizione(aModel);
		 */
		lStatement += setOrder();

		setStatement(lStatement);
	}

	/**
	 * Ricerca Soggetto
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaSoggettoAltreBDI(SoggettoModel aModel) throws DAOException {
		ricercaSoggettoAltreBDI(aModel, false);
	}

	public void ricercaSoggettoAltreBDI(SoggettoModel aModel, boolean checkMin_Maj) throws DAOException {

		String lStatement = getSoggettoAltreBDISqlQuery(checkMin_Maj);

		// lStatement += " " + setCondizione(aModel); // 07/02/2008
		lStatement += " " + setCondizioneAltreBDI(aModel);

		// Ufficio per la prima query
		if (aModel.getCodUfficioInserimento() != null) {
			lStatement += " AND COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
		}

		lStatement += setOrder();

		setStatement(lStatement);
	}

	public void ricercaSoggettoPerDistretto(SoggettoModel aModel, String aCodDistretto, int aPage)
			throws DAOException {
		ricercaSoggettoPerDistretto(aModel, aCodDistretto, aPage, "");
	}

	public void ricercaSoggettoPerDistretto(SoggettoModel aModel, String aCodDistretto, int aPage,
			String majorOffice) throws DAOException {
		String lStatement = "";

		// If il parametro aPage=0 uso la query solo per il conteggio totale delle righe
		// If il parametro aPage>0 uso la query per caricare il vettore a 20 a 20 coi dati
		// per poi passarlo alla jsp per la lista

		if (aPage > 0) {
			lStatement = new String("SELECT * FROM (  ");
			lStatement += " SELECT ID_SOGGETTO, COD_FISCALE, COD_CS, COD_AFIS, COGNOME, NOME, ANNO_NASCITA,  ";
			lStatement += " DATA_NASCITA, DATA_REATO_SIUS, DATA_NASCITA_PRESUNTA, COD_COMUNE_NASCITA, COMUNE,  ";
			lStatement += " COD_PROVINCIA_NASCITA, COD_STATO_NASCITA,  ";
			lStatement += " DESC_COMUNE_NASCITA_ESTERO, NAZIONALITA, PATERNITA,  ";
			lStatement += " COGNOME_MADRE, NOME_MADRE, SESSO, ATTO_NASCITA, NOTE, COD_COMUNE_CASELLARIO,  ";
			lStatement += " FLAG_PRESENZA_FASCICOLO, COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO,  ";
			lStatement += " COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO,  COD_UFFICIO_AGGIORNAMENTO,  MESE_NASCITA,   DESCR_SEDE_GIUDIZIARIA, SOG_ID_SOGGETTO, KEY_SOGG_NSC, ETA_PRESUNTA_ANNI, ETA_PRESUNTA_MESI ";
			lStatement += ", ROWNUM rn ";
		} else {
			lStatement += " SELECT count(*) HowManyRecords ";
		}

		lStatement += " FROM (" + getSoggettoAliasSqlQuery();
		lStatement += " AND SOGGETTO.COD_UFFICIO_INSERIMENTO IN (SELECT cod_ufficio FROM UFFICIO WHERE COD_DISTRETTO='"
				+ aCodDistretto + "')";
		lStatement += " " + setCondizione(aModel);
		if (StringUtils.checkValidValue(majorOffice)) {
			lStatement += MinorMask.minorCondition("vsm", aModel.getCodUfficioInserimento());
		}
		lStatement += setOrderTotale();
		lStatement += " )";

		if (aPage > 0) {
			lStatement += " inner ) WHERE rn BETWEEN " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		}

		setStatement(lStatement);

	} // chiude ricercaSoggettoPerDistretto

	/**
	 * Ricerca Soggetto
	 *
	 * [EC] - 16/01/2018: esplicitato meglio il nome del parametro di input
	 *
	 * @return
	 */
	protected String getSoggettoAltreBDISqlQuery(boolean checkMajor) {
		String lStatement = new String("");

		lStatement += " SELECT ID_SOGGETTO," + " COD_FISCALE," + " COD_CS," + " COD_AFIS," + " COGNOME,"
				+ " NOME," + " ANNO_NASCITA," + " DATA_NASCITA," + " DATA_REATO_SIUS,"
				+ " DATA_NASCITA_PRESUNTA," + " COD_COMUNE_NASCITA," + " COM.DESCRIZIONE COMUNE,"
				+ " COD_PROVINCIA_NASCITA," + " COD_STATO_NASCITA," + " DESC_COMUNE_NASCITA_ESTERO,"
				+ " NAZIONALITA," + " PATERNITA," + " COGNOME_MADRE," + " NOME_MADRE," + " SESSO,"
				+ " ATTO_NASCITA," + " SOGGETTO.NOTE," + " COD_COMUNE_CASELLARIO,"
				+ " FLAG_PRESENZA_FASCICOLO," + " SOGGETTO.COD_OPERATORE_INSERIMENTO,"
				+ " SOGGETTO.DATA_INSERIMENTO," + " SOGGETTO.COD_UFFICIO_INSERIMENTO,"
				+ " SOGGETTO.DATA_AGGIORNAMENTO,  SOGGETTO.COD_UFFICIO_AGGIORNAMENTO, "
				+ " SOGGETTO.COD_OPERATORE_AGGIORNAMENTO, " + " MESE_NASCITA, "
				+ " SEDE_GIUD.DESCRIZIONE DESCR_SEDE_GIUDIZIARIA, " + " KEY_SOGG_NSC, "
				+ " ETA_PRESUNTA_ANNI, " + " ETA_PRESUNTA_MESI " + " FROM SOGGETTO, " +
				// " FASCICOLO_SIEP fasc, " + // commentato il 07/02/2008
				" SEDE_GIUDIZIARIA SEDE_GIUD, " + " COMUNE COM, V_SOGGETTO_ETA VSE  "
				+ " WHERE  SEDE_GIUD.COD_SEDE_GIUDIZIARIA = COD_COMUNE_CASELLARIO " +
				// " fasc.SOG_ID_SOGGETTO = SOGGETTO.ID_SOGGETTO AND " + // commentato il 07/02/2008
				" AND COD_COMUNE_NASCITA = COM.COD_COMUNE " + " AND SOGGETTO.ID_SOGGETTO = VSE.COD_SOGGETTO ";

		if (checkMajor) {
			lStatement += " AND " + ICostantiSoggetto.CONDIZIONE_MAGGIORENNI + " ";
		}

		return lStatement;
	}

	/**
	 * Ricerca Soggetto
	 *
	 * @return
	 */
	protected String getSoggettoSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT ID_SOGGETTO," + " COD_FISCALE," + " COD_CS," + " COD_AFIS," + " COGNOME,"
				+ " NOME," + " ANNO_NASCITA," + " DATA_NASCITA," + " DATA_REATO_SIUS,"
				+ " DATA_NASCITA_PRESUNTA," + " COD_COMUNE_NASCITA," + " COM.DESCRIZIONE COMUNE,"
				+ " COD_PROVINCIA_NASCITA," +
				/* " PRO.RV_MEANING PROVINCIA," + */
				" COD_STATO_NASCITA," +
				/* " NAZ.RV_MEANING STATO," + */
				" DESC_COMUNE_NASCITA_ESTERO," + " NAZIONALITA," +
				// " DECODENAZ.RV_MEANING DESCRNAZ," +
				" PATERNITA," + " COGNOME_MADRE," + " NOME_MADRE," + " SESSO," + " ATTO_NASCITA,"
				+ " SOGGETTO.NOTE," + " COD_COMUNE_CASELLARIO," + " FLAG_PRESENZA_FASCICOLO,"
				+ " SOGGETTO.COD_OPERATORE_INSERIMENTO," + " SOGGETTO.DATA_INSERIMENTO,"
				+ " SOGGETTO.COD_UFFICIO_INSERIMENTO,"
				+ " SOGGETTO.DATA_AGGIORNAMENTO,  SOGGETTO.COD_UFFICIO_AGGIORNAMENTO, "
				+ " SOGGETTO.COD_OPERATORE_AGGIORNAMENTO, " + " MESE_NASCITA, "
				+ " SEDE_GIUD.DESCRIZIONE DESCR_SEDE_GIUDIZIARIA, " + " KEY_SOGG_NSC, "
				+ " ETA_PRESUNTA_ANNI, " + " ETA_PRESUNTA_MESI " + " FROM SOGGETTO, " +
				/*
				 * " CG_REF_CODES NAZ," + " CG_REF_CODES PRO," +
				 */
				// " CG_REF_CODES DECODENAZ ," +
				" SEDE_GIUDIZIARIA SEDE_GIUD, " + " COMUNE COM  "
				+ " WHERE  SEDE_GIUD.COD_SEDE_GIUDIZIARIA = COD_COMUNE_CASELLARIO AND " +
				/*
				 * " NAZ.RV_LOW_VALUE = COD_STATO_NASCITA AND " + " NAZ.RV_DOMAIN = 'NAZIONE' AND " +
				 * " PRO.RV_LOW_VALUE = COD_PROVINCIA_NASCITA AND " + " PRO.RV_DOMAIN = 'PROVINCIA' AND " +
				 * "  DECODENAZ.RV_LOW_VALUE = NAZIONALITA " + " AND DECODENAZ.RV_DOMAIN='NAZIONE' AND " +
				 */
				" COD_COMUNE_NASCITA = COM.COD_COMUNE";

		return lStatement;
	}

	/**
	 * Ricerca Soggetto e Alias
	 *
	 * @return
	 */
	protected String getSoggettoAliasSqlQuery() {
		String lStatement = new String("");
		/*
		 * Modifica per ALIAS: e' stata inserita la disctinct e la LEFT OUTER JOIN sulla tabella ALIAS per
		 * identificare i soggetti che hanno gli alias il campo aggiunto sulla query che identifica la
		 * presenza dell'alias è SOG_ID_SOGGETTO dove è riportato l'id del soggetto.
		 */
		lStatement += " SELECT DISTINCT SOGGETTO.ID_SOGGETTO," + " SOGGETTO.COD_FISCALE,"
				+ " SOGGETTO.COD_CS," + " SOGGETTO.COD_AFIS," + " SOGGETTO.COGNOME," + " SOGGETTO.NOME,"
				+ " SOGGETTO.ANNO_NASCITA," + " SOGGETTO.DATA_NASCITA," + " SOGGETTO.DATA_REATO_SIUS,"
				+ " SOGGETTO.DATA_NASCITA_PRESUNTA," + " SOGGETTO.COD_COMUNE_NASCITA,"
				+ " COM.DESCRIZIONE COMUNE," + " SOGGETTO.COD_PROVINCIA_NASCITA," +
				/* " PRO.RV_MEANING PROVINCIA," + */
				" SOGGETTO.COD_STATO_NASCITA," +
				/* " NAZ.RV_MEANING STATO," + */
				" SOGGETTO.DESC_COMUNE_NASCITA_ESTERO," + " SOGGETTO.NAZIONALITA," +
				// " DECODENAZ.RV_MEANING DESCRNAZ," +
				" SOGGETTO.PATERNITA," + " SOGGETTO.COGNOME_MADRE," + " SOGGETTO.NOME_MADRE,"
				+ " SOGGETTO.SESSO," + " SOGGETTO.ATTO_NASCITA," + " SOGGETTO.NOTE,"
				+ " SOGGETTO.COD_COMUNE_CASELLARIO," + " SOGGETTO.FLAG_PRESENZA_FASCICOLO,"
				+ " SOGGETTO.COD_OPERATORE_INSERIMENTO," + " SOGGETTO.DATA_INSERIMENTO,"
				+ " SOGGETTO.COD_UFFICIO_INSERIMENTO,"
				+ " SOGGETTO.DATA_AGGIORNAMENTO,  SOGGETTO.COD_UFFICIO_AGGIORNAMENTO, "
				+ " SOGGETTO.COD_OPERATORE_AGGIORNAMENTO, " + " SOGGETTO.MESE_NASCITA, "
				+ " SEDE_GIUD.DESCRIZIONE DESCR_SEDE_GIUDIZIARIA, " + " SOG_ID_SOGGETTO, " + " KEY_SOGG_NSC, "
				+ " SOGGETTO.ETA_PRESUNTA_ANNI, " + " SOGGETTO.ETA_PRESUNTA_MESI "
				+ " FROM SOGGETTO LEFT OUTER JOIN ALIAS ON ID_SOGGETTO = SOG_ID_SOGGETTO, " +
				/* " CG_REF_CODES NAZ," + */
				/* " CG_REF_CODES PRO," + */
				// " CG_REF_CODES DECODENAZ ," +

				// MODIFICA DEL 05/11/2018 (SOSTITUISCO LA VISTA V_SOGGETTO_MAGGIORENNE_NEW A POSTO DI QUELLA
				// VECCHIA V_SOGGETTO_MAGGIORENNE)
				" SEDE_GIUDIZIARIA SEDE_GIUD, " + " COMUNE COM,  " + " V_SOGGETTO_MAGGIORENNE_NEW VSM  "
				+ " WHERE  SEDE_GIUD.COD_SEDE_GIUDIZIARIA = COD_COMUNE_CASELLARIO AND " +
				/*
				 * " NAZ.RV_LOW_VALUE = SOGGETTO.COD_STATO_NASCITA AND " + " NAZ.RV_DOMAIN = 'NAZIONE' AND " +
				 * " PRO.RV_LOW_VALUE = SOGGETTO.COD_PROVINCIA_NASCITA " +
				 * " AND PRO.RV_DOMAIN = 'PROVINCIA' AND " + "  DECODENAZ.RV_LOW_VALUE = NAZIONALITA " +
				 * " AND DECODENAZ.RV_DOMAIN='NAZIONE' AND " +
				 */
				" SOGGETTO.COD_COMUNE_NASCITA = COM.COD_COMUNE AND "
				+ " SOGGETTO.ID_SOGGETTO = VSM.ID_SOGGETTO";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		SoggettoModel aModel = new SoggettoModel();

		aModel.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		aModel.setCodFiscale(getString("COD_FISCALE"));
		aModel.setCodCs(getString("COD_CS"));
		aModel.setCodAfis(getString("COD_AFIS"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setDataReatoSius(getDate("DATA_REATO_SIUS"));
		aModel.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA"));
		aModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		aModel.setDescrComuneNascita(getString("COMUNE"));
		aModel.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		// La descrizione della Provincia di nascita la si ricava dalle Decodifiche in memoria per risparmiare
		// una JOIN
		aModel.setDescrProvinciaNascita(DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getProvincie(), aModel.getCodProvinciaNascita()));
		// aModel.setDescrProvinciaNascita(getString("PROVINCIA") );
		aModel.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		aModel.setAttoNascita(getString("ATTO_NASCITA"));

		// La descrizione dello stato di nascita la si ricava dalle Decodifiche in memoria per risparmiare una
		// JOIN
		aModel.setDescrStatoNascita(DecodificheUtils
				.getDescbyCode(DecodificheManager.getInstance().getNazioni(), aModel.getCodStatoNascita()));
		// aModel.setDescrStatoNascita(getString("STATO") );

		aModel.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		aModel.setNazionalita(getString("NAZIONALITA"));
		// La descrizione della nazionalità la si ricava dalle Decodifiche in memoria per risparmiare una JOIN
		aModel.setDescrNazionalita(DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getStatoCittadinanza(), aModel.getNazionalita()));
		// aModel.setDescrNazionalita(getString("DESCRNAZ") );

		aModel.setPaternita(getString("PATERNITA"));
		aModel.setCognomeMadre(getString("COGNOME_MADRE"));
		aModel.setNomeMadre(getString("NOME_MADRE"));
		aModel.setSesso(getString("SESSO"));
		aModel.setAttoNascita(getString("ATTO_NASCITA"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO"));
		aModel.setDescrComuneCasellario(getString("DESCR_SEDE_GIUDIZIARIA"));
		aModel.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setMeseNascita(getBigDecimal("MESE_NASCITA"));
		aModel.setKeySoggNsc(getBigDecimal("KEY_SOGG_NSC"));
		aModel.setEtaPresuntaAnni(getBigDecimal("ETA_PRESUNTA_ANNI"));
		aModel.setEtaPresuntaMesi(getBigDecimal("ETA_PRESUNTA_MESI"));

		return aModel;
	}

	public GenericModel getAliasModel() throws DAOException {
		SoggettoModel aModel = new SoggettoModel();

		aModel.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		aModel.setCodFiscale(getString("COD_FISCALE"));
		aModel.setCodCs(getString("COD_CS"));
		aModel.setCodAfis(getString("COD_AFIS"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setDataReatoSius(getDate("DATA_REATO_SIUS"));
		aModel.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA"));
		aModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		aModel.setDescrComuneNascita(getString("COMUNE"));
		aModel.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		// La descrizione della Provincia di nascita la si ricava dalle Decodifiche in memoria per risparmiare
		// una JOIN
		aModel.setDescrProvinciaNascita(DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getProvincie(), aModel.getCodProvinciaNascita()));
		// aModel.setDescrProvinciaNascita(getString("PROVINCIA") );

		aModel.setCodStatoNascita(getString("COD_STATO_NASCITA"));

		// La descrizione dello stato di nascita la si ricava dalle Decodifiche in memoria per risparmiare una
		// JOIN
		aModel.setDescrStatoNascita(DecodificheUtils
				.getDescbyCode(DecodificheManager.getInstance().getNazioni(), aModel.getCodStatoNascita()));
		// aModel.setDescrStatoNascita(getString("STATO") );

		aModel.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		aModel.setNazionalita(getString("NAZIONALITA"));
		// La descrizione della nazionalità la si ricava dalle Decodifiche in memoria per risparmiare una JOIN
		aModel.setDescrNazionalita(DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getStatoCittadinanza(), aModel.getNazionalita()));
		// aModel.setDescrNazionalita(getString("DESCRNAZ") );

		aModel.setPaternita(getString("PATERNITA"));
		aModel.setCognomeMadre(getString("COGNOME_MADRE"));
		aModel.setNomeMadre(getString("NOME_MADRE"));
		aModel.setSesso(getString("SESSO"));
		aModel.setAttoNascita(getString("ATTO_NASCITA"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO"));
		aModel.setDescrComuneCasellario(getString("DESCR_SEDE_GIUDIZIARIA"));
		aModel.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setMeseNascita(getBigDecimal("MESE_NASCITA"));
		aModel.setSogIdSoggetto(getString("SOG_ID_SOGGETTO"));
		aModel.setKeySoggNsc(getBigDecimal("KEY_SOGG_NSC"));
		aModel.setEtaPresuntaAnni(getBigDecimal("ETA_PRESUNTA_ANNI"));
		aModel.setEtaPresuntaMesi(getBigDecimal("ETA_PRESUNTA_MESI"));

		return aModel;
	}

	/**
	 * Settaggio della condizione sul Soggetto
	 *
	 * @param aSm
	 * @return
	 */
	private String setCondizione(SoggettoModel aSm) {
		String lCondizioni = "";

		if (aSm.getIdSoggetto().doubleValue() == 0) {
			String lCondizioni1 = "";
			String lCondizioni2 = "";
			if (!(aSm.getCognome().equals(""))) {
				// Paolo Cherubini 16/02/2012 b2/rr010 va in errore sulle lettere minuscole accentate aggiungo
				// upper
				lCondizioni1 += " AND upper(SOGGETTO.COGNOME) like '"
						+ StringUtils.convertSqlString(aSm.getCognome()) + "%'";
			}
			if (!(aSm.getDescComuneNascitaEstero().equals(""))) {
				lCondizioni1 += " AND upper(SOGGETTO.DESC_COMUNE_NASCITA_ESTERO) like '"
						+ StringUtils.convertSqlString(aSm.getDescComuneNascitaEstero().toUpperCase()) + "%'";
			}

			if (!(aSm.getNome().equals(""))) {
				// Paolo Cherubini 16/02/2012 b2/rr010 va in errore sulle lettere minuscole accentate aggiungo
				// upper
				lCondizioni1 += " AND upper(SOGGETTO.NOME) like '"
						+ StringUtils.convertSqlString(aSm.getNome()) + "%'";
			}

			if (!(aSm.getCodComuneNascita().equals(""))) {
				lCondizioni1 += " AND SOGGETTO.COD_COMUNE_NASCITA = '" + aSm.getCodComuneNascita() + "'";
			}

			if (!(aSm.getPaternita().equals(""))) {
				lCondizioni1 += " AND upper(SOGGETTO.PATERNITA) LIKE '"
						+ StringUtils.convertSqlString(aSm.getPaternita().toUpperCase()) + "%'";
			}

			if (!(aSm.getNomeMadre().equals(""))) {
				lCondizioni1 += " AND upper(SOGGETTO.NOME_MADRE) LIKE '"
						+ StringUtils.convertSqlString(aSm.getNomeMadre().toUpperCase()) + "%'";
			}

			if (!(aSm.getCognomeMadre().equals(""))) {
				lCondizioni1 += " AND upper(SOGGETTO.COGNOME_MADRE) LIKE '"
						+ StringUtils.convertSqlString(aSm.getCognomeMadre().toUpperCase()) + "%'";
			}

			if (!(aSm.getCodStatoNascita().equals(""))) {
				lCondizioni1 += " AND SOGGETTO.COD_STATO_NASCITA = '" + aSm.getCodStatoNascita() + "'";
			}

			if (aSm.getDataNascita() != null) {
				// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
				lCondizioni1 += " AND trunc(SOGGETTO.DATA_NASCITA) = TO_DATE('"
						+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
			}

			if (aSm.getAttoNascita() != null && !aSm.getAttoNascita().equals("")) {
				lCondizioni1 += " AND SOGGETTO.ATTO_NASCITA = '"
						+ StringUtils.convertSqlString(aSm.getAttoNascita().toUpperCase()) + "'";
			}

			if (aSm.getCodCs() != null && !aSm.getCodCs().equals("")) {
				lCondizioni1 += " AND SOGGETTO.COD_CS = '"
						+ StringUtils.convertSqlString(aSm.getCodCs().toUpperCase()) + "'";
			}

			if (aSm.getClassiFascicolo() != null && aSm.getClassiFascicolo().length > 0) {
				String[] lClassiFascicolo = aSm.getClassiFascicolo();
				for (int i = 0; i < lClassiFascicolo.length; i++) {
					if (i != 0) {
						lCondizioni1 += " OR ";
					} else {
						lCondizioni1 += " AND (";
					}

					int lTipoClasse = Integer.parseInt(lClassiFascicolo[i]);
					if (lTipoClasse > 1) {
						lCondizioni1 += " (fasc.CHIAVE_PROGR BETWEEN " + (lTipoClasse * 10000) + " AND "
								+ (lTipoClasse * 10000 + 9999) + " ) ";
					} else // TipoProgressivo =1
					{
						lCondizioni1 += " (fasc.CHIAVE_PROGR BETWEEN 1 AND " + (lTipoClasse * 10000 + 9999)
								+ " ) ";
					}
				}

				lCondizioni1 += " ) ";
			}

			if (lCondizioni1.equals("") && aSm.getCodAfis() != null && !aSm.getCodAfis().equals("")) {
				lCondizioni2 += " AND SOGGETTO.COD_AFIS = '"
						+ StringUtils.convertSqlString(aSm.getCodAfis().toUpperCase()) + "'";
			} else if (!lCondizioni1.equals("") && aSm.getCodAfis() != null && !aSm.getCodAfis().equals("")) {
				lCondizioni2 += " SOGGETTO.COD_AFIS = '"
						+ StringUtils.convertSqlString(aSm.getCodAfis().toUpperCase()) + "'";
			}

			if (lCondizioni1.length() > 0 && lCondizioni2.length() > 0) {
				int primoAnd = lCondizioni1.toLowerCase().indexOf("and");
				lCondizioni1 = lCondizioni1.substring(primoAnd + 3);
				lCondizioni = " AND ((" + lCondizioni1 + ")  OR (" + lCondizioni2 + "))";
			} else if (lCondizioni1.length() > 0) {
				lCondizioni = lCondizioni1;
			} else if (lCondizioni2.length() > 0) {
				lCondizioni = lCondizioni2;
			}

		} else {
			lCondizioni = " AND ID_SOGGETTO = " + aSm.getIdSoggetto();
		}

		return lCondizioni;
	}

	/**
	 * Settaggio della condizione sul Soggetto per ricercare un Omonimo
	 *
	 * @param aSm
	 * @return
	 */
	private String setCondizioneOmonimi(SoggettoModel aSm) {
		String lCondizioni = new String();

		if (!(aSm.getCognome().equals(""))) {
			// Paolo Cherubini 16/02/2012 b2/rr010 va in errore sulle lettere minuscole accentate aggiungo
			// upper
			lCondizioni += " AND upper(COGNOME) = '" + StringUtils.convertSqlString(aSm.getCognome()) + "'";
		}
		if (!(aSm.getDescComuneNascitaEstero().equals(""))) {
			lCondizioni += " AND upper(DESC_COMUNE_NASCITA_ESTERO) = '"
					+ StringUtils.convertSqlString(aSm.getDescComuneNascitaEstero().toUpperCase()) + "'";
		}

		if (!(aSm.getNome().equals(""))) {
			// Paolo Cherubini 16/02/2012 b2/rr010 va in errore sulle lettere minuscole accentate aggiungo
			// upper
			lCondizioni += " AND upper(NOME) = '" + StringUtils.convertSqlString(aSm.getNome()) + "'";
		}

		if (!(aSm.getCodComuneNascita().equals(""))) {
			lCondizioni += " AND COD_COMUNE_NASCITA = '" + aSm.getCodComuneNascita() + "'";
		}
		if (aSm.getDataNascita() != null) {
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lCondizioni += " AND trunc(DATA_NASCITA) = TO_DATE('"
					+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
		}

		return lCondizioni;
	}

	/**
	 * Settaggio della condizione sul Soggetto per ricerca su Altra BDI
	 *
	 * @param aSm
	 * @return
	 */
	private String setCondizioneAltreBDI(SoggettoModel aSm) {
		String lCondizioni = new String();

		if (!(aSm.getCognome().equals(""))) {
			// Paolo Cherubini 16/02/2012 b2/rr010 va in errore sulle lettere minuscole accentate aggiungo
			// upper
			lCondizioni += " AND upper(COGNOME) = '" + StringUtils.convertSqlString(aSm.getCognome()) + "'";
		}
		if (!(aSm.getDescComuneNascitaEstero().equals(""))) {
			lCondizioni += " AND upper(DESC_COMUNE_NASCITA_ESTERO) = '"
					+ StringUtils.convertSqlString(aSm.getDescComuneNascitaEstero().toUpperCase()) + "'";
		}

		if (!(aSm.getNome().equals(""))) {
			// Paolo Cherubini 16/02/2012 b2/rr010 va in errore sulle lettere minuscole accentate aggiungo
			// upper
			lCondizioni += " AND upper(NOME) = '" + StringUtils.convertSqlString(aSm.getNome()) + "'";
		}

		if (!(aSm.getCodComuneNascita().equals(""))) {
			lCondizioni += " AND COD_COMUNE_NASCITA = '" + aSm.getCodComuneNascita() + "'";
		}
		if (aSm.getDataNascita() != null) {
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lCondizioni += " AND trunc(DATA_NASCITA) = TO_DATE('"
					+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (aSm.getAttoNascita() != null && !aSm.getAttoNascita().equals("")) {
			lCondizioni += " AND ATTO_NASCITA = '"
					+ StringUtils.convertSqlString(aSm.getAttoNascita().toUpperCase()) + "'";
		}
		if (aSm.getCodAfis() != null && !aSm.getCodAfis().equals("")) {
			lCondizioni += " AND COD_AFIS = '" + StringUtils.convertSqlString(aSm.getCodAfis()) + "'";
		}
		return lCondizioni;
	}

	/**
	 * setta l'ordinamento della Query
	 *
	 * @return
	 */
	private String setOrder() {
		return "  ORDER BY COGNOME, NOME ";
	}

	/**
	 * setta l'ordinamento della Query Totale
	 *
	 * @return
	 */
	private String setOrderTotale() {
		return "  ORDER BY COGNOME, NOME, SESSO, data_nascita, comune, paternita, cod_afis,"
				+ "  DESC_COMUNE_NASCITA_ESTERO, COD_FISCALE,COD_CS,ANNO_NASCITA,MESE_NASCITA, "
				+ "  COGNOME_MADRE, NOME_MADRE,DATA_NASCITA_PRESUNTA, ATTO_NASCITA";
	}

	/**
	 * Ricerca Soggetto
	 *
	 * @return
	 */
	protected String getSuperSoggettoSqlQuery(String StrFascicolo, String strCodDistrettoUtenteConnesso,
			String majorOffice) {
		String lStatement = new String("");

		// MEV_6: aggiunto hint /*+ index(soggetto SOG_COG_NOM_COM_DAT_I) */
		lStatement += " SELECT /*+ index(soggetto SOG_COG_NOM_COM_DAT_I) */ ID_SOGGETTO," + " COD_FISCALE,"
				+ " COD_CS," + " COD_AFIS," + " COGNOME," + " NOME," + " ANNO_NASCITA," + " DATA_NASCITA,"
				+ " DATA_REATO_SIUS," + " DATA_NASCITA_PRESUNTA," + " COD_COMUNE_NASCITA,"
				+ " COM.DESCRIZIONE COMUNE," + " COD_PROVINCIA_NASCITA," + " COD_STATO_NASCITA,"
				+ " DESC_COMUNE_NASCITA_ESTERO," + " NAZIONALITA," + " PATERNITA," + " COGNOME_MADRE,"
				+ " NOME_MADRE," + " SESSO," + " ATTO_NASCITA," + " SOGGETTO.NOTE,"
				+ " COD_COMUNE_CASELLARIO," + " FLAG_PRESENZA_FASCICOLO,"
				+ " SOGGETTO.COD_OPERATORE_INSERIMENTO," + " SOGGETTO.DATA_INSERIMENTO,"
				+ " SOGGETTO.COD_UFFICIO_INSERIMENTO,"
				+ " SOGGETTO.DATA_AGGIORNAMENTO,  SOGGETTO.COD_UFFICIO_AGGIORNAMENTO, "
				+ " SOGGETTO.COD_OPERATORE_AGGIORNAMENTO, " + " MESE_NASCITA, "
				+ " SEDE_GIUD.DESCRIZIONE DESCR_SEDE_GIUDIZIARIA, " + " KEY_SOGG_NSC, "
				+ " ETA_PRESUNTA_ANNI, " + " ETA_PRESUNTA_MESI " + " FROM SOGGETTO, " + StrFascicolo + ","
				+ " SEDE_GIUDIZIARIA SEDE_GIUD, " + " COMUNE COM, UFFICIO U";
		if (StrFascicolo != null && !StrFascicolo.equals("")) {
			if (StrFascicolo.equals("FASCICOLO_SIEP")) {
				lStatement += ", v_soggetto_eta vse ";
			} else if (StrFascicolo.equals("FASCICOLO_SIUS")) {
				lStatement += ", v_sogsius_eta vse ";
			}
		}
		lStatement += "  WHERE  SEDE_GIUD.COD_SEDE_GIUDIZIARIA = COD_COMUNE_CASELLARIO AND "
				+ " ID_SOGGETTO = SOG_ID_SOGGETTO AND ";
		if (StrFascicolo != null && !StrFascicolo.equals("")) {
			if (StrFascicolo.equals("FASCICOLO_SIEP")) {
				lStatement += StrFascicolo + ".ID_FASCICOLO_SIEP = vse.fas_sie_id_fascicolo_siep AND ";
			} else if (StrFascicolo.equals("FASCICOLO_SIUS")) {
				lStatement += StrFascicolo + ".ID_FASCICOLO_SIUS = vse.id_fascicolo_sius AND ";
			}
		}
		lStatement += " COD_COMUNE_NASCITA = COM.COD_COMUNE AND " + " CHIAVE_UFFICIO = U.COD_UFFICIO";
		if (strCodDistrettoUtenteConnesso.length() > 1)
			lStatement += " AND U.COD_DISTRETTO = '" + strCodDistrettoUtenteConnesso + "'";
		return lStatement;
	}

	public void ricercaSoggettoIgnoto() throws DAOException {
		String lStatement = "SELECT ID_SOGGETTO, COGNOME, NOME FROM SOGGETTO ";
		lStatement += " WHERE UPPER(COGNOME) = 'IGNOTO' AND UPPER(NOME) = 'IGNOTO' ";
		lStatement += " AND DATA_NASCITA IS NULL ";

		setStatement(lStatement);
	}

	public GenericModel getSoggettoIgnotoModel() throws DAOException {
		SoggettoModel aModel = new SoggettoModel();

		aModel.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));

		return aModel;
	}

	/**
	 * AVVOCATURA: aggiunto metodo di ricerca
	 *
	 * @param sm
	 * @param codDistretto
	 * @param codFiscaleAvvocato
	 * @param codTipoUfficio
	 */
	// public void ricercaSuperSoggettoConProcedimenti(SoggettoModel sm, String codDistretto,
	// String codFiscaleAvvocato, String codTipoUfficio) {
	//
	// // info per il log
	// LogF3B.getLogger().info(
	// "Starting Point della classe: SoggettoSqlDAO, metodo: ricercaSuperSoggettoConProcedimenti");
	//
	// // instanzio ed inizializzo un oggetto di tipo "String"
	// String query = new String("");
	// query += " SELECT ID_SOGGETTO,"
	// + " SOGGETTO.COD_FISCALE,"
	// + " COD_CS,"
	// + " COD_AFIS,"
	// + " SOGGETTO.COGNOME,"
	// + " SOGGETTO.NOME,"
	// + " ANNO_NASCITA,"
	// + " SOGGETTO.DATA_NASCITA,"
	// + " DATA_REATO_SIUS,"
	// + " DATA_NASCITA_PRESUNTA,"
	// + " COD_COMUNE_NASCITA,"
	// + " COM.DESCRIZIONE COMUNE,"
	// + " COD_PROVINCIA_NASCITA,"
	// + " COD_STATO_NASCITA,"
	// + " DESC_COMUNE_NASCITA_ESTERO,"
	// + " NAZIONALITA,"
	// + " PATERNITA,"
	// + " COGNOME_MADRE,"
	// + " NOME_MADRE,"
	// + " SESSO,"
	// + " ATTO_NASCITA,"
	// + " SOGGETTO.NOTE,"
	// + " COD_COMUNE_CASELLARIO,"
	// + " FLAG_PRESENZA_FASCICOLO,"
	// + " SOGGETTO.COD_OPERATORE_INSERIMENTO,"
	// + " SOGGETTO.DATA_INSERIMENTO,"
	// + " SOGGETTO.COD_UFFICIO_INSERIMENTO,"
	// + " SOGGETTO.DATA_AGGIORNAMENTO, SOGGETTO.COD_UFFICIO_AGGIORNAMENTO,"
	// + " SOGGETTO.COD_OPERATORE_AGGIORNAMENTO,"
	// + " MESE_NASCITA,"
	// + " SEDE_GIUD.DESCRIZIONE DESCR_SEDE_GIUDIZIARIA,"
	// + " KEY_SOGG_NSC,"
	// + " ETA_PRESUNTA_ANNI,"
	// + " ETA_PRESUNTA_MESI"
	// + " FROM SOGGETTO, FASCICOLO_SIUS,"
	// +
	// " SEDE_GIUDIZIARIA SEDE_GIUD, COMUNE COM, UFFICIO U, AVVOCATO AVV, AVVOCATO_FASCICOLO_SIUS AFS,
	// v_sogsius_eta vse";
	// query += " WHERE SEDE_GIUD.COD_SEDE_GIUDIZIARIA = COD_COMUNE_CASELLARIO AND"
	// + " ID_SOGGETTO = SOG_ID_SOGGETTO AND";
	// query += " FASCICOLO_SIUS.ID_FASCICOLO_SIUS = vse.id_fascicolo_sius AND";
	// query += " COD_COMUNE_NASCITA = COM.COD_COMUNE AND " + " CHIAVE_UFFICIO = U.COD_UFFICIO";
	// query += " AND U.COD_TIPO_UFFICIO = '" + codTipoUfficio + "'";
	// query += " AND FASCICOLO_SIUS.ID_FASCICOLO_SIUS = AFS.FAS_SIU_ID_FASCICOLO_SIUS";
	// query += " AND AFS.AVV_ID_AVVOCATO = AVV.ID_AVVOCATO";
	// query += " AND AVV.COD_FISCALE = '" + codFiscaleAvvocato + "'";
	// if (codDistretto.length() > 1)
	// query += " AND U.COD_DISTRETTO = '" + codDistretto + "'";
	//
	// if (sm.getAnnoNascita() != null)
	// query += " AND soggetto.ANNO_NASCITA = '" + sm.getAnnoNascita() + "'";
	// else
	// query += " AND soggetto.ANNO_NASCITA is null";
	//
	// if (sm.getAttoNascita() != null && sm.getAttoNascita().length() > 0)
	// query += " AND soggetto.ATTO_NASCITA = '" + sm.getAttoNascita() + "'";
	// else
	// query += " AND soggetto.ATTO_NASCITA is null";
	//
	// if (sm.getCodAfis() != null && sm.getCodAfis().length() > 0)
	// query += " AND soggetto.COD_AFIS = '" + sm.getCodAfis() + "'";
	// else
	// query += " AND soggetto.COD_AFIS is null";
	//
	// if (sm.getCodComuneNascita() != null && sm.getCodComuneNascita().length() > 0)
	// query += " AND soggetto.COD_COMUNE_NASCITA = '" + sm.getCodComuneNascita() + "'";
	// else
	// query += " AND soggetto.COD_COMUNE_NASCITA is null";
	//
	// if (sm.getCodCs() != null && sm.getCodCs().length() > 0)
	// query += " AND soggetto.COD_CS = '" + sm.getCodCs() + "'";
	// else
	// query += " AND soggetto.COD_CS is null";
	//
	// if (sm.getCodFiscale() != null && sm.getCodFiscale().length() > 0)
	// query += " AND soggetto.COD_FISCALE = '" + sm.getCodFiscale() + "'";
	// else
	// query += " AND soggetto.COD_FISCALE is null";
	//
	// if (sm.getCodProvinciaNascita() != null && sm.getCodProvinciaNascita().length() > 0)
	// query += " AND soggetto.COD_PROVINCIA_NASCITA = '" + sm.getCodProvinciaNascita() + "'";
	// else
	// query += " AND soggetto.COD_PROVINCIA_NASCITA is null";
	//
	// if (sm.getCodStatoNascita() != null && sm.getCodStatoNascita().length() > 0)
	// query += " AND soggetto.COD_STATO_NASCITA = '" + sm.getCodStatoNascita() + "'";
	// else
	// query += " AND soggetto.COD_STATO_NASCITA is null";
	//
	// if (sm.getCognome() != null && sm.getCognome().length() > 0)
	// query += " AND upper(soggetto.COGNOME) = '" + StringUtils.convertSqlString(sm.getCognome()) + "'";
	// else
	// query += " AND soggetto.COGNOME is null";
	//
	// if (sm.getNome() != null && sm.getNome().length() > 0)
	// query += " AND upper(soggetto.NOME) = '" + StringUtils.convertSqlString(sm.getNome()) + "'";
	// else
	// query += " AND soggetto.NOME is null";
	//
	// if (sm.getDataNascita() != null)
	// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
	// query += " AND trunc(soggetto.DATA_NASCITA) = to_date('"
	// + DateUtils.getDateToString(sm.getDataNascita(), "dd/MM/yyyy") + "','DD-MM-YYYY')";
	// else
	// query += " AND soggetto.DATA_NASCITA is null";
	//
	// if (sm.getDataNascitaPresunta() != null && sm.getDataNascitaPresunta().length() > 0)
	// query += " AND soggetto.DATA_NASCITA_PRESUNTA = '" + sm.getDataNascitaPresunta() + "'";
	// else
	// query += " AND soggetto.DATA_NASCITA_PRESUNTA is null";
	//
	// if (sm.getEtaPresuntaAnni() != null)
	// query += " AND soggetto.ETA_PRESUNTA_ANNI = " + sm.getEtaPresuntaAnni();
	// else
	// query += " AND soggetto.ETA_PRESUNTA_ANNI is null";
	//
	// if (sm.getEtaPresuntaMesi() != null)
	// query += " AND soggetto.ETA_PRESUNTA_MESI = " + sm.getEtaPresuntaMesi();
	// else
	// query += " AND soggetto.ETA_PRESUNTA_MESI is null";
	//
	// if (sm.getDescComuneNascitaEstero() != null && sm.getDescComuneNascitaEstero().length() > 0)
	// query += " AND upper(soggetto.DESC_COMUNE_NASCITA_ESTERO) = '"
	// + StringUtils.convertSqlString(sm.getDescComuneNascitaEstero().toUpperCase()) + "'";
	// else
	// query += " AND soggetto.DESC_COMUNE_NASCITA_ESTERO is null";
	//
	// if (sm.getNazionalita() != null && sm.getNazionalita().length() > 0)
	// query += " AND soggetto.NAZIONALITA = '" + sm.getNazionalita() + "'";
	// else
	// query += " AND soggetto.NAZIONALITA is null";
	//
	// if (sm.getPaternita() != null && sm.getPaternita().length() > 0)
	// query += " AND upper(soggetto.PATERNITA) = '"
	// + StringUtils.convertSqlString(sm.getPaternita().toUpperCase()) + "'";
	// else
	// query += " AND soggetto.PATERNITA is null";
	//
	// if (sm.getCognomeMadre() != null && sm.getCognomeMadre().length() > 0)
	// query += " AND upper(soggetto.COGNOME_MADRE) = '"
	// + StringUtils.convertSqlString(sm.getCognomeMadre().toUpperCase()) + "'";
	// else
	// query += " AND soggetto.COGNOME_MADRE is null";
	//
	// if (sm.getNomeMadre() != null && sm.getNomeMadre().length() > 0)
	// query += " AND upper(soggetto.NOME_MADRE) = '"
	// + StringUtils.convertSqlString(sm.getNomeMadre().toUpperCase()) + "'";
	// else
	// query += " AND soggetto.NOME_MADRE is null";
	//
	// if (sm.getSesso() != null && sm.getSesso().length() > 0)
	// query += " AND soggetto.SESSO = '" + sm.getSesso() + "'";
	// else
	// query += " AND soggetto.SESSO is null";
	//
	// if (sm.getMeseNascita() != null)
	// query += " AND soggetto.MESE_NASCITA = " + sm.getMeseNascita();
	// else
	// query += " AND soggetto.MESE_NASCITA is null";
	//
	// if (sm.getCodUfficioInserimento() != null && sm.getCodUfficioInserimento().length() > 0)
	// query += " AND soggetto.COD_UFFICIO_INSERIMENTO = '" + sm.getCodUfficioInserimento() + "'";
	//
	// setStatement(query);
	// }

}