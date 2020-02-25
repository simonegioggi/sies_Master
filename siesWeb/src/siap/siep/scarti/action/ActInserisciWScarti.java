package siap.siep.scarti.action;


/**
* <p>Title: ActInserisciWScarti</p>
* <p>Description: Classe Action per l'inserimento di WScarti</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import siap.siep.scarti.controller.IWScarti;
import siap.siep.scarti.model.WScartiModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciWScarti extends ActionSiap implements ICostantiWScarti
{
/**
* Azione di Inserimento del WScarti
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
 		 {
 		 WScartiModel lWScMod = new WScartiModel();

		 lWScMod.setIdScarti( getRequestBigDecimalParameter( CAMPO_ID_SCARTI) );
		 lWScMod.setTabella( getRequestStringParameter( CAMPO_TABELLA) );
		 lWScMod.setAnnRes( getRequestBigDecimalParameter( CAMPO_ANN_RES) );
		 lWScMod.setNumRes( getRequestStringParameter( CAMPO_NUM_RES) );
		 lWScMod.setLetRes( getRequestStringParameter( CAMPO_LET_RES) );
		 lWScMod.setChiaveAlternativa( getRequestStringParameter( CAMPO_CHIAVE_ALTERNATIVA) );
		 lWScMod.setNoteScarto( getRequestStringParameter( CAMPO_NOTE_SCARTO) );
		 lWScMod.setCausaScarto( getRequestStringParameter( CAMPO_CAUSA_SCARTO) );

		 //---Aggiungere in SIEPLookupRemote il metodo getWScartiRemote()

		 IWScarti lCtrl = SIEPLookupRemote.getWScartiRemote();
		 WScartiModel llWScModRet = lCtrl.ExInserisciWScarti(lWScMod);		 // setta la risposta nella request
		 setRequestAttribute("wscarti", llWScModRet);
		  //Prepara la pagina di destinazione
		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.scarti.action.ActLoadDettaglioWScarti&"+CAMPO_ID_SCARTI+"="+llWScModRet.getIdScarti().toString();
		 return lPage;
	 }



}