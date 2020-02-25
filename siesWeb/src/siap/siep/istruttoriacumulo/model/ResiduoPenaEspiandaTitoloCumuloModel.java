package siap.siep.istruttoriacumulo.model;

/**
* <p>Title: ResiduoPenaEspiandaTitoloCumuloModel</p>
* <p>Description: Nella 'Stampa prospetto dei Titoli Coinvolti nel cumulo',</p>
* <p> 			Riporta il Totale pena Residua Espianda del singolo titolo</p>
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;
import f3b.util.StringUtils;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;

public class ResiduoPenaEspiandaTitoloCumuloModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 7743789747865633531L;
	private String mCodTipoSanzione;
	private String mDescrTipoSanzione;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private BigDecimal mSanzionePecuniariaAmmenda;
	private BigDecimal mSanzionePecuniariaMulta;
	private String mStringaSanzione;
	private String mPeriodoSanzione;

	private String mCodTipoPenaDetentiva;
	private String mDescrTipoPenaDetentiva;
	private BigDecimal mNumAnniReclusione;
	private BigDecimal mNumMesiReclusione;
	private BigDecimal mNumGiorniReclusione;
	private BigDecimal mImportoMulta;
	private BigDecimal mNumAnniArresto;
	private BigDecimal mNumMesiArresto;
	private BigDecimal mNumGiorniArresto;
	private BigDecimal mImportoAmmenda;
	private String mStringaArresto;
	private String mStringaReclusione;

	// COSTRUTTORE DI DEFAULT
	public ResiduoPenaEspiandaTitoloCumuloModel() {
		this.mCodTipoSanzione = null;
		this.mDescrTipoSanzione = null;
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mSanzionePecuniariaMulta = null;
		this.mSanzionePecuniariaAmmenda = null;
		this.mStringaSanzione = null;
		this.mPeriodoSanzione = null;

		this.mCodTipoPenaDetentiva = null;
		this.mDescrTipoPenaDetentiva = null;
		this.mNumAnniReclusione = null;
		this.mNumMesiReclusione = null;
		this.mNumGiorniReclusione = null;
		this.mImportoMulta = null;
		this.mNumAnniArresto = null;
		this.mNumMesiArresto = null;
		this.mNumGiorniArresto = null;
		this.mImportoAmmenda = null;
	}

	/**
	 * Costruttore di inizializzazione a partire da SanzioneSostitutivaModel
	 * 
	 * @param aSansModel
	 */
	public ResiduoPenaEspiandaTitoloCumuloModel(SanzioneSostitutivaCumuloModel aSansModel) {
		this.mCodTipoSanzione = aSansModel.getCodTipoSanzione();
		this.mDescrTipoSanzione = aSansModel.getDescrTipoSanzione();
		this.mNumAnni = aSansModel.getNumAnni();
		this.mNumMesi = aSansModel.getNumMesi();
		this.mNumGiorni = aSansModel.getNumGiorni();
		this.mSanzionePecuniariaMulta = aSansModel.getSanzionePecuniariaMulta();
		this.mSanzionePecuniariaAmmenda = aSansModel.getSanzionePecuniariaAmmenda();
		this.mStringaSanzione = aSansModel.getStringaSanzione();
		this.mPeriodoSanzione = aSansModel.getPeriodoSanzione();

	}

	//
	// METODI GET()
	//

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

	public String getStringaSanzione() {
		return mStringaSanzione;
	}

	public String getPeriodoSanzione() {
		return mPeriodoSanzione;
	}

	public String getCodTipoPenaDetentiva() {
		return mCodTipoPenaDetentiva;
	}

	public String getDescTipoPenaDetentiva() {
		return mDescrTipoPenaDetentiva;
	}

	public BigDecimal getNumAnniReclusione() {
		return mNumAnniReclusione;
	}

	public BigDecimal getNumMesiReclusione() {
		return mNumMesiReclusione;
	}

	public BigDecimal getNumGiorniReclusione() {
		return mNumGiorniReclusione;
	}

	public BigDecimal getImportoMulta() {
		if (mImportoMulta != null)
			return mImportoMulta;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumAnniArresto() {
		return mNumAnniArresto;
	}

	public BigDecimal getNumMesiArresto() {
		return mNumMesiArresto;
	}

	public BigDecimal getNumGiorniArresto() {
		return mNumGiorniArresto;
	}

	public BigDecimal getImportoAmmenda() {
		if (mImportoAmmenda != null)
			return mImportoAmmenda;
		else
			return new BigDecimal(0);
	}

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public String getStringaArresto() {
		return mStringaArresto;
	}

	//
	// METODI SET()
	//

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

	public void setStringaSanzione(String aValore) {
		mStringaSanzione = aValore;
	}

	public void setPeriodoSanzione(String aValore) {
		mPeriodoSanzione = aValore;
	}

	public void setCodTipoPenaDetentiva(String aValore) {
		mCodTipoPenaDetentiva = aValore;
	}

	public void setDescrTipoPenaDetentiva(String aValore) {
		mDescrTipoPenaDetentiva = aValore;
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

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public boolean isDurataSanzioneZero() {
		if ((mNumAnni != null && mNumAnni.intValue() > 0) || (mNumMesi != null && mNumMesi.intValue() > 0)
				|| (mNumGiorni != null && mNumGiorni.intValue() > 0))
			return false;
		else
			return true;
	}

	/**
	 * 
	 * @deprecated per ora mai usato da capire lutilità (stampe?)
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
	 * calcolaStringaArresto per la Stampa in cui serve la stringa composta di anni mesi giorni
	 */
	public void calcolaStringaArrestoCum() {
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
			this.mStringaArresto = "Arresto " + lStringArresto.trim();
		else
			this.mStringaArresto = null;
	}

	/**
	 * calcolaStringaReclusione per la Stampa in cui serve la stringa composta di anni mesi giorni
	 */
	public void calcolaStringaReclusioneCum() {
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
			this.mStringaReclusione = "Reclusione " + lStringReclusione.trim();
		else
			this.mStringaReclusione = null;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "ResiduoPenaEspiandaTitoloCumuloModel:\n" + "[ mCodTipoSanzione              = "
				+ mCodTipoSanzione + " ]\n" + "[ mDescrTipoSanzione            = " + mDescrTipoSanzione
				+ " ]\n" + "[ mNumAnni                      = " + mNumAnni + " ]\n"
				+ "[ mNumMesi                      = " + mNumMesi + " ]\n"
				+ "[ mNumGiorni                    = " + mNumGiorni + " ]\n"
				+ "[ mSanzionePecuniariaMulta      = " + mSanzionePecuniariaMulta + " ]\n"
				+ "[ mSanzionePecuniariaAmmenda    = " + mSanzionePecuniariaAmmenda + " ]\n"
				+ "[ mStringaSanzione			    = " + mStringaSanzione + " ]\n"
				+ "[ mPeriodoSanzione			    = " + mPeriodoSanzione + " ]\n"
				+ "[ mCodTipoPenaDetentiva      = " + mCodTipoPenaDetentiva + " ]\n"
				+ "[ mDescrTipoPenaDetentiva    = " + mDescrTipoPenaDetentiva + " ]\n"
				+ "[ mNumAnniReclusione         = " + mNumAnniReclusione + " ]\n"
				+ "[ mNumMesiReclusione         = " + mNumMesiReclusione + " ]\n"
				+ "[ mNumGiorniReclusione       = " + mNumGiorniReclusione + " ]\n"
				+ "[ mImportoMulta              = " + mImportoMulta + " ]\n"
				+ "[ mNumAnniArresto            = " + mNumAnniArresto + " ]\n"
				+ "[ mNumMesiArresto            = " + mNumMesiArresto + " ]\n"
				+ "[ mNumGiorniArresto          = " + mNumGiorniArresto + " ]\n"
				+ "[ mImportoAmmenda            = " + mImportoAmmenda + " ]";

		return lStr;
	}

}
