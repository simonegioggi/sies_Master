package siap.siep.misurasicurezza.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * MEV_39: aggiunto model per le MS notificate
 *
 * @author Gioggi
 */
public class MisuraSicurezzaNotificataModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -2407138641719801831L;
	private Date mDataEmissione;
	private String mDescrTipoProvvedimento;
	private String mDescrMotivoProvvedimento;
	private String mDescrTipoAutorita;
	private String mDescrComune;
	private String mCodTipoProvvedimento;
	private String mCodMotivoProvvedimento;
	private String mCodTipoAutorita;
	private String mCodComune;
	private BigDecimal mAutEstIdAutoritaEsterna;
	private BigDecimal mIdNotifica;
	private BigDecimal mIdEvento;
	private String mNote;
	private String mIdIstitutoDetenzione;

	/**
	 * costruttore senza parametri
	 */
	public MisuraSicurezzaNotificataModel() {

		this.mDataEmissione = null;
		this.mDescrTipoProvvedimento = "";
		this.mDescrMotivoProvvedimento = "";
		this.mDescrTipoAutorita = "";
		this.mDescrComune = "";
		this.mCodTipoProvvedimento = "";
		this.mCodMotivoProvvedimento = "";
		this.mCodTipoAutorita = "";
		this.mCodComune = "";
		this.mAutEstIdAutoritaEsterna = null;
		this.mIdNotifica = null;
		this.mIdEvento = null;
		this.mNote = null;
		this.mIdIstitutoDetenzione = null;
	}

	/**
	 * costruttore con parametri
	 *
	 * @param aDataEmissione
	 * @param aDescrTipoProvvedimento
	 * @param aDescrMotivoProvvedimento
	 * @param aDescrTipoAutorita
	 * @param aDescrComune
	 * @param aCodTipoProvvedimento
	 * @param aCodMotivoProvvedimento
	 * @param aCodTipoAutorita
	 * @param aCodComune
	 * @param aIdAutoritaEsterna
	 * @param aIdNotifica
	 * @param aIdEvento
	 * @param aNote
	 */
	public MisuraSicurezzaNotificataModel(Date aDataEmissione, String aDescrTipoProvvedimento,
			String aDescrMotivoProvvedimento, String aDescrTipoAutorita, String aDescrComune,
			String aCodTipoProvvedimento, String aCodMotivoProvvedimento, String aCodTipoAutorita,
			String aCodComune, BigDecimal aAutEstIdAutoritaEsterna, BigDecimal aIdNotifica,
			BigDecimal aIdEvento, String aNote, String aIdIstitutoDetenzione) {

		this.mDataEmissione = aDataEmissione;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mDescrMotivoProvvedimento = aDescrMotivoProvvedimento;
		this.mDescrTipoAutorita = aDescrTipoAutorita;
		this.mDescrComune = aDescrComune;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mCodMotivoProvvedimento = aCodMotivoProvvedimento;
		this.mCodTipoAutorita = aCodTipoAutorita;
		this.mCodComune = aCodComune;
		this.mAutEstIdAutoritaEsterna = aAutEstIdAutoritaEsterna;
		this.mIdNotifica = aIdNotifica;
		this.mIdEvento = aIdEvento;
		this.mNote = aNote;
		this.mIdIstitutoDetenzione = aIdIstitutoDetenzione;
	}

	/**
	 * costruttore con parametro modello
	 *
	 * @param aModel
	 */
	public MisuraSicurezzaNotificataModel(MisuraSicurezzaNotificataModel aModel) {

		this.mDataEmissione = aModel.mDataEmissione;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mDescrMotivoProvvedimento = aModel.mDescrMotivoProvvedimento;
		this.mDescrTipoAutorita = aModel.mDescrTipoAutorita;
		this.mDescrComune = aModel.mDescrComune;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mCodMotivoProvvedimento = aModel.mCodMotivoProvvedimento;
		this.mCodTipoAutorita = aModel.mCodTipoAutorita;
		this.mCodComune = aModel.mCodComune;
		this.mAutEstIdAutoritaEsterna = aModel.mAutEstIdAutoritaEsterna;
		this.mIdNotifica = aModel.mIdNotifica;
		this.mIdEvento = aModel.mIdEvento;
		this.mNote = aModel.mNote;
		this.mIdIstitutoDetenzione = aModel.mIdIstitutoDetenzione;
	}

	// =============================================
	// Metodi Getter & Setter
	// =============================================
	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public void setDataEmissione(Date mDataEmissione) {
		this.mDataEmissione = mDataEmissione;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public void setDescrTipoProvvedimento(String mDescrTipoProvvedimento) {
		this.mDescrTipoProvvedimento = mDescrTipoProvvedimento;
	}

	public String getDescrMotivoProvvedimento() {
		return mDescrMotivoProvvedimento;
	}

	public void setDescrMotivoProvvedimento(String mDescrMotivoProvvedimento) {
		this.mDescrMotivoProvvedimento = mDescrMotivoProvvedimento;
	}

	public String getDescrTipoAutorita() {
		return mDescrTipoAutorita;
	}

	public void setDescrTipoAutorita(String mDescrTipoAutorita) {
		this.mDescrTipoAutorita = mDescrTipoAutorita;
	}

	public String getDescrComune() {
		return mDescrComune;
	}

	public void setDescrComune(String mDescrComune) {
		this.mDescrComune = mDescrComune;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public void setCodTipoProvvedimento(String mCodTipoProvvedimento) {
		this.mCodTipoProvvedimento = mCodTipoProvvedimento;
	}

	public String getCodMotivoProvvedimento() {
		return mCodMotivoProvvedimento;
	}

	public void setCodMotivoProvvedimento(String mCodMotivoProvvedimento) {
		this.mCodMotivoProvvedimento = mCodMotivoProvvedimento;
	}

	public String getCodTipoAutorita() {
		return mCodTipoAutorita;
	}

	public void setCodTipoAutorita(String mCodTipoAutorita) {
		this.mCodTipoAutorita = mCodTipoAutorita;
	}

	public String getCodComune() {
		return mCodComune;
	}

	public void setCodComune(String mCodComune) {
		this.mCodComune = mCodComune;
	}

	public BigDecimal getAutEstIdAutoritaEsterna() {
		return mAutEstIdAutoritaEsterna;
	}

	public void setAutEstIdAutoritaEsterna(BigDecimal mAutEstIdAutoritaEsterna) {
		this.mAutEstIdAutoritaEsterna = mAutEstIdAutoritaEsterna;
	}

	public BigDecimal getIdNotifica() {
		return mIdNotifica;
	}

	public void setIdNotifica(BigDecimal mIdNotifica) {
		this.mIdNotifica = mIdNotifica;
	}

	public BigDecimal getIdEvento() {
		return mIdEvento;
	}

	public void setIdEvento(BigDecimal mIdEvento) {
		this.mIdEvento = mIdEvento;
	}

	public String getNote() {
		return mNote;
	}

	public void setNote(String mNote) {
		this.mNote = mNote;
	}

	public String getIdIstitutoDetenzione() {
		return mIdIstitutoDetenzione;
	}

	public void setIdIstitutoDetenzione(String mIdIstitutoDetenzione) {
		this.mIdIstitutoDetenzione = mIdIstitutoDetenzione;
	}

	/**
	 * @return String
	 */
	@Override
	public String toString() {

		String s = "[ mDataEmissione = " + mDataEmissione + "]\n" + "[ mDescrTipoProvvedimento = "
				+ mDescrTipoProvvedimento + "]\n" + "[ mDescrMotivoProvvedimento = "
				+ mDescrMotivoProvvedimento + "]\n" + "[ mDescrTipoAutorita = " + mDescrTipoAutorita + "]\n"
				+ "[ mDescrComune = " + mDescrComune + "]\n" + "[ mCodTipoProvvedimento = "
				+ mCodTipoProvvedimento + "]\n" + "[ mCodMotivoProvvedimento = " + mCodMotivoProvvedimento
				+ "]\n" + "[ mCodTipoAutorita = " + mCodTipoAutorita + "]\n" + "[ mCodComune = " + mCodComune
				+ "]\n" + "[ mAutEstIdAutoritaEsterna = " + mAutEstIdAutoritaEsterna + "]\n"
				+ "[ mIdNotifica = " + mIdNotifica + "]\n" + "[ mIdEvento = " + mIdEvento + "]\n"
				+ "[ mNote = " + mNote + "]\n" + "[ mIdIstitutoDetenzione = " + mIdIstitutoDetenzione + "]";
		return s;
	}

}