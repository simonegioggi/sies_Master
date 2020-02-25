package siap.sige.richiestaatti.action;

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
  
  public static final String TEMPLATE_INIZIO_ESECUZIONE =  "ISIP";
  public static final String TEMPLATE_ESTRATTO_SENTENZA = "ISES";
  public static final String NUMERO_COPIE = "NumeroCopie";
  public static final String ESTREMI_SENTENZA = "EstremiSentenza";
 
  public static final String AUTORITA_DESTINATARIO = "AutoritaDestinatario";
  //public static final String NOTA_AUTORITA = "NotaAutorita";
  public static final String SEDE_AUTORITA_DESTINATARIO = "SedeAutoritaDestinatario";
  
  public static final String AUTORITA = "Autorita";
  public static final String SEDE_AUTORITA = "SedeAutorita";  
  
  // Campo Ulteriore di tipo data
  public static final String CAMPO_ANNO_ULTERIORE         = "CampoAnnoUlteriore";
  public static final String CAMPO_MESE_ULTERIORE         = "CampoMeseUlteriore";
  public static final String CAMPO_GIORNO_ULTERIORE       = "CampoGiornoUlteriore";

  // Elenco stampe.
  public static final String PG_ELENCOSTAMPEDOCISTRUTTORI = IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/ElencoStampeDocIstruttori.jsp";
  public static final String PG_ELENCOESITOPARERE = IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/ElencoEsitoParere.jsp";
  
  // Pagina di dettaglio Richiesta atti
  public static final String PG_DETTAGLIO_RICHIESTAATTI = IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioRichiestaAtti.jsp";
  public static final String PG_LOAD_RICHIESTA_SENTENZAINTEGRALE =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadRichiestaSentenzaIntegrale.jsp";
  public static final String PG_DETTAGLIO_ESTRATTO_SENTENZA_INTEGRALE =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioRichiestaSentenzaIntegrale.jsp";
  public static final String PG_LOAD_RICHIESTA_POSGIURIDICA =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadRichiestaPosizioneGiuridica.jsp";
  public static final String PG_DETTAGLIO_POSIZIONEGIURIDICA =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioPosizioneGiuridica.jsp";
  public static final String PG_DETTAGLIO_RICHIESTA_PM =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioRichiestaPM.jsp";
  public static final String PG_LOAD_RICHIESTA_FASCICOLOPENALE =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadRichiestaFascicoloPenale.jsp";
  public static final String PG_DETTAGLIO_RICHIESTA_FASCICOLOPENALE =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioRichiestaFascicoloPenale.jsp";
  public static final String PG_DETTAGLIO_ACCERTAMENTIANAGRAFICI =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioAccertamentiAnagrafici.jsp";
  public static final String PG_LOAD_INSERISCI_ANAGRAFICA_STRANIERI =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciRichiestaGenerica.jsp";
  public static final String PG_DETTAGLIO_ANAGRAFICASTRANIERI =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioAnagraficaStranieri.jsp";
  public static final String PG_LOAD_RICHIESTA_ALTRAAUTORITA =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciAltraAutorita.jsp";
  public static final String PG_DETTAGLIO_RICHIESTA_ALTRAAUTORITA =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioRicAltraAutorita.jsp";
  public static final String PG_LOAD_RICHIESTA_DELEGAINDAGINI =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciDelegaIndagini.jsp";
  
  public static final String PG_DETTAGLIO_RICHIESTAGENERICA =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioRichiestaGenerica.jsp";
  public static final String PG_LOAD_INSERISCI_RICHIESTAGENERICA =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciRichiestaGenerica.jsp";
  
  
  public static final String PG_ELENCOSTATOATTI =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/ElencoStatoAttiRichiesti.jsp";
  public static final String PG_ELENCOSOLLECITI =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/ElencoSolleciti.jsp";
  
  public static final String PG_LOAD_INSERISCI_RICHIESTA_CUI=  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciRichiestaCodiceCui.jsp";
  public static final String PG_DETTAGLIO_RICHIESTACUI=  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioRichiestaCodiceCui.jsp";
  
  public static final String PG_DETTAGLIO_RICHIESTAPARERE=  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioRichiestaParere.jsp";
  public static final String PG_LOAD_RICHIESTAPARERE=  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciRichiestaParere.jsp";
  
  public static final String PG_LOAD_INSERISCIESITOPARERE=  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciEsitoParere.jsp";
  public static final String PG_LOAD_CANCELLAESITOPARERE=  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadCancellaRichiestaEsitoParere.jsp";
  public static final String PG_DETTAGLIO_ESITOPARERE=  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioEsitoParere.jsp";
  public static final String PG_DETTAGLIO_RICHIESTA_DELEGAINDAGINI=  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioRichiestaDelegaIndagini.jsp";
  
  
  public static final String PG_DETTAGLIO_RICHIESTA_CORPOREATO=  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioRichiestaCorpoReato.jsp";
  public static final String PG_LOAD_RICHIESTA_CORPOREATO=  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciRichiestaCorpoReato.jsp";
  
  public static final String PG_LOAD_INSERISCIESITOPAREREINAMM = IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciEsitoParereInamm.jsp";
  public static final String PG_DETTAGLIO_ESITOPAREREINAMM = IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioEsitoParereInamm.jsp";

  public static final String PG_LOAD_RICHIESTA_PM =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciRichiestaPM.jsp";
  public static final String PG_LOAD_RICHIESTA_ACCERTAMENTIANAGRAFICI =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciAnagraficaCittadini.jsp";

  public static final String PG_LOAD_RICHIESTA_ROGATORIA =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciRichiestaRogatoriaMdS.jsp";
  public static final String PG_DETTAGLIO_RICHIESTA_ROGATORIA_MdS =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioRichiestaRogatoriaMdS.jsp";

  public static final String PG_DETTAGLIO_ORDINE_TRADUZIONE =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioOrdineTraduzione.jsp";

  public static final String PG_LOAD_ORDINE_TRADUZIONE =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciOrdineTraduzione.jsp";

  public static final String PG_LOAD_RICHIESTA_STATOESECUZIONE =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadInserisciRicStatoEsecuzione.jsp";
  public static final String PG_DETTAGLIO_RICSTATOESECUZIONE =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/DettaglioRicStatoEsecuzione.jsp";

  public static final String PG_LOAD_ELENCOPARERI =  IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/LoadElencoPareri.jsp";

  public static final String PG_RICERCAPARERI = IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/RicercaPareri.jsp";

  //Pagina x L'ELENCO DEGLI ATTI ISTRUTTORI
  public static final String PG_ELENCOPROVVEDIMENTI = IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/ElencoAttiIstruttori.jsp";
  public static final String PG_BUTTONS_DETTAGLIORICHIESTAATTI = IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/ButtonsDettaglioRichiestaAtti.jsp";
  public static final String PG_BUTTONS = IWebConstants.ROOT_DIR + "files/siap/sige/richiestaatti/buttonsElencoAtti.jsp";

}