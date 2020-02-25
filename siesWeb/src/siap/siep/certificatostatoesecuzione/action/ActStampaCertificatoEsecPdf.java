package siap.siep.certificatostatoesecuzione.action;

import java.io.ByteArrayOutputStream;

import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.certificatostatoesecuzione.controller.ICertificatoStatoEsec;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaCertificatoEsec</p>
 * <p>Description: La classe invoca il metodo per la stampa del Certificato di esecuzione in formato PDF.
 * Contestualmente viene inserito un record sulla tabella contenente le informazioni
 * sull'operazione e il file che viene prodotto</p>
 *
 */

public class ActStampaCertificatoEsecPdf extends ActionSiap implements ICostantiCertificatoStatoEsec
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();

    TemplateModel lTemMod = new TemplateModel();
    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();

    lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(null, null, "0303",null);

    ICertificatoStatoEsec lCtrl = SIEPLookupRemote.getCertificatoStatoEsecRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumentoPDF(lFascicoloModel, lTemMod.getIdTemplate(), lUtenteMod);

    //Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD_PDF;    
  }
}