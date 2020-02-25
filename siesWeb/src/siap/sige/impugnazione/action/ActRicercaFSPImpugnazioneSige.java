package siap.sige.impugnazione.action;

/**
* <p>Title: ActRicercaFSPImpugnazioneSige</p>
* <p>Description: Classe Action per la ricerca puntuale del Fascicolo SIGE finalizzata alla trasmissione atti</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActRicercaFSPImpugnazioneSige extends ActRicercaFSigePuntuale implements ICostantiImpugnazioneSige
{
  public String processRequest() throws Exception
  {
    super.processRequest();
    setLinkRitorno();

    String lCodTipoImpugnazione = "";
    
    // Recupero del Sige Esteso in sessione. Se non in sessione solleva un errore di eccezione.
    if( isSessionAttributeNullObj("FascicoloSigeEsteso") )
      throw new SIGEException( SIGEException.USER_MESSAGE, "Procedimento non selezionato" );
    FascicoloSigeEstesoModel lFasEst = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
    
    
    if(!isSessionAttributeNullObj("codTipoImpugnazione"))
    	lCodTipoImpugnazione = (String) getSessionAttribute("codTipoImpugnazione");

    // Verifica esistenza di un provvedimento impugnato per quel procedimento.
    IImpugnazioneSige lCtrlImp = SIGELookupRemote.getImpugnazioneSigeRemote();

    Vector <ImpugnazioneSigeModel>lImpugnazioni = lCtrlImp.ExRicercaImpugnazioniFascicoloSige(lFasEst.getFascicoloSige().getIdFascicoloSige(), lCodTipoImpugnazione );
    
    // Recupero dei provvedimenti per il fascicolo.
    ProvvedimentoSigeModel lProvSige = new ProvvedimentoSigeModel();
    lProvSige.setFasIdFascicoloSige(lFasEst.getFascicoloSige().getIdFascicoloSige());
    IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
    
    Vector <ProvvedimentoSigeEventoModel>lVect = mCtrl.ExRicercaProvvedimentiSigePerOpposizioni(lFasEst.getFascicoloSige().getIdFascicoloSige());

    BigDecimal countImpugnazioni = new BigDecimal ( lImpugnazioni.size());
    String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
    setRequestAttribute("tipoUfficio", strCodTipoUfficio);

    setRequestAttribute("provvedimenti", lVect);  // 16/11/2007
    setRequestAttribute("impugnazioni", lImpugnazioni);  // 29/10/2007
    //setRequestAttribute("flag_Impugnato", flagImpugnato);
    setRequestAttribute("numero_Impugnazioni", countImpugnazioni.toString());
    setRequestAttribute("flag_valida", "NO");
    RedirectTo redirect = new RedirectTo();
    redirect.setPage(IWebConstants.PG_MAIN);
    redirect.setAction("siap.sige.impugnazione.action.ActLoadGrigliaMenuImpugnazioni");
    //return PG_ELENCOPROVVEDIMENTISIGEXIMPUGNAZIONE;
    return redirect.toString();
  }
}