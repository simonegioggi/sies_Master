package siap.siep.istruttoria.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 *
 * <p>Title: ActLoadInserisciCertificatoEsecuzione</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciCertificatoEsecuzione extends ActionSiap implements ICostantiIstruttoria
{
  public String processRequest() throws Exception
  {

    if (isSessionAttributeNullObj("fascicolo"))
    {
      String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
        "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
        ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" +
        "siap.siep.istruttoria.action.ActLoadInserisciCertificatoEsecuzione";

      return lPage;
    }
    this.isFascicoloSiepDiCompetenza();

    this.isEventoNonValidato();
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    
 // Se vengo da IstruttoriaCUMULO
    if(!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) )
    {
    	setRequestAttribute("IdIstruttoriaCumulo", getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
    }

    // Riempimento  ComboBoX
    //Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS());
    Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioCertStatoEsec());

    setRequestAttribute("autorita", "" + lOption);
    setRequestAttribute("datairrevocabilita", DateUtils.getDateToString(lFascicoloModel.getDataIrrevocabilita(), "dd/MM/yyyy"));

    return PG_LOAD_INSERISCI_CERTIFICATO_ESECUZIONE;
  }

}