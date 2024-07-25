package siap.sius.depositoordinanzapc.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * DepositoOrdinanzaPcSqlDAO - Classe SqlDAO che rappresenta la tabella DepositoOrdinanzaPc
 *
 * @version 1.0
 */
public class DepositoOrdinanzaPcSqlDAO extends SIAPSqlDAO {

	public DepositoOrdinanzaPcSqlDAO(Connection con) {

		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaDepositoOrdinanzaPc(DepositoOrdinanzaPcModel aModel) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaDepositoOrdinanzaPcByS3(DepositoOrdinanzaPcModel aModel) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizioneByS3(aModel);
		setStatement(lSql);
	}

	/**
	 * Metodo di ricerca DepositoOrdinanzaPc per Generale Procedimento
	 *
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaDepositoOrdinanzaPcByGenProcedimento(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " AND GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
		lSql += " ORDER BY ID_DEPOSITO_ORDINANZA_PC DESC";
		setStatement(lSql);
	}

	public void ricercaDepositoOrdinanzaPcByGenProcedimento(BigDecimal aKey, String aCodTipoOrdinanza)
			throws DAOException {

		String lSql = getSqlQuery0();
		lSql += " WHERE GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
		// MEV_39: aggiunta casisitica
		String all = "('22','37','38','39','40','41','42','MS','TM')";
		if ("ALL".equals(aCodTipoOrdinanza))
			lSql += " AND COD_TIPO_ORDINANZA IN " + all;
		else
			lSql += " AND COD_TIPO_ORDINANZA = '" + aCodTipoOrdinanza + "'";
		lSql += " ORDER BY ID_DEPOSITO_ORDINANZA_PC DESC";
		setStatement(lSql);
	}

	public void ricercaDepositoOrdinanzaPcByIdEveGenerato(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery0();

		lSql += " WHERE ID_EVENTO_GENERATO = " + aKey;
		setStatement(lSql);
	}

	// - - > 01/2014 MIS SIC
	public void ricercaDepositoOrdinanzaPcByFascicoloSiepIdEveGenerato(BigDecimal aKey) throws DAOException {

		String lSql = "";
		lSql += "SELECT distinct (FASC.ID_FASCICOLO_SIUS), FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR, "
				+ "FASC.CHIAVE_UFFICIO,";
		lSql += " EVE.COD_TIPO_PROVVEDIMENTO, EVE.COD_MOTIVO, EVE.DATA_EMISSIONE, EVE.ID_EVENTO, "
				+ "EVE.COD_ESITO,";
		lSql += " TEN.COD_OGGETTO_TENORE, TEN.COD_ESITO_TENORE, DEPO.ANNO_S3, DEPO.NUM_S3, "
				+ "DEPO.FLAG_ELABORATO,";
		lSql += " UFD.DESCR_TIPO_UFFICIO DESCR_TIPO_UF, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lSql += " MOTIVO_PROVVEDIMENTO.RV_MEANING OGGETTO,";
		// 28/01/2015 lSql+=" ESITO_TENORE.RV_MEANING ESITO";
		lSql += " ESITO_PROVVEDIMENTO.RV_MEANING ESITO,";
		lSql += " ESITO_TENORE.RV_ABBREVIATION COD_ESITO";
		lSql += " FROM FASCICOLO_SIUS FASC, EVENTO EVE, TENORE TEN, DEPOSITO_ORDINANZA_PC DEPO,";
		lSql += " UFFICIO UFF, COMUNE DESCR_COM_UFF, UFFICIO_DESCR UFD,";
		lSql += " CG_REF_CODES MOTIVO_PROVVEDIMENTO, CG_REF_CODES ESITO_TENORE";
		lSql += ", CG_REF_CODES ESITO_PROVVEDIMENTO"; // 28/01/2015
		lSql += " WHERE FASC.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND EVE.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		// lSql+=" AND (DEPO.FLAG_ELABORATO is null or (DEPO.FLAG_ELABORATO is not null AND
		// DEPO.FLAG_ELABORATO <>'S')) ";
		lSql += " AND EVE.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		// lSql+=" AND EVE.COD_TIPO_PROVVEDIMENTO = '03' AND EVE.COD_MOTIVO IN ('2110','2114')";
		lSql += " AND EVE.FLAG_DOCUMENTO_REGISTRATO = 'S'";
		lSql += " AND EVE.COD_MOTIVO = TEN.COD_OGGETTO_TENORE";
		lSql += " AND TEN.DEP_OPID_DEPOSITO_ORDINANZA_PC = DEPO.ID_DEPOSITO_ORDINANZA_PC";
		lSql += " AND EVE.ID_EVENTO = DEPO.ID_EVENTO_GENERATO";
		lSql += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lSql += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lSql += " AND UFF.COD_UFFICIO = UFD.COD_UFFICIO";
		lSql += " AND MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'";
		lSql += " AND MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE = TEN.COD_OGGETTO_TENORE";
		// 18/11/2014
		// lSql+=" AND ESITO_TENORE.RV_DOMAIN = 'ESITO_TENORE' AND ESITO_TENORE.RV_HIGH_VALUE = 'U023'";
		lSql += " AND ESITO_TENORE.RV_DOMAIN = 'ESITO_TENORE'";
		lSql += " AND ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'"; // 28/01/2015
		lSql += " AND ESITO_PROVVEDIMENTO.RV_LOW_VALUE = ESITO_TENORE.RV_ABBREVIATION"; // 28/01/2015
		// 28/01/2015
		// lSql+=" AND ESITO_TENORE.RV_HIGH_VALUE IN ('U023', 'U088','U064',
		// 'U066','U095','U096','C036','U093')";
		lSql += " AND ESITO_TENORE.RV_HIGH_VALUE IN ('U023','U088','U064','U066','U067','U077','U082',"
				+ "'U086','U089','U091','U095','U096','C029','C036','U093')";
		lSql += " AND ESITO_TENORE.RV_ABBREVIATION = TEN.COD_ESITO_TENORE";
		lSql += " ORDER BY FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR";

		setStatement(lSql);
	}

	// - - > 06/2014 MIS SIC
	public void RicercaEventoProvvediementiArchiviazioneSIUSByFascicoloSiep(BigDecimal aKey)
			throws DAOException {

		String lSql = "";
		lSql += "SELECT distinct (FASC.ID_FASCICOLO_SIUS), FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR, "
				+ "FASC.CHIAVE_UFFICIO,";
		lSql += " EVE.COD_TIPO_PROVVEDIMENTO, EVE.COD_MOTIVO, EVE.DATA_EMISSIONE, EVE.ID_EVENTO, "
				+ "EVE.COD_ESITO,";
		lSql += " EVE.ANNO_PROTOCOLLO, EVE.PROGR_PROTOCOLLO, EVE.DATA_RICEZIONE_ATTI,";
		lSql += " UFD.DESCR_TIPO_UFFICIO DESCR_TIPO_UF, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lSql += " MOTIVO_PROVVEDIMENTO.RV_MEANING OGGETTO,";
		lSql += " ESITO_PROVVEDIMENTO.RV_MEANING ESITO,";
		lSql += " ESITO_PROVV_ALT3.RV_ALT3_VALUE ESITO_ALT3,";
		lSql += " TIPO_PROVVEDIMENTO.RV_MEANING PROVVEDIMENTO";
		lSql += " FROM FASCICOLO_SIUS FASC, EVENTO EVE,";
		lSql += " UFFICIO UFF, COMUNE DESCR_COM_UFF, UFFICIO_DESCR UFD,";
		lSql += " CG_REF_CODES MOTIVO_PROVVEDIMENTO, CG_REF_CODES ESITO_PROVVEDIMENTO, "
				+ "CG_REF_CODES TIPO_PROVVEDIMENTO";
		lSql += ", CG_REF_CODES ESITO_PROVV_ALT3";
		lSql += " WHERE FASC.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND EVE.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND EVE.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		lSql += " AND EVE.COD_TIPO_PROVVEDIMENTO IN ('02','03')";
		// lSql+=" AND EVE.COD_MOTIVO IN ('2440','2441','2110','2111','2113','2670')";
		// lSql+=" AND EVE.COD_MOTIVO IN ('2440','2441','2110','2111','2113','2114','2670')";

		// MEV 39: APPELLO CONTRO: OCCORRE CONSIDERARE ANCHE MOTIVO 0258, 0259 E 0260
		// Ticket#20220404012 - Si aggiungono anche i codici 9073 e 9074 usati da UDSM
		// lSql += " AND EVE.COD_MOTIVO IN ('2440','2441','2110','2111','2113','2114','2670','2697','2698',
		// '2700','2701', '0258','0259','0260')";
		lSql += " AND EVE.COD_MOTIVO IN ('2440','2441','2110','2111','2113','2114','2670','2697','2698',"
				+ "'2700','2701', '0258','0259','0260', '9073','9074')";
		//Ticket#20220404012 - FINE
		// lSql+=" AND EVE.COD_ESITO IN ('0052','0054','0118','0193','0194','0195','0350','0351','0387')";
		// lSql+=" AND EVE.COD_ESITO IN ('0052','0193','0194','0195','0350','0351','0387')";

		// MEV 39: APPELLO CONTRO: OCCORRE CONSIDERARE ANCHE ESITO 0209 (Accoglie Appello e Revoca
		// Provvedimento Mds)
		
		// Ticket#20220404012 - Si aggiunge anche il codice 0054 - Dichiara cessata la pericolosità sociale
		// e revoca la misura
		// lSql += " AND EVE.COD_ESITO IN ('0052','0193','0194','0195','0350','0351','0387','0324','0329','0209')";
		lSql += " AND EVE.COD_ESITO IN ('0052','0193','0194','0195','0350','0351','0387','0324','0329',"
				+ "'0209','0054')";
		//Ticket#20220404012 - FINE
		lSql += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lSql += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lSql += " AND UFF.COD_UFFICIO = UFD.COD_UFFICIO";
		lSql += " AND TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'";
		lSql += " AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_TIPO_PROVVEDIMENTO";
		lSql += " AND MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'";
		lSql += " AND MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_MOTIVO";
		lSql += " AND ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'";
		lSql += " AND ESITO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_ESITO";
		lSql += " AND ESITO_PROVV_ALT3.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'";
		lSql += " AND ESITO_PROVV_ALT3.RV_LOW_VALUE = EVE.COD_ESITO";
		lSql += " ORDER BY FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR";

		setStatement(lSql);
	}

	// METODO GETMODEL() per ricercaDepositoOrdinanzaPcByFascicoloSiepIdEveGenerato
	public GenericModel getModelEsitoMisSic() throws DAOException {

		OrdinanzaEventoTenoriFascicoloSiusModel aModel = new OrdinanzaEventoTenoriFascicoloSiusModel();

		FascicoloSiusModel lFas = new FascicoloSiusModel();
		lFas.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFas.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFas.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFas.setChiaveUfficio(getString("CHIAVE_UFFICIO"));

		aModel.setDescrTipoUfficio(getString("DESCR_TIPO_UF"));
		aModel.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));

		EventoModel lEve = new EventoModel();
		lEve.setIdEvento(getBigDecimal("ID_EVENTO"));
		lEve.setCodMotivo(getString("COD_MOTIVO"));
		lEve.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lEve.setDataEmissione(getDate("DATA_EMISSIONE"));
		lEve.setCodEsito(getString("COD_ESITO"));

		TenoreModel lTen = new TenoreModel();
		aModel.setDescrOggetto(getString("OGGETTO"));
		aModel.setDescrEsito(getString("ESITO"));
		lTen.setCodOggettoTenore(getString("COD_OGGETTO_TENORE"));
		lTen.setCodEsitoTenore(getString("COD_ESITO_TENORE"));

		DepositoOrdinanzaPcModel lDep = new DepositoOrdinanzaPcModel();
		lDep.setAnnoS3(getBigDecimal("ANNO_S3"));
		lDep.setNumS3(getBigDecimal("NUM_S3"));

		aModel.setFascicoloSius(lFas);
		aModel.setEvento(lEve);
		aModel.setTenore(lTen);
		aModel.setOrdinanza(lDep);

		aModel.getOrdinanza().setFlagElaborato(getString("FLAG_ELABORATO")); // 21/11/2014

		return aModel;
	}

	// METODO GETMODEL() per ricercaDepositoOrdinanzaPcByFascicoloSiepIdEveGenerato
	public GenericModel getModelEsitoArchMisSic() throws DAOException {

		OrdinanzaEventoTenoriFascicoloSiusModel aModel = new OrdinanzaEventoTenoriFascicoloSiusModel();

		FascicoloSiusModel lFas = new FascicoloSiusModel();
		lFas.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFas.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFas.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFas.setChiaveUfficio(getString("CHIAVE_UFFICIO"));

		aModel.setDescrTipoUfficio(getString("DESCR_TIPO_UF"));
		aModel.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));

		EventoModel lEve = new EventoModel();
		lEve.setIdEvento(getBigDecimal("ID_EVENTO"));
		lEve.setCodMotivo(getString("COD_MOTIVO"));
		lEve.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lEve.setDataEmissione(getDate("DATA_EMISSIONE"));
		lEve.setCodEsito(getString("COD_ESITO"));
		lEve.setAnnoProtocollo(getBigDecimal("ANNO_PROTOCOLLO"));
		lEve.setProgrProtocollo(getBigDecimal("PROGR_PROTOCOLLO"));
		lEve.setDataRicezioneAtti(getDate("DATA_RICEZIONE_ATTI"));

		aModel.setDescrOggetto(getString("OGGETTO"));
		aModel.setDescrEsito(getString("ESITO"));
		aModel.setDescrProvvedimento(getString("PROVVEDIMENTO"));
		aModel.setCodEsitoAlt3(getString("ESITO_ALT3"));

		aModel.setFascicoloSius(lFas);
		aModel.setEvento(lEve);

		return aModel;
	}

	/*
	 * ISSUE MEV : Aggiunto metodo per impostare il nuovo model 
	 * Numero MEV : 39 
	 * Autore : Gioggi 
	 * Data : 03/mar/2017 
	 * Branch : MEV_39
	 */
	public GenericModel getModelEsitoDiffMisSic() throws DAOException {

		OrdinanzaEventoTenoriFascicoloSiusModel aModel = new OrdinanzaEventoTenoriFascicoloSiusModel();

		FascicoloSiusModel lFas = new FascicoloSiusModel();
		lFas.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFas.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFas.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFas.setChiaveUfficio(getString("CHIAVE_UFFICIO"));

		aModel.setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		aModel.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));

		EventoModel lEve = new EventoModel();
		lEve.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lEve.setCodMotivo(getString("COD_MOTIVO"));
		lEve.setDataEmissione(getDate("DATA_EMISSIONE"));
		lEve.setIdEvento(getBigDecimal("ID_EVENTO"));
		lEve.setCodEsito(getString("COD_ESITO"));
		lEve.setAnnoProtocollo(getBigDecimal("ANNO_PROTOCOLLO"));
		lEve.setProgrProtocollo(getBigDecimal("PROGR_PROTOCOLLO"));
		lEve.setCodTipoUfficioEmittente(getString("COD_TIPO_UFFICIO"));
		lEve.setCodLuogoEmittente(getString("COD_UFFICIO"));

		aModel.setDescrOggetto(getString("DESCR_OGGETTO"));
		aModel.setDescrEsito(getString("DESCR_ESITO"));
		aModel.setDescrProvvedimento(getString("DESCR_PROVVEDIMENTO"));

		DepositoOrdinanzaPcModel dopm = new DepositoOrdinanzaPcModel();
		dopm.setDataInizioPeriodo(getDate("DATA_INIZIO_PERIODO"));
		dopm.setDataFineMisura(getDate("DATA_FINE_MISURA"));
		dopm.setSospensioneAASS(getBigDecimal("SOSPENSIONE_AA"));
		dopm.setSospensioneMMSS(getBigDecimal("SOSPENSIONE_MM"));
		dopm.setSospensioneGGSS(getBigDecimal("SOSPENSIONE_GG"));
		dopm.setLuogoSvolgimentoProva(getString("LUOGO_SVOLGIMENTO_PROVA"));

		MisuraAlternativaModel mam = new MisuraAlternativaModel();
		mam.setCodTipoUfficioScarcerazione(getString("COD_TIPO_UFFICIO_SCARCERAZIONE"));
		mam.setDataScarcerazione(getDate("DATA_SCARCERAZIONE"));
		mam.setFlagDecisioneTribunale(getString("FLAG_DECISIONE_TRIBUNALE"));
		mam.setIdMisuraAlternativa(getBigDecimal("ID_MISURA_ALTERNATIVA"));
		mam.setDataInizioMisura(getDate("DATA_INIZIO_MISURA"));
		mam.setDataFineMisura(getDate("DATA_FINE_MISURA"));

		aModel.setFascicoloSius(lFas);
		aModel.setEvento(lEve);
		aModel.setOrdinanza(dopm);
		aModel.setMisuraAlternativa(mam);

		// valore di ritorno
		return aModel;
	}
	// ***** FINE INTERVENTO MEV_39 *****//
	// END MIS SIC

	public void ricercaDepositoOrdinanzaCssaUssmPcByIdEveGenerato(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQueryCssaUssm();

		lSql += " WHERE ID_EVENTO_GENERATO = " + aKey;
		setStatement(lSql);
	}

	public void ricercaDepositoOrdinanzaPcByKey(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaOrdinanzaRimessioneAttiByKeyPerUpdate(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery1();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {

		String lStatement = new String("");

		lStatement += "SELECT ";
		lStatement += "ID_DEPOSITO_ORDINANZA_PC, ";
		lStatement += "ANNO_S3, ";
		lStatement += "NUM_S3, ";
		lStatement += "OGGETTO_PROCEDIMENTO, ";
		lStatement += "DATA_UDIENZA, ";
		lStatement += "DATA_CAMERA_CONSIGLIO, ";
		lStatement += "DATA_DEPOSITO, ";
		lStatement += "COD_NATURA_PROVVEDIMENTO, ";
		lStatement += "ID_CSSA_COMP, CSSA.COMUNE COMCSSADESC, ";
		lStatement += "COD_UFFICIO_MAGISTRATO_COMP, COM_UFF_MAG.DESCRIZIONE DESCCOMMAG, ";
		lStatement += "LUOGO_SVOLGIMENTO_PROVA, ";
		lStatement += "SERVIZIO_TERAPEUTICO_COMP, ";
		lStatement += "NUM_GIORNI_DETENZIONE_DOM, ";
		lStatement += "NUM_MESI_DETENZIONE_DOM, ";
		lStatement += "NUM_ANNI_DETENZIONE_DOM, ";
		lStatement += "NUM_GIORNI_PERMESSO_ACCORDATI, ";
		lStatement += "NUM_GIORNI_RIDUZIONE_PENA, ";
		lStatement += "NUM_GIORNI_RIDUZIONE_USUFRUITI, ";
		lStatement += "NVL(COD_UFF_TDS_CONCESSO_RIDUZIONE, '-') COD_UFF_TDS_CONCESSO_RIDUZIONE, ";
		lStatement += "COD_OPERATORE_INSERIMENTO, ";
		lStatement += "DATA_INSERIMENTO, ";
		lStatement += "COD_UFFICIO_INSERIMENTO, ";
		lStatement += "COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += "COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += "DATA_AGGIORNAMENTO, ";
		lStatement += "GEN_PRID_GENERALE_PROCEDIMENTO, ";
		lStatement += "COD_MAGISTRATO, ";
		lStatement += "ID_EVENTO_GENERATO, ";
		lStatement += "COM_UFF_TDS.DESCRIZIONE DESCRUFFTDSCONCRID, ";
		lStatement += "NUM_GIORNI_LIBANTICIPATA, ";
		lStatement += "FLAG_ELABORATO, ";
		lStatement += "COD_TIPO_ORDINANZA, ";
		// Nuovi campi 9-6-2004
		lStatement += "DATA_FINE_MISURA, ";
		lStatement += "DATA_DECORRENZA, ";
		lStatement += "DATA_INIZIO_PERIODO, ";
		lStatement += "FLAG_ESISTENZA_REATOOSTATIVO, ";
		lStatement += "FLAG_ESPIAZIONE_REATOOSTATIVO, ";
		lStatement += "AUTORITA_VIGILANTE, ";
		lStatement += "DATA_TRASMISSIONE, ";
		lStatement += "DATA_COMP_FOGLIO_COMPLEMENTARE, ";
		lStatement += "NUM_GIORNI_ARRESTO_REV, ";
		lStatement += "NUM_MESI_ARRESTO_REV, ";
		lStatement += "NUM_ANNI_ARRESTO_REV, ";
		lStatement += "ULTERIORE_DESCRIZIONE, ";
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		lStatement += "DATA_SOSPENSIONE_SS, ";
		lStatement += "GIORNI_RECUPERO_SS, ";
		lStatement += "FLAG_RECUPERO_SS, ";
		lStatement += "DATA_SCADENZA_SOSPENSIONE_SS, ";
		lStatement += "SOSPENSIONE_GG, ";
		lStatement += "SOSPENSIONE_MM, ";
		lStatement += "SOSPENSIONE_AA";
		// nuovi campi richiesta ottemperanza
		lStatement += ", TIPO_CONTROLLO_ESECUZIONE";
		lStatement += ", null as DESC_TIPO_CONTROLLO_ESECUZIONE";
		lStatement += ", FLAG_NOMINA_COMM_ACTA";
		lStatement += ", DESCR_COMM_ACTA";
		// 10102014 - DL 92 2014 Violazione CEDU
		lStatement += ", SOMMA_RISARC_DANNI";
		lStatement += ", COD_USSM";
		// MEV_2023-35
		lStatement += ", COD_TIPO_SANZIONE";
		lStatement += ", COD_TIPO_PENA_ACCESSORIA, DURATA, NUM_ANNI, NUM_MESI, NUM_GIORNI";
		lStatement += ", TIPOPENA.RV_MEANING DESCRPENA, TIPODURATAPENA.RV_MEANING DESCRDURATA ";
		lStatement += "FROM DEPOSITO_ORDINANZA_PC, CSSA, cg_ref_codes UFF_TIPO_MAG, ";
		lStatement += "COMUNE COM_UFF_MAG, UFFICIO UFFI_MAG, ";
		lStatement += "COMUNE COM_UFF_TDS, UFFICIO UFFI_TDS, ";
		// MEV_2023-35
		lStatement += "CG_REF_CODES TIPOPENA, CG_REF_CODES TIPODURATAPENA ";
		lStatement += "WHERE CSSA.ID_CSSA = NVL('9999',ID_CSSA_COMP) ";
		lStatement += "AND UFF_TIPO_MAG.RV_LOW_VALUE = UFFI_MAG.COD_TIPO_UFFICIO ";
		lStatement += "AND UFFI_MAG.COD_UFFICIO = COD_UFFICIO_MAGISTRATO_COMP "
				+ "AND UFF_TIPO_MAG.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += "AND COM_UFF_MAG.COD_COMUNE = UFFI_MAG.COD_COMUNE ";
		lStatement += "AND UFFI_TDS.COD_UFFICIO = NVL('-',COD_UFF_TDS_CONCESSO_RIDUZIONE) ";
		lStatement += "AND COM_UFF_TDS.COD_COMUNE = UFFI_TDS.COD_COMUNE ";
		// MEV_2023-35
		lStatement += "AND TIPOPENA.RV_DOMAIN = 'TIPO_PENA_ACCESSORIA' ";
		lStatement += "AND TIPOPENA.RV_LOW_VALUE = COD_TIPO_PENA_ACCESSORIA ";
		lStatement += "AND TIPODURATAPENA.RV_DOMAIN = 'TIPO_DURATA' ";
		lStatement += "AND TIPODURATAPENA.RV_LOW_VALUE = DURATA";

		return lStatement;
	}

	protected String getSqlQuery0() {

		String lStatement = new String("");

		lStatement += "SELECT ";
		lStatement += "ID_DEPOSITO_ORDINANZA_PC, ";
		lStatement += "ANNO_S3, ";
		lStatement += "NUM_S3, ";
		lStatement += "OGGETTO_PROCEDIMENTO, ";
		lStatement += "DATA_UDIENZA, ";
		lStatement += "DATA_CAMERA_CONSIGLIO, ";
		lStatement += "DATA_DEPOSITO, ";
		lStatement += "COD_NATURA_PROVVEDIMENTO, ";
		lStatement += "ID_CSSA_COMP, COD_USSM, CSSA.COMUNE COMCSSADESC, ";
		lStatement += "COD_UFFICIO_MAGISTRATO_COMP, '' DESCCOMMAG, ";
		lStatement += "LUOGO_SVOLGIMENTO_PROVA, ";
		lStatement += "SERVIZIO_TERAPEUTICO_COMP, ";
		lStatement += "NUM_GIORNI_DETENZIONE_DOM, ";
		lStatement += "NUM_MESI_DETENZIONE_DOM, ";
		lStatement += "NUM_ANNI_DETENZIONE_DOM, ";
		lStatement += "NUM_GIORNI_PERMESSO_ACCORDATI, ";
		lStatement += "NUM_GIORNI_RIDUZIONE_PENA, ";
		lStatement += "NUM_GIORNI_RIDUZIONE_USUFRUITI, ";
		lStatement += "COD_UFF_TDS_CONCESSO_RIDUZIONE, '' DESCRUFFTDSCONCRID, ";
		lStatement += "COD_OPERATORE_INSERIMENTO, ";
		lStatement += "DATA_INSERIMENTO, ";
		lStatement += "COD_UFFICIO_INSERIMENTO, ";
		lStatement += "COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += "COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += "DATA_AGGIORNAMENTO, ";
		lStatement += "GEN_PRID_GENERALE_PROCEDIMENTO, ";
		lStatement += "COD_MAGISTRATO, ";
		lStatement += "ID_EVENTO_GENERATO, ";
		lStatement += "NUM_GIORNI_LIBANTICIPATA, ";
		lStatement += "FLAG_ELABORATO, ";
		lStatement += "COD_TIPO_ORDINANZA, ";
		// Nuovi campi 9-6-2004
		lStatement += "DATA_FINE_MISURA, ";
		lStatement += "DATA_DECORRENZA, ";
		lStatement += "DATA_INIZIO_PERIODO, ";
		lStatement += "FLAG_ESISTENZA_REATOOSTATIVO, ";
		lStatement += "FLAG_ESPIAZIONE_REATOOSTATIVO, ";
		lStatement += "AUTORITA_VIGILANTE, ";
		lStatement += "DATA_TRASMISSIONE, ";
		lStatement += "DATA_COMP_FOGLIO_COMPLEMENTARE, ";
		lStatement += "NUM_GIORNI_ARRESTO_REV, ";
		lStatement += "NUM_MESI_ARRESTO_REV, ";
		lStatement += "NUM_ANNI_ARRESTO_REV, ";
		lStatement += "ULTERIORE_DESCRIZIONE, ";
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		lStatement += "DATA_SOSPENSIONE_SS, ";
		lStatement += "GIORNI_RECUPERO_SS, ";
		lStatement += "FLAG_RECUPERO_SS, ";
		lStatement += "DATA_SCADENZA_SOSPENSIONE_SS, ";
		lStatement += "SOSPENSIONE_GG, ";
		lStatement += "SOSPENSIONE_MM, ";
		lStatement += "SOSPENSIONE_AA";
		// nuovi campi richiesta ottemperanza
		lStatement += ", FLAG_NOMINA_COMM_ACTA";
		lStatement += ", DESCR_COMM_ACTA";
		lStatement += ", TIPO_CONTROLLO_ESECUZIONE, "
				+ "CODTIPOCONTROLLOESECUZIONE.RV_MEANING AS DESC_TIPO_CONTROLLO_ESECUZIONE";
		// 10102014 - DL 92 2014 Violazione CEDU
		lStatement += ", SOMMA_RISARC_DANNI";
		lStatement += ", COD_USSM";
		// MEV_2023-35
		lStatement += ", COD_TIPO_SANZIONE";
		lStatement += ", COD_TIPO_PENA_ACCESSORIA, DURATA, NUM_ANNI, NUM_MESI, NUM_GIORNI";
		lStatement += ", TIPOPENA.RV_MEANING DESCRPENA, TIPODURATAPENA.RV_MEANING DESCRDURATA ";
		lStatement += "FROM DEPOSITO_ORDINANZA_PC LEFT OUTER JOIN CSSA ON CSSA.ID_CSSA = ID_CSSA_COMP ";
		lStatement += "LEFT OUTER JOIN CG_REF_CODES CODTIPOCONTROLLOESECUZIONE "
				+ " ON (CODTIPOCONTROLLOESECUZIONE.RV_LOW_VALUE = TIPO_CONTROLLO_ESECUZIONE "
				+ "	AND CODTIPOCONTROLLOESECUZIONE.RV_DOMAIN = 'TIPO_CONTROLLO_ESECUZIONE') ";
		// MEV_2023-35
		lStatement += "LEFT OUTER JOIN CG_REF_CODES TIPOPENA ";
		lStatement += "ON (TIPOPENA.RV_DOMAIN = 'TIPO_PENA_ACCESSORIA' ";
		lStatement += "AND TIPOPENA.RV_LOW_VALUE = COD_TIPO_PENA_ACCESSORIA) ";
		lStatement += "LEFT OUTER JOIN CG_REF_CODES TIPODURATAPENA ";
		lStatement += "ON (TIPODURATAPENA.RV_DOMAIN = 'TIPO_DURATA' ";
		lStatement += "AND TIPODURATAPENA.RV_LOW_VALUE = DURATA)";

		return lStatement;
	}

	protected String getSqlQueryCssaUssm() {

		String lStatement = new String("");

		lStatement += "SELECT ";
		lStatement += "ID_DEPOSITO_ORDINANZA_PC, ";
		lStatement += "ANNO_S3, ";
		lStatement += "NUM_S3, ";
		lStatement += "OGGETTO_PROCEDIMENTO, ";
		lStatement += "DATA_UDIENZA, ";
		lStatement += "DATA_CAMERA_CONSIGLIO, ";
		lStatement += "DATA_DEPOSITO, ";
		lStatement += "COD_NATURA_PROVVEDIMENTO, ";
		lStatement += "ID_CSSA_COMP, COD_USSM, CSSA_CSSA.COMUNE COMCSSADESC, CSSA_USSM.COMUNE COMUSSMDESC, ";
		lStatement += "COD_UFFICIO_MAGISTRATO_COMP, '' DESCCOMMAG, ";
		lStatement += "LUOGO_SVOLGIMENTO_PROVA, ";
		lStatement += "SERVIZIO_TERAPEUTICO_COMP, ";
		lStatement += "NUM_GIORNI_DETENZIONE_DOM, ";
		lStatement += "NUM_MESI_DETENZIONE_DOM, ";
		lStatement += "NUM_ANNI_DETENZIONE_DOM, ";
		lStatement += "NUM_GIORNI_PERMESSO_ACCORDATI, ";
		lStatement += "NUM_GIORNI_RIDUZIONE_PENA, ";
		lStatement += "NUM_GIORNI_RIDUZIONE_USUFRUITI, ";
		lStatement += "COD_UFF_TDS_CONCESSO_RIDUZIONE, '' DESCRUFFTDSCONCRID, ";
		lStatement += "COD_OPERATORE_INSERIMENTO, ";
		lStatement += "DATA_INSERIMENTO, ";
		lStatement += "COD_UFFICIO_INSERIMENTO, ";
		lStatement += "COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += "COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += "DATA_AGGIORNAMENTO, ";
		lStatement += "GEN_PRID_GENERALE_PROCEDIMENTO, ";
		lStatement += "COD_MAGISTRATO, ";
		lStatement += "ID_EVENTO_GENERATO, ";
		lStatement += "NUM_GIORNI_LIBANTICIPATA, ";
		lStatement += "FLAG_ELABORATO, ";
		lStatement += "COD_TIPO_ORDINANZA, ";
		lStatement += "DATA_FINE_MISURA, ";
		lStatement += "DATA_DECORRENZA, ";
		lStatement += "DATA_INIZIO_PERIODO, ";
		lStatement += "FLAG_ESISTENZA_REATOOSTATIVO, ";
		lStatement += "FLAG_ESPIAZIONE_REATOOSTATIVO, ";
		lStatement += "AUTORITA_VIGILANTE, ";
		lStatement += "DATA_TRASMISSIONE, ";
		lStatement += "DATA_COMP_FOGLIO_COMPLEMENTARE, ";
		lStatement += "NUM_GIORNI_ARRESTO_REV, ";
		lStatement += "NUM_MESI_ARRESTO_REV, ";
		lStatement += "NUM_ANNI_ARRESTO_REV, ";
		lStatement += "ULTERIORE_DESCRIZIONE, ";
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		lStatement += "DATA_SOSPENSIONE_SS, ";
		lStatement += "GIORNI_RECUPERO_SS, ";
		lStatement += "FLAG_RECUPERO_SS, ";
		lStatement += "DATA_SCADENZA_SOSPENSIONE_SS, ";
		lStatement += "SOSPENSIONE_GG, ";
		lStatement += "SOSPENSIONE_MM, ";
		lStatement += "SOSPENSIONE_AA";
		// nuovi campi richiesta ottemperanza
		lStatement += ", FLAG_NOMINA_COMM_ACTA";
		lStatement += ", DESCR_COMM_ACTA";
		lStatement += ", TIPO_CONTROLLO_ESECUZIONE, "
				+ "CODTIPOCONTROLLOESECUZIONE.RV_MEANING AS DESC_TIPO_CONTROLLO_ESECUZIONE";
		lStatement += ", SOMMA_RISARC_DANNI";
		// MEV_2023-35
		lStatement += ", COD_TIPO_SANZIONE";
		lStatement += ", COD_TIPO_PENA_ACCESSORIA, DURATA, NUM_ANNI, NUM_MESI, NUM_GIORNI";
		lStatement += ", TIPOPENA.RV_MEANING DESCRPENA, TIPODURATAPENA.RV_MEANING DESCRDURATA ";
		lStatement += "FROM DEPOSITO_ORDINANZA_PC "
				+ "LEFT OUTER JOIN CSSA CSSA_CSSA ON CSSA_CSSA.ID_CSSA = ID_CSSA_COMP ";
		lStatement += "LEFT OUTER JOIN CSSA CSSA_USSM ON CSSA_USSM.ID_CSSA = DEPOSITO_ORDINANZA_PC.COD_USSM ";
		lStatement += "LEFT OUTER JOIN CG_REF_CODES CODTIPOCONTROLLOESECUZIONE "
				+ "ON (CODTIPOCONTROLLOESECUZIONE.RV_LOW_VALUE = TIPO_CONTROLLO_ESECUZIONE "
				+ "AND CODTIPOCONTROLLOESECUZIONE.RV_DOMAIN = 'TIPO_CONTROLLO_ESECUZIONE')";
		// MEV_2023-35
		lStatement += "LEFT OUTER JOIN CG_REF_CODES TIPOPENA ";
		lStatement += "ON (TIPOPENA.RV_DOMAIN = 'TIPO_PENA_ACCESSORIA' ";
		lStatement += "AND TIPOPENA.RV_LOW_VALUE = COD_TIPO_PENA_ACCESSORIA) ";
		lStatement += "LEFT OUTER JOIN CG_REF_CODES TIPODURATAPENA ";
		lStatement += "ON (TIPODURATAPENA.RV_DOMAIN = 'TIPO_DURATA' ";
		lStatement += "AND TIPODURATAPENA.RV_LOW_VALUE = DURATA)";

		return lStatement;
	}

	// Query per la ricerca di Ordinanza Rimessione Atti in base a chiave unica
	protected String getSqlQuery1() {

		String lStatement = new String("");

		lStatement += "SELECT ";
		lStatement += "ID_DEPOSITO_ORDINANZA_PC, ";
		lStatement += "ANNO_S3, ";
		lStatement += "NUM_S3, ";
		lStatement += "OGGETTO_PROCEDIMENTO, ";
		lStatement += "DATA_UDIENZA, ";
		lStatement += "DATA_CAMERA_CONSIGLIO, ";
		lStatement += "DATA_DEPOSITO, ";
		lStatement += "COD_NATURA_PROVVEDIMENTO, ";
		lStatement += "ID_CSSA_COMP, CSSA.COMUNE COMCSSADESC, ";
		lStatement += "COD_UFFICIO_MAGISTRATO_COMP, '' DESCCOMMAG, ";
		lStatement += "LUOGO_SVOLGIMENTO_PROVA, ";
		lStatement += "SERVIZIO_TERAPEUTICO_COMP, ";
		lStatement += "NUM_GIORNI_DETENZIONE_DOM, ";
		lStatement += "NUM_MESI_DETENZIONE_DOM, ";
		lStatement += "NUM_ANNI_DETENZIONE_DOM, ";
		lStatement += "NUM_GIORNI_PERMESSO_ACCORDATI, ";
		lStatement += "NUM_GIORNI_RIDUZIONE_PENA, ";
		lStatement += "NUM_GIORNI_RIDUZIONE_USUFRUITI, ";
		lStatement += "COD_UFF_TDS_CONCESSO_RIDUZIONE, '' DESCRUFFTDSCONCRID, ";
		lStatement += "COD_OPERATORE_INSERIMENTO, ";
		lStatement += "DATA_INSERIMENTO, ";
		lStatement += "COD_UFFICIO_INSERIMENTO, ";
		lStatement += "COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += "COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += "DATA_AGGIORNAMENTO, ";
		lStatement += "GEN_PRID_GENERALE_PROCEDIMENTO, ";
		lStatement += "COD_MAGISTRATO, ";
		lStatement += "ID_EVENTO_GENERATO, ";
		lStatement += "NUM_GIORNI_LIBANTICIPATA, ";
		lStatement += "FLAG_ELABORATO, ";
		lStatement += "COD_TIPO_ORDINANZA, ";
		// Nuovi campi 9-6-2004
		lStatement += "DATA_FINE_MISURA, ";
		lStatement += "DATA_DECORRENZA, ";
		lStatement += "DATA_INIZIO_PERIODO, ";
		lStatement += "FLAG_ESISTENZA_REATOOSTATIVO, ";
		lStatement += "FLAG_ESPIAZIONE_REATOOSTATIVO, ";
		lStatement += "AUTORITA_VIGILANTE, ";
		lStatement += "DATA_TRASMISSIONE, ";
		lStatement += "DATA_COMP_FOGLIO_COMPLEMENTARE, ";
		lStatement += "NUM_GIORNI_ARRESTO_REV, ";
		lStatement += "NUM_MESI_ARRESTO_REV, ";
		lStatement += "NUM_ANNI_ARRESTO_REV, ";
		lStatement += "ULTERIORE_DESCRIZIONE, ";
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		lStatement += "DATA_SOSPENSIONE_SS, ";
		lStatement += "GIORNI_RECUPERO_SS, ";
		lStatement += "FLAG_RECUPERO_SS, ";
		lStatement += "DATA_SCADENZA_SOSPENSIONE_SS, ";
		lStatement += "SOSPENSIONE_GG, ";
		lStatement += "SOSPENSIONE_MM, ";
		lStatement += "SOSPENSIONE_AA";
		// nuovi campi richiesta ottemperanza
		lStatement += ", FLAG_NOMINA_COMM_ACTA";
		lStatement += ", DESCR_COMM_ACTA";
		lStatement += ", TIPO_CONTROLLO_ESECUZIONE";
		lStatement += ",null as DESC_TIPO_CONTROLLO_ESECUZIONE";
		// 10102014 - DL 92 2014 Violazione CEDU
		lStatement += ", SOMMA_RISARC_DANNI";
		lStatement += ", COD_USSM";
		// MEV_2023-35
		lStatement += ", COD_TIPO_SANZIONE";
		lStatement += ", COD_TIPO_PENA_ACCESSORIA, DURATA, NUM_ANNI, NUM_MESI, NUM_GIORNI ";
		lStatement += "FROM DEPOSITO_ORDINANZA_PC, CSSA ";
		lStatement += "WHERE ID_CSSA_COMP = ID_CSSA ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {

		DepositoOrdinanzaPcModel aModel = new DepositoOrdinanzaPcModel();

		aModel.setIdDepositoOrdinanzaPc(getBigDecimal("ID_DEPOSITO_ORDINANZA_PC"));
		aModel.setAnnoS3(getBigDecimal("ANNO_S3"));
		aModel.setNumS3(getBigDecimal("NUM_S3"));
		aModel.setOggettoProcedimento(getString("OGGETTO_PROCEDIMENTO"));
		aModel.setDataUdienza(getDate("DATA_UDIENZA"));
		aModel.setDataCameraConsiglio(getDate("DATA_CAMERA_CONSIGLIO"));
		aModel.setDataDeposito(getDate("DATA_DEPOSITO"));
		aModel.setCodNaturaProvvedimento(getString("COD_NATURA_PROVVEDIMENTO"));
		// aModel.setDescrNaturaProvvedimento(getString("DESCCOMMAG") );
		aModel.setIdCssaComp(getBigDecimal("ID_CSSA_COMP"));
		aModel.setDescrComuneCssaComp(getString("COMCSSADESC"));
		aModel.setCodUssm(getBigDecimal("COD_USSM"));
		if (findColumn("COMUSSMDESC"))
			aModel.setDescrComuneUssmComp(getString("COMUSSMDESC"));
		aModel.setCodUfficioMagistratoComp(getString("COD_UFFICIO_MAGISTRATO_COMP"));
		aModel.setDescrUfficioMagistratoComp(getString("DESCCOMMAG"));
		aModel.setLuogoSvolgimentoProva(getString("LUOGO_SVOLGIMENTO_PROVA"));
		aModel.setServizioTerapeuticoComp(getString("SERVIZIO_TERAPEUTICO_COMP"));
		aModel.setNumGiorniDetenzioneDom(getBigDecimal("NUM_GIORNI_DETENZIONE_DOM"));
		aModel.setNumMesiDetenzioneDom(getBigDecimal("NUM_MESI_DETENZIONE_DOM"));
		aModel.setNumAnniDetenzioneDom(getBigDecimal("NUM_ANNI_DETENZIONE_DOM"));
		aModel.setNumGiorniPermessoAccordati(getBigDecimal("NUM_GIORNI_PERMESSO_ACCORDATI"));
		aModel.setNumGiorniRiduzionePena(getBigDecimal("NUM_GIORNI_RIDUZIONE_PENA"));
		aModel.setNumGiorniRiduzioneUsufruiti(getBigDecimal("NUM_GIORNI_RIDUZIONE_USUFRUITI"));
		aModel.setCodUffTdsConcessoRiduzione(getString("COD_UFF_TDS_CONCESSO_RIDUZIONE"));
		aModel.setDescrUffTdsConcessoRiduzione(getString("DESCRUFFTDSCONCRID"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setGenPridGeneraleProcedimento(getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO"));
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		aModel.setIdEventoGenerato(getBigDecimal("ID_EVENTO_GENERATO"));
		aModel.setNumGiorniLibanticipata(getBigDecimal("NUM_GIORNI_LIBANTICIPATA"));
		aModel.setFlagElaborato(getString("FLAG_ELABORATO"));
		aModel.setCodTipoOrdinanza(getString("COD_TIPO_ORDINANZA"));
		// Nuovi campi 9-6-2004
		aModel.setDataFineMisura(getDate("DATA_FINE_MISURA"));
		aModel.setDataDecorrenza(getDate("DATA_DECORRENZA"));
		aModel.setDataInizioPeriodo(getDate("DATA_INIZIO_PERIODO"));
		aModel.setFlagEsistenzaReatoostativo(getString("FLAG_ESISTENZA_REATOOSTATIVO"));
		aModel.setFlagEspiazioneReatoostativo(getString("FLAG_ESPIAZIONE_REATOOSTATIVO"));
		aModel.setAutoritaVigilante(getString("AUTORITA_VIGILANTE"));
		aModel.setDataTrasmissione(getDate("DATA_TRASMISSIONE"));
		aModel.setDataCompFoglioComplementare(getDate("DATA_COMP_FOGLIO_COMPLEMENTARE"));
		aModel.setNumGiorniArrestoRev(getBigDecimal("NUM_GIORNI_ARRESTO_REV"));
		aModel.setNumMesiArrestoRev(getBigDecimal("NUM_MESI_ARRESTO_REV"));
		aModel.setNumAnniArrestoRev(getBigDecimal("NUM_ANNI_ARRESTO_REV"));
		aModel.setUlterioreDescrizione(getString("ULTERIORE_DESCRIZIONE"));
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		aModel.setDataSospensioneSS(getDate("DATA_SOSPENSIONE_SS"));
		aModel.setGiorniRecuperoSS(getBigDecimal("GIORNI_RECUPERO_SS"));
		aModel.setFlagRecuperoSS(getString("FLAG_RECUPERO_SS"));
		aModel.setDataScadenzaSospensioneSS(getDate("DATA_SCADENZA_SOSPENSIONE_SS"));
		aModel.setSospensioneGGSS(getBigDecimal("SOSPENSIONE_GG"));
		aModel.setSospensioneMMSS(getBigDecimal("SOSPENSIONE_MM"));
		aModel.setSospensioneAASS(getBigDecimal("SOSPENSIONE_AA"));
		aModel.setFlagNominaComActa(getString("FLAG_NOMINA_COMM_ACTA"));
		aModel.setDescrCommActa(getString("DESCR_COMM_ACTA"));
		// 20140603 - ( SIUS - implemntazione per il D.L. 146 )
		aModel.setCodTipoControlloEsecuzione(getString("TIPO_CONTROLLO_ESECUZIONE"));
		aModel.setDescrTipoControlloEsecuzione(getString("DESC_TIPO_CONTROLLO_ESECUZIONE"));
		// 10102014 - DL 92 2014 Violazione CEDU
		aModel.setSommaRisarcimento(getBigDecimal("SOMMA_RISARC_DANNI"));
		// MEV_2023-35
		if (findColumn("COD_TIPO_SANZIONE"))
			aModel.setCodTipoSanzione(getString("COD_TIPO_SANZIONE"));
		if (findColumn("COD_TIPO_PENA_ACCESSORIA"))
			aModel.setCodTipoPenaAccessoria(getString("COD_TIPO_PENA_ACCESSORIA"));
		if (findColumn("DESCRPENA"))
			aModel.setDescrTipoPenaAccessoria(getString("DESCRPENA"));
		if (findColumn("DURATA"))
			aModel.setDurata(getString("DURATA"));
		if (findColumn("DESCRDURATA"))
			aModel.setDescrDurata(getString("DESCRDURATA"));
		if (findColumn("NUM_ANNI"))
			aModel.setNumAnni(getBigDecimal("NUM_ANNI"));
		if (findColumn("NUM_MESI"))
			aModel.setNumMesi(getBigDecimal("NUM_MESI"));
		if (findColumn("NUM_GIORNI"))
			aModel.setNumGiorni(getBigDecimal("NUM_GIORNI"));

		return aModel;
	}

	public String setCondizione(DepositoOrdinanzaPcModel aModel) {

		String lCondizioni = new String();
		return lCondizioni;
	}

	public String setCondizioneByS3(DepositoOrdinanzaPcModel aModel) {

		String lCondizioniS3 = new String();

		if (aModel.getAnnoS3() != null)
			lCondizioniS3 += "AND ANNO_S3 = " + aModel.getAnnoS3();
		if (aModel.getNumS3() != null)
			lCondizioniS3 += " AND NUM_S3 = " + aModel.getNumS3();
		if (aModel.getCodUfficioInserimento() != null)
			lCondizioniS3 += " AND COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";

		return lCondizioniS3;
	}

	/**
	 * Imposta condizione di filtro su la chiave id del deposito ordinanza.
	 *
	 * @param aKey chiave deposito ordinanza.
	 * @return la codizione sql.
	 */
	public String setCondizioniByKey(BigDecimal aKey) {

		return " AND ID_DEPOSITO_ORDINANZA_PC = " + aKey;
	}

	/**
	 * Imposta condizione unica di filtro sulla chiave id del deposito ordinanza.
	 *
	 * @param aKey chiave deposito ordinanza.
	 * @return la codizione sql.
	 */
	public String setCondizioneByKey(BigDecimal aKey) {

		return " WHERE ID_DEPOSITO_ORDINANZA_PC = " + aKey;
	}

	/**
	 * Calcola il Massimo NUM_S3 relativo ad un certo ufficio e all'anno in corso. Il massimo NUM_S3
	 * rappresenta l'ultimo NUM_S3 inserito all'interno dell'ufficio trattato.
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void getProgressivoS3(DepositoOrdinanzaPcModel aModel) throws DAOException {

		String lStatement = new String();

		lStatement += " SELECT MAX(NUM_S3) aMAX";
		lStatement += " FROM DEPOSITO_ORDINANZA_PC ";
		lStatement += " WHERE ANNO_S3 = " + aModel.getAnnoS3();
		lStatement += " AND COD_UFFICIO_INSERIMENTO = " + aModel.getCodUfficioInserimento();

		setStatement(lStatement);
	}

	/**
	 * Imposta select count per uso verifica esistenza di una ordinanza per id generale procedimento.
	 *
	 * @param aKey id generale procedimento.
	 */
	public void ricercaEsistenzaDepositoOrdinanzaByIdGenProc(BigDecimal aKey) {

		String lSql = "SELECT COUNT(*) AS NUM_REC FROM DEPOSITO_ORDINANZA_PC ";
		lSql += " WHERE GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
		setStatement(lSql);
	}

	/**
	 * Description: metodo di ricerca, restituisce il numero di record in DEPOSITO_ORDINANZA_PC con un record
	 * evento collegato con data di emissione specificata.
	 *
	 * @param BigDecimal aKey : Identificativo Generale Procedimento
	 * @param Date aDataEmissione : Data Emissione
	 * @return int : numero di record trovati
	 * @throws DAOException
	 */

	public int getNumDepoOrdinanzaByGenProcDataEmissione(BigDecimal aKey, Date aDataEmissione)
			throws DAOException {

		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from DEPOSITO_ORDINANZA_PC D, "
				+ "EVENTO E WHERE D.ID_EVENTO_GENERATO = E.ID_EVENTO ";
		lStatement += "AND E.DATA_EMISSIONE = TO_DATE("
				+ DateUtils.getDateToString(aDataEmissione, "yyyyMMdd") + ",'YYYYMMDD') ";
		lStatement += "AND D.GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
		setStatement(lStatement);

		this.start();
		if (this.next()) {
			lCount = this.getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}
		return retNum;
	}

	/**
	 * Description: metodo di ricerca, restituisce il numero di record in DEPOSITO_ORDINANZA_PC relativi ad un
	 * generale procedimento specificato dalla sua chiave passata come primo argomento e di tipo non presente
	 * tra quelli passati nella liata secondo argomento della funzione.
	 *
	 * @param BigDecimal
	 *            aKey : Identificativo Generale Procedimento
	 * @param aTipi
	 *            : String[] elenco dei tipi Ordinanza esclusi dalla ricerca.
	 * @return int : numero di record trovati
	 * @throws DAOException
	 */
	public int getNumDepoOrdinanzaByGenProcEccettoTipi(BigDecimal aKey, String[] aTipi) throws DAOException {

		// Numero di tipi decreti da escludere dalla ricerca
		int lNumTipi = (aTipi != null) ? aTipi.length : 0;
		// numero di record trovati
		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from DEPOSITO_ORDINANZA_PC D join EVENTO E ON "
				+ "(D.ID_EVENTO_GENERATO = E.ID_EVENTO  AND (E.FLAG_DOCUMENTO_REGISTRATO IS NULL OR "
				+ "E.FLAG_DOCUMENTO_REGISTRATO <> 'A'))";
		lStatement += " WHERE D.GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
		if (lNumTipi > 0) {
			lStatement += " AND D.COD_TIPO_ORDINANZA NOT IN ('" + aTipi[0] + "'";
			for (int i = 1; i < lNumTipi; i++) {
				lStatement += ", '" + aTipi[i] + "'";
			}
			lStatement += ")";
		}
		// Le ordinanze di rinvio hanno il codice tipo a NULL. Versioni precedenti alla 2.3.
		// Dalla versione 2.3 il COD_TIPO_ORDINANZA per Rinvio Udienza, e' valorizzato come RU.
		// Tuttavia si rimane questa condizione per compatibilita' ad ordinanze di rinvio udienza
		// emesse con versione precedente alla 2.3, per le quali il COD_TIPO_ORDINANZA veniva
		// valorizzato a NULL.
		lStatement += " AND D.COD_TIPO_ORDINANZA IS NOT NULL ";

		setStatement(lStatement);

		start();
		if (next()) {
			lCount = getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}

		return retNum;
	}

	/**
	 * Description: metodo di ricerca, restituisce la DATA_DEPOSITO di record in DEPOSITO_ORDINANZA_PC
	 * selezionato tramite ID_EVENTO_GENERATO.
	 *
	 * @param BigDecimal
	 *            aKey : Identificativo Evento
	 * @return Date : Data Deposito Ordinanza
	 * @throws DAOException
	 */
	public Date getDataDepositoByEve(BigDecimal aIdEve) throws DAOException {

		Date retData = null;

		String lStatement = "select DATA_DEPOSITO from DEPOSITO_ORDINANZA_PC D WHERE D.ID_EVENTO_GENERATO = "
				+ aIdEve;
		setStatement(lStatement);

		this.start();
		if (this.next()) {
			retData = getDate("DATA_DEPOSITO");
		}
		return retData;
	}

	/**
	 * 13-11-2014 Misre Sicurezza Fuori Sentenza Query usata nella Gestione MISURE SICUREZZA dalla parte SIEP.
	 * Esegue la RICERCA/CONTA dei provvedimenti SIUS ordinanza per DATA; // MEV_39: aggiunto parametro di
	 * passaggio
	 *
	 * @param aData_inizio
	 * @param aData_fine
	 * @param aElaborati
	 * @param codUfficio
	 * @param aPage
	 */
	public void RicercaProvvFascicoloSiusTenoreSoggetto(Date aData_inizio, Date aData_fine,
			Boolean aElaborati, String codUfficio, int aPage) {

		String strQuery = "";
		String lPaginedStatement = new String("");

		strQuery += getQueryProvvFascicoloSiusTenoreSoggetto();

		strQuery += " FROM";
		strQuery += " FASCICOLO_SIUS FASC,";
		strQuery += " EVENTO EVE, DEPOSITO_ORDINANZA_PC DEPO, TENORE TEN, SOGGETTO SOG,";
		strQuery += " UFFICIO UFF, UFFICIO_DESCR UFD, COMUNE DESCR_COM_UFF,";
		strQuery += " CG_REF_CODES MOTIVO_PROVVEDIMENTO, CG_REF_CODES TIPO_PROVVEDIMENTO, "
				+ "CG_REF_CODES ESITO_TENORE";
		// MEV_39: aggiunte tabelle in join
		strQuery += ", fascicolo_siep s, UFFICIO UFF_SIEP";
		strQuery += setCondizioneWhere(aData_inizio, aData_fine, aElaborati, codUfficio);
		strQuery += " ORDER BY FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR";

		if (aPage > 0) {
			lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + strQuery
					+ ") INNER ) WHERE rn between " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
			// lPaginedStatement =
			// "SELECT * FROM (SELECT INNER.* , Rownum rn FROM ("+strQuery+" ) INNER ) WHERE rn between
			// "+((aPage-1)*IWebConstants.RESULT_PER_PAGE_PROVA+1)+
			// " AND "+ (aPage)*IWebConstants.RESULT_PER_PAGE_PROVA;
		} else {
			lPaginedStatement = strQuery;
		}
		// settaggio della stringa SQL appena costruita prima della query
		setStatement(lPaginedStatement);
	}

	// MEV_39: aggiunto parametro di passaggio
	public void CountProvvFascicoloSiusTenoreSoggetto(Date aData_inizio, Date aData_fine, Boolean aElaborati,
			String codUfficio) {

		String lStatement = "SELECT COUNT (a.ID_FASCICOLO_SIUS) howmanyrecords ";
		lStatement += "  FROM (";
		lStatement += " SELECT DISTINCT ID_FASCICOLO_SIUS FROM FASCICOLO_SIUS FASC,";
		lStatement += " EVENTO EVE, DEPOSITO_ORDINANZA_PC DEPO, TENORE TEN, SOGGETTO SOG,";
		lStatement += " UFFICIO UFF, UFFICIO_DESCR UFD, COMUNE DESCR_COM_UFF,";
		lStatement += " CG_REF_CODES MOTIVO_PROVVEDIMENTO, CG_REF_CODES TIPO_PROVVEDIMENTO, "
				+ "CG_REF_CODES ESITO_TENORE";
		// MEV_39: aggiunte tabelle in join
		lStatement += ", fascicolo_siep s, UFFICIO UFF_SIEP";
		lStatement += setCondizioneWhere(aData_inizio, aData_fine, aElaborati, codUfficio);

		// lStatement += " ORDER BY FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR";
		lStatement += " )a";

		setStatement(lStatement);
	}

	protected String getQueryProvvFascicoloSiusTenoreSoggetto() {

		String lStatement = new String();

		lStatement += " SELECT DISTINCT (FASC.ID_FASCICOLO_SIUS),";
		lStatement += " FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR, FASC.CHIAVE_UFFICIO,";
		lStatement += " EVE.COD_TIPO_PROVVEDIMENTO, EVE.COD_MOTIVO, EVE.DATA_EMISSIONE, EVE.ID_EVENTO,";
		lStatement += " DEPO.ANNO_S3, DEPO.NUM_S3, DEPO.ID_DEPOSITO_ORDINANZA_PC, DEPO.FLAG_ELABORATO,";
		lStatement += " TEN.COD_ESITO_TENORE, TEN.COD_OGGETTO_TENORE,";
		lStatement += " UFD.DESCR_TIPO_UFFICIO DESCR_TIPO_UF,";
		lStatement += " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " MOTIVO_PROVVEDIMENTO.RV_MEANING OGGETTO, TIPO_PROVVEDIMENTO.RV_MEANING PROVVEDIMENTO,";
		lStatement += " ESITO_TENORE.RV_MEANING ESITO,";
		lStatement += " SOG.ID_SOGGETTO, SOG.COGNOME, SOG.NOME";

		return lStatement;
	}

	// METODO GETMODEL() per ExRicercaProvvedimentiSoggettoPerMisuraFuoriSentenza
	public GenericModel getModelMisFuoriSentenza() throws DAOException {

		OrdinanzaEventoTenoriFascicoloSiusModel aModel = new OrdinanzaEventoTenoriFascicoloSiusModel();

		FascicoloSiusModel lFasc = new FascicoloSiusModel();
		lFasc.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFasc.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFasc.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFasc.setChiaveUfficio(getString("CHIAVE_UFFICIO"));

		aModel.setDescrTipoUfficio(getString("DESCR_TIPO_UF"));
		aModel.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));

		EventoModel lEve = new EventoModel();
		lEve.setIdEvento(getBigDecimal("ID_EVENTO"));
		lEve.setCodMotivo(getString("COD_MOTIVO"));
		lEve.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lEve.setDataEmissione(getDate("DATA_EMISSIONE"));

		aModel.setDescrProvvedimento(getString("PROVVEDIMENTO")); // Ordinanza/Decreto
		aModel.setDescrOggetto(getString("OGGETTO")); // Motivo Provvedimento
		aModel.setDescrEsito(getString("ESITO"));

		TenoreModel lTen = new TenoreModel();
		lTen.setCodOggettoTenore(getString("COD_OGGETTO_TENORE"));
		lTen.setCodEsitoTenore(getString("COD_ESITO_TENORE"));

		DepositoOrdinanzaPcModel lOrd = new DepositoOrdinanzaPcModel();
		lOrd.setIdDepositoOrdinanzaPc(getBigDecimal("ID_DEPOSITO_ORDINANZA_PC"));
		lOrd.setAnnoS3(getBigDecimal("ANNO_S3"));
		lOrd.setNumS3(getBigDecimal("NUM_S3"));
		lOrd.setFlagElaborato(getString("FLAG_ELABORATO"));

		SoggettoModel lSog = new SoggettoModel();
		lSog.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSog.setCognome(getString("COGNOME"));
		lSog.setNome(getString("NOME"));

		aModel.setFascicoloSius(lFasc);
		aModel.setEvento(lEve);
		aModel.setTenore(lTen);
		aModel.setOrdinanza(lOrd);
		aModel.setSoggetto(lSog);

		return aModel;
	}

	// MEV_39: aggiunto parametro di passaggio
	protected String setCondizioneWhere(Date aData_inizio, Date aData_fine, Boolean aElaborati,
			String codUfficio) {

		String lStatement = new String();

		lStatement += " WHERE EVE.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		lStatement += " AND EVE.ID_EVENTO = DEPO.ID_EVENTO_GENERATO";
		lStatement += " AND TEN.DEP_OPID_DEPOSITO_ORDINANZA_PC = DEPO.ID_DEPOSITO_ORDINANZA_PC";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND UFF.COD_UFFICIO = UFD.COD_UFFICIO";
		lStatement += " AND SOG.ID_SOGGETTO = FASC.SOG_ID_SOGGETTO";
		// MEV_39: aggiunti codici 2726, 0258, 0259, 0260
		lStatement += " AND EVE.COD_TIPO_PROVVEDIMENTO = '03' AND EVE.COD_MOTIVO in ('0258', '0259', '0260',"
				+ " '2116', '2117', '2118', '2119', '2726')";
		// lStatement += " AND EVE.FLAG_DOCUMENTO_REGISTRATO = 'S'";
		lStatement += " AND MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' "
				+ "AND MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_MOTIVO";
		lStatement += " AND TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'";
		lStatement += " AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_TIPO_PROVVEDIMENTO";
		lStatement += " AND ESITO_TENORE.RV_DOMAIN = 'ESITO_TENORE'";
		// MEV_39: aggiunto codice C029
		lStatement += " AND ESITO_TENORE.RV_HIGH_VALUE in ('C029', 'U086')";
		lStatement += " AND ESITO_TENORE.RV_ABBREVIATION = TEN.COD_ESITO_TENORE";
		// MEV_39: aggiunte cinque and condition
		lStatement += " AND ESITO_TENORE.RV_ABBREVIATION in ('0061', '0190', '0191', '0210', '0380')";
		lStatement += " and s.id_fascicolo_siep = fasc.fas_sie_id_fascicolo_siep";
		lStatement += " and s.chiave_ufficio = '" + codUfficio + "'";
		// MEV_39: and condition errata e non funzionante per uffici diversi nello stesso distretto
		// (es: verbania non riceveva più da torino)
		//lStatement += " and UFF_SIEP.COD_COMUNE = uff.cod_comune";
		lStatement += " and UFF_SIEP.COD_UFFICIO = s.chiave_ufficio";
		lStatement += " AND (DEPO.DATA_DEPOSITO is not null) ";
		lStatement += " AND (DEPO.DATA_DEPOSITO >= TO_DATE('"
				+ DateUtils.getDateToString(aData_inizio, "ddMMyyyy") + "', 'DDMMYYYY')) ";
		lStatement += " AND (DEPO.DATA_DEPOSITO <= TO_DATE('"
				+ DateUtils.getDateToString(aData_fine, "ddMMyyyy") + "', 'DDMMYYYY')) ";
		if (!aElaborati) {
			lStatement += " AND (DEPO.FLAG_ELABORATO is null OR DEPO.FLAG_ELABORATO <> 'S') ";
		}

		return lStatement;
	}

	/*
	 * ISSUE MEV : aggiunta query per ricercare i provvedimenti di Sorveglianza in materia di differimento
	 * della pena 
	 * Numero MEV : 39 
	 * Autore : Gioggi 
	 * Data : 03/mar/2017 
	 * Branch : MEV_39
	 */
	public void RicercaEventoProvvedimentiDifferimentoSIUSByFascicoloSiep(BigDecimal idFascicoloSiep)
			throws DAOException {

		String lSql = "SELECT DISTINCT FASC.ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO, "
				+ "FASC.CHIAVE_PROGR, FASC.CHIAVE_UFFICIO, "
				+ "EVE.COD_TIPO_PROVVEDIMENTO, EVE.COD_MOTIVO, "
				+ "EVE.DATA_EMISSIONE, EVE.ID_EVENTO, "
				+ "EVE.COD_ESITO, EVE.ANNO_PROTOCOLLO, "
				+ "EVE.PROGR_PROTOCOLLO, "
				+ "UFF.COD_TIPO_UFFICIO COD_TIPO_UFFICIO, "
				+ "UFF.COD_UFFICIO OD_UFFICIO, "
				+ "UFD.DESCR_TIPO_UFFICIO DESCR_TIPO_UFFICIO, "
				+ "DESCR_COM_UFF.DESCRIZIONE ESCR_COMUNE_UFFICIO, "
				+ "MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_OGGETTO, "
				+ "ESITO_PROVVEDIMENTO.RV_MEANING DESCR_ESITO, "
				+ "TIPO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, "
				+ "NULL FLAG_DECISIONE_TRIBUNALE, "
				+ "P.LUOGO_SVOLGIMENTO_PROVA, P.DATA_INIZIO_PERIODO, "
				+ "P.DATA_FINE_MISURA, P.SOSPENSIONE_GG, "
				+ "P.SOSPENSIONE_MM, P.SOSPENSIONE_AA, "
				+ "MA.DATA_SCARCERAZIONE, "
				+ "MA.COD_TIPO_UFFICIO_SCARCERAZIONE, "
				+ "MA.ID_MISURA_ALTERNATIVA, MA.DATA_INIZIO_MISURA, "
				+ "MA.DATA_FINE_MISURA FROM FASCICOLO_SIUS FASC, "
				+ "EVENTOEVE, UFFICIO UFF, "
				+ "COMUNEDESCR_COM_UFF, UFFICIO_DESCR  UFD, "
				+ "CG_REF_CODES MOTIVO_PROVVEDIMENTO, "
				+ "CG_REF_CODES ESITO_PROVVEDIMENTO, "
				+ "CG_REF_CODES TIPO_PROVVEDIMENTO, DEPOSITO_ORDINANZA_PC P, "
				+ "MISURA_ALTERNATIVA MA, FASCICOLO_SIEP FS "
				+ "WHERE FASC.FAS_SIE_ID_FASCICOLO_SIEP = " + idFascicoloSiep
				+ " AND EVE.FAS_SIE_ID_FASCICOLO_SIEP = " + idFascicoloSiep
				+ " AND EVE.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS"
				+ " AND EVE.ID_EVENTO = P.ID_EVENTO_GENERATO(+)"
				+ " AND EVE.COD_TIPO_PROVVEDIMENTO IN ('03')"
				// 20190919 [SG]: modificata condizione per mancanza codici
				// + " AND EVE.COD_MOTIVO IN"
				// + " ('0428', '0429', '0430', '0431', '0432', '0433', '2550', '2551', '2552', '2553',
				// '2554', '2555', '2610', '2611')"
				// + " AND MOTIVO_PROVVEDIMENTO.rv_alt2_value in ('1148','1138','1139')" in alternativa
				+ " AND MOTIVO_PROVVEDIMENTO.rv_high_value in ('C036','U077','U082')"
				+ " AND EVE.COD_ESITO IN ('0002','0003','0004','0005','0035','0119','0145','0360')"
				+ " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO"
				+ " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE"
				+ " AND UFF.COD_UFFICIO = UFD.COD_UFFICIO"
				+ " AND TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'"
				+ " AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_TIPO_PROVVEDIMENTO"
				+ " AND MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'"
				+ " AND MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_MOTIVO"
				+ " AND ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'"
				+ " AND ESITO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_ESITO"
				+ " AND MA.FAS_SIE_ID_FASCICOLO_SIEP(+) = " + idFascicoloSiep
				+ "	AND MA.EVE_ID_EVENTO(+) = EVE.ID_EVENTO"
				+ " AND FS.ID_FASCICOLO_SIEP = FASC.FAS_SIE_ID_FASCICOLO_SIEP AND FS.ID_FASCICOLO_SIEP NOT IN("
				+ " (SELECT EE.FAS_SIE_ID_FASCICOLO_SIEP FROM EVENTO EE WHERE EE.FAS_SIE_ID_FASCICOLO_SIEP ="
				+ " FS.ID_FASCICOLO_SIEP"
				+ " AND EE.COD_MOTIVO IN ('1132') AND EE.FLAG_DOCUMENTO_REGISTRATO <> 'A'))"
				+ " ORDER BY FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR, FASC.ID_FASCICOLO_SIUS";

		setStatement(lSql);
	}

	public void RicercaEventoProvvDiffSIUSByFascSiepEFascSius(BigDecimal idFascicoloSiep,
			BigDecimal idFascSius, BigDecimal idEveFascSius, BigDecimal idEvento) throws DAOException {

		String lSql = "SELECT DISTINCT FASC.ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO, "
				+ "FASC.CHIAVE_PROGR, FASC.CHIAVE_UFFICIO, "
				+ "EVE.COD_TIPO_PROVVEDIMENTO, EVE.COD_MOTIVO, "
				+ "EVE.DATA_EMISSIONE, EVE.ID_EVENTO, "
				+ "EVE.COD_ESITO, EVE.ANNO_PROTOCOLLO, "
				+ "EVE.PROGR_PROTOCOLLO, "
				+ "UFF.COD_TIPO_UFFICIO COD_TIPO_UFFICIO, "
				+ "UFF.COD_UFFICIO COD_UFFICIO, "
				+ "UFD.DESCR_TIPO_UFFICIO DESCR_TIPO_UFFICIO, "
				+ "DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, "
				+ "MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_OGGETTO, "
				+ "ESITO_PROVVEDIMENTO.RV_MEANING DESCR_ESITO, "
				+ "TIPO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, "
				+ "NULL	FLAG_DECISIONE_TRIBUNALE, "
				+ "P.LUOGO_SVOLGIMENTO_PROVA, P.DATA_INIZIO_PERIODO, "
				+ "P.DATA_FINE_MISURA, P.SOSPENSIONE_GG, "
				+ "P.SOSPENSIONE_MM, P.SOSPENSIONE_AA, "
				+ "MA.DATA_SCARCERAZIONE, "
				+ "MA.COD_TIPO_UFFICIO_SCARCERAZIONE, "
				+ "MA.ID_MISURA_ALTERNATIVA, MA.DATA_INIZIO_MISURA, "
				+ "MA.DATA_FINE_MISURA FROM FASCICOLO_SIUS FASC, "
				+ "EVENTOEVE, UFFICIO UFF, "
				+ "COMUNEDESCR_COM_UFF, UFFICIO_DESCR  UFD, "
				+ "CG_REF_CODES MOTIVO_PROVVEDIMENTO, "
				+ "CG_REF_CODES ESITO_PROVVEDIMENTO, "
				+ "CG_REF_CODES TIPO_PROVVEDIMENTO, DEPOSITO_ORDINANZA_PC P, "
				+ "MISURA_ALTERNATIVA MA WHERE FASC.FAS_SIE_ID_FASCICOLO_SIEP = "
				+ idFascicoloSiep + " AND EVE.FAS_SIE_ID_FASCICOLO_SIEP = " + idFascicoloSiep
				+ "	AND FASC.ID_FASCICOLO_SIUS = " + idFascSius;
		if (idEveFascSius != null)
			lSql = lSql + " AND EVE.ID_EVENTO = " + idEveFascSius;
		lSql = lSql + " AND EVE.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS"
				+ " AND EVE.ID_EVENTO = P.ID_EVENTO_GENERATO(+)"
				+ " AND EVE.COD_TIPO_PROVVEDIMENTO IN ('02', '03')"
				// 20190919 [SG]: modificata condizione per mancanza codici
				// + " AND EVE.COD_MOTIVO IN"
				// + " ('0428', '0429', '0430', '0431', '0432', '0433', '2550', '2551', '2552', '2553',
				// '2554', '2555', '2610', '2611')"
				// + " AND MOTIVO_PROVVEDIMENTO.rv_alt2_value in ('1148','1138','1139')" in alternativa
				+ " AND MOTIVO_PROVVEDIMENTO.rv_high_value in ('C036','U077','U082')"
				+ " AND EVE.COD_ESITO IN ('0002', '0003', '0004', '0005', '0035', '0119', '0145', '0360')"
				// " ('0035', '0119', '0145', '0360')" + 
				+ " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO"
				+ " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE"
				+ " AND UFF.COD_UFFICIO = UFD.COD_UFFICIO"
				+ " AND TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'"
				+ " AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_TIPO_PROVVEDIMENTO"
				+ " AND MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'"
				+ " AND MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_MOTIVO"
				+ " AND ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'"
				+ " AND ESITO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_ESITO"
				+ " AND MA.FAS_SIE_ID_FASCICOLO_SIEP = " + idFascicoloSiep + " AND MA.EVE_ID_EVENTO = "
				+ idEvento + " ORDER BY FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR, FASC.ID_FASCICOLO_SIUS";

		setStatement(lSql);
	}
	// ***** FINE INTERVENTO MEV_39 *****//

}