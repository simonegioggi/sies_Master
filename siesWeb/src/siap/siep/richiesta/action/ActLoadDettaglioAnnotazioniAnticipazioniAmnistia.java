package siap.siep.richiesta.action;

import java.util.Date;

import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.calcolopena.action.ActLoadDettaglioAnticipazioni;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 *
 * <p>Title: ActLoadDettaglioAnnotazioniAnticipazioniAmnistia</p>
 * <p>Description: Dettaglio Annotazioni Anticipazioni Amnistia</p>
 * <p> </p>
 * <p>Company: Bull Italia S.p.A.</p>
 */
public class ActLoadDettaglioAnnotazioniAnticipazioniAmnistia extends ActLoadDettaglioAnticipazioni
                                                              implements ICostantiAnnotazioneManuale
{
  public String processRequest() throws Exception
  {
    try
    {
      String lPageErr = loadDettaglio("0122", AMINISTIA_INDULTO, "RICH_AMNI");

      if (lPageErr != null)
        return lPageErr;
    }
    catch(F3BException ex)
    {
      if(ex.getErrorCode() == SIEPException.EX_NOT_FOUND)
      {
        this.isEventoNonValidato();

        // Non sono presenti annotazioni non validate. Prima di procedere
        // all'inserimento di una nuova annotazione verifico le condizioni di
        // calcolo della pena. Infatti se il calcolo non è abInizio, il soggetto
        // è libero e non esiste una pena validata, il calcolo partirà proprio
        // dalla pena non validata per cui si avverte l'utente di aggiornare il
        // calcolo pena.
        if ( isRequestParameterNullObj("fromRedirigi") )
        {
          String lMessage = checkCalcoloPena();
          if ( !lMessage.equals("") )
          {
            RedirectTo lRedirigi = new RedirectTo();
            lRedirigi.setPage(IWebConstants.PG_MAIN);
            setRequestAttribute(IWebConstants.MESSAGE_TEXT,  lMessage );
            lRedirigi.setAction( "siap.siep.richiesta.action.ActLoadRichiestaAmnistiaIndulto&fromRedirigi=true"  );
            setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
            return IWebConstants.PG_MESSAGE;
          }
          else
          {
            return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.richiesta.action.ActLoadRichiestaAmnistiaIndulto";
          }
        }
        else
        {
          return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.richiesta.action.ActLoadRichiestaAmnistiaIndulto";
        }
      }
      else
        throw ex;
    }

    if ( !isRequestParameterNullObj("dataScarcerazione") ) {
      String dataScarcerazioneStr = getRequestStringParameter("dataScarcerazione");
      Date dataScarcerazione = DateUtils.getDate(dataScarcerazioneStr,"dd/MM/yyyy");
      setRequestAttribute("dataScarcerazione",dataScarcerazione);
    }
    return PG_LOAD_DETTAGLIO_ANTICIPAZIONI;
  }
}