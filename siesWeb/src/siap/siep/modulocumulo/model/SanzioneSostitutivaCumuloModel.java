package siap.siep.modulocumulo.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.StringUtils;

import siap.sico.calendar.model.CalendarModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;

/**
 * <p>
 * Title: SanzioneSostitutivaCumuloModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il SanzioneSostitutiva
 * </p>
 * <p>
 * in ambito Cumulo (Sanzione_Sostitutiva_Cumulo)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 4.0
 */
public class SanzioneSostitutivaCumuloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5835039695953597288L;

	private BigDecimal mIdSanzioneSostitutivaCum;
	private String mCodTipoSanzione;
	private String mDescrTipoSanzione;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private BigDecimal mSanzionePecuniariaAmmenda;
	private BigDecimal mSanzionePecuniariaMulta;

	private BigDecimal mPcIdPenaComplessivaCum;

	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mTitIdTitoloCumulato;
	private BigDecimal mIdSanzioneSostOrigine;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private String mStringaSanzione;
	private String mPeriodoSanzione;

	// Sul riepilogo calcolo pena, indica se trattasi di calcolo Lordo o Netto
	private String mFlagPenaNetta; // S/N/null

  private boolean mIsRevocata;
  private String mStringaRevoca;
	
	// COSTRUTTORE DI DEFAULT
	public SanzioneSostitutivaCumuloModel() {
		this.mIdSanzioneSostitutivaCum = null;
		this.mCodTipoSanzione = null;
		this.mDescrTipoSanzione = null;
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mSanzionePecuniariaMulta = null;
		this.mSanzionePecuniariaAmmenda = null;

		this.mPcIdPenaComplessivaCum = null;

		this.mFlagStato = null;
		this.mMotivoModifica = null;
		this.mTitIdTitoloCumulato = null;
		this.mIdSanzioneSostOrigine = null;

		this.mCodOperatoreInserimento = null;
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = null;
		this.mCodOperatoreAggiornamento = null;
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = null;

		this.mStringaSanzione = null;
		this.mPeriodoSanzione = null;
		this.mFlagPenaNetta = null;

		this.mIsRevocata = false;
	}

	// COSTRUTTORE DI COPIA
	public SanzioneSostitutivaCumuloModel(SanzioneSostitutivaCumuloModel aModel) {
		this.mIdSanzioneSostitutivaCum = aModel.mIdSanzioneSostitutivaCum;
		this.mCodTipoSanzione = aModel.mCodTipoSanzione;
		this.mDescrTipoSanzione = aModel.mDescrTipoSanzione;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mSanzionePecuniariaMulta = aModel.mSanzionePecuniariaMulta;
		this.mSanzionePecuniariaAmmenda = aModel.mSanzionePecuniariaAmmenda;

		this.mPcIdPenaComplessivaCum = aModel.mPcIdPenaComplessivaCum;

		this.mFlagStato = aModel.mFlagStato;
		this.mMotivoModifica = aModel.mMotivoModifica;
		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mIdSanzioneSostOrigine = aModel.mIdSanzioneSostOrigine;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		
		this.mIsRevocata = aModel.mIsRevocata;
	}

	// COSTRUTTORE MODEL
	public SanzioneSostitutivaCumuloModel(BigDecimal aIdSanzioneSostitutivaCum, String aCodTipoSanzione,
			String aDescrTipoSanzione, BigDecimal aNumAnni, BigDecimal aNumMesi, BigDecimal aNumGiorni,
			BigDecimal aSanzionePecuniariaMulta, BigDecimal aSanzionePecuniariaAmmenda,

			BigDecimal aPcIdPenaComplessivaCum,

			String aFlagStato, String aMotivoModifica, BigDecimal aTitIdTitoloCumulato,
			BigDecimal aIdSanzioneSostOrigine,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdSanzioneSostitutivaCum = aIdSanzioneSostitutivaCum;
		this.mCodTipoSanzione = aCodTipoSanzione;
		this.mDescrTipoSanzione = aDescrTipoSanzione;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;

		this.mSanzionePecuniariaMulta = aSanzionePecuniariaMulta;
		this.mSanzionePecuniariaAmmenda = aSanzionePecuniariaAmmenda;

		this.mPcIdPenaComplessivaCum = aPcIdPenaComplessivaCum;

		this.mFlagStato = aFlagStato;
		this.mMotivoModifica = aMotivoModifica;
		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mIdSanzioneSostOrigine = aIdSanzioneSostOrigine;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	/**
	 * Costruttore di inizializzazione a partire da SanzioneSostitutivaModel
	 * 
	 * @param aSansModel
	 */
	public SanzioneSostitutivaCumuloModel(SanzioneSostitutivaModel aSansModel) {
		this.mCodTipoSanzione = aSansModel.getCodTipoSanzione();
		this.mNumAnni = aSansModel.getNumAnni();
		this.mNumMesi = aSansModel.getNumMesi();
		this.mNumGiorni = aSansModel.getNumGiorni();

		this.mSanzionePecuniariaMulta = aSansModel.getSanzionePecuniariaMulta();
		this.mSanzionePecuniariaAmmenda = aSansModel.getSanzionePecuniariaAmmenda();

		this.mIdSanzioneSostOrigine = aSansModel.getIdSanzioneSostitutiva();
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdSanzioneSostitutivaCum() {
		return mIdSanzioneSostitutivaCum;
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

	public BigDecimal getSanzionePecuniariaAmmenda() {
		return mSanzionePecuniariaAmmenda;
	}

	public BigDecimal getPcIdPenaComplessivaCum() {
		return mPcIdPenaComplessivaCum;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getMotivoModifica() {
		return mMotivoModifica;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}

	public BigDecimal getIdSanzioneSostOrigine() {
		return mIdSanzioneSostOrigine;
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

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getStringaSanzione() {
		return mStringaSanzione;
	}

	public String getPeriodoSanzione() {
		return mPeriodoSanzione;
	}

	public String getFlagPenaNetta() {
		return mFlagPenaNetta;
	}
	
  public boolean getIsRevocata() {
    return mIsRevocata;
  }
  
  public String getStringaRevoca() {
    return mStringaRevoca;
  }
  
	//
	// METODI SET()
	//

	public void setIdSanzioneSostitutivaCum(BigDecimal aValore) {
		mIdSanzioneSostitutivaCum = aValore;
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

	public void setSanzionePecuniariaAmmenda(BigDecimal aValore) {
		mSanzionePecuniariaAmmenda = aValore;
	}

	public void setPcIdPenaComplessivaCum(BigDecimal aValore) {
		mPcIdPenaComplessivaCum = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setMotivoModifica(String aValore) {
		mMotivoModifica = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}

	public void setIdSanzioneSostOrigine(BigDecimal aValore) {
		mIdSanzioneSostOrigine = aValore;
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

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setStringaSanzione(String aValore) {
		mStringaSanzione = aValore;
	}

	public void setPeriodoSanzione(String aValore) {
		mPeriodoSanzione = aValore;
	}

	public void setFlagPenaNetta(String aValore) {
		mFlagPenaNetta = aValore;
	}

  public void setIsRevocata (boolean aValore) {
    mIsRevocata = aValore;
  }
  
  public void setStringaRevoca(String aValore) {
    mStringaRevoca = aValore;
  }
  
	public boolean isDurataSanzioneZero() {
		if ((mNumAnni != null && mNumAnni.intValue() > 0) || (mNumMesi != null && mNumMesi.intValue() > 0)
				|| (mNumGiorni != null && mNumGiorni.intValue() > 0))
			return false;
		else
			return true;
	}

	public CalendarModel getQuantumSS() {
		CalendarModel lQuantum = new CalendarModel();
		lQuantum.setNumGiorni(mNumAnni);
		lQuantum.setNumMesi(mNumMesi);
		lQuantum.setNumAnni(mNumGiorni);

		return lQuantum;
	}

	public void setQuantumSS(CalendarModel aCalendar) {
		this.mNumAnni = new BigDecimal(aCalendar.getNumGiorni());
		this.mNumMesi = new BigDecimal(aCalendar.getNumMesi());
		this.mNumGiorni = new BigDecimal(aCalendar.getNumAnni());
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "SanzioneSostitutivaCumuloModel:\n" + "[ mIdSanzioneSostitutivaCum     = "
				+ mIdSanzioneSostitutivaCum + " ]\n" + "[ mCodTipoSanzione              = " + mCodTipoSanzione
				+ " ]\n" + "[ mDescrTipoSanzione            = " + mDescrTipoSanzione + " ]\n"
				+ "[ mNumAnni                      = " + mNumAnni + " ]\n"
				+ "[ mNumMesi                      = " + mNumMesi + " ]\n"
				+ "[ mNumGiorni                    = " + mNumGiorni + " ]\n"
				+ "[ mSanzionePecuniariaMulta      = " + mSanzionePecuniariaMulta + " ]\n"
				+ "[ mSanzionePecuniariaAmmenda    = " + mSanzionePecuniariaAmmenda + " ]\n" +

				"[ mPcIdPenaComplessivaCum       = " + mPcIdPenaComplessivaCum + " ]\n" +

				"[ mFlagStato                    = " + mFlagStato + " ]\n"
				+ "[ mMotivoModifica               = " + mMotivoModifica + " ]\n"
				+ "[ mTitIdTitoloCumulato          = " + mTitIdTitoloCumulato + " ]\n"
				+ "[ mIdSanzioneSostOrigine        = " + mIdSanzioneSostOrigine + " ]\n" +

				"[ mCodOperatoreInserimento      = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento              = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento        = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento    = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento            = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento      = " + mCodUfficioAggiornamento + " ]";

		return lStr;
	}

	/**
	* 
	*/
	public void calcolaStringaSanzione() {
		String lString = this.mDescrTipoSanzione + " ";

		if ("E".equals(mCodTipoSanzione)) {
			lString += "per un periodo di ";
		}

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
			lString += ", " + StringUtils.toEuroFormat(mSanzionePecuniariaMulta) + " Euro";
		}

		if (this.mSanzionePecuniariaAmmenda != null && this.mSanzionePecuniariaAmmenda.intValue() != 0) {
			lString += ", " + StringUtils.toEuroFormat(mSanzionePecuniariaAmmenda) + " Euro";
		}

		if (lString.length() > 1)
			this.mStringaSanzione = lString;
		else
			this.mStringaSanzione = null;
	}

	/**
	 * Calcola il periodo della Sanzione sostitutiva Cumulo e setta il parametro mPeriodoSanzione Utile per
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
			lString += ", " + StringUtils.toEuroFormat(mSanzionePecuniariaMulta) + " Euro";
		}

		if (this.mSanzionePecuniariaAmmenda != null && this.mSanzionePecuniariaAmmenda.intValue() != 0) {
			lString += ", " + StringUtils.toEuroFormat(mSanzionePecuniariaAmmenda) + " Euro";
		}

		if (lString.length() > 1)
			this.mPeriodoSanzione = lString;
		else
			this.mPeriodoSanzione = null;
	}

	/**
	 * Valorizza StringaSanzione per Stampa Prospetto Cumulo (Proposta)
	 */
	public void calcolaStringaSanzionePerStampaProspetto() {
		String lString = this.mDescrTipoSanzione + " ";

		if (!"P".equals(mCodTipoSanzione)) {
			lString += "per ";

			if (this.mNumAnni != null) {
				if (this.mNumAnni.intValue() != 0)
					lString += "Anni " + this.mNumAnni + " ";
			}

			if (this.getNumMesi() != null) {
				if (this.getNumMesi().intValue() != 0)
					lString += "Mesi " + this.mNumMesi + " ";
			}

			if (this.mNumGiorni != null) {
				if (this.mNumGiorni.intValue() != 0)
					lString += "Giorni " + this.mNumGiorni + " ";
			}
		} else if ("P".equals(mCodTipoSanzione)) {
			lString += "di ";

			if (this.mSanzionePecuniariaMulta != null && this.mSanzionePecuniariaMulta.intValue() != 0) {
				lString += "Euro " + StringUtils.toEuroFormat(mSanzionePecuniariaMulta) + " di Multa";

				if (this.mSanzionePecuniariaAmmenda != null
						&& this.mSanzionePecuniariaAmmenda.intValue() != 0) {
					lString += ", e ";
				}

			}

			if (this.mSanzionePecuniariaAmmenda != null && this.mSanzionePecuniariaAmmenda.intValue() != 0) {
				lString += "Euro " + StringUtils.toEuroFormat(mSanzionePecuniariaAmmenda) + " di Ammenda";
			}
		}

		if (lString.length() > 1)
			this.mStringaSanzione = lString;
		else
			this.mStringaSanzione = null;
	}

}