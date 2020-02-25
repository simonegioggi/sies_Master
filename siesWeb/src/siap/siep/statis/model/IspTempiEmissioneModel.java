package siap.siep.statis.model;

/**
* <p>Title: IspTempiEmissioneModel</p>
* <p>Description: Classe Model che rappresenta il IspTempiEmissione</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class IspTempiEmissioneModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -1593165987637502563L;
	private BigDecimal mIdFascicoloSiep;
	private Integer mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private String mCodUfficio;
	private String mCodMagistrato;
	private String mDescrMagistrato;
	private Date mDataPassatoGiudicato;
	private Date mDataArrivoAtto;
	private BigDecimal mTempiGiudicatoArrivo;
	private Date mDataIscrizioneFascicolo;
	private BigDecimal mTempiArrivoIscrizione;
	private BigDecimal mIdEventoPrimoAtto;
	private Date mDataPrimoAtto;
	private String mCodMotivoPrimoAtto;
	private String mDescrizioneMotivoPrimoAtto;
	private BigDecimal mIdEventoEsecuzione;
	private Date mDataOrdineEsecuzione;
	private String mCodMotivoEsecuzione;
	private String mDescrizioneMotivoEsecuzione;
	private BigDecimal mTempoIscrizioneEmissione;
	private Date mDataInizioIstruttoria;
	private Date mDataFineIstruttoria;
	private BigDecimal mTempiIstruttoria;
	private Date mDataInizioInattivita;
	private Date mDataFineInattivita;
	private BigDecimal mTempiInattivita;
	private BigDecimal mGiorniDaNeutralizzare;
	// NGG Statistiche SIEP
	private String mCodUfficioInserimento;
	private BigDecimal mChiaveProgrOrig;
	private String mDescUfficioInserimento;

	/**
	 * COSTRUTTORE DI DEFAULT
	 */
	public IspTempiEmissioneModel() {
		this.mIdFascicoloSiep = null;
		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mCodUfficio = "";
		this.mCodMagistrato = "";
		this.mDescrMagistrato = "";
		this.mDataPassatoGiudicato = null;
		this.mDataArrivoAtto = null;
		this.mTempiGiudicatoArrivo = null;
		this.mDataIscrizioneFascicolo = null;
		this.mTempiArrivoIscrizione = null;
		this.mIdEventoPrimoAtto = null;
		this.mDataPrimoAtto = null;
		this.mCodMotivoPrimoAtto = "";
		this.mDescrizioneMotivoPrimoAtto = "";
		this.mIdEventoEsecuzione = null;
		this.mDataOrdineEsecuzione = null;
		this.mCodMotivoEsecuzione = "";
		this.mDescrizioneMotivoEsecuzione = "";
		this.mTempoIscrizioneEmissione = null;
		this.mDataInizioIstruttoria = null;
		this.mDataFineIstruttoria = null;
		this.mTempiIstruttoria = null;
		this.mDataInizioInattivita = null;
		this.mDataFineInattivita = null;
		this.mTempiInattivita = null;
		this.mGiorniDaNeutralizzare = null;
		// NGG Statistiche SIEP
		this.mCodUfficioInserimento = null;
		this.mChiaveProgrOrig = null;
		this.mDescUfficioInserimento = null;
	}

	/**
	 * COSTRUTTORE DI COPIA
	 */
	public IspTempiEmissioneModel(IspTempiEmissioneModel aModel) {
		this.mIdFascicoloSiep = aModel.mIdFascicoloSiep;
		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mChiaveProgr = aModel.mChiaveProgr;
		this.mCodUfficio = aModel.mCodUfficio;
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mDescrMagistrato = aModel.mDescrMagistrato;
		this.mDataPassatoGiudicato = aModel.mDataPassatoGiudicato;
		this.mDataArrivoAtto = aModel.mDataArrivoAtto;
		this.mTempiGiudicatoArrivo = aModel.mTempiGiudicatoArrivo;
		this.mDataIscrizioneFascicolo = aModel.mDataIscrizioneFascicolo;
		this.mTempiArrivoIscrizione = aModel.mTempiArrivoIscrizione;
		this.mIdEventoPrimoAtto = aModel.mIdEventoPrimoAtto;
		this.mDataPrimoAtto = aModel.mDataPrimoAtto;
		this.mCodMotivoPrimoAtto = aModel.mCodMotivoPrimoAtto;
		this.mDescrizioneMotivoPrimoAtto = aModel.mDescrizioneMotivoPrimoAtto;
		this.mIdEventoEsecuzione = aModel.mIdEventoEsecuzione;
		this.mDataOrdineEsecuzione = aModel.mDataOrdineEsecuzione;
		this.mCodMotivoEsecuzione = aModel.mCodMotivoEsecuzione;
		this.mDescrizioneMotivoEsecuzione = aModel.mDescrizioneMotivoEsecuzione;
		this.mTempoIscrizioneEmissione = aModel.mTempoIscrizioneEmissione;
		this.mDataInizioIstruttoria = aModel.mDataInizioIstruttoria;
		this.mDataFineIstruttoria = aModel.mDataFineIstruttoria;
		this.mTempiIstruttoria = aModel.mTempiIstruttoria;
		this.mDataInizioInattivita = aModel.mDataInizioInattivita;
		this.mDataFineInattivita = aModel.mDataFineInattivita;
		this.mTempiInattivita = aModel.mTempiInattivita;
		this.mGiorniDaNeutralizzare = aModel.mGiorniDaNeutralizzare;
		// NGG Statistiche SIEP
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mChiaveProgrOrig = aModel.mChiaveProgrOrig;
		this.mDescUfficioInserimento = aModel.mDescUfficioInserimento;
	}

	/**
	 * COSTRUTTORE MODEL
	 */
	public IspTempiEmissioneModel(BigDecimal aIdFascicoloSiep, Integer aChiaveAnno, BigDecimal aChiaveProgr,
			String aCodUfficio, String aCodMagistrato, String aDescrMagistrato, Date aDataPassatoGiudicato,
			Date aDataArrivoAtto, BigDecimal aTempiGiudicatoArrivo, Date aDataIscrizioneFascicolo,
			BigDecimal aTempiArrivoIscrizione, BigDecimal aIdEventoPrimoAtto, Date aDataPrimoAtto,
			String aCodMotivoPrimoAtto, String aDescrizioneMotivoPrimoAtto, BigDecimal aIdEventoEsecuzione,
			Date aDataOrdineEsecuzione, String aCodMotivoEsecuzione, String aDescrizioneMotivoEsecuzione,
			BigDecimal aTempoIscrizioneEmissione, Date aDataInizioIstruttoria, Date aDataFineIstruttoria,
			BigDecimal aTempiIstruttoria, Date aDataInizioInattivita, Date aDataFineInattivita,
			BigDecimal aTempiInattivita, BigDecimal aGiorniDaNeutralizzare,
			// NGG Statistiche SIEP
			String aCodUfficioInserimento, BigDecimal aChiaveProgrOrig, String aDescUfficioInserimento) {
		this.mIdFascicoloSiep = aIdFascicoloSiep;
		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveProgr = aChiaveProgr;
		this.mCodUfficio = aCodUfficio;
		this.mCodMagistrato = aCodMagistrato;
		this.mDescrMagistrato = aDescrMagistrato;
		this.mDataPassatoGiudicato = aDataPassatoGiudicato;
		this.mDataArrivoAtto = aDataArrivoAtto;
		this.mTempiGiudicatoArrivo = aTempiGiudicatoArrivo;
		this.mDataIscrizioneFascicolo = aDataIscrizioneFascicolo;
		this.mTempiArrivoIscrizione = aTempiArrivoIscrizione;
		this.mIdEventoPrimoAtto = aIdEventoPrimoAtto;
		this.mDataPrimoAtto = aDataPrimoAtto;
		this.mCodMotivoPrimoAtto = aCodMotivoPrimoAtto;
		this.mDescrizioneMotivoPrimoAtto = aDescrizioneMotivoPrimoAtto;
		this.mIdEventoEsecuzione = aIdEventoEsecuzione;
		this.mDataOrdineEsecuzione = aDataOrdineEsecuzione;
		this.mCodMotivoEsecuzione = aCodMotivoEsecuzione;
		this.mDescrizioneMotivoEsecuzione = aDescrizioneMotivoEsecuzione;
		this.mTempoIscrizioneEmissione = aTempoIscrizioneEmissione;
		this.mDataInizioIstruttoria = aDataInizioIstruttoria;
		this.mDataFineIstruttoria = aDataFineIstruttoria;
		this.mTempiIstruttoria = aTempiIstruttoria;
		this.mDataInizioInattivita = aDataInizioInattivita;
		this.mDataFineInattivita = aDataFineInattivita;
		this.mTempiInattivita = aTempiInattivita;
		this.mGiorniDaNeutralizzare = aGiorniDaNeutralizzare;
		// NGG Statistiche SIEP
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mChiaveProgrOrig = aChiaveProgrOrig;
		this.mDescUfficioInserimento = aDescUfficioInserimento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdFascicoloSiep() {
		return mIdFascicoloSiep;
	}

	public Integer getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public String getCodUfficio() {
		return mCodUfficio;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public String getDescrMagistrato() {
		return mDescrMagistrato;
	}

	public Date getDataPassatoGiudicato() {
		return mDataPassatoGiudicato;
	}

	public Date getDataArrivoAtto() {
		return mDataArrivoAtto;
	}

	public BigDecimal getTempiGiudicatoArrivo() {
		return mTempiGiudicatoArrivo;
	}

	public Date getDataIscrizioneFascicolo() {
		return mDataIscrizioneFascicolo;
	}

	public BigDecimal getTempiArrivoIscrizione() {
		return mTempiArrivoIscrizione;
	}

	public BigDecimal getIdEventoPrimoAtto() {
		return mIdEventoPrimoAtto;
	}

	public Date getDataPrimoAtto() {
		return mDataPrimoAtto;
	}

	public String getCodMotivoPrimoAtto() {
		return mCodMotivoPrimoAtto;
	}

	public String getDescrizioneMotivoPrimoAtto() {
		return mDescrizioneMotivoPrimoAtto;
	}

	public BigDecimal getIdEventoEsecuzione() {
		return mIdEventoEsecuzione;
	}

	public Date getDataOrdineEsecuzione() {
		return mDataOrdineEsecuzione;
	}

	public String getCodMotivoEsecuzione() {
		return mCodMotivoEsecuzione;
	}

	public String getDescrizioneMotivoEsecuzione() {
		return mDescrizioneMotivoEsecuzione;
	}

	public BigDecimal getTempoIscrizioneEmissione() {
		return mTempoIscrizioneEmissione;
	}

	public Date getDataInizioIstruttoria() {
		return mDataInizioIstruttoria;
	}

	public Date getDataFineIstruttoria() {
		return mDataFineIstruttoria;
	}

	public BigDecimal getTempiIstruttoria() {
		return mTempiIstruttoria;
	}

	public Date getDataInizioInattivita() {
		return mDataInizioInattivita;
	}

	public Date getDataFineInattivita() {
		return mDataFineInattivita;
	}

	public BigDecimal getTempiInattivita() {
		return mTempiInattivita;
	}

	public BigDecimal getGiorniDaNeutralizzare() {
		return mGiorniDaNeutralizzare;
	}

	// NGG Statistiche SIEP
	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public BigDecimal getChiaveProgrOrig() {
		return mChiaveProgrOrig;
	}

	public String getDescUfficioInserimento() {
		return mDescUfficioInserimento;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdFascicoloSiep(BigDecimal aValore) {
		mIdFascicoloSiep = aValore;
	}

	public void setChiaveAnno(Integer aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setDescrMagistrato(String aValore) {
		mDescrMagistrato = aValore;
	}

	public void setDataPassatoGiudicato(Date aValore) {
		mDataPassatoGiudicato = aValore;
	}

	public void setDataArrivoAtto(Date aValore) {
		mDataArrivoAtto = aValore;
	}

	public void setTempiGiudicatoArrivo(BigDecimal aValore) {
		mTempiGiudicatoArrivo = aValore;
	}

	public void setDataIscrizioneFascicolo(Date aValore) {
		mDataIscrizioneFascicolo = aValore;
	}

	public void setTempiArrivoIscrizione(BigDecimal aValore) {
		mTempiArrivoIscrizione = aValore;
	}

	public void setIdEventoPrimoAtto(BigDecimal aValore) {
		mIdEventoPrimoAtto = aValore;
	}

	public void setDataPrimoAtto(Date aValore) {
		mDataPrimoAtto = aValore;
	}

	public void setCodMotivoPrimoAtto(String aValore) {
		mCodMotivoPrimoAtto = aValore;
	}

	public void setDescrizioneMotivoPrimoAtto(String aValore) {
		mDescrizioneMotivoPrimoAtto = aValore;
	}

	public void setIdEventoEsecuzione(BigDecimal aValore) {
		mIdEventoEsecuzione = aValore;
	}

	public void setDataOrdineEsecuzione(Date aValore) {
		mDataOrdineEsecuzione = aValore;
	}

	public void setCodMotivoEsecuzione(String aValore) {
		mCodMotivoEsecuzione = aValore;
	}

	public void setDescrizioneMotivoEsecuzione(String aValore) {
		mDescrizioneMotivoEsecuzione = aValore;
	}

	public void setTempoIscrizioneEmissione(BigDecimal aValore) {
		mTempoIscrizioneEmissione = aValore;
	}

	public void setDataInizioIstruttoria(Date aValore) {
		mDataInizioIstruttoria = aValore;
	}

	public void setDataFineIstruttoria(Date aValore) {
		mDataFineIstruttoria = aValore;
	}

	public void setTempiIstruttoria(BigDecimal aValore) {
		mTempiIstruttoria = aValore;
	}

	public void setDataInizioInattivita(Date aValore) {
		mDataInizioInattivita = aValore;
	}

	public void setDataFineInattivita(Date aValore) {
		mDataFineInattivita = aValore;
	}

	public void setTempiInattivita(BigDecimal aValore) {
		mTempiInattivita = aValore;
	}

	public void setGiorniDaNeutralizzare(BigDecimal aValore) {
		mGiorniDaNeutralizzare = aValore;
	}

	// NGG Statistiche SIEP
	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setChiaveProgrOrig(BigDecimal aValore) {
		mChiaveProgrOrig = aValore;
	}

	public void setDescUfficioInserimento(String aValore) {
		mDescUfficioInserimento = aValore;
	}

}
