package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.web.html.Option;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.modulocumulo.controller.ICircostanzaCumulo;

/**
* <p>Title: ActLoadModificaCircostanzaCumulo</p>
* <p>Description: Classe Action per la load 'Modifica Circostanza Cumulo'</p>
* @version 1.0
*/

public class ActLoadModificaCircostanzaCumulo extends ActionModuloCumulo implements ICostantiCircostanzaCumulo
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
    
    if(mlCirMod!=null && mlCirMod.getIdCircostanzaCumulo()!=null)
    {
    	if(mlCirMod.getCodBilanciamentoCircostanze()==null)
    	{
    		mlCirMod.setCodBilanciamentoCircostanze("-");
    	}
    }

    Option lOption  = new Option( DecodificheManager.getInstance().getTipoFonteReato(), mlCirMod.getCodFonte() );
    setRequestAttribute("TipiFontiReato", "" + lOption );

    lOption  = new Option( DecodificheManager.getInstance().getSottonumerazione(), mlCirMod.getCodSottonumerazione() );
    setRequestAttribute("TipiSottonumerazione", "" + lOption );

    lOption  = new Option( DecodificheManager.getInstance().getBilanciamentoCircostanze(), mlCirMod.getCodBilanciamentoCircostanze() );
    setRequestAttribute("BilanciamentoCircostanze", "" + lOption );

    lOption = new Option(DecodificheManager.getInstance().getSottonumerazione(), mlCirMod.getCommaQualificante());
    setRequestAttribute("TipiCommaQualificante", "" + lOption);
    
   setRequestAttribute("modalita", "M");
    
    return PG_LOAD_MODIFICACIRCOSTANZA_CUMULO;
  }
}