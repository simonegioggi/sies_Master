package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.modulocumulo.controller.IContinuazioneCumulo;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadModificaContinuazioneCumulo</p>
* <p>Description: Classe Action per la load Modifica Continuazione (Pena Complessiva)</p>
* <p>		in ambito Cumulo (Pena_complessiva_Cumulo/Continuazione_Cumulo) </p>
* @version 1.0
*/

public class ActLoadModificaContinuazioneCumulo extends ActionModuloCumulo implements ICostantiPenaComplessivaCumulo,
                                                                        			ICostantiContinuazioneCumulo
{
  public String processRequest() throws Exception
  {
    //==========================================================================
    // Recupero i dati dell'ISTRUTTORIA, TITOLO, da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();   
    
    // Recupera il record Continuazione
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_CONTINUAZIONE_CUM);

    IContinuazioneCumulo lCtrl = SIEPLookupRemote.getContinuazioneCumuloRemote();
    ContinuazioneCumuloModel lContMod = lCtrl.ExRicercaContinuazioneCumByKey(lId);
    setRequestAttribute("continuazioneCum", lContMod);

    Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioS());
    if (lContMod.getCodTipoAutorita()!=null) 
      lOption.setSelected (lContMod.getCodTipoAutorita());
    setRequestAttribute("autoritaSentenza", ""+lOption );

    lOption = new Option( DecodificheManager.getInstance().getTipoContinuazione());
    if (lContMod.getCodTipoContinuazione()!=null)
      lOption.setSelected (lContMod.getCodTipoContinuazione());
    setRequestAttribute("tipoContinuazione", ""+lOption );
    
    // combo Tipo Registro Generale
    Option tipoRegGen = new Option ( DecodificheManager.getInstance().getTipoRegistroGenerale(), "-");
    if (lContMod.getTipoRegGen()!=null)
      tipoRegGen.setSelected(lContMod.getTipoRegGen());
    setRequestAttribute("tipoRegGen", "" + tipoRegGen );    

    return PG_LOAD_MODIFICA_CONTINUAZIONE_CUM;
  }
}