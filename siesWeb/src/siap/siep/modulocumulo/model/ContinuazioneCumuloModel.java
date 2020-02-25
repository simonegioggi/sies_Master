package siap.siep.modulocumulo.model;

/**
* <p>Title: ContinuazioneCumuloModel</p>
* <p>Description: Classe Model che rappresenta il Continuazione</p>
* <p>   in ambito Cumulo (Pena_complessiva_/SanzSost /Continuaz Cumulo) </p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.siep.continuazione.model.ContinuazioneModel;
import f3b.model.GenericModel;

public class ContinuazioneCumuloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5743615014674469852L;

	private BigDecimal mIdContinuazioneCum;
	private BigDecimal mProgrContinuazione;
	private String mCodTipoContinuazione;
	private String mDescrTipoContinuazione;

	private String mCodTipoAutorita;
	private String mDescrTipoAutorita;
	private String mCodLuogoAutorita;
	private String mDescrLuogoAutorita;

	private Date mDataSentenza;
	private BigDecimal mAnnoSentenza;
	private String mNumSentenza;

	private BigDecimal mAnnoRegePm;
	private String mNumRegePm;

	private BigDecimal mAnnoRegGen;
	private String mNumeroRegGen;
	private String mTipoRegGen;

	private BigDecimal mPcIdPenaComplessivaCum;
	private BigDecimal mTitIdTitoloCumulatoCont;

	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mTitIdTitoloCumulato;
	private BigDecimal mIdContinuazioneOrigine;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	// COSTRUTTORE DI DEFAULT
	public ContinuazioneCumuloModel() {
		this.mIdContinuazioneCum = null;
		this.mProgrContinuazione = null;
		this.mCodTipoContinuazione = "";
		this.mDescrTipoContinuazione = "";
		this.mCodTipoAutorita = "";
		this.mDescrTipoAutorita = "";
		this.mCodLuogoAutorita = "";
		this.mDescrLuogoAutorita = "";
		this.mDataSentenza = null;
		this.mAnnoSentenza = null;
		this.mNumSentenza = "";

		this.mAnnoRegePm = null;
		this.mNumRegePm = "";

		this.mAnnoRegGen = null;
		this.mNumeroRegGen = "";
		this.mTipoRegGen = "";

		this.mPcIdPenaComplessivaCum = null;
		this.mTitIdTitoloCumulatoCont = null;

		this.mFlagStato = "";
		this.mMotivoModifica = "";
		this.mTitIdTitoloCumulato = null;
		this.mIdContinuazioneOrigine = null;

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
	public ContinuazioneCumuloModel(ContinuazioneCumuloModel aModel) {
		this.mIdContinuazioneCum = aModel.mIdContinuazioneCum;
		this.mProgrContinuazione = aModel.mProgrContinuazione;
		this.mCodTipoContinuazione = aModel.mCodTipoContinuazione;
		this.mDescrTipoContinuazione = aModel.mDescrTipoContinuazione;
		this.mCodTipoAutorita = aModel.mCodTipoAutorita;
		this.mDescrTipoAutorita = aModel.mDescrTipoAutorita;
		this.mCodLuogoAutorita = aModel.mCodLuogoAutorita;
		this.mDescrLuogoAutorita = aModel.mDescrLuogoAutorita;
		this.mDataSentenza = aModel.mDataSentenza;
		this.mAnnoSentenza = aModel.mAnnoSentenza;
		this.mNumSentenza = aModel.mNumSentenza;

		this.mAnnoRegePm = aModel.mAnnoRegePm;
		this.mNumRegePm = aModel.mNumRegePm;

		this.mAnnoRegGen = aModel.mAnnoRegGen;
		this.mNumeroRegGen = aModel.mNumeroRegGen;
		this.mTipoRegGen = aModel.mTipoRegGen;

		this.mPcIdPenaComplessivaCum = aModel.mPcIdPenaComplessivaCum;
		this.mTitIdTitoloCumulatoCont = aModel.mTitIdTitoloCumulato;

		this.mFlagStato = aModel.mFlagStato;
		this.mMotivoModifica = aModel.mMotivoModifica;
		this.mIdContinuazioneOrigine = aModel.mIdContinuazioneOrigine;
		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
	}

	// COSTRUTTORE MODEL
	public ContinuazioneCumuloModel(BigDecimal aIdContinuazioneCum, BigDecimal aProgrContinuazione,
			String aCodTipoContinuazione, String aDescrTipoContinuazione, String aCodTipoAutorita,
			String aDescrTipoAutorita, String aCodLuogoAutorita, String aDescrLuogoAutorita,
			Date aDataSentenza, BigDecimal aAnnoSentenza, String aNumSentenza, BigDecimal aAnnoRegePm,
			String aNumRegePm,

			BigDecimal aAnnoRegGen, String aNumeroRegGen, String aTipoRegGen,

			BigDecimal aPcIdPenaComplessivaCum, BigDecimal aTitIdTitoloCumulatoCont,

			String aFlagStato, String aMotivoModifica, BigDecimal aTitIdTitoloCumulato,
			BigDecimal aIdContinuazioneOrigine,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdContinuazioneCum = aIdContinuazioneCum;
		this.mProgrContinuazione = aProgrContinuazione;
		this.mCodTipoContinuazione = aCodTipoContinuazione;
		this.mDescrTipoContinuazione = aDescrTipoContinuazione;
		this.mCodTipoAutorita = aCodTipoAutorita;
		this.mDescrTipoAutorita = aDescrTipoAutorita;
		this.mCodLuogoAutorita = aCodLuogoAutorita;
		this.mDescrLuogoAutorita = aDescrLuogoAutorita;
		this.mDataSentenza = aDataSentenza;
		this.mAnnoSentenza = aAnnoSentenza;
		this.mNumSentenza = aNumSentenza;

		this.mAnnoRegePm = aAnnoRegePm;
		this.mNumRegePm = aNumRegePm;

		this.mAnnoRegGen = aAnnoRegGen;
		this.mNumeroRegGen = aNumeroRegGen;
		this.mTipoRegGen = aTipoRegGen;

		this.mPcIdPenaComplessivaCum = aPcIdPenaComplessivaCum;
		this.mTitIdTitoloCumulatoCont = aTitIdTitoloCumulatoCont;

		this.mFlagStato = aFlagStato;
		this.mMotivoModifica = aMotivoModifica;
		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mIdContinuazioneOrigine = aIdContinuazioneOrigine;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	/**
	 * Metodo che rimappa un oggetto ContinuazioneModel su un oggetto ContinuazioneCumuloModel per il
	 * caricamento dati in istruttoria
	 * 
	 * @param aContModel
	 */
	public ContinuazioneCumuloModel(ContinuazioneModel aContModel) {
		this.mProgrContinuazione = aContModel.getProgrContinuazione();
		this.mCodTipoContinuazione = aContModel.getCodTipoContinuazione();
		this.mDescrTipoContinuazione = aContModel.getDescrTipoContinuazione();
		this.mCodTipoAutorita = aContModel.getCodTipoAutorita();
		this.mDescrTipoAutorita = aContModel.getDescrTipoAutorita();
		this.mCodLuogoAutorita = aContModel.getCodLuogoAutorita();
		this.mDescrLuogoAutorita = aContModel.getDescrLuogoAutorita();

		this.mDataSentenza = aContModel.getDataSentenza();
		this.mAnnoSentenza = aContModel.getAnnoSentenza();
		this.mNumSentenza = aContModel.getNumSentenza();

		this.mAnnoRegePm = aContModel.getAnnoRegePm();
		this.mNumRegePm = aContModel.getNumRegePm();

		this.mTipoRegGen = "-";

		// Anno/Numero e tipo Registro Generale
		if (aContModel.getAnnoRegeCap() != null) {
			this.mAnnoRegGen = aContModel.getAnnoRegeCap();
			this.mNumeroRegGen = aContModel.getNumRegeCap();
			this.mTipoRegGen = "CAP";
		} else if (aContModel.getAnnoRegeCas() != null) {
			this.mAnnoRegGen = aContModel.getAnnoRegeCas();
			this.mNumeroRegGen = aContModel.getNumRegeCas();
			this.mTipoRegGen = "CAS";
		} else if (aContModel.getAnnoRegeDib() != null) {
			this.mAnnoRegGen = aContModel.getAnnoRegeDib();
			this.mNumeroRegGen = aContModel.getNumRegeDib();
			this.mTipoRegGen = "DIB";
		} else if (aContModel.getAnnoRegeCasap() != null) {
			this.mAnnoRegGen = aContModel.getAnnoRegeCasap();
			this.mNumeroRegGen = aContModel.getNumRegeCasap();
			this.mTipoRegGen = "CASAP";
		} else if (aContModel.getAnnoRegeGip() != null) {
			this.mAnnoRegGen = aContModel.getAnnoRegeGip();
			this.mNumeroRegGen = aContModel.getNumRegeGip();
			this.mTipoRegGen = "GIP";
		}

		this.mIdContinuazioneOrigine = aContModel.getIdContinuazione();

	}

	/**
	 * Confronta i dati della continuazione con quelli del Titolo in input e restituisce true se i titoli
	 * 'coincidono' n.b. il confronto viene fatto solo se i dati delle due entità sono 'completi'
	 * 
	 * @param aTitoloModel
	 * @return
	 */
	public boolean isStessoTitolo(TitoloCumulatoModel aTitoloModel) {
		boolean isStessoTitolo = true;

		if (this.mCodTipoAutorita == null || this.mCodTipoAutorita.equals("")
				|| this.mCodLuogoAutorita == null || this.mCodLuogoAutorita.equals("")
				|| this.mDataSentenza == null || this.mAnnoSentenza == null || this.mNumSentenza == null
				|| this.mNumSentenza.equals(""))
			return false;

		if (aTitoloModel.getCodTipoAutoritaEmittente() == null
				|| aTitoloModel.getCodTipoAutoritaEmittente().equals("")
				|| aTitoloModel.getCodLuogoEmittente() == null
				|| aTitoloModel.getCodLuogoEmittente().equals("")
				|| aTitoloModel.getDataProvvedimento() == null || aTitoloModel.getAnnoSentenza() == null
				|| aTitoloModel.getNumeroSentenza() == null || aTitoloModel.getNumeroSentenza().equals(""))
			return false;

		if (!this.mCodTipoAutorita.equals(aTitoloModel.getCodTipoAutoritaEmittente()))
			return false;

		if (!this.mCodLuogoAutorita.equals(aTitoloModel.getCodLuogoEmittente()))
			return false;

		// FIXME CUMULO verificare di che date si tratta e quindi se confrontabili
		// if (!this.mDataSentenza.equals(aTitoloModel.getDataProvvedimento()))
		// return false;

		if (this.mAnnoSentenza.compareTo(aTitoloModel.getAnnoSentenza()) != 0)
			return false;

		if (!this.mNumSentenza.equals(aTitoloModel.getNumeroSentenza()))
			return false;

		// Gli altri campi (RGNR) e (Reg. Gen.) non sono obbligatori. Per ora non si controllano

		// this.mAnnoRegePm = aTitoloModel.getAnnoRegePm();
		// this.mNumRegePm = aTitoloModel.getNumeroRegePm();
		//
		// this.mAnnoRegGen = aTitoloModel.getAnnoRegGen();
		// this.mNumeroRegGen = aTitoloModel.getNumeroRegGen();
		// this.mTipoRegGen = aTitoloModel.getTipoRegGen();

		return isStessoTitolo;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdContinuazioneCum() {
		return mIdContinuazioneCum;
	}

	public BigDecimal getProgrContinuazione() {
		return mProgrContinuazione;
	}

	public String getCodTipoContinuazione() {
		return mCodTipoContinuazione;
	}

	public String getDescrTipoContinuazione() {
		return mDescrTipoContinuazione;
	}

	public String getCodTipoAutorita() {
		return mCodTipoAutorita;
	}

	public String getDescrTipoAutorita() {
		return mDescrTipoAutorita;
	}

	public String getCodLuogoAutorita() {
		return mCodLuogoAutorita;
	}

	public String getDescrLuogoAutorita() {
		return mDescrLuogoAutorita;
	}

	public Date getDataSentenza() {
		return mDataSentenza;
	}

	public BigDecimal getAnnoSentenza() {
		return mAnnoSentenza;
	}

	public String getNumSentenza() {
		return mNumSentenza;
	}

	public BigDecimal getAnnoRegePm() {
		return mAnnoRegePm;
	}

	public String getNumRegePm() {
		return mNumRegePm;
	}

	public BigDecimal getAnnoRegGen() {
		return mAnnoRegGen;
	}

	public String getNumeroRegGen() {
		return mNumeroRegGen;
	}

	public String getTipoRegGen() {
		return mTipoRegGen;
	}

	public BigDecimal getPcIdPenaComplessivaCum() {
		return mPcIdPenaComplessivaCum;
	}

	public BigDecimal getTitIdTitoloCumulatoCont() {
		return mTitIdTitoloCumulatoCont;
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

	public BigDecimal getIdContinuazioneOrigine() {
		return mIdContinuazioneOrigine;
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

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdContinuazioneCum(BigDecimal aValore) {
		mIdContinuazioneCum = aValore;
	}

	public void setProgrContinuazione(BigDecimal aValore) {
		mProgrContinuazione = aValore;
	}

	public void setCodTipoContinuazione(String aValore) {
		mCodTipoContinuazione = aValore;
	}

	public void setDescrTipoContinuazione(String aValore) {
		mDescrTipoContinuazione = aValore;
	}

	public void setCodTipoAutorita(String aValore) {
		mCodTipoAutorita = aValore;
	}

	public void setDescrTipoAutorita(String aValore) {
		mDescrTipoAutorita = aValore;
	}

	public void setCodLuogoAutorita(String aValore) {
		mCodLuogoAutorita = aValore;
	}

	public void setDescrLuogoAutorita(String aValore) {
		mDescrLuogoAutorita = aValore;
	}

	public void setDataSentenza(Date aValore) {
		mDataSentenza = aValore;
	}

	public void setAnnoSentenza(BigDecimal aValore) {
		mAnnoSentenza = aValore;
	}

	public void setNumSentenza(String aValore) {
		mNumSentenza = aValore;
	}

	public void setAnnoRegePm(BigDecimal aValore) {
		mAnnoRegePm = aValore;
	}

	public void setNumRegePm(String aValore) {
		mNumRegePm = aValore;
	}

	public void setAnnoRegGen(BigDecimal aValore) {
		mAnnoRegGen = aValore;
	}

	public void setNumeroRegGen(String aValore) {
		mNumeroRegGen = aValore;
	}

	public void setTipoRegGen(String aValore) {
		mTipoRegGen = aValore;
	}

	public void setPcIdPenaComplessivaCum(BigDecimal aValore) {
		mPcIdPenaComplessivaCum = aValore;
	}

	public void setTitIdTitoloCumulatoCont(BigDecimal aValore) {
		mTitIdTitoloCumulatoCont = aValore;
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

	public void setIdContinuazioneOrigine(BigDecimal aValore) {
		mIdContinuazioneOrigine = aValore;
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

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		// String lStr = new String();
		/*
		 * lStr = "ContinuazioneCumuloModel:\n" +
		 * "[ mIdContinuazioneCum        = "+mIdContinuazioneCum+" ]\n"+
		 * "[ mProgrContinuazione        = "+mProgrContinuazione+" ]\n"+
		 * "[ mCodTipoContinuazione      = "+mCodTipoContinuazione+" ]\n"+
		 * "[ mCodTipoAutorita           = "+mCodTipoAutorita+" ]\n"+
		 * "[ mCodLuogoAutorita          = "+mCodLuogoAutorita+" ]\n"+
		 * "[ mDataSentenza              = "+mDataSentenza+" ]\n"+
		 * "[ mAnnoSentenza              = "+mAnnoSentenza+" ]\n"+
		 * "[ mNumSentenza               = "+mNumSentenza+" ]\n"+
		 * "[ mAnnoRegePm                = "+mAnnoRegePm+" ]\n"+
		 * "[ mNumRegePm                 = "+mNumRegePm+" ]\n"+
		 * 
		 * 
		 * "[ mPcIdPenaComplessivaCum    = "+mPcIdPenaComplessivaCum+" ]\n"+
		 * "[ mTitIdTitoloCumulatoCont   = "+mTitIdTitoloCumulatoCont+" ]\n"+
		 * "[ mFlagStato                 = "+mFlagStato+" ]\n"+
		 * "[ mMotivoModifica            = "+mMotivoModifica+" ]\n"+
		 * "[ mIdContinuazioneOrigine    = "+mIdContinuazioneOrigine+" ]\n"+
		 * "[ mTitIdTitoloCumulato       = "+mTitIdTitoloCumulato+" ]\n"+
		 * "[ mCodOperatoreInserimento   = "+mCodOperatoreInserimento+" ]\n"+
		 * "[ mDataInserimento           = "+mDataInserimento+" ]\n"+
		 * "[ mCodUfficioInserimento     = "+mCodUfficioInserimento+" ]\n"+
		 * "[ mCodOperatoreAggiornamento = "+mCodOperatoreAggiornamento+" ]\n"+
		 * "[ mDataAggiornamento         = "+mDataAggiornamento+" ]\n"+
		 * "[ mCodUfficioAggiornamento   = "+mCodUfficioAggiornamento+" ]";
		 */
		StringBuffer sb = new StringBuffer("ContinuazioneCumuloModel:\n");
		sb.append("[ mIdContinuazioneCum        = ").append(mIdContinuazioneCum).append(" ]\n");
		sb.append("[ mProgrContinuazione        = ").append(mProgrContinuazione).append(" ]\n");
		sb.append("[ mCodTipoContinuazione      = ").append(mCodTipoContinuazione).append(" ]\n");
		sb.append("[ mCodTipoAutoritasb         = ").append(mCodTipoAutorita).append(" ]\n");
		sb.append("[ mCodLuogoAutorita          = ").append(mCodLuogoAutorita).append(" ]\n");
		sb.append("[ mDataSentenza              = ").append(mDataSentenza).append(" ]\n");
		sb.append("[ mAnnoSentenza              = ").append(mAnnoSentenza).append(" ]\n");
		sb.append("[ mNumSentenza               = ").append(mNumSentenza).append(" ]\n");
		sb.append("[ mAnnoRegePm                = ").append(mAnnoRegePm).append(" ]\n");
		sb.append("[ mNumRegePm                 = ").append(mNumRegePm).append(" ]\n");
		sb.append("[ mAnnoRegGen                = ").append(mAnnoRegGen).append(" ]\n");
		sb.append("[ mNumeroRegGen              = ").append(mNumeroRegGen).append(" ]\n");
		sb.append("[ mTipoRegGen                = ").append(mTipoRegGen).append(" ]\n");
		sb.append("[ mPcIdPenaComplessivaCum    = ").append(mPcIdPenaComplessivaCum).append(" ]\n");
		sb.append("[ mTitIdTitoloCumulatoCont   = ").append(mTitIdTitoloCumulatoCont).append(" ]\n");
		sb.append("[ mFlagStato                 = ").append(mFlagStato).append(" ]\n");
		sb.append("[ mMotivoModifica            = ").append(mMotivoModifica).append(" ]\n");
		sb.append("[ mIdContinuazioneOrigine    = ").append(mIdContinuazioneOrigine).append(" ]\n");
		sb.append("[ mTitIdTitoloCumulato       = ").append(mTitIdTitoloCumulato).append(" ]\n");
		sb.append("[ mCodOperatoreInserimento   = ").append(mCodOperatoreInserimento).append(" ]\n");
		sb.append("[ mDataInserimento           = ").append(mDataInserimento).append(" ]\n");
		sb.append("[ mCodUfficioInserimento     = ").append(mCodUfficioInserimento).append(" ]\n");
		sb.append("[ mCodOperatoreAggiornamento = ").append(mCodOperatoreAggiornamento).append(" ]\n");
		sb.append("[ mDataAggiornamento         = ").append(mDataAggiornamento).append(" ]\n");
		sb.append("[ mCodUfficioAggiornamento   = ").append(mCodUfficioAggiornamento).append(" ]");

		return sb.toString();
	}

}