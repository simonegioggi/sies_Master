package siap.siep.statis.model;

/**
* <p>Title: DettaglioArchiviazioniCPPModel </p>
* <p>Description: Classe Model usato per foglio xls DETTAGLIO_ARCHIVIAZIONI </p>
* <p>	prodotto per la statistica 'Riepilogo procedimenti pendenti' per 	</p>
* <p>	la Classe VII (Fascicoi di Conversione Pene Pecuniarie)				</p>
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class DettaglioArchiviazioniCPPModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 5747699864189554140L;

	private BigDecimal mIdFascicoloSiep;
	private Integer mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private Date mDataIscrizione;
	private Date mDataArchiviazione;
	private Date mDataInizioSS;
	private Date mDataScadenzaSS;
	private String mCodTipoSanzione;
	private String mUltCodMotivo;
	private String mCognome;
	private String mNome;
	private String mDescTipoSanzione;
	private String mDescMotivoArch;
	private Integer mAnno;
	private String mPeriodo;

	// COSTRUTTORE DI DEFAULT
	public DettaglioArchiviazioniCPPModel() {
		this.mIdFascicoloSiep = null;
		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mDataIscrizione = null;
		this.mDataArchiviazione = null;
		this.mDataInizioSS = null;
		this.mDataScadenzaSS = null;
		this.mCodTipoSanzione = null;
		this.mUltCodMotivo = null;
		this.mCognome = null;
		this.mNome = null;
		this.mDescTipoSanzione = null;
		this.mDescMotivoArch = null;
		this.mAnno = null;
		this.mPeriodo = null;

	}

	// COSTRUTTORE MODEL
	public DettaglioArchiviazioniCPPModel(BigDecimal aIdFascicoloSiep, Integer aChiaveAnno,
			BigDecimal aChiaveProgr, Date aDataIscrizione, Date aDataArchiviazione, Date aDataInizioSS,
			Date aDataScadenzaSS, String aCodTipoSanzione, String aUltCodMotivo, String aCognome,
			String aNome, String aDescTipoSanzione, String aDescMotivoArch, Integer aAnno, String aPeriodo

	) {
		this.mIdFascicoloSiep = aIdFascicoloSiep;
		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveProgr = aChiaveProgr;
		this.mDataIscrizione = aDataIscrizione;
		this.mDataArchiviazione = aDataArchiviazione;
		this.mDataInizioSS = aDataInizioSS;
		this.mDataScadenzaSS = aDataScadenzaSS;
		this.mCodTipoSanzione = aCodTipoSanzione;
		this.mUltCodMotivo = aUltCodMotivo;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mDescTipoSanzione = aDescTipoSanzione;
		this.mDescMotivoArch = aDescMotivoArch;
		this.mAnno = aAnno;
		this.mPeriodo = aPeriodo;

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

	public Date getDataIscrizione() {
		return mDataIscrizione;
	}

	public Date getDataArchiviazione() {
		return mDataArchiviazione;
	}

	public Date getDataInizioSS() {
		return mDataInizioSS;
	}

	public Date getDataScadenzaSS() {
		return mDataScadenzaSS;
	}

	public String getCodTipoSanzione() {
		return mCodTipoSanzione;
	}

	public String getUltCodMotivo() {
		return mUltCodMotivo;
	}

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public String getDescTipoSanzione() {
		return mDescTipoSanzione;
	}

	public String getDescMotivoArch() {
		return mDescMotivoArch;
	}

	public Integer getAnno() {
		return mAnno;
	}

	public String getPeriodo() {
		return mPeriodo;
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

	public void setDataIscrizione(Date aValore) {
		mDataIscrizione = aValore;
	}

	public void setDataArchiviazione(Date aValore) {
		mDataArchiviazione = aValore;
	}

	public void setDataInizioSS(Date aValore) {
		mDataInizioSS = aValore;
	}

	public void setDataScadenzaSS(Date aValore) {
		mDataScadenzaSS = aValore;
	}

	public void setCodTiposanzione(String aValore) {
		mCodTipoSanzione = aValore;
	}

	public void setUltCodMotivo(String aValore) {
		mUltCodMotivo = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setDescTipoSanzione(String aValore) {
		mDescTipoSanzione = aValore;
	}

	public void setDescMotivoArch(String aValore) {
		mDescMotivoArch = aValore;
	}

	public void setAnno(Integer aValore) {
		mAnno = aValore;
	}

	public void setPeriodo(String aValore) {
		mPeriodo = aValore;
	}

}