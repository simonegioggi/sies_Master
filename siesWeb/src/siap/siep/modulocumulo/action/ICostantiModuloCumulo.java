package siap.siep.modulocumulo.action;

/**
* <p>Title: ICostantiModuloCumulo</p>
* <p>Description: Classe di costanti di ICostantiModuloCumulo</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiModuloCumulo 
{
  public static final String NOME_FORM = "NomeForm";
  public static final String CAMPO_MOTIVO_MODIFICA = "MotivoModifica";

  public static final String MODALITA = "modalita";
  public static final String MODALITA_MODIFICA = "M";
  public static final String MODALITA_INSERIMENTO = "I";
  public static final String MODALITA_CANCELLA = "C";
  
  
  public static final String PG_LOAD_GRIGLIA_DATI_ANALITICI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/GrigliaDatiAnalitici.jsp";
  public static final String PG_LOAD_POPUP_ANNULLAMENTO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/PupUpAnnullamento.jsp";
  
  //============================================================================
  //
  //============================================================================
  public static final String PG_LOAD_GRIGLIA_RICH_ISTRUTTORIE  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/GrigliaRichiesteIstruttorie.jsp";
  public static final String PG_LOAD_CANCELLA_RICH_ISTRUTTORIE  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadCancellaRichiesteIstruttorie.jsp";

  //Include
  public static final String PG_DETTAGLIO_ISTRUTTORIA_INCLUDE  = IWebConstants.ROOT_DIR  + "files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp";
  public static final String PG_DETTAGLIO_TITOLO_INCLUDE  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioTitoloCumulatoInclude.jsp";

  
  //============================================================================
  // Richiesta Trasmissione Atti
  //============================================================================
  public static final String PG_LOAD_RICERCA_TITOLO_DA_RICHIEDERE = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadRicercaTitoloDaRichiedere.jsp";
  public static final String PG_LOAD_RICHIESTA_TRASMISSIONE_TITOLO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadRichiestaTrasmissioneTitolo.jsp";
  public static final String PG_LOAD_DETTAGLIO_RICHIESTA_TRASMISSIONE_TITOLO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaTrasmissioneTitolo.jsp";
  public static final String PG_LOAD_TRASFERISCI_RICHIESTA_TRASMISSIONE_TITOLO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadTrasferisciRichiestaTrasmissioneTitolo.jsp";
    
  // Richieste Atti Ricevute
  public static final String PG_LISTA_RICHIESTE_ATTI_RICEVUTE        = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ListaRichiesteAttiRicevute.jsp";
  public static final String PG_DETTAGLIO_RICHIESTA_ATTI_RICEVUTA    = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaAttiRicevuta.jsp";
  public static final String PG_LISTA_PROCEDIMENTI_SENTENZA_SOGGETTO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ListaProcedimentiPerTitoloSoggetto.jsp";

//Richieste Atti Trasmesse
 public static final String PG_RICERCA_RICHIESTE_ATTI_TRASMESSE       = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/RicercaRichiesteAttiTrasmessiCumulo.jsp";
 public static final String PG_DETTAGLIO_RICHIESTA_ATTI_CUMULO        = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaAttiCumulo.jsp";
 public static final String PG_LOAD_INSERISCI_SOLLECITO_RICHIESTA_ATTI_TC_CUMULO   = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciSollecitoRichiestaAttiPerTrasfCompCumulo.jsp";
 public static final String PG_DETTAGLIO_SOLLECITO_RICHIESTA_ATTI_TC_CUMULO   = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioSollecitoRichiestaAttiTrasfCompCumulo.jsp";
 public static final String PG_LOAD_TRASFERISCI_SOLLECITO_RICHIESTA_ATTI_TC_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadTrasferisciRichiestaAttiTrasfCompCumulo.jsp";

   
  //============================================================================
  // Richieste del PM
  //============================================================================
  public static final String PG_LOAD_GRIGLIA_RICHIESTE_PM           = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/GrigliaRichiesteDelPM.jsp";
  public static final String PG_LOAD_GRIGLIA_RICHIESTE_PM_AL_GE     = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/GrigliaRichiesteDelPMalGE.jsp";
  public static final String PG_LOAD_GRIGLIA_RICHIESTE_PM_ALLA_SORV = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/GrigliaRichiesteDelPMallaSORV.jsp";
  public static final String PG_LOAD_ELENCO_RICHIESTE_DEL_PM        = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ListaRichiesteDelPM.jsp";
  public static final String PG_LOAD_INSERISCI_RICHIESTE_DEL_PM     = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciRichiesteDelPM.jsp";
  public static final String PG_LOAD_EMETTI_RICHIESTE_DEL_PM     	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadEmettiRichiesteDelPM.jsp";
  public static final String PG_LOAD_EMETTI_RICHIESTE_DEL_PM_SORV  	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadEmettiRichiesteDelPMallaSORV.jsp";
  public static final String PG_LOAD_ELENCO_RICHIESTE_DEL_PM_SORV   = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ListaRichiesteDelPMallaSORV.jsp";
  
  
  //============================================================================
  // Dati Finali Cumulo da spostare in ICostantiDatiFinaliCumulo
  //============================================================================
  //public static final String PG_LOAD_INSERISCI_DATI_FINALI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciDatiFinali.jsp";
  public static final String PG_LOAD_DETTAGLIO_DATI_FINALI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioDatiFinali.jsp";

  public static final String PG_LOAD_INSERISCI_PENE_RIDETERMINATE = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciPeneRideterminate.jsp";
  public static final String PG_LOAD_DETTAGLIO_PENE_RIDETERMINATE = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioPeneRideterminate.jsp";
  
  
  public static final String PG_LOAD_DETTAGLIO_ALTRE_SANZIONI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioAltreSanzioni.jsp";
  public static final String PG_LOAD_SELEZIONA_MISURE_SICUREZZA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/SelezionaMisureSicurezzaDatiFinali.jsp";
  public static final String PG_LOAD_SELEZIONA_PENA_ACCESSORIA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/SelezionaPenaAccessoriaDatiFinali.jsp";

  
  public static final String PG_LOAD_INSERISCI_POSIZIONE_GIURIDICA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciPosGiuridicaCumulo.jsp";
  public static final String PG_LOAD_DETTAGLIO_POSIZIONE_GIURIDICA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioPosGiuridicaCumulo.jsp";

  public static final String PG_LOAD_DETTAGLIO_PENA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioPenaCumulo.jsp";

  public static final String PG_LOAD_INSERISCI_PROVVEDIMENTO_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciProvvedimentoCumulo.jsp";
  public static final String PG_LOAD_DETTAGLIO_PROVVEDIMENTO_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioProvvedimentoCumulo.jsp";

  //
  public static final String PG_LOAD_ELENCO_ESPIAZIONE = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoEspiatoCumulo.jsp";
  public static final String PG_LOAD_ELENCO_RIDETPENA_ALTRO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoRidetPenaPMAltroCumulo.jsp";

  public static final String PG_LOAD_DETTAGLIO_ESPIAZIONE_ATTUALE = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioEspiazioneAttuale.jsp";
  
  public static final String PG_LOAD_INSERISCI_ESPIAZIONE_PREGRESSA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsEspiazionePregressa.jsp";
  public static final String PG_LOAD_DETTAGLIO_ESPIAZIONE_PREGRESSA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioEspiazionePregressa.jsp";
  
  public static final String PG_GRIGLIA_COMUNICAZIONI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/GrigliaComunicazioni.jsp";
  public static final String PG_GRIGLIA_COMUNICAZIONI_PROCURE = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/GrigliaComunicazioniProcure.jsp";
  public static final String PG_LOAD_DETTAGLIO_COMUNICAZIONI_PROCURE = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioComunicazioniProcure.jsp";
  public static final String PG_GRIGLIA_COMUNICAZIONI_CANCELLERIE = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/GrigliaComunicazioniCancellerie.jsp";
  public static final String PG_GRIGLIA_COMUNICAZIONI_ALTRE = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/GrigliaComunicazioniAltreAutorita.jsp";

  public static final String PG_ATTESA_CUMULO = IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/Attesa_Cumulo.jsp";
 
}