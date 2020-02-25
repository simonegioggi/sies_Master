package siap.sius.rifasiep.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiRifFasicoloSiep</p>
* <p>Description: Classe di costanti di RifFasicoloSiep</p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiRifFascicoloSiep
{
  public static final String CAMPO_ID_RIFERIMENTO_FASCICOLO_SIEP = "IdRiferimentoFascicoloSiep";
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
  public static final String CAMPO_ANNO_FASCICOLO_SIEP = "AnnoFascicoloSiep";
  public static final String CAMPO_PROGR_FASCICOLO_SIEP = "ProgrFascicoloSiep";
  public static final String CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN = "ProgrFascicoloSiepOrigin";
  public static final String CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP = "CodTipoUffFascicoloSiep";
  public static final String CAMPO_SEDE_UFF_FASCICOLO_SIEP = "SedeUffFascicoloSiep";
  public static final String CAMPO_SEDE_UFF_ACCORPATO = "SedeUffAccorpato";
  public static final String CAMPO_COD_TIPO_PROVVEDIMENTO = "CodTipoProvvedimento";
  public static final String CAMPO_GIORNO_DATA_PROVVEDIMENTO = "GiornoDataProvvedimento";
  public static final String CAMPO_MESE_DATA_PROVVEDIMENTO = "MeseDataProvvedimento";
  public static final String CAMPO_ANNO_DATA_PROVVEDIMENTO = "AnnoDataProvvedimento";
  public static final String CAMPO_ANNO_PROVVEDIMENTO = "AnnoProvvedimento";
  public static final String CAMPO_NUMERO_PROVVEDIMENTO = "NumeroProvvedimento";
  public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE = "CodTipoAutoritaEmittente";
  public static final String CAMPO_COD_LUOGO_EMITTENTE = "CodLuogoEmittente";
  public static final String CAMPO_DESCR_LUOGO_EMITTENTE = "DescrLuogoEmittente";
  public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA = "GiornoDataIrrevocabilita";
  public static final String CAMPO_MESE_DATA_IRREVOCABILITA = "MeseDataIrrevocabilita";
  public static final String CAMPO_ANNO_DATA_IRREVOCABILITA = "AnnoDataIrrevocabilita";
  public static final String CAMPO_FAS_SIU_ID_FASCICOLO_SIUS = "FasSiuIdFascicoloSius";
  public static final String CAMPO_NOTE = "Note";
  public static final String ACTION_DOPO_CANCELLAZIONE = "ActDopoCanc";
  
  // Aggiunta campi nella form di "Inserimento Riferimenti Altri Titoli Esecutivi"
  public static final String CAMPO_ANNO_MISURA_SICUREZZA = "AnnoMisuraSicurezza";
  public static final String CAMPO_NUMERO_MISURA_SICUREZZA = "NumeroMisuraSicurezza";
  public static final String CAMPO_COD_TIPO_UFF_MISURA_SICUREZZA = "CodTipoUffMisuraSicurezza";
  public static final String CAMPO_SEDE_UFF_MISURA_SICUREZZA = "SedeUffMisuraSicurezza";
  public static final String CAMPO_COD_TIPO_PROVVEDIMENTO_MSIC = "CodTipoProvvedimentoMSic";
  public static final String CAMPO_GIORNO_DATA_PROVVEDIMENTO_MSIC = "GiornoDataProvvedimentoMSic";
  public static final String CAMPO_MESE_DATA_PROVVEDIMENTO_MSIC = "MeseDataProvvedimentoMSic";
  public static final String CAMPO_ANNO_DATA_PROVVEDIMENTO_MSIC = "AnnoDataProvvedimentoMSic";
  public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA_MSIC = "GiornoDataIrrevocabilitaMSic";
  public static final String CAMPO_MESE_DATA_IRREVOCABILITA_MSIC = "MeseDataIrrevocabilitaMSic";
  public static final String CAMPO_ANNO_DATA_IRREVOCABILITA_MSIC = "AnnoDataIrrevocabilitaMSic";
  public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE_MSIC = "CodTipoAutoritaEmittenteMSic";
  public static final String CAMPO_DESCR_LUOGO_EMITTENTE_MSIC = "DescrLuogoEmittenteMSic";
  public static final String CAMPO_COD_LUOGO_EMITTENTE_MSIC = "CodLuogoEmittenteMSic";
  public static final String CAMPO_COD_TIPO_INSERIMENTO = "CodTipoInserimento";
  public static final String CAMPO_COD_TIPO_NUMERAZIONE = "CodTipoNumerazione";
  
  public static final String PG_LOAD_INSERISCIRIFASIEP	= IWebConstants.ROOT_DIR + "files/siap/sius/rifasiep/LoadInserisciRifFascicoloSiep.jsp";
  public static final String PG_LOAD_DETTAGLIORIFASIEP	= IWebConstants.ROOT_DIR + "files/siap/sius/rifasiep/DettaglioRifFascicoloSiep.jsp";
  public static final String PG_ELENCO_RIFERIMENTI	= IWebConstants.ROOT_DIR + "files/siap/sius/rifasiep/ElencoRifFascicoloSiep.jsp";
}