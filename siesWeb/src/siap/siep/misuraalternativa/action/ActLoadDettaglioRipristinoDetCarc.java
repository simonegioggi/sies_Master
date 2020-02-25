package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadDettaglioRipristinoDetCarc</p>
* <p>Description: Classe Action per la load dettaglio di Ripristino detenzione in carcere</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioRipristinoDetCarc extends ActionSiap implements ICostantiMisuraAlternativa
{
 public String processRequest() throws F3BException
  {
 		String lId = getRequestStringParameter(ICostantiVerbale.CAMPO_ID_VERBALE);
    BigDecimal lIdFasc =((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep();

 		VerbaleModel lVerMod = new VerbaleModel();
		IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
    lVerMod = lCtrl.ExRicercaVerbaleByKey(new BigDecimal(lId));

    LuogoDetenzioneModel lLuoDetMod = new LuogoDetenzioneModel();
    ILuogoDetenzione lCtrlDet = SIEPLookupRemote.getLuogoDetenzioneRemote();
	  lLuoDetMod = lCtrlDet.ExRicercaLuogoDetenzioneCorrenteByFascicoloSiep(lIdFasc);

     //preparo il model di pena residua
    PenaResiduaModel lPenMod = new PenaResiduaModel();
    IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
    lPenMod = IPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFasc);

    setRequestAttribute("luogodetenzione", lLuoDetMod);
    setRequestAttribute("verbale", lVerMod);
    setRequestAttribute("penaresidua", lPenMod);

    return PG_LOAD_DETTAGLIO_MA_RIPRISTINO_DET_CARC;
	}
}