package siap.siepe.ricezioneatti.action;

import java.math.BigDecimal;
//import java.util.List;

import siap.jms.ICostantiJMS;
//import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
//import siap.jms.messaggio.controller.IMessaggio;
//import siap.jms.messaggio.model.MessaggioModel;
//import siap.jms.util.ParserMessage;
//import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.web.ActionSiap;
//import siap.sius.tenore.model.TenoreModel;
import siap.sius.SIUSException;
//import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActDettaglioAttoRicevuto</p>
 * <p>Description: Classe Azione di "switch" nel senso che questa classe viene chiamata
 * dal link del dettaglio ed in funzione del tipo di messaggio ricevuto chiama la classe
 * action di pertinenza. </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActDettaglioAttoRicevuto extends ActionSiap
implements ICostantiMessaggio, ICostantiDepositoOrdinanzaPc
{
  public String processRequest() throws Exception
  {
    String lRetPage = null;

    // gestioneRitorno();
    this.setLinkRitorno();

    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
    String lCodTipoOperazione = this.getRequestStringParameter(ICostantiMessaggio.CAMPO_TIPO_OPERAZIONE);

   if (lCodTipoOperazione.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_ORDINANZA))
   {
     // L'azione viene ridiretta al Dettaglio Ordinanza Ricevuta
     RedirectTo lRedirigi = new RedirectTo();
     lRedirigi.setPage(IWebConstants.PG_MAIN);
     lRedirigi.setAction("siap.sius.depositoordinanzapc.action.ActDettaglioOrdinanzaRicevuta");
     lRedirigi.setParameter(CAMPO_ID_MESSAGGIO, lIdMessage.toString());
     /*
     if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
       lRedirigi.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO));
    */
     lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "20");
     lRetPage = lRedirigi.toString();
   } else if (lCodTipoOperazione.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_SENTENZA))
   {
	     // L'azione viene ridiretta al Dettaglio Sentenza Ricevuta
	     RedirectTo lRedirigi = new RedirectTo();
	     lRedirigi.setPage(IWebConstants.PG_MAIN);
	     lRedirigi.setAction("siap.sius.depositosentenza.action.ActDettaglioSentenzaRicevuta");
	     lRedirigi.setParameter(CAMPO_ID_MESSAGGIO, lIdMessage.toString());
	     lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "20");
	     lRetPage = lRedirigi.toString();
   }
   else if (lCodTipoOperazione.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_DECRETO))
   {
     // L'azione viene ridiretta al Dettaglio Decreto Ricevuto
     RedirectTo lRedirigi = new RedirectTo();
     lRedirigi.setPage(IWebConstants.PG_MAIN);
     lRedirigi.setAction("siap.sius.depositodecreto.action.ActDettaglioDecretoRicevuto");
     lRedirigi.setParameter(CAMPO_ID_MESSAGGIO, lIdMessage.toString());
     /*
     if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
       lRedirigi.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO));
    */
     lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "20");
     lRetPage = lRedirigi.toString();
   }
   else if (lCodTipoOperazione.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_PROVVEDIMENTO))
   {
     // L'azione viene ridiretta al Dettaglio Provvedimento Ricevuto
     RedirectTo lRedirigi = new RedirectTo();
     lRedirigi.setPage(IWebConstants.PG_MAIN);
     lRedirigi.setAction("siap.siepe.ricezioneatti.action.ActDettaglioProvvedimentoRicevuto");
     lRedirigi.setParameter(CAMPO_ID_MESSAGGIO, lIdMessage.toString());
     /*
     if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
       lRedirigi.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO));
    */
     lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "20");
     lRetPage = lRedirigi.toString();
   }
   else if (lCodTipoOperazione.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_RICHIESTA_RELAZIONE))
   {
     // L'azione viene ridiretta al Dettaglio Provvedimento Ricevuto
     RedirectTo lRedirigi = new RedirectTo();
     lRedirigi.setPage(IWebConstants.PG_MAIN);
     lRedirigi.setAction("siap.siepe.ricezioneatti.action.ActDettaglioRichiestaRelRicevuta");
     lRedirigi.setParameter(CAMPO_ID_MESSAGGIO, lIdMessage.toString());
     /*
     if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
       lRedirigi.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO));
    */
     lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "20");
     lRetPage = lRedirigi.toString();
   }
   /*
   else if (lCodTipoOperazione.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_ATTIVITA))
   {
     RedirectTo lRedirigi = new RedirectTo();
     lRedirigi.setPage(IWebConstants.PG_MAIN);
     lRedirigi.setAction("siap.siepe.ricezioneatti.action.ActDettaglioAttivitaRicevuta");
     lRedirigi.setParameter(CAMPO_ID_MESSAGGIO, lIdMessage.toString());
     lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "20");
     lRetPage = lRedirigi.toString();
   }
   */
   else if (lCodTipoOperazione.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_RICHIESTA_UEPE))
   {
     RedirectTo lRedirigi = new RedirectTo();
     lRedirigi.setPage(IWebConstants.PG_MAIN);
     lRedirigi.setAction("siap.siepe.ricezioneatti.action.ActDettaglioRichiestaRicevuta");
     lRedirigi.setParameter(CAMPO_ID_MESSAGGIO, lIdMessage.toString());
     lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "20");
     lRetPage = lRedirigi.toString();
   }
   else if (lCodTipoOperazione.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_RELAZIONE_RICHIESTA_UEPE))
   {
     RedirectTo lRedirigi = new RedirectTo();
     lRedirigi.setPage(IWebConstants.PG_MAIN);
     lRedirigi.setAction("siap.siepe.ricezioneatti.action.ActDettaglioRelazioneRichiestaRicevuta");
     lRedirigi.setParameter(CAMPO_ID_MESSAGGIO, lIdMessage.toString());
     lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "20");
     lRetPage = lRedirigi.toString();
   }
   else if (lCodTipoOperazione.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_RELAZIONE_ATTIVITA))
   {
     RedirectTo lRedirigi = new RedirectTo();
     lRedirigi.setPage(IWebConstants.PG_MAIN);
     lRedirigi.setAction("siap.siepe.ricezioneatti.action.ActDettaglioRelazioneAttivitaRicevuta");
     lRedirigi.setParameter(CAMPO_ID_MESSAGGIO, lIdMessage.toString());
     lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "20");
     lRetPage = lRedirigi.toString();
   }
   else if (lCodTipoOperazione.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_ATTIVITA))
   {
     RedirectTo lRedirigi = new RedirectTo();
     lRedirigi.setPage(IWebConstants.PG_MAIN);
     lRedirigi.setAction("siap.siepe.ricezioneatti.action.ActDettaglioAttivitaRicevuta");
     lRedirigi.setParameter(CAMPO_ID_MESSAGGIO, lIdMessage.toString());
     lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "20");
     lRetPage = lRedirigi.toString();
   }
   else
     throw new SIUSException( SIUSException.USER_MESSAGE, "Tipo di Atto non gestito !" );

    return lRetPage;
  }
}
