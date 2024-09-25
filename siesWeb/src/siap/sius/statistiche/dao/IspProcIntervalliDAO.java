package siap.sius.statistiche.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sius.statistiche.model.IspProcIntervalliModel;

/**
 * IspProcIntervalliDAO - Classe DAO che rappresenta la tabella IspProcIntervalli
 *
 * @version 1.0
 */
public class IspProcIntervalliDAO extends TableDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public IspProcIntervalliDAO(Connection con) {

		super(con);
		setTable("ISP_PROC_INTERVALLI");

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
		setField("DEP_OPID_DEPOSITO_ORDINANZA_PC", BIG_DECIMAL);
		setField("DEP_DEC_ID_DEPOSITO_DECRETO", BIG_DECIMAL);
		setField("TEN_DATA", DATE);
		setField("TEN_DATA_FINE", DATE);
		setField("COD_ESITO_STATISTICA", STRING);
		setField("DESC_CONTENUTO_STATIS", STRING);
		setField("DATA_RICEZIONE", DATE);
		setField("DATA_PRIMA_UDIENZA", DATE);
		setField("DATA_ULTIMA_UDIENZA", DATE);
		setField("DATA_DECISIONE", DATE);
		setField("DATA_DEPOSITO", DATE);
		setField("TEMPO_DECISIONE_DEPOSITO", BIG_DECIMAL);
		setField("TEPO_RICEZIONE_FISSAZIONE1", BIG_DECIMAL);
		setField("TEMPO_FISSAZIONE2_DEPOSITO", BIG_DECIMAL);
		setField("TEMPO_RICEZIONE_DEPOSITO", BIG_DECIMAL);
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

	public String getCodEsitoStatistica() throws DAOException {
		return getString("COD_ESITO_STATISTICA");
	}

	public String getDescContenutoStatis() throws DAOException {
		return getString("DESC_CONTENUTO_STATIS");
	}

	public Date getDataRicezione() throws DAOException {
		return getDate("DATA_RICEZIONE");
	}

	public Date getDataPrimaUdienza() throws DAOException {
		return getDate("DATA_PRIMA_UDIENZA");
	}

	public Date getDataUltimaUdienza() throws DAOException {
		return getDate("DATA_ULTIMA_UDIENZA");
	}

	public Date getDataDecisione() throws DAOException {
		return getDate("DATA_DECISIONE");
	}

	public Date getDataDeposito() throws DAOException {
		return getDate("DATA_DEPOSITO");
	}

	public BigDecimal getTempoDecisioneDeposito() throws DAOException {
		return getBigDecimal("TEMPO_DECISIONE_DEPOSITO");
	}

	public BigDecimal getTepoRicezioneFissazione1() throws DAOException {
		return getBigDecimal("TEPO_RICEZIONE_FISSAZIONE1");
	}

	public BigDecimal getTempoFissazione2Deposito() throws DAOException {
		return getBigDecimal("TEMPO_FISSAZIONE2_DEPOSITO");
	}

	public BigDecimal getTempoRicezioneDeposito() throws DAOException {
		return getBigDecimal("TEMPO_RICEZIONE_DEPOSITO");
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

	public void setCodEsitoStatistica(String aValore) {
		setString("COD_ESITO_STATISTICA", aValore);
	}

	public void setDescContenutoStatis(String aValore) {
		setString("DESC_CONTENUTO_STATIS", aValore);
	}

	public void setDataRicezione(Date aValore) {
		setDate("DATA_RICEZIONE", aValore);
	}

	public void setDataPrimaUdienza(Date aValore) {
		setDate("DATA_PRIMA_UDIENZA", aValore);
	}

	public void setDataUltimaUdienza(Date aValore) {
		setDate("DATA_ULTIMA_UDIENZA", aValore);
	}

	public void setDataDecisione(Date aValore) {
		setDate("DATA_DECISIONE", aValore);
	}

	public void setDataDeposito(Date aValore) {
		setDate("DATA_DEPOSITO", aValore);
	}

	public void setTempoDecisioneDeposito(BigDecimal aValore) {
		setBigDecimal("TEMPO_DECISIONE_DEPOSITO", aValore);
	}

	public void setTepoRicezioneFissazione1(BigDecimal aValore) {
		setBigDecimal("TEPO_RICEZIONE_FISSAZIONE1", aValore);
	}

	public void setTempoFissazione2Deposito(BigDecimal aValore) {
		setBigDecimal("TEMPO_FISSAZIONE2_DEPOSITO", aValore);
	}

	public void setTempoRicezioneDeposito(BigDecimal aValore) {
		setBigDecimal("TEMPO_RICEZIONE_DEPOSITO", aValore);
	}

	// MEV_9: modificato metodo getModel(); poi commentato poichè prendo per buono il TICKET#202409020116
	// public GenericModel getModel() throws DAOException {
	//
	// return new IspProcIntervalliModel(getFasSiuIdFascicoloSius(), getFasSiuChiaveAnno(),
	// getFasSiuChiaveUfficio(), getFasSiuChiaveProgr(), getFasSiuCodStatoFascicolo(),
	// getFasSiuDataIscrizione(), getFasSiuDataDefinizione(), getCodOggettoTenore(),
	// DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getMotivoProvvedimento(),
	// getCodOggettoTenore()),
	// getCodEsitoTenore(),
	// // MEV_9: cambiato dominio di estrazione
	// // DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getEsitoTenore(),
	// // getCodEsitoTenore()),
	// calcolaEsitoProvvedimento(getCodEsitoTenore()),
	// // FINE MEV_9
	// getCodMagistrato(), "", getGenPridGeneraleProcedimento(), getDepOpidDepositoOrdinanzaPc(),
	// getDepDecIdDepositoDecreto(), getTenData(), getTenDataFine(), getCodEsitoStatistica(), "",
	// getDescContenutoStatis(), getDataRicezione(), getDataPrimaUdienza(), getDataUltimaUdienza(),
	// getDataDecisione(), getDataDeposito(), getTempoDecisioneDeposito(),
	// getTepoRicezioneFissazione1(), getTempoFissazione2Deposito(), getTempoRicezioneDeposito());
	// }

	/*
	 * MEV_9: aggiunto metodo di estrazione codice esito provvedimento
	 */
	// private String calcolaEsitoProvvedimento(String codEsitoTenore) throws DAOException {
	//
	// try {
	// return DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getEsitoProvvedimento(),
	// codEsitoTenore);
	// } catch (DAOException de) {
	// throw de;
	// } catch (Exception e) {
	// throw new DAOException(e.getMessage());
	// }
	// }
	// ***** FINE INTERVENTO MEV_9 *****//

	/*
	 * public GenericModel getModel() throws DAOException { return new IspProcIntervalliModel(
	 * getFasSiuIdFascicoloSius() , getFasSiuChiaveAnno() , getFasSiuChiaveUfficio() , getFasSiuChiaveProgr()
	 * , getFasSiuCodStatoFascicolo() , getFasSiuDataIscrizione() , getFasSiuDataDefinizione() ,
	 * getCodOggettoTenore() ,
	 * DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getMotivoProvvedimento(),
	 * getCodOggettoTenore()), getCodEsitoTenore() , // TICKET#202409020116 - si decodifaca dal dominio
	 * ESITO_PROVVEDIMENTO e non ESITO_TENORE
	 * DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getEsitoTenore(), getCodEsitoTenore()),
	 * //DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getEsitoProvvedimento(),
	 * getCodEsitoTenore()), // TICKET#202409020116 - FINE getCodMagistrato() , "",
	 * getGenPridGeneraleProcedimento() , getDepOpidDepositoOrdinanzaPc() , getDepDecIdDepositoDecreto() ,
	 * getTenData() , getTenDataFine() , getCodEsitoStatistica() , "", getDescContenutoStatis() ,
	 * getDataRicezione() , getDataPrimaUdienza() , getDataUltimaUdienza() , getDataDecisione() ,
	 * getDataDeposito() , getTempoDecisioneDeposito() , getTepoRicezioneFissazione1() ,
	 * getTempoFissazione2Deposito() , getTempoRicezioneDeposito() ); }
	 */

	// TICKET#202409020116 si riscrive la getModel per gestire l'eccezione rilanciata dal metodo
	// getEsitoProvvedimento sovrascrivendo anche quanto fatto con la MEV_9 (conflitto nel MERGE)
	public GenericModel getModel() throws DAOException {

		IspProcIntervalliModel lProcIntervalliModel = new IspProcIntervalliModel();

		lProcIntervalliModel.setFasSiuIdFascicoloSius(getFasSiuIdFascicoloSius());
		lProcIntervalliModel.setFasSiuChiaveAnno(getFasSiuChiaveAnno());
		lProcIntervalliModel.setFasSiuChiaveUfficio(getFasSiuChiaveUfficio());
		lProcIntervalliModel.setFasSiuChiaveProgr(getFasSiuChiaveProgr());
		lProcIntervalliModel.setFasSiuCodStatoFascicolo(getFasSiuCodStatoFascicolo());
		lProcIntervalliModel.setFasSiuDataIscrizione(getFasSiuDataIscrizione());
		lProcIntervalliModel.setFasSiuDataDefinizione(getFasSiuDataDefinizione());
		lProcIntervalliModel.setCodOggettoTenore(getCodOggettoTenore());
		lProcIntervalliModel.setDescrOggettoTenore(DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getMotivoProvvedimento(), getCodOggettoTenore()));
		lProcIntervalliModel.setCodEsitoTenore(getCodEsitoTenore());
		// TICKET#202409020116 - si decodifica dal dominio ESITO_PROVVEDIMENTO e non ESITO_TENORE
		try {
			lProcIntervalliModel.setDescrEsitoTenore(DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getEsitoProvvedimento(), getCodEsitoTenore()));
		} catch (Exception e) {
			lProcIntervalliModel.setDescrEsitoTenore("");
		}
		// TICKET#202409020116 - FINE
		lProcIntervalliModel.setCodMagistrato(getCodMagistrato());
		lProcIntervalliModel.setDescrMagistrato("");
		lProcIntervalliModel.setGenPridGeneraleProcedimento(getGenPridGeneraleProcedimento());
		lProcIntervalliModel.setDepOpidDepositoOrdinanzaPc(getDepOpidDepositoOrdinanzaPc());
		lProcIntervalliModel.setDepDecIdDepositoDecreto(getDepDecIdDepositoDecreto());
		lProcIntervalliModel.setTenData(getTenData());
		lProcIntervalliModel.setTenDataFine(getTenDataFine());
		lProcIntervalliModel.setCodEsitoStatistica(getCodEsitoStatistica());
		lProcIntervalliModel.setDescrEsitoStatistica("");
		lProcIntervalliModel.setDescContenutoStatis(getDescContenutoStatis());
		lProcIntervalliModel.setDataRicezione(getDataRicezione());
		lProcIntervalliModel.setDataPrimaUdienza(getDataPrimaUdienza());
		lProcIntervalliModel.setDataUltimaUdienza(getDataUltimaUdienza());
		lProcIntervalliModel.setDataDecisione(getDataDecisione());
		lProcIntervalliModel.setDataDeposito(getDataDeposito());
		lProcIntervalliModel.setTempoDecisioneDeposito(getTempoDecisioneDeposito());
		lProcIntervalliModel.setTepoRicezioneFissazione1(getTepoRicezioneFissazione1());
		lProcIntervalliModel.setTempoFissazione2Deposito(getTempoFissazione2Deposito());
		lProcIntervalliModel.setTempoRicezioneDeposito(getTempoRicezioneDeposito());

		return lProcIntervalliModel;
	}

	public void setDAOFromModel(IspProcIntervalliModel aModel) throws DAOException {

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
		setDepOpidDepositoOrdinanzaPc(aModel.getDepOpidDepositoOrdinanzaPc());
		setDepDecIdDepositoDecreto(aModel.getDepDecIdDepositoDecreto());
		setTenData(aModel.getTenData());
		setTenDataFine(aModel.getTenDataFine());
		setCodEsitoStatistica(aModel.getCodEsitoStatistica());
		setDescContenutoStatis(aModel.getDescContenutoStatis());
		setDataRicezione(aModel.getDataRicezione());
		setDataPrimaUdienza(aModel.getDataPrimaUdienza());
		setDataUltimaUdienza(aModel.getDataUltimaUdienza());
		setDataDecisione(aModel.getDataDecisione());
		setDataDeposito(aModel.getDataDeposito());
		setTempoDecisioneDeposito(aModel.getTempoDecisioneDeposito());
		setTepoRicezioneFissazione1(aModel.getTepoRicezioneFissazione1());
		setTempoFissazione2Deposito(aModel.getTempoFissazione2Deposito());
		setTempoRicezioneDeposito(aModel.getTempoRicezioneDeposito());
	}

	public void setCondizione(IspProcIntervalliModel aModel) {

		boolean lInserito = false;
		String lCondizioni = "";
		String lCodOggetto = aModel.getCodOggettoTenore();
		String lCodMagistrato = aModel.getCodMagistrato();
		String lFasSiuChiaveUfficio = aModel.getFasSiuChiaveUfficio();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("FiltroRicercaOggetti" + aModel.toString());

		if (lCodOggetto != null && lCodOggetto.length() > 0) {
			lCondizioni = " COD_OGGETTO_TENORE = '" + lCodOggetto + "'";
			lInserito = true;
		}
		if (lCodMagistrato != null && lCodMagistrato.length() > 0) {
			if (lInserito)
				lCondizioni += " AND COD_MAGISTRATO = '" + lCodMagistrato + "'";
			else
				lCondizioni += " COD_MAGISTRATO = '" + lCodMagistrato + "'";
			lInserito = true;
		}

		if (lFasSiuChiaveUfficio != null && lFasSiuChiaveUfficio.length() > 0) {
			if (lInserito)
				lCondizioni += " AND FAS_SIU_CHIAVE_UFFICIO = '" + lFasSiuChiaveUfficio + "'";
			else
				lCondizioni += " FAS_SIU_CHIAVE_UFFICIO = '" + lFasSiuChiaveUfficio + "'";
			lInserito = true;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Condizione di ricerca: " + lCondizioni);

		if (lInserito)
			setCondition(lCondizioni);
	}

	/**
	 * 20131124 - gestione di eventuale ordinamento
	 *
	 * @param aModel
	 */
	public void setOrder(IspProcIntervalliModel aModel) {

		setOrder(aModel.getOrder()); // 20131124 - eventuale inclusione di un ordinamento ORDER BY.
	}

}