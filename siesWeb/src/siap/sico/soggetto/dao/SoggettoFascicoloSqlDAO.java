package siap.sico.soggetto.dao;

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.MinorMask;

/**
 * SoggettoFascicoloSqlDAO - Classe SqlDAO che rappresenta la tabella Soggetto e Fascicolo Siep
 * 
 * @version 1.0
 */
public class SoggettoFascicoloSqlDAO extends SIAPSqlDAO {

	public SoggettoFascicoloSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	/**
	 * Ricerca Soggetti e Fascicoli
	 *
	 * @param aModel
	 * @param strCodUfficioUtenteConnesso
	 * @param aPage
	 * @param strCodDistrettoUtenteConnesso
	 * @param strTipoRicerca
	 */
	public void ricercaSoggettiFascicoliBySoggettoPaged(SoggettoModel aModel,
			String strCodUfficioUtenteConnesso, int aPage, String strCodDistrettoUtenteConnesso,
			String strTipoRicerca) {
		String strQuery = "";
		String lPaginedStatement = new String("");

		// Costruzione della query parametrizzata per ufficio.
		if (strTipoRicerca != null && strTipoRicerca.equals("ufficio")) {
			strQuery += getFascicoliBySoggettoSqlQueryUfficio(strCodDistrettoUtenteConnesso, aModel);
			strQuery += setCondizione(aModel);
			strQuery += setCondizioneUfficio(strCodUfficioUtenteConnesso, strTipoRicerca);
			strQuery += setOrderCognome();
		}
		// Costruzione della query parametrizzata per distretto.
		else if (strTipoRicerca != null && strTipoRicerca.equals("distretto")) {
			strQuery += getFascicoliBySoggettoSqlQueryDistretto(strCodDistrettoUtenteConnesso, aModel);
			strQuery += setOrderCognome();
		}

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + strQuery
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	// Ambros SuperSoggetto 082009
	/**
	 * Ricerca Soggetti e Fascicoli per SuperSoggetto
	 *
	 * @param aModel
	 * @param strCodUfficioUtenteConnesso
	 * @param aPage
	 * @param strCodDistrettoUtenteConnesso
	 * @param strTipoRicerca
	 */
	public void ricercaSuperSoggettoFascicoliBySoggetto(SoggettoModel aModel,
			String strCodUfficioUtenteConnesso, int aPage, String strCodDistrettoUtenteConnesso,
			String strTipoRicerca) {
		ricercaSuperSoggettoFascicoliBySoggetto(aModel, strCodUfficioUtenteConnesso, aPage,
				strCodDistrettoUtenteConnesso, strTipoRicerca, "", false, "");
	}

	public void ricercaSuperSoggettoFascicoliBySoggetto(SoggettoModel aModel,
			String strCodUfficioUtenteConnesso, int aPage, String strCodDistrettoUtenteConnesso,
			String strTipoRicerca, String majorOffice, boolean fromDetail, String tipoUfficio) {
		String strQuery = "";
		String lPaginedStatement = new String("");

		// query parametrizzata per ufficio.
		if (strTipoRicerca != null && strTipoRicerca.equals("ufficio")) {
			strQuery += getFascicoliBySuperSoggettoSqlQueryUffi1(strCodDistrettoUtenteConnesso, aModel,
					strCodUfficioUtenteConnesso, aPage, majorOffice);
			strQuery += setCondizioneSS(aModel);
			strQuery += setGroupSoggettoSS();
			strQuery += setOrderCognome();
		}
		// Costruzione della query parametrizzata per distretto.
		else if (strTipoRicerca != null && strTipoRicerca.equals("distretto")) {
			strQuery += getFascicoliBySuperSoggettoSqlQueryDistre1(strCodDistrettoUtenteConnesso, aModel,
					strCodUfficioUtenteConnesso, aPage, majorOffice, fromDetail, tipoUfficio);
			strQuery += setCondizioneSS(aModel);
			strQuery += setGroupSoggettoSSDistre1();
			strQuery += setOrderCognome();
		}
		// 03-11-2014 - Ricerca Soggetto per Iscrizione Procedimento Misura Sicurezza PROVVISORIA
		// Costruzione della query su tutta la BaseDati.
		else if (strTipoRicerca != null && strTipoRicerca.equals("tutto")) {
			strQuery += getFascicoliBySuperSoggettoSqlQueryTuttaBDI(strCodDistrettoUtenteConnesso, aModel,
					strCodUfficioUtenteConnesso, aPage);
			strQuery += setCondizioneSS(aModel);
			strQuery += setGroupSoggettoSSDistre1();
			strQuery += setOrderCognome();
		}
		// Ambros 03/2010 Se apage = 0 , la Query serve per contare
		if (aPage == 0) {
			strQuery += ")";
			setStatement(strQuery);
		} else {
			lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + strQuery
					+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
			setStatement(lPaginedStatement);
		}

	}

	// Fine Ambros

	/**
	 * Condizioni di ricerca per numero di fascicoli per il soggetto selezionato per UFFICIO
	 *
	 * @param aModel
	 * @param strCodUfficioUtenteConnesso
	 * @return
	 */
	protected String getFascicoliBySoggettoSqlQueryUfficio(String strCodDistrettoUtenteConnesso,
			SoggettoModel aSogModel) {

		String lStatement = new String();

		lStatement += " SELECT decode(procedimenti,1,tipo_uff.rv_low_value||' di '||comune_uff.descrizione,procedimenti) numero_proc, ";
		lStatement += " sogg.id_soggetto, sogg.cognome cognome, sogg.nome nome, ";
		lStatement += " sogg.sesso sesso, sogg.nome_madre, sogg.cognome_madre, sogg.paternita paternita, ";
		lStatement += " sogg.cod_afis, sogg.data_nascita data_nascita, ";
		lStatement += " sogg.desc_comune_nascita_estero desc_comune_nascita_estero, ";
		lStatement += " descr_com_nascita.descrizione, ";
		lStatement += " sogg.cod_comune_nascita cod_comune_nascita, ";
		lStatement += " num_proc.sog_id_soggetto sog_id_soggetto, ";
		lStatement += " sogg.cod_provincia_nascita cod_provincia_nascita, ";
		lStatement += " comune_uff.descrizione descr_comune, ";
		lStatement += " sogg.cod_stato_nascita, naz.rv_meaning stato, ";
		lStatement += " sogg.cod_cs, sogg.COD_FISCALE, sogg.ANNO_NASCITA, ";
		lStatement += " sogg.MESE_NASCITA, sogg.DATA_NASCITA_PRESUNTA, ";
		lStatement += " sogg.ATTO_NASCITA, sogg.COD_UFFICIO_INSERIMENTO, sogg.NAZIONALITA, ";
		lStatement += " tipo_uff.RV_LOW_VALUE COD_TIPO_UFFICIO, ";
		lStatement += " sogg.ETA_PRESUNTA_ANNI, sogg.ETA_PRESUNTA_MESI ";
		lStatement += " FROM ";
		lStatement += " soggetto sogg, ";
		// lStatement +=" fascicolo_siep fasc, ";
		lStatement += " comune descr_com_nascita, ";
		lStatement += " cg_ref_codes naz, ";
		lStatement += " comune comune_uff,cg_ref_codes tipo_uff, ";
		lStatement += " (SELECT cod_ufficio,cod_tipo_ufficio,cod_comune  FROM ufficio WHERE cod_distretto = '"
				+ strCodDistrettoUtenteConnesso + "') uff, ";
		lStatement += " ( ";
		lStatement += "   select count(*) procedimenti,sog_id_soggetto,chiave_ufficio from fascicolo_siep";

		if (aSogModel.getClassiFascicolo() != null && aSogModel.getClassiFascicolo().length > 0) {
			String[] lClassiFascicolo = aSogModel.getClassiFascicolo();
			for (int i = 0; i < lClassiFascicolo.length; i++) {
				if (i != 0) {
					lStatement += " OR ";
				} else {
					lStatement += " WHERE (";
				}

				int lTipoClasse = Integer.parseInt(lClassiFascicolo[i]);
				if (lTipoClasse > 1) {
					lStatement += " (CHIAVE_PROGR BETWEEN " + (lTipoClasse * 10000) + " AND "
							+ (lTipoClasse * 10000 + 9999) + " ) ";
				} else // TipoProgressivo =1
				{
					lStatement += " (CHIAVE_PROGR BETWEEN 1 AND " + (lTipoClasse * 10000 + 9999) + " ) ";
				}
			}

			lStatement += " ) ";
		}

		lStatement += " group by sog_id_soggetto,chiave_ufficio) num_proc";

		lStatement += " WHERE ";
		lStatement += " sogg.id_soggetto=num_proc.sog_id_soggetto ";
		lStatement += " AND naz.rv_domain = 'NAZIONE' ";
		lStatement += " AND naz.rv_low_value =sogg.cod_stato_nascita ";
		// lStatement +=" AND sogg.id_soggetto=fasc.sog_id_soggetto ";

		return lStatement;

	}

	/**
	 * Condizioni di ricerca per numero di fascicoli per il soggetto selezionato per DISTRETTO
	 *
	 * @param aModel
	 * @param strCodUfficioUtenteConnesso
	 * @param SoggettoModel
	 * @return
	 */
	protected String getFascicoliBySoggettoSqlQueryDistretto(String strCodDistrettoUtenteConnesso,
			SoggettoModel aModel) {
		String lStatement = new String();

		lStatement += " SELECT   DECODE (procedimenti,1,tipo_uff.rv_low_value || ' di ' || comune_uff.descrizione,procedimenti) numero_proc, ";
		lStatement += " sogg.id_soggetto, sogg.cognome cognome, sogg.nome nome, sogg.sesso sesso, ";
		lStatement += " sogg.nome_madre, sogg.cognome_madre, sogg.paternita, sogg.cod_afis, ";
		lStatement += " sogg.data_nascita data_nascita, ";
		lStatement += " sogg.desc_comune_nascita_estero desc_comune_nascita_estero, ";
		lStatement += " descr_com_nascita.descrizione, ";
		lStatement += " sogg.cod_comune_nascita cod_comune_nascita, ";
		lStatement += " num_proc.sog_id_soggetto sog_id_soggetto, ";
		lStatement += " sogg.cod_provincia_nascita cod_provincia_nascita, ";
		lStatement += " sogg.cod_stato_nascita, naz.rv_meaning stato, ";
		lStatement += " sogg.eta_presunta_anni, naz.eta_presunta_mesi ";

		lStatement += " FROM soggetto sogg, ";
		lStatement += " fascicolo_siep fasc, ";
		lStatement += " comune descr_com_nascita, ";
		lStatement += " cg_ref_codes naz, ";
		lStatement += " comune comune_uff, ";
		lStatement += " cg_ref_codes tipo_uff, ";

		lStatement += " (SELECT cod_ufficio, cod_tipo_ufficio, cod_comune ";
		lStatement += " FROM ufficio ";
		lStatement += " WHERE cod_distretto = '" + strCodDistrettoUtenteConnesso + "') uff, ";
		lStatement += " (SELECT   COUNT (*) procedimenti, sog_id_soggetto ";
		lStatement += " FROM fascicolo_siep ";
		lStatement += " where chiave_ufficio in (SELECT cod_ufficio ";
		lStatement += " FROM ufficio ";
		lStatement += " WHERE cod_distretto = '" + strCodDistrettoUtenteConnesso + "') ";
		lStatement += " GROUP BY sog_id_soggetto) num_proc ";
		lStatement += " WHERE sogg.id_soggetto = num_proc.sog_id_soggetto ";
		lStatement += " AND naz.rv_domain = 'NAZIONE' ";
		lStatement += " AND naz.rv_low_value = sogg.cod_stato_nascita ";
		lStatement += setCondizione(aModel);
		lStatement += " AND descr_com_nascita.cod_comune = sogg.cod_comune_nascita ";
		lStatement += " and fasc.sog_id_soggetto=sogg.id_soggetto ";
		lStatement += " AND uff.cod_ufficio = fasc.chiave_ufficio ";
		lStatement += " AND tipo_uff.rv_domain = 'TIPO_UFFICIO' ";
		lStatement += " AND uff.cod_tipo_ufficio = tipo_uff.rv_low_value ";
		lStatement += " AND uff.cod_comune = comune_uff.cod_comune ";
		lStatement += " and procedimenti > 1 ";
		lStatement += " UNION ";

		lStatement += " SELECT   tipo_uff.rv_low_value || ' di ' || comune_uff.descrizione numero_proc, ";
		lStatement += " sogg.id_soggetto, sogg.cognome cognome, sogg.nome nome, sogg.sesso sesso, ";
		lStatement += " sogg.nome_madre, sogg.cognome_madre, sogg.paternita, sogg.cod_afis, ";
		lStatement += " sogg.data_nascita data_nascita, ";
		lStatement += " sogg.desc_comune_nascita_estero desc_comune_nascita_estero, ";
		lStatement += " descr_com_nascita.descrizione, ";
		lStatement += " sogg.cod_comune_nascita cod_comune_nascita, ";
		lStatement += " num_proc.sog_id_soggetto sog_id_soggetto, ";
		lStatement += " sogg.cod_provincia_nascita cod_provincia_nascita, ";
		lStatement += " sogg.cod_stato_nascita, naz.rv_meaning stato ";
		lStatement += " sogg.cod_stato_nascita, naz.rv_meaning stato, ";
		lStatement += " sogg.eta_presunta_anni, naz.eta_presunta_mesi ";
		lStatement += " FROM soggetto sogg, fascicolo_siep fasc, ";
		lStatement += " comune descr_com_nascita, ";
		lStatement += " cg_ref_codes naz, ";
		lStatement += " comune comune_uff, ";
		lStatement += " cg_ref_codes tipo_uff, ";
		lStatement += " (SELECT cod_ufficio, cod_tipo_ufficio, cod_comune ";
		lStatement += " FROM ufficio ";
		lStatement += " WHERE cod_distretto = '" + strCodDistrettoUtenteConnesso + "') uff, ";
		lStatement += " (SELECT   COUNT (*) procedimenti, sog_id_soggetto ";
		lStatement += " FROM fascicolo_siep ";
		lStatement += " where chiave_ufficio in (SELECT cod_ufficio ";
		lStatement += " FROM ufficio ";
		lStatement += " WHERE cod_distretto = '" + strCodDistrettoUtenteConnesso + "') ";
		lStatement += " GROUP BY sog_id_soggetto) num_proc ";
		lStatement += " WHERE sogg.id_soggetto = num_proc.sog_id_soggetto ";
		lStatement += " AND naz.rv_domain = 'NAZIONE' ";
		lStatement += " AND naz.rv_low_value = sogg.cod_stato_nascita ";
		lStatement += setCondizione(aModel);
		lStatement += " AND descr_com_nascita.cod_comune = sogg.cod_comune_nascita ";
		lStatement += " and fasc.sog_id_soggetto=sogg.id_soggetto ";
		lStatement += " AND uff.cod_ufficio = fasc.chiave_ufficio ";
		lStatement += " AND tipo_uff.rv_domain = 'TIPO_UFFICIO' ";
		lStatement += " AND uff.cod_tipo_ufficio = tipo_uff.rv_low_value ";
		lStatement += " AND uff.cod_comune = comune_uff.cod_comune ";
		lStatement += " and procedimenti =1 ";

		return lStatement;

	}

	/*
	 * Condizioni di ricerca inserite nella form di ricerca
	 *
	 * @param SoggettoModel
	 *
	 * @return
	 */
	private String setCondizione(SoggettoModel aSm) {

		String lCondizioni = new String();

		if (aSm.getIdSoggetto().doubleValue() == 0) {
			if (!(aSm.getCognome().equals("")))
				lCondizioni += " AND COGNOME like '" + StringUtils.convertSqlString(aSm.getCognome()) + "%'";
			if (!(aSm.getNome().equals("")))
				lCondizioni += " AND NOME like '" + StringUtils.convertSqlString(aSm.getNome()) + "%'";
			if (!(aSm.getCodComuneNascita().equals("")))
				lCondizioni += " AND COD_COMUNE_NASCITA = '"
						+ StringUtils.convertSqlString(aSm.getCodComuneNascita()) + "'";
			if (!(aSm.getPaternita().equals("")))
				lCondizioni += " AND upper(PATERNITA) LIKE '"
						+ StringUtils.convertSqlString(aSm.getPaternita().toUpperCase()) + "%'";
			if (!(aSm.getCodCs().equals("")))
				lCondizioni += " AND COD_CS LIKE '"
						+ StringUtils.convertSqlString(aSm.getCodCs().toUpperCase()) + "%'";
			if (!(aSm.getNomeMadre().equals("")))
				lCondizioni += " AND upper(NOME_MADRE) LIKE '"
						+ StringUtils.convertSqlString(aSm.getNomeMadre().toUpperCase()) + "%'";
			if (!(aSm.getCognomeMadre().equals("")))
				lCondizioni += " AND upper(COGNOME_MADRE) LIKE '"
						+ StringUtils.convertSqlString(aSm.getCognomeMadre().toUpperCase()) + "%'";
			if (!(aSm.getCodStatoNascita().equals("")))
				lCondizioni += " AND COD_STATO_NASCITA = '" + aSm.getCodStatoNascita() + "'";
			if (!(aSm.getCodAfis().equals("")))
				lCondizioni += " AND COD_AFIS = '" + aSm.getCodAfis() + "'";
			if (aSm.getDataNascita() != null)
				// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
				lCondizioni += " AND trunc(DATA_NASCITA) = TO_DATE('"
						+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
		} else {
			lCondizioni = " AND ID_SOGGETTO = " + aSm.getIdSoggetto();
		}

		return lCondizioni;
	}

	/*
	 * Condizioni per ricerca ufficio, e controllo comune e tipo ufficio
	 *
	 * @param strCodUfficioUtenteConnesso
	 *
	 * @param strTipoRicerca
	 *
	 * @return
	 */
	private String setCondizioneUfficio(String strCodUfficioUtenteConnesso, String strTipoRicerca) {
		String lCondizioniU = new String();

		lCondizioniU += " AND descr_com_nascita.cod_comune = sogg.cod_comune_nascita ";
		lCondizioniU += " and uff.cod_ufficio=num_proc.chiave_ufficio ";
		if (strTipoRicerca != null) {
			if (strTipoRicerca.equals("ufficio")) {
				lCondizioniU += " and num_proc.chiave_ufficio = '" + strCodUfficioUtenteConnesso + "' ";
			}
		}
		lCondizioniU += " and tipo_uff.rv_domain='TIPO_UFFICIO' ";
		lCondizioniU += " and uff.cod_tipo_ufficio=tipo_uff.rv_low_value ";
		lCondizioniU += " and uff.cod_comune=comune_uff.cod_comune ";

		return lCondizioniU;
	}

	/*
	 * Ordinamento
	 *
	 * @return
	 */
	private String setOrderCognome() {

		String lOrder = new String();
		lOrder = " ORDER BY COGNOME, NOME ";
		return lOrder;
	}

	/*
	 * Funzione per il COUNT Soggetti
	 *
	 * @param SoggettoModel
	 *
	 * @param strCodUfficioUtenteConnesso
	 *
	 * @param strCodDistrettoUtenteConnesso
	 *
	 * @param strTipoRicerca
	 *
	 * @return
	 */
	public void getCountSoggettiPerProcedimenti(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodDistrettoUtenteConnesso, String strTipoRicerca) throws DAOException

	{
		String lStatement = "SELECT COUNT (*) HowManyRecords FROM ( ";
		// Costruzione della query parametrizzata per ufficio.
		if (strTipoRicerca != null && strTipoRicerca.equals("ufficio")) {
			lStatement += getFascicoliBySoggettoSqlQueryUfficio(strCodDistrettoUtenteConnesso, aModel);
			lStatement += setCondizione(aModel);
			lStatement += setCondizioneUfficio(strCodUfficioUtenteConnesso, strTipoRicerca);
			lStatement += setOrderCognome();
		}
		// Costruzione della query parametrizzata per distretto.
		else if (strTipoRicerca != null && strTipoRicerca.equals("distretto")) {
			lStatement += getFascicoliBySoggettoSqlQueryDistretto(strCodDistrettoUtenteConnesso, aModel);
			lStatement += setOrderCognome();
		}
		lStatement += " ) A";

		setStatement(lStatement);

	}

	public GenericModel getSoggettoFascicoliModel() throws DAOException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

		// Inserire le opportune set delle descrizioni!
		lFascicolo.setNumFascicoli(getString("NUMERO_PROC"));
		//
		// ambros
		lFascicolo.setDescrComuneUfficio(getString("DESCR_COMUNE"));
		//
		// lFascicolo.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );

		SoggettoModel lSoggetto = new SoggettoModel();
		// lSoggetto.setDescrStatoNascita(getString("STATO") );
		lSoggetto.setCodAfis(getString("COD_AFIS"));
		lSoggetto.setCodCs(getString("COD_CS"));
		lSoggetto.setCodFiscale(getString("COD_FISCALE"));

		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		lSoggetto.setMeseNascita(getBigDecimal("MESE_NASCITA"));
		lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA"));

		lSoggetto.setAttoNascita(getString("ATTO_NASCITA"));

		lSoggetto.setDescrComuneNascita(getString("DESCRIZIONE"));
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));

		lSoggetto.setNazionalita(getString("NAZIONALITA"));
		lSoggetto.setPaternita(getString("PATERNITA"));
		lSoggetto.setCognomeMadre(getString("COGNOME_MADRE"));
		lSoggetto.setNomeMadre(getString("NOME_MADRE"));
		// Ambros aggiunge col SESSO 08/2009
		lSoggetto.setSesso(getString("SESSO"));
		// Ambros
		lSoggetto.setDescrUfficioInserimento(getString("COD_TIPO_UFFICIO"));
		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));

		lSoggetto.setEtaPresuntaAnni(getBigDecimal("ETA_PRESUNTA_ANNI"));
		lSoggetto.setEtaPresuntaMesi(getBigDecimal("ETA_PRESUNTA_MESI"));

		lFascicolo.setSoggetto(lSoggetto);

		return lFascicolo;

	}

	public GenericModel getSoggettoFascicoliModelDistre() throws DAOException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

		// Inserire le opportune set delle descrizioni!
		lFascicolo.setNumFascicoli(getString("NUMERO_PROC"));
		//
		// ambros
		// lFascicolo.setDescrComuneUfficio(getString("DESCR_COMUNE"));
		//
		// lFascicolo.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );

		SoggettoModel lSoggetto = new SoggettoModel();
		// lSoggetto.setDescrStatoNascita(getString("STATO") );
		lSoggetto.setCodAfis(getString("COD_AFIS"));
		lSoggetto.setCodCs(getString("COD_CS"));
		lSoggetto.setCodFiscale(getString("COD_FISCALE"));

		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		lSoggetto.setMeseNascita(getBigDecimal("MESE_NASCITA"));
		lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA"));

		lSoggetto.setAttoNascita(getString("ATTO_NASCITA"));

		lSoggetto.setDescrComuneNascita(getString("DESCRIZIONE"));
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));

		lSoggetto.setNazionalita(getString("NAZIONALITA"));
		lSoggetto.setPaternita(getString("PATERNITA"));
		lSoggetto.setCognomeMadre(getString("COGNOME_MADRE"));
		lSoggetto.setNomeMadre(getString("NOME_MADRE"));
		// Ambros aggiunge col SESSO 08/2009
		lSoggetto.setSesso(getString("SESSO"));
		// Ambros
		// lSoggetto.setDescrUfficioInserimento(getString("COD_TIPO_UFFICIO"));
		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));

		lSoggetto.setEtaPresuntaAnni(getBigDecimal("ETA_PRESUNTA_ANNI"));
		lSoggetto.setEtaPresuntaMesi(getBigDecimal("ETA_PRESUNTA_MESI"));

		lFascicolo.setSoggetto(lSoggetto);

		return lFascicolo;

	}

	/*
	 * private String setGroupSoggetto() { String lGroupBy = new String(); lGroupBy =
	 * " group by Cognome, nome, sogg.DATA_NASCITA, sogg.COD_COMUNE_NASCITA, COMUNE_NASCITA.DESCRIZIONE, sogg.DESC_COMUNE_NASCITA_ESTERO, sogg.COD_PROVINCIA_NASCITA "
	 * ;
	 *
	 * // paolo cherubini x supersoggetto Agosto 2009 // aggiungo le seguenti righe String lSuperSogg = new
	 * String(); lSuperSogg =
	 * ", sogg.COD_FISCALE, sogg.COD_CS, sogg.COD_AFIS, sogg.ANNO_NASCITA, sogg.DATA_NASCITA_PRESUNTA";
	 * lSuperSogg +=
	 * ", sogg.COD_STATO_NASCITA, sogg.NAZIONALITA, sogg.PATERNITA, sogg.COGNOME_MADRE, sogg.NOME_MADRE";
	 * lSuperSogg += ", sogg.SESSO, sogg.ATTO_NASCITA, sogg.MESE_NASCITA, sogg.PROG_ANAG_RES "; lGroupBy +=
	 * lSuperSogg; //fine
	 *
	 * return lGroupBy; }
	 */
	// // Ambros SuperSoggetto 08/2009
	/**
	 * Condizioni di ricerca per numero di fascicoli per il SUPERsoggetto selezionato per UFFICIO
	 *
	 * @param aModel
	 * @param strCodUfficioUtenteConnesso
	 * @return
	 */
	protected String getFascicoliBySuperSoggettoSqlQueryUffi1(String lCodDistretto, SoggettoModel aModel,
			String strCodUfficioUtenteConnesso, int aPage) {
		return getFascicoliBySuperSoggettoSqlQueryUffi1(lCodDistretto, aModel, strCodUfficioUtenteConnesso,
				aPage, "");
	}

	protected String getFascicoliBySuperSoggettoSqlQueryUffi1(String lCodDistretto, SoggettoModel aModel,
			String strCodUfficioUtenteConnesso, int aPage, String majorOffice) {
		String lStatement = new String();
		if (aPage > 0) {
			lStatement += " SELECT  count(*) numero_proc, S.COGNOME COGNOME, S.NOME NOME, S.DATA_NASCITA DATA_NASCITA,  S.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
			lStatement += " S.COD_COMUNE_NASCITA COD_COMUNE_NASCITA, COMUNE_NASCITA.DESCRIZIONE";

			// paolo cherubini x supersoggetto 27 luglio 2009
			// lStatement += " F.SOG_ID_SOGGETTO ID_SOGGETTO, ";
			lStatement += ", 1 ID_SOGGETTO";

			lStatement += ", S.COD_FISCALE, S.COD_CS, S.COD_AFIS, S.ANNO_NASCITA, S.DATA_NASCITA_PRESUNTA";
			lStatement += ", S.COD_STATO_NASCITA, S.NAZIONALITA, S.PATERNITA, S.COGNOME_MADRE, S.NOME_MADRE";
			lStatement += ", S.SESSO, S.ATTO_NASCITA, S.MESE_NASCITA";
			// S.PROG_ANAG_RES, tolgo dal raggrupamento il seguente campo poiche' non e' inserito nella query
			// successiva che fa anch'essa
			// un raggruppamento ma senza questo campo Paolo 29/07/2010
			lStatement += ", S.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASCITA, S.COD_UFFICIO_INSERIMENTO";
			lStatement += ", U.COD_TIPO_UFFICIO COD_TIPO_UFFICIO";
			lStatement += ", UD.DESCR_COMUNE DESCR_COMUNE";
			lStatement += ", S.ETA_PRESUNTA_ANNI ETA_PRESUNTA_ANNI";
			lStatement += ", S.ETA_PRESUNTA_MESI ETA_PRESUNTA_MESI";
		} else {
			lStatement += " Select count(*) HowManyRecords FROM ( Select count(*)  ";
		}

		lStatement += " FROM FASCICOLO_SIEP F, SOGGETTO S, ";
		lStatement += " UFFICIO_DESCR UD,UFFICIO U, COMUNE C, COMUNE COMUNE_NASCITA, V_SOGGETTO_ETA VSE";

		lStatement += " WHERE F.SOG_ID_SOGGETTO = ID_SOGGETTO AND F.CHIAVE_UFFICIO = U.COD_UFFICIO AND U.COD_COMUNE = C.COD_COMUNE";
		// 20250616 [SG]: risolto problema ricerca avvocato senza cod stato nascita
		// Ticket#202506120155 - avvocato con foro incompetente- impossibilità aggiornamento secondo avvocato
		lStatement += " AND S.COD_COMUNE_NASCITA = COMUNE_NASCITA.COD_COMUNE(+)";
		lStatement += " AND F.CHIAVE_UFFICIO = UD.COD_UFFICIO";
		lStatement += " AND F.CHIAVE_UFFICIO ='" + strCodUfficioUtenteConnesso + "'";

		if (aModel.getClassiFascicolo() != null && aModel.getClassiFascicolo().length > 0) {
			String[] lClassiFascicolo = aModel.getClassiFascicolo();
			for (int i = 0; i < lClassiFascicolo.length; i++) {
				if (i != 0) {
					lStatement += " OR ";
				} else {
					lStatement += " AND (";
				}

				int lTipoClasse = Integer.parseInt(lClassiFascicolo[i]);
				if (lTipoClasse > 1) {
					lStatement += " (CHIAVE_PROGR BETWEEN " + (lTipoClasse * 10000) + " AND "
							+ (lTipoClasse * 10000 + 9999) + " ) ";
				} else // TipoProgressivo =1
				{
					lStatement += " (CHIAVE_PROGR BETWEEN 1 AND " + (lTipoClasse * 10000 + 9999) + " ) ";
				}
			}

			lStatement += " ) ";

		} // Chiude if aModel.getClassiFascicolo()

		lStatement += " AND F.ID_FASCICOLO_SIEP = VSE.FAS_SIE_ID_FASCICOLO_SIEP";
		if (StringUtils.checkValidValue(majorOffice)) {
			lStatement += MinorMask.minorCondition("vse", "F", majorOffice);
		}

		return lStatement;
	}

	private String setCondizioneSS(SoggettoModel aSm) {

		String lCondizioni = "";
		if (aSm.getIdSoggetto().doubleValue() == 0) {

			String lCondizioni1 = "";
			String lCondizioni2 = "";
			if (!(aSm.getCognome().equals("")))
				lCondizioni1 += " AND COGNOME like '" + StringUtils.convertSqlString(aSm.getCognome()) + "%'";
			if (!(aSm.getNome().equals("")))
				lCondizioni1 += " AND NOME like '" + StringUtils.convertSqlString(aSm.getNome()) + "%'";
			if (!(aSm.getCodComuneNascita().equals("")))
				lCondizioni1 += " AND COD_COMUNE_NASCITA = '"
						+ StringUtils.convertSqlString(aSm.getCodComuneNascita()) + "'";
			if (!(aSm.getPaternita().equals("")))
				lCondizioni1 += " AND upper(PATERNITA) LIKE '"
						+ StringUtils.convertSqlString(aSm.getPaternita().toUpperCase()) + "%'";
			if (aSm.getCodCs() != null && !(aSm.getCodCs().equals("")))
				lCondizioni1 += " AND COD_CS LIKE '"
						+ StringUtils.convertSqlString(aSm.getCodCs().toUpperCase()) + "%'";
			if (!(aSm.getNomeMadre().equals("")))
				lCondizioni1 += " AND upper(NOME_MADRE) LIKE '"
						+ StringUtils.convertSqlString(aSm.getNomeMadre().toUpperCase()) + "%'";
			if (!(aSm.getCognomeMadre().equals("")))
				lCondizioni1 += " AND upper(COGNOME_MADRE) LIKE '"
						+ StringUtils.convertSqlString(aSm.getCognomeMadre().toUpperCase()) + "%'";
			if (!(aSm.getCodStatoNascita().equals("")))
				lCondizioni1 += " AND COD_STATO_NASCITA = '" + aSm.getCodStatoNascita() + "'";
			if (aSm.getDataNascita() != null)
				// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
				lCondizioni1 += " AND trunc(DATA_NASCITA) = TO_DATE('"
						+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
			if (!(aSm.getDescComuneNascitaEstero().equals("")))
				lCondizioni1 += " AND upper(DESC_COMUNE_NASCITA_ESTERO) LIKE '"
						+ StringUtils.convertSqlString(aSm.getDescComuneNascitaEstero().toUpperCase()) + "%'";
			if (!(aSm.getAttoNascita().equals("")))
				lCondizioni1 += " AND upper(ATTO_NASCITA) = '"
						+ StringUtils.convertSqlString(aSm.getAttoNascita().toUpperCase()) + "'";
			if (!(aSm.getCodAfis().equals("")))
				lCondizioni2 += " COD_AFIS = '" + aSm.getCodAfis() + "'";
			if (lCondizioni1.length() > 0 && lCondizioni2.length() > 0) {
				int primoAnd = lCondizioni1.toLowerCase().indexOf("and");
				lCondizioni1 = lCondizioni1.substring(primoAnd + 3);
				lCondizioni = " AND ((" + lCondizioni1 + ")  OR (" + lCondizioni2 + "))";
			} else if (lCondizioni1.length() > 0) {
				lCondizioni = lCondizioni1;
			} else if (lCondizioni2.length() > 0) {
				lCondizioni = " AND " + lCondizioni2;
			}
		} else {
			lCondizioni = " AND ID_SOGGETTO = " + aSm.getIdSoggetto();
		}

		return lCondizioni;
	}

	// // Ambros SuperSoggetto 08/2009

	/**
	 * Condizioni di GroupBy per il SUPERsoggetto selezionato per UFFICIO
	 *
	 * @param aModel
	 * @param strCodUDistrettoUtente
	 * @return
	 */

	private String setGroupSoggettoSS() {
		String lGroupBy = new String();
		lGroupBy = " group by Cognome, nome, S.DATA_NASCITA, S.COD_COMUNE_NASCITA, COMUNE_NASCITA.DESCRIZIONE, S.DESC_COMUNE_NASCITA_ESTERO, S.COD_PROVINCIA_NASCITA ";
		lGroupBy += ", S.COD_FISCALE, S.COD_CS, S.COD_AFIS, S.ANNO_NASCITA, S.DATA_NASCITA_PRESUNTA";
		lGroupBy += ", S.COD_STATO_NASCITA, S.NAZIONALITA, S.PATERNITA, S.COGNOME_MADRE, S.NOME_MADRE";
		lGroupBy += ", S.SESSO, S.ATTO_NASCITA, S.MESE_NASCITA, S.COD_UFFICIO_INSERIMENTO";
		lGroupBy += ", U.COD_TIPO_UFFICIO, UD.DESCR_COMUNE, ETA_PRESUNTA_ANNI, ETA_PRESUNTA_MESI ";
		// S.PROG_ANAG_RES, tolgo dal raggrupamento il seguente campo poiche' non e' inserito nella query
		// successiva che fa anch'essa
		// un raggruppamento ma senza questo campo Paolo 29/07/2010

		return lGroupBy;
	}

	// // Ambros SuperSoggetto 03/2010

	/**
	 * Condizioni di GroupBy per il SUPERsoggetto selezionato per Distretto
	 *
	 * @param aModel
	 * @param strCodUDistrettoUtente
	 * @return
	 */

	private String setGroupSoggettoSSDistre1() {

		String lGroupBy = new String();
		lGroupBy = " group by Cognome, nome, S.DATA_NASCITA, S.COD_COMUNE_NASCITA, COMUNE_NASCITA.DESCRIZIONE, S.DESC_COMUNE_NASCITA_ESTERO, S.COD_PROVINCIA_NASCITA ";
		lGroupBy += ", S.COD_FISCALE, S.COD_CS, S.COD_AFIS, S.ANNO_NASCITA, S.DATA_NASCITA_PRESUNTA";
		lGroupBy += ", S.COD_STATO_NASCITA, S.NAZIONALITA, S.PATERNITA, S.COGNOME_MADRE, S.NOME_MADRE";
		lGroupBy += ", S.SESSO, S.ATTO_NASCITA, S.MESE_NASCITA, ETA_PRESUNTA_ANNI, ETA_PRESUNTA_MESI";
		// lGroupBy += ", U.COD_TIPO_UFFICIO, UD.DESCR_COMUNE ";

		// S.PROG_ANAG_RES, tolgo dal raggrupamento il seguente campo poiche' non e' inserito nella query
		// successiva che fa anch'essa
		// un raggruppamento ma senza questo campo Paolo 29/07/2010

		return lGroupBy;
	}

	// // Ambros SuperSoggetto 08/2009

	/**
	 * Condizioni di ricerca per numero di fascicoli per il SUPERsoggetto selezionato per UFFICIO
	 *
	 * @param aModel
	 * @param strCodUDistrettoUtente
	 * @return
	 */
	protected String getFascicoliBySuperSoggettoSqlQueryDistre1(String strCodDistrettoUtenteConnesso,
			SoggettoModel aModel, String strCodUfficioUtenteConnesso, int aPage) {
		return getFascicoliBySuperSoggettoSqlQueryDistre1(strCodDistrettoUtenteConnesso, aModel,
				strCodUfficioUtenteConnesso, aPage, "", false, "");
	}

	protected String getFascicoliBySuperSoggettoSqlQueryDistre1(String strCodDistrettoUtenteConnesso,
			SoggettoModel aModel, String strCodUfficioUtenteConnesso, int aPage, String majorOffice,
			boolean fromDetail, String tipoUfficio) {
		String lStatement = new String();
		if (aPage > 0) {
			lStatement += " SELECT  count(*) numero_proc, S.COGNOME COGNOME, S.NOME NOME, S.DATA_NASCITA DATA_NASCITA,  S.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO";
			lStatement += ", S.COD_COMUNE_NASCITA COD_COMUNE_NASCITA,  COMUNE_NASCITA.DESCRIZIONE";

			// paolo cherubini x supersoggetto 27 luglio 2009
			// Non voglio lStatement += " F.SOG_ID_SOGGETTO ID_SOGGETTO, ", perche'
			// altrimenti non raggruuppa i
			// soggetti uguali, ma li distingue tutti per ID_SOGGETTO

			lStatement += ", 1 ID_SOGGETTO";
			lStatement += ", S.COD_FISCALE, S.COD_CS, S.COD_AFIS, S.ANNO_NASCITA, S.DATA_NASCITA_PRESUNTA";
			lStatement += ", S.COD_STATO_NASCITA, S.NAZIONALITA, S.PATERNITA, S.COGNOME_MADRE, S.NOME_MADRE";
			lStatement += ", S.SESSO, S.ATTO_NASCITA, S.MESE_NASCITA";
			// S.PROG_ANAG_RES, tolgo dal raggrupamento il seguente campo poiche' non e' inserito nella query
			// successiva che fa anch'essa
			// un raggruppamento ma senza questo campo Paolo 29/07/2010
			lStatement += ", S.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASCITA";
			lStatement += ", S.ETA_PRESUNTA_ANNI ETA_PRESUNTA_ANNI";
			lStatement += ", S.ETA_PRESUNTA_MESI ETA_PRESUNTA_MESI";

		} else {
			lStatement += " Select count(*) HowManyRecords FROM ( Select count(*)  ";
		}

		lStatement += " FROM FASCICOLO_SIEP F, SOGGETTO S, ";
		lStatement += " UFFICIO U, COMUNE C, COMUNE COMUNE_NASCITA, V_SOGGETTO_ETA VSE";
		lStatement += " WHERE F.SOG_ID_SOGGETTO = ID_SOGGETTO AND U.COD_COMUNE = C.COD_COMUNE";
		// 20250616 [SG]: risolto problema ricerca soggetto senza comune nascita
		lStatement += " AND S.COD_COMUNE_NASCITA = COMUNE_NASCITA.COD_COMUNE(+)";
		lStatement += " AND F.CHIAVE_UFFICIO = U.COD_UFFICIO";
		lStatement += " AND U.COD_DISTRETTO = '" + strCodDistrettoUtenteConnesso + "'";
		// lStatement += " AND F.CHIAVE_UFFICIO = S.COD_UFFICIO_INSERIMENTO";
		// 09/2011 Introduzione filtro FLAG_VALIDATO per Ricerca Altre BDI (solo fascicoli validati)
		// Per segnalazione durante periodo garanzia
		lStatement += " AND (F.CHIAVE_UFFICIO = '" + strCodUfficioUtenteConnesso
				+ "' OR (F.CHIAVE_UFFICIO != '" + strCodUfficioUtenteConnesso
				+ "' AND F.FLAG_VALIDATO = 'S'))";

		lStatement += " AND F.ID_FASCICOLO_SIEP = VSE.FAS_SIE_ID_FASCICOLO_SIEP";
		if (StringUtils.checkValidValue(majorOffice)) {
			// se il tipo ufficio delll'utente connesso e' PGCAP e provengo
			// dal dettaglio, devo poter vedere anche i fascicoli validati di
			// soggetti minorenni iscritti da altri uffici, pertanto non aggiungo
			// il filtro sull'eta' del soggetto
			if (fromDetail && tipoUfficio != null && !tipoUfficio.equals("") && tipoUfficio.equals("PGCAP")) {
				// lStatement += MinorMask.minorConditionPGCAP("vse", "F", majorOffice);
			} else {
				lStatement += MinorMask.minorCondition("vse", "F", majorOffice);
			}
		}
		return lStatement;
	}

	/**
	 * Imposta la query di ricerca sull'intera BDI di tutti i fascicoli collegati a Soggetti avento Nome e
	 * Cognome uguali a quelli del Model passato in Input.
	 *
	 * La query recupera sia i dati del Soggetto che quelli del fascioli
	 *
	 * @param aSoggettoModel
	 */
	public void ricercaFascicoliESoggettoPerSoggettoBDI(SoggettoModel aSoggettoModel) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ANNO_FASCICOLO_UNIONE, FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR,";
		lStatement += " FASC.CHIAVE_UFFICIO, DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_TIPO_UFF.RV_LOW_VALUE  COD_TIPO_UFFICIO,";
		lStatement += " FASC.COD_MOTIVO_ARCHIVIAZIONE, MOTIVO_ARCHIVIAZIONE.RV_MEANING DESCR_MOTIVO_ARCHIVIAZIONE,";
		lStatement += " FASC.COD_OPERATORE_AGGIORNAMENTO, FASC.COD_OPERATORE_INSERIMENTO,";
		lStatement += " FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,";
		// lStatement += " FASC.COD_TIPO_POS_LIBERO, TIPO_POS_LIBERO.RV_MEANING DESCR_TIPO_POS_LIBERO, ";
		lStatement += " FASC.COD_TIPO_POS_LIBERO, '-' DESCR_TIPO_POS_LIBERO, ";
		lStatement += " FASC.COD_UFFICIO_AGGIORNAMENTO, FASC.COD_UFFICIO_INSERIMENTO, FASC.DATA_AGGIORNAMENTO,";
		lStatement += " FASC.DATA_ARCHIVIAZIONE, FASC.DATA_INSERIMENTO,";
		lStatement += " FASC.DATA_ISCRIZIONE, FASC.DATA_UNIONE, FASC.FAS_SIE_ID_FASCICOLO_SIEP,";
		lStatement += " FASC.FLAG_VALIDATO, FASC.ID_FASCICOLO_SIEP, FASC.LETTERA_FASCICOLO,";
		lStatement += " FASC.NOTE NOTE_FASCICOLO, FASC.NUM_FASCICOLO_UNIONE, FASC.SEN_ID_SENTENZA, FASC.SOG_ID_SOGGETTO,";
		lStatement += " FASC.FLAG_ALTRA_CAUSA, FASC.DATA_IRREVOCABILITA, ";
		lStatement += " FASC.FLAG_CUMULANTE, ";
		lStatement += " FASC.FLAG_CUMULATO, ";
		lStatement += " FASC.COD_UFFICIO_UNIONE, DESCR_TIPO_UFFUNIONE.RV_MEANING DESCR_TIPO_UFFICIO_UNIONE, DESCR_COM_UFFUNIONE.DESCRIZIONE DESCR_COMUNE_UFFICIO_UNIONE, FASC.KEY_PROVV_NSC ";
		lStatement += " , FASC.DATA_ARRIVO_ATTO ";
		lStatement += " , FASC.CHIAVE_PROGR_ORIG ";
		lStatement += " , UFFINSERIMENTO.COD_TIPO_UFFICIO COD_TIPO_UFFICIO_INS, DESCR_TIPO_UFFINSERIMENTO.RV_MEANING DESCR_TIPO_UFFICIO_INS, DESCR_COM_UFFINSERIMENTO.DESCRIZIONE DESCR_COMUNE_UFFICIO_INS ";
		lStatement += " , UFFINSERIMENTO.FLAG_ACCORP FLAG_UFFICIO_ACCORPATO";
		// Dati del soggetto
		lStatement += " , ID_SOGGETTO, COD_FISCALE, COD_CS, COD_AFIS ";
		lStatement += " , COGNOME, NOME, ANNO_NASCITA, DATA_NASCITA, DATA_NASCITA_PRESUNTA ";
		lStatement += " , SESSO ";
		lStatement += " , COD_COMUNE_NASCITA "; // , DESCR_COMUNE_NASCITA
		lStatement += " , COD_PROVINCIA_NASCITA "; // , DESCR_PROVINCIA_NASCITA
		lStatement += " , COD_STATO_NASCITA "; // , DESCR_STATO_NASCITA
		lStatement += " , DESC_COMUNE_NASCITA_ESTERO ";
		lStatement += " , NAZIONALITA  "; // DESCR_NAZIONALITA
		lStatement += " , PATERNITA, COGNOME_MADRE, NOME_MADRE ";
		lStatement += " , ATTO_NASCITA ";
		lStatement += " , SOGGETTO.NOTE NOTE_SOGGETTO ";
		lStatement += " , COD_COMUNE_CASELLARIO ";
		// ================================================
		lStatement += " FROM SOGGETTO, FASCICOLO_SIEP FASC";
		lStatement += " LEFT OUTER JOIN UFFICIO UFF ON (FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFF ON (UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFF ON (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE)";
		lStatement += " LEFT OUTER JOIN UFFICIO UFFUNIONE ON (FASC.COD_UFFICIO_UNIONE = UFFUNIONE.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFFUNIONE ON (UFFUNIONE.COD_TIPO_UFFICIO = DESCR_TIPO_UFFUNIONE.RV_LOW_VALUE AND DESCR_TIPO_UFFUNIONE.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFFUNIONE ON (UFFUNIONE.COD_COMUNE = DESCR_COM_UFFUNIONE.COD_COMUNE)";

		lStatement += " LEFT OUTER JOIN CG_REF_CODES STATO_FASCICOLO ON (FASC.COD_STATO_FASCICOLO = STATO_FASCICOLO.RV_LOW_VALUE AND STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO')";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES MOTIVO_ARCHIVIAZIONE ON (FASC.COD_MOTIVO_ARCHIVIAZIONE = MOTIVO_ARCHIVIAZIONE.RV_LOW_VALUE AND MOTIVO_ARCHIVIAZIONE.RV_DOMAIN = 'MOTIVO_ARCHIVIAZIONE')";
		// lStatement +=
		// " LEFT OUTER JOIN CG_REF_CODES TIPO_POS_LIBERO ON (FASC.COD_TIPO_POS_LIBERO =
		// TIPO_POS_LIBERO.RV_LOW_VALUE AND TIPO_POS_LIBERO.RV_DOMAIN = 'TIPO_POS_LIBERO')";
		// Modifica Accorpamento Uffici
		lStatement += " LEFT OUTER JOIN UFFICIO UFFINSERIMENTO ON (FASC.Cod_Ufficio_Inserimento = UFFINSERIMENTO.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFFINSERIMENTO ON (UFFINSERIMENTO.COD_TIPO_UFFICIO = DESCR_TIPO_UFFINSERIMENTO.RV_LOW_VALUE AND DESCR_TIPO_UFFINSERIMENTO.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFFINSERIMENTO ON (UFFINSERIMENTO.COD_COMUNE = DESCR_COM_UFFINSERIMENTO.COD_COMUNE)";
		// =======================================================================
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGGETTO.ID_SOGGETTO";
		lStatement += " AND UPPER(SOGGETTO.COGNOME) = '" + aSoggettoModel.getCognome().toUpperCase() + "'";
		lStatement += " AND UPPER(SOGGETTO.NOME) = '" + aSoggettoModel.getNome().toUpperCase() + "'";

		//
		lStatement += " ORDER BY FASC.DATA_ISCRIZIONE DESC ";

		setStatement(lStatement);

	}

	/**
	 * Metodo get Mode da utilizzare per l'esecuzione della query costruita dal metodo
	 * ricercaFascicoliESoggettoPerSoggettoBDI
	 *
	 * @return
	 * @throws DAOException
	 */
	public GenericModel getModelRicercaFascicoliESoggettoPerSoggettoBDI() throws DAOException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		lFascicolo.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		lFascicolo.setDescrStatoFascicolo(getString("DESCR_STATO_FASCICOLO"));
		lFascicolo.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		lFascicolo.setDataArchiviazione(getDate("DATA_ARCHIVIAZIONE"));
		lFascicolo.setCodMotivoArchiviazione(getString("COD_MOTIVO_ARCHIVIAZIONE"));
		lFascicolo.setDescrMotivoArchiviazione(getString("DESCR_MOTIVO_ARCHIVIAZIONE"));
		lFascicolo.setLetteraFascicolo(getString("LETTERA_FASCICOLO"));
		lFascicolo.setAnnoFascicoloUnione(getString("ANNO_FASCICOLO_UNIONE"));
		lFascicolo.setNumFascicoloUnione(getString("NUM_FASCICOLO_UNIONE"));
		lFascicolo.setDataUnione(getDate("DATA_UNIONE"));
		lFascicolo.setNote(getString("NOTE_FASCICOLO"));
		lFascicolo.setCodTipoPosLibero(getString("COD_TIPO_POS_LIBERO"));
		lFascicolo.setDescrTipoPosLibero(getString("DESCR_TIPO_POS_LIBERO"));
		lFascicolo.setFlagValidato(getString("FLAG_VALIDATO"));
		lFascicolo.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		lFascicolo.setDataInserimento(getDate("DATA_INSERIMENTO"));
		lFascicolo.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		lFascicolo.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		lFascicolo.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		lFascicolo.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		lFascicolo.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		lFascicolo.setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA"));
		lFascicolo.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		lFascicolo.setFlagAltraCausa(getString("FLAG_ALTRA_CAUSA"));
		lFascicolo.setCodTipoUfficio(getString("COD_TIPO_UFFICIO"));
		lFascicolo.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA"));
		lFascicolo.setFlagCumulante(getString("FLAG_CUMULANTE"));
		lFascicolo.setFlagCumulato(getString("FLAG_CUMULATO"));

		lFascicolo.setCodUfficioUnione(getString("COD_UFFICIO_UNIONE"));
		lFascicolo.setDescrTipoUfficioUnione(getString("DESCR_TIPO_UFFICIO_UNIONE"));
		lFascicolo.setDescrComuneUfficioUnione(getString("DESCR_COMUNE_UFFICIO_UNIONE"));
		lFascicolo.setKeyProvvNsc(getBigDecimal("KEY_PROVV_NSC"));

		lFascicolo.setDataArrivoAtto(getDate("DATA_ARRIVO_ATTO"));

		// Modifica Accorpamento Uffici
		lFascicolo.setChiaveProgrOrig(getBigDecimal("CHIAVE_PROGR_ORIG"));
		lFascicolo.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		lFascicolo.setCodTipoUfficioInserimento(getString("COD_TIPO_UFFICIO_INS"));
		lFascicolo.setDescrTipoUfficioInserimento(getString("DESCR_TIPO_UFFICIO_INS"));
		lFascicolo.setDescrComuneUfficioInserimento(getString("DESCR_COMUNE_UFFICIO_INS"));
		lFascicolo.setFlagUfficioAccorpato(getString("FLAG_UFFICIO_ACCORPATO"));

		// =================================
		//
		// =================================
		SoggettoModel lSoggetto = new SoggettoModel();

		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggetto.setCodFiscale(getString("COD_FISCALE"));
		lSoggetto.setCodCs(getString("COD_CS"));
		lSoggetto.setCodAfis(getString("COD_AFIS"));
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA"));
		lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		// lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA") );
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		// lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
		lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		// lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
		lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		lSoggetto.setNazionalita(getString("NAZIONALITA"));
		// lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		lSoggetto.setPaternita(getString("PATERNITA"));
		lSoggetto.setCognomeMadre(getString("COGNOME_MADRE"));
		lSoggetto.setNomeMadre(getString("NOME_MADRE"));
		lSoggetto.setSesso(getString("SESSO"));
		lSoggetto.setAttoNascita(getString("ATTO_NASCITA"));
		lSoggetto.setNote(getString("NOTE_SOGGETTO"));
		lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO"));
		// lSoggetto.setDescrComuneCasellario(getString("") );
		// lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
		// lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		// lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		// lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		// lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );

		lFascicolo.setSoggetto(lSoggetto);

		return lFascicolo;
	}

	// 03-11-2014 - Ricerca Soggetto per Iscrizione Procedimento Misura Sicurezza PROVVISORIA
	/**
	 * Condizioni di ricerca per numero di fascicoli per il SUPERsoggetto selezionato su tutta la BDI
	 *
	 * @param aModel
	 * @return
	 */
	protected String getFascicoliBySuperSoggettoSqlQueryTuttaBDI(String strCodDistrettoUtenteConnesso,
			SoggettoModel aModel, String strCodUfficioUtenteConnesso, int aPage) {

		String lStatement = new String();
		if (aPage > 0) {
			lStatement += " SELECT  count(*) numero_proc, S.COGNOME COGNOME, S.NOME NOME, S.DATA_NASCITA DATA_NASCITA,  "
					+ "S.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO";
			lStatement += ", S.COD_COMUNE_NASCITA COD_COMUNE_NASCITA,  COMUNE_NASCITA.DESCRIZIONE";

			// paolo cherubini x supersoggetto 27 luglio 2009
			// Non voglio lStatement += " F.SOG_ID_SOGGETTO ID_SOGGETTO, ", perche'
			// altrimenti non raggruuppa i
			// soggetti uguali, ma li distingue tutti per ID_SOGGETTO

			lStatement += ", 1 ID_SOGGETTO";
			lStatement += ", S.COD_FISCALE, S.COD_CS, S.COD_AFIS, S.ANNO_NASCITA, S.DATA_NASCITA_PRESUNTA";
			lStatement += ", S.COD_STATO_NASCITA, S.NAZIONALITA, S.PATERNITA, S.COGNOME_MADRE, S.NOME_MADRE";
			lStatement += ", S.SESSO, S.ATTO_NASCITA, S.MESE_NASCITA";
			lStatement += ", S.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASCITA";

		} else {
			lStatement += " Select count(*) HowManyRecords FROM ( Select count(*)  ";
		}

		lStatement += " FROM FASCICOLO_SIEP F, SOGGETTO S, ";
		lStatement += " UFFICIO U, COMUNE C, COMUNE COMUNE_NASCITA";
		lStatement += " WHERE F.SOG_ID_SOGGETTO = ID_SOGGETTO";
		// 20250616 [SG]: risolto problema ricerca avvocato senza cod stato nascita
		// Ticket#202506120155 - avvocato con foro incompetente- impossibilità aggiornamento secondo avvocato
		lStatement += " AND S.COD_COMUNE_NASCITA = COMUNE_NASCITA.COD_COMUNE(+)";
		lStatement += " AND U.COD_COMUNE = C.COD_COMUNE";
		lStatement += " AND F.CHIAVE_UFFICIO = U.COD_UFFICIO";

		// lStatement += " AND U.COD_DISTRETTO = '"+strCodDistrettoUtenteConnesso+"'";
		// lStatement += " AND F.CHIAVE_UFFICIO = S.COD_UFFICIO_INSERIMENTO";
		// 09/2011 Introduzione filtro FLAG_VALIDATO per Ricerca Altre BDI (solo fascicoli validati)
		// Per segnalazione durante periodo garanzia
		// lStatement +=
		// " AND (F.CHIAVE_UFFICIO = '"+strCodUfficioUtenteConnesso+"' OR (F.CHIAVE_UFFICIO !=
		// '"+strCodUfficioUtenteConnesso+"' AND F.FLAG_VALIDATO = 'S'))"
		// ;

		return lStatement;
	}

} // Fine DAO