package siap.sius.statistiche.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.utente.model.UtenteModel;

public class RicercaProcedimentoModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -8114280665381264285L;
	private Date mDataCameraConsiglioInizio = null;
	private Date mDataCameraConsiglioFine = null;
	private Date mDataIscrizioneInizio = null;
	private Date mDataIscrizioneFine = null;
	private String mCodPosizioneGiuridica = null;
	private String mDescrPosizioneGiuridica = null;
	private String mCodOggettoProcedimento = null;
	private String mDescrOggettoProcedimento = null;
	private UtenteModel mUtenteConnesso = null;

	// In uso da FR15-FA16 Ricerca Procedimenti per Provvedimenti non validati/depositati
	private int mStatoProcedimento = 0;
	private BigDecimal mAnnoInizio = null;
	private BigDecimal mNumeroInizio = null;
	private BigDecimal mAnnoFine = null;
	private BigDecimal mNumeroFine = null;
	private Date mDataDepositoInizio = null;
	private Date mDataDepositoFine = null;

	// In uso da FR017-FA018 Ricerca Procedimenti con Data Udienza Fissata non definiti per NUM GG.
	private Date mDataFine = null;
	private Integer mNumeroGiorni = null;

	// In uso da FR019-FA020 Ricerca Procedimenti Emessi non Depositati per NUM GG.
	private Date mDataEmissioneInizio = null;
	private Date mDataEmissioneFine = null;

	// MEV 9
	private Date mDataRestituzioneInizio = null;
	private Date mDataRestituzioneFine = null;
	// MEV 9 - FINE
	

	// Utilizzata da FR034 - FA035
	private String mCodTipoUfficio = null;
	private String mDescrTipoUfficio = null;
	private String mCodSede = null;
	private String mDescrSede = null;
	private Date mDataFinePendenza = null;
	private String mCodMagistrato = null;
	private String mDescrMagistrato = null;
	private String mCodCancelleria = null;
	private String mDescrCancelleria = null;
	private String mCodUfficio = null;
	// Utilizzata da FI021_FR022_FA023_FA024
	private String[] mCodOggetti = null;
	private String[] mCodMotivi = null;
	private String[] mCodMagistrati = null;

	public RicercaProcedimentoModel() {
	}

	public RicercaProcedimentoModel(RicercaProcedimentoModel aModel) {
		// in uso FR012 -
		setDataCameraConsiglioInizio(aModel.getDataCameraConsiglioInizio());
		setDataCameraConsiglioFine(aModel.getDataCameraConsiglioFine());
		setDataIscrizioneInizio(aModel.getDataIscrizioneInizio());
		setDataIscrizioneFine(aModel.getDataIscrizioneFine());
		setCodPosizioneGiuridica(aModel.getCodPosizioneGiuridica());
		setDescrPosizioneGiuridica(aModel.getDescrPosizioneGiuridica());
		setCodOggettoProcedimento(aModel.getCodOggettoProcedimento());
		setDescrOggettoProcedimento(aModel.getDescrOggettoProcedimento());

		// In uso da FR15-FA16 Ricerca Procedimenti per Provvedimenti non validati/depositati
		setStatoProcedimento(aModel.getStatoProcedimento());
		setAnnoInizio(aModel.getAnnoInizio());
		setNumeroInizio(aModel.getNumeroInizio());
		setAnnoFine(aModel.getAnnoFine());
		setNumeroFine(aModel.getNumeroFine());
		setDataDepositoInizio(aModel.getDataDepositoInizio());
		setDataDepositoFine(aModel.getDataDepositoFine());

		// in uso comune
		setUtenteConnesso(aModel.getUtenteConnesso());

		// FR017-FA018
		setDataFine(aModel.getDataFine());
		setNumeroGiorni(aModel.getNumeroGiorni());

		// FR019-FA020
		setDataEmissioneInizio(aModel.getDataEmissioneInizio());
		setDataEmissioneFine(aModel.getDataEmissioneFine());

		// FR034-FA035
		setCodUfficio(aModel.getCodUfficio());

		// FI021_FR022_FA023_FA024
		setCodOggetti(aModel.mCodOggetti);
		setCodMagistrati(aModel.mCodMagistrati);
		setCodMotivi(aModel.mCodMotivi);

		// MEV9
		setDataRestituzioneInizio(aModel.getDataRestituzioneInizio()) ;
		setDataRestituzioneFine (aModel.getDataRestituzioneFine()) ;
		// MEV9 - FINE		
	}

	// setter and otter

	public Date getDataCameraConsiglioInizio() {
		return mDataCameraConsiglioInizio;
	}

	public void setDataCameraConsiglioInizio(Date aDataCameraConsiglioInizio) {
		this.mDataCameraConsiglioInizio = aDataCameraConsiglioInizio;
	}

	public Date getDataCameraConsiglioFine() {
		return mDataCameraConsiglioFine;
	}

	public void setDataCameraConsiglioFine(Date aDataCameraConsiglioFine) {
		this.mDataCameraConsiglioFine = aDataCameraConsiglioFine;
	}

	public Date getDataIscrizioneInizio() {
		return mDataIscrizioneInizio;
	}

	public void setDataIscrizioneInizio(Date aDataIscrizioneInizio) {
		this.mDataIscrizioneInizio = aDataIscrizioneInizio;
	}

	public Date getDataIscrizioneFine() {
		return mDataIscrizioneFine;
	}

	public void setDataIscrizioneFine(Date aDataIscrizioneFine) {
		this.mDataIscrizioneFine = aDataIscrizioneFine;
	}

	public String getCodPosizioneGiuridica() {
		return mCodPosizioneGiuridica;
	}

	public void setCodPosizioneGiuridica(String aCodPosizioneGiuridica) {
		this.mCodPosizioneGiuridica = aCodPosizioneGiuridica;
	}

	public String getDescrPosizioneGiuridica() {
		return mDescrPosizioneGiuridica;
	}

	public void setDescrPosizioneGiuridica(String aDescrPosizioneGiuridica) {
		this.mDescrPosizioneGiuridica = aDescrPosizioneGiuridica;
	}

	public String getCodOggettoProcedimento() {
		return mCodOggettoProcedimento;
	}

	public void setCodOggettoProcedimento(String aCodOggettoProcedimento) {
		this.mCodOggettoProcedimento = aCodOggettoProcedimento;
	}

	public String getDescrOggettoProcedimento() {
		return mDescrOggettoProcedimento;
	}

	public void setDescrOggettoProcedimento(String aDescrOggettoProcedimento) {
		this.mDescrOggettoProcedimento = aDescrOggettoProcedimento;
	}

	public UtenteModel getUtenteConnesso() {
		return this.mUtenteConnesso;
	}

	public void setUtenteConnesso(UtenteModel aUtenteConnesso) {
		this.mUtenteConnesso = aUtenteConnesso;
	}

	public int getStatoProcedimento() {
		return mStatoProcedimento;
	}

	public void setStatoProcedimento(int aStatoProcedimento) {
		this.mStatoProcedimento = aStatoProcedimento;
	}

	public BigDecimal getAnnoInizio() {
		return mAnnoInizio;
	}

	public void setAnnoInizio(BigDecimal aAnnoInizio) {
		this.mAnnoInizio = aAnnoInizio;
	}

	public BigDecimal getNumeroInizio() {
		return mNumeroInizio;
	}

	public void setNumeroInizio(BigDecimal aNumeroInizio) {
		this.mNumeroInizio = aNumeroInizio;
	}

	public BigDecimal getAnnoFine() {
		return mAnnoFine;
	}

	public void setAnnoFine(BigDecimal aAnnoFine) {
		this.mAnnoFine = aAnnoFine;
	}

	public BigDecimal getNumeroFine() {
		return mNumeroFine;
	}

	public void setNumeroFine(BigDecimal aNumeroFine) {
		this.mNumeroFine = aNumeroFine;
	}

	public Date getDataDepositoInizio() {
		return mDataDepositoInizio;
	}

	public void setDataDepositoInizio(Date aDataDepositoInizio) {
		this.mDataDepositoInizio = aDataDepositoInizio;
	}

	public Date getDataDepositoFine() {
		return mDataDepositoFine;
	}

	public void setDataDepositoFine(Date aDataDepositoFine) {
		this.mDataDepositoFine = aDataDepositoFine;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public void setDataFine(Date aDataFine) {
		this.mDataFine = aDataFine;
	}

	public Integer getNumeroGiorni() {
		return mNumeroGiorni;
	}

	public void setNumeroGiorni(Integer aNumeroGiorni) {
		this.mNumeroGiorni = aNumeroGiorni;
	}

	public Date getDataEmissioneInizio() {
		return mDataEmissioneInizio;
	}

	public void setDataEmissioneInizio(Date aDataEmissioneInizio) {
		this.mDataEmissioneInizio = aDataEmissioneInizio;
	}

	public Date getDataEmissioneFine() {
		return mDataEmissioneFine;
	}

	public void setDataEmissioneFine(Date aDataEmissioneFine) {
		this.mDataEmissioneFine = aDataEmissioneFine;
	}

	public void setCodTipoUfficio(String aCodTipoUfficio) {
		this.mCodTipoUfficio = aCodTipoUfficio;
	}

	public String getCodTipoUfficio() {
		return this.mCodTipoUfficio;
	}

	public void setCodSede(String aCodSede) {
		this.mCodSede = aCodSede;
	}

	public String getCodSede() {
		return this.mCodSede;
	}

	public void setDescrSede(String aDescrSede) {
		this.mDescrSede = aDescrSede;
	}

	public String getDescrSede() {
		return this.mDescrSede;
	}

	public void setDataFinePendenza(Date aDateFinePendenza) {
		this.mDataFinePendenza = aDateFinePendenza;
	}

	public Date getDataFinePendenza() {
		return this.mDataFinePendenza;
	}

	public String getDescrTipoUfficio() {
		return this.mDescrTipoUfficio;
	}

	public void setDescrTipoUfficio(String aDescrTipoUfficio) {
		this.mDescrTipoUfficio = aDescrTipoUfficio;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public void setCodMagistrato(String aCodMagistrato) {
		this.mCodMagistrato = aCodMagistrato;
	}

	public String getDescrMagistrato() {
		return mDescrMagistrato;
	}

	public void setDescrMagistrato(String aDescrMagistrato) {
		this.mDescrMagistrato = aDescrMagistrato;
	}

	public String getCodCancelleria() {
		return mCodCancelleria;
	}

	public void setCodCancelleria(String aCodCancelleria) {
		this.mCodCancelleria = aCodCancelleria;
	}

	public String getDescrCancelleria() {
		return this.mDescrCancelleria;
	}

	public void setDescrCancelleria(String aDescrCancelleria) {
		this.mDescrCancelleria = aDescrCancelleria;
	}

	public String getCodUfficio() {
		return this.mCodUfficio;
	}

	public void setCodUfficio(String aCodUfficio) {
		this.mCodUfficio = aCodUfficio;
	}

	public String[] getCodOggetti() {
		return mCodOggetti;
	}

	public void setCodOggetti(String[] aCodOggetti) {
		mCodOggetti = aCodOggetti;
	}

	public String[] getCodMotivi() {
		return mCodMotivi;
	}

	public void setCodMotivi(String[] aCodMotivi) {
		mCodMotivi = aCodMotivi;
	}

	public String[] getCodMagistrati() {
		return mCodMagistrati;
	}

	public void setCodMagistrati(String[] aCodMagistrati) {
		mCodMagistrati = aCodMagistrati;
	}

	// MEV9
	public Date getDataRestituzioneInizio() {
		return mDataRestituzioneInizio;
	}

	public void setDataRestituzioneInizio(Date mDataRestituzioneInizio) {
		this.mDataRestituzioneInizio = mDataRestituzioneInizio;
	}

	public Date getDataRestituzioneFine() {
		return mDataRestituzioneFine;
	}

	public void setDataRestituzioneFine(Date mDataRestituzioneFine) {
		this.mDataRestituzioneFine = mDataRestituzioneFine;
	}
	// MEV9 - FINE
}