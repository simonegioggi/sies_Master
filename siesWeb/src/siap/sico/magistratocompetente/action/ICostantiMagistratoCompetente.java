package siap.sico.magistratocompetente.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiMagistratoCompetente</p>
* <p>Description: Classe di costanti di MagistratoCompetente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiMagistratoCompetente
{
  public static final String CAMPO_GIORNO_DATA_INIZIO = "GiornoDataInizio";
  public static final String CAMPO_MESE_DATA_INIZIO = "MeseDataInizio";
  public static final String CAMPO_ANNO_DATA_INIZIO = "AnnoDataInizio";
  public static final String CAMPO_GIORNO_DATA_FINE = "GiornoDataFine";
  public static final String CAMPO_MESE_DATA_FINE = "MeseDataFine";
  public static final String CAMPO_ANNO_DATA_FINE = "AnnoDataFine";
  public static final String CAMPO_COD_RUOLO_MAGISTRATO = "CodRuoloMagistrato";
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
  public static final String CAMPO_MAG_COD_MAGISTRATO = "MagCodMagistrato";
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";

  public static final String PG_LOAD_RICERCAMAGISTRATOCOMPETENTE	    = IWebConstants.ROOT_DIR + "files/siap/sico/magistratocompetente/LoadRicercaMagistratoCompetente.jsp";
  public static final String PG_LOAD_DETTAGLIO_MAGISTRATO_COMPETENTE	= IWebConstants.ROOT_DIR + "files/siap/sico/magistratocompetente/LoadDettaglioMagistratoCompetente.jsp";
  public static final String PG_RICERCAMAGISTRATOCOMPETENTE	          = IWebConstants.ROOT_DIR + "files/siap/sico/magistratocompetente/RicercaMagistratoCompetente.jsp";
  public static final String PG_LOAD_INSERISCI_MAGISTRATO_COMPETENTE	= IWebConstants.ROOT_DIR + "files/siap/sico/magistratocompetente/LoadInserisciMagistratoCompetente.jsp";

  public static final String PG_LOAD_RICERCAPROCEDIMENTI  = IWebConstants.ROOT_DIR + "files/siap/sico/magistratocompetente/LoadRicercaProcedimentiMagistratoCompetente.jsp";
  public static final String PG_ESITO_RICERCAPROCEDIMENTI = IWebConstants.ROOT_DIR + "files/siap/sico/magistratocompetente/EsitoRicercaProcedimentiMagistratoCompetente.jsp";
  public static final String PG_ESITO_MODIFICAPROCEDIMENTI = IWebConstants.ROOT_DIR + "files/siap/sico/magistratocompetente/EsitoModificaMultiplaMagistratoAssegnatario.jsp";
}
