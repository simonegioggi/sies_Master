package siap.siep.reato.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciPenaReato</p>
* <p>Description: Classe Action per la load inserisci di PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadModificaPenaReato extends ActionSiap implements ICostantiReato
{
/**
 * Azione di Load Inserisci Pena Reato
 * @return Nome della pagina JSP su cui posizionarsi
 * al termine dell'elaborazione
 * @throws Exception
 */
  public String processRequest() throws Exception
  {
	 // Si legge il reato in sessione
    ReatoModel lMod = (ReatoModel)getSessionAttribute("reato");


    IReato lCtrl = SIEPLookupRemote.getReatoRemote();

   BigDecimal lFasc = getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP);
   lMod = lCtrl.ExRicercaNormaPrincipaleByReatoFascicoloSiep(lMod.getProgrReato(), lFasc);

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