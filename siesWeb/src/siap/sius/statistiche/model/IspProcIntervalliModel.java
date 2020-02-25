package siap.sius.statistiche.model;

/**
* <p>Title: IspProcIntervalliModel</p>
* <p>Description: Classe Model che rappresenta il IspProcIntervalli</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class IspProcIntervalliModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = 1371835724288911160L;
	private BigDecimal mFasSiuIdFascicoloSius;
	private BigDecimal mFasSiuChiaveAnno;
	private String mFasSiuChiaveUfficio;
	private BigDecimal mFasSiuChiaveProgr;
	private String mFasSiuCodStatoFascicolo;
	private Date mFasSiuDataIscrizione;
	private Date mFasSiuDataDefinizione;
	private String mCodOggettoTenore;
	private String mDescrOggettoTenore;
	private String mCodEsitoTenore;
	private String mDescrEsitoTenore;
	private String mCodMagistrato;
	private String mDescrMagistrato;
	private BigDecimal mGenPridGeneraleProcedimento;
	private BigDecimal mDepOpidDepositoOrdinanzaPc;
	private BigDecimal mDepDecIdDepositoDecreto;
	private Date mTenData;
	private Date mTenDataFine;
	private String mCodEsitoStatistica;
	private String mDescrEsitoStatistica;
	private String mDescContenutoStatis;
	private Date mDataRicezione;
	private Date mDataPrimaUdienza;
	private Date mDataUltimaUdienza;
	private Date mDataDecisione;
	private Date mDataDeposito;
	private BigDecimal mTempoDecisioneDeposito;
	private BigDecimal mTepoRicezioneFissazione1;
	private BigDecimal mTempoFissazione2Deposito;
	private BigDecimal mTempoRicezioneDeposito;
	private String mOrder;

	// COSTRUTTORE DI DEFAULT
	public IspProcIntervalliModel() {
		this.mFasSiuIdFascicoloSius = null;
		this.mFasSiuChiaveAnno = null;
		this.mFasSiuChiaveUfficio = "";
		this.mFasSiuChiaveProgr = null;
		this.mFasSiuCodStatoFascicolo = "";
		this.mFasSiuDataIscrizione = null;
		this.mFasSiuDataDefinizione = null;
		this.mCodOggettoTenore = "";
		this.mDescrOggettoTenore = "";
		this.mCodEsitoTenore = "";
		this.mDescrEsitoTenore = "";
		this.mCodMagistrato = "";
		this.mDescrMagistrato = "";
		this.mGenPridGeneraleProcedimento = null;
		this.mDepOpidDepositoOrdinanzaPc = null;
		this.mDepDecIdDepositoDecreto = null;
		this.mTenData = null;
		this.mTenDataFine = null;
		this.mCodEsitoStatistica = "";
		this.mDescrEsitoStatistica = "";
		this.mDescContenutoStatis = "";
		this.mDataRicezione = null;
		this.mDataPrimaUdienza = null;
		this.mDataUltimaUdienza = null;
		this.mDataDecisione = null;
		this.mDataDeposito = null;
		this.mTempoDecisioneDeposito = null;
		this.mTepoRicezioneFissazione1 = null;
		this.mTempoFissazione2Deposito = null;
		this.mTempoRicezioneDeposito = null;
		this.mOrder = "";
	}

	// COSTRUTTORE DI COPIA
	public IspProcIntervalliModel(IspProcIntervalliModel aModel) {
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mFasSiuChiaveAnno = aModel.mFasSiuChiaveAnno;
		this.mFasSiuChiaveUfficio = aModel.mFasSiuChiaveUfficio;
		this.mFasSiuChiaveProgr = aModel.mFasSiuChiaveProgr;
		this.mFasSiuCodStatoFascicolo = aModel.mFasSiuCodStatoFascicolo;
		this.mFasSiuDataIscrizione = aModel.mFasSiuDataIscrizione;
		this.mFasSiuDataDefinizione = aModel.mFasSiuDataDefinizione;
		this.mCodOggettoTenore = aModel.mCodOggettoTenore;
		this.mDescrOggettoTenore = aModel.mDescrOggettoTenore;
		this.mCodEsitoTenore = aModel.mCodEsitoTenore;
		this.mDescrEsitoTenore = aModel.mDescrEsitoTenore;
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mDescrMagistrato = aModel.mDescrMagistrato;
		this.mGenPridGeneraleProcedimento = aModel.mGenPridGeneraleProcedimento;
		this.mDepOpidDepositoOrdinanzaPc = aModel.mDepOpidDepositoOrdinanzaPc;
		this.mDepDecIdDepositoDecreto = aModel.mDepDecIdDepositoDecreto;
		this.mTenData = aModel.mTenData;
		this.mTenDataFine = aModel.mTenDataFine;
		this.mCodEsitoStatistica = aModel.mCodEsitoStatistica;
		this.mDescrEsitoStatistica = aModel.mDescrEsitoStatistica;
		this.mDescContenutoStatis = aModel.mDescContenutoStatis;
		this.mDataRicezione = aModel.mDataRicezione;
		this.mDataPrimaUdienza = aModel.mDataPrimaUdienza;
		this.mDataUltimaUdienza = aModel.mDataUltimaUdienza;
		this.mDataDecisione = aModel.mDataDecisione;
		this.mDataDeposito = aModel.mDataDeposito;
		this.mTempoDecisioneDeposito = aModel.mTempoDecisioneDeposito;
		this.mTepoRicezioneFissazione1 = aModel.mTepoRicezioneFissazione1;
		this.mTempoFissazione2Deposito = aModel.mTempoFissazione2Deposito;
		this.mTempoRicezioneDeposito = aModel.mTempoRicezioneDeposito;
	}

	// COSTRUTTORE MODEL
	public IspProcIntervalliModel(BigDecimal aFasSiuIdFascicoloSius, BigDecimal aFasSiuChiaveAnno,
			String aFasSiuChiaveUfficio, BigDecimal aFasSiuChiaveProgr, String aFasSiuCodStatoFascicolo,
			Date aFasSiuDataIscrizione, Date aFasSiuDataDefinizione, String aCodOggettoTenore,
			String aDescrOggettoTenore, String aCodEsitoTenore, String aDescrEsitoTenore,
			String aCodMagistrato, String aDescrMagistrato, BigDecimal aGenPridGeneraleProcedimento,
			BigDecimal aDepOpidDepositoOrdinanzaPc, BigDecimal aDepDecIdDepositoDecreto, Date aTenData,
			Date aTenDataFine, String aCodEsitoStatistica, String aDescrEsitoStatistica,
			String aDescContenutoStatis, Date aDataRicezione, Date aDataPrimaUdienza, Date aDataUltimaUdienza,
			Date aDataDecisione, Date aDataDeposito, BigDecimal aTempoDecisioneDeposito,
			BigDecimal aTepoRicezioneFissazione1, BigDecimal aTempoFissazione2Deposito,
			BigDecimal aTempoRicezioneDeposito) {
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mFasSiuChiaveAnno = aFasSiuChiaveAnno;
		this.mFasSiuChiaveUfficio = aFasSiuChiaveUfficio;
		this.mFasSiuChiaveProgr = aFasSiuChiaveProgr;
		this.mFasSiuCodStatoFascicolo = aFasSiuCodStatoFascicolo;
		this.mFasSiuDataIscrizione = aFasSiuDataIscrizione;
		this.mFasSiuDataDefinizione = aFasSiuDataDefinizione;
		this.mCodOggettoTenore = aCodOggettoTenore;
		this.mDescrOggettoTenore = aDescrOggettoTenore;
		this.mCodEsitoTenore = aCodEsitoTenore;
		this.mDescrEsitoTenore = aDescrEsitoTenore;
		this.mCodMagistrato = aCodMagistrato;
		this.mDescrMagistrato = aDescrMagistrato;
		this.mGenPridGeneraleProcedimento = aGenPridGeneraleProcedimento;
		this.mDepOpidDepositoOrdinanzaPc = aDepOpidDepositoOrdinanzaPc;
		this.mDepDecIdDepositoDecreto = aDepDecIdDepositoDecreto;
		this.mTenData = aTenData;
		this.mTenDataFine = aTenDataFine;
		this.mCodEsitoStatistica = aCodEsitoStatistica;
		this.mDescrEsitoStatistica = aDescrEsitoStatistica;
		this.mDescContenutoStatis = aDescContenutoStatis;
		this.mDataRicezione = aDataRicezione;
		this.mDataPrimaUdienza = aDataPrimaUdienza;
		this.mDataUltimaUdienza = aDataUltimaUdienza;
		this.mDataDecisione = aDataDecisione;
		this.mDataDeposito = aDataDeposito;
		this.mTempoDecisioneDeposito = aTempoDecisioneDeposito;
		this.mTepoRicezioneFissazione1 = aTepoRicezioneFissazione1;
		this.mTempoFissazione2Deposito = aTempoFissazione2Deposito;
		this.mTempoRicezioneDeposito = aTempoRicezioneDeposito;
	}

	//
	// METODI GET()
	//

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public BigDecimal getFasSiuChiaveAnno() {
		return mFasSiuChiaveAnno;
	}

	public String getFasSiuChiaveUfficio() {
		return mFasSiuChiaveUfficio;
	}

	public BigDecimal getFasSiuChiaveProgr() {
		return mFasSiuChiaveProgr;
	}

	public String getFasSiuCodStatoFascicolo() {
		return mFasSiuCodStatoFascicolo;
	}

	public Date getFasSiuDataIscrizione() {
		return mFasSiuDataIscrizione;
	}

	public Date getFasSiuDataDefinizione() {
		return mFasSiuDataDefinizione;
	}

	public String getCodOggettoTenore() {
		return mCodOggettoTenore;
	}

	public String getDescrOggettoTenore() {
		return mDescrOggettoTenore;
	}

	public String getCodEsitoTenore() {
		return mCodEsitoTenore;
	}

	public String getDescrEsitoTenore() {
		return mDescrEsitoTenore;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public String getDescrMagistrato() {
		return mDescrMagistrato;
	}

	public BigDecimal getGenPridGeneraleProcedimento() {
		return mGenPridGeneraleProcedimento;
	}

	public BigDecimal getDepOpidDepositoOrdinanzaPc() {
		return mDepOpidDepositoOrdinanzaPc;
	}

	public BigDecimal getDepDecIdDepositoDecreto() {
		return mDepDecIdDepositoDecreto;
	}

	public Date getTenData() {
		return mTenData;
	}

	public Date getTenDataFine() {
		return mTenDataFine;
	}

	public String getCodEsitoStatistica() {
		return mCodEsitoStatistica;
	}

	public String getDescrEsitoStatistica() {
		return mDescrEsitoStatistica;
	}

	public String getDescContenutoStatis() {
		return mDescContenutoStatis;
	}

	public Date getDataRicezione() {
		return mDataRicezione;
	}

	public Date getDataPrimaUdienza() {
		return mDataPrimaUdienza;
	}

	public Date getDataUltimaUdienza() {
		return mDataUltimaUdienza;
	}

	public Date getDataDecisione() {
		return mDataDecisione;
	}

	public Date getDataDeposito() {
		return mDataDeposito;
	}

	public BigDecimal getTempoDecisioneDeposito() {
		return mTempoDecisioneDeposito;
	}

	public BigDecimal getTepoRicezioneFissazione1() {
		return mTepoRicezioneFissazione1;
	}

	public BigDecimal getTempoFissazione2Deposito() {
		return mTempoFissazione2Deposito;
	}

	public BigDecimal getTempoRicezioneDeposito() {
		return mTempoRicezioneDeposito;
	}

	public String getOrder() {
		return mOrder;
	}

	//
	// METODI SET()
	//

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setFasSiuChiaveAnno(BigDecimal aValore) {
		mFasSiuChiaveAnno = aValore;
	}

	public void setFasSiuChiaveUfficio(String aValore) {
		mFasSiuChiaveUfficio = aValore;
	}

	public void setFasSiuChiaveProgr(BigDecimal aValore) {
		mFasSiuChiaveProgr = aValore;
	}

	public void setFasSiuCodStatoFascicolo(String aValore) {
		mFasSiuCodStatoFascicolo = aValore;
	}

	public void setFasSiuDataIscrizione(Date aValore) {
		mFasSiuDataIscrizione = aValore;
	}

	public void setFasSiuDataDefinizione(Date aValore) {
		mFasSiuDataDefinizione = aValore;
	}

	public void setCodOggettoTenore(String aValore) {
		mCodOggettoTenore = aValore;
	}

	public void setDescrOggettoTenore(String aValore) {
		mDescrOggettoTenore = aValore;
	}

	public void setCodEsitoTenore(String aValore) {
		mCodEsitoTenore = aValore;
	}

	public void setDescrEsitoTenore(String aValore) {
		mDescrEsitoTenore = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setDescrMagistrato(String aValore) {
		mDescrMagistrato = aValore;
	}

	public void setGenPridGeneraleProcedimento(BigDecimal aValore) {
		mGenPridGeneraleProcedimento = aValore;
	}

	public void setDepOpidDepositoOrdinanzaPc(BigDecimal aValore) {
		mDepOpidDepositoOrdinanzaPc = aValore;
	}

	public void setDepDecIdDepositoDecreto(BigDecimal aValore) {
		mDepDecIdDepositoDecreto = aValore;
	}

	public void setTenData(Date aValore) {
		mTenData = aValore;
	}

	public void setTenDataFine(Date aValore) {
		mTenDataFine = aValore;
	}

	public void setCodEsitoStatistica(String aValore) {
		mCodEsitoStatistica = aValore;
	}

	public void setDescrEsitoStatistica(String aValore) {
		mDescrEsitoStatistica = aValore;
	}

	public void setDescContenutoStatis(String aValore) {
		mDescContenutoStatis = aValore;
	}

	public void setDataRicezione(Date aValore) {
		mDataRicezione = aValore;
	}

	public void setDataPrimaUdienza(Date aValore) {
		mDataPrimaUdienza = aValore;
	}

	public void setDataUltimaUdienza(Date aValore) {
		mDataUltimaUdienza = aValore;
	}

	public void setDataDecisione(Date aValore) {
		mDataDecisione = aValore;
	}

	public void setDataDeposito(Date aValore) {
		mDataDeposito = aValore;
	}

	public void setTempoDecisioneDeposito(BigDecimal aValore) {
		mTempoDecisioneDeposito = aValore;
	}

	public void setTepoRicezioneFissazione1(BigDecimal aValore) {
		mTepoRicezioneFissazione1 = aValore;
	}

	public void setTempoFissazione2Deposito(BigDecimal aValore) {
		mTempoFissazione2Deposito = aValore;
	}

	public void setTempoRicezioneDeposito(BigDecimal aValore) {
		mTempoRicezioneDeposito = aValore;
	}

	public void setOrder(String aValore) {
		mOrder = aValore;
	}

}
