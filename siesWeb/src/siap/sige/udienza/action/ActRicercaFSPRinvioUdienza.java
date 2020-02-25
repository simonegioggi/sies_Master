package siap.sige.udienza.action;

import java.math.BigDecimal;

import siap.sige.documentoallegato.controller.IDocumentoAllegato;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


public class ActRicercaFSPRinvioUdienza extends ActRicercaFSigePuntuale implements ICostantiUdienzaProcedimentoSige {
  public String processRequest() throws Exception {
    

    //  Passando il parametro noQuery non effettua nuovamente la ricerca
    if( isRequestParameterNullObj( "noQuery") )
      super.processRequest();

    // Bottone di ritorno
    setLinkRitorno();
    checkDecretiFissazioneUdienzaValidati();
    RedirectTo rect=this.forward();
    return rect.toString();
  }
  
  public void checkDecretiFissazioneUdienzaValidati () throws F3BException {
	  FascicoloSigeEstesoModel fasEsteso = getFascicoloSigeEstesoInSessione();
	  UdienzaProcedimentoSigeModel udm = fasEsteso.getUdienzaProcedimento();
	  
	  if (udm == null)
		  throw new F3BException(F3BException.USER_MESSAGE,"Rinvio Udienza non consentito. Per il procedimento non risulta fissata una precedente Udienza.");  
		
	  
	  BigDecimal idFascicolo=fasEsteso.getFascicoloSige().getIdFascicoloSige();
	  IDocumentoAllegato ctrlDoc=SIGELookupRemote.getDocumentoAllegatoController();
	  BigDecimal numNonValidati=ctrlDoc.countDecretiDepositoFissazioneUdienzaNonValidati(idFascicolo);
	  
	  if (numNonValidati.intValue() > 0)
	      throw new F3BException(F3BException.USER_MESSAGE,"Esiste un evento NON validato. Validarlo o cancellarlo e rieseguire la funzione.");
	  
	  //BigDecimal numValidati=ctrlDoc.countDecretiDepositoFissazioneUdienzaValidati(idFascicolo);
	  
	  //IUdienzaProcedimentoSige ctrlUdi = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
	  //BigDecimal numUdienze = ctrlUdi.countUdienzeByIdFascicolo(idFascicolo);
	  
	  //if (numUdienze.intValue() > numValidati.intValue())
	  //	  throw new F3BException(F3BException.USER_MESSAGE,"Esiste un evento NON validato. Validarlo o cancellarlo e rieseguire la funzione.");
  }
  
  private RedirectTo forward () throws F3BException{
	  RedirectTo rect=new RedirectTo();
	  rect.setPage(IWebConstants.PG_MAIN);
	  rect.setAction("siap.sige.udienzaprocedimento.action.ActLoadInserisciOrdinanzaRinvioUdienza");
	  FascicoloSigeEstesoModel fasEsteso = getFascicoloSigeEstesoInSessione();
	  IProvvedimentoSige ctrlProvv=SIGELookupRemote.getProvvedimentoRemote();
	  ProvvedimentoSigeModel provv=ctrlProvv.ExRicercaOrdinanzaRinvioUdienzaDaValidareByFascicolo (fasEsteso.getFascicoloSige().getIdFascicoloSige());

	  if (provv != null) {
		  rect.setAction("siap.sige.udienzaprocedimento.action.ActLoadDettaglioOrdinanzaRinvioUdienza");
		  rect.setParameter("IdEvento", provv.getIdEventoGenerato().toString());
		  rect.setParameter("TornaQui", "0");
	  }			  
	  return rect;
  }
}
