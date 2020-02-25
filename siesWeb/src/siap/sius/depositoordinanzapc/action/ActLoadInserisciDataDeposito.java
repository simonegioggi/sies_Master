package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sius.depositodecreto.action.ActLoadInserisciDataDepositoDecreto;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

 /**
 * <p>Title: ActLoadInserisciDataDeposito</p>
 * <p>Description: Classe Action per la load inserisci di Data Deposito Ordinanza</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

 public class ActLoadInserisciDataDeposito extends ActLoadInserisciDataDepositoDecreto
 implements ICostantiDepositoOrdinanzaPc
 {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
   public ActLoadInserisciDataDeposito()
   {
     // Reinizializzazione dell'attributo ereditato
     mRetPage = PG_LOAD_INSERISCIDATADEPOSITO;
     //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     //siesLogger.debug( super.getClass().getName()+".ActLoadInserisciDataDeposito() mRetPage: "+  mRetPage);
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

  // Ricerca del Deposito Ordinanza dall'ID Evento
  public void ricercaProvvedimento(BigDecimal aIdEvento) throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( super.getClass().getName()+".ricercaProvvedimento: inizio");

    // Ricerca del Deposito Ordinanza dall'ID Evento
    IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
    DepositoOrdinanzaPcModel lDepOrdMod = lCtrl.
        ExRicercaDepositoOrdinanzaPcByEvento(getRequestBigDecimalParameter(
        ICostantiEvento.CAMPO_ID_EVENTO));

    if (lDepOrdMod == null)
      throw new F3BException(F3BException.USER_MESSAGE, "Nessuna Ordinanza emessa per il procedimento ");
    
    // Se l'Ordinanza non è già depositata bisogna lockare per evitare che 2 utenti tentino di depositare contemporaneamente la stessa Ordinanza.
    if(lDepOrdMod.getNumS3()== null )
    {
	    // Lock
	    LockModel lck = LockController.lockIfNotLocked(getServletContext(),"DEPOSITO_ORDINANZA_PC",lDepOrdMod.getIdDepositoOrdinanzaPc().toString(),getCodUtenteConnesso(),getSession().getId());

	    if (lck!=null)
	    {
	      setRequestAttribute(IWebConstants.MESSAGE_TEXT,   "Il "+lck.getEntity()+" (ID: " + lck.getIdEntity()+ ") è in gestione ad un altro utente!<BR>Riprovare più tardi !" );
	      mRetPage =  IWebConstants.PG_MESSAGE;
	      return;
	    }
     }
     
    if (lDepOrdMod.getDataDeposito() != null)
    {
      mIsDepositato = true;
      if (isRequestParameterNullObj("Aggiungi"))
      {
        // Se l'Ordinanza è già stata depositata
        IDocumentoAllegato lDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
        DocumentoAllegatoModel lDocAll = lDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO),"02");
        // Potrebbe capitare che il documento allegato non sia recuperabile. Luigi 9-1-2006
        if (lDocAll == null || lDocAll.getIdDocumentoAllegato() == null)
          throw new F3BException(F3BException.EX_NOT_FOUND, "Errore nella lettura del Documento Allegato per IDEvento -> "+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
        // Se il Documento allegato è validato
        // Si passa al dettaglio
        RedirectTo lPage = new RedirectTo();
        lPage.setPage(IWebConstants.PG_MAIN);
        lPage.setAction("siap.sius.depositoordinanzapc.action.ActLoadDettaglioDataDepositoOrdinanza");
        lPage.setParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ID_DOCUMENTO_ALLEGATO, "" + lDocAll.getIdDocumentoAllegato());
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

    setRequestAttribute("lDepositoOrdinanza", lDepOrdMod);
    setSessionAttribute("lDepositoOrdinanza", lDepOrdMod);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( super.getClass().getName()+".ricercaProvvedimento: fine");

    return;
  }
 }

