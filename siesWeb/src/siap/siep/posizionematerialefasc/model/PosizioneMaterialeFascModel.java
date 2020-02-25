package siap.siep.posizionematerialefasc.model;

/**
* <p>Title: PosizioneMaterialeFascModel</p>
* <p>Description: Classe Model che rappresenta la PosizioneMaterialeFasc</p>
 * Poichè la Posizione materiale fa riferimento a due tabelle diverse che differiscono
 * tra loro per il campo ID Fascicolo che nel caso di Fascicolo SIEP punta all'ID FASCICOLO SIEP nell'altro caso punta all'ID FASCICOLO SIUS.</p>
 * Per distinguere i due casi la classe contiene un attributo "tipo Posizione Materiale" che stabilisce quale caso viene trattato.
 * Per inizializzare tale attributo occorre passarlo nel costruttore, di default il Tipo di Posizione
 * Materiale è inizializzato a Fascicolo Siep.
 * il tipo di Posizione materiale.
 *
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class PosizioneMaterialeFascModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 4280980797093140207L;
	public static final String POSIZIONE_MATERIALE_FASCICOLO_SIEP = "SIEP";
	public static final String POSIZIONE_MATERIALE_FASCICOLO_SIUS = "SIUS";
	public static final String POSIZIONE_MATERIALE_FASCICOLO_SIGE = "SIGE";

	private String mCodPosizioneMateriale;
	private String mDescrPosizioneMateriale;
	private String mCodUfficio;
	private String mDescrUfficio;
	private BigDecimal mIdFascicolo;
	private String mCodStatoProcedimento;
	private String mDescrStatoProcedimento;
	private Date mDataInizio;
	private Date mDataFine;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mTipoPosizioneMateriale = POSIZIONE_MATERIALE_FASCICOLO_SIEP;

	// COSTRUTTORE DI DEFAULT
	public PosizioneMaterialeFascModel() {
		this.mCodPosizioneMateriale = "";
		this.mDescrPosizioneMateriale = "";
		this.mCodUfficio = "";
		this.mDescrUfficio = "";
		this.mIdFascicolo = null;
		this.mCodStatoProcedimento = "";
		this.mDescrStatoProcedimento = "";
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		mTipoPosizioneMateriale = POSIZIONE_MATERIALE_FASCICOLO_SIEP;
	}

	// COSTRUTTORE DI COPIA
	public PosizioneMaterialeFascModel(PosizioneMaterialeFascModel aModel) {
		this.mCodPosizioneMateriale = aModel.mCodPosizioneMateriale;
		this.mDescrPosizioneMateriale = aModel.mDescrPosizioneMateriale;
		this.mCodUfficio = aModel.mCodUfficio;
		this.mDescrUfficio = aModel.mDescrUfficio;
		this.mIdFascicolo = aModel.mIdFascicolo;
		this.mCodStatoProcedimento = aModel.mCodStatoProcedimento;
		this.mDescrStatoProcedimento = aModel.mDescrStatoProcedimento;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mTipoPosizioneMateriale = aModel.mTipoPosizioneMateriale;
	}

	// COSTRUTTORE MODEL
	public PosizioneMaterialeFascModel(String aCodPosizioneMateriale, String aDescrPosizioneMateriale,
			String aCodUfficio, String aDescrUfficio, BigDecimal aFasSieIdFascicoloSiep,
			String aCodStatoProcedimento, String aDescrStatoProcedimento, Date aDataInizio, Date aDataFine,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento) {
		this.mCodPosizioneMateriale = aCodPosizioneMateriale;
		this.mDescrPosizioneMateriale = aDescrPosizioneMateriale;
		this.mCodUfficio = aCodUfficio;
		this.mDescrUfficio = aDescrUfficio;
		this.mIdFascicolo = aFasSieIdFascicoloSiep;
		this.mCodStatoProcedimento = aCodStatoProcedimento;
		this.mDescrStatoProcedimento = aDescrStatoProcedimento;
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		mTipoPosizioneMateriale = POSIZIONE_MATERIALE_FASCICOLO_SIEP;
	}

	public PosizioneMaterialeFascModel(String aTipoPosizioneMateriale) {
		this();
		if (aTipoPosizioneMateriale.equalsIgnoreCase(POSIZIONE_MATERIALE_FASCICOLO_SIUS)) {
			mTipoPosizioneMateriale = POSIZIONE_MATERIALE_FASCICOLO_SIUS;
		} else if (aTipoPosizioneMateriale.equalsIgnoreCase(POSIZIONE_MATERIALE_FASCICOLO_SIGE)) {
			mTipoPosizioneMateriale = POSIZIONE_MATERIALE_FASCICOLO_SIGE;
		}
	}

	//
	// METODI GET()
	//

	public String getCodPosizioneMateriale() {
		return mCodPosizioneMateriale;
	}

	public String getDescrPosizioneMateriale() {
		return mDescrPosizioneMateriale;
	}

	public String getCodUfficio() {
		return mCodUfficio;
	}

	public String getDescrUfficio() {
		return mDescrUfficio;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mIdFascicolo;
	}

	public String getCodStatoProcedimento() {
		return mCodStatoProcedimento;
	}

	public String getDescrStatoProcedimento() {
		return mDescrStatoProcedimento;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
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

	public String getTipoPosizioneMateriale() {
		return mTipoPosizioneMateriale;
	}

	//
	// METODI SET()
	//

	public void setCodPosizioneMateriale(String aValore) {
		mCodPosizioneMateriale = aValore;
	}

	public void setDescrPosizioneMateriale(String aValore) {
		mDescrPosizioneMateriale = aValore;
	}

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

	public void setDescrUfficio(String aValore) {
		mDescrUfficio = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mIdFascicolo = aValore;
	}

	public void setCodStatoProcedimento(String aValore) {
		mCodStatoProcedimento = aValore;
	}

	public void setDescrStatoProcedimento(String aValore) {
		mDescrStatoProcedimento = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
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

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mCodPosizioneMateriale + " - " + mDescrPosizioneMateriale + " - " + mCodUfficio + " - "
				+ mDescrUfficio + " - " + mIdFascicolo + " - " + mCodStatoProcedimento + " - "
				+ mDescrStatoProcedimento + " - " + mDataInizio + " - " + mDataFine + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento + " - " + mTipoPosizioneMateriale;

		return lStr;
	}
}
