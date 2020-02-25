package siap.sius.produzioneatti.action;

import f3b.web.IWebConstants;

public interface ICostantiProduzioneAtti
{
  // Costanti per form Richiesta Parere.
  public static final String CAMPO_GIORNO_DATA_EMISSIONE  = "giornoDataEmissione";
  public static final String CAMPO_MESE_DATA_EMISSIONE    = "meseDataEmissione";
  public static final String CAMPO_ANNO_DATA_EMISSIONE    = "annoDataEmissione";
  public static final String CAMPO_COD_DESTINATARIO       = "codDestinatario";
  public static final String CAMPO_DES_DESTINATARIO       = "desDestinatario";
  public static final String CAMPO_SEDE                   = "sede";
  public static final String CAMPO_NOTE                   = "note";
  public static final String CAMPO_COD_MOTIVO             = "codMotivo";
  public static final String CAMPO_AGGIUNTIVO             = "campoAggiuntivo";
  public static final String CODTIPOEVENTO                = "08";
  public static final String CODTIPONOTIFICA              = "R";

// genny 23/03/2004
  public static final String CAMPO_GIORNO_DATA_EMISSIONE2  = "giornoDataEmissione2";
  public static final String CAMPO_MESE_DATA_EMISSIONE2    = "meseDataEmissione2";
  public static final String CAMPO_ANNO_DATA_EMISSIONE2    = "annoDataEmissione2";
  public static final String CAMPO_ESITO_PARERE            = "esitoparere";

  public static final String CAMPO_TIPO_ORDINAMENTO        = "tipoOrdinamento";


  // Elenco stampe.
  public static final String PG_LOAD_RICHIESTAPARERE = IWebConstants.ROOT_DIR + "files/siap/sius/produzioneatti/LoadInserisciRichiestaParere.jsp";

  // Pagina di dettaglio Richiesta atti
  public static final String PG_DETTAGLIO_RICHIESTAPARERE = IWebConstants.ROOT_DIR + "files/siap/sius/produzioneatti/DettaglioRichiestaParere.jsp";

// genny 22/03/2004
  public static final String PG_ELENCOESITOPAREREINAMM = IWebConstants.ROOT_DIR + "files/siap/sius/produzioneatti/ElencoEsitoParereInamm.jsp";
  public static final String PG_LOAD_RICERCAFSESITOPAREREINAMM = IWebConstants.ROOT_DIR + "files/siap/sius/produzioneatti/LoadFSEsitoParereInamm.jsp";

// genny 23/03/2004
  public static final String PG_LOAD_INSERISCIESITOPAREREINAMM = IWebConstants.ROOT_DIR + "files/siap/sius/produzioneatti/LoadInserisciEsitoParereInamm.jsp";

// genny 24/03/2004
  public static final String PG_DETTAGLIO_ESITOPAREREINAMM = IWebConstants.ROOT_DIR + "files/siap/sius/produzioneatti/DettaglioEsitoParereInamm.jsp";

  public static final String PG_LOAD_RICERCAPARERI = IWebConstants.ROOT_DIR + "files/siap/sius/produzioneatti/LoadRicercaPareri.jsp";
  public static final String PG_RICERCAPARERI = IWebConstants.ROOT_DIR + "files/siap/sius/produzioneatti/RicercaPareri.jsp";

// Codice Motivo Provvedimento in CG_REF_CODES corrispondente al Parere di innammissibilità
  public static final String PARERE_INAMMISSIBILITA  = "0750";
}
