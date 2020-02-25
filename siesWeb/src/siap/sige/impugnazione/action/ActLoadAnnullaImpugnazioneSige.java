package siap.sige.impugnazione.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

/**
* <p>Title: ActLoadAnnullaImpugnazioneSige</p>
* <p>Description: Classe Action per la load Annullamento Impugnazione Sige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadAnnullaImpugnazioneSige extends ActionSige implements ICostantiImpugnazioneSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {

	IImpugnazioneSige lCtrl = SIGELookupRemote.getImpugnazioneSigeRemote();
    ImpugnazioneSigeModel lImpMod = null;

    if( !isRequestParameterNullObj(CAMPO_ID_IMPUGNAZIONE))
    {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( CAMPO_ID_IMPUGNAZIONE + ": " + getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
      lImpMod = lCtrl.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
    }else
    	throw new SIGEException(SIGEException.USER_MESSAGE,"Errore nei dati !");
    
    ProvvedimentoSigeEventoModel lProvvEveMod = new ProvvedimentoSigeEventoModel();
    BigDecimal IdFascSige = null;
    if( !isRequestParameterNullObj(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE)) {
    	BigDecimal idProvvSige = getRequestBigDecimalParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE);
    	// Recupero il Provvedimento Sige
        IProvvedimentoSige lProvvCtrl = SIGELookupRemote.getProvvedimentoRemote();
        lProvvEveMod = lProvvCtrl.ExRicercaProvvedimentoById(idProvvSige);
        if(lProvvEveMod != null && lProvvEveMod.getProvvedimento() != null && lProvvEveMod.getProvvedimento().getFasIdFascicoloSige() != null){
        	IdFascSige = lProvvEveMod.getProvvedimento().getFasIdFascicoloSige();
        }
    }
    
    // Lock
    LockModel lck = LockController.lockIfNotLocked(getServletContext(),"Impugnazione",lImpMod.getIdImpugnazioneSige().toString(),getCodUtenteConnesso(),getSession().getId());
    if (lck!=null)
    {
     setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Il/la  "+lck.getEntity()+" è in gestione ad un altro utente!<BR>Riprovare più tardi !");
     return IWebConstants.PG_MESSAGE;
    }

    setRequestAttribute("nextAction", "siap.sige.impugnazione.action.ActAnnullaImpugnazioneSige" );
    setRequestAttribute(CAMPO_ID_IMPUGNAZIONE, lImpMod.getIdImpugnazioneSige().toString());
    setRequestAttribute(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE, IdFascSige.toString());
    return PG_LOAD_ANNULLAMENTO_IMPUGNAZIONESIGE;
  }



}