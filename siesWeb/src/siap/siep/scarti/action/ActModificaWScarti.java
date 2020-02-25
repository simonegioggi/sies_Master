package siap.siep.scarti.action;


/**
* <p>Title: ActModificaWScarti</p>
* <p>Description: Classe Action per la modifica di WScarti</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.scarti.controller.IWScarti;
import siap.siep.scarti.model.WScartiModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaWScarti extends ActionSiap implements ICostantiWScarti
{
/**
* Azione di Modifica del WScarti
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
{

 		 String lId = getRequestStringParameter(CAMPO_ID_SCARTI);
 		 // riempie il model
 		 WScartiModel lWScMod = new WScartiModel ();

 		 lWScMod.setIdScarti(new BigDecimal(lId));
		 lWScMod.setIdScarti( getRequestBigDecimalParameter( CAMPO_ID_SCARTI) );
		 lWScMod.setTabella( getRequestStringParameter( CAMPO_TABELLA) );
		 lWScMod.setAnnRes( getRequestBigDecimalParameter( CAMPO_ANN_RES) );
		 lWScMod.setNumRes( getRequestStringParameter( CAMPO_NUM_RES) );
		 lWScMod.setLetRes( getRequestStringParameter( CAMPO_LET_RES) );
		 lWScMod.setChiaveAlternativa( getRequestStringParameter( CAMPO_CHIAVE_ALTERNATIVA) );
		 lWScMod.setNoteScarto( getRequestStringParameter( CAMPO_NOTE_SCARTO) );
		 lWScMod.setCausaScarto( getRequestStringParameter( CAMPO_CAUSA_SCARTO) );

		 // chiama il controller
		 IWScarti lCtrl = SIEPLookupRemote.getWScartiRemote();
		 WScartiModel llWScModRet = lCtrl.ExModificaWScarti(lWScMod);

		 setRequestAttribute("modalita", "M");
		 setRequestAttribute("wscarti", llWScModRet);

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.scarti.action.ActLoadDettaglioWScarti&"+CAMPO_ID_SCARTI+"="+llWScModRet.getIdScarti().toString();
		 return lPage;
	 }



}