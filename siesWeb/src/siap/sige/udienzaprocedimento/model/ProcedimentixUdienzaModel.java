package siap.sige.udienzaprocedimento.model;

/**
 * <p>Title: ProcedimentixUdienzaModel</p>
 * <p>Description:
 * Classe Model che rappresenta il ProcedimentixUdienzaModel</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.tenore.model.TenoreSigeModel;

public class ProcedimentixUdienzaModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1787101079806709190L;
	// Udienza
	private BigDecimal mIdUdienza;
	private Integer mNumCollegio;

	// Fascicolo SIUS
	private BigDecimal mIdFasSIGE;
	private BigDecimal mChiaveAnnoFasSIGE;
	private BigDecimal mChiaveProgrFasSIGE;
	private String mCodStatoFasSIGE;
	private String mDescrStatoFasSIGE;

	// Soggetto
	private BigDecimal mIdSoggetto;
	private String mCognomeSog;
	private String mNomeSog;
	private Date mDataNascitaSog;
	private String mCodComuneNascitaSog;
	private String mDescrComuneNascitaSog;
	private String mDescrComuneNascitaEsteroSog;

	// Generale Procedimento
	// private BigDecimal mIdGeneraleProcedimento;
	private String mCodOggettoProcedimento;
	private String mDescrOggettoProcedimento;
	private String mSezioneProcedimento;

	// Oggetti per procedimento
	private String[] mCodOggettiProcedimento;
	private String[] mDescrOggettiProcedimento;

	// Udienza Procedimento
	private String mFlagRinviata;
	private String mEsitoProvvedimento;
	private String mMotivoProvvedimento;
	private String mDescStatoUdienza;
	private String mDescPosizioneGiuridicaSige;
	private String mDescPosizioneGiuridica;

	// Evento
	private BigDecimal mIdEvento;
	private String mCodEsito;

	// Magistrato
	private String mCognomeMagistrato;
	private String mNomeMagistrato;
	private String mCodMagistrato;

	// Avvocato difensore
	private BigDecimal mIdAvvocato;
	private String mCognomeAvvocato;
	private String mNomeAvvocato;
	private String mDescrTipoAvvocato;

	// Esperto in funzione di Magistrato Relatore
	private BigDecimal mIdEsperto;

	// 2011-01-10
	// Oggetti aggregati d'uso per la generazione xml per stampe
	// Nota : Verificare se normalizzare, anche le view in jsp
	private FascicoloSigeModel mFascicoloSige;
	private SoggettoModel mSoggetto;
	private MagistratoModel mMagistrato;
	private AvvocatoModel mAvvocato;
	private TenoreSigeModel[] mTenori;
	// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
	// variabile introdotta per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018 (errore
	// prototipo PALERMO)
	private AvvocatoModel[] mAvvocati;

  // Procuratore 
  private String      mDescrProcuratore; 
  // Cancelliere
  private String      mDescrIdAssistente;

  
  
	// COSTRUTTORE DI DEFAULT
	public ProcedimentixUdienzaModel() {
		this.mIdUdienza = null;
		this.mNumCollegio = null;
		this.mIdFasSIGE = null;
		this.mChiaveAnnoFasSIGE = null;
		this.mChiaveProgrFasSIGE = null;
		this.mCodStatoFasSIGE = "";
		this.mDescrStatoFasSIGE = "";
		this.mIdSoggetto = null;
		this.mCognomeSog = "";
		this.mNomeSog = "";
		this.mDataNascitaSog = null;
		this.mCodComuneNascitaSog = "";
		this.mDescrComuneNascitaSog = "";
		this.mDescrComuneNascitaEsteroSog = "";
		// this.mIdGeneraleProcedimento = null;
		this.mCodOggettoProcedimento = "";
		this.mDescrOggettoProcedimento = "";

		this.mCodOggettiProcedimento = null;
		this.mDescrOggettiProcedimento = null;

		this.mSezioneProcedimento = "";
		this.mFlagRinviata = "";
		this.mEsitoProvvedimento = "";
		this.mMotivoProvvedimento = "";
		this.mDescStatoUdienza = "";
		this.mDescPosizioneGiuridicaSige = "";
		this.mDescPosizioneGiuridica = "";
		this.mIdEvento = null;
		this.mCodEsito = "";
		this.mCognomeMagistrato = "";
		this.mNomeMagistrato = "";
		this.mCodMagistrato = "";
		this.mIdEsperto = null;

		// 2011-01-10
		// Oggetti aggregati d'uso per la generazione xml per stampe
		// Nota : Verificare se normalizzare, anche le view in jsp
		this.mFascicoloSige = new FascicoloSigeModel();
		this.mSoggetto = new SoggettoModel();
		this.mMagistrato = new MagistratoModel();
		this.mAvvocato = new AvvocatoModel();
		this.mTenori = new TenoreSigeModel[0];
		// 20180109 [EC] metodo introdotto per problematica inviata tramite email Maffucci/Alfieri del
		// 02/01/2018 (errore prototipo PALERMO)
		this.mAvvocati = new AvvocatoModel[0];
  //interventi epr sies 11.2.1
    this.mDescrProcuratore = "";
    this.mDescrIdAssistente = "";

	}

	// COSTRUTTORE DI COPIA
	public ProcedimentixUdienzaModel(ProcedimentixUdienzaModel aModel) {
		this.mIdUdienza = aModel.mIdUdienza;
		this.mNumCollegio = aModel.mNumCollegio;
		this.mIdFasSIGE = aModel.mIdFasSIGE;
		this.mChiaveAnnoFasSIGE = aModel.mChiaveAnnoFasSIGE;
		this.mChiaveProgrFasSIGE = aModel.mChiaveProgrFasSIGE;
		this.mCodStatoFasSIGE = aModel.mCodStatoFasSIGE;
		this.mDescrStatoFasSIGE = aModel.mDescrStatoFasSIGE;
		this.mIdSoggetto = aModel.mIdSoggetto;
		this.mCognomeSog = aModel.mCognomeSog;
		this.mNomeSog = aModel.mNomeSog;
		this.mDataNascitaSog = aModel.mDataNascitaSog;
		this.mCodComuneNascitaSog = aModel.mCodComuneNascitaSog;
		this.mDescrComuneNascitaSog = aModel.mDescrComuneNascitaSog;
		this.mDescrComuneNascitaEsteroSog = aModel.mDescrComuneNascitaEsteroSog;
		// this.mIdGeneraleProcedimento = aModel.mIdGeneraleProcedimento;
		this.mCodOggettoProcedimento = aModel.mCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aModel.mDescrOggettoProcedimento;

		this.mCodOggettiProcedimento = aModel.mCodOggettiProcedimento;
		this.mDescrOggettiProcedimento = aModel.mDescrOggettiProcedimento;

		this.mSezioneProcedimento = aModel.mSezioneProcedimento;
		this.mFlagRinviata = aModel.mFlagRinviata;
		this.mEsitoProvvedimento = aModel.mEsitoProvvedimento;
		this.mMotivoProvvedimento = aModel.mMotivoProvvedimento;
		this.mDescStatoUdienza = aModel.mDescStatoUdienza;
		this.mDescPosizioneGiuridicaSige = aModel.mDescPosizioneGiuridicaSige;
		this.mDescPosizioneGiuridica = aModel.mDescPosizioneGiuridica;
		this.mIdEvento = aModel.mIdEvento;
		this.mCodEsito = aModel.mCodEsito;

		this.mCognomeMagistrato = aModel.mCognomeMagistrato;
		this.mNomeMagistrato = aModel.mNomeMagistrato;
		this.mCodMagistrato = aModel.mCodMagistrato;

		this.mIdAvvocato = aModel.mIdAvvocato;
		this.mCognomeAvvocato = aModel.mCognomeAvvocato;
		this.mNomeAvvocato = aModel.mNomeAvvocato;
		this.mDescrTipoAvvocato = aModel.mDescrTipoAvvocato;

		this.mIdEsperto = aModel.mIdEsperto;

		// 2011-01-10
		// Oggetti aggregati d'uso per la generazione xml per stampe
		// Nota : Verificare se normalizzare, anche le view in jsp
		this.mFascicoloSige = aModel.mFascicoloSige;
		this.mSoggetto = aModel.mSoggetto;
		this.mMagistrato = aModel.mMagistrato;
		this.mAvvocato = aModel.mAvvocato;
		this.mTenori = aModel.mTenori;
		// 20180109 [EC] metodo introdotto per problematica inviata tramite email Maffucci/Alfieri del
		// 02/01/2018 (errore prototipo PALERMO)
		this.mAvvocati = aModel.mAvvocati;
    //interventi epr sies 11.2.1
    this.mDescrProcuratore = aModel.mDescrProcuratore;
    this.mDescrIdAssistente = aModel.mDescrIdAssistente;
	}

	// COSTRUTTORE MODEL
	public ProcedimentixUdienzaModel(BigDecimal aIdUdienza, Integer aNumCollegio, BigDecimal aIdFasSIGE,
			BigDecimal aChiaveAnnoFasSIGE, BigDecimal aChiaveProgrFasSIGE, String aCodStatoFascicolo,
			String aDescrStatoFascicolo, BigDecimal aIdSoggetto, String aCognomeSog, String aNomeSog,
			Date aDataNascitaSog, String aCodComuneNascitaSog, String aDescrComuneNascitaSog,
			String aDescrComuneNascitaEsteroSog,
			// BigDecimal aIdGeneraleProcedimento,
			String aCodOggettoProcedimento, String aDescrOggettoProcedimento, String aSezioneProcedimento,
			String aFlagRinviata, String aEsitoProvvedimento, String aMotivoProvvedimento,
			String aDescStatoUdienza, String aDescPosizioneGiuridicaSige, String aDescPosizioneGiuridica,
			BigDecimal aIdEvento, String aCognomeMagistrato, String aCodEsito, String aNomeMagistrato,
			String aCodMagistrato, BigDecimal aIdAvvocato, String aCognomeAvvocato, String aNomeAvvocato,
			String aDescrTipoAvvocato, BigDecimal aIdEsperto, FascicoloSigeModel aFascicoloSige,
			SoggettoModel aSoggetto, MagistratoModel aMagistrato, AvvocatoModel aAvvocato,
			TenoreSigeModel[] aTenori, AvvocatoModel[] aAvvocati, String aDescrProcuratore,
      		String aDescrIdAssistente) {
		this.mIdUdienza = aIdUdienza;
		this.mNumCollegio = aNumCollegio;
		this.mIdFasSIGE = aIdFasSIGE;
		this.mChiaveAnnoFasSIGE = aChiaveAnnoFasSIGE;
		this.mChiaveProgrFasSIGE = aChiaveProgrFasSIGE;
		this.mCodStatoFasSIGE = aCodStatoFascicolo;
		this.mDescrStatoFasSIGE = aDescrStatoFascicolo;
		this.mIdSoggetto = aIdSoggetto;
		this.mCognomeSog = aCognomeSog;
		this.mNomeSog = aNomeSog;
		this.mDataNascitaSog = aDataNascitaSog;
		this.mCodComuneNascitaSog = aCodComuneNascitaSog;
		this.mDescrComuneNascitaSog = aDescrComuneNascitaSog;
		this.mDescrComuneNascitaEsteroSog = aDescrComuneNascitaEsteroSog;
		// this.mIdGeneraleProcedimento = aIdGeneraleProcedimento;
		this.mCodOggettoProcedimento = aCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aDescrOggettoProcedimento;
		this.mSezioneProcedimento = aSezioneProcedimento;
		this.mFlagRinviata = aFlagRinviata;
		this.mEsitoProvvedimento = aEsitoProvvedimento;
		this.mMotivoProvvedimento = aMotivoProvvedimento;
		this.mDescStatoUdienza = aDescStatoUdienza;
		this.mDescPosizioneGiuridicaSige = aDescPosizioneGiuridicaSige;
		this.mDescPosizioneGiuridica = aDescPosizioneGiuridica;
		this.mIdEvento = aIdEvento;
		this.mCodEsito = aCodEsito;
		this.mCognomeMagistrato = aCognomeMagistrato;
		this.mNomeMagistrato = aNomeMagistrato;
		this.mCodMagistrato = aCodMagistrato;
		this.mIdAvvocato = aIdAvvocato;
		this.mCognomeAvvocato = aCognomeAvvocato;
		this.mNomeAvvocato = aNomeAvvocato;
		this.mDescrTipoAvvocato = aDescrTipoAvvocato;

		this.mIdEsperto = aIdSoggetto;

		// 2011-01-10
		// Oggetti aggregati d'uso per la generazione xml per stampe
		// Nota : Verificare se normalizzare, anche le view in jsp
		this.mFascicoloSige = aFascicoloSige;
		this.mSoggetto = aSoggetto;
		this.mMagistrato = aMagistrato;
		this.mAvvocato = aAvvocato;
		this.mTenori = aTenori;
		// 20180109 [EC] metodo introdotto per problematica inviata tramite email Maffucci/Alfieri del
		// 02/01/2018 (errore prototipo PALERMO)
		this.mAvvocati = aAvvocati;

      //interventi epr sies 11.2.1
      this.mDescrProcuratore = aDescrProcuratore;
      this.mDescrIdAssistente = aDescrIdAssistente;

	}

	//
	// METODI GET()
	//
	public BigDecimal getIdUdienza() {
		return mIdUdienza;
	}

	public Integer getNumCollegio() {
		return mNumCollegio;
	}

	public BigDecimal getIdFasSIGE() {
		return mIdFasSIGE;
	}

	public BigDecimal getChiaveAnnoFasSIGE() {
		return mChiaveAnnoFasSIGE;
	}

	public BigDecimal getChiaveProgrFasSIGE() {
		return mChiaveProgrFasSIGE;
	}

	public String getCodStatoFasSIGE() {
		return mCodStatoFasSIGE;
	}

	public String getDescrStatoFasSIGE() {
		return mDescrStatoFasSIGE;
	}

	public BigDecimal getIdSoggetto() {
		return mIdSoggetto;
	}

	public String getCognomeSog() {
		return mCognomeSog;
	}

	public String getNomeSog() {
		return mNomeSog;
	}

	public Date getDataNascitaSog() {
		return mDataNascitaSog;
	}

	public String getCodComuneNascitaSog() {
		return mCodComuneNascitaSog;
	}

	public String getDescrComuneNascitaSog() {
		return mDescrComuneNascitaSog;
	}

	public String getDescrComuneNascitaEsteroSog() {
		return mDescrComuneNascitaEsteroSog;
	}

	// public BigDecimal getIdGeneraleProcedimento() { return mIdGeneraleProcedimento; }
	public String getCodOggettoProcedimento() {
		return mCodOggettoProcedimento;
	}

	public String getDescrOggettoProcedimento() {
		return mDescrOggettoProcedimento;
	}

	public String[] getCodOggettiProcedimento() {
		return mCodOggettiProcedimento;
	}

	public String[] getDescrOggettiProcedimento() {
		return mDescrOggettiProcedimento;
	}

	public String getSezioneProcedimento() {
		return mSezioneProcedimento;
	}

	public String getFlagRinviata() {
		return mFlagRinviata;
	}

	public String getEsitoProvvedimento() {
		return mEsitoProvvedimento;
	}

	public String getMotivoProvvedimento() {
		return mMotivoProvvedimento;
	}

	public String getDescStatoUdienza() {
		return mDescStatoUdienza;
	}

	public String getDescPosizioneGiuridicaSige() {
		return mDescPosizioneGiuridicaSige;
	}

	public String getDescPosizioneGiuridica() {
		return mDescPosizioneGiuridica;
	}

	public BigDecimal getIdEvento() {
		return mIdEvento;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getCognomeMagistrato() {
		return mCognomeMagistrato;
	}

	public String getNomeMagistrato() {
		return mNomeMagistrato;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public BigDecimal getIdAvvocato() {
		return mIdAvvocato;
	}

	public String getCognomeAvvocato() {
		return mCognomeAvvocato;
	}

	public String getNomeAvvocato() {
		return mNomeAvvocato;
	}

	public String getDescrTipoAvvocato() {
		return mDescrTipoAvvocato;
	}

	public BigDecimal getIdEsperto() {
		return mIdEsperto;
	}

	// 2011-01-10
	// Oggetti aggregati d'uso per la generazione xml per stampe
	// Nota : Verificare se normalizzare, anche le view in jsp
	public FascicoloSigeModel getFascicoloSige() {
		return mFascicoloSige;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public MagistratoModel getMagistrato() {
		return mMagistrato;
	}

	public AvvocatoModel getAvvocato() {
		return mAvvocato;
	}

	public TenoreSigeModel[] getTenori() {
		return mTenori;
	}

	// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
	// variabile introdotta per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018 (errore
	// prototipo PALERMO)
	public AvvocatoModel[] getAvvocati() {
		return mAvvocati;
	}
  //interventi epr sies 11.2.1
  public String       getDescrProcuratore()              { return mDescrProcuratore; }
  public String       getDescrIdAssistente()              { return mDescrIdAssistente; }


	//
	// METODI SET()
	//
	public void setIdUdienza(BigDecimal aValore) {
		mIdUdienza = aValore;
	}

	public void setNumCollegio(Integer aValore) {
		mNumCollegio = aValore;
	}

	public void setIdFasSIGE(BigDecimal aValore) {
		mIdFasSIGE = aValore;
	}

	public void setChiaveAnnoFasSIGE(BigDecimal aValore) {
		mChiaveAnnoFasSIGE = aValore;
	}

	public void setChiaveProgrFasSIGE(BigDecimal aValore) {
		mChiaveProgrFasSIGE = aValore;
	}

	public void setCodStatoFasSIGE(String aValore) {
		mCodStatoFasSIGE = aValore;
	}

	public void setDescrStatoFasSIGE(String aValore) {
		mDescrStatoFasSIGE = aValore;
	}

	public void setIdSoggetto(BigDecimal aValore) {
		mIdSoggetto = aValore;
	}

	public void setCognomeSog(String aValore) {
		mCognomeSog = aValore;
	}

	public void setNomeSog(String aValore) {
		mNomeSog = aValore;
	}

	public void setDataNascitaSog(Date aValore) {
		mDataNascitaSog = aValore;
	}

	public void setCodComuneNascitaSog(String aValore) {
		mCodComuneNascitaSog = aValore;
	}

	public void setDescrComuneNascitaSog(String aValore) {
		mDescrComuneNascitaSog = aValore;
	}

	public void setDescrComuneNascitaEsteroSog(String aValore) {
		mDescrComuneNascitaEsteroSog = aValore;
	}

	// public void setIdGeneraleProcedimento(BigDecimal aValore) { mIdGeneraleProcedimento = aValore; }
	public void setCodOggettoProcedimento(String aValore) {
		mCodOggettoProcedimento = aValore;
	}

	public void setDescrOggettoProcedimento(String aValore) {
		mDescrOggettoProcedimento = aValore;
	}

	public void setCodOggettiProcedimento(String[] aValore) {
		mCodOggettiProcedimento = aValore;
	}

	public void setDescrOggettiProcedimento(String[] aValore) {
		mDescrOggettiProcedimento = aValore;
	}

	public void setSezioneProcedimento(String aValore) {
		mSezioneProcedimento = aValore;
	}

	public void setFlagRinviata(String aValore) {
		mFlagRinviata = aValore;
	}

	public void setEsitoProvvedimento(String aValore) {
		mEsitoProvvedimento = aValore;
	}

	public void setMotivoProvvedimento(String aValore) {
		mMotivoProvvedimento = aValore;
	}

	public void setDescStatoUdienza(String aValore) {
		mDescStatoUdienza = aValore;
	}

	public void setDescPosizioneGiuridicaSige(String aValore) {
		mDescPosizioneGiuridicaSige = aValore;
	}

	public void setDescPosizioneGiuridica(String aValore) {
		mDescPosizioneGiuridica = aValore;
	}

	public void setIdEvento(BigDecimal aValore) {
		mIdEvento = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setCognomeMagistrato(String aValore) {
		mCognomeMagistrato = aValore;
	}

	public void setNomeMagistrato(String aValore) {
		mNomeMagistrato = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setIdAvvocato(BigDecimal aValore) {
		mIdAvvocato = aValore;
	}

	public void setCognomeAvvocato(String aValore) {
		mCognomeAvvocato = aValore;
	}

	public void setNomeAvvocato(String aValore) {
		mNomeAvvocato = aValore;
	}

	public void setDescrTipoAvvocato(String aValore) {
		mDescrTipoAvvocato = aValore;
	}

	public void setIdEsperto(BigDecimal aValore) {
		mIdEsperto = aValore;
	}

	// 2011-01-10
	// Oggetti aggregati d'uso per la generazione xml per stampe
	// Nota : Verificare se normalizzare, anche le view in jsp
	public void setFascicoloSige(FascicoloSigeModel aValore) {
		mFascicoloSige = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setMagistrato(MagistratoModel aValore) {
		mMagistrato = aValore;
	}

	public void setAvvocato(AvvocatoModel aValore) {
		mAvvocato = aValore;
	}

	public void setTenori(TenoreSigeModel[] aValori) {
		mTenori = aValori;
	}

	// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
	// metodo introdotto per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018 (errore
	// prototipo PALERMO)
	public void setAvvocati(AvvocatoModel[] aValori) {
		mAvvocati = aValori;
	}

  //interventi epr sies 11.2.1
  public void   setDescrProcuratore(String aValore) 	 { mDescrProcuratore = aValore;  }
  public void   setDescrIdAssistente(String aValore) 	 { mDescrIdAssistente = aValore;  }

}