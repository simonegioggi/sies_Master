package siap.siep.reato.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>Title: ActLoadDettaglioReato</p>
 * <p>Description: Classe Action per la load dettaglio di Reato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadDettaglioReato extends ActionSiap implements ICostantiReato
{
  public String processRequest() throws Exception
  {
	gestioneRitorno();  
	
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_REATO);

    IReato lCtrl = SIEPLookupRemote.getReatoRemote();
    ReatoModel lReaMod = lCtrl.ExRicercaReatoByKey(lId);

    setRequestAttribute("reato", lReaMod);
    setSessionAttribute("reato", lReaMod);

    if(!this.isRequestAttributeNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione guidata
   {
     this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
   }

   // paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO SOGGETTO
   if(!this.isRequestParameterNullObj("lTipoFunzione"))
   {
    this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
   }


    return PG_LOAD_DETTAGLIOREATO;
  }
}