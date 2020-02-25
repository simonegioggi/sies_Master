package siap.sius.magistratorelatore.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiMagistratoRelatore</p>
* <p>Description: Classe di costanti di MagistratoRelatore</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiMagistratoRelatore
{
  public static final String CAMPO_GIORNO_DATA_INIZIO           = "GiornoDataInizio";
  public static final String CAMPO_MESE_DATA_INIZIO             = "MeseDataInizio";
  public static final String CAMPO_ANNO_DATA_INIZIO             = "AnnoDataInizio";
  public static final String CAMPO_GIORNO_DATA_FINE             = "GiornoDataFine";
  public static final String CAMPO_MESE_DATA_FINE               = "MeseDataFine";
  public static final String CAMPO_ANNO_DATA_FINE               = "AnnoDataFine";
  public static final String CAMPO_COD_RUOLO_MAGISTRATO         = "CodRuoloMagistrato";
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO    = "CodOperatoreInserimento";
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO      = "GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO        = "MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO        = "AnnoDataInserimento";
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO      = "CodUfficioInserimento";
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO  = "CodOperatoreAggiornamento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO    = "GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO      = "MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO      = "AnnoDataAggiornamento";
  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO    = "CodUfficioAggiornamento";
  public static final String CAMPO_MAG_COD_MAGISTRATO           = "MagCodMagistrato";
  public static final String CAMPO_FAS_SIU_ID_FASCICOLO_SIUS    = "FasSiuIdFascicoloSius";
  public static final String CAMPO_ESP_ID_ESPERTO               = "EspIdEsperto";
  public static final String CAMPO_RITORNO                      = "MagRelRitorno";

  public static final String PG_LOAD_RICERCAMAGISTRATORELATORE	= IWebConstants.ROOT_DIR + "files/siap/sius/magistratorelatore/LoadRicercaMagistratoRelatore.jsp";
  public static final String PG_LOAD_DETTAGLIOMAGISTRATORELATORE= IWebConstants.ROOT_DIR + "files/siap/sius/magistratorelatore/LoadDettaglioMagistratoRelatore.jsp";
  public static final String PG_RICERCAMAGISTRATORELATORE	      = IWebConstants.ROOT_DIR + "files/siap/sius/magistratorelatore/RicercaMagistratoRelatore.jsp";
  public static final String PG_LOAD_INSERISCI_MAGISTRATORELATORE= IWebConstants.ROOT_DIR + "files/siap/sius/magistratorelatore/LoadInserisciMagistratoRelatore.jsp";
  public static final String PG_SINTESIMAGISTRATORELATORE = IWebConstants.ROOT_DIR + "files/siap/sius/magistratorelatore/SintesiMagistratoRelatore.jsp";
  
  public static final String PG_LOAD_RICERCAPROCEDIMENTI_SORVEGLIANZA  = IWebConstants.ROOT_DIR + "files/siap/sius/magistratorelatore/LoadRicercaProcedimentiMagistratoRelatore.jsp";
  public static final String PG_ESITO_RICERCAPROCEDIMENTI_SORVEGLIANZA = IWebConstants.ROOT_DIR + "files/siap/sius/magistratorelatore/EsitoRicercaProcedimentiMagistratoRelatore.jsp";
  public static final String PG_ESITO_MODIFICAPROCEDIMENTI_SORVEGLIANZA = IWebConstants.ROOT_DIR + "files/siap/sius/magistratorelatore/EsitoModificaMultiplaMagistratoSorveglianzaAssegnatario.jsp";

}