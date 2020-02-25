package siap.siep.misurasicurezza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaNotificataModel;
import siap.sige.misurasicurezza.model.MisuraSicurezzaSigeModel;

/**
 * <p>
 * Title: MisuraSicurezzaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella MisuraSicurezza
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

public class MisuraSicurezzaSqlDAO extends SIAPSqlDAO {
	public MisuraSicurezzaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaMisuraSicurezza(MisuraSicurezzaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		lSql += " " + setOrderByIns();
		setStatement(lSql);
	}

	public void ricercaMisuraSicurezzaApplicata(MisuraSicurezzaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		if (aModel.getEveIdEvento() != null)
			lSql += " " + setCondizioniByEveIdEvento(aModel.getEveIdEvento());
		lSql += " " + setOrderByIns();
		setStatement(lSql);
	}

	public void ricercaMisuraSicurezzaEstesa(MisuraSicurezzaModel aModel) throws DAOException {
		String lSql = getSqlQueryEstesa();

		lSql += " " + setCondizioniRicercaMisuraEstesaByIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
		setStatement(lSql);
	}

	public void ricercaFascicoliMisuraSicurezza(String aCodUfficioUtenteConnesso) throws DAOException {
		String lStatement = getSqlQueryMisuraFascicoli();

		lStatement += " AND FASCICOLO_SIEP.CHIAVE_UFFICIO ='" + aCodUfficioUtenteConnesso + "'";

		lStatement += " ORDER BY FASCICOLO_SIEP.CHIAVE_ANNO ASC, FASCICOLO_SIEP.CHIAVE_PROGR ASC";

		setStatement(lStatement);
	}

	public void getCountFascicoliMisuraSicurezza(String aCodUfficioUtenteConnesso) throws DAOException {
		String lStatement = " SELECT count(distinct(fascicolo_siep.id_fascicolo_siep)) HowManyRecords";
		lStatement += " FROM misura_sicurezza, fascicolo_siep";
		lStatement += " WHERE fascicolo_siep.chiave_ufficio = '" + aCodUfficioUtenteConnesso + "'";
		lStatement += " AND misura_sicurezza.fas_sie_id_fascicolo_siep = fascicolo_siep.id_fascicolo_siep";

		setStatement(lStatement);
	}

	public void ricercaMisuraSicurezzaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaMisuraSicurezzaByKeyMisuraCollegata(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " AND MIS_ID_MISURA_SICUREZZA = " + aKey;
		setStatement(lSql);
	}

	public void ricercaMisuraSicurezzaByEventoKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND EVE_ID_EVENTO = " + aKey;
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_MISURA_SICUREZZA, " + "COD_NATURA,  NATURA.RV_MEANING DESCR_NATURA, "
				+ "COD_TIPO, TIPOMISURA.RV_MEANING DESCR_TIPO, TIPOMISURA.RV_HIGH_VALUE COD_OGGETTO_ESECUZIONE,"
				+ "NUM_ANNI, " + "NUM_MESI, " + "NUM_GIORNI, " + "ANNO_REG_38, " + "NUM_REG_38, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, " + "FAS_SIU_ID_FASCICOLO_SIUS, " + "EVE_ID_EVENTO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP_RIF, " + "SEN_ID_SENTENZA, " + "DATA_DECORRENZA, "
				+ "FL_FORMA_MISURA, " + "DESCRIZIONE_COMUNITA, " +
				// 10-12-2014
				"FLAG_ANNULLA_MISURA, DATA_FINE_VALIDITA, MIS_ID_MISURA_SICUREZZA, " +
				// 03-03-2015
				"IST_DET_ID_ISTITUTO_DETENZIONE, LUOGO_ESECUZIONE_MISURA";
		//
		lStatement += " FROM MISURA_SICUREZZA , CG_REF_CODES NATURA , CG_REF_CODES TIPOMISURA";
		lStatement += " WHERE   NATURA.RV_DOMAIN = 'NATURA_MISURA_SICUREZZA' AND NATURA.RV_LOW_VALUE = COD_NATURA";
		lStatement += " AND TIPOMISURA.RV_DOMAIN = 'TIPO_MISURA_SICUREZZA' AND TIPOMISURA.RV_LOW_VALUE = COD_TIPO";

		return lStatement;
	}

	protected String getSqlQueryEstesa() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_MISURA_SICUREZZA, " + "COD_NATURA,  NATURA.RV_MEANING DESCR_NATURA, "
				+ "COD_TIPO, TIPOMISURA.RV_MEANING DESCR_TIPO, TIPOMISURA.RV_HIGH_VALUE COD_OGGETTO_ESECUZIONE, "
				+ "NUM_ANNI, " + "NUM_MESI, " + "NUM_GIORNI, " + "ANNO_REG_38, " + "NUM_REG_38, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "MISURA_SICUREZZA.FAS_SIE_ID_FASCICOLO_SIEP, " + "FAS_SIU_ID_FASCICOLO_SIUS, "
				+ "EVE_ID_EVENTO, " + "CHIAVE_ANNO, " + "CHIAVE_PROGR, " + "FAS_SIE_ID_FASCICOLO_SIEP_RIF, "
				+ "SEN_ID_SENTENZA, " + "DATA_DECORRENZA, " + "FL_FORMA_MISURA, " + "DESCRIZIONE_COMUNITA ";
		lStatement += " FROM MISURA_SICUREZZA ";
		lStatement += " INNER JOIN CG_REF_CODES NATURA ON NATURA.RV_LOW_VALUE = COD_NATURA AND NATURA.RV_DOMAIN = 'NATURA_MISURA_SICUREZZA' ";
		lStatement += " INNER JOIN CG_REF_CODES TIPOMISURA ON TIPOMISURA.RV_LOW_VALUE = COD_TIPO AND TIPOMISURA.RV_DOMAIN = 'TIPO_MISURA_SICUREZZA' ";
		lStatement += " LEFT OUTER JOIN FASCICOLO_SIEP ON MISURA_SICUREZZA.FAS_SIE_ID_FASCICOLO_SIEP = FASCICOLO_SIEP.ID_FASCICOLO_SIEP ";

		return lStatement;
	}

	protected String getSqlQueryMisuraFascicoli() {
		String lStatement = new String("");

		lStatement += " SELECT" + " DISTINCT(FASCICOLO_SIEP.ID_FASCICOLO_SIEP),"
				+ " FASCICOLO_SIEP.CHIAVE_ANNO," + " FASCICOLO_SIEP.CHIAVE_PROGR,"
				+ " FASCICOLO_SIEP.FLAG_VALIDATO";
		lStatement += " FROM MISURA_SICUREZZA,";
		lStatement += " FASCICOLO_SIEP";
		lStatement += " WHERE MISURA_SICUREZZA.FAS_SIE_ID_FASCICOLO_SIEP = FASCICOLO_SIEP.ID_FASCICOLO_SIEP";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		MisuraSicurezzaModel aModel = new MisuraSicurezzaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdMisuraSicurezza(getBigDecimal("ID_MISURA_SICUREZZA"));
		aModel.setCodNatura(getString("COD_NATURA"));
		aModel.setDescrNatura(getString("DESCR_NATURA"));
		aModel.setCodTipo(getString("COD_TIPO"));
		aModel.setDescrTipo(getString("DESCR_TIPO"));
		aModel.setCodOggettoEsecuzione(getString("COD_OGGETTO_ESECUZIONE"));
		aModel.setNumAnni(getBigDecimal("NUM_ANNI"));
		aModel.setNumMesi(getBigDecimal("NUM_MESI"));
		aModel.setNumGiorni(getBigDecimal("NUM_GIORNI"));
		aModel.setAnnoReg38(getBigDecimal("ANNO_REG_38"));
		aModel.setNumReg38(getBigDecimal("NUM_REG_38"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setFasSieIdFascicoloSiepRif(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP_RIF"));
		aModel.setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA"));
		aModel.setDataDecorrenza(getDate("DATA_DECORRENZA"));
		aModel.setFlFormaMisura(getBigDecimal("FL_FORMA_MISURA"));
		aModel.setDescrizioneComunita(getString("DESCRIZIONE_COMUNITA"));
		// 12-12-2014
		aModel.setFlagAnnullaMisura(getString("FLAG_ANNULLA_MISURA"));
		aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
		aModel.setMisIdMisuraSicurezza(getBigDecimal("MIS_ID_MISURA_SICUREZZA"));
		// 03-03-2015
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
		aModel.setLuogoEsecuzioneMisura(getString("LUOGO_ESECUZIONE_MISURA"));

		return aModel;
	}

	public GenericModel getModelMisuraFascicoli() throws DAOException {
		MisuraSicurezzaModel aModel = new MisuraSicurezzaModel();

		aModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		aModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		aModel.setFlagValidato(getString("FLAG_VALIDATO"));

		return aModel;
	}

	public BigDecimal getMaxNumReg38(BigDecimal aAnnoReg38, String aUfficio) throws DAOException {
		String lStatement = new String();

		lStatement += "SELECT MAX(NUM_REG_38) aMAX";
		lStatement += " FROM MISURA_SICUREZZA";
		lStatement += " WHERE ANNO_REG_38 = " + aAnnoReg38;
		lStatement += " AND COD_UFFICIO_INSERIMENTO = '" + aUfficio + "'";

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
	 * La funzione setta la condizione di ricerca tra 3 possibili alternative: per ID_MISURA_SICUREZZA, per
	 * ID_FASCICOLO_SIEP, per ID_FAS_SIGE_SENTENZA.
	 *
	 * @param aModel
	 * @return
	 */
	public String setCondizione(MisuraSicurezzaModel aModel) {
		String lCondizioni = new String();
		if (aModel.getIdMisuraSicurezza() != null)
			lCondizioni = setCondizioniByKey(aModel.getIdMisuraSicurezza());
		else if (aModel.getIdFascicoloSiep() != null)
			lCondizioni = setCondizioniByIdFascicolo(aModel.getIdFascicoloSiep());
		else if (aModel.getFasSiuIdFascicoloSius() != null)
			lCondizioni = setCondizioniByIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
		else if (aModel instanceof MisuraSicurezzaSigeModel)
			lCondizioni = " AND ID_MISURA_SICUREZZA IN (SELECT MIS_ID_MISURA_SICUREZZA FROM MISURA_SICUREZZA_SENTENZA_SIGE WHERE FAS_SIGE_SEN_ID = "
					+ ((MisuraSicurezzaSigeModel) aModel).getFasSigeSenId() + ")";

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_MISURA_SICUREZZA = " + aKey;
	}

	public void ricercaMisuraSicurezzaByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicolo(aIdFascicolo);

		setStatement(lSql);
	}

	public void ricercaMisuraSicurezzaByIdEvento(BigDecimal aIdEvento) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdEvento(aIdEvento);

		setStatement(lSql);
	}

	public String setCondizioniByIdFascicolo(BigDecimal aIdFascicolo) {
		return " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo + " AND FAS_SIU_ID_FASCICOLO_SIUS IS NULL ";
	}

	public String setCondizioniByIdFascicoloSius(BigDecimal aIdFascicolo) {
		return " AND FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFascicolo;
	}

	public String setCondizioniByEveIdEvento(BigDecimal aEveIdEvento) {
		return " AND EVE_ID_EVENTO = " + aEveIdEvento;
	}

	public String setCondizioniRicercaMisuraEstesaByIdFascicoloSius(BigDecimal aIdFascicolo) // Eventuali
																								// altre
																								// condizioni
																								// da vedersi
																								// dopo
	{
		return " WHERE FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFascicolo;
	}

	public String setOrderByIns() {
		return " ORDER BY DATA_INSERIMENTO ";
	}

	public void ricercaMisuraSicurezzaByIdFascicoloSIUS(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicoloSIUS(aIdFascicolo);

		setStatement(lSql);
	}

	public String setCondizioniByIdFascicoloSIUS(BigDecimal aIdFascicolo) {
		return " AND FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFascicolo;
	}

	public String setCondizioniByIdEvento(BigDecimal aIdFascicolo) {
		return " AND EVE_ID_EVENTO = " + aIdFascicolo;
	}

	// 10-02-2014
	public void ricercaMisuraSicurezzaByIdFascicoloOrd(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicolo(aIdFascicolo);
		lSql += " AND (FLAG_ANNULLA_MISURA IS NULL OR FLAG_ANNULLA_MISURA != 'A') AND DATA_FINE_VALIDITA IS NULL";
		lSql += " ORDER BY ID_MISURA_SICUREZZA";
		setStatement(lSql);
	}

	// 17-12-2014
	public void ricercaTutteMisureSicurezzaByIdFascicoloOrd(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicolo(aIdFascicolo);
		// lSql +=
		// " AND (FLAG_ANNULLA_MISURA IS NULL OR FLAG_ANNULLA_MISURA != 'A') AND DATA_FINE_VALIDITA IS NULL";
		lSql += " ORDER BY ID_MISURA_SICUREZZA";
		setStatement(lSql);
	}

	// 15-12-2014 Ricerca per MIS_ID_MISURA
	public void ricercaMisuraSicurezzaByMisIdMisura(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND MIS_ID_MISURA_SICUREZZA = " + aKey;
		setStatement(lSql);
	}

	// 10/02/2015 Ricerca CodTipoMisura per ID_EVENTO
	public void ricercaCodTipoMisurabyIdEvento(BigDecimal aKey) throws DAOException {
		String lSql = " Select E.COD_ESITO, E_P.RV_ALT2_VALUE CodTipoMisura "
				+ " FROM EVENTO E, CG_REF_CODES E_P " + " WHERE E_P.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' "
				+ " AND E.COD_ESITO = E_P.RV_LOW_VALUE " + " AND E.ID_EVENTO = " + aKey;
		setStatement(lSql);
	}

	/**
	 * MEV_39: aggiunto metodo di ricerca
	 *
	 * @param idFascicoloSiep
	 * @throws DAOException
	 */
	public void ricercaMSNotificateByIdFascIdEve(BigDecimal idFascicoloSiep, String tipoRicerca,
			BigDecimal idEventoRestituzione) throws DAOException {

		String lSql = "select e.data_emissione," + " e.id_evento," + " mp.rv_meaning as MOTIVO_PROVVEDIMENTO,"
				+ " mp.rv_low_value as CODICE_MOTIVO_PROVVEDIMENTO,"

				+ " ta.rv_meaning as TIPO_AUTORITA,"
				// + " decode( ta.rv_meaning, '-', TI.RV_MEANING, ta.rv_meaning) as TIPO_AUTORITA, "
				+ " ta.rv_low_value as CODICE_TIPO_AUTORITA," + " tp.rv_meaning as TIPO_PROVVEDIMENTO,"
				+ " tp.rv_low_value as CODICE_TIPO_PROVVEDIMENTO,"
				+ " nvl(c.descrizione, d.descrizione) as DESCRIZIONE,"
				+ " nvl(c.cod_comune, d.cod_comune) AS COD_COMUNE,"
				+ " nvl(UPPER(n.note), d.indirizzo) AS INDIRIZZO," + " n.id_notifica,"
				+ " n.aut_est_id_autorita_esterna," + " d.id_istituto_detenzione" + " from evento e,"
				+ " notifica n," + " cg_ref_codes tp," + " cg_ref_codes mp," + " cg_ref_codes ta,"
				+ " autorita_esterna ae," + " comune c," + " istituto_detenzione d"
				+ " where e.id_evento = n.eve_id_evento" + " and e.cod_tipo_provvedimento in ('06', '12')"
				+ " and e.cod_motivo in ('1126', '1128', '1131')" + " and tp.rv_domain = 'TIPO_PROVVEDIMENTO'"
				+ " and tp.rv_low_value = e.cod_tipo_provvedimento"
				+ " and mp.rv_domain = 'MOTIVO_PROVVEDIMENTO'" + " and mp.rv_low_value = e.cod_motivo"
				+ " and ta.rv_domain = 'TIPO_AUTORITA'"
				+ " and nvl(ta.rv_low_value, '-') = nvl(ae.cod_tipo_autorita, '-')"
				+ " and c.cod_comune(+) = ae.cod_sede"
				+ " and ae.id_autorita_esterna(+) = n.aut_est_id_autorita_esterna"
				+ " and e.fas_sie_id_fascicolo_siep = '" + idFascicoloSiep + "'"
				// solo le notifiche relative ai destinatari per l'esecuzione
				// + " and n.cod_tipo_notifica = 'E'"
				+ " and d.id_istituto_detenzione(+) = n.ist_det_id_istituto_detenzione";

		// recupero solo quelle per cui non è stato ancora richiesta una RESTITUZIONE
		// lSql= lSql + " and e.eve_id_evento NOT IN ( (select ee.id_evento from evento ee, notifica nn "
		// + " where ee.id_evento=nn.eve_id_evento"
		// + " and ee.eve_id_evento=e.id_evento and nn.cod_tipo_notifica='R' "
		// + " and ee.flag_documento_registrato <> 'A'"
		// + " and ee.cod_motivo = '1149' ))" ;

		// trasferta TORINO del 07/10/2019 (eliminiamo LA IN CHE SEGUE PERCHè IN ANALISI NON è PREVISTO DI ESCLUDERE LE NOTIFICHE SE GIA' è STATO CHIESTO UNA RESTITUZIONE)!
//		lSql = lSql
//				+ " and n.id_notifica NOT in (select nn1149.id_notifica from notifica nn, evento ev, evento ee1149, notifica nn1149"
//				+ " where nn.eve_id_evento = ev.id_evento" + " and ee1149.id_evento = nn1149.eve_id_evento"
//				+ " and ee1149.eve_id_evento = ev.id_evento"
//				+ " and nn.aut_est_id_autorita_esterna = nn1149.aut_est_id_autorita_esterna"
//				+ " and nn1149.cod_tipo_notifica = 'R' and ee1149.flag_documento_registrato <> 'A' and ee1149.cod_motivo = '1149')";

		// if("M".equals(tipoRicerca) && idEventoRestituzione!= null){
		// lSql= lSql + " and e.id_evento= ' " +idEventoRestituzione + "'";
		// }
		// lSql = lSql + " AND TI.RV_DOMAIN='TIPO_ISTITUTO' and nvl(TI.rv_low_value, '-') =
		// nvl(d.cod_tipo_istituto, '-')";
		// emma intervento post collaudo 11.3 (aggiungo la condizione che segue in modo da non recuperare gli
		// eventi annullati)
		lSql = lSql + " AND e.flag_documento_registrato <> 'A' ";
		
		// trasferta TORINO del 07/10/2019 (eliminiamo da elenco le notifiche al difensore : es :UNEP)
		lSql = lSql + "  and n.cod_tipo_notifica not  in ('ND') ";
		lSql = lSql + " order by e.id_evento";

		setStatement(lSql);
	}

	/**
	 * MEV_39: aggiunto model per recupero info
	 *
	 * @return GenericModel
	 * @throws DAOException
	 */
	public GenericModel getModelMSNotificate() throws DAOException {

		MisuraSicurezzaNotificataModel msnm = new MisuraSicurezzaNotificataModel();
		msnm.setDataEmissione(getDate("data_emissione"));
		msnm.setDescrTipoProvvedimento(getString("TIPO_PROVVEDIMENTO"));
		msnm.setDescrMotivoProvvedimento(getString("MOTIVO_PROVVEDIMENTO"));
		msnm.setDescrTipoAutorita(getString("TIPO_AUTORITA"));
		msnm.setDescrComune(getString("DESCRIZIONE"));
		msnm.setCodTipoProvvedimento(getString("CODICE_TIPO_PROVVEDIMENTO"));
		msnm.setCodMotivoProvvedimento(getString("CODICE_MOTIVO_PROVVEDIMENTO"));
		msnm.setCodTipoAutorita(getString("CODICE_TIPO_AUTORITA"));
		msnm.setCodComune(getString("COD_COMUNE"));
		msnm.setAutEstIdAutoritaEsterna(getBigDecimal("aut_est_id_autorita_esterna"));
		msnm.setIdNotifica(getBigDecimal("id_notifica"));
		msnm.setIdEvento(getBigDecimal("id_evento"));
		msnm.setNote(getString("INDIRIZZO"));
		msnm.setIdIstitutoDetenzione(getString("id_istituto_detenzione"));

		// valore di ritorno
		return msnm;
	}

} // Chiude SqlDAO