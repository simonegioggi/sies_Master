package siap.siep.richiesta.action;

import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

public class ActCancellaAnnotazioniManualiRichieste extends ActionSiap
                                                    implements ICostantiAnnotazioneManuale
{
  public String processRequest() throws Exception
  {
    IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
    AnnotazioneManualeModel lAnnMan = lCtrlAnnMan.ExCancellaAnnotazioneManualeComputo(getRequestBigDecimalParameter(CAMPO_ID_ANNOTAZIONE_MANUALE), true);

    String lPage = "";
    if(  lAnnMan.getCodTipoAnnotazione() != null         //AMNISTIA-INDULTO
      && (lAnnMan.getCodTipoAnnotazione().equals("002") || lAnnMan.getCodTipoAnnotazione().equals("003"))
      )
    {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.richiesta.action.ActLoadRichiestaAmnistiaIndulto";
    }
    else if(lAnnMan.getCodTipoAnnotazione() != null
   	      && ( lAnnMan.getCodTipoAnnotazione().equals("004")   //DEPENALIZZAZIONE
    		  // MEV 37 - Inizio
  	    	  || lAnnMan.getCodTipoAnnotazione().equals("017") ) )	 // ILLECITO AMMINISTRATIVO
    		  // MEV 37 - Fine	
    {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.richiesta.action.ActLoadRichiestaDepenalizzazione";
    }
    else                                                 //INCOSTITUZIONALITA'
    {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.richiesta.action.ActLoadRichiestaIncostituzionalita";
    }

    return lPage;
  }
}