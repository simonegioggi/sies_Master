package siap.siep.reato.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadModificaReato</p>
 * <p>Description: Classe Action per la load modifica di Reato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
public class ActLoadModificaReato extends ActionSiap implements ICostantiReato
{
  public String processRequest() throws Exception
  {
    //Controllo che non si stia lavorando su una entità in modifica ad altri
    LockModel lck = lockIfNotLocked("reato", getRequestStringParameter(CAMPO_ID_REATO), getCodUtenteConnesso());
    if (lck != null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il " + lck.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
      return IWebConstants.PG_MESSAGE;
    }

    ReatoModel lMod = new ReatoModel();
    boolean proceed = true;
    if (isFascicoloArchiviatoDefinito())
    {
      proceed = false;
      // setta la risposta nella request
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Impossibile Aggiungere Circostanze Ai Capi di Imputazione");
    }

    if (!isFascicoloNonValidato())
    {
      proceed = false;
      // setta la risposta nella request
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il fascicolo è stato validato! Impossibile Aggiungere Circostanze Ai Capi di Imputazione");
    }

    if (proceed)
    {
      lMod.setIdReato(getRequestBigDecimalParameter(ICostantiReato.CAMPO_ID_REATO));

      IReato lCtrl = SIEPLookupRemote.getReatoRemote();
      lMod = lCtrl.ExRicercaReatoByKey(lMod.getIdReato());

      setRequestAttribute("reato", lMod);

      Option lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato(), lMod.getCodFonte());
      setRequestAttribute("TipiFontiReato", "" + lOption);

      lOption = new Option(DecodificheManager.getInstance().getSottonumerazione(), lMod.getCodSottonumerazione());
      setRequestAttribute("TipiSottonumerazione", "" + lOption);
      
      lOption = new Option(DecodificheManager.getInstance().getTipoReato(), lMod.getCodTipoReato());
      setRequestAttribute("TipiReato", "" + lOption);

      lOption = new Option(DecodificheManager.getInstance().getPeriodoConsumazione(), lMod.getCodPeriodoConsumazione());
      setRequestAttribute("PeriodoConsumazione", "" + lOption);

      //**************************************************************************************************
      //Federica - a9-rr-078
      //aggiunto campo Comma-Qualificante 
      lOption = new Option(DecodificheManager.getInstance().getSottonumerazione(), lMod.getCommaQualificante());
      setRequestAttribute("TipiCommaQualificante", "" + lOption);
      //**************************************************************************************************
      
      setRequestAttribute("modalita", "M");

      return PG_LOAD_MODIFICAREATO; //restituisce la jsp di VIEW
    }
    else
    {
      //Prepara la "pagina" di destinAction
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" + ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" +
        ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
    }
  }
}