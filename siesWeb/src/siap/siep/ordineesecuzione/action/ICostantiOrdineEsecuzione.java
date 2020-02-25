package siap.siep.ordineesecuzione.action;

/**
 * <p>Title: ICostantiEvento</p>
 * <p>Description: Classe di costanti di Evento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import f3b.web.IWebConstants;

public interface ICostantiOrdineEsecuzione
{

  public static final String CAMPO_COD_SEDE_TDS = "CodSedeTDS";
  public static final String CAMPO_COD_SEDE_UDS = "CodSedeUDS";
  public static final String CAMPO_COD_SEDE_UDS_NOTIFICA = "CodSedeUDSNotifica";
  public static final String CAMPO_COD_SEDE_SSPA = "CodSedeSSPA";
  public static final String CAMPO_COD_SEDE_UEPE_USSM_SS = "CodSedeUepeUssmSS";
  public static final String CAMPO_COD_SEDE_UDS_UDSM = "CodSedeUdsUdsm";
  public static final String CAMPO_COD_SEDE_TDS_TDSM = "CodSedeTdsTdsm";
  public static final String CAMPO_TIPO_UFFICIO_TDS = "TribunaleSorveglianza";
  public static final String CAMPO_TIPO_UFFICIO_UDS = "UfficioSorveglianza";
  public static final String CAMPO_NOTE_UDS = "NoteUDS";
  public static final String CAMPO_NOTE_SSPA = "NoteSSPA";
  public static final String CAMPO_NOTE_TDS = "NoteTDS";
  public static final String CAMPO_GIORNO_PERVENIMENTO_VARIAZIONE = "GiornoPervenimentoVariazione";
  public static final String CAMPO_MESE_PERVENIMENTO_VARIAZIONE = "MesePervenimentoVariazione";
  public static final String CAMPO_ANNO_PERVENIMENTO_VARIAZIONE = "AnnoPervenimentoVariazione";
  public static final String CAMPO_MOTIVAZIONI_VARIAZIONE = "MotivazioniVariazione";
  public static final String CAMPO_GIORNO_FINE_ALTRO = "GiornoFineAltro";
  public static final String CAMPO_MESE_FINE_ALTRO = "MeseFineAltro";
  public static final String CAMPO_ANNO_FINE_ALTRO = "AnnoFineAltro";
  
  public static final String TEMPLATE_ORDINE_ESECUZIONE_CONDANNATO_LIBERO = "OE1";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_CONDANNATO_DETENUTO_QC = "OE2";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_ARRESTI_DOMICILIARI = "OE5";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_ALTRA_CAUSA_D = "OE4";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_ALTRA_CAUSA_C = "OE3";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_SIMEONE_LIBERO_ESTERNE = "SOLNI";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_SIMEONE_LIBERO_POLIZIA = "SOLQN";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_SIMEONE_ALTRA_CAUSA = "SDACN";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_SIMEONE_ALTRA_CAUSA_MIS = "SDACNM";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_SIMEONE_LIBERO_ISTANZA_ESTERNE = "SOLSI";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_SIMEONE_LIBERO_ISTANZA_POLIZIA = "SOLQS";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_SIMEONE_ARRESTI_DOMICILIARI = "SDAR";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_SIMEONE_ALTRA_CAUSA_ISTANZA = "SDACS";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_SIMEONE_REVOCA_LIBERO_ISTANZA = "RSOLQS";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_SIMEONE_REVOCA_LIBERO = "RSOLQN";
  public static final String TEMPLATE_REVOCA_ORDINE_ESECUZIONE_SIMEONE_ALTRA_CAUSA_ISTANZA = "RSDACS";
  public static final String TEMPLATE_REVOCA_ORDINE_ESECUZIONE_SIMEONE_ALTRA_CAUSA = "RSDACN";
  public static final String TEMPLATE_REVOCA_SIMEONE_ALTRA_CAUSA_ISTANZA_FINE_PENA = "RSDACR";
  public static final String TEMPLATE_REVOCA_SIMEONE_ALTRA_CAUSA_FINE_PENA = "RSDACO";
  public static final String TEMPLATE_ALTRE_POSIZIONI_VUOTO = "VUOTO";
  public static final String TEMPLATE_NUOVA_PENA = "OE7";
  public static final String FOGLIO_COMPLEMENTARE = "foglio";
  public static final String ABILITA_NOTIFICA = "abilitanotifica";

  public static final String PG_LOAD_DETTAGLIO_OE_CONDANNATO_LIBERO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioOECondannatoLibero.jsp";
  public static final String PG_LOAD_DETTAGLIO_OE_CONDANNATO_LIBERO_SAN_SOS = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioOECondannatoLiberoSanSos.jsp";
  public static final String PG_LOAD_DETTAGLIO_OE_ARRESTI_DOMICILIARI = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioOEArrestiDomiciliari.jsp";
  public static final String PG_LOAD_DETTAGLIO_OE_CONDANNATO_DETENUTO_QC = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioOEDetenutoQC.jsp";

  public static final String PG_LOAD_INSERISCI_OE_CONDANNATO_DETENUTO_QC = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciOECondannatoDetenutoQC.jsp";
  public static final String PG_LOAD_INSERISCI_OE_CONDANNATO_LIBERO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciOECondannatoLibero.jsp";

  public static final String PG_STATO_ESECUZIONE = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/StatoEsecuzione.jsp";
  public static final String PG_RICERCA_STATO_ESECUZIONE = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/RicercaStatoEsecuzione.jsp";

  public static final String PG_LOAD_INSERISCI_ORDINE_ESECUZIONE = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciOrdineEsecuzione.jsp";
  public static final String PG_LOAD_INSERISCI_ORDINE_ESECUZIONE_SAN_SOS = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciOrdineEsecuzioneSanSos.jsp";
  public static final String PG_LOAD_INSERISCI_ORDINE_ESECUZIONE_SIMEONE = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciOrdineEsecuzioneSimeone.jsp";
  public static final String PG_LOAD_INSERISCI_ORDINE_ESECUZIONE_SIMEONE_SAN_SOS = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciOrdineEsecuzioneSimeoneSanSos.jsp";
  public static final String PG_LOAD_DETTAGLIO_OE_ALTRA_CAUSA = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioOEAltraCausa.jsp";
  public static final String PG_LOAD_DETTAGLIO_OE_ALTRA_CAUSA_SAN_SOS = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioOEAltraCausaSanSos.jsp";
  
  public static final String PG_DETTAGLIO_LS_LIBERO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioLSLibero.jsp";
  public static final String PG_DETTAGLIO_LS_LIBERO_SAN_SOS = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioLSLiberoSanSos.jsp";
  public static final String PG_DETTAGLIO_LS_ALTRA_CAUSA = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioLSAltraCausaSenzaIstanza.jsp";
  public static final String PG_DETTAGLIO_LS_ALTRA_CAUSA_ISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioLSAltraCausa.jsp";
  public static final String PG_DETTAGLIO_LS_ALTRA_CAUSA_SAN_SOS = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioLSAltraCausaSenzaIstanzaSanSos.jsp";

  public static final String PG_LOAD_INSERISCI_REVOCA_SOSPENSIONE_SIMEONE = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciRevocaSospensioneSimeone.jsp";
  public static final String PG_LOAD_INSERISCI_REVOCA_SOSPENSIONE_ALFANO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciRevocaSospensioneAlfano.jsp";
  public static final String PG_DETTAGLIO_REVOCA_LS_LIBERO_CON_ISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioRevocaLSLiberoIstanzaProdotta.jsp";
  public static final String PG_DETTAGLIO_REVOCA_LS_ALTRA_CAUSA = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioRevocaLSAltraCausa.jsp";
  public static final String PG_DETTAGLIO_REVOCA_LS_LIBERO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioRevocaLSLibero.jsp";

  public static final String PG_DETTAGLIO_LS_LIBERO_CON_ISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioLSLiberoIstanzaProdotta.jsp";
  public static final String PG_DETTAGLIO_LS_ARRESTI_DOMICILIARI = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioLSArrestiDomiciliari.jsp";
  public static final String PG_RICERCASCADENZARIOEVENTO = IWebConstants.ROOT_DIR + "files/siap/siep/scadenzario/LoadRicercaScadenzarioEvento.jsp";
  public static final String PG_DETTAGLIO_SCADENZARIO_EVENTO = IWebConstants.ROOT_DIR + "files/siap/siep/scadenzario/DettaglioScadenzarioEvento.jsp";
//public static final String PG_AGGIORNA_AVVENUTA_NOTIFICA = IWebConstants.ROOT_DIR + "files/siap/siep/scadenzario/DettaglioScadenzarioEventoAvvenutaNotifica.jsp";
  public static final String PG_DETTAGLIO_REVOCA_LS_ALTRE_POSIZIONI = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioRevocaAltrePosizioni.jsp";
  public static final String PG_DETTAGLIO_REVOCA_LALF_ALTRE_POSIZIONI = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioRevocaLAlfAltrePosizioni.jsp";
  public static final String PG_RICERCA_PROVVEDIMENTI = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/ElencoProvvedimenti.jsp";
  public static final String PG_RICERCA_PROVVEDIMENTI_NON_VALIDATI = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/ElencoProvvedimentiNonValidati.jsp";
  public static final String PG_DETTAGLIO_LS_ALTRE_POSIZIONI = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioLSAltrePosizioni.jsp";
  public static final String PG_DETTAGLIO_LALF_ALTRE_POSIZIONI = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioLAlfAltrePosizioni.jsp";
  public static final String PG_DETTAGLIO_OE_ALTRE_POSIZIONI = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioOEAltrePosizioni.jsp";
  public static final String PG_LOAD_TRASFERISCI_PROVVEDIMENTO_LS = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadTrasferisciProvvedimentoLS.jsp";

  public static final String PG_DETTAGLIO_MESSAGGIO_RICEVUTO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioMessaggiProvvedimentoRicevuto.jsp";
  public static final String PG_DETTAGLIO_MESSAGGIO_TRASMESSO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioProvvedimentoSpedito.jsp";

  public static final String PG_LOAD_STAMPA_CERTIFICATO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LaodCertificatoEsec.jsp";

  public static final String PG_BUTTONS = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/buttonsEsecuzione.jsp";
  public static final String PG_BUTTONS_ORDINANZE = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/buttonsEsecuzioneOrdinanze.jsp";

  public static final String PG_INSERICI_MOTIVAZIONI_EVENTO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadCancellazioneProvvedimento.jsp";

  public static final String PG_LOAD_VARIAZIONE_DECORRENZA_SCADENZA = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadVariazioneDecorrenzaScadenza.jsp";  
  public static final String PG_LOAD_INSERISCI_VARIAZIONE_DECORRENZA_SCADENZA = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciVariazioneDecorrenzaScadenza.jsp";
  public static final String PG_DETTAGLIO_VARIAZIONE_DECORRENZA_SCADENZA = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioVariazioneDecorrenzaScadenza.jsp";

  // Veriazione decorrenza scadenza questa causa
  public static final String PG_LOAD_VARIAZIONE_DECORRENZA_SCADENZA_QC           = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadVariazioneDecorrenzaScadenzaQC.jsp";  
  public static final String PG_LOAD_INSERISCI_VARIAZIONE_DECORRENZA_SCADENZA_QC = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciVariazioneDecorrenzaScadenzaQC.jsp";
  public static final String PG_DETTAGLIO_VARIAZIONE_DECORRENZA_SCADENZA_QC      = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioVariazioneDecorrenzaScadenzaQC.jsp";

  // Rideterminazione Pena Altro
  public static final String PG_LOAD_INSERISCI_OE_RIDET_PENA_ALTRO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciOERidetPenaAltro.jsp";
  public static final String PG_DETTAGLIO_OE_RIDET_PENA_ALTRO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioOERidetPenaAltro.jsp";

  // 02/12/2010 Esecuzione Pena Presso Domicilio (Decreto Alfano)
  public static final String PG_LOAD_INSERISCI_ORDINE_ESECUZIONE_ALFANO_LIBERO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciOrdineEsecuzioneAlfanoLibero.jsp";
  public static final String PG_DETTAGLIO_LALFANO_LIBERO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioLAlfanoLibero.jsp";
  public static final String PG_DETTAGLIO_LALFANO_LIBERO_CON_ISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioLAlfanoLiberoIstanzaProdotta.jsp";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_ALFANO_LIBERO = "SOLNI_ESECDOM";
  public static final String PG_LOAD_TRASFERISCI_PROVVEDIMENTO_LA = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadTrasferisciProvvedimentoLAlfano.jsp";
  public static final String TEMPLATE_TRASFERISCI_PROVVEDIMENTO = "STXISBDI_ESECDOM";
  // 07/12/2010 Decreto Sospensione Alfano Libero.
  public static final String PG_LOAD_INSERISCI_DECRETO_SOSPENSIONE_ALFANO_LIBERO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciDecretoSospensioneAlfanoLibero.jsp";
 
  // 09/12/2010 Ordine Esecuzione Decreto Alfano per Non Libero (pos. 01 e 02)
  public static final String PG_LOAD_INSERISCI_ORDINE_ESECUZIONE_ALFANO_NON_LIBERO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciOrdineEsecuzioneAlfanoNonLibero.jsp";
  public static final String PG_DETTAGLIO_LALFANO_NON_LIBERO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioLAlfanoNonLibero.jsp";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_ALFANO_NONLIBERO_01 = "SDACN_ESECDOM";
  public static final String TEMPLATE_ORDINE_ESECUZIONE_ALFANO_NONLIBERO_02 = "SDAR_ESECDOM";
  public static final String PG_DETTAGLIO_DECRETOSOS_LALFANO_LIBERO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioDecretoSospLAlfanoLibero.jsp";
  public static final String TEMPLATE_DECRETO_SOSPENSIONE_ALFANO_LIBERO = "SOLNI_ESECDOM_DS";
  public static final String PG_LOAD_CONFERMA_TRASMISSIONE_PROVVEDIMENTO = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadConfermaTrasmissioneProvvedimento.jsp";
  
  // AMBROS Decreto Legge Giugno 2013
  public static final String PG_LOAD_INSERISCI_COMUNICAZIONE_LEGGE_78_2013 = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciComunicazioneL78del2013.jsp";
  public static final String PG_DETTAGLIO_COMUNICAZIONE_LEGGE_78_2013 = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioComunicazioneL78del2013.jsp";
  public static final String TEMPLATE_COMUNICAZIONE_LEGGE_78_2013_01 = "SIEP_COMU_781";
  public static final String TEMPLATE_COMUNICAZIONE_LEGGE_78_2013_02 = "SIEP_COMU_782";
  public static final String TEMPLATE_OE_LEGGE_78_2013_01 = "SIEP_OE_783";
  public static final String PG_LOAD_TRASFERISCI_PROVVEDIMENTO_L78_2013 = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadTrasferisciProvvedimentoL78del2013.jsp";
  public static final String PG_LOAD_INSERISCI_ORDINE_ESECUZIONE_LEGGE_78_2013 = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/LoadInserisciOrdineEsecuzioneL78del2013.jsp";
  public static final String PG_DETTAGLIO_OE_LEGGE_78_2013 = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/DettaglioOrdineEsecuzioneL78del2013.jsp";
  
}