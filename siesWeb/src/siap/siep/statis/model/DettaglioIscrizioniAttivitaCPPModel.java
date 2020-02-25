package siap.siep.statis.model;

/**
* <p>Title: DettaglioIscrizioniAttivitaCPPModel - Settembre 2015 </p>
* <p>Description: Classe Model che rappresenta il ISP_ISCRIZIONI_ATTIVITA_CPP</p>
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class DettaglioIscrizioniAttivitaCPPModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -6587087171394837793L;

	private BigDecimal mIdFascicoloSiep;
	private Integer mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private Date mDataArrivoAtto;
	private Date mDataTrasmissione;
	private Date mDataUltEventoPM;
	private Date mDataIscrizione;
	private Date mDataImpEsazione;
	private Date mDataEventoSorv;
	private String mCognome;
	private String mNome;
	private String mTipoDettaglio;
	private Integer mAnno;
	private String mPeriodo;

	// private String mDescTipoAutoritaEmittente;
	// private String mDescLuogoEmittente;
	// private String mDescSezioneAutorita;
	// private String mCodUfficioInserimento;
	// private BigDecimal mChiaveProgrOrig;
	// private String mDescUfficioInserimento;

	// COSTRUTTORE DI DEFAULT
	public DettaglioIscrizioniAttivitaCPPModel() {
		this.mIdFascicoloSiep = null;
		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mDataArrivoAtto = null;
		this.mDataTrasmissione = null;
		this.mDataUltEventoPM = null;
		this.mDataIscrizione = null;
		this.mDataImpEsazione = null;
		this.mDataEventoSorv = null;
		this.mCognome = null;
		this.mNome = null;
		this.mTipoDettaglio = null;
		this.mAnno = null;
		this.mPeriodo = null;

		/*
		 * this.mDescTipoAutoritaEmittente = null; this.mDescLuogoEmittente = null; this.mDescSezioneAutorita
		 * = null; this.mCodUfficioInserimento = null; this.mChiaveProgrOrig = null;
		 * this.mDescUfficioInserimento = null;
		 */
	}

	// COSTRUTTORE MODEL
	public DettaglioIscrizioniAttivitaCPPModel(BigDecimal aIdFascicoloSiep, Integer aChiaveAnno,
			BigDecimal aChiaveProgr, Date aDataArrivoAtto, Date aDataTrasmissione, Date aDataUltEventoPM,
			Date aDataIscrizione, Date aDataImpEsazione, Date aDataEventoSorv, String aCognome, String aNome,
			String aTipoDettaglio, Integer aAnno, String aPeriodo
	/*
	 * String aDescTipoAutoritaEmittente, String aDescLuogoEmittente, String aDescSezioneAutorita, String
	 * aCodUfficioInserimento, BigDecimal aChiaveProgrOrig, String aDescUfficioInserimento
	 */
	) {
		this.mIdFascicoloSiep = aIdFascicoloSiep;
		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveProgr = aChiaveProgr;
		this.mDataArrivoAtto = aDataArrivoAtto;
		this.mDataTrasmissione = aDataTrasmissione;
		this.mDataUltEventoPM = aDataUltEventoPM;
		this.mDataIscrizione = aDataIscrizione;
		this.mDataImpEsazione = aDataImpEsazione;
		this.mDataEventoSorv = aDataEventoSorv;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mTipoDettaglio = aTipoDettaglio;
		this.mAnno = aAnno;
		this.mPeriodo = aPeriodo;

		/*
		 * this.mDescTipoAutoritaEmittente = aDescTipoAutoritaEmittente; this.mDescLuogoEmittente =
		 * aDescLuogoEmittente; this.mDescSezioneAutorita = aDescSezioneAutorita; this.mCodUfficioInserimento
		 * = aCodUfficioInserimento; this.mChiaveProgrOrig = aChiaveProgrOrig; this.mDescUfficioInserimento =
		 * aDescUfficioInserimento;
		 */
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdFascicoloSiep() {
		return mIdFascicoloSiep;
	}

	public Integer getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public Date getDataArrivoAttoinCancelleria() {
		return mDataArrivoAtto;
	}

	public Date getDataTrasmissione() {
		return mDataTrasmissione;
	}

	public Date getDataUltEventoPM() {
		return mDataUltEventoPM;
	}

	public Date getDataIscrizione() {
		return mDataIscrizione;
	}

	public Date getDataImpEsazione() {
		return mDataImpEsazione;
	}

	public Date getDataEventoSorv() {
		return mDataEventoSorv;
	}

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public String getTipodettaglio() {
		return mTipoDettaglio;
	}

	public Integer getAnno() {
		return mAnno;
	}

	public String getPeriodo() {
		return mPeriodo;
	}
	/*
	 * public String getDescTipoAutoritaEmittente() { return mDescTipoAutoritaEmittente; } public String
	 * getDescLuogoEmittente() { return mDescLuogoEmittente; } public String getDescSezioneAutorita() { return
	 * mDescSezioneAutorita; } public String getCodUfficioInserimento() { return mCodUfficioInserimento; }
	 * public BigDecimal getChiaveProgrOrig() { return mChiaveProgrOrig; } public String
	 * getDescUfficioInserimento() { return mDescUfficioInserimento; }
	 */
	//
	// METODI SET()
	//

	public void setIdFascicoloSiep(BigDecimal aValore) {
		mIdFascicoloSiep = aValore;
	}

	public void setChiaveAnno(Integer aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setDataArrivoAttoinCancelleria(Date aValore) {
		mDataArrivoAtto = aValore;
	}

	public void setDataTrasmissione(Date aValore) {
		mDataTrasmissione = aValore;
	}

	public void setDataUltEventoPM(Date aValore) {
		mDataUltEventoPM = aValore;
	}

	public void setDataIscrizione(Date aValore) {
		mDataIscrizione = aValore;
	}

	public void setDataImpEsazione(Date aValore) {
		mDataImpEsazione = aValore;
	}

	public void setDataEventoSorv(Date aValore) {
		mDataEventoSorv = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setTipoDettaglio(String aValore) {
		mTipoDettaglio = aValore;
	}

	public void setAnno(Integer aValore) {
		mAnno = aValore;
	}

	public void setPeriodo(String aValore) {
		mPeriodo = aValore;
	}

	/*
	 * public void setDescTipoAutoritaEmittente(String aValore) {mDescTipoAutoritaEmittente = aValore; }
	 * public void setDescLuogoEmittente(String aValore) {mDescLuogoEmittente = aValore; } public void
	 * setDescSezioneAutorita(String aValore) {mDescSezioneAutorita = aValore; } public void
	 * setCodUfficioInserimento(String aValore ) { mCodUfficioInserimento = aValore; } public void
	 * setChiaveProgrOrig(BigDecimal aValore ) { mChiaveProgrOrig = aValore; } public void
	 * setdescUfficioInserimento(String aValore ) { mDescUfficioInserimento = aValore; }
	 */

}