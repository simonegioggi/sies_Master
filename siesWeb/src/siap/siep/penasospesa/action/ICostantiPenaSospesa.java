package siap.siep.penasospesa.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiPenaAccessoria</p>
* <p>Description: Classe di costanti di PenaAccessoria</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiPenaSospesa
{
  public static final String CAMPO_COD_TIPO_PROVVEDIMENTO = "TipoProvvedimento";
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";

  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
  public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
  public static final String CAMPO_GIORNO_DATA_EMISSIONE = "GiornoDataEmissione";
  public static final String CAMPO_MESE_DATA_EMISSIONE = "MeseDataEmissione";
  public static final String CAMPO_ANNO_DATA_EMISSIONE = "AnnoDataEmissione";
  public static final String CAMPO_COD_DESTINATARIO = "CodDestinatario";

  //Sentenza
  public static final String CAMPO_GIORNO_DATA_ARRIVO_ATTO= "GiornoDataArrivoAtto";
  public static final String CAMPO_MESE_DATA_ARRIVO_ATTO = "MeseDataArrivoAtto";
  public static final String CAMPO_ANNO_DATA_ARRIVO_ATTO = "AnnoDataArrivoAtto";

  public static final String CAMPO_GIORNO_DATA_SENTENZA_REVOCA= "GiornoDataSentenzaRevoca";
  public static final String CAMPO_MESE_DATA_SENTENZA_REVOCA = "MeseDataSentenzaRevoca";
  public static final String CAMPO_ANNO_DATA_SENTENZA_REVOCA = "AnnoDataSentenzaRevoca";

  public static final String CAMPO_NUMERO_RGNR_REVOCA = "NumeroRGNRRevoca";
  public static final String CAMPO_ANNO_RGNR_REVOCA = "AnnoRGNRRevoca";  
  public static final String CAMPO_NUMERO_RegGen_REVOCA = "NumeroRegGenRevoca";
  public static final String CAMPO_ANNO_RegGen_REVOCA = "AnnoRegGenRevoca";
  public static final String CAMPO_ANNO_SENTENZA_REVOCA = "AnnoSentenzaRevoca";
  public static final String CAMPO_NUMERO_SENTENZA_REVOCA = "NumeroSentenzaRevoca";
  
  public static final String   CAMPO_FLAG_SOSP_COND = "FlagSospCondizionale";
  public static final String   CAMPO_FLAG_NON_MENZIONE = "FlagNonMenzione";

  public static final String CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO = "CodTipoUfficioSentenzaRevo";
  public static final String CAMPO_COD_LUOGO_SENTENZA_REVOCA = "CodLuogoSentenzaRevoca";
  public static final String CAMPO_SEZIONE_SENTENZA_REVOCA = "CodSezioneSentenzaRevoca";

  //Ordinanza
  public static final String CAMPO_GIORNO_DATA_EMIS_PROV = "GiornoDataEmissioneProvv";
  public static final String CAMPO_MESE_DATA_EMIS_PROV = "MeseDataEmissioneProvv";
  public static final String CAMPO_ANNO_DATA_EMIS_PROV = "AnnoDataEmissioneProvv";
  public static final String CAMPO_COD_ARTICOLO = "ArticoloRevoca";
  public static final String CAMPO_COD_MOTIVO = "MotivoRevoca";
  
  public static final String CAMPO_GIORNO_DATA_ORDINANZA_REVOCA= "GiornoDataOrdinanzaRevoca";
  public static final String CAMPO_MESE_DATA_ORDINANZA_REVOCA = "MeseDataOrdinanzaRevoca";
  public static final String CAMPO_ANNO_DATA_ORDINANZA_REVOCA = "AnnoDataOrdinanzaRevoca";

  public static final String CAMPO_ANNO_ORDINANZA_REVOCA = "AnnoOrdinanzaRevoca";
  public static final String CAMPO_NUMERO_ORDINANZA_REVOCA = "NumeroOrdinanzaRevoca"; 
  public static final String CAMPO_COD_TIPO_UFFICIO_ORDINANZA_REVO = "CodTipoUfficioOrdinanzaRevo";
  public static final String CAMPO_COD_LUOGO_ORDINANZA_REVOCA = "CodLuogoOrdinanzaRevoca";
  public static final String CAMPO_SEZIONE_ORDINANZA_REVOCA = "CodSezioneOrdinanzaRevoca";
  public static final String CAMPO_NOTE = "Note";

  public static final String CAMPO_COD_UFFICIO_GE="CodUfficioGE";
  public static final String CAMPO_COD_TIPO_UFFICIO_GE="CodTipoUfficioGE";
  public static final String CAMPO_SEDE_UFFICIO_GE="SedeUfficioGE";
  public static final String CAMPO_SEZIONE_UFFICIO_GE="SezioneUfficioGE";
    
  public static final String PG_LOAD_INSERISCIANNOTAZIONEREVOCA= IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/LoadInserisciAnnotazioneRevoca.jsp";
  public static final String PG_DETTAGLIO_ANNOTAZIONEREVOCA= IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/DettaglioAnnotazioneRevoca.jsp";

  public static final String PG_LOAD_INSERISCIRICHIESTAESTINZIONEREATO= IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/LoadInserisciRicEstinzioneReato.jsp";
  public static final String PG_DETTAGLIO_RICHIESTAESTINZIONEREATO = IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/DettaglioRicEstinzioneReato.jsp";

  public static final String PG_LOAD_INSERISCIRICHIESTADETERMINAZIONETERMINI = IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/LoadInserisciRicDeterminazioneTermini.jsp";
  public static final String PG_DETTAGLIO_RICHIESTADETERMINAZIONETERMINI = IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/DettaglioRicDeterminazioneTermini.jsp";

  public static final String PG_LOAD_INSERISCIANNOTAZIONETERMINI = IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/LoadInserisciAnnotazioneTermini.jsp";
  public static final String PG_DETTAGLIO_ANNOTAZIONETERMINI = IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/DettaglioAnnotazioneTermini.jsp";
 
  public static final String PG_LOAD_INSERISCI_ANN_ESTINZIONE_REATO = IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/LoadInserisciAnnEstinzioneReato.jsp";
  public static final String PG_DETTAGLIO_ANN_ESTINZIONE_REATO = IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/DettaglioAnnEstinzioneReato.jsp";

  
}
