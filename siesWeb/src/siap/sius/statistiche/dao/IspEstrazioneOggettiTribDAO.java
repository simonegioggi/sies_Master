package siap.sius.statistiche.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sius.statistiche.model.IspEstrazioneOggettiModel;

/**
 * IspEstrazioneOggettiTribDAO - Classe DAO che rappresenta la tabella IspEstrazioneOggettiTrib
 *
 * @version 1.0
 */
public class IspEstrazioneOggettiTribDAO extends TableDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public IspEstrazioneOggettiTribDAO(Connection con) {

		super(con);
		setTable("ISP_ESTRAZIONE_OGGETTI_TRIB");

		// Settare la Sequence e i campi chiave

		setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
		setField("FAS_SIU_CHIAVE_ANNO", BIG_DECIMAL);
		setField("FAS_SIU_CHIAVE_UFFICIO", STRING);
		setField("FAS_SIU_CHIAVE_PROGR", BIG_DECIMAL);
		setField("FAS_SIU_COD_STATO_FASCICOLO", STRING);
		setField("FAS_SIU_DATA_ISCRIZIONE", DATE);
		setField("FAS_SIU_DATA_DEFINIZIONE", DATE);
		setField("COD_OGGETTO_TENORE", STRING);
		setField("COD_ESITO_TENORE", STRING);
		setField("COD_MAGISTRATO", STRING);
		setField("GEN_PRID_GENERALE_PROCEDIMENTO", BIG_DECIMAL);
		setField("COD_OGGETTO_PROCEDIMENTO", STRING);
		setField("DEP_OPID_DEPOSITO_ORDINANZA_PC", BIG_DECIMAL);
		setField("DEP_DEC_ID_DEPOSITO_DECRETO", BIG_DECIMAL);
		setField("TEN_DATA", DATE);
		setField("TEN_DATA_FINE", DATE);
		setField("TEN_DATA_INS", DATE);
		setField("COD_ESITO_STATISTICA", STRING);
		setField("DATA_DEPOSITO", DATE);
		setField("DEFINITO", STRING);
	}

	//
	// METODI GET()
	//

	public BigDecimal getFasSiuIdFascicoloSius() throws DAOException {
		return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS");
	}

	public BigDecimal getFasSiuChiaveAnno() throws DAOException {
		return getBigDecimal("FAS_SIU_CHIAVE_ANNO");
	}

	public String getFasSiuChiaveUfficio() throws DAOException {
		return getString("FAS_SIU_CHIAVE_UFFICIO");
	}

	public BigDecimal getFasSiuChiaveProgr() throws DAOException {
		return getBigDecimal("FAS_SIU_CHIAVE_PROGR");
	}

	public String getFasSiuCodStatoFascicolo() throws DAOException {
		return getString("FAS_SIU_COD_STATO_FASCICOLO");
	}

	public Date getFasSiuDataIscrizione() throws DAOException {
		return getDate("FAS_SIU_DATA_ISCRIZIONE");
	}

	public Date getFasSiuDataDefinizione() throws DAOException {
		return getDate("FAS_SIU_DATA_DEFINIZIONE");
	}

	public String getCodOggettoTenore() throws DAOException {
		return getString("COD_OGGETTO_TENORE");
	}

	public String getCodEsitoTenore() throws DAOException {
		return getString("COD_ESITO_TENORE");
	}

	public String getCodMagistrato() throws DAOException {
		return getString("COD_MAGISTRATO");
	}

	public BigDecimal getGenPridGeneraleProcedimento() throws DAOException {
		return getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO");
	}

	public String getCodOggettoProcedimento() throws DAOException {
		return getString("COD_OGGETTO_PROCEDIMENTO");
	}

	public BigDecimal getDepOpidDepositoOrdinanzaPc() throws DAOException {
		return getBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC");
	}

	public BigDecimal getDepDecIdDepositoDecreto() throws DAOException {
		return getBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO");
	}

	public Date getTenData() throws DAOException {
		return getDate("TEN_DATA");
	}

	public Date getTenDataFine() throws DAOException {
		return getDate("TEN_DATA_FINE");
	}

	public Date getTenDataIns() throws DAOException {
		return getDate("TEN_DATA_INS");
	}

	public String getCodEsitoStatistica() throws DAOException {
		return getString("COD_ESITO_STATISTICA");
	}

	public Date getDataDeposito() throws DAOException {
		return getDate("DATA_DEPOSITO");
	}

	public String getDefinito() throws DAOException {
		return getString("DEFINITO");
	}

	//
	// METODI SET()
	//

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore);
	}

	public void setFasSiuChiaveAnno(BigDecimal aValore) {
		setBigDecimal("FAS_SIU_CHIAVE_ANNO", aValore);
	}

	public void setFasSiuChiaveUfficio(String aValore) {
		setString("FAS_SIU_CHIAVE_UFFICIO", aValore);
	}

	public void setFasSiuChiaveProgr(BigDecimal aValore) {
		setBigDecimal("FAS_SIU_CHIAVE_PROGR", aValore);
	}

	public void setFasSiuCodStatoFascicolo(String aValore) {
		setString("FAS_SIU_COD_STATO_FASCICOLO", aValore);
	}

	public void setFasSiuDataIscrizione(Date aValore) {
		setDate("FAS_SIU_DATA_ISCRIZIONE", aValore);
	}

	public void setFasSiuDataDefinizione(Date aValore) {
		setDate("FAS_SIU_DATA_DEFINIZIONE", aValore);
	}

	public void setCodOggettoTenore(String aValore) {
		setString("COD_OGGETTO_TENORE", aValore);
	}

	public void setCodEsitoTenore(String aValore) {
		setString("COD_ESITO_TENORE", aValore);
	}

	public void setCodMagistrato(String aValore) {
		setString("COD_MAGISTRATO", aValore);
	}

	public void setGenPridGeneraleProcedimento(BigDecimal aValore) {
		setBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO", aValore);
	}

	public void setCodOggettoProcedimento(String aValore) {
		setString("COD_OGGETTO_PROCEDIMENTO", aValore);
	}

	public void setDepOpidDepositoOrdinanzaPc(BigDecimal aValore) {
		setBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC", aValore);
	}

	public void setDepDecIdDepositoDecreto(BigDecimal aValore) {
		setBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO", aValore);
	}

	public void setTenData(Date aValore) {
		setDate("TEN_DATA", aValore);
	}

	public void setTenDataFine(Date aValore) {
		setDate("TEN_DATA_FINE", aValore);
	}

	public void setTenDataIns(Date aValore) {
		setDate("TEN_DATA_INS", aValore);
	}

	public void setCodEsitoStatistica(String aValore) {
		setString("COD_ESITO_STATISTICA", aValore);
	}

	public void setDataDeposito(Date aValore) {
		setDate("DATA_DEPOSITO", aValore);
	}

	public void setDefinito(String aValore) {
		setString("DEFINITO", aValore);
	}

	public GenericModel getModel() throws DAOException {
		return new IspEstrazioneOggettiModel(getFasSiuIdFascicoloSius(), getFasSiuChiaveAnno(),
				getFasSiuChiaveUfficio(), getFasSiuChiaveProgr(), getFasSiuCodStatoFascicolo(),
				getFasSiuDataIscrizione(), getFasSiuDataDefinizione(), getCodOggettoTenore(),
				DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getMotivoProvvedimento(),
						getCodOggettoTenore()),
				getCodEsitoTenore(), "", getCodMagistrato(), "", getGenPridGeneraleProcedimento(),
				getCodOggettoProcedimento(), getDepOpidDepositoOrdinanzaPc(), getDepDecIdDepositoDecreto(),
				getTenData(), getTenDataFine(), getTenDataIns(), getCodEsitoStatistica(),
				decodificaEsitoStatistica(getCodEsitoStatistica()), getDataDeposito(), getDefinito());

	}

	public void setDAOFromModel(IspEstrazioneOggettiModel aModel) throws DAOException {
		setFasSiuIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
		setFasSiuChiaveAnno(aModel.getFasSiuChiaveAnno());
		setFasSiuChiaveUfficio(aModel.getFasSiuChiaveUfficio());
		setFasSiuChiaveProgr(aModel.getFasSiuChiaveProgr());
		setFasSiuCodStatoFascicolo(aModel.getFasSiuCodStatoFascicolo());
		setFasSiuDataIscrizione(aModel.getFasSiuDataIscrizione());
		setFasSiuDataDefinizione(aModel.getFasSiuDataDefinizione());
		setCodOggettoTenore(aModel.getCodOggettoTenore());
		setCodEsitoTenore(aModel.getCodEsitoTenore());
		setCodMagistrato(aModel.getCodMagistrato());
		setGenPridGeneraleProcedimento(aModel.getGenPridGeneraleProcedimento());
		setCodOggettoProcedimento(aModel.getCodOggettoProcedimento());
		setDepOpidDepositoOrdinanzaPc(aModel.getDepOpidDepositoOrdinanzaPc());
		setDepDecIdDepositoDecreto(aModel.getDepDecIdDepositoDecreto());
		setTenData(aModel.getTenData());
		setTenDataFine(aModel.getTenDataFine());
		setTenDataIns(aModel.getTenDataIns());
		setCodEsitoStatistica(aModel.getCodEsitoStatistica());
		setDataDeposito(aModel.getDataDeposito());
		setDefinito(aModel.getDefinito());
	}

	private String decodificaEsitoStatistica(String aCodEsito) {

		String lEsito = "";
		if (aCodEsito != null && aCodEsito.trim().length() > 0) {
			if (aCodEsito.equalsIgnoreCase("1"))
				lEsito = "Accolto";
			else if (aCodEsito.equalsIgnoreCase("2"))
				lEsito = "Rigettato";
			else if (aCodEsito.equalsIgnoreCase("3"))
				lEsito = "Inammissibilità";
			else if (aCodEsito.equalsIgnoreCase("4"))
				lEsito = "NLP/NDP";
			else if (aCodEsito.equalsIgnoreCase("5"))
				lEsito = "Incompetenza";
			else if (aCodEsito.equalsIgnoreCase("8"))
				lEsito = "Cancellato";
			else if (aCodEsito.equalsIgnoreCase("7"))
				lEsito = "Unificato";
			// MEV_2019-09 si aggiunge la decodifica per gli Accolti ex art.678 c.1 ter c.p.p.
			else if (aCodEsito.equalsIgnoreCase("9"))
				// MEV_2024-092: modificata la dicitura da "Accolti Provvisoriamente" a "Accolti ex art.678
				// c.1 ter c.p.p."
				// lEsito = "Applicato Provvisoriamente"; ???? Applicato ????
				lEsito = "Accolto ex art.678 c.1 ter c.p.p.";
			else
				lEsito = "Altro";
		}
		return lEsito;
	}

	public void setCondizione(IspEstrazioneOggettiModel aModel) {
		boolean lInserito = false;
		String lCondizioni = "";
		String lCodOggetto = aModel.getCodOggettoTenore();
		String lCodMagistrato = aModel.getCodMagistrato();
		String lCodUfficio = aModel.getFasSiuChiaveUfficio();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("FiltroRicercaOggetti" + aModel.toString());

		if (lCodOggetto != null && lCodOggetto.length() > 0) {
			lCondizioni = " COD_OGGETTO_TENORE = '" + lCodOggetto + "'";
			lInserito = true;
		}
		if (lCodUfficio != null && lCodUfficio.length() > 0) {
			if (lInserito)
				lCondizioni += " AND FAS_SIU_CHIAVE_UFFICIO = '" + lCodUfficio + "'";
			else
				lCondizioni += " FAS_SIU_CHIAVE_UFFICIO = '" + lCodUfficio + "'";
			lInserito = true;
		}
		if (lCodMagistrato != null && lCodMagistrato.length() > 0) {
			if (lInserito)
				lCondizioni += " AND COD_MAGISTRATO = '" + lCodMagistrato + "'";
			else
				lCondizioni += " COD_MAGISTRATO = '" + lCodMagistrato + "'";
			lInserito = true;
		}

		lCondizioni += " ORDER BY FAS_SIU_CHIAVE_ANNO, FAS_SIU_CHIAVE_PROGR ";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Condizione di ricerca: " + lCondizioni);

		// if (lInserito)
		setCondition(lCondizioni);
	}

	public void setCondizioneOrdinataPerOggetto(IspEstrazioneOggettiModel aModel) {
		boolean lInserito = false;
		String lCondizioni = "";
		String lCodOggetto = aModel.getCodOggettoTenore();
		String lCodMagistrato = aModel.getCodMagistrato();
		String lCodUfficio = aModel.getFasSiuChiaveUfficio();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("FiltroRicercaOggetti" + aModel.toString());

		if (lCodOggetto != null && lCodOggetto.length() > 0) {
			lCondizioni = " COD_OGGETTO_TENORE = '" + lCodOggetto + "'";
			lInserito = true;
		}
		if (lCodUfficio != null && lCodUfficio.length() > 0) {
			if (lInserito)
				lCondizioni += " AND FAS_SIU_CHIAVE_UFFICIO = '" + lCodUfficio + "'";
			else
				lCondizioni += " FAS_SIU_CHIAVE_UFFICIO = '" + lCodUfficio + "'";
			lInserito = true;
		}
		if (lCodMagistrato != null && lCodMagistrato.length() > 0) {
			if (lInserito)
				lCondizioni += " AND COD_MAGISTRATO = '" + lCodMagistrato + "'";
			else
				lCondizioni += " COD_MAGISTRATO = '" + lCodMagistrato + "'";
			lInserito = true;
		}

		lCondizioni += " ORDER BY COD_OGGETTO_TENORE, FAS_SIU_CHIAVE_ANNO, FAS_SIU_CHIAVE_PROGR ";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Condizione di ricerca: " + lCondizioni);

		// if (lInserito)
		setCondition(lCondizioni);
	}

	/**
	 * Imposta la condizione per l'estrazione delle occorrenze per un insieme di oggetti. l'eknco degli
	 * oggetti sono separati da ",".
	 *
	 * @param aModel
	 */
	public void setCondizioneOrdinataPerOggetti(IspEstrazioneOggettiModel aModel) {
		boolean lInserito = false;
		String lCondizioni = "";
		String lCodOggetti = aModel.getCodOggettoTenore();
		String lCodMagistrato = aModel.getCodMagistrato();
		String lCodUfficio = aModel.getFasSiuChiaveUfficio();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("FiltroRicercaOggetti" + aModel.toString());

		if (lCodOggetti != null && lCodOggetti.length() > 0) {
			lCondizioni = " COD_OGGETTO_TENORE IN ('" + lCodOggetti + "')";
			lInserito = true;
		}
		if (lCodUfficio != null && lCodUfficio.length() > 0) {
			if (lInserito)
				lCondizioni += " AND FAS_SIU_CHIAVE_UFFICIO = '" + lCodUfficio + "'";
			else
				lCondizioni += " FAS_SIU_CHIAVE_UFFICIO = '" + lCodUfficio + "'";
			lInserito = true;
		}
		if (lCodMagistrato != null && lCodMagistrato.length() > 0) {
			if (lInserito)
				lCondizioni += " AND COD_MAGISTRATO = '" + lCodMagistrato + "'";
			else
				lCondizioni += " COD_MAGISTRATO = '" + lCodMagistrato + "'";
			lInserito = true;
		}

		lCondizioni += " ORDER BY COD_OGGETTO_TENORE, FAS_SIU_CHIAVE_ANNO, FAS_SIU_CHIAVE_PROGR ";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Condizione di ricerca: " + lCondizioni);

		// if (lInserito)
		setCondition(lCondizioni);
	}

	public void setCondizioneMagNull(String lCodUfficio) {
		String lCondizioni = "";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("FiltroRicercaOggetti");

		lCondizioni = " COD_MAGISTRATO is NULL ";

		lCondizioni += " AND FAS_SIU_CHIAVE_UFFICIO = '" + lCodUfficio + "'";

		lCondizioni += " ORDER BY FAS_SIU_CHIAVE_ANNO, FAS_SIU_CHIAVE_PROGR ";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Condizione di ricerca: " + lCondizioni);

		setCondition(lCondizioni);
	}

	public void setCondizioneTenDataFineNotNull(String lCodUfficio, String lCodMagistrato) {
		String lCondizioni = "";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("FiltroRicercaOggetti");

		lCondizioni = " TEN_DATA_FINE is NOT NULL ";

		if (lCodMagistrato != null && lCodMagistrato.length() > 0 && lCodMagistrato.compareTo("0") != 0) {
			lCondizioni += " AND COD_MAGISTRATO = '" + lCodMagistrato + "'";
		}

		lCondizioni += " AND FAS_SIU_CHIAVE_UFFICIO = '" + lCodUfficio + "'";

		lCondizioni += " ORDER BY COD_OGGETTO_TENORE, FAS_SIU_CHIAVE_ANNO, FAS_SIU_CHIAVE_PROGR ";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Condizione di ricerca: " + lCondizioni);

		setCondition(lCondizioni);
	}

	public void setCondizioneTenDataFineCancellati(String lCodUfficio, String lCodMagistrato, Date lDataIni,
			Date lDataFine) {

		String lCondizioni = "";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("FiltroRicercaOggetti");

		// String strDataIni = lDataIni.toString();
		// String strDataFine = lDataFine.toString();
		String strDataIni = DateUtils.getDateToString(lDataIni, "dd/MM/yyyy");
		String strDataFine = DateUtils.getDateToString(lDataFine, "dd/MM/yyyy");

		lCondizioni = " TEN_DATA_FINE is NOT NULL ";

		if (lCodMagistrato != null && lCodMagistrato.length() > 0 && lCodMagistrato.compareTo("0") != 0) {
			lCondizioni += " AND COD_MAGISTRATO = '" + lCodMagistrato + "'";
		}

		lCondizioni += " AND FAS_SIU_CHIAVE_UFFICIO = '" + lCodUfficio + "'";

		lCondizioni += " AND TRUNC( TEN_DATA_FINE, 'dd') <= TO_DATE('" + strDataFine + "','dd/mm/yyyy') ";
		lCondizioni += " AND TRUNC( TEN_DATA_FINE, 'dd') >= TO_DATE('" + strDataIni + "','dd/mm/yyyy') ";

		lCondizioni += " AND DEFINITO = 'S'";

		lCondizioni += " ORDER BY COD_OGGETTO_TENORE, FAS_SIU_CHIAVE_ANNO, FAS_SIU_CHIAVE_PROGR ";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Condizione di ricerca: " + lCondizioni);

		setCondition(lCondizioni);
	}

}