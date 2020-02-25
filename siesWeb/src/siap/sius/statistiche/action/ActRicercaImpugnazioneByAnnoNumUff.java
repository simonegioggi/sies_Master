package siap.sius.statistiche.action;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sius.depositoordinanzapc.action.ActRicercaDepositoOrdinanzaPcByAnnoNumUff;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.impugnazione.action.ICostantiImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActRicercaImpugnazioneByAnnoNumUff</p>
* <p>Description: Classe Action per la ricerca del Ricorso/Impugnazione 
* da Anno, Num e codice Ufficio di Inserimento.</p>
* <p>La classe è stata ottenuta specializzando la ActRicercaDepositoOrdinanzaPc 
* per riutilizzare la stessa funzione per la ricerca dei dati da inserire in session.</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActRicercaImpugnazioneByAnnoNumUff extends ActRicercaDepositoOrdinanzaPcByAnnoNumUff 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    RedirectTo lRedirectTo = null;
    
    // Valorizzazione del model Impugnazione per la ricerca
    ImpugnazioneModel lImpugnazione = new ImpugnazioneModel();
    lImpugnazione.setAnnoS7( getRequestBigDecimalParameter( ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_S3) );
    lImpugnazione.setProgrS7(getRequestBigDecimalParameter( ICostantiDepositoOrdinanzaPc.CAMPO_NUM_S3) );
    lImpugnazione.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    
    EveFasGepSogProvModel lRisultatoModel = null;
     
    // Ricerca 
    IStatisticheSius lCtrl = SIUSLookupRemote.getStatisticheSiusRemote();
    lRisultatoModel = lCtrl.ExRicercaImpugnazioneByAnnoNumUfficio(lImpugnazione);
      
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Impugnazione: " + lImpugnazione);
     
    if (lRisultatoModel.getImpugnazione() != null && lRisultatoModel.getEvento()  != null && lRisultatoModel.getEvento().getIdEvento() != null && lRisultatoModel.getImpugnazione().getIdImpugnazione() != null )
    {
      inserimentoDatiInSessione(lRisultatoModel.getEvento().getIdEvento());
     
      // Costruzione della pagina di redirect
      lRedirectTo = new RedirectTo();
      lRedirectTo.setPage(IWebConstants.PG_MAIN);
      lRedirectTo.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lRisultatoModel.getEvento().getIdEvento().toString() );
      lRedirectTo.setParameter( ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE, lRisultatoModel.getImpugnazione().getIdImpugnazione().toString() );
     
      // Dettaglio Impugnazione/Opposizione
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("TipoImpugnazione = "+lRisultatoModel.getImpugnazione().getCodTipoImpugnazione());
      if ("04".equals(lRisultatoModel.getImpugnazione().getCodTipoImpugnazione())) {
        lRedirectTo.setAction("siap.sius.impugnazione.action.ActLoadDettaglioOpposizione");
      }
      else {
        lRedirectTo.setAction("siap.sius.impugnazione.action.ActLoadDettaglioImpugnazione");
      }
      
      // Passaggio dei parametri inerenti il bottone di ritorno
      if(!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
        lRedirectTo.setParameter( IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO) );
      else if(!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
        lRedirectTo.setParameter( IWebConstants.FLAG_RITORNO, getRequestStringParameter(IWebConstants.FLAG_RITORNO) );
    }
    else
      throw new F3BException (F3BException.USER_MESSAGE, "Errore nei dati !");
     
     
    return lRedirectTo.toString();
  }

  
}