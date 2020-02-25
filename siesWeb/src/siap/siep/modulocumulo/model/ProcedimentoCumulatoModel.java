package siap.siep.modulocumulo.model;

/**
* <p>Title: ProcedimentoCumulatoModel</p>
* <p>Description: Classe Model che rappresenta il ProcedimentoCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;

public class ProcedimentoCumulatoModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -6390402150623362839L;
	private BigDecimal mIdProcedimentoCumulato;
	private BigDecimal mChiaveAnnoFasCumulato;
	private BigDecimal mChiaveProgrFasCumulato;
	private String mCodTipoUfficioFasCumulato;
	private String mDescrTipoUfficioFasCumulato;
	private String mCodLuogoUfficioFasCumulato;
	private String mDescrLuogoUfficioFasCumulato;
	private String mCodUfficioFasCumulato;
	private String mDescrUfficioFasCumulato;
	private BigDecimal mKeyProvvNsc;

	private Date mDataRichiestaFascicolo;
	private Date mDataPervenimentoFascicolo;
	private String mNote;

	private String mFlagAccorpato;
	private String mChiaveUfficioOrigine;
	private BigDecimal mChiaveProgrOrigine;

	private BigDecimal mTitIdTitoloCumulato;
	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mIdFascicoloSiepOrigine;
	private BigDecimal mEveIdEvento;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private UfficioModel mUfficioOrigine = null;

	private String mStringaProcedimento = null;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public ProcedimentoCumulatoModel() {
		this.mIdProcedimentoCumulato = null;
		this.mChiaveAnnoFasCumulato = null;
		this.mChiaveProgrFasCumulato = null;
		this.mCodTipoUfficioFasCumulato = "";
		this.mDescrTipoUfficioFasCumulato = "";
		this.mCodLuogoUfficioFasCumulato = "";
		this.mDescrLuogoUfficioFasCumulato = "";
		this.mCodUfficioFasCumulato = "";
		this.mDescrUfficioFasCumulato = "";
		this.mKeyProvvNsc = null;

		this.mDataRichiestaFascicolo = null;
		this.mDataPervenimentoFascicolo = null;
		this.mNote = "";
		this.mIdFascicoloSiepOrigine = null;
		this.mEveIdEvento = null;

		this.mFlagAccorpato = "";
		this.mChiaveUfficioOrigine = "";
		this.mChiaveProgrOrigine = null;

		this.mTitIdTitoloCumulato = null;
		this.mFlagStato = "";
		this.mMotivoModifica = "";

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";

		this.mUfficioOrigine = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public ProcedimentoCumulatoModel(ProcedimentoCumulatoModel aModel) {
		this.mIdProcedimentoCumulato = aModel.mIdProcedimentoCumulato;
		this.mChiaveAnnoFasCumulato = aModel.mChiaveAnnoFasCumulato;
		this.mChiaveProgrFasCumulato = aModel.mChiaveProgrFasCumulato;
		this.mCodTipoUfficioFasCumulato = aModel.mCodTipoUfficioFasCumulato;
		this.mDescrTipoUfficioFasCumulato = aModel.mDescrTipoUfficioFasCumulato;
		this.mCodLuogoUfficioFasCumulato = aModel.mCodLuogoUfficioFasCumulato;
		this.mDescrLuogoUfficioFasCumulato = aModel.mDescrLuogoUfficioFasCumulato;
		this.mCodUfficioFasCumulato = aModel.mCodUfficioFasCumulato;
		this.mDescrUfficioFasCumulato = aModel.mDescrUfficioFasCumulato;
		this.mKeyProvvNsc = aModel.mKeyProvvNsc;

		this.mDataRichiestaFascicolo = aModel.mDataRichiestaFascicolo;
		this.mDataPervenimentoFascicolo = aModel.mDataPervenimentoFascicolo;
		this.mNote = aModel.mNote;
		this.mIdFascicoloSiepOrigine = aModel.mIdFascicoloSiepOrigine;
		this.mEveIdEvento = aModel.mEveIdEvento;

		this.mFlagAccorpato = aModel.mFlagAccorpato;
		this.mChiaveUfficioOrigine = aModel.mChiaveUfficioOrigine;
		this.mChiaveProgrOrigine = aModel.mChiaveProgrOrigine;

		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mFlagStato = aModel.mFlagStato;
		this.mMotivoModifica = aModel.mMotivoModifica;

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
	public ProcedimentoCumulatoModel(BigDecimal aIdProcedimentoCumulato, BigDecimal aChiaveAnnoFasCumulato,
			BigDecimal aChiaveProgrFasCumulato, String aCodTipoUfficioFasCumulato,
			String aDescrTipoUfficioFasCumulato, String aCodLuogoUfficioFasCumulato,
			String aDescrLuogoUfficioFasCumulato, String aCodUfficioFasCumulato,
			String aDescrUfficioFasCumulato, BigDecimal aKeyProvvNsc,

			Date aDataRichiestaFascicolo, Date aDataPervenimentoFascicolo, String aNote,
			BigDecimal aIdFascicoloSiepOrigine, BigDecimal aEveIdEvento,

			String aFlagAccorpato, String aChiaveUfficioOrigine, BigDecimal aChiaveProgrOrigine,

			BigDecimal aTitIdTitoloCumulato, String aFlagStato, String aMotivoModifica,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdProcedimentoCumulato = aIdProcedimentoCumulato;
		this.mChiaveAnnoFasCumulato = aChiaveAnnoFasCumulato;
		this.mChiaveProgrFasCumulato = aChiaveProgrFasCumulato;
		this.mCodTipoUfficioFasCumulato = aCodTipoUfficioFasCumulato;
		this.mDescrTipoUfficioFasCumulato = aDescrTipoUfficioFasCumulato;
		this.mCodLuogoUfficioFasCumulato = aCodLuogoUfficioFasCumulato;
		this.mDescrLuogoUfficioFasCumulato = aDescrLuogoUfficioFasCumulato;
		this.mCodUfficioFasCumulato = aCodUfficioFasCumulato;
		this.mDescrUfficioFasCumulato = aDescrUfficioFasCumulato;
		this.mKeyProvvNsc = aKeyProvvNsc;

		this.mDataRichiestaFascicolo = aDataRichiestaFascicolo;
		this.mDataPervenimentoFascicolo = aDataPervenimentoFascicolo;
		this.mNote = aNote;
		this.mIdFascicoloSiepOrigine = aIdFascicoloSiepOrigine;
		this.mEveIdEvento = aEveIdEvento;

		this.mFlagAccorpato = aFlagAccorpato;
		this.mChiaveUfficioOrigine = aChiaveUfficioOrigine;
		this.mChiaveProgrOrigine = aChiaveProgrOrigine;

		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mFlagStato = aFlagStato;
		this.mMotivoModifica = aMotivoModifica;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	/**
	 * Costruttore di estrazione dati a partire da FascicoloSiepModel
	 * 
	 * @param aFascicoloModel
	 */
	public ProcedimentoCumulatoModel(FascicoloSiepModel aFascicoloModel) {
		this.mChiaveAnnoFasCumulato = aFascicoloModel.getChiaveAnno();
		this.mChiaveProgrFasCumulato = aFascicoloModel.getChiaveProgr();
		this.mCodUfficioFasCumulato = aFascicoloModel.getChiaveUfficio();

		this.mKeyProvvNsc = aFascicoloModel.getKeyProvvNsc();

		this.mIdFascicoloSiepOrigine = aFascicoloModel.getIdFascicoloSiep();

		if (aFascicoloModel.getChiaveProgrOrig() != null) {
			mFlagAccorpato = "S";
			mChiaveProgrOrigine = aFascicoloModel.getChiaveProgrOrig();

			BigDecimal lIncrement = null;
			lIncrement = aFascicoloModel.getChiaveProgr().subtract(aFascicoloModel.getChiaveProgrOrig());

			UfficioModel lUfficio = new UfficioModel();

			try {
				IUfficio lUff = SICOLookupRemote.getUfficioRemote();
				lUfficio = lUff.getUfficioAccorpatoByAccorpanteIncrement(aFascicoloModel.getChiaveUfficio(),
						"" + lIncrement);

				this.mChiaveUfficioOrigine = lUfficio.getCodUfficio();
			} catch (Exception e) {
			}

		}
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdProcedimentoCumulato() {
		return mIdProcedimentoCumulato;
	}

	public BigDecimal getChiaveAnnoFasCumulato() {
		return mChiaveAnnoFasCumulato;
	}

	public BigDecimal getChiaveProgrFasCumulato() {
		return mChiaveProgrFasCumulato;
	}

	public String getCodTipoUfficioFasCumulato() {
		return mCodTipoUfficioFasCumulato;
	}

	public String getDescrTipoUfficioFasCumulato() {
		return mDescrTipoUfficioFasCumulato;
	}

	public String getCodLuogoUfficioFasCumulato() {
		return mCodLuogoUfficioFasCumulato;
	}

	public String getDescrLuogoUfficioFasCumulato() {
		return mDescrLuogoUfficioFasCumulato;
	}

	public String getCodUfficioFasCumulato() {
		return mCodUfficioFasCumulato;
	}

	public String getDescrUfficioFasCumulato() {
		return mDescrUfficioFasCumulato;
	}

	public BigDecimal getKeyProvvNsc() {
		return mKeyProvvNsc;
	}

	public Date getDataRichiestaFascicolo() {
		return mDataRichiestaFascicolo;
	}

	public Date getDataPervenimentoFascicolo() {
		return mDataPervenimentoFascicolo;
	}

	public String getNote() {
		return mNote;
	}

	public BigDecimal getIdFascicoloSiepOrigine() {
		return mIdFascicoloSiepOrigine;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public String getFlagAccorpato() {
		return mFlagAccorpato;
	}

	public String getChiaveUfficioOrigine() {
		return mChiaveUfficioOrigine;
	}

	public BigDecimal getChiaveProgrOrigine() {
		return mChiaveProgrOrigine;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getMotivoModifica() {
		return mMotivoModifica;
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

	public UfficioModel getUfficioOrigine() {
		return mUfficioOrigine;
	}

	public String getStringaProcedimento() {
		return mStringaProcedimento;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdProcedimentoCumulato(BigDecimal aValore) {
		mIdProcedimentoCumulato = aValore;
	}

	public void setChiaveAnnoFasCumulato(BigDecimal aValore) {
		mChiaveAnnoFasCumulato = aValore;
	}

	public void setChiaveProgrFasCumulato(BigDecimal aValore) {
		mChiaveProgrFasCumulato = aValore;
	}

	public void setCodTipoUfficioFasCumulato(String aValore) {
		mCodTipoUfficioFasCumulato = aValore;
	}

	public void setDescrTipoUfficioFasCumulato(String aValore) {
		mDescrTipoUfficioFasCumulato = aValore;
	}

	public void setCodLuogoUfficioFasCumulato(String aValore) {
		mCodLuogoUfficioFasCumulato = aValore;
	}

	public void setDescrLuogoUfficioFasCumulato(String aValore) {
		mDescrLuogoUfficioFasCumulato = aValore;
	}

	public void setCodUfficioFasCumulato(String aValore) {
		mCodUfficioFasCumulato = aValore;
	}

	public void setDescrUfficioFasCumulato(String aValore) {
		mDescrUfficioFasCumulato = aValore;
	}

	public void setKeyProvvNsc(BigDecimal aValore) {
		mKeyProvvNsc = aValore;
	}

	public void setDataRichiestaFascicolo(Date aValore) {
		mDataRichiestaFascicolo = aValore;
	}

	public void setDataPervenimentoFascicolo(Date aValore) {
		mDataPervenimentoFascicolo = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setIdFascicoloSiepOrigine(BigDecimal aValore) {
		mIdFascicoloSiepOrigine = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setFlagAccorpato(String aValore) {
		mFlagAccorpato = aValore;
	}

	public void setChiaveUfficioOrigine(String aValore) {
		mChiaveUfficioOrigine = aValore;
	}

	public void setChiaveProgrOrigine(BigDecimal aValore) {
		mChiaveProgrOrigine = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setMotivoModifica(String aValore) {
		mMotivoModifica = aValore;
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

	public void setUfficioOrigine(UfficioModel aValore) {
		mUfficioOrigine = aValore;
	}

	public void setStringaProcedimento(String aValore) {
		mStringaProcedimento = aValore;
	}

	public String calcolaStringaProcedimento() {
		String lStrProc = "";

		if ("S".equals(mFlagAccorpato) && mUfficioOrigine != null) {
			lStrProc += "N. SIEP " + mChiaveProgrOrigine + "/" + mChiaveAnnoFasCumulato;
			lStrProc += " ex " + mUfficioOrigine.getDescrTipoUfficio() + " di "
					+ mUfficioOrigine.getDescrComune();
			lStrProc += " in esecuzione presso " + mDescrTipoUfficioFasCumulato + " di "
					+ mDescrLuogoUfficioFasCumulato;
		} else {
			lStrProc += "N. SIEP " + mChiaveProgrFasCumulato + "/" + mChiaveAnnoFasCumulato;
			lStrProc += " di " + mDescrTipoUfficioFasCumulato + " di " + mDescrLuogoUfficioFasCumulato;
		}
		mStringaProcedimento = lStrProc;

		return mStringaProcedimento;
	}

	public BigDecimal getIncrement() {
		BigDecimal lIncrement = null;

		if (mChiaveProgrFasCumulato != null && mChiaveProgrOrigine != null) {
			lIncrement = mChiaveProgrFasCumulato.subtract(mChiaveProgrOrigine);
		}

		return lIncrement;

	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "ProcedimentoCumulatoModel:\n" + "[ mIdProcedimentoCumulato     = " + mIdProcedimentoCumulato
				+ " ]\n" + "[ mChiaveAnnoFasCumulato      = " + mChiaveAnnoFasCumulato + " ]\n"
				+ "[ mChiaveProgrFasCumulato     = " + mChiaveProgrFasCumulato + " ]\n"
				+ "[ mCodTipoUfficioFasCumulato  = " + mCodTipoUfficioFasCumulato + " ]\n"
				+ "[ mCodLuogoUfficioFasCumulato = " + mCodLuogoUfficioFasCumulato + " ]\n"
				+ "[ mCodUfficioFasCumulato      = " + mCodUfficioFasCumulato + " ]\n"
				+ "[ mKeyProvvNsc                = " + mKeyProvvNsc + " ]\n" +

				"[ mDataRichiestaFascicolo     = " + mDataRichiestaFascicolo + " ]\n"
				+ "[ mDataPervenimentoFascicolo  = " + mDataPervenimentoFascicolo + " ]\n"
				+ "[ mNote                       = " + mNote + " ]\n" + "[ mIdFascicoloSiepOrigine     = "
				+ mIdFascicoloSiepOrigine + " ]\n" + "[ mEveIdEvento     			  = " + mEveIdEvento
				+ " ]\n" +

				"[ mFlagAccorpato              = " + mFlagAccorpato + " ]\n"
				+ "[ mChiaveUfficioOrigine       = " + mChiaveUfficioOrigine + " ]\n"
				+ "[ mChiaveProgrOrigine         = " + mChiaveProgrOrigine + " ]\n" +

				"[ mTitIdTitoloCumulato        = " + mTitIdTitoloCumulato + " ]\n"
				+ "[ mFlagStato                  = " + mFlagStato + " ]\n"
				+ "[ mMotivoModifica             = " + mMotivoModifica + " ]\n" +

				"[ mCodOperatoreInserimento    = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento            = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento      = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento  = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento          = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento    = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}
}
