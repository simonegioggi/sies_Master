package siap.siep.penacomplessiva.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiPenaComplessiva</p>
* <p>Description: Classe di costanti di PenaComplessiva</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public interface ICostantiPenaComplessiva
{
  public static final String CAMPO_ID_PENA_COMPLESSIVA = "IdPenaComplessiva";
  public static final String CAMPO_COD_TIPO_PENA_DETENTIVA = "CodTipoPenaDetentiva";
  public static final String CAMPO_NUM_ANNI_RECLUSIONE = "NumAnniReclusione";
  public static final String CAMPO_NUM_MESI_RECLUSIONE = "NumMesiReclusione";
  public static final String CAMPO_NUM_GIORNI_RECLUSIONE = "NumGiorniReclusione";
  public static final String CAMPO_IMPORTO_MULTA = "ImportoMulta";
  public static final String CAMPO_NUM_ANNI_ARRESTO = "NumAnniArresto";
  public static final String CAMPO_NUM_MESI_ARRESTO = "NumMesiArresto";
  public static final String CAMPO_NUM_GIORNI_ARRESTO = "NumGiorniArresto";
  public static final String CAMPO_IMPORTO_AMMENDA = "ImportoAmmenda";
  public static final String CAMPO_GIORNO_DATA_INIZIO = "GiornoDataInizio";
  public static final String CAMPO_MESE_DATA_INIZIO = "MeseDataInizio";
  public static final String CAMPO_ANNO_DATA_INIZIO = "AnnoDataInizio";
  public static final String CAMPO_GIORNO_DATA_FINE = "GiornoDataFine";
  public static final String CAMPO_MESE_DATA_FINE = "MeseDataFine";
  public static final String CAMPO_ANNO_DATA_FINE = "AnnoDataFine";
  public static final String CAMPO_COD_TIPO_RITO = "CodTipoRito";
  public static final String CAMPO_NUM_ANNI_CONDONATI = "NumAnniCondonati";
  public static final String CAMPO_NUM_MESI_CONDONATI = "NumMesiCondonati";
  public static final String CAMPO_NUM_GIORNI_CONDONATI = "NumGiorniCondonati";
  public static final String CAMPO_SANZIONE_PECUNIARIA_CONDONATA = "SanzionePecuniariaCondonata";
  public static final String CAMPO_FLAG_PENA_IN_CONTINUAZIONE = "FlagPenaInContinuazione";
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
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
  public static final String CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO = "GiornoDataInizioIsolamentoDiurno";
  public static final String CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO = "MeseDataInizioIsolamentoDiurno";
  public static final String CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO = "AnnoDataInizioIsolamentoDiurno";
  public static final String CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO = "GiornoDataFineIsolamentoDiurno";
  public static final String CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO = "MeseDataFineIsolamentoDiurno";
  public static final String CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO = "AnnoDataFineIsolamentoDiurno";
  public static final String CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO = "NumAnniIsolamentoDiurno";
  public static final String CAMPO_NUM_MESI_ISOLAMENTO_DIURNO = "NumMesiIsolamentoDiurno";
  public static final String CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO = "NumGiorniIsolamentoDiurno";
  public static final String CAMPO_ANNO_DATA_PRESCRIZIONE = "AnnoDataPrescrizione";
  public static final String CAMPO_MESE_DATA_PRESCRIZIONE = "MeseDataPrescrizione";
  public static final String CAMPO_GIORNO_DATA_PRESCRIZIONE = "GiornoDataPrescrizione";

  public static final String CAMPO_FLAG_SANZIONE_SOSTITUTIVA = "FlagSanzioneSostitutiva";

  public static final String CAMPO_INTERO_IMPORTO_MULTA = "interoImportoMulta";
  public static final String CAMPO_DECIMALE_IMPORTO_MULTA = "decimaleImportoMulta";
  public static final String CAMPO_INTERO_IMPORTO_AMMENDA = "interoImportoAmmenda";
  public static final String CAMPO_DECIMALE_IMPORTO_AMMENDA = "decimaleImportoAmmenda";
  public static final String CAMPO_VALUTA_IMPORTO_MULTA = "valutaImportoMulta";
  public static final String CAMPO_VALUTA_IMPORTO_AMMENDA = "valutaImportoAmmenda";

  // MEV_2023-13
  public static final String CAMPO_FLAG_PENA_SOSTITUTIVA = "FlagPenaSostitutiva";
  // MEV_2023-13 - FINE
  
  public static final String PG_LOAD_RICERCAPENACOMPLESSIVA   = IWebConstants.ROOT_DIR + "files/siap/siep/penacomplessiva/LoadRicercaPenaComplessiva.jsp";
  public static final String PG_LOAD_DETTAGLIOPENACOMPLESSIVA = IWebConstants.ROOT_DIR + "files/siap/siep/penacomplessiva/DettaglioPenaComplessiva.jsp";
  public static final String PG_RICERCAPENACOMPLESSIVA	      = IWebConstants.ROOT_DIR + "files/siap/siep/penacomplessiva/RicercaPenaComplessiva.jsp";
  public static final String PG_LOAD_INSERISCIPENACOMPLESSIVA = IWebConstants.ROOT_DIR + "files/siap/siep/penacomplessiva/LoadInserisciPenaComplessiva.jsp";

  public static final String PG_LOAD_INSERISCI_ULTERIORI_CONTINUAZIONI = IWebConstants.ROOT_DIR + "files/siap/siep/penacomplessiva/LoadInserisciUlterioriContinuazioni.jsp";
  public static final String PG_LOAD_MODIFICA_CONTINUAZIONE = IWebConstants.ROOT_DIR + "files/siap/siep/penacomplessiva/LoadModificaContinuazione.jsp";
  public static final String PG_LOAD_DETTAGLIOMODIFICAPENACOMPLESSIVA  = IWebConstants.ROOT_DIR + "files/siap/siep/penacomplessiva/DettaglioModificaPenaComplessiva.jsp";
  public static final String PG_LOAD_DETTAGLIOCANCELLAPENACOMPLESSIVA  = IWebConstants.ROOT_DIR + "files/siap/siep/penacomplessiva/DettaglioCancellaPenaComplessiva.jsp";
}