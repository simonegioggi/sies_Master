package siap.sige.magistratoassegnatario.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiMagistratoAssegnatario</p>
* <p>Description: Classe di costanti di MagistratoAssegnatario</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public interface ICostantiMagistratoAssegnatario
{
  public static final String CAMPO_GIORNO_DATA_INIZIO           = "CampoGiornoDataInizio";
  public static final String CAMPO_MESE_DATA_INIZIO             = "CampoMeseDataInizio";
  public static final String CAMPO_ANNO_DATA_INIZIO             = "CampoAnnoDataInizio";
  //public static final String CAMPO_GIORNO_DATA_FINE             = "CampoGiornoDataFine";
  //public static final String CAMPO_MESE_DATA_FINE               = "CampoMeseDataFine";
  //public static final String CAMPO_ANNO_DATA_FINE               = "CampoAnnoDataFine";
  //public static final String CAMPO_COD_RUOLO_MAGISTRATO         = "CodRuoloMagistrato";
  //public static final String CAMPO_COD_OPERATORE_INSERIMENTO    = "CodOperatoreInserimento";
  //public static final String CAMPO_GIORNO_DATA_INSERIMENTO      = "GiornoDataInserimento";
  //public static final String CAMPO_MESE_DATA_INSERIMENTO        = "MeseDataInserimento";
  //public static final String CAMPO_ANNO_DATA_INSERIMENTO        = "AnnoDataInserimento";
  //public static final String CAMPO_COD_UFFICIO_INSERIMENTO      = "CodUfficioInserimento";
  //public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO  = "CodOperatoreAggiornamento";
  //public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO    = "GiornoDataAggiornamento";
  //public static final String CAMPO_MESE_DATA_AGGIORNAMENTO      = "MeseDataAggiornamento";
  //public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO      = "AnnoDataAggiornamento";
  //public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO    = "CodUfficioAggiornamento";
  public static final String CAMPO_MAG_COD_MAGISTRATO           = "MagCodMagistrato";
  //public static final String CAMPO_FAS_SIGE_ID_FASCICOLO_SIGE   = "FasSigeIdFascicoloSige";
  //public static final String CAMPO_RITORNO                      = "MagRelRitorno";

  //public static final String PG_LOAD_RICERCAMAGISTRATOASSEGNATARIO	 = IWebConstants.ROOT_DIR + "files/siap/sige/magistratoassegnatario/LoadRicercaMagistratoAssegnatario.jsp";
  public static final String PG_LOAD_DETTAGLIOMAGISTRATOASSEGNATARIO = IWebConstants.ROOT_DIR + "files/siap/sige/magistratoassegnatario/LoadDettaglioMagistratoAssegnatario.jsp";
  public static final String PG_RICERCAMAGISTRATOASSEGNATARIO	     = IWebConstants.ROOT_DIR + "files/siap/sige/magistratoassegnatario/RicercaMagistratoByProcedimentoSige.jsp";
  
  public static final String PG_LOAD_INSERISCI_MAGISTRATOASSEGNATARIO = IWebConstants.ROOT_DIR + "files/siap/sige/magistratoassegnatario/LoadInserisciMagistratoAssegnatario.jsp";
  public static final String PG_SINTESIMAGISTRATOASSEGNATARIO = IWebConstants.ROOT_DIR + "files/siap/sige/magistratoassegnatario/SintesiMagistratoAssegnatario.jsp";

  public static final String PG_ELENCOMAGISTRATOASSEGNATARIO	     = IWebConstants.ROOT_DIR + "files/siap/sige/magistratoassegnatario/ElencoMagistratoAssegnatario.jsp";
  
}