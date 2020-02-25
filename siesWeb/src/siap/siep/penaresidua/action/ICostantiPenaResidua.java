package siap.siep.penaresidua.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiPenaResidua</p>
* <p>Description: Classe di costanti di PenaResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiPenaResidua
{
  public static final String CAMPO_ID_PENA_RESIDUA = "IdPenaResidua";
  public static final String CAMPO_GIORNO_DATA_INIZIO = "GiornoDataInizio";
  public static final String CAMPO_MESE_DATA_INIZIO = "MeseDataInizio";
  public static final String CAMPO_ANNO_DATA_INIZIO = "AnnoDataInizio";
  public static final String CAMPO_GIORNO_DATA_FINE = "GiornoDataFine";
  public static final String CAMPO_MESE_DATA_FINE = "MeseDataFine";
  public static final String CAMPO_ANNO_DATA_FINE = "AnnoDataFine";
  public static final String CAMPO_NUM_ANNI_RECLUSIONE = "NumAnniReclusione";
  public static final String CAMPO_NUM_MESI_RECLUSIONE = "NumMesiReclusione";
  public static final String CAMPO_NUM_GIORNI_RECLUSIONE = "NumGiorniReclusione";
  public static final String CAMPO_IMPORTO_MULTA = "ImportoMulta";
  public static final String CAMPO_NUM_ANNI_ARRESTO = "NumAnniArresto";
  public static final String CAMPO_NUM_MESI_ARRESTO = "NumMesiArresto";
  public static final String CAMPO_NUM_GIORNI_ARRESTO = "NumGiorniArresto";
  public static final String CAMPO_IMPORTO_AMMENDA = "ImportoAmmenda";
  public static final String CAMPO_DIES_A_QUO = "DiesAQuo";
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
  public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
  public static final String CAMPO_FLAG_PENA_SOSPESA = "FlagPenaSospesa";
  public static final String CAMPO_FLAG_VALIDATO = "FlagValidato";
  public static final String CAMPO_GIORNO_DATA_DECORRENZA_PENA = "GiornoDataDecorrenzaPena";
  public static final String CAMPO_MESE_DATA_DECORRENZA_PENA = "MeseDataDecorrenzaPena";
  public static final String CAMPO_ANNO_DATA_DECORRENZA_PENA = "AnnoDataDecorrenzaPena";
  public static final String CAMPO_GIORNO_DATA_FINE_RECLUSIONE = "GiornoDataFineReclusione";
  public static final String CAMPO_MESE_DATA_FINE_RECLUSIONE = "MeseDataFineReclusione";
  public static final String CAMPO_ANNO_DATA_FINE_RECLUSIONE = "AnnoDataFineReclusione";
  public static final String CAMPO_GIORNO_DATA_INIZIO_ARRESTO = "GiornoDataInizioArresto";
  public static final String CAMPO_MESE_DATA_INIZIO_ARRESTO = "MeseDataInizioArresto";
  public static final String CAMPO_ANNO_DATA_INIZIO_ARRESTO = "AnnoDataInizioArresto";
  public static final String CAMPO_FLAG_ERGASTOLO = "FlagErgastolo";
  public static final String CAMPO_FLAG_TIPO_PENA = "FlagTipoPena";
//20/05/2014 - Nuova Ordinanza L.A. - >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
  public static final String CAMPO_NUM_GIORNI_LIBER_ANT_ORDINARIA = "LibAntGiorniOrd";
  public static final String CAMPO_NUM_GIORNI_LIBER_ANT_SPECIALE = "LibAntGiorniSpe";
  public static final String CAMPO_NUM_GIORNI_LIBER_ANT_INTEGRAZIONE = "LibAntGiorniInt";
  // DL92/2014 10/2014
  public static final String CAMPO_NUM_GIORNI_RISARCIMENTO_DANNI_DL92 = "GiorniRisarcimentoDanni";
  

  // Sanzioni Sostitutive
  public static final String CAMPO_FLAG_SANZIONE_SOSTITUTIVA = "FlagSanzioneSostitutiva";
  public static final String CAMPO_COD_TIPO_SANZIONE = "CodTipoSanzione";
  public static final String CAMPO_NUM_ANNI_SS    = "NumAnniSS";
  public static final String CAMPO_NUM_MESI_SS    = "NumMesiSS";
  public static final String CAMPO_NUM_GIORNI_SS  = "NumGiorniSS";
  public static final String CAMPO_IMPORTO_MULTA_SS   = "ImportoMultaSS";
  public static final String CAMPO_IMPORTO_AMMENDA_SS = "ImportoAmmendaSS";
  public static final String CAMPO_DATA_INIZIO_SS = "DataInizioSS";
  public static final String CAMPO_DATA_FINE_SS   = "DataFineSS";
  
  
  //============================================================================
  //
  //============================================================================
  public static final String PG_LOAD_RICERCAPENARESIDUA	= IWebConstants.ROOT_DIR + "files/siap/siep/penaresidua/LoadRicercaPenaResidua.jsp";
  public static final String PG_LOAD_DETTAGLIOPENARESIDUA	= IWebConstants.ROOT_DIR + "files/siap/siep/penaresidua/LoadRicercaPenaResidua.jsp";
  public static final String PG_RICERCAPENARESIDUA	= IWebConstants.ROOT_DIR + "files/siap/siep/penaresidua/RicercaPenaResidua.jsp";
  public static final String PG_LOAD_INSERISCIPENARESIDUA	= IWebConstants.ROOT_DIR + "files/siap/siep/penaresidua/LoadInserisciPenaResidua.jsp";
  public static final String PG_LOAD_INSERISCIPENARESIDUA_MANUALE_RES	= IWebConstants.ROOT_DIR + "files/siap/siep/penacomplessiva/LoadInserisciPenaManuale.jsp";
  public static final String PG_LOAD_DETTAGLIOPENARESIDUA_MANUALE	= IWebConstants.ROOT_DIR + "files/siap/siep/penacomplessiva/LoadDettaglioPenaResiduaManuale.jsp";
}