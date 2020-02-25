package siap.sico.profilo.action;


/**
* <p>Title: ActInserisciProfilo</p>
* <p>Description: Classe Action per l'inserimento di Profilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.profilo.controller.IProfilo;
import siap.sico.profilo.model.ProfiloModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciProfilo extends ActionSiap implements ICostantiProfilo
{
/**
* Azione di Inserimento del Profilo
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
 		 {
 		 ProfiloModel lProMod = new ProfiloModel();

		 lProMod.setCodProfilo( getRequestBigDecimalParameter( CAMPO_COD_PROFILO) );
		 lProMod.setDescrizione( getRequestStringParameter( CAMPO_DESCRIZIONE) );
		 lProMod.setDataFineValidita( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_VALIDITA,CAMPO_MESE_DATA_FINE_VALIDITA,CAMPO_GIORNO_DATA_FINE_VALIDITA) );

		 //---Aggiungere in SICOLookupRemote il metodo getProfiloRemote()

		 IProfilo lCtrl = SICOLookupRemote.getProfiloRemote();
		 ProfiloModel llProModRet = lCtrl.ExInserisciProfilo(lProMod);		 // setta la risposta nella request
		 setRequestAttribute("profilo", llProModRet);
		  //Prepara la pagina di destinazione
		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.profilo.action.ActLoadDettaglioProfilo&"+CAMPO_COD_PROFILO+"="+llProModRet.getCodProfilo().toString();
		 return lPage;
	 }



}