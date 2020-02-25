package siap.sico.assistentegiudiziario.action;

import java.math.BigDecimal;

import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import siap.sico.decodifiche.controller.DecodificheManager;
// per la decodifica
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadDettaglioAssistenteGiudiziario</p>
* <p>Description: Classe Action per la load dettaglio di AssistenteGiudiziario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioAssistenteGiudiziario extends ActionSiap implements ICostantiAssistenteGiudiziario
{
  public String processRequest() throws F3BException {

    String lId = getRequestStringParameter(CAMPO_ID_ASSISTENTE_GIUDIZIARIO);

    // chiama il controller
    IAssistenteGiudiziario lCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
    AssistenteGiudiziarioModel llAssMod = lCtrl.ExRicercaAssistenteGiudiziarioByKey(new BigDecimal(lId));

    // Decodifica di Flag_stato
   // Collection lCol = (DecodificheManager.getInstance()).getFlagStato();
    llAssMod.setFlagStato(DecodificheUtils.getDescbyCode((DecodificheManager.getInstance()).getFlagStato(),llAssMod.getFlagStato()));

    setRequestAttribute("assistentegiudiziario", llAssMod);

    return PG_LOAD_DETTAGLIOASSISTENTEGIUDIZIARIO;
}



}