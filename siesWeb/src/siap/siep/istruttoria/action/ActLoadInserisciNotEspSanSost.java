package siap.siep.istruttoria.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import f3b.util.DateUtils;

/**
 *
 * <p>Title: ActLoadInserisciNotEspSanSost</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Eutelia</p>
 * @author Dario
 * @version 1.0
 */
public class ActLoadInserisciNotEspSanSost extends ActionSiap implements ICostantiIstruttoria
{
   public String processRequest() throws Exception
   {
    if (this.isSessionAttributeNullObj("fascicolo"))
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
  
    this.isFascicoloSiepDiCompetenza();
    this.isEventoNonValidato();

    setRequestAttribute("fascicolo",lFascMod);
  
    setRequestAttribute("datairrevocabilita", DateUtils.getDateToString(lFascMod.getDataIrrevocabilita(),"dd/MM/yyyy"));
    
 // Se vengo da IstruttoriaCUMULO
    if(!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) )
    {
    	setRequestAttribute("IdIstruttoriaCumulo", getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
    }
    
    return PG_LOAD_INSERISCI_NOT_ESP_SAN_SOST;
   }
}