package siap.siep.modulocumulo.action;

import f3b.web.IWebConstants;

/**
 * Classe con i riferimenti alla popup dello stato esecuzione cumulo e altre costanti
 * @author d.fiorletta
 *
 */
public interface ICostantiStatoEsecuzioneCumulo {
  
  public static final String CAMPO_TIPO_ATTIVITA       = "tipoAttivita";
  public static final String CAMPO_TIPO_ATTIVITA_PM    = "tipoAttivitaPM";
  public static final String CAMPO_TIPO_ATTIVITA_GE    = "tipoAttivitaGE";
  public static final String CAMPO_TIPO_ATTIVITA_SORV  = "tipoAttivitaSORV";
  public static final String CAMPO_COD_ATTIVITA  	   = "codAttivitaSORV";
  
  
  
  public static final String PG_POPUP_STATO_ESEC_FASCICOLO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/StatoEsecuzioneFascicoloCumulato.jsp";
  public static final String PG_POPUP_RET_STATO_ESEC_FASCICOLO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/MessageSET.jsp";

  // Azioni dello stato esecuzione
  public static final String AZIONE_DETTAGLIO = "DettaglioStatoEsecuzione";
  public static final String AZIONE_MODIFICA  = "ModificaStatoEsecuzione";
  public static final String AZIONE_CANCELLA  = "CancellaStatoEsecuzione";
  
  
  public static final String PG_POPUP_PM_SOSP_C5 = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/StatoEsec_PM_SOSP_C5.jsp";

  
  
}
