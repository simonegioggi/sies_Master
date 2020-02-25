package siap.siep.misuracautelare.model;

/**
* <p>Title: MisuraCautelareModel</p>
* <p>Description: Classe Model che rappresenta il MisuraCautelare</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.calendar.model.CalendarModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;

public class MisuraCautelareModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 8916578654091046585L;
	private BigDecimal mIdMisuraCautelare;
	private String mCodTipoMisura;
	private String mDescrTipoMisura;
	private Date mDataInizio;
	private Date mDataFine;
	private Date mDataEmissioneOrdinanza;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private BigDecimal mGiorni;
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

	private String mFlagComputabile;
	private String mFlagModificaManuale;
	// nuovi
	private String mCodMotivoNonComputabile;
	private String mDescrMotivoNonComputabile;
	// modifica relativa al tipo istituto
	private String mIstDetIdIstitutoDetenzione;
	// private String mCodTipoIstitutoDetenzione;
	// private String mDescrTipoIstitutoDetenzione;
	private String mAltroLuogoDetenzione;
	// private String mCodLuogoDetenzione;
	// private String mDescrLuogoDetenzione;
	private String mNumRifer;
	private String mCodTipoUfficioRifer;
	private String mDescrTipoUfficioRifer;
	private String mCodLuogoUfficioRifer;
	private String mDescrLuogoUfficioRifer;
	private Date mDataFungibilita;
	private String mNote;
	// modifica relativa al tipo istituto
	private IstitutoDetenzioneModel mIstitutoDetenzione;

	// misure cautelari computabili e non computabili
	private BigDecimal mAnnoFascBdmc;
	private BigDecimal mNumeFascBdmc;
	private BigDecimal mAnnoRgnr;
	private BigDecimal mNumeroRgnr;
	private BigDecimal mAnnoRegGen;
	private BigDecimal mNumeroRegGen;
	private String mTipoUfficioRegGen;
	private String mAutoritaEmittente;
	private String mAutoritaEmittenteLuogo;
	private String mAutoritaEmittenteLuogoDesc;
	private String mAutoritaCompetente;
	private String mAutoritaCompetenteSede;
	private String mAutoritaCompetenteSedeDesc;
	private String mAutoritaCompetenteIndirizzo;
	private BigDecimal mAnnoRifer;
	private String mCodiceUfficioPmSede;
	private String mDescriceUfficioPmSede;
	private BigDecimal mPosGiuIdPosizioneGiuridica;
	private String mFlagEspiazionePenaIstitutoDetenzione;
	// private String mAutoritaCompetentePerTerritorio;

	// variabile utilizzata nelle stampe
	private String mAutoritaCompetenteDesc;
	private String mMisCautContinuativa;
	private String mMisCautTotParziale;

	// calcolo dei totali parziali
	// Se nell’elenco delle misure cautelari sono presenti 2 o più misure cautelari computabili
	// il sistema nella riga dell’ultima misura cautelare computabile continuativa
	// dovrà mostrare il totale espresso in Anni, Mesi e Giorni delle suddette
	// misure cautelari computabili continuative.
	private BigDecimal mNumTotAnniUltimaMisCauCom;
	private BigDecimal mNumTotMesiUltimaMisCauCom;
	private BigDecimal mNumTotGiorniUltimaMisCauCom;
	private String mFlagUltimaMisCauCommutabile;

	// COSTRUTTORE DI DEFAULT
	public MisuraCautelareModel() {
		this.mIdMisuraCautelare = null;
		this.mCodTipoMisura = "";
		this.mMisCautContinuativa = "";
		this.mDescrTipoMisura = "";
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mDataEmissioneOrdinanza = null;
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mNumTotAnniUltimaMisCauCom = null;
		this.mNumTotMesiUltimaMisCauCom = null;
		this.mNumTotGiorniUltimaMisCauCom = null;
		this.mGiorni = null;
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
		this.mFlagComputabile = "";
		this.mFlagModificaManuale = "";
		this.mFlagUltimaMisCauCommutabile = "";
		// nuovi
		this.mCodMotivoNonComputabile = "";
		this.mDescrMotivoNonComputabile = "";
		// modifica del 10/03/2004 per trattare l'id dell'istitituto come un BigDecimal
		// this.mIstDetIdIstitutoDetenzione = "";
		this.mIstDetIdIstitutoDetenzione = null;
		// this.mCodTipoIstitutoDetenzione = "";
		// this.mDescrTipoIstitutoDetenzione = "";
		this.mAltroLuogoDetenzione = "";
		// this.mCodLuogoDetenzione = "";
		// this.mDescrLuogoDetenzione = "";
		this.mNumRifer = "";
		this.mCodTipoUfficioRifer = "";
		this.mDescrTipoUfficioRifer = "";
		this.mCodLuogoUfficioRifer = "";
		this.mDescrLuogoUfficioRifer = "";
		this.mDataFungibilita = null;
		this.mNote = "";
		// modifica relativa al tipo istituto
		this.mIstitutoDetenzione = null;

		// misure cautelari computabili e non computabili
		this.mAnnoFascBdmc = null;
		this.mNumeFascBdmc = null;
		this.mAnnoRgnr = null;
		this.mNumeroRgnr = null;
		this.mAnnoRegGen = null;
		this.mNumeroRegGen = null;
		this.mTipoUfficioRegGen = "";
		this.mAutoritaEmittente = "";
		this.mAutoritaEmittenteLuogo = "";
		this.mAutoritaEmittenteLuogoDesc = "";
		this.mAutoritaCompetente = "";
		this.mAutoritaCompetenteSede = "";
		this.mAutoritaCompetenteSedeDesc = "";
		this.mAutoritaCompetenteIndirizzo = "";
		this.mAnnoRifer = null;
		this.mCodiceUfficioPmSede = "";
		this.mDescriceUfficioPmSede = "";
		this.mPosGiuIdPosizioneGiuridica = null;

		this.mFlagEspiazionePenaIstitutoDetenzione = "";
		// this.mAutoritaCompetentePerTerritorio="";
		this.mAutoritaCompetenteDesc = "";
		this.mMisCautContinuativa = "";
		this.mMisCautTotParziale = "";

	}

	// COSTRUTTORE DI COPIA
	public MisuraCautelareModel(MisuraCautelareModel aModel) {
		this.mIdMisuraCautelare = aModel.mIdMisuraCautelare;
		this.mCodTipoMisura = aModel.mCodTipoMisura;
		this.mMisCautContinuativa = aModel.mMisCautContinuativa;
		this.mDescrTipoMisura = aModel.mDescrTipoMisura;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mDataEmissioneOrdinanza = aModel.mDataEmissioneOrdinanza;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mNumTotAnniUltimaMisCauCom = aModel.mNumTotAnniUltimaMisCauCom;
		this.mNumTotMesiUltimaMisCauCom = aModel.mNumTotMesiUltimaMisCauCom;
		this.mNumTotGiorniUltimaMisCauCom = aModel.mNumTotGiorniUltimaMisCauCom;
		this.mGiorni = aModel.mGiorni;
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
		this.mFlagComputabile = aModel.mFlagComputabile;
		this.mFlagModificaManuale = aModel.mFlagModificaManuale;
		this.mFlagUltimaMisCauCommutabile = aModel.mFlagUltimaMisCauCommutabile;
		// nuovi
		this.mCodMotivoNonComputabile = aModel.mCodMotivoNonComputabile;
		this.mDescrMotivoNonComputabile = aModel.mDescrMotivoNonComputabile;
		// modifica relativa al tipo istituto
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		// this.mCodTipoIstitutoDetenzione = aModel.mCodTipoIstitutoDetenzione;
		// this.mDescrTipoIstitutoDetenzione = aModel.mDescrTipoIstitutoDetenzione;
		this.mAltroLuogoDetenzione = aModel.mAltroLuogoDetenzione;
		// this.mCodLuogoDetenzione = aModel.mCodLuogoDetenzione;
		// this.mDescrLuogoDetenzione = aModel.mDescrLuogoDetenzione;
		this.mNumRifer = aModel.mNumRifer;
		this.mCodTipoUfficioRifer = aModel.mCodTipoUfficioRifer;
		this.mDescrTipoUfficioRifer = aModel.mDescrTipoUfficioRifer;
		this.mCodLuogoUfficioRifer = aModel.mCodLuogoUfficioRifer;
		this.mDescrLuogoUfficioRifer = aModel.mDescrLuogoUfficioRifer;
		this.mDataFungibilita = aModel.mDataFungibilita;
		this.mNote = aModel.mNote;
		// modifica relativa al tipo istituto
		this.mIstitutoDetenzione = aModel.mIstitutoDetenzione;

		// misure cautelari computabili e non computabili
		this.mAnnoFascBdmc = aModel.mAnnoFascBdmc;
		this.mNumeFascBdmc = aModel.mNumeFascBdmc;
		this.mAnnoRgnr = aModel.mAnnoRgnr;
		this.mNumeroRgnr = aModel.mNumeroRgnr;
		this.mAnnoRegGen = aModel.mAnnoRegGen;
		this.mNumeroRegGen = aModel.mNumeroRegGen;
		this.mTipoUfficioRegGen = aModel.mTipoUfficioRegGen;
		this.mAutoritaEmittente = aModel.mAutoritaEmittente;
		this.mAutoritaEmittenteLuogo = aModel.mAutoritaEmittenteLuogo;
		this.mAutoritaEmittenteLuogoDesc = aModel.mAutoritaEmittenteLuogoDesc;
		this.mAutoritaCompetente = aModel.mAutoritaCompetente;
		this.mAutoritaCompetenteDesc = aModel.mAutoritaCompetenteDesc;
		this.mAutoritaCompetenteSede = aModel.mAutoritaCompetenteSede;
		this.mAutoritaCompetenteSedeDesc = aModel.mAutoritaCompetenteSedeDesc;
		this.mAutoritaCompetenteIndirizzo = aModel.mAutoritaCompetenteIndirizzo;
		this.mAnnoRifer = aModel.mAnnoRifer;
		this.mCodiceUfficioPmSede = aModel.mCodiceUfficioPmSede;
		this.mDescriceUfficioPmSede = aModel.mDescriceUfficioPmSede;
		this.mPosGiuIdPosizioneGiuridica = aModel.mPosGiuIdPosizioneGiuridica;

		this.mFlagEspiazionePenaIstitutoDetenzione = aModel.mFlagEspiazionePenaIstitutoDetenzione;
		// this.mAutoritaCompetentePerTerritorio=aModel.mAutoritaCompetentePerTerritorio;
	}

	// COSTRUTTORE MODEL
	public MisuraCautelareModel(BigDecimal aIdMisuraCautelare, String aCodTipoMisura, String aDescrTipoMisura,
			Date aDataInizio, Date aDataFine, Date aDataEmissioneOrdinanza, BigDecimal aNumAnni,
			BigDecimal aNumMesi, BigDecimal aNumGiorni, BigDecimal aGiorni,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aEveIdEvento,

			String aFlagComputabile, String aFlagModificaManuale, String aFlagUltimaMisCauCommutabile,
			String aCodMotivoNonComputabile, String aDescrMotivoNonComputabile,

			String aIstDetIdIstitutoDetenzione, String aAltroLuogoDetenzione,
			// String aCodLuogoDetenzione,
			// String aDescrLuogoDetenzione,

			String aNumRifer, String aCodTipoUfficioRifer, String aDescrTipoUfficioRifer,
			String aCodLuogoUfficioRifer, String aDescrLuogoUfficioRifer, Date aDataFungibilita, String aNote,
			// modifica relativa al tipo istituto
			IstitutoDetenzioneModel aIstitutoDetenzione

			// misure cautelari computabili e non computabili
			, BigDecimal aAnnoFascBdmc, BigDecimal aNumeFascBdmc, BigDecimal aAnnoRgnr,
			BigDecimal aNumeroRgnr, BigDecimal aAnnoRegGen, BigDecimal aNumeroRegGen,
			String aTipoUfficioRegGen, String aAutoritaEmittente, String aAutoritaEmittenteLuogo,
			String aAutoritaCompetente, String aAutoritaCompetenteSede, String aAutoritaCompetenteIndirizzo,
			BigDecimal aAnnoRifer, String aCodiceUfficioPmSede, String aDescriceUfficioPmSede,
			BigDecimal aPosGiuIdPosizioneGiuridica, String aFlagEspiazionePenaIstitutoDetenzione

	) {
		this.mIdMisuraCautelare = aIdMisuraCautelare;
		this.mCodTipoMisura = aCodTipoMisura;
		this.mDescrTipoMisura = aDescrTipoMisura;
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mDataEmissioneOrdinanza = aDataEmissioneOrdinanza;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
		this.mGiorni = aGiorni;
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
		this.mFlagComputabile = aFlagComputabile;
		this.mFlagModificaManuale = aFlagModificaManuale;
		this.mFlagUltimaMisCauCommutabile = aFlagUltimaMisCauCommutabile;
		// nuovi
		this.mCodMotivoNonComputabile = aCodMotivoNonComputabile;
		this.mDescrMotivoNonComputabile = aDescrMotivoNonComputabile;
		// modifica relativa al tipo istituto
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		// this.mCodTipoIstitutoDetenzione = aCodTipoIstitutoDetenzione;
		// this.mDescrTipoIstitutoDetenzione = aDescrTipoIstitutoDetenzione;
		this.mAltroLuogoDetenzione = aAltroLuogoDetenzione;
		// this.mCodLuogoDetenzione = aCodLuogoDetenzione;
		// this.mDescrLuogoDetenzione = aDescrLuogoDetenzione;
		this.mNumRifer = aNumRifer;
		this.mCodTipoUfficioRifer = aCodTipoUfficioRifer;
		this.mDescrTipoUfficioRifer = aDescrTipoUfficioRifer;
		this.mCodLuogoUfficioRifer = aCodLuogoUfficioRifer;
		this.mDescrLuogoUfficioRifer = aDescrLuogoUfficioRifer;
		this.mDataFungibilita = aDataFungibilita;
		this.mNote = aNote;
		// modifica relativa al tipo istituto
		this.mIstitutoDetenzione = aIstitutoDetenzione;

		// misure cautelari computabili e non computabili
		this.mAnnoFascBdmc = aAnnoFascBdmc;
		this.mNumeFascBdmc = aNumeFascBdmc;
		this.mAnnoRgnr = aAnnoRgnr;
		this.mNumeroRgnr = aNumeroRgnr;
		this.mAnnoRegGen = aAnnoRegGen;
		this.mNumeroRegGen = aNumeroRegGen;
		this.mTipoUfficioRegGen = aTipoUfficioRegGen;
		this.mAutoritaEmittente = aAutoritaEmittente;
		this.mAutoritaEmittenteLuogo = aAutoritaEmittenteLuogo;
		this.mAutoritaCompetente = aAutoritaCompetente;
		this.mAutoritaCompetenteSede = aAutoritaCompetenteSede;
		this.mAutoritaCompetenteIndirizzo = aAutoritaCompetenteIndirizzo;
		this.mAnnoRifer = aAnnoRifer;
		this.mCodiceUfficioPmSede = aCodiceUfficioPmSede;
		this.mDescriceUfficioPmSede = aDescriceUfficioPmSede;
		this.mPosGiuIdPosizioneGiuridica = aPosGiuIdPosizioneGiuridica;
		this.mFlagEspiazionePenaIstitutoDetenzione = aFlagEspiazionePenaIstitutoDetenzione;
		// this.mAutoritaCompetentePerTerritorio=aAutoritaCompetentePerTerritorio;

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdMisuraCautelare() {
		return mIdMisuraCautelare;
	}

	public String getCodTipoMisura() {
		return mCodTipoMisura;
	}

	public String getDescrTipoMisura() {
		return mDescrTipoMisura;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public Date getDataEmissioneOrdinanza() {
		return mDataEmissioneOrdinanza;
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

	public BigDecimal getNumTotAnniUltimaMisCauCom() {
		return mNumTotAnniUltimaMisCauCom;
	}

	public BigDecimal getNumTotMesiUltimaMisCauCom() {
		return mNumTotMesiUltimaMisCauCom;
	}

	public BigDecimal getNumTotGiorniUltimaMisCauCom() {
		return mNumTotGiorniUltimaMisCauCom;
	}

	public BigDecimal getGiorni() {
		return mGiorni;
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

	public String getFlagComputabile() {
		return mFlagComputabile;
	}

	public String getFlagModificaManuale() {
		return mFlagModificaManuale;
	}

	public String getFlagUltimaMisCauCommutabile() {
		return mFlagUltimaMisCauCommutabile;
	}

	// nuovi
	public String getCodMotivoNonComputabile() {
		return mCodMotivoNonComputabile;
	}

	public String getDescrMotivoNonComputabile() {
		return mDescrMotivoNonComputabile;
	}

	// modifica relativa al tipo istituto
	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	// public String getCodTipoIstitutoDetenzione() { return mCodTipoIstitutoDetenzione; }
	// public String getDescrTipoIstitutoDetenzione() { return mDescrTipoIstitutoDetenzione; }
	public String getAltroLuogoDetenzione() {
		return mAltroLuogoDetenzione;
	}

	// public String getCodLuogoDetenzione() { return mCodLuogoDetenzione; }
	// public String getDescrLuogoDetenzione() { return mDescrLuogoDetenzione; }
	public String getNumRifer() {
		return mNumRifer;
	}

	public String getCodTipoUfficioRifer() {
		return mCodTipoUfficioRifer;
	}

	public String getDescrTipoUfficioRifer() {
		return mDescrTipoUfficioRifer;
	}

	public String getCodLuogoUfficioRifer() {
		return mCodLuogoUfficioRifer;
	}

	public String getDescrLuogoUfficioRifer() {
		return mDescrLuogoUfficioRifer;
	}

	public Date getDataFungibilita() {
		return mDataFungibilita;
	}

	public String getNote() {
		return mNote;
	}

	public BigDecimal getAnnoFascBdmc() {
		return mAnnoFascBdmc;
	}

	public BigDecimal getNumeFascBdmc() {
		return mNumeFascBdmc;
	}

	public BigDecimal getAnnoRgnr() {
		return mAnnoRgnr;
	}

	public BigDecimal getNumeroRgnr() {
		return mNumeroRgnr;
	}

	public BigDecimal getAnnoRegGen() {
		return mAnnoRegGen;
	}

	public BigDecimal getNumeroRegGen() {
		return mNumeroRegGen;
	}

	public String getTipoUfficioRegGen() {
		return mTipoUfficioRegGen;
	}

	public String getAutoritaEmittente() {
		return mAutoritaEmittente;
	}

	public String getAutoritaEmittenteLuogo() {
		return mAutoritaEmittenteLuogo;
	}

	public String getAutoritaEmittenteLuogoDesc() {
		return mAutoritaEmittenteLuogoDesc;
	}

	public String getAutoritaCompetente() {
		return mAutoritaCompetente;
	}

	public String getAutoritaCompetenteSede() {
		return mAutoritaCompetenteSede;
	}

	public String getAutoritaCompetenteSedeDesc() {
		return mAutoritaCompetenteSedeDesc;
	}

	public String getAutoritaCompetenteIndirizzo() {
		return mAutoritaCompetenteIndirizzo;
	}

	public BigDecimal getAnnoRifer() {
		return mAnnoRifer;
	}

	public String getCodiceUfficioPmSede() {
		return mCodiceUfficioPmSede;
	}

	public String getDescriceUfficioPmSede() {
		return mDescriceUfficioPmSede;
	}

	public BigDecimal getPosGiuIdPosizioneGiuridica() {
		return mPosGiuIdPosizioneGiuridica;
	}

	public String getFlagEspiazionePenaIstitutoDetenzione() {
		return mFlagEspiazionePenaIstitutoDetenzione;
	}

	// public String getAutoritaCompetentePerTerritorio() { return mAutoritaCompetentePerTerritorio; }
	public String getAutoritaCompetenteDesc() {
		return mAutoritaCompetenteDesc;
	}

	public String getMisCautContinuativa() {
		return mMisCautContinuativa;
	}

	public String getMisCautTotParziale() {
		return mMisCautTotParziale;
	}

	public CalendarModel getQuantum() {
		CalendarModel lCalReclusione = new CalendarModel();

		lCalReclusione.setNumGiorni(this.mNumGiorni);
		lCalReclusione.setNumMesi(this.mNumMesi);
		lCalReclusione.setNumAnni(this.mNumAnni);

		return lCalReclusione;
	}

	// modifica relativa al tipo istituto
	public IstitutoDetenzioneModel getIstitutoDetenzione() {
		return mIstitutoDetenzione;
	}

	//
	// METODI SET()
	//

	public void setIdMisuraCautelare(BigDecimal aValore) {
		mIdMisuraCautelare = aValore;
	}

	public void setCodTipoMisura(String aValore) {
		mCodTipoMisura = aValore;
	}

	public void setDescrTipoMisura(String aValore) {
		mDescrTipoMisura = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setDataEmissioneOrdinanza(Date aValore) {
		mDataEmissioneOrdinanza = aValore;
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

	public void setNumTotAnniUltimaMisCauCom(BigDecimal aValore) {
		mNumTotAnniUltimaMisCauCom = aValore;
	}

	public void setNumTotMesiUltimaMisCauCom(BigDecimal aValore) {
		mNumTotMesiUltimaMisCauCom = aValore;
	}

	public void setNumTotGiorniUltimaMisCauCom(BigDecimal aValore) {
		mNumTotGiorniUltimaMisCauCom = aValore;
	}

	public void setGiorni(BigDecimal aValore) {
		mGiorni = aValore;
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

	public void setFlagComputabile(String aValore) {
		mFlagComputabile = aValore;
	}

	public void setFlagModificaManuale(String aValore) {
		mFlagModificaManuale = aValore;
	}

	public void setFlagUltimaMisCauCommutabile(String aValore) {
		mFlagUltimaMisCauCommutabile = aValore;
	}

	// nuovi
	public void setCodMotivoNonComputabile(String aValore) {
		mCodMotivoNonComputabile = aValore;
	}

	public void setDescrMotivoNonComputabile(String aValore) {
		mDescrMotivoNonComputabile = aValore;
	}

	// modifica relativa al tipo istituto
	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	// public void setCodTipoIstitutoDetenzione(String aValore ) { mCodTipoIstitutoDetenzione = aValore; }
	// public void setDescrTipoIstitutoDetenzione(String aValore ) { mDescrTipoIstitutoDetenzione = aValore; }
	public void setAltroLuogoDetenzione(String aValore) {
		mAltroLuogoDetenzione = aValore;
	}

	// public void setCodLuogoDetenzione(String aValore ) { mCodLuogoDetenzione = aValore; }
	// public void setDescrLuogoDetenzione(String aValore ) { mDescrLuogoDetenzione = aValore; }
	public void setNumRifer(String aValore) {
		mNumRifer = aValore;
	}

	public void setCodTipoUfficioRifer(String aValore) {
		mCodTipoUfficioRifer = aValore;
	}

	public void setDescrTipoUfficioRifer(String aValore) {
		mDescrTipoUfficioRifer = aValore;
	}

	public void setCodLuogoUfficioRifer(String aValore) {
		mCodLuogoUfficioRifer = aValore;
	}

	public void setDescrLuogoUfficioRifer(String aValore) {
		mDescrLuogoUfficioRifer = aValore;
	}

	public void setDataFungibilita(Date aValore) {
		mDataFungibilita = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	// modifica relativa al tipo istituto
	public void setIstitutoDetenzione(IstitutoDetenzioneModel aValore) {
		mIstitutoDetenzione = aValore;
	}

	public void setAnnoFascBdmc(BigDecimal aValore) {
		mAnnoFascBdmc = aValore;
	}

	public void setNumeFascBdmc(BigDecimal aValore) {
		mNumeFascBdmc = aValore;
	}

	public void setAnnoRgnr(BigDecimal aValore) {
		mAnnoRgnr = aValore;
	}

	public void setNumeroRgnr(BigDecimal aValore) {
		mNumeroRgnr = aValore;
	}

	public void setAnnoRegGen(BigDecimal aValore) {
		mAnnoRegGen = aValore;
	}

	public void setNumeroRegGen(BigDecimal aValore) {
		mNumeroRegGen = aValore;
	}

	public void setTipoUfficioRegGen(String aValore) {
		mTipoUfficioRegGen = aValore;
	}

	public void setAutoritaEmittente(String aValore) {
		mAutoritaEmittente = aValore;
	}

	public void setAutoritaEmittenteLuogo(String aValore) {
		mAutoritaEmittenteLuogo = aValore;
	}

	public void setAutoritaEmittenteLuogoDesc(String aValore) {
		mAutoritaEmittenteLuogoDesc = aValore;
	}

	public void setAutoritaCompetente(String aValore) {
		mAutoritaCompetente = aValore;
	}

	public void setAutoritaCompetenteSede(String aValore) {
		mAutoritaCompetenteSede = aValore;
	}

	public void setAutoritaCompetenteSedeDesc(String aValore) {
		mAutoritaCompetenteSedeDesc = aValore;
	}

	public void setAutoritaCompetenteIndirizzo(String aValore) {
		mAutoritaCompetenteIndirizzo = aValore;
	}

	public void setAnnoRifer(BigDecimal aValore) {
		mAnnoRifer = aValore;
	}

	public void setCodiceUfficioPmSede(String aValore) {
		mCodiceUfficioPmSede = aValore;
	}

	public void setDescriceUfficioPmSede(String aValore) {
		mDescriceUfficioPmSede = aValore;
	}

	public void setPosGiuIdPosizioneGiuridica(BigDecimal aValore) {
		mPosGiuIdPosizioneGiuridica = aValore;
	}

	public void setFlagEspiazionePenaIstitutoDetenzione(String aValore) {
		mFlagEspiazionePenaIstitutoDetenzione = aValore;
	}

	// public void setAutoritaCompetentePerTerritorio(String aValore) { mAutoritaCompetentePerTerritorio =
	// aValore; }
	public void setAutoritaCompetenteDesc(String aValore) {
		mAutoritaCompetenteDesc = aValore;
	}

	public void setMisCautContinuativa(String aValore) {
		mMisCautContinuativa = aValore;
	}

	public void setMisCautTotParziale(String aValore) {
		mMisCautTotParziale = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdMisuraCautelare + " - " + mCodTipoMisura + " - " + mDescrTipoMisura + " - "
				+ mDataInizio + " - " + mDataFine + " - " + mDataEmissioneOrdinanza + " - " + mNumAnni + " - "
				+ mNumMesi + " - " + mNumGiorni + " - " + mGiorni + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mFasSieIdFascicoloSiep + " - " + mEveIdEvento
				+ " - " + mCodMotivoNonComputabile + " - " + mDescrMotivoNonComputabile + " - " +
				// modifica relativa al tipo istituto
				mIstDetIdIstitutoDetenzione + " - " +
				// mCodTipoIstitutoDetenzione +" - " +
				// mDescrTipoIstitutoDetenzione +" - " +
				mAltroLuogoDetenzione + " - " +
				// mCodLuogoDetenzione +" - " +
				// mDescrLuogoDetenzione +" - " +
				mNumRifer + " - " + mCodTipoUfficioRifer + " - " + mDescrTipoUfficioRifer + " - "
				+ mCodLuogoUfficioRifer + " - " + mDescrLuogoUfficioRifer + " - " +
				// misure cautelari computabili e non computabili
				mAnnoFascBdmc + " - " + mNumeFascBdmc + " - " + mCodiceUfficioPmSede + " - " + mAnnoRgnr
				+ " - " + mNumeroRgnr + " - " + mAnnoRegGen + " - " + mNumeroRegGen + " - "
				+ mTipoUfficioRegGen + " - " + mAutoritaEmittente + " - " + mAutoritaEmittenteLuogo + " - "
				+ mAutoritaCompetente + " - " + mAutoritaCompetenteSede + " - " + mAutoritaCompetenteIndirizzo
				+ " - " + mAnnoRifer + " - " + mFlagEspiazionePenaIstitutoDetenzione + " - "
				+ mFlagModificaManuale + " - " + mDataFungibilita + " - " + mNote;
		// modifica relativa al tipo istituto
		if (mIstitutoDetenzione != null)
			lStr += "" + mIstitutoDetenzione;

		return lStr;
	}

}
