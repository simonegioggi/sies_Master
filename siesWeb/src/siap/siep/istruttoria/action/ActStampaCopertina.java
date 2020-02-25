package siap.siep.istruttoria.action;

import java.io.ByteArrayOutputStream;

import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoria.controller.IIstruttoria;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaCopertina</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaCopertina extends ActionSiap implements ICostantiIstruttoria
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();

//Arresti Domiciliari
    TemplateModel lTemMod = new TemplateModel();
    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();

    lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(null, null, "0302",null);

    IIstruttoria lCtrl = SIEPLookupRemote.getIstruttoriaRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento( lFascicoloModel, lTemMod.getIdTemplate(), lUtenteMod );

//Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}