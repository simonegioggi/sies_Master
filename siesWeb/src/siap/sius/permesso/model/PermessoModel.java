package siap.sius.permesso.model;

/**
* <p>Title: PermessoModel</p>
* <p>Description: Classe Model che rappresenta tutti i dati di FascicoloSius, Generale Procedimento ed Evento (legato al permesso) </p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.soggetto.model.SoggettoModel;

public class PermessoModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 7708390993738358728L;
	// Da Fascicolo SIUS.
	private BigDecimal mIdFascicoloSius;
	private BigDecimal mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private String mChiaveUfficio;
	private String mDescrTipoUfficio;
	private String mDescrComuneUfficio;
	private String mCodTipoUfficio;
	private BigDecimal mSogIdSoggetto;
	private SoggettoModel mSoggetto;

	// Da Evento.
	private BigDecimal mIdEvento;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private Date mDataEmissione;
	private String mCodEsito;
	private String mDescrEsito;

	// Da LicenzaLibAnticipata.
	private BigDecimal mNumeroGiorni;
	private String mCodStatoPermesso;
	private String mDescrStatoPermesso;
	private String mCodEsitoPermesso;
	private String mDescrEsitoPermesso;
	private BigDecimal mNumeroOre; // 07/06/2007

	private BigDecimal mNumeroGiorniNoFruiti;
	private BigDecimal mNumeroOreNoFruite;

	// COSTRUTTORE DI DEFAULT
	public PermessoModel() {
		this.mIdFascicoloSius = null;
		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mChiaveUfficio = null;
		this.mDescrTipoUfficio = null;
		this.mDescrComuneUfficio = null;
		this.mCodTipoUfficio = null;
		this.mSogIdSoggetto = null;
		this.mSoggetto = null;

		this.mIdEvento = null;
		this.mCodTipoProvvedimento = null;
		this.mDescrTipoProvvedimento = null;
		this.mDataEmissione = null;
		this.mCodEsito = null;
		this.mDescrEsito = null;

		this.mNumeroGiorni = null;
		this.mCodStatoPermesso = "";
		this.mDescrStatoPermesso = "";
		this.mCodEsitoPermesso = "";
		this.mDescrEsitoPermesso = "";
		this.mNumeroOre = null;
		this.mNumeroGiorniNoFruiti = null;
		this.mNumeroOreNoFruite = null;
	}

	// COSTRUTTORE DI COPIA
	public PermessoModel(PermessoModel aModel) {
		this.mIdFascicoloSius = aModel.mIdFascicoloSius;
		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mChiaveProgr = aModel.mChiaveProgr;
		this.mChiaveUfficio = aModel.mChiaveUfficio;
		this.mDescrTipoUfficio = aModel.mDescrTipoUfficio;
		this.mDescrComuneUfficio = aModel.mDescrComuneUfficio;
		this.mCodTipoUfficio = aModel.mCodTipoUfficio;
		this.mSogIdSoggetto = aModel.mSogIdSoggetto;
		this.mSoggetto = aModel.mSoggetto;

		this.mIdEvento = aModel.mIdEvento;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;

		this.mNumeroGiorni = aModel.mNumeroGiorni;
		this.mCodStatoPermesso = aModel.mCodStatoPermesso;
		this.mDescrStatoPermesso = aModel.mDescrStatoPermesso;
		this.mCodEsitoPermesso = aModel.mCodEsitoPermesso;
		this.mDescrEsitoPermesso = aModel.mDescrEsitoPermesso;
		this.mNumeroOre = aModel.mNumeroOre;
		this.mNumeroGiorniNoFruiti = aModel.mNumeroGiorniNoFruiti;
		this.mNumeroOreNoFruite = aModel.mNumeroOreNoFruite;
	}

	// COSTRUTTORE MODEL
	public PermessoModel(BigDecimal aIdFascicoloSius, BigDecimal aChiaveAnno, BigDecimal aChiaveProgr,
			String aChiaveUfficio, String aDescrTipoUfficio, String aDescrComuneUfficio,
			String aCodTipoUfficio, BigDecimal aSogIdSoggetto, SoggettoModel aSoggetto, BigDecimal aIdEvento,
			String aCodTipoProvvedimento, String aDescrTipoProvvedimento, Date aDataEmissione,
			String aCodEsito, String aDescrEsito, BigDecimal aNumeroGiorni, String aCodStatoPermesso,
			String aDescrStatoPermesso, String aCodEsitoPermesso, String aDescrEsitoPermesso,
			BigDecimal aNumeroOre, BigDecimal aNumeroGiorniNoFruiti, BigDecimal aNumeroOreNoFruite) {
		this.mIdFascicoloSius = aIdFascicoloSius;
		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveProgr = aChiaveProgr;
		this.mChiaveUfficio = aChiaveUfficio;
		this.mDescrTipoUfficio = aDescrTipoUfficio;
		this.mDescrComuneUfficio = aDescrComuneUfficio;
		this.mCodTipoUfficio = aCodTipoUfficio;
		this.mSogIdSoggetto = aSogIdSoggetto;
		this.mSoggetto = aSoggetto;
		this.mIdEvento = aIdEvento;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mDataEmissione = aDataEmissione;
		this.mCodEsito = aCodEsito;
		this.mDescrEsito = aDescrEsito;
		this.mNumeroGiorni = aNumeroGiorni;
		this.mCodStatoPermesso = aCodStatoPermesso;
		this.mDescrStatoPermesso = aDescrStatoPermesso;
		this.mCodEsitoPermesso = aCodEsitoPermesso;
		this.mDescrEsitoPermesso = aDescrEsitoPermesso;
		this.mNumeroOre = aNumeroOre;
		this.mNumeroGiorniNoFruiti = aNumeroGiorniNoFruiti;
		this.mNumeroOreNoFruite = aNumeroOreNoFruite;

	}

	//
	// METODI GET()
	//
	public BigDecimal getIdFascicoloSius() {
		return mIdFascicoloSius;
	}

	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public String getChiaveUfficio() {
		return mChiaveUfficio;
	}

	public String getDescrTipoUfficio() {
		return mDescrTipoUfficio;
	}

	public String getDescrComuneUfficio() {
		return mDescrComuneUfficio;
	}

	public String getCodTipoUfficio() {
		return mCodTipoUfficio;
	}

	public BigDecimal getSogIdSoggetto() {
		return mSogIdSoggetto;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public BigDecimal getIdEvento() {
		return mIdEvento;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public BigDecimal getNumeroGiorni() {
		return mNumeroGiorni;
	}

	public String getCodStatoPermesso() {
		return mCodStatoPermesso;
	}

	public String getDescrStatoPermesso() {
		return mDescrStatoPermesso;
	}

	public String getCodEsitoPermesso() {
		return mCodEsitoPermesso;
	}

	public String getDescrEsitoPermesso() {
		return mDescrEsitoPermesso;
	}

	public BigDecimal getNumeroOre() {
		return mNumeroOre;
	}

	public BigDecimal getNumeroGiorniNoFruiti() {
		return mNumeroGiorniNoFruiti;
	}

	public BigDecimal getNumeroOreNoFruite() {
		return mNumeroOreNoFruite;
	}

	//
	// METODI SET()
	//
	public void setIdFascicoloSius(BigDecimal aValore) {
		mIdFascicoloSius = aValore;
	}

	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setChiaveUfficio(String aValore) {
		mChiaveUfficio = aValore;
	}

	public void setDescrTipoUfficio(String aValore) {
		mDescrTipoUfficio = aValore;
	}

	public void setDescrComuneUfficio(String aValore) {
		mDescrComuneUfficio = aValore;
	}

	public void setCodTipoUfficio(String aValore) {
		mCodTipoUfficio = aValore;
	}

	public void setSogIdSoggetto(BigDecimal aValore) {
		mSogIdSoggetto = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setIdEvento(BigDecimal aValore) {
		mIdEvento = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setNumeroGiorni(BigDecimal aValore) {
		mNumeroGiorni = aValore;
	}

	public void setCodStatoPermesso(String aValore) {
		mCodStatoPermesso = aValore;
	}

	public void setDescrStatoPermesso(String aValore) {
		mDescrStatoPermesso = aValore;
	}

	public void setCodEsitoPermesso(String aValore) {
		mCodEsitoPermesso = aValore;
	}

	public void setDescrEsitoPermesso(String aValore) {
		mDescrEsitoPermesso = aValore;
	}

	public void setNumeroOre(BigDecimal aValore) {
		mNumeroOre = aValore;
	} // 07/06/2007

	public void setNumeroGiorniNoFruiti(BigDecimal aValore) {
		mNumeroGiorniNoFruiti = aValore;
	}

	public void setNumeroOreNoFruite(BigDecimal aValore) {
		mNumeroOreNoFruite = aValore;
	}
}