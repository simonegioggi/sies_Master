package siap.siep.ulterioresanzionecumulo.action;

import f3b.web.IWebConstants;

/**
 * <p>Title: ICostantiUlterioreSanzioneCumulo</p>
 * <p>Description: Classe di costanti di UlterioreSanzioneCumulo</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public interface ICostantiUlterioreSanzioneCumulo
{
  public static final String CAMPO_ID_ULTERIORE_SANZIONE_CUMULO = "IdUlterioreSanzioneCumulo";
  public static final String CAMPO_COD_TIPO_ULTERIORE_SANZIONE = "CodTipoUlterioreSanzione";
  public static final String CAMPO_NUM_ANNI = "NumAnni";
  public static final String CAMPO_NUM_MESI = "NumMesi";
  public static final String CAMPO_NUM_GIORNI = "NumGiorni";
  public static final String CAMPO_SANZIONE = "Sanzione";
  public static final String CAMPO_MULTA = "Multa";
  public static final String CAMPO_INTERO_IMPORTO_SANZIONE = "ImportoSanzioneIntero";
  public static final String CAMPO_DECIMALE_IMPORTO_SANZIONE = "ImportoSanzioneDecimale";
  public static final String CAMPO_INTERO_IMPORTO_MULTA = "ImportoMultaIntero";
  public static final String CAMPO_DECIMALE_IMPORTO_MULTA = "ImportoMultaDecimale";


  public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
  public static final String CAMPO_CUM_ID_CUMULO = "CumIdCumulo";
  public static final String CAMPO_NUM_ANNI_SEMIDETENZIONE = "NumAnniSemidetenzione";
  public static final String CAMPO_NUM_MESI_SEMIDETENZIONE = "NumMesiSemidetenzione";
  public static final String CAMPO_NUM_GIORNI_SEMIDETENZIONE = "NumGiorniSemidetenzione";
  public static final String CAMPO_NUM_ANNI_LIBERTA = "NumAnniLiberta";
  public static final String CAMPO_NUM_MESI_LIBERTA = "NumMesiLiberta";
  public static final String CAMPO_NUM_GIORNI_LIBERTA = "NumGiorniLiberta";
  public static final String CAMPO_NUM_ANNI_ESPULSIONE = "NumAnniEpulsione";
  public static final String CAMPO_NUM_MESI_ESPULSIONE = "NumMesiEpulsione";
  public static final String CAMPO_NUM_GIORNI_ESPULSIONE = "NumGiorniEpulsione";
  public static final String CAMPO_NUM_ANNI_PECUNIARIA = "NumAnniPecuniaria";
  public static final String CAMPO_NUM_MESI_PECUNIARIA = "NumMesiPecuniaria";
  public static final String CAMPO_NUM_GIORNI_PECUNIARIA = "NumGiorniPecuniaria";
  public static final String CAMPO_NUM_ANNI_CONVERSIONE = "NumAnniConversione";
  public static final String CAMPO_NUM_MESI_CONVERSIONE = "NumMesiConversione";
  public static final String CAMPO_NUM_GIORNI_CONVERSIONE = "NumGiorniConversione";
  public static final String CAMPO_NUM_ANNI_MILITARE = "NumAnniMilitare";
  public static final String CAMPO_NUM_MESI_MILITARE = "NumMesiMilitare";
  public static final String CAMPO_NUM_GIORNI_MILITARE = "NumGiorniMilitare";
  public static final String CAMPO_NUM_ANNI_SANZIONI = "NumAnniSanzioni";
  public static final String CAMPO_NUM_MESI_SANZIONI = "NumMesiSanzioni";
  public static final String CAMPO_NUM_GIORNI_SANZIONI = "NumGiorniSanzioni";
  public static final String CAMPO_NUM_ANNI_LAV_SOST = "NumAnniLavSost";
  public static final String CAMPO_NUM_MESI_LAV_SOST = "NumMesiLavSost";
  public static final String CAMPO_NUM_GIORNI_LAV_SOST = "NumGiorniLavSost";
  public static final String CAMPO_NUM_ANNI_LAV_SOST_GP = "NumAnniLavSostGp";
 public static final String CAMPO_NUM_MESI_LAV_SOST_GP = "NumMesiLavSostGp";
 public static final String CAMPO_NUM_GIORNI_LAV_SOST_GP = "NumGiorniLavSostGp";

  public static final String CAMPO_NUM_ANNI_LAV_PUB = "NumAnniLavPub";
  public static final String CAMPO_NUM_MESI_LAV_PUB = "NumMesiLavPub";
  public static final String CAMPO_NUM_GIORNI_LAV_PUB = "NumGiorniLavPub";

  public static final String PG_LOAD_RICERCAULTERIORESANZIONECUMULO = IWebConstants.ROOT_DIR + "files/siap/siep/ulterioresanzionecumulo/LoadRicercaUlterioreSanzioneCumulo.jsp";
  public static final String PG_LOAD_DETTAGLIOULTERIORESANZIONECUMULO = IWebConstants.ROOT_DIR + "files/siap/siep/ulterioresanzionecumulo/LoadRicercaUlterioreSanzioneCumulo.jsp";
  public static final String PG_RICERCAULTERIORESANZIONECUMULO = IWebConstants.ROOT_DIR + "files/siap/siep/ulterioresanzionecumulo/RicercaUlterioreSanzioneCumulo.jsp";
  public static final String PG_LOAD_INSERISCIULTERIORESANZIONECUMULO = IWebConstants.ROOT_DIR + "files/siap/siep/ulterioresanzionecumulo/LoadInserisciUlterioreSanzioneCumulo.jsp";
}