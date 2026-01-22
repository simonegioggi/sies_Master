package siap.sius.scadenzario.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;

/**
 * ScadenzarioSiusModel - Classe Model che rappresenta lo Scadenzario Sius
 *
 * @version 1.0
 */
public class ScadenzarioSiusModel extends GenericModel {

	/**
	 * serialVersionUID
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
	// MEV_2026-1: aggiunte variabili e gestite nella classe
	private RiferimentoFascicoloSiepModel mFascicoloSiep;
	private SoggettoModel mSoggetto;
	private PosizioneGiuridicaModel mPosizioneGiuridica;
	private GeneraleProcedimentoModel mGeneraleProcedimento;
	private Date mDataFinePenaVirtuale;
	private BigDecimal mGiorniResiduiVirtuali;
	// FINE MEV_2026-1

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
		this.mFascicoloSiep = null;
		this.mSoggetto = null;
		this.mPosizioneGiuridica = null;
		this.mGeneraleProcedimento = null;
		this.mDataFinePenaVirtuale = null;
		this.mGiorniResiduiVirtuali = null;
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
		this.mFascicoloSiep = aModel.mFascicoloSiep;
		this.mSoggetto = aModel.mSoggetto;
		this.mPosizioneGiuridica = aModel.mPosizioneGiuridica;
		this.mGeneraleProcedimento = aModel.mGeneraleProcedimento;
		this.mDataFinePenaVirtuale = aModel.mDataFinePenaVirtuale;
		this.mGiorniResiduiVirtuali = aModel.mGiorniResiduiVirtuali;
	}

	// COSTRUTTORE MODEL
	public ScadenzarioSiusModel(BigDecimal aIdScadenzarioSius, String aCodTipoScadenzario,
			String aDescrTipoScadenzario, Date aDataInizioScadenza, Date aDataFineScadenza, String aFlagVisto,
			Date aDataVisto, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aGiorniResidui, BigDecimal aFasSiuIdFascicoloSius, FascicoloSiusModel aFascicoloSius,
			BigDecimal aEveIdEvento, EventoModel aEvento, RiferimentoFascicoloSiepModel aFascicoloSiep,
			SoggettoModel aSoggetto, PosizioneGiuridicaModel aPosizioneGiuridica,
			GeneraleProcedimentoModel aGeneraleProcedimento, Date aDataFinePenaVirtuale,
			BigDecimal aGiorniResiduiVirtuali) {

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
		this.mFascicoloSiep = aFascicoloSiep;
		this.mSoggetto = aSoggetto;
		this.mPosizioneGiuridica = aPosizioneGiuridica;
		this.mGeneraleProcedimento = aGeneraleProcedimento;
		this.mDataFinePenaVirtuale = aDataFinePenaVirtuale;
		this.mGiorniResiduiVirtuali = aGiorniResiduiVirtuali;
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

	public RiferimentoFascicoloSiepModel getFascicoloSiep() {
		return mFascicoloSiep;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public PosizioneGiuridicaModel getPosizioneGiuridica() {
		return mPosizioneGiuridica;
	}

	public GeneraleProcedimentoModel getGeneraleProcedimento() {
		return mGeneraleProcedimento;
	}

	public Date getDataFinePenaVirtuale() {
		return mDataFinePenaVirtuale;
	}

	public BigDecimal getGiorniResiduiVirtuali() {
		return mGiorniResiduiVirtuali;
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

	public void setFascicoloSiep(RiferimentoFascicoloSiepModel aValore) {
		mFascicoloSiep = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setPosizioneGiuridica(PosizioneGiuridicaModel aValore) {
		mPosizioneGiuridica = aValore;
	}

	public void setGeneraleProcedimento(GeneraleProcedimentoModel aValore) {
		mGeneraleProcedimento = aValore;
	}

	public void setDataFinePenaVirtuale(Date aValore) {
		this.mDataFinePenaVirtuale = aValore;
	}

	public void setGiorniResiduiVirtuali(BigDecimal aValore) {
		this.mGiorniResiduiVirtuali = aValore;
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