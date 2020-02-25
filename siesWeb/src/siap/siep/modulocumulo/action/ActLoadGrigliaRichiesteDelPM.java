package siap.siep.modulocumulo.action;


import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import f3b.util.F3BException;

/**
 * Action per il caricamento delle Griglia per la gestione delle Richieste  del 
 * P.M. dell'Esecuzione
 * 
 * @author 
 */
public class ActLoadGrigliaRichiesteDelPM extends ActionModuloCumulo implements ICostantiModuloCumulo
{
  /**
   * 
   */
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    super.getDatiIstruttoria();
    //super.getDatiCumulo();

    return PG_LOAD_GRIGLIA_RICHIESTE_PM;
  }
}
