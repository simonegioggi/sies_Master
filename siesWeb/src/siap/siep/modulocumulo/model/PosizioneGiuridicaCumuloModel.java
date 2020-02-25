package siap.siep.modulocumulo.model;

/**
* <p>Title: PosizioneGiuridicaCumuloModel</p>
* <p>Description: Classe Model che rappresenta il PosizioneGiuridicaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;

public class PosizioneGiuridicaCumuloModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -367522939058980256L;
	private BigDecimal mIdPosizioneGiuridicaCum;
	private String mCodPosizioneGiuridica;
	private String mDescrPosizioneGiuridica;
	private Date mDataInizio;

	private String mIstDetIdIstitutoDetenzione;
	private String mAltroLuogo;

	private BigDecimal mChiaveAnnoFasSius;
	private BigDecimal mChiaveProgrFasSius;
	private String mChiaveUffFasSius;
	private BigDecimal mAnnoRegistro;
	private BigDecimal mNumeroRegistro;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private Date mDataEmissioneProvv;
	private BigDecimal mNumAnniMisura;
	private BigDecimal mNumMesiMisura;
	private BigDecimal mNumGiorniMisura;
	private Date mDataFineMisura;
	private String mFlagDecisioneTDS;

	private String mFlagDifferimentoDetDom;
	private Date mDataInizioMisura;

	private BigDecimal mDatIdDatiFinaliCumulo;
	private BigDecimal mIstrIdIstruttoriaCumulo;
	private BigDecimal mTitIdTitoloCumulato;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private String mTipoPosGiu;
	private IstitutoDetenzioneModel mIstitutoDetenzione;
	private UfficioModel mUfficioSorv;

	private String mDescrIstituto;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public PosizioneGiuridicaCumuloModel() {
		this.mIdPosizioneGiuridicaCum = null;
		this.mCodPosizioneGiuridica = "";
		this.mDescrPosizioneGiuridica = "";
		this.mDataInizio = null;
		this.mIstDetIdIstitutoDetenzione = "";
		this.mAltroLuogo = "";

		this.mChiaveAnnoFasSius = null;
		this.mChiaveProgrFasSius = null;
		this.mChiaveUffFasSius = "";
		this.mAnnoRegistro = null;
		this.mNumeroRegistro = null;
		this.mCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";
		this.mDataEmissioneProvv = null;
		this.mNumAnniMisura = null;
		this.mNumMesiMisura = null;
		this.mNumGiorniMisura = null;
		this.mDataFineMisura = null;
		this.mFlagDecisioneTDS = null;

		this.mFlagDifferimentoDetDom = "";
		this.mDataInizioMisura = null;

		this.mDatIdDatiFinaliCumulo = null;
		this.mIstrIdIstruttoriaCumulo = null;
		this.mTitIdTitoloCumulato = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public PosizioneGiuridicaCumuloModel(PosizioneGiuridicaCumuloModel aModel) {
		this.mIdPosizioneGiuridicaCum = aModel.mIdPosizioneGiuridicaCum;
		this.mCodPosizioneGiuridica = aModel.mCodPosizioneGiuridica;
		this.mDescrPosizioneGiuridica = aModel.mDescrPosizioneGiuridica;
		this.mDataInizio = aModel.mDataInizio;

		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mAltroLuogo = aModel.mAltroLuogo;

		this.mChiaveAnnoFasSius = aModel.mChiaveAnnoFasSius;
		this.mChiaveProgrFasSius = aModel.mChiaveProgrFasSius;
		this.mChiaveUffFasSius = aModel.mChiaveUffFasSius;
		this.mAnnoRegistro = aModel.mAnnoRegistro;
		this.mNumeroRegistro = aModel.mNumeroRegistro;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mDataEmissioneProvv = aModel.mDataEmissioneProvv;
		this.mNumAnniMisura = aModel.mNumAnniMisura;
		this.mNumMesiMisura = aModel.mNumMesiMisura;
		this.mNumGiorniMisura = aModel.mNumGiorniMisura;
		this.mDataFineMisura = aModel.mDataFineMisura;
		this.mFlagDecisioneTDS = aModel.mFlagDecisioneTDS;

		this.mFlagDifferimentoDetDom = aModel.mFlagDifferimentoDetDom;
		this.mDataInizioMisura = aModel.mDataInizioMisura;

		this.mDatIdDatiFinaliCumulo = aModel.mDatIdDatiFinaliCumulo;
		this.mIstrIdIstruttoriaCumulo = aModel.mIstrIdIstruttoriaCumulo;
		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public PosizioneGiuridicaCumuloModel(BigDecimal aIdPosizioneGiuridicaCum, String aCodPosizioneGiuridica,
			String aDescrPosizioneGiuridica, Date aDataInizio, String aIstDetIdIstitutoDetenzione,
			String aAltroLuogo, BigDecimal aChiaveAnnoFasSius, BigDecimal aChiaveProgrFasSius,
			String aChiaveUffFasSius, BigDecimal aAnnoRegistro, BigDecimal aNumeroRegistro,
			String aCodTipoProvvedimento, String aDescrTipoProvvedimento, Date aDataEmissioneProvv,
			BigDecimal aNumAnniMisura, BigDecimal aNumMesiMisura, BigDecimal aNumGiorniMisura,
			Date aDataFineMisura, String aFlagDecisioneTDS,

			String aFlagDifferimentoDetDom, Date aDataInizioMisura,

			BigDecimal aDatIdDatiFinaliCumulo, BigDecimal aIstrIdIstruttoriaCumulo,
			BigDecimal aTitIdTitoloCumulato, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento) {
		this.mIdPosizioneGiuridicaCum = aIdPosizioneGiuridicaCum;
		this.mCodPosizioneGiuridica = aCodPosizioneGiuridica;
		this.mDescrPosizioneGiuridica = aDescrPosizioneGiuridica;
		this.mDataInizio = aDataInizio;
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		this.mAltroLuogo = aAltroLuogo;
		this.mChiaveAnnoFasSius = aChiaveAnnoFasSius;
		this.mChiaveProgrFasSius = aChiaveProgrFasSius;
		this.mChiaveUffFasSius = aChiaveUffFasSius;
		this.mAnnoRegistro = aAnnoRegistro;
		this.mNumeroRegistro = aNumeroRegistro;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mDataEmissioneProvv = aDataEmissioneProvv;
		this.mNumAnniMisura = aNumAnniMisura;
		this.mNumMesiMisura = aNumMesiMisura;
		this.mNumGiorniMisura = aNumGiorniMisura;
		this.mDataFineMisura = aDataFineMisura;
		this.mFlagDecisioneTDS = aFlagDecisioneTDS;

		this.mFlagDifferimentoDetDom = aFlagDifferimentoDetDom;
		this.mDataInizioMisura = aDataInizioMisura;

		this.mDatIdDatiFinaliCumulo = aDatIdDatiFinaliCumulo;
		this.mIstrIdIstruttoriaCumulo = aIstrIdIstruttoriaCumulo;
		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdPosizioneGiuridicaCum() {
		return mIdPosizioneGiuridicaCum;
	}

	public String getCodPosizioneGiuridica() {
		return mCodPosizioneGiuridica;
	}

	public String getDescrPosizioneGiuridica() {
		return mDescrPosizioneGiuridica;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	public String getAltroLuogo() {
		return mAltroLuogo;
	}

	public BigDecimal getChiaveAnnoFasSius() {
		return mChiaveAnnoFasSius;
	}

	public BigDecimal getChiaveProgrFasSius() {
		return mChiaveProgrFasSius;
	}

	public String getChiaveUffFasSius() {
		return mChiaveUffFasSius;
	}

	public BigDecimal getAnnoRegistro() {
		return mAnnoRegistro;
	}

	public BigDecimal getNumeroRegistro() {
		return mNumeroRegistro;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public Date getDataEmissioneProvv() {
		return mDataEmissioneProvv;
	}

	public BigDecimal getNumAnniMisura() {
		return mNumAnniMisura;
	}

	public BigDecimal getNumMesiMisura() {
		return mNumMesiMisura;
	}

	public BigDecimal getNumGiorniMisura() {
		return mNumGiorniMisura;
	}

	public Date getDataFineMisura() {
		return mDataFineMisura;
	}

	public String getFlagDecisioneTDS() {
		return mFlagDecisioneTDS;
	}

	public String getFlagDifferimentoDetDom() {
		return mFlagDifferimentoDetDom;
	}

	public Date getDataInizioMisura() {
		return mDataInizioMisura;
	}

	public BigDecimal getDatIdDatiFinaliCumulo() {
		return mDatIdDatiFinaliCumulo;
	}

	public BigDecimal getIstrIdIstruttoriaCumulo() {
		return mIstrIdIstruttoriaCumulo;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
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

	public String getTipoPosGiu() {
		return mTipoPosGiu;
	}

	public IstitutoDetenzioneModel getIstitutoDetenzione() {
		return mIstitutoDetenzione;
	}

	public UfficioModel getUfficioSorv() {
		return mUfficioSorv;
	}

	public String getDescrIstituto() {
		return mDescrIstituto;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdPosizioneGiuridicaCum(BigDecimal aValore) {
		mIdPosizioneGiuridicaCum = aValore;
	}

	public void setCodPosizioneGiuridica(String aValore) {
		mCodPosizioneGiuridica = aValore;
	}

	public void setDescrPosizioneGiuridica(String aValore) {
		mDescrPosizioneGiuridica = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	public void setAltroLuogo(String aValore) {
		mAltroLuogo = aValore;
	}

	public void setChiaveAnnoFasSius(BigDecimal aValore) {
		mChiaveAnnoFasSius = aValore;
	}

	public void setChiaveProgrFasSius(BigDecimal aValore) {
		mChiaveProgrFasSius = aValore;
	}

	public void setChiaveUffFasSius(String aValore) {
		mChiaveUffFasSius = aValore;
	}

	public void setAnnoRegistro(BigDecimal aValore) {
		mAnnoRegistro = aValore;
	}

	public void setNumeroRegistro(BigDecimal aValore) {
		mNumeroRegistro = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setDataEmissioneProvv(Date aValore) {
		mDataEmissioneProvv = aValore;
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

	public void setDataFineMisura(Date aValore) {
		mDataFineMisura = aValore;
	}

	public void setFlagDecisioneTDS(String aValore) {
		mFlagDecisioneTDS = aValore;
	}

	public void setFlagDifferimentoDetDom(String aValore) {
		mFlagDifferimentoDetDom = aValore;
	}

	public void setDataInizioMisura(Date aValore) {
		mDataInizioMisura = aValore;
	}

	public void setDatIdDatiFinaliCumulo(BigDecimal aValore) {
		mDatIdDatiFinaliCumulo = aValore;
	}

	public void setIstrIdIstruttoriaCumulo(BigDecimal aValore) {
		mIstrIdIstruttoriaCumulo = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
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

	public void setTipoPosGiu(String aValore) {
		mTipoPosGiu = aValore;
	}

	public void setIstitutoDetenzione(IstitutoDetenzioneModel aValore) {
		mIstitutoDetenzione = aValore;
	}

	public void setUfficioSorv(UfficioModel aValore) {
		mUfficioSorv = aValore;
	}

	public void setDescrIstituto(String aValore) {
		mDescrIstituto = aValore;
	}

	public boolean isMisura() {
		boolean isMisura = false;

		if (mCodPosizioneGiuridica != null) {
			if (mCodPosizioneGiuridica.equals("12") || mCodPosizioneGiuridica.equals("13")
					|| mCodPosizioneGiuridica.equals("29") || mCodPosizioneGiuridica.equals("54")
					|| mCodPosizioneGiuridica.equals("16") || mCodPosizioneGiuridica.equals("17")) {
				return true;
			}
		}

		return isMisura;

	}

	public String getStringaDurata() {
		String lStrDurata = "";

		if (mNumAnniMisura != null) {
			lStrDurata += " Anni " + mNumAnniMisura;
		}
		if (mNumMesiMisura != null) {
			lStrDurata += " Mesi " + mNumMesiMisura;
		}
		if (mNumGiorniMisura != null) {
			lStrDurata += " Giorni " + mNumGiorniMisura;
		}

		if ("".equals(lStrDurata))
			lStrDurata = null;
		else
			lStrDurata = lStrDurata.substring(1); // tolgo il primo spazio

		return lStrDurata;

	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "PosizioneGiuridicaCumuloModel:\n" + "[ mIdPosizioneGiuridicaCum    = "
				+ mIdPosizioneGiuridicaCum + " ]\n" + "[ mCodPosizioneGiuridica      = "
				+ mCodPosizioneGiuridica + " ]\n" + "[ mTipoPosGiu                 = " + mTipoPosGiu + " ]\n"
				+ "[ mDataInizio                 = " + mDataInizio + " ]\n"
				+ "[ mIstDetIdIstitutoDetenzione = " + mIstDetIdIstitutoDetenzione + " ]\n"
				+ "[ mAltroLuogo                 = " + mAltroLuogo + " ]\n"
				+ "[ mChiaveAnnoFasSius          = " + mChiaveAnnoFasSius + " ]\n"
				+ "[ mChiaveProgrFasSius         = " + mChiaveProgrFasSius + " ]\n"
				+ "[ mChiaveUffFasSius           = " + mChiaveUffFasSius + " ]\n"
				+ "[ mAnnoRegistro               = " + mAnnoRegistro + " ]\n"
				+ "[ mNumeroRegistro             = " + mNumeroRegistro + " ]\n"
				+ "[ mCodTipoProvvedimento       = " + mCodTipoProvvedimento + " ]\n"
				+ "[ mDataEmissioneProvv         = " + mDataEmissioneProvv + " ]\n"
				+ "[ mNumAnniMisura              = " + mNumAnniMisura + " ]\n"
				+ "[ mNumMesiMisura              = " + mNumMesiMisura + " ]\n"
				+ "[ mNumGiorniMisura            = " + mNumGiorniMisura + " ]\n"
				+ "[ mDataFineMisura             = " + mDataFineMisura + " ]\n"
				+ "[ mFlagDecisioneTDS			  = " + mFlagDecisioneTDS + " ]\n"
				+ "[ mFlagDifferimentoDetDom	  = " + mFlagDifferimentoDetDom + " ]\n"
				+ "[ mDataInizioMisura           = " + mDataInizioMisura + " ]\n"
				+ "[ mDatIdDatiFinaliCumulo      = " + mDatIdDatiFinaliCumulo + " ]\n"
				+ "[ mIstrIdIstruttoriaCumulo    = " + mIstrIdIstruttoriaCumulo + " ]\n"
				+ "[ mTitIdTitoloCumulato	      = " + mTitIdTitoloCumulato + " ]\n"
				+ "[ mCodOperatoreInserimento    = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento            = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento      = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento  = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento          = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento    = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}
}
