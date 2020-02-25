package siap.siep.nuovaistanza.action;


/**
* <p>Title: ActModificaNuovaIstanza</p>
* <p>Description: Classe Action per la modifica di NuovaIstanza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile s.r.l.</p>
* @version 5.0
*/

import siap.sico.evento.action.ICostantiEvento;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

public class ActModificaNuovaIstanza extends ActionNuovaIstanza implements ICostantiNuovaIstanza
{

  public String processRequest() throws Exception 
  {

	INuovaIstanza lCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
	NuovaIstanzaModel lNuoMod  = lCtrl.ExRicercaNuovaIstanzaById(this.getRequestBigDecimalParameter(CAMPO_ID_NUOVA_ISTANZA));
	
	NuovaIstanzaModel lNuoModel = getModificaNuovaIstanza(lNuoMod);
		
    //modifica istanza

    lCtrl.ExModificaNuovaIstanza(lNuoModel);


    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza";
    lPage += "&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lNuoMod.getEveIdEvento().toString();

    return lPage;
  }
}