package siap.siep.modulocumulo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadModificaReatoCumulo</p>
 * <p>Description: Classe Action per la load modifica di Reato</p>
 * <p>					legato al titolo Cumulato	</p>
 */

public class ActLoadModificaReatoCumulo extends ActionModuloCumulo implements ICostantiReatoCumulo
{
  public String processRequest() throws Exception
  {
    //==========================================================================
    // Recupero i dati del da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();   
    
    ReatoCumuloModel lMod = new ReatoCumuloModel();
    
    lMod.setIdReatoCum(getRequestBigDecimalParameter(ICostantiReatoCumulo.CAMPO_ID_REATO_CUM));

    IReatoCumulo lCtrl = SIEPLookupRemote.getReatoCumuloRemote();
    lMod = lCtrl.ExRicercaReatoCumuloByKey(lMod.getIdReatoCum());

    setRequestAttribute("reato", lMod);

    Option lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato(), lMod.getCodFonte());
    setRequestAttribute("TipiFontiReato", "" + lOption);

    lOption = new Option(DecodificheManager.getInstance().getSottonumerazione(), lMod.getCodSottonumerazione());
    setRequestAttribute("TipiSottonumerazione", "" + lOption);

    lOption = new Option(DecodificheManager.getInstance().getTipoReato(), lMod.getCodTipoReato());
    setRequestAttribute("TipiReato", "" + lOption);

    lOption = new Option(DecodificheManager.getInstance().getPeriodoConsumazione(), lMod.getCodPeriodoConsumazione());
    setRequestAttribute("PeriodoConsumazione", "" + lOption);

    lOption = new Option(DecodificheManager.getInstance().getSottonumerazione(), lMod.getCommaQualificante());
    setRequestAttribute("TipiCommaQualificante", "" + lOption);

    return PG_LOAD_MODIFICAREATO_CUM;
  }
}