package siap.siep.modulocumulo.action;

/**
 * <p>Title: ActLoadModificaPenaComplessivaCumulo</p>
 * <p>Description: Azione di load della Modifica della PenaComplessiva</p>
 * <p>    in ambito Cumulo (Pena_complessiva_Cumulo) </p>
 */

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.model.LockModel;
import siap.siep.modulocumulo.controller.IPenaComplessivaCumulo;
import siap.siep.modulocumulo.model.PenaComplessivaSanzioneSostitutivaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

public class ActLoadModificaPenaComplessivaCumulo extends ActionModuloCumulo implements ICostantiPenaComplessivaCumulo
{
  public String processRequest() throws Exception
  {
    //Controllo che non si stia lavorando su una entità in modifica ad altri
    LockModel lck = lockIfNotLocked("pena complessiva Cumulo", getRequestStringParameter(CAMPO_ID_PENA_COMPLESSIVA_CUM), getCodUtenteConnesso());
    if (lck != null)
    {
      setRequestAttribute (IWebConstants.MESSAGE_TEXT, "La "+lck.getEntity()+" è in gestione ad un altro utente! <BR>Riprovare più tardi!");
      return IWebConstants.PG_MESSAGE;
    }
    
    //==========================================================================
    // Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();   

    preparazioneDati() ;
    
    return PG_LOAD_INSERISCIPENACOMPLESSIVA_CUM;
  }
  
  
  /**
   * Recupera i dati da visualizzare e carica opportunamente le combo
   * @throws Exception
   */
  protected void preparazioneDati() throws Exception
  {
    // riempie il model.
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA_CUM);

    IPenaComplessivaCumulo lCtrl = SIEPLookupRemote.getPenaComplessivaCumuloRemote();
    PenaComplessivaSanzioneSostitutivaCumuloModel lPenSanMod = lCtrl.ExRicercaPenaComplessivaSanzioneSostitutivaCumByKey(lId);
    setRequestAttribute("penaComplessivaSanzioneSostitutivaCumulo", lPenSanMod);

    Option lOption = new Option(DecodificheManager.getInstance().getTipoPenaDetentivaErgastolo(), lPenSanMod.getPenaComplessivaCumulo().getCodTipoPenaDetentiva());
    setRequestAttribute("tipoPenaDetentiva", ""+lOption );

    lOption  = new Option( DecodificheManager.getInstance().getFlagLireEuro(), "EUR");
    setRequestAttribute("valute", ""+lOption );

    // Combo Tipo Sanzione
    if(lPenSanMod.getSanzioneSostitutivaCumulo() != null)
      lOption = new Option( DecodificheManager.getInstance().getTipoSanzioneSostitutiva(), lPenSanMod.getSanzioneSostitutivaCumulo().getCodTipoSanzione());
    else
      lOption = new Option( DecodificheManager.getInstance().getTipoSanzioneSostitutiva(), "-");

    setRequestAttribute("tipoSanzioneSostitutiva", ""+lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");
  }
  
  
}
