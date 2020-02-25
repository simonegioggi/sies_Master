package siap.siep.beneficio.model;

/**
* <p>Title: BeneficioModel</p>
* <p>Description: Classe Model che rappresenta il Beneficio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.calendar.model.CalendarModel;
import f3b.model.GenericModel;

public class BeneficioModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -7547133956166925595L;

	private BigDecimal mIdBeneficio;
	private String mCodNaturaBeneficio;
	private String mDescrNaturaBeneficio;
	private String mCodTipoBeneficio;
	private String mDescrTipoBeneficio;
	private String mCodTipoSospSubordinata;
	private String mDescrTipoSospSubordinata;
	private BigDecimal mNumAnniReclusione;
	private BigDecimal mNumMesiReclusione;
	private BigDecimal mNumGiorniReclusione;
	private BigDecimal mImportoMulta;
	private BigDecimal mNumAnniArresto;
	private BigDecimal mNumMesiArresto;
	private BigDecimal mNumGiorniArresto;
	private BigDecimal mImportoAmmenda;
	private String mCodDpr;
	private String mDescrDpr;
	private String mNote;
	private String mInapplicabilitaMisSicurezza;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mEveIdEvento;
	private String mStringaReclusione;
	private String mStringaArresto;

	private String mCodSottotipoBeneficio;
	private String mDescrSottotipoBeneficio;

	private BigDecimal mNumAnniSospensione;
	private BigDecimal mNumGiorniPrestazione;
	private BigDecimal mNumMesiPrestazione;
	private BigDecimal mNumOreSettimanali;
	private String mFlagFrequenzaSettimanale;
	private BigDecimal mRifIdProvvedimento;
	private String mRifCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private Date mRifDataProvvedimento;
	private String mRifCodTipoAutoEmittente;
	private String mRifCodLuogoAutoEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mDescrLuogoAutoritaEmittente;
	private String mRifNumSezioneAutoEmittente;
	private BigDecimal mRifAnnoProvvedimento;
	private String mRifNumeroProvvedimento;
	private BigDecimal mNumAnniAdempimento;
	private BigDecimal mNumMesiAdempimento;
	private BigDecimal mNumGiorniAdempimento;
	private BigDecimal mBenIdBeneficio;
	private String mStringaAdempimento;
	private Date mRifDataIrrevocabilita;

	// COSTRUTTORE DI DEFAULT
	public BeneficioModel() {
		this.mIdBeneficio = null;
		this.mCodNaturaBeneficio = "";
		this.mDescrNaturaBeneficio = "";
		this.mCodTipoBeneficio = "";
		this.mDescrTipoBeneficio = "";
		this.mCodTipoSospSubordinata = "";
		this.mDescrTipoSospSubordinata = "";
		this.mNumAnniReclusione = null;
		this.mNumMesiReclusione = null;
		this.mNumGiorniReclusione = null;
		this.mImportoMulta = null;
		this.mNumAnniArresto = null;
		this.mNumMesiArresto = null;
		this.mNumGiorniArresto = null;
		this.mImportoAmmenda = null;
		this.mCodDpr = "";
		this.mDescrDpr = "";
		this.mNote = "";
		this.mInapplicabilitaMisSicurezza = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mEveIdEvento = null;

		this.mCodSottotipoBeneficio = "";
		this.mDescrSottotipoBeneficio = "";
		this.mNumAnniSospensione = null;
		this.mNumGiorniPrestazione = null;
		this.mNumMesiPrestazione = null;
		this.mNumOreSettimanali = null;
		this.mFlagFrequenzaSettimanale = "";
		this.mRifIdProvvedimento = null;
		this.mRifCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";
		this.mRifDataProvvedimento = null;
		this.mRifCodTipoAutoEmittente = "";
		this.mRifCodLuogoAutoEmittente = "";
		this.mDescrTipoAutoritaEmittente = "";
		this.mDescrLuogoAutoritaEmittente = "";
		this.mRifNumSezioneAutoEmittente = "";
		this.mRifAnnoProvvedimento = null;
		this.mRifNumeroProvvedimento = "";

		this.mNumAnniAdempimento = null;
		this.mNumMesiAdempimento = null;
		this.mNumGiorniAdempimento = null;
		this.mBenIdBeneficio = null;
		this.mRifDataIrrevocabilita = null;

	}

	// COSTRUTTORE DI COPIA
	public BeneficioModel(BeneficioModel aModel) {
		this.mIdBeneficio = aModel.mIdBeneficio;
		this.mCodNaturaBeneficio = aModel.mCodNaturaBeneficio;
		this.mDescrNaturaBeneficio = aModel.mDescrNaturaBeneficio;
		this.mCodTipoBeneficio = aModel.mCodTipoBeneficio;
		this.mDescrTipoBeneficio = aModel.mDescrTipoBeneficio;
		this.mCodTipoSospSubordinata = aModel.mCodTipoSospSubordinata;
		this.mDescrTipoSospSubordinata = aModel.mDescrTipoSospSubordinata;
		this.mNumAnniReclusione = aModel.mNumAnniReclusione;
		this.mNumMesiReclusione = aModel.mNumMesiReclusione;
		this.mNumGiorniReclusione = aModel.mNumGiorniReclusione;
		this.mImportoMulta = aModel.mImportoMulta;
		this.mNumAnniArresto = aModel.mNumAnniArresto;
		this.mNumMesiArresto = aModel.mNumMesiArresto;
		this.mNumGiorniArresto = aModel.mNumGiorniArresto;
		this.mImportoAmmenda = aModel.mImportoAmmenda;
		this.mCodDpr = aModel.mCodDpr;
		this.mDescrDpr = aModel.mDescrDpr;
		this.mNote = aModel.mNote;
		this.mInapplicabilitaMisSicurezza = aModel.mInapplicabilitaMisSicurezza;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mEveIdEvento = aModel.mEveIdEvento;

		this.mCodSottotipoBeneficio = aModel.mCodSottotipoBeneficio;
		this.mDescrSottotipoBeneficio = aModel.mDescrSottotipoBeneficio;
		this.mNumAnniSospensione = aModel.mNumAnniSospensione;
		this.mNumGiorniPrestazione = aModel.mNumGiorniPrestazione;
		this.mNumMesiPrestazione = aModel.mNumMesiPrestazione;
		this.mNumOreSettimanali = aModel.mNumOreSettimanali;
		this.mFlagFrequenzaSettimanale = aModel.mFlagFrequenzaSettimanale;
		this.mRifIdProvvedimento = aModel.mRifIdProvvedimento;
		this.mRifCodTipoProvvedimento = aModel.mRifCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mRifDataProvvedimento = aModel.mRifDataProvvedimento;
		this.mRifCodTipoAutoEmittente = aModel.mRifCodTipoAutoEmittente;
		this.mRifCodLuogoAutoEmittente = aModel.mRifCodLuogoAutoEmittente;
		this.mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		this.mDescrLuogoAutoritaEmittente = aModel.mDescrLuogoAutoritaEmittente;
		this.mRifNumSezioneAutoEmittente = aModel.mRifNumSezioneAutoEmittente;
		this.mRifAnnoProvvedimento = aModel.mRifAnnoProvvedimento;
		this.mRifNumeroProvvedimento = aModel.mRifNumeroProvvedimento;

		this.mNumAnniAdempimento = aModel.mNumAnniAdempimento;
		this.mNumMesiAdempimento = aModel.mNumMesiAdempimento;
		this.mNumGiorniAdempimento = aModel.mNumGiorniAdempimento;
		this.mBenIdBeneficio = aModel.mBenIdBeneficio;
		this.mRifDataIrrevocabilita = aModel.mRifDataIrrevocabilita;
	}

	// COSTRUTTORE MODEL
	public BeneficioModel(BigDecimal aIdBeneficio, String aCodNaturaBeneficio, String aDescrNaturaBeneficio,
			String aCodTipoBeneficio, String aDescrTipoBeneficio, String aCodTipoSospSubordinata,
			String aDescrTipoSospSubordinata, BigDecimal aNumAnniReclusione, BigDecimal aNumMesiReclusione,
			BigDecimal aNumGiorniReclusione, BigDecimal aImportoMulta, BigDecimal aNumAnniArresto,
			BigDecimal aNumMesiArresto, BigDecimal aNumGiorniArresto, BigDecimal aImportoAmmenda,
			String aCodDpr, String aDescrDpr, String aNote, String aInapplicabilitaMisSicurezza,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aEveIdEvento, String aCodSottotipoBeneficio,
			String aDescrSottotipoBeneficio, BigDecimal aNumAnniSospensione, BigDecimal aNumGiorniPrestazione,
			BigDecimal aNumMesiPrestazione, BigDecimal aNumOreSettimanali, String aFlagFrequenzaSettimanale,
			BigDecimal aRifIdProvvedimento, String aRifCodTipoProvvedimento, String aDescrTipoProvvedimento,
			Date aRifDataProvvedimento, String aRifCodTipoAutoEmittente, String aRifCodLuogoAutoEmittente,
			String aDescrTipoAutoritaEmittente, String aDescrLuogoAutoritaEmittente,
			String aRifNumSezioneAutoEmittente, BigDecimal aRifAnnoProvvedimento,
			String aRifNumeroProvvedimento, BigDecimal aNumAnniAdempimento, BigDecimal aNumMesiAdempimento,
			BigDecimal aNumGiorniAdempimento, BigDecimal aBenIdBeneficio, Date aRifDataIrrevocabilita) {
		this.mIdBeneficio = aIdBeneficio;
		this.mCodNaturaBeneficio = aCodNaturaBeneficio;
		this.mDescrNaturaBeneficio = aDescrNaturaBeneficio;
		this.mCodTipoBeneficio = aCodTipoBeneficio;
		this.mDescrTipoBeneficio = aDescrTipoBeneficio;
		this.mCodTipoSospSubordinata = aCodTipoSospSubordinata;
		this.mDescrTipoSospSubordinata = aDescrTipoSospSubordinata;
		this.mNumAnniReclusione = aNumAnniReclusione;
		this.mNumMesiReclusione = aNumMesiReclusione;
		this.mNumGiorniReclusione = aNumGiorniReclusione;
		this.mImportoMulta = aImportoMulta;
		this.mNumAnniArresto = aNumAnniArresto;
		this.mNumMesiArresto = aNumMesiArresto;
		this.mNumGiorniArresto = aNumGiorniArresto;
		this.mImportoAmmenda = aImportoAmmenda;
		this.mCodDpr = aCodDpr;
		this.mDescrDpr = aDescrDpr;
		this.mNote = aNote;
		this.mInapplicabilitaMisSicurezza = aInapplicabilitaMisSicurezza;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mEveIdEvento = aEveIdEvento;

		this.mCodSottotipoBeneficio = aCodSottotipoBeneficio;
		this.mDescrSottotipoBeneficio = aDescrSottotipoBeneficio;
		this.mNumAnniSospensione = aNumAnniSospensione;
		this.mNumGiorniPrestazione = aNumGiorniPrestazione;
		this.mNumMesiPrestazione = aNumMesiPrestazione;
		this.mNumOreSettimanali = aNumOreSettimanali;
		this.mFlagFrequenzaSettimanale = aFlagFrequenzaSettimanale;

		this.mRifIdProvvedimento = aRifIdProvvedimento;
		this.mRifCodTipoProvvedimento = aRifCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mRifDataProvvedimento = aRifDataProvvedimento;
		this.mRifCodTipoAutoEmittente = aRifCodTipoAutoEmittente;
		this.mRifCodLuogoAutoEmittente = aRifCodLuogoAutoEmittente;
		this.mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		this.mDescrLuogoAutoritaEmittente = aDescrLuogoAutoritaEmittente;
		this.mRifNumSezioneAutoEmittente = aRifNumSezioneAutoEmittente;
		this.mRifAnnoProvvedimento = aRifAnnoProvvedimento;
		this.mRifNumeroProvvedimento = aRifNumeroProvvedimento;

		this.mNumAnniAdempimento = aNumAnniAdempimento;
		this.mNumMesiAdempimento = aNumMesiAdempimento;
		this.mNumGiorniAdempimento = aNumGiorniAdempimento;
		this.mBenIdBeneficio = aBenIdBeneficio;
		this.mRifDataIrrevocabilita = aRifDataIrrevocabilita;

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdBeneficio() {
		return mIdBeneficio;
	}

	public String getCodNaturaBeneficio() {
		return mCodNaturaBeneficio;
	}

	public String getDescrNaturaBeneficio() {
		return mDescrNaturaBeneficio;
	}

	public String getCodTipoBeneficio() {
		return mCodTipoBeneficio;
	}

	public String getDescrTipoBeneficio() {
		return mDescrTipoBeneficio;
	}

	public String getCodTipoSospSubordinata() {
		return mCodTipoSospSubordinata;
	}

	public String getDescrTipoSospSubordinata() {
		return mDescrTipoSospSubordinata;
	}

	public BigDecimal getNumAnniReclusione() {
		return mNumAnniReclusione;
	}

	public BigDecimal getNumMesiReclusione() {
		return mNumMesiReclusione;
	}

	public BigDecimal getNumGiorniReclusione() {
		return mNumGiorniReclusione;
	}

	public BigDecimal getImportoMulta() {
		return mImportoMulta;
	}

	public BigDecimal getNumAnniArresto() {
		return mNumAnniArresto;
	}

	public BigDecimal getNumMesiArresto() {
		return mNumMesiArresto;
	}

	public BigDecimal getNumGiorniArresto() {
		return mNumGiorniArresto;
	}

	public BigDecimal getImportoAmmenda() {
		return mImportoAmmenda;
	}

	public String getCodDpr() {
		return mCodDpr;
	}

	public String getDescrDpr() {
		return mDescrDpr;
	}

	public String getNote() {
		return mNote;
	}

	public String getInapplicabilitaMisSicurezza() {
		return mInapplicabilitaMisSicurezza;
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

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public String getStringaArresto() {
		return mStringaArresto;
	}

	public String getCodSottotipoBeneficio() {
		return mCodSottotipoBeneficio;
	}

	public String getDescrSottotipoBeneficio() {
		return mDescrSottotipoBeneficio;
	}

	public BigDecimal getNumAnniSospensione() {
		return mNumAnniSospensione;
	}

	public BigDecimal getNumGiorniPrestazione() {
		return mNumGiorniPrestazione;
	}

	public BigDecimal getNumMesiPrestazione() {
		return mNumMesiPrestazione;
	}

	public BigDecimal getNumOreSettimanali() {
		return mNumOreSettimanali;
	}

	public String getFlagFrequenzaSettimanale() {
		return mFlagFrequenzaSettimanale;
	}

	public BigDecimal getRifIdProvvedimento() {
		return mRifIdProvvedimento;
	}

	public String getRifCodTipoProvvedimento() {
		return mRifCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public Date getRifDataProvvedimento() {
		return mRifDataProvvedimento;
	}

	public String getRifCodTipoAutoEmittente() {
		return mRifCodTipoAutoEmittente;
	}

	public String getRifCodLuogoAutoEmittente() {
		return mRifCodLuogoAutoEmittente;
	}

	public String getDescrTipoAutoritaEmittente() {
		return mDescrTipoAutoritaEmittente;
	}

	public String getDescrLuogoAutoritaEmittente() {
		return mDescrLuogoAutoritaEmittente;
	}

	public String getRifNumSezioneAutoEmittente() {
		return mRifNumSezioneAutoEmittente;
	}

	public BigDecimal getRifAnnoProvvedimento() {
		return mRifAnnoProvvedimento;
	}

	public String getRifNumeroProvvedimento() {
		return mRifNumeroProvvedimento;
	}

	public BigDecimal getNumAnniAdempimento() {
		return mNumAnniAdempimento;
	}

	public BigDecimal getNumMesiAdempimento() {
		return mNumMesiAdempimento;
	}

	public BigDecimal getNumGiorniAdempimento() {
		return mNumGiorniAdempimento;
	}

	public BigDecimal getBenIdBeneficio() {
		return mBenIdBeneficio;
	}

	public String getStringaAdempimento() {
		return mStringaAdempimento;
	}

	public Date getRifDataIrrevocabilita() {
		return mRifDataIrrevocabilita;
	}

	//
	// METODI SET()
	//

	public void setIdBeneficio(BigDecimal aValore) {
		mIdBeneficio = aValore;
	}

	public void setCodNaturaBeneficio(String aValore) {
		mCodNaturaBeneficio = aValore;
	}

	public void setDescrNaturaBeneficio(String aValore) {
		mDescrNaturaBeneficio = aValore;
	}

	public void setCodTipoBeneficio(String aValore) {
		mCodTipoBeneficio = aValore;
	}

	public void setDescrTipoBeneficio(String aValore) {
		mDescrTipoBeneficio = aValore;
	}

	public void setCodTipoSospSubordinata(String aValore) {
		mCodTipoSospSubordinata = aValore;
	}

	public void setDescrTipoSospSubordinata(String aValore) {
		mDescrTipoSospSubordinata = aValore;
	}

	public void setNumAnniReclusione(BigDecimal aValore) {
		mNumAnniReclusione = aValore;
	}

	public void setNumMesiReclusione(BigDecimal aValore) {
		mNumMesiReclusione = aValore;
	}

	public void setNumGiorniReclusione(BigDecimal aValore) {
		mNumGiorniReclusione = aValore;
	}

	public void setImportoMulta(BigDecimal aValore) {
		mImportoMulta = aValore;
	}

	public void setNumAnniArresto(BigDecimal aValore) {
		mNumAnniArresto = aValore;
	}

	public void setNumMesiArresto(BigDecimal aValore) {
		mNumMesiArresto = aValore;
	}

	public void setNumGiorniArresto(BigDecimal aValore) {
		mNumGiorniArresto = aValore;
	}

	public void setImportoAmmenda(BigDecimal aValore) {
		mImportoAmmenda = aValore;
	}

	public void setCodDpr(String aValore) {
		mCodDpr = aValore;
	}

	public void setDescrDpr(String aValore) {
		mDescrDpr = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setInapplicabilitaMisSicurezza(String aValore) {
		mInapplicabilitaMisSicurezza = aValore;
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

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public void setCodSottotipoBeneficio(String aValore) {
		mCodSottotipoBeneficio = aValore;
	}

	public void setDescrSottotipoBeneficio(String aValore) {
		mDescrSottotipoBeneficio = aValore;
	}

	public void setNumAnniSospensione(BigDecimal aValore) {
		mNumAnniSospensione = aValore;
	}

	public void setNumGiorniPrestazione(BigDecimal aValore) {
		mNumGiorniPrestazione = aValore;
	}

	public void setNumMesiPrestazione(BigDecimal aValore) {
		mNumMesiPrestazione = aValore;
	}

	public void setNumOreSettimanali(BigDecimal aValore) {
		mNumOreSettimanali = aValore;
	}

	public void setFlagFrequenzaSettimanale(String aValore) {
		mFlagFrequenzaSettimanale = aValore;
	}

	public void setRifIdProvvedimento(BigDecimal aValore) {
		mRifIdProvvedimento = aValore;
	}

	public void setRifCodTipoProvvedimento(String aValore) {
		mRifCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setRifDataProvvedimento(Date aValore) {
		mRifDataProvvedimento = aValore;
	}

	public void setRifCodTipoAutoEmittente(String aValore) {
		mRifCodTipoAutoEmittente = aValore;
	}

	public void setRifCodLuogoAutoEmittente(String aValore) {
		mRifCodLuogoAutoEmittente = aValore;
	}

	public void setDescrTipoAutoritaEmittente(String aValore) {
		mDescrTipoAutoritaEmittente = aValore;
	}

	public void setDescrLuogoAutoritaEmittente(String aValore) {
		mDescrLuogoAutoritaEmittente = aValore;
	}

	public void setRifNumSezioneAutoEmittente(String aValore) {
		mRifNumSezioneAutoEmittente = aValore;
	}

	public void setRifAnnoProvvedimento(BigDecimal aValore) {
		mRifAnnoProvvedimento = aValore;
	}

	public void setRifNumeroProvvedimento(String aValore) {
		mRifNumeroProvvedimento = aValore;
	}

	public void setNumAnniAdempimento(BigDecimal aValore) {
		mNumAnniAdempimento = aValore;
	}

	public void setNumMesiAdempimento(BigDecimal aValore) {
		mNumMesiAdempimento = aValore;
	}

	public void setNumGiorniAdempimento(BigDecimal aValore) {
		mNumGiorniAdempimento = aValore;
	}

	public void setStringaAdempimento(String aValore) {
		mStringaAdempimento = aValore;
	}

	public void setBenIdBeneficio(BigDecimal aValore) {
		mBenIdBeneficio = aValore;
	}

	public void setRifDataIrrevocabilita(Date aValore) {
		mRifDataIrrevocabilita = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdBeneficio + " - " + mCodNaturaBeneficio + " - " + mDescrNaturaBeneficio + " - "
				+ mCodTipoBeneficio + " - " + mDescrTipoBeneficio + " - " + mCodTipoSospSubordinata + " - "
				+ mDescrTipoSospSubordinata + " - " + mNumAnniReclusione + " - " + mNumMesiReclusione + " - "
				+ mNumGiorniReclusione + " - " + mImportoMulta + " - " + mNumAnniArresto + " - "
				+ mNumMesiArresto + " - " + mNumGiorniArresto + " - " + mImportoAmmenda + " - " + mCodDpr
				+ " - " + mDescrDpr + " - " + mNote + " - " + mInapplicabilitaMisSicurezza + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mFasSieIdFascicoloSiep + " - " + mEveIdEvento + " - " +

				mCodSottotipoBeneficio + " - " + mDescrSottotipoBeneficio + " - " + mNumAnniSospensione
				+ " - " + mNumGiorniPrestazione + " - " + mNumMesiPrestazione + " - " + mNumOreSettimanali
				+ " - " + mFlagFrequenzaSettimanale + " - " + mRifIdProvvedimento + " - "
				+ mRifCodTipoProvvedimento + " - " + mDescrTipoProvvedimento + " - " + mRifDataProvvedimento
				+ " - " + mRifCodTipoAutoEmittente + " - " + mRifCodLuogoAutoEmittente + " - "
				+ mDescrTipoAutoritaEmittente + " - " + mDescrLuogoAutoritaEmittente + " - "
				+ mRifNumSezioneAutoEmittente + " - " + mRifAnnoProvvedimento + " - "
				+ mRifNumeroProvvedimento + " - " +

				mNumAnniAdempimento + " - " + mNumMesiAdempimento + " - " + mNumGiorniAdempimento + " - "
				+ mBenIdBeneficio + " - " + mRifDataIrrevocabilita;

		return lStr;
	}

	/**
	 * calcolaStringaAdempimento per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaAdempimento() {
		String lStringAdempimento = "";
		if (this.getNumAnniAdempimento() != null) {
			if (this.getNumAnniAdempimento().intValue() != 0)
				lStringAdempimento = "Anni " + this.getNumAnniAdempimento();
		}
		if (this.getNumMesiAdempimento() != null) {
			if (this.getNumMesiAdempimento().intValue() != 0)
				lStringAdempimento += " Mesi " + this.getNumMesiAdempimento();
		}
		if (this.getNumGiorniAdempimento() != null) {
			if (this.getNumGiorniAdempimento().intValue() != 0)
				lStringAdempimento += " Giorni " + this.getNumGiorniAdempimento();
		}

		if (lStringAdempimento.length() > 1)
			this.mStringaAdempimento = lStringAdempimento;
		else
			this.mStringaAdempimento = null;
	}

	/**
	 * calcolaStringaArresto per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaArresto() {
		String lStringArresto = "";
		if (this.getNumAnniArresto() != null) {
			if (this.getNumAnniArresto().intValue() != 0)
				lStringArresto = "Anni " + this.getNumAnniArresto();
		}
		if (this.getNumMesiArresto() != null) {
			if (this.getNumMesiArresto().intValue() != 0)
				lStringArresto += " Mesi " + this.getNumMesiArresto();
		}
		if (this.getNumGiorniArresto() != null) {
			if (this.getNumGiorniArresto().intValue() != 0)
				lStringArresto += " Giorni " + this.getNumGiorniArresto();
		}

		if (lStringArresto.length() > 1)
			this.mStringaArresto = lStringArresto;
		else
			this.mStringaArresto = null;
	}

	/**
	 * calcolaStringaReclusione per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaReclusione() {
		String lStringReclusione = "";
		if (this.getNumAnniReclusione() != null) {
			if (this.getNumAnniReclusione().intValue() != 0)
				lStringReclusione = "Anni " + this.getNumAnniReclusione();
		}
		if (this.getNumMesiReclusione() != null) {
			if (this.getNumMesiReclusione().intValue() != 0)
				lStringReclusione += " Mesi " + this.getNumMesiReclusione();
		}
		if (this.getNumGiorniReclusione() != null) {
			if (this.getNumGiorniReclusione().intValue() != 0)
				lStringReclusione += " Giorni " + this.getNumGiorniReclusione();
		}

		if (lStringReclusione.length() > 1)
			this.mStringaReclusione = lStringReclusione;
		else
			this.mStringaReclusione = null;
	}

	public CalendarModel getQuantumReclusione() {
		CalendarModel lCalReclusione = new CalendarModel();

		lCalReclusione.setNumGiorni(this.mNumGiorniReclusione);
		lCalReclusione.setNumMesi(this.mNumMesiReclusione);
		lCalReclusione.setNumAnni(this.mNumAnniReclusione);
		if (this.mImportoMulta != null) {
			lCalReclusione.setImportoMulta(this.mImportoMulta.doubleValue());
		} else {
			lCalReclusione.setImportoMulta(0);
		}
		return lCalReclusione;
	}

	/**
	 * 
	 */
	public CalendarModel getQuantumArresto() {
		CalendarModel lCalArresto = new CalendarModel();

		lCalArresto.setNumGiorni(this.mNumGiorniArresto);
		lCalArresto.setNumMesi(this.mNumMesiArresto);
		lCalArresto.setNumAnni(this.mNumAnniArresto);
		if (this.mImportoAmmenda != null) {
			lCalArresto.setImportoAmmenda(this.mImportoAmmenda.doubleValue());
		} else {
			lCalArresto.setImportoAmmenda(0);
		}

		return lCalArresto;
	}

}