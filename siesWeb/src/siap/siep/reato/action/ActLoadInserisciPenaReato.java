package siap.siep.reato.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciPenaReato</p>
* <p>Description: Classe Action per la load inserisci di PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadInserisciPenaReato extends ActionSiap implements ICostantiReato
{
/**
 * Azione di Load Inserisci Pena Reato
 * @return Nome della pagina JSP su cui posizionarsi
 * al termine dell'elaborazione
 * @throws Exception
 */
  public String processRequest() throws Exception
  {

    if(!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione guidata
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
    }

    ReatoModel lMod = new ReatoModel();

    lMod.setIdReato(getRequestBigDecimalParameter(ICostantiReato.CAMPO_ID_REATO));

    IReato lCtrl = SIEPLookupRemote.getReatoRemote();

    lMod = lCtrl.ExRicercaReatoByKey(lMod.getIdReato());

    if(lMod.isPenaReatoInserita())
      throw new F3BException(F3BException.USER_MESSAGE, "Pena Reato già inserita");

    setRequestAttribute("reato", lMod);

    Option lOption  = new Option( DecodificheManager.getInstance().getTipoPenaDetentiva(), lMod.getCodTipoPenaDetentiva() );
    setRequestAttribute("TipiPeneDetentive", "" + lOption );

    lOption  = new Option( DecodificheManager.getInstance().getFlagLireEuro(), "EUR");
    setRequestAttribute("Valute", "" + lOption );

    lOption  = new Option( DecodificheManager.getInstance().getTipoSanzione(), lMod.getCodTipoSanzione());
    setRequestAttribute("TipoSanzione", "" + lOption );

    setRequestAttribute("modalita", "M");

    return PG_LOAD_INSERISCIPENAREATO;
  }
}