package siap.sige.reato.action;


import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.reato.action.ICostantiReato;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.web.ActionSige;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciPenaReatoSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActLoadModificaPenaReatoSige extends ActionSige implements ICostantiReato
{

  public String processRequest() throws Exception
  {
	
	  // lock
	  lockApplicativoSuReato();

	  
	  gestioneRitorno();
	
    ReatoModel lMod = new ReatoModel();

    lMod.setIdReato(getRequestBigDecimalParameter(ICostantiReato.CAMPO_ID_REATO));

    IReato lCtrl = SIEPLookupRemote.getReatoRemote();
    
    lMod = lCtrl.ExRicercaReatoByKey(lMod.getIdReato());

    setRequestAttribute("reato", lMod);

    Option lOption  = new Option( DecodificheManager.getInstance().getTipoPenaDetentiva(), lMod.getCodTipoPenaDetentiva() );
    setRequestAttribute("TipiPeneDetentive", "" + lOption );

    lOption  = new Option( DecodificheManager.getInstance().getFlagLireEuro(), "EUR");
    setRequestAttribute("Valute", "" + lOption );

    lOption  = new Option( DecodificheManager.getInstance().getTipoSanzione(), lMod.getCodTipoSanzione());
    setRequestAttribute("TipoSanzione", "" + lOption );

    setRequestAttribute("modalita", "M");
    if(!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione guidata
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
    }
	setRequestAttribute("modo", "SIGE");

    return ICostantiReato.PG_LOAD_INSERISCIPENAREATO;
  }
}