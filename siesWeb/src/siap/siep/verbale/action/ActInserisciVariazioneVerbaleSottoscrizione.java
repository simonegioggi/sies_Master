package siap.siep.verbale.action;

/**
* <p>Title: ActInserisciVariazioneVerbaleSottoscrizione</p>
* <p>Description: Classe Action per la varizione del Verbale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleDataInizioModel;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciVariazioneVerbaleSottoscrizione extends ActionSiap
                                               implements ICostantiVerbale, ICostantiMisuraAlternativa
{
 /**
  * Azione di Inserimento del Verbale
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws Exception
  {
     FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
     VerbaleModel lVerMod = new VerbaleModel();

 // - - - - - - > Ricerca del Verbale per la Variazione :
 // Potrebbe non esssere presente il verbale ma solo il provvedimento 

     VerbaleDataInizioModel lVerDatMod = new VerbaleDataInizioModel();
     IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
     lVerDatMod = lCtrl.ExRicercaVerbaleByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
     
     lVerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
     lVerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
     lVerMod.setDataInserimento(DateUtils.getSysDate());
     
     if(!this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_NOTE))
     {
    	 lVerMod.setNote(getRequestStringParameter(ICostantiVerbale.CAMPO_NOTE));
     }

 	 lVerMod.setDataPervenimento(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO,
                ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO,
                ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO)); 
 		
 	 lVerMod.setDataEmissione(getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA,
	    		 ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA,
	    		 ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA));

 	  EventoModel lEveMod = new EventoModel();
	  IVerbale lCtrl1 = SIEPLookupRemote.getVerbaleRemote();
	  lEveMod = lCtrl1.ExInserisciVariazioneVerbaleSottoscrizione(lFascMod.getIdFascicoloSiep(), lVerMod);
	  
// AMBROSINO 08/2010 -  Dettaglio provvedimento = NO perchè l'operazione dovrà poi continuare 
//						e non fermarsi al dettaglio	  

	  setRequestAttribute("evento", lEveMod); // Annotazione
	  String NoDett="NO";
	  setRequestAttribute("dettaglioProvvedimento", NoDett);
	  String lPage = "";
	  
	  if (lVerDatMod.getIdVerbale() != null)
	  {	  
		  	setRequestAttribute("verbale", lVerDatMod);
	  	  	//Prepara la pagina di destinazione
	  		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.verbale.action.ActLoadDettaglioVariazioneVerbaleSottoscrizione&"+CAMPO_ID_VERBALE+"="+lVerDatMod.getIdVerbale().toString()+"&IdEvento="+lEveMod.getIdEvento().toString();
	  }
	  else
	  {	  
		  lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.verbale.action.ActLoadDettaglioVariazioneVerbaleSottoscrizione&IdEvento="+lEveMod.getIdEvento().toString(); 
	  }
  
     return lPage;
  }
}