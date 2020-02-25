package siap.siep.richiesta.action;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

 
 import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActLoadRichiestaStampa extends ActionSiap implements ICostantiRichiesta
{
 public String processRequest() throws F3BException
  {

    if( isSessionAttributeNullObj("fascicolo") )
      {
         RedirectTo lRedirigi = new RedirectTo();
         lRedirigi.setPage( IWebConstants.PG_MAIN );
         lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicolo" );
         setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
         throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare un procedimento." );
      }

    if( isSessionAttributeNullObj("soggetto") && isSessionAttributeNullObj("sentenza") )
      throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare il soggetto e la sentenza." );

    if( isSessionAttributeNullObj("soggetto") )
      {
         RedirectTo lRedirigi = new RedirectTo();
         lRedirigi.setPage( IWebConstants.PG_MAIN );
         lRedirigi.setAction("siap.sico.soggetto.action.ActLoadRicercaSoggetto" );
         setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
         throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare un soggetto." );
      }

    if( isSessionAttributeNullObj("sentenza") )
      {
         RedirectTo lRedirigi = new RedirectTo();
         lRedirigi.setPage( IWebConstants.PG_MAIN );
         lRedirigi.setAction("siap.siep.sentenza.action.ActLoadRicercaSentenza" );
         setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
         throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare una sentenza." );
      }

    setRequestAttribute("soggettoSiep",(SoggettoModel)getSessionAttribute("soggetto"));
    setRequestAttribute("sentenzaSiep",(SentenzaModel)getSessionAttribute("sentenza"));
    setRequestAttribute("fascicoloSiep",(FascicoloSiepModel)getSessionAttribute("fascicolo"));

		 return PG_LOAD_RICHIESTA_STAMPA;  //restituisce la jsp di VIEW
  }

}
