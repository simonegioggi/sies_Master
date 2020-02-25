package siap.siep.istruttoria.action;

import f3b.web.IWebConstants;

public interface ICostantiIstruttoria
{
   public static final String TEMPLATE_INIZIO_ESECUZIONE =  "ISIP";
   public static final String TEMPLATE_ESECUZIONE_SENTENZA = "ISCES";
   public static final String TEMPLATE_ESTRATTO_SENTENZA = "ISES";
   public static final String TEMPLATE_PAGAMENTO_PENA_PECUNIARIA = "ISPP";
   public static final String NUMERO_COPIE = "NumeroCopie";
   public static final String ESTREMI_SENTENZA = "EstremiSentenza";
   public static final String ESTREMI_SOGGETTO = "EstremiSoggetto";
   
   public static final String CAMPO_DATA_EMISSIONE_PROVV = "DataEmissioneProvvedimento";
   public static final String CAMPO_DATA_IRREVOCABILITA = "DataIrrevocabilita";
   
   public static final String CAMPO_DESCR_PROVVEDIMENTO = "DescrProvvedimento";
 
   public static final String AUTORITA_DESTINATARIO = "AutoritaDestinatario";
   //public static final String NOTA_AUTORITA = "NotaAutorita";
   public static final String SEDE_AUTORITA_DESTINATARIO = "SedeAutoritaDestinatario";
   
   public static final String AUTORITA = "Autorita";
   public static final String SEDE_AUTORITA = "SedeAutorita";  
   
   public static final String DATA_RICHIESTA_GIORNO = "DataRichiestaGiorno";
   public static final String DATA_RICHIESTA_MESE = "DataRichiestaMese";
   public static final String DATA_RICHIESTA_ANNO = "DataRichiestaAnno";
   public static final String TIPO_DOCUMENTO = "TipoDocumento";
   public static final String CAMPO_NOTE = "CampoNote";
   public static final String DATA_INIZIO_GIORNO = "DataInizioGiorno";
   public static final String DATA_INIZIO_MESE = "DataInizioMese";
   public static final String DATA_INIZIO_ANNO = "DataInizioAnno";
   public static final String DATA_FINE_GIORNO = "DataFineGiorno";
   public static final String DATA_FINE_MESE = "DataFineMese";
   public static final String DATA_FINE_ANNO = "DataFineAnno";
   public static final String TEMPLATE_CERTIFICATO_ESECUZIONE= "ISCE";
   public static final String TEMPLATE_CERTIFICATO_DAP= "ISDAP";
   public static final String TEMPLATE_ANAGRAFICA_CITTADINI_STRANIERI= "ISCS";
   public static final String TEMPLATE_POSIZIONE_GIURIDICA= "ISPG";
   public static final String TEMPLATE_INFO_ARRESTO_DENUNCIA= "ISIA";
   public static final String TEMPLATE_ANAGRAFICA_CITTADINI_ITALIANI= "ISCI";
   public static final String TEMPLATE_ARRESTI_DOMICILIARI_PRECEDENTI= "ISAD";
   public static final String TEMPLATE_ARRESTI_DOMICILIARI_ATTUALI= "ISAD";
   public static final String TEMPLATE_CERTIFICATO_PENALE= "ISCC";   
   public static final String INDIRIZZO_DESTINATARIO= "IndirizzoDestinatario";
   
   public static final String AUT_QUESTURA_DIVISIONE_ANTICRIMINE= "AutQuesturaDivisioneAnticrimine";
   public static final String FLAG_QUESTURA= "flagQuestura";
   public static final String FLAG_GABINETTO= "flagGabinetto";
   
   public static final String DATA_ESPULSIONE_GIORNO = "DataEspulsioneGiorno";
   public static final String DATA_ESPULSIONE_MESE = "DataEspulsioneMese";
   public static final String DATA_ESPULSIONE_ANNO = "DataEspulsioneAnno";   
   
   //
   public static final String CAMPO_PARENT_FORM_NAME = "ParentFormName";
   public static final String CAMPO_PARENT_FORM_TYPE = "ParentFormType";
   public static final String FORM_TYPE_SENTENZA_INTEGRALE =  "InserisciSentenzaIntegrale";
   public static final String FORM_TYPE_CERTIFICATO_ESECUZIONE =  "InserisciCertificatoEsecuzione";
   public static final String FORM_TYPE_PAGAMENTO_PENA_PEC =  "NotiziaPagamentoPenaPec";
   
   public static final String PG_BUTTONS_RICERCA = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/buttonsIstruttoria.jsp";
   public static final String PG_BUTTONS_CANC_RICH_CUMULO = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/buttonsCancRichiesteCumulo.jsp";
   
   public static final String PG_LOAD_INSERISCI_ESTRATTO_SENTENZE =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciEstrattoSentenze.jsp";
   public static final String PG_LOAD_RICERCAEVENTO	= IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/RicercaIstruttoria.jsp";
   public static final String PG_DETTAGLIO_ESTRATTO_SENTENZA =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioEstrattoSentenze.jsp";
   public static final String PG_DETTAGLIO_INIZIO_ESECUZIONE =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioInizioEsecuzione.jsp";
   public static final String PG_DETTAGLIO_ESECUTIVITA_SENTENZA =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioEsecutivitaSentenza.jsp";
   public static final String PG_LOAD_INSERISCI_CERTIFICATO_ESECUZIONE =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciCertificatoEsecuzione.jsp";
   public static final String PG_DETTAGLIO_CERTIFICATO_ESECUZIONE =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioCertificatoEsecuzione.jsp";
   public static final String PG_LOAD_INSERISCI_CERTIFICATO_DAP =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciCertificatoDap.jsp";
   public static final String PG_DETTAGLIO_CERTIFICATO_DAP =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioCertificatoDap.jsp";
   public static final String PG_LOAD_INSERISCI_POSIZIONE_GIURIDICA =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciPosizioneGiuridica.jsp";
   public static final String PG_DETTAGLIO_POSIZIONE_GIURIDICA =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioPosizioneGiuridica.jsp";
   public static final String PG_LOAD_INSERISCI_ANAGRAFICA_CITTADINI_STRANIERI=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciAnagraficaCittadiniStranieri.jsp";
   public static final String PG_DETTAGLIO_ANAGRAFICA_CITTADINI_STRANIERI=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioAnagraficaCittadiniStranieri.jsp";
   public static final String PG_DETTAGLIO_ANAGRAFICA_CITTADINI_ITALIANI=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioAnagraficaCittadiniItaliani.jsp";
   public static final String PG_DETTAGLIO_INFO_ARRESTO_DENUNCIA=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioInfoArrestoDenuncia.jsp";
   public static final String PG_LOAD_INSERISCI_INFO_ARRESTO_DENUNCIA=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciInfoArrestoDenuncia.jsp";
   public static final String PG_LOAD_INSERISCI_ANAGRAFICA_CITTADINI_ITALIANI=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciAnagraficaCittadiniItaliani.jsp";
   public static final String PG_LOAD_INSERISCI_ARRESTI_DOMICILIARI_PRECEDENTI=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciArrestiDomiciliariPrecedenti.jsp";
   public static final String PG_DETTAGLIO_ARRESTI_DOMICILIARI_ATTUALI=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioArrestiDomiciliariAttuali.jsp";
   public static final String PG_LOAD_INSERISCI_ARRESTI_DOMICILIARI_ATTUALI=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciArrestiDomiciliariAttuali.jsp";
   public static final String PG_DETTAGLIO_ARRESTI_DOMICILIARI_PRECEDENTI=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioArrestiDomiciliariPrecedenti.jsp";
   public static final String PG_LOAD_INSERISCI_CERTIFICATO_PENALE=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciCertificatoPenale.jsp";
   public static final String PG_DETTAGLIO_CERTIFICATO_PENALE=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioCertificatoPenale.jsp";
   public static final String PG_LOAD_RICHIESTA_CERTIFICATO_PENALE=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadRichiestaCertificatoPenale.jsp";
   
   public static final String PG_LOAD_INSERISCI_ANAGRAFICA_CITTADINI=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciAnagraficaCittadini.jsp";
   public static final String PG_LOAD_INSERISCI_INIZIO_ESECUZIONE=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciInizioEsecuzione.jsp";
   public static final String PG_DETTAGLIO_ANAGRAFICA_CITTADINI=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioAnagraficaCittadini.jsp";
    
   public static final String PG_LOAD_INSERISCI_NOT_ESP_SAN_SOST=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciNotEspSanSost.jsp";
   public static final String PG_DETTAGLIO_NOT_ESP_SAN_SOST=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioNotEspSanSost.jsp"; 
   
   public static final String PG_LOAD_RICERCA_COPERTINE=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadRicercaCopertine.jsp";
   
   public static final String PG_LOAD_INSERISCI_RICHIESTA_CODICE_CUI=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciRichiestaCodiceCui.jsp";
   public static final String PG_DETTAGLIO_RICHIESTA_CODICE_CUI=  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioRichiestaCodiceCui.jsp";
  
   public static final String PG_LOAD_COPERTINA_FASCICOLO =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadCopertinaFasc.jsp";
   public static final String PG_GRIGLIA_ISTRUTTORIE =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadGrigliaIstruttoria.jsp";
   
   public static final String PG_LOAD_INSERISCI_RICHIESTA_PAGAMENTO_PENA_PEC =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadInserisciIstruttoriaRichiestaPagamentoPP.jsp";
   public static final String PG_DETTAGLIO_RICHIESTA_PAGAMENTO_PENA_PEC =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/DettaglioIstruttoriaRichiestaPagamentoPP.jsp";
   
   public static final String PG_LOAD_POPUP_LISTA_TITOLI_ESTRATTO_SENTENZA =  IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadPopupElencoTitoli.jsp";
  
   
}