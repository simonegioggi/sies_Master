package siap.sige.unificazione.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiUnificazioneSige</p>
* <p>Description: Classe di costanti di Unificazione</p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiVerbaleUnificazioneSige
{
  public static final String CAMPO_ANNO_DA_UNIF = "AnnoDaUnif";
  public static final String CAMPO_NUMERO_DA_UNIF = "NumeroDaUnif";
  public static final String CAMPO_NUMERO_DA_UNIF_ORIGIN = "NumeroDaUnifOrigin";
  public static final String CAMPO_ANNO_UNIFICANTE = "AnnoUnificante";
  public static final String CAMPO_NUMERO_UNIFICANTE = "NumeroUnificante";
  public static final String CAMPO_NUMERO_UNIFICANTE_ORIGIN = "NumeroUnificanteOrigin";
  public static final String CAMPO_DATA_UNIFICAZIONE = "DataUnificazione";
  public static final String CAMPO_DATA_GG_UNIFICAZIONE = "DataGGUnificazione";
  public static final String CAMPO_DATA_MM_UNIFICAZIONE = "DataMMUnificazione";
  public static final String CAMPO_DATA_AAAA_UNIFICAZIONE = "DataAAAAUnificazione";
  public static final String CAMPO_ID_FASCICOLO_UNIFICANTE = "IdFascicoloUnificante";
  public static final String CAMPO_ID_FASCICOLO_UNIFICATO = "IdFascicoloUnificato";
  public static final String CAMPO_ID_EVENTO_UNIFICAZIONE = "IdEvento";
  public static final String CAMPO_ID_VERBALE_UNIFICAZIONE = "IdVerbaleUnificazione";
  public static final String CAMPO_COD_TIPO_UFFICIO_DA_UNIF = "CodTipoUfficioDaUnif";
  public static final String CAMPO_DESCR_COMUNE_UFFICIO_DA_UNIF = "DescrComuneUfficioDaUnif";
  public static final String CAMPO_COD_TIPO_UFFICIO_UNIFICANTE = "CodTipoUfficioUnificante";
  public static final String CAMPO_DESCR_COMUNE_UFFICIO_UNIFICANTE = "DescrComuneUfficioUnificante";
  public static final String CAMPO_ID_SOGGETTO_UNIFICANTE = "IdSoggettoUnificante";
  public static final String CAMPO_ID_SOGGETTO_UNIFICATO = "IdSoggettoUnificato";
  public static final String CAMPO_CHECKBOX = "Checkbox";

  public static final String ACTION_DOPO_CANCELLAZIONE    = "ActDopoCanc";

  public static final String RUOLO_FASCICOLO_DA_UNIFICARE = "da Unificare";
  public static final String RUOLO_FASCICOLO_UNIFICANTE = "Unificante";

  public static final String PG_LOAD_VERIFICAVERBALEUNIFICAZIONESIGE   = IWebConstants.ROOT_DIR + "files/siap/sige/unificazione/LoadVerificaVerbaleUnificazioneSige.jsp";
  public static final String PG_LOAD_INSERISCIVERBALEUNIFICAZIONESIGE  = IWebConstants.ROOT_DIR + "files/siap/sige/unificazione/LoadInserisciVerbaleUnificazioneSige.jsp";
  public static final String PG_INSERISCIVERBALEUNIFICAZIONESIGE	    = IWebConstants.ROOT_DIR + "files/siap/sige/unificazione/InserisciVerbaleUnificazioneSige.jsp";
  public static final String PG_DETTAGLIOVERBALEUNIFICAZIONESIGE       = IWebConstants.ROOT_DIR + "files/siap/sige/unificazione/DettaglioVerbaleUnificazioneSige.jsp";
  public static final String PG_LOAD_UNIFICAZIONESOGGETTISIGE  = IWebConstants.ROOT_DIR + "files/siap/sige/unificazione/LoadUnificazioneSoggetti.jsp";
  public static final String PG_LOAD_INS_UNIFICAZIONESOGGETTISIGE  = IWebConstants.ROOT_DIR + "files/siap/sige/unificazione/LoadInsUnificazioneSoggettiSige.jsp";
  public static final String PG_LOAD_INSERISCIVERBALEUNIFICAZIONESIGE_FASCIOLO_UNIFICANTE = IWebConstants.ROOT_DIR + "files/siap/sige/unificazione/LoadInserisciVerbaleUnificazioneSigeFascicoloUnificante.jsp";

}