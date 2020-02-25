package siap.siep.misuracautelarebdmc.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.web.ISIAPCostantiWeb;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: MisuraCautelareBdmcSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella MisuraCautelareBdmc
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
public class MisuraCautelareBdmcSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public MisuraCautelareBdmcSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountMisuraCautelareBdmc(MisuraCautelareBdmcModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM MISURA_CAUTELARE_BDMC ";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagfina
	 * passata in input
	 * 
	 * @param aModel
	 * @param aPage
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaMisuraCautelareBdmcPaged(MisuraCautelareBdmcModel aModel, int aPage)
			throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		lStatement += " " + getOrderBy() + " ";

		String lPaginedStatement = "";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * ISIAPCostantiWeb.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * ISIAPCostantiWeb.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/*****************************************************************************
	 * Effettua la generica ricerca in base ai dati specificati nel model
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaMisuraCautelareBdmc(MisuraCautelareBdmcModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += " WHERE " + lCondizioni;

		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 * 
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaMisuraCautelareBdmcByKey(BigDecimal aIdMisuraCautelare) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aIdMisuraCautelare);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query
	 * 
	 * @return
	 ****************************************************************************/
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_MISURA_CAUTELARE_BDMC, " + "ID_MISURA_CAUTELARE, "
				+ "ID_ANNOTAZIONE_MANUALE, " + "ID_PROVV_BDMC, " + "STATO_TRASMISSIONE_ISC, "
				+ "STATO_TRASMISSIONE_VAL, " + "DATA_INIZIO_USATA, " + "DATA_FINE_USATA, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, " + "SOG_ID_SOGGETTO, " + "EVE_ID_EVENTO, "
				+ "PEN_RES_ID_PENA_RESIDUA, " + "ID_PREN, " + "PROG_PERI_PRES, " + "FLAG_CARICAMENTO, "
				+ "FLAG_STATO, " + "COD_TIPO_MISURA, " + "DATA_INIZIO, " + "DATA_FINE, " + "NUM_ANNI, "
				+ "NUM_MESI, " + "NUM_GIORNI, " + "IST_DET_ID_ISTITUTO_DETENZIONE, "
				+ "ALTRO_LUOGO_DETENZIONE, " + "FLAG_COMPUTABILE, " + "COD_MOTIVO_NON_COMPUTABILE, "
				+ "COD_TIPO_UFFICIO_RIFER, " + "COD_LUOGO_UFFICIO_RIFER, " + "DATA_COMPUTO, "
				+ "ANNO_FASC_SIEP, " + "NUME_FASC_SIEP, " + "NOTE, " + "ANNO_FASC_BDMC, "
				+ "NUME_FASC_BDMC, " + "COD_UFFICIO_BDMC, " + "ANNO_RGNR, " + "NUME_RGNR, "
				+ "COD_UFFICIO_RGNR, " + "ANNO_REGE_GIP, " + "NUMERO_REGE_GIP, " + "COD_UFFICIO_GIP, "
				+ "ANNO_REGE_DIB, " + "NUMERO_REGE_DIB, " + "COD_UFFICIO_DIB, " + "ANNO_REGE_CAS, "
				+ "NUMERO_REGE_CAS, " + "COD_UFFICIO_CAS, " + "ANNO_REGE_CAP, " + "NUMERO_REGE_CAP, "
				+ "COD_UFFICIO_CAP, " + "ANNO_REGE_CASAP, " + "NUMERO_REGE_CASAP, " + "COD_UFFICIO_CASAP, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM MISURA_CAUTELARE_BDMC";

		/*
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_TIPO_MISURA,'-') = CODTIPOMISURA.RV_LOW_VALUE AND CODTIPOMISURA.RV_DOMAIN = 'TIPO_MISURA' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_MOTIVO_NON_COMPUTABILE,'-') = CODMOTIVONONCOMPUTABILE.RV_LOW_VALUE AND CODMOTIVONONCOMPUTABILE.RV_DOMAIN = 'MOTIVO_NON_COMPUTABILE' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_TIPO_UFFICIO_RIFER,'-') = CODTIPOUFFICIORIFER.RV_LOW_VALUE AND CODTIPOUFFICIORIFER.RV_DOMAIN = 'TIPO_UFFICIO_RIFER' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_LUOGO_UFFICIO_RIFER,'-') = CODLUOGOUFFICIORIFER.RV_LOW_VALUE AND CODLUOGOUFFICIORIFER.RV_DOMAIN = 'LUOGO_UFFICIO_RIFER' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_UFFICIO_BDMC,'-') = CODUFFICIOBDMC.RV_LOW_VALUE AND CODUFFICIOBDMC.RV_DOMAIN = 'UFFICIO_BDMC' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_UFFICIO_RGNR,'-') = CODUFFICIORGNR.RV_LOW_VALUE AND CODUFFICIORGNR.RV_DOMAIN = 'UFFICIO_RGNR' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_UFFICIO_GIP,'-') = CODUFFICIOGIP.RV_LOW_VALUE AND CODUFFICIOGIP.RV_DOMAIN = 'UFFICIO_GIP' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_UFFICIO_DIB,'-') = CODUFFICIODIB.RV_LOW_VALUE AND CODUFFICIODIB.RV_DOMAIN = 'UFFICIO_DIB' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_UFFICIO_CAS,'-') = CODUFFICIOCAS.RV_LOW_VALUE AND CODUFFICIOCAS.RV_DOMAIN = 'UFFICIO_CAS' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_UFFICIO_CAP,'-') = CODUFFICIOCAP.RV_LOW_VALUE AND CODUFFICIOCAP.RV_DOMAIN = 'UFFICIO_CAP' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_UFFICIO_CASAP,'-') = CODUFFICIOCASAP.RV_LOW_VALUE AND CODUFFICIOCASAP.RV_DOMAIN = 'UFFICIO_CASAP' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_OPERATORE_INSERIMENTO,'-') = CODOPERATOREINSERIMENTO.RV_LOW_VALUE AND CODOPERATOREINSERIMENTO.RV_DOMAIN = 'OPERATORE_INSERIMENTO' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_UFFICIO_INSERIMENTO,'-') = CODUFFICIOINSERIMENTO.RV_LOW_VALUE AND CODUFFICIOINSERIMENTO.RV_DOMAIN = 'UFFICIO_INSERIMENTO' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_OPERATORE_AGGIORNAMENTO,'-') = CODOPERATOREAGGIORNAMENTO.RV_LOW_VALUE AND CODOPERATOREAGGIORNAMENTO.RV_DOMAIN = 'OPERATORE_AGGIORNAMENTO' ) "
		 * lStatement +=
		 * " (     nvl(MISURA_CAUTELARE_BDMC.COD_UFFICIO_AGGIORNAMENTO,'-') = CODUFFICIOAGGIORNAMENTO.RV_LOW_VALUE AND CODUFFICIOAGGIORNAMENTO.RV_DOMAIN = 'UFFICIO_AGGIORNAMENTO' ) "
		 */
		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		MisuraCautelareBdmcModel aModel = new MisuraCautelareBdmcModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdMisuraCautelareBdmc(getBigDecimal("ID_MISURA_CAUTELARE_BDMC"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setPenResIdPenaResidua(getBigDecimal("PEN_RES_ID_PENA_RESIDUA"));
		aModel.setIdPren(getBigDecimal("ID_PREN"));
		aModel.setProgPeriPres(getBigDecimal("PROG_PERI_PRES"));
		aModel.setFlagCaricamento(getString("FLAG_CARICAMENTO"));
		aModel.setFlagStato(getString("FLAG_STATO"));
		aModel.setCodTipoMisura(getString("COD_TIPO_MISURA"));
		// aModel.setDescrTipoMisura(getString("") );
		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setNumAnni(getBigDecimal("NUM_ANNI"));
		aModel.setNumMesi(getBigDecimal("NUM_MESI"));
		aModel.setNumGiorni(getBigDecimal("NUM_GIORNI"));
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
		aModel.setAltroLuogoDetenzione(getString("ALTRO_LUOGO_DETENZIONE"));
		aModel.setFlagComputabile(getString("FLAG_COMPUTABILE"));
		aModel.setCodMotivoNonComputabile(getString("COD_MOTIVO_NON_COMPUTABILE"));
		// aModel.setDescrMotivoNonComputabile(getString("") );
		aModel.setCodTipoUfficioRifer(getString("COD_TIPO_UFFICIO_RIFER"));
		// aModel.setDescrTipoUfficioRifer(getString("") );
		aModel.setCodLuogoUfficioRifer(getString("COD_LUOGO_UFFICIO_RIFER"));
		// aModel.setDescrLuogoUfficioRifer(getString("") );
		aModel.setDataComputo(getDate("DATA_COMPUTO"));
		aModel.setAnnoFascSiep(getBigDecimal("ANNO_FASC_SIEP"));
		aModel.setNumeFascSiep(getBigDecimal("NUME_FASC_SIEP"));
		aModel.setNote(getString("NOTE"));
		aModel.setAnnoFascBdmc(getBigDecimal("ANNO_FASC_BDMC"));
		aModel.setNumeFascBdmc(getBigDecimal("NUME_FASC_BDMC"));
		aModel.setCodUfficioBdmc(getString("COD_UFFICIO_BDMC"));
		// aModel.setDescrUfficioBdmc(getString("") );
		aModel.setAnnoRgnr(getBigDecimal("ANNO_RGNR"));
		aModel.setNumeRgnr(getBigDecimal("NUME_RGNR"));
		aModel.setCodUfficioRgnr(getString("COD_UFFICIO_RGNR"));
		// aModel.setDescrUfficioRgnr(getString("") );
		aModel.setAnnoRegeGip(getBigDecimal("ANNO_REGE_GIP"));
		aModel.setNumeroRegeGip(getBigDecimal("NUMERO_REGE_GIP"));
		aModel.setCodUfficioGip(getString("COD_UFFICIO_GIP"));
		// aModel.setDescrUfficioGip(getString("") );
		aModel.setAnnoRegeDib(getBigDecimal("ANNO_REGE_DIB"));
		aModel.setNumeroRegeDib(getBigDecimal("NUMERO_REGE_DIB"));
		aModel.setCodUfficioDib(getString("COD_UFFICIO_DIB"));
		// aModel.setDescrUfficioDib(getString("") );
		aModel.setAnnoRegeCas(getBigDecimal("ANNO_REGE_CAS"));
		aModel.setNumeroRegeCas(getBigDecimal("NUMERO_REGE_CAS"));
		aModel.setCodUfficioCas(getString("COD_UFFICIO_CAS"));
		// aModel.setDescrUfficioCas(getString("") );
		aModel.setAnnoRegeCap(getBigDecimal("ANNO_REGE_CAP"));
		aModel.setNumeroRegeCap(getBigDecimal("NUMERO_REGE_CAP"));
		aModel.setCodUfficioCap(getString("COD_UFFICIO_CAP"));
		// aModel.setDescrUfficioCap(getString("") );
		aModel.setAnnoRegeCasap(getBigDecimal("ANNO_REGE_CASAP"));
		aModel.setNumeroRegeCasap(getBigDecimal("NUMERO_REGE_CASAP"));
		aModel.setCodUfficioCasap(getString("COD_UFFICIO_CASAP"));
		// aModel.setDescrUfficioCasap(getString("") );
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setIdMisuraCautelare(getBigDecimal("ID_MISURA_CAUTELARE"));
		aModel.setIdAnnotazioneManuale(getBigDecimal("ID_ANNOTAZIONE_MANUALE"));
		aModel.setIdProvvBdmc(getBigDecimal("ID_PROVV_BDMC"));
		aModel.setStatoTrasmissioneIsc(getString("STATO_TRASMISSIONE_ISC"));
		aModel.setStatoTrasmissioneVal(getString("STATO_TRASMISSIONE_VAL"));
		aModel.setDataInizioUsata(getDate("DATA_INIZIO_USATA"));
		aModel.setDataFineUsata(getDate("DATA_FINE_USATA"));

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(MisuraCautelareBdmcModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdMisuraCautelareBdmc() != null) {
			lCondizioni += " and ID_MISURA_CAUTELARE_BDMC = " + aModel.getIdMisuraCautelareBdmc() + "";
		}
		if (aModel.getIdMisuraCautelare() != null) {
			lCondizioni += " and ID_MISURA_CAUTELARE = " + aModel.getIdMisuraCautelare() + "";
		}
		if (aModel.getIdAnnotazioneManuale() != null) {
			lCondizioni += " and ID_ANNOTAZIONE_MANUALE = " + aModel.getIdAnnotazioneManuale() + "";
		}

		if (aModel.getStatoTrasmissioneIsc() != null) {
			lCondizioni += " and STATO_TRASMISSIONE_ISC = " + aModel.getStatoTrasmissioneIsc() + "";
		}
		if (aModel.getStatoTrasmissioneVal() != null) {
			lCondizioni += " and STATO_TRASMISSIONE_VAL = " + aModel.getStatoTrasmissioneVal() + "";
		}
		if (aModel.getDataInizioUsata() != null) {
			lCondizioni += " and to_char(DATA_INIZIO_USATA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInizioUsata(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataFineUsata() != null) {
			lCondizioni += " and to_char(DATA_FINE_USATA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataFineUsata(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + "";
		}
		if (aModel.getSogIdSoggetto() != null) {
			lCondizioni += " and SOG_ID_SOGGETTO = " + aModel.getSogIdSoggetto() + "";
		}
		if (aModel.getEveIdEvento() != null) {
			lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + "";
		}
		if (aModel.getPenResIdPenaResidua() != null) {
			lCondizioni += " and PEN_RES_ID_PENA_RESIDUA = " + aModel.getPenResIdPenaResidua() + "";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getProgPeriPres() != null) {
			lCondizioni += " and PROG_PERI_PRES = " + aModel.getProgPeriPres() + "";
		}
		if (aModel.getFlagCaricamento() != null && aModel.getFlagCaricamento().length() > 0) {
			lCondizioni += " and FLAG_CARICAMENTO = '" + aModel.getFlagCaricamento() + "' ";
		}
		if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) {
			lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' ";
		}
		if (aModel.getCodTipoMisura() != null && aModel.getCodTipoMisura().length() > 0) {
			lCondizioni += " and COD_TIPO_MISURA = '" + aModel.getCodTipoMisura() + "' ";
		}
		if (aModel.getDataInizio() != null) {
			lCondizioni += " and to_char(DATA_INIZIO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInizio(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataFine() != null) {
			lCondizioni += " and to_char(DATA_FINE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataFine(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getNumAnni() != null) {
			lCondizioni += " and NUM_ANNI = " + aModel.getNumAnni() + "";
		}
		if (aModel.getNumMesi() != null) {
			lCondizioni += " and NUM_MESI = " + aModel.getNumMesi() + "";
		}
		if (aModel.getNumGiorni() != null) {
			lCondizioni += " and NUM_GIORNI = " + aModel.getNumGiorni() + "";
		}
		if (aModel.getIstDetIdIstitutoDetenzione() != null
				&& aModel.getIstDetIdIstitutoDetenzione().length() > 0) {
			lCondizioni += " and IST_DET_ID_ISTITUTO_DETENZIONE = '" + aModel.getIstDetIdIstitutoDetenzione()
					+ "' ";
		}
		if (aModel.getAltroLuogoDetenzione() != null && aModel.getAltroLuogoDetenzione().length() > 0) {
			lCondizioni += " and ALTRO_LUOGO_DETENZIONE = '" + aModel.getAltroLuogoDetenzione() + "' ";
		}
		if (aModel.getFlagComputabile() != null && aModel.getFlagComputabile().length() > 0) {
			lCondizioni += " and FLAG_COMPUTABILE = '" + aModel.getFlagComputabile() + "' ";
		}
		if (aModel.getCodMotivoNonComputabile() != null && aModel.getCodMotivoNonComputabile().length() > 0) {
			lCondizioni += " and COD_MOTIVO_NON_COMPUTABILE = '" + aModel.getCodMotivoNonComputabile() + "' ";
		}
		if (aModel.getCodTipoUfficioRifer() != null && aModel.getCodTipoUfficioRifer().length() > 0) {
			lCondizioni += " and COD_TIPO_UFFICIO_RIFER = '" + aModel.getCodTipoUfficioRifer() + "' ";
		}
		if (aModel.getCodLuogoUfficioRifer() != null && aModel.getCodLuogoUfficioRifer().length() > 0) {
			lCondizioni += " and COD_LUOGO_UFFICIO_RIFER = '" + aModel.getCodLuogoUfficioRifer() + "' ";
		}
		if (aModel.getDataComputo() != null) {
			lCondizioni += " and to_char(DATA_COMPUTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataComputo(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getAnnoFascSiep() != null) {
			lCondizioni += " and ANNO_FASC_SIEP = " + aModel.getAnnoFascSiep() + "";
		}
		if (aModel.getNumeFascSiep() != null) {
			lCondizioni += " and NUME_FASC_SIEP = " + aModel.getNumeFascSiep() + "";
		}
		if (aModel.getNote() != null && aModel.getNote().length() > 0) {
			lCondizioni += " and NOTE = '" + aModel.getNote() + "' ";
		}
		if (aModel.getAnnoFascBdmc() != null) {
			lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + "";
		}
		if (aModel.getNumeFascBdmc() != null) {
			lCondizioni += " and NUME_FASC_BDMC = " + aModel.getNumeFascBdmc() + "";
		}
		if (aModel.getCodUfficioBdmc() != null && aModel.getCodUfficioBdmc().length() > 0) {
			lCondizioni += " and COD_UFFICIO_BDMC = '" + aModel.getCodUfficioBdmc() + "' ";
		}
		if (aModel.getAnnoRgnr() != null) {
			lCondizioni += " and ANNO_RGNR = " + aModel.getAnnoRgnr() + "";
		}
		if (aModel.getNumeRgnr() != null) {
			lCondizioni += " and NUME_RGNR = " + aModel.getNumeRgnr() + "";
		}
		if (aModel.getCodUfficioRgnr() != null && aModel.getCodUfficioRgnr().length() > 0) {
			lCondizioni += " and COD_UFFICIO_RGNR = '" + aModel.getCodUfficioRgnr() + "' ";
		}
		if (aModel.getAnnoRegeGip() != null) {
			lCondizioni += " and ANNO_REGE_GIP = " + aModel.getAnnoRegeGip() + "";
		}
		if (aModel.getNumeroRegeGip() != null) {
			lCondizioni += " and NUMERO_REGE_GIP = " + aModel.getNumeroRegeGip() + "";
		}
		if (aModel.getCodUfficioGip() != null && aModel.getCodUfficioGip().length() > 0) {
			lCondizioni += " and COD_UFFICIO_GIP = '" + aModel.getCodUfficioGip() + "' ";
		}
		if (aModel.getAnnoRegeDib() != null) {
			lCondizioni += " and ANNO_REGE_DIB = " + aModel.getAnnoRegeDib() + "";
		}
		if (aModel.getNumeroRegeDib() != null) {
			lCondizioni += " and NUMERO_REGE_DIB = " + aModel.getNumeroRegeDib() + "";
		}
		if (aModel.getCodUfficioDib() != null && aModel.getCodUfficioDib().length() > 0) {
			lCondizioni += " and COD_UFFICIO_DIB = '" + aModel.getCodUfficioDib() + "' ";
		}
		if (aModel.getAnnoRegeCas() != null) {
			lCondizioni += " and ANNO_REGE_CAS = " + aModel.getAnnoRegeCas() + "";
		}
		if (aModel.getNumeroRegeCas() != null) {
			lCondizioni += " and NUMERO_REGE_CAS = " + aModel.getNumeroRegeCas() + "";
		}
		if (aModel.getCodUfficioCas() != null && aModel.getCodUfficioCas().length() > 0) {
			lCondizioni += " and COD_UFFICIO_CAS = '" + aModel.getCodUfficioCas() + "' ";
		}
		if (aModel.getAnnoRegeCap() != null) {
			lCondizioni += " and ANNO_REGE_CAP = " + aModel.getAnnoRegeCap() + "";
		}
		if (aModel.getNumeroRegeCap() != null) {
			lCondizioni += " and NUMERO_REGE_CAP = " + aModel.getNumeroRegeCap() + "";
		}
		if (aModel.getCodUfficioCap() != null && aModel.getCodUfficioCap().length() > 0) {
			lCondizioni += " and COD_UFFICIO_CAP = '" + aModel.getCodUfficioCap() + "' ";
		}
		if (aModel.getAnnoRegeCasap() != null) {
			lCondizioni += " and ANNO_REGE_CASAP = " + aModel.getAnnoRegeCasap() + "";
		}
		if (aModel.getNumeroRegeCasap() != null) {
			lCondizioni += " and NUMERO_REGE_CASAP = " + aModel.getNumeroRegeCasap() + "";
		}
		if (aModel.getCodUfficioCasap() != null && aModel.getCodUfficioCasap().length() > 0) {
			lCondizioni += " and COD_UFFICIO_CASAP = '" + aModel.getCodUfficioCasap() + "' ";
		}
		if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' ";
		}
		if (aModel.getDataInserimento() != null) {
			lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
		}
		if (aModel.getCodOperatoreAggiornamento() != null
				&& aModel.getCodOperatoreAggiornamento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
					+ "' ";
		}
		if (aModel.getDataAggiornamento() != null) {
			lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAggiornamento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' ";
		}
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		logger.info("lCondizioni = " + lCondizioni);
		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di select per chiave
	 * 
	 * @param aKey
	 * @return
	 ****************************************************************************/
	public String setCondizioniByKey(BigDecimal aIdMisuraCautelare) {

		String lCondizioni = new String();

		lCondizioni += " and ID_MISURA_CAUTELARE_BDMC = " + aIdMisuraCautelare;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		logger.info("lCondizioni = " + lCondizioni);

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sezione order by
	 * 
	 * @return
	 ****************************************************************************/
	protected String getOrderBy() {

		String orderBy = new String("");
		// orderBy = " ORDER BY ";
		return orderBy;
	}

}