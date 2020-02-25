package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.modulocumulo.controller.ICircostanzaCumulo;

/**
* <p>Title: ActDettaglioCircostanzaCumulo</p>
* <p>Description: Classe Action per la load 'Dettaglio Circostanza Cumulo'</p>
* @version 1.0
*/

public class ActDettaglioCircostanzaCumulo extends ActionModuloCumulo implements ICostantiCircostanzaCumulo
{
  protected CircostanzaCumuloModel mlCirMod =null;

  public String processRequest() throws Exception
  {
	  // =========================================================================
	  // Recupero i dati del TITOLOCUMULO e ISTRUTTORIACUMULO da passare alla form
	  //==========================================================================
	  super.getDatiIstruttoria();
	  super.getDatiTitoloCumulato();
	  
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_CIRCOSTANZA_CUMULO);

    ICircostanzaCumulo lCtrl = SIEPLookupRemote.getCircostanzaCumuloRemote();
    mlCirMod = lCtrl.ExRicercaCircostanzaCumuloByKey(lId);

    setRequestAttribute("circostanza", mlCirMod);

    return PG_DETTAGLIO_CIRCOSTANZA_CUMULO;
  }
}