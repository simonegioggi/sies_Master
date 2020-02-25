package siap.siep.ordinescarcerazione.action;

/**
 * <p>Title: ICostantiEvento</p>
 * <p>Description: Classe di costanti di Evento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import f3b.web.IWebConstants;

public interface ICostantiOrdineScarcerazione
{

  public static final String CAMPO_SEDE_TRIBUNALE = "SedeTribunale";
  public static final String CAMPO_COD_TRIBUNALE = "CodTribunale";

  public static final String CAMPO_SEDE_UDS = "SedeUDS";

  public static final String PG_INSERISCI_ORDINE_SCARCERAZIONE = IWebConstants.ROOT_DIR + "files/siap/siep/ordinescarcerazione/LoadInserisciOrdineScarcerazione.jsp";
  public static final String PG_BUTTONS = IWebConstants.ROOT_DIR + "files/siap/siep/ordineesecuzione/buttonsEsecuzione.jsp";

  public static final String PG_DETTAGLIO_ORDINE_SCARCERAZIONE = IWebConstants.ROOT_DIR + "files/siap/siep/ordinescarcerazione/DettaglioOrdineScarcerazione.jsp";
  public static final String PG_INSERISCI_OS_FUNGIBILITA = IWebConstants.ROOT_DIR + "files/siap/siep/ordinescarcerazione/LoadInserisciOSFungibilita.jsp";

  public static final String PG_INSERISCI_OS_LIBERAZIONE_ANTICIPATA = IWebConstants.ROOT_DIR + "files/siap/siep/ordinescarcerazione/LoadInserisciOSLiberazioneAnticipata.jsp";
  public static final String PG_INSERISCI_OS_FUNGIBILITA_LIBERAZIONE_ANTICIPATA = IWebConstants.ROOT_DIR + "files/siap/siep/ordinescarcerazione/LoadInserisciOSFungibilitaLiberazioneAnticipata.jsp";
  public static final String PG_DETTAGLIO_OS_LIBERAZIONE_ANTICIPATA = IWebConstants.ROOT_DIR + "files/siap/siep/ordinescarcerazione/DettaglioOSLiberazioneAnticipata.jsp";

  public static final String PG_INSERISCI_OS_LIBERAZIONE_ANTICIPATA_MA = IWebConstants.ROOT_DIR + "files/siap/siep/ordinescarcerazione/LoadInserisciOSLiberazioneAnticipataMA.jsp";
  public static final String PG_INSERISCI_OS_FUNGIBILITA_LIBERAZIONE_ANTICIPATA_MA = IWebConstants.ROOT_DIR + "files/siap/siep/ordinescarcerazione/LoadInserisciOSFungibilitaLiberazioneAnticipataMA.jsp";
  public static final String PG_DETTAGLIO_OS_LIBERAZIONE_ANTICIPATA_MA = IWebConstants.ROOT_DIR + "files/siap/siep/ordinescarcerazione/DettaglioOSLiberazioneAnticipataMA.jsp";

  public static final String TEMPLATE_OS_ALTRE_POSIZIONI_VUOTO = "VUOTO";
  public static final String TEMPLATE_ORDINE_SCARCERAZIONE = "SCOS01";
  public static final String TEMPLATE_ORDINE_SCARCERAZIONE_ALTRA_CAUSA = "SCOS02";
  public static final String TEMPLATE_OS_LIBERAZIONE_ANTICIPATA = "SCLIBAN";
  public static final String TEMPLATE_OS_LIBERAZIONE_ANTICIPATA_MA = "SCLARAL";
}