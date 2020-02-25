package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActRicercaFSigeProvvedimenti </p>
 * <p>Description: Azione per la ricerca dei Provvedimenti
 * legati al fascicolo SIGE a partire da ANNO e PROGR del Fascicolo.
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: </p>
 * @version 1.0
 */

public class ActRicercaFSigeUdienze extends ActRicercaFSigePuntuale
  implements ICostantiProvvedimentoSige, ICostantiFascicoloSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  public String processRequest() throws Exception
  {
    super.processRequest();

    // Fascicolo Sige Esteso in sessione.
    FascicoloSigeEstesoModel lFasEsteso = this.getFascicoloSigeEstesoInSessione();

    // Si passa alla Action successiva per la ricerca.
    RedirectTo lPage = new RedirectTo();
    lPage.setPage(IWebConstants.PG_MAIN);
    lPage.setAction("siap.sige.provvedimento.action.ActRicercaOrdinanze");
    
    BigDecimal lIdFascicolo = lFasEsteso.getFascicoloSige().getIdFascicoloSige();
    
    IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
    //Vector <ProvvedimentoSigeEventoModel>lVect = mCtrl.ExRicercaProvvSigePerIdFasSigeTipiProvv(lIdFascicolo, TIPI_PROVVEDIMENTI_UDIENZE);
    Vector <ProvvedimentoSigeEventoModel>lVect = mCtrl.ExRicercaProvvedimentiUdienzeByIdFascicolo(lIdFascicolo);
    setRequestAttribute("provvedimenti", lVect);
    
    // Ricerca dell'eventuale prima impugnazione valida per ciascun provvedimento
  
	IImpugnazioneSige ctrIS = SIGELookupRemote.getImpugnazioneSigeRemote();
	Vector <ImpugnazioneSigeModel>impugnazioniProvvedimento = new Vector<ImpugnazioneSigeModel>();
	Vector <ImpugnazioneSigeModel>impugnazioniProvvedimenti = new Vector<ImpugnazioneSigeModel>();
    for  (ProvvedimentoSigeEventoModel lProvEve : lVect)
    {
      
      impugnazioniProvvedimento = ctrIS.ExRicercaImpugnazioniProvvedimentoSige(lProvEve.getProvvedimento().getIdProvvedimentoSige());
      if (impugnazioniProvvedimento != null && impugnazioniProvvedimento.size() >0)
    	  impugnazioniProvvedimenti.addElement(impugnazioniProvvedimento.firstElement());
    }
    setRequestAttribute("impugnazioni", impugnazioniProvvedimenti);
	// fine Ricerca   


    // Controlla se il fascicolo e' modificabile (in questo contesto la modificabilità va impostata per tutti
    // i fascicoli di competenza, tranne quelli con provvedimento definitorio con deposito validato).
    String lModificabile = "NO"; 

    FascicoloSigeUtils lFasUtil = new FascicoloSigeUtils();
    
    if (getCodUfficioUtenteConnesso().equalsIgnoreCase(lFasEsteso.getFascicoloSige().getChiaveUfficio() ) ) 
    {
    	if(lFasUtil.HasFascicoloSigeProvvDefinitorioConDepositoValidato(lIdFascicolo))
        lModificabile="NO";
    	else
        lModificabile="SI";
    }

    setRequestAttribute("isModificabile", lModificabile);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("ActRicercaProvvedimenti : fine");
    return PG_ELENCOPROVVEDIMENTI;
  }
}