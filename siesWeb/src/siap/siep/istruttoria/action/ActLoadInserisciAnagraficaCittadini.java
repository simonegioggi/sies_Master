package siap.siep.istruttoria.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import f3b.util.DateUtils;
import f3b.web.html.Option;

/**
 *
 * <p>Title: ActLoadInserisciAnagraficaCittadini</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Eutelia</p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciAnagraficaCittadini extends ActionSiap implements ICostantiIstruttoria
{
   public String processRequest() throws Exception
   {

    if (this.isSessionAttributeNullObj("fascicolo"))
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
  
    this.isFascicoloSiepDiCompetenza();
    this.isEventoNonValidato();
    
    // Se vengo da IstruttoriaCUMULO
    if(!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) )
    {
    	setRequestAttribute("IdIstruttoriaCumulo", getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
    }

    setRequestAttribute("fascicolo",lFascMod);

    Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaSanzioni());
    setRequestAttribute("autorita", "" + lOption );
    setRequestAttribute("datairrevocabilita", DateUtils.getDateToString(lFascMod.getDataIrrevocabilita(),"dd/MM/yyyy"));
    
    
    return PG_LOAD_INSERISCI_ANAGRAFICA_CITTADINI;
   }
}