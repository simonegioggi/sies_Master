package siap.siep.modulocumulo.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiReatoCumulo</p>
* <p>Description: Classe di costanti di Reato_Cumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 4.0
*/

public interface ICostantiReatoCumulo
{
  public static final String CAMPO_ID_REATO_CUM = "IdReatoCum";
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
  public static final String CAMPO_ARTICOLO = "Articolo";
  public static final String CAMPO_COD_SOTTONUMERAZIONE = "CodSottonumerazione";
  public static final String CAMPO_COMMA = "Comma";
  public static final String CAMPO_COMMA_QUALIFICANTE = "CommaQualificante";
  public static final String CAMPO_LETTERA = "Lettera";
  public static final String CAMPO_NUMERO = "Numero";

  public static final String CAMPO_COD_TIPO_PENA_DETENTIVA = "CodTipoPenaDetentiva";
  public static final String CAMPO_NUM_ANNI = "NumAnni";
  public static final String CAMPO_NUM_MESI = "NumMesi";
  public static final String CAMPO_NUM_GIORNI = "NumGiorni";
  public static final String CAMPO_COD_TIPO_SANZIONE = "CodTipoSanzione";
  public static final String CAMPO_SANZIONE_PECUNIARIA = "SanzionePecuniaria";
  public static final String CAMPO_SANZIONE_PECUNIARIA_INT = "SanzionePecuniariaInt";
  public static final String CAMPO_SANZIONE_PECUNIARIA_DEC = "SanzionePecuniariaDec";
  public static final String CAMPO_FLAG_ERGASTOLO = "FlagErgastolo";
  public static final String CAMPO_GIORNI_ISOLAMENTO_DIURNO = "GiorniIsolamentoDiurno";
  public static final String CAMPO_MESI_ISOLAMENTO_DIURNO = "MesiIsolamentoDiurno";
  public static final String CAMPO_ANNI_ISOLAMENTO_DIURNO = "AnniIsolamentoDiurno";

  public static final String CAMPO_ID_CONTINUAZIONE_REATO_CUM = "IdContinuazioneReatoCum";
  public static final String CAMPO_TIPO_CONTINUAZIONE_REATO = "TipoContinuazioneReato";
  public static final String CAMPO_KEY_REATO_NSC = "KeyReatoNsc";
  public static final String CAMPO_NOTE = "Note";

  public static final String CAMPO_FLAG_STATO = "FlagStato";
  public static final String CAMPO_MOTIVO_MODIFICA = "MotivoModifica";
  public static final String CAMPO_TIT_ID_TITOLO_CUMULATO = "TitIdTitoloCumulato";
  public static final String CAMPO_ID_REATO_ORIGINE= "IdReatoOrigine";
 
//
  public static final String PG_RICERCAREATO_CUM	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/RicercaReatoCumulo.jsp";  
  public static final String PG_LOAD_INSERISCIREATO_CUM	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadInserisciReatoCumulo.jsp";
  public static final String PG_LOAD_DETTAGLIOREATO_CUM	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/DettaglioReatoCumulo.jsp";
  public static final String PG_LOAD_MODIFICAREATO_CUM	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadModificaReatoCumulo.jsp";
  public static final String PG_ORGANIZZAREATI_CUM	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/OrganizzaReatiCumulo.jsp";
  public static final String PG_LOADMODIFICACONTINUAZIONEREATI_CUM	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadModificaContinuazioneReatiCumulo.jsp";
  
  public static final String PG_LOAD_INSERISCI_PENAREATO_CUM	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadInserisciPenaReatoCumulo.jsp";
      
  public static final String SEP_NORME = "|@";
  public static final String SEP_CAMPI = "#~"; 
}