package siap.sius.depositoordinanzapc.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;

/**
 * DepositoOrdinanzaPcModel - Classe Model che rappresenta il DepositoOrdinanzaPc
 *
 * @version 1.0
 */
public class DepositoOrdinanzaPcModel extends GenericModel {

	private static final long serialVersionUID = -5228289147247068924L;

	private BigDecimal mIdDepositoOrdinanzaPc;
	private BigDecimal mAnnoS3;
	private BigDecimal mNumS3;
	private String mOggettoProcedimento;
	private Date mDataUdienza;
	private Date mDataCameraConsiglio;
	private String mAnnoDataCameraConsiglio;
	private String mMeseDataCameraConsiglio;
	private String mGiornoDataCameraConsiglio;
	private Date mDataDeposito;
	private String mCodNaturaProvvedimento;
	private String mDescrNaturaProvvedimento;
	private BigDecimal mIdCssaComp;
	private String mDescrComuneCssaComp;
	private BigDecimal mCodUssm;
	private String mDescrComuneUssmComp;
	private String mCodUfficioMagistratoComp;
	private String mDescrUfficioMagistratoComp;
	private String mLuogoSvolgimentoProva;
	private String mServizioTerapeuticoComp;
	private BigDecimal mNumGiorniDetenzioneDom;
	private BigDecimal mNumMesiDetenzioneDom;
	private BigDecimal mNumAnniDetenzioneDom;
	private BigDecimal mNumGiorniPermessoAccordati;
	private BigDecimal mNumGiorniRiduzionePena;
	private BigDecimal mNumGiorniRiduzioneUsufruiti;
	private String mCodUffTdsConcessoRiduzione;
	private String mDescrUffTdsConcessoRiduzione;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private Date mDataAggiornamento;
	private BigDecimal mGenPridGeneraleProcedimento;
	private String mCodMagistrato;
	private BigDecimal mIdEventoGenerato;
	private BigDecimal mNumGiorniLibanticipata;
	private String mFlagElaborato;
	private String mCodTipoOrdinanza;
	private String mDescrTipoOrdinanza;
	// Nuovi campi 9-6-2004
	private Date mDataFineMisura;
	private Date mDataDecorrenza;
	private Date mDataInizioPeriodo;
	private String mFlagEsistenzaReatoostativo;
	private String mFlagEspiazioneReatoostativo;
	private String mAutoritaVigilante;
	private Date mDataTrasmissione;
	private Date mDataCompFoglioComplementare;
	private String mNumSemestri = "";
	// MODEL
	private LicenzaPeriodiLibAnticipataModel mLicenzaPeriodiLibAnticipata;
	// Nuovi campi 8-5-2006 Pena Arresto rideterminata nella Revoca Misura alternativa
	private BigDecimal mNumGiorniArrestoRev;
	private BigDecimal mNumMesiArrestoRev;
	private BigDecimal mNumAnniArrestoRev;
	// Nuovo campo
	private String mUlterioreDescrizione;
	// Nuovi campi per Sospensione Sanzioni Sostitutive
	private Date mDataSospensioneSS;
	private BigDecimal mGiorniRecuperoSS;
	private String mFlagRecuperoSS;
	private Date mDataScadenzaSospensioneSS;
	private BigDecimal mSospensioneGGSS;
	private BigDecimal mSospensioneMMSS;
	private BigDecimal mSospensioneAASS;
	private String mFlagNominaComActa;
	private String mDescrCommActa;
	// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
	private String mCodTipoControlloEsecuzione;
	private String mDescrTipoControlloEsecuzione;
	// 10102014 - D.L. 92 2014 Violazione CEDU
	private BigDecimal mSommaRisarcimento;
	// MEV_2023-35: aggiunta variabile di classe per memorizzare il tipo di sanzione comminata
	private String mCodTipoSanzione;
	// MEV_2023-35: aggiunte variabili di classe per memorizzare il tipo di PA sospesa
	private String mCodTipoPenaAccessoria;
	private String mDescrTipoPenaAccessoria;
	private String mDurata;
	private String mDescrDurata;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;

	// COSTRUTTORE DI DEFAULT
	public DepositoOrdinanzaPcModel() {

		this.mIdDepositoOrdinanzaPc = null;
		this.mAnnoS3 = null;
		this.mNumS3 = null;
		this.mOggettoProcedimento = "";
		this.mDataUdienza = null;
		this.mDataCameraConsiglio = null;
		this.mDataDeposito = null;
		this.mCodNaturaProvvedimento = "";
		this.mDescrNaturaProvvedimento = "";
		this.mIdCssaComp = null;
		this.mDescrComuneCssaComp = "";
		this.mCodUssm = null;
		this.mDescrComuneUssmComp = "";
		this.mCodUfficioMagistratoComp = "";
		this.mDescrUfficioMagistratoComp = "";
		this.mLuogoSvolgimentoProva = "";
		this.mServizioTerapeuticoComp = "";
		this.mNumGiorniDetenzioneDom = null;
		this.mNumMesiDetenzioneDom = null;
		this.mNumAnniDetenzioneDom = null;
		this.mNumGiorniPermessoAccordati = null;
		this.mNumGiorniRiduzionePena = null;
		this.mNumGiorniRiduzioneUsufruiti = null;
		this.mCodUffTdsConcessoRiduzione = "";
		this.mDescrUffTdsConcessoRiduzione = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mGenPridGeneraleProcedimento = null;
		this.mCodMagistrato = "";
		this.mIdEventoGenerato = null;
		this.mNumGiorniLibanticipata = null;
		this.mFlagElaborato = "";
		this.mCodTipoOrdinanza = "";
		this.mDescrTipoOrdinanza = "";
		// Nuovi campi 9-6-2004
		this.mDataFineMisura = null;
		this.mDataDecorrenza = null;
		this.mDataInizioPeriodo = null;
		this.mFlagEsistenzaReatoostativo = "";
		this.mFlagEspiazioneReatoostativo = "";
		this.mAutoritaVigilante = "";
		this.mDataTrasmissione = null;
		this.mDataCompFoglioComplementare = null;
		// mLicenzaPeriodiLibAnticipata = null;
		this.mNumGiorniArrestoRev = null;
		this.mNumMesiArrestoRev = null;
		this.mNumAnniArrestoRev = null;
		this.mUlterioreDescrizione = "";
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		this.mDataSospensioneSS = null;
		this.mGiorniRecuperoSS = null;
		this.mFlagRecuperoSS = "";
		this.mDataScadenzaSospensioneSS = null;
		this.mSospensioneGGSS = null;
		this.mSospensioneMMSS = null;
		this.mSospensioneAASS = null;
		this.mFlagNominaComActa = null;
		this.mDescrCommActa = null;
		// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
		this.mCodTipoControlloEsecuzione = null;
		this.mDescrTipoControlloEsecuzione = null;
		// 10102014 - D.L. 92 2014 Violazione CEDU
		this.mSommaRisarcimento = null;
		this.mCodTipoSanzione = "";
		this.mCodTipoPenaAccessoria = "-";
		this.mDescrTipoPenaAccessoria = null;
		this.mDurata = "-";
		this.mDescrDurata = null;
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
	}

	// COSTRUTTORE DI COPIA
	public DepositoOrdinanzaPcModel(DepositoOrdinanzaPcModel aModel) {

		this.mIdDepositoOrdinanzaPc = aModel.mIdDepositoOrdinanzaPc;
		this.mAnnoS3 = aModel.mAnnoS3;
		this.mNumS3 = aModel.mNumS3;
		this.mOggettoProcedimento = aModel.mOggettoProcedimento;
		this.mDataUdienza = aModel.mDataUdienza;
		this.mDataCameraConsiglio = aModel.mDataCameraConsiglio;
		this.mDataDeposito = aModel.mDataDeposito;
		this.mCodNaturaProvvedimento = aModel.mCodNaturaProvvedimento;
		this.mDescrNaturaProvvedimento = aModel.mDescrNaturaProvvedimento;
		this.mIdCssaComp = aModel.mIdCssaComp;
		this.mDescrComuneCssaComp = aModel.mDescrComuneCssaComp;
		this.mCodUssm = aModel.mCodUssm;
		this.mDescrComuneUssmComp = aModel.mDescrComuneUssmComp;
		this.mCodUfficioMagistratoComp = aModel.mCodUfficioMagistratoComp;
		this.mDescrUfficioMagistratoComp = aModel.mDescrUfficioMagistratoComp;
		this.mLuogoSvolgimentoProva = aModel.mLuogoSvolgimentoProva;
		this.mServizioTerapeuticoComp = aModel.mServizioTerapeuticoComp;
		this.mNumGiorniDetenzioneDom = aModel.mNumGiorniDetenzioneDom;
		this.mNumMesiDetenzioneDom = aModel.mNumMesiDetenzioneDom;
		this.mNumAnniDetenzioneDom = aModel.mNumAnniDetenzioneDom;
		this.mNumGiorniPermessoAccordati = aModel.mNumGiorniPermessoAccordati;
		this.mNumGiorniRiduzionePena = aModel.mNumGiorniRiduzionePena;
		this.mNumGiorniRiduzioneUsufruiti = aModel.mNumGiorniRiduzioneUsufruiti;
		this.mCodUffTdsConcessoRiduzione = aModel.mCodUffTdsConcessoRiduzione;
		this.mDescrUffTdsConcessoRiduzione = aModel.mDescrUffTdsConcessoRiduzione;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mGenPridGeneraleProcedimento = aModel.mGenPridGeneraleProcedimento;
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mIdEventoGenerato = aModel.mIdEventoGenerato;
		this.mNumGiorniLibanticipata = aModel.mNumGiorniLibanticipata;
		this.mFlagElaborato = aModel.mFlagElaborato;
		this.mCodTipoOrdinanza = aModel.mCodTipoOrdinanza;
		this.mDescrTipoOrdinanza = aModel.mDescrTipoOrdinanza;
		// Nuovi campi 9-6-2004
		this.mDataFineMisura = aModel.mDataFineMisura;
		this.mDataDecorrenza = aModel.mDataDecorrenza;
		this.mDataInizioPeriodo = aModel.mDataInizioPeriodo;
		this.mFlagEsistenzaReatoostativo = aModel.mFlagEsistenzaReatoostativo;
		this.mFlagEspiazioneReatoostativo = aModel.mFlagEspiazioneReatoostativo;
		this.mAutoritaVigilante = aModel.mAutoritaVigilante;
		this.mDataTrasmissione = aModel.mDataTrasmissione;
		this.mDataCompFoglioComplementare = aModel.mDataCompFoglioComplementare;
		// mLicenzaPeriodiLibAnticipata = aModel.mLicenzaPeriodiLibAnticipata;
		this.mNumGiorniArrestoRev = aModel.mNumGiorniArrestoRev;
		this.mNumMesiArrestoRev = aModel.mNumMesiArrestoRev;
		this.mNumAnniArrestoRev = aModel.mNumAnniArrestoRev;
		// Nuovo campo.
		this.mUlterioreDescrizione = aModel.mUlterioreDescrizione;
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		this.mDataSospensioneSS = aModel.mDataSospensioneSS;
		this.mGiorniRecuperoSS = aModel.mGiorniRecuperoSS;
		this.mFlagRecuperoSS = aModel.mFlagRecuperoSS;
		this.mDataScadenzaSospensioneSS = aModel.mDataScadenzaSospensioneSS;
		this.mSospensioneGGSS = aModel.mSospensioneGGSS;
		this.mSospensioneMMSS = aModel.mSospensioneMMSS;
		this.mSospensioneAASS = aModel.mSospensioneAASS;
		this.mFlagNominaComActa = aModel.mFlagNominaComActa;
		this.mDescrCommActa = aModel.mDescrCommActa;
		// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
		this.mCodTipoControlloEsecuzione = aModel.mCodTipoControlloEsecuzione;
		this.mDescrTipoControlloEsecuzione = aModel.mDescrTipoControlloEsecuzione;
		// 10102014 - D.L. 92 2014 Violazione CEDU
		this.mSommaRisarcimento = aModel.mSommaRisarcimento;
		this.mCodTipoSanzione = aModel.mCodTipoSanzione;
		this.mCodTipoPenaAccessoria = aModel.mCodTipoPenaAccessoria;
		this.mDescrTipoPenaAccessoria = aModel.mDescrTipoPenaAccessoria;
		this.mDurata = aModel.mDurata;
		this.mDescrDurata = aModel.mDescrDurata;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
	}

	// COSTRUTTORE MODEL
	public DepositoOrdinanzaPcModel(BigDecimal aIdDepositoOrdinanzaPc, BigDecimal aAnnoS3, BigDecimal aNumS3,
			String aOggettoProcedimento, Date aDataUdienza, Date aDataCameraConsiglio, Date aDataDeposito,
			String aCodNaturaProvvedimento, String aDescrNaturaProvvedimento, BigDecimal aIdCssaComp,
			String aDescrComuneCssaComp, BigDecimal aCodUssm, String aDescrComuneUssmComp,
			String aCodUfficioMagistratoComp, String aDescrUfficioMagistratoComp,
			String aLuogoSvolgimentoProva, String aServizioTerapeuticoComp,
			BigDecimal aNumGiorniDetenzioneDom, BigDecimal aNumMesiDetenzioneDom,
			BigDecimal aNumAnniDetenzioneDom, BigDecimal aNumGiorniPermessoAccordati,
			BigDecimal aNumGiorniRiduzionePena, BigDecimal aNumGiorniRiduzioneUsufruiti,
			String aCodUffTdsConcessoRiduzione, String aDescrUffTdsConcessoRiduzione,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, Date aDataAggiornamento,
			BigDecimal aGenPridGeneraleProcedimento, String aCodMagistrato, BigDecimal aIdEventoGenerato,
			BigDecimal aNumGiorniLibanticipata, String aFlagElaborato, String aCodTipoOrdinanza,
			String aDescrTipoOrdinanza, Date aDataFineMisura, Date aDataDecorrenza, Date aDataInizioPeriodo,
			String aFlagEsistenzaReatoostativo, String aFlagEspiazioneReatoostativo,
			String aAutoritaVigilante, Date aDataTrasmissione, Date aDataCompFoglioComplementare,
			// LicenzaPeriodiLibAnticipataModel aLicenzaPeriodiLibAnticipata
			BigDecimal aNumGiorniArrestoRev, BigDecimal aNumMesiArrestoRev, BigDecimal aNumAnniArrestoRev,
			String aUlterioreDescrizione,
			// Nuovi campi per Sospensione Sanzioni Sostitutive
			Date aDataSospensioneSS, BigDecimal aGiorniRecuperoSS, String aFlagRecuperoSS,
			Date aDataScadenzaSospensioneSS, BigDecimal aSospensioneGGSS, BigDecimal aSospensioneMMSS,
			BigDecimal aSospensioneAASS, String aFlagNominaComActa, String aDescrCommActa,
			String aCodTipoControlloEsecuzione, String aDescrTipoControlloEsecuzione,
			BigDecimal aSommaRisarcimento, String aCodTipoSanzione, String aCodTipoPenaAccessoria,
			String aDescrTipoPenaAccessoria, String aDurata, String aDescrDurata, BigDecimal aNumAnni,
			BigDecimal aNumMesi, BigDecimal aNumGiorni) {

		this.mIdDepositoOrdinanzaPc = aIdDepositoOrdinanzaPc;
		this.mAnnoS3 = aAnnoS3;
		this.mNumS3 = aNumS3;
		this.mOggettoProcedimento = aOggettoProcedimento;
		this.mDataUdienza = aDataUdienza;
		this.mDataCameraConsiglio = aDataCameraConsiglio;
		this.mDataDeposito = aDataDeposito;
		this.mCodNaturaProvvedimento = aCodNaturaProvvedimento;
		this.mDescrNaturaProvvedimento = aDescrNaturaProvvedimento;
		this.mIdCssaComp = aIdCssaComp;
		this.mDescrComuneCssaComp = aDescrComuneCssaComp;
		this.mCodUssm = aCodUssm;
		this.mDescrComuneUssmComp = aDescrComuneUssmComp;
		this.mCodUfficioMagistratoComp = aCodUfficioMagistratoComp;
		this.mDescrUfficioMagistratoComp = aDescrUfficioMagistratoComp;
		this.mLuogoSvolgimentoProva = aLuogoSvolgimentoProva;
		this.mServizioTerapeuticoComp = aServizioTerapeuticoComp;
		this.mNumGiorniDetenzioneDom = aNumGiorniDetenzioneDom;
		this.mNumMesiDetenzioneDom = aNumMesiDetenzioneDom;
		this.mNumAnniDetenzioneDom = aNumAnniDetenzioneDom;
		this.mNumGiorniPermessoAccordati = aNumGiorniPermessoAccordati;
		this.mNumGiorniRiduzionePena = aNumGiorniRiduzionePena;
		this.mNumGiorniRiduzioneUsufruiti = aNumGiorniRiduzioneUsufruiti;
		this.mCodUffTdsConcessoRiduzione = aCodUffTdsConcessoRiduzione;
		this.mDescrUffTdsConcessoRiduzione = aDescrUffTdsConcessoRiduzione;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mGenPridGeneraleProcedimento = aGenPridGeneraleProcedimento;
		this.mCodMagistrato = aCodMagistrato;
		this.mIdEventoGenerato = aIdEventoGenerato;
		this.mNumGiorniLibanticipata = aNumGiorniLibanticipata;
		this.mFlagElaborato = aFlagElaborato;
		this.mCodTipoOrdinanza = aCodTipoOrdinanza;
		this.mDescrTipoOrdinanza = aDescrTipoOrdinanza;
		this.mDataFineMisura = aDataFineMisura;
		this.mDataDecorrenza = aDataDecorrenza;
		this.mDataInizioPeriodo = aDataInizioPeriodo;
		this.mFlagEsistenzaReatoostativo = aFlagEsistenzaReatoostativo;
		this.mFlagEspiazioneReatoostativo = aFlagEspiazioneReatoostativo;
		this.mAutoritaVigilante = aAutoritaVigilante;
		this.mDataTrasmissione = aDataTrasmissione;
		this.mDataCompFoglioComplementare = aDataCompFoglioComplementare;
		// mLicenzaPeriodiLibAnticipata = aLicenzaPeriodiLibAnticipata;
		this.mNumGiorniArrestoRev = aNumGiorniArrestoRev;
		this.mNumMesiArrestoRev = aNumMesiArrestoRev;
		this.mNumAnniArrestoRev = aNumAnniArrestoRev;
		this.mUlterioreDescrizione = aUlterioreDescrizione;
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		this.mDataSospensioneSS = aDataSospensioneSS;
		this.mGiorniRecuperoSS = aGiorniRecuperoSS;
		this.mFlagRecuperoSS = aFlagRecuperoSS;
		this.mDataScadenzaSospensioneSS = aDataScadenzaSospensioneSS;
		this.mSospensioneGGSS = aSospensioneGGSS;
		this.mSospensioneMMSS = aSospensioneMMSS;
		this.mSospensioneAASS = aSospensioneAASS;
		this.mFlagNominaComActa = aFlagNominaComActa;
		this.mDescrCommActa = aDescrCommActa;
		// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
		this.mCodTipoControlloEsecuzione = aCodTipoControlloEsecuzione;
		this.mDescrTipoControlloEsecuzione = aDescrTipoControlloEsecuzione;
		// 10102014 - D.L. 92 2014 Violazione CEDU
		this.mSommaRisarcimento = aSommaRisarcimento;
		this.mCodTipoSanzione = aCodTipoSanzione;
		this.mCodTipoPenaAccessoria = aCodTipoPenaAccessoria;
		this.mDescrTipoPenaAccessoria = aDescrTipoPenaAccessoria;
		this.mDurata = aDurata;
		this.mDescrDurata = aDescrDurata;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdDepositoOrdinanzaPc() {
		return mIdDepositoOrdinanzaPc;
	}

	public BigDecimal getAnnoS3() {
		return mAnnoS3;
	}

	public BigDecimal getNumS3() {
		return mNumS3;
	}

	public String getOggettoProcedimento() {
		return mOggettoProcedimento;
	}

	public Date getDataUdienza() {
		return mDataUdienza;
	}

	public Date getDataCameraConsiglio() {
		return mDataCameraConsiglio;
	}

	public Date getDataDeposito() {
		return mDataDeposito;
	}

	public String getCodNaturaProvvedimento() {
		return mCodNaturaProvvedimento;
	}

	public String getDescrNaturaProvvedimento() {
		return mDescrNaturaProvvedimento;
	}

	public BigDecimal getIdCssaComp() {
		return mIdCssaComp;
	}

	public String getDescrComuneCssaComp() {
		return mDescrComuneCssaComp;
	}

	public BigDecimal getCodUssm() {
		return mCodUssm;
	}

	public String getDescrComuneUssmComp() {
		return mDescrComuneUssmComp;
	}

	public String getCodUfficioMagistratoComp() {
		return mCodUfficioMagistratoComp;
	}

	public String getDescrUfficioMagistratoComp() {
		return mDescrUfficioMagistratoComp;
	}

	public String getLuogoSvolgimentoProva() {
		return mLuogoSvolgimentoProva;
	}

	public String getServizioTerapeuticoComp() {
		return mServizioTerapeuticoComp;
	}

	public BigDecimal getNumGiorniDetenzioneDom() {
		return mNumGiorniDetenzioneDom;
	}

	public BigDecimal getNumMesiDetenzioneDom() {
		return mNumMesiDetenzioneDom;
	}

	public BigDecimal getNumAnniDetenzioneDom() {
		return mNumAnniDetenzioneDom;
	}

	public BigDecimal getNumGiorniPermessoAccordati() {
		return mNumGiorniPermessoAccordati;
	}

	public BigDecimal getNumGiorniRiduzionePena() {
		return mNumGiorniRiduzionePena;
	}

	public BigDecimal getNumGiorniRiduzioneUsufruiti() {
		return mNumGiorniRiduzioneUsufruiti;
	}

	public String getCodUffTdsConcessoRiduzione() {
		return mCodUffTdsConcessoRiduzione;
	}

	public String getDescrUffTdsConcessoRiduzione() {
		return mDescrUffTdsConcessoRiduzione;
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

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public BigDecimal getGenPridGeneraleProcedimento() {
		return mGenPridGeneraleProcedimento;
	}

	public String getAnnoDataCameraConsiglio() {
		return mAnnoDataCameraConsiglio;
	}

	public String getMeseDataCameraConsiglio() {
		return mMeseDataCameraConsiglio;
	}

	public String getGiornoDataCameraConsiglio() {
		return mGiornoDataCameraConsiglio;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public BigDecimal getIdEventoGenerato() {
		return mIdEventoGenerato;
	}

	public BigDecimal getNumGiorniLibanticipata() {
		return mNumGiorniLibanticipata;
	}

	public String getFlagElaborato() {
		return mFlagElaborato;
	}

	public String getCodTipoOrdinanza() {
		return mCodTipoOrdinanza;
	}

	public String getDescrTipoOrdinanza() {
		return mDescrTipoOrdinanza;
	}

	public Date getDataFineMisura() {
		return mDataFineMisura;
	}

	public Date getDataDecorrenza() {
		return mDataDecorrenza;
	}

	public Date getDataInizioPeriodo() {
		return mDataInizioPeriodo;
	}

	public String getFlagEsistenzaReatoostativo() {
		return mFlagEsistenzaReatoostativo;
	}

	public String getFlagEspiazioneReatoostativo() {
		return mFlagEspiazioneReatoostativo;
	}

	public String getAutoritaVigilante() {
		return mAutoritaVigilante;
	}

	public Date getDataTrasmissione() {
		return mDataTrasmissione;
	}

	public Date getDataCompFoglioComplementare() {
		return mDataCompFoglioComplementare;
	}

	public LicenzaPeriodiLibAnticipataModel getLicenzaPeriodiLibAnticipata() {
		return mLicenzaPeriodiLibAnticipata;
	}

	public String getNumSemestri() {
		return mNumSemestri;
	}

	public BigDecimal getNumGiorniArrestoRev() {
		return mNumGiorniArrestoRev;
	}

	public BigDecimal getNumMesiArrestoRev() {
		return mNumMesiArrestoRev;
	}

	public BigDecimal getNumAnniArrestoRev() {
		return mNumAnniArrestoRev;
	}

	public String getUlterioreDescrizione() {
		return mUlterioreDescrizione;
	}

	// Nuovi campi per Sospensione Sanzioni Sostitutive
	public Date getDataSospensioneSS() {
		return mDataSospensioneSS;
	}

	public BigDecimal getGiorniRecuperoSS() {
		return mGiorniRecuperoSS;
	}

	public String getFlagRecuperoSS() {
		return mFlagRecuperoSS;
	}

	public Date getDataScadenzaSospensioneSS() {
		return mDataScadenzaSospensioneSS;
	}

	public BigDecimal getSospensioneGGSS() {
		if (mSospensioneGGSS != null)
			return mSospensioneGGSS;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getSospensioneMMSS() {
		if (mSospensioneMMSS != null)
			return mSospensioneMMSS;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getSospensioneAASS() {
		if (mSospensioneAASS != null)
			return mSospensioneAASS;
		else
			return new BigDecimal(0);
	}

	public String getFlagNominaComActa() {
		return mFlagNominaComActa;
	}

	public String getDescrCommActa() {
		return mDescrCommActa;
	}

	// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
	public String getCodTipoControlloEsecuzione() {
		return mCodTipoControlloEsecuzione;
	}

	public String getDescrTipoControlloEsecuzione() {
		return mDescrTipoControlloEsecuzione;
	}

	// 10102014 - D.L. 92 2014 Violazione CEDU
	public BigDecimal getSommaRisarcimento() {
		if (mSommaRisarcimento != null)
			return mSommaRisarcimento;
		else
			return new BigDecimal(0);
	}

	public String getCodTipoSanzione() {
		return mCodTipoSanzione;
	}

	public String getCodTipoPenaAccessoria() {
		return mCodTipoPenaAccessoria;
	}

	public String getDescrTipoPenaAccessoria() {
		return mDescrTipoPenaAccessoria;
	}

	public String getDurata() {
		return mDurata;
	}

	public String getDescrDurata() {
		return mDescrDurata;
	}

	public BigDecimal getNumAnni() {
		return mNumAnni;
	}

	public BigDecimal getNumMesi() {
		return mNumMesi;
	}

	public BigDecimal getNumGiorni() {
		return mNumGiorni;
	}

	//
	// METODI SET()
	//
	public void setIdDepositoOrdinanzaPc(BigDecimal aValore) {
		this.mIdDepositoOrdinanzaPc = aValore;
	}

	public void setAnnoS3(BigDecimal aValore) {
		this.mAnnoS3 = aValore;
	}

	public void setNumS3(BigDecimal aValore) {
		this.mNumS3 = aValore;
	}

	public void setOggettoProcedimento(String aValore) {
		this.mOggettoProcedimento = aValore;
	}

	public void setDataUdienza(Date aValore) {
		this.mDataUdienza = aValore;
	}

	public void setDataCameraConsiglio(Date aValore) {
		this.mDataCameraConsiglio = aValore;
	}

	public void setDataDeposito(Date aValore) {
		this.mDataDeposito = aValore;
	}

	public void setCodNaturaProvvedimento(String aValore) {
		this.mCodNaturaProvvedimento = aValore;
	}

	public void setDescrNaturaProvvedimento(String aValore) {
		this.mDescrNaturaProvvedimento = aValore;
	}

	public void setIdCssaComp(BigDecimal aValore) {
		this.mIdCssaComp = aValore;
	}

	public void setDescrComuneCssaComp(String aValore) {
		this.mDescrComuneCssaComp = aValore;
	}

	public void setCodUssm(BigDecimal aValore) {
		this.mCodUssm = aValore;
	}

	public void setDescrComuneUssmComp(String aValore) {
		this.mDescrComuneUssmComp = aValore;
	}

	public void setCodUfficioMagistratoComp(String aValore) {
		this.mCodUfficioMagistratoComp = aValore;
	}

	public void setDescrUfficioMagistratoComp(String aValore) {
		this.mDescrUfficioMagistratoComp = aValore;
	}

	public void setLuogoSvolgimentoProva(String aValore) {
		this.mLuogoSvolgimentoProva = aValore;
	}

	public void setServizioTerapeuticoComp(String aValore) {
		this.mServizioTerapeuticoComp = aValore;
	}

	public void setNumGiorniDetenzioneDom(BigDecimal aValore) {
		this.mNumGiorniDetenzioneDom = aValore;
	}

	public void setNumMesiDetenzioneDom(BigDecimal aValore) {
		this.mNumMesiDetenzioneDom = aValore;
	}

	public void setNumAnniDetenzioneDom(BigDecimal aValore) {
		this.mNumAnniDetenzioneDom = aValore;
	}

	public void setNumGiorniPermessoAccordati(BigDecimal aValore) {
		this.mNumGiorniPermessoAccordati = aValore;
	}

	public void setNumGiorniRiduzionePena(BigDecimal aValore) {
		this.mNumGiorniRiduzionePena = aValore;
	}

	public void setNumGiorniRiduzioneUsufruiti(BigDecimal aValore) {
		this.mNumGiorniRiduzioneUsufruiti = aValore;
	}

	public void setCodUffTdsConcessoRiduzione(String aValore) {
		this.mCodUffTdsConcessoRiduzione = aValore;
	}

	public void setDescrUffTdsConcessoRiduzione(String aValore) {
		this.mDescrUffTdsConcessoRiduzione = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		this.mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		this.mDataInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		this.mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		this.mDescrUfficioInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		this.mCodOperatoreAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		this.mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		this.mDescrUfficioAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		this.mDataAggiornamento = aValore;
	}

	public void setGenPridGeneraleProcedimento(BigDecimal aValore) {
		this.mGenPridGeneraleProcedimento = aValore;
	}

	public void setAnnoDataCameraConsiglio(String aValore) {
		this.mAnnoDataCameraConsiglio = aValore;
	}

	public void setMeseDataCameraConsiglio(String aValore) {
		this.mMeseDataCameraConsiglio = aValore;
	}

	public void setGiornoDataCameraConsiglio(String aValore) {
		this.mGiornoDataCameraConsiglio = aValore;
	}

	public void setCodMagistrato(String aValore) {
		this.mCodMagistrato = aValore;
	}

	public void setIdEventoGenerato(BigDecimal aValore) {
		this.mIdEventoGenerato = aValore;
	}

	public void setNumGiorniLibanticipata(BigDecimal aValore) {
		this.mNumGiorniLibanticipata = aValore;
	}

	public void setFlagElaborato(String aValore) {
		this.mFlagElaborato = aValore;
	}

	public void setCodTipoOrdinanza(String aValore) {
		this.mCodTipoOrdinanza = aValore;
	}

	public void setDescrTipoOrdinanza(String aValore) {
		this.mDescrTipoOrdinanza = aValore;
	}

	public void setDataFineMisura(Date aValore) {
		this.mDataFineMisura = aValore;
	}

	public void setDataDecorrenza(Date aValore) {
		this.mDataDecorrenza = aValore;
	}

	public void setDataInizioPeriodo(Date aValore) {
		this.mDataInizioPeriodo = aValore;
	}

	public void setFlagEsistenzaReatoostativo(String aValore) {
		this.mFlagEsistenzaReatoostativo = aValore;
	}

	public void setFlagEspiazioneReatoostativo(String aValore) {
		this.mFlagEspiazioneReatoostativo = aValore;
	}

	public void setAutoritaVigilante(String aValore) {
		this.mAutoritaVigilante = aValore;
	}

	public void setDataTrasmissione(Date aValore) {
		this.mDataTrasmissione = aValore;
	}

	public void setDataCompFoglioComplementare(Date aValore) {
		this.mDataCompFoglioComplementare = aValore;
	}

	public void setLicenzaPeriodiLibAnticipata(LicenzaPeriodiLibAnticipataModel aValore) {
		this.mLicenzaPeriodiLibAnticipata = aValore;
	}

	public void setNumSemestri(String aValore) {
		this.mNumSemestri = aValore;
	}

	public void setNumGiorniArrestoRev(BigDecimal aValore) {
		this.mNumGiorniArrestoRev = aValore;
	}

	public void setNumMesiArrestoRev(BigDecimal aValore) {
		this.mNumMesiArrestoRev = aValore;
	}

	public void setNumAnniArrestoRev(BigDecimal aValore) {
		this.mNumAnniArrestoRev = aValore;
	}

	public void setUlterioreDescrizione(String aValore) {
		this.mUlterioreDescrizione = aValore;
	}

	// Nuovi campi per Sospensione Sanzioni Sostitutive
	public void setDataSospensioneSS(Date aValore) {
		this.mDataSospensioneSS = aValore;
	}

	public void setGiorniRecuperoSS(BigDecimal aValore) {
		this.mGiorniRecuperoSS = aValore;
	}

	public void setFlagRecuperoSS(String aValore) {
		this.mFlagRecuperoSS = aValore;
	}

	public void setDataScadenzaSospensioneSS(Date aValore) {
		this.mDataScadenzaSospensioneSS = aValore;
	}

	public void setSospensioneGGSS(BigDecimal aValore) {
		this.mSospensioneGGSS = aValore;
	}

	public void setSospensioneMMSS(BigDecimal aValore) {
		this.mSospensioneMMSS = aValore;
	}

	public void setSospensioneAASS(BigDecimal aValore) {
		this.mSospensioneAASS = aValore;
	}

	public void setFlagNominaComActa(String aValore) {
		this.mFlagNominaComActa = aValore;
	}

	public void setDescrCommActa(String aValore) {
		this.mDescrCommActa = aValore;
	}

	// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
	public void setCodTipoControlloEsecuzione(String aValore) {
		this.mCodTipoControlloEsecuzione = aValore;
	}

	public void setDescrTipoControlloEsecuzione(String aValore) {
		this.mDescrTipoControlloEsecuzione = aValore;
	}

	// 10102014 - D.L. 92 2014 Violazione CEDU
	public void setSommaRisarcimento(BigDecimal aValore) {
		this.mSommaRisarcimento = aValore;
	}

	public void setCodTipoSanzione(String aValore) {
		this.mCodTipoSanzione = aValore;
	}

	public void setCodTipoPenaAccessoria(String aValore) {
		mCodTipoPenaAccessoria = aValore;
	}

	public void setDescrTipoPenaAccessoria(String aValore) {
		mDescrTipoPenaAccessoria = aValore;
	}

	public void setDurata(String aValore) {
		mDurata = aValore;
	}

	public void setDescrDurata(String aValore) {
		mDescrDurata = aValore;
	}

	public void setNumAnni(BigDecimal aValore) {
		mNumAnni = aValore;
	}

	public void setNumMesi(BigDecimal aValore) {
		mNumMesi = aValore;
	}

	public void setNumGiorni(BigDecimal aValore) {
		mNumGiorni = aValore;
	}

	// Metodo toString()
	public String toString() {

		String lStr = new String();

		lStr = "" + mIdDepositoOrdinanzaPc + " - " + mAnnoS3 + " - " + mNumS3 + " - " + mOggettoProcedimento
				+ " - " + mDataUdienza + " - " + mDataCameraConsiglio + " - " + mDataDeposito + " - "
				+ mCodNaturaProvvedimento + " - " + mDescrNaturaProvvedimento + " - " + mIdCssaComp + " - "
				+ mDescrComuneCssaComp + " - " + mCodUssm + " - " + mDescrComuneUssmComp + " - "
				+ mCodUfficioMagistratoComp + " - " + mDescrUfficioMagistratoComp + " - "
				+ mLuogoSvolgimentoProva + " - " + mServizioTerapeuticoComp + " - " + mNumGiorniDetenzioneDom
				+ " - " + mNumMesiDetenzioneDom + " - " + mNumAnniDetenzioneDom + " - "
				+ mNumGiorniPermessoAccordati + " - " + mNumGiorniRiduzionePena + " - "
				+ mNumGiorniRiduzioneUsufruiti + " - " + mCodUffTdsConcessoRiduzione + " - "
				+ mDescrUffTdsConcessoRiduzione + " - " + mCodOperatoreInserimento + " - " + mDataInserimento
				+ " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mCodUfficioAggiornamento + " - "
				+ mDescrUfficioAggiornamento + " - " + mDataAggiornamento + " - "
				+ mGenPridGeneraleProcedimento + " - " + mCodMagistrato + " - " + mIdEventoGenerato + " - "
				+ mNumGiorniLibanticipata + " - " + mFlagElaborato + " - " + mCodTipoOrdinanza + " - "
				+ mDescrTipoOrdinanza + " - " + mDataFineMisura + " - " + mDataDecorrenza + " - "
				+ mDataInizioPeriodo + " - " + mFlagEsistenzaReatoostativo + " - "
				+ mFlagEspiazioneReatoostativo + " - " + mAutoritaVigilante + " - " + mDataTrasmissione
				+ " - " + mDataCompFoglioComplementare + " - " + mNumGiorniArrestoRev + " - "
				+ mNumMesiArrestoRev + " - " + mNumAnniArrestoRev + " - " + mUlterioreDescrizione + " - " +
				// Nuovi campi per Sospensione Sanzioni Sostitutive
				mDataSospensioneSS + " - " + mGiorniRecuperoSS + " - " + mFlagRecuperoSS + " - "
				+ mDataScadenzaSospensioneSS + " - " + mSospensioneGGSS + " - " + mSospensioneMMSS + " - "
				+ mSospensioneAASS + " - " + mFlagNominaComActa + " - " + mDescrCommActa + " - " +
				// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
				mCodTipoControlloEsecuzione + " - " + mDescrTipoControlloEsecuzione + " - " +
				// 10102014 - D.L. 92 2014 Violazione CEDU
				mSommaRisarcimento + " - " + mCodTipoSanzione + " - " + mCodTipoPenaAccessoria + " - "
				+ mDescrTipoPenaAccessoria + " - " + mDurata + " - " + mDescrDurata + " - " + mNumAnni + " - "
				+ mNumMesi + " - " + mNumGiorni;

		return lStr;
	}

} // Chiude Classe DepositoOrdinanzaPcModel