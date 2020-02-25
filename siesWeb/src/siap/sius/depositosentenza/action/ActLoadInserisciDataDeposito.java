package siap.sius.depositosentenza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sius.depositodecreto.action.ActLoadInserisciDataDepositoDecreto;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

 /**
 * <p>Title: ActLoadInserisciDataDeposito</p>
 * <p>Description: Classe Action per la load inserisci di Data Deposito Sentenza</p>
 * <p>Company: Engineering S.p.A.</p>
 * @version 1.0
 */
 public class ActLoadInserisciDataDeposito extends ActLoadInserisciDataDepositoDecreto
 	implements ICostantiDepositoSentenza
 {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
   public ActLoadInserisciDataDeposito()
   {
	   // Reinizializzazione dell'attributo ereditato
	   mRetPage = PG_LOAD_INSERISCIDATADEPOSITO;
   }

   public ActLoadInserisciDataDeposito(String aRetPage)
   {
	   // Reinizializzazione dell'attributo ereditato
	   mRetPage = aRetPage;
   }


   public String processRequest() throws Exception
   {
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug( super.getClass().getName()+".processRequest() mRetPage: "+  mRetPage);

       return (super.processRequest());
   }

  // Ricerca del Deposito Sentenza dall'ID Evento
  public void ricercaProvvedimento(BigDecimal aIdEvento) throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( super.getClass().getName()+".ricercaProvvedimento: inizio");

    // Ricerca del Deposito Sentenza dall'ID Evento
    IDepositoSentenza lCtrl = SIUSLookupRemote.getDepositoSentenzaRemote();
    DepositoSentenzaModel lDepSenMod = lCtrl.ExRicercaDepositoSentenzaByEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

    if (lDepSenMod == null)
      throw new F3BException(F3BException.USER_MESSAGE, "Nessuna Sentenza emessa per il procedimento ");
    
    // Se la Sentenza non è già depositata bisogna lockare per evitare che 2 
    //utenti tentino di depositare contemporaneamente la stessa Sentenza.
    if(lDepSenMod.getNumSentenza() == null)
    {
	    // Lock
	    LockModel lck = LockController.lockIfNotLocked(getServletContext(), "DEPOSITO_SENTENZA", lDepSenMod.getIdDepositoSentenza().toString(), getCodUtenteConnesso(), getSession().getId());

	    if (lck!=null)
	    {
	      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il "+lck.getEntity()+" (ID: " + lck.getIdEntity()+ ") è in gestione ad un altro utente!<BR>Riprovare più tardi !" );
	      mRetPage =  IWebConstants.PG_MESSAGE;
	      return;
	    }
     }
     
    if (lDepSenMod.getDataDeposito() != null)
    {
      mIsDepositato = true;
      if (isRequestParameterNullObj("Aggiungi"))
      {
        // Se la Sentenza è già stata depositata
        IDocumentoAllegato lDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
        DocumentoAllegatoModel lDocAll = lDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO), "01");
        // Potrebbe capitare che il documento allegato non sia recuperabile. Luigi 9-1-2006
        if (lDocAll == null || lDocAll.getIdDocumentoAllegato() == null)
          throw new F3BException(F3BException.EX_NOT_FOUND, "Errore nella lettura del Documento Allegato per IDEvento -> "+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
        // Se il Documento allegato è validato
        // Si passa al dettaglio
        RedirectTo lPage = new RedirectTo();
        lPage.setPage(IWebConstants.PG_MAIN);
        lPage.setAction("siap.sius.depositosentenza.action.ActLoadDettaglioDataDepositoSentenza");
        lPage.setParameter(ICostantiDepositoSentenza.CAMPO_ID_DOCUMENTO_ALLEGATO, "" + lDocAll.getIdDocumentoAllegato());
        // Passaggio al dettaglio del LINK di ritorno
        if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
          lPage.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO));
        if (!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
         lPage.setParameter(IWebConstants.FLAG_RITORNO, getRequestStringParameter(IWebConstants.FLAG_RITORNO));

        mRetPage = lPage.toString();
      }
      else
        setLinkRitorno();
    }
    else
      setLinkRitorno();

    setRequestAttribute("lDepositoSentenza", lDepSenMod);
    setSessionAttribute("lDepositoSentenza", lDepSenMod);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( super.getClass().getName()+".ricercaProvvedimento: fine");

    return;
  }
 }

