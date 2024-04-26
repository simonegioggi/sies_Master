package siap.siep.scadenzario.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.scadenzario.model.ScadenzarioModel;

/**
 * Classe SqlDAO che rappresenta la tabella Scadenzario in join con Fascicolo e Soggetto
 *
 * @version 1.0
 */
public class ScadenzarioSoggettoSqlDAO extends SqlDAO {

	public ScadenzarioSoggettoSqlDAO(Connection con) {
		super(con);
	}

	// MEV_39: aggiunto parametro di passaggio
	public void ricercaScadenzarioCompleta(ScadenzarioModel aModel, String tipoRicerca) throws DAOException {

		String lSql = "";
		// Select generale
		if ("CSMS".equals(tipoRicerca))
			lSql = getSqlQueryMisSic();
		else if ("DIFFMS".equals(tipoRicerca)) { // 05/12/2019 : ANOMALIA IN COLLAUDO 11.3 (terza sessione)
			lSql = getSqlQueryNew();
		} else
			lSql = getSqlQueryOLD(); // 05/12/2019 : ANOMALIA IN COLLAUDO 11.3 (terza sessione)
		// Imposto le Condizioni
		lSql += " " + setCondizione(aModel);
		// MEV_39: differenziamo l'ordinamento
		if ("CSMS".equals(tipoRicerca)) {

			// MEV 39 INTERVENTO POST COLLAUDO 113 (ANOMALIA 16)
			lSql += " GROUP BY SOG.ID_SOGGETTO, SOG.NOME, SOG.COGNOME, SOG.DATA_NASCITA, FAS.ID_FASCICOLO_SIEP, FAS.CHIAVE_ANNO,"
					+ " FAS.CHIAVE_PROGR,FASCMS.FAS_SIE_ID_FASCICOLO_COLLEGATO, FASCMS.CHIAVE_ANNO_SIEP_COLLEGATO,"
					+ "  FASCMS.CHIAVE_PROGR_SIEP_COLLEGATO, TIPSCA.RV_MEANING, SCA.ID_SCADENZARIO_SIEP,  PR.DATA_INIZIO,"
					+ " PR.DATA_FINE, SCA.COD_STATO_NOTIFICA, SCA.FLAG_VISTO, TIPCOM.DESCRIZIONE, RMS.RV_MEANING ";

			lSql += " ORDER BY DATA_SCADENZA_COMUNICAZIONE DESC";

			// INIZIO MEV_39: AGGIUNGO LA LISTAGG
			lSql += ") XX GROUP BY xx.ID_SOGGETTO, xx.NOME,xx.COGNOME, xx.DATA_NASCITA,xx.COMUNE_NASCITA,xx.ID_FASCICOLO_SIEP, xx.CHIAVE_ANNO,"
					+ " xx.CHIAVE_PROGR, xx.FAS_SIE_ID_FASCICOLO_COLLEGATO, xx.CHIAVE_ANNO_SIEP_COLLEGATO, xx.CHIAVE_PROGR_SIEP_COLLEGATO,"
					+ " xx.RV_MEANING, xx.ID_SCADENZARIO_SIEP, XX.DATA_INIZIO_SCADENZA, XX.DATA_FINE_SCADENZA, xx.COD_STATO_NOTIFICA, XX.RESIDUO,"
					+ " XX.DATA_SCADENZA_COMUNICAZIONE, xx.FLAG_VISTO ";
			// FINE MEV_39: AGGIUNGO LA LISTAGG
		} else
			lSql += " ORDER BY SCA.DATA_FINE_SCADENZA DESC";
		setStatement(lSql);
	}

	public void ricercaScadenzarioPagedCompleta(ScadenzarioModel aModel, int aPage) throws DAOException {

		// 05/12/2019 : ANOMALIA IN COLLAUDO 11.3 (terza sessione)
		String lSql = new String("");
		if ("20".equals(aModel.getCodTipoScadenzario())) {
			lSql = getSqlQueryNew();
		} else {
			lSql = getSqlQueryOLD();
		}

		String lPaginedStatement = new String("");

		lSql += " " + setCondizione(aModel);
		lSql += " " + setOrderByResiduo();

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	private String getSqlQueryNew() {
		String lStatement = new String("");
		lStatement = "SELECT SOG.ID_SOGGETTO, SOG.NOME, SOG.COGNOME, TIPCOM.DESCRIZIONE COMUNE_NASCITA,";
		lStatement += " SOG.DATA_NASCITA, FAS.ID_FASCICOLO_SIEP, FAS.CHIAVE_ANNO, FAS.CHIAVE_PROGR,";
		lStatement += " SCA.ID_SCADENZARIO_SIEP, SCA.DATA_INIZIO_SCADENZA, SCA.DATA_FINE_SCADENZA,";
		lStatement += " TIPSCA.RV_MEANING, SCA.COD_STATO_NOTIFICA,";
		// MEV_39: aggiunto campo in estrazione
		lStatement += " SCA.FLAG_VISTO,";
		lStatement += " (SCA.DATA_FINE_SCADENZA-TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) RESIDUO";
		lStatement += " FROM SOGGETTO SOG, SCADENZARIO_SIEP SCA, FASCICOLO_SIEP FAS,";
		// MWV 39 , AGGIUNGO LA TABELLA EVENTO
		lStatement += " CG_REF_CODES TIPSCA, COMUNE TIPCOM,  EVENTO E ";
		lStatement += " WHERE SCA.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND FAS.SOG_ID_SOGGETTO = SOG.ID_SOGGETTO";
		lStatement += " AND TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO'";
		lStatement += " AND TIPSCA.RV_LOW_VALUE = SCA.COD_TIPO_SCADENZARIO";
		lStatement += " AND TIPCOM.COD_COMUNE = SOG.COD_COMUNE_NASCITA";
		// MEV 39 , INTERVENTO POST 11.3
		lStatement += " AND E.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND FAS.COD_STATO_FASCICOLO NOT IN ('01', '06')";
		// 05/12/2019 : ANOMALIA IN COLLAUDO 11.3 (terza sessione)
		lStatement += " AND SCA.EVE_ID_EVENTO=E.ID_EVENTO  AND E.FLAG_DOCUMENTO_REGISTRATO ='S' ";

		return lStatement;
	}

	protected String getSqlQueryOLD() {
		String lStatement = new String("");
		lStatement = "SELECT SOG.ID_SOGGETTO, SOG.NOME, SOG.COGNOME, TIPCOM.DESCRIZIONE COMUNE_NASCITA,";
		lStatement += " SOG.DATA_NASCITA, FAS.ID_FASCICOLO_SIEP, FAS.CHIAVE_ANNO, FAS.CHIAVE_PROGR,";
		lStatement += " SCA.ID_SCADENZARIO_SIEP, SCA.DATA_INIZIO_SCADENZA, SCA.DATA_FINE_SCADENZA,";
		lStatement += " TIPSCA.RV_MEANING, SCA.COD_STATO_NOTIFICA,";
		// MEV_39: aggiunto campo in estrazione
		lStatement += " SCA.FLAG_VISTO,";
		lStatement += " (SCA.DATA_FINE_SCADENZA-TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) RESIDUO";
		lStatement += " FROM SOGGETTO SOG, SCADENZARIO_SIEP SCA, FASCICOLO_SIEP FAS,";
		lStatement += " CG_REF_CODES TIPSCA, COMUNE TIPCOM";
		lStatement += " WHERE SCA.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND FAS.SOG_ID_SOGGETTO = SOG.ID_SOGGETTO";
		lStatement += " AND TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO'";
		lStatement += " AND TIPSCA.RV_LOW_VALUE = SCA.COD_TIPO_SCADENZARIO";
		lStatement += " AND TIPCOM.COD_COMUNE = SOG.COD_COMUNE_NASCITA";

		return lStatement;
	}

	// AMBROSINO 04-02-2011 Vers 5.1 - Su segnalazione di Marchese Aggiungo Data
	// VVR alla ricerca Scadenzario
	public void ricercaScadenzarioVVRPagedCompleta(ScadenzarioModel aModel, int aPage) throws DAOException {
		String lStatement = "";

		// If il parametro aPage=0 uso la query solo per il conteggio totale
		// delle righe
		// If il parametro aPage>0 uso la query per caricare il vettore a 20 a
		// 20 coi dati
		// per poi passarlo alla jsp per la lista

		if (aPage > 0) {
			lStatement = new String("SELECT * FROM   ");
			// Ambrosino 03/11/2011 - Aggiungo --> lStatement +=
			// "(SELECT INNER.* , Rownum rn";
			lStatement += "(SELECT INNER.* , Rownum rn FROM ";

			lStatement += "(SELECT SOG.ID_SOGGETTO, SOG.NOME, SOG.COGNOME, TIPCOM.DESCRIZIONE COMUNE_NASCITA,";
			lStatement += " SOG.DATA_NASCITA, FAS.ID_FASCICOLO_SIEP, FAS.CHIAVE_ANNO, FAS.CHIAVE_PROGR,";
			lStatement += " SCA.ID_SCADENZARIO_SIEP, SCA.DATA_INIZIO_SCADENZA, SCA.DATA_FINE_SCADENZA,";
			lStatement += " TIPSCA.RV_MEANING, SCA.COD_STATO_NOTIFICA,";
			lStatement += " (SCA.DATA_FINE_SCADENZA-TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) RESIDUO,";

			lStatement += " VER.DATA_EMISSIONE";
			// Ambrosino 03/11/2011 - Tolgo --> lStatement +=", ROWNUM rn ";
			// lStatement +=", ROWNUM rn ";
		} else {
			lStatement += " SELECT count(*) HowManyRecords ";
		}

		lStatement += " FROM SOGGETTO SOG, SCADENZARIO_SIEP SCA, FASCICOLO_SIEP FAS, EVENTO EVE, VERBALE VER,";
		lStatement += " CG_REF_CODES TIPSCA, COMUNE TIPCOM, EVENTO EVE313";
		lStatement += " WHERE SCA.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND SCA.EVE_ID_EVENTO = EVE.ID_EVENTO";
		lStatement += " AND FAS.SOG_ID_SOGGETTO = SOG.ID_SOGGETTO";
		lStatement += " AND TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO'";
		lStatement += " AND TIPSCA.RV_LOW_VALUE = SCA.COD_TIPO_SCADENZARIO";
		lStatement += " AND TIPCOM.COD_COMUNE = SOG.COD_COMUNE_NASCITA";
		lStatement += " AND EVE313.ID_EVENTO IN (SELECT ID_EVENTO FROM EVENTO EVE313VR WHERE EVE313VR.EVE_ID_EVENTO = EVE.ID_EVENTO";
		lStatement += "        AND EVE313VR.COD_MOTIVO = '0313'";
		lStatement += "        AND DATA_EMISSIONE IN (SELECT MAX(DATA_EMISSIONE)";
		lStatement += "        FROM EVENTO";
		lStatement += "        WHERE EVE_ID_EVENTO = EVE.ID_EVENTO";
		lStatement += "        AND  COD_MOTIVO = '0313'))";
		lStatement += " AND VER.EVE_ID_EVENTO = EVE313.ID_EVENTO";

		lStatement += " " + setCondizione(aModel);

		if (aPage > 0) {
			lStatement += setOrderByResiduo();
			// Ambrosino 03/11/2011 - Cambio Riga 130 con Riga 131
			// lStatement +=
			// " ) WHERE rn BETWEEN "+((aPage-1)*IWebConstants.RESULT_PER_PAGE+1)+
			// " AND "+ (aPage)*IWebConstants.RESULT_PER_PAGE;
			lStatement += " ) INNER )WHERE rn BETWEEN " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		}

		setStatement(lStatement);
	}

	// private String getSqlQueryVVR() {
	// String lStatement = new String("");
	// lStatement = "SELECT SOG.ID_SOGGETTO, SOG.NOME, SOG.COGNOME, TIPCOM.DESCRIZIONE COMUNE_NASCITA,";
	// lStatement += " SOG.DATA_NASCITA, FAS.ID_FASCICOLO_SIEP, FAS.CHIAVE_ANNO, FAS.CHIAVE_PROGR,";
	// lStatement += " SCA.ID_SCADENZARIO_SIEP, SCA.DATA_INIZIO_SCADENZA, SCA.DATA_FINE_SCADENZA,";
	// lStatement += " TIPSCA.RV_MEANING, SCA.COD_STATO_NOTIFICA,";
	// lStatement += " (SCA.DATA_FINE_SCADENZA-TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) RESIDUO,";
	//
	// lStatement += " VER.DATA_EMISSIONE";
	//
	// lStatement += " FROM SOGGETTO SOG, SCADENZARIO_SIEP SCA, FASCICOLO_SIEP FAS, EVENTO EVE, VERBALE VER,";
	// lStatement += " CG_REF_CODES TIPSCA, COMUNE TIPCOM";
	// lStatement += " WHERE SCA.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
	// lStatement += " AND FAS.SOG_ID_SOGGETTO = SOG.ID_SOGGETTO";
	// lStatement += " AND TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO'";
	// lStatement += " AND TIPSCA.RV_LOW_VALUE = SCA.COD_TIPO_SCADENZARIO";
	// lStatement += " AND TIPCOM.COD_COMUNE = SOG.COD_COMUNE_NASCITA";
	//
	// lStatement += " AND EVE.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIE";
	// lStatement += " AND VER.EVE_ID_EVENTO = EVE.ID_EVENTO";
	// lStatement += " AND VER.COD_MOTIVO = '0313'";
	//
	// return lStatement;
	// }
	// End AMBROSINO

	public void ricercaScadenzarioSimeoneCompleta(ScadenzarioModel aModel) throws DAOException {
		String lSql = getSqlQuerySimeone();

		lSql += " " + setCondizione(aModel);
		lSql += " " + setOrderByDataInizioScadenza();

		setStatement(lSql);
	}

	public void ricercaScadenzarioSimeonePagedCompleta(ScadenzarioModel aModel, int aPage)
			throws DAOException {
		String lSql = "";

		// 20171128 [EC]
		// per scadenzario Legge 165/98 (Decreti Sospensione In Definizione) modifico la query per anomalia
		// segnalata da TESTA dovuta a duplicazione di record
		// inseriti su SCADENZARIO_SIEP
		if ("01".equals(aModel.getCodTipoScadenzario()))
			lSql = getSqlQuerySimeoneDecretiSospInDefinizione();
		else
			lSql = getSqlQuerySimeone();

		String lPaginedStatement = new String("");

		lSql += " " + setCondizione(aModel);
		lSql += " " + setOrderByDataInizioScadenza();

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	public void getCountScadenzari(ScadenzarioModel aModel) throws DAOException {
		String lStatement = "SELECT COUNT(*) HowManyRecords ";

		lStatement += " FROM SCADENZARIO_SIEP SCA, CG_REF_CODES TIPSCA, FASCICOLO_SIEP FAS";
		lStatement += " WHERE TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO' AND TIPSCA.RV_LOW_VALUE = COD_TIPO_SCADENZARIO  ";
		lStatement += " AND SCA.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";

		lStatement += " " + setCondizione(aModel);

		setStatement(lStatement);
	}

	private String getSqlQuerySimeone() {
		String lStatement = new String("");

		lStatement = "SELECT SOG.ID_SOGGETTO, SOG.NOME, SOG.COGNOME, ";

		// Paolo Cherubini 16/01/2012 b1/rr/065 sostituisco la seguente riga per
		// ovviare al fatto che non esce il comune nei soggetti nati all'estero
		// per cui si procede con il seguente ordine si prende il comune di
		// nascita se non cè il comune nascita estero se non cè lo stato nascita
		// lStatement += "TIPCOM.DESCRIZIONE COMUNE_NASCITA, ";
		lStatement += " nvl (replace(tipcom.descrizione,'-',sog.DESC_COMUNE_NASCITA_ESTERO), upper(nazione.rv_meaning )) comune_nascita, ";
		// fine Paolo Cherubini 16/01/2012

		lStatement += " SOG.DATA_NASCITA, FAS.ID_FASCICOLO_SIEP, FAS.CHIAVE_ANNO, FAS.CHIAVE_PROGR, FAS.DATA_IRREVOCABILITA,";
		lStatement += " SCA.ID_SCADENZARIO_SIEP, SCA.DATA_INIZIO_SCADENZA, SCA.DATA_FINE_SCADENZA,";
		lStatement += " TIPSCA.RV_MEANING, SCA.COD_STATO_NOTIFICA,";
		lStatement += " (SCA.DATA_FINE_SCADENZA-TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) RESIDUO";
		lStatement += " FROM SOGGETTO SOG, SCADENZARIO_SIEP SCA, FASCICOLO_SIEP FAS,";
		lStatement += " CG_REF_CODES TIPSCA, COMUNE TIPCOM";
		lStatement += " , CG_REF_CODES NAZIONE "; // b1/rr/065
		lStatement += " WHERE SCA.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND FAS.SOG_ID_SOGGETTO = SOG.ID_SOGGETTO";
		lStatement += " AND TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO'";
		lStatement += " AND TIPSCA.RV_LOW_VALUE = SCA.COD_TIPO_SCADENZARIO";
		lStatement += " AND TIPCOM.COD_COMUNE = SOG.COD_COMUNE_NASCITA";
		lStatement += " AND COD_STATO_NASCITA = NAZIONE.RV_LOW_VALUE and NAZIONE.RV_DOMAIN = 'NAZIONE'"; // b1/rr/065

		return lStatement;
	}

	/**
	 * Query per estrarre la lista dei Decreti di Sospensione In Definizione per la Legge 165/98
	 *
	 * @return
	 */
	protected String getSqlQuerySimeoneDecretiSospInDefinizione() {
		String lStatement = new String("");

		lStatement = "SELECT DISTINCT SOG.ID_SOGGETTO, SOG.NOME, SOG.COGNOME, ";

		// Paolo Cherubini 16/01/2012 b1/rr/065 sostituisco la seguente riga per ovviare al fatto che non esce
		// il comune nei soggetti nati all'estero
		// per cui si procede con il seguente ordine si prende il comune di nascita se non cè il comune
		// nascita estero se non cè lo stato nascita
		// lStatement += "TIPCOM.DESCRIZIONE COMUNE_NASCITA, ";
		lStatement += " nvl (replace(tipcom.descrizione,'-',sog.DESC_COMUNE_NASCITA_ESTERO), upper(nazione.rv_meaning )) comune_nascita, ";
		// fine Paolo Cherubini 16/01/2012

		lStatement += " SOG.DATA_NASCITA, FAS.ID_FASCICOLO_SIEP, FAS.CHIAVE_ANNO, FAS.CHIAVE_PROGR, FAS.DATA_IRREVOCABILITA,";
		lStatement += " SCA.ID_SCADENZARIO_SIEP, SCA.DATA_INIZIO_SCADENZA, SCA.DATA_FINE_SCADENZA,";
		lStatement += " TIPSCA.RV_MEANING, SCA.COD_STATO_NOTIFICA,";
		lStatement += " (SCA.DATA_FINE_SCADENZA-TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) RESIDUO";
		lStatement += " FROM SOGGETTO SOG, SCADENZARIO_SIEP SCA, FASCICOLO_SIEP FAS,";
		lStatement += " CG_REF_CODES TIPSCA, COMUNE TIPCOM";
		lStatement += " , CG_REF_CODES NAZIONE "; // b1/rr/065
		lStatement += " WHERE SCA.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND FAS.SOG_ID_SOGGETTO = SOG.ID_SOGGETTO";
		lStatement += " AND TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO'";
		lStatement += " AND TIPSCA.RV_LOW_VALUE = SCA.COD_TIPO_SCADENZARIO";
		lStatement += " AND TIPCOM.COD_COMUNE = SOG.COD_COMUNE_NASCITA";
		lStatement += " AND COD_STATO_NASCITA = NAZIONE.RV_LOW_VALUE and NAZIONE.RV_DOMAIN = 'NAZIONE'"; // b1/rr/065

		return lStatement;
	}

	// 27/03/2015 Scadenzario Fine pena Mis Sic
	public void ricercaScadenzarioPagedCompletaMisSic(ScadenzarioModel aModel, int aPage)
			throws DAOException {

		String lSql = getSqlQueryMisSic();

		String lPaginedStatement = new String("");

		lSql += " " + setCondizione(aModel);
		// MEV 39 INTERVENTO POST COLLAUDO 113 (ANOMALIA 16)

		lSql += " GROUP BY SOG.ID_SOGGETTO, SOG.NOME, SOG.COGNOME, SOG.DATA_NASCITA, FAS.ID_FASCICOLO_SIEP, FAS.CHIAVE_ANNO,"
				+ " FAS.CHIAVE_PROGR,FASCMS.FAS_SIE_ID_FASCICOLO_COLLEGATO, FASCMS.CHIAVE_ANNO_SIEP_COLLEGATO,"
				+ " FASCMS.CHIAVE_PROGR_SIEP_COLLEGATO, TIPSCA.RV_MEANING, SCA.ID_SCADENZARIO_SIEP,  PR.DATA_INIZIO,"
				+ " PR.DATA_FINE, SCA.COD_STATO_NOTIFICA, SCA.FLAG_VISTO, TIPCOM.DESCRIZIONE,RMS.RV_MEANING ";

		// MEV_39: cambiato ordinamento
		lSql += " ORDER BY DATA_SCADENZA_COMUNICAZIONE DESC";

		// INIZIO MEV_39: AGGIUNGO LA LISTAGG
		lSql += ") XX GROUP BY xx.ID_SOGGETTO, xx.NOME,xx.COGNOME, xx.DATA_NASCITA,xx.COMUNE_NASCITA,xx.ID_FASCICOLO_SIEP, xx.CHIAVE_ANNO,"
				+ " xx.CHIAVE_PROGR, xx.FAS_SIE_ID_FASCICOLO_COLLEGATO, xx.CHIAVE_ANNO_SIEP_COLLEGATO, xx.CHIAVE_PROGR_SIEP_COLLEGATO,"
				+ " xx.RV_MEANING, xx.ID_SCADENZARIO_SIEP, XX.DATA_INIZIO_SCADENZA, XX.DATA_FINE_SCADENZA, xx.COD_STATO_NOTIFICA, XX.RESIDUO,"
				+ " XX.DATA_SCADENZA_COMUNICAZIONE, xx.FLAG_VISTO ";
		// FINE MEV_39: AGGIUNGO LA LISTAGG

		// 20191121 [SG]: aggiunto controllo
		if (Utils.isPresent(aPage))
			lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
					+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		else
			lPaginedStatement = "SELECT COUNT(*) totale FROM (" + lSql + ")";

		setStatement(lPaginedStatement);
	}

	private String getSqlQueryMisSic() {

		String lStatement = new String("");
		// INIZIO MEV_39: AGGIUNGO LA LISTAGG
		lStatement += " select xx.ID_SOGGETTO, xx.NOME, xx.COGNOME, xx.DATA_NASCITA, xx.COMUNE_NASCITA, xx.ID_FASCICOLO_SIEP,"
				+ " xx.CHIAVE_ANNO, xx.CHIAVE_PROGR, xx.FAS_SIE_ID_FASCICOLO_COLLEGATO, xx.CHIAVE_ANNO_SIEP_COLLEGATO, xx.CHIAVE_PROGR_SIEP_COLLEGATO,"
				+ " xx.RV_MEANING, xx.ID_SCADENZARIO_SIEP,  XX.DATA_INIZIO_SCADENZA, XX.DATA_FINE_SCADENZA, xx.COD_STATO_NOTIFICA, XX.RESIDUO, "
				+ "XX.DATA_SCADENZA_COMUNICAZIONE, xx.FLAG_VISTO,   listagg(xx.mm, ';' || chr(10)) WITHIN GROUP(ORDER BY xx.ID_SCADENZARIO_SIEP) DESCTIPOMS  from ( ";
		// FINE MEV_39: AGGIUNGO LA LISTAGG
		lStatement += " SELECT SOG.ID_SOGGETTO, SOG.NOME, SOG.COGNOME, SOG.DATA_NASCITA,";
		lStatement += " TIPCOM.DESCRIZIONE COMUNE_NASCITA, FAS.ID_FASCICOLO_SIEP, FAS.CHIAVE_ANNO, FAS.CHIAVE_PROGR,";
		lStatement += " FASCMS.FAS_SIE_ID_FASCICOLO_COLLEGATO, FASCMS.CHIAVE_ANNO_SIEP_COLLEGATO, FASCMS.CHIAVE_PROGR_SIEP_COLLEGATO, TIPSCA.RV_MEANING,";
		lStatement += " SCA.ID_SCADENZARIO_SIEP,"
				+ " NVL(PR.DATA_INIZIO, NULL) DATA_INIZIO_SCADENZA, NVL(PR.DATA_FINE, NULL) DATA_FINE_SCADENZA,"
				+ " SCA.COD_STATO_NOTIFICA,";
		// MEV_39: aggiunti campi in estrazione e relative tabelle e joins
		lStatement += " NVL((PR.DATA_FINE - TO_DATE(TO_CHAR(SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY')), NULL) RESIDUO,";
		lStatement += " NVL(ADD_MONTHS(PR.DATA_FINE, -6), NULL) DATA_SCADENZA_COMUNICAZIONE,";
		lStatement += " SCA.FLAG_VISTO , RMS.RV_MEANING mm";
		lStatement += " FROM SOGGETTO SOG, SCADENZARIO_SIEP SCA, FASCICOLO_SIEP FAS,";
		lStatement += " CG_REF_CODES TIPSCA, COMUNE TIPCOM, FASC_MS_TO_FASC_SIEP FASCMS, PENA_RESIDUA PR, EVENTO EV,";
		lStatement += " MISURA_SICUREZZA MS, CG_REF_CODES RMS, STATO_PROCEDIMENTO   SP ";
		lStatement += " WHERE FAS.SOG_ID_SOGGETTO = SOG.ID_SOGGETTO";
		lStatement += " AND TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO'";
		lStatement += " AND TIPSCA.RV_LOW_VALUE = SCA.COD_TIPO_SCADENZARIO";
		lStatement += " AND TIPCOM.COD_COMUNE = SOG.COD_COMUNE_NASCITA";
		lStatement += " AND SCA.FAS_SIE_ID_FASCICOLO_SIEP = FASCMS.FAS_SIE_ID_FASCICOLO_SIEP(+)";

		lStatement += "  AND SCA.ID_SCADENZARIO_SIEP ="
				+ "                     (SELECT MAX(SCADE.ID_SCADENZARIO_SIEP)"
				+ "                        FROM SCADENZARIO_SIEP SCADE"
				// 20191121 [SG]: aggiunta and condition
				+ "                       WHERE SCADE.FAS_SIE_ID_FASCICOLO_SIEP=FAS.ID_FASCICOLO_SIEP and COD_TIPO_SCADENZARIO = '20')";

		// AGGIUNGO CON MEV_39
		lStatement += " AND FASCMS.FAS_SIE_ID_FASCICOLO_SIEP(+) = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND FAS.ID_FASCICOLO_SIEP = SCA.FAS_SIE_ID_FASCICOLO_SIEP";
		lStatement += " AND PR.FAS_SIE_ID_FASCICOLO_SIEP(+) = SCA.RIF_FASC_SIEP_ORIG AND PR.FLAG_VALIDATO = 'S' AND PR.DATA_FINE IS NOT NULL AND SP.FAS_SIE_ID_FASCICOLO_SIEP=FAS.ID_FASCICOLO_SIEP "
				+ "     AND SP.COD_STATO_PROCEDIMENTO <> '0076' " // 0076 Non devo prendere procedimenti
																	// archiviati/Definiti
				+ " AND PR.EVE_ID_EVENTO(+) = EV.ID_EVENTO AND EV.FLAG_DOCUMENTO_REGISTRATO <> 'A'";
		lStatement += " AND PR.DATA_INSERIMENTO = (SELECT MAX(DATA_INSERIMENTO) FROM PENA_RESIDUA"
				+ "  WHERE FAS_SIE_ID_FASCICOLO_SIEP = SCA.RIF_FASC_SIEP_ORIG AND FLAG_VALIDATO = 'S')";
		lStatement += " AND FAS.FLAG_VALIDATO = 'S' ";
		// MEV_39 [EC] In scadenziario non devono apparire i procedimenti per i quali è stata fatta richiesta
		// di accertamento di pericolosità sociale
		// oppure una trasmissione atti per competenza. Se poi a seguito della trasmissione è stata fatta una
		// restituzione atti (5200 conn esito 01003), il procedimento deve riapparire
		lStatement += " AND ( FAS.ID_FASCICOLO_SIEP NOT IN (SELECT EE.FAS_SIE_ID_FASCICOLO_SIEP"
				+ " FROM EVENTO EE WHERE  EE.FAS_SIE_ID_FASCICOLO_SIEP=FAS.ID_FASCICOLO_SIEP "
				+ " AND EE.COD_MOTIVO IN ('5404', '5416', '2110', '2114') AND EE.FLAG_DOCUMENTO_REGISTRATO <> 'A' ) OR   FAS.ID_FASCICOLO_SIEP  IN"
				+ "  (SELECT EE.FAS_SIE_ID_FASCICOLO_SIEP FROM EVENTO EE, ANNOTAZIONE_ESITO_TRASMISSIONE AA"
				+ "  WHERE EE.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP AND EE.COD_MOTIVO IN ('5200')"
				+ "  AND EE.FLAG_DOCUMENTO_REGISTRATO <> 'A' AND AA.FAS_SIE_ID_FASCICOLO_SIEP=FAS.ID_FASCICOLO_SIEP  AND AA.EVE_ID_EVENTO=EE.ID_EVENTO AND AA.COD_ESITO = '01003') )";
		//
		// lStatement += " AND MS.FAS_SIE_ID_FASCICOLO_SIEP(+) = FAS.ID_FASCICOLO_SIEP";
		// modifico per non estrarre i dati incrociati del titolo 4 e titolo 1
		// 20191121 [SG]: aggiunta where condition
		// 20200124 [SG]: modifica by BVN per risolvere problematica scadenzario classe IV fascicoli mancanti
		// problema riscontrato in collaudo 11.3 con versione oracle installata a PA!
		// lStatement += " AND ((MS.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP and
		// FAS.ID_FASCICOLO_SIEP not in (select AAA.Fas_Sie_Id_Fascicolo_Collegato from FASC_MS_TO_FASC_SIEP
		// AAA where AAA.chiave_progr_siep > 40000"
		// + " ) )or (FASCMS.Fas_Sie_Id_Fascicolo_Siep=FAS.ID_FASCICOLO_SIEP and
		// fascms.chiave_progr_siep>40000 AND MS.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP))";
		lStatement += " AND ((MS.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP and not exists"
				+ " (select AAA.Fas_Sie_Id_Fascicolo_Collegato" + "    from FASC_MS_TO_FASC_SIEP AAA"
				+ " where AAA.chiave_progr_siep > 40000 and FAS.ID_FASCICOLO_SIEP=AAA.Fas_Sie_Id_Fascicolo_Collegato))"
				+ " or" + "(FASCMS.Fas_Sie_Id_Fascicolo_Siep = FAS.ID_FASCICOLO_SIEP and"
				+ " fascms.chiave_progr_siep > 40000 AND"
				+ " MS.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP))";
		//
		lStatement += " AND RMS.RV_DOMAIN = 'TIPO_MISURA_SICUREZZA' AND RMS.RV_LOW_VALUE = MS.COD_TIPO";
		// FINE MEV_39
		return lStatement;
	}

	// <---
	private String setCondizione(ScadenzarioModel aModel) {
		String lCondizioni = new String();

		String ldata1 = new String();
		String ldata2 = new String();

		if (aModel.getDataInizioScadenza() != null)
			ldata1 = DateUtils.getDateToString(aModel.getDataInizioScadenza(), "dd/MM/yyyy");

		if (aModel.getDataFineScadenza() != null)
			ldata2 = DateUtils.getDateToString(aModel.getDataFineScadenza(), "dd/MM/yyyy");

		if (aModel.getCodTipoScadenzario() != null) {
			lCondizioni += " AND SCA.COD_TIPO_SCADENZARIO = '" + aModel.getCodTipoScadenzario() + "'";
		}

		if (aModel.getFasSieIdFascicoloSiep() != null
				&& aModel.getFasSieIdFascicoloSiep().compareTo(new BigDecimal(0)) != 0) {
			lCondizioni += " AND SCA.FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
		}

		// MEV_39: il controllo ora va fatto sul campo DATA_FINE della tabella PENA_RESIDUA
		// per tipo scadenzario = 20
		if (aModel.getTipoRic().equals("sette")) {
			if ("20".equals(aModel.getCodTipoScadenzario()))
				lCondizioni += " AND PR.DATA_FINE BETWEEN TO_DATE('" + ldata1
						+ "','DD/MM/YYYY') AND TO_DATE('" + ldata2 + "','DD/MM/YYYY')";
			else
				lCondizioni += " AND SCA.DATA_FINE_SCADENZA BETWEEN TO_DATE('" + ldata1
						+ "','DD/MM/YYYY') AND TO_DATE('" + ldata2 + "','DD/MM/YYYY')";
		}

		// MEV_39: il controllo ora va fatto sul campo DATA_FINE della tabella PENA_RESIDUA
		// per tipo scadenzario = 20
		// scaduti
		if (aModel.getTipoRic().equals("scaduto")) {
			if ("20".equals(aModel.getCodTipoScadenzario()))
				lCondizioni += " AND PR.DATA_FINE < TO_DATE('" + ldata1 + "','DD/MM/YYYY')";
			else
				lCondizioni += " AND SCA.DATA_FINE_SCADENZA < TO_DATE('" + ldata1 + "','DD/MM/YYYY')";
		}

		// MEV_39: il controllo ora va fatto sul campo DATA_FINE della tabella PENA_RESIDUA
		// per tipo scadenzario = 20
		// oggi
		if (aModel.getTipoRic().equals("oggi")) {
			if ("20".equals(aModel.getCodTipoScadenzario()))
				lCondizioni += " AND PR.DATA_FINE = TO_DATE('" + ldata2 + "','DD/MM/YYYY')";
			else
				lCondizioni += " AND SCA.DATA_FINE_SCADENZA = TO_DATE('" + ldata2 + "','DD/MM/YYYY')";
		}

		// PER UFFICIO
		if (aModel.getCodUfficioInserimento() != null && !aModel.getCodUfficioInserimento().equals("")) {
			lCondizioni += " AND SCA.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
		}

		// Cerca i fascicoli a partire da una coppia Progressivo/Anno
		if ((aModel.getChiaveAnnoIniziale() != null) && (aModel.getChiaveAnnoIniziale().intValue() >= 0)
				&& (aModel.getChiaveProgrIniziale() != null)
				&& (aModel.getChiaveProgrIniziale().intValue() >= 0)) {
			lCondizioni += " AND ( (FAS.CHIAVE_ANNO > " + aModel.getChiaveAnnoIniziale() + ")";
			lCondizioni += " OR (FAS.CHIAVE_ANNO = " + aModel.getChiaveAnnoIniziale()
					+ " AND FAS.CHIAVE_PROGR >= " + aModel.getChiaveProgrIniziale() + "))";
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
				lCondizioni += " AND ( (FAS.CHIAVE_ANNO > " + aModel.getChiaveAnnoFinale() + ")";
				lCondizioni += " OR (FAS.CHIAVE_ANNO = " + aModel.getChiaveAnnoFinale()
						+ " AND FAS.CHIAVE_PROGR >= 1))";
			}

			lCondizioni += " AND ( (FAS.CHIAVE_ANNO < " + aModel.getChiaveAnnoFinale() + ")";
			lCondizioni += " OR (FAS.CHIAVE_ANNO = " + aModel.getChiaveAnnoFinale()
					+ " AND FAS.CHIAVE_PROGR <= " + aModel.getChiaveProgrFinale() + "))";
		}

		// Cerca i fascicoli a partire da una data
		if ((aModel.getDataEmissioneIniziale() != null)) {
			lCondizioni += " AND ( SCA.DATA_INIZIO_SCADENZA >= TO_DATE('"
					+ DateUtils.getDateToString(aModel.getDataEmissioneIniziale(), "ddMMyyyy")
					+ "', 'DDMMYYYY')) ";
		}

		if ((aModel.getDataEmissioneFinale() != null)) {
			lCondizioni += " AND ( SCA.DATA_INIZIO_SCADENZA <= TO_DATE('"
					+ DateUtils.getDateToString(aModel.getDataEmissioneFinale(), "ddMMyyyy")
					+ "', 'DDMMYYYY')) ";
		}

		// 20180122 [EC]
		// per scadenzario Legge 165/98 (Legge Simeone) - email del 22012018 ricevuta da
		// sies.dgsia@giustizia.it
		// DEVO CONSERVARMI LA STRINGA DI CONDIZIONE PER COD_STATO_NOTIFICA
		String sqlCodStatoNotifica = "";
		if (aModel.getCodiciStatoNotifica() != null && aModel.getCodiciStatoNotifica().length > 0) {
			String[] lFiltro = aModel.getCodiciStatoNotifica();

			lCondizioni += " AND SCA.COD_STATO_NOTIFICA IN (";
			sqlCodStatoNotifica += " AND SCA.COD_STATO_NOTIFICA IN (";
			for (int i = 0; i < lFiltro.length; i++) {
				lCondizioni += "'" + lFiltro[i] + "'";
				sqlCodStatoNotifica += "'" + lFiltro[i] + "'";

				if (lFiltro.length > 1 && i < lFiltro.length - 1) {
					lCondizioni += ",";
					sqlCodStatoNotifica += ",";
				}
			}

			lCondizioni += ")";
			sqlCodStatoNotifica += ")";
		}
		// PER STATO NOTIFICA
		else if (aModel.getCodStatoNotifica() != null && !aModel.getCodStatoNotifica().equals("")) {
			lCondizioni += " AND SCA.COD_STATO_NOTIFICA = '" + aModel.getCodStatoNotifica() + "'";
			sqlCodStatoNotifica += " AND SCA.COD_STATO_NOTIFICA = '" + aModel.getCodStatoNotifica() + "'";
		}

		// 20171128 [EC]
		// per scadenzario Legge 165/98 (Decreti Sospensione In Definizione) modifico la query per anomalia
		// segnalata da TESTA dovuta a duplicazione di record
		// inseriti su SCADENZARIO_SIEP
		if ("01".equals(aModel.getCodTipoScadenzario())) {
			lCondizioni += " AND SCA.ID_SCADENZARIO_SIEP IN (SELECT MAX(AAA.ID_SCADENZARIO_SIEP) OVER (PARTITION BY AAA.FAS_SIE_ID_FASCICOLO_SIEP) FROM "
					+ " SCADENZARIO_SIEP AAA WHERE AAA.FAS_SIE_ID_FASCICOLO_SIEP=FAS.ID_FASCICOLO_SIEP   AND aaa.COD_TIPO_SCADENZARIO = '01' "
					+ " AND aaa.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'"
					+ sqlCodStatoNotifica + "  )  ";
		}

		if ("21".equals(aModel.getCodTipoScadenzario())) {
			lCondizioni += "  AND SCA.DATA_INIZIO_SCADENZA IS NOT NULL AND SCA.DATA_FINE_SCADENZA IS NOT NULL  ";
		}

		return lCondizioni;
	}

	private String setOrderByResiduo() {
		return " ORDER BY RESIDUO DESC ";
	}

	private String setOrderByDataInizioScadenza() {
		return " ORDER BY SCA.DATA_INIZIO_SCADENZA ASC ";
	}

	// private String setOrderByIdFascicoloSiep() {
	// return " ORDER BY a.id_fascicolo_siep";
	// }

	public GenericModel getModel() throws DAOException {

		ScadenzarioModel aModel = new ScadenzarioModel();
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
		SoggettoModel lSoggMod = new SoggettoModel();

		lSoggMod.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggMod.setNome(getString("NOME"));
		lSoggMod.setCognome(getString("COGNOME"));
		lSoggMod.setDescrComuneNascita(getString("COMUNE_NASCITA"));
		lSoggMod.setDataNascita(getDate("DATA_NASCITA"));

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setSoggetto(lSoggMod);
		lFascicolo.setSogIdSoggetto(lSoggMod.getIdSoggetto());

		aModel.setIdScadenzario(getBigDecimal("ID_SCADENZARIO_SIEP"));
		aModel.setDataInizioScadenza(getDate("DATA_INIZIO_SCADENZA"));
		aModel.setDataFineScadenza(getDate("DATA_FINE_SCADENZA"));
		aModel.setTipoRic(getString("RV_MEANING"));
		aModel.setGiorniResidui(getBigDecimal("RESIDUO"));
		aModel.setFascicoloModel(lFascicolo);
		aModel.setFasSieIdFascicoloSiep(lFascicolo.getIdFascicoloSiep());
		aModel.setCodStatoNotifica(getString("COD_STATO_NOTIFICA"));
		// MEV_39: aggiunto campo in estrazione
		aModel.setFlagVisto(getString("FLAG_VISTO"));

		return aModel;
	}

	public GenericModel getModelSimeone() throws DAOException {
		ScadenzarioModel aModel = new ScadenzarioModel();
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
		SoggettoModel lSoggMod = new SoggettoModel();

		lSoggMod.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggMod.setNome(getString("NOME"));
		lSoggMod.setCognome(getString("COGNOME"));
		lSoggMod.setDescrComuneNascita(getString("COMUNE_NASCITA"));
		lSoggMod.setDataNascita(getDate("DATA_NASCITA"));

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA"));
		lFascicolo.setSoggetto(lSoggMod);
		lFascicolo.setSogIdSoggetto(lSoggMod.getIdSoggetto());

		aModel.setIdScadenzario(getBigDecimal("ID_SCADENZARIO_SIEP"));
		aModel.setDataInizioScadenza(getDate("DATA_INIZIO_SCADENZA"));
		aModel.setDataFineScadenza(getDate("DATA_FINE_SCADENZA"));
		aModel.setTipoRic(getString("RV_MEANING"));
		aModel.setGiorniResidui(getBigDecimal("RESIDUO"));
		aModel.setFascicoloModel(lFascicolo);
		aModel.setFasSieIdFascicoloSiep(lFascicolo.getIdFascicoloSiep());
		aModel.setCodStatoNotifica(getString("COD_STATO_NOTIFICA"));

		return aModel;
	}

	// AMBROSINO 04-02-2011 Vers 5.1 - Su segnalazione di Marchese Aggiungo Data
	// VVR alla ricerca Scadenzario .
	public GenericModel getVVRModel() throws DAOException {
		ScadenzarioModel aModel = new ScadenzarioModel();
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
		SoggettoModel lSoggMod = new SoggettoModel();

		lSoggMod.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggMod.setNome(getString("NOME"));
		lSoggMod.setCognome(getString("COGNOME"));
		lSoggMod.setDescrComuneNascita(getString("COMUNE_NASCITA"));
		lSoggMod.setDataNascita(getDate("DATA_NASCITA"));

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setSoggetto(lSoggMod);
		lFascicolo.setSogIdSoggetto(lSoggMod.getIdSoggetto());

		aModel.setIdScadenzario(getBigDecimal("ID_SCADENZARIO_SIEP"));
		aModel.setDataInizioScadenza(getDate("DATA_INIZIO_SCADENZA"));
		aModel.setDataFineScadenza(getDate("DATA_FINE_SCADENZA"));
		aModel.setTipoRic(getString("RV_MEANING"));
		aModel.setGiorniResidui(getBigDecimal("RESIDUO"));
		aModel.setFascicoloModel(lFascicolo);
		aModel.setFasSieIdFascicoloSiep(lFascicolo.getIdFascicoloSiep());
		aModel.setCodStatoNotifica(getString("COD_STATO_NOTIFICA"));
		// AMBROSINO 04-02-2011 Vers 5.1 - Su segnalazione di Marchese Aggiungo
		// Data VVR alla ricerca Scadenzario .
		aModel.setDataEmissioneIniziale(getDate("DATA_EMISSIONE"));

		return aModel;
	}

	public GenericModel getModelScadeMisSic() throws DAOException {

		ScadenzarioModel aModel = new ScadenzarioModel();
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
		SoggettoModel lSoggMod = new SoggettoModel();
		FascMsToFascSiepModel lFascToFasc = new FascMsToFascSiepModel();

		lSoggMod.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggMod.setNome(getString("NOME"));
		lSoggMod.setCognome(getString("COGNOME"));
		lSoggMod.setDescrComuneNascita(getString("COMUNE_NASCITA"));
		lSoggMod.setDataNascita(getDate("DATA_NASCITA"));

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setSoggetto(lSoggMod);
		lFascicolo.setSogIdSoggetto(lSoggMod.getIdSoggetto());

		lFascToFasc.setFasSieIdFascicoloCollegato(getBigDecimal("FAS_SIE_ID_FASCICOLO_COLLEGATO"));
		lFascToFasc.setChiaveAnnoSiepCollegato(getBigDecimal("CHIAVE_ANNO_SIEP_COLLEGATO"));
		lFascToFasc.setChiaveProgrSiepCollegato(getBigDecimal("CHIAVE_PROGR_SIEP_COLLEGATO"));

		aModel.setIdScadenzario(getBigDecimal("ID_SCADENZARIO_SIEP"));
		aModel.setDataInizioScadenza(getDate("DATA_INIZIO_SCADENZA"));
		aModel.setDataFineScadenza(getDate("DATA_FINE_SCADENZA"));
		aModel.setTipoRic(getString("RV_MEANING"));
		// MEV_39: modificato campo in estrazione
		aModel.setGiorniResidui(getBigDecimal("RESIDUO"));
		aModel.setDataScadenzaComunicazione(getDate("DATA_SCADENZA_COMUNICAZIONE"));
		aModel.setFascicoloModel(lFascicolo);
		aModel.setFasSieIdFascicoloSiep(lFascicolo.getIdFascicoloSiep());
		aModel.setFasMsToFascSiep(lFascToFasc);
		aModel.setCodStatoNotifica(getString("COD_STATO_NOTIFICA"));
		// MEV_39: aggiunti campi in estrazione
		aModel.setDescrTipoMS(getString("DESCTIPOMS"));
		aModel.setFlagVisto(getString("FLAG_VISTO"));

		return aModel;
	}

	/*
	 * ISSUE MEV : aggiunti metodi di ricerca Numero MEV : 39 Autore : Gioggi Data : 27/giu/2017 Branch :
	 * MEV_39
	 */
	public void ricercaScadenzarioCSMSByKey(BigDecimal idScadenzario, String codUfficioUtenteConnesso)
			throws DAOException {

		String lSql = "";
		lSql += "SELECT SOG.ID_SOGGETTO,";
		lSql += "       SOG.NOME,";
		lSql += "       SOG.COGNOME,";
		lSql += "       SOG.DATA_NASCITA,";
		lSql += "       NULL COMUNE_NASCITA,";
		lSql += "       FAS.ID_FASCICOLO_SIEP,";
		lSql += "       FAS.CHIAVE_ANNO,";
		lSql += "       FAS.CHIAVE_PROGR,";
		lSql += "       NULL FAS_SIE_ID_FASCICOLO_COLLEGATO,";
		lSql += "       NULL CHIAVE_ANNO_SIEP_COLLEGATO,";
		lSql += "       NULL CHIAVE_PROGR_SIEP_COLLEGATO,";
		lSql += "       NULL RV_MEANING,";
		lSql += "       SCA.ID_SCADENZARIO_SIEP,";
		lSql += "       NVL(PR.DATA_INIZIO, NULL) DATA_INIZIO_SCADENZA,";
		lSql += "       NVL(PR.DATA_FINE, NULL) DATA_FINE_SCADENZA,";
		lSql += "       SCA.COD_STATO_NOTIFICA,";
		lSql += "       NVL((PR.DATA_FINE -";
		lSql += "           TO_DATE(TO_CHAR(SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY')),";
		lSql += "           NULL) RESIDUO,";
		lSql += "       NVL(ADD_MONTHS(PR.DATA_FINE, -6), NULL) DATA_SCADENZA_COMUNICAZIONE,";
		lSql += "       NULL DESCTIPOMS,";
		lSql += "       SCA.FLAG_VISTO";
		lSql += "  FROM SOGGETTO         SOG,";
		lSql += "       SCADENZARIO_SIEP SCA,";
		lSql += "       PENA_RESIDUA     PR,";
		lSql += "       FASCICOLO_SIEP   FAS";
		lSql += " WHERE FAS.SOG_ID_SOGGETTO = SOG.ID_SOGGETTO";
		lSql += "   AND SCA.ID_SCADENZARIO_SIEP = " + idScadenzario;
		lSql += "   AND FAS.ID_FASCICOLO_SIEP = SCA.FAS_SIE_ID_FASCICOLO_SIEP";
		lSql += "   AND PR.FAS_SIE_ID_FASCICOLO_SIEP(+) = SCA.RIF_FASC_SIEP_ORIG";
		lSql += "   AND PR.FLAG_VALIDATO = 'S'";
		lSql += "   AND PR.DATA_INSERIMENTO =";
		lSql += "       (SELECT MAX(DATA_INSERIMENTO)";
		lSql += "          FROM PENA_RESIDUA";
		lSql += "         WHERE FAS_SIE_ID_FASCICOLO_SIEP = SCA.RIF_FASC_SIEP_ORIG";
		lSql += "           AND FLAG_VALIDATO = 'S')";
		lSql += "   AND SCA.COD_TIPO_SCADENZARIO = '20'";
		lSql += "   AND SCA.COD_UFFICIO_INSERIMENTO = '" + codUfficioUtenteConnesso + "'";
		lSql += "   AND SCA.COD_STATO_NOTIFICA = 'N'";

		// Select generale
		setStatement(lSql);
	}

	public void getCountScadenzariDifferimentoMS(ScadenzarioModel aScadenzario) throws DAOException {

		String lStatement = "SELECT COUNT(DISTINCT SCA.ID_SCADENZARIO_SIEP) HowManyRecords ";
		lStatement += " FROM SCADENZARIO_SIEP SCA, CG_REF_CODES TIPSCA, FASCICOLO_SIEP FAS, SOGGETTO SOG, COMUNE TIPCOM, EVENTO E";
		lStatement += " WHERE TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO' AND TIPSCA.RV_LOW_VALUE = SCA.COD_TIPO_SCADENZARIO";
		lStatement += " AND SCA.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND FAS.SOG_ID_SOGGETTO = SOG.ID_SOGGETTO AND TIPCOM.COD_COMUNE = SOG.COD_COMUNE_NASCITA";
		lStatement += " AND E.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND E.COD_MOTIVO = '1132'";
		lStatement += " AND FAS.COD_STATO_FASCICOLO NOT IN ('01', '06')";
		lStatement += " AND ((FAS.CHIAVE_PROGR BETWEEN 40000 AND 49999) OR (FAS.CHIAVE_PROGR BETWEEN 10000000040000 AND 10000000049999) OR"
				+ "  (FAS.CHIAVE_PROGR BETWEEN 20000000040000 AND 20000000049999) OR (FAS.CHIAVE_PROGR BETWEEN 30000000040000 AND 30000000049999)) ";
		// PROCEDIMENTI COMPLETI DI DATI DEL DIFFERIMENTO
		lStatement += " AND SCA.DATA_INIZIO_SCADENZA IS NOT NULL  AND SCA.DATA_FINE_SCADENZA IS NOT NULL ";
		// INTERVENTO PER ANOMALIA 14 POST COLLAUDO 11.3
		lStatement += " AND SCA.EVE_ID_EVENTO=E.ID_EVENTO AND E.FLAG_DOCUMENTO_REGISTRATO = 'S' ";

		lStatement += " " + setCondizione(aScadenzario);
		setStatement(lStatement);
	}

	public void ricercaScadenzarioDifferimentoMSPaged(ScadenzarioModel aScadenzario, int aPage) {

		String lPaginedStatement = new String("");
		String lSql = "SELECT DISTINCT SCA.ID_SCADENZARIO_SIEP, " + "SOG.ID_SOGGETTO, " + "SOG.NOME, "
				+ "SOG.COGNOME, " + "TIPCOM.DESCRIZIONE COMUNE_NASCITA, " + "SOG.DATA_NASCITA, "
				+ "FAS.ID_FASCICOLO_SIEP, " + "FAS.CHIAVE_ANNO, " + "FAS.CHIAVE_PROGR, "
				+ "SCA.DATA_INIZIO_SCADENZA, " + "SCA.DATA_FINE_SCADENZA, " + "TIPSCA.RV_MEANING, "
				+ "SCA.COD_STATO_NOTIFICA, " + "SCA.FLAG_VISTO, " + "(SCA.DATA_FINE_SCADENZA - "
				+ "TO_DATE(TO_CHAR(SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY')) RESIDUO " + "FROM SOGGETTO SOG, "
				+ "SCADENZARIO_SIEP SCA, " + "FASCICOLO_SIEP FAS, " + "CG_REF_CODES TIPSCA, "
				+ "COMUNE TIPCOM, " + "EVENTO E "
				+ "WHERE SCA.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP "
				+ "AND FAS.SOG_ID_SOGGETTO = SOG.ID_SOGGETTO " + "AND TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO' "
				+ "AND TIPSCA.RV_LOW_VALUE = SCA.COD_TIPO_SCADENZARIO "
				+ "AND TIPCOM.COD_COMUNE = SOG.COD_COMUNE_NASCITA "
				+ "AND E.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP " + "AND E.COD_MOTIVO = '1132' "
				+ "AND FAS.COD_STATO_FASCICOLO NOT IN ('01', '06') " // FASCICOLI NON ARCHIVIATI/DEFINITI
				// RECUPERARE SOLO I PROCEDIMENTI DI CLASSE IV
				+ " AND ((FAS.CHIAVE_PROGR BETWEEN 40000 AND 49999) OR (FAS.CHIAVE_PROGR BETWEEN 10000000040000 AND 10000000049999) OR"
				+ " (FAS.CHIAVE_PROGR BETWEEN 20000000040000 AND 20000000049999) OR (FAS.CHIAVE_PROGR BETWEEN 30000000040000 AND 30000000049999))";
		// PROCEDIMENTI COMPLETI DI DATI DEL DIFFERIMENTO
		lSql += " AND SCA.DATA_INIZIO_SCADENZA IS NOT NULL  AND SCA.DATA_FINE_SCADENZA IS NOT NULL ";
		// INTERVENTO PER ANOMALIA 14 POST COLLAUDO 11.3
		// 05/12/2019 : ULTERIORE ANOMALIA IN COLLAUDO 11.3 DI NOVEMBRE 2019: DEVONO ESSERE VISIBILI IN
		// SCADENZIARIO SOLO QUELLI VALIDATI
		lSql += " AND SCA.EVE_ID_EVENTO=E.ID_EVENTO AND E.FLAG_DOCUMENTO_REGISTRATO = 'S'";

		lSql += setCondizione(aScadenzario);
		lSql += " ORDER BY SCA.DATA_FINE_SCADENZA DESC";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
				+ ") INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + " AND "
				+ (aPage) * IWebConstants.RESULT_PER_PAGE;
		setStatement(lPaginedStatement);
	}
	// ***** FINE INTERVENTO MEV_39 *****//

	// MEV_2023-33
	private String getSqlQueryScadenzarioPP() {
		String sqlQuery = "";
		sqlQuery += "SELECT SCA.ID_SCADENZARIO_SIEP, SCA.EVE_ID_EVENTO, SCA.NOT_ID_NOTIFICA ";
		sqlQuery += "     , SOG.ID_SOGGETTO, SOG.NOME, SOG.COGNOME, TIPCOM.DESCRIZIONE COMUNE_NASCITA, SOG.DATA_NASCITA ";
		sqlQuery += "     , FAS.ID_FASCICOLO_SIEP, FAS.CHIAVE_ANNO, FAS.CHIAVE_PROGR ";
		sqlQuery += "     , SCA.DATA_INIZIO_SCADENZA, SCA.DATA_FINE_SCADENZA ";
		sqlQuery += "     , BOL.IMPORTO_RATA, BOL.PROG_RATA, BOL.NUMERO_RATE, BOL.DATA_SCADENZA, BOL.STATO_PAGAMENTO, BOL.IMPORTO_PAGATO ";
		sqlQuery += "     , BOL.RAT_ID_RATEIZZAZIONE_PP, BOL.DATA_AVV_PAGAMENTO, BOL.TIPO_RATEIZZAZIONE ";
		sqlQuery += "     , TIPSCA.RV_MEANING, SCA.COD_STATO_NOTIFICA, SCA.FLAG_VISTO ";
		sqlQuery += "     , (BOL.DATA_SCADENZA-TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) RESIDUO  ";
		sqlQuery += "  FROM SOGGETTO SOG, SCADENZARIO_SIEP SCA, FASCICOLO_SIEP FAS, BOLLETTINO_PAGOPA BOL ";
		sqlQuery += "     , CG_REF_CODES TIPSCA, COMUNE TIPCOM ";
		sqlQuery += "     , RATEIZZAZIONE_PP RAT  ";
		sqlQuery += "     , EVENTO EVE  ";
		sqlQuery += " WHERE SCA.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP  ";
		sqlQuery += "   AND FAS.SOG_ID_SOGGETTO = SOG.ID_SOGGETTO  ";
		sqlQuery += "   AND TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO'  ";
		sqlQuery += "   AND TIPSCA.RV_LOW_VALUE = SCA.COD_TIPO_SCADENZARIO  ";
		sqlQuery += "   AND TIPCOM.COD_COMUNE = SOG.COD_COMUNE_NASCITA  ";
		sqlQuery += "   AND BOL.RAT_ID_RATEIZZAZIONE_PP = RAT.ID_RATEIZZAZIONE_PP ";
		sqlQuery += "   AND RAT.EVE_ID_EVENTO = SCA.EVE_ID_EVENTO ";
		sqlQuery += "   AND BOL.STATO_PAGAMENTO = 'PN' "; // solo i non pagati
		//
		sqlQuery += "   AND BOL.IUV IS NOT null ";
		sqlQuery += "   AND BOL.DATA_SCADENZA IS NOT null ";
		sqlQuery += "   AND RAT.EVE_ID_EVENTO = EVE.ID_EVENTO ";
		sqlQuery += "   AND EVE.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// sqlQuery += " AND BOL.PROG_RATA=1 ";

		return sqlQuery;
	}

	public void getCountScadenzariPP(ScadenzarioModel aModel) throws DAOException {
		String lStatement = "SELECT COUNT(*) HowManyRecords ";
		lStatement += " from (" + getSqlQueryScadenzarioPP();
		lStatement += " " + setCondizionePP(aModel);
		lStatement += ")";

		setStatement(lStatement);
	}

	public void ricercaScadenzarioPagedPPCompleta(ScadenzarioModel aModel, int aPage) throws DAOException {
		String lSql = new String("");
		lSql = getSqlQueryScadenzarioPP();

		String lPaginedStatement = new String("");

		lSql += " " + setCondizionePP(aModel);

		lSql += " order by BOL.DATA_SCADENZA ASC ";
		// lSql += " " + setOrderByResiduo();

		if (aPage == 0)
			lPaginedStatement = lSql;
		else
			lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
					+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	public GenericModel getModelScadePP() throws DAOException {

		ScadenzarioModel aModel = new ScadenzarioModel();
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
		SoggettoModel lSoggMod = new SoggettoModel();
		BollettinoPagopaModel lBollettino = new BollettinoPagopaModel();

		lSoggMod.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggMod.setNome(getString("NOME"));
		lSoggMod.setCognome(getString("COGNOME"));
		lSoggMod.setDescrComuneNascita(getString("COMUNE_NASCITA"));
		lSoggMod.setDataNascita(getDate("DATA_NASCITA"));

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setSoggetto(lSoggMod);
		lFascicolo.setSogIdSoggetto(lSoggMod.getIdSoggetto());

		lBollettino.setProgRata(getInt("PROG_RATA"));
		lBollettino.setNumeroRate(getInt("NUMERO_RATE"));
		lBollettino.setImportoRata(getBigDecimal("IMPORTO_RATA"));
		lBollettino.setImportoPagato(getBigDecimal("IMPORTO_PAGATO"));
		lBollettino.setDataScadenza(getDate("DATA_SCADENZA"));
		lBollettino.setStatoPagamento(getString("STATO_PAGAMENTO"));
		lBollettino.setTipoRateizzazione(getString("TIPO_RATEIZZAZIONE"));

		aModel.setIdScadenzario(getBigDecimal("ID_SCADENZARIO_SIEP"));
		aModel.setDataInizioScadenza(getDate("DATA_INIZIO_SCADENZA"));
		aModel.setDataFineScadenza(getDate("DATA_FINE_SCADENZA"));
		aModel.setTipoRic(getString("RV_MEANING"));
		aModel.setGiorniResidui(getBigDecimal("RESIDUO"));
		aModel.setFascicoloModel(lFascicolo);
		aModel.setFasSieIdFascicoloSiep(lFascicolo.getIdFascicoloSiep());
		aModel.setCodStatoNotifica(getString("COD_STATO_NOTIFICA"));
		aModel.setFlagVisto(getString("FLAG_VISTO"));

		aModel.setBollettinoModel(lBollettino);

		return aModel;
	}

	private String setCondizionePP(ScadenzarioModel aModel) {

		String lCondizioni = new String();

		String ldata1 = new String();
		String ldata2 = new String();

		if (aModel.getDataInizioScadenza() != null)
			ldata1 = DateUtils.getDateToString(aModel.getDataInizioScadenza(), "dd/MM/yyyy");

		if (aModel.getDataFineScadenza() != null)
			ldata2 = DateUtils.getDateToString(aModel.getDataFineScadenza(), "dd/MM/yyyy");

		if (aModel.getCodTipoScadenzario() != null) {
			lCondizioni += " AND SCA.COD_TIPO_SCADENZARIO = '" + aModel.getCodTipoScadenzario() + "'";
		}

		if (aModel.getFasSieIdFascicoloSiep() != null
				&& aModel.getFasSieIdFascicoloSiep().compareTo(new BigDecimal(0)) != 0) {
			lCondizioni += " AND SCA.FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
		}

		if (aModel.getTipoRic().equals("sette")) {
			lCondizioni += " AND BOL.DATA_SCADENZA BETWEEN TO_DATE('" + ldata1
					+ "','DD/MM/YYYY') AND TO_DATE('" + ldata2 + "','DD/MM/YYYY')";
		} else if (aModel.getTipoRic().equals("scaduto")) {
			lCondizioni += " AND BOL.DATA_SCADENZA < TO_DATE('" + ldata1 + "','DD/MM/YYYY')";
		} else if (aModel.getTipoRic().equals("oggi")) {
			lCondizioni += " AND BOL.DATA_SCADENZA = TO_DATE('" + ldata2 + "','DD/MM/YYYY')";
		}

		// PER UFFICIO
		if (aModel.getCodUfficioInserimento() != null && !aModel.getCodUfficioInserimento().equals("")) {
			lCondizioni += " AND SCA.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
		}

		// Cerca i fascicoli a partire da una coppia Progressivo/Anno
		if (aModel.getChiaveAnnoIniziale() != null && aModel.getChiaveAnnoIniziale().intValue() >= 0
				&& aModel.getChiaveProgrIniziale() != null
				&& aModel.getChiaveProgrIniziale().intValue() >= 0) {
			lCondizioni += " AND (   (FAS.CHIAVE_ANNO > " + aModel.getChiaveAnnoIniziale() + ")";
			lCondizioni += "      OR (    FAS.CHIAVE_ANNO = " + aModel.getChiaveAnnoIniziale();
			lCondizioni += "          AND FAS.CHIAVE_PROGR >= " + aModel.getChiaveProgrIniziale() + "))";
		}

		// Cerca i fascicoli fino ad una coppia Progressivo/Anno
		if (aModel.getChiaveAnnoFinale() != null && aModel.getChiaveAnnoFinale().intValue() >= 0
				&& aModel.getChiaveProgrFinale() != null && aModel.getChiaveProgrFinale().intValue() >= 0) {
			if ((aModel.getChiaveAnnoIniziale() == null)
					|| (aModel.getChiaveAnnoIniziale().intValue() <= 0)
							&& (aModel.getChiaveProgrIniziale() == null)
					|| (aModel.getChiaveProgrIniziale().intValue() <= 0)) {
				lCondizioni += " AND (   (FAS.CHIAVE_ANNO > " + aModel.getChiaveAnnoFinale() + ")";
				lCondizioni += "      OR (    FAS.CHIAVE_ANNO = " + aModel.getChiaveAnnoFinale();
				lCondizioni += "          AND FAS.CHIAVE_PROGR >= 1))";
			}

			lCondizioni += " AND (   (FAS.CHIAVE_ANNO < " + aModel.getChiaveAnnoFinale() + ")";
			lCondizioni += "      OR (    FAS.CHIAVE_ANNO = " + aModel.getChiaveAnnoFinale();
			lCondizioni += "          AND FAS.CHIAVE_PROGR <= " + aModel.getChiaveProgrFinale() + "))";
		}

		return lCondizioni;
	}

}