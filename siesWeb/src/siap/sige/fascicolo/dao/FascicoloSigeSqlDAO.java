package siap.sige.fascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.util.Utils;
import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.fascicolo.model.RicercaFascicoloSigeModel;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.richiesta.model.RichiestaSigeModel;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;

/**
 * <p>
 * Title: FascicoloSigeSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella FascicoloSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @version 1.0
 */
public class FascicoloSigeSqlDAO extends SIAPSqlDAO {

	public FascicoloSigeSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaFascicoloSige(FascicoloSigeModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaFascicoloSigeByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_FASCICOLO_SIGE, " + "SOG_ID_SOGGETTO, " + "CHIAVE_ANNO, "
				+ "CHIAVE_UFFICIO, " + "'' DESCR_TIPO_UFFICIO, " + "'' DESCR_COMUNE_UFFICIO, "
				+ "null DATA_NASCITA, " + "'' NOME, " + "'' COGNOME, " + "CHIAVE_PROGR, " + "SEZ_ID_SEZIONE, "
				+ "COD_STATO_FASCICOLO, " + "COD_TIPO_GIUDIZIO, " + "DATA_ISCRIZIONE, " + "DATA_DEFINIZIONE, "
				+ "RIC_ID_RICHIESTA_SIGE, " + "COD_OPERATORE_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "NOTE, " + "COD_POSIZIONE_GIURIDICA, " + "DATA_FINE_PENA, "
				+ "COD_TIPO_DEFINIZIONE, " + "DESCR_DEFINIZIONE, " + "FAS_SIG_ID_FASCICOLO_SIGE, "
				+ "NUMERO_FASCICOLI_UNIFICATI, " + "PG.RV_MEANING POSIZIONE_GIURIDICA, " +
				// Modifica Accorpamento Uffici
				"CHIAVE_PROGR_ORIG, " + "UFFINSERIMENTO.COD_TIPO_UFFICIO COD_TIPO_UFFICIO_INS, "
				+ "DESCR_TIPO_UFFINSERIMENTO.RV_MEANING DESCR_TIPO_UFFICIO_INS, "
				+ "DESCR_COM_UFFINSERIMENTO.DESCRIZIONE DESCR_COMUNE_UFFICIO_INS, "
				+ "UFFINSERIMENTO.FLAG_ACCORP FLAG_UFFICIO_ACCORPATO, " + "ID_FASCICOLO_SIGE_ORIGINE, "
				+ "SEN_ID_SENTENZA_CUMULO, " + "ID_EVENTO_PROVV_CUMULO";
		lStatement += " FROM FASCICOLO_SIGE LEFT OUTER JOIN CG_REF_CODES PG ON (COD_POSIZIONE_GIURIDICA = PG.RV_LOW_VALUE AND PG.RV_DOMAIN = 'POSIZIONE_GIURIDICA')";
		// Modifica Accorpamento Uffici
		lStatement += " LEFT OUTER JOIN UFFICIO UFFINSERIMENTO ON (COD_UFFICIO_INSERIMENTO = UFFINSERIMENTO.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFFINSERIMENTO ON (UFFINSERIMENTO.COD_TIPO_UFFICIO = DESCR_TIPO_UFFINSERIMENTO.RV_LOW_VALUE";
		lStatement += " AND DESCR_TIPO_UFFINSERIMENTO.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFFINSERIMENTO ON (UFFINSERIMENTO.COD_COMUNE = DESCR_COM_UFFINSERIMENTO.COD_COMUNE)";

		// lStatement += " WHERE ";
		return lStatement;
	}

	// MEV_57: aggiunto parametro di passaggio, aggiunto alias "F."
	protected String getSqlQueryPerEstesa(String majorOffice) {
		String lStatement = new String("");
		lStatement += " SELECT F.ID_FASCICOLO_SIGE, "
				+ "F.SOG_ID_SOGGETTO, F.CHIAVE_ANNO, F.CHIAVE_UFFICIO, F.CHIAVE_PROGR, SEZ_ID_SEZIONE, F.COD_STATO_FASCICOLO, "
				+ "COD_TIPO_GIUDIZIO, F.DATA_ISCRIZIONE, DATA_DEFINIZIONE, RIC_ID_RICHIESTA_SIGE, "
				+ "F.COD_OPERATORE_INSERIMENTO, F.COD_UFFICIO_INSERIMENTO, F.DATA_INSERIMENTO, "
				+ "F.COD_OPERATORE_AGGIORNAMENTO, F.COD_UFFICIO_AGGIORNAMENTO, F.DATA_AGGIORNAMENTO, F.NOTE, F.COD_POSIZIONE_GIURIDICA, F.DATA_FINE_PENA, "
				+ "F.COD_TIPO_DEFINIZIONE, F.DESCR_DEFINIZIONE, "
				+ "F.FAS_SIG_ID_FASCICOLO_SIGE, F.NUMERO_FASCICOLI_UNIFICATI, "
				+ "S.NOME, S.COGNOME, DATA_NASCITA, R.COD_TIPO_ATTO, TRS.RV_MEANING DESCR_TIPO_ATTO, "
				+ "U.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO, C.DESCRIZIONE DESCR_COMUNE_UFFICIO, E.COGNOME||' '||E.NOME NOM_MAG, "
				+ "STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO, NVL (SEZIONE.DESCRIZIONE,'-') DESCRIZIONE_SEZIONE, "
				+ "F.ID_FASCICOLO_SIGE_ORIGINE, " + "SEN_ID_SENTENZA_CUMULO, ID_EVENTO_PROVV_CUMULO ";
		lStatement += " FROM FASCICOLO_SIGE F, SOGGETTO S, RICHIESTA_SIGE R, CG_REF_CODES TRS, UFFICIO U, COMUNE C, MAGISTRATO_ASSEGNATARIO D, MAGISTRATO E, "
				+ "	CG_REF_CODES STATO_FASCICOLO, SEZIONE ";
		// MEV_57: aggiunta vista ed alias "S."
		if (StringUtils.checkValidValue(majorOffice))
			// 20181031: aggiunta nuova vista e modificata query
			// lStatement += ", V_SOGGETTO_MAGGIORENNE VSM";
			// lStatement += ", v_sogsige_eta vse";
			// 20190909 [SG]: aggiunta nuova vista e modificata query by BVN su segnalazione N.A. & M.T.
			lStatement += ", v_sogsige_eta_sige vse";

		lStatement += " WHERE F.SOG_ID_SOGGETTO = S.ID_SOGGETTO ";
		lStatement += " AND U.COD_UFFICIO = F.CHIAVE_UFFICIO ";
		lStatement += " AND U.COD_COMUNE = C.COD_COMUNE ";
		lStatement += " AND F.RIC_ID_RICHIESTA_SIGE = R.ID_RICHIESTA_SIGE ";
		lStatement += " AND TRS.RV_LOW_VALUE = R.COD_TIPO_ATTO ";
		lStatement += " AND TRS.RV_DOMAIN = 'TIPO_ATTO_SIGE' ";
		lStatement += " AND F.ID_FASCICOLO_SIGE = D.FAS_SIGE_ID_FASCICOLO_SIGE(+) ";
		lStatement += " AND D.DATA_FINE IS NULL ";
		lStatement += " AND D.MAG_COD_MAGISTRATO = E.COD_MAGISTRATO(+) ";
		lStatement += " AND (STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO' AND STATO_FASCICOLO.RV_LOW_VALUE = F.COD_STATO_FASCICOLO)";
		lStatement += " AND SEZIONE.ID_SEZIONE(+) =F.SEZ_ID_SEZIONE ";
		return lStatement;
	}

	protected String getSqlQueryPerEstremi(RicercaFascicoloSigeModel aRicercaFascModel) {

		String lStatement = new String("");

		// [SG] 20190312: aggiunta distinct
		lStatement += "SELECT distinct ID_FASCICOLO_SIGE, "
				+ "SOG_ID_SOGGETTO, CHIAVE_ANNO, CHIAVE_UFFICIO, CHIAVE_PROGR, SEZ_ID_SEZIONE, COD_STATO_FASCICOLO, "
				+ "COD_TIPO_GIUDIZIO, DATA_ISCRIZIONE, DATA_DEFINIZIONE, RIC_ID_RICHIESTA_SIGE, "
				+ "F.COD_OPERATORE_INSERIMENTO, F.COD_UFFICIO_INSERIMENTO, F.DATA_INSERIMENTO, "
				+ "F.COD_OPERATORE_AGGIORNAMENTO, F.COD_UFFICIO_AGGIORNAMENTO, F.DATA_AGGIORNAMENTO, F.NOTE, F.COD_POSIZIONE_GIURIDICA, F.DATA_FINE_PENA, "
				+ "F.COD_TIPO_DEFINIZIONE, F.DESCR_DEFINIZIONE, "
				+ "F.FAS_SIG_ID_FASCICOLO_SIGE, F.NUMERO_FASCICOLI_UNIFICATI, "
				+ "NOME, COGNOME, DATA_NASCITA, " + "R.COD_TIPO_ATTO, TRS.RV_MEANING DESCR_TIPO_ATTO, "
				+ "U.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO, C.DESCRIZIONE DESCR_COMUNE_UFFICIO, "
				+ "F.ID_FASCICOLO_SIGE_ORIGINE, " + "SEN_ID_SENTENZA_CUMULO, ID_EVENTO_PROVV_CUMULO ";
		lStatement += " FROM FASCICOLO_SIGE F, SOGGETTO S, RICHIESTA_SIGE R, CG_REF_CODES TRS, UFFICIO U, COMUNE C ";
		if (aRicercaFascModel.getCodMagistrato() != null && (aRicercaFascModel.getCodMagistrato().length() > 1
				|| aRicercaFascModel.getCodMagistrato() == "9"))
			lStatement += " ,MAGISTRATO_ASSEGNATARIO MA";
		if (aRicercaFascModel.getCodOggettoSige() != null
				&& aRicercaFascModel.getCodOggettoSige().length() > 1)
			lStatement += " , TENORE_SIGE TS ";
		lStatement += " WHERE SOG_ID_SOGGETTO = ID_SOGGETTO ";
		if (aRicercaFascModel.getCodMagistrato() != null && (aRicercaFascModel.getCodMagistrato().length() > 1
				|| aRicercaFascModel.getCodMagistrato() == "9")) {
			lStatement += " AND MA.FAS_SIGE_ID_FASCICOLO_SIGE = ID_FASCICOLO_SIGE ";
			lStatement += " AND MA.DATA_FINE IS NULL ";
		}
		if (aRicercaFascModel.getCodMagistrato() != null && aRicercaFascModel.getCodMagistrato() == "0")
			lStatement += " AND id_fascicolo_sige not in (select ma.fas_sige_id_fascicolo_sige from magistrato_assegnatario ma where ma.data_fine IS NULL )";
		if (aRicercaFascModel.getCodOggettoSige() != null
				&& aRicercaFascModel.getCodOggettoSige().length() > 1) {
			lStatement += " AND TS.FAS_ID_FASCICOLO_SIGE = ID_FASCICOLO_SIGE ";
			lStatement += " AND TS.DATA_FINE IS NULL ";
		}
		lStatement += " AND U.COD_UFFICIO = F.CHIAVE_UFFICIO ";
		lStatement += " AND U.COD_COMUNE = C.COD_COMUNE ";
		lStatement += " AND F.RIC_ID_RICHIESTA_SIGE = R.ID_RICHIESTA_SIGE ";
		lStatement += " AND TRS.RV_LOW_VALUE = R.COD_TIPO_ATTO ";
		lStatement += " AND TRS.RV_DOMAIN = 'TIPO_ATTO_SIGE' ";
		return lStatement;
	}

	protected String getSqlQueryPerEstremiStatistica(RicercaFascicoloSigeModel aRicercaFascModel) {
		String lStatement = new String("");
		// [SG] 20190312: aggiunta distinct ed aggiunte outer join su ts, d, e, b, r, TRS, TG, TSC
		lStatement = "SELECT distinct F.ID_FASCICOLO_SIGE, 'COD_STATO_FASCICOLO' COD_STATO_FASCICOLO, 'COD_TIPO_GIUDIZIO' COD_TIPO_GIUDIZIO,0 SOG_ID_SOGGETTO, F.CHIAVE_ANNO, F.CHIAVE_PROGR,  0 RIC_ID_RICHIESTA_SIGE, '' DESCR_TIPO_UFFICIO, '' DESCR_COMUNE_UFFICIO, CHIAVE_ANNO||'/'||CHIAVE_PROGR NUM_FASC,  '' COD_OPERATORE_INSERIMENTO, '' COD_UFFICIO_INSERIMENTO, NULL as DATA_INSERIMENTO, '' COD_OPERATORE_AGGIORNAMENTO, '' COD_UFFICIO_AGGIORNAMENTO, NULL as DATA_AGGIORNAMENTO, '' NOTE, '' COD_POSIZIONE_GIURIDICA, NULL as DATA_FINE_PENA, '' COD_TIPO_DEFINIZIONE, '' DESCR_DEFINIZIONE, 0 FAS_SIG_ID_FASCICOLO_SIGE, 0 NUMERO_FASCICOLI_UNIFICATI, NULL as DATA_NASCITA, 'NOME' NOME, 'COGNOME' COGNOME, ";
		lStatement += "R.COD_TIPO_ATTO, TRS.RV_MEANING DESCR_TIPO_ATTO, ";
		lStatement += "d.mag_cod_magistrato,E.COGNOME||' '||E.NOME NOM_MAG, ";
		lStatement += "CHIAVE_UFFICIO,SEZ_ID_SEZIONE, ";
		lStatement += "DATA_ISCRIZIONE, MIN(A.DATA_UDIENZA)DATA_UDIENZA_MIN, MAX ";
		lStatement += "(A.DATA_UDIENZA) DATA_UDIENZA_MAX, ";
		// 20170901; [SG] modificata query poichè data iscrizione ha anche ore, mi, sec ed il round non
		// funziona
		lStatement += "DATA_DEFINIZIONE, ROUND(to_date(DATA_DEFINIZIONE, 'dd/MM/yyyy') - to_date(DATA_ISCRIZIONE, 'dd/MM/yyyy')) ";
		lStatement += "DIFF_TOT, decode(sign(ROUND((to_date(DATA_DEFINIZIONE, 'dd/MM/yyyy') - to_date(DATA_ISCRIZIONE, 'dd/MM/yyyy'))) - 365), 1, ROUND((to_date(DATA_DEFINIZIONE, 'dd/MM/yyyy') - to_date(DATA_ISCRIZIONE, 'dd/MM/yyyy')) - 365), -1, null) diff_anno ";
		// lStatement+="DATA_DEFINIZIONE, ROUND((DATA_DEFINIZIONE - DATA_ISCRIZIONE)) ";
		// lStatement +=
		// "DIFF_TOT, decode(sign(ROUND((DATA_DEFINIZIONE - DATA_ISCRIZIONE)) - 365), 1,
		// ROUND((DATA_DEFINIZIONE - DATA_ISCRIZIONE) - 365), -1, null) diff_anno ";
		lStatement += ",F.ID_FASCICOLO_SIGE_ORIGINE, ID_EVENTO_PROVV_CUMULO";

		// @emma 17072018 post COLLAUDO 11.2 (aggiungo campo DATA_ARRIVO_CANCELLERIA , decodifca del tipo rito
		// in estrazione e DESCR_DEFINIZIONE )
		lStatement += ", R.DATA_ARRIVO_CANCELLERIA, TG.RV_MEANING TIPOLOGIA_RITO,";

		// @emma 13052019 INTEVENTO PER 11.2.1
		lStatement += "  CASE WHEN F.DATA_DEFINIZIONE IS NOT NULL"
				+ " THEN COALESCE(TIPO_D.RV_MEANING,STATO_DEF.RV_MEANING) END  MOTIVO_DEFINIZIONE, ";

		// @emma 13052019 INTEVENTO PER 11.2.1
		lStatement += " listagg(TSC.RV_MEANING , ';'||chr(10))  WITHIN GROUP (ORDER BY F.ID_FASCICOLO_SIGE)  as OGGETTI ";

		// @emma 13072018 post COLLAUDO 11.2 (aggiungo lo spazio prima di FROM )
		lStatement += " FROM FASCICOLO_SIGE F,  RICHIESTA_SIGE R, CG_REF_CODES TRS, UFFICIO U, ";
		lStatement += "COMUNE C, magistrato_assegnatario d, MAGISTRATO E, ";
		lStatement += "UDIENZA_SIGE A, UDIENZA_PROCEDIMENTO_SIGE B ";
		// @emma 13072018 post COLLAUDO 11.2
		lStatement += ",CG_REF_CODES TG ";

		// if (aRicercaFascModel.getCodOggettoSige() != null
		// && aRicercaFascModel.getCodOggettoSige().length() > 1)

		// @emma 17072018 post COLLAUDO 11.2 (deve andare sempre in lettura su TENORE_SIGE)
		lStatement += ",CG_REF_CODES TSC, TENORE_SIGE TS ";

		// @emma 13052019 INTEVENTO PER 11.2.1
		lStatement += ", CG_REF_CODES TIPO_D,  CG_REF_CODES STATO_DEF ";

		lStatement += "WHERE U.COD_UFFICIO = F.CHIAVE_UFFICIO   ";
		if (aRicercaFascModel.getCodMagistrato() != null && (aRicercaFascModel.getCodMagistrato().length() > 1
				|| aRicercaFascModel.getCodMagistrato() == "9")) {
			lStatement += " AND E.DATA_FINE_VALIDITA IS NULL ";
		}
		if (aRicercaFascModel.getCodMagistrato() != null && aRicercaFascModel.getCodMagistrato() == "0")
			lStatement += " AND id_fascicolo_sige not in (select ma.fas_sige_id_fascicolo_sige from magistrato_assegnatario ma where ma.data_fine IS NULL )";

		// if (aRicercaFascModel.getCodOggettoSige() != null
		// && aRicercaFascModel.getCodOggettoSige().length() > 1) {
		// @emma 17072018 post COLLAUDO 11.2 (deve andare sempre in lettura su TENORE_SIGE)
		lStatement += " AND TS.FAS_ID_FASCICOLO_SIGE(+) = ID_FASCICOLO_SIGE";
		lStatement += " AND TS.DATA_FINE(+) IS NULL ";
		// }

		lStatement += "AND U.COD_COMUNE = C.COD_COMUNE   ";
		lStatement += "AND f.id_fascicolo_sige = d.fas_sige_id_fascicolo_sige(+) ";
		lStatement += "AND D.DATA_FINE(+) IS NULL ";
		lStatement += "AND D.MAG_COD_MAGISTRATO = E.COD_MAGISTRATO(+) ";
		lStatement += "AND E.COD_UFFICIO_INSERIMENTO(+) = F.COD_UFFICIO_INSERIMENTO ";
		lStatement += "AND A.ID_UDIENZA_SIGE(+) = B.UDI_ID_UDIENZA_SIGE ";
		lStatement += "AND B.FAS_ID_FASCICOLO_SIGE (+) = F.ID_FASCICOLO_SIGE ";
		lStatement += "AND F.RIC_ID_RICHIESTA_SIGE = R.ID_RICHIESTA_SIGE  ";
		lStatement += "AND TRS.RV_LOW_VALUE = R.COD_TIPO_ATTO(+) ";
		lStatement += "AND TRS.RV_DOMAIN(+) = 'TIPO_ATTO_SIGE' ";
		// lStatement += "AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') <= 'null' ";

		// @emma 13072018 post COLLAUDO 11.2
		lStatement += "AND TG.RV_LOW_VALUE(+) = F.COD_TIPO_GIUDIZIO AND TG.RV_DOMAIN(+) = 'TIPO_GIUDIZIO_SIGE' ";
		// @emma 17072018 post COLLAUDO 11.2 (deve andare sempre in lettura su TENORE_SIGE)
		lStatement += "AND TSC.RV_LOW_VALUE(+) = TS.COD_OGGETTO_SIGE AND TSC.RV_DOMAIN(+) = 'OGGETTO_SIGE' ";

		// @emma 13052019 INTEVENTO PER 11.2.1
		lStatement += " AND TIPO_D.RV_LOW_VALUE(+) = F.COD_TIPO_DEFINIZIONE AND TIPO_D.RV_DOMAIN(+) = 'TIPO_DEFINIZIONE' "
				+ " AND STATO_DEF.RV_LOW_VALUE(+) = F.COD_STATO_FASCICOLO  AND STATO_DEF.RV_DOMAIN(+) = 'STATO_FASCICOLO' ";

		return lStatement;
	}

	protected String getSqlQueryPerEstremiAttoStatistica(RicercaFascicoloSigeModel aRicercaFascModel) {
		String lStatement = new String("");
		lStatement = "SELECT ";
		// aggiunto bvn 28/03/2017
		// Nella colonna "Tipologia Incidente d'Esecuzione" viene riportato l'oggetto del procedimento.
		// In presenza di più oggetti viene riportata la descrizione "Oggetto Multiplo"
		lStatement += "CASE ";
		lStatement += "WHEN NVL((SELECT COUNT(DISTINCT TT.COD_OGGETTO_SIGE) FROM tenore_sige tt WHERE  tt.fas_id_fascicolo_sige=f.id_fascicolo_sige),0)>1 ";
		lStatement += "  THEN 'Oggetto Multiplo' ";
		lStatement += "  ELSE CC.RV_MEANING ";
		lStatement += "END Oggetti, ";
		// fine aggiunto bvn 28/03/2017
		lStatement += "F.ID_FASCICOLO_SIGE,  'COD_STATO_FASCICOLO' COD_STATO_FASCICOLO, ";
		lStatement += "'COD_TIPO_GIUDIZIO' COD_TIPO_GIUDIZIO,0 SOG_ID_SOGGETTO, F.CHIAVE_ANNO, ";
		lStatement += "F.CHIAVE_PROGR,  0 RIC_ID_RICHIESTA_SIGE, '' DESCR_TIPO_UFFICIO, '' DESCR_COMUNE_UFFICIO, ";
		lStatement += "CHIAVE_ANNO||'/'||CHIAVE_PROGR NUM_FASC,  '' COD_OPERATORE_INSERIMENTO, '' COD_UFFICIO_INSERIMENTO, ";
		lStatement += "NULL as DATA_INSERIMENTO, '' COD_OPERATORE_AGGIORNAMENTO, '' COD_UFFICIO_AGGIORNAMENTO, NULL as DATA_AGGIORNAMENTO, ";
		lStatement += "'' NOTE, '' COD_POSIZIONE_GIURIDICA, NULL as DATA_FINE_PENA, '' COD_TIPO_DEFINIZIONE, '' DESCR_DEFINIZIONE, ";
		lStatement += "0 FAS_SIG_ID_FASCICOLO_SIGE, 0 NUMERO_FASCICOLI_UNIFICATI, NULL as DATA_NASCITA, 'NOME' NOME, 'COGNOME' COGNOME, ";
		lStatement += "R.COD_TIPO_ATTO, TRS.RV_MEANING DESCR_TIPO_ATTO, ";
		lStatement += "d.mag_cod_magistrato,E.COGNOME||' '||E.NOME NOM_MAG, ";
		lStatement += "CHIAVE_UFFICIO,SEZ_ID_SEZIONE, ";
		lStatement += "R.DATA_ARRIVO_CANCELLERIA , TRS1.RV_MEANING AS TIPOLOGIA_RITO, ";

		// Modifica del 01/12/2016 MEV_15_S4
		// Modifica necessaria per valorizzare la colonna "Motivo Altra Definizione Opposizione/Ricorso"
		// in presenza di Impugnazioni (Opposizioni/Ricorsi) nel foglio excel delle statistiche
		// richiamate dalla funzionalità "Ricerca Procedimento SIGE per Estremi Atto"

		// lStatement+="TRS2.RV_MEANING AS MOTIVO_DEFINIZIONE, ";

		// inizio aggiunta nuovo codice
		lStatement += "DECODE(TRS2.RV_MEANING,NULL,(SELECT listagg(tipo_imp, '/') WITHIN GROUP (ORDER BY tipo_imp) impugnazione FROM ( ";
		lStatement += "SELECT CASE WHEN lag(im.rv_meaning) OVER (ORDER BY im.rv_meaning) = im.rv_meaning THEN NULL ELSE im.rv_meaning END as tipo_imp ";
		lStatement += " FROM IMPUGNAZIONE_SIGE S, PROVVEDIMENTO_SIGE PS, cg_ref_codes im ";
		lStatement += "WHERE S.PROVV_ID_PROVV_GENERATO = PS.ID_PROVVEDIMENTO_SIGE ";
		lStatement += "AND PS.FAS_ID_FASCICOLO_SIGE = f.id_fascicolo_sige ";
		lStatement += "AND s.cod_tipo_impugnazione=im.rv_low_value ";
		lStatement += "AND im.rv_domain = 'TIPO_RICORSO_SIGE')),TRS2.RV_MEANING) MOTIVO_DEFINIZIONE, ";
		// fine aggiunta nuovo codice
		lStatement += "DATA_ISCRIZIONE, MIN(A.DATA_UDIENZA)DATA_UDIENZA_MIN, MAX ";
		lStatement += "(A.DATA_UDIENZA) DATA_UDIENZA_MAX, ";
		lStatement += "DATA_DEFINIZIONE, ROUND((DATA_DEFINIZIONE - DATA_ISCRIZIONE)) ";
		lStatement += "DIFF_TOT, decode(sign(ROUND((DATA_DEFINIZIONE - DATA_ISCRIZIONE)) -  365), 1,ROUND((DATA_DEFINIZIONE - DATA_ISCRIZIONE)-365), -1, null) diff_anno ";
		lStatement += ",F.ID_FASCICOLO_SIGE_ORIGINE, ID_EVENTO_PROVV_CUMULO ";
		lStatement += "FROM FASCICOLO_SIGE F,  RICHIESTA_SIGE R, CG_REF_CODES TRS, UFFICIO U, ";
		lStatement += "COMUNE C, magistrato_assegnatario d, MAGISTRATO E, ";
		lStatement += "UDIENZA_SIGE A, UDIENZA_PROCEDIMENTO_SIGE B, ";
		lStatement += "CG_REF_CODES TRS1, ";
		lStatement += "CG_REF_CODES TRS2, ";

		// aggiunto bvn 28/03/2017
		lStatement += "TENORE_SIGE T, ";
		lStatement += "TENORE_SENTENZA_REATO TSR, ";
		lStatement += "CG_REF_CODES CC ";
		// fine aggiunto bvn 28/03/2017

		if (aRicercaFascModel.getCodOggettoSige() != null
				&& aRicercaFascModel.getCodOggettoSige().length() > 1)
			lStatement += " , TENORE_SIGE TS ";

		lStatement += "WHERE U.COD_UFFICIO = F.CHIAVE_UFFICIO   ";
		if (aRicercaFascModel.getCodMagistrato() != null && (aRicercaFascModel.getCodMagistrato().length() > 1
				|| aRicercaFascModel.getCodMagistrato() == "9")) {
			lStatement += " AND E.DATA_FINE_VALIDITA IS NULL ";
		}
		if (aRicercaFascModel.getCodMagistrato() != null && aRicercaFascModel.getCodMagistrato() == "0")
			lStatement += " AND id_fascicolo_sige not in (select ma.fas_sige_id_fascicolo_sige from magistrato_assegnatario ma where ma.data_fine IS NULL )";
		if (aRicercaFascModel.getCodOggettoSige() != null
				&& aRicercaFascModel.getCodOggettoSige().length() > 1) {
			lStatement += " AND TS.FAS_ID_FASCICOLO_SIGE = ID_FASCICOLO_SIGE ";
			lStatement += " AND TS.DATA_FINE IS NULL ";
		}

		lStatement += "AND U.COD_COMUNE = C.COD_COMUNE   ";
		lStatement += "AND f.id_fascicolo_sige = d.fas_sige_id_fascicolo_sige (+)";
		lStatement += "AND D.DATA_FINE IS NULL  ";
		lStatement += "AND D.MAG_COD_MAGISTRATO = E.COD_MAGISTRATO (+)";
		lStatement += "AND A.ID_UDIENZA_SIGE(+) = B.UDI_ID_UDIENZA_SIGE ";
		lStatement += "AND B.FAS_ID_FASCICOLO_SIGE (+) = F.ID_FASCICOLO_SIGE ";
		// lStatement+="AND E.COD_UFFICIO_INSERIMENTO = F.COD_UFFICIO_INSERIMENTO ";
		lStatement += "AND F.RIC_ID_RICHIESTA_SIGE = R.ID_RICHIESTA_SIGE  ";
		lStatement += "AND TRS.RV_LOW_VALUE = R.COD_TIPO_ATTO  ";
		lStatement += "AND TRS.RV_DOMAIN = 'TIPO_ATTO_SIGE' ";
		lStatement += "AND TRS1.RV_LOW_VALUE(+) = f.cod_tipo_giudizio ";
		lStatement += "AND TRS1.RV_DOMAIN(+) = 'TIPO_GIUDIZIO_SIGE' ";
		lStatement += "AND TRS2.RV_LOW_VALUE(+) = f.cod_tipo_definizione ";
		lStatement += "AND TRS2.RV_DOMAIN(+) = 'TIPO_DEFINIZIONE' ";
		lStatement += "AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') <= 'null' ";

		// aggiunto bvn 28/03/2017
		lStatement += "AND t.fas_id_fascicolo_sige=f.id_fascicolo_sige ";
		lStatement += "AND t.RIC_SIG_ID_RICHIESTA_SIGE=F.RIC_ID_RICHIESTA_SIGE ";
		lStatement += "AND T.ID_TENORE_SIGE = TSR.TEN_ID_TENORE_SIGE ";
		lStatement += "AND CC.RV_DOMAIN = 'OGGETTO_SIGE' ";
		lStatement += "AND CC.RV_LOW_VALUE = T.COD_OGGETTO_SIGE ";
		// fine aggiunto bvn 28/03/2017

		// lStatement+="ORDER BY CHIAVE_ANNO ASC ,CHIAVE_PROGR ASC ";
		// lStatement+="CHIAVE_UFFICIO = 00127202202 AND ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		FascicoloSigeModel aModel = new FascicoloSigeModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdFascicoloSige(getBigDecimal("ID_FASCICOLO_SIGE"));
		aModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		aModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		aModel.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		aModel.setIdSezione(getBigDecimal("SEZ_ID_SEZIONE"));
		aModel.setDescrSezione("");
		aModel.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		aModel.setDescrStatoFascicolo("");
		aModel.setCodTipoGiudizio(getString("COD_TIPO_GIUDIZIO"));
		aModel.setDescrTipoGiudizio("");
		aModel.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		aModel.setDataDefinizione(getDate("DATA_DEFINIZIONE"));
		aModel.setRicIdRichiestaSige(getBigDecimal("RIC_ID_RICHIESTA_SIGE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setDescrUfficioInserimento("");
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setDescrUfficioAggiornamento("");
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodPosizioneGiuridica(getString("COD_POSIZIONE_GIURIDICA"));
		aModel.setDescrPosizioneGiuridica(getString("POSIZIONE_GIURIDICA"));
		aModel.setDataFinePena(getDate("DATA_FINE_PENA"));
		aModel.setCodTipoDefinizione(getString("COD_TIPO_DEFINIZIONE"));
		aModel.setDescrDefinizione(getString("DESCR_DEFINIZIONE"));
		aModel.setFasSigIdFascicoloSige(getBigDecimal("FAS_SIG_ID_FASCICOLO_SIGE")); // 26/07/2010
		aModel.setNumeroFascicoliUnificati(getBigDecimal("NUMERO_FASCICOLI_UNIFICATI")); // 26/07/2010

		// Modifica Accorpamento Uffici
		aModel.setChiaveProgrOrig(getBigDecimal("CHIAVE_PROGR_ORIG"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodTipoUfficioInserimento(getString("COD_TIPO_UFFICIO_INS"));
		aModel.setDescrTipoUfficioInserimento(getString("DESCR_TIPO_UFFICIO_INS"));
		aModel.setDescrComuneUfficioInserimento(getString("DESCR_COMUNE_UFFICIO_INS"));
		aModel.setFlagUfficioAccorpato(getString("FLAG_UFFICIO_ACCORPATO"));

		aModel.setIdFascicoloSigeOrigine(getBigDecimal("ID_FASCICOLO_SIGE_ORIGINE"));

		aModel.setIdEventoProvvCumulo(getBigDecimal("ID_EVENTO_PROVV_CUMULO"));

		if (findColumn("NOM_MAG"))
			aModel.setNomeCognomeMagistrato(getString("NOM_MAG"));

		if (findColumn("DATA_UDIENZA_MIN"))
			aModel.setDataPrimaUdienza(getDate("DATA_UDIENZA_MIN"));

		if (findColumn("DATA_UDIENZA_MAX"))
			aModel.setDataUltimaUdienza(getDate("DATA_UDIENZA_MAX"));

		if (findColumn("DIFF_TOT"))
			aModel.setNumGiorniIntercorsiTraIscrizioneEDeposito(getBigDecimal("DIFF_TOT"));

		if (findColumn("DIFF_ANNO"))
			aModel.setNumGiorniIntercorsiTraIscrizioneEDepositoAnno(getBigDecimal("DIFF_ANNO"));

		if (findColumn("DESCR_STATO_FASCICOLO"))
			aModel.setDescrStatoFascicolo(getString("DESCR_STATO_FASCICOLO"));

		if (findColumn("DESCRIZIONE_SEZIONE"))
			aModel.setDescrSezione(getString("DESCRIZIONE_SEZIONE"));

		if (findColumn("SEN_ID_SENTENZA_CUMULO"))
			aModel.setSenIdSentenzaCumulo(getBigDecimal("SEN_ID_SENTENZA_CUMULO"));

		return aModel;
	}

	public GenericModel getModelEsteso() throws DAOException {
		FascicoloSigeEstesoModel fasEstesoModel = new FascicoloSigeEstesoModel();
		FascicoloSigeModel fasModel = new FascicoloSigeModel();
		SoggettoModel sogModel = new SoggettoModel();
		RichiestaSigeModel ricSigeModel = new RichiestaSigeModel();

		// Inserire le opportune set delle descrizioni!
		fasModel.setIdFascicoloSige(getBigDecimal("ID_FASCICOLO_SIGE"));
		fasModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		fasModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		fasModel.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		fasModel.setDescrUfficio(
				getString("DESCR_TIPO_UFFICIO") + " di " + getString("DESCR_COMUNE_UFFICIO"));
		fasModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		fasModel.setIdSezione(getBigDecimal("SEZ_ID_SEZIONE"));
		fasModel.setDescrSezione("");
		fasModel.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		fasModel.setDescrStatoFascicolo("");
		fasModel.setCodTipoGiudizio(getString("COD_TIPO_GIUDIZIO"));
		fasModel.setDescrTipoGiudizio("");
		fasModel.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		fasModel.setDataDefinizione(getDate("DATA_DEFINIZIONE"));
		fasModel.setRicIdRichiestaSige(getBigDecimal("RIC_ID_RICHIESTA_SIGE"));
		fasModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		fasModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		fasModel.setDescrUfficioInserimento("");
		fasModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		fasModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		fasModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		fasModel.setDescrUfficioAggiornamento("");
		fasModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		fasModel.setNote(getString("NOTE"));
		fasModel.setCodPosizioneGiuridica(getString("COD_POSIZIONE_GIURIDICA"));
		fasModel.setDataFinePena(getDate("DATA_FINE_PENA"));
		fasModel.setCodTipoDefinizione(getString("COD_TIPO_DEFINIZIONE"));
		fasModel.setDescrDefinizione(getString("DESCR_DEFINIZIONE"));
		fasModel.setFasSigIdFascicoloSige(getBigDecimal("FAS_SIG_ID_FASCICOLO_SIGE")); // 26/07/2010
		fasModel.setNumeroFascicoliUnificati(getBigDecimal("NUMERO_FASCICOLI_UNIFICATI")); // 26/07/2010

		fasModel.setIdFascicoloSigeOrigine(getBigDecimal("ID_FASCICOLO_SIGE_ORIGINE")); // 26/07/2010

		fasModel.setIdEventoProvvCumulo(getBigDecimal("ID_EVENTO_PROVV_CUMULO"));

		if (findColumn("SEN_ID_SENTENZA_CUMULO"))
			fasModel.setSenIdSentenzaCumulo(getBigDecimal("SEN_ID_SENTENZA_CUMULO"));

		if (findColumn("NOM_MAG"))
			fasModel.setNomeCognomeMagistrato(getString("NOM_MAG"));

		if (findColumn("DATA_UDIENZA_MIN"))
			fasModel.setDataPrimaUdienza(getDate("DATA_UDIENZA_MIN"));

		if (findColumn("DATA_UDIENZA_MAX"))
			fasModel.setDataUltimaUdienza(getDate("DATA_UDIENZA_MAX"));

		if (findColumn("DIFF_TOT"))
			fasModel.setNumGiorniIntercorsiTraIscrizioneEDeposito(getBigDecimal("DIFF_TOT"));

		if (findColumn("DIFF_ANNO"))
			fasModel.setNumGiorniIntercorsiTraIscrizioneEDepositoAnno(getBigDecimal("DIFF_ANNO"));

		if (findColumn("NOM_MAG"))
			fasModel.setNomeCognomeMagistrato(getString("NOM_MAG"));

		if (findColumn("DATA_UDIENZA_MIN"))
			fasModel.setDataPrimaUdienza(getDate("DATA_UDIENZA_MIN"));

		if (findColumn("DATA_UDIENZA_MAX"))
			fasModel.setDataUltimaUdienza(getDate("DATA_UDIENZA_MAX"));

		if (findColumn("DIFF_TOT"))
			fasModel.setNumGiorniIntercorsiTraIscrizioneEDeposito(getBigDecimal("DIFF_TOT"));

		if (findColumn("DIFF_ANNO"))
			fasModel.setNumGiorniIntercorsiTraIscrizioneEDepositoAnno(getBigDecimal("DIFF_ANNO"));

		if (findColumn("DESCR_STATO_FASCICOLO"))
			fasModel.setDescrStatoFascicolo(getString("DESCR_STATO_FASCICOLO"));

		if (findColumn("DESCRIZIONE_SEZIONE"))
			fasModel.setDescrSezione(getString("DESCRIZIONE_SEZIONE"));

		if (findColumn("MOTIVO_DEFINIZIONE"))
			fasModel.setMotivoAltraDefinizione(getString("MOTIVO_DEFINIZIONE"));

		if (findColumn("TIPOLOGIA_RITO"))
			fasModel.setTipologiaRito(getString("TIPOLOGIA_RITO"));

		if (findColumn("OGGETTI"))
			fasModel.setDescOggetto(getString("OGGETTI"));

		sogModel.setIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		sogModel.setDataNascita(getDate("DATA_NASCITA"));
		sogModel.setNome(getString("NOME"));
		sogModel.setCognome(getString("COGNOME"));

		ricSigeModel.setIdRichiestaSige(getBigDecimal("RIC_ID_RICHIESTA_SIGE"));
		ricSigeModel.setCodTipoAtto(getString("COD_TIPO_ATTO"));
		ricSigeModel.setDescrTipoRichiesta(getString("DESCR_TIPO_ATTO"));

		if (findColumn("DATA_ARRIVO_CANCELLERIA"))
			ricSigeModel.setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA"));

		fasEstesoModel.setFascicoloSige(fasModel);
		fasEstesoModel.setSoggetto(sogModel);
		fasEstesoModel.setRichiestaSige(ricSigeModel);

		return fasEstesoModel;
	}

	/**
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */

	public GenericModel getModelEstesoRicSoggettiConProc() throws DAOException {
		FascicoloSigeEstesoModel fasEstesoModel = new FascicoloSigeEstesoModel();
		FascicoloSigeModel fasModel = new FascicoloSigeModel();
		SoggettoModel sogModel = new SoggettoModel();

		fasModel.setNote(getBigDecimal("NUM_FASCICOLI").toString());

		sogModel.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		sogModel.setCognome(getString("COGNOME"));
		sogModel.setNome(getString("NOME"));
		sogModel.setDataNascita(getDate("DATA_NASCITA"));
		sogModel.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		sogModel.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		sogModel.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));

		// paolo cherubini x supersoggetto 30 luglio 2009
		sogModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		sogModel.setCodFiscale(getString("COD_FISCALE"));
		sogModel.setCodCs(getString("COD_CS"));
		sogModel.setCodAfis(getString("COD_AFIS"));
		sogModel.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		sogModel.setAttoNascita(getString("ATTO_NASCITA"));
		sogModel.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA"));
		sogModel.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		sogModel.setNazionalita(getString("NAZIONALITA"));
		sogModel.setPaternita(getString("PATERNITA"));
		sogModel.setCognomeMadre(getString("COGNOME_MADRE"));
		sogModel.setNomeMadre(getString("NOME_MADRE"));
		sogModel.setSesso(getString("SESSO"));
		sogModel.setMeseNascita(getBigDecimal("MESE_NASCITA"));
		// fine paolo

		if (findColumn("ETA_PRESUNTA_ANNI"))
			sogModel.setEtaPresuntaAnni(getBigDecimal("ETA_PRESUNTA_ANNI"));

		if (findColumn("ETA_PRESUNTA_MESI"))
			sogModel.setEtaPresuntaMesi(getBigDecimal("ETA_PRESUNTA_MESI"));

		fasEstesoModel.setFascicoloSige(fasModel);
		fasEstesoModel.setSoggetto(sogModel);
		return fasEstesoModel;
	}

	public String setCondizione(FascicoloSigeModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " ID_FASCICOLO_SIGE = " + aKey;
	}

	public String setCondizioneUnificati(FascicoloSigeModel aModel) {
		String lCondizioni = new String();

		if (aModel.getFasSigIdFascicoloSige() != null) {
			lCondizioni += " FASCICOLO_SIGE.FAS_SIG_ID_FASCICOLO_SIGE =" + aModel.getFasSigIdFascicoloSige();
		}

		return lCondizioni;
	}

	/**
	 * Calcola il Massimo Progressivo relativo ad un dato ufficio ed anno. Il massimo progressivo rappresenta
	 * il progressivo più alto tra i Fascicoli SIGE inseriti in quell'anno e di competenza di quell'ufficio.
	 * <p>
	 * A seguito dell'accorpamento di più uffici è stata aggiunta la condizione sulla numerazione compresa tra
	 * 1 e 99.999 in quanto i fascicoli importati da altri uffici verranno migrati con numerazioni fittizie
	 * >100.000
	 *
	 * @return BigDecimal
	 * @param FascicoloSiepeModel
	 *            model del fascicolo SIEPE.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public BigDecimal getProgressivoFascicoloSige(FascicoloSigeModel aModel) throws DAOException {
		BigDecimal lProgr = new BigDecimal(0);

		String lStatement = "";

		lStatement += " SELECT MAX(CHIAVE_PROGR) aMAX";
		lStatement += " FROM FASCICOLO_SIGE FS";
		lStatement += " WHERE FS.CHIAVE_ANNO = " + aModel.getChiaveAnno();
		lStatement += " AND FS.CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "'";
		lStatement += " AND FS.CHIAVE_PROGR Between 1 AND 99999 ";

		setStatement(lStatement);
		start();

		if (next() && (getBigDecimal("aMAX") != null))
			lProgr = getBigDecimal("aMAX");
		stop();
		if (lProgr == null)
			lProgr = new BigDecimal(0);
		return lProgr;
	}

	// MEV_57: aggiunto parametro di passaggio
	public void ricercaFascicoloSigeEstesa(RicercaFascicoloSigeModel aRicercaFascModel, String majorOffice) {
		String QueryFascicoloSige = "";

		// Costruita la query relativa soltanto al model fascicolo SIGE
		// MEV_57: aggiunto parametro di passaggio
		QueryFascicoloSige += getSqlQueryPerEstesa(majorOffice);
		// Impostazione delle condizioni per Estremi
		QueryFascicoloSige += setCondizionePerEstesa(aRicercaFascModel);

		// MEV_57: aggiunta condizione
		if (StringUtils.checkValidValue(majorOffice)) {
			// QueryFascicoloSige += " and S.ID_SOGGETTO = VSM.ID_SOGGETTO";
			// 20181031: aggiunta nuova vista e modificata query
			QueryFascicoloSige += " and (nvl(vse.eta_soggetto_ora, 18) >= 18 or s.cod_ufficio_inserimento = '"
					+ majorOffice + "') ";
			QueryFascicoloSige += " and F.ID_FASCICOLO_SIGE = vse.id_fascicolo_sige";
		}

		QueryFascicoloSige += setGroupByPerEstesa();
		QueryFascicoloSige += setOrderAnnoProgrAsc();
		// Settaggio della stringa SQL appena costruita prima della query
		setStatement(QueryFascicoloSige);
	}

	public void ricercaFascicoloSigePerEstremi(RicercaFascicoloSigeModel aRicercaFascModel) {
		String QueryFascicoloSige = "";

		// Costruita la query relativa soltanto al model fascicolo SIGE
		QueryFascicoloSige += getSqlQueryPerEstremi(aRicercaFascModel);
		// Impostezione delle condizioni per Estremi
		QueryFascicoloSige += setCondizionePerEstremi(aRicercaFascModel);

		QueryFascicoloSige += setOrderAnnoProgrAsc();
		// Settaggio della stringa SQL appena costruita prima della query
		setStatement(QueryFascicoloSige);
	}

	public void ricercaStatisticaFascicoloSigePerEstremi(RicercaFascicoloSigeModel aRicercaFascModel) {
		String queryFascicoloSige = "";

		// Costruita la query relativa soltanto al model fascicolo SIGE
		queryFascicoloSige = getSqlQueryPerEstremiStatistica(aRicercaFascModel);
		queryFascicoloSige += setCondizionePerEstremiStatistica(aRicercaFascModel);
		// Impostezione delle condizioni per Estremi

		// 20171006: [SG] aggiunta condizione
		queryFascicoloSige += " and b.flag_rinviata(+) <> 'A'  AND d.data_fine IS NULL ";
		queryFascicoloSige += " GROUP BY F.ID_FASCICOLO_SIGE,  ";
		queryFascicoloSige += "CHIAVE_ANNO||'/'||CHIAVE_PROGR,R.COD_TIPO_ATTO, TRS.RV_MEANING, ";
		queryFascicoloSige += "d.mag_cod_magistrato,E.COGNOME||' '||E.NOME,  ";
		queryFascicoloSige += "CHIAVE_UFFICIO,SEZ_ID_SEZIONE, ";
		// @emma 13072018 intervento post COLLAUDO 11.2 (AGGIUNGO CAMPI NELLA GROUP BY)
		queryFascicoloSige += "DATA_ISCRIZIONE, F.CHIAVE_ANNO, F.CHIAVE_PROGR, DATA_DEFINIZIONE,"
				+ " F.ID_FASCICOLO_SIGE_ORIGINE, ID_EVENTO_PROVV_CUMULO, DATA_ARRIVO_CANCELLERIA , "
				+ " TG.RV_MEANING, TIPO_D.RV_MEANING, f.cod_stato_fascicolo,  STATO_DEF.RV_MEANING  ";

		queryFascicoloSige += setOrderAnnoProgrAsc();
		// Settaggio della stringa SQL appena costruita prima della query
		setStatement(queryFascicoloSige);
	}

	public void ricercaStatisticaFascicoloSigePerEstremiAttoStatisica(
			RicercaFascicoloSigeModel aRicercaFascModel) {
		String queryFascicoloSige = "";
		// Costruita la query relativa soltanto al model fascicolo SIGE
		queryFascicoloSige = getSqlQueryPerEstremiAttoStatistica(aRicercaFascModel);
		queryFascicoloSige += setCondizionePerEstremiStatistica(aRicercaFascModel);
		// Impostezione delle condizioni per Estremi

		queryFascicoloSige += "GROUP BY F.ID_FASCICOLO_SIGE,  ";
		queryFascicoloSige += "CHIAVE_ANNO||'/'||CHIAVE_PROGR,R.COD_TIPO_ATTO, TRS.RV_MEANING, ";
		queryFascicoloSige += "d.mag_cod_magistrato,E.COGNOME||' '||E.NOME,  ";
		queryFascicoloSige += "CHIAVE_UFFICIO,SEZ_ID_SEZIONE, ";
		queryFascicoloSige += "DATA_ISCRIZIONE,F.CHIAVE_ANNO, F.CHIAVE_PROGR, DATA_DEFINIZIONE, ";
		queryFascicoloSige += "DATA_ARRIVO_CANCELLERIA,  TRS1.RV_MEANING, TRS2.RV_MEANING, ";
		queryFascicoloSige += "F.ID_FASCICOLO_SIGE_ORIGINE, ID_EVENTO_PROVV_CUMULO, ";
		// aggiunto bvn 28/03/2017
		queryFascicoloSige += "CC.RV_MEANING, ";
		queryFascicoloSige += "F.RIC_ID_RICHIESTA_SIGE ";
		// fine aggiunto bvn 28/03/2017

		queryFascicoloSige += setOrderAnnoProgrAsc();
		// Settaggio della stringa SQL appena costruita prima della query
		setStatement(queryFascicoloSige);
	}

	/**
	 * Settaggio della condizione per Ricerca Estesa
	 *
	 * @param aSm
	 *            ;
	 * @param sTipoAtto
	 *            ;
	 * @return lCondizioni
	 */
	private String setCondizionePerEstesa(RicercaFascicoloSigeModel aRFSM) {
		String lCondizioni = new String();

		// Cerca i fascicoli per l'ufficio selezionato.
		if ((aRFSM.getChiaveUfficio().trim().length() > 0)) {
			if (aRFSM.getChiaveUfficioInserimento() != null
					&& !aRFSM.getChiaveUfficioInserimento().equals("")) {
				lCondizioni += " AND F.COD_UFFICIO_INSERIMENTO = " + aRFSM.getChiaveUfficioInserimento();
			} else {
				lCondizioni += " AND CHIAVE_UFFICIO = " + aRFSM.getChiaveUfficio();
			}
		}

		// Cerca i fascicoli a partire da una data iscrizione.
		if ((aRFSM.getDataIscrizioneIniziale() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aRFSM.getDataIscrizioneIniziale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli fino ad una data iscrizione.
		if ((aRFSM.getDataIscrizioneFinale() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aRFSM.getDataIscrizioneFinale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli a partire da un anno iniziale.
		if ((aRFSM.getChiaveAnnoIniziale() != null))
			lCondizioni += " AND F.CHIAVE_ANNO >= " + aRFSM.getChiaveAnnoIniziale();

		// Cerca i fascicoli a partire da un progressivo iniziale.
		if ((aRFSM.getChiaveProgrIniziale() != null))
			lCondizioni += " AND F.CHIAVE_PROGR >= " + aRFSM.getChiaveProgrIniziale();

		// Cerca i fascicoli fino a un anno finale.
		if ((aRFSM.getChiaveAnnoFinale() != null))
			lCondizioni += " AND F.CHIAVE_ANNO <= " + aRFSM.getChiaveAnnoFinale();

		// Cerca i fascicoli fino a un progressivo finale.
		if ((aRFSM.getChiaveProgrFinale() != null))
			lCondizioni += " AND F.CHIAVE_PROGR <= " + aRFSM.getChiaveProgrFinale();

		if (aRFSM.getIdSezione() != null && aRFSM.getIdSezione().intValue() == 1) {
			lCondizioni += " AND SEZ_ID_SEZIONE is NULL ";
		}

		if (aRFSM.getIdSezione() != null && aRFSM.getIdSezione().intValue() > 1) {
			lCondizioni += " AND SEZ_ID_SEZIONE=" + aRFSM.getIdSezione();
		}

		return lCondizioni;
	}

	/**
	 * Settaggio della condizione su Estremi Atto
	 *
	 * @param aSm
	 *            ;
	 * @param sTipoAtto
	 *            ;
	 * @return lCondizioni
	 */
	private String setCondizionePerEstremi(RicercaFascicoloSigeModel aRFSM) {
		String lCondizioni = new String();

		// Cerca i fascicoli per l'ufficio selezionato.
		if ((aRFSM.getChiaveUfficio().trim().length() > 0))
			lCondizioni += " AND CHIAVE_UFFICIO = " + aRFSM.getChiaveUfficio();

		// Cerca i fascicoli a partire da una data iscrizione.
		if ((aRFSM.getDataIscrizioneIniziale() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aRFSM.getDataIscrizioneIniziale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli fino ad una data iscrizione.
		if ((aRFSM.getDataIscrizioneFinale() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aRFSM.getDataIscrizioneFinale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli a partire da un anno iniziale.
		if ((aRFSM.getChiaveAnnoIniziale() != null))
			lCondizioni += " AND CHIAVE_ANNO >= " + aRFSM.getChiaveAnnoIniziale();

		// Cerca i fascicoli a partire da un progressivo iniziale.
		if ((aRFSM.getChiaveProgrIniziale() != null))
			lCondizioni += " AND CHIAVE_PROGR >= " + aRFSM.getChiaveProgrIniziale();

		// Cerca i fascicoli fino a un anno finale.
		if ((aRFSM.getChiaveAnnoFinale() != null))
			lCondizioni += " AND CHIAVE_ANNO <= " + aRFSM.getChiaveAnnoFinale();

		// Cerca i fascicoli fino a un progressivo finale.
		if ((aRFSM.getChiaveProgrFinale() != null))
			lCondizioni += " AND CHIAVE_PROGR <= " + aRFSM.getChiaveProgrFinale();

		// 03/07/2009 Ricerca Procedimenti SIGE per Estremi Atto.
		/*
		 * if ((!isRequestParameterNullObj(ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO)) &&
		 * (!getRequestStringParameter(ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO).equals("-") ) )
		 * lRicercaFascicolo
		 * .setCodTipoAtto(getRequestStringParameter(ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO) ); if
		 * ((!isRequestParameterNullObj(ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE)) &&
		 * (!getRequestStringParameter(ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE).equals("Tutti") ) )
		 * lRicercaFascicolo
		 * .setCodOggettoSige(getRequestStringParameter(ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE) ); if
		 * ((!isRequestParameterNullObj(CAMPO_COD_MAG_ASS)) &&
		 * (!getRequestStringParameter(CAMPO_COD_MAG_ASS).equals("Tutti") ) )
		 * lRicercaFascicolo.setCodMagistrato(getRequestStringParameter(CAMPO_COD_MAG_ASS) ); if
		 * ((!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE)) &&
		 * (!getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).equals("-") ) )
		 * lRicercaFascicolo.setIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE) );
		 */

		// Modifica del 05/09/2013 "Implementazione SIES per accorpamento uffici"
		// Aggiunti apici per correggere errore preesistente
		// Cerca i fascicoli per Tipo Atto.
		if ((aRFSM.getCodTipoAtto() != null) && aRFSM.getCodTipoAtto().length() > 0)
			lCondizioni += " AND COD_TIPO_ATTO = '" + aRFSM.getCodTipoAtto() + "'";

		// Cerca i fascicoli per Tipo Oggetto.
		if ((aRFSM.getCodOggettoSige() != null) && aRFSM.getCodOggettoSige().length() > 0)
			lCondizioni += " AND COD_OGGETTO_SIGE = " + aRFSM.getCodOggettoSige();

		// Cerca i fascicoli per Magistrato.
		if ((aRFSM.getCodMagistrato() != null)) {
			if (aRFSM.getCodMagistrato().compareTo("9") == 0)
				lCondizioni += " AND MAG_COD_MAGISTRATO is not null ";
			else if (aRFSM.getCodMagistrato().compareTo("0") != 0)
				lCondizioni += " AND MAG_COD_MAGISTRATO = '" + aRFSM.getCodMagistrato() + "'";
		}

		// Cerca i fascicoli per Sezione.
		if ((aRFSM.getIdSezione() != null)) {
			if (aRFSM.getIdSezione().compareTo(new BigDecimal("9")) == 0)
				lCondizioni += " AND SEZ_ID_SEZIONE is not null ";
			else if (aRFSM.getIdSezione().compareTo(new BigDecimal("0")) == 0)
				lCondizioni += " AND SEZ_ID_SEZIONE is null ";
			else
				lCondizioni += " AND SEZ_ID_SEZIONE = " + aRFSM.getIdSezione();
		}

		// Cerca i fascicoli fino ad una data fine pendenza.
		if ((aRFSM.getDataFinePendenza() != null))
			lCondizioni += " AND (DATA_DEFINIZIONE is null OR TO_CHAR(DATA_DEFINIZIONE,'YYYYMMDD') > '"
					+ DateUtils.getDateToString(aRFSM.getDataFinePendenza(), "yyyyMMdd") + "')";
		lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') <= '"
				+ DateUtils.getDateToString(aRFSM.getDataFinePendenza(), "yyyyMMdd") + "'";

		// Cerca i fascicoli a partire da una data definizione.
		if ((aRFSM.getDataDefinizioneIniziale() != null))
			lCondizioni += " AND TO_CHAR(DATA_DEFINIZIONE,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aRFSM.getDataDefinizioneIniziale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli fino ad una data iscrizione.
		if ((aRFSM.getDataDefinizioneFinale() != null))
			lCondizioni += " AND TO_CHAR(DATA_DEFINIZIONE,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aRFSM.getDataDefinizioneFinale(), "yyyyMMdd") + "'";

		// 30/11/2009 Cerca i fascicoli per Tipo Rito.
		if ((aRFSM.getCodTipoRito() != null) && aRFSM.getCodTipoRito() != "-") {
			if (aRFSM.getCodTipoRito().compareTo("N") == 0)
				lCondizioni += " AND (COD_TIPO_GIUDIZIO is null or COD_TIPO_GIUDIZIO = '-') ";
			else if (aRFSM.getCodTipoRito().compareTo("T") == 0)
				lCondizioni += " AND (COD_TIPO_GIUDIZIO = 'C' or COD_TIPO_GIUDIZIO = 'M')";
			else
				lCondizioni += " AND COD_TIPO_GIUDIZIO = '" + aRFSM.getCodTipoRito() + "'";
		}
		return lCondizioni;
	}

	private String setCondizionePerEstremiStatistica(RicercaFascicoloSigeModel aRFSM) {
		String lCondizioni = new String();

		// Cerca i fascicoli per l'ufficio selezionato.
		if ((aRFSM.getChiaveUfficio().trim().length() > 0))
			lCondizioni += " AND CHIAVE_UFFICIO = " + aRFSM.getChiaveUfficio();

		// Cerca i fascicoli a partire da una data iscrizione.
		if ((aRFSM.getDataIscrizioneIniziale() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aRFSM.getDataIscrizioneIniziale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli fino ad una data iscrizione.
		if ((aRFSM.getDataIscrizioneFinale() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aRFSM.getDataIscrizioneFinale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli a partire da un anno iniziale.
		if ((aRFSM.getChiaveAnnoIniziale() != null))
			lCondizioni += " AND CHIAVE_ANNO >= " + aRFSM.getChiaveAnnoIniziale();

		// Cerca i fascicoli a partire da un progressivo iniziale.
		if ((aRFSM.getChiaveProgrIniziale() != null))
			lCondizioni += " AND CHIAVE_PROGR >= " + aRFSM.getChiaveProgrIniziale();

		// Cerca i fascicoli fino a un anno finale.
		if ((aRFSM.getChiaveAnnoFinale() != null))
			lCondizioni += " AND CHIAVE_ANNO <= " + aRFSM.getChiaveAnnoFinale();

		// Cerca i fascicoli fino a un progressivo finale.
		if ((aRFSM.getChiaveProgrFinale() != null))
			lCondizioni += " AND CHIAVE_PROGR <= " + aRFSM.getChiaveProgrFinale();

		if ((aRFSM.getCodTipoAtto() != null) && aRFSM.getCodTipoAtto().length() > 0)
			lCondizioni += " AND COD_TIPO_ATTO = '" + aRFSM.getCodTipoAtto() + "'";

		// Cerca i fascicoli per Tipo Oggetto.
		if ((aRFSM.getCodOggettoSige() != null) && aRFSM.getCodOggettoSige().length() > 0)
			lCondizioni += " AND COD_OGGETTO_SIGE = " + aRFSM.getCodOggettoSige();

		// Cerca i fascicoli per Magistrato.
		if ((aRFSM.getCodMagistrato() != null)) {
			if (aRFSM.getCodMagistrato().compareTo("9") == 0)
				lCondizioni += " AND MAG_COD_MAGISTRATO is not null ";
			else if (aRFSM.getCodMagistrato().compareTo("0") != 0)
				lCondizioni += " AND MAG_COD_MAGISTRATO = " + aRFSM.getCodMagistrato();
		}

		// Cerca i fascicoli per Sezione.
		if ((aRFSM.getIdSezione() != null)) {
			if (aRFSM.getIdSezione().compareTo(new BigDecimal("9")) == 0)
				lCondizioni += " AND SEZ_ID_SEZIONE is not null ";
			else if (aRFSM.getIdSezione().compareTo(new BigDecimal("0")) == 0)
				lCondizioni += " AND SEZ_ID_SEZIONE is null ";
			else
				lCondizioni += " AND SEZ_ID_SEZIONE = " + aRFSM.getIdSezione();
		}

		// Cerca i fascicoli fino ad una data fine pendenza.
		if ((aRFSM.getDataFinePendenza() != null))
			lCondizioni += " AND (DATA_DEFINIZIONE is null OR TO_CHAR(DATA_DEFINIZIONE,'YYYYMMDD') > '"
					+ DateUtils.getDateToString(aRFSM.getDataFinePendenza(), "yyyyMMdd") + "')";
		lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') <= '"
				+ DateUtils.getDateToString(aRFSM.getDataFinePendenza(), "yyyyMMdd") + "'";

		// Cerca i fascicoli a partire da una data definizione.
		if ((aRFSM.getDataDefinizioneIniziale() != null))
			lCondizioni += " AND TO_CHAR(DATA_DEFINIZIONE,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aRFSM.getDataDefinizioneIniziale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli fino ad una data iscrizione.
		if ((aRFSM.getDataDefinizioneFinale() != null))
			lCondizioni += " AND TO_CHAR(DATA_DEFINIZIONE,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aRFSM.getDataDefinizioneFinale(), "yyyyMMdd") + "'";

		// 30/11/2009 Cerca i fascicoli per Tipo Rito.
		if ((aRFSM.getCodTipoRito() != null) && aRFSM.getCodTipoRito() != "-") {
			if (aRFSM.getCodTipoRito().compareTo("N") == 0)
				lCondizioni += " AND (COD_TIPO_GIUDIZIO is null or COD_TIPO_GIUDIZIO = '-') ";
			else if (aRFSM.getCodTipoRito().compareTo("T") == 0)
				lCondizioni += " AND (COD_TIPO_GIUDIZIO = 'C' or COD_TIPO_GIUDIZIO = 'M')";
			else
				lCondizioni += " AND COD_TIPO_GIUDIZIO = '" + aRFSM.getCodTipoRito() + "'";
		}
		return lCondizioni;
	}

	private String setOrderAnnoProgrAsc() {
		String lOrder = new String();
		lOrder = " ORDER BY CHIAVE_ANNO ASC ,CHIAVE_PROGR ASC";
		return lOrder;
	}

	// MEV_57: aggiunto parametro di passaggio
	public void ricercaFascicoliBySoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String lCodDistretto, String majorOffice) {
		String strQuery = "";

		// Costruzione della query parametrizzata
		strQuery += getFascicoliBySoggettoSqlQuery(aModel, strCodUfficioUtenteConnesso, lCodDistretto,
				majorOffice);
		strQuery += setCondizione(aModel);

		// MEV_57: aggiunto controllo e and condition
		if (StringUtils.checkValidValue(majorOffice)) {
			// 20181031: aggiunta nuova vista e modificata query
			// strQuery += " and S.ID_SOGGETTO = vse.ID_SOGGETTO";
			strQuery += " and (nvl(vse.eta_soggetto_ora, 18) >= 18 or s.cod_ufficio_inserimento = '"
					+ majorOffice + "') ";
			strQuery += " and F.ID_FASCICOLO_SIGE = vse.id_fascicolo_sige";
		}

		strQuery += setGroupSoggetto();
		strQuery += setOrderCognome();

		setStatement(strQuery);
	}

	// MEV_57: aggiunto parametro di passaggio
	protected String getFascicoliBySoggettoSqlQuery(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String lCodDistretto, String majorOffice) {
		String lStatement = new String();

		lStatement += " SELECT  count(*) NUM_FASCICOLI, "
				+ " S.COGNOME, S.NOME, S.DATA_NASCITA,  S.DESC_COMUNE_NASCITA_ESTERO,"
				+ " S.COD_COMUNE_NASCITA, COMUNE_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, "
				+ " 1 ID_SOGGETTO, "
				+ " S.COD_FISCALE, S.COD_CS, S.COD_AFIS, S.ANNO_NASCITA, S.DATA_NASCITA_PRESUNTA, "
				+ " S.COD_STATO_NASCITA, S.NAZIONALITA, S.PATERNITA, S.COGNOME_MADRE, S.NOME_MADRE, "
				+ " S.SESSO, S.ATTO_NASCITA, S.MESE_NASCITA, " + " S.COD_PROVINCIA_NASCITA, "
				// 20180109: [SG] aggiunta estrazione di 2 campi
				+ " S.ETA_PRESUNTA_ANNI, S.ETA_PRESUNTA_MESI ";
		lStatement += " FROM FASCICOLO_SIGE F, SOGGETTO S, ";
		lStatement += " UFFICIO U, COMUNE C, COMUNE COMUNE_NASCITA";
		// MEV_57: aggiunto parametro di passaggio ed alias "S."
		if (StringUtils.checkValidValue(majorOffice))
			// 20181031: aggiunta nuova vista e modificata query
			// lStatement += ", V_SOGGETTO_MAGGIORENNE VSM";
			lStatement += ", v_sogsige_eta vse";
		lStatement += " WHERE SOG_ID_SOGGETTO = S.ID_SOGGETTO  AND U.COD_UFFICIO = F.CHIAVE_UFFICIO  AND U.COD_COMUNE = C.COD_COMUNE";
		lStatement += " AND S.COD_COMUNE_NASCITA = COMUNE_NASCITA.COD_COMUNE";

		if (lCodDistretto.length() > 1) {
			lStatement += " AND F.CHIAVE_UFFICIO in (select u.cod_ufficio from ufficio where u.COD_DISTRETTO='"
					+ lCodDistretto + "')";
		} else if (lCodDistretto.length() != 1) // Nella ricerca per tutto il DB viene passato
												// lCodDistretto="3"
		{
			lStatement += " AND F.CHIAVE_UFFICIO ='" + strCodUfficioUtenteConnesso + "'";
		}
		return lStatement;
	}

	/**
	 * Settaggio della condizione sul Soggetto
	 *
	 * @param aSm
	 *            ;
	 * @return lCondizioni
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
				lCondizioni += " AND PATERNITA LIKE '"
						+ StringUtils.convertSqlString(aSm.getPaternita().toUpperCase()) + "%'";
			if (!(aSm.getCodCs().equals("")))
				lCondizioni += " AND COD_AFIS LIKE '"
						+ StringUtils.convertSqlString(aSm.getCodCs().toUpperCase()) + "%'";
			if (!(aSm.getNomeMadre().equals("")))
				lCondizioni += " AND NOME_MADRE LIKE '"
						+ StringUtils.convertSqlString(aSm.getNomeMadre().toUpperCase()) + "%'";
			if (!(aSm.getCognomeMadre().equals("")))
				lCondizioni += " AND COGNOME_MADRE LIKE '"
						+ StringUtils.convertSqlString(aSm.getCognomeMadre().toUpperCase()) + "%'";
			if (!(aSm.getCodStatoNascita().equals("")))
				lCondizioni += " AND COD_STATO_NASCITA = '" + aSm.getCodStatoNascita() + "'";
			if (aSm.getDataNascita() != null)
				// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
				lCondizioni += " AND trunc(DATA_NASCITA) = TO_DATE('"
						+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
		} else {
			lCondizioni = " AND ID_SOGGETTO = " + aSm.getIdSoggetto();
		}
		return lCondizioni;
	}

	/**
	 * Esegue la ricerca dei fascicoli in base al super soggetto
	 *
	 * @param strCodiceDistrettoUtente
	 */
	protected String setCondizioneSuperSoggetto(SoggettoModel aModel) {
		String lCondizioni = new String();

		if (aModel.getAnnoNascita() != null)
			lCondizioni += " AND S.ANNO_NASCITA = '" + aModel.getAnnoNascita() + "'";
		else
			lCondizioni += " AND S.ANNO_NASCITA is null";

		// MEV_39: risolta casistica per unix (l'atto di nascita comprende il carattere ' --> NA'00 287
		if (aModel.getAttoNascita() != null && aModel.getAttoNascita().length() > 0)
			lCondizioni += " AND S.ATTO_NASCITA = '" + StringUtils.convertSqlString(aModel.getAttoNascita())
					+ "'";
		else
			lCondizioni += " AND S.ATTO_NASCITA is null";

		if (aModel.getCodAfis() != null && aModel.getCodAfis().length() > 0)
			lCondizioni += " AND S.COD_AFIS = '" + aModel.getCodAfis() + "'";
		else
			lCondizioni += " AND S.COD_AFIS is null";

		if (aModel.getCodComuneNascita() != null && aModel.getCodComuneNascita().length() > 0)
			lCondizioni += " AND S.COD_COMUNE_NASCITA = '" + aModel.getCodComuneNascita() + "'";
		else
			lCondizioni += " AND S.COD_COMUNE_NASCITA is null";

		if (aModel.getCodCs() != null && aModel.getCodCs().length() > 0)
			lCondizioni += " AND S.COD_CS = '" + StringUtils.convertSqlString(aModel.getCodCs().toUpperCase())
					+ "'";
		else
			lCondizioni += " AND S.COD_CS is null";

		if (aModel.getCodFiscale() != null && aModel.getCodFiscale().length() > 0)
			lCondizioni += " AND S.COD_FISCALE = '" + aModel.getCodFiscale() + "'";
		else
			lCondizioni += " AND S.COD_FISCALE is null";

		if (aModel.getCodProvinciaNascita() != null && aModel.getCodProvinciaNascita().length() > 0)
			lCondizioni += " AND S.COD_PROVINCIA_NASCITA = '" + aModel.getCodProvinciaNascita() + "'";
		else
			lCondizioni += " AND S.COD_PROVINCIA_NASCITA is null";

		if (aModel.getCodStatoNascita() != null && aModel.getCodStatoNascita().length() > 0)
			lCondizioni += " AND S.COD_STATO_NASCITA = '" + aModel.getCodStatoNascita() + "'";
		else
			lCondizioni += " AND S.COD_STATO_NASCITA is null";

		if (aModel.getCognome() != null && aModel.getCognome().length() > 0)
			lCondizioni += " AND S.COGNOME = '" + StringUtils.convertSqlString(aModel.getCognome()) + "'";
		else
			lCondizioni += " AND S.COGNOME is null";

		if (aModel.getNome() != null && aModel.getNome().length() > 0)
			lCondizioni += " AND S.NOME = '" + StringUtils.convertSqlString(aModel.getNome()) + "'";
		else
			lCondizioni += " AND S.NOME is null";

		if (aModel.getDataNascita() != null)
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lCondizioni += " AND trunc(S.DATA_NASCITA) = to_date('"
					+ DateUtils.getDateToString(aModel.getDataNascita(), "dd/MM/yyyy") + "','DD-MM-YYYY')";
		else
			lCondizioni += " AND S.DATA_NASCITA is null";

		if (aModel.getDataNascitaPresunta() != null && aModel.getDataNascitaPresunta().length() > 0)
			lCondizioni += " AND S.DATA_NASCITA_PRESUNTA = '" + aModel.getDataNascitaPresunta() + "'";
		else
			lCondizioni += " AND S.DATA_NASCITA_PRESUNTA is null";

		if (aModel.getDescComuneNascitaEstero() != null && aModel.getDescComuneNascitaEstero().length() > 0)
			lCondizioni += " AND S.DESC_COMUNE_NASCITA_ESTERO = '"
					+ StringUtils.convertSqlString(aModel.getDescComuneNascitaEstero()) + "'";
		else
			lCondizioni += " AND S.DESC_COMUNE_NASCITA_ESTERO is null";

		if (aModel.getNazionalita() != null && aModel.getNazionalita().length() > 0)
			lCondizioni += " AND S.NAZIONALITA = '" + aModel.getNazionalita() + "'";
		else
			lCondizioni += " AND S.NAZIONALITA is null";

		if (aModel.getPaternita() != null && aModel.getPaternita().length() > 0)
			lCondizioni += " AND S.PATERNITA = '"
					+ StringUtils.convertSqlString(aModel.getPaternita().toUpperCase()) + "'";
		else
			lCondizioni += " AND S.PATERNITA is null";

		if (aModel.getCognomeMadre() != null && aModel.getCognomeMadre().length() > 0)
			lCondizioni += " AND S.COGNOME_MADRE = '"
					+ StringUtils.convertSqlString(aModel.getCognomeMadre().toUpperCase()) + "'";
		else
			lCondizioni += " AND S.COGNOME_MADRE is null";

		if (aModel.getNomeMadre() != null && aModel.getNomeMadre().length() > 0)
			lCondizioni += " AND S.NOME_MADRE = '"
					+ StringUtils.convertSqlString(aModel.getNomeMadre().toUpperCase()) + "'";
		else
			lCondizioni += " AND S.NOME_MADRE is null";

		if (aModel.getSesso() != null && aModel.getSesso().length() > 0)
			lCondizioni += " AND S.SESSO = '" + aModel.getSesso() + "'";
		else
			lCondizioni += " AND S.SESSO is null";

		if (aModel.getMeseNascita() != null)
			lCondizioni += " AND S.MESE_NASCITA = " + aModel.getMeseNascita();
		else
			lCondizioni += " AND S.MESE_NASCITA is null";

		return lCondizioni;
	}

	private String setOrderCognome() {
		String lOrder = new String();
		lOrder = " ORDER BY COGNOME, NOME ";
		return lOrder;
	}

	private String setGroupSoggetto() {
		String lGroup = new String();
		lGroup += " group by " + " S.COGNOME, S.NOME, S.DATA_NASCITA,  S.DESC_COMUNE_NASCITA_ESTERO,"
				+ " S.COD_COMUNE_NASCITA, COMUNE_NASCITA.DESCRIZIONE, "
				+ " S.COD_FISCALE, S.COD_CS, S.COD_AFIS, S.ANNO_NASCITA, S.DATA_NASCITA_PRESUNTA, "
				+ " S.COD_STATO_NASCITA, S.NAZIONALITA, S.PATERNITA, S.COGNOME_MADRE, S.NOME_MADRE, "
				+ " S.SESSO, S.ATTO_NASCITA, S.MESE_NASCITA, " + " S.COD_PROVINCIA_NASCITA, "
				+ " S.ETA_PRESUNTA_ANNI, S.ETA_PRESUNTA_MESI ";
		return lGroup;
	}

	/*
	 * private String setGroupSoggetto() { String lGroupBy = new String(); lGroupBy =
	 * " group by Cognome, nome, S.DATA_NASCITA, S.COD_COMUNE_NASCITA, COMUNE_NASCITA.DESCRIZIONE, S.DESC_COMUNE_NASCITA_ESTERO, S.COD_PROVINCIA_NASCITA "
	 * ;
	 *
	 * // paolo cherubini x supersoggetto 30 luglio 2009 // commento la seguente riga // lGroupBy +=
	 * " Id_Soggetto, F.SOG_ID_SOGGETTO, "; //fine
	 *
	 * // paolo cherubini x supersoggetto 30 luglio 2009 // aggiungo le seguenti righe String lSuperSogg = new
	 * String(); lSuperSogg =
	 * ", S.COD_FISCALE, S.COD_CS, S.COD_AFIS, S.ANNO_NASCITA, S.DATA_NASCITA_PRESUNTA"; lSuperSogg +=
	 * ", S.COD_STATO_NASCITA, S.NAZIONALITA, S.PATERNITA, S.COGNOME_MADRE, S.NOME_MADRE"; lSuperSogg +=
	 * ", S.SESSO, S.ATTO_NASCITA, S.MESE_NASCITA "; lGroupBy += lSuperSogg; //fine
	 *
	 * return lGroupBy; }
	 */
	public void ricercaFascicoliDelSoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String lCodDistretto) {
		String strQuery = "";

		// Costruzione della query parametrizzata.
		strQuery += getFascicoliDelSoggetto(aModel, strCodUfficioUtenteConnesso, lCodDistretto);

		// paolo cherubini x supersoggetto 31 luglio 2009
		// inserisco la ricerca x soggetto
		strQuery += setCondizioneSuperSoggetto(aModel);
		// fine paolo

		// strQuery += setCondizione(aModel);

		setStatement(strQuery);
	}

	protected String getFascicoliDelSoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String lCodDistretto) {
		String lStatement = new String();
		lStatement += " SELECT ID_FASCICOLO_SIGE, ";
		lStatement += "SOG_ID_SOGGETTO, CHIAVE_ANNO, CHIAVE_UFFICIO, CHIAVE_PROGR, SEZ_ID_SEZIONE, COD_STATO_FASCICOLO, ";
		lStatement += "COD_TIPO_GIUDIZIO, DATA_ISCRIZIONE, DATA_DEFINIZIONE, RIC_ID_RICHIESTA_SIGE, ";
		lStatement += "F.COD_OPERATORE_INSERIMENTO, F.COD_UFFICIO_INSERIMENTO, F.DATA_INSERIMENTO, ";
		lStatement += "F.COD_OPERATORE_AGGIORNAMENTO, F.COD_UFFICIO_AGGIORNAMENTO, F.DATA_AGGIORNAMENTO, F.NOTE, F.COD_POSIZIONE_GIURIDICA, F.DATA_FINE_PENA, ";
		lStatement += "F.COD_TIPO_DEFINIZIONE, F.DESCR_DEFINIZIONE, ";
		lStatement += "F.FAS_SIG_ID_FASCICOLO_SIGE, F.NUMERO_FASCICOLI_UNIFICATI, ";
		lStatement += "NOME, COGNOME, DATA_NASCITA, ";
		lStatement += "R.COD_TIPO_ATTO, TRS.RV_MEANING DESCR_TIPO_ATTO, ";
		lStatement += "U.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO, C.DESCRIZIONE DESCR_COMUNE_UFFICIO, ";
		lStatement += "F.ID_FASCICOLO_SIGE_ORIGINE, ID_EVENTO_PROVV_CUMULO ";
		lStatement += " FROM FASCICOLO_SIGE F, SOGGETTO S, RICHIESTA_SIGE R, CG_REF_CODES TRS, UFFICIO U, COMUNE C ";
		lStatement += " WHERE SOG_ID_SOGGETTO = ID_SOGGETTO ";
		lStatement += " AND U.COD_UFFICIO = F.CHIAVE_UFFICIO ";
		lStatement += " AND U.COD_COMUNE = C.COD_COMUNE ";
		lStatement += " AND F.RIC_ID_RICHIESTA_SIGE = R.ID_RICHIESTA_SIGE ";
		lStatement += " AND TRS.RV_LOW_VALUE = R.COD_TIPO_ATTO ";
		lStatement += " AND TRS.RV_DOMAIN = 'TIPO_ATTO_SIGE' ";
		if (lCodDistretto.length() > 1) {
			lStatement += " AND F.CHIAVE_UFFICIO in (select u.cod_ufficio from ufficio where u.COD_DISTRETTO='"
					+ lCodDistretto + "')";
		} else if (lCodDistretto.length() != 1) // Nella ricerca per tutto il DB viene passato
												// lCodDistretto="3"
		{
			lStatement += " AND F.CHIAVE_UFFICIO ='" + strCodUfficioUtenteConnesso + "'";
		}
		return lStatement;
	}

	/**
	 * Metodo di ricerca di un FASCICOLO_SIGE con stessi: CHIAVE_ANNO, CHIAVE_PROGR, CHIAVE_UFFICIO.
	 * <p>
	 *
	 * @param aChiaveAnno
	 * @param aChiaveProgr
	 * @param aChiaveUfficio
	 * @throws DAOException
	 * @return boolean
	 */
	public String ExistAnnoProgrSige(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr, String aChiaveUfficio)
			throws DAOException {
		String response = "";
		String lStatement = "select FS.CHIAVE_ANNO , FS.CHIAVE_PROGR, FS.CHIAVE_UFFICIO ";
		lStatement += " from FASCICOLO_SIGE FS";
		lStatement += " where FS.CHIAVE_ANNO = '" + aChiaveAnno + "' ";
		lStatement += " and FS.CHIAVE_PROGR =  '" + aChiaveProgr + "' ";
		lStatement += " and FS.CHIAVE_UFFICIO =  '" + aChiaveUfficio + "' ";

		setStatement(lStatement);

		start();

		if (next()) {
			response = getBigDecimal("CHIAVE_ANNO").toString() + "/"
					+ getBigDecimal("CHIAVE_PROGR").toString();
			return response;
		} else
			return response;
	}

	public void ricercaFasSigePerIdFasSiep(BigDecimal aIdFascicoloSiep, String codUffUtenteConnesso) {
		String strQuery = "";

		// Costruzione della query parametrizzata.
		strQuery += getFasSigePerIdFasSiep(aIdFascicoloSiep, codUffUtenteConnesso);
		// strQuery += setCondizioneByIdFasSiepCodUfficio(aIdFascicoloSiep, codUffUtenteConnesso);

		setStatement(strQuery);
	}

	public void ricercaElencoFascicoliUnificati(FascicoloSigeModel aModel) throws DAOException {
		String lStatement = new String("");
		lStatement = getSqlQuery();
		lStatement += " WHERE " + setCondizioneUnificati(aModel);
		setStatement(lStatement);
	}

	protected String getFasSigePerIdFasSiep(BigDecimal aIdFascicoloSiep, String codUffUtenteConnesso) {
		String lStatement = new String();
		lStatement += " SELECT ID_FASCICOLO_SIGE, ";
		lStatement += "SOG_ID_SOGGETTO, CHIAVE_ANNO, CHIAVE_UFFICIO, CHIAVE_PROGR, SEZ_ID_SEZIONE, COD_STATO_FASCICOLO, ";
		lStatement += "COD_TIPO_GIUDIZIO, DATA_ISCRIZIONE, DATA_DEFINIZIONE, RIC_ID_RICHIESTA_SIGE, ";
		lStatement += "F.COD_OPERATORE_INSERIMENTO, F.COD_UFFICIO_INSERIMENTO, F.DATA_INSERIMENTO, ";
		lStatement += "F.COD_OPERATORE_AGGIORNAMENTO, F.COD_UFFICIO_AGGIORNAMENTO, F.DATA_AGGIORNAMENTO, F.NOTE, F.COD_POSIZIONE_GIURIDICA, F.DATA_FINE_PENA, ";
		lStatement += "F.COD_TIPO_DEFINIZIONE, F.DESCR_DEFINIZIONE, ";
		lStatement += "F.FAS_SIG_ID_FASCICOLO_SIGE, F.NUMERO_FASCICOLI_UNIFICATI, ";
		lStatement += "NOME, COGNOME, DATA_NASCITA, ";
		lStatement += "R.COD_TIPO_ATTO, TRS.RV_MEANING DESCR_TIPO_ATTO, ";
		lStatement += "U.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO, C.DESCRIZIONE DESCR_COMUNE_UFFICIO, ";
		lStatement += "F.ID_FASCICOLO_SIGE_ORIGINE, ID_EVENTO_PROVV_CUMULO ";
		lStatement += " FROM FASCICOLO_SIGE F, SOGGETTO S, RICHIESTA_SIGE R, CG_REF_CODES TRS, UFFICIO U, COMUNE C ";
		lStatement += " WHERE SOG_ID_SOGGETTO = ID_SOGGETTO ";
		lStatement += " AND U.COD_UFFICIO = F.CHIAVE_UFFICIO ";
		lStatement += " AND U.COD_COMUNE = C.COD_COMUNE ";
		lStatement += " AND F.RIC_ID_RICHIESTA_SIGE = R.ID_RICHIESTA_SIGE ";
		lStatement += " AND TRS.RV_LOW_VALUE = R.COD_TIPO_ATTO ";
		lStatement += " AND TRS.RV_DOMAIN = 'TIPO_ATTO_SIGE' ";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = '" + aIdFascicoloSiep + "'";
		if (codUffUtenteConnesso.length() > 1) {
			lStatement += " AND F.CHIAVE_UFFICIO = '" + codUffUtenteConnesso + "'";
		}
		return lStatement;
	}

	/**
	 * Settaggio della condizione sul Fascicolo Siep
	 *
	 * @param aIdFasSiep
	 *            ;
	 * @param aCodUfficio
	 *            ;
	 * @return lCondizioni
	 */
	// private String setCondizioneByIdFasSiepCodUfficio(BigDecimal aIdFasSiep, String aCodUfficio) {
	// String lCondizioni = new String();
	// String condUfficio = "";
	// if (aCodUfficio.length() > 1)
	// condUfficio += " AND FASCICOLO_SIGE.COD_UFFICIO_INSERIMENTO = " + aCodUfficio;
	//
	// lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFasSiep
	// + " AND RIC_ID_RICHIESTA_SIGE = ID_RICHIESTA_SIGE " + condUfficio;
	//
	// return lCondizioni;
	// }

	public void ricercaFascicoloSigeBySoggetto(BigDecimal aIdSoggetto) {
		String QueryFascicoloSige = "";

		// Costruita la query relativa soltanto al model fascicolo SIGE
		// MEV_57: aggiunto parametro di passaggio
		QueryFascicoloSige += getSqlQueryPerEstesa("");
		// Impostezione delle condizioni per Estremi
		QueryFascicoloSige += setCondizioneByIdSoggetto(aIdSoggetto);

		QueryFascicoloSige += setGroupByPerEstesa();

		// Settaggio della stringa SQL appena costruita prima della query
		setStatement(QueryFascicoloSige);
	}

	/**
	 * Condizione di ricerca dei FASCICOLI SIGE legati ad un Soggetto.
	 *
	 * @param aIdSoggetto
	 */
	public String setCondizioneByIdSoggetto(BigDecimal aIdSoggetto) {
		String lCondizioni = new String();

		lCondizioni += " AND F.SOG_ID_SOGGETTO = " + aIdSoggetto;

		return lCondizioni;
	}

	// private boolean findColumn(String aValue) {
	// try {
	// mRs.findColumn(aValue);
	// } catch (Exception sqex) {
	// return false;
	// }
	// return true;
	// }

	private String setGroupByPerEstesa() {
		String lGroup = new String();
		lGroup += " group by " + " F.ID_FASCICOLO_SIGE, SOG_ID_SOGGETTO, CHIAVE_ANNO, CHIAVE_UFFICIO, "
				+ " CHIAVE_PROGR, SEZ_ID_SEZIONE, COD_STATO_FASCICOLO, COD_TIPO_GIUDIZIO, "
				+ " DATA_ISCRIZIONE, DATA_DEFINIZIONE, RIC_ID_RICHIESTA_SIGE, F.COD_OPERATORE_INSERIMENTO, "
				+ " F.COD_UFFICIO_INSERIMENTO, F.DATA_INSERIMENTO, F.COD_OPERATORE_AGGIORNAMENTO, "
				+ " F.COD_UFFICIO_AGGIORNAMENTO, F.DATA_AGGIORNAMENTO, F.NOTE, F.COD_POSIZIONE_GIURIDICA, "
				+ " F.DATA_FINE_PENA, F.COD_TIPO_DEFINIZIONE, F.DESCR_DEFINIZIONE, F.FAS_SIG_ID_FASCICOLO_SIGE, "
				+ " F.NUMERO_FASCICOLI_UNIFICATI, S.NOME, S.COGNOME, DATA_NASCITA, R.COD_TIPO_ATTO, "
				+ " TRS.RV_MEANING, U.COD_TIPO_UFFICIO, C.DESCRIZIONE, E.COGNOME || ' ' || E.NOME, "
				+ " STATO_FASCICOLO.RV_MEANING, NVL(SEZIONE.DESCRIZIONE, '-'), ID_FASCICOLO_SIGE_ORIGINE, SEN_ID_SENTENZA_CUMULO, ID_EVENTO_PROVV_CUMULO";
		return lGroup;
	}

	public void ricercaFascicoloCollegato(BigDecimal idFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE " + setCondizioniIdFascOrigine(idFascicolo);
		setStatement(lSql);
	}

	public String setCondizioniIdFascOrigine(BigDecimal idFascicolo) {
		return " ID_FASCICOLO_SIGE_ORIGINE = " + idFascicolo;
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'
	 *
	 * @param rfsm
	 * @param pagine
	 * @throws DAOException
	 */
	public void ricercaSoggettiSigePerPosizioneGiuridica(RicercaFascicoloSigeModel rfsm, int pagine)
			throws DAOException {

		// se pagine = 0 la ricerca è per il foglio excel
		String s = new String();
		// 20191002 [SG]: intervento post collaudo 11.3 aggiunta query esterna
		if (pagine == 0)
			s += "select distinct X.ID_FASCICOLO_SIGE, X.CHIAVE_ANNO, X.CHIAVE_PROGR, X.COD_POSIZIONE_GIURIDICA, "
					+ "X.DATA_FINE_PENA, X.DATA_ISCRIZIONE, X.DATA_ARRIVO_CANCELLERIA, X.ID_RICHIESTA_SIGE, "
					+ "X.NOME, X.COGNOME, X.DATA_NASCITA, X.ID_SOGGETTO, X.COD_COMUNE_NASCITA, X.ANNO_NASCITA, "
					+ "X.DATA_NASCITA_PRESUNTA, X.MESE_NASCITA, X.ETA_PRESUNTA_ANNI, X.ETA_PRESUNTA_MESI, "
					+ "X.COD_STATO_NASCITA, X.DESC_COMUNE_NASCITA_ESTERO, X.DESCR_COMUNE_NASCITA, "
					+ "X.DESCR_NAZIONE_NASCITA, X.DESC_POSIZIONE_GIURIDICA, X.COD_MAGISTRATO, X.CM, X.NM, "
					+ "X.DESC_SEZIONE, LISTAGG(TO_CHAR(x.data_udienza,'DD/MM/YYYY'), ';' || chr(10)) "
					+ "WITHIN GROUP(order by x.ID_FASCICOLO_SIGE) "
					+ "OVER(PARTITION BY x.ID_FASCICOLO_SIGE) DATA_UDIENZA from (";
		s += "SELECT DISTINCT F.ID_FASCICOLO_SIGE, F.CHIAVE_ANNO, F.CHIAVE_PROGR, F.COD_POSIZIONE_GIURIDICA, "
				+ "F.DATA_FINE_PENA, F.DATA_ISCRIZIONE, H.DATA_ARRIVO_CANCELLERIA, H.ID_RICHIESTA_SIGE, "
				+ "S.NOME, S.COGNOME, S.DATA_NASCITA, S.ID_SOGGETTO, S.COD_COMUNE_NASCITA, S.ANNO_NASCITA, "
				+ "S.DATA_NASCITA_PRESUNTA, S.MESE_NASCITA, S.ETA_PRESUNTA_ANNI, S.ETA_PRESUNTA_MESI, "
				+ "S.COD_STATO_NASCITA, S.DESC_COMUNE_NASCITA_ESTERO, C.DESCRIZIONE DESCR_COMUNE_NASCITA, "
				+ "N.RV_MEANING DESCR_NAZIONE_NASCITA";
		if (pagine == 0)
			s += ", U.DATA_UDIENZA";
		s += ", DECODE(R.RV_MEANING, '-', 'Libero', R.RV_MEANING) DESC_POSIZIONE_GIURIDICA";
		// if (Utils.isPresent(rfsm.getCodMagistrato()) && !"-".equals(rfsm.getCodMagistrato()))
		s += ", M.COD_MAGISTRATO, M.COGNOME CM, M.NOME NM";
		// if (rfsm.getIdSezione() != null)
		s += ", Z.CODICE || ' - ' || Z.DESCRIZIONE DESC_SEZIONE";
		s += " FROM FASCICOLO_SIGE F, SOGGETTO S, COMUNE C, RICHIESTA_SIGE H, CG_REF_CODES R, CG_REF_CODES N";
		if (pagine == 0)
			s += ", UDIENZA_PROCEDIMENTO_SIGE P, UDIENZA_SIGE U";
		// if (Utils.isPresent(rfsm.getCodMagistrato()) && !"-".equals(rfsm.getCodMagistrato()))
		s += ", MAGISTRATO_ASSEGNATARIO A, MAGISTRATO M";
		// if (rfsm.getIdSezione() != null)
		s += ", SEZIONE Z";
		s += " WHERE F.SOG_ID_SOGGETTO = S.ID_SOGGETTO AND S.COD_COMUNE_NASCITA = C.COD_COMUNE ";
		s += " AND S.COD_STATO_NASCITA = N.RV_LOW_VALUE AND N.RV_DOMAIN = 'NAZIONE' ";
		s += "AND H.ID_RICHIESTA_SIGE(+) = F.RIC_ID_RICHIESTA_SIGE ";
		if (pagine == 0)
			s += "AND U.ID_UDIENZA_SIGE(+) = P.UDI_ID_UDIENZA_SIGE "
					+ "AND P.FAS_ID_FASCICOLO_SIGE(+) = F.ID_FASCICOLO_SIGE "
					// 20191002 [SG]: intervento post collaudo 11.3
					+ "AND p.flag_rinviata(+) <> 'A' ";
		if (rfsm.getDataIscrizioneIniziale() != null)
			s += "AND TO_CHAR(F.DATA_ISCRIZIONE,'YYYYMMDD') BETWEEN '"
					+ DateUtils.getDateToString(rfsm.getDataIscrizioneIniziale(), "yyyyMMdd") + "' AND '"
					+ DateUtils.getDateToString(rfsm.getDataIscrizioneFinale(), "yyyyMMdd") + "'";
		if (rfsm.getChiaveAnnoIniziale() != null) {
			s += " AND ((F.CHIAVE_ANNO > " + rfsm.getChiaveAnnoIniziale() + ")";
			s += " OR (F.CHIAVE_ANNO = " + rfsm.getChiaveAnnoIniziale() + " AND F.CHIAVE_PROGR >= "
					+ rfsm.getChiaveProgrIniziale() + "))";
			s += " AND ((F.CHIAVE_ANNO < " + rfsm.getChiaveAnnoFinale() + ")";
			s += " OR (F.CHIAVE_ANNO = " + rfsm.getChiaveAnnoFinale() + " AND F.CHIAVE_PROGR <= "
					+ rfsm.getChiaveProgrFinale() + "))";
		}
		switch (new Integer(rfsm.getCodPosizioneGiuridica()).intValue()) {
		case 1:
			s += "AND R.RV_DOMAIN = 'POSIZIONE_GIURIDICA_BENEFICI' AND R.RV_HIGH_VALUE = 'DETENUTO' "
					+ "AND R.RV_LOW_VALUE = F.COD_POSIZIONE_GIURIDICA ";
			break;
		case 2:
			s += "AND R.RV_DOMAIN = 'POSIZIONE_GIURIDICA_BENEFICI' "
					+ "AND R.RV_HIGH_VALUE = 'MISURA ALTERNATIVA' "
					+ "AND R.RV_LOW_VALUE = F.COD_POSIZIONE_GIURIDICA ";
			break;
		case 3:
			s += "AND R.RV_DOMAIN = 'POSIZIONE_GIURIDICA_BENEFICI' AND R.RV_HIGH_VALUE = 'LIBERO' "
					+ "AND R.RV_LOW_VALUE = F.COD_POSIZIONE_GIURIDICA ";
			break;
		default:
			s += "AND R.RV_DOMAIN = 'POSIZIONE_GIURIDICA_BENEFICI' "
					+ "AND R.RV_LOW_VALUE = F.COD_POSIZIONE_GIURIDICA ";
			// s += "AND F.COD_POSIZIONE_GIURIDICA IS NOT NULL ";
			// NESSUNA Pos. Giu. --> le prendo tutte poichè nel db esiste sempre valorizzato questo campo
			// F.COD_POSIZIONE_GIURIDICA = '-' nel DB la posizione giuridica '-' equivale a libero
			break;
		}
		if (rfsm.getDataFinePendenza() != null) {
			s += "AND (F.DATA_DEFINIZIONE is null OR TO_CHAR(DATA_DEFINIZIONE, 'YYYYMMDD') > '"
					+ DateUtils.getDateToString(rfsm.getDataFinePendenza(), "yyyyMMdd") + "') ";
			s += " AND TO_CHAR(F.DATA_ISCRIZIONE, 'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(rfsm.getDataFinePendenza(), "yyyyMMdd") + "' ";
		}
		// magistrato + sezione + nazionalita'
		s += "AND F.ID_FASCICOLO_SIGE = A.FAS_SIGE_ID_FASCICOLO_SIGE(+) ";
		s += "AND A.MAG_COD_MAGISTRATO = M.COD_MAGISTRATO(+) ";
		s += "AND A.DATA_FINE IS NULL ";
		if (Utils.isPresent(rfsm.getCodMagistrato()) && !"-".equals(rfsm.getCodMagistrato())) {
			// s += "AND A.DATA_FINE IS NULL ";
			if ("9".equals(rfsm.getCodMagistrato())) {
				s += "AND M.COD_MAGISTRATO IS NOT NULL ";
				s += "AND M.DATA_FINE_VALIDITA IS NULL ";
				s += "AND M.COD_UFFICIO_INSERIMENTO = F.COD_UFFICIO_INSERIMENTO ";
			} else if ("0".equals(rfsm.getCodMagistrato())) {
				s += "AND M.COD_MAGISTRATO IS NULL ";
				s += "AND F.ID_FASCICOLO_SIGE NOT IN (SELECT MA.FAS_SIGE_ID_FASCICOLO_SIGE FROM "
						+ "MAGISTRATO_ASSEGNATARIO MA WHERE MA.DATA_FINE IS NULL) ";
			} else {
				s += "AND M.COD_MAGISTRATO = '" + rfsm.getCodMagistrato() + "' ";
				s += "AND M.DATA_FINE_VALIDITA IS NULL ";
				s += "AND M.COD_UFFICIO_INSERIMENTO = F.COD_UFFICIO_INSERIMENTO ";
			}
		}
		s += "AND Z.ID_SEZIONE(+) = F.SEZ_ID_SEZIONE ";
		if (rfsm.getIdSezione() != null) {
			if ("9".equals(rfsm.getIdSezione().toString()))
				s += "AND F.SEZ_ID_SEZIONE IS NOT NULL ";
			else if ("0".equals(rfsm.getIdSezione().toString()))
				s += "AND F.SEZ_ID_SEZIONE IS NULL ";
			else
				s += "AND F.SEZ_ID_SEZIONE = '" + rfsm.getIdSezione().toString() + "' ";
		}
		if (Utils.isPresent(rfsm.getCodNazione()) && !"-".equals(rfsm.getCodNazione())) {
			if ("S".equals(rfsm.getCodNazione()))
				s += "AND S.COD_STATO_NASCITA != '039'";
			else
				s += "AND S.COD_STATO_NASCITA = '" + rfsm.getCodNazione() + "'";
		}
		// UFFICIO INTERESSATO
		if (Utils.isPresent(rfsm.getChiaveUfficioInserimento()))
			s += " AND F.COD_UFFICIO_INSERIMENTO = " + rfsm.getChiaveUfficioInserimento();
		if (Utils.isPresent(rfsm.getChiaveUfficio()))
			s += " AND F.CHIAVE_UFFICIO = " + rfsm.getChiaveUfficio();

		// 20191002 [SG]: intervento post collaudo 11.3 aggiunta query esterna
		if (pagine == 0)
			s += " ) x order by X.chiave_anno, x.chiave_progr";
		else
			// 20191002 [SG]: intervento post collaudo 11.3 --> aggiunto ordinamento
			s += " order by f.chiave_anno, f.chiave_progr";

		setStatement(s);
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'
	 *
	 * @return GenericModel
	 * @throws DAOException
	 */
	public GenericModel getModelRicercaSoggettiSigePerPosizioneGiuridica() throws DAOException {

		FascicoloSigeEstesoModel fsem = new FascicoloSigeEstesoModel();
		FascicoloSigeModel fsm = new FascicoloSigeModel();
		SoggettoModel sm = new SoggettoModel();
		RichiestaSigeModel rsm = new RichiestaSigeModel();
		UdienzaProcedimentoSigeModel upsm = new UdienzaProcedimentoSigeModel();
		MagistratoAssegnatarioModel mam = new MagistratoAssegnatarioModel();
		MagistratoModel mm = new MagistratoModel();

		fsm.setIdFascicoloSige(getBigDecimal("ID_FASCICOLO_SIGE"));
		fsm.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		fsm.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		fsm.setCodPosizioneGiuridica(getString("COD_POSIZIONE_GIURIDICA"));
		fsm.setDataFinePena(getDate("DATA_FINE_PENA"));
		fsm.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		if (findColumn("DESC_SEZIONE"))
			fsm.setDescrSezione(getString("DESC_SEZIONE"));
		fsm.setDescrPosizioneGiuridica(getString("DESC_POSIZIONE_GIURIDICA"));

		if (findColumn("COD_MAGISTRATO"))
			mm.setCodMagistrato(getString("COD_MAGISTRATO"));
		if (findColumn("CM"))
			mm.setCognome(getString("CM"));
		if (findColumn("NM"))
			mm.setNome(getString("NM"));
		mam.setMagistrato(mm);

		rsm.setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA"));
		rsm.setIdRichiestaSige(getBigDecimal("ID_RICHIESTA_SIGE"));

		// 20191002 [SG]: intervento post collaudo 11.3 aggiunta query esterna
		if (findColumn("DATA_UDIENZA"))
			// upsm.setDataUdienzaSige(getString("DATA_UDIENZA"));
			upsm.setListaDateUdienzaSige(getString("DATA_UDIENZA"));

		sm.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		sm.setNome(getString("NOME"));
		sm.setCognome(getString("COGNOME"));
		sm.setDataNascita(getDate("DATA_NASCITA"));
		sm.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		sm.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		sm.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		sm.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA"));
		sm.setMeseNascita(getBigDecimal("MESE_NASCITA"));
		sm.setEtaPresuntaAnni(getBigDecimal("ETA_PRESUNTA_ANNI"));
		sm.setEtaPresuntaMesi(getBigDecimal("ETA_PRESUNTA_MESI"));
		sm.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		sm.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		sm.setDescrStatoNascita(getString("DESCR_NAZIONE_NASCITA"));

		fsem.setFascicoloSige(fsm);
		fsem.setSoggetto(sm);
		fsem.setRichiestaSige(rsm);
		fsem.setUdienzaProcedimento(upsm);
		fsem.setMagAssegnatario(mam);

		// valore di ritorno
		return fsem;
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'
	 *
	 * @param rfsm
	 * @param pagine
	 * @throws DAOException
	 */
	public void ricercaProcedimentiSigeConRicorsoOpposizione(RicercaFascicoloSigeModel rfsm, int pagine)
			throws DAOException {

		// se pagine = 0 la ricerca è per il foglio excel
		String s = new String();
		s += "select DISTINCT i.id_impugnazione_sige, i.anno_s7, i.progr_s7, f.chiave_anno, f.chiave_progr, "
				+ "s.cognome, s.nome, i.data_arrivo_cancelleria, p.data_emissione, p.cod_tipo_provvedimento, "
				+ "tp.rv_meaning estremi, p.chiave_anno anno, p.chiave_progr prog, i.soggetto_impugnante, "
				+ "i.data_decisione, i.cod_tipo_impugnazione, trs.rv_meaning tipo, i.cod_tenore_decisione, "
				+ "tdrs.rv_meaning tenore, sis.rv_meaning parte, p.id_provvedimento_sige, s.id_soggetto, "
				+ "p.cod_tipo_provvedimento_sige, tps.rv_meaning estremi_sige, f.id_fascicolo_sige, "
				+ "p.data_deposito";
		if (pagine == 0) {
			s += ", e.id_evento, e.data_emissione data_emissione_evento, i.data_ricorso, i.data_restituzione_atti, "
					+ "i.cod_autorita_destinataria, i.flag_sosp_esec, i.flag_annullamento, "
					+ "e.cod_tipo_provvedimento cod_tipo_provvedimento_evento, i.data_trasmissione_atti, "
					+ "e.cod_motivo, e.cod_esito, ctp.rv_meaning descr_tipo_provvedimento, "
					+ "cm.rv_meaning descr_motivo_provvedimento, ce.rv_meaning descr_esito_provvedimento, "
					+ "s.data_nascita, s.cod_comune_nascita, s.cod_stato_nascita, s.anno_nascita, "
					+ "s.data_nascita_presunta, s.mese_nascita, s.eta_presunta_anni, s.eta_presunta_mesi, "
					+ "s.desc_comune_nascita_estero, s.cod_comune_nascita, s.cod_stato_nascita, "
					+ "s.cod_provincia_nascita, cn.descrizione descr_comune_nascita, "
					+ "nn.rv_meaning descr_nazione_nascita, pn.rv_meaning descr_provincia_nascita";
			// 20191002 [SG]: intervento post collaudo 11.3 rimossi i due campi
			// + "ad.rv_meaning descr_autorita_destinataria, os.rv_meaning descr_oggetto_sige, "
			// + "ets.rv_meaning descr_esito_tenore_sige";
			// + "listagg(os.rv_meaning, ';' || chr(10)) WITHIN GROUP(ORDER BY i.id_impugnazione_sige)
			// descr_oggetto_sige, "
			// + "listagg(ets.rv_meaning, ';' || chr(10)) WITHIN GROUP(ORDER BY i.id_impugnazione_sige)
			// descr_esito_tenore_sige";
			// , dps.rv_meaning descr_dati_provvedimento_sige"
		}
		s += " from fascicolo_sige f, provvedimento_sige p, impugnazione_sige i, cg_ref_codes tdrs, "
				+ "cg_ref_codes tps, cg_ref_codes trs, cg_ref_codes sis, cg_ref_codes tp, soggetto s";
		if (pagine == 0)
			s += ", cg_ref_codes ctp, cg_ref_codes cm, cg_ref_codes ce, evento e, comune cn, "
					+ "cg_ref_codes nn, cg_ref_codes pn, cg_ref_codes ad, tenore_sige ts, "
					+ "cg_ref_codes os, cg_ref_codes ets";
		// , cg_ref_codes dps"
		s += " where i.provv_id_provvedimento_sige = p.id_provvedimento_sige "
				+ "and p.fas_id_fascicolo_sige = f.id_fascicolo_sige "
				+ "and tdrs.rv_domain = 'TENORE_DECISIONE_RICORSO_SIGE' "
				+ "and tdrs.rv_low_value = i.cod_tenore_decisione "
				+ "and tps.rv_domain = 'TIPO_PROVVEDIMENTO_SIGE' "
				+ "and tps.rv_low_value = p.cod_tipo_provvedimento_sige "
				+ "and tp.rv_domain = 'TIPO_PROVVEDIMENTO' "
				+ "and tp.rv_low_value = p.cod_tipo_provvedimento "
				+ "and trs.rv_domain = 'TIPO_RICORSO_SIGE' "
				+ "and trs.rv_low_value = i.cod_tipo_impugnazione "
				// aggiunta outer join su sis
				+ "and sis.rv_domain(+) = 'SOGGETTO_IMPUGNANTE_SIGE' "
				+ "and sis.rv_low_value(+) = i.soggetto_impugnante "
				+ "and s.id_soggetto = f.sog_id_soggetto";
		s += scegliTipoRicorso(rfsm.getTipoRicorso());
		if (rfsm.getChiaveAnnoRicorso() != null)
			s += " and i.anno_s7 = '" + rfsm.getChiaveAnnoRicorso().toString() + "' and i.progr_s7 = '"
					+ rfsm.getChiaveProgrRicorso().toString() + "'";
		if (rfsm.getChiaveAnnoIniziale() != null) {
			s += " and ((i.anno_s7 > " + rfsm.getChiaveAnnoIniziale() + ")";
			s += " or (i.anno_s7 = " + rfsm.getChiaveAnnoIniziale() + " and i.progr_s7 >= "
					+ rfsm.getChiaveProgrIniziale() + "))";
			s += " and ((i.anno_s7 < " + rfsm.getChiaveAnnoFinale() + ")";
			s += " or (i.anno_s7 = " + rfsm.getChiaveAnnoFinale() + " and i.progr_s7 <= "
					+ rfsm.getChiaveProgrFinale() + "))";
		}
		if (rfsm.getDataArrivoCancelleriaIniziale() != null)
			s += " and to_char(i.data_arrivo_cancelleria,'yyyyMMdd') between '"
					+ DateUtils.getDateToString(rfsm.getDataArrivoCancelleriaIniziale(), "yyyyMMdd")
					+ "' and '" + DateUtils.getDateToString(rfsm.getDataArrivoCancelleriaFinale(), "yyyyMMdd")
					+ "'";
		if (!"Tutti".equals(rfsm.getStatoValidazione())) {
			if ("Annullati".equals(rfsm.getStatoValidazione()))
				s += " and i.flag_annullamento = 'S'";
			else
				s += " and (i.flag_annullamento != 'S' or i.flag_annullamento is null)";
		}
		// UFFICIO INTERESSATO
		if (Utils.isPresent(rfsm.getChiaveUfficioInserimento()))
			s += " and f.cod_ufficio_inserimento = " + rfsm.getChiaveUfficioInserimento();
		if (Utils.isPresent(rfsm.getChiaveUfficio()))
			s += " and f.chiave_ufficio = " + rfsm.getChiaveUfficio();
		if (pagine == 0) {
			s += " and e.id_evento = p.id_evento_generato and ctp.rv_domain = 'TIPO_PROVVEDIMENTO'"
					+ " and ctp.rv_low_value = e.cod_tipo_provvedimento"
					+ " and cm.rv_domain = 'MOTIVO_PROVVEDIMENTO' and cm.rv_low_value = e.cod_motivo"
					// 20191002 [SG]: intervento post collaudo 11.3 --> aggiunta left outer join su ce
					+ " and ce.rv_domain(+) = 'ESITO_PROVVEDIMENTO_SIGE' and ce.rv_low_value(+) = e.cod_esito"
					+ " and cn.cod_comune = s.cod_comune_nascita and nn.rv_domain(+) = 'NAZIONE'"
					+ " and nn.rv_low_value(+) = s.cod_stato_nascita and pn.rv_domain(+) = 'PROVINCIA'"
					+ " and pn.rv_low_value(+) = s.cod_provincia_nascita"
					+ " and ad.rv_domain = 'TIPO_UFFICIO' and ad.rv_low_value = i.cod_autorita_destinataria"
					+ " and ts.prov_id_provvedimento_sige = p.id_provvedimento_sige"
					+ " and ts.fas_id_fascicolo_sige = f.id_fascicolo_sige"
					+ " and os.rv_domain = 'OGGETTO_SIGE'" + " and os.rv_low_value = ts.cod_oggetto_sige"
					+ " and ets.rv_domain = 'ESITO_TENORE_SIGE'" + " and ets.rv_low_value = ts.cod_esito_sige"
					// " and dps.rv_domain(+) = 'DATI_PROVVEDIMENTO_SIGE'"
					// " and dps.rv_high_value(+) = os.rv_high_value"
					// " and dps.rv_low_value(+) = ets.rv_low_value"
					// 20191002 [SG]: intervento post collaudo 11.3
					+ " group by i.id_impugnazione_sige, i.anno_s7, i.progr_s7, f.chiave_anno, f.chiave_progr,"
					+ " s.cognome, s.nome, i.data_arrivo_cancelleria, p.data_emissione, p.cod_tipo_provvedimento,"
					+ " tp.rv_meaning, p.chiave_anno, p.chiave_progr, i.soggetto_impugnante, i.data_decisione,"
					+ " i.cod_tipo_impugnazione, trs.rv_meaning, i.cod_tenore_decisione, tdrs.rv_meaning,"
					+ " sis.rv_meaning, p.id_provvedimento_sige, s.id_soggetto, p.cod_tipo_provvedimento_sige,"
					+ " tps.rv_meaning, f.id_fascicolo_sige, p.data_deposito, e.id_evento, e.data_emissione,"
					+ " i.data_ricorso, i.data_restituzione_atti, i.cod_autorita_destinataria, i.flag_sosp_esec,"
					+ " i.flag_annullamento, e.cod_tipo_provvedimento, i.data_trasmissione_atti, e.cod_motivo,"
					+ " e.cod_esito, ctp.rv_meaning, cm.rv_meaning, ce.rv_meaning, s.data_nascita,"
					+ " s.cod_comune_nascita, s.cod_stato_nascita, s.anno_nascita, s.data_nascita_presunta,"
					+ " s.mese_nascita, s.eta_presunta_anni, s.eta_presunta_mesi, s.desc_comune_nascita_estero,"
					+ " s.cod_comune_nascita, s.cod_stato_nascita, s.cod_provincia_nascita, cn.descrizione,"
					+ " nn.rv_meaning, pn.rv_meaning, ad.rv_meaning";
		}

		s += " order by i.anno_s7, i.progr_s7, f.chiave_anno, f.chiave_progr";

		setStatement(s);
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'
	 *
	 * @param tipoRicorso
	 * @return
	 */
	private String scegliTipoRicorso(String tipoRicorso) {

		String s;
		switch (new Integer(tipoRicorso).intValue()) {
		// <option value="01">Ricorso</option>
		// <option value="02">Ricorso senza esito</option>
		// <option value="03">Ricorso con esito</option>
		// <option value="04">Opposizione</option>
		// <option value="05">Opposizione senza esito</option>
		// <option value="06">Opposizione con esito</option>
		case 1:
			s = " and i.cod_tipo_impugnazione = '01'";
			break;
		case 2:
			s = " and i.cod_tipo_impugnazione = '01' and i.cod_tenore_decisione = '-'";
			break;
		case 3:
			s = " and i.cod_tipo_impugnazione = '01' and i.cod_tenore_decisione != '-'";
			break;
		case 4:
			s = " and i.cod_tipo_impugnazione = '04'";
			break;
		case 5:
			s = " and i.cod_tipo_impugnazione = '04' and i.cod_tenore_decisione = '-'";
			break;
		case 6:
			s = " and i.cod_tipo_impugnazione = '04' and i.cod_tenore_decisione != '-'";
			break;
		default:
			s = " and i.cod_tipo_impugnazione is not null";
			break;
		}
		return s;
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'
	 *
	 * @return GenericModel
	 * @throws DAOException
	 */
	public FascicoloSigeEstesoModel getModelRicercaProcedimentiSigeConRicorsoOpposizione()
			throws DAOException {

		FascicoloSigeEstesoModel fsem = new FascicoloSigeEstesoModel();
		FascicoloSigeModel fsm = new FascicoloSigeModel();
		SoggettoModel sm = new SoggettoModel();
		ImpugnazioneSigeModel ism = new ImpugnazioneSigeModel();
		ProvvedimentoSigeEventoModel psem = new ProvvedimentoSigeEventoModel();
		EventoNotificaModel enm = new EventoNotificaModel();
		EventoModel em = new EventoModel();

		ism.setAnnoS7(getBigDecimal("ANNO_S7"));
		ism.setProgrS7(getBigDecimal("PROGR_S7"));
		ism.setIdImpugnazioneSige(getBigDecimal("ID_IMPUGNAZIONE_SIGE"));
		ism.setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA"));
		ism.setSoggettoImpugnante(getString("SOGGETTO_IMPUGNANTE"));
		ism.setDataDecisione(getDate("DATA_DECISIONE"));
		ism.setCodTipoImpugnazione(getString("COD_TIPO_IMPUGNAZIONE"));
		ism.setDescrTenoreDecisione(getString("TENORE"));
		ism.setDescrSoggettoImpugnante(getString("PARTE"));
		ism.setDescrTipoImpugnazione(getString("TIPO"));
		ism.setCodTenoreDecisione(getString("COD_TENORE_DECISIONE"));
		if (findColumn("DATA_RICORSO"))
			ism.setDataRicorso(getDate("DATA_RICORSO"));
		if (findColumn("DATA_RESTITUZIONE_ATTI"))
			ism.setDataRestituzioneAtti(getDate("DATA_RESTITUZIONE_ATTI"));
		if (findColumn("DATA_TRASMISSIONE_ATTI"))
			ism.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
		if (findColumn("COD_AUTORITA_DESTINATARIA"))
			ism.setCodAutoritaDestinataria(getString("COD_AUTORITA_DESTINATARIA"));
		if (findColumn("FLAG_SOSP_ESEC"))
			ism.setFlagSospEsec(getString("FLAG_SOSP_ESEC"));
		if (findColumn("FLAG_ANNULLAMENTO"))
			ism.setFlagAnnullamento(getString("FLAG_ANNULLAMENTO"));
		if (findColumn("DESCR_AUTORITA_DESTINATARIA"))
			ism.setDescrAutoritaDestinataria(getString("DESCR_AUTORITA_DESTINATARIA"));

		// 20191002 [SG]: intervento post collaudo 11.3 rimossi i due campi
		// TenoreSigeEstesoModel tsem = new TenoreSigeEstesoModel();
		// if (findColumn("DESCR_OGGETTO_SIGE"))
		// tsem.setDescrOggettoSige(getString("DESCR_OGGETTO_SIGE"));
		// if (findColumn("DESCR_ESITO_TENORE_SIGE"))
		// tsem.setDescrEsitoSige(getString("DESCR_ESITO_TENORE_SIGE"));
		// Vector<TenoreSigeEstesoModel> v = new Vector<>();
		// v.add(tsem);
		// psem.setTenoriEstesi(v);

		ProvvedimentoSigeModel psm = new ProvvedimentoSigeModel();
		psm.setChiaveAnno(getBigDecimal("ANNO"));
		psm.setChiaveProgr(getBigDecimal("PROG"));
		psm.setDataEmissione(getDate("DATA_EMISSIONE"));
		psm.setIdProvvedimentoSige(getBigDecimal("ID_PROVVEDIMENTO_SIGE"));
		psm.setCodTipoProvvedimentoSige(getString("COD_TIPO_PROVVEDIMENTO_SIGE"));
		psm.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		psm.setDescrTipoProvvedimento(getString("ESTREMI"));
		psm.setDescrTipoProvvedimentoSige(getString("ESTREMI_SIGE"));
		psm.setDataDeposito(getDate("DATA_DEPOSITO"));
		psem.setProvvedimento(psm);

		fsm.setIdFascicoloSige(getBigDecimal("ID_FASCICOLO_SIGE"));
		fsm.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		fsm.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));

		sm.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		sm.setNome(getString("NOME"));
		sm.setCognome(getString("COGNOME"));
		if (findColumn("DATA_NASCITA"))
			sm.setDataNascita(getDate("DATA_NASCITA"));
		if (findColumn("COD_COMUNE_NASCITA"))
			sm.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		if (findColumn("COD_STATO_NASCITA"))
			sm.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		if (findColumn("ANNO_NASCITA"))
			sm.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		if (findColumn("DATA_NASCITA_PRESUNTA"))
			sm.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA"));
		if (findColumn("MESE_NASCITA"))
			sm.setMeseNascita(getBigDecimal("MESE_NASCITA"));
		if (findColumn("ETA_PRESUNTA_ANNI"))
			sm.setEtaPresuntaAnni(getBigDecimal("ETA_PRESUNTA_ANNI"));
		if (findColumn("ETA_PRESUNTA_MESI"))
			sm.setEtaPresuntaMesi(getBigDecimal("ETA_PRESUNTA_MESI"));
		if (findColumn("DESC_COMUNE_NASCITA_ESTERO"))
			sm.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		if (findColumn("DESCR_COMUNE_NASCITA"))
			sm.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		if (findColumn("DESCR_NAZIONE_NASCITA"))
			sm.setDescrStatoNascita(getString("DESCR_NAZIONE_NASCITA"));
		if (findColumn("DESCR_PROVINCIA_NASCITA"))
			sm.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA"));
		if (findColumn("COD_PROVINCIA_NASCITA"))
			sm.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));

		if (findColumn("ID_EVENTO"))
			em.setIdEvento(getBigDecimal("ID_EVENTO"));
		if (findColumn("DATA_EMISSIONE_EVENTO"))
			em.setDataEmissione(getDate("DATA_EMISSIONE_EVENTO"));
		if (findColumn("COD_TIPO_PROVVEDIMENTO_EVENTO"))
			em.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO_EVENTO"));
		if (findColumn("COD_MOTIVO"))
			em.setCodMotivo(getString("COD_MOTIVO"));
		if (findColumn("COD_ESITO"))
			em.setCodEsito(getString("COD_ESITO"));
		if (findColumn("DESCR_TIPO_PROVVEDIMENTO"))
			em.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO"));
		if (findColumn("DESCR_MOTIVO_PROVVEDIMENTO"))
			em.setDescrMotivo(getString("DESCR_MOTIVO_PROVVEDIMENTO"));
		if (findColumn("DESCR_ESITO_PROVVEDIMENTO"))
			em.setDescrEsito(getString("DESCR_ESITO_PROVVEDIMENTO"));

		enm.setEvento(em);
		psem.setEventoNotifica(enm);
		fsem.setFascicoloSige(fsm);
		fsem.setSoggetto(sm);
		fsem.setImpugnazioneSige(ism);
		fsem.setProvvedimentoSigeEvento(psem);

		// valore di ritorno
		return fsem;
	}

}