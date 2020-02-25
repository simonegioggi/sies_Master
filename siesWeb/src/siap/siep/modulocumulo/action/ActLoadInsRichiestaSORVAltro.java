package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.siep.SIEPException;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load Inserimento e Modifica delle Richieste del PM 
 * al Magistrato di SORVEGLIANZA - Tipo Richiesta:	 Altro (cod = 014) - (Gestione Cumulo)
 * 
 * @author Intersistemi Italia S.p.A.
 */

public class ActLoadInsRichiestaSORVAltro extends ActionModuloCumulo implements ICostantiRichiestePmInCumulo
{
  public String processRequest() throws F3BException {

    IstruttoriaCumuloModel lIstrCumulo = super.getDatiIstruttoria();
    if (!ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstrCumulo.getFlagStato())){
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "L'istruttoria risulta chiusa. Non è possibile procedere all'emissione di ulteriori richieste");
      return IWebConstants.PG_MESSAGE;
    }    
    
    RichiestePmInCumuloModel lRicMod = null;

    String lModalita = "I"; //default inserimento
    if (!isRequestParameterNullObj("modalita")) 
      lModalita = getRequestStringParameter("modalita");
    
    if ("I".equals(lModalita)){
      // Inserimento      
    }
    else if ("M".equals(lModalita)){
      // Modifica 
      BigDecimal lIdRich = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);
      
      IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
      lRicMod = (RichiestePmInCumuloModel)lCtrlRich.ExRicercaRichiestePmInCumuloById(lIdRich);
      setRequestAttribute("RichiestaSORV", lRicMod);
      
      // Ricerca del Titolo collegato alla Richiesta (tramite tabelle di Relazione RICHPM_TITOLO_CUM )  
      TitoloCumulatoModel lTitoloMod = lCtrlRich.ExRicercaTitolo_ByRichiestaGE(lIdRich);
      setRequestAttribute("Titolo", lTitoloMod);

    }
    else {
      //Rilanciare Eccezione - Operazione non supportata
    	throw new SIEPException(SIEPException.USER_MESSAGE, "Modalità operazione sconosciuta. Impossibile eseguire la richiesta.");
    }

    setRequestAttribute("modalita", lModalita);
   
    return PG_INS_RICH_SORV_ALTRO;
  }
}
