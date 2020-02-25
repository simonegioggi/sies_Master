package siap.regesies.regereato.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regereato.controller.IRegeReato;
import siap.regesies.regereato.model.RegeReatoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;
import f3b.web.html.Option;


/**
* <p>Title: ActLoadModificaRegeReato</p>
* <p>Description: Classe Action per la load Modifica di RegeReato</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadModificaRegeReato extends ActionRegeSiap
  implements ICostantiRegeReato
{
	 public String processRequest() throws F3BException
		{

	   String lId = getRequestStringParameter(CAMPO_ID_FILE);
     int lProgr = getRequestIntParameter(CAMPO_PROGR_REATO);
     int lProgrCirc = getRequestIntParameter(CAMPO_PROGR_CIRCOSTANZA);

 		 IRegeReato lCtrl = RegeSiesLookupRemote.getRegeReatoRemote();
  	 RegeReatoModel lRegMod = lCtrl.ExRicercaRegeReatoByKey(lId, lProgr,lProgrCirc);

     setRequestAttribute("regereato", lRegMod);


      Option lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato(), lRegMod.getCodFonte());
      setRequestAttribute("TipiFontiReato", "" + lOption);

      lOption = new Option(DecodificheManager.getInstance().getSottonumerazione(), lRegMod.getCodSottonumerazione());
      setRequestAttribute("TipiSottonumerazione", "" + lOption);

      lOption = new Option(DecodificheManager.getInstance().getTipoReato(), lRegMod.getCodTipoReato());
      setRequestAttribute("TipiReato", "" + lOption);

      lOption = new Option(DecodificheManager.getInstance().getPeriodoConsumazione(), lRegMod.getCodPeriodoConsumazione());
      setRequestAttribute("PeriodoConsumazione", "" + lOption);

      setRequestAttribute("modalita", "M");

		 return PG_LOAD_INSERISCIREGEREATO;  //restituisce la jsp di VIEW

		}



}