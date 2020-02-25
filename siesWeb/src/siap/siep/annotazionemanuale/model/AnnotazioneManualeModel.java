package siap.siep.annotazionemanuale.model;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.calendar.model.CalendarModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: AnnotazioneManualeModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il AnnotazioneManuale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class AnnotazioneManualeModel extends GenericModel {

	private static final long serialVersionUID = 5836011801845785449L;

	private BigDecimal mIdAnnotazioneManuale;
	private String mCodTipoAnnotazione;
	private String mDescrTipoAnnotazione;
	private String mFlagPiuMeno;
	private BigDecimal mNumAnniReclusione;
	private BigDecimal mNumMesiReclusione;
	private BigDecimal mNumGiorniReclusione;
	private BigDecimal mImportoMulta;
	private BigDecimal mNumAnniArresto;
	private BigDecimal mNumMesiArresto;
	private BigDecimal mNumGiorniArresto;
	private BigDecimal mImportoAmmenda;
	private Date mDataReclusioneDa;
	private Date mDataReclusioneA;
	private Date mDataArrestoDa;
	private Date mDataArrestoA;
	private Date mDataRicezioneDoc;
	private String mMotivazioni;
	private String mNoteReclusione;
	private BigDecimal mAnnoGe;
	private String mNumeroGe;
	private BigDecimal mAnnoRege;
	private String mNumeroRege;
	private BigDecimal mAnnoMc;
	private String mNumeroMc;
	private BigDecimal mAnnoCda;
	private String mNumeroCda;
	private BigDecimal mAnnoCc;
	private String mNumeroCc;
	private BigDecimal mAnnoSiep;
	private String mNumeroSiep;
	private String mCodTipoUfficioSiep;
	private String mDescrTipoUfficioSiep;
	private String mCodLuogoUfficioSiep;
	private String mDescrLuogoUfficioSiep;
	private Date mDataIscrizioneSiep;
	private String mCodFonte;
	private String mDescrFonte;
	private BigDecimal mAnnoFonte;
	private String mNumeroFonte;
	private String mCodSottonumerazione;
	private String mDescrSottonumerazione;
	private String mComma;
	private String mLettera;
	private String mNumero;
	private String mArticolo;
	private String mCodCausaleComputo;
	private String mDescrCausaleComputo;
	private String mCodDpr;
	private String mDescrDpr;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mReaIdReato;
	private BigDecimal mEveIdEvento;
	private String mFlagValidato;
	private String mFlagConforme;
	private String mFlagAppProvvisoria;
	private BigDecimal mPenResIdPenaResidua;
	private BigDecimal mFunIdFungibilita;
	private Date mDataRichiesta;
	private Date mDataGE;
	private Date mDataCC;
	private BigDecimal mAnnoSentenzaSiap;
	private String mNumeroSentenzaSiap;
	private Date mDataSentenzaSiap;
	private BigDecimal mAnnoIdAnnotazioneManuale;

	private String mStringaReclusione;
	private String mStringaArresto;
	// Dario per la Stampa dello stato di Esecuzione
	private String mBeneficio;

	private String mFlagComputabile;
	private String mFlagBeneficioDetratto;
	private BigDecimal mSenIdSentenza;
	private BigDecimal mTenIdTenoreSige;

	private String mFlagSelQuantum;

	// MEV 29 punto 11 - Anno e Numero Procedimento SIGE
	private BigDecimal mAnnoSige;
	private BigDecimal mNumeroSige;

	// COSTRUTTORE DI DEFAULT
	public AnnotazioneManualeModel() {
		this.mIdAnnotazioneManuale = null;
		this.mCodTipoAnnotazione = "";
		this.mDescrTipoAnnotazione = "";
		this.mFlagPiuMeno = "";
		this.mNumAnniReclusione = null;
		this.mNumMesiReclusione = null;
		this.mNumGiorniReclusione = null;
		this.mImportoMulta = null;
		this.mNumAnniArresto = null;
		this.mNumMesiArresto = null;
		this.mNumGiorniArresto = null;
		this.mImportoAmmenda = null;
		this.mDataReclusioneDa = null;
		this.mDataReclusioneA = null;
		this.mDataArrestoA = null;
		this.mDataArrestoDa = null;
		this.mDataRicezioneDoc = null;
		this.mMotivazioni = "";
		this.mNoteReclusione = "";
		this.mAnnoGe = null;
		this.mNumeroGe = "";
		this.mAnnoRege = null;
		this.mNumeroRege = "";
		this.mAnnoMc = null;
		this.mNumeroMc = "";
		this.mAnnoCda = null;
		this.mNumeroCda = "";
		this.mAnnoCc = null;
		this.mNumeroCc = "";
		this.mAnnoSiep = null;
		this.mNumeroSiep = "";
		this.mCodTipoUfficioSiep = "";
		this.mDescrTipoUfficioSiep = "";
		this.mCodLuogoUfficioSiep = "";
		this.mDescrLuogoUfficioSiep = "";
		this.mDataIscrizioneSiep = null;
		this.mCodFonte = "";
		this.mDescrFonte = "";
		this.mAnnoFonte = null;
		this.mNumeroFonte = "";
		this.mCodSottonumerazione = "";
		this.mDescrSottonumerazione = "";
		this.mComma = "";
		this.mLettera = "";
		this.mNumero = "";
		this.mArticolo = "";
		this.mCodCausaleComputo = "";
		this.mDescrCausaleComputo = "";
		this.mCodDpr = "";
		this.mDescrDpr = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mReaIdReato = null;
		this.mEveIdEvento = null;
		this.mFlagValidato = "";
		this.mFlagConforme = "";
		this.mFlagAppProvvisoria = "";
		this.mPenResIdPenaResidua = null;
		this.mFunIdFungibilita = null;
		this.mDataRichiesta = null;
		this.mDataCC = null;
		this.mDataGE = null;
		this.mAnnoSentenzaSiap = null;
		this.mNumeroSentenzaSiap = "";
		this.mDataSentenzaSiap = null;
		this.mBeneficio = null;
		this.mAnnoIdAnnotazioneManuale = null;
		this.mFlagComputabile = null;
		this.mFlagBeneficioDetratto = null;
		this.mSenIdSentenza = null;
		this.mTenIdTenoreSige = null;
		this.mFlagSelQuantum = null;
		this.mAnnoSige = null;
		this.mNumeroSige = null;
	}

	// COSTRUTTORE DI COPIA
	public AnnotazioneManualeModel(AnnotazioneManualeModel aModel) {
		this.mIdAnnotazioneManuale = aModel.mIdAnnotazioneManuale;
		this.mCodTipoAnnotazione = aModel.mCodTipoAnnotazione;
		this.mDescrTipoAnnotazione = aModel.mDescrTipoAnnotazione;
		this.mFlagPiuMeno = aModel.mFlagPiuMeno;
		this.mNumAnniReclusione = aModel.mNumAnniReclusione;
		this.mNumMesiReclusione = aModel.mNumMesiReclusione;
		this.mNumGiorniReclusione = aModel.mNumGiorniReclusione;
		this.mImportoMulta = aModel.mImportoMulta;
		this.mNumAnniArresto = aModel.mNumAnniArresto;
		this.mNumMesiArresto = aModel.mNumMesiArresto;
		this.mNumGiorniArresto = aModel.mNumGiorniArresto;
		this.mImportoAmmenda = aModel.mImportoAmmenda;

		this.mDataReclusioneA = aModel.mDataReclusioneA;
		this.mDataReclusioneDa = aModel.mDataReclusioneDa;
		this.mDataArrestoA = aModel.mDataArrestoA;
		this.mDataArrestoDa = aModel.mDataArrestoDa;

		this.mDataRicezioneDoc = aModel.mDataRicezioneDoc;
		this.mMotivazioni = aModel.mMotivazioni;
		this.mNoteReclusione = aModel.mNoteReclusione;
		this.mAnnoGe = aModel.mAnnoGe;
		this.mNumeroGe = aModel.mNumeroGe;
		this.mAnnoRege = aModel.mAnnoRege;
		this.mNumeroRege = aModel.mNumeroRege;
		this.mAnnoMc = aModel.mAnnoMc;
		this.mNumeroMc = aModel.mNumeroMc;
		this.mAnnoCda = aModel.mAnnoCda;
		this.mNumeroCda = aModel.mNumeroCda;
		this.mAnnoCc = aModel.mAnnoCc;
		this.mNumeroCc = aModel.mNumeroCc;
		this.mAnnoSiep = aModel.mAnnoSiep;
		this.mNumeroSiep = aModel.mNumeroSiep;
		this.mCodTipoUfficioSiep = aModel.mCodTipoUfficioSiep;
		this.mDescrTipoUfficioSiep = aModel.mDescrTipoUfficioSiep;
		this.mCodLuogoUfficioSiep = aModel.mCodLuogoUfficioSiep;
		this.mDescrLuogoUfficioSiep = aModel.mDescrLuogoUfficioSiep;
		this.mDataIscrizioneSiep = aModel.mDataIscrizioneSiep;
		this.mCodFonte = aModel.mCodFonte;
		this.mDescrFonte = aModel.mDescrFonte;
		this.mAnnoFonte = aModel.mAnnoFonte;
		this.mNumeroFonte = aModel.mNumeroFonte;
		this.mCodSottonumerazione = aModel.mCodSottonumerazione;
		this.mDescrSottonumerazione = aModel.mDescrSottonumerazione;
		this.mComma = aModel.mComma;
		this.mLettera = aModel.mLettera;
		this.mNumero = aModel.mNumero;
		this.mArticolo = aModel.mArticolo;
		this.mCodCausaleComputo = aModel.mCodCausaleComputo;
		this.mDescrCausaleComputo = aModel.mDescrCausaleComputo;
		this.mCodDpr = aModel.mCodDpr;
		this.mDescrDpr = aModel.mDescrDpr;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mReaIdReato = aModel.mReaIdReato;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mFlagValidato = aModel.mFlagValidato;
		this.mFlagConforme = aModel.mFlagConforme;
		this.mFlagAppProvvisoria = aModel.mFlagAppProvvisoria;
		this.mPenResIdPenaResidua = aModel.mPenResIdPenaResidua;
		this.mFunIdFungibilita = aModel.mFunIdFungibilita;
		this.mDataRichiesta = aModel.mDataRichiesta;
		this.mDataCC = aModel.mDataCC;
		this.mDataGE = aModel.mDataGE;
		this.mAnnoSentenzaSiap = aModel.mAnnoSentenzaSiap;
		this.mNumeroSentenzaSiap = aModel.mNumeroSentenzaSiap;
		this.mDataSentenzaSiap = aModel.mDataSentenzaSiap;
		this.mBeneficio = aModel.mBeneficio;
		this.mAnnoIdAnnotazioneManuale = aModel.mAnnoIdAnnotazioneManuale;
		this.mFlagComputabile = aModel.mFlagComputabile;
		this.mFlagBeneficioDetratto = aModel.mFlagBeneficioDetratto;
		this.mSenIdSentenza = aModel.mSenIdSentenza;
		this.mTenIdTenoreSige = aModel.mTenIdTenoreSige;
		this.mFlagSelQuantum = aModel.mFlagSelQuantum;
		this.mAnnoSige = aModel.mAnnoSige;
		this.mNumeroSige = aModel.mNumeroSige;
	}

	// COSTRUTTORE MODEL
	public AnnotazioneManualeModel(BigDecimal aIdAnnotazioneManuale, String aCodTipoAnnotazione,
			String aDescrTipoAnnotazione, String aFlagPiuMeno, BigDecimal aNumAnniReclusione,
			BigDecimal aNumMesiReclusione, BigDecimal aNumGiorniReclusione, BigDecimal aImportoMulta,
			BigDecimal aNumAnniArresto, BigDecimal aNumMesiArresto, BigDecimal aNumGiorniArresto,
			BigDecimal aImportoAmmenda, Date aDataArrestoDa, Date aDataArrestoA, Date aDataReclusioneDa,
			Date aDataReclusioneA, Date aDataRicezioneDoc, String aMotivazioni, String aNoteReclusione,
			BigDecimal aAnnoGe, String aNumeroGe, BigDecimal aAnnoRege, String aNumeroRege,
			BigDecimal aAnnoMc, String aNumeroMc, BigDecimal aAnnoCda, String aNumeroCda, BigDecimal aAnnoCc,
			String aNumeroCc, BigDecimal aAnnoSiep, String aNumeroSiep, String aCodTipoUfficioSiep,
			String aDescrTipoUfficioSiep, String aCodLuogoUfficioSiep, String aDescrLuogoUfficioSiep,
			Date aDataIscrizioneSiep, String aCodFonte, String aDescrFonte, BigDecimal aAnnoFonte,
			String aNumeroFonte, String aCodSottonumerazione, String aDescrSottonumerazione, String aComma,
			String aLettera, String aNumero, String aArticolo, String aCodCausaleComputo,
			String aDescrCausaleComputo, String aCodDpr, String aDescrDpr, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aFasSieIdFascicoloSiep, BigDecimal aReaIdReato,
			BigDecimal aEveIdEvento, String aFlagValidato, String aFlagConforme, String aFlagAppProvvisoria,
			BigDecimal aPenResIdPenaResidua, BigDecimal aFunIdFungibilita, Date aDataRichiesta, Date aDataCC,
			Date aDataGE, BigDecimal aAnnoSentenzaSiap, String aNumeroSentenzaSiap, Date aDataSentenzaSiap,
			BigDecimal aAnnoIdAnnotazioneManuale, String aFlagComputabile, String aFlagBeneficioDetratto, BigDecimal aSenIdSentenza,
			BigDecimal aTenIdTenoreSige, String aFlagSelQuantum, BigDecimal aAnnoSige, BigDecimal aNumeroSige) {
		this.mIdAnnotazioneManuale = aIdAnnotazioneManuale;
		this.mCodTipoAnnotazione = aCodTipoAnnotazione;
		this.mDescrTipoAnnotazione = aDescrTipoAnnotazione;
		this.mFlagPiuMeno = aFlagPiuMeno;
		this.mNumAnniReclusione = aNumAnniReclusione;
		this.mNumMesiReclusione = aNumMesiReclusione;
		this.mNumGiorniReclusione = aNumGiorniReclusione;
		this.mImportoMulta = aImportoMulta;
		this.mNumAnniArresto = aNumAnniArresto;
		this.mNumMesiArresto = aNumMesiArresto;
		this.mNumGiorniArresto = aNumGiorniArresto;
		this.mImportoAmmenda = aImportoAmmenda;

		this.mDataArrestoDa = aDataArrestoDa;
		this.mDataArrestoA = aDataArrestoA;
		this.mDataReclusioneDa = aDataReclusioneDa;
		this.mDataReclusioneA = aDataReclusioneA;

		this.mDataRicezioneDoc = aDataRicezioneDoc;
		this.mMotivazioni = aMotivazioni;
		this.mNoteReclusione = aNoteReclusione;
		this.mAnnoGe = aAnnoGe;
		this.mNumeroGe = aNumeroGe;
		this.mAnnoRege = aAnnoRege;
		this.mNumeroRege = aNumeroRege;
		this.mAnnoMc = aAnnoMc;
		this.mNumeroMc = aNumeroMc;
		this.mAnnoCda = aAnnoCda;
		this.mNumeroCda = aNumeroCda;
		this.mAnnoCc = aAnnoCc;
		this.mNumeroCc = aNumeroCc;
		this.mAnnoSiep = aAnnoSiep;
		this.mNumeroSiep = aNumeroSiep;
		this.mCodTipoUfficioSiep = aCodTipoUfficioSiep;
		this.mDescrTipoUfficioSiep = aDescrTipoUfficioSiep;
		this.mCodLuogoUfficioSiep = aCodLuogoUfficioSiep;
		this.mDescrLuogoUfficioSiep = aDescrLuogoUfficioSiep;
		this.mDataIscrizioneSiep = aDataIscrizioneSiep;
		this.mCodFonte = aCodFonte;
		this.mDescrFonte = aDescrFonte;
		this.mAnnoFonte = aAnnoFonte;
		this.mNumeroFonte = aNumeroFonte;
		this.mCodSottonumerazione = aCodSottonumerazione;
		this.mDescrSottonumerazione = aDescrSottonumerazione;
		this.mComma = aComma;
		this.mLettera = aLettera;
		this.mNumero = aNumero;
		this.mArticolo = aArticolo;
		this.mCodCausaleComputo = aCodCausaleComputo;
		this.mDescrCausaleComputo = aDescrCausaleComputo;
		this.mCodDpr = aCodDpr;
		this.mDescrDpr = aDescrDpr;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mReaIdReato = aReaIdReato;
		this.mEveIdEvento = aEveIdEvento;
		this.mFlagValidato = aFlagValidato;
		this.mFlagConforme = aFlagConforme;
		this.mFlagAppProvvisoria = aFlagAppProvvisoria;
		this.mPenResIdPenaResidua = aPenResIdPenaResidua;
		this.mFunIdFungibilita = aFunIdFungibilita;
		this.mDataRichiesta = aDataRichiesta;
		this.mDataCC = aDataCC;
		this.mDataGE = aDataGE;
		this.mAnnoSentenzaSiap = aAnnoSentenzaSiap;
		this.mNumeroSentenzaSiap = aNumeroSentenzaSiap;
		this.mDataSentenzaSiap = aDataSentenzaSiap;
		this.mAnnoIdAnnotazioneManuale = aAnnoIdAnnotazioneManuale;
		this.mFlagComputabile = aFlagComputabile;
		this.mFlagBeneficioDetratto = aFlagBeneficioDetratto;
		this.mSenIdSentenza = aSenIdSentenza;
		this.mTenIdTenoreSige = aTenIdTenoreSige;
		this.mFlagSelQuantum = aFlagSelQuantum;
		this.mAnnoSige = aAnnoSige;
		this.mNumeroSige = aNumeroSige;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdAnnotazioneManuale() {
		return mIdAnnotazioneManuale;
	}

	public String getCodTipoAnnotazione() {
		return mCodTipoAnnotazione;
	}

	public String getDescrTipoAnnotazione() {
		return mDescrTipoAnnotazione;
	}

	public String getFlagPiuMeno() {
		return mFlagPiuMeno;
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

	public Date getDataReclusioneDa() {
		return mDataReclusioneDa;
	}

	public Date getDataReclusioneA() {
		return mDataReclusioneA;
	}

	public Date getDataArrestoDa() {
		return mDataArrestoDa;
	}

	public Date getDataArrestoA() {
		return mDataArrestoA;
	}

	public Date getDataRicezioneDoc() {
		return mDataRicezioneDoc;
	}

	public String getMotivazioni() {
		return mMotivazioni;
	}

	public String getNoteReclusione() {
		return mNoteReclusione;
	}

	public BigDecimal getAnnoGe() {
		return mAnnoGe;
	}

	public String getNumeroGe() {
		return mNumeroGe;
	}

	public BigDecimal getAnnoRege() {
		return mAnnoRege;
	}

	public String getNumeroRege() {
		return mNumeroRege;
	}

	public BigDecimal getAnnoMc() {
		return mAnnoMc;
	}

	public String getNumeroMc() {
		return mNumeroMc;
	}

	public BigDecimal getAnnoCda() {
		return mAnnoCda;
	}

	public String getNumeroCda() {
		return mNumeroCda;
	}

	public BigDecimal getAnnoCc() {
		return mAnnoCc;
	}

	public String getNumeroCc() {
		return mNumeroCc;
	}

	public BigDecimal getAnnoSiep() {
		return mAnnoSiep;
	}

	public String getNumeroSiep() {
		return mNumeroSiep;
	}

	public String getCodTipoUfficioSiep() {
		return mCodTipoUfficioSiep;
	}

	public String getDescrTipoUfficioSiep() {
		return mDescrTipoUfficioSiep;
	}

	public String getCodLuogoUfficioSiep() {
		return mCodLuogoUfficioSiep;
	}

	public String getDescrLuogoUfficioSiep() {
		return mDescrLuogoUfficioSiep;
	}

	public Date getDataIscrizioneSiep() {
		return mDataIscrizioneSiep;
	}

	public String getCodFonte() {
		return mCodFonte;
	}

	public String getDescrFonte() {
		return mDescrFonte;
	}

	public BigDecimal getAnnoFonte() {
		return mAnnoFonte;
	}

	public String getNumeroFonte() {
		return mNumeroFonte;
	}

	public String getCodSottonumerazione() {
		return mCodSottonumerazione;
	}

	public String getDescrSottonumerazione() {
		return mDescrSottonumerazione;
	}

	public String getComma() {
		return mComma;
	}

	public String getLettera() {
		return mLettera;
	}

	public String getNumero() {
		return mNumero;
	}

	public String getArticolo() {
		return mArticolo;
	}

	public String getCodCausaleComputo() {
		return mCodCausaleComputo;
	}

	public String getDescrCausaleComputo() {
		return mDescrCausaleComputo;
	}

	public String getCodDpr() {
		return mCodDpr;
	}

	public String getDescrDpr() {
		return mDescrDpr;
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

	public BigDecimal getReaIdReato() {
		return mReaIdReato;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public String getFlagValidato() {
		return mFlagValidato;
	}

	public String getFlagConforme() {
		return mFlagConforme;
	}

	public String getFlagAppProvvisoria() {
		return mFlagAppProvvisoria;
	}

	public BigDecimal getPenResIdPenaResidua() {
		return mPenResIdPenaResidua;
	}

	public BigDecimal getFunIdFungibilita() {
		return mFunIdFungibilita;
	}

	public Date getDataRichiesta() {
		return mDataRichiesta;
	}

	public Date getDataCC() {
		return mDataCC;
	}

	public Date getDataGE() {
		return mDataGE;
	}

	public BigDecimal getAnnoSentenzaSiap() {
		return mAnnoSentenzaSiap;
	}

	public String getNumeroSentenzaSiap() {
		return mNumeroSentenzaSiap;
	}

	public Date getDataSentenzaSiap() {
		return mDataSentenzaSiap;
	}

	public BigDecimal getAnnoIdAnnotazioneManuale() {
		return mAnnoIdAnnotazioneManuale;
	}

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public String getStringaArresto() {
		return mStringaArresto;
	}

	public String getBeneficio() {
		return mBeneficio;
	}

	public String getFlagComputabile() {
		return mFlagComputabile;
	}

	public String getFlagBeneficioDetratto() {
		return mFlagBeneficioDetratto;
	}

	public BigDecimal getSenIdSentenza() {
		return this.mSenIdSentenza;
	}

	public BigDecimal getTenIdTenoreSige() {
		return this.mTenIdTenoreSige;
	}

	public String getFlagSelQuantum() {
		return this.mFlagSelQuantum;
	}

	public BigDecimal getChiaveAnnoSige() {
		return mAnnoSige;
	}

	public BigDecimal getChiaveNumeroSige() {
		return mNumeroSige;
	}

	//
	// METODI SET()
	//
	public void setIdAnnotazioneManuale(BigDecimal aValore) {
		mIdAnnotazioneManuale = aValore;
	}

	public void setCodTipoAnnotazione(String aValore) {
		mCodTipoAnnotazione = aValore;
	}

	public void setDescrTipoAnnotazione(String aValore) {
		mDescrTipoAnnotazione = aValore;
	}

	public void setFlagPiuMeno(String aValore) {
		mFlagPiuMeno = aValore;
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

	public void setDataArrestoDa(Date aValore) {
		mDataArrestoDa = aValore;
	}

	public void setDataArrestoA(Date aValore) {
		mDataArrestoA = aValore;
	}

	public void setDataReclusioneDa(Date aValore) {
		mDataReclusioneDa = aValore;
	}

	public void setDataReclusioneA(Date aValore) {
		mDataReclusioneA = aValore;
	}

	public void setDataRicezioneDoc(Date aValore) {
		mDataRicezioneDoc = aValore;
	}

	public void setMotivazioni(String aValore) {
		mMotivazioni = aValore;
	}

	public void setNoteReclusione(String aValore) {
		mNoteReclusione = aValore;
	}

	public void setAnnoGe(BigDecimal aValore) {
		mAnnoGe = aValore;
	}

	public void setNumeroGe(String aValore) {
		mNumeroGe = aValore;
	}

	public void setAnnoRege(BigDecimal aValore) {
		mAnnoRege = aValore;
	}

	public void setNumeroRege(String aValore) {
		mNumeroRege = aValore;
	}

	public void setAnnoMc(BigDecimal aValore) {
		mAnnoMc = aValore;
	}

	public void setNumeroMc(String aValore) {
		mNumeroMc = aValore;
	}

	public void setAnnoCda(BigDecimal aValore) {
		mAnnoCda = aValore;
	}

	public void setNumeroCda(String aValore) {
		mNumeroCda = aValore;
	}

	public void setAnnoCc(BigDecimal aValore) {
		mAnnoCc = aValore;
	}

	public void setNumeroCc(String aValore) {
		mNumeroCc = aValore;
	}

	public void setAnnoSiep(BigDecimal aValore) {
		mAnnoSiep = aValore;
	}

	public void setNumeroSiep(String aValore) {
		mNumeroSiep = aValore;
	}

	public void setCodTipoUfficioSiep(String aValore) {
		mCodTipoUfficioSiep = aValore;
	}

	public void setDescrTipoUfficioSiep(String aValore) {
		mDescrTipoUfficioSiep = aValore;
	}

	public void setCodLuogoUfficioSiep(String aValore) {
		mCodLuogoUfficioSiep = aValore;
	}

	public void setDescrLuogoUfficioSiep(String aValore) {
		mDescrLuogoUfficioSiep = aValore;
	}

	public void setDataIscrizioneSiep(Date aValore) {
		mDataIscrizioneSiep = aValore;
	}

	public void setCodFonte(String aValore) {
		mCodFonte = aValore;
	}

	public void setDescrFonte(String aValore) {
		mDescrFonte = aValore;
	}

	public void setAnnoFonte(BigDecimal aValore) {
		mAnnoFonte = aValore;
	}

	public void setNumeroFonte(String aValore) {
		mNumeroFonte = aValore;
	}

	public void setCodSottonumerazione(String aValore) {
		mCodSottonumerazione = aValore;
	}

	public void setDescrSottonumerazione(String aValore) {
		mDescrSottonumerazione = aValore;
	}

	public void setComma(String aValore) {
		mComma = aValore;
	}

	public void setLettera(String aValore) {
		mLettera = aValore;
	}

	public void setNumero(String aValore) {
		mNumero = aValore;
	}

	public void setArticolo(String aValore) {
		mArticolo = aValore;
	}

	public void setCodCausaleComputo(String aValore) {
		mCodCausaleComputo = aValore;
	}

	public void setDescrCausaleComputo(String aValore) {
		mDescrCausaleComputo = aValore;
	}

	public void setCodDpr(String aValore) {
		mCodDpr = aValore;
	}

	public void setDescrDpr(String aValore) {
		mDescrDpr = aValore;
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

	public void setReaIdReato(BigDecimal aValore) {
		mReaIdReato = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setFlagValidato(String aValore) {
		mFlagValidato = aValore;
	}

	public void setFlagConforme(String aValore) {
		mFlagConforme = aValore;
	}

	public void setFlagAppProvvisoria(String aValore) {
		mFlagAppProvvisoria = aValore;
	}

	public void setPenResIdPenaResidua(BigDecimal aValore) {
		mPenResIdPenaResidua = aValore;
	}

	public void setFunIdFungibilita(BigDecimal aValore) {
		mFunIdFungibilita = aValore;
	}

	public void setDataRichiesta(Date aValore) {
		mDataRichiesta = aValore;
	}

	public void setDataCC(Date aValore) {
		mDataCC = aValore;
	}

	public void setDataGE(Date aValore) {
		mDataGE = aValore;
	}

	public void setAnnoSentenzaSiap(BigDecimal aValore) {
		mAnnoSentenzaSiap = aValore;
	}

	public void setNumeroSentenzaSiap(String aValore) {
		mNumeroSentenzaSiap = aValore;
	}

	public void setDataSentenzaSiap(Date aValore) {
		mDataSentenzaSiap = aValore;
	}

	public void setAnnoIdAnnotazioneManuale(BigDecimal aValore) {
		mAnnoIdAnnotazioneManuale = aValore;
	}

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public void setBeneficio(String aValore) {
		mBeneficio = aValore;
	}

	public void setFlagComputabile(String aValore) {
		mFlagComputabile = aValore;
	}

	public void setFlagBeneficioDetratto(String aValore) {
		mFlagBeneficioDetratto = aValore;
	}

	public void setChiaveAnnoSige(BigDecimal aValore) {
		mAnnoSige = aValore;
	}

	public void setChiaveNumeroSige(BigDecimal aValore) {
		mNumeroSige = aValore;
	}

	public void setSenIdSentenza(BigDecimal aSenIdSentenza) {
		this.mSenIdSentenza = aSenIdSentenza;
	};

	public void setTenIdTenoreSige(BigDecimal aTenIdTenoreSige) {
		this.mTenIdTenoreSige = aTenIdTenoreSige;
	}

	public void setFlagSelQuantum(String aSelFlagQuantum) {
		this.mFlagSelQuantum = aSelFlagQuantum;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdAnnotazioneManuale + " - " + mCodTipoAnnotazione + " - " + mDescrTipoAnnotazione
				+ " - " + mFlagPiuMeno + " - " + mNumAnniReclusione + " - " + mNumMesiReclusione + " - "
				+ mNumGiorniReclusione + " - " + mImportoMulta + " - " + mNumAnniArresto + " - "
				+ mNumMesiArresto + " - " + mNumGiorniArresto + " - " + mImportoAmmenda + " - " +

				mDataReclusioneDa + " - " + mDataReclusioneA + " - " + mDataArrestoDa + " - " + mDataArrestoA
				+ " - " +

				mDataRicezioneDoc + " - " + mMotivazioni + " - " + mNoteReclusione + " - " + mAnnoGe + " - "
				+ mNumeroGe + " - " + mAnnoRege + " - " + mNumeroRege + " - " + mAnnoMc + " - " + mNumeroMc
				+ " - " + mAnnoCda + " - " + mNumeroCda + " - " + mAnnoCc + " - " + mNumeroCc + " - "
				+ mAnnoSiep + " - " + mNumeroSiep + " - " + mCodTipoUfficioSiep + " - "
				+ mDescrTipoUfficioSiep + " - " + mCodLuogoUfficioSiep + " - " + mDescrLuogoUfficioSiep
				+ " - " + mDataIscrizioneSiep + " - " + mCodFonte + " - " + mDescrFonte + " - " + mAnnoFonte
				+ " - " + mNumeroFonte + " - " + mCodSottonumerazione + " - " + mDescrSottonumerazione
				+ " - " + mComma + " - " + mLettera + " - " + mNumero + " - " + mArticolo + " - "
				+ mCodCausaleComputo + " - " + mDescrCausaleComputo + " - " + mCodDpr + " - " + mDescrDpr
				+ " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - "
				+ mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mFasSieIdFascicoloSiep + " - " + mReaIdReato
				+ " - " + mEveIdEvento + " - " + mFlagValidato + " - " + mFlagConforme + " - "
				+ mFlagAppProvvisoria + " - " + mPenResIdPenaResidua + " - " + mFunIdFungibilita + " - "
				+ mDataRichiesta + " - " + mDataCC + " - " + mDataGE + " - " + mAnnoSentenzaSiap + " - "
				+ mNumeroSentenzaSiap + " - " + mDataSentenzaSiap + " - " + mBeneficio + " - "
				+ this.mSenIdSentenza + " - " + this.mTenIdTenoreSige + " - " + this.mFlagSelQuantum + " - "
				+ mAnnoSige + " - " + mNumeroSige;

		return lStr;
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

	/**
	 * Verifica se e' un'anticipazione cioe' lFlagAppProvv = 'A'
	 * 
	 * @return
	 */
	public boolean isAnticipazione() {
		boolean isAnticipazione = false;

		String lFlagAppProvv = getFlagAppProvvisoria();
		if (lFlagAppProvv != null && !lFlagAppProvv.equals("") && lFlagAppProvv.equals("A")) {
			isAnticipazione = true;
		}

		return isAnticipazione;
	}

	public boolean isValidato() {
		boolean isValidato = false;

		String lFlagValidato = getFlagValidato();
		if (lFlagValidato != null && !lFlagValidato.equals("") && lFlagValidato.equals("S")) {
			isValidato = true;
		}

		return isValidato;
	}

	/**
	 * Restituisce un calendarModel popolato con i quantum di reclusione e l'importo dell'ammenda
	 * 
	 * @return
	 */
	public CalendarModel getQuantumReclusione() {
		CalendarModel lCalReclusione = new CalendarModel();

		lCalReclusione.setNumGiorni(this.mNumGiorniReclusione);
		lCalReclusione.setNumMesi(this.mNumMesiReclusione);
		lCalReclusione.setNumAnni(this.mNumAnniReclusione);

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

		return lCalArresto;
	}

	/**
	 * 
	 * @param aCalReclusione
	 */
	public void setQuantumReclusione(CalendarModel aCalReclusione) {
		this.mNumGiorniReclusione = new BigDecimal(aCalReclusione.getNumGiorni());
		this.mNumMesiReclusione = new BigDecimal(aCalReclusione.getNumMesi());
		this.mNumAnniReclusione = new BigDecimal(aCalReclusione.getNumAnni());
	}

	/**
	 * 
	 * @param aCalArresto
	 */
	public void setQuantumArresto(CalendarModel aCalArresto) {
		this.mNumGiorniArresto = new BigDecimal(aCalArresto.getNumGiorni());
		this.mNumMesiArresto = new BigDecimal(aCalArresto.getNumMesi());
		this.mNumAnniArresto = new BigDecimal(aCalArresto.getNumAnni());
	}

	public String toString2() {
		String lStr = new String();

		lStr = "AnnotazioneManualeModel:\n" + "[ mIdAnnotazioneManuale      = " + mIdAnnotazioneManuale
				+ " ]\n" + "[ mFasSieIdFascicoloSiep     = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mEveIdEvento               = " + mEveIdEvento + " ]\n"
				+ "[ mFlagValidato              = " + mFlagValidato + " ]\n"
				+ "[ mFlagAppProvvisoria        = " + mFlagAppProvvisoria + " ]\n"
				+ "[ mCodTipoAnnotazione        = " + mCodTipoAnnotazione + " - " + mDescrTipoAnnotazione
				+ " ]\n" + "[ mFlagPiuMeno               = " + mFlagPiuMeno + " ]\n"
				+ "[ mNumAnniReclusione         = " + mNumAnniReclusione + " ]\n"
				+ "[ mNumMesiReclusione         = " + mNumMesiReclusione + " ]\n"
				+ "[ mNumGiorniReclusione       = " + mNumGiorniReclusione + " ]\n"
				+ "[ mImportoMulta              = " + mImportoMulta + " ]\n"
				+ "[ mNumAnniArresto            = " + mNumAnniArresto + " ]\n"
				+ "[ mNumMesiArresto            = " + mNumMesiArresto + " ]\n"
				+ "[ mNumGiorniArresto          = " + mNumGiorniArresto + " ]\n"
				+ "[ mImportoAmmenda            = " + mImportoAmmenda + " ]\n"
				+ "[ mFlagComputabile           = " + mFlagComputabile + " ]\n"
				+ "[ mAnnoIdAnnotazioneManuale  = " + mAnnoIdAnnotazioneManuale + " ]\n"
				+ "[ mMotivazioni               = " + mMotivazioni + " ]\n" + "[ mAnnoSige	              = "
				+ mAnnoSige + " ]\n" + "[ mNumeroSige	              = " + mNumeroSige + " ]";

		// "[ mCodOperatoreInserimento   = "+mCodOperatoreInserimento+" ]\n"+
		// "[ mDataInserimento           = "+mDataInserimento+" ]\n"+
		// "[ mCodUfficioInserimento     = "+mCodUfficioInserimento+" ]\n"+
		// "[ mCodOperatoreAggiornamento = "+mCodOperatoreAggiornamento+" ]\n"+
		// "[ mDataAggiornamento         = "+mDataAggiornamento+" ]\n"+
		// "[ mCodUfficioAggiornamento   = "+mCodUfficioAggiornamento+" ]";
		return lStr;
	}

}