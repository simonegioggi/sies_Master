package siap.sico.decodifiche.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;

/**
 * DecodificheManagerBean - Classe con le piu' vecchie decodifiche
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DecodificheManagerBean {

	private Collection mNazioni;
	private Collection mProvince;
	private Collection mTipoProvvedimenti;
	private Collection mTipoProvvedimentiRif;
	private Collection mTipoProvvedimentiRifP;
	private Collection mTipoProvvedimentiRifPG;
	private Collection mTipoAvvocato;
	private Collection mTipoReato;
	private Collection mTipoFonteReato;
	private Collection mSottonumerazione;
	private Collection mPeriodoConsumazione;

	private Collection mTipoIstituto;
	private Collection mTipoDecisioneCassazione;
	private Collection mTenoreDecisioneRicorso;
	private Collection mTenoreDecisioneRicorsoSige;
	private Collection mStatoFascicolo;

	private Collection mTipoUfficio;
	private Collection mTipoUfficioPerCod;
	private Collection mTipoUfficioS;
	private Collection mTipoUfficioSius;
	private Collection<DecodificheModel> mTipoUfficioSiusTrattino;
	private Collection mTipoUfficioCertStatoEsec;

	private Collection mUfficioLogin;
	private Collection mTipoUfficioSosp;

	private Collection mSesso;
	private Collection mNazionalita;
	private Collection mTipoPenaAccessoria;
	private Collection mDurataPenaAccessoria;
	private Collection mTipoPenaDetentiva;
	private Collection mTipoPenaDetentivaErgastolo;
	private Collection mTipoMisuraCautelare;
	private Collection mTipoMisuraEsecuzione;

	private Collection mPosizioneGiuridicaIscrizione;
	private Collection mPosizioneGiuridicaIscrizioneCodiceMaschera;
	private Collection mPosizioneGiuridicaEsecuzione;
	private Collection mPosizioneGiuridicaAltraCausa;
	private Collection mPosizioneGiuridicaMisAlt;

	private Collection mPosizioneProcessuale;

	private Collection mFlagErgastolo;
	private Collection mFlagSN;
	private Collection mFlagLireEuro;
	private Collection mFlagSNTrattino;
	private Collection mSottoTipoBeneficio;

	private Collection mTipoBeneficio;
	private Collection mNaturaBeneficio;
	private Collection mTipoSospSubordinata;
	private Collection mDPR;

	private Collection mTipoNotifica;

	private Collection mEsitoNotifica;
	private Collection mEsitoProvvedimentoIstanza;
	private Collection mEsitoTenore;

	private Collection<DecodificheModel> mTipoAutorita;
	private Collection mTipoAutoritaSanzioni;
	private Collection mTipoAutoritaArresto;
	private Collection mTipoAutoritaEmittente;
	private Collection mTipoAutoritaCumulo;
	private Collection mTipoUfficioCumulo;
	private Collection mTipoUfficioCumuloSentenzaDecreto;
	private Collection mTipoAutoritaMinorenni;

	// Elenco TipiUfficiCumulo ( T+C+S )
	private Collection<DecodificheModel> mTipoUfficioCumuloRifSiep;

	// Elenco TipiUfficiCumulo ( T+C+S ) e UFFICIO_LOGIN (V)
	private Collection mTipoUfficioCumuloUfficioLoginRifSiep;

	// Elenco TipiUfficiCumulo M.Sic ( T+C+S+D )
	private Collection mTipoUfficioCumuloRifMSic;

	// modifica relativa al tipo istituto
	private Collection mTipoAutoritaIstituto;
	private Collection mTipoProcura;
	private Collection<DecodificheModel> mTipoUfficioSIUS;
	private Collection<DecodificheModel> mTipoUfficioSIUSMinor;
	private Collection mTipoUfficioSIEP;
	private Collection mTipoUfficioSIEPTrattino;

	private Collection mBilanciamentoCircostanze;
	private Collection mTipoAssoluzione;
	private Collection mTipoSanzioneSostitutiva;

	private Collection mTipoAtto;
	private Collection mStampeCumulo;
	private Collection mMittenteAtto;
	private Collection mRevocaDecSosp;
	private Collection mMotivoProvvedimento;
	private Collection mMotivoProvvedimentoIstanza;

	private Collection mOggettoProcedimento;
	private Collection mOggettoProcedimentoTDS;
	private Collection mOggettoProcedimentoUDS;
	private Collection mOggettoSospensioni;

	private Collection mFlagStato;

	private Collection mNaturaMisuraSicurezza;
	private Collection mTipoMisuraSicurezza;
	private Collection mStatoMisuraSicurezza;

	private Collection mTipoMisuraSicurezzaMinorenni;

	private Collection mDestinatarioDeposito;
	private Collection mDestinatarioDepositoMinorenni;

	private Collection mTipoSanzione;

	private Collection mMotivoNonComputabile;
	private Collection mMotivoNonComputabileDescCompleta;
	private Collection mTipoContinuazione;
	private Collection mTipoFirma;
	private Collection mTipoRicorso;
	private Collection mTipoRicorsoSige;
	private Collection mSoggettoImpugnante;
	private Collection mMotivoProvvedimentoMADDom;
	private Collection mMotivoProvvedimentoMAEspPressoDom; // 03/08/2010
	private Collection mMotivoProvvedimentoMASospEspPressoDom; // 27/09/2010
	private Collection mMotivoProvvedimentoMAPerEfficEspPressoDom; // 27/09/2010
	private Collection mMotivoProvvedimentoMARipristinoEspPressoDom; // 27/09/2010
	private Collection mMotivoProvvedimentoRevocaEspPressoDom; // 27/09/2010
	private Collection mMotivoProvvedimentoProsecProvvMAEspPressoDom; // 27/09/2010

	private Collection mMotivoProvvedimentoOS;
	private Collection mMotivoProvvedimentoOSLibAntMA;
	private Collection mMotivoProvvedimentoMAAffP;
	private Collection mMotivoProvvedimentoMASemiL;

	private Collection mMotivoProvvedimentoSospProvvDetDom;
	private Collection mMotivoProvvedimentoSospProvvAffP;
	private Collection mMotivoProvvedimentoSospProvvSemi;

	private Collection mMotivoProvvedimentoRevocaDetDom;
	private Collection mMotivoProvvedimentoRevocaAffP;
	private Collection mMotivoProvvedimentoRevocaSemi;
	private Collection mMotivoProvvedimentoRevocaIndultino;

	private Collection mMotivoSollecitoMisureSicurezza; // d.f. 05/08/2014

	// 02/12/2010 Inizio : Aggiunti Daniela
	private Collection mMotivoProvvedimentoCessazioneDetDom;
	private Collection mMotivoProvvedimentoCessazioneAffP;
	private Collection mMotivoProvvedimentoCessazioneSemi;
	private Collection mMotivoProvvedimentoCessazioneIndultino;
	private Collection mMotivoProvvedimentoCessazioneDetDomTerm;
	private Collection mMotivoProvvedimentoCessazioneEspPressoDom; // 27/09/2010
	// 02/12/2010 Fine

	// 02/2014 DL146/2013 codici di cessazione 51bis MDS
	private Collection mMotivoProvvedimentoCessazioneAffPMDS51bis;
	private Collection mMotivoProvvedimentoCessazioneDetDomMDS51bis;
	private Collection mMotivoProvvedimentoCessazioneSemiMDS51bis;
	private Collection mMotivoProvvedimentoCessazioneIndultinoMDS51bis;
	private Collection mMotivoProvvedimentoCessazioneDetDomTermMDS51bis;
	private Collection mMotivoProvvedimentoCessazioneEspPressoDomMDS51bis;

	// 02/2014 DL146/2013 codici di cessazione 51bis TDS su Reclamo PM
	private Collection mMotivoProvvedimentoCessazioneAffPTDS51bis;
	private Collection mMotivoProvvedimentoCessazioneDetDomTDS51bis;
	private Collection mMotivoProvvedimentoCessazioneSemiTDS51bis;
	private Collection mMotivoProvvedimentoCessazioneDetDomTermTDS51bis;
	private Collection mMotivoProvvedimentoCessazioneEspPressoDomTDS51bis;

	private Collection mMotivoProvvedimentoRipristinoMAffP;
	private Collection mMotivoProvvedimentoRipristinoDetDom;
	private Collection mMotivoProvvedimentoRipristinoSemi;

	private Collection mMotivoProvvedimentoDicEffMAAffP;
	private Collection mMotivoProvvedimentoMAPreEffAffPro;
	private Collection mMotivoProvvedimentoMAPreEffDetDom;
	private Collection mMotivoProvvedimentoMAPreEffSemli;

	private Collection mMotivoProvvedimentoRigettoMA;

	private Collection mMotivoProvvedimentoDetDomSpecAmmiPeriodo;
	private Collection mMotivoProvvedimentoDetDomSpecSospProvv;

	private Collection mMotivoProvvedimentoMAConIndultino;
	private Collection mMotivoProvvedimentoSospProvvMAIndultino;
	private Collection mMotivoProvvedimentoMAPerEfficIndultino;
	private Collection mMotivoProvvedimentoMARipristinoIndultino;

	// sia per Prosecuzione Provvisoria che Sospensione 51 bis
	private Collection mMotivoProvvedimentoProsecProvvMAAffPro;
	private Collection mMotivoProvvedimentoProsecProvvMADetDom;
	private Collection mMotivoProvvedimentoProsecProvvMASem;
	private Collection mMotivoProvvedimentoProsecProvvMAIndultino;
	// private Collection mMotivoProvvedimentoProsecProvvMADetDomTer;

	// DL 146/2013
	private Collection mMotivoProvvedimentoProsecMAAffProMDS51Bis;
	private Collection mMotivoProvvedimentoProsecMADetDomMDS51Bis;
	private Collection mMotivoProvvedimentoProsecMASemMDS51Bis;
	private Collection mMotivoProvvedimentoProsecMAEsecPreDomMDS51Bis;
	private Collection mMotivoProvvedimentoProsecMADetDomTerMDS51Bis;

	private Collection mMotivoProvvedimentoProsecMAAffProTDS51Bis;
	private Collection mMotivoProvvedimentoProsecMADetDom_TDS_51Bis;
	private Collection mMotivoProvvedimentoProsecMASem_TDS_51Bis;
	private Collection mMotivoProvvedimentoProsecMAEsecPreDom_TDS_51Bis;
	private Collection mMotivoProvvedimentoProsecMADetDomTer_TDS_51Bis;
	// fine DL 146/2013

	private Collection mMotivoProvvedimentoEstDefMAffP;
	private Collection mMotivoProvvedimentoEstDefMADetDom;
	private Collection mMotivoProvvedimentoEstDefMASem;

	private Collection mMotivoProvvedimentoProrogaUlteriorePeriodo;
	private Collection mMotivoProvvedimentoRipristinoDetDomSpec;
	private Collection mMotivoProvvedimentoAmmProvDetDom;
	private Collection mMotivoProvvedimentoAmmProvAffi;
	// MEV_2019-09-SIEP
	private Collection mMotivoProvvedimentoAmmProvDetDomPmm;
	private Collection mMotivoProvvedimentoAmmProvAffiPmm;
	private Collection mMotivoProvvedimentoAmmProvSemilibPm;
	private Collection mMotivoProvvedimentoAmmProvSemilibPmm;
	private Collection mMotivoProvvedimentoMAAffPMinor;
	private Collection mMotivoProvvedimentoMADDomMinor;
	private Collection mMotivoProvvedimentoMASemiLMinor;
	// MEV_2019-09-SIEP - FINE

	private Collection mMotivoProvvedimentoMADetDomTemp;
	private Collection mMotivoProvvedimentoMADetDomTempProroga;
	private Collection mMotivoProvvedimentoMADetDomTempProrogaProvvisoria;
	private Collection mMotivoProvvedimentoMAReLibCond;
	private Collection mMotivoProvvedimentoMACOLibCond;
	private Collection mMotivoProvvedimentoUlteriorePeriodoMA;
	private Collection mTipoAnnotazioneManuale;

	private Collection mTipoAnnotazioneManualeBenefici;
	private Collection mRideterminazionePenaAltro;
	private Collection mRideterminazionePenaAltroUff; // d'ufficio
	private Collection mRideterminazionePenaAltroAUff; // su provv altro ufficio
	private Collection mRideterminazionePenaAltroSORV; // su provv SORV
	private Collection mRideterminazionePenaAltroGE; // su provv GE

	// annotazione manuale MC
	private Collection mTipoAnnotazioneManualeMC;
	private Collection mTipoAnnotazioneManualeTutte;
	private Collection mTipoCausaleComputoMCSenzaTitolo;
	private Collection mTipoCausaleComputoMCAltroTitolo;

	// Conversione Pene Pecuniaria
	private Collection mOggettiConversionePP;

	// MA Detenzione Domiciliare Speciale Affidamento
	private Collection mMotivoProvvedimentoMADetDomSpeAmmAff;
	private Collection mTipoDecreto;
	private Collection mStatoLibertatis;
	private Collection mTipoOrdinanza;
	private Collection mTipoEvento; // 01/03/2004
	private Collection mStatoPermesso; // 13/05/2004
	private Collection mTipoRegistroOrdinanza;
	private Collection mListaAutoritaSospensione;
	private Collection mListaOggettiSospensione;
	private Collection mListaOggettiRevoca;
	private Collection mListaMotiviProvvedimentoSospensione;
	private Collection mListaMotiviProvvedimentoRevoca;
	private Collection mListaEsitiTenoreSospensione;
	private Collection mListaEsitiTenoreRevoca;
	private Collection mRegistroProvGenerico;
	private Collection mTipoProvvedimentoCumulo;
	private Collection mListaAttivitaAvvocato;
	private Collection mListaAutoritaSospTDSUDS;
	private Collection mListaAutoritaSospTDSUDSTrattino;
	private Collection mOggettoSospensioneDiff;
	private Collection mOggettoSospensioneDiffProvv;
	private Collection mOggettoSospensioneDiffDef;
	private Collection mOggettoRevocaDiff;

	private Collection mTipologiaDecisioneSospensioneDiff;
	private Collection mTipologiaDecisioneSospensioneDiffProvv;
	private Collection mTipologiaDecisioneSospensioneDiffDef;
	private Collection mTipologiaDecisioneRevocaDiff;
	private Collection mTipologiaDecisioneRigettoDiff;

	private Collection mSottoTipoBeneficioIndulto;
	private Collection mMotivoDesignazione;
	private Collection mMotivoInterruzione;
	private Collection mMotivoNLP;
	private Collection mMotivoFineEspiazione;
	private Collection mMotivoDefiAltro;

	private Collection mMotivoRevocaSS;

	private Collection mMotivoDefiGe;
	private Collection mMotivoDefAltro;
	private Collection mMotivoDefiSor;

	private Collection mAutoritaSorveglianza;
	private Collection mAutoritaGE;
	private Collection mAutoritaAltro;

	private Collection mMotivoRDS;
	private Collection mMotivoProvvedimentoEspulsione;
	private Collection mMotivoProvvedimentoAccoglieOpEspulsione;
	private Collection mMotivoProvvedimentoRigettoOpEspulsione;

	// TIPO SOSPENSIONE/INTERRUZIONE
	private Collection mTipoIntSosp;
	private Collection mMotivoIntSosp;

	// RICHIESTE GENERICHE
	private Collection mTipoRichiestaRC;
	private Collection mTipoRichiestaPVR;
	private Collection mTipoRichiestaT;
	private Collection mTipoRichiestaTrasmComp;
	private Collection mMotivoProvvedimentiRichGen;

	private Collection mTipoUfficioGE;
	private Collection mTipoUfficioPM;

	private Collection mTipoProvvSorveglianza;
	private Collection mMotivoSospensionePm;
	private Collection mOggettoDecisione;
	// MEV_2019-09-SIEP - nuovi oggetti sospensione art 678 e minori
	private Collection mOggettoDecisioneMinor;
	private Collection mOggettiDecisioneSosp678;
	// MEV_2019-09-SIEP - FIME
	// private Collection mEsitoSiep;
	private Collection mTipoPermesso; // 23/07/2004
	private Collection mStatoProcedimento; // 8/9/2004

	private Collection mTipoLicenza; // 28/04/2005
	private Collection mTipoContenuto;
	private Collection mTipoContenutoIstanza;
	private Collection mTipoRitoSentenza; // 07/07/2006
	private Collection mVistaPm;

	// Aggiunto per mev a8-rr-002
	private Collection mQuesture;

	// SIUS Sanzioni Sostitutive
	private Collection mMotivoSanzioneSostitutiva;
	private Collection mPosizioniSanzione;
	private Collection mTipoSanzioneConvertita = null;

	// SIUS Misure Sicurezza
	private Collection mMotivoMisuraSicurezza;

	private Collection mAutoritaRdpGE;
	private Collection mAutoritaRdpAltro;
	private Collection mAutoritaRdpSorv;

	private Collection mTrasmissioni;

	private Collection mStatoCittadinanza;
	private Collection mTipoProvvedimentoMSic;

	private Collection mTipologiaNumerazione;

	private Collection mMotivoNonInvio; // 24/10/2013

	private Collection mAutoritaCompetente;

	private Collection<DecodificheModel> mListaTipoProvvCumulo;
	private Collection<DecodificheModel> mListaTipoProvvOrdinanza;
	private Collection<DecodificheModel> mListaTipoProvvDecretoArchiviazione;

	// 15-01-2015 combo Autorita Emittente in Associa Titolo Esecutivo a Misura Sicurezza
	private Collection mAutoritaEmi_Sorveglianza = null;

	private Collection mTipoSentenza;

	// 01-09-2015 MEV_2 - Mis Sic STEP2
	private Collection mOggettoProcedimentoMS;

	// MEV26 - Tipo Reg.Gen. x Titolo Cumulato
	private Collection mTipoRegistroGenerale;

	// FIXME MEV26 ma da utilizzare la versione LPU ufficiale
	private Collection mTipoSanzioneSostitutivaLpu;

	// MEV26 - Cumulo Sottodomini Posizioni Giuridica
	private Collection mPosizioniGiuridicheCumLibero;
	private Collection mPosizioniGiuridicheCumEspIst;
	private Collection mPosizioniGiuridicheCumEspAltro;

	// MEV26 - Cumulo - Sottodominio Motivo Provvedimento
	private Collection mMotiviProvvCumuloNew;

	private Collection mTipoUfficioSez1DDTrattino;

	// MEV10-s3: aggiunte collection per gestire invio mail segnalazione
	private Collection mTitoloPersona;
	private Collection mFunzionalitaSIEPE;
	private Collection mFunzionalitaSIUS;
	private Collection mFunzionalitaSIEP;
	private Collection mFunzionalitaSIGE;
	private Collection mAzione;
	private Collection mTipoSegnalazione;
	private Collection mGravitaSegnalazione;
	// altre gestioni
	private Collection<DecodificheModel> mTipoUfficioSiusTDSMUDSM;
	private Collection<DecodificheModel> mTipoMSMinorenniNonDetentiva;
	private Collection<DecodificheModel> mTipoMSMinorenniDetentiva;
	private Collection mOggettoProcedimentoTDSM;
	private Collection mOggettoProcedimentoUDSM;
	// FINE MEV10-s3
	private Collection<DecodificheModel> mTipoUfficioSiepTDSMUDSM;
	private Collection<DecodificheModel> mTipoUffEsePenEstSerSocMin;

	private Collection mTipoUfficioSige;

	private Collection mSoggettoImpugnanteSige;

	// MEV_2023-13: aggiunta collezione per il tipo tutore
	private Collection mTipoTutore;
	// MEV_2023-13: aggiunta collezione per la ragione sociale
	private Collection mRagioneSocialeCO;

	// MEV_2023-13
	private Collection mTipoPenaSostitutiva;
	// MEV_2023-33: aggiunta collezione per le autorita' di polizia (HighValue='AP')
	private Collection mTipoAutoritaPolizia;

	// MEV_2023-35 Lista Oggetti Esecuzione Pene Sostitutive (U126)
	private Collection mMotivoEsecuzionePeneSostitutive;
	// MEV_2023-35 Lista Oggetti Sospensione esecuzione pene accessorie (U141, C066)
	private Collection mTipoPenaAccessoriaPS;

	/**
	 * Inizializzazione degli attributi del Singleton
	 */
	protected void init() {
		DecodificheModel lModel = new DecodificheModel();

		try {
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();

			lModel.setContesto("TIPO_ANNOTAZIONE");
			lModel.setCodiceAlternativo("B");
			mTipoAnnotazioneManualeBenefici = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setCodiceAlternativo("M");

			DecodificheModel lDecModCau = new DecodificheModel();
			lDecModCau.setContesto("TIPO_ANNOTAZIONE");
			lDecModCau.setCode("-");
			lDecModCau.setDescription("-");
			List lListP = new ArrayList();
			// Aggiungo alla collection un '-'
			lListP.add(lDecModCau);
			lListP.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mTipoAnnotazioneManualeMC = lListP;

			lModel.setCodiceAlternativo("");
			mTipoAnnotazioneManualeTutte = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("CAUSALE_COMPUTO");
			lModel.setCodiceAlternativo("PDAR");
			mTipoCausaleComputoMCSenzaTitolo = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setCodiceAlternativo("MCAR");
			mTipoCausaleComputoMCAltroTitolo = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("");
			lModel.setCodiceAlternativo("");

			//
			DecodificheModel lModelPP = new DecodificheModel();
			lModelPP.setContesto("MOTIVO_PROVVEDIMENTO");
			lModelPP.setCodiceAlternativo("U070"); // RV_HIGH_VALUE
			mOggettiConversionePP = lDecodifiche.ExRicercaDecodifiche(lModelPP);

			// Rideterminazione Pena Altro
			lModel.setCodiceAlternativo("RIDPE");
			DecodificheModel lDecModRidPena = new DecodificheModel();
			lDecModRidPena.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModRidPena.setCode("-");
			lDecModRidPena.setDescription("-");
			List lListRidPena = new ArrayList();
			// Aggiungo alla collection un '-'
			lListRidPena.add(lDecModRidPena);
			lListRidPena.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mRideterminazionePenaAltro = lListRidPena;

			// Nuovi codici per rideterminazione pena Altro (4.0)
			List lListRidPenaUff = new ArrayList();
			// Aggiungo alla collection un '-'
			DecodificheModel lDecModRidPenaUff = new DecodificheModel();
			lDecModRidPenaUff.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModRidPenaUff.setCode("-");
			lDecModRidPenaUff.setDescription("-");
			lListRidPenaUff.add(lDecModRidPenaUff);
			// Carico i dati
			lModel.setCodiceAlternativo("RIDPE_UFF");
			lListRidPenaUff.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mRideterminazionePenaAltroUff = lListRidPenaUff;
			lModel.setContesto("");
			lModel.setCodiceAlternativo("");

			// Altro ufficio
			List lListRidPenaAUff = new ArrayList();
			// Aggiungo alla collection un '-'
			DecodificheModel lDecModRidPenaAUff = new DecodificheModel();
			lDecModRidPenaAUff.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModRidPenaAUff.setCode("-");
			lDecModRidPenaAUff.setDescription("-");
			lListRidPenaAUff.add(lDecModRidPenaAUff);
			// Carico i dati
			lModel.setCodiceAlternativo("RIDPE_AUFF");
			lListRidPenaAUff.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mRideterminazionePenaAltroAUff = lListRidPenaAUff;
			lModel.setContesto("");
			lModel.setCodiceAlternativo("");

			List lListRidPenaGE = new ArrayList();
			// Aggiungo alla collection un '-'
			DecodificheModel lDecModRidPenaGE = new DecodificheModel();
			lDecModRidPenaGE.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModRidPenaGE.setCode("-");
			lDecModRidPenaGE.setDescription("-");
			lListRidPenaGE.add(lDecModRidPenaGE);
			// Carico i dati
			lModel.setCodiceAlternativo("RIDPE_GE");
			lListRidPenaGE.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mRideterminazionePenaAltroGE = lListRidPenaGE;
			lModel.setContesto("");
			lModel.setCodiceAlternativo("");

			List lListRidPenaSORV = new ArrayList();
			// Aggiungo alla collection un '-'
			DecodificheModel lDecModRidPenaSORV = new DecodificheModel();
			lDecModRidPenaSORV.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModRidPenaSORV.setCode("-");
			lDecModRidPenaSORV.setDescription("-");
			lListRidPenaSORV.add(lDecModRidPenaSORV);
			// Carico i dati
			lModel.setCodiceAlternativo("RIDPE_SORV");
			lListRidPenaSORV.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mRideterminazionePenaAltroSORV = lListRidPenaSORV;
			lModel.setContesto("");
			lModel.setCodiceAlternativo("");

			lModel.setCodiceAlternativo("ANN_MAN");
			DecodificheModel lDecModAnn = new DecodificheModel();
			lDecModAnn.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModAnn.setCode("-");
			lDecModAnn.setDescription("-");
			List lListAnn = new ArrayList();
			// Aggiungo alla collection un '-'
			lListAnn.add(lDecModAnn);
			lListAnn.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mTipoAnnotazioneManuale = lListAnn;

			// Autorità Provvedimento Altro Ufficio Rideterminazione Pena Altro
			lModel.setCodiceAlternativo("");
			lModel.setContesto("TIPO_UFFICIO_RPA");
			lModel.setFiltro("RPA_GE");
			mAutoritaRdpGE = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_UFFICIO_RPA");
			lModel.setFiltro("RPA_ALTRO");
			mAutoritaRdpAltro = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_UFFICIO_RPA");
			lModel.setFiltro("RPA_SORV");
			mAutoritaRdpSorv = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setFiltro("");
			lModel.setCodiceAlternativo("");
			lModel.setContesto("NAZIONE");
			mNazioni = lDecodifiche.ExRicercaDecodifiche(lModel);

			// Ambrosino 04/2010 - SuperSoggetto - Inseriemnto Soggetto
			// Il campo Nazionalità viene sostituito da 'Stato di Cittadinanza'.
			// Lo 'Stato di Cittadinanza' è una combo dove sono presenti tutti gli stati
			// (stessa collection di 'Stato di Nascita').
			// Sul pregresso occorre impostare un valore di ‘default’.

			lModel.setFiltro("");
			lModel.setCodiceAlternativo("");
			lModel.setContesto("NAZIONE");
			mStatoCittadinanza = lDecodifiche.ExRicercaDecodifiche(lModel);
			//
			lModel.setContesto("PROVINCIA");
			mProvince = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_PROVVEDIMENTO");
			mTipoProvvedimenti = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_PROVVEDIMENTO_RIF");
			mTipoProvvedimentiRif = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_PROVVEDIMENTO_RIF_P");
			mTipoProvvedimentiRifP = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_DECISIONE_CASSAZIONE");
			mTipoDecisioneCassazione = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TENORE_DECISIONE_RICORSO");
			mTenoreDecisioneRicorso = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TENORE_DECISIONE_RICORSO_SIGE");
			mTenoreDecisioneRicorsoSige = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_AVVOCATO");
			mTipoAvvocato = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_UFFICIO");
			mTipoUfficio = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_UFFICIO");
			mTipoUfficioPerCod = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);

			lModel.setContesto("SOTTOTIPO_BENEFICIO");
			lModel.setFiltro("INDULTO");
			DecodificheModel lDecModSottotipo = new DecodificheModel();
			lDecModSottotipo.setContesto("SOTTOTIPO_BENEFICIO");
			lDecModSottotipo.setCode("-");
			lDecModSottotipo.setDescription("-");
			List lListSotto = new ArrayList();
			// Aggiungo alla collection un '-'
			lListSotto.add(lDecModSottotipo);
			lListSotto.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mSottoTipoBeneficioIndulto = lListSotto;
			lModel.setFiltro("");

			lModel.setContesto("TIPO_UFFICIO_GE");
			mTipoUfficioGE = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_UFFICIO_PM");
			mTipoUfficioPM = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_UFFICIO");
			lModel.setFiltro("S");
			mTipoUfficioS = lDecodifiche.ExRicercaDecodifiche(lModel);
			// a8-rr-067
			mTipoUfficioS.add(new DecodificheModel("PGCAP",
					"Procura Generale della Repubblica Presso la Corte D'Appello", "TIPO_UFFICIO", "", "T",
					"", "", "", ""));
			lModel.setFiltro(""); // Ripulisce la condizione di filtro

			mTipoUfficioCertStatoEsec = new ArrayList(mTipoUfficioS);
			mTipoUfficioCertStatoEsec.add(new DecodificheModel("PGCAP",
					"Procura Generale della Repubblica Presso la Corte D'Appello", "TIPO_UFFICIO", "", "T",
					"", "", "", ""));
			/*
			 * modifica a7/rr/094
			 */
			mTipoUfficioCertStatoEsec.add(new DecodificheModel("PMM",
					"Procura della Repubblica Presso il Tribunale per i Minorenni", "TIPO_UFFICIO", "", "",
					"", "", "", ""));

			lModel.setContesto("TIPO_UFFICIO");
			lModel.setCodiceAlternativo("T");
			mTipoUfficioSius = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCodiceAlternativo(""); // Ripulisce la condizione di CodiceAlternativo

			lModel.setContesto("TIPO_UFFICIO");
			mTipoUfficioSiusTrattino = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCodiceAlternativo(""); // Ripulisce la condizione di CodiceAlternativo

			lModel.setContesto("UFFICIO_LOGIN");
			mUfficioLogin = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_UFFICIO_SOSP");
			mTipoUfficioSosp = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("STATO_FASCICOLO");
			mStatoFascicolo = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_REATO");
			mTipoReato = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_ISTITUTO");
			mTipoIstituto = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_UFFICIO_EMITTENTE");
			mTipoAutoritaEmittente = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("AUTORITA_CUMULO");
			DecodificheModel lDecAutorita = new DecodificheModel();
			lDecAutorita.setContesto("AUTORITA_CUMULO");
			lDecAutorita.setCode("-");
			lDecAutorita.setDescription("-");
			List lListAutorita = new ArrayList();
			// Aggiungo alla collection delle posizioni giuridiche iscrizione un '-'
			lListAutorita.add(lDecAutorita);

			lListAutorita.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mTipoAutoritaCumulo = lListAutorita;

			// TIPO_UFFICIO_CUMULO
			lModel.setContesto("TIPO_UFFICIO_CUMULO");
			lModel.setCodiceAlternativo("T");
			Collection lListaAutoritaComuni = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCodiceAlternativo("C");
			Collection lListaAutoritaSoloCumulo = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCodiceAlternativo("S");
			Collection lListaAutoritaSoloSentenzaDecreto = lDecodifiche.ExRicercaDecodifiche(lModel);

			List lListaAutoritaCumulo = new ArrayList(lListaAutoritaComuni);
			lListaAutoritaCumulo.addAll(lListaAutoritaSoloCumulo);
			mTipoUfficioCumulo = lListaAutoritaCumulo;

			// STUB 10/01/2006 Modifica Lista Autorità Cumulo x Sentenza/Decreto.
			// List lListaAutoritaCumuloSentenzaDecreto = new ArrayList(lListaAutoritaComuni);
			List lListaAutoritaCumuloSentenzaDecreto = new ArrayList();
			lListaAutoritaCumuloSentenzaDecreto.add(lDecAutorita);

			lListaAutoritaCumuloSentenzaDecreto.addAll(lListaAutoritaSoloSentenzaDecreto);
			mTipoUfficioCumuloSentenzaDecreto = lListaAutoritaCumuloSentenzaDecreto;

			lModel.setCodiceAlternativo(""); // Ripulisce la condizione di CodiceAlternativo

			// TIPO_UFFICI_CUMULO Filtrati per RV_HIGH_VALUE T,C e S
			mTipoUfficioCumuloRifSiep = lDecodifiche.ExListaTipiUfficioCumuloRifSiep();

			// TIPO_UFFICI_CUMULO Filtrati per RV_HIGH_VALUE T,C,S e RV_ABBREVIATION V
			mTipoUfficioCumuloUfficioLoginRifSiep = lDecodifiche
					.ExListaTipiUfficioCumuloUfficioLoginRifSiep();

			// TIPO_UFFICI_CUMULO Filtrati per RV_HIGH_VALUE T,C,S e D
			mTipoUfficioCumuloRifMSic = lDecodifiche.ExListaTipiUfficioCumuloRifMSic();

			// POSIZIONE GIURIDICA REVOCA/CONVERSIONE SANZIONE SOSTITUTIVA
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setCodiceAlternativo("SAN_REV");

			DecodificheModel lDecModPosSa = new DecodificheModel();
			lDecModPosSa.setContesto("POSIZIONE_GIURIDICA");
			lDecModPosSa.setCode("-");
			lDecModPosSa.setDescription("-");
			List lListPosSan = new ArrayList();
			// Aggiungo alla collection delle posizioni giuridiche iscrizione un '-'
			lListPosSan.add(lDecModPosSa);

			lListPosSan.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			mPosizioniSanzione = lListPosSan;
			lModel.setCodiceAlternativo("");

			// POSIZIONE GIURIDICA ISCRIZIONE
			// Aggiungo alla collection delle posizioni giuridiche iscrizione un '-'
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setFiltro("PRIMA");

			DecodificheModel lDecMod = new DecodificheModel();
			lDecMod.setContesto("POSIZIONE_GIURIDICA");
			lDecMod.setCode("-");
			lDecMod.setDescription("-");
			List lListPrima = new ArrayList();
			// Aggiungo alla collection delle posizioni giuridiche iscrizione un '-'
			lListPrima.add(lDecMod);
			// Aggiungo alla collection delle posizioni giuridiche con
			// POSIZIONE_GIURIDICA='07','01','73','02','70','71','72'
			lModel = new DecodificheModel();
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setFiltro("PRIMA");
			lModel.setCode("07");
			lListPrima.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setFiltro("PRIMA");
			lModel.setCode("01");
			lListPrima.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setFiltro("PRIMA");
			lModel.setCode("73");
			lListPrima.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setFiltro("PRIMA");
			lModel.setCode("02");
			lListPrima.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setFiltro("PRIMA");
			lModel.setCode("70");
			lListPrima.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setFiltro("PRIMA");
			lModel.setCode("71");
			lListPrima.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setFiltro("PRIMA");
			lModel.setCode("72");
			lListPrima.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setFiltro("PRIMA");
			// MEV 39: AGGIUNGO DUE NUOVE POSIZIONE GIURIDICHE PER IL DIFFERIMENTO MS
			lModel.setCode("89");
			lListPrima.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setFiltro("PRIMA");
			lModel.setCode("90");
			lListPrima.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			mPosizioneGiuridicaIscrizione = lListPrima;

			// POSIZIONE GIURIDICA ISCRIZIONE CIDICE MASCHERA
			// Aggiungo alla collection delle posizioni giuridiche iscrizione un '-'
			lDecMod = new DecodificheModel();
			lDecMod.setContesto("POSIZIONE_GIURIDICA");
			lDecMod.setCode("-");
			lDecMod.setDescription("-");
			List lListCodiceMaschera = new ArrayList();
			lListCodiceMaschera.add(lDecMod);
			// Aggiungo alla collection delle posizioni giuridiche con COD_MASCHERA='L','EI','EA'
			lModel = new DecodificheModel();
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setCodiceAlt5("L");
			lListCodiceMaschera.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			lModel.setCodiceAlt5("EI");
			lListCodiceMaschera.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			lModel.setCodiceAlt5("EA");
			lListCodiceMaschera.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			mPosizioneGiuridicaIscrizioneCodiceMaschera = lListCodiceMaschera;
			lModel = new DecodificheModel();

			// ** POSIZIONE GIURIDICA ESECUZIONE
			lModel.setFiltro("MIS_ALT");
			lModel.setContesto("POSIZIONE_GIURIDICA");
			// mPosizioneGiuridicaEsecuzione = lDecodifiche.ExRicercaDecodificheFiltroNull(lModel);
			mPosizioneGiuridicaEsecuzione = lDecodifiche
					.ExRicercaDecodificheFiltroNullOrRvAbbreviation(lModel);

			// ** POSIZIONE GIURIDICA CON RV_ABBREVIATION = MIS ALT
			mPosizioneGiuridicaMisAlt = lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel);

			// ** POSIZIONE GIURIDICA ALTRA CAUSA
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setFiltro("ALTRA_CAUSA");

			List lListAltraCausa = new ArrayList();
			// Aggiungo alla collection delle posizioni giuridiche altra causa un '-'
			lListAltraCausa.add(lDecMod);
			lListAltraCausa.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel));
			mPosizioneGiuridicaAltraCausa = lListAltraCausa;
			lModel.setFiltro(""); // Ripulisce la condizione di filtro

			// ========================================================================
			// MEV26 - Gestione Posizioni giuridiche CUMULO
			// Libero
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setCodiceAlt4("CUMULO_LIBERO");
			mPosizioniGiuridicheCumLibero = lDecodifiche
					.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel);
			lModel.setCodiceAlt4("");

			// Espiazione in Istituto
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setCodiceAlt4("CUMULO_ESP_IST");
			mPosizioniGiuridicheCumEspIst = lDecodifiche
					.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel);
			lModel.setCodiceAlt4("");

			// Espiazione in altro luogo
			lModel.setContesto("POSIZIONE_GIURIDICA");
			lModel.setCodiceAlt4("CUMULO_ESP_ALTRO");
			mPosizioniGiuridicheCumEspAltro = lDecodifiche
					.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel);
			lModel.setCodiceAlt4("");

			// Fine MEV26
			// ========================================================================

			lModel.setContesto("POSIZIONE_PROCESSUALE");
			mPosizioneProcessuale = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("FLAG_ERGASTOLO");
			mFlagErgastolo = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("SESSO");
			mSesso = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("NAZIONALITA");
			mNazionalita = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_PENA_ACCESSORIA");
			mTipoPenaAccessoria = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_DURATA");
			mDurataPenaAccessoria = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("FONTE");
			mTipoFonteReato = lDecodifiche.ExRicercaDecodificheRwLowValue(lModel);

			lModel.setContesto("SOTTONUMERAZIONE");
			mSottonumerazione = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);

			lModel.setContesto("PERIODO_CONSUMAZIONE");
			mPeriodoConsumazione = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_PENA_DETENTIVA");
			mTipoPenaDetentiva = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);

			lModel.setContesto("TIPO_RITO_SENTENZA");
			mTipoRitoSentenza = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("CONTENUTO_SIEP");
			mTipoContenuto = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("CONTENUTO_ISTANZA");
			mTipoContenutoIstanza = lDecodifiche.ExRicercaDecodifiche(lModel);

			// 01-09-2015 MEV_2 - Mis Sic STEP2
			lModel.setContesto("OGGETTO_PROCEDIMENTO");
			// mOggettoProcedimentoMS = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCode("MS01");
			Collection mListaOggettoProcedimentoMS01 = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCode("MS02");
			Collection mListaOggettoProcedimentoMS02 = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCode("MS03");
			Collection mListaOggettoProcedimentoMS03 = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCode("MS04");
			Collection mListaOggettoProcedimentoMS04 = lDecodifiche.ExRicercaDecodifiche(lModel);

			List lListaOggettiProcMS = new ArrayList(mListaOggettoProcedimentoMS01);
			lListaOggettiProcMS.addAll(mListaOggettoProcedimentoMS02);
			lListaOggettiProcMS.addAll(mListaOggettoProcedimentoMS03);
			lListaOggettiProcMS.addAll(mListaOggettoProcedimentoMS04);

			mOggettoProcedimentoMS = lListaOggettiProcMS;
			lModel.setCode(""); // Ripulisce la condizione di Low_Value;

			lModel.setContesto("TIPO_PENA_DETENTIVA");
			lModel.setFiltro("COMPLESSIVA");
			DecodificheModel lDecModPena = new DecodificheModel();
			lDecModPena.setContesto("TIPO_PENA_DETENTIVA");
			lDecModPena.setCode("-");
			lDecModPena.setDescription("-");
			List lListTipoPenaDetErgastolo = new ArrayList();
			// Aggiungo alla collection delle posizioni giuridiche altra causa un '-'
			lListTipoPenaDetErgastolo.add(lDecModPena);
			lListTipoPenaDetErgastolo.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mTipoPenaDetentivaErgastolo = lListTipoPenaDetErgastolo;
			lModel.setFiltro("");

			lModel.setContesto("TIPO_MISURA_CAUTELARE");
			mTipoMisuraCautelare = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_BENEFICIO");
			lModel.setFiltro("CB");
			mTipoBeneficio = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setFiltro("");

			lModel.setContesto("NATURA_BENEFICIO");
			mNaturaBeneficio = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("DPR");
			mDPR = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);

			lModel.setContesto("TIPO_SOSP_SUBORDINATA");
			mTipoSospSubordinata = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_NOTIFICA");
			mTipoNotifica = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("ESITO_NOTIFICA");
			mEsitoNotifica = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("ESITO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("I");
			mEsitoProvvedimentoIstanza = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCodiceAlternativo("");

			lModel.setContesto("ESITO_TENORE");
			mEsitoTenore = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_AUTORITA");
			// mTipoAutorita = lDecodifiche.ExRicercaDecodifiche(lModel);
			mTipoAutorita = lDecodifiche.ExRicercaDecodificheTipoAutorita();

			lModel.setCodiceAlternativo("AUTORITA_MINORENNI");
			mTipoAutoritaMinorenni = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setCodiceAlternativo("");
			lModel.setContesto("TIPO_AUTORITA");
			lModel.setFiltro("S");
			DecodificheModel lDecModSanz = new DecodificheModel();
			lDecModSanz.setContesto("TIPO_AUTORITA");
			lDecModSanz.setCode("-");
			lDecModSanz.setDescription("-");
			List lListTipoAutSanz = new ArrayList();
			// Aggiungo alla collection un '-'
			lListTipoAutSanz.add(lDecModSanz);
			lListTipoAutSanz.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mTipoAutoritaSanzioni = lListTipoAutSanz;
			lModel.setCodiceAlternativo("");
			lModel.setFiltro("");

			lModel.setContesto("TIPO_AUTORITA");
			lModel.setCodiceAlternativo("A");
			DecodificheModel lDecModArr = new DecodificheModel();
			lDecModArr.setContesto("TIPO_AUTORITA");
			lDecModArr.setCode("-");
			lDecModArr.setDescription("-");
			List lListTipoAutArr = new ArrayList();
			// Aggiungo alla collection un '-'
			lListTipoAutArr.add(lDecModArr);
			lListTipoAutArr.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mTipoAutoritaArresto = lListTipoAutArr;
			lModel.setCodiceAlternativo("");

			// modifica relativa al tipo istituto
			lModel.setContesto("TIPO_AUTORITA");
			lModel.setCodiceAlternativo("ISTITUTO");
			DecodificheModel lDecModIst = new DecodificheModel();
			lDecModIst.setContesto("TIPO_AUTORITA");
			lDecModIst.setCode("-");
			lDecModIst.setDescription("-");
			List lListTipoAutIst = new ArrayList();
			// Aggiungo alla collection un '-'
			lListTipoAutIst.add(lDecModArr);
			lListTipoAutIst.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mTipoAutoritaIstituto = lListTipoAutIst;
			lModel.setCodiceAlternativo("");

			/*
			 * mSesso = new Vector(); mSesso.add(new DecodificheModel("M","M","SESSO") ); mSesso.add(new
			 * DecodificheModel("F","F","SESSO") );
			 */

			mFlagSN = new Vector();
			mFlagSN.add(new DecodificheModel("S", "S", "SN", "", "", "", "", "", ""));
			mFlagSN.add(new DecodificheModel("N", "N", "SN", "", "", "", "", "", ""));

			mFlagSNTrattino = new Vector();
			mFlagSNTrattino.add(new DecodificheModel("-", "-", "-", "", "", "", "", "", ""));
			mFlagSNTrattino.add(new DecodificheModel("S", "S", "SN", "", "", "", "", "", ""));
			mFlagSNTrattino.add(new DecodificheModel("N", "N", "SN", "", "", "", "", "", ""));

			mFlagLireEuro = new Vector();
			mFlagLireEuro.add(new DecodificheModel("LIT", "Lire", "VALUTA", "", "", "", "", "", ""));
			mFlagLireEuro.add(new DecodificheModel("EUR", "Euro", "VALUTA", "", "", "", "", "", ""));

			mTipoProcura = new Vector();
			mTipoProcura.add(new DecodificheModel("-", "-", "-", "", "", "", "", "", ""));
			mTipoProcura.add(new DecodificheModel("PM", "PROCURA REPUBBLICA PRESSO TRIBUNALE", "TIPO_PROCURA",
					"", "", "", "", "", ""));
			mTipoProcura.add(new DecodificheModel("PGCAP", "PROCURA GENERALE PRESSO CORTE D'APPELLO",
					"TIPO_PROCURA", "", "", "", "", "", ""));

			lModel.setContesto("BILANCIAMENTO_CIRCOSTANZE");
			mBilanciamentoCircostanze = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_ASSOLUZIONE");
			mTipoAssoluzione = lDecodifiche.ExRicercaDecodifiche(lModel);

			mListaTipoProvvCumulo = new Vector();
			mListaTipoProvvCumulo.add(new DecodificheModel("13", "Cumulo", "", "", "", "", "", "", ""));

			mListaTipoProvvOrdinanza = new Vector();
			mListaTipoProvvOrdinanza.add(new DecodificheModel("03", "Ordinanza", "", "", "", "", "", "", ""));

			mListaTipoProvvDecretoArchiviazione = new Vector();
			mListaTipoProvvDecretoArchiviazione
					.add(new DecodificheModel("63", "Decreto di Archiviazione", "", "", "", "", "", "", ""));

			lModel.setContesto("TIPO_SANZIONE_SOSTITUTIVA");
			// MEV_2023-13 aggiunto filtro sul dominio per l'aggiunta dei codici della "Pena Sostitutiva"
			lModel.setFiltro("Sanzione Sostitutiva");
			mTipoSanzioneSostitutiva = lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel);
			lModel.setFiltro(""); // ripulisco il filtro

			// MEV_2023-13
			lModel.setContesto("TIPO_SANZIONE_SOSTITUTIVA");
			lModel.setFiltro("Pena Sostitutiva");
			mTipoPenaSostitutiva = lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel);
			lModel.setFiltro(""); // ripulisco il filtro
			// MEV_2023-13 - FINE

			lModel.setContesto("TIPO_SANZIONE_CONVERTITA");
			mTipoSanzioneConvertita = lDecodifiche.ExRicercaDecodificheOrdinatePerCodiceAlternativo(lModel);
			lModel.setContesto("TIPO_ATTO");
			mTipoAtto = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("STAMPE_CUMULO");
			mStampeCumulo = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_RICORSO");
			mTipoRicorso = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_RICORSO_SIGE");
			mTipoRicorsoSige = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("SOGGETTO_IMPUGNANTE");
			mSoggettoImpugnante = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);

			lModel.setContesto("SOGGETTO_IMPUGNANTE_SIGE");
			mSoggettoImpugnanteSige = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);

			lModel.setContesto("OGGETTO_SOSPENSIONI");
			mOggettoSospensioni = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("MITTENTE_ATTO");
			mMittenteAtto = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			mMotivoProvvedimento = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("REVOCA_DECSOSP");
			mRevocaDecSosp = lDecodifiche.ExRicercaDecodifiche(lModel);

			// Aggiunto il 01/03/2004.
			lModel.setContesto("TIPO_EVENTO");
			mTipoEvento = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("NON_ATTIVITA");
			mListaAttivitaAvvocato = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("MOTIVO_DESIGNAZIONE");
			mMotivoDesignazione = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("INTERR");
			mMotivoInterruzione = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("NLP");
			mMotivoNLP = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("RDS");
			mMotivoRDS = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("ESPIAZ");
			DecodificheModel lDecModEspia = new DecodificheModel();
			lDecModEspia.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModEspia.setCode("-");
			lDecModEspia.setDescription("-");
			List lListTipoEspiaz = new ArrayList();
			// Aggiungo alla collection un '-'
			lListTipoEspiaz.add(lDecModEspia);
			lListTipoEspiaz.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mMotivoFineEspiazione = lListTipoEspiaz;
			lModel.setCodiceAlternativo("");

			List lListVistoPm = new ArrayList();
			lModel.setContesto("MOTIVO_PROVVEDIMENTO");

			lModel.setCodiceAlternativo("NLP");
			lListVistoPm.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCodiceAlternativo("ESPIAZ");
			lListVistoPm.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCodiceAlternativo("CUMU");
			lListVistoPm.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCodiceAlternativo("DEFI_GE");
			lListVistoPm.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCodiceAlternativo("DEFI_SORV");
			lListVistoPm.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCodiceAlternativo("DEFI_ALTRO");
			lListVistoPm.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mVistaPm = lListVistoPm;
			lModel.setCodiceAlternativo("");

			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("DEFI_GE");
			DecodificheModel lDecModGE = new DecodificheModel();
			lDecModGE.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModGE.setCode("-");
			lDecModGE.setDescription("-");
			List lListTipoGE = new ArrayList();
			// Aggiungo alla collection un '-'
			lListTipoGE.add(lDecModGE);
			lListTipoGE.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel));
			mMotivoDefiGe = lListTipoGE;
			lModel.setCodiceAlternativo("");

			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("DEFI_ALTRO");
			DecodificheModel lDecModAltro = new DecodificheModel();
			lDecModAltro.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModAltro.setCode("-");
			lDecModAltro.setDescription("-");
			List lListTipoAltro = new ArrayList();
			// Aggiungo alla collection un '-'
			lListTipoAltro.add(lDecModAltro);
			lListTipoAltro.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel));
			mMotivoDefAltro = lListTipoAltro;
			lModel.setCodiceAlternativo("");

			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("DEFI_SORV");
			DecodificheModel lDecModSorv = new DecodificheModel();
			lDecModSorv.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModSorv.setCode("-");
			lDecModSorv.setDescription("-");
			List lListTipoSorv = new ArrayList();
			// Aggiungo alla collection un '-'
			lListTipoSorv.add(lDecModSorv);
			lListTipoSorv.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel));
			mMotivoDefiSor = lListTipoSorv;
			lModel.setCodiceAlternativo("");

			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("C030");
			mMotivoRevocaSS = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCodiceAlternativo("");

			lModel.setContesto("DEFI_ALTRO");
			mMotivoDefiAltro = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_PROVVEDIMENTO_ARC");
			mTipoProvvSorveglianza = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_UFFICIO_DEFI");
			lModel.setFiltro("DEFI_SORV");
			mAutoritaSorveglianza = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_UFFICIO_DEFI");
			lModel.setFiltro("DEFI_GE");
			mAutoritaGE = lDecodifiche.ExRicercaDecodifiche(lModel);

			// richiesta generica
			lModel.setFiltro("");
			lModel.setContesto("TIPO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("G001");
			// Aggiungo il trattino (a6-rr-122 - Fabio 08-02-2008)
			DecodificheModel lRicMod = new DecodificheModel();
			lRicMod.setContesto("TIPO_PROVVEDIMENTO");
			lRicMod.setCode("-");
			lRicMod.setDescription("-");
			List lRicGenMod = new ArrayList();
			lRicGenMod.add(lRicMod);
			lRicGenMod.addAll(lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel));
			mTipoRichiestaRC = lRicGenMod;

			lModel.setContesto("TIPO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("G002");
			// Aggiungo il trattino (a6-rr-122 - Fabio 08-02-2008)
			DecodificheModel lTipoAtto = new DecodificheModel();
			lTipoAtto.setContesto("TIPO_PROVVEDIMENTO");
			lTipoAtto.setCode("-");
			lTipoAtto.setDescription("-");
			List lTipoAttoMod = new ArrayList();
			lTipoAttoMod.add(lTipoAtto);
			lTipoAttoMod.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mTipoRichiestaPVR = lTipoAttoMod;

			lModel.setContesto("SOTTOTIPO_BENEFICIO");
			lModel.setCodiceAlternativo("");
			lModel.setFiltro("SC");
			DecodificheModel lSottoBen = new DecodificheModel();
			lSottoBen.setContesto("SOTTOTIPO_BENEFICIO");
			lSottoBen.setCode("-");
			lSottoBen.setDescription("-");
			List lSottoBenLis = new ArrayList();
			lSottoBenLis.add(lSottoBen);
			lSottoBenLis.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mSottoTipoBeneficio = lSottoBenLis;
			lModel.setFiltro("");

			lModel.setContesto("TIPO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("G003");
			mTipoRichiestaT = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("G003"); // 14-01-2009
			DecodificheModel lTipoAttoTC = new DecodificheModel();
			lTipoAttoTC.setContesto("TIPO_PROVVEDIMENTO");
			lTipoAttoTC.setCode("-");
			lTipoAttoTC.setDescription("-");
			List lTipoAttoTCMod = new ArrayList();
			lTipoAttoTCMod.add(lTipoAttoTC);
			lTipoAttoTCMod.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			// mTipoRichiestaTrasmComp = lDecodifiche.ExRicercaDecodifiche(lModel);
			mTipoRichiestaTrasmComp = lTipoAttoTCMod;
			lModel.setCodiceAlternativo("");

			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("RICHGEN");
			/*
			 * lModel.setContesto("MOTIVO_PROVVEDIMENTO"); lModel.setCode("-"); lModel.setDescription("-");
			 */

			mMotivoProvvedimentiRichGen = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCodiceAlternativo("");

			lModel.setContesto("TIPO_INTSOSP");
			mTipoIntSosp = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCodiceAlternativo("");

			lModel.setContesto("MOTIVO_INTSOSP");
			mMotivoIntSosp = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCodiceAlternativo("");

			lModel.setContesto("TIPO_UFFICIO_DEFI");
			lModel.setFiltro("DEFI_ALTRO");
			mAutoritaAltro = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setFiltro("");

			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("SOS_PM");
			mMotivoSospensionePm = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);

			DecodificheModel lDecModSosp = new DecodificheModel();
			lDecModSosp.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModSosp.setCode("-");
			lDecModSosp.setDescription("-");
			List lListMotivoProvvedimentoSospenzione = new ArrayList();
			// Aggiungo alla collection un '-'
			lListMotivoProvvedimentoSospenzione.add(lDecModSosp);
			lListMotivoProvvedimentoSospenzione.addAll(mMotivoSospensionePm);
			mMotivoSospensionePm = lListMotivoProvvedimentoSospenzione;
			lModel.setCodiceAlternativo("");

			// Sollecito MS
			DecodificheModel lDecModSollMS = new DecodificheModel();
			lDecModSollMS.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModSollMS.setCode("5201");
			mMotivoSollecitoMisureSicurezza = lDecodifiche.ExRicercaDecodifiche(lDecModSollMS);

			// MEV_2023-35 Lista Oggetti Esecuzione Pene Sostitutive U126
			DecodificheModel lDecModEPS = new DecodificheModel();
			lDecModSollMS.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModEPS.setCodiceAlternativo("U126");
			mMotivoEsecuzionePeneSostitutive = lDecodifiche.ExRicercaDecodifiche(lDecModEPS);
			// MEV_2023-35 Lista Oggetti Sospensione esecuzione pene accessorie (U141, C066)
			DecodificheModel dmTPA = new DecodificheModel();
			dmTPA.setContesto("TIPO_PENA_ACCESSORIA");
			dmTPA.setCodiceAlternativo("PS");
			mTipoPenaAccessoriaPS = lDecodifiche.ExRicercaDecodifiche(dmTPA);
			// MEV_2023-35 - FINE

			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("MIS-ALT");
			DecodificheModel lDecModMot = new DecodificheModel();
			lDecModMot.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModMot.setCode("-");
			lDecModMot.setDescription("-");
			List lListMotivoProvvedimentoIstanza = new ArrayList();
			// Aggiungo alla collection un '-'
			lListMotivoProvvedimentoIstanza.add(lDecModMot);
			lListMotivoProvvedimentoIstanza.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mMotivoProvvedimentoIstanza = lListMotivoProvvedimentoIstanza;
			lModel.setCodiceAlternativo("");

			// Gestione differenziata dell' "Oggetto Procedimento" Tra Tribunale di Sorveglianza / Ufficio di
			// Sorveglianza.
			mOggettoProcedimentoTDS = lDecodifiche.ExListaContenuti("TDS");

			mOggettoProcedimentoUDS = lDecodifiche.ExListaContenuti("UDS");
			// MEV10-s3: aggiunta gestione per i minori
			mOggettoProcedimentoTDSM = lDecodifiche.ExListaContenuti("TDSM");
			mOggettoProcedimentoUDSM = lDecodifiche.ExListaContenuti("UDSM");
			mOggettoProcedimento = lDecodifiche.ExListaContenuti("");

			// MEV_2019-09-SIEP: cambiata firma del metodo per distinguere PM da PMM (x3)
			// Gestione motivo provvedimento MA
			mMotivoProvvedimentoMADDom = lDecodifiche.ExListaMotivoProvvMA("DETENZIONE", "PM");

			mMotivoProvvedimentoMAAffP = lDecodifiche.ExListaMotivoProvvMA("AFFIDAMENTO", "PM");

			mMotivoProvvedimentoMASemiL = lDecodifiche.ExListaMotivoProvvMA("SEMILIBERTA", "PM");

			mMotivoProvvedimentoOS = lDecodifiche.ExListaMotivoOS("OS_LIBERAZIONE_ANTICIPATA");

			mMotivoProvvedimentoOSLibAntMA = lDecodifiche
					.ExListaMotivoOSLiberazioneAnticipataMA("OS_LIBERAZIONE_ANTICIPATA_MA");

			mMotivoProvvedimentoSospProvvDetDom = lDecodifiche.ExListaMotivoProvvSospProvvMA("DETENZIONE");

			mMotivoProvvedimentoSospProvvAffP = lDecodifiche.ExListaMotivoProvvSospProvvMA("AFFIDAMENTO");

			mMotivoProvvedimentoSospProvvSemi = lDecodifiche.ExListaMotivoProvvSospProvvMA("SEMILIBERTA");

			mMotivoProvvedimentoRevocaDetDom = lDecodifiche.ExListaMotivoRevocaProvvMA("DETENZIONE");

			mMotivoProvvedimentoRevocaAffP = lDecodifiche.ExListaMotivoRevocaProvvMA("AFFIDAMENTO");

			mMotivoProvvedimentoRevocaSemi = lDecodifiche.ExListaMotivoRevocaProvvMA("SEMILIBERTA");

			// 03.12.2010 Inizio: Aggiunti Daniela
			mMotivoProvvedimentoCessazioneDetDom = lDecodifiche.ExListaMotivoCessazioneProvvMA("DETENZIONE");
			mMotivoProvvedimentoCessazioneAffP = lDecodifiche.ExListaMotivoCessazioneProvvMA("AFFIDAMENTO");
			mMotivoProvvedimentoCessazioneSemi = lDecodifiche.ExListaMotivoCessazioneProvvMA("SEMILIBERTA");
			mMotivoProvvedimentoCessazioneIndultino = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("INDULTINO");
			mMotivoProvvedimentoCessazioneDetDomTerm = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("DETENZIONE_TERM");
			mMotivoProvvedimentoCessazioneEspPressoDom = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("ESP_PRESSO_DOM");
			// 03.12.2010 Fine

			// Aggiunte D.F. nuovi codici DL 146/2013 Cessazione disposta dal MDS
			mMotivoProvvedimentoCessazioneAffPMDS51bis = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("AFFIDAMENTO_MDS_51BIS");
			mMotivoProvvedimentoCessazioneDetDomMDS51bis = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("DETENZIONE_MDS_51BIS");
			mMotivoProvvedimentoCessazioneSemiMDS51bis = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("SEMILIBERTA_MDS_51BIS");
			mMotivoProvvedimentoCessazioneIndultinoMDS51bis = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("INDULTINO_MDS_51BIS");
			mMotivoProvvedimentoCessazioneDetDomTermMDS51bis = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("DETENZIONE_TERM_MDS_51BIS");
			mMotivoProvvedimentoCessazioneEspPressoDomMDS51bis = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("ESP_PRESSO_DOM_MDS_51BIS");

			mMotivoProvvedimentoCessazioneAffPTDS51bis = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("AFFIDAMENTO_TDS_51BIS");
			mMotivoProvvedimentoCessazioneDetDomTDS51bis = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("DETENZIONE_TDS_51BIS");
			mMotivoProvvedimentoCessazioneSemiTDS51bis = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("SEMILIBERTA_TDS_51BIS");
			mMotivoProvvedimentoCessazioneDetDomTermTDS51bis = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("DETENZIONE_TERM_TDS_51BIS");
			mMotivoProvvedimentoCessazioneEspPressoDomTDS51bis = lDecodifiche
					.ExListaMotivoCessazioneProvvMA("ESP_PRESSO_DOM_TDS_51BIS");
			// fine d.f

			mMotivoProvvedimentoRipristinoMAffP = lDecodifiche.ExListaMotivoRipristinoMA("AFFIDAMENTO");

			mMotivoProvvedimentoRipristinoDetDom = lDecodifiche.ExListaMotivoRipristinoMA("DETENZIONE");

			mMotivoProvvedimentoRipristinoSemi = lDecodifiche.ExListaMotivoRipristinoMA("SEMILIBERTA");

			mMotivoProvvedimentoDicEffMAAffP = lDecodifiche.ExListaMotivoDicEffMA("AFFIDAMENTO");

			mMotivoProvvedimentoDetDomSpecAmmiPeriodo = lDecodifiche
					.ExListaMotivoProvvDetDomSpeciale("AMMISSIONE_PERIODO");

			mMotivoProvvedimentoDetDomSpecSospProvv = lDecodifiche
					.ExListaMotivoProvvDetDomSpeciale("SOSPENSIONE_PROVVISORIA");

			mMotivoProvvedimentoProrogaUlteriorePeriodo = lDecodifiche
					.ExListaMotivoProvvProrogaUltPeriodo("PROROGA_ULT_PERIODO");

			mMotivoProvvedimentoMADetDomSpeAmmAff = lDecodifiche
					.ExListaMotivoProvvDetDomSpecialeAmmAff("AMMISSIONE_AFFIDAMENTO");

			mMotivoProvvedimentoRipristinoDetDomSpec = lDecodifiche
					.ExListaMotivoProvvRipristinoDetDomSpeciale("RIPRISTINO_DET_DOM_SPEC");

			// MEV_2019-09-SIEP: aggiunti metodi di estrazione dati
			// mMotivoProvvedimentoAmmProvDetDom = lDecodifiche
			// .ExListaMotivoProvvAmmProvvisoria("AMMISSIONE_PROV_DET_DOM");
			// mMotivoProvvedimentoAmmProvAffi = lDecodifiche
			// .ExListaMotivoProvvAmmProvvisoria("AMMISSIONE_PROV_AFFI");
			mMotivoProvvedimentoAmmProvDetDom = lDecodifiche
					.ExListaMotivoProvvAmmProvvisoria("AMMISSIONE_PROV_DET_DOM_PM");
			mMotivoProvvedimentoAmmProvDetDomPmm = lDecodifiche
					.ExListaMotivoProvvAmmProvvisoria("AMMISSIONE_PROV_DET_DOM_PMM");
			mMotivoProvvedimentoAmmProvAffi = lDecodifiche
					.ExListaMotivoProvvAmmProvvisoria("AMMISSIONE_PROV_AFFI_PM");
			mMotivoProvvedimentoAmmProvAffiPmm = lDecodifiche
					.ExListaMotivoProvvAmmProvvisoria("AMMISSIONE_PROV_AFFI_PMM");
			mMotivoProvvedimentoAmmProvSemilibPm = lDecodifiche
					.ExListaMotivoProvvAmmProvvisoria("AMMISSIONE_PROV_SEMILIB_PM");
			mMotivoProvvedimentoAmmProvSemilibPmm = lDecodifiche
					.ExListaMotivoProvvAmmProvvisoria("AMMISSIONE_PROV_SEMILIB_PMM");
			mMotivoProvvedimentoMAAffPMinor = lDecodifiche.ExListaMotivoProvvMA("AFFIDAMENTO", "PMM");
			mMotivoProvvedimentoMADDomMinor = lDecodifiche.ExListaMotivoProvvMA("DETENZIONE", "PMM");
			mMotivoProvvedimentoMASemiLMinor = lDecodifiche.ExListaMotivoProvvMA("SEMILIBERTA", "PMM");
			// MEV_2019-09-SIEP - FINE

			mMotivoProvvedimentoMADetDomTemp = lDecodifiche
					.ExListaMotivoProvvMADetDomTemp("AMMISSIONE_PROV_DET_DOM_TEMP");

			mMotivoProvvedimentoMADetDomTempProroga = lDecodifiche
					.ExListaMotivoProvvMADetDomTemp("PROROGA_AMMISSIONE_PROV_DET_DOM_TEMP");

			mMotivoProvvedimentoMADetDomTempProrogaProvvisoria = lDecodifiche
					.ExListaMotivoProvvMADetDomTemp("PROROGA_PROVVISORIA_DET_DOM_TEMP");

			mMotivoProvvedimentoMAReLibCond = lDecodifiche
					.ExListaMotivoProvvMAReLibCond("AMMISSIONE_PROV_RE_LIB_COND");

			mMotivoProvvedimentoMACOLibCond = lDecodifiche
					.ExListaMotivoProvvMACOLibCond("AMMISSIONE_CO_LIB_COND");

			mMotivoProvvedimentoUlteriorePeriodoMA = lDecodifiche
					.ExListaMotivoProvvUltPeriodoMA("ULT_PERIODO_MA");

			mMotivoProvvedimentoMAPreEffAffPro = lDecodifiche.ExListaMotivoMAPreEff("AFFIDAMENTO");

			mMotivoProvvedimentoMAPreEffDetDom = lDecodifiche.ExListaMotivoMAPreEff("DETENZIONE");

			mMotivoProvvedimentoMAPreEffSemli = lDecodifiche.ExListaMotivoMAPreEff("SEMILIBERTA");

			mMotivoProvvedimentoMARipristinoIndultino = lDecodifiche.ExListaMotivoRipristinoMA("INDULTINO");

			mMotivoProvvedimentoSospProvvMAIndultino = lDecodifiche
					.ExListaMotivoProvvSospProvvMA("INDULTINO");

			mMotivoProvvedimentoMAPerEfficIndultino = lDecodifiche.ExListaMotivoMAPreEff("INDULTINO");

			mMotivoProvvedimentoRevocaIndultino = lDecodifiche.ExListaMotivoRevocaProvvMA("INDULTINO");

			// MEV_2019-09-SIEP: cambiata firma del metodo per distinguere PM da PMM (x2)
			mMotivoProvvedimentoMAConIndultino = lDecodifiche.ExListaMotivoProvvMA("INDULTINO", "PM");
			// 27/09/2010 Espiazione Presso Domicilio
			mMotivoProvvedimentoMAEspPressoDom = lDecodifiche.ExListaMotivoProvvMA("ESP_PRESSO_DOM", "PM");

			mMotivoProvvedimentoMASospEspPressoDom = lDecodifiche
					.ExListaMotivoProvvSospProvvMA("ESP_PRESSO_DOM");
			mMotivoProvvedimentoMAPerEfficEspPressoDom = lDecodifiche.ExListaMotivoMAPreEff("ESP_PRESSO_DOM");
			mMotivoProvvedimentoMARipristinoEspPressoDom = lDecodifiche
					.ExListaMotivoRipristinoMA("ESP_PRESSO_DOM");
			mMotivoProvvedimentoRevocaEspPressoDom = lDecodifiche
					.ExListaMotivoRevocaProvvMA("ESP_PRESSO_DOM");
			mMotivoProvvedimentoProsecProvvMAEspPressoDom = lDecodifiche
					.ExListaMotivoProvvedimentoProsecProvvMA("SOSP_ESP_PRESSO_DOM_51BIS");

			mMotivoProvvedimentoProsecProvvMAAffPro = lDecodifiche
					.ExListaMotivoProvvedimentoProsecProvvMA("AFFIDAMENTO");
			mMotivoProvvedimentoProsecProvvMADetDom = lDecodifiche
					.ExListaMotivoProvvedimentoProsecProvvMA("DETENZIONE");
			mMotivoProvvedimentoProsecProvvMASem = lDecodifiche
					.ExListaMotivoProvvedimentoProsecProvvMA("SEMILIBERTA");
			mMotivoProvvedimentoProsecProvvMAIndultino = lDecodifiche
					.ExListaMotivoProvvedimentoProsecProvvMA("INDULTINO51BIS");

			// DL 146/2013 Codici del TDS
			//
			mMotivoProvvedimentoProsecMAAffProMDS51Bis = lDecodifiche
					.ExListaMotivoProvvedimentoProsecMA51Bis("AFFIDAMENTO_MDS");
			mMotivoProvvedimentoProsecMADetDomMDS51Bis = lDecodifiche
					.ExListaMotivoProvvedimentoProsecMA51Bis("DETENZIONE_MDS");
			mMotivoProvvedimentoProsecMASemMDS51Bis = lDecodifiche
					.ExListaMotivoProvvedimentoProsecMA51Bis("SEMILIBERTA_MDS");
			mMotivoProvvedimentoProsecMAEsecPreDomMDS51Bis = lDecodifiche
					.ExListaMotivoProvvedimentoProsecMA51Bis("ESECPREDOM_MDS");
			mMotivoProvvedimentoProsecMADetDomTerMDS51Bis = lDecodifiche
					.ExListaMotivoProvvedimentoProsecMA51Bis("DETDOMTERM_MDS");

			mMotivoProvvedimentoProsecMAAffProTDS51Bis = lDecodifiche
					.ExListaMotivoProvvedimentoProsecMA51Bis("AFFIDAMENTO_TDS");
			mMotivoProvvedimentoProsecMADetDom_TDS_51Bis = lDecodifiche
					.ExListaMotivoProvvedimentoProsecMA51Bis("DETENZIONE_TDS");
			mMotivoProvvedimentoProsecMASem_TDS_51Bis = lDecodifiche
					.ExListaMotivoProvvedimentoProsecMA51Bis("SEMILIBERTA_TDS");
			mMotivoProvvedimentoProsecMAEsecPreDom_TDS_51Bis = lDecodifiche
					.ExListaMotivoProvvedimentoProsecMA51Bis("ESECPREDOM_TDS");
			mMotivoProvvedimentoProsecMADetDomTer_TDS_51Bis = lDecodifiche
					.ExListaMotivoProvvedimentoProsecMA51Bis("DETDOMTERM_TDS");

			mMotivoProvvedimentoEstDefMAffP = lDecodifiche.ExListaMotivoProvvedimentoEstDefMA("AFFIDAMENTO");
			mMotivoProvvedimentoEstDefMADetDom = lDecodifiche
					.ExListaMotivoProvvedimentoEstDefMA("DETENZIONE");
			mMotivoProvvedimentoEstDefMASem = lDecodifiche.ExListaMotivoProvvedimentoEstDefMA("SEMILIBERTA");

			mMotivoProvvedimentoRigettoMA = lDecodifiche.ExListaMotivoProvvedimentoRigettoMA();

			mListaAutoritaSospTDSUDS = lDecodifiche.ExListaAutoritaSospTDSUDS();

			DecodificheModel lDecModTipoUffSosp = new DecodificheModel();
			lDecModTipoUffSosp.setContesto("TIPO_UFFICIO_SOSP");
			lDecModTipoUffSosp.setCode("-");
			lDecModTipoUffSosp.setDescription("-");
			List lListTipoUffSosp = new ArrayList();
			// Aggiungo alla collection un '-'
			lListTipoUffSosp.add(lDecModTipoUffSosp);
			lListTipoUffSosp.addAll(lDecodifiche.ExListaAutoritaSospTDSUDS());
			mListaAutoritaSospTDSUDSTrattino = lListTipoUffSosp;

			// Oggetti del differimento
			mOggettoSospensioneDiff = lDecodifiche.ExListaOggettiSospensioneDifferimento();
			mOggettoSospensioneDiffDef = lDecodifiche.ExListaOggettiSospensioneDifferimentoDef();
			mOggettoSospensioneDiffProvv = lDecodifiche.ExListaOggettiSospensioneDifferimentoProvv();
			mOggettoRevocaDiff = lDecodifiche.ExListaOggettiRevocaDifferimento();

			mTipologiaDecisioneSospensioneDiff = lDecodifiche.ExListaMotivoProvvSospDifferimento();
			mTipologiaDecisioneSospensioneDiffProvv = lDecodifiche.ExListaMotivoProvvSospDifferimentoProvv();
			mTipologiaDecisioneSospensioneDiffDef = lDecodifiche.ExListaMotivoProvvSospDifferimentoDef();
			mTipologiaDecisioneRevocaDiff = lDecodifiche.ExListaMotivoProvvRevocaDifferimento();
			mTipologiaDecisioneRigettoDiff = lDecodifiche.ExListaMotivoProvvRigettoDifferimento();
			// MEV_2019-09-SIEP: cambiata firma del metodo per distinguere PM da PMM
			mOggettoDecisione = lDecodifiche.ExListaOggettiSospensioneDecisioneSor("PM");
			// MEV_2019-09-SIEP: aggiunto metodo per i minori
			mOggettoDecisioneMinor = lDecodifiche.ExListaOggettiSospensioneDecisioneSor("PMM");

			mOggettiDecisioneSosp678 = lDecodifiche.ExListaMotivoProvvSosp678();

			// Oggetti dell'espulsione
			mMotivoProvvedimentoEspulsione = lDecodifiche.ExListaMotivoProvvedimentoEspulsione("CONCESSIONE");
			mMotivoProvvedimentoAccoglieOpEspulsione = lDecodifiche
					.ExListaMotivoProvvedimentoEspulsione("ACCOGLIE");
			mMotivoProvvedimentoRigettoOpEspulsione = lDecodifiche
					.ExListaMotivoProvvedimentoEspulsione("RIFIUTA");

			lModel.setContesto("FLAG_STATO");
			mFlagStato = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("DESTINATARIO_DEPOSITO");
			mDestinatarioDeposito = lDecodifiche.ExRicercaDecodifiche(lModel);

			List lListDestMinorenni = new ArrayList();
			lModel.setContesto("DESTINATARIO_DEPOSITO");
			lModel.setCode("CC");
			lListDestMinorenni.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("CPS");
			lListDestMinorenni.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("P");
			lListDestMinorenni.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("PGCAP");
			lListDestMinorenni.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("PM");
			lListDestMinorenni.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("Q");
			lListDestMinorenni.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("SERT");
			lListDestMinorenni.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("UEPE");
			lListDestMinorenni.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setContesto("DESTINATARIO_DEPOSITO_MINORENNI");
			lModel.setCode("");
			lListDestMinorenni.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mDestinatarioDepositoMinorenni = lListDestMinorenni;

			lModel.setContesto("NATURA_MISURA_SICUREZZA");
			mNaturaMisuraSicurezza = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_MISURA_SICUREZZA");
			// mTipoMisuraSicurezza = lDecodifiche.ExRicercaDecodifiche(lModel);
			mTipoMisuraSicurezza = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);

			// MEV26 Cumulo
			lModel.setContesto("STATO_MISURA_CUMULO");
			mStatoMisuraSicurezza = lDecodifiche.ExRicercaDecodifiche(lModel);

			mTipoMisuraSicurezzaMinorenni = lDecodifiche.ExRicercaDecodificheTipoMisureMinorenni();

			// MEV10-s3: aggiunte collections
			mTipoMSMinorenniNonDetentiva = lDecodifiche.ExRicercaDecodificheTipoMSMinorenniByNatura("02");
			mTipoMSMinorenniDetentiva = lDecodifiche.ExRicercaDecodificheTipoMSMinorenniByNatura("01");

			mTipoUfficioSIEP = new Vector();
			// MEV10-s3: UPPERCASE
			mTipoUfficioSIEP.add(
					new DecodificheModel("PMM", "PROCURA DELLA REPUBBLICA PRESSO IL TRIBUNALE DEI MINORENNI",
							"TIPO_PROCURA", "", "", "", "", "", ""));
			mTipoUfficioSIEP.add(new DecodificheModel("PM", "PROCURA REPUBBLICA PRESSO TRIBUNALE",
					"TIPO_PROCURA", "", "", "", "", "", ""));
			mTipoUfficioSIEP.add(new DecodificheModel("PGCAP", "PROCURA GENERALE PRESSO CORTE D'APPELLO",
					"TIPO_PROCURA", "", "", "", "", "", ""));

			mTipoUfficioSIEPTrattino = new Vector();
			mTipoUfficioSIEPTrattino.add(new DecodificheModel("-", "-", "-", "-", "-", "-", "-", "-", "-"));
			mTipoUfficioSIEPTrattino.add(new DecodificheModel("PM", "PROCURA REPUBBLICA PRESSO TRIBUNALE",
					"TIPO_PROCURA", "", "", "", "", "", ""));
			mTipoUfficioSIEPTrattino.add(new DecodificheModel("PGCAP",
					"PROCURA GENERALE PRESSO CORTE D'APPELLO", "TIPO_PROCURA", "", "", "", "", "", ""));

			mTipoUfficioSIUS = new Vector<>();
			mTipoUfficioSIUS.add(new DecodificheModel("TDS", "TRIBUNALE DI SORVEGLIANZA", "TIPO_UFFICIO_SIUS",
					"", "", "", "", "", ""));
			mTipoUfficioSIUS.add(new DecodificheModel("UDS", "UFFICIO DI SORVEGLIANZA", "TIPO_UFFICIO_SIUS",
					"", "", "", "", "", ""));
			// mTipoUfficioSIUS.add(new DecodificheModel("TM", "TRIBUNALE PER I MINORENNI",
			// "TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));
			// mTipoUfficioSIUS.add(new DecodificheModel("UDSM",
			// "UFFICIO DI SORVEGLIANZA PRESSO IL TRIBUNALE PER MINORENNI", "TIPO_UFFICIO_SIUS", "", "", "",
			// "", "", ""));

			mTipoUfficioSIUSMinor = new Vector<>();
			mTipoUfficioSIUSMinor.add(new DecodificheModel("TDSM",
					"TRIBUNALE PER I MINORENNI IN FUNZIONE DI TRIBUNALE SORVEGLIANZA", "TIPO_UFFICIO_SIUS",
					"", "", "", "", "", ""));
			mTipoUfficioSIUSMinor.add(
					new DecodificheModel("UDSM", "UFFICIO DI SORVEGLIANZA PRESSO IL TRIBUNALE PER MINORENNI",
							"TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));

			mTipoUfficioSiusTrattino = new Vector();
			mTipoUfficioSiusTrattino.add(new DecodificheModel("-", "-", "-", "-", "-", "", "", "", ""));
			mTipoUfficioSiusTrattino.add(new DecodificheModel("TDS", "TRIBUNALE DI SORVEGLIANZA",
					"TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));
			mTipoUfficioSiusTrattino.add(new DecodificheModel("UDS", "UFFICIO DI SORVEGLIANZA",
					"TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));
			// mTipoUfficioSiusTrattino.add(new DecodificheModel("TM", "TRIBUNALE PER I MINORENNI",
			// "TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));
			// mTipoUfficioSiusTrattino.add(new DecodificheModel("UDSM",
			// "UFFICIO DI SORVEGLIANZA PER I MINORENNI", "TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));

			// MEV10-s3: aggiunta collection
			lModel.setContesto("TIPO_UFFICIO");
			mTipoUfficioSiusTDSMUDSM = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setCodiceAlternativo("");
			mTipoUfficioSiusTDSMUDSM = new Vector();
			mTipoUfficioSiusTDSMUDSM.add(new DecodificheModel("-", "-", "-", "-", "-", "", "", "", ""));
			mTipoUfficioSiusTDSMUDSM.add(new DecodificheModel("TDS", "TRIBUNALE DI SORVEGLIANZA",
					"TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));
			mTipoUfficioSiusTDSMUDSM.add(new DecodificheModel("UDS", "UFFICIO DI SORVEGLIANZA",
					"TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));
			mTipoUfficioSiusTDSMUDSM.add(new DecodificheModel("TDSM",
					"TRIBUNALE PER I MINORENNI IN FUNZIONE DI TRIBUNALE SORVEGLIANZA", "TIPO_UFFICIO_SIUS",
					"", "", "", "", "", ""));
			// MEV_65: corretta descrizione come in cg_ref_codes (ex "UFFICIO DI SORVEGLIANZA PER I
			// MINORENNI")
			mTipoUfficioSiusTDSMUDSM.add(
					new DecodificheModel("UDSM", "UFFICIO DI SORVEGLIANZA PRESSO IL TRIBUNALE PER MINORENNI",
							"TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));

			// aggiunta collection per Siep dove la descrizione per UDSM e' diversa da quella presente sul DB
			// e da Sius A.S. 18/05/2015 su richiesta di Michele/Nunzia
			// Per SIEP la descrizione UDSM cambia da
			// "Ufficio di Sorveglianza presso il Tribunale per minorenni"
			// in "Magistrato di Sorveglianza per i minorenni"
			lModel.setCodiceAlternativo("");
			mTipoUfficioSiepTDSMUDSM = new Vector();
			mTipoUfficioSiepTDSMUDSM.add(new DecodificheModel("-", "-", "-", "-", "-", "", "", "", ""));
			mTipoUfficioSiepTDSMUDSM.add(new DecodificheModel("TDS", "TRIBUNALE DI SORVEGLIANZA",
					"TIPO_UFFICIO", "", "", "", "", "", ""));
			mTipoUfficioSiepTDSMUDSM.add(new DecodificheModel("UDS", "UFFICIO DI SORVEGLIANZA",
					"TIPO_UFFICIO", "", "", "", "", "", ""));
			mTipoUfficioSiepTDSMUDSM.add(new DecodificheModel("TDSM",
					"TRIBUNALE PER I MINORENNI IN FUNZIONE DI TRIBUNALE DI SORVEGLIANZA", "TIPO_UFFICIO", "",
					"", "", "", "", ""));
			mTipoUfficioSiepTDSMUDSM.add(new DecodificheModel("UDSM",
					"MAGISTRATO DI SORVEGLIANZA PER I MINORENNI", "TIPO_UFFICIO", "", "", "", "", "", ""));

			// Lista UFFICIO ESECUZIONE PENALE ESTERNA (UEPE/UEPESS) e UFFICIO SERVIZI SOCIALI MINORILI
			// (USSM/USSMSS)
			mTipoUffEsePenEstSerSocMin = new Vector();
			mTipoUffEsePenEstSerSocMin.add(new DecodificheModel("-", "-", "-", "-", "-", "", "", "", ""));
			mTipoUffEsePenEstSerSocMin.add(new DecodificheModel("UEPE", "UFFICIO ESECUZIONE PENALE ESTERNA",
					"TIPO_UFFICIO", "", "", "", "", "", ""));
			mTipoUffEsePenEstSerSocMin.add(new DecodificheModel("UEPESS", "UFFICIO ESECUZIONE PENALE ESTERNA",
					"TIPO_UFFICIO", "", "", "", "", "", ""));
			mTipoUffEsePenEstSerSocMin.add(new DecodificheModel("USSM", "UFFICIO SERVIZI SOCIALI MINORILI",
					"TIPO_UFFICIO", "", "", "", "", "", ""));
			mTipoUffEsePenEstSerSocMin.add(new DecodificheModel("USSMSS", "UFFICIO SERVIZI SOCIALI MINORILI",
					"TIPO_UFFICIO", "", "", "", "", "", ""));

			mTipologiaNumerazione = new Vector();
			mTipologiaNumerazione.add(
					new DecodificheModel("EMS", "ES. MIS. SIC.", "TIPO_NUMERAZIONE", "", "", "", "", "", ""));
			mTipologiaNumerazione.add(
					new DecodificheModel("EPP", "ES. PENE PEC.", "TIPO_NUMERAZIONE", "", "", "", "", "", ""));

			lModel.setContesto("TIPO_SANZIONE");
			mTipoSanzione = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("MOTIVO_NON_COMPUTABILE");
			mMotivoNonComputabile = lDecodifiche.ExRicercaDecodificheRvAbbreviation(lModel);

			// lista motivo non computabile con descrizione completa
			lModel.setContesto("MOTIVO_NON_COMPUTABILE");
			mMotivoNonComputabileDescCompleta = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_CONTINUAZIONE");
			mTipoContinuazione = lDecodifiche.ExRicercaDecodifiche(lModel);

			// lModel.setContesto("ESITO_SIEP");
			// mEsitoSiep = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("FIRMA_UFFICIO");
			mTipoFirma = lDecodifiche.ExRicercaDecodifiche(lModel);

			mTipoDecreto = lDecodifiche.ExListaTipoDecreto();

			lModel.setContesto("STATO_LIBERTATIS");
			mStatoLibertatis = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("STATO_PERMESSO");
			mStatoPermesso = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_ORDINANZA");
			mTipoOrdinanza = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_SENTENZA");
			mTipoSentenza = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("MOTIVO_PERIODO_ESS");
			mMotivoSanzioneSostitutiva = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setContesto("TIPO_PROVVEDIMENTO");
			lModel.setFiltro("C");
			mTipoProvvedimentoCumulo = lDecodifiche.ExRicercaDecodifiche(lModel);
			mTipoProvvedimentoCumulo
					.remove(new DecodificheModel("03", "", "TIPO_PROVVEDIMENTO", "C", "", "", "", "", ""));

			lModel.setContesto("TIPO_PROVVEDIMENTO");
			lModel.setFiltro("C");
			mTipoProvvedimentoMSic = lDecodifiche.ExRicercaDecodifiche(lModel);

			mTipoMisuraEsecuzione = new Vector();
			mTipoMisuraEsecuzione.add(new DecodificheModel("U004", "GESTIONE ESECUZIONE MISURE ALTERNATIVE",
					"TIPO_MISURA_ESECUZIONE", "", "", "", "", "", ""));
			mTipoMisuraEsecuzione.add(new DecodificheModel("U016", "GESTIONE DELLE MISURE DI SICUREZZA",
					"TIPO_MISURA_ESECUZIONE", "", "", "", "", "", ""));

			lModel.setFiltro("");
			lModel.setContesto("TIPO_REGISTRO_ORDINANZA");
			mTipoRegistroOrdinanza = lDecodifiche.ExRicercaDecodifiche(lModel);
			mTipoRegistroOrdinanza.remove(
					new DecodificheModel("-", "-", "TIPO_REGISTRO_ORDINANZA", "", "", "", "", "", ""));

			mListaAutoritaSospensione = lDecodifiche.ExListaAutoritaSospensione(mTipoRegistroOrdinanza);
			mListaOggettiSospensione = lDecodifiche.ExListaOggettiSospensione(mTipoRegistroOrdinanza);
			mListaOggettiRevoca = lDecodifiche.ExListaOggettiRevoca(mTipoRegistroOrdinanza);
			mListaMotiviProvvedimentoSospensione = lDecodifiche
					.ExListaMotiviProvvedimentoSospensione(mTipoRegistroOrdinanza);
			mListaMotiviProvvedimentoRevoca = lDecodifiche
					.ExListaMotiviProvvedimentoRevoca(mTipoRegistroOrdinanza);

			// Solo quelli legati solo a Tipo Registro '0002' e '0003'
			Collection mTipoRegistroOrdinanzaNo0001 = new ArrayList();
			mTipoRegistroOrdinanzaNo0001.addAll(mTipoRegistroOrdinanza);
			mTipoRegistroOrdinanzaNo0001.remove(new DecodificheModel("SIUS", "SORVEGLIANZA",
					"TIPO_REGISTRO_ORDINANZA", "", "0001", "", "", "", ""));

			mListaEsitiTenoreSospensione = lDecodifiche
					.ExListaEsitiTenoreSospensione(mTipoRegistroOrdinanzaNo0001);
			mListaEsitiTenoreRevoca = lDecodifiche.ExListaEsitiTenoreRevoca(mTipoRegistroOrdinanzaNo0001);

			// Solo quelli legati solo a Tipo Registro '0001' e '0002'
			Collection mTipoRegistroOrdinanzaNo0003 = new ArrayList();
			mTipoRegistroOrdinanzaNo0003.addAll(mTipoRegistroOrdinanza);
			mTipoRegistroOrdinanzaNo0003.remove(new DecodificheModel("RG", "Autorita' Cognizione",
					"TIPO_REGISTRO_ORDINANZA", "", "0003", "", "", "", ""));
			mRegistroProvGenerico = mTipoRegistroOrdinanzaNo0003;

			lModel.setContesto("TIPO_PERMESSO");
			mTipoPermesso = lDecodifiche.ExRicercaDecodifiche(lModel);

			lModel.setFiltro("");
			lModel.setCodiceAlternativo("");
			lModel.setDescription("");
			lModel.setCode("");
			lModel.setContesto("STATO_PROCEDIMENTO");
			mStatoProcedimento = lDecodifiche.ExRicercaDecodifiche(lModel);

			// STUB 28/04/2005.
			lModel.setCodiceAlternativo("RSL");
			lModel.setContesto("TIPO_LICENZA");
			mTipoLicenza = lDecodifiche.ExRicercaDecodifiche(lModel);

			// Mev a8-rr-002
			lModel = new DecodificheModel();
			lModel.setContesto("QUESTURE");
			mQuesture = lDecodifiche.ExRicercaAndSetCodHighValue(lModel);

			lModel.setFiltro("");
			lModel.setCodiceAlternativo("");
			lModel.setContesto("TIPO_TRASMISSIONE");
			mTrasmissioni = lDecodifiche.ExRicercaDecodifiche(lModel);
			lModel.setFiltro("");
			lModel.setCodiceAlternativo("");

			// Motivazione Non Invio FC
			lModel.setContesto("MOTIVAZIONE_NON_INVIO_FC");
			DecodificheModel lDecMoMotNonInvioFC = new DecodificheModel();
			lDecMoMotNonInvioFC.setContesto("MOTIVAZIONE_NON_INVIO_FC");
			lDecMoMotNonInvioFC.setCode("-");
			lDecMoMotNonInvioFC.setDescription("-");
			List lListMotNonInvio = new ArrayList();
			// Aggiungo alla collection un '-'
			lListMotNonInvio.add(lDecMoMotNonInvioFC);
			lListMotNonInvio.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mMotivoNonInvio = lListMotNonInvio;

			lModel.setContesto("AUTORITA_COMPETENTE");
			mAutoritaCompetente = lDecodifiche.ExRicercaDecodifiche(lModel);

			// 15-01-2015 gestione Combo Autorita Emittente in Associa Titolo Esecutivo a Misura Sicurezza
			lModel.setContesto("TIPO_UFFICIO_EMITTENTE");
			List lListAutorita_Sorv = new ArrayList();
			lListAutorita_Sorv.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lListAutorita_Sorv.add(new DecodificheModel("TDS", "Tribunale di Sorveglianza",
					"TIPO_UFFICIO_EMITTENTE", "", "", "", "", "", ""));
			lListAutorita_Sorv.add(new DecodificheModel("UDS", "Ufficio di Sorveglianza",
					"TIPO_UFFICIO_EMITTENTE", "", "", "", "", "", ""));
			lListAutorita_Sorv.add(new DecodificheModel("UDSM",
					"Magistrato di Sorveglianza presso il Tribunale per minorenni", "TIPO_UFFICIO_EMITTENTE",
					"", "", "", "", "", ""));
			mAutoritaEmi_Sorveglianza = lListAutorita_Sorv;

			mTipoUfficioSez1DDTrattino = new Vector();
			mTipoUfficioSez1DDTrattino.add(new DecodificheModel("-", "-", "-", "-", "-", "-", "-", "-", "-"));
			mTipoUfficioSez1DDTrattino.add(new DecodificheModel("PGCAP",
					"PROCURA GENERALE PRESSO CORTE D'APPELLO", "TIPO_UFFICIO", "", "", "", "", "", ""));
			mTipoUfficioSez1DDTrattino.add(new DecodificheModel("PM",
					"PROCURA REPUBBLICA PRESSO TRIBUNALE ORDINARIO", "TIPO_UFFICIO", "", "", "", "", "", ""));
			mTipoUfficioSez1DDTrattino.add(new DecodificheModel("PMM",
					"PROCURA REPUBBLICA PRESSO TRIBUNALE MINORENNI", "TIPO_UFFICIO", "", "", "", "", "", ""));

			// MEV10-s3: aggiunte collection per gestire invio mail segnalazione
			lModel.setContesto("SEGNALAZIONE_TITOLO");
			mTitoloPersona = lDecodifiche.ExTitoloPersona(lModel);
			lModel.setContesto("SEGNALAZIONE_FUNZIONALITA");
			lModel.setCodiceAlternativo("SIEP");
			mFunzionalitaSIEP = lDecodifiche.ExFunzionalita(lModel);
			lModel.setCodiceAlternativo("SIEPE");
			mFunzionalitaSIEPE = lDecodifiche.ExFunzionalita(lModel);
			lModel.setCodiceAlternativo("SIGE");
			mFunzionalitaSIGE = lDecodifiche.ExFunzionalita(lModel);
			lModel.setCodiceAlternativo("SIUS");
			mFunzionalitaSIUS = lDecodifiche.ExFunzionalita(lModel);
			lModel.setContesto("SEGNALAZIONE_AZIONE");
			lModel.setCodiceAlternativo("");
			mAzione = lDecodifiche.ExAzione(lModel);
			lModel.setContesto("SEGNALAZIONE_TIPOLOGIA");
			mTipoSegnalazione = lDecodifiche.ExTipoSegnalazione(lModel);
			lModel.setContesto("SEGNALAZIONE_GRAVITA");
			mGravitaSegnalazione = lDecodifiche.ExGravitaSegnalazione(lModel);
			// FINE MEV10-s3

			// MEV26 Cumulo
			DecodificheModel lDecModRegGen = new DecodificheModel();
			lDecModRegGen.setContesto("TIPO_REG_GEN");
			mTipoRegistroGenerale = lDecodifiche.ExRicercaDecodifiche(lDecModRegGen);

			// MEV26 Cumulo
			DecodificheModel lDecModMotProvvCumNew = new DecodificheModel();
			lDecModMotProvvCumNew.setContesto("MOTIVO_PROVVEDIMENTO");
			lDecModMotProvvCumNew.setCodiceAlternativo("CUMULO_NEW");
			mMotiviProvvCumuloNew = lDecodifiche.ExRicercaDecodifiche(lDecModMotProvvCumNew);

			lModel.setContesto("TIPO_UFFICIO");
			mTipoUfficioSige = lDecodifiche.ExRicercaDecodifiche(lModel);

			// MEV_2023-13: aggiunta collezione per il tipo tutore
			lModel.setContesto("TIPO_TUTORE");
			mTipoTutore = lDecodifiche.ExRicercaDecodifiche(lModel);

			// MEV_2023-13: aggiunta collezione per la ragione sociale
			mRagioneSocialeCO = new Vector();
			mRagioneSocialeCO.add(new DecodificheModel("-", "-", "-", "-", "-", "-", "-", "-", "-"));
			mRagioneSocialeCO
					.add(new DecodificheModel("Ente", "Ente", "RAGIONE_SOCIALE", "", "", "", "", "", ""));

			// FIXME MEV26 solo per sviluppo da sostituire con la versione ufficiale LPU
			DecodificheModel lTipoLPUModel = new DecodificheModel();
			lTipoLPUModel.setContesto("TIPO_SANZIONE_SOSTITUTIVA_LPU");
			mTipoSanzioneSostitutivaLpu = lDecodifiche.ExRicercaDecodifiche(lTipoLPUModel);

			// MEV_2023-33 - INIZIO
			lModel.setContesto("TIPO_AUTORITA");
			lModel.setCodiceAlt2("AP");
			mTipoAutoritaPolizia = lDecodifiche.ExRicercaDecodifiche(lModel);
			mTipoAutoritaPolizia.add(new DecodificheModel("-", "-", "-", "-", "-", "-", "-", "-", "-"));
			lModel.setContesto("");
			lModel.setCodiceAlt2("");
			// MEV_2023-33 - FINE
		} catch (F3BException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Collection getTrasmissioni() {
		return mTrasmissioni;
	}

	public Collection getNazioni() {
		return mNazioni;
	}

	// Ambrosino 04/2010 SuperSoggetto

	public Collection getStatoCittadinanza() {
		return mStatoCittadinanza;
	}

	//

	public Collection getProvincie() {
		return mProvince;
	}

	public Collection getTipoProvvedimenti() {
		return mTipoProvvedimenti;
	}

	public Collection getTipoProvvedimentiRifP() {
		return mTipoProvvedimentiRifP;
	}

	public Collection getTipoProvvedimentiRif() {
		return mTipoProvvedimentiRif;
	}

	public Collection getTipoProvvedimentiRifPG() {
		return mTipoProvvedimentiRifPG;
	}

	public Collection getTipoDecisioneCassazione() {
		return mTipoDecisioneCassazione;
	}

	public Collection getTenoreDecisioneRicorso() {
		return mTenoreDecisioneRicorso;
	}

	public Collection getTenoreDecisioneRicorsoSige() {
		return mTenoreDecisioneRicorsoSige;
	}

	public Collection getTipoAvvocato() {
		return mTipoAvvocato;
	}

	public Collection getTipoUfficio() {
		return mTipoUfficio;
	}

	// Stessa collection di quella sopra ma Ordinata per Codice Ufficio (quella sopra e' per descrizione)
	public Collection getTipoUfficioPerCodice() {
		return mTipoUfficioPerCod;
	}

	public Collection getTipoUfficioSosp() {
		return mTipoUfficioSosp;
	}

	public Collection getTipoUfficioS() {
		return mTipoUfficioS;
	}

	public Collection getTipoUfficioCertStatoEsec() {
		return mTipoUfficioCertStatoEsec;
	}

	public Collection getTipoUfficioSius() {
		return mTipoUfficioSius;
	}

	public Collection<DecodificheModel> getTipoUfficioSiusTrattino() {
		return mTipoUfficioSiusTrattino;
	}

	public Collection getUfficioLogin() {
		return mUfficioLogin;
	}

	public Collection getStatoFascicolo() {
		return mStatoFascicolo;
	}

	public Collection getTipoReato() {
		return mTipoReato;
	}

	public Collection getTipoFonteReato() {
		return mTipoFonteReato;
	}

	public Collection getSottonumerazione() {
		return mSottonumerazione;
	}

	public Collection getPeriodoConsumazione() {
		return mPeriodoConsumazione;
	}

	public Collection getTipoPenaDetentiva() {
		return mTipoPenaDetentiva;
	}

	public Collection getTipoPenaDetentivaErgastolo() {
		return mTipoPenaDetentivaErgastolo;
	}

	public Collection getTipoIstituto() {
		return mTipoIstituto;
	}

	public Collection getSesso() {
		return mSesso;
	}

	public Collection getNazionalita() {
		return mNazionalita;
	}

	public Collection getPosizioneProcessuale() {
		return mPosizioneProcessuale;
	}

	public Collection getPosizioneGiuridicaIscrizione() {
		return mPosizioneGiuridicaIscrizione;
	}

	public Collection getPosizioneGiuridicaIscrizioneCodiceMaschera() {
		return mPosizioneGiuridicaIscrizioneCodiceMaschera;
	}

	public Collection getPosizioneGiuridicaEsecuzione() {
		return mPosizioneGiuridicaEsecuzione;
	}

	public Collection getPosizioneGiuridicaMisAlt() {
		return mPosizioneGiuridicaMisAlt;
	}

	public Collection getPosizioneGiuridicaAltraCausa() {
		return mPosizioneGiuridicaAltraCausa;
	}

	public Collection getFlagErgastolo() {
		return mFlagErgastolo;
	}

	public Collection getTipoPeneAccessorie() {
		return mTipoPenaAccessoria;
	}

	public Collection getDurataPeneAccessorie() {
		return mDurataPenaAccessoria;
	}

	public Collection getTipoMisuraCautelare() {
		return mTipoMisuraCautelare;
	}

	public Collection getTipoMisuraEsecuzione() {
		return mTipoMisuraEsecuzione;
	}

	public Collection getFlagSN() {
		return mFlagSN;
	}

	public Collection getFlagLireEuro() {
		return mFlagLireEuro;
	}

	public Collection getTipoBeneficio() {
		return mTipoBeneficio;
	}

	public Collection getDPR() {
		return mDPR;
	}

	public Collection getNaturaBeneficio() {
		return mNaturaBeneficio;
	}

	public Collection getTipoSospSubordinata() {
		return mTipoSospSubordinata;
	}

	public Collection getSottoTipoBeneficio() {
		return mSottoTipoBeneficio;
	}

	public Collection getSottoTipoBeneficioIndulto() {
		return mSottoTipoBeneficioIndulto;
	}

	public Collection getTipoNotifica() {
		return mTipoNotifica;
	}

	public Collection getEsitoNotifica() {
		return mEsitoNotifica;
	}

	public Collection getEsitoProvvedimentoIstanza() {
		return mEsitoProvvedimentoIstanza;
	}

	public Collection getEsitoTenore() {
		return mEsitoTenore;
	}

	public Collection<DecodificheModel> getTipoAutorita() {
		return mTipoAutorita;
	}

	public Collection getTipoAutoritaSanzioni() {
		return mTipoAutoritaSanzioni;
	}

	public Collection getTipoAutoritaEmittente() {
		return mTipoAutoritaEmittente;
	}

	public Collection getTipoAutoritaCumulo() {
		return mTipoAutoritaCumulo;
	}

	public Collection getTipoUfficioCumulo() {
		return mTipoUfficioCumulo;
	}

	public Collection getTipoUfficioCumuloSentenzaDecreto() {
		return mTipoUfficioCumuloSentenzaDecreto;
	}

	public Collection<DecodificheModel> getTipoUfficioCumuloRifSiep() {
		return mTipoUfficioCumuloRifSiep;
	}

	public Collection getTipoUfficioCumuloUfficioLoginRifSiep() {
		return mTipoUfficioCumuloUfficioLoginRifSiep;
	}

	public Collection getTipoUfficioCumuloRifMSic() {
		return mTipoUfficioCumuloRifMSic;
	}

	public Collection getTipoAutoritaArresto() {
		return mTipoAutoritaArresto;
	}

	// modifica relativa al tipo istituto
	public Collection getTipoAutoritaIstituto() {
		return mTipoAutoritaIstituto;
	}

	public Collection getTipoProcura() {
		return mTipoProcura;
	}

	public Collection getBilanciamentoCircostanze() {
		return mBilanciamentoCircostanze;
	}

	public Collection getTipoAssoluzione() {
		return mTipoAssoluzione;
	}

	public Collection getTipoSanzioneSostitutiva() {
		return mTipoSanzioneSostitutiva;
	}

	// MEV_2023-13
	public Collection getTipoPenaSostitutiva() {
		return mTipoPenaSostitutiva;
	}

	public Collection getTipoAtto() {
		return mTipoAtto;
	}

	public Collection getStampeCumulo() {
		return mStampeCumulo;
	}

	public Collection getTipoRicorso() {
		return mTipoRicorso;
	}

	public Collection getTipoRicorsoSige() {
		return mTipoRicorsoSige;
	}

	public Collection getSoggettoImpugnante() {
		return mSoggettoImpugnante;
	}

	public Collection getSoggettoImpugnanteSige() {
		return mSoggettoImpugnanteSige;
	}

	public Collection getOggettoSospensioni() {
		return mOggettoSospensioni;
	}

	public Collection getMittenteAtto() {
		return mMittenteAtto;
	}

	public Collection getMotivoProvvedimento() {
		return mMotivoProvvedimento;
	}

	public Collection getRevocaDecSosp() {
		return mRevocaDecSosp;
	}

	public Collection getMotivoProvvedimentoIstanza() {
		return mMotivoProvvedimentoIstanza;
	}

	public Collection getOggettoProcedimento() {
		return mOggettoProcedimento;
	}

	public Collection getOggettoProcedimentoTDS() {
		return mOggettoProcedimentoTDS;
	}

	public Collection getOggettoProcedimentoUDS() {
		return mOggettoProcedimentoUDS;
	}

	public Collection getFlagStato() {
		return mFlagStato;
	}

	public Collection getTipoUfficioSIEP() {
		return mTipoUfficioSIEP;
	}

	public Collection getTipoUfficioSIEPTrattino() {
		return mTipoUfficioSIEPTrattino;
	}

	public Collection<DecodificheModel> getTipoUfficioSIUS() {
		return mTipoUfficioSIUS;
	}

	public Collection<DecodificheModel> getTipoUfficioSIUSMinor() {
		return mTipoUfficioSIUSMinor;
	}

	public Collection getNaturaMisuraSicurezza() {
		return mNaturaMisuraSicurezza;
	}

	public Collection getTipoMisuraSicurezza() {
		return mTipoMisuraSicurezza;
	}

	public Collection getStatoMisuraSicurezzaCumulo() {
		return mStatoMisuraSicurezza;
	}

	public Collection getDestinatarioDeposito() {
		return mDestinatarioDeposito;
	}

	public Collection getDestinatarioDepositoMinorenni() {
		return mDestinatarioDepositoMinorenni;
	}

	public Collection getTipoSanzione() {
		return mTipoSanzione;
	}

	public Collection getMotivoNonComputabile() {
		return mMotivoNonComputabile;
	}

	public Collection getMotivoNonComputabileDescCompleta() {
		return mMotivoNonComputabileDescCompleta;
	}

	public Collection getTipoContinuazione() {
		return mTipoContinuazione;
	}

	public Collection getFirmaUfficio() {
		return mTipoFirma;
	}

	public Collection getTipoAnnotazioneManualeBenefici() {
		return mTipoAnnotazioneManualeBenefici;
	}

	public Collection getRideterminazionePenaAltro() {
		return mRideterminazionePenaAltro;
	}

	public Collection getRideterminazionePenaAltroDufficio() {
		return mRideterminazionePenaAltroUff;
	}

	public Collection getRideterminazionePenaAltroAUfficio() {
		return mRideterminazionePenaAltroAUff;
	}

	public Collection getRideterminazionePenaAltroSORV() {
		return mRideterminazionePenaAltroSORV;
	}

	public Collection getRideterminazionePenaAltroGE() {
		return mRideterminazionePenaAltroGE;
	}

	public Collection getTipoAnnotazioneManualeMC() {
		return mTipoAnnotazioneManualeMC;
	}

	public Collection getTipoAnnotazioneManualeTutte() {
		return mTipoAnnotazioneManualeTutte;
	}

	public Collection getOggettiConversionePP() {
		return mOggettiConversionePP;
	}

	public Collection getTipoAnnotazioneManuale() {
		return mTipoAnnotazioneManuale;
	}

	public Collection getTipoCausaleComputoMCSenzaTitolo() {
		return mTipoCausaleComputoMCSenzaTitolo;
	}

	public Collection getTipoCausaleComputoMCAltroTitolo() {
		return mTipoCausaleComputoMCAltroTitolo;
	}

	public Collection getMotivoProvvedimentoMADDom() {
		return mMotivoProvvedimentoMADDom;
	}

	// 03/08/2010 Espiazione Pena presso Domicilio.
	public Collection getMotivoProvvedimentoMAEspPressoDom() {
		return mMotivoProvvedimentoMAEspPressoDom;
	}

	// 27/09/2010 Espiazione Pena presso Domicilio.
	public Collection getMotivoProvvedimentoMASospEspPressoDom() {
		return mMotivoProvvedimentoMASospEspPressoDom;
	}

	// 27/09/2010 Espiazione Pena presso Domicilio.
	public Collection getMotivoProvvedimentoMARipristinoEspPressoDom() {
		return mMotivoProvvedimentoMARipristinoEspPressoDom;
	}

	// 27/09/2010 Espiazione Pena presso Domicilio.
	public Collection getMotivoProvvedimentoMAPerEfficEspPressoDom() {
		return mMotivoProvvedimentoMAPerEfficEspPressoDom;
	}

	// 27/09/2010 Espiazione Pena presso Domicilio.
	public Collection getMotivoProvvedimentoRevocaMAEspPressoDom() {
		return mMotivoProvvedimentoRevocaEspPressoDom;
	}

	// 27/09/2010 Espiazione Pena presso Domicilio.
	public Collection getMotivoProvvedimentoProsecProvvMAEspPressoDom() {
		return mMotivoProvvedimentoProsecProvvMAEspPressoDom;
	}

	public Collection getMotivoOrdineScarcerazione() {
		return mMotivoProvvedimentoOS;
	}

	public Collection getMotivoOSLiberazioneAnticipataMA() {
		return mMotivoProvvedimentoOSLibAntMA;
	}

	public Collection getMotivoProvvedimentoMAffP() {
		return mMotivoProvvedimentoMAAffP;
	}

	public Collection getMotivoProvvedimentoMAConIndultino() {
		return mMotivoProvvedimentoMAConIndultino;
	}

	public Collection getMotivoProvvedimentoMASospIndultino() {
		return mMotivoProvvedimentoSospProvvMAIndultino;
	}

	public Collection getMotivoProvvedimentoMARipristinoIndultino() {
		return mMotivoProvvedimentoMARipristinoIndultino;
	}

	public Collection getMotivoProvvedimentoMAPerEfficIndultino() {
		return mMotivoProvvedimentoMAPerEfficIndultino;
	}

	public Collection getMotivoProvvedimentoEspulsione() {
		return mMotivoProvvedimentoEspulsione;
	}

	public Collection getMotivoProvvedimentoAccoglieOpEspulsione() {
		return mMotivoProvvedimentoAccoglieOpEspulsione;
	}

	public Collection getMotivoProvvedimentoRigettoOpEspulsione() {
		return mMotivoProvvedimentoRigettoOpEspulsione;
	}

	public Collection getMotivoProvvedimentoMAPreEffAffPro() {
		return mMotivoProvvedimentoMAPreEffAffPro;
	}

	public Collection getMotivoProvvedimentoMAPreEffDetDom() {
		return mMotivoProvvedimentoMAPreEffDetDom;
	}

	public Collection getMotivoProvvedimentoMAPreEffSemli() {
		return mMotivoProvvedimentoMAPreEffSemli;
	}

	public Collection getMotivoProvvedimentoMADetDomSpeAff() {
		return mMotivoProvvedimentoMADetDomSpeAmmAff;
	}

	public Collection getMotivoProvvedimentoDicEffMAffP() {
		return mMotivoProvvedimentoDicEffMAAffP;
	}

	public Collection getMotivoProvvedimentoMASemiL() {
		return mMotivoProvvedimentoMASemiL;
	}

	public Collection getMotivoProvvedimentoDetDomSpecAmmiPeriodo() {
		return mMotivoProvvedimentoDetDomSpecAmmiPeriodo;
	}

	public Collection getMotivoProvvedimentoDetDomSpecSospProvv() {
		return mMotivoProvvedimentoDetDomSpecSospProvv;
	}

	public Collection getMotivoProvvedimentoSospProvvMADetDom() {
		return mMotivoProvvedimentoSospProvvDetDom;
	}

	public Collection getMotivoProvvedimentoSospProvvMAffP() {
		return mMotivoProvvedimentoSospProvvAffP;
	}

	public Collection getMotivoProvvedimentoSospProvvMASemiL() {
		return mMotivoProvvedimentoSospProvvSemi;
	}

	public Collection getMotivoProvvedimentoRevocaMADetDom() {
		return mMotivoProvvedimentoRevocaDetDom;
	}

	public Collection getMotivoProvvedimentoRevocaMAffP() {
		return mMotivoProvvedimentoRevocaAffP;
	}

	public Collection getMotivoProvvedimentoRevocaMASemiL() {
		return mMotivoProvvedimentoRevocaSemi;
	}

	public Collection getMotivoProvvedimentoRevocaMAIndultino() {
		return mMotivoProvvedimentoRevocaIndultino;
	}

	public Collection getMotivoSollecitoMisureSicurezza() {
		return mMotivoSollecitoMisureSicurezza;
	}

	// 02/12/2010 Inizio : Aggiunti Daniela
	public Collection getMotivoProvvedimentoCessazioneMADetDom() {
		return mMotivoProvvedimentoCessazioneDetDom;
	}

	public Collection getMotivoProvvedimentoCessazioneMAffP() {
		return mMotivoProvvedimentoCessazioneAffP;
	}

	public Collection getMotivoProvvedimentoCessazioneMADetDomTerm() {
		return mMotivoProvvedimentoCessazioneDetDomTerm;
	}

	// ========== DL 146/2013 cessazione disposta dal MDS =======
	public Collection getMotivoProvvedimentoCessazioneMAffPMDS51bis() {
		return mMotivoProvvedimentoCessazioneAffPMDS51bis;
	}

	public Collection getMotivoProvvedimentoCessazioneMADetDomMDS51bis() {
		return mMotivoProvvedimentoCessazioneDetDomMDS51bis;
	}

	public Collection getMotivoProvvedimentoCessazioneMASemiLMDS51bis() {
		return mMotivoProvvedimentoCessazioneSemiMDS51bis;
	}

	public Collection getMotivoProvvedimentoCessazioneMAIndultinoMDS51bis() {
		return mMotivoProvvedimentoCessazioneIndultinoMDS51bis;
	}

	public Collection getMotivoProvvedimentoCessazioneMADetDomTermMDS51bis() {
		return mMotivoProvvedimentoCessazioneDetDomTermMDS51bis;
	}

	public Collection getMotivoProvvedimentoCessazioneMAEspPressoDomMDS51bis() {
		return mMotivoProvvedimentoCessazioneEspPressoDomMDS51bis;
	}

	// ===================================================================

	// ===================================================================
	public Collection getMotivoProvvedimentoCessazioneMAffPTDS51bis() {
		return mMotivoProvvedimentoCessazioneAffPTDS51bis;
	}

	public Collection getMotivoProvvedimentoCessazioneMADetDomTDS51bis() {
		return mMotivoProvvedimentoCessazioneDetDomTDS51bis;
	}

	public Collection getMotivoProvvedimentoCessazioneMASemiLTDS51bis() {
		return mMotivoProvvedimentoCessazioneSemiTDS51bis;
	}

	public Collection getMotivoProvvedimentoCessazioneMADetDomTermTDS51bis() {
		return mMotivoProvvedimentoCessazioneDetDomTermTDS51bis;
	}

	public Collection getMotivoProvvedimentoCessazioneMAEspPressoDomTDS51bis() {
		return mMotivoProvvedimentoCessazioneEspPressoDomTDS51bis;
	}

	// ===================================================================

	public Collection getMotivoProvvedimentoCessazioneMASemiL() {
		return mMotivoProvvedimentoCessazioneSemi;
	}

	public Collection getMotivoProvvedimentoCessazioneMAIndultino() {
		return mMotivoProvvedimentoCessazioneIndultino;
	}

	public Collection getMotivoProvvedimentoCessazioneMAEspPressoDom() {
		return mMotivoProvvedimentoCessazioneEspPressoDom;
	}

	// 02/12/2010 Fine
	public Collection getMotivoProvvedimentoProsecProvvMAAffPro() {
		return mMotivoProvvedimentoProsecProvvMAAffPro;
	}

	public Collection getMotivoProvvedimentoProsecProvvMADetDom() {
		return mMotivoProvvedimentoProsecProvvMADetDom;
	}

	public Collection getMotivoProvvedimentoProsecProvvMASem() {
		return mMotivoProvvedimentoProsecProvvMASem;
	}

	// ============================================================================
	// new DL 146 2013
	// MDS dispone con Ordinanza e TDS dispone su Reclamo
	public Collection getMotivoProvvedimentoProsecMAAffProMDS51Bis() { // Affidamento in prova
		return mMotivoProvvedimentoProsecMAAffProMDS51Bis;
	}

	public Collection getMotivoProvvedimentoProsecMADetDomMDS51Bis() { // Detenzione Domiciliare
		return mMotivoProvvedimentoProsecMADetDomMDS51Bis;
	}

	public Collection getMotivoProvvedimentoProsecMASemMDS51Bis() { // Semilibertà
		return mMotivoProvvedimentoProsecMASemMDS51Bis;
	}

	public Collection getMotivoProvvedimentoProsecMAEsecPreDomMDS51Bis() { // Esecuzione presso domicilio
		return mMotivoProvvedimentoProsecMAEsecPreDomMDS51Bis;
	}

	public Collection getMotivoProvvedimentoProsecMADetDomTerMDS51Bis() { // Detenzione Domiciliare a Termine
		return mMotivoProvvedimentoProsecMADetDomTerMDS51Bis;
	}

	// Codici del TDS che dispone su Reclamo
	public Collection getMotivoProvvedimentoProsecMAAffPro_TDS_51Bis() { // Affidamento in prova
		return mMotivoProvvedimentoProsecMAAffProTDS51Bis;
	}

	public Collection getMotivoProvvedimentoProsecMADetDom_TDS_51Bis() { // Detenzione Domiciliare
		return mMotivoProvvedimentoProsecMADetDom_TDS_51Bis;
	}

	public Collection getMotivoProvvedimentoProsecMASem_TDS_51Bis() { // Semilibertà
		return mMotivoProvvedimentoProsecMASem_TDS_51Bis;
	}

	public Collection getMotivoProvvedimentoProsecMAEsecPreDom_TDS_51Bis() { // Esecuzione presso domicilio
		return mMotivoProvvedimentoProsecMAEsecPreDom_TDS_51Bis;
	}

	public Collection getMotivoProvvedimentoProsecMADetDomTer_TDS_51Bis() { // Detenzione Domiciliare a
																			// Termine
		return mMotivoProvvedimentoProsecMADetDomTer_TDS_51Bis;
	}

	// FIne modifiche DL 146 /2013
	// ===========
	public Collection getMotivoProvvedimentoProsecProvvMAIndultino() {
		return mMotivoProvvedimentoProsecProvvMAIndultino;
	}

	public Collection getMotivoRevocaSS() {
		return mMotivoRevocaSS;
	}

	public Collection getMotivoProvvedimentoEstDefMAffP() {
		return mMotivoProvvedimentoEstDefMAffP;
	}

	public Collection getMotivoProvvedimentoRigettoMA() {
		return mMotivoProvvedimentoRigettoMA;
	}

	public Collection getMotivoProvvedimentoEstDefMADetDom() {
		return mMotivoProvvedimentoEstDefMADetDom;
	}

	public Collection getMotivoProvvedimentoEstDefMASem() {
		return mMotivoProvvedimentoEstDefMASem;
	}

	public Collection getMotivoProvvedimentoRipristinoMAffP() {
		return mMotivoProvvedimentoRipristinoMAffP;
	}

	public Collection getMotivoProvvedimentoRipristinoDetDom() {
		return mMotivoProvvedimentoRipristinoDetDom;
	}

	public Collection getMotivoProvvedimentoRipristinoSemi() {
		return mMotivoProvvedimentoRipristinoSemi;
	}

	public Collection getMotivoProvvedimentoProrogaUlteriorePeriodo() {
		return mMotivoProvvedimentoProrogaUlteriorePeriodo;
	}

	public Collection getMotivoProvvedimentoRipristinoDetDomSpec() {
		return mMotivoProvvedimentoRipristinoDetDomSpec;
	}

	public Collection getMotivoProvvedimentoAmmProvDetDom() {
		return mMotivoProvvedimentoAmmProvDetDom;
	}

	public Collection getMotivoProvvedimentoAmmProvAffi() {
		return mMotivoProvvedimentoAmmProvAffi;
	}

	// MEV_2019-09-SIEP
	public Collection getMotivoProvvedimentoAmmProvDetDomPmm() {
		return mMotivoProvvedimentoAmmProvDetDomPmm;
	}

	public Collection getMotivoProvvedimentoAmmProvAffiPmm() {
		return mMotivoProvvedimentoAmmProvAffiPmm;
	}

	public Collection getMotivoProvvedimentoMAffPMinor() {
		return mMotivoProvvedimentoMAAffPMinor;
	}

	public Collection getMotivoProvvedimentoAmmProvSemilibPmm() {
		return mMotivoProvvedimentoAmmProvSemilibPmm;
	}

	public Collection getMotivoProvvedimentoAmmProvSemilibPm() {
		return mMotivoProvvedimentoAmmProvSemilibPm;
	}

	public Collection getMotivoProvvedimentoMADDomMinor() {
		return mMotivoProvvedimentoMADDomMinor;
	}

	public Collection getMotivoProvvedimentoMASemiLMinor() {
		return mMotivoProvvedimentoMASemiLMinor;
	}
	// MEV_2019-09-SIEP - FINE

	public Collection getMotivoProvvedimentoMADetDomTemp() {
		return mMotivoProvvedimentoMADetDomTemp;
	}

	public Collection getMotivoProvvedimentoMADetDomTempProroga() {
		return mMotivoProvvedimentoMADetDomTempProroga;
	}

	public Collection getMotivoProvvedimentoMADetDomTempProrogaProvvisoria() {
		return mMotivoProvvedimentoMADetDomTempProrogaProvvisoria;
	}

	public Collection getMotivoProvvedimentoMAReLibCond() {
		return mMotivoProvvedimentoMAReLibCond;
	}

	public Collection getMotivoProvvedimentoMACOLibCond() {
		return mMotivoProvvedimentoMACOLibCond;
	}

	public Collection getMotivoProvvedimentoUlteriorePeriodoMA() {
		return mMotivoProvvedimentoUlteriorePeriodoMA;
	}

	public Collection getTipoDecreto() {
		return mTipoDecreto;
	}

	public Collection getStatoLibertatis() {
		return mStatoLibertatis;
	}

	public Collection getStatoPermesso() {
		return mStatoPermesso;
	}

	public Collection getTipoOrdinanza() {
		return mTipoOrdinanza;
	}

	public Collection getTipoSentenza() {
		return mTipoSentenza;
	}

	public Collection getFlagSNTrattino() {
		return mFlagSNTrattino;
	}

	public Collection getTipoEvento() {
		return mTipoEvento;
	}

	public Collection getTipoRegistroOrdinanza() {
		return mTipoRegistroOrdinanza;
	}

	public Collection getListaAutoritaSospensione() {
		return mListaAutoritaSospensione;
	}

	public Collection getListaOggettiSospensione() {
		return mListaOggettiSospensione;
	}

	public Collection getListaOggettiRevoca() {
		return mListaOggettiRevoca;
	}

	public Collection getListaMotiviProvvedimentoSospensione() {
		return mListaMotiviProvvedimentoSospensione;
	}

	public Collection getListaMotiviProvvedimentoRevoca() {
		return mListaMotiviProvvedimentoRevoca;
	}

	public Collection getListaEsitiTenoreSospensione() {
		return mListaEsitiTenoreSospensione;
	}

	public Collection getListaEsitiTenoreRevoca() {
		return mListaEsitiTenoreRevoca;
	}

	public Collection getRegistroProvGenerico() {
		return mRegistroProvGenerico;
	}

	public Collection getListaAttivitaAvvocato() {
		return mListaAttivitaAvvocato;
	}

	public Collection getMotivoDesignazione() {
		return mMotivoDesignazione;
	}

	public Collection getMotivoSanzioneSostitutiva() {
		return mMotivoSanzioneSostitutiva;
	}

	public Collection getMotivoMisuraSicurezza() {
		return mMotivoMisuraSicurezza;
	}

	public Collection getTipoProvvedimentoCumulo() {
		return mTipoProvvedimentoCumulo;
	}

	public Collection getListaAutoritaSospTDSUDS() {
		return mListaAutoritaSospTDSUDS;
	}

	public Collection getListaAutoritaSospTDSUDSTrattino() {
		return mListaAutoritaSospTDSUDSTrattino;
	}

	public Collection getOggettoSospensioneDiff() {
		return mOggettoSospensioneDiff;
	}

	public Collection getOggettoSospensioneDiffProvv() {
		return mOggettoSospensioneDiffProvv;
	}

	public Collection getOggettoSospensioneDiffDef() {
		return mOggettoSospensioneDiffDef;
	}

	public Collection getOggettoRevocaDifferimento() {
		return mOggettoRevocaDiff;
	}

	public Collection getTipologiaDecisioneSospensioneDiff() {
		return mTipologiaDecisioneSospensioneDiff;
	}

	public Collection getTipologiaDecisioneSospensioneDiffProvv() {
		return mTipologiaDecisioneSospensioneDiffProvv;
	}

	public Collection getTipologiaDecisioneSospensioneDiffDef() {
		return mTipologiaDecisioneSospensioneDiffDef;
	}

	public Collection getTipologiaDecisioneRevocaDifferimento() {
		return mTipologiaDecisioneRevocaDiff;
	}

	public Collection getTipologiaDecisioneRigettoDifferimento() {
		return mTipologiaDecisioneRigettoDiff;
	}

	public Collection getMotivoInterruzione() {
		return mMotivoInterruzione;
	}

	public Collection getMotivoNLP() {
		return mMotivoNLP;
	}

	public Collection getPosizioniSanzione() {
		return mPosizioniSanzione;
	}

	public Collection getMotivoFineEspiazione() {
		return mMotivoFineEspiazione;
	}

	public Collection getMotivoDefiGe() {
		return mMotivoDefiGe;
	}

	public Collection getMotivoDefAltro() {
		return mMotivoDefAltro;
	}

	public Collection getMotivoDefiSor() {
		return mMotivoDefiSor;
	}

	public Collection getMotivoDefiAltro() {
		return mMotivoDefiAltro;
	}

	public Collection getTipoProvvSorveglianza() {
		return mTipoProvvSorveglianza;
	}

	public Collection getAutoritaSorveglianza() {
		return mAutoritaSorveglianza;
	}

	public Collection getAutoritaGE() {
		return mAutoritaGE;
	}

	public Collection getAutoritaAltro() {
		return mAutoritaAltro;
	}

	public Collection getMotivoSospensionePm() {
		return mMotivoSospensionePm;
	}

	public Collection getTipoPermesso() {
		return mTipoPermesso;
	}

	public Collection getOggettoDecisione() {
		return mOggettoDecisione;
	}

	// MEV_2019-09-SIEP: aggiunti metodi GET
	public Collection getOggettoDecisioneMinor() {
		return mOggettoDecisioneMinor;
	}

	public Collection getOggettiDecisioneSosp678() {
		return mOggettiDecisioneSosp678;
	}
	// FINE MEV_2019-09-SIEP

	public Collection getStatoProcedimento() {
		return this.mStatoProcedimento;
	}

	public Collection getTipoRichiestaRC() {
		return this.mTipoRichiestaRC;
	}

	public Collection getTipoRichiestaPVR() {
		return this.mTipoRichiestaPVR;
	}

	public Collection getQuesture() {
		return this.mQuesture;
	}

	public Collection getTipoRichiestaT() {
		return this.mTipoRichiestaT;
	}

	public Collection getTipoRichiestaTrasmComp() {
		return this.mTipoRichiestaTrasmComp;
	}

	public Collection getMotivoProvvedimentiRichGen() {
		return this.mMotivoProvvedimentiRichGen;
	}

	public Collection getTipoIntSosp() {
		return this.mTipoIntSosp;
	}

	public Collection getMotivoIntSosp() {
		return this.mMotivoIntSosp;
	}

	public Collection getTipoUfficioGE() {
		return this.mTipoUfficioGE;
	}

	public Collection getTipoUfficioPM() {
		return this.mTipoUfficioPM;
	}

	public Collection getMotivoRDS() {
		return this.mMotivoRDS;
	}

	public Collection getTipoContenuto() {
		return mTipoContenuto;
	}

	public Collection getTipoContenutoIstanza() {
		return mTipoContenutoIstanza;
	}

	public Collection getVistaPm() {
		return mVistaPm;
	}

	public Collection getTipoRitoSentenza() {
		return mTipoRitoSentenza;
	}

	public Collection getTipoProvvedimentoMSic() {
		return mTipoProvvedimentoMSic;
	}

	public Collection getTipoLicenza() {
		return mTipoLicenza;
	}

	public Collection getAutoritaRdpGE() {
		return mAutoritaRdpGE;
	}

	public Collection getAutoritaRdpAltro() {
		return mAutoritaRdpAltro;
	}

	public Collection getAutoritaRdpSorv() {
		return mAutoritaRdpSorv;
	}

	public Collection getTipologiaNumerazione() {
		return mTipologiaNumerazione;
	}

	public Collection getMotivoNonInvio() {
		return mMotivoNonInvio;
	}

	public Collection getAutoritaCompetente() {
		return mAutoritaCompetente;
	}

	public Collection getListaTipoProvvCumulo() {
		return mListaTipoProvvCumulo;
	}

	public Collection getListaTipoProvvOrdinanza() {
		return mListaTipoProvvOrdinanza;
	}

	public Collection getListaTipoProvvDecretoArchiviazione() {
		return mListaTipoProvvDecretoArchiviazione;
	}

	public Collection getTipoSanzioneConvertita() {
		return mTipoSanzioneConvertita;
	}

	// 15-01-2015 combo Autorita Emittente in Associa Titolo Esecutivo a Misura Sicurezza
	public Collection getAutoritaEmittente_Sorveglianza() {
		return mAutoritaEmi_Sorveglianza;
	}

	// 01-09-2015 MEV_2 - Mis Sic STEP2
	public Collection getOggettoProcedimentoMS() {
		return mOggettoProcedimentoMS;
	}

	// MEV26 - Cumulo, ma non solo
	public Collection getTipoRegistroGenerale() {
		return mTipoRegistroGenerale;
	}

	public Collection getTipoSanzioneSostitutivaLpu() {
		return mTipoSanzioneSostitutivaLpu;
	}

	public Collection getPosizioniGiuridicheCumLibero() {
		return mPosizioniGiuridicheCumLibero;
	}

	public Collection getPosizioniGiuridicheCumEspIst() {
		return mPosizioniGiuridicheCumEspIst;
	}

	public Collection getPosizioniGiuridicheCumEspAltro() {
		return mPosizioniGiuridicheCumEspAltro;
	}

	public Collection getMotiviProvvCumuloNew() {
		return mMotiviProvvCumuloNew;
	}
	// Fine MEV26

	public Collection<DecodificheModel> getTipoAutoritaMinorenni() {
		return mTipoAutoritaMinorenni;
	}

	public void setTipoAutoritaMinorenni(Collection<DecodificheModel> mTipoAutoritaMinorenni) {
		this.mTipoAutoritaMinorenni = mTipoAutoritaMinorenni;
	}

	public Collection<DecodificheModel> getTipoMisuraSicurezzaMinorenni() {
		return mTipoMisuraSicurezzaMinorenni;
	}

	public void setTipoMisuraSicurezzaMinorenni(Collection<DecodificheModel> mTipoMisuraSicurezzaMinorenni) {
		this.mTipoMisuraSicurezzaMinorenni = mTipoMisuraSicurezzaMinorenni;
	}

	public Collection getTipoUfficioSez1DDTrattino() {
		return mTipoUfficioSez1DDTrattino;
	}

	// MEV10-s3: aggiunte collection per gestire invio mail segnalazione
	public Collection getTitoloPersona() {
		return mTitoloPersona;
	}

	public Collection getFunzionalitaSIEPE() {
		return mFunzionalitaSIEPE;
	}

	public Collection getFunzionalitaSIUS() {
		return mFunzionalitaSIUS;
	}

	public Collection getFunzionalitaSIEP() {
		return mFunzionalitaSIEP;
	}

	public Collection getFunzionalitaSIGE() {
		return mFunzionalitaSIGE;
	}

	public Collection getAzione() {
		return mAzione;
	}

	public Collection getTipoSegnalazione() {
		return mTipoSegnalazione;
	}

	public Collection getGravitaSegnalazione() {
		return mGravitaSegnalazione;
	}

	public Collection<DecodificheModel> getTipoUfficioSiusTDSMUDSM() {
		return mTipoUfficioSiusTDSMUDSM;
	}

	public Collection<DecodificheModel> getTipoMSMinorenniNonDetentiva() {
		return mTipoMSMinorenniNonDetentiva;
	}

	public Collection<DecodificheModel> getTipoMSMinorenniDetentiva() {
		return mTipoMSMinorenniDetentiva;
	}

	public Collection getOggettoProcedimentoTDSM() {
		return mOggettoProcedimentoTDSM;
	}

	public Collection getOggettoProcedimentoUDSM() {
		return mOggettoProcedimentoUDSM;
	}

	// FINE MEV10-s3
	public Collection<DecodificheModel> getTipoUfficioSiepTDSMUDSM() {
		return mTipoUfficioSiepTDSMUDSM;
	}

	public Collection<DecodificheModel> getTipoUffEsePenEstSerSocMin() {
		return mTipoUffEsePenEstSerSocMin;
	}

	public Collection getTipoUfficioSige() {
		return mTipoUfficioSige;
	}

	// MEV_2023-13: aggiunta collezione per il tipo tutore
	public Collection getTipoTutore() {
		return mTipoTutore;
	}

	// MEV_2023-13: aggiunta collezione per la ragione sociale
	public Collection getRagioneSocialeCO() {
		return mRagioneSocialeCO;
	}

	// MEV_2023-33
	public Collection getTipoAutoritaPolizia() {
		return mTipoAutoritaPolizia;
	}

	// MEV_2023-35 Lista Oggetti Esecuzione Pene Sostitutive U126
	public Collection getMotivoEsecuzionePeneSostitutive() {
		return mMotivoEsecuzionePeneSostitutive;
	}

	// MEV_2023-35 Lista Oggetti Sospensione esecuzione pene accessorie (U141, C066)
	public Collection getTipoPenaAccessoriaPS() {
		return mTipoPenaAccessoriaPS;
	}

}