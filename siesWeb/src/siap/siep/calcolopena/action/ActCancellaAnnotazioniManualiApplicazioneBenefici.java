package siap.siep.calcolopena.action;

import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

/**
 * Classe Action per la cancellazione delle annotazioni manuali - Decisioni del GE -
 * Applicazione Benefici (Amnistia/Indulto, Depenalizzazione, Incostituzionalità)
 *
 */
public class ActCancellaAnnotazioniManualiApplicazioneBenefici extends ActionSiap
                                                               implements ICostantiAnnotazioneManuale
{
  /*****************************************************************************
   * Metodo per la cancellazione delle Annotazioni Manuali. Al termine della 
   * cancellazione viene invocata la action di LoadInserimento della tipo 
   * annotazione appena cancellata
   * 
   * @return Action Load Inserimento
   * @throws
   ************************************************************************** */
  public String processRequest() throws Exception
  {
    IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
    AnnotazioneManualeModel lAnnMan = lCtrlAnnMan.ExCancellaAnnotazioneManualeComputo(getRequestBigDecimalParameter(CAMPO_ID_ANNOTAZIONE_MANUALE), false);

    String lPage = "";
    if(  lAnnMan.getCodTipoAnnotazione() != null         //AMNISTIA-INDULTO
      && (lAnnMan.getCodTipoAnnotazione().equals("002") || lAnnMan.getCodTipoAnnotazione().equals("003"))
      )
    {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActLoadNuovoCalcoloPenaBenefici";
    }
    else if(lAnnMan.getCodTipoAnnotazione() != null
      && ( lAnnMan.getCodTipoAnnotazione().equals("004")   //DEPENALIZZAZIONE
    		  // MEV 37 - Inizio
    	  || lAnnMan.getCodTipoAnnotazione().equals("017") ) )	 // ILLECITO AMMINISTRATIVO
    		  // MEV 37 - Fine	
    {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActLoadGEDepen";
    }
    else                                                 //INCOSTITUZIONALITA'
    {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActLoadGEIncost";
    }

    return lPage;
  }
}