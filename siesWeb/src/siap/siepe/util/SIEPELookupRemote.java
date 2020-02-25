package siap.siepe.util;

import siap.siepe.assistentesociale.controller.IAssistenteSociale;
import siap.siepe.assistentesocialeattivita.controller.IAssistenteSocialeAttivita;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.espertoattivita.controller.IEspertoAttivita;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.jms.controller.ITrasmissioneJMS;
import siap.siepe.relazione.controller.IRelazione;
import siap.siepe.richiesta.controller.IRichiesta;
import f3b.util.F3BException;
import f3b.util.LookupClass;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company Bull Italia S.p.A.: </p>
 * @author not attributable
 * @version 1.0
 */

public class SIEPELookupRemote extends LookupClass
{
  /**
   * Metodo che ritorna, l'istanza del Controller di FascicoloSiepe
   * <p>
   * @return istanza del controller castata alla corrispondente
   *         interfaccia.
   * @throws F3BException propaga errore di eccezione.
   */
  public static IFascicoloSiepe getFascicoloSiepeRemote() throws F3BException
  {
    Object lRef;
    IFascicoloSiepe lRemote;

    lRef = lookup("siap.siepe.fascicolo.controller.FascicoloSiepeController");
    lRemote = (IFascicoloSiepe) lRef;

    return lRemote;
  }

  /**
   * Metodo che ritorna, l'istanza del Controller all'AssistenteSociale
   * <p>
   * @return istanza del controller castata alla corrispondente
   *         interfaccia.
   * @throws F3BException propaga errore di eccezione.
   */
  public static IAssistenteSociale getAssistenteSocialeRemote() throws F3BException
  {
    Object lRef;
    IAssistenteSociale lRemote;

    lRef = lookup("siap.siepe.assistentesociale.controller.AssistenteSocialeController");
    lRemote = (IAssistenteSociale) lRef;

    return lRemote;
  }

  /**
   * Metodo che ritorna, l'istanza del Controller dell'Attività
   * <p>
   * @return istanza del controller castata alla corrispondente
   *         interfaccia.
   * @throws F3BException propaga errore di eccezione.
   */
  public static IAttivita getAttivitaRemote() throws F3BException
  {
    Object lRef;
    IAttivita lRemote;

    lRef = lookup("siap.siepe.attivita.controller.AttivitaController");
    lRemote = (IAttivita) lRef;

    return lRemote;
  }

  /**
   * Metodo che ritorna, l'istanza del Controller della Richiesta
   * <p>
   * @return IRichiesta istanza del controller castata alla corrispondente
   *         interfaccia.
   * @throws F3BException propaga errore di eccezione.
   */
  public static IRichiesta getRichiestaRemote() throws F3BException
  {
    Object lRef;
    IRichiesta lRemote;

    lRef = lookup("siap.siepe.richiesta.controller.RichiestaController");
    lRemote = (IRichiesta)lRef;

    return lRemote;
  }

  /**
 * Metodo che ritorna, l'istanza del Controller della TrasmissioneJMS
 * <p>
 * @return IRichiesta istanza del controller castata alla corrispondente
 *         interfaccia.
 * @throws F3BException propaga errore di eccezione.
 */
public static ITrasmissioneJMS getTrasmissioneJMSRemote() throws F3BException
{
  Object lRef;
  ITrasmissioneJMS lRemote;

  lRef = lookup("siap.siepe.jms.controller.TrasmissioneJMSController");
  lRemote = (ITrasmissioneJMS)lRef;

  return lRemote;
}

   /**
    * Metodo che ritorna, l'istanza del Controller della Relazione <p>
    * @return IRelazione istanza del controller castata alla corrispondente
    * interfaccia.
    * @throws F3BException propaga errore di eccezione.
    */
   public static IRelazione getRelazioneRemote() throws F3BException
   {
     Object lRef;
     IRelazione lRemote;

     lRef = lookup("siap.siepe.relazione.controller.RelazioneController");
     lRemote = (IRelazione)lRef;

     return lRemote;
   }

   /**
    * Metodo che ritorna, l'istanza del Controller  EspertoAttivita <p>
    * @return IEspertoAttivita istanza del controller castata alla corrispondente
    * interfaccia.
    * @throws F3BException propaga errore di eccezione.
    */
   public static IEspertoAttivita getEspertoAttivitaRemote() throws F3BException
   {
     Object lRef;
     IEspertoAttivita lRemote;

     lRef = lookup("siap.siepe.espertoattivita.controller.EspertoAttivitaController");
     lRemote = (IEspertoAttivita)lRef;

     return lRemote;
   }

   /**
    * Metodo che ritorna, l'istanza del Controller  per AssistenteSocialeAttivita <p>
    * @return IAssistenteSocialeAttivita istanza del controller castata alla corrispondente
    * interfaccia.
    * @throws F3BException propaga errore di eccezione.
    */
   public static IAssistenteSocialeAttivita getAssistenteSocialeAttivitaRemote() throws F3BException
   {
     Object lRef;
     IAssistenteSocialeAttivita lRemote;

     lRef = lookup("siap.siepe.assistentesocialeattivita.controller.AssistenteSocialeAttivitaController");
     lRemote = (IAssistenteSocialeAttivita)lRef;

     return lRemote;
   }


}
