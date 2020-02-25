package siap.sius.scadenzario.model;

/**
* <p>Title: ScadenzarioSiusModel</p>
* <p>Description: Classe Model che rappresenta lo ScadenzarioSius</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.model.EventoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import f3b.model.GenericModel;

public class ScadenzarioSiusModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6473173902669874789L;

	private BigDecimal mIdScadenzarioSius;
	private String mCodTipoScadenzario;
	private String mDescrTipoScadenzario;
	private Date mDataInizioScadenza;
	private Date mDataFineScadenza;
	private String mFlagVisto;
	private Date mDataVisto;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mGiorniResidui;
	private BigDecimal mFasSiuIdFascicoloSius;
	private FascicoloSiusModel mFascicoloSius;
	private BigDecimal mEveIdEvento; // 02/08/2004
	private EventoModel mEvento; // 02/08/2004

	// COSTRUTTORE DI DEFAULT
	public ScadenzarioSiusModel() {
		this.mIdScadenzarioSius = null;
		this.mCodTipoScadenzario = "";
		this.mDescrTipoScadenzario = "";
		this.mDataInizioScadenza = null;
		this.mDataFineScadenza = null;
		this.mFlagVisto = "";
		this.mDataVisto = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mGiorniResidui = null;
		this.mFasSiuIdFascicoloSius = null;
		this.mFascicoloSius = null;
		this.mEveIdEvento = null;
		this.mEvento = null;
	}

	// COSTRUTTORE DI COPIA
	public ScadenzarioSiusModel(ScadenzarioSiusModel aModel) {
		this.mIdScadenzarioSius = aModel.mIdScadenzarioSius;
		this.mCodTipoScadenzario = aModel.mCodTipoScadenzario;
		this.mDescrTipoScadenzario = aModel.mDescrTipoScadenzario;
		this.mDataInizioScadenza = aModel.mDataInizioScadenza;
		this.mDataFineScadenza = aModel.mDataFineScadenza;
		this.mFlagVisto = aModel.mFlagVisto;
		this.mDataVisto = aModel.mDataVisto;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mFascicoloSius = aModel.mFascicoloSius;
		this.mGiorniResidui = aModel.mGiorniResidui;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mEvento = aModel.mEvento;
	}

	// COSTRUTTORE MODEL
	public ScadenzarioSiusModel(BigDecimal aIdScadenzarioSius, String aCodTipoScadenzario,
			String aDescrTipoScadenzario, Date aDataInizioScadenza, Date aDataFineScadenza, String aFlagVisto,
			Date aDataVisto, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aGiorniResidui, BigDecimal aFasSiuIdFascicoloSius, FascicoloSiusModel aFascicoloSius,
			BigDecimal aEveIdEvento, EventoModel aEvento) {
		this.mIdScadenzarioSius = aIdScadenzarioSius;
		this.mCodTipoScadenzario = aCodTipoScadenzario;
		this.mDescrTipoScadenzario = aDescrTipoScadenzario;
		this.mDataInizioScadenza = aDataInizioScadenza;
		this.mDataFineScadenza = aDataFineScadenza;
		this.mFlagVisto = aFlagVisto;
		this.mDataVisto = aDataVisto;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mGiorniResidui = aGiorniResidui;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mFascicoloSius = aFascicoloSius;
		this.mEveIdEvento = aEveIdEvento;
		this.mEvento = aEvento;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdScadenzarioSius() {
		return mIdScadenzarioSius;
	}

	public String getCodTipoScadenzario() {
		return mCodTipoScadenzario;
	}

	public String getDescrTipoScadenzario() {
		return mDescrTipoScadenzario;
	}

	public Date getDataInizioScadenza() {
		return mDataInizioScadenza;
	}

	public Date getDataFineScadenza() {
		return mDataFineScadenza;
	}

	public String getFlagVisto() {
		return mFlagVisto;
	}

	public Date getDataVisto() {
		return mDataVisto;
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

	public BigDecimal getGiorniResidui() {
		return mGiorniResidui;
	}

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public FascicoloSiusModel getFascicoloSius() {
		return mFascicoloSius;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public EventoModel getEvento() {
		return mEvento;
	}

	//
	// METODI SET()
	//
	public void setIdScadenzarioSius(BigDecimal aValore) {
		mIdScadenzarioSius = aValore;
	}

	public void setCodTipoScadenzario(String aValore) {
		mCodTipoScadenzario = aValore;
	}

	public void setDescrTipoScadenzario(String aValore) {
		mDescrTipoScadenzario = aValore;
	}

	public void setDataInizioScadenza(Date aValore) {
		mDataInizioScadenza = aValore;
	}

	public void setDataFineScadenza(Date aValore) {
		mDataFineScadenza = aValore;
	}

	public void setFlagVisto(String aValore) {
		mFlagVisto = aValore;
	}

	public void setDataVisto(Date aValore) {
		mDataVisto = aValore;
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

	public void setGiorniResidui(BigDecimal aValore) {
		mGiorniResidui = aValore;
	}

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setFascicoloSius(FascicoloSiusModel aValore) {
		mFascicoloSius = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdScadenzarioSius + " - " + mCodTipoScadenzario + " - " + mDescrTipoScadenzario + " - "
				+ mDataInizioScadenza + " - " + mDataFineScadenza + " - " + mFlagVisto + " - " + mDataVisto
				+ " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento
				+ " - " + mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - "
				+ mDataAggiornamento + " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento
				+ " - " + mFasSiuIdFascicoloSius + " - " + mEveIdEvento;

		return lStr;
	}

}