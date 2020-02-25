package siap.siep.istruttoria.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 *
 * <p>Title: ActLoadCopertinaFasc</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadCopertinaFasc extends ActionSiap implements ICostantiIstruttoria
{
  public String processRequest() throws Exception
  {

    if (this.isSessionAttributeNullObj("fascicolo"))
  {
    return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
  }
  FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

/* --- a6-rr-088 levato controllo sulla validazione
  if (lFascMod.getFlagValidato().equalsIgnoreCase("N"))
  {
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage(IWebConstants.PG_MAIN);
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                        "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
    lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&" +
                       ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
    setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
    return IWebConstants.PG_MESSAGE;
  }
*/

      this.isFascicoloSiepDiCompetenza();

  if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
  {
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage(IWebConstants.PG_MAIN);
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                        "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " Il fascicolo risulta Definito. Impossibile procedere!");
    lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                        ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
    setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
    return IWebConstants.PG_MESSAGE;
  }


    return PG_LOAD_COPERTINA_FASCICOLO;
  }

}