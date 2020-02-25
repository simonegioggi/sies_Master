package siap.sico.libertaanticipata.action;

/**
 * <p>Title: ActLoadInserisciLAManuale</p>
 * <p>Description: Classe Action per la load inserimento Liberazione Anticipata Manuale</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActLoadInserisciLAManuale extends ActionSiap
                                       implements ICostantiLicenzaLibanticipata
{
  public String processRequest() throws Exception
  {
//Controllo Presenza del Fascicolo in Sessione
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    this.isFascicoloSiepDiCompetenza();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

//Controllo Validazione Fascicolo
    if (lFascMod.getFlagValidato().equalsIgnoreCase("N"))
    {
      RedirectTo lRedirigi = new RedirectTo();

      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

//Controllo Fascicolo definito
    if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
    {
      RedirectTo lRedirigi = new RedirectTo();

      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

    //this.isEventoNonValidato();

    ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
    int lTotGiorni = lCtrlLib.ExTotalePeriodiConcessiElaboratiByIdFascicoloSiep(lIdFascicolo);

    setRequestAttribute("lTotGiorniConcessi",""+lTotGiorni);

    return PG_LOAD_INSERISCI_LIBERAZIONE_ANTICIPATA_MANUALE;
  }
}
