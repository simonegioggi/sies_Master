package siap.sius.avvocato.model;

/**
* <p>Title: AvvocatoFascicoloSiusModel</p>
* <p>Description: Classe Model che rappresenta il AvvocatoFascicoloSius</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class AvvocatoFascicoloSiusModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 2131858664720523102L;
	private BigDecimal mIdAvvocatoFascicoloSius;
	private String mCodTipoAvvocato;
	private String mDescrTipoAvvocato;
	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mAvvIdAvvocato;
	private BigDecimal mFasSiuIdFascicoloSius;
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

	// COSTRUTTORE DI DEFAULT
	public AvvocatoFascicoloSiusModel() {
		this.mIdAvvocatoFascicoloSius = null;
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
		this.mFasSiuIdFascicoloSius = null;
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
	}

	// COSTRUTTORE DI COPIA
	public AvvocatoFascicoloSiusModel(AvvocatoFascicoloSiusModel aModel) {
		this.mIdAvvocatoFascicoloSius = aModel.mIdAvvocatoFascicoloSius;
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
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
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
	}

	// COSTRUTTORE MODEL
	public AvvocatoFascicoloSiusModel(BigDecimal aIdAvvocatoFascicoloSius, String aCodTipoAvvocato,
			String aDescrTipoAvvocato, Date aDataInizioValidita, Date aDataFineValidita,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, BigDecimal aAvvIdAvvocato,
			BigDecimal aFasSiuIdFascicoloSius, String aCodMotivoDesignazione, String aCodTipoAutorita,
			String aSedeAutorita, String aIndirizzoTipoAutorita, String aIstDetIdIstitutoDetenzione,
			String aNote, String aCodTipoAutoritaDif, String aSedeAutoritaDif,
			String aDescrMotivoDesignazione, String aDescrTipoAutorita, String aDescrTipoAutoritaDif,
			String aComuneTipoAutorita, String aComuneTipoAutoritaDif) {
		this.mIdAvvocatoFascicoloSius = aIdAvvocatoFascicoloSius;
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
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
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
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdAvvocatoFascicoloSius() {
		return mIdAvvocatoFascicoloSius;
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

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
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

	//
	// METODI SET()
	//
	public void setIdAvvocatoFascicoloSius(BigDecimal aValore) {
		mIdAvvocatoFascicoloSius = aValore;
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

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
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

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdAvvocatoFascicoloSius + " - " + mCodTipoAvvocato + " - " + mDescrTipoAvvocato + " - "
				+ mDataInizioValidita + " - " + mDataFineValidita + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mAvvIdAvvocato + " - " + mFasSiuIdFascicoloSius
				+ " - " + mCodMotivoDesignazione + " - " + mCodTipoAutorita + " - " + mSedeAutorita + " - "
				+ mIndirizzoTipoAutorita + " - " + mIstDetIdIstitutoDetenzione + " - " + mNote + " - "
				+ mCodTipoAutoritaDif + " - " + mSedeAutoritaDif + " - " + mDescrMotivoDesignazione + " - "
				+ mDescrTipoAutorita + " - " + mDescrTipoAutoritaDif + " - " + mComuneTipoAutorita + " - "
				+ mComuneTipoAutoritaDif;

		return lStr;
	}
}