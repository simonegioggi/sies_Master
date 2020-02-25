package siap.siep.beneficio.action;

/**
* <p>Title: ActCancellaRevSospCondAP</p>
* <p>Description: Classe Action per la cancellazione della Revoca Beneficio (Sospensione Condizionale)</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActCancellaRevSospCondAP extends ActionSiap implements ICostantiBeneficio
{
/**
* Azione di Cancellazione della Revoca Beneficio 
* (di tipo Sospensione Condizionale e Non Menzione)
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
	
public String processRequest() throws F3BException
 {
     String lId = getRequestStringParameter(CAMPO_ID_BENEFICIO);
     BeneficioModel lBenMod = new BeneficioModel();
     lBenMod.setIdBeneficio(new BigDecimal(lId));
     IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
     lCtrl.ExCancellaBeneficio(lBenMod);

      String lPage="";
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() ;
      return lPage; //restituisce la jsp di VIEW

}



}