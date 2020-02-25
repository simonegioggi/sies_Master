package siap.sico.soggetto.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.soggetto.model.SoggettoAliasFascicoloModel;
// import siap.sico.decodifiche.controller.DecodificheManager;
// import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.util.MinorMask;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: SoggettoAliasFascicoloSqlDao
 * </p>
 * <p>
 * Description: Classe per la ricerca dei soggetti, degli alias e fascicoli assegnati
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class SoggettoAliasFascicoloSqlDao extends SIAPSqlDAO {

	public SoggettoAliasFascicoloSqlDao(Connection con) {
		super(con);
	}

	/**
	 * Ricerca Fascicoli da Soggetto e Alias paginati
	 * 
	 * @param SoggettoModel
	 * @param lCodUfficioUtenteConnesso
	 * @param aPage
	 * @param lCodDistrettoUtenteConnesso
	 * @param TipoRicerca
	 * @return
	 * @throws F3BException
	 */
	public void ricercaSoggettoAliasFascicoloPaged(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			int aPage, String strCodDistrettoUtenteConnesso, String strTipoRicerca) {
		ricercaSoggettoAliasFascicoloPaged(aModel, strCodUfficioUtenteConnesso, aPage,
				strCodDistrettoUtenteConnesso, strTipoRicerca, "");
	}

	public void ricercaSoggettoAliasFascicoloPaged(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			int aPage, String strCodDistrettoUtenteConnesso, String strTipoRicerca, String majorOffice) {

		String strQuery = "";
		String lPaginedStatement = new String("");

		// Costruzione della query parametrizzata Alias.
		strQuery += getAliasFascicoliSqlQuery(); // paolo 03/01/2011 anche qui dentro aggiunti campi per il
													// supersoggetto
		strQuery += setCondizioneUfficioDistrettoAlias(strCodUfficioUtenteConnesso,
				strCodDistrettoUtenteConnesso, strTipoRicerca, majorOffice);
		strQuery += setCondizioneAlias(aModel);

		// paolo cherubini 03/01/2011 aggiungo per raggrupare il supersoggetto
		strQuery += getAliasByGroupSuperSoggetto(); // fine paolo

		// Costruzione della query parametrizzata Soggetto.
		strQuery += getSoggettoFascicoliSqlQuery();
		strQuery += setCondizioneUfficioDistrettoSoggetto(strCodUfficioUtenteConnesso,
				strCodDistrettoUtenteConnesso, strTipoRicerca, majorOffice);
		strQuery += setCondizioneSoggetto(aModel);

		// paolo cherubini 03/01/2011 aggiungo per raggrupare il supersoggetto
		strQuery += getSoggettoByGroupSuperSoggetto(); // fine paolo

		strQuery += setOrder();

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + strQuery
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/**
	 * Sql Query Ricerca Fascicoli da Alias
	 * 
	 * @return
	 */
	protected String getAliasFascicoliSqlQuery() {
		String lStatement = new String();

		lStatement += " SELECT   ali.cognome || ' ' || ali.nome nominativo, ali.data_nascita, ";
		lStatement += " DECODE(comune_nascita.descrizione||' ('|| ali.cod_provincia_nascita||')','- (-)', ";
		lStatement += " DECODE (UPPER (stato_estero.rv_meaning),'-', NULL,UPPER (stato_estero.rv_meaning)||' (')||''|| ";
		lStatement += " DECODE (ali.desc_comune_nascita_estero,NULL, NULL,ali.desc_comune_nascita_estero|| ";
		lStatement += " DECODE (UPPER (stato_estero.rv_meaning), '-', NULL,')' )),comune_nascita.descrizione|| ' ('|| ";
		lStatement += " ali.cod_provincia_nascita|| ')') luogo_nascita, ";
		lStatement += " ali.paternita pater, sog.cognome || ' ' || sog.nome alias_di, ";
		// lStatement += " ali.cod_afis codice_afis, ";
		lStatement += " sog.cod_afis codice_afis, "; // Paolo Cherubini 27/04/2011 sostituisce quella sopra

		// paolo cherubini 03/01/2011 supersoggetto aggiunta delle seguenti righe
		// alcuni campi vengono presi dal soggetto poichè non presenti sul ALIAS, cmq non vengono usati nella
		// ricerca
		// sono lasciati solo perchè altrimenti la select da errore
		// il campo id_soggetto_alias a 0 indica che è un alias in maschera comparirà in rosso
		lStatement += " COUNT(*) numero_proc, 0 id_soggetto_alias, 1 id_sogg, ";
		lStatement += " ali.COD_FISCALE, ali.COD_CS,  ali.COGNOME, ali.NOME, SOG.ANNO_NASCITA, ";
		lStatement += " SOG.DATA_NASCITA_PRESUNTA, ali.COD_COMUNE_NASCITA,  ali.COD_PROVINCIA_NASCITA, ali.COD_STATO_NASCITA, ";
		lStatement += " ali.ATTO_NASCITA, ali.DESC_COMUNE_NASCITA_ESTERO,  SOG.NAZIONALITA, ";
		lStatement += " SOG.COGNOME_MADRE, SOG.NOME_MADRE,  ali.SESSO, SOG.MESE_NASCITA, SOG.ETA_PRESUNTA_ANNI, SOG.ETA_PRESUNTA_MESI ";
		// fine paolo

		return lStatement;
	}

	/**
	 * Sql Query Ricerca Fascicoli da Soggetto
	 * 
	 * @return
	 */
	protected String getSoggettoFascicoliSqlQuery() {
		String lStatement = new String();

		lStatement += " UNION ";
		lStatement += " SELECT   sog.cognome || ' ' || sog.nome nominativo, sog.data_nascita, ";
		lStatement += " DECODE(comune_nascita.descrizione||' ('|| sog.cod_provincia_nascita||')','- (-)', ";
		lStatement += " DECODE (UPPER (stato_estero.rv_meaning),'-', NULL,UPPER (stato_estero.rv_meaning)||' (')||''|| ";
		lStatement += " DECODE (sog.desc_comune_nascita_estero,NULL, NULL,sog.desc_comune_nascita_estero|| ";
		lStatement += " DECODE (UPPER (stato_estero.rv_meaning), '-', NULL,')' )),comune_nascita.descrizione|| ' ('|| ";
		lStatement += " sog.cod_provincia_nascita|| ')') luogo_nascita, ";
		lStatement += " sog.paternita pater, NULL, sog.cod_afis codice_afis, ";

		// paolo cherubini 03/01/2011 supersoggetto aggiunta delle seguenti righe
		// il campo id_soggetto_alias a null indica che è un soggetto e non un alias in maschera non comparirà
		// in rosso
		lStatement += " COUNT(*) numero_proc, null id_soggetto_alias, 1 id_sogg, ";
		lStatement += " SOG.COD_FISCALE, SOG.COD_CS,  SOG.COGNOME, SOG.NOME, SOG.ANNO_NASCITA, ";
		lStatement += " SOG.DATA_NASCITA_PRESUNTA, SOG.COD_COMUNE_NASCITA,  SOG.COD_PROVINCIA_NASCITA, SOG.COD_STATO_NASCITA, ";
		lStatement += " SOG.ATTO_NASCITA, SOG.DESC_COMUNE_NASCITA_ESTERO,  SOG.NAZIONALITA, ";
		lStatement += " SOG.COGNOME_MADRE, SOG.NOME_MADRE,  SOG.SESSO, SOG.MESE_NASCITA, SOG.ETA_PRESUNTA_ANNI, SOG.ETA_PRESUNTA_MESI ";
		// fine paolo

		return lStatement;

	}

	/**
	 * Altre Condizioni per Alias
	 * 
	 * @param SoggettoModel
	 * @return
	 */
	private String setCondizioneAlias(SoggettoModel aSm) {
		String lCondizioni = new String();

		if (!(aSm.getCognome().equals("")))
			lCondizioni += " AND ALI.COGNOME like '" + StringUtils.convertSqlString(aSm.getCognome()) + "%'";

		if (!(aSm.getNome().equals("")))
			lCondizioni += " AND ALI.NOME like '" + StringUtils.convertSqlString(aSm.getNome()) + "%'";

		if (aSm.getDataNascita() != null)
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lCondizioni += " AND trunc(ALI.DATA_NASCITA) = TO_DATE('"
					+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";

		if (!(aSm.getCodComuneNascita().equals("")))
			lCondizioni += " AND ALI.COD_COMUNE_NASCITA = '"
					+ StringUtils.convertSqlString(aSm.getCodComuneNascita()) + "'";

		if (!(aSm.getCodStatoNascita().equals("")))
			lCondizioni += " AND ALI.COD_STATO_NASCITA = '" + aSm.getCodStatoNascita() + "'";

		if (aSm.getClassiFascicolo() != null && aSm.getClassiFascicolo().length > 0) {
			String[] lClassiFascicolo = aSm.getClassiFascicolo();
			for (int i = 0; i < lClassiFascicolo.length; i++) {
				if (i != 0) {
					lCondizioni += " OR ";
				} else {
					lCondizioni += " AND (";
				}

				int lTipoClasse = Integer.parseInt(lClassiFascicolo[i]);
				if (lTipoClasse > 1) {
					lCondizioni += " (fasc.CHIAVE_PROGR BETWEEN " + (lTipoClasse * 10000) + " AND "
							+ (lTipoClasse * 10000 + 9999) + " ) ";
				} else // TipoProgressivo =1
				{
					lCondizioni += " (fasc.CHIAVE_PROGR BETWEEN 1 AND " + (lTipoClasse * 10000 + 9999)
							+ " ) ";
				}
			}

			lCondizioni += " ) ";
		}

		lCondizioni += " AND ali.sog_id_soggetto = sog.id_soggetto ";

		// paolo cherubini 03/01/2011 supersoggetto commento questa riga
		// lCondizioni += " AND sog.id_soggetto = num_proc.sog_id_soggetto ";

		lCondizioni += " AND ali.cod_comune_nascita = comune_nascita.cod_comune ";
		lCondizioni += " AND stato_estero.rv_domain = 'NAZIONE' ";
		lCondizioni += " AND stato_estero.rv_low_value = ali.cod_stato_nascita ";
		lCondizioni += " AND tipo_uff.rv_domain = 'TIPO_UFFICIO' ";
		lCondizioni += " AND tipo_uff.rv_low_value = uff.cod_tipo_ufficio ";
		lCondizioni += " AND fasc.sog_id_soggetto=sog.id_soggetto ";
		lCondizioni += " AND comune_uff.cod_comune = uff.cod_comune ";

		return lCondizioni;
	}

	/**
	 * Altre Condizioni per Soggetto
	 * 
	 * @param SoggettoModel
	 * @return
	 */
	private String setCondizioneSoggetto(SoggettoModel aSm) {
		String lCondizioni = new String();

		if (!(aSm.getCognome().equals("")))
			lCondizioni += " AND SOG.COGNOME like '" + StringUtils.convertSqlString(aSm.getCognome()) + "%'";

		if (!(aSm.getNome().equals("")))
			lCondizioni += " AND SOG.NOME like '" + StringUtils.convertSqlString(aSm.getNome()) + "%'";

		if (aSm.getDataNascita() != null)
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lCondizioni += " AND trunc(SOG.DATA_NASCITA) = TO_DATE('"
					+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";

		if (!(aSm.getCodComuneNascita().equals("")))
			lCondizioni += " AND SOG.COD_COMUNE_NASCITA = '"
					+ StringUtils.convertSqlString(aSm.getCodComuneNascita()) + "'";

		if (!(aSm.getCodStatoNascita().equals("")))
			lCondizioni += " AND SOG.COD_STATO_NASCITA = '" + aSm.getCodStatoNascita() + "'";

		if (aSm.getClassiFascicolo() != null && aSm.getClassiFascicolo().length > 0) {
			String[] lClassiFascicolo = aSm.getClassiFascicolo();
			for (int i = 0; i < lClassiFascicolo.length; i++) {
				if (i != 0) {
					lCondizioni += " OR ";
				} else {
					lCondizioni += " AND (";
				}

				int lTipoClasse = Integer.parseInt(lClassiFascicolo[i]);
				if (lTipoClasse > 1) {
					lCondizioni += " (fasc.CHIAVE_PROGR BETWEEN " + (lTipoClasse * 10000) + " AND "
							+ (lTipoClasse * 10000 + 9999) + " ) ";
				} else // TipoProgressivo =1
				{
					lCondizioni += " (fasc.CHIAVE_PROGR BETWEEN 1 AND " + (lTipoClasse * 10000 + 9999)
							+ " ) ";
				}
			}

			lCondizioni += " ) ";
		}

		// paolo 03/01/2011 lCondizioni += " AND sog.id_soggetto = num_proc.sog_id_soggetto ";
		lCondizioni += " AND sog.cod_comune_nascita = comune_nascita.cod_comune ";
		lCondizioni += " AND stato_estero.rv_domain = 'NAZIONE' ";
		lCondizioni += " AND stato_estero.rv_low_value = sog.cod_stato_nascita ";
		lCondizioni += " AND tipo_uff.rv_domain = 'TIPO_UFFICIO' ";
		lCondizioni += " AND tipo_uff.rv_low_value = uff.cod_tipo_ufficio ";
		lCondizioni += " AND fasc.sog_id_soggetto=sog.id_soggetto ";
		lCondizioni += " AND comune_uff.cod_comune = uff.cod_comune ";

		return lCondizioni;
	}

	/**
	 * Ordinamento
	 * 
	 * @return
	 */
	private String setOrder() {
		String lOrder = new String();
		lOrder = " ORDER BY nominativo ";
		return lOrder;
	}

	/**
	 * Condizioni per ufficio o per distretto Alias
	 * 
	 * @param strCodUfficioUtenteConnesso
	 * @param strCodDistrettoUtenteConnesso
	 * @param strTipoRicerca
	 * @return
	 */
	// private String setCondizioneUfficioDistrettoAlias(String strCodUfficioUtenteConnesso,
	// String strCodDistrettoUtenteConnesso, String strTipoRicerca) {
	// return setCondizioneUfficioDistrettoAlias(strCodUfficioUtenteConnesso, strCodDistrettoUtenteConnesso,
	// strTipoRicerca, "");
	// }

	private String setCondizioneUfficioDistrettoAlias(String strCodUfficioUtenteConnesso,
			String strCodDistrettoUtenteConnesso, String strTipoRicerca, String majorOffice) {
		String lCondizioniUD = new String();

		if (strTipoRicerca != null) {
			if (strTipoRicerca.equals("ufficio")) {
				lCondizioniUD += " FROM alias ali, ";

				// paolo cherubini 03/01/2011 supersoggetto commento queste righe
				// lCondizioniUD +=" (SELECT COUNT (*) procedimenti, sog_id_soggetto FROM fascicolo_siep ";
				// lCondizioniUD +=" where chiave_ufficio = '"+ strCodUfficioUtenteConnesso+"' ";
				// lCondizioniUD +=" GROUP BY sog_id_soggetto) num_proc, ";
				// fine paolo

				lCondizioniUD += " soggetto sog,fascicolo_siep fasc, ";
				lCondizioniUD += " comune comune_nascita, ";
				lCondizioniUD += " v_soggetto_eta vse, ";
				lCondizioniUD += " cg_ref_codes stato_estero, ";
				lCondizioniUD += " (SELECT cod_ufficio, cod_tipo_ufficio, cod_comune FROM ufficio ";
				lCondizioniUD += " WHERE cod_distretto = '" + strCodDistrettoUtenteConnesso + "') uff, ";
				lCondizioniUD += " comune comune_uff, ";
				lCondizioniUD += " cg_ref_codes tipo_uff ";
				lCondizioniUD += " WHERE uff.cod_ufficio = fasc.chiave_ufficio ";
				lCondizioniUD += " AND fasc.chiave_ufficio = '" + strCodUfficioUtenteConnesso + "' ";
				lCondizioniUD += " AND fasc.id_fascicolo_siep = vse.fas_sie_id_fascicolo_siep ";
				if (StringUtils.checkValidValue(majorOffice)) {
					lCondizioniUD += MinorMask.minorCondition("vse", "fasc", majorOffice);
				}
			} else if (strTipoRicerca.equals("distretto")) {
				lCondizioniUD += " FROM alias ali, ";

				// paolo cherubini 03/01/2011 supersoggetto commento queste righe
				// lCondizioniUD +=" (SELECT COUNT (*) procedimenti, sog_id_soggetto FROM fascicolo_siep ";
				// lCondizioniUD +=" where chiave_ufficio in (SELECT cod_ufficio ";
				// lCondizioniUD +=" FROM ufficio ";
				// lCondizioniUD +=" WHERE cod_distretto = '"+ strCodDistrettoUtenteConnesso +"') ";
				// lCondizioniUD +=" GROUP BY sog_id_soggetto) num_proc, ";
				// fine paolo

				lCondizioniUD += " soggetto sog,fascicolo_siep fasc, ";
				lCondizioniUD += " comune comune_nascita, ";
				lCondizioniUD += " v_soggetto_eta vse, ";
				lCondizioniUD += " cg_ref_codes stato_estero, ";
				lCondizioniUD += " (SELECT cod_ufficio, cod_tipo_ufficio, cod_comune FROM ufficio ";
				lCondizioniUD += " WHERE cod_distretto = '" + strCodDistrettoUtenteConnesso + "') uff, ";
				lCondizioniUD += " comune comune_uff, ";
				lCondizioniUD += " cg_ref_codes tipo_uff ";
				lCondizioniUD += " WHERE uff.cod_ufficio = fasc.chiave_ufficio ";
				// 09/2011 Introduzione filtro FLAG_VALIDATO per Ricerca Altre BDI (solo fascicoli validati)
				// Per segnalazione durante periodo garanzia
				lCondizioniUD += " AND (fasc.chiave_ufficio = '" + strCodUfficioUtenteConnesso
						+ "' OR (fasc.chiave_ufficio != '" + strCodUfficioUtenteConnesso
						+ "' AND fasc.FLAG_VALIDATO = 'S'))";
				lCondizioniUD += " AND fasc.id_fascicolo_siep = vse.fas_sie_id_fascicolo_siep ";
				if (StringUtils.checkValidValue(majorOffice)) {
					lCondizioniUD += MinorMask.minorCondition("vse", "fasc", majorOffice);
				}
			}
		}

		return lCondizioniUD;
	}

	/**
	 * Condizioni per ufficio o per distretto Soggetto
	 * 
	 * @param strCodUfficioUtenteConnesso
	 * @param strCodDistrettoUtenteConnesso
	 * @param strTipoRicerca
	 * @return
	 */
	// private String setCondizioneUfficioDistrettoSoggetto(String strCodUfficioUtenteConnesso,
	// String strCodDistrettoUtenteConnesso, String strTipoRicerca) {
	// return setCondizioneUfficioDistrettoSoggetto(strCodUfficioUtenteConnesso,
	// strCodDistrettoUtenteConnesso, strTipoRicerca, "");
	// }

	private String setCondizioneUfficioDistrettoSoggetto(String strCodUfficioUtenteConnesso,
			String strCodDistrettoUtenteConnesso, String strTipoRicerca, String majorOffice) {

		String lCondizioniUD = new String();

		if (strTipoRicerca != null) {
			if (strTipoRicerca.equals("ufficio")) {
				lCondizioniUD += " FROM soggetto sog, fascicolo_siep fasc, ";

				// paolo cherubini 03/01/2011 supersoggetto commento queste righe
				// lCondizioniUD +=" (SELECT COUNT (*) procedimenti, sog_id_soggetto FROM fascicolo_siep ";
				// lCondizioniUD +=" where chiave_ufficio = '"+
				// strCodUfficioUtenteConnesso+"' GROUP BY sog_id_soggetto) num_proc, ";
				// fine paolo

				lCondizioniUD += " comune comune_nascita, ";
				lCondizioniUD += " v_soggetto_eta vse, ";
				lCondizioniUD += " cg_ref_codes stato_estero, ";
				lCondizioniUD += " (SELECT cod_ufficio, cod_tipo_ufficio, cod_comune FROM ufficio ";
				lCondizioniUD += " WHERE cod_distretto = '" + strCodDistrettoUtenteConnesso + "') uff, ";
				lCondizioniUD += " comune comune_uff, ";
				lCondizioniUD += " cg_ref_codes tipo_uff ";
				lCondizioniUD += " WHERE  uff.cod_ufficio = fasc.chiave_ufficio ";
				lCondizioniUD += " AND fasc.chiave_ufficio = '" + strCodUfficioUtenteConnesso + "' ";
				lCondizioniUD += " AND fasc.id_fascicolo_siep = vse.fas_sie_id_fascicolo_siep ";
				if (StringUtils.checkValidValue(majorOffice)) {
					lCondizioniUD += MinorMask.minorCondition("vse", "fasc", majorOffice);
				}
			} else if (strTipoRicerca.equals("distretto")) {
				lCondizioniUD += " FROM soggetto sog, fascicolo_siep fasc, ";

				// paolo cherubini 03/01/2011 supersoggetto commento queste righe
				// lCondizioniUD +=" (SELECT COUNT (*) procedimenti, sog_id_soggetto FROM fascicolo_siep ";
				// lCondizioniUD +=" where chiave_ufficio in (SELECT cod_ufficio ";
				// lCondizioniUD +=" FROM ufficio ";
				// lCondizioniUD +=" WHERE cod_distretto = '"+ strCodDistrettoUtenteConnesso +"') ";
				// lCondizioniUD +=" GROUP BY sog_id_soggetto) num_proc, ";
				// fine paolo

				lCondizioniUD += " comune comune_nascita, ";
				lCondizioniUD += " v_soggetto_eta vse, ";
				lCondizioniUD += " cg_ref_codes stato_estero, ";
				lCondizioniUD += " (SELECT cod_ufficio, cod_tipo_ufficio, cod_comune FROM ufficio ";
				lCondizioniUD += " WHERE cod_distretto = '" + strCodDistrettoUtenteConnesso + "') uff, ";
				lCondizioniUD += " comune comune_uff, ";
				lCondizioniUD += " cg_ref_codes tipo_uff ";
				lCondizioniUD += " WHERE  uff.cod_ufficio = fasc.chiave_ufficio ";

				// 09/2011 Introduzione filtro FLAG_VALIDATO per Ricerca Altre BDI (solo fascicoli validati)
				// Per segnalazione durante periodo garanzia
				lCondizioniUD += " AND (fasc.chiave_ufficio = '" + strCodUfficioUtenteConnesso
						+ "' OR (fasc.chiave_ufficio != '" + strCodUfficioUtenteConnesso
						+ "' AND fasc.FLAG_VALIDATO = 'S'))";
				lCondizioniUD += " AND fasc.id_fascicolo_siep = vse.fas_sie_id_fascicolo_siep ";
				if (StringUtils.checkValidValue(majorOffice)) {
					lCondizioniUD += MinorMask.minorCondition("vse", "fasc", majorOffice);
				}
			}
		}

		return lCondizioniUD;
	}

	/**
	 * Count dei Fascicoli da Soggetto e Alias
	 * 
	 * @param SoggettoModel
	 * @param lCodUfficioUtenteConnesso
	 * @param lCodDistrettoUtenteConnesso
	 * @param TipoRicerca
	 * @return
	 * @throws F3BException
	 */
	public void getCountSoggettoAliasFascicolo(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodDistrettoUtenteConnesso, String strTipoRicerca) {
		getCountSoggettoAliasFascicolo(aModel, strCodUfficioUtenteConnesso, strCodDistrettoUtenteConnesso,
				strTipoRicerca, "");
	}

	public void getCountSoggettoAliasFascicolo(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodDistrettoUtenteConnesso, String strTipoRicerca, String majorOffice) {

		String lStatement = "SELECT COUNT (*) HowManyRecords FROM (";

		// Costruzione della query parametrizzata Alias.
		lStatement += getAliasFascicoliSqlQuery();
		lStatement += setCondizioneUfficioDistrettoAlias(strCodUfficioUtenteConnesso,
				strCodDistrettoUtenteConnesso, strTipoRicerca, majorOffice);
		lStatement += setCondizioneAlias(aModel);

		// paolo cherubini 03/01/2011 aggiungo per raggrupare il supersoggetto
		lStatement += getAliasByGroupSuperSoggetto(); // fine paolo

		// Costruzione della query parametrizzata Soggetto.
		lStatement += getSoggettoFascicoliSqlQuery();
		lStatement += setCondizioneUfficioDistrettoSoggetto(strCodUfficioUtenteConnesso,
				strCodDistrettoUtenteConnesso, strTipoRicerca, majorOffice);
		lStatement += setCondizioneSoggetto(aModel);

		// paolo cherubini 03/01/2011 aggiungo per raggrupare il supersoggetto
		lStatement += getSoggettoByGroupSuperSoggetto(); // fine paolo

		lStatement += setOrder();
		lStatement += " ) A";
		setStatement(lStatement);
	}

	// Model di risposta
	public GenericModel getSoggettoAliasFascicoliModel() throws DAOException {
		SoggettoAliasFascicoloModel lSoggettoAlias = new SoggettoAliasFascicoloModel();

		lSoggettoAlias.setNumeroFascicoliUfficioSedeCompetente(getString("numero_proc"));
		lSoggettoAlias.setSogIdSoggettoAlias(getBigDecimal("id_soggetto_alias"));
		lSoggettoAlias.setCognomeNomeLegatoAlias(getString("alias_di"));
		lSoggettoAlias.setCognomeNomeSoggettoAlias(getString("nominativo"));
		lSoggettoAlias.setLuogoProvinciaNascita(getString("luogo_nascita"));

		// Model del Soggetto per scrivere le altre informazioni
		SoggettoModel lSoggetto = new SoggettoModel();

		// Dati del Soggetto/Alias
		lSoggetto.setDataNascita(getDate("data_nascita"));
		lSoggetto.setPaternita(getString("pater"));
		lSoggetto.setCodAfis(getString("codice_afis"));
		lSoggetto.setIdSoggetto(getBigDecimal("id_sogg"));

		// paolo cherubini per supersoggetto 03/01/2011;
		lSoggetto.setCodFiscale(getString("COD_FISCALE"));
		lSoggetto.setCodCs(getString("COD_CS"));
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA"));
		lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		lSoggetto.setAttoNascita(getString("ATTO_NASCITA"));
		lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		lSoggetto.setNazionalita(getString("NAZIONALITA"));
		lSoggetto.setCognomeMadre(getString("COGNOME_MADRE"));
		lSoggetto.setNomeMadre(getString("NOME_MADRE"));
		lSoggetto.setSesso(getString("SESSO"));
		lSoggetto.setMeseNascita(getBigDecimal("MESE_NASCITA")); // fine paolo

		lSoggetto.setEtaPresuntaAnni(getBigDecimal("ETA_PRESUNTA_ANNI"));
		lSoggetto.setEtaPresuntaMesi(getBigDecimal("ETA_PRESUNTA_MESI"));

		lSoggettoAlias.setSoggetto(lSoggetto);
		return lSoggettoAlias;
	}

	// paolo cherubini 03/01/2011 aggiungo 2 metodi per raggrupare il supersoggetto
	/**
	 * Raggruppamento per il supersoggetto
	 * 
	 * @return
	 */
	protected String getAliasByGroupSuperSoggetto() {
		String lStatement = new String();

		lStatement += " GROUP BY ";
		lStatement += " ali.cognome, ali.nome, ali.data_nascita, comune_nascita.descrizione, ali.cod_provincia_nascita, ";
		lStatement += " stato_estero.rv_meaning, ali.desc_comune_nascita_estero, ali.paternita,";
		// lStatement += " ali.cod_afis, ";
		lStatement += " sog.cod_afis, "; // Paolo Cherubini 27/04/2011 sostituisce quella sopra
		lStatement += " ali.COD_FISCALE, ali.COD_CS,  SOG.COGNOME, SOG.NOME, SOG.ANNO_NASCITA, ";
		lStatement += " SOG.DATA_NASCITA_PRESUNTA, ali.COD_COMUNE_NASCITA,  ali.COD_PROVINCIA_NASCITA, ali.COD_STATO_NASCITA, ";
		lStatement += " ali.ATTO_NASCITA, ali.DESC_COMUNE_NASCITA_ESTERO,  SOG.NAZIONALITA, ";
		lStatement += " SOG.COGNOME_MADRE, SOG.NOME_MADRE,  ali.SESSO, SOG.MESE_NASCITA, SOG.ETA_PRESUNTA_ANNI, SOG.ETA_PRESUNTA_MESI ";
		return lStatement;
	}

	/**
	 * Raggruppamento per il supersoggetto
	 * 
	 * @return
	 */
	protected String getSoggettoByGroupSuperSoggetto() {
		String lStatement = new String();
		lStatement += " GROUP BY ";
		lStatement += " SOG.cognome, SOG.nome, SOG.data_nascita, comune_nascita.descrizione, SOG.cod_provincia_nascita, ";
		lStatement += " stato_estero.rv_meaning, SOG.desc_comune_nascita_estero, SOG.paternita, SOG.cod_afis, ";
		lStatement += " SOG.COD_FISCALE, SOG.COD_CS,  SOG.COGNOME, SOG.NOME, SOG.ANNO_NASCITA, ";
		lStatement += " SOG.DATA_NASCITA_PRESUNTA, SOG.COD_COMUNE_NASCITA,  SOG.COD_PROVINCIA_NASCITA, SOG.COD_STATO_NASCITA, ";
		lStatement += " SOG.ATTO_NASCITA, SOG.DESC_COMUNE_NASCITA_ESTERO,  SOG.NAZIONALITA, ";
		lStatement += " SOG.COGNOME_MADRE, SOG.NOME_MADRE,  SOG.SESSO, SOG.MESE_NASCITA, SOG.ETA_PRESUNTA_ANNI, SOG.ETA_PRESUNTA_MESI ";
		return lStatement;
	}

}