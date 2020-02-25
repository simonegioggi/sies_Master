package siap.sige.fogliocomplementare.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sige.SIGEException;
import siap.sige.web.ActionSige;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

/**
* <p>Title: ActLoadAnnullaCFC</p>
* <p>Description: Classe Action per la load Annullamento Foglio Complementare</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class ActLoadAnnullaCFC extends ActionSige implements ICostantiFoglioComp
{
  public String processRequest() throws Exception
  {
    if (isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO))
    	throw new SIGEException(SIGEException.USER_MESSAGE,"Errore nei dati !");

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

    setRequestAttribute("nextAction", "siap.sige.fogliocomplementare.action.ActAnnullaCFC" );
    setRequestAttribute(CAMPO_ID_DOCUMENTO_ALLEGATO, lDocAll.getIdDocumentoAllegato().toString());
    setRequestAttribute(ICostantiEvento.CAMPO_ID_EVENTO, lIdEvento.toString());
    return PG_LOADANNULLAMENTOFOGLIOCOMP;
  }



}