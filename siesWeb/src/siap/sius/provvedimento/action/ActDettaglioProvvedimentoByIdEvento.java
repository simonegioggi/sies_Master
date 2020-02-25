package siap.sius.provvedimento.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActDettaglioProvvedimentoByIdEvento</p>
* <p>Description: Classe Action per la ricerca del Provvedimento e del Procedimento SIUS legati ad un Evento</p>
* <p>Copyright: Copyright (c) 2005</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActDettaglioProvvedimentoByIdEvento extends ActionSiap implements ICostantiProvvedimento
{
  public String processRequest() throws F3BException
  {
    //Lettura dell'Evento e del FascicoloGPModel.
    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEvento = lCtrl.ExRicercaEventoByKey (lIdEvento);

    IFascicoloSius lCtrlFSius = SIUSLookupRemote.getFascicoloSiusRemote();
    FascicoloGPModel lFasGPMod = lCtrlFSius.ExRicercaFascicoloByKey(lEvento.getFasSiuIdFascicoloSius());

    //Metto in sessione il fascicolo SIUS per consentire le funzionalità annesse.
    setSessionAttribute("fascicoloSiusGP", lFasGPMod);
    
    // 24/04/2009 Metto in sessione il fascicolo SIEP da cui ha origine il Fascicolo SIUS.
    IFascicoloSiep lCtrlSIEP = SIEPLookupRemote.getFascicoloSiepRemote();
    if(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep()!=null)
    {
    	FascicoloSiepModel lFasSIEP = lCtrlSIEP.ExRicercaFascicoloByKeyNoError( lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
    	setSessionAttribute("fascicolo", lFasSIEP);
    } else
    	removeSessionAttribute("fascicolo");

    // Prepara la pagina di destinazione, puntando all'azione di dettaglio.
    /* Ordinanza e funzione dettaglio di posizione 1 */
    if (lEvento.getCodTipoProvvedimento().compareTo("03")==0 && lEvento.getCodEsito().compareTo("0603")!=0)
    {
      RedirectTo lPage = new RedirectTo();
      lPage.setPage(IWebConstants.PG_MAIN);
      lPage.setAction("siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza");
      lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,""+ lEvento.getIdEvento());
      if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
       lPage.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO));

      return "" + lPage;
    }
    /* Ordinanza di rinvio Udienza */
    else if (lEvento.getCodTipoProvvedimento().compareTo("03")==0 && lEvento.getCodEsito().compareTo("0603")==0)
    {
      RedirectTo lPage = new RedirectTo();
      lPage.setPage(IWebConstants.PG_MAIN);
      lPage.setAction("siap.sius.udienza.action.ActDettaglioVerbaleRinvioUdienza");
      lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,""+ lEvento.getIdEvento());
      if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
       lPage.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO));

      return "" + lPage;
    }
    /* Decreto  generale*/
    else if (lEvento.getCodTipoProvvedimento().compareTo("02")==0 && lEvento.getCodEsito().compareTo("0600")!=0)
    {
      RedirectTo lPage = new RedirectTo();
      lPage.setPage(IWebConstants.PG_MAIN);
      lPage.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito");
      lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,""+ lEvento.getIdEvento());
      if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
       lPage.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO));

      return "" + lPage;
    }
    /* Decreto di Unificazione */
    else if (lEvento.getCodTipoProvvedimento().compareTo("02")==0 && lEvento.getCodEsito().compareTo("0600")==0)
    {
      RedirectTo lPage = new RedirectTo();
      lPage.setPage(IWebConstants.PG_MAIN);
      lPage.setAction("siap.sius.decretounificazione.action.ActLoadDettaglioDecretoUnificazione");
      lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,""+ lEvento.getIdEvento());
      if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
       lPage.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO));

      return "" + lPage;
    }
    /* Unificazione da Verbale */
    else if (lEvento.getCodTipoProvvedimento().compareTo("14")==0 && lEvento.getCodEsito().compareTo("0600")==0)
    {
      RedirectTo lPage = new RedirectTo();
      lPage.setPage(IWebConstants.PG_MAIN);
      lPage.setAction("siap.sius.unificazione.action.ActLoadDettaglioUnificazione");
      lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,""+ lEvento.getIdEvento());
      if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
       lPage.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO));

      return "" + lPage;
    }
    else
    {
      RedirectTo lPage = new RedirectTo();
      lPage.setPage(IWebConstants.PG_MAIN);
      lPage.setAction("siap.sius.provvedimento.action.ActRicercaProvvedimenti");
      lPage.setParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS,""+ lEvento.getFasSiuIdFascicoloSius());
      if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
       lPage.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO));

      return "" + lPage;
    }

  }
}