package siap.sige.detenzione.action;

/**
 * <p>Title: ActLoadInseriscietenzioneFasSige</p>
 * <p>Description: Classe Action per la modifica del Luogo detenzione x fascicolo SIGE</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: </p>
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sige.detenzione.controller.IFasSigeDetenzione;
import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;

public class ActLoadModificaDetenzioneFasSige extends ActionSiap implements ICostantiFasSigeDetenzione
{
  public String processRequest() throws Exception
  {
    // punto di Ritorno
    gestioneRitorno();

    FascicoloSigeEstesoModel lEstesoModel = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");
    BigDecimal lIdFascicolo = lEstesoModel.getFascicoloSige().getIdFascicoloSige();

    IFasSigeDetenzione lCtrl = SIGELookupRemote.getFasSigeDetenzioneRemote();
    FasSigeDetenzioneModel lFasSigeDet = lCtrl.ExRicercaUltimaDetenzioneFascicolo(lIdFascicolo);
    setRequestAttribute("fasSigeDetCorrente", lFasSigeDet);

    setRequestAttribute("modalita", "M");

    return PG_LOAD_INSERISCIFASSIGEDETENZIONE;  //restituisce la jsp di VIEW
  }
}