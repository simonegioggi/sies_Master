package siap.siep.pagoPA.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.siep.notifica.model.NotificaModel;

/**
 * MEV_2023-13: aggiunta classe model per pagoPA
 *
 * @author sgioggi
 * @version 1.0
 */
public class CivilmenteObbligatoDifensoreModel extends GenericModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2820081491232852270L;

	private BigDecimal mIdAvvocatoCivilmenteObbligato;
	private String mCodTipoAvvocato;
	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private String mCodMotivoDesignazione;
	private String mCodTipoAutorita;
	private String mSedeTipoAutorita;
	private String mIndirizzoTipoAutorita;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private BigDecimal mAvvIdAvvocato;
	private BigDecimal mSoggIdSoggetto;
	private String mNote;
	private String mCodTipoAutoritaDif;
	private String mSedeAutoritaDif;
	private String mIstDetIdIstitutoDetenzione;
	private String mDescrMotivoDesignazione;
	private String mDescrTipoAutorita;
	private String mDescrTipoAutoritaDif;
	private String mComuneTipoAutorita;
	private String mComuneTipoAutoritaDif;
	private AvvocatoModel mAvvocato;
	private NotificaModel mNotifica;

	// parametro utilizzato per gestire il flag SNT (Sistema Notifiche Telematiche)
	private boolean mFlagSNT = false;

	// COSTRUTTORE DI DEFAULT
	public CivilmenteObbligatoDifensoreModel() {

		this.mIdAvvocatoCivilmenteObbligato = null;
		this.mCodTipoAvvocato = "";
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mCodMotivoDesignazione = "";
		this.mCodTipoAutorita = "";
		this.mSedeTipoAutorita = "";
		this.mIndirizzoTipoAutorita = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mAvvIdAvvocato = null;
		this.mSoggIdSoggetto = null;
		this.mIstDetIdIstitutoDetenzione = "";
		this.mNote = "";
		this.mCodTipoAutoritaDif = "";
		this.mSedeAutoritaDif = "";
		this.mDescrMotivoDesignazione = "";
		this.mDescrTipoAutorita = "";
		this.mDescrTipoAutoritaDif = "";
		this.mComuneTipoAutorita = "";
		this.mComuneTipoAutoritaDif = "";
		this.mAvvocato = null;
		this.mNotifica = null;
		this.mFlagSNT = false;
	}

	// COSTRUTTORE DI COPIA
	public CivilmenteObbligatoDifensoreModel(CivilmenteObbligatoDifensoreModel aModel) {

		this.mIdAvvocatoCivilmenteObbligato = aModel.mIdAvvocatoCivilmenteObbligato;
		this.mCodTipoAvvocato = aModel.mCodTipoAvvocato;
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mCodMotivoDesignazione = aModel.mCodMotivoDesignazione;
		this.mCodTipoAutorita = aModel.mCodTipoAutorita;
		this.mSedeTipoAutorita = aModel.mSedeTipoAutorita;
		this.mIndirizzoTipoAutorita = aModel.mIndirizzoTipoAutorita;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mAvvIdAvvocato = aModel.mAvvIdAvvocato;
		this.mSoggIdSoggetto = aModel.mSoggIdSoggetto;
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mNote = aModel.mNote;
		this.mCodTipoAutoritaDif = aModel.mCodTipoAutoritaDif;
		this.mSedeAutoritaDif = aModel.mSedeAutoritaDif;
		this.mDescrMotivoDesignazione = aModel.mDescrMotivoDesignazione;
		this.mDescrTipoAutorita = aModel.mDescrTipoAutorita;
		this.mDescrTipoAutoritaDif = aModel.mDescrTipoAutoritaDif;
		this.mComuneTipoAutorita = aModel.mComuneTipoAutorita;
		this.mComuneTipoAutoritaDif = aModel.mComuneTipoAutoritaDif;
		this.mAvvocato = aModel.mAvvocato;
		this.mNotifica = aModel.mNotifica;
		this.mFlagSNT = aModel.mFlagSNT;
	}

	// COSTRUTTORE MODEL
	public CivilmenteObbligatoDifensoreModel(BigDecimal aIdAvvocatoCivilmenteObbligato,
			String aCodTipoAvvocato, Date aDataInizioValidita, Date aDataFineValidita,
			String aCodMotivoDesignazione, String aCodTipoAutorita, String aSedeTipoAutorita,
			String aIndirizzoTipoAutorita, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, BigDecimal aAvvIdAvvocato, BigDecimal aSoggIdSoggetto,
			String aIstDetIdIstitutoDetenzione, String aNote, String aCodTipoAutoritaDif,
			String aSedeAutoritaDif, String aDescrMotivoDesignazione, String aDescrTipoAutorita,
			String aDescrTipoAutoritaDif, String aComuneTipoAutorita, String aComuneTipoAutoritaDif,
			AvvocatoModel aAvvocato, NotificaModel aNotifica, boolean aFlagSNT) {

		this.mIdAvvocatoCivilmenteObbligato = aIdAvvocatoCivilmenteObbligato;
		this.mCodTipoAvvocato = aCodTipoAvvocato;
		this.mDataInizioValidita = aDataInizioValidita;
		this.mDataFineValidita = aDataFineValidita;
		this.mCodMotivoDesignazione = aCodMotivoDesignazione;
		this.mCodTipoAutorita = aCodTipoAutorita;
		this.mSedeTipoAutorita = aSedeTipoAutorita;
		this.mIndirizzoTipoAutorita = aIndirizzoTipoAutorita;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mAvvIdAvvocato = aAvvIdAvvocato;
		this.mSoggIdSoggetto = aSoggIdSoggetto;
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		this.mNote = aNote;
		this.mCodTipoAutoritaDif = aCodTipoAutoritaDif;
		this.mSedeAutoritaDif = aSedeAutoritaDif;
		this.mDescrMotivoDesignazione = aDescrMotivoDesignazione;
		this.mDescrTipoAutorita = aDescrTipoAutorita;
		this.mDescrTipoAutoritaDif = aDescrTipoAutoritaDif;
		this.mComuneTipoAutorita = aComuneTipoAutorita;
		this.mComuneTipoAutoritaDif = aComuneTipoAutoritaDif;
		this.mAvvocato = aAvvocato;
		this.mNotifica = aNotifica;
		this.mFlagSNT = aFlagSNT;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdAvvocatoCivilmenteObbligato() {
		return mIdAvvocatoCivilmenteObbligato;
	}

	public String getCodTipoAvvocato() {
		return mCodTipoAvvocato;
	}

	public Date getDataInizioValidita() {
		return mDataInizioValidita;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public String getCodMotivoDesignazione() {
		return mCodMotivoDesignazione;
	}

	public String getCodTipoAutorita() {
		return mCodTipoAutorita;
	}

	public String getSedeTipoAutorita() {
		return mSedeTipoAutorita;
	}

	public String getIndirizzoTipoAutorita() {
		return mIndirizzoTipoAutorita;
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

	public BigDecimal getAvvIdAvvocato() {
		return mAvvIdAvvocato;
	}

	public BigDecimal getSoggIdSoggetto() {
		return mSoggIdSoggetto;
	}

	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	public String getNote() {
		return mNote;
	}

	public String getCodTipoAutoritaDif() {
		return mCodTipoAutoritaDif;
	}

	public String getSedeAutoritaDif() {
		return mSedeAutoritaDif;
	}

	public String getDescrMotivoDesignazione() {
		return mDescrMotivoDesignazione;
	}

	public String getDescrTipoAutorita() {
		return mDescrTipoAutorita;
	}

	public String getDescrTipoAutoritaDif() {
		return mDescrTipoAutoritaDif;
	}

	public String getComuneTipoAutorita() {
		return mComuneTipoAutorita;
	}

	public String getComuneTipoAutoritaDif() {
		return mComuneTipoAutoritaDif;
	}

	public String getNomeCognomeAvvocato() {
		return this.mAvvocato.getNome() + " " + this.mAvvocato.getCognome();
	}

	public String getForo() {
		return this.mAvvocato.getForo();
	}

	public AvvocatoModel getAvvocato() {
		return mAvvocato;
	}

	public NotificaModel getNotifica() {
		return mNotifica;
	}

	public boolean isFlagSNT() {
		return mFlagSNT;
	}

	//
	// METODI SET()
	//
	public void setIdAvvocatoCivilmenteObbligato(BigDecimal aValore) {
		mIdAvvocatoCivilmenteObbligato = aValore;
	}

	public void setCodTipoAvvocato(String aValore) {
		mCodTipoAvvocato = aValore;
	}

	public void setDataInizioValidita(Date aValore) {
		mDataInizioValidita = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setCodMotivoDesignazione(String aValore) {
		mCodMotivoDesignazione = aValore;
	}

	public void setCodTipoAutorita(String aValore) {
		mCodTipoAutorita = aValore;
	}

	public void setSedeTipoAutorita(String aValore) {
		mSedeTipoAutorita = aValore;
	}

	public void setIndirizzoTipoAutorita(String aValore) {
		mIndirizzoTipoAutorita = aValore;
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

	public void setAvvIdAvvocato(BigDecimal aValore) {
		mAvvIdAvvocato = aValore;
	}

	public void setSoggIdSoggetto(BigDecimal aValore) {
		mSoggIdSoggetto = aValore;
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setCodTipoAutoritaDif(String aValore) {
		mCodTipoAutoritaDif = aValore;
	}

	public void setSedeAutoritaDif(String aValore) {
		mSedeAutoritaDif = aValore;
	}

	public void setDescrMotivoDesignazione(String aValore) {
		mDescrMotivoDesignazione = aValore;
	}

	public void setDescrTipoAutorita(String aValore) {
		mDescrTipoAutorita = aValore;
	}

	public void setDescrTipoAutoritaDif(String aValore) {
		mDescrTipoAutoritaDif = aValore;
	}

	public void setComuneTipoAutorita(String aValore) {
		mComuneTipoAutorita = aValore;
	}

	public void setComuneTipoAutoritaDif(String aValore) {
		mComuneTipoAutoritaDif = aValore;
	}

	public void setAvvocato(AvvocatoModel aValore) {
		mAvvocato = aValore;
	}

	public void setNotifica(NotificaModel aValore) {
		mNotifica = aValore;
	}

	public void setFlagSNT(boolean flagSNT) {
		mFlagSNT = flagSNT;
	}

}