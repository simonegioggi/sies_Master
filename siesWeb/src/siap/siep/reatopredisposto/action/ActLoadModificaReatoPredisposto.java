package siap.siep.reatopredisposto.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.reatopredisposto.controller.ReatoPredispostoController;
import siap.siep.reatopredisposto.model.ReatoPredispostoModel;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadModificaReatoPredisposto</p>
* <p>Description: Classe Action per la load modifica di Reato Predisposto</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadModificaReatoPredisposto extends ActionSiap implements ICostantiReatoPredisposto
{
	 public String processRequest() throws Exception
		{

    //Controllo che non si stia lavorando su una entità in modifica ad altri
      LockModel lck =
      lockIfNotLocked("reatopredisposto", getRequestStringParameter(CAMPO_ID_REATO_PREDISPOSTO), getCodUtenteConnesso());
      if (lck != null)
      {
        setRequestAttribute (IWebConstants.MESSAGE_TEXT, "Il "+lck.getEntity()+" è in gestione ad un altro utente! <BR>Riprovare più tardi!");
        return IWebConstants.PG_MESSAGE;
      }
      
      ReatoPredispostoModel lMod = new ReatoPredispostoModel();

        lMod.setIdReatoPredisposto(getRequestBigDecimalParameter(ICostantiReatoPredisposto.CAMPO_ID_REATO_PREDISPOSTO));

        //IReato lCtrl = SIEPLookupRemote.getReatoRemote();
        
        ReatoPredispostoController lCtrl = new ReatoPredispostoController();
        
        lMod = lCtrl.ExRicercaReatoPredispostoByKey(lMod.getIdReatoPredisposto());

        setRequestAttribute("reatopredisposto", lMod);

        Option lOption  = new Option( DecodificheManager.getInstance().getTipoFonteReato(), lMod.getCodFonte() );
        setRequestAttribute("TipiFontiReato", "" + lOption );        

        lOption  = new Option( DecodificheManager.getInstance().getSottonumerazione(), lMod.getCodSottonumerazione() );
        setRequestAttribute("TipiSottonumerazione", "" + lOption );

        setRequestAttribute("modalita", "M");

        return PG_LOAD_MODIFICAREATOPREDISPOSTO;  //restituisce la jsp di VIEW

		}
}