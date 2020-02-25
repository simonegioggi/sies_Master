package siap.sius.penapecuniaria.model;

/**
* <p>Title: RichiestaConversioneModel</p>
* <p>Description: Classe Model che rappresenta il RichiestaConversione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class RichiesteConversioniPerOrdinanzaModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -2147350601147294127L;
	private String[] mIdRichiestaConversione;
	private String[] mCodTipoRichiesta;
	private String[] mNumGiorniDurataEsito;
	private String[] mNumMesiDurataEsito;
	private String[] mNumAnniDurataEsito;
	private String[] mCodTipoSanzione;
	private String[] mNumeroRate;
	private BigDecimal[] mValoreRata;
	private BigDecimal[] mValoreUltimaRata;
	private Date mDataInizioPagamento;
	private BigDecimal mNumGiorniInizioPagamento;

	/**
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 */
	public RichiesteConversioniPerOrdinanzaModel() {
		this.mIdRichiestaConversione = null;
		this.mCodTipoRichiesta = null;
		this.mNumGiorniDurataEsito = null;
		this.mNumMesiDurataEsito = null;
		this.mNumAnniDurataEsito = null;
		this.mCodTipoSanzione = null;
		this.mNumeroRate = null;
		this.mValoreRata = null;
		this.mValoreUltimaRata = null;
		this.mDataInizioPagamento = null;
		this.mNumGiorniInizioPagamento = null;
	}

	/**
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 */
	public RichiesteConversioniPerOrdinanzaModel(RichiesteConversioniPerOrdinanzaModel aModel) {
		this.mIdRichiestaConversione = aModel.mIdRichiestaConversione;
		this.mCodTipoRichiesta = aModel.mCodTipoRichiesta;
		this.mNumGiorniDurataEsito = aModel.mNumGiorniDurataEsito;
		this.mNumMesiDurataEsito = aModel.mNumMesiDurataEsito;
		this.mNumAnniDurataEsito = aModel.mNumAnniDurataEsito;
		this.mCodTipoSanzione = aModel.mCodTipoSanzione;
		this.mNumeroRate = aModel.mNumeroRate;
		this.mValoreRata = aModel.mValoreRata;
		this.mValoreUltimaRata = aModel.mValoreUltimaRata;
		this.mDataInizioPagamento = aModel.mDataInizioPagamento;
		this.mNumGiorniInizioPagamento = aModel.mNumGiorniInizioPagamento;
	}

	/**
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 */
	public RichiesteConversioniPerOrdinanzaModel(String[] aIdRichiestaConversione, String[] aCodTipoRichiesta,
			String[] aNumGiorniDurataEsito, String[] aNumMesiDurataEsito, String[] aNumAnniDurataEsito,
			String[] aCodTipoSanzione, String[] aNumeroRate, BigDecimal[] aValoreRata,
			BigDecimal[] aValoreUltimaRata, Date aDataInizioPagamento, BigDecimal aNumGiorniInizioPagamento)

	{
		this.mIdRichiestaConversione = aIdRichiestaConversione;
		this.mCodTipoRichiesta = aCodTipoRichiesta;
		this.mNumGiorniDurataEsito = aNumGiorniDurataEsito;
		this.mNumMesiDurataEsito = aNumMesiDurataEsito;
		this.mNumAnniDurataEsito = aNumAnniDurataEsito;
		this.mCodTipoSanzione = aCodTipoSanzione;
		this.mNumeroRate = aNumeroRate;
		this.mValoreRata = aValoreRata;
		this.mValoreUltimaRata = aValoreUltimaRata;
		this.mDataInizioPagamento = aDataInizioPagamento;
		this.mNumGiorniInizioPagamento = aNumGiorniInizioPagamento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public String[] getIdRichiestaConversione() {
		return mIdRichiestaConversione;
	}

	public String[] getCodTipoRichiesta() {
		return mCodTipoRichiesta;
	}

	public String[] getNumGiorniDurataEsito() {
		return mNumGiorniDurataEsito;
	}

	public String[] getNumMesiDurataEsito() {
		return mNumMesiDurataEsito;
	}

	public String[] getNumAnniDurataEsito() {
		return mNumAnniDurataEsito;
	}

	public String[] getCodTipoSanzione() {
		return mCodTipoSanzione;
	}

	public String[] getNumeroRate() {
		return mNumeroRate;
	}

	public BigDecimal[] getValoreRata() {
		return mValoreRata;
	}

	public BigDecimal[] getValoreUltimaRata() {
		return mValoreUltimaRata;
	}

	public Date getDataInizioPagamento() {
		return mDataInizioPagamento;
	}

	public BigDecimal getNumGiorniInizioPagamento() {
		return mNumGiorniInizioPagamento;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdRichiestaConversione(String[] aValore) {
		mIdRichiestaConversione = aValore;
	}

	public void setCodTipoRichiesta(String[] aCodTipoRichiesta) {
		mCodTipoRichiesta = aCodTipoRichiesta;
	}

	public void setNumGiorniDurataEsito(String[] aNumGiorniDurataEsito) {
		mNumGiorniDurataEsito = aNumGiorniDurataEsito;
	}

	public void setNumMesiDurataEsito(String[] aNumMesiDurataEsito) {
		mNumMesiDurataEsito = aNumMesiDurataEsito;
	}

	public void setNumAnniDurataEsito(String[] aNumAnniDurataEsito) {
		mNumAnniDurataEsito = aNumAnniDurataEsito;
	}

	public void setCodTipoSanzione(String[] aCodTipoSanzione) {
		mCodTipoSanzione = aCodTipoSanzione;
	}

	public void setNumeroRate(String[] aNumeroRate) {
		mNumeroRate = aNumeroRate;
	}

	public void setValoreRata(BigDecimal[] aValoreRata) {
		mValoreRata = aValoreRata;
	}

	public void setValoreUltimaRata(BigDecimal[] aValoreUltimaRata) {
		mValoreUltimaRata = aValoreUltimaRata;
	}

	public void setDataInizioPagamento(Date aDataInizioPagamento) {
		mDataInizioPagamento = aDataInizioPagamento;
	}

	public void setNumGiorniInizioPagamento(BigDecimal aNumGiorniInizioPagamento) {
		mNumGiorniInizioPagamento = aNumGiorniInizioPagamento;
	}

}
