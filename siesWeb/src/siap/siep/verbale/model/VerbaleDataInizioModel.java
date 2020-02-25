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

public class VerbaleDataInizioModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6785960617815471132L;

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

	private BigDecimal mNumAnniMisura;
	private BigDecimal mNumMesiMisura;
	private BigDecimal mNumGiorniMisura;
	private Date mDataInizioMisura;
	private Date mDataFineMisura;

	// COSTRUTTORE DI DEFAULT
	public VerbaleDataInizioModel() {
		this.mIdVerbale = null;
		this.mCodTipoVerbale = "";
		this.mDescrTipoVerbale = "";
		this.mDataEmissione = null;
		this.mDataPervenimento = null;
		this.mCodTipoUfficioFirmatario = "";
		this.mDescrTipoUfficioFirmatario = "";
		this.mCodLuogoUfficioFirmatario = "";
		this.mDescrLuogoUfficioFirmatario = "";
		this.mNote = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mEveIdEvento = null;
		this.mCssIdCssa = null;
		// modifica del 10/03/2004 per trattare l'id dell'istitituto come un BigDecimal
		// this.mIstDetIdIstitutoDetenzione = "";
		this.mIstDetIdIstitutoDetenzione = null;
		this.mFasSiuIdFascicoloSius = null;
		this.mNumeroProtocollo = "";
		this.mNumAnniEspulsione = null;
		this.mNumMesiEspulsione = null;
		this.mNumGiorniEspulsione = null;

		this.mNumAnniMisura = null;
		this.mNumMesiMisura = null;
		this.mNumGiorniMisura = null;
		this.mDataInizioMisura = null;
		this.mDataFineMisura = null;

	}

	// COSTRUTTORE DI COPIA
	public VerbaleDataInizioModel(VerbaleDataInizioModel aModel) {
		this.mIdVerbale = aModel.mIdVerbale;
		this.mCodTipoVerbale = aModel.mCodTipoVerbale;
		this.mDescrTipoVerbale = aModel.mDescrTipoVerbale;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mDataPervenimento = aModel.mDataPervenimento;
		this.mCodTipoUfficioFirmatario = aModel.mCodTipoUfficioFirmatario;
		this.mDescrTipoUfficioFirmatario = aModel.mDescrTipoUfficioFirmatario;
		this.mCodLuogoUfficioFirmatario = aModel.mCodLuogoUfficioFirmatario;
		this.mDescrLuogoUfficioFirmatario = aModel.mDescrLuogoUfficioFirmatario;
		this.mNote = aModel.mNote;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mCssIdCssa = aModel.mCssIdCssa;
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mNumeroProtocollo = aModel.mNumeroProtocollo;
		this.mNumAnniEspulsione = aModel.mNumAnniEspulsione;
		this.mNumMesiEspulsione = aModel.mNumMesiEspulsione;
		this.mNumGiorniEspulsione = aModel.mNumGiorniEspulsione;

		this.mNumAnniMisura = aModel.mNumAnniMisura;
		this.mNumMesiMisura = aModel.mNumMesiMisura;
		this.mNumGiorniMisura = aModel.mNumGiorniMisura;
		this.mDataInizioMisura = aModel.mDataInizioMisura;
		this.mDataFineMisura = aModel.mDataFineMisura;
	}

	// COSTRUTTORE MODEL
	public VerbaleDataInizioModel(BigDecimal aIdVerbale, String aCodTipoVerbale, String aDescrTipoVerbale,
			Date aDataEmissione, Date aDataPervenimento, String aCodTipoUfficioFirmatario,
			String aDescrTipoUfficioFirmatario, String aCodLuogoUfficioFirmatario,
			String aDescrLuogoUfficioFirmatario, String aNote, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aEveIdEvento, BigDecimal aCssIdCssa,
			String aIstDetIdIstitutoDetenzione, BigDecimal aFasSiuIdFascicoloSius, String aNumeroProtocollo,
			BigDecimal aNumAnniEspulsione, BigDecimal aNumMesiEspulsione, BigDecimal aNumGiorniEspulsione,

			BigDecimal aNumAnniMisura, BigDecimal aNumMesiMisura, BigDecimal aNumGiorniMisura,
			Date aDataInizioMisura, Date aDataFineMisura

	) {
		this.mIdVerbale = aIdVerbale;
		this.mCodTipoVerbale = aCodTipoVerbale;
		this.mDescrTipoVerbale = aDescrTipoVerbale;
		this.mDataEmissione = aDataEmissione;
		this.mDataPervenimento = aDataPervenimento;
		this.mCodTipoUfficioFirmatario = aCodTipoUfficioFirmatario;
		this.mDescrTipoUfficioFirmatario = aDescrTipoUfficioFirmatario;
		this.mCodLuogoUfficioFirmatario = aCodLuogoUfficioFirmatario;
		this.mDescrLuogoUfficioFirmatario = aDescrLuogoUfficioFirmatario;
		this.mNote = aNote;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mEveIdEvento = aEveIdEvento;
		this.mCssIdCssa = aCssIdCssa;
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mNumeroProtocollo = aNumeroProtocollo;
		this.mNumAnniEspulsione = aNumAnniEspulsione;
		this.mNumMesiEspulsione = aNumMesiEspulsione;
		this.mNumGiorniEspulsione = aNumGiorniEspulsione;

		this.mNumAnniMisura = aNumAnniMisura;
		this.mNumMesiMisura = aNumMesiMisura;
		this.mNumGiorniMisura = aNumGiorniMisura;
		this.mDataInizioMisura = aDataInizioMisura;
		this.mDataFineMisura = aDataFineMisura;

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

	public BigDecimal mNumAnniMisura() {
		if (mNumAnniMisura != null)
			return mNumAnniMisura;
		else
			return new BigDecimal(0);
	}

	public BigDecimal mNumMesiMisura() {
		if (mNumMesiMisura != null)
			return mNumMesiMisura;
		else
			return new BigDecimal(0);
	}

	public BigDecimal mNumGiorniMisura() {
		if (mNumGiorniMisura != null)
			return mNumGiorniMisura;
		else
			return new BigDecimal(0);
	}

	public Date getDataInizioMisura() {
		return mDataInizioMisura;
	}

	public Date getDataFineMisura() {
		return mDataFineMisura;
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

	public void setNumAnniMisura(BigDecimal aValore) {
		mNumAnniMisura = aValore;
	}

	public void setNumMesiMisura(BigDecimal aValore) {
		mNumMesiMisura = aValore;
	}

	public void setNumGiorniMisura(BigDecimal aValore) {
		mNumGiorniMisura = aValore;
	}

	public void setDataInizioMisura(Date aValore) {
		mDataInizioMisura = aValore;
	}

	public void setDataFineMisura(Date aValore) {
		mDataFineMisura = aValore;
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
				+ " - " + mNumAnniEspulsione + " - " + mNumMesiEspulsione + " - " + mNumGiorniEspulsione
				+ " - " + mNumAnniMisura + " - " + mNumMesiMisura + " - " + mNumGiorniMisura + " - "
				+ mDataInizioMisura + " - " + mDataInizioMisura;

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
				+ "[ mCodUfficioAggiornamento    = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mNumAnniMisura             = " + mNumAnniMisura + " ]\n"
				+ "[ mNumMesiMisura             = " + mNumMesiMisura + " ]\n"
				+ "[ mNumGiorniMisura           = " + mNumGiorniMisura + " ]\n"
				+ "[ mDataInizioMisura          = " + mDataInizioMisura + " ]\n"
				+ "[ mDataFineMisura            = " + mDataFineMisura + " ]";

		return lStr;
	}

	/**
	 * calcolaStringaEspulsione per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaEspulsione() {
		String lStringEspulsione = "";
		if (this.getNumAnniEspulsione() != null) {
			if (this.getNumAnniEspulsione().intValue() != 0)
				lStringEspulsione = "anni " + this.getNumAnniEspulsione();
		}
		if (this.getNumMesiEspulsione() != null) {
			if (this.getNumMesiEspulsione().intValue() != 0)
				lStringEspulsione += " mesi " + this.getNumMesiEspulsione();
		}
		if (this.getNumGiorniEspulsione() != null) {
			if (this.getNumGiorniEspulsione().intValue() != 0)
				lStringEspulsione += " giorni " + this.getNumGiorniEspulsione();
		}

		if (lStringEspulsione.length() > 1)
			this.mStringaEspulsione = lStringEspulsione;

		else
			this.mStringaEspulsione = null;
	}

}