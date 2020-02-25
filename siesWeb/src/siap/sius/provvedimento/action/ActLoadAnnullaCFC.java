package siap.sius.provvedimento.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.impugnazione.action.ICostantiImpugnazione;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

/**
* <p>Title: ActLoadAnnullaCFC</p>
* <p>Description: Classe Action per la load Annullamento Foglio Complementare</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadAnnullaCFC extends ActionSius
{
  public String processRequest() throws Exception
  {
    if (isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO))
      throw new SIUSException(SIUSException.USER_MESSAGE,"Errore nei dati !");

    // Questo parametro mi indica se oltre ad effettuare la cancellazione
    // del FC su SIES devo annullare lo stesso, se trasmesso, anche su NSC.
    String annullaNSC = "No";
    if (!isRequestParameterNullObj("tipoOperazione")){
    	if(this.getRequestStringParameter("tipoOperazione").equals("ANNULLANSC")){
    		annullaNSC = "Si";
    	} 
    }
    
    BigDecimal lIdEvento = getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO);

    // Ricerca del Documento Allegato da Annullare
    IDocumentoAllegato lDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
    DocumentoAllegatoModel lDocAll = null;
    lDocAll = lDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lIdEvento,"06");

    // Lock
    LockModel lck = LockController.lockIfNotLocked(getServletContext(),"DocumentoAllegato",lDocAll.getIdDocumentoAllegato().toString(),getCodUtenteConnesso(),getSession().getId());
    if (lck!=null)
    {
     setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Il  "+lck.getEntity()+" è in gestione ad un altro utente!<BR>Riprovare più tardi !");
     return IWebConstants.PG_MESSAGE;
    }

    //Poiche si utilizza la jsp di Annullamento Impugnazione si usano i nomi dei campi già definiti
    setRequestAttribute("nextAction", "siap.sius.provvedimento.action.ActAnnullaCFC&annullaNSC= " + annullaNSC );
    setRequestAttribute(ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE, lDocAll.getIdDocumentoAllegato().toString());
    setRequestAttribute(ICostantiEvento.CAMPO_ID_EVENTO, lIdEvento.toString());
    return ICostantiImpugnazione.PG_LOAD_ANNULLAMENTO_IMPUGNAZIONE;
  }



}