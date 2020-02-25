package siap.siep.verbale.model;

/**
* <p>Title: VerbaleModel</p>
* <p>Description: Classe Model che rappresenta il Verbale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class VerbaleModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6477998355025551141L;

	private BigDecimal mIdVerbale;
	private String mCodTipoVerbale;
	private String mDescrTipoVerbale;
	private Date mDataEmissione;
	private Date mDataPervenimento;
	private String mCodTipoUfficioFirmatario;
	private String mDescrTipoUfficioFirmatario;
	private String mCodLuogoUfficioFirmatario;
	private String mDescrLuogoUfficioFirmatario;
	private String mNote;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mEveIdEvento;
	private BigDecimal mCssIdCssa;
	private String mIstDetIdIstitutoDetenzione;
	private BigDecimal mFasSiuIdFascicoloSius;
	private String mNumeroProtocollo;
	private BigDecimal mNumAnniEspulsione;
	private BigDecimal mNumMesiEspulsione;
	private BigDecimal mNumGiorniEspulsione;
	private String mStringaEspulsione;

	// COSTRUTTORE DI DEFAULT
	public VerbaleModel() {
		mIdVerbale = null;
		mCodTipoVerbale = "";
		mDescrTipoVerbale = "";
		mDataEmissione = null;
		mDataPervenimento = null;
		mCodTipoUfficioFirmatario = "";
		mDescrTipoUfficioFirmatario = "";
		mCodLuogoUfficioFirmatario = "";
		mDescrLuogoUfficioFirmatario = "";
		mNote = "";
		mCodOperatoreInserimento = "";
		mDataInserimento = null;
		mCodUfficioInserimento = "";
		mDescrUfficioInserimento = "";
		mCodOperatoreAggiornamento = "";
		mDataAggiornamento = null;
		mCodUfficioAggiornamento = "";
		mDescrUfficioAggiornamento = "";
		mEveIdEvento = null;
		mCssIdCssa = null;
		// modifica del 10/03/2004 per trattare l'id dell'istitituto come un BigDecimal
		// mIstDetIdIstitutoDetenzione = "";
		mIstDetIdIstitutoDetenzione = null;
		mFasSiuIdFascicoloSius = null;
		mNumeroProtocollo = "";
		mNumAnniEspulsione = null;
		mNumMesiEspulsione = null;
		mNumGiorniEspulsione = null;

	}

	// COSTRUTTORE DI COPIA
	public VerbaleModel(VerbaleModel aModel) {
		mIdVerbale = aModel.mIdVerbale;
		mCodTipoVerbale = aModel.mCodTipoVerbale;
		mDescrTipoVerbale = aModel.mDescrTipoVerbale;
		mDataEmissione = aModel.mDataEmissione;
		mDataPervenimento = aModel.mDataPervenimento;
		mCodTipoUfficioFirmatario = aModel.mCodTipoUfficioFirmatario;
		mDescrTipoUfficioFirmatario = aModel.mDescrTipoUfficioFirmatario;
		mCodLuogoUfficioFirmatario = aModel.mCodLuogoUfficioFirmatario;
		mDescrLuogoUfficioFirmatario = aModel.mDescrLuogoUfficioFirmatario;
		mNote = aModel.mNote;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		mEveIdEvento = aModel.mEveIdEvento;
		mCssIdCssa = aModel.mCssIdCssa;
		mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		mNumeroProtocollo = aModel.mNumeroProtocollo;
		mNumAnniEspulsione = aModel.mNumAnniEspulsione;
		mNumMesiEspulsione = aModel.mNumMesiEspulsione;
		mNumGiorniEspulsione = aModel.mNumGiorniEspulsione;
	}

	// COSTRUTTORE MODEL
	public VerbaleModel(BigDecimal aIdVerbale, String aCodTipoVerbale, String aDescrTipoVerbale,
			Date aDataEmissione, Date aDataPervenimento, String aCodTipoUfficioFirmatario,
			String aDescrTipoUfficioFirmatario, String aCodLuogoUfficioFirmatario,
			String aDescrLuogoUfficioFirmatario, String aNote, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aEveIdEvento, BigDecimal aCssIdCssa,
			String aIstDetIdIstitutoDetenzione, BigDecimal aFasSiuIdFascicoloSius, String aNumeroProtocollo,
			BigDecimal aNumAnniEspulsione, BigDecimal aNumMesiEspulsione, BigDecimal aNumGiorniEspulsione) {
		mIdVerbale = aIdVerbale;
		mCodTipoVerbale = aCodTipoVerbale;
		mDescrTipoVerbale = aDescrTipoVerbale;
		mDataEmissione = aDataEmissione;
		mDataPervenimento = aDataPervenimento;
		mCodTipoUfficioFirmatario = aCodTipoUfficioFirmatario;
		mDescrTipoUfficioFirmatario = aDescrTipoUfficioFirmatario;
		mCodLuogoUfficioFirmatario = aCodLuogoUfficioFirmatario;
		mDescrLuogoUfficioFirmatario = aDescrLuogoUfficioFirmatario;
		mNote = aNote;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mDataInserimento = aDataInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDescrUfficioInserimento = aDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		mEveIdEvento = aEveIdEvento;
		mCssIdCssa = aCssIdCssa;
		mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		mNumeroProtocollo = aNumeroProtocollo;
		mNumAnniEspulsione = aNumAnniEspulsione;
		mNumMesiEspulsione = aNumMesiEspulsione;
		mNumGiorniEspulsione = aNumGiorniEspulsione;

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdVerbale() {
		return mIdVerbale;
	}

	public String getCodTipoVerbale() {
		return mCodTipoVerbale;
	}

	public String getDescrTipoVerbale() {
		return mDescrTipoVerbale;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public Date getDataPervenimento() {
		return mDataPervenimento;
	}

	public String getCodTipoUfficioFirmatario() {
		return mCodTipoUfficioFirmatario;
	}

	public String getDescrTipoUfficioFirmatario() {
		return mDescrTipoUfficioFirmatario;
	}

	public String getCodLuogoUfficioFirmatario() {
		return mCodLuogoUfficioFirmatario;
	}

	public String getDescrLuogoUfficioFirmatario() {
		return mDescrLuogoUfficioFirmatario;
	}

	public String getNote() {
		return mNote;
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

	public BigDecimal getCssIdCssa() {
		return mCssIdCssa;
	}

	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public String getNumeroProtocollo() {
		return mNumeroProtocollo;
	}

	public BigDecimal getNumAnniEspulsione() {
		if (mNumAnniEspulsione != null)
			return mNumAnniEspulsione;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumMesiEspulsione() {
		if (mNumMesiEspulsione != null)
			return mNumMesiEspulsione;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumGiorniEspulsione() {
		if (mNumGiorniEspulsione != null)
			return mNumGiorniEspulsione;
		else
			return new BigDecimal(0);
	}

	public String getStringaEspulsione() {
		return mStringaEspulsione;
	}

	//
	// METODI SET()
	//

	public void setIdVerbale(BigDecimal aValore) {
		mIdVerbale = aValore;
	}

	public void setCodTipoVerbale(String aValore) {
		mCodTipoVerbale = aValore;
	}

	public void setDescrTipoVerbale(String aValore) {
		mDescrTipoVerbale = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setDataPervenimento(Date aValore) {
		mDataPervenimento = aValore;
	}

	public void setCodTipoUfficioFirmatario(String aValore) {
		mCodTipoUfficioFirmatario = aValore;
	}

	public void setDescrTipoUfficioFirmatario(String aValore) {
		mDescrTipoUfficioFirmatario = aValore;
	}

	public void setCodLuogoUfficioFirmatario(String aValore) {
		mCodLuogoUfficioFirmatario = aValore;
	}

	public void setDescrLuogoUfficioFirmatario(String aValore) {
		mDescrLuogoUfficioFirmatario = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
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

	public void setCssIdCssa(BigDecimal aValore) {
		mCssIdCssa = aValore;
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setNumeroProtocollo(String aValore) {
		mNumeroProtocollo = aValore;
	}

	public void setNumAnniEspulsione(BigDecimal aValore) {
		mNumAnniEspulsione = aValore;
	}

	public void setNumMesiEspulsione(BigDecimal aValore) {
		mNumMesiEspulsione = aValore;
	}

	public void setNumGiorniEspulsione(BigDecimal aValore) {
		mNumGiorniEspulsione = aValore;
	}

	public void setStringaEspulsione(String aValore) {
		mStringaEspulsione = aValore;
	}

	public String toString2() {
		String lStr = new String();

		lStr = "" + mIdVerbale + " - " + mCodTipoVerbale + " - " + mDescrTipoVerbale + " - " + mDataEmissione
				+ " - " + mDataPervenimento + " - " + mCodTipoUfficioFirmatario + " - "
				+ mDescrTipoUfficioFirmatario + " - " + mCodLuogoUfficioFirmatario + " - "
				+ mDescrLuogoUfficioFirmatario + " - " + mNote + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mEveIdEvento + " - " + mCssIdCssa + " - "
				+ mIstDetIdIstitutoDetenzione + " - " + mFasSiuIdFascicoloSius + " - " + mNumeroProtocollo
				+ " - " + mNumAnniEspulsione + " - " + mNumMesiEspulsione + " - " + mNumGiorniEspulsione;
		return lStr;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "VerbaleModel:\n" + "[ mIdVerbale                  = " + mIdVerbale + " ]\n"
				+ "[ mEveIdEvento                = " + mEveIdEvento + " ]\n"
				+ "[ mCodTipoVerbale             = " + mCodTipoVerbale + " ]\n"
				+ "[ mDescrTipoVerbale           = " + mDescrTipoVerbale + " ]\n"
				+ "[ mDataEmissione              = " + mDataEmissione + " ]\n"
				+ "[ mDataPervenimento           = " + mDataPervenimento + " ]\n"
				+ "[ mCodTipoUfficioFirmatario   = " + mCodTipoUfficioFirmatario + " ]\n"
				+ "[ mDescrTipoUfficioFirmatario = " + mDescrTipoUfficioFirmatario + " ]\n"
				+ "[ mCodLuogoUfficioFirmatario  = " + mCodLuogoUfficioFirmatario + " ]\n"
				+ "[ mDescrLuogoUfficioFirmatario  = " + mDescrLuogoUfficioFirmatario + " ]\n"
				+ "[ mNote                       = " + mNote + " ]\n" + "[ mNumeroProtocollo           = "
				+ mNumeroProtocollo + " ]\n" + "[ mNumAnniEspulsione          = " + mNumAnniEspulsione
				+ " ]\n" + "[ mNumMesiEspulsione          = " + mNumMesiEspulsione + " ]\n"
				+ "[ mNumGiorniEspulsione        = " + mNumGiorniEspulsione + " ]\n"
				+ "[ mCssIdCssa                  = " + mCssIdCssa + " ]\n"
				+ "[ mIstDetIdIstitutoDetenzione = " + mIstDetIdIstitutoDetenzione + " ]\n"
				+ "[ mFasSiuIdFascicoloSius      = " + mFasSiuIdFascicoloSius + " ]\n"
				+ "[ mCodOperatoreInserimento    = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento            = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento      = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento  = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento          = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento    = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

	/**
	 * calcolaStringaEspulsione per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaEspulsione() {
		String lStringEspulsione = "";
		if (getNumAnniEspulsione() != null) {
			if (getNumAnniEspulsione().intValue() != 0)
				lStringEspulsione = "anni " + getNumAnniEspulsione();
		}
		if (getNumMesiEspulsione() != null) {
			if (getNumMesiEspulsione().intValue() != 0)
				lStringEspulsione += " mesi " + getNumMesiEspulsione();
		}
		if (getNumGiorniEspulsione() != null) {
			if (getNumGiorniEspulsione().intValue() != 0)
				lStringEspulsione += " giorni " + getNumGiorniEspulsione();
		}

		if (lStringEspulsione.length() > 1)
			mStringaEspulsione = lStringEspulsione;

		else
			mStringaEspulsione = null;
	}

}