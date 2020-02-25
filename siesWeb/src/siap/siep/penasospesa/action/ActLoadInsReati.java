package siap.siep.penasospesa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.IWebConstants;
import f3b.web.html.Option;


/**
* <p>Title: ActLoadInserisciReato</p>
* <p>Description: Classe Action per la load inserisci di Reato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInsReati extends ActionSiap implements ICostantiPenaSospesa
{
 public String processRequest() throws Exception
  {

    if(!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione guidata
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
    }


      Option lOption = new Option( DecodificheManager.getInstance().getTipoReato());
      setRequestAttribute("TipiReato", "" + lOption );
      lOption  = new Option( DecodificheManager.getInstance().getTipoFonteReato() );
      setRequestAttribute("TipiFontiReato", "" + lOption );
      lOption  = new Option( DecodificheManager.getInstance().getSottonumerazione() );
      setRequestAttribute("TipiSottonumerazione", "" + lOption );
      lOption  = new Option( DecodificheManager.getInstance().getPeriodoConsumazione() );
      setRequestAttribute("PeriodoConsumazione", "" + lOption );
      lOption  = new Option( DecodificheManager.getInstance().getTipoPenaDetentiva() );
      setRequestAttribute("TipiPeneDetentive", "" + lOption );

	    setRequestAttribute("modalita", "I");

      return IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/LoadInserisciReato.jsp";

	}
}