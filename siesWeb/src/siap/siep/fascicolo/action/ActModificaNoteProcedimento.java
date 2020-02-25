package siap.siep.fascicolo.action;


/**
 * <p>Title: ActModificaNoteProcedimento</p>
 * <p>Description: Azione di Modifica delle Note del Procedimento</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActModificaNoteProcedimento extends ActionSiap implements ICostantiFascicoloSiep
{
  public String processRequest() throws Exception
  {
    UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

    FascicoloSiepModel lFasMod = new FascicoloSiepModel();

    lFasMod.setIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIEP));
    
    if (isRequestParameterNullObj(CAMPO_NOTE))
    	lFasMod.setNote("");
    else
    	lFasMod.setNote(getRequestStringParameter(CAMPO_NOTE));
 
    lFasMod.setCodOperatoreAggiornamento( lUtenteMod.getUserId() );
    lFasMod.setDataAggiornamento(DateUtils.getSysDate());
    lFasMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());

    //FascicoloSiepController lCtrl = new FascicoloSiepController();
    IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

    lCtrl.ExModificaNoteFascicoloSiep(lFasMod);

    return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+CAMPO_ID_FASCICOLO_SIEP+"="+lFasMod.getIdFascicoloSiep().toString();
  }
}
