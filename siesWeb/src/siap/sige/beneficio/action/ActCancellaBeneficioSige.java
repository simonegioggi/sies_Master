package siap.sige.beneficio.action;


import siap.siep.beneficio.action.ICostantiBeneficio;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.beneficio.model.BeneficioSigeModel;
import siap.sige.web.ActionSige;

public class ActCancellaBeneficioSige extends ActionSige
{
  /**
   * Azione di cancellazione del Beneficio SIGE
   */
	
  public String processRequest() throws Exception
  {
	gestioneRitorno();
   
	// il model viene valorizzato con l'ID del Beneficio da cancellare
    BeneficioSigeModel lBenMod = new BeneficioSigeModel();
    lBenMod.setIdBeneficio(getRequestBigDecimalParameter(ICostantiBeneficio.CAMPO_ID_BENEFICIO));

    // chiama il controller
    IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
    lCtrl.ExCancellaBeneficioTipologiaOrario(lBenMod);
 
    return ritornoDopoCancellazione("Beneficio cancellato", null);

  }
}
