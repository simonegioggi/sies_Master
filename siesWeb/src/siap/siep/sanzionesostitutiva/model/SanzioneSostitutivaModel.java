package siap.siep.sanzionesostitutiva.model;

/**
* <p>Title: SanzioneSostitutivaModel</p>
* <p>Description: Classe Model che rappresenta il SanzioneSostitutiva</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.StringUtils;
import siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva;

public class SanzioneSostitutivaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7996140011519909938L;

	private BigDecimal mIdSanzioneSostitutiva;
	private String mCodTipoSanzione;
	private String mDescrTipoSanzione;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private BigDecimal mSanzionePecuniariaMulta;
	private BigDecimal mAnnoRegistro;
	private BigDecimal mNumRegistro;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mPenComIdPenaComplessiva;
	private String mStringaSanzione;
	private String mPeriodoSanzione;
	private BigDecimal mSanzionePecuniariaAmmenda;
	
	// MEV_2023-13 - Serve per distinguere tra SS e le nuove Pene sostitutive Pene Detentive Brevi
	// il dato è recuperato dalla RV_ABBREVIATION della CG
	private String mDescrCategoriaSanzione;



    // COSTRUTTORE DI DEFAULT
	public SanzioneSostitutivaModel() {
		this.mIdSanzioneSostitutiva = null;
		this.mCodTipoSanzione = null;
		this.mDescrTipoSanzione = null;
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mSanzionePecuniariaMulta = null;
		this.mAnnoRegistro = null;
		this.mNumRegistro = null;
		this.mCodOperatoreInserimento = null;
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = null;
		this.mDescrUfficioInserimento = null;
		this.mCodOperatoreAggiornamento = null;
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = null;
		this.mDescrUfficioAggiornamento = null;
		this.mPenComIdPenaComplessiva = null;
		this.mStringaSanzione = null;
		this.mSanzionePecuniariaAmmenda = null;
		
		this.mDescrCategoriaSanzione = null; // MEV_2023-13
	}

	// COSTRUTTORE DI COPIA
	public SanzioneSostitutivaModel(SanzioneSostitutivaModel aModel) {
		this.mIdSanzioneSostitutiva = aModel.mIdSanzioneSostitutiva;
		this.mCodTipoSanzione = aModel.mCodTipoSanzione;
		this.mDescrTipoSanzione = aModel.mDescrTipoSanzione;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mSanzionePecuniariaMulta = aModel.mSanzionePecuniariaMulta;
		this.mAnnoRegistro = aModel.mAnnoRegistro;
		this.mNumRegistro = aModel.mNumRegistro;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mPenComIdPenaComplessiva = aModel.mPenComIdPenaComplessiva;
		this.mSanzionePecuniariaAmmenda = aModel.mSanzionePecuniariaAmmenda;
		
		this.mDescrCategoriaSanzione = aModel.mDescrCategoriaSanzione;// MEV_2023-13
	}

	// COSTRUTTORE MODEL
	public SanzioneSostitutivaModel(BigDecimal aIdSanzioneSostitutiva, String aCodTipoSanzione,
			String aDescrTipoSanzione, BigDecimal aNumAnni, BigDecimal aNumMesi, BigDecimal aNumGiorni,
			BigDecimal aSanzionePecuniariaMulta, BigDecimal aAnnoRegistro, BigDecimal aNumRegistro,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aPenComIdPenaComplessiva, BigDecimal aSanzionePecuniariaAmmenda
			, String aDescrCategoriaSanzione
	        ) {
		this.mIdSanzioneSostitutiva = aIdSanzioneSostitutiva;
		this.mCodTipoSanzione = aCodTipoSanzione;
		this.mDescrTipoSanzione = aDescrTipoSanzione;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
		this.mSanzionePecuniariaMulta = aSanzionePecuniariaMulta;
		this.mAnnoRegistro = aAnnoRegistro;
		this.mNumRegistro = aNumRegistro;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mPenComIdPenaComplessiva = aPenComIdPenaComplessiva;
		this.mSanzionePecuniariaAmmenda = aSanzionePecuniariaAmmenda;
		
		this.mDescrCategoriaSanzione = aDescrCategoriaSanzione;// MEV_2023-13
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdSanzioneSostitutiva() {
		return mIdSanzioneSostitutiva;
	}

	public String getCodTipoSanzione() {
		return mCodTipoSanzione;
	}

	public String getDescrTipoSanzione() {
		return mDescrTipoSanzione;
	}

	public BigDecimal getNumAnni() {
		return mNumAnni;
	}

	public BigDecimal getNumMesi() {
		return mNumMesi;
	}

	public BigDecimal getNumGiorni() {
		return mNumGiorni;
	}

	public BigDecimal getSanzionePecuniariaMulta() {
		return mSanzionePecuniariaMulta;
	}

	public BigDecimal getAnnoRegistro() {
		return mAnnoRegistro;
	}

	public BigDecimal getNumRegistro() {
		return mNumRegistro;
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

	public BigDecimal getPenComIdPenaComplessiva() {
		return mPenComIdPenaComplessiva;
	}

	public String getStringaSanzione() {
		return mStringaSanzione;
	}

	public String getPeriodoSanzione() {
		return mPeriodoSanzione;
	}

	public BigDecimal getSanzionePecuniariaAmmenda() {
		return mSanzionePecuniariaAmmenda;
	}

	// MEV_2023-13 
	public String getDescrCategoriaSanzione() {
        return mDescrCategoriaSanzione;
    }
	// MEV_2023-13 - FINE

	//
	// METODI SET()
	//

	public void setIdSanzioneSostitutiva(BigDecimal aValore) {
		mIdSanzioneSostitutiva = aValore;
	}

	public void setCodTipoSanzione(String aValore) {
		mCodTipoSanzione = aValore;
	}

	public void setDescrTipoSanzione(String aValore) {
		mDescrTipoSanzione = aValore;
	}

	public void setNumAnni(BigDecimal aValore) {
		mNumAnni = aValore;
	}

	public void setNumMesi(BigDecimal aValore) {
		mNumMesi = aValore;
	}

	public void setNumGiorni(BigDecimal aValore) {
		mNumGiorni = aValore;
	}

	public void setSanzionePecuniariaMulta(BigDecimal aValore) {
		mSanzionePecuniariaMulta = aValore;
	}

	public void setAnnoRegistro(BigDecimal aValore) {
		mAnnoRegistro = aValore;
	}

	public void setNumRegistro(BigDecimal aValore) {
		mNumRegistro = aValore;
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

	public void setPenComIdPenaComplessiva(BigDecimal aValore) {
		mPenComIdPenaComplessiva = aValore;
	}

	public void setStringaSanzione(String aValore) {
		mStringaSanzione = aValore;
	}

	public void setPeriodoSanzione(String aValore) {
		mPeriodoSanzione = aValore;
	}

	public void setSanzionePecuniariaAmmenda(BigDecimal aValore) {
		mSanzionePecuniariaAmmenda = aValore;
	}

	// MEV_2023-13
    public void setDescrCategoriaSanzione(String mDescrCategoriaSanzione) {
        this.mDescrCategoriaSanzione = mDescrCategoriaSanzione;
    }
    // MEV_2023-13 - FINE
	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "SanzioneSostitutivaModel:\n" + "[ mIdSanzioneSostitutiva     = " + mIdSanzioneSostitutiva
				+ " ]\n" + "[ mPenComIdPenaComplessiva   = " + mPenComIdPenaComplessiva + " ]\n"
				+ "[ mCodTipoSanzione           = " + mCodTipoSanzione + " ]\n"
				+ "[ mDescrTipoSanzione         = " + mDescrTipoSanzione + " ]\n"
				+ "[ mNumAnni                   = " + mNumAnni + " ]\n" + "[ mNumMesi                   = "
				+ mNumMesi + " ]\n" + "[ mNumGiorni                 = " + mNumGiorni + " ]\n"
				+ "[ mSanzionePecuniariaMulta   = " + mSanzionePecuniariaMulta + " ]\n"
				+ "[ mAnnoRegistro              = " + mAnnoRegistro + " ]\n"
				+ "[ mNumRegistro               = " + mNumRegistro + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mDescrCategoriaSanzione     = " + mDescrCategoriaSanzione + " ]\n"
				+ "[ mSanzionePecuniariaAmmenda   = " + mSanzionePecuniariaAmmenda + " ]";
		return lStr;
	}
	
	public String toString2() {
		String lStr = new String();

		lStr = "" + mIdSanzioneSostitutiva + " - " + mCodTipoSanzione + " - " + mDescrTipoSanzione + " - "
				+ mNumAnni + " - " + mNumMesi + " - " + mNumGiorni + " - " + mSanzionePecuniariaMulta + " - "
				+ mAnnoRegistro + " - " + mNumRegistro + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mPenComIdPenaComplessiva + " - "
				+ mSanzionePecuniariaAmmenda;

		return lStr;
	}

	public void calcolaStringaSanzione() {
		String lString = this.mDescrTipoSanzione;

		if ("E".equals(mCodTipoSanzione)) {
			lString += " per un periodo di ";
		}

		if (this.mNumAnni != null) {
			if (this.mNumAnni.intValue() != 0)
				lString += " Anni " + this.mNumAnni;
		}
		if (this.getNumMesi() != null) {
			if (this.getNumMesi().intValue() != 0)
				lString += " Mesi " + this.mNumMesi;
		}
		if (this.mNumGiorni != null) {
			if (this.mNumGiorni.intValue() != 0)
				lString += " Giorni " + this.mNumGiorni;
		}
		if (this.mSanzionePecuniariaMulta != null && this.mSanzionePecuniariaMulta.intValue() != 0) {
			lString += ": Multa " + StringUtils.toEuroFormat(mSanzionePecuniariaMulta) + " Euro";
		}

		if (this.mSanzionePecuniariaAmmenda != null && this.mSanzionePecuniariaAmmenda.intValue() != 0) {
			lString += ": Ammenda " + StringUtils.toEuroFormat(mSanzionePecuniariaAmmenda) + " Euro";
		}

		if (lString.length() > 1)
			this.mStringaSanzione = lString;
		else
			this.mStringaSanzione = null;
	}

	/**
	 * Calcola il periodo della Sanzione sostitutiva e setta il parametro mPeriodoSanzione Utile per
	 * l'epulsione per cui serve solo il periodo e non la descrizione *
	 */
	public void calcolaPeriodoSanzione() {
		String lString = "";

		if (this.mNumAnni != null) {
			if (this.mNumAnni.intValue() != 0)
				lString += "Anni " + this.mNumAnni;
		}
		if (this.getNumMesi() != null) {
			if (this.getNumMesi().intValue() != 0)
				lString += " Mesi " + this.mNumMesi;
		}
		if (this.mNumGiorni != null) {
			if (this.mNumGiorni.intValue() != 0)
				lString += " Giorni " + this.mNumGiorni;
		}

		if (this.mSanzionePecuniariaMulta != null && this.mSanzionePecuniariaMulta.intValue() != 0) {
			lString += ", Multa " + StringUtils.toEuroFormat(mSanzionePecuniariaMulta) + " Euro";
		}

		if (this.mSanzionePecuniariaAmmenda != null && this.mSanzionePecuniariaAmmenda.intValue() != 0) {
			lString += ", Ammenda " + StringUtils.toEuroFormat(mSanzionePecuniariaAmmenda) + " Euro";
		}

		if (lString.length() > 1)
			this.mPeriodoSanzione = lString;
		else
			this.mPeriodoSanzione = null;
	}
	
	
	/**
	 * Ritorna true se ilcodice appartiene alle nuove "Pene sostitutive Pene Detentive Brevi"
	 * @return
	 * @since MEV_2023-13
	 */
	public boolean isPenaSostitutiva () {
	    if (ICostantiSanzioneSostitutiva.TIPO_PENA_SOSTITUTIVA_PS.equals(mDescrCategoriaSanzione))
	        return true;
	    else
	        return false;
	}
	

}