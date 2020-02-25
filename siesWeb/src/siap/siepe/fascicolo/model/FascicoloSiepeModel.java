package siap.siepe.fascicolo.model;

/**
* <p>Title: FascicoloSiepeModel</p>
* <p>Description: Classe Model che rappresenta il FascicoloSiepe</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class FascicoloSiepeModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -903145581679934873L;

	private BigDecimal mIdFascicoloSiepe;
	private BigDecimal mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private String mChiaveUfficio;
	private BigDecimal mNumUepe;
	private BigDecimal mAnnoUepe;
	private BigDecimal mProgrUepe;
	private String mCodStatoFascicolo;
	private String mDescrStatoFascicolo;
	private String mCodOperatoreInserimento;
	private Date mDataIscrizione;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mSogIdSoggetto;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mFasSiuIdFascicoloSius;
	private String mCodIncarico;
	private String mDescrIncarico;
	private String mNote;
	private String mCodUfficioMittente;
	private String mDescrUfficioMittente;
	private BigDecimal mEveIdEvento;
	private String mTipoDefinizione;
	private String mDescrTipoDefinizione;
	private Date mDataDefinizione;
	private String mDescrDefinizione;

	// COSTRUTTORE DI DEFAULT
	public FascicoloSiepeModel() {
		this.mIdFascicoloSiepe = null;
		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mChiaveUfficio = "";
		this.mNumUepe = null;
		this.mAnnoUepe = null;
		this.mProgrUepe = null;
		this.mCodStatoFascicolo = "";
		this.mDescrStatoFascicolo = "";
		this.mCodOperatoreInserimento = "";
		this.mDataIscrizione = null;
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mSogIdSoggetto = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mFasSiuIdFascicoloSius = null;
		this.mCodIncarico = "";
		this.mDescrIncarico = "";
		this.mNote = "";
		this.mCodUfficioMittente = "";
		this.mDescrUfficioMittente = "";
		this.mEveIdEvento = null;
		this.mTipoDefinizione = null;
		this.mDescrTipoDefinizione = "";
		this.mDataDefinizione = null;
		this.mDescrDefinizione = "";
	}

	// COSTRUTTORE DI COPIA
	public FascicoloSiepeModel(FascicoloSiepeModel aModel) {
		this.mIdFascicoloSiepe = aModel.mIdFascicoloSiepe;
		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mChiaveProgr = aModel.mChiaveProgr;
		this.mChiaveUfficio = aModel.mChiaveUfficio;
		this.mNumUepe = aModel.mNumUepe;
		this.mAnnoUepe = aModel.mAnnoUepe;
		this.mProgrUepe = aModel.mProgrUepe;
		this.mCodStatoFascicolo = aModel.mCodStatoFascicolo;
		this.mDescrStatoFascicolo = aModel.mDescrStatoFascicolo;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataIscrizione = aModel.mDataIscrizione;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mSogIdSoggetto = aModel.mSogIdSoggetto;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mCodIncarico = aModel.mCodIncarico;
		this.mDescrIncarico = aModel.mDescrIncarico;
		this.mNote = aModel.mNote;
		this.mCodUfficioMittente = aModel.mCodUfficioMittente;
		this.mDescrUfficioMittente = aModel.mDescrUfficioMittente;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mTipoDefinizione = aModel.mTipoDefinizione;
		this.mDescrTipoDefinizione = aModel.mDescrTipoDefinizione;
		this.mDataDefinizione = aModel.mDataDefinizione;
		this.mDescrDefinizione = aModel.mDescrDefinizione;
	}

	// COSTRUTTORE MODEL
	public FascicoloSiepeModel(BigDecimal aIdFascicoloSiepe, BigDecimal aChiaveAnno, BigDecimal aChiaveProgr,
			String aChiaveUfficio, BigDecimal aNumUepe, BigDecimal aAnnoUepe, BigDecimal aProgrUepe,
			String aCodStatoFascicolo, String aDescrStatoFascicolo, String aCodOperatoreInserimento,
			Date aDataIscrizione, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, BigDecimal aSogIdSoggetto,
			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aFasSiuIdFascicoloSius, String aCodIncarico,
			String aDescrIncarico, String aNote, String aCodUfficioMittente, String aDescrUfficioMittente,
			BigDecimal aEveIdEvento, String aTipoDefinizione, String aDescrTipoDefinizione,
			Date aDataDefinizione, String aDescrDefinizione) {
		this.mIdFascicoloSiepe = aIdFascicoloSiepe;
		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveProgr = aChiaveProgr;
		this.mChiaveUfficio = aChiaveUfficio;
		this.mNumUepe = aNumUepe;
		this.mAnnoUepe = aAnnoUepe;
		this.mProgrUepe = aProgrUepe;
		this.mCodStatoFascicolo = aCodStatoFascicolo;
		this.mDescrStatoFascicolo = aDescrStatoFascicolo;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataIscrizione = aDataIscrizione;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mSogIdSoggetto = aSogIdSoggetto;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mCodIncarico = aCodIncarico;
		this.mDescrIncarico = aDescrIncarico;
		this.mNote = aNote;
		this.mCodUfficioMittente = aCodUfficioMittente;
		this.mDescrUfficioMittente = aDescrUfficioMittente;
		this.mEveIdEvento = aEveIdEvento;
		this.mTipoDefinizione = aTipoDefinizione;
		this.mDescrTipoDefinizione = aDescrTipoDefinizione;
		this.mDataDefinizione = aDataDefinizione;
		this.mDescrDefinizione = aDescrDefinizione;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdFascicoloSiepe() {
		return mIdFascicoloSiepe;
	}

	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public String getChiaveUfficio() {
		return mChiaveUfficio;
	}

	public BigDecimal getNumUepe() {
		return mNumUepe;
	}

	public BigDecimal getAnnoUepe() {
		return mAnnoUepe;
	}

	public BigDecimal getProgrUepe() {
		return mProgrUepe;
	}

	public String getCodStatoFascicolo() {
		return mCodStatoFascicolo;
	}

	public String getDescrStatoFascicolo() {
		return mDescrStatoFascicolo;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataIscrizione() {
		return mDataIscrizione;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public BigDecimal getSogIdSoggetto() {
		return mSogIdSoggetto;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public String getCodIncarico() {
		return mCodIncarico;
	}

	public String getDescrIncarico() {
		return mDescrIncarico;
	}

	public String getNote() {
		return mNote;
	}

	public String getCodUfficioMittente() {
		return mCodUfficioMittente;
	}

	public String getDescrUfficioMittente() {
		return mDescrUfficioMittente;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public String getTipoDefinizione() {
		return mTipoDefinizione;
	}

	public String getDescrTipoDefinizione() {
		return mDescrTipoDefinizione;
	}

	public Date getDataDefinizione() {
		return mDataDefinizione;
	}

	public String getDescrDefinizione() {
		return mDescrDefinizione;
	}

	//
	// METODI SET()
	//

	public void setIdFascicoloSiepe(BigDecimal aValore) {
		mIdFascicoloSiepe = aValore;
	}

	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setChiaveUfficio(String aValore) {
		mChiaveUfficio = aValore;
	}

	public void setNumUepe(BigDecimal aValore) {
		mNumUepe = aValore;
	}

	public void setAnnoUepe(BigDecimal aValore) {
		mAnnoUepe = aValore;
	}

	public void setProgrUepe(BigDecimal aValore) {
		mProgrUepe = aValore;
	}

	public void setCodStatoFascicolo(String aValore) {
		mCodStatoFascicolo = aValore;
	}

	public void setDescrStatoFascicolo(String aValore) {
		mDescrStatoFascicolo = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataIscrizione(Date aValore) {
		mDataIscrizione = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public void setSogIdSoggetto(BigDecimal aValore) {
		mSogIdSoggetto = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setCodIncarico(String aValore) {
		mCodIncarico = aValore;
	}

	public void setDescrIncarico(String aValore) {
		mDescrIncarico = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setCodUfficioMittente(String aValore) {
		mCodUfficioMittente = aValore;
	}

	public void setDescrUfficioMittente(String aValore) {
		mDescrUfficioMittente = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setTipoDefinizione(String aValore) {
		mTipoDefinizione = aValore;
	}

	public void setDescrTipoDefinizione(String aValore) {
		mDescrTipoDefinizione = aValore;
	}

	public void setDataDefinizione(Date aValore) {
		mDataDefinizione = aValore;
	}

	public void setDescrDefinizione(String aValore) {
		mDescrDefinizione = aValore;
	}

}