package siap.siep.avvocato.model;

/**
* <p>Title: AvvocatoFascicoloSiepModel</p>
* <p>Description: Classe Model che rappresenta l'entità AvvocatoFascicoloSiep</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class AvvocatoFascicoloSiepModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4912886776018879223L;

	private BigDecimal mIdAvvocatoFascicoloSiep;
	private String mCodTipoAvvocato;
	private String mDescrTipoAvvocato;// desc
	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento; // desc
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento; // desc
	private BigDecimal mAvvIdAvvocato;
	private BigDecimal mFasSieIdFascicoloSiep;
	private String mCodMotivoDesignazione;
	private String mCodTipoAutorita;
	private String mSedeAutorita;
	private String mIndirizzoTipoAutorita;
	private String mIstDetIdIstitutoDetenzione;
	private String mNote;
	private String mCodTipoAutoritaDif;
	private String mSedeAutoritaDif;
	private String mDescrMotivoDesignazione;
	private String mDescrTipoAutorita;
	private String mDescrTipoAutoritaDif;
	private String mComuneTipoAutorita;
	private String mComuneTipoAutoritaDif;
	private BigDecimal mEveIdEvento;
	private String mMotivo;
	private BigDecimal mAvvIdAvvocatoFascicoloSost;

	// COSTRUTTORE DI DEFAULT
	public AvvocatoFascicoloSiepModel() {
		this.mIdAvvocatoFascicoloSiep = null;
		this.mCodTipoAvvocato = "";
		this.mDescrTipoAvvocato = "";
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mAvvIdAvvocato = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mCodMotivoDesignazione = "";
		this.mCodTipoAutorita = "";
		this.mSedeAutorita = "";
		this.mIndirizzoTipoAutorita = "";
		this.mIstDetIdIstitutoDetenzione = "";
		this.mNote = "";
		this.mCodTipoAutoritaDif = "";
		this.mSedeAutoritaDif = "";
		this.mDescrMotivoDesignazione = "";
		this.mDescrTipoAutorita = "";
		this.mDescrTipoAutoritaDif = "";
		this.mComuneTipoAutorita = "";
		this.mComuneTipoAutoritaDif = "";
		this.mEveIdEvento = null;
		this.mMotivo = "";
		this.mAvvIdAvvocatoFascicoloSost = null;
	}

	// COSTRUTTORE DI COPIA
	public AvvocatoFascicoloSiepModel(AvvocatoFascicoloSiepModel aModel) {
		this.mIdAvvocatoFascicoloSiep = aModel.mIdAvvocatoFascicoloSiep;
		this.mCodTipoAvvocato = aModel.mCodTipoAvvocato;
		this.mDescrTipoAvvocato = aModel.mDescrTipoAvvocato;
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mAvvIdAvvocato = aModel.mAvvIdAvvocato;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mCodMotivoDesignazione = aModel.mCodMotivoDesignazione;
		this.mCodTipoAutorita = aModel.mCodTipoAutorita;
		this.mSedeAutorita = aModel.mSedeAutorita;
		this.mIndirizzoTipoAutorita = aModel.mIndirizzoTipoAutorita;
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mNote = aModel.mNote;
		this.mCodTipoAutoritaDif = aModel.mCodTipoAutoritaDif;
		this.mSedeAutoritaDif = aModel.mSedeAutoritaDif;
		this.mDescrMotivoDesignazione = aModel.mDescrMotivoDesignazione;
		this.mDescrTipoAutorita = aModel.mDescrTipoAutorita;
		this.mDescrTipoAutoritaDif = aModel.mDescrTipoAutoritaDif;
		this.mComuneTipoAutorita = aModel.mComuneTipoAutorita;
		this.mComuneTipoAutoritaDif = aModel.mComuneTipoAutoritaDif;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mMotivo = aModel.mMotivo;
		this.mAvvIdAvvocatoFascicoloSost = aModel.mAvvIdAvvocatoFascicoloSost;

	}

	// COSTRUTTORE MODEL
	public AvvocatoFascicoloSiepModel(BigDecimal aIdAvvocatoFascicoloSiep, String aCodTipoAvvocato,
			String aDescrTipoAvvocato, Date aDataInizioValidita, Date aDataFineValidita,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, BigDecimal aAvvIdAvvocato,
			BigDecimal aFasSieIdFascicoloSiep, String aCodMotivoDesignazione, String aCodTipoAutorita,
			String aSedeAutorita, String aIndirizzoTipoAutorita, String aIstDetIdIstitutoDetenzione,
			String aNote, String aCodTipoAutoritaDif, String aSedeAutoritaDif,
			String aDescrMotivoDesignazione, String aDescrTipoAutorita, String aDescrTipoAutoritaDif,
			String aComuneTipoAutorita, String aComuneTipoAutoritaDif, BigDecimal aEveIdEvento,
			String aMotivo, BigDecimal aAvvIdAvvocatoFascicoloSost) {
		this.mIdAvvocatoFascicoloSiep = aIdAvvocatoFascicoloSiep;
		this.mCodTipoAvvocato = aCodTipoAvvocato;
		this.mDescrTipoAvvocato = aDescrTipoAvvocato;
		this.mDataInizioValidita = aDataInizioValidita;
		this.mDataFineValidita = aDataFineValidita;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mAvvIdAvvocato = aAvvIdAvvocato;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mCodMotivoDesignazione = aCodMotivoDesignazione;
		this.mCodTipoAutorita = aCodTipoAutorita;
		this.mSedeAutorita = aSedeAutorita;
		this.mIndirizzoTipoAutorita = aIndirizzoTipoAutorita;
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		this.mNote = aNote;
		this.mCodTipoAutoritaDif = aCodTipoAutoritaDif;
		this.mSedeAutoritaDif = aSedeAutoritaDif;
		this.mDescrMotivoDesignazione = aDescrMotivoDesignazione;
		this.mDescrTipoAutorita = aDescrTipoAutorita;
		this.mDescrTipoAutoritaDif = aDescrTipoAutoritaDif;
		this.mComuneTipoAutorita = aComuneTipoAutorita;
		this.mComuneTipoAutoritaDif = aComuneTipoAutoritaDif;
		this.mEveIdEvento = aEveIdEvento;
		this.mMotivo = aMotivo;
		this.mAvvIdAvvocatoFascicoloSost = aAvvIdAvvocatoFascicoloSost;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdAvvocatoFascicoloSiep() {
		return mIdAvvocatoFascicoloSiep;
	}

	public String getCodTipoAvvocato() {
		return mCodTipoAvvocato;
	}

	public String getDescrTipoAvvocato() {
		return mDescrTipoAvvocato;
	}

	public Date getDataInizioValidita() {
		return mDataInizioValidita;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
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

	public BigDecimal getAvvIdAvvocato() {
		return mAvvIdAvvocato;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public String getCodMotivoDesignazione() {
		return mCodMotivoDesignazione;
	}

	public String getCodTipoAutorita() {
		return mCodTipoAutorita;
	}

	public String getSedeAutorita() {
		return mSedeAutorita;
	}

	public String getIndirizzoTipoAutorita() {
		return mIndirizzoTipoAutorita;
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

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public String getMotivo() {
		return mMotivo;
	}

	public BigDecimal getAvvIdAvvocatoFascicoloSost() {
		return mAvvIdAvvocatoFascicoloSost;
	}

	//
	// METODI SET()
	//
	public void setIdAvvocatoFascicoloSiep(BigDecimal aValore) {
		mIdAvvocatoFascicoloSiep = aValore;
	}

	public void setCodTipoAvvocato(String aValore) {
		mCodTipoAvvocato = aValore;
	}

	public void setDescrTipoAvvocato(String aValore) {
		mDescrTipoAvvocato = aValore;
	}

	public void setDataInizioValidita(Date aValore) {
		mDataInizioValidita = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
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

	public void setAvvIdAvvocato(BigDecimal aValore) {
		mAvvIdAvvocato = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setCodMotivoDesignazione(String aValore) {
		mCodMotivoDesignazione = aValore;
	}

	public void setCodTipoAutorita(String aValore) {
		mCodTipoAutorita = aValore;
	}

	public void setSedeAutorita(String aValore) {
		mSedeAutorita = aValore;
	}

	public void setIndirizzoTipoAutorita(String aValore) {
		mIndirizzoTipoAutorita = aValore;
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

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setMotivo(String aValore) {
		mMotivo = aValore;
	}

	public void setAvvIdAvvocatoFascicoloSost(BigDecimal aValore) {
		mAvvIdAvvocatoFascicoloSost = aValore;
	}

	public String toString() {
		String lStr = new String();
		lStr = "" + mIdAvvocatoFascicoloSiep + " - " + mCodTipoAvvocato + " - " + mDescrTipoAvvocato + " - "
				+ mDataInizioValidita + " - " + mDataFineValidita + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mAvvIdAvvocato + " - " + mFasSieIdFascicoloSiep
				+ " - " + mCodMotivoDesignazione + " - " + mCodTipoAutorita + " - " + mSedeAutorita + " - "
				+ mIndirizzoTipoAutorita + " - " + mIstDetIdIstitutoDetenzione + " - " + mNote + " - "
				+ mCodTipoAutoritaDif + " - " + mSedeAutoritaDif + " - " + mDescrMotivoDesignazione + " - "
				+ mDescrTipoAutorita + " - " + mDescrTipoAutoritaDif + " - " + mComuneTipoAutorita + " - "
				+ mComuneTipoAutoritaDif + " - " + mEveIdEvento + " - " + mMotivo + " - "
				+ mAvvIdAvvocatoFascicoloSost;

		return lStr;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString2() {
		String lStr = new String();
		lStr = "AvvocatoFascicoloSiepModel:\n" + "[ mIdAvvocatoFascicoloSiep    = " + mIdAvvocatoFascicoloSiep
				+ " ]\n" + "[ mAvvIdAvvocato              = " + mAvvIdAvvocato + " ]\n"
				+ "[ mFasSieIdFascicoloSiep      = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mCodTipoAvvocato            = " + mCodTipoAvvocato + " (" + mDescrTipoAvvocato + ") ]\n"
				+ "[ mDataInizioValidita         = " + mDataInizioValidita + " ]\n"
				+ "[ mDataFineValidita           = " + mDataFineValidita + " ]\n"
				+ "[ mCodMotivoDesignazione      = " + mCodMotivoDesignazione + " ("
				+ mDescrMotivoDesignazione + ") ]\n" + "[ mCodTipoAutorita            = " + mCodTipoAutorita
				+ " (" + mDescrTipoAutorita + ") ]\n" + "[ mSedeAutorita               = " + mSedeAutorita
				+ " ]\n" + "[ mIndirizzoTipoAutorita      = " + mIndirizzoTipoAutorita + " ]\n"
				+ "[ mCodTipoAutoritaDif         = " + mCodTipoAutoritaDif + " (" + mDescrTipoAutoritaDif
				+ ") ]\n" + "[ mSedeAutoritaDif            = " + mSedeAutoritaDif + " ]\n"
				+ "[ mCodOperatoreInserimento    = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento            = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento      = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento  = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento          = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento    = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mNote                       = " + mNote + " ]\n" + "[ mIstDetIdIstitutoDetenzione = "
				+ mIstDetIdIstitutoDetenzione + " ]\n" + "[ mEveIdEvento      		  = " + mEveIdEvento
				+ " ]\n" + "[ mMotivo      		  		  = " + mMotivo + " ]\n"
				+ "[ mAvvIdAvvocatoFascicoloSost = " + mAvvIdAvvocatoFascicoloSost + " ]\n";
		return lStr;
	}

}