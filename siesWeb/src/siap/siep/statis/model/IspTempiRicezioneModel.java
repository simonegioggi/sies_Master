package siap.siep.statis.model;

/**
* <p>Title: IspTempiRicezioneModel</p>
* <p>Description: Classe Model che rappresenta il IspTempiRicezione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class IspTempiRicezioneModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = 5519741604159573842L;
	private BigDecimal mIdFascicoloSiep;
	private Integer mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private String mCodUfficio;
	private String mDescrUfficio;
	private Date mDataArrivoAtto;
	private Date mDataIrrevocabilita;
	private Integer mTempoGiudicatoRicezione;
	private String mDescTipoAutoritaEmittente;
	private String mDescLuogoEmittente;
	private String mDescSezioneAutorita;
	// NGG Statistiche SIEP
	private String mCodUfficioInserimento;
	private BigDecimal mChiaveProgrOrig;
	private String mDescUfficioInserimento;

	// COSTRUTTORE DI DEFAULT
	public IspTempiRicezioneModel() {
		this.mIdFascicoloSiep = null;
		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mCodUfficio = "";
		this.mDescrUfficio = "";
		this.mDataArrivoAtto = null;
		this.mDataIrrevocabilita = null;
		this.mTempoGiudicatoRicezione = null;
		this.mDescTipoAutoritaEmittente = null;
		this.mDescLuogoEmittente = null;
		this.mDescSezioneAutorita = null;
		// NGG Statistiche SIEP
		this.mCodUfficioInserimento = null;
		this.mChiaveProgrOrig = null;
		this.mDescUfficioInserimento = null;
	}

	// COSTRUTTORE MODEL
	public IspTempiRicezioneModel(BigDecimal aIdFascicoloSiep, Integer aChiaveAnno, BigDecimal aChiaveProgr,
			String aCodUfficio, String aDescrUfficio, Date aDataArrivoAtto, Date aDataIrrevocabilita,
			Integer aTempoGiudicatoRicezione, String aDescTipoAutoritaEmittente, String aDescLuogoEmittente,
			String aDescSezioneAutorita,
			// NGG Statistiche SIEP
			String aCodUfficioInserimento, BigDecimal aChiaveProgrOrig, String aDescUfficioInserimento) {
		this.mIdFascicoloSiep = aIdFascicoloSiep;
		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveProgr = aChiaveProgr;
		this.mCodUfficio = aCodUfficio;
		this.mDescrUfficio = aDescrUfficio;
		this.mDataArrivoAtto = aDataArrivoAtto;
		this.mDataIrrevocabilita = aDataIrrevocabilita;
		this.mTempoGiudicatoRicezione = aTempoGiudicatoRicezione;
		this.mDescTipoAutoritaEmittente = aDescTipoAutoritaEmittente;
		this.mDescLuogoEmittente = aDescLuogoEmittente;
		this.mDescSezioneAutorita = aDescSezioneAutorita;
		// NGG Statistiche SIEP
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mChiaveProgrOrig = aChiaveProgrOrig;
		this.mDescUfficioInserimento = aDescUfficioInserimento;
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

	public String getCodUfficio() {
		return mCodUfficio;
	}

	public String getDescrUfficio() {
		return mDescrUfficio;
	}

	public Date getDataArrivoAtto() {
		return mDataArrivoAtto;
	}

	public Date getDataIrrevocabilita() {
		return mDataIrrevocabilita;
	}

	public Integer getTempoGiudicatoRicezione() {
		return mTempoGiudicatoRicezione;
	}

	public String getDescTipoAutoritaEmittente() {
		return mDescTipoAutoritaEmittente;
	}

	public String getDescLuogoEmittente() {
		return mDescLuogoEmittente;
	}

	public String getDescSezioneAutorita() {
		return mDescSezioneAutorita;
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

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

	public void setDescrUfficio(String aValore) {
		mDescrUfficio = aValore;
	}

	public void setDataArrivoAtto(Date aValore) {
		mDataArrivoAtto = aValore;
	}

	public void setDataIrrevocabilita(Date aValore) {
		mDataIrrevocabilita = aValore;
	}

	public void setTempoGiudicatoRicezione(Integer aValore) {
		mTempoGiudicatoRicezione = aValore;
	}

	public void setDescTipoAutoritaEmittente(String aValore) {
		mDescTipoAutoritaEmittente = aValore;
	}

	public void setDescLuogoEmittente(String aValore) {
		mDescLuogoEmittente = aValore;
	}

	public void setDescSezioneAutorita(String aValore) {
		mDescSezioneAutorita = aValore;
	}

	// NGG Statistiche SIEP
	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setChiaveProgrOrig(BigDecimal aValore) {
		mChiaveProgrOrig = aValore;
	}

	public void setdescUfficioInserimento(String aValore) {
		mDescUfficioInserimento = aValore;
	}

}
