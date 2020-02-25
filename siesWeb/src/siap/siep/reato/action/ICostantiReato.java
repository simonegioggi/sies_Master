package siap.siep.reato.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiReato</p>
* <p>Description: Classe di costanti di Reato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiReato
{
  public static final String CAMPO_ID_REATO = "IdReato";
  public static final String CAMPO_COD_TIPO_REATO = "CodTipoReato";
  public static final String CAMPO_GIORNO_DATA_REATO = "GiornoDataReato";
  public static final String CAMPO_MESE_DATA_REATO = "MeseDataReato";
  public static final String CAMPO_ANNO_DATA_REATO = "AnnoDataReato";
  public static final String CAMPO_PROGR_NUMERO_MANUALE = "ProgrNumeroManuale";
  public static final String CAMPO_PROGR_REATO = "ProgrReato";
  public static final String CAMPO_PROGR_CIRCOSTANZA = "ProgrCircostanza";
  public static final String CAMPO_GIORNO_DATA_INIZIO = "GiornoDataInizio";
  public static final String CAMPO_MESE_DATA_INIZIO = "MeseDataInizio";
  public static final String CAMPO_ANNO_DATA_INIZIO = "AnnoDataInizio";
  public static final String CAMPO_ANNO_INIZIO = "AnnoInizio";
  public static final String CAMPO_MESE_INIZIO = "MeseInizio";
  public static final String CAMPO_GIORNO_INIZIO = "GiornoInizio";
  public static final String CAMPO_GIORNO_DATA_FINE = "GiornoDataFine";
  public static final String CAMPO_MESE_DATA_FINE = "MeseDataFine";
  public static final String CAMPO_ANNO_DATA_FINE = "AnnoDataFine";
  public static final String CAMPO_ANNO_FINE = "AnnoFine";
  public static final String CAMPO_MESE_FINE = "MeseFine";
  public static final String CAMPO_GIORNO_FINE = "GiornoFine";
  public static final String CAMPO_COD_PERIODO_CONSUMAZIONE = "CodPeriodoConsumazione";
  public static final String CAMPO_DESC_LUOGO = "DescLuogo";
  public static final String CAMPO_COD_FONTE = "CodFonte";
  public static final String CAMPO_ANNO_FONTE = "AnnoFonte";
  public static final String CAMPO_NUMERO_FONTE = "NumeroFonte";
  public static final String CAMPO_COD_SOTTONUMERAZIONE = "CodSottonumerazione";
  public static final String CAMPO_COD_NAZIONE = "CodNazione";
  public static final String CAMPO_COMMA = "Comma";
  public static final String CAMPO_LETTERA = "Lettera";
  public static final String CAMPO_NUMERO = "Numero";
  public static final String CAMPO_ARTICOLO = "Articolo";
  public static final String CAMPO_NOTE = "Note";
  public static final String CAMPO_COD_TIPO_PENA_DETENTIVA = "CodTipoPenaDetentiva";
  public static final String CAMPO_NUM_ANNI = "NumAnni";
  public static final String CAMPO_NUM_MESI = "NumMesi";
  public static final String CAMPO_NUM_GIORNI = "NumGiorni";
  public static final String CAMPO_SANZIONE_PECUNIARIA = "SanzionePecuniaria";
  public static final String CAMPO_FLAG_ERGASTOLO = "FlagErgastolo";
  public static final String CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO = "GiornoDataInizioIsolamentoDiurno";
  public static final String CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO = "MeseDataInizioIsolamentoDiurno";
  public static final String CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO = "AnnoDataInizioIsolamentoDiurno";
  public static final String CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO = "GiornoDataFineIsolamentoDiurno";
  public static final String CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO = "MeseDataFineIsolamentoDiurno";
  public static final String CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO = "AnnoDataFineIsolamentoDiurno";
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
  public static final String CAMPO_COD_TIPO_SANZIONE = "CodTipoSanzione";
  public static final String CAMPO_GIORNI_ISOLAMENTO_DIURNO = "GiorniIsolamentoDiurno";
  public static final String CAMPO_MESI_ISOLAMENTO_DIURNO = "MesiIsolamentoDiurno";
  public static final String CAMPO_ANNI_ISOLAMENTO_DIURNO = "AnniIsolamentoDiurno";
  public static final String CAMPO_ID_CONTINUAZIONE_REATO = "IdContinuazioneReato";
  public static final String CAMPO_TIPO_CONTINUAZIONE_REATO = "TipoContinuazioneReato";
  public static final String CAMPO_KEY_REATO_NSC = "KeyReatoNsc";
  //***********************************************************************************
  //Federica - a9-rr-078
  //aggiunto campo Comma-Qualificante 
  public static final String CAMPO_COMMA_QUALIFICANTE = "CommaQualificante";
  //***********************************************************************************
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_DA_COPIA = "FasSieIdFascicoloSiepdaCopia";
  public static final String CAMPO_NUM_REATO_DA_COPIA = "NumReatodaCopia";
  public static final String CAMPO_NUM_REATI_N = "IdReatiN";
  
  //***********************************************************************************
  //Mev Agosto 2014 - Aggiunti criteri di ricerca
  //
  public static final String CAMPO_CERCA_DEFINITI = "CercaDefiniti";
  public static final String CAMPO_CERCA_CUMULATI = "CercaCumulati";
  
  public static final String CAMPO_DESC_FONTE = "DescFonte";
  public static final String CAMPO_DESC_SOTTONUMERAZIONE = "DescSottonumerazione";
  public static final String CAMPO_DESC_COMMA_QUAL = "DescCommaQualificante";
  public static final String CAMPO_DESC_NAZIONE = "DescNazione";
  
  //***********************************************************************************
  
  public static final String PG_LOAD_RICERCAREATO	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/LoadRicercaReato.jsp";


  public static final String PG_BUTTONS_REATI	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/buttonsElencoProcReati.jsp";
  public static final String PG_LOAD_DETTAGLIOREATO	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/DettaglioReato.jsp";
  public static final String PG_RICERCAREATO	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/RicercaReato.jsp";
  public static final String PG_LOAD_INSERISCIREATO	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/LoadInserisciReato.jsp";
  public static final String PG_LOAD_INSERISCIULTERIORIREATI	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/LoadInserisciUlterioriReati.jsp";
  public static final String PG_LOAD_MODIFICAREATO	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/LoadModificaReato.jsp";
  public static final String PG_LOAD_INSERISCIPENAREATO	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/LoadInserisciPenaReato.jsp";
  public static final String PG_LOAD_DETTAGLIOPENAREATO	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/DettaglioPenaReato.jsp";
  public static final String PG_RICERCAULTERIORINORME	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/RicercaUlterioriNorme.jsp";
  public static final String PG_LOADRICERCAREATIFASCICOLOFRAMSET	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/LoadRicercaReatiFascicoloFramset.jsp";
  public static final String PG_LOADRICERCAREATIFASCICOLO	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/LoadRicercaReatiFascicolo.jsp";
  public static final String PG_RICERCAREATIFASCICOLO	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/RicercaReatiFascicolo.jsp";
  public static final String PG_LOADMODIFICACONTINUAZIONEREATI	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/LoadModificaContinuazioneReati.jsp";
  public static final String PG_ORGANIZZAREATI	= IWebConstants.ROOT_DIR + "files/siap/siep/reato/OrganizzaReati.jsp";
  
  public static final String PG_ATTESA_CON_ROTELLA	= IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/AttesaConRotella.jsp";
  
  public static final String SEP_NORME = "|@";
  public static final String SEP_CAMPI = "#~"; 
}