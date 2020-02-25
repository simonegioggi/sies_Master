package siap.siep.istruttoria.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.util.DateUtils;
import f3b.web.html.Option;


/**
 *
 * <p>Title: ActLoadInserisciInizioEsecuzione</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Eutelia</p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciInizioEsecuzione extends ActionSiap implements ICostantiIstruttoria
{
   public String processRequest() throws Exception
   {

    if (this.isSessionAttributeNullObj("fascicolo"))
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    SentenzaModel sentmod = (SentenzaModel) getSessionAttribute("sentenza");
  
    this.isFascicoloSiepDiCompetenza();
    this.isEventoNonValidato();

    setRequestAttribute("fascicolo",lFascMod);
    setRequestAttribute("sentenza",sentmod);

    Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaSanzioni());
    setRequestAttribute("autorita", "" + lOption );
    setRequestAttribute("datairrevocabilita", DateUtils.getDateToString(lFascMod.getDataIrrevocabilita(),"dd/MM/yyyy"));
    
    
    return PG_LOAD_INSERISCI_INIZIO_ESECUZIONE;
   }
}