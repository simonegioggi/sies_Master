package siap.siep.avvocato.action;

/**
 * <p>Title: ActLoadSostituzioneDifensore</p>
 * <p>Description: Classe Action per la sostituzione dell'avvocato difensore
 *    asegnato a un fascicolo</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActLoadSostituzioneDifensore extends ActionSiap
    implements ICostantiAvvocato
{
  /*****************************************************************************
   * Azione per il caricamento della pagina di inserimento nuovo avvocato
   * difensore nel caso di sostituzione.
   * 
   * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
   *         vale a dire la pagina di inserimento
   * @throws F3BException
   ************************************************************************** */
  public String processRequest() throws Exception
  {
    if( isSessionAttributeNullObj("fascicolo") )
    			throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare il fascicolo." );
    
    this.isEventoNonValidato(); 
    BigDecimal id_fascicolo = ((FascicoloSiepModel)(getSessionAttribute("fascicolo"))).getIdFascicoloSiep();
    
    //==========================================================================
    // Recupero i dati dell'avvocato da sostituire e li passo alla maschera che
    // li precarica
    //==========================================================================
    BigDecimal id_avv_old = new BigDecimal(this.getRequestStringParameter("tipo"));
    IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
    AvvocatoSiepModel avvocato = lCtrl.ExRicercaAvvocatoFascicoloSiepByIdAvvocatoIdFascicolo(id_avv_old, id_fascicolo);
    setRequestAttribute("avvocato", avvocato);
    
    //==========================================================================
    // Carico i dati per le combo della finestra di inserimento nuovo avvocato 
    //==========================================================================
    Option lOption = new Option(DecodificheManager.getInstance().getTipoAvvocato());
    setRequestAttribute("tipoAvvocato", "" + lOption);
    
    lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("autoritaEsterna", "" + lOption);
    
    lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),"22");
    setRequestAttribute("autoritaEsternaDif", "" + lOption);
    
    lOption = new Option(DecodificheManager.getInstance().getMotivoDesignazione());
    setRequestAttribute("motivoDesignazione", "" + lOption);
    
    // 19/03/2010 Nuova gestione Combo per Foro avvocato.
    //IAvvocato lCtrl1 = SIEPLookupRemote.getAvvocatoRemote();
    //Vector lVect = lCtrl1.ExRicercaForiCaricati();
    //this.setRequestAttribute("foro", lVect);
    	
    UfficioModel lUffUte = this.getUfficioUtenteConnesso();
    String lDescrComune = lUffUte.getDescrComune();
    this.setRequestAttribute("comune", lDescrComune);
    
    // 19/03/2010 Nuova gestione Combo per Foro avvocato.
    //lOption = new Option(DecodificheManager.getInstance().getForo(), lDescrComune.toUpperCase().trim(), Option.NO_BLANK_ITEM);
    String lStatoForo = DecodificheUtils.getCodAltebyCode(DecodificheManager.getInstance().getForoAll(), avvocato.getAvvocato().getForo());
    
    if ("SOPPRESSO".equals(lStatoForo)){
      // aggiungo un black item. La combo foro non deve presentare un valore preselezionato
      lOption = new Option(DecodificheManager.getInstance().getForo(), Option.BLANK_ITEM);
    } else {
      lOption = new Option(DecodificheManager.getInstance().getForo(), avvocato.getAvvocato().getForo(),Option.NO_BLANK_ITEM);
    }
    setRequestAttribute("foro", ""+ lOption);

    // 20210627 MEV_21 Nuova gestione Combo per Stato di Nascita
  	lOption = new Option(DecodificheManager.getInstance().getNazioni(), "-");
  	setRequestAttribute("nazione", "" + lOption );      

  	// 20210627 MEV_21 Nuova gestione Combo per Stato Difensore
  	lOption = new Option(DecodificheManager.getInstance().getListaAttivitaAvvocato(), "-");
  	setRequestAttribute("statoAvv", "" + lOption );      
    
    return PG_SOSTITUZIONE_AVVOCATO;
  }
}