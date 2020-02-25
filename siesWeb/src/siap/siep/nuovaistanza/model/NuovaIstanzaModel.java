package siap.siep.nuovaistanza.model;

/**
* <p>Title: NuovaIstanzaModel</p>
* <p>Description: Classe Model che rappresenta il NuovaIstanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.siep.avvocato.model.AvvocatoModel;

public class NuovaIstanzaModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -5020220527282472242L;
	private BigDecimal mIdNuovaIstanza;
	private String mCodContenuto;
	private String mDescrContenuto;
	private Date mDataIstanza;
	private String mNote;
	private String mFlagPresdep;
	private String mSoggPresentante;
	private String mSoggPresentanteIdentificato;
	private BigDecimal mAvvIdAvvocatoPresentante;
	private String mDescrAvvocatoPresentante; // 07/03/2011
	private String mCodAutoritaMittente;
	private String mDescrAutoritaMittente;
	private String mCodSedeMittente;
	private String mDescrSedeMittente;
	private String mDescrMittente;
	private BigDecimal mAvvIdAvvocato;
	private String mCodEsito;
	private String mDescrEsito;
	private BigDecimal mAnnoRegistro;
	private BigDecimal mProgrRegistro;
	private String mCodTipoUfficioDestinatario;
	private String mDescrTipoUfficioDestinatario;
	private String mCodLuogoDestinatario;
	private String mDescrLuogoDestinatario;
	private String mCodUfficioDestinatario;
	private String mDescrUfficioDestinatario;
	private String mCodStatoIstanza;
	private String mDescrStatoIstanza;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mOraInserimento; // 02/03/2011
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mEveIdEvento;
	private Date mDataNotificaAvvocato;
	private String mTipoAvvocato;
	private String mDescrTipoAvvocato;

	private Date mDataInoltroPM;

	private AvvocatoModel mAvvocato;
	private AvvocatoModel mAvvocatoPresentante;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public NuovaIstanzaModel() {
		this.mIdNuovaIstanza = null;
		this.mCodContenuto = "";
		this.mDescrContenuto = "";
		this.mDataIstanza = null;
		this.mNote = "";
		this.mFlagPresdep = "";
		this.mSoggPresentante = "";
		this.mSoggPresentanteIdentificato = "";
		this.mAvvIdAvvocatoPresentante = null;
		this.mDescrAvvocatoPresentante = ""; // 07/03/2011
		this.mCodAutoritaMittente = "";
		this.mDescrAutoritaMittente = "";
		this.mCodSedeMittente = "";
		this.mDescrSedeMittente = "";
		this.mDescrMittente = "";
		this.mAvvIdAvvocato = null;
		this.mCodEsito = "";
		this.mDescrEsito = "";
		this.mAnnoRegistro = null;
		this.mProgrRegistro = null;
		this.mCodTipoUfficioDestinatario = "";
		this.mDescrTipoUfficioDestinatario = "";
		this.mCodLuogoDestinatario = "";
		this.mDescrLuogoDestinatario = "";
		this.mCodUfficioDestinatario = "";
		this.mDescrUfficioDestinatario = "";
		this.mCodStatoIstanza = "";
		this.mDescrStatoIstanza = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mOraInserimento = "";
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mEveIdEvento = null;
		this.mDataNotificaAvvocato = null;
		this.mTipoAvvocato = "";
		this.mDataInoltroPM = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public NuovaIstanzaModel(NuovaIstanzaModel aModel) {
		this.mIdNuovaIstanza = aModel.mIdNuovaIstanza;
		this.mCodContenuto = aModel.mCodContenuto;
		this.mDescrContenuto = aModel.mDescrContenuto;
		this.mDataIstanza = aModel.mDataIstanza;
		this.mNote = aModel.mNote;
		this.mFlagPresdep = aModel.mFlagPresdep;
		this.mSoggPresentante = aModel.mSoggPresentante;
		this.mSoggPresentanteIdentificato = aModel.mSoggPresentanteIdentificato;
		this.mAvvIdAvvocatoPresentante = aModel.mAvvIdAvvocatoPresentante;
		this.mDescrAvvocatoPresentante = aModel.mDescrAvvocatoPresentante; // 07/03/2011
		this.mCodAutoritaMittente = aModel.mCodAutoritaMittente;
		this.mDescrAutoritaMittente = aModel.mDescrAutoritaMittente;
		this.mCodSedeMittente = aModel.mCodSedeMittente;
		this.mDescrSedeMittente = aModel.mDescrSedeMittente;
		this.mDescrMittente = aModel.mDescrMittente;
		this.mAvvIdAvvocato = aModel.mAvvIdAvvocato;
		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mAnnoRegistro = aModel.mAnnoRegistro;
		this.mProgrRegistro = aModel.mProgrRegistro;
		this.mCodTipoUfficioDestinatario = aModel.mCodTipoUfficioDestinatario;
		this.mDescrTipoUfficioDestinatario = aModel.mDescrTipoUfficioDestinatario;
		this.mCodLuogoDestinatario = aModel.mCodLuogoDestinatario;
		this.mDescrLuogoDestinatario = aModel.mDescrLuogoDestinatario;
		this.mCodUfficioDestinatario = aModel.mCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aModel.mDescrUfficioDestinatario;
		this.mCodStatoIstanza = aModel.mCodStatoIstanza;
		this.mDescrStatoIstanza = aModel.mDescrStatoIstanza;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mOraInserimento = aModel.mOraInserimento; // 02/03/2011
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mDataNotificaAvvocato = aModel.mDataNotificaAvvocato;
		this.mTipoAvvocato = aModel.mTipoAvvocato;
		this.mDataInoltroPM = aModel.mDataInoltroPM;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public NuovaIstanzaModel(BigDecimal aIdNuovaIstanza, String aCodContenuto, String aDescrContenuto,
			Date aDataIstanza, String aNote, String aFlagPresdep, String aSoggPresentante,
			String aSoggPresentanteIdentificato, BigDecimal aAvvIdAvvocatoPresentante,
			String aDescrAvvocatoPresentante, // 07/03/2011
			String aCodAutoritaMittente, String aDescrAutoritaMittente, String aCodSedeMittente,
			String aDescrSedeMittente, String aDescrMittente, BigDecimal aAvvIdAvvocato, String aCodEsito,
			String aDescrEsito, BigDecimal aAnnoRegistro, BigDecimal aProgrRegistro,
			String aCodTipoUfficioDestinatario, String aDescrTipoUfficioDestinatario,
			String aCodLuogoDestinatario, String aDescrLuogoDestinatario, String aCodUfficioDestinatario,
			String aDescrUfficioDestinatario, String aCodStatoIstanza, String aDescrStatoIstanza,
			String aCodOperatoreInserimento, Date aDataInserimento, String aOraInserimento, // 02/03/2011
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aEveIdEvento, Date aDataNotificaAvvocato,
			String aTipoAvvocato, Date aDataInoltroPM) {
		this.mIdNuovaIstanza = aIdNuovaIstanza;
		this.mCodContenuto = aCodContenuto;
		this.mDescrContenuto = aDescrContenuto;
		this.mDataIstanza = aDataIstanza;
		this.mNote = aNote;
		this.mFlagPresdep = aFlagPresdep;
		this.mSoggPresentante = aSoggPresentante;
		this.mSoggPresentanteIdentificato = aSoggPresentanteIdentificato;
		this.mAvvIdAvvocatoPresentante = aAvvIdAvvocatoPresentante;
		this.mDescrAvvocatoPresentante = aDescrAvvocatoPresentante; // 07/03/2011
		this.mCodAutoritaMittente = aCodAutoritaMittente;
		this.mDescrAutoritaMittente = aDescrAutoritaMittente;
		this.mCodSedeMittente = aCodSedeMittente;
		this.mDescrSedeMittente = aDescrSedeMittente;
		this.mDescrMittente = aDescrMittente;
		this.mAvvIdAvvocato = aAvvIdAvvocato;
		this.mCodEsito = aCodEsito;
		this.mDescrEsito = aDescrEsito;
		this.mAnnoRegistro = aAnnoRegistro;
		this.mProgrRegistro = aProgrRegistro;
		this.mCodTipoUfficioDestinatario = aCodTipoUfficioDestinatario;
		this.mDescrTipoUfficioDestinatario = aDescrTipoUfficioDestinatario;
		this.mCodLuogoDestinatario = aCodLuogoDestinatario;
		this.mDescrLuogoDestinatario = aDescrLuogoDestinatario;
		this.mCodUfficioDestinatario = aCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aDescrUfficioDestinatario;
		this.mCodStatoIstanza = aCodStatoIstanza;
		this.mDescrStatoIstanza = aDescrStatoIstanza;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mOraInserimento = aOraInserimento; // 02/03/2011
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mEveIdEvento = aEveIdEvento;
		this.mDataNotificaAvvocato = aDataNotificaAvvocato;
		this.mTipoAvvocato = aTipoAvvocato;
		this.mDataInoltroPM = aDataInoltroPM;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdNuovaIstanza() {
		return mIdNuovaIstanza;
	}

	public String getCodContenuto() {
		return mCodContenuto;
	}

	public String getDescrContenuto() {
		return mDescrContenuto;
	}

	public Date getDataIstanza() {
		return mDataIstanza;
	}

	public String getNote() {
		return mNote;
	}

	public String getFlagPresdep() {
		return mFlagPresdep;
	}

	public String getSoggPresentante() {
		return mSoggPresentante;
	}

	public String getSoggPresentanteIdentificato() {
		return mSoggPresentanteIdentificato;
	}

	public BigDecimal getAvvIdAvvocatoPresentante() {
		return mAvvIdAvvocatoPresentante;
	}

	public String getDescrAvvocatoPresentante() {
		return mDescrAvvocatoPresentante;
	}

	public String getCodAutoritaMittente() {
		return mCodAutoritaMittente;
	}

	public String getDescrAutoritaMittente() {
		return mDescrAutoritaMittente;
	}

	public String getCodSedeMittente() {
		return mCodSedeMittente;
	}

	public String getDescrSedeMittente() {
		return mDescrSedeMittente;
	}

	public String getDescrMittente() {
		return mDescrMittente;
	}

	public BigDecimal getAvvIdAvvocato() {
		return mAvvIdAvvocato;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public BigDecimal getAnnoRegistro() {
		return mAnnoRegistro;
	}

	public BigDecimal getProgrRegistro() {
		return mProgrRegistro;
	}

	public String getCodTipoUfficioDestinatario() {
		return mCodTipoUfficioDestinatario;
	}

	public String getDescrTipoUfficioDestinatario() {
		return mDescrTipoUfficioDestinatario;
	}

	public String getCodLuogoDestinatario() {
		return mCodLuogoDestinatario;
	}

	public String getDescrLuogoDestinatario() {
		return mDescrLuogoDestinatario;
	}

	public String getCodUfficioDestinatario() {
		return mCodUfficioDestinatario;
	}

	public String getDescrUfficioDestinatario() {
		return mDescrUfficioDestinatario;
	}

	public String getCodStatoIstanza() {
		return mCodStatoIstanza;
	}

	public String getDescrStatoIstanza() {
		return mDescrStatoIstanza;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getOraInserimento() {
		return mOraInserimento;
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

	public AvvocatoModel getAvvocato() {
		return mAvvocato;
	}

	public AvvocatoModel getAvvocatoPresentante() {
		return mAvvocatoPresentante;
	}

	public Date getDataNotificaAvvocato() {
		return mDataNotificaAvvocato;
	}

	public String getTipoAvvocato() {
		return mTipoAvvocato;
	}

	public String getDescrTipoAvvocato() {
		return mDescrTipoAvvocato;
	}

	public Date getDataInoltroPM() {
		return mDataInoltroPM;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdNuovaIstanza(BigDecimal aValore) {
		mIdNuovaIstanza = aValore;
	}

	public void setCodContenuto(String aValore) {
		mCodContenuto = aValore;
	}

	public void setDescrContenuto(String aValore) {
		mDescrContenuto = aValore;
	}

	public void setDataIstanza(Date aValore) {
		mDataIstanza = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setFlagPresdep(String aValore) {
		mFlagPresdep = aValore;
	}

	public void setSoggPresentante(String aValore) {
		mSoggPresentante = aValore;
	}

	public void setSoggPresentanteIdentificato(String aValore) {
		mSoggPresentanteIdentificato = aValore;
	}

	public void setAvvIdAvvocatoPresentante(BigDecimal aValore) {
		mAvvIdAvvocatoPresentante = aValore;
	}

	public void setDescrAvvocatoPresentante(String aValore) {
		mDescrAvvocatoPresentante = aValore;
	}

	public void setCodAutoritaMittente(String aValore) {
		mCodAutoritaMittente = aValore;
	}

	public void setDescrAutoritaMittente(String aValore) {
		mDescrAutoritaMittente = aValore;
	}

	public void setCodSedeMittente(String aValore) {
		mCodSedeMittente = aValore;
	}

	public void setDescrSedeMittente(String aValore) {
		mDescrSedeMittente = aValore;
	}

	public void setDescrMittente(String aValore) {
		mDescrMittente = aValore;
	}

	public void setAvvIdAvvocato(BigDecimal aValore) {
		mAvvIdAvvocato = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setAnnoRegistro(BigDecimal aValore) {
		mAnnoRegistro = aValore;
	}

	public void setProgrRegistro(BigDecimal aValore) {
		mProgrRegistro = aValore;
	}

	public void setCodTipoUfficioDestinatario(String aValore) {
		mCodTipoUfficioDestinatario = aValore;
	}

	public void setDescrTipoUfficioDestinatario(String aValore) {
		mDescrTipoUfficioDestinatario = aValore;
	}

	public void setCodLuogoDestinatario(String aValore) {
		mCodLuogoDestinatario = aValore;
	}

	public void setDescrLuogoDestinatario(String aValore) {
		mDescrLuogoDestinatario = aValore;
	}

	public void setCodUfficioDestinatario(String aValore) {
		mCodUfficioDestinatario = aValore;
	}

	public void setDescrUfficioDestinatario(String aValore) {
		mDescrUfficioDestinatario = aValore;
	}

	public void setCodStatoIstanza(String aValore) {
		mCodStatoIstanza = aValore;
	}

	public void setDescrStatoIstanza(String aValore) {
		mDescrStatoIstanza = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setOraInserimento(String aValore) {
		mOraInserimento = aValore;
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

	public void setAvvocato(AvvocatoModel mAvvocato) {
		this.mAvvocato = mAvvocato;
	}

	public void setAvvocatoPresentante(AvvocatoModel mAvvocatoPresentante) {
		this.mAvvocatoPresentante = mAvvocatoPresentante;
	}

	public void setDataNotificaAvvocato(Date mDataNotificaAvvocato) {
		this.mDataNotificaAvvocato = mDataNotificaAvvocato;
	}

	public void setTipoAvvocato(String mTipoAvvocato) {
		this.mTipoAvvocato = mTipoAvvocato;
	}

	public void setDescrTipoAvvocato(String mDescrTipoAvvocato) {
		this.mDescrTipoAvvocato = mDescrTipoAvvocato;
	}

	public void setDataInoltroPM(Date mDataInoltroPM) {
		this.mDataInoltroPM = mDataInoltroPM;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "NuovaIstanzaModel:\n" + "[ mIdNuovaIstanza              = " + mIdNuovaIstanza + " ]\n"
				+ "[ mCodContenuto                = " + mCodContenuto + " ]\n"
				+ "[ mDataIstanza                 = " + mDataIstanza + " ]\n"
				+ "[ mNote                        = " + mNote + " ]\n" + "[ mFlagPresdep                 = "
				+ mFlagPresdep + " ]\n" + "[ mSoggPresentante             = " + mSoggPresentante + " ]\n"
				+ "[ mSoggPresentanteIdentificato = " + mSoggPresentanteIdentificato + " ]\n"
				+ "[ mAvvIdAvvocatoPresentante    = " + mAvvIdAvvocatoPresentante + " ]\n"
				+ "[ mDescrAvvocatoPresentante    = " + mDescrAvvocatoPresentante + " ]\n"
				+ "[ mCodAutoritaMittente         = " + mCodAutoritaMittente + " ]\n"
				+ "[ mCodSedeMittente             = " + mCodSedeMittente + " ]\n"
				+ "[ mDescrMittente            	 = " + mDescrMittente + " ]\n"
				+ "[ mAvvIdAvvocato               = " + mAvvIdAvvocato + " ]\n"
				+ "[ mCodEsito                    = " + mCodEsito + " ]\n"
				+ "[ mAnnoRegistro                = " + mAnnoRegistro + " ]\n"
				+ "[ mProgrRegistro               = " + mProgrRegistro + " ]\n"
				+ "[ mCodTipoUfficioDestinatario  = " + mCodTipoUfficioDestinatario + " ]\n"
				+ "[ mCodLuogoDestinatario        = " + mCodLuogoDestinatario + " ]\n"
				+ "[ mCodUfficioDestinatario      = " + mCodUfficioDestinatario + " ]\n"
				+ "[ mCodStatoIstanza             = " + mCodStatoIstanza + " ]\n"
				+ "[ mCodOperatoreInserimento     = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento             = " + mDataInserimento + " ]\n"
				+ "[ mOraInserimento              = " + mOraInserimento + " ]\n"
				+ "[ mCodUfficioInserimento       = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento   = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento           = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento     = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mFasSieIdFascicoloSiep       = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mEveIdEvento                 = " + mEveIdEvento + " ]"
				+ "[ mDataNotificaAvvocato        = " + mDataNotificaAvvocato + " ]"
				+ "[ mTipoAvvocato                = " + mTipoAvvocato + " ]"
				+ "[ mDataInoltroPM		       = " + mDataInoltroPM + " ]";

		return lStr;
	}
}
