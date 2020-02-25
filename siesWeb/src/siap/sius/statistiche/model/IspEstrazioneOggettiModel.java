package siap.sius.statistiche.model;

/**
* <p>Title: IspEstrazioneOggettiTribModel</p>
* <p>Description: Classe Model che rappresenta il IspEstrazioneOggettiTrib</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class IspEstrazioneOggettiModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -2032618880925831622L;
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
	private String mCodOggettoProcedimento;
	private BigDecimal mDepOpidDepositoOrdinanzaPc;
	private BigDecimal mDepDecIdDepositoDecreto;
	private Date mTenData;
	private Date mTenDataFine;
	private Date mTenDataIns;
	private String mCodEsitoStatistica;
	private String mDescrEsitoStatistica;
	private Date mDataDeposito;
	private String mDefinito;

	// COSTRUTTORE DI DEFAULT
	public IspEstrazioneOggettiModel() {
		mFasSiuIdFascicoloSius = null;
		mFasSiuChiaveAnno = null;
		mFasSiuChiaveUfficio = "";
		mFasSiuChiaveProgr = null;
		mFasSiuCodStatoFascicolo = "";
		mFasSiuDataIscrizione = null;
		mFasSiuDataDefinizione = null;
		mCodOggettoTenore = "";
		mDescrOggettoTenore = "";
		mCodEsitoTenore = "";
		mDescrEsitoTenore = "";
		mCodMagistrato = "";
		mDescrMagistrato = "";
		mGenPridGeneraleProcedimento = null;
		mCodOggettoProcedimento = null;
		mDepOpidDepositoOrdinanzaPc = null;
		mDepDecIdDepositoDecreto = null;
		mTenData = null;
		mTenDataFine = null;
		mTenDataIns = null;
		mCodEsitoStatistica = "";
		mDescrEsitoStatistica = "";
		mDataDeposito = null;
		mDefinito = "";
	}

	// COSTRUTTORE DI COPIA
	public IspEstrazioneOggettiModel(IspEstrazioneOggettiModel aModel) {
		mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		mFasSiuChiaveAnno = aModel.mFasSiuChiaveAnno;
		mFasSiuChiaveUfficio = aModel.mFasSiuChiaveUfficio;
		mFasSiuChiaveProgr = aModel.mFasSiuChiaveProgr;
		mFasSiuCodStatoFascicolo = aModel.mFasSiuCodStatoFascicolo;
		mFasSiuDataIscrizione = aModel.mFasSiuDataIscrizione;
		mFasSiuDataDefinizione = aModel.mFasSiuDataDefinizione;
		mCodOggettoTenore = aModel.mCodOggettoTenore;
		mDescrOggettoTenore = aModel.mDescrOggettoTenore;
		mCodEsitoTenore = aModel.mCodEsitoTenore;
		mDescrEsitoTenore = aModel.mDescrEsitoTenore;
		mCodMagistrato = aModel.mCodMagistrato;
		mDescrMagistrato = aModel.mDescrMagistrato;
		mGenPridGeneraleProcedimento = aModel.mGenPridGeneraleProcedimento;
		mCodOggettoProcedimento = aModel.mCodOggettoProcedimento;
		mDepOpidDepositoOrdinanzaPc = aModel.mDepOpidDepositoOrdinanzaPc;
		mDepDecIdDepositoDecreto = aModel.mDepDecIdDepositoDecreto;
		mTenData = aModel.mTenData;
		mTenDataFine = aModel.mTenDataFine;
		mTenDataIns = aModel.mTenDataIns;
		mCodEsitoStatistica = aModel.mCodEsitoStatistica;
		mDescrEsitoStatistica = aModel.mDescrEsitoStatistica;
		mDataDeposito = aModel.mDataDeposito;
		mDefinito = aModel.mDefinito;
	}

	// COSTRUTTORE MODEL
	public IspEstrazioneOggettiModel(BigDecimal aFasSiuIdFascicoloSius, BigDecimal aFasSiuChiaveAnno,
			String aFasSiuChiaveUfficio, BigDecimal aFasSiuChiaveProgr, String aFasSiuCodStatoFascicolo,
			Date aFasSiuDataIscrizione, Date aFasSiuDataDefinizione, String aCodOggettoTenore,
			String aDescrOggettoTenore, String aCodEsitoTenore, String aDescrEsitoTenore,
			String aCodMagistrato, String aDescrMagistrato, BigDecimal aGenPridGeneraleProcedimento,
			String aCodOggettoProcedimento, BigDecimal aDepOpidDepositoOrdinanzaPc,
			BigDecimal aDepDecIdDepositoDecreto, Date aTenData, Date aTenDataFine, Date aTenDataIns,
			String aCodEsitoStatistica, String aDescrEsitoStatistica, Date aDataDeposito, String aDefinito) {
		mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		mFasSiuChiaveAnno = aFasSiuChiaveAnno;
		mFasSiuChiaveUfficio = aFasSiuChiaveUfficio;
		mFasSiuChiaveProgr = aFasSiuChiaveProgr;
		mFasSiuCodStatoFascicolo = aFasSiuCodStatoFascicolo;
		mFasSiuDataIscrizione = aFasSiuDataIscrizione;
		mFasSiuDataDefinizione = aFasSiuDataDefinizione;
		mCodOggettoTenore = aCodOggettoTenore;
		mDescrOggettoTenore = aDescrOggettoTenore;
		mCodEsitoTenore = aCodEsitoTenore;
		mDescrEsitoTenore = aDescrEsitoTenore;
		mCodMagistrato = aCodMagistrato;
		mDescrMagistrato = aDescrMagistrato;
		mGenPridGeneraleProcedimento = aGenPridGeneraleProcedimento;
		mCodOggettoProcedimento = aCodOggettoProcedimento;
		mDepOpidDepositoOrdinanzaPc = aDepOpidDepositoOrdinanzaPc;
		mDepDecIdDepositoDecreto = aDepDecIdDepositoDecreto;
		mTenData = aTenData;
		mTenDataFine = aTenDataFine;
		mTenDataIns = aTenDataIns;
		mCodEsitoStatistica = aCodEsitoStatistica;
		mDescrEsitoStatistica = aDescrEsitoStatistica;
		mDataDeposito = aDataDeposito;
		mDefinito = aDefinito;
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

	public String getCodOggettoProcedimento() {
		return mCodOggettoProcedimento;
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

	public Date getTenDataIns() {
		return mTenDataIns;
	}

	public String getCodEsitoStatistica() {
		return mCodEsitoStatistica;
	}

	public String getDescrEsitoStatistica() {
		return mDescrEsitoStatistica;
	}

	public Date getDataDeposito() {
		return mDataDeposito;
	}

	public String getDefinito() {
		return mDefinito;
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

	public void setCodOggettoProcedimento(String aValore) {
		mCodOggettoProcedimento = aValore;
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

	public void setTenDataIns(Date aValore) {
		mTenDataIns = aValore;
	}

	public void setCodEsitoStatistica(String aValore) {
		mCodEsitoStatistica = aValore;
	}

	public void setDescrEsitoStatistica(String aValore) {
		mDescrEsitoStatistica = aValore;
	}

	public void setDataDeposito(Date aValore) {
		mDataDeposito = aValore;
	}

	public void setDefinito(String aValore) {
		mDefinito = aValore;
	}
}
