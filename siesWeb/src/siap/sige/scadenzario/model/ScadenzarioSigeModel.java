package siap.sige.scadenzario.model;

/**
* <p>Title: ScadenzarioSigeModel</p>
* <p>Description: Classe Model che rappresenta lo ScadenzarioSige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import f3b.model.GenericModel;

public class ScadenzarioSigeModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6642895521721487413L;

	private BigDecimal mIdScadenzarioSige;
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
	private BigDecimal mFasIdFascicoloSige;
	private FascicoloSigeModel mFascicoloSige;
	private BigDecimal mEveIdEvento; // 02/08/2004
	private EventoModel mEvento; // 02/08/2004
	private SoggettoModel mSoggetto; // 02/08/2004

	// COSTRUTTORE DI DEFAULT
	public ScadenzarioSigeModel() {
		this.mIdScadenzarioSige = null;
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
		this.mFasIdFascicoloSige = null;
		this.mFascicoloSige = null;
		this.mEveIdEvento = null;
		this.mEvento = null;
		this.mSoggetto = null;
	}

	// COSTRUTTORE DI COPIA
	public ScadenzarioSigeModel(ScadenzarioSigeModel aModel) {
		this.mIdScadenzarioSige = aModel.mIdScadenzarioSige;
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
		this.mFasIdFascicoloSige = aModel.mFasIdFascicoloSige;
		this.mFascicoloSige = aModel.mFascicoloSige;
		this.mGiorniResidui = aModel.mGiorniResidui;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mEvento = aModel.mEvento;
		this.mSoggetto = aModel.mSoggetto;
	}

	// COSTRUTTORE MODEL
	public ScadenzarioSigeModel(BigDecimal aIdScadenzarioSige, String aCodTipoScadenzario,
			String aDescrTipoScadenzario, Date aDataInizioScadenza, Date aDataFineScadenza, String aFlagVisto,
			Date aDataVisto, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aGiorniResidui, BigDecimal aFasIdFascicoloSige, FascicoloSigeModel aFascicoloSige,
			BigDecimal aEveIdEvento, EventoModel aEvento, SoggettoModel aSoggetto) {
		this.mIdScadenzarioSige = aIdScadenzarioSige;
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
		this.mFasIdFascicoloSige = aFasIdFascicoloSige;
		this.mFascicoloSige = aFascicoloSige;
		this.mEveIdEvento = aEveIdEvento;
		this.mEvento = aEvento;
		this.mSoggetto = aSoggetto;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdScadenzarioSige() {
		return mIdScadenzarioSige;
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

	public BigDecimal getFasIdFascicoloSige() {
		return mFasIdFascicoloSige;
	}

	public FascicoloSigeModel getFascicoloSige() {
		return mFascicoloSige;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public EventoModel getEvento() {
		return mEvento;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	//
	// METODI SET()
	//
	public void setIdScadenzarioSige(BigDecimal aValore) {
		mIdScadenzarioSige = aValore;
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

	public void setFasIdFascicoloSige(BigDecimal aValore) {
		mFasIdFascicoloSige = aValore;
	}

	public void setFascicoloSige(FascicoloSigeModel aValore) {
		mFascicoloSige = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdScadenzarioSige + " - " + mCodTipoScadenzario + " - " + mDescrTipoScadenzario + " - "
				+ mDataInizioScadenza + " - " + mDataFineScadenza + " - " + mFlagVisto + " - " + mDataVisto
				+ " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento
				+ " - " + mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - "
				+ mDataAggiornamento + " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento
				+ " - " + mFasIdFascicoloSige + " - " + mEveIdEvento;

		return lStr;
	}

}