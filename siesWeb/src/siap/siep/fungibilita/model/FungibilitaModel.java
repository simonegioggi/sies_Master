package siap.siep.fungibilita.model;

/**
* <p>Title: FungibilitaModel</p>
* <p>Description: Classe Model che rappresenta il Fungibilita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.calendar.model.CalendarModel;
import f3b.model.GenericModel;

public class FungibilitaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3169417062766871489L;

	private BigDecimal mIdFungibilita;
	private String mCodTipoFungibilita;
	private String mDescrTipoFungibilita;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private Date mDataDa;
	private Date mDataA;
	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private String mCodUfficioFruitore;
	private String mDescrUfficioFruitore;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mEveIdEvento;
	private String mFlagValidato;
	private String mStringaFungibilita;

	private String mDataOdierna;

	private BigDecimal mNumGiorniFruiti;
	private BigDecimal mNumGiorniNonFruiti;

	// COSTRUTTORE DI DEFAULT
	public FungibilitaModel() {
		this.mIdFungibilita = null;
		this.mCodTipoFungibilita = "";
		this.mDescrTipoFungibilita = "";
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mDataDa = null;
		this.mDataA = null;
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mCodUfficioFruitore = "";
		this.mDescrUfficioFruitore = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mEveIdEvento = null;
		this.mFlagValidato = "";

		this.mDataOdierna = "";

		this.mNumGiorniFruiti = null;
		this.mNumGiorniNonFruiti = null;

	}

	// COSTRUTTORE DI COPIA
	public FungibilitaModel(FungibilitaModel aModel) {
		this.mIdFungibilita = aModel.mIdFungibilita;
		this.mCodTipoFungibilita = aModel.mCodTipoFungibilita;
		this.mDescrTipoFungibilita = aModel.mDescrTipoFungibilita;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mDataDa = aModel.mDataDa;
		this.mDataA = aModel.mDataA;
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mCodUfficioFruitore = aModel.mCodUfficioFruitore;
		this.mDescrUfficioFruitore = aModel.mDescrUfficioFruitore;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mFlagValidato = aModel.mFlagValidato;

		this.mDataOdierna = aModel.mDataOdierna;

		this.mNumGiorniFruiti = aModel.mNumGiorniFruiti;
		this.mNumGiorniNonFruiti = aModel.mNumGiorniNonFruiti;
	}

	// COSTRUTTORE MODEL
	public FungibilitaModel(BigDecimal aIdFungibilita, String aCodTipoFungibilita,
			String aDescrTipoFungibilita, BigDecimal aNumAnni, BigDecimal aNumMesi, BigDecimal aNumGiorni,
			Date aDataDa, Date aDataA, Date aDataInizioValidita, Date aDataFineValidita,
			String aCodUfficioFruitore, String aDescrUfficioFruitore, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aFasSieIdFascicoloSiep, BigDecimal aEveIdEvento,
			BigDecimal aNumGiorniFruiti, BigDecimal aNumGiorniNonFruiti, String aFlagValidato) {
		this.mIdFungibilita = aIdFungibilita;
		this.mCodTipoFungibilita = aCodTipoFungibilita;
		this.mDescrTipoFungibilita = aDescrTipoFungibilita;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
		this.mDataDa = aDataDa;
		this.mDataA = aDataA;
		this.mDataInizioValidita = aDataInizioValidita;
		this.mDataFineValidita = aDataFineValidita;
		this.mCodUfficioFruitore = aCodUfficioFruitore;
		this.mDescrUfficioFruitore = aDescrUfficioFruitore;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mEveIdEvento = aEveIdEvento;
		this.mFlagValidato = aFlagValidato;

		this.mNumGiorniFruiti = aNumGiorniFruiti;
		this.mNumGiorniNonFruiti = aNumGiorniNonFruiti;

		this.mDataOdierna = "";
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdFungibilita() {
		return mIdFungibilita;
	}

	public String getCodTipoFungibilita() {
		return mCodTipoFungibilita;
	}

	public String getDescrTipoFungibilita() {
		return mDescrTipoFungibilita;
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

	public Date getDataDa() {
		return mDataDa;
	}

	public Date getDataA() {
		return mDataA;
	}

	public Date getDataInizioValidita() {
		return mDataInizioValidita;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public String getCodUfficioFruitore() {
		return mCodUfficioFruitore;
	}

	public String getDescrUfficioFruitore() {
		return mDescrUfficioFruitore;
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

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public String getFlagValidato() {
		return mFlagValidato;
	}

	public String getStringaFungibilita() {
		return mStringaFungibilita;
	}

	public String getDataOdierna() {
		return mDataOdierna;
	}

	public BigDecimal getNumGiorniFruiti() {
		return mNumGiorniFruiti;
	}

	public BigDecimal getNumGiorniNonFruiti() {
		return mNumGiorniNonFruiti;
	}

	public CalendarModel getQuantumFungibilita() {
		CalendarModel lQuantumFungibilita = new CalendarModel();

		lQuantumFungibilita.setNumAnni(this.mNumAnni);
		lQuantumFungibilita.setNumMesi(this.mNumMesi);
		lQuantumFungibilita.setNumGiorni(this.mNumGiorni);

		return lQuantumFungibilita;
	}

	//
	// METODI SET()
	//

	public void setIdFungibilita(BigDecimal aValore) {
		mIdFungibilita = aValore;
	}

	public void setCodTipoFungibilita(String aValore) {
		mCodTipoFungibilita = aValore;
	}

	public void setDescrTipoFungibilita(String aValore) {
		mDescrTipoFungibilita = aValore;
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

	public void setDataDa(Date aValore) {
		mDataDa = aValore;
	}

	public void setDataA(Date aValore) {
		mDataA = aValore;
	}

	public void setDataInizioValidita(Date aValore) {
		mDataInizioValidita = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setCodUfficioFruitore(String aValore) {
		mCodUfficioFruitore = aValore;
	}

	public void setDescrUfficioFruitore(String aValore) {
		mDescrUfficioFruitore = aValore;
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

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setFlagValidato(String aValore) {
		mFlagValidato = aValore;
	}

	public void setStringaFungibilita(String aValore) {
		mStringaFungibilita = aValore;
	}

	public void setDataOdierna(String aValore) {
		mDataOdierna = aValore;
	}

	public void setNumGiorniFruiti(BigDecimal aValore) {
		mNumGiorniFruiti = aValore;
	}

	public void setNumGiorniNonFruiti(BigDecimal aValore) {
		mNumGiorniNonFruiti = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "FungibilitaModel:\n" + "[ mIdFungibilita             = " + mIdFungibilita + " ]\n"
				+ "[ mCodTipoFungibilita        = " + mCodTipoFungibilita + " ]\n"
				+ "[ mDescrTipoFungibilita      = " + mDescrTipoFungibilita + " ]\n"
				+ "[ mNumAnni                   = " + mNumAnni + " ]\n" + "[ mNumMesi                   = "
				+ mNumMesi + " ]\n" + "[ mNumGiorni                 = " + mNumGiorni + " ]\n"
				+ "[ mDataDa                    = " + mDataDa + " ]\n" + "[ mDataA                     = "
				+ mDataA + " ]\n" + "[ mDataInizioValidita        = " + mDataInizioValidita + " ]\n"
				+ "[ mDataFineValidita          = " + mDataFineValidita + " ]\n"
				+ "[ mCodUfficioFruitore        = " + mCodUfficioFruitore + " ]\n"
				+ "[ mDescrUfficioFruitore      = " + mDescrUfficioFruitore + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mDescrUfficioInserimento   = " + mDescrUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mDescrUfficioAggiornamento = " + mDescrUfficioAggiornamento + " ]\n"
				+ "[ mFasSieIdFascicoloSiep     = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mEveIdEvento               = " + mEveIdEvento + " ]\n"
				+ "[ mFlagValidato              = " + mFlagValidato + " ]\n"
				+ "[ mNumGiorniFruiti           = " + mNumGiorniFruiti + " ]\n"
				+ "[ mNumGiorniNonFruiti        = " + mNumGiorniNonFruiti + " ]\n"
				+ "[ mDataOdierna               = " + mDataOdierna + " ]";

		return lStr;
	}

	public String toString2() {
		String lStr = new String();

		lStr = "" + mIdFungibilita + " - " + mCodTipoFungibilita + " - " + mDescrTipoFungibilita + " - "
				+ mNumAnni + " - " + mNumMesi + " - " + mNumGiorni + " - " + mDataDa + " - " + mDataA + " - "
				+ mDataInizioValidita + " - " + mDataFineValidita + " - " + mCodUfficioFruitore + " - "
				+ mDescrUfficioFruitore + " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - "
				+ mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mFasSieIdFascicoloSiep + " - " + mEveIdEvento
				+ " - " + mFlagValidato;

		return lStr;
	}

	public void calcolaStringaFungibilita() {
		String lString = "";
		if (this.getNumAnni() != null) {
			if (this.getNumAnni().intValue() != 0)
				lString = "Anni " + this.getNumAnni();
		}
		if (this.getNumMesi() != null) {
			if (this.getNumMesi().intValue() != 0)
				lString += " Mesi " + this.getNumMesi();
		}
		if (this.getNumGiorni() != null) {
			if (this.getNumGiorni().intValue() != 0)
				lString += " Giorni " + this.getNumGiorni();
		}

		if (lString.length() > 1)
			this.mStringaFungibilita = lString;
		else
			this.mStringaFungibilita = null;
	}

}