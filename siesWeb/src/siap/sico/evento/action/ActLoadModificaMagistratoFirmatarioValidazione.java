package siap.sico.evento.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;

/**
 * <p>Title: ActLoadModificaMagistratoFirmatarioValidazione</p>
 * <p>Description: Classe Action Load per modificare il MAgistrato firmatario e la data Emissione in fase di validazione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadModificaMagistratoFirmatarioValidazione  extends ActionSiap 
                                                             implements ICostantiEvento
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws Exception
	{	
		// id dell'evento
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO );
		
		//ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

    setRequestAttribute("eventonotifica", lEveMod);

    // Modifica introdotta per far funzionare l'azione anche nel
    // caso venga richiamata da "Ricerche -> Provvedimenti non Validati"
    // Infatti selezionando questa azione dalla ricerca occorre ricaricare
    // il fascicolo siep in sessione
    if(    lEveMod != null 
        && lEveMod.getEvento() != null 
        && lEveMod.getEvento().getFasSieIdFascicoloSiep() != null )
    {
      BigDecimal lIdFascicolo = lEveMod.getEvento().getFasSieIdFascicoloSiep();
      IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
      FascicoloSiepModel lFasRet = lCtrlFasc.ExRicercaFascicoloByKeyNoError(lIdFascicolo);
      
      if (lFasRet != null)
      {          
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("lFasRet " + lFasRet);
  
        setRequestAttribute("fascicolo", lFasRet);
        setSessionAttribute("fascicolo", lFasRet);
      }
    }
		
		return PG_MODIFICA_MAGISTRATO_FIRMATARIO_VALIDAZIONE;
	}	
}