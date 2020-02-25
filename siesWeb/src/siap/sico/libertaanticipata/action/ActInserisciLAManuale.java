package siap.sico.libertaanticipata.action;

/**
 * <p>Title: ActInserisciLAManuale</p>
 * <p>Description: Classe Action per l'inserimento della La da Forzatura</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 *
 * <p>Title: ActInserisciLAManuale</p>
 * <p>Description: ActInserisciLAManuale</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActInserisciLAManuale extends ActionSiap
                                           implements ICostantiLicenzaLibanticipata
{
  /**
   * Azione di Inserimento della LA da Forzatura
   * @return Nome della pagina JSP da visualizzare
   * al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    String lCodiceOperatore = this.getCodUtenteConnesso();
    String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

    /*
     ---------------------------------------------------------------------------------
      LIBERAZIONE ANTICIPATA
     ---------------------------------------------------------------------------------
     */
    LicenzaLibAnticipataModel lLibAntMod = new LicenzaLibAnticipataModel();

    if (!this.isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA))
    {
      lLibAntMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
      lLibAntMod.setFlagConcesso("C");
      lLibAntMod.setFlagElaborato("F"); // FLAG CHE IDICA LA FORZATURA
      lLibAntMod.setCodTipoLicenza("LA");

      BigDecimal lTotGiorni = getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA);

      lLibAntMod.setNumeroGiorni(lTotGiorni);
      lLibAntMod.setCodUfficioInserimento(lCodiceUfficio);
      lLibAntMod.setCodOperatoreInserimento(lCodiceOperatore);
      lLibAntMod.setDataInserimento(DateUtils.getSysDate());
    }

    ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
    lCtrlLib.ExInserisciLicenzaLibanticipata(lLibAntMod);

    // Preparo la pagina di ritorno
    RedirectTo lRedirigi = new RedirectTo();

    lRedirigi.setPage(IWebConstants.PG_MAIN);
    lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
    lRedirigi.setParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP,""+lFascicoloModel.getIdFascicoloSiep() );

    return lRedirigi.toString();
  }
}