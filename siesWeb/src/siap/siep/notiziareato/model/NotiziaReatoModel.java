package siap.siep.notiziareato.model;

/**
* <p>Title: NotiziaReatoModel</p>
* <p>Description: Classe Model che rappresenta il NotiziaReato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class NotiziaReatoModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = 4889308679695037722L;
	private BigDecimal mIdNotiziaReato;
	private String mProgrNotizia;
	private Date mDataPervenimento;
	private String mAcquisizioneDiretta;
	private Date mDataFatto;
	private String mCodFonte;
	private String mDescrFonte;
	private String mTipoFonte;
	private String mCodComuneFonte;
	private String mDescrComuneFonte;
	private String mNumRegAutorita;
	private String mLuogoProvenienza;
	private Date mDataAcquisizione;
	private String mNumeroRicevuta;
	private String mDescrizioneFonte;
	private BigDecimal mFasSieIdFascicoloSiep;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	// modifiche per integrazione REGE-SIES
	private String mFlagArrestato;
	private String mFlagFotosegnalato;
	private Date mDataArresto;
	private Date mDataFermo;
	private Date mDataFoto;
	private String mCodAutoritaFoto;
	private String mCodComuneFoto;
	private String mDescAutoritaFoto;
	private String mDescComuneFoto;
	private BigDecimal mFasIdFascicoloSige;

	// COSTRUTTORE DI DEFAULT
	public NotiziaReatoModel() {
		this.mIdNotiziaReato = null;
		this.mProgrNotizia = "";
		this.mDataPervenimento = null;
		this.mAcquisizioneDiretta = "";
		this.mDataFatto = null;
		this.mCodFonte = "";
		this.mDescrFonte = "";
		this.mTipoFonte = "";
		this.mCodComuneFonte = "";
		this.mDescrComuneFonte = "";
		this.mNumRegAutorita = "";
		this.mLuogoProvenienza = "";
		this.mDataAcquisizione = null;
		this.mNumeroRicevuta = "";
		this.mDescrizioneFonte = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		// modifiche per integrazione REGE-SIES
		this.mFlagArrestato = "";
		this.mFlagFotosegnalato = "";
		this.mDataArresto = null;
		this.mDataFermo = null;
		this.mDataFoto = null;
		this.mCodAutoritaFoto = "";
		this.mCodComuneFoto = "";
		this.mFasIdFascicoloSige = null;

	}

	// COSTRUTTORE DI COPIA
	public NotiziaReatoModel(NotiziaReatoModel aModel) {
		this.mIdNotiziaReato = aModel.mIdNotiziaReato;
		this.mProgrNotizia = aModel.mProgrNotizia;
		this.mDataPervenimento = aModel.mDataPervenimento;
		this.mAcquisizioneDiretta = aModel.mAcquisizioneDiretta;
		this.mDataFatto = aModel.mDataFatto;
		this.mCodFonte = aModel.mCodFonte;
		this.mDescrFonte = aModel.mDescrFonte;
		this.mTipoFonte = aModel.mTipoFonte;
		this.mCodComuneFonte = aModel.mCodComuneFonte;
		this.mDescrComuneFonte = aModel.mDescrComuneFonte;
		this.mNumRegAutorita = aModel.mNumRegAutorita;
		this.mLuogoProvenienza = aModel.mLuogoProvenienza;
		this.mDataAcquisizione = aModel.mDataAcquisizione;
		this.mNumeroRicevuta = aModel.mNumeroRicevuta;
		this.mDescrizioneFonte = aModel.mDescrizioneFonte;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		// modifiche per integrazione REGE-SIES
		this.mFlagArrestato = aModel.mFlagArrestato;
		this.mFlagFotosegnalato = aModel.mFlagFotosegnalato;
		this.mDataArresto = aModel.mDataArresto;
		this.mDataFermo = aModel.mDataFermo;
		this.mDataFoto = aModel.mDataFoto;
		this.mCodAutoritaFoto = aModel.mCodAutoritaFoto;
		this.mCodComuneFoto = aModel.mCodComuneFoto;
		this.mFasIdFascicoloSige = aModel.mFasIdFascicoloSige;

	}

	// COSTRUTTORE MODEL
	public NotiziaReatoModel(BigDecimal aIdNotiziaReato, String aProgrNotizia, Date aDataPervenimento,
			String aAcquisizioneDiretta, Date aDataFatto, String aCodFonte, String aDescrFonte,
			String aTipoFonte, String aCodComuneFonte, String aDescrComuneFonte, String aNumRegAutorita,
			String aLuogoProvenienza, Date aDataAcquisizione, String aNumeroRicevuta,
			String aDescrizioneFonte, BigDecimal aFasSieIdFascicoloSiep, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			Date aDataArresto, Date aDataFermo, String aFlagFotosegnalato, String aFlagArrestato,
			Date aDataFoto, String aCodAutoritaFoto, String aComuneFoto, BigDecimal aFasIdFascicoloSige

	) {
		this.mIdNotiziaReato = aIdNotiziaReato;
		this.mProgrNotizia = aProgrNotizia;
		this.mDataPervenimento = aDataPervenimento;
		this.mAcquisizioneDiretta = aAcquisizioneDiretta;
		this.mDataFatto = aDataFatto;
		this.mCodFonte = aCodFonte;
		this.mDescrFonte = aDescrFonte;
		this.mTipoFonte = aTipoFonte;
		this.mCodComuneFonte = aCodComuneFonte;
		this.mDescrComuneFonte = aDescrComuneFonte;
		this.mNumRegAutorita = aNumRegAutorita;
		this.mLuogoProvenienza = aLuogoProvenienza;
		this.mDataAcquisizione = aDataAcquisizione;
		this.mNumeroRicevuta = aNumeroRicevuta;
		this.mDescrizioneFonte = aDescrizioneFonte;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		// this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		// modifiche per integrazione REGE-SIES
		this.mDataArresto = aDataArresto;
		this.mDataFermo = aDataFermo;
		this.mFlagFotosegnalato = aFlagFotosegnalato;
		this.mFlagArrestato = aFlagArrestato;
		this.mDataFoto = aDataFoto;
		this.mCodAutoritaFoto = aCodAutoritaFoto;
		this.mCodComuneFoto = aComuneFoto;
		this.mFasIdFascicoloSige = aFasIdFascicoloSige;

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdNotiziaReato() {
		return mIdNotiziaReato;
	}

	public String getProgrNotizia() {
		return mProgrNotizia;
	}

	public Date getDataPervenimento() {
		return mDataPervenimento;
	}

	public String getAcquisizioneDiretta() {
		return mAcquisizioneDiretta;
	}

	public Date getDataFatto() {
		return mDataFatto;
	}

	public String getCodFonte() {
		return mCodFonte;
	}

	public String getDescrFonte() {
		return mDescrFonte;
	}

	public String getTipoFonte() {
		return mTipoFonte;
	}

	public String getCodComuneFonte() {
		return mCodComuneFonte;
	}

	public String getDescrComuneFonte() {
		return mDescrComuneFonte;
	}

	public String getNumRegAutorita() {
		return mNumRegAutorita;
	}

	public String getLuogoProvenienza() {
		return mLuogoProvenienza;
	}

	public Date getDataAcquisizione() {
		return mDataAcquisizione;
	}

	public String getNumeroRicevuta() {
		return mNumeroRicevuta;
	}

	public String getDescrizioneFonte() {
		return mDescrizioneFonte;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
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

	// modifiche integrazione REGE-SIES
	public Date getDataArresto() {
		return mDataArresto;
	}

	public String getFlagArrestato() {
		return mFlagArrestato;
	}

	public Date getDataFermo() {
		return mDataFermo;
	}

	public String getFlagFotosegnalato() {
		return mFlagFotosegnalato;
	}

	public Date getDataFoto() {
		return mDataFoto;
	}

	public String getCodAutoritaFoto() {
		return mCodAutoritaFoto;
	}

	public String getCodComuneFoto() {
		return mCodComuneFoto;
	}

	public String getDescAutoritaFoto() {
		return mDescAutoritaFoto;
	}

	public String getDescComuneFoto() {
		return mDescComuneFoto;
	}

	public BigDecimal getFasIdFascicoloSige() {
		return mFasIdFascicoloSige;
	}

	//
	// METODI SET()
	//

	public void setIdNotiziaReato(BigDecimal aValore) {
		mIdNotiziaReato = aValore;
	}

	public void setProgrNotizia(String aValore) {
		mProgrNotizia = aValore;
	}

	public void setDataPervenimento(Date aValore) {
		mDataPervenimento = aValore;
	}

	public void setAcquisizioneDiretta(String aValore) {
		mAcquisizioneDiretta = aValore;
	}

	public void setDataFatto(Date aValore) {
		mDataFatto = aValore;
	}

	public void setCodFonte(String aValore) {
		mCodFonte = aValore;
	}

	public void setDescrFonte(String aValore) {
		mDescrFonte = aValore;
	}

	public void setTipoFonte(String aValore) {
		mTipoFonte = aValore;
	}

	public void setCodComuneFonte(String aValore) {
		mCodComuneFonte = aValore;
	}

	public void setDescrComuneFonte(String aValore) {
		mDescrComuneFonte = aValore;
	}

	public void setNumRegAutorita(String aValore) {
		mNumRegAutorita = aValore;
	}

	public void setLuogoProvenienza(String aValore) {
		mLuogoProvenienza = aValore;
	}

	public void setDataAcquisizione(Date aValore) {
		mDataAcquisizione = aValore;
	}

	public void setNumeroRicevuta(String aValore) {
		mNumeroRicevuta = aValore;
	}

	public void setDescrizioneFonte(String aValore) {
		mDescrizioneFonte = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
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

	// modifiche integrazione REGE-SIES
	public void setDataArresto(Date dataArresto) {
		mDataArresto = dataArresto;
	}

	public void setDataFermo(Date dataFermo) {
		mDataFermo = dataFermo;
	}

	public void setFlagArrestato(String flagArrestato) {
		mFlagArrestato = flagArrestato;
	}

	public void setFlagFotosegnalato(String flagFotosegnalato) {
		mFlagFotosegnalato = flagFotosegnalato;
	}

	public void setDataFoto(Date dataFoto) {
		mDataFoto = dataFoto;
	}

	public void setCodAutoritaFoto(String autoritaFoto) {
		mCodAutoritaFoto = autoritaFoto;
	}

	public void setCodComuneFoto(String comuneFoto) {
		mCodComuneFoto = comuneFoto;
	}

	public void setDescAutoritaFoto(String descAutoritaFoto) {
		mDescAutoritaFoto = descAutoritaFoto;
	}

	public void setDescComuneFoto(String descComuneFoto) {
		mDescComuneFoto = descComuneFoto;
	}

	public void setFasIdFascicoloSige(BigDecimal aValore) {
		mFasIdFascicoloSige = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdNotiziaReato + " - " + mProgrNotizia + " - " + mDataPervenimento + " - "
				+ mAcquisizioneDiretta + " - " + mDataFatto + " - " + mCodFonte + " - " + mDescrFonte + " - "
				+ mTipoFonte + " - " + mCodComuneFonte + " - " + mDescrComuneFonte + " - " + mNumRegAutorita
				+ " - " + mLuogoProvenienza + " - " + mDataAcquisizione + " - " + mNumeroRicevuta + " - "
				+ mDescrizioneFonte + " - " + mFasSieIdFascicoloSiep + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento + " - " +
				// modifiche per integrazione REGE-SIES
				mFlagArrestato + " - " + mFlagFotosegnalato + " - " + mDataArresto + " - " + mDataFermo
				+ " - " + mDataFoto + " - " + mCodAutoritaFoto + " - " + mCodComuneFoto + " - "
				+ mDescAutoritaFoto + " - " + mDescComuneFoto + " - " + mFasIdFascicoloSige;

		return lStr;
	}

	public String toStringNotizia() {
		String lNotizia = "";

		if (getDescrComuneFonte() != null)
			lNotizia = this.getDescrComuneFonte();
		if (getDescrizioneFonte() != null)
			lNotizia += " " + this.getDescrizioneFonte();

		if (getDataPervenimento() != null)
			lNotizia += " in data " + DateUtils.getDateToString(this.getDataPervenimento(), "dd-MM-yyyy");

		return lNotizia;

	}
}
