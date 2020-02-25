package siap.siep.modulocumulo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciPenaReatoCumulo		</p>
* <p>Description: Classe Action per la load dell'Inserimento della Pena </p>
* <p> 			  sul singolo Reato Cumulato							</p>
*/
public class ActLoadInserisciPenaReatoCumulo extends ActionModuloCumulo implements ICostantiReatoCumulo
{

  public String processRequest() throws Exception
  {
	// Recupero Dati di Istruttoria e Titolo Cumulo 
	super.getDatiIstruttoria();
	super.getDatiTitoloCumulato();
	  
    ReatoCumuloModel lMod = new ReatoCumuloModel();

    lMod.setIdReatoCum(getRequestBigDecimalParameter(ICostantiReatoCumulo.CAMPO_ID_REATO_CUM));

    IReatoCumulo lCtrl = SIEPLookupRemote.getReatoCumuloRemote();

    lMod = lCtrl.ExRicercaReatoCumuloByKey(lMod.getIdReatoCum());

    //if(lMod.isPenaReatoInserita())
    //  throw new F3BException(F3BException.USER_MESSAGE, "Pena Reato già inserita");

    setRequestAttribute("reatoCum", lMod);

    Option lOption  = new Option( DecodificheManager.getInstance().getTipoPenaDetentiva(), lMod.getCodTipoPenaDetentiva() );
    setRequestAttribute("TipiPeneDetentive", "" + lOption );

    lOption  = new Option( DecodificheManager.getInstance().getFlagLireEuro(), "EUR");
    setRequestAttribute("Valute", "" + lOption );

    lOption  = new Option( DecodificheManager.getInstance().getTipoSanzione(), lMod.getCodTipoSanzione());
    setRequestAttribute("TipoSanzione", "" + lOption );

    setRequestAttribute("modalita", "M");

    return PG_LOAD_INSERISCI_PENAREATO_CUM;
  }
}