package siap.sico.utente.action;


/**
* <p>Title: ActModificaUtente</p>
* <p>Description: Classe Action per la modifica di Utente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.utente.controller.IUtente;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActForzaPassword extends ActionSiap implements ICostantiUtente
{
/**
* Azione di Modifica del Utente
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
{
    String lPage=new String();
 		 String lId = getRequestStringParameter(CAMPO_COD_UTENTE);
                  UtenteModel lUt=new UtenteModel();
                  lUt.setUserId(lId);
                  lUt.setPwd(lId);
                  IUtente lIut=SICOLookupRemote.getUtenteRemote();
                  lIut.ExModificaPassword(lUt);

 		lPage = IWebConstants.PG_MESSAGE;
                 setRequestAttribute(IWebConstants.MESSAGE_TEXT,"La password per l'utente <u>"+lUt.getUserId()+"</u> è stata reimpostata");
                 //Prepara la "pagina" di destinazione
                 RedirectTo lRedirigi = new RedirectTo();
                 lRedirigi.setPage( IWebConstants.PG_MAIN );
                 lRedirigi.setAction( "siap.sico.utente.action.ActListaUtentiAttivi" );
                 setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
                 return lPage;
	 }



}