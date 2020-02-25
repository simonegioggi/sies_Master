package siap.siep.penaresidua.model;

/**
* <p>Title: PenaResiduaModel</p>
* <p>Description: Classe Model che rappresenta il PenaResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.siep.posizione.model.PosizioneGiuridicaModel;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class PenaPrecedenteModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 3767643200815725973L;

	private BigDecimal mIdPenaResidua;
	private Date mDataInizio;
	private Date mDataFine;
	private BigDecimal mNumAnniReclusione;
	private BigDecimal mNumMesiReclusione;
	private BigDecimal mNumGiorniReclusione;
	private BigDecimal mImportoMulta;
	private BigDecimal mNumAnniArresto;
	private BigDecimal mNumMesiArresto;
	private BigDecimal mNumGiorniArresto;
	private BigDecimal mImportoAmmenda;
	private String mDiesAQuo;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mEveIdEvento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mMisAltIdMisuraAlternativa;

	private String mFlagValidato;
	private Date mDataFinePresunta;
	private Date mDataFineReclusione;
	private Date mDataInizioArresto;
	private String mFlagErgastolo;
	private Date mDataInzioIsolamentoDiurno;
	private Date mDataFineIsolamentoDiurno;
	private BigDecimal mNumAnniIsolamentoDiurno;
	private BigDecimal mNumMesiIsolamentoDiurno;
	private BigDecimal mNumGiorniIsolamentoDiurno;

	private String mStringaReclusione;
	private String mStringaArresto;
	private String mImmediataScarcerazione;
	private String mFlagPenaSospesa;

	private String mStringaIsolamentoDiurno;

	/**
	 * COSTRUTTORE DI DEFAULT
	 */
	public PenaPrecedenteModel() {
		this.mIdPenaResidua = null;
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mNumAnniReclusione = null;
		this.mNumMesiReclusione = null;
		this.mNumGiorniReclusione = null;
		this.mImportoMulta = null;
		this.mNumAnniArresto = null;
		this.mNumMesiArresto = null;
		this.mNumGiorniArresto = null;
		this.mImportoAmmenda = null;
		this.mDiesAQuo = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mEveIdEvento = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mFlagValidato = "";
		this.mDataFinePresunta = null;
		this.mDataFineReclusione = null;
		this.mDataInizioArresto = null;
		this.mFlagErgastolo = "";
		this.mDataInzioIsolamentoDiurno = null;
		this.mDataFineIsolamentoDiurno = null;
		this.mNumAnniIsolamentoDiurno = null;
		this.mNumMesiIsolamentoDiurno = null;
		this.mNumGiorniIsolamentoDiurno = null;
		this.mMisAltIdMisuraAlternativa = null;
		this.mImmediataScarcerazione = "";
		this.mFlagPenaSospesa = "";
	}

	/**
	 * COSTRUTTORE DI COPIA
	 */
	public PenaPrecedenteModel(PenaPrecedenteModel aModel) {
		this.mIdPenaResidua = aModel.mIdPenaResidua;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mNumAnniReclusione = aModel.mNumAnniReclusione;
		this.mNumMesiReclusione = aModel.mNumMesiReclusione;
		this.mNumGiorniReclusione = aModel.mNumGiorniReclusione;
		this.mImportoMulta = aModel.mImportoMulta;
		this.mNumAnniArresto = aModel.mNumAnniArresto;
		this.mNumMesiArresto = aModel.mNumMesiArresto;
		this.mNumGiorniArresto = aModel.mNumGiorniArresto;
		this.mImportoAmmenda = aModel.mImportoAmmenda;
		this.mDiesAQuo = aModel.mDiesAQuo;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mFlagValidato = aModel.mFlagValidato;
		this.mDataFinePresunta = aModel.mDataFinePresunta;
		this.mDataFineReclusione = aModel.mDataFineReclusione;
		this.mDataInizioArresto = aModel.mDataInizioArresto;
		this.mFlagErgastolo = aModel.mFlagErgastolo;
		this.mDataInzioIsolamentoDiurno = aModel.mDataInzioIsolamentoDiurno;
		this.mDataFineIsolamentoDiurno = aModel.mDataFineIsolamentoDiurno;
		this.mNumAnniIsolamentoDiurno = aModel.mNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aModel.mNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aModel.mNumGiorniIsolamentoDiurno;
		this.mMisAltIdMisuraAlternativa = aModel.mMisAltIdMisuraAlternativa;
		this.mImmediataScarcerazione = aModel.mImmediataScarcerazione;
		this.mFlagPenaSospesa = aModel.mFlagPenaSospesa;

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdPenaResidua() {
		return mIdPenaResidua;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public BigDecimal getNumAnniReclusione() {
		if (mNumAnniReclusione != null)
			return mNumAnniReclusione;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumMesiReclusione() {
		if (mNumMesiReclusione != null)
			return mNumMesiReclusione;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumGiorniReclusione() {
		if (mNumGiorniReclusione != null)
			return mNumGiorniReclusione;
		else
			return new BigDecimal(0);
	}

	// public BigDecimal getImportoMulta() { return mImportoMulta; }
	public BigDecimal getImportoMulta() {
		if (mImportoMulta != null)
			return mImportoMulta;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumAnniArresto() {
		if (mNumAnniArresto != null)
			return mNumAnniArresto;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumMesiArresto() {
		if (mNumMesiArresto != null)
			return mNumMesiArresto;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumGiorniArresto() {
		if (mNumGiorniArresto != null)
			return mNumGiorniArresto;
		else
			return new BigDecimal(0);
	}

	// public BigDecimal getImportoAmmenda() { return mImportoAmmenda; }
	public BigDecimal getImportoAmmenda() {
		if (mImportoAmmenda != null)
			return mImportoAmmenda;
		else
			return new BigDecimal(0);
	}

	public String getDiesAQuo() {
		return mDiesAQuo;
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

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public String getFlagValidato() {
		return mFlagValidato;
	}

	public Date getDataFinePresunta() {
		return mDataFinePresunta;
	}

	public Date getDataFineReclusione() {
		return mDataFineReclusione;
	}

	public Date getDataInizioArresto() {
		return mDataInizioArresto;
	}

	public String getFlagErgastolo() {
		return mFlagErgastolo;
	}

	public Date getDataInizioIsolamentoDiurno() {
		return mDataInzioIsolamentoDiurno;
	}

	public Date getDataFineIsolamentoDiurno() {
		return mDataFineIsolamentoDiurno;
	}

	public BigDecimal getNumAnniIsolamentoDiurno() {
		if (mNumAnniIsolamentoDiurno != null)
			return mNumAnniIsolamentoDiurno;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumMesiIsolamentoDiurno() {
		if (mNumMesiIsolamentoDiurno != null)
			return mNumMesiIsolamentoDiurno;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumGiorniIsolamentoDiurno() {
		if (mNumGiorniIsolamentoDiurno != null)
			return mNumGiorniIsolamentoDiurno;
		else
			return new BigDecimal(0);
	}

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public String getStringaArresto() {
		return mStringaArresto;
	}

	public BigDecimal getMisAltIdMisuraAlternativa() {
		return mMisAltIdMisuraAlternativa;
	}

	public String getImmediataScarcerazione() {
		return mImmediataScarcerazione;
	}

	public String getFlagPenaSospesa() {
		return mFlagPenaSospesa;
	}

	public String getStringaIsolamentoDiurno() {
		return mStringaIsolamentoDiurno;
	}

	//
	// METODI SET()
	//

	public void setIdPenaResidua(BigDecimal aValore) {
		mIdPenaResidua = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setNumAnniReclusione(BigDecimal aValore) {
		mNumAnniReclusione = aValore;
	}

	public void setNumMesiReclusione(BigDecimal aValore) {
		mNumMesiReclusione = aValore;
	}

	public void setNumGiorniReclusione(BigDecimal aValore) {
		mNumGiorniReclusione = aValore;
	}

	public void setImportoMulta(BigDecimal aValore) {
		mImportoMulta = aValore;
	}

	public void setNumAnniArresto(BigDecimal aValore) {
		mNumAnniArresto = aValore;
	}

	public void setNumMesiArresto(BigDecimal aValore) {
		mNumMesiArresto = aValore;
	}

	public void setNumGiorniArresto(BigDecimal aValore) {
		mNumGiorniArresto = aValore;
	}

	public void setImportoAmmenda(BigDecimal aValore) {
		mImportoAmmenda = aValore;
	}

	public void setDiesAQuo(String aValore) {
		mDiesAQuo = aValore;
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

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setFlagValidato(String aValore) {
		mFlagValidato = aValore;
	}

	public void setDataFinePresunta(Date aValore) {
		mDataFinePresunta = aValore;
	}

	public void setDataFineReclusione(Date aValore) {
		mDataFineReclusione = aValore;
	}

	public void setDataInizioArresto(Date aValore) {
		mDataInizioArresto = aValore;
	}

	public void setFlagErgastolo(String aValore) {
		mFlagErgastolo = aValore;
	}

	public void setDataInizioIsolamentoDiurno(Date aValore) {
		mDataInzioIsolamentoDiurno = aValore;
	}

	public void setDataFineIsolamentoDiurno(Date aValore) {
		mDataFineIsolamentoDiurno = aValore;
	}

	public void setNumAnniIsolamentoDiurno(BigDecimal aValore) {
		mNumAnniIsolamentoDiurno = aValore;
	}

	public void setNumMesiIsolamentoDiurno(BigDecimal aValore) {
		mNumMesiIsolamentoDiurno = aValore;
	}

	public void setNumGiorniIsolamentoDiurno(BigDecimal aValore) {
		mNumGiorniIsolamentoDiurno = aValore;
	}

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public void setMisAltIdMisuraAlternativa(BigDecimal aValore) {
		mMisAltIdMisuraAlternativa = aValore;
	}

	public void setImmediataScarcerazione(String aValore) {
		mImmediataScarcerazione = aValore;
	}

	public void setFlagPenaSospesa(String aValore) {
		mFlagPenaSospesa = aValore;
	}

	public void setStringaIsolamentoDiurno(String aValore) {
		mStringaIsolamentoDiurno = aValore;
	}

	public PenaResiduaModel getPenaResiduaModel() {
		PenaResiduaModel lPenaRes = new PenaResiduaModel();

		lPenaRes.setIdPenaResidua(this.mIdPenaResidua);
		lPenaRes.setDataInizio(this.mDataInizio);
		lPenaRes.setDataFine(this.mDataFine);
		lPenaRes.setNumAnniReclusione(this.mNumAnniReclusione);
		lPenaRes.setNumMesiReclusione(this.mNumMesiReclusione);
		lPenaRes.setNumGiorniReclusione(this.mNumGiorniReclusione);
		lPenaRes.setImportoMulta(this.mImportoMulta);
		lPenaRes.setNumAnniArresto(this.mNumAnniArresto);
		lPenaRes.setNumMesiArresto(this.mNumMesiArresto);
		lPenaRes.setNumGiorniArresto(this.mNumGiorniArresto);
		lPenaRes.setImportoAmmenda(this.mImportoAmmenda);
		lPenaRes.setDiesAQuo(this.mDiesAQuo);
		lPenaRes.setCodOperatoreInserimento(this.mCodOperatoreInserimento);
		lPenaRes.setDataInserimento(this.mDataInserimento);
		lPenaRes.setCodUfficioInserimento(this.mCodUfficioInserimento);
		lPenaRes.setDescrUfficioInserimento(this.mDescrUfficioInserimento);
		lPenaRes.setCodOperatoreAggiornamento(this.mCodOperatoreAggiornamento);
		lPenaRes.setDataAggiornamento(this.mDataAggiornamento);
		lPenaRes.setCodUfficioAggiornamento(this.mCodUfficioAggiornamento);
		lPenaRes.setDescrUfficioAggiornamento(this.mDescrUfficioAggiornamento);
		lPenaRes.setEveIdEvento(this.mEveIdEvento);
		lPenaRes.setFasSieIdFascicoloSiep(this.mFasSieIdFascicoloSiep);
		lPenaRes.setFlagValidato(this.mFlagValidato);
		lPenaRes.setDataFinePresunta(this.mDataFinePresunta);
		lPenaRes.setDataFineReclusione(this.mDataFineReclusione);
		lPenaRes.setDataInizioArresto(this.mDataInizioArresto);
		lPenaRes.setFlagErgastolo(this.mFlagErgastolo);
		// lPenaRes.setDataInzioIsolamentoDiurno ( this.mDataInzioIsolamentoDiurno );
		lPenaRes.setDataFineIsolamentoDiurno(this.mDataFineIsolamentoDiurno);
		lPenaRes.setNumAnniIsolamentoDiurno(this.mNumAnniIsolamentoDiurno);
		lPenaRes.setNumMesiIsolamentoDiurno(this.mNumMesiIsolamentoDiurno);
		lPenaRes.setNumGiorniIsolamentoDiurno(this.mNumGiorniIsolamentoDiurno);
		lPenaRes.setMisAltIdMisuraAlternativa(this.mMisAltIdMisuraAlternativa);
		lPenaRes.setImmediataScarcerazione(this.mImmediataScarcerazione);
		lPenaRes.setFlagPenaSospesa(this.mFlagPenaSospesa);

		return lPenaRes;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdPenaResidua + " - " + mDataInizio + " - " + mDataFine + " - " + mNumAnniReclusione
				+ " - " + mNumMesiReclusione + " - " + mNumGiorniReclusione + " - " + mImportoMulta + " - "
				+ mNumAnniArresto + " - " + mNumMesiArresto + " - " + mNumGiorniArresto + " - "
				+ mImportoAmmenda + " - " + mDiesAQuo + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mEveIdEvento + " - " + mFasSieIdFascicoloSiep
				+ " - " + mFlagValidato + " - " + mDataFinePresunta + " - " + mDataFineReclusione + " - "
				+ mDataInizioArresto + " - " + mFlagErgastolo + " - " + mDataInzioIsolamentoDiurno + " - "
				+ mDataFineIsolamentoDiurno + " - " + mNumAnniIsolamentoDiurno + " - "
				+ mNumMesiIsolamentoDiurno + " - " + mNumGiorniIsolamentoDiurno + " - "
				+ mImmediataScarcerazione + " - " + mMisAltIdMisuraAlternativa + " - " + mFlagPenaSospesa;

		return lStr;
	}

	/**
	 * calcolaStringaArresto per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaArresto() {
		String lStringArresto = "";
		if (this.getNumAnniArresto() != null) {
			if (this.getNumAnniArresto().intValue() != 0)
				lStringArresto = "Anni " + this.getNumAnniArresto();
		}
		if (this.getNumMesiArresto() != null) {
			if (this.getNumMesiArresto().intValue() != 0)
				lStringArresto += " Mesi " + this.getNumMesiArresto();
		}
		if (this.getNumGiorniArresto() != null) {
			if (this.getNumGiorniArresto().intValue() != 0)
				lStringArresto += " Giorni " + this.getNumGiorniArresto();
		}

		if (lStringArresto.length() > 1)
			this.mStringaArresto = lStringArresto;
		else
			this.mStringaArresto = null;
	}

	/**
	 * calcolaStringaReclusione per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaReclusione() {
		String lStringReclusione = "";
		if (this.getNumAnniReclusione() != null) {
			if (this.getNumAnniReclusione().intValue() != 0)
				lStringReclusione = "Anni " + this.getNumAnniReclusione();
		}
		if (this.getNumMesiReclusione() != null) {
			if (this.getNumMesiReclusione().intValue() != 0)
				lStringReclusione += " Mesi " + this.getNumMesiReclusione();
		}
		if (this.getNumGiorniReclusione() != null) {
			if (this.getNumGiorniReclusione().intValue() != 0)
				lStringReclusione += " Giorni " + this.getNumGiorniReclusione();
		}

		if (lStringReclusione.length() > 1)
			this.mStringaReclusione = lStringReclusione;
		else
			this.mStringaReclusione = null;
	}

	/**
	 * calcolaStringaIsolamento per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */

	public void calcolaStringaIsolamento() {
		String lStringIsolamento = "";
		if (this.getNumAnniIsolamentoDiurno() != null) {
			if (this.getNumAnniIsolamentoDiurno().intValue() != 0)
				lStringIsolamento = "Anni " + this.getNumAnniIsolamentoDiurno();
		}
		if (this.getNumMesiIsolamentoDiurno() != null) {
			if (this.getNumMesiIsolamentoDiurno().intValue() != 0)
				lStringIsolamento += " Mesi " + this.getNumMesiIsolamentoDiurno();
		}
		if (this.getNumGiorniIsolamentoDiurno() != null) {
			if (this.getNumGiorniIsolamentoDiurno().intValue() != 0)
				lStringIsolamento += " Giorni " + this.getNumGiorniIsolamentoDiurno();
		}

		if (lStringIsolamento.length() > 1) {
			this.mStringaIsolamentoDiurno = lStringIsolamento;
		} else {
			this.mStringaIsolamentoDiurno = null;
		}
	}

	/**
	 * setta alcuni parametri per la stampa
	 * 
	 * @param lPos
	 */
	public void setPenaPrecedentePerStampa(PosizioneGiuridicaModel lPos) {
		this.calcolaStringaReclusione();
		this.calcolaStringaArresto();
		this.calcolaStringaIsolamento();
		if (this.getDataFine() != null && this.getDataFine().compareTo(DateUtils.getSysDate()) <= 0
				&& (lPos != null && !(lPos.isLibero())))
			this.setImmediataScarcerazione("S");
		else
			this.setImmediataScarcerazione("N");

	}

}