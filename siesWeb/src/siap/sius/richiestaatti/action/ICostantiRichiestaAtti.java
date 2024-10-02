package siap.sius.richiestaatti.action;

import f3b.web.IWebConstants;

public interface ICostantiRichiestaAtti
{
  // Costanti per form Richiesta Atti.
  public static final String CAMPO_GIORNO_DATA_EMISSIONE  = "giornoDataEmissione";
  public static final String CAMPO_MESE_DATA_EMISSIONE    = "meseDataEmissione";
  public static final String CAMPO_ANNO_DATA_EMISSIONE    = "annoDataEmissione";
  public static final String CAMPO_COD_DESTINATARIO       = "codDestinatario";
  public static final String CAMPO_DES_DESTINATARIO       = "desDestinatario";
  public static final String CAMPO_SEDE                   = "sede";
  public static final String CAMPO_NOTE                   = "note";
  public static final String CAMPO_AGGIUNTIVO             = "campoAggiuntivo";
  public static final String CODTIPOEVENTO                = "05";
  public static final String CODTIPONOTIFICA              = "R";
  public static final String CAMPO_COD_AVVOCATO_DESTINATARIO       = "codAvvocatoDestinatario";
  public static final String CAMPO_SEDE_AVVOCATO	      = "codSedeAvvocatoDestinatario";
  public static final String CAMPO_INDIRIZZO_AVVOCATO     = "indirizzoAvvocatoDestinatario";
  public static final String CAMPO_NOTIFICA_VIA_FAX = "notificaViaFax";
  public static final String CAMPO_NOTIFICHE_VIA_FAX = "notificheViaFax";
  
  // Campo Ulteriore di tipo data
  public static final String CAMPO_ANNO_ULTERIORE         = "CampoAnnoUlteriore";
  public static final String CAMPO_MESE_ULTERIORE         = "CampoMeseUlteriore";
  public static final String CAMPO_GIORNO_ULTERIORE       = "CampoGiornoUlteriore";

  // INIZIO: MEV_2019-09 (D.lgs. 123/2018)
  public static final String CHECK_DATA_RESTITUZIONE         = "checkDataRestituzione";
  public static final String CAMPO_GIORNO_DATA_RESTITUZIONE  = "giornoDataRestituzione";
  public static final String CAMPO_MESE_DATA_RESTITUZIONE    = "meseDataRestituzione";
  public static final String CAMPO_ANNO_DATA_RESTITUZIONE    = "annoDataRestituzione";
  //FINE: MEV_2019-09 (D.lgs. 123/2018)
  
  
  // Elenco stampe.
  public static final String PG_ELENCOSTAMPEDOCISTRUTTORI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/ElencoStampeDocIstruttori.jsp";
  public static final String PG_LOAD_RICHIESTACARICHIPENDENTI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciCarichiPendenti.jsp";
  public static final String PG_LOAD_RICHIESTACERTIFICATOCASELLARIO = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciCertificatoCasellario.jsp";
  public static final String PG_LOAD_RICHIESTARELAZCOMPORTVARIPERIODI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRelazCompVariPeriodi.jsp";
  public static final String PG_LOAD_RICHIESTAACCERTPROGTERAPEUT = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciAccertProgTerapeut.jsp";
  public static final String PG_LOAD_RICHIESTACUMULO = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRichiestaCumulo.jsp";
  public static final String PG_LOAD_RICHIESTAINFORMAZIONI58TER = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciInformazioni58Ter.jsp";
  public static final String PG_LOAD_RICHIESTAESTRATTOSENTENZA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciEstrattoSentenza.jsp";
  public static final String PG_LOAD_RICHIESTASENTENZAINTEGRALE = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciSentenzaIntegrale.jsp";
  public static final String PG_LOAD_RICHIESTARICERCHECEDDAP = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRicercheCedDAP.jsp";
  public static final String PG_LOAD_RICHIESTARICERCHECED = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRicercheCed.jsp";
  public static final String PG_LOAD_RICHIESTACARTEGGIODAP41BIS = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciCarteggioDAP41bis.jsp";
  public static final String PG_LOAD_RICHIESTAPSCONCEDMINISTEROINTERNI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciPSconCedMinisteroInterni.jsp";
  public static final String PG_LOAD_RICHIESTAQUESTURAART4BIS = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciQuesturaArt4bis.jsp";
  public static final String PG_LOAD_RICHIESTACPSOP4BIS = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciCPSOP4bis.jsp";
  public static final String PG_LOAD_RICHIESTADAPCARTELLABIOGRAFICA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciDAPCartellaBiografica.jsp";
  public static final String PG_LOAD_RICHIESTARELAZIONECOMPORTAMENTALE = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRelazioneComportamentale.jsp";
  public static final String PG_LOAD_RICHIESTAINFORMLIBCONDIZGDF = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciInformLibCondizGDF.jsp";
  public static final String PG_LOAD_RICHIESTAPROGRAMMAPROTEZIONE = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciProgrammaProtezione.jsp";
  public static final String PG_LOAD_RICHIESTAPROGRAMMAPROTEZIONENN = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciProgrammaProtezioneNN.jsp";
  public static final String PG_LOAD_RICHIESTAINFORMSERVCENTRPROTEZIONE = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciInformServCentrProtezione.jsp";
  public static final String PG_LOAD_RICHIESTACOMMISSIONECENTRALENL = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciCommissioneCentraleNL.jsp";
  public static final String PG_LOAD_RICHIESTAPSPERART47TERE5030TER = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciPSPerArt47TerE5030Ter.jsp";
  public static final String PG_LOAD_RICHIESTAINFORMAZIONIATTIVITALAVORATIVA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciInformazioniAttivitaLavorativa.jsp";
  public static final String PG_LOAD_RICHIESTAINFORMAZIONI656C10AD = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciInformazioni656c10AD.jsp";
  public static final String PG_LOAD_RICHIESTAPSDOMICILIARIINCORSO = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciPSDomiciliariInCorso.jsp";
  public static final String PG_LOAD_RICHIESTAINFORMLIBERAZIONECONDIZ = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciInformLiberazioneCondiz.jsp";
  public static final String PG_LOAD_RICHIESTARELAZIONESINTESI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRelazioneSintesi.jsp";
  public static final String PG_LOAD_RICHIESTAVERIFICACONDOTTAPROGRSERT = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciVerificaCondottaProgrSerT.jsp";
  public static final String PG_LOAD_RICHIESTACONFERMADISPONIBILITASERT = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciConfermaDisponibilitaSERT.jsp";
  public static final String PG_LOAD_RICHIESTAIDONEITAPROGRTERAPEUTICO = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciIdoneitaProgrTerapeutico.jsp";
  public static final String PG_LOAD_RICHIESTARELAZIONECONCLUSIVACSSA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRelazioneConclusivaCSSA.jsp";
  public static final String PG_LOAD_RICHIESTARELAZIONECSSA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRelazioneCSSA.jsp";
  public static final String PG_LOAD_RICHIESTAIRREVOCABILITASENTENZA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciIrrevocabilitaSentenza.jsp";
  public static final String PG_LOAD_RICHIESTAVISITAMEDICA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciVisitaMedica.jsp";
  public static final String PG_LOAD_RICHIESTARICERCHEPOLIZIAGIUDIZIARIA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRicerchePoliziaGiudiziaria.jsp";
  public static final String PG_LOAD_RICHIESTARELAZIONESANITARIA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRelazioneSanitaria.jsp";
  public static final String PG_LOAD_RICHIESTAPROGRAMMACURAAIDS = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciProgrammaCuraAIDS.jsp";
  public static final String PG_LOAD_RICHIESTAORDINDECRALTROTDSUDS = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciOrdinDecrAltroTdSUdS.jsp";
  public static final String PG_LOAD_RICHIESTASTATODIESECUZIONE = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciStatoDiEsecuzione.jsp";
  public static final String PG_LOAD_RICHIESTAINFORMAZIONIART330CC = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciInformazioniArt330cc.jsp";
  public static final String PG_LOAD_RICHIESTAINFORMAZIONIART474C = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciInformazioniart474c.jsp";
  public static final String PG_LOAD_RICHIESTAINFORMAZIONISUSEMILIBERO = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciInformazioniSuSemilibero.jsp";
  public static final String PG_LOAD_RICHIESTARESIDENZAANAGRAFICA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciResidenzaAnagrafica.jsp";
  public static final String PG_LOAD_RICHIESTAROGATORIA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRogatoria.jsp";
  public static final String PG_LOAD_RICHIESTACONCDETENZIONEDOMICILIARE = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciConcDetenzioneDomiciliare.jsp";
  public static final String PG_LOAD_RICHIESTACERTIFCAMPIONEPENALE = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciCertifCampionePenale.jsp";
  public static final String PG_LOAD_RICHIESTARIDETERMPENA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRidetermPena.jsp";
  public static final String PG_LOAD_RICHIESTAIDONEITADOMICILIOINDULTINO = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciIdoneit‡DomicilioIndultino.jsp";
  public static final String PG_LOAD_RICHIESTAPERMESSOSOGGIORNOINDULTINO = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciPermessoSoggiornoIndultino.jsp";
  public static final String PG_LOAD_RICHIESTASORVPARTICOLAREINDULTINO = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciSorvParticolareIndultino.jsp";
  public static final String PG_LOAD_RICHIESTAINFORMAZIONIQUESTURAESPULSIONE = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciInformazioniQuesturaEspulsione.jsp";
  public static final String PG_LOAD_RICHIESTAINFORMATIVAPERMESSOART30OP = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciInformativaArt30OP.jsp";  // STUB 12/07/2005
  public static final String PG_LOAD_RICHIESTAINFORMDIFFESPENAART146C2 = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciInformDiffEsPenaArt146c2.jsp";  // STUB 09/09/2005
  public static final String PG_LOAD_RICHIESTAINFORMDETESTERO = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciInformDetEstero.jsp";  // STUB 17/11/2005
  public static final String PG_LOAD_RICHIESTAPAREREDDADNA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciParereDDADNA.jsp";  // STUB 24/11/2005
  public static final String PG_LOAD_RICHIESTAALTREISTRUTTORIE = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRichiestaAltreIstruttorie.jsp";
  public static final String PG_LOAD_RICHIESTAINFORIABILITAZIONE = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRichiestaInfoRiabilitazione.jsp"; 
  public static final String PG_LOAD_RICHIESTAVISITAFISCALEFAMILIARE = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRichiestaVisitaFiscaleFamiliare.jsp";  
  
  public static final String PG_ELENCOSTAMPEMODELLIRICHIESTAATTI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/ElencoStampeModelliRichiestaAtti.jsp";

  // Pagina di dettaglio Richiesta atti
  public static final String PG_DETTAGLIO_RICHIESTAATTI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/DettaglioRichiestaAtti.jsp";

  
  
  // Sanzioni Sostitutive, Doc Istruttori
  public static final String PG_LOAD_RICHIESTASANZSOSTDOCUMENTIISTRUTTORI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRichiestaSanzSostDocumentiIstruttori.jsp";
  public static final String PG_LOAD_RICHIESTASANZSOSTRELAZIONEFINALE = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRichiestaSanzSostRelazioneFinale.jsp";
  public static final String PG_LOAD_RICHIESTAINFORMATIVA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciRichiestaInformativa.jsp";
  public static final String PG_LOAD_RICHIESTALAVOROSOSTITUTIVO = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciLavoroSostitutivo.jsp";
  
  // Remissione Debito
  public static final String PG_LOAD_RICHIESTASOSPATTIREMDEBUFFRECUPEROCREDITI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciSospAttiRemDebUffRecuperoCrediti.jsp";
  public static final String PG_LOAD_RICHIESTASOSPATTIREMDEBAGENZIAENTRATE = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciSospAttiRemDebAgenziaEntrate.jsp";
  public static final String PG_LOAD_RICHIESTASOSPATTIREMDEBISTITUTOPENITENZIARIO = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadInserisciSospAttiRemDebIstPenitenziario.jsp";
  
  // Lista Atti Istruttori Richiesti
  public static final String PG_ELENCOSOLLECITI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/ElencoSolleciti.jsp";
  public static final String PG_ELENCOATTIRICHIESTI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/ElencoAttiRichiesti.jsp";
  public static final String PG_LISTAATTIRICHIESTI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/ListaAttiRichiesti.jsp";
  public static final String PG_BUTTONS = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/buttonsElencoAtti.jsp";
  public static final String PG_BUTTONS_DETTAGLIORICHIESTAATTI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/ButtonsDettaglioRichiestaAtti.jsp";
  public static final String PG_LOAD_TRASFERISCI_RICHIESTA = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/LoadTrasferisciRichiesta.jsp";

  public static final String MSG_BUTTON_HISTORY = "Ritorna ad elenco documenti istruttori";

  public static final String TEMPLATE_MOD_RICHIESTAATTI_TIPO1 = "SIUS_IM_001";
  public static final String TEMPLATE_MOD_RICHIESTAATTI_TIPO2 = "SIUS_IM_002";
  public static final String TEMPLATE_MOD_RICHIESTAATTI_TIPO3 = "SIUS_IM_003";
  public static final String TEMPLATE_MOD_RICHIESTAATTI_TIPO4 = "SIUS_IM_004";
  public static final String TEMPLATE_MOD_RICHIESTAATTI_TIPO5 = "SIUS_IM_005";
}