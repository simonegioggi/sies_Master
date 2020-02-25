package siap.siep.sentenza.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.StringUtils;
import f3b.web.html.Option;

public class ActLoadInserisciSentenzaDuplicata extends ActionSiap
                                               implements ICostantiSentenza
{
  public String processRequest() throws Exception
  {
    // Parse della request
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_SENTENZA);

    ISentenza lCtrl = SIEPLookupRemote.getSentenzaRemote();
    
    SentenzaModel lSen = lCtrl.ExRicercaSentenzaByKey(lId);
    
    /*
    if(lSen.isAltroGiudizio() && lSen.isSentenzaCassazione())
    {
      return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.sentenza.action.ActLoadDettaglioSentenza&"+CAMPO_ID_SENTENZA+"="+lSen.getIdSentenza().toString();
    }
    */
    
    setRequestAttribute("sentenza", lSen);

    String lCodTipoSentenza = lSen.getCodTipoProvvRif();
    if( "03".equals(lCodTipoSentenza))
    {
      lSen.setCodTipoProvvRif("01");
    }
    else if("04".equals(lCodTipoSentenza))
    {
      lSen.setCodTipoProvvRif("02");
    }
    
  	Option lOption = new Option( DecodificheManager.getInstance().getTipoProvvedimentiRif(), lSen.getCodTipoProvvRif());
  	
    setRequestAttribute("tipoProvvedimentiRif", "" + lOption );

    lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), lSen.getCodTipoAutoritaProvvRif());
    setRequestAttribute("autoritaProvRif", "" + lOption );

    lOption = new Option( DecodificheManager.getInstance().getTipoDecisioneCassazione(), lSen.getCodTipoDecisioneCassazione());
    setRequestAttribute("tipoDecisioneCassazione", "" + lOption );


    String selected2 = new String("-");
    if (lSen.getCodTipoAutoritaProvvRif().equals("DIB") || lSen.getCodTipoAutoritaProvvRif().equals("TRIBSD")) {
    	selected2 = StringUtils.toStringJSP(lSen.getCodTipoRito());
    }    
    lOption = new Option( DecodificheManager.getInstance().getTipoRitoSentenza(), selected2);
    setRequestAttribute("tipoRito2", "" + lOption );
    
    Option lOptionProvv = new Option( DecodificheManager.getInstance().getTipoProvvedimenti(), 
    		lSen.getCodTipoProvvedimentoRif());
    lOptionProvv.setFilter(new String[]{"-", "01", "53"});    
    setRequestAttribute("tipoProvvedimenti", "" + lOptionProvv );
    Option lOptionProvvAltro = new Option( DecodificheManager.getInstance().getTipoProvvedimenti(), 
    		lSen.getCodTipoProvvedimentoAltro());
    lOptionProvvAltro.setFilter(new String[]{"-", "01", "53"});    
    setRequestAttribute("tipoProvvedimentiAltro", "" + lOptionProvvAltro );
    
    String lPage = PG_LOAD_INSERISCISENTENZA_DUPLICATA;

    return lPage;
  }
}
