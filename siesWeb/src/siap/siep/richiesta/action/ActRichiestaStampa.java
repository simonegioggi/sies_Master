package siap.siep.richiesta.action;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.io.ByteArrayOutputStream;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
//import siap.siep.richiesta.controller.RichiestaController;
import siap.siep.richiesta.controller.IRichiesta;
import siap.siep.richiesta.model.RichiestaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;

public class ActRichiestaStampa extends ActionSiap implements ICostantiRichiesta
{
  public String processRequest() throws F3BException
  {
    //RichiestaModel      lRichMod = new RichiestaModel();
    SoggettoModel       lSogMod = new SoggettoModel((SoggettoModel)getSessionAttribute("soggetto"));
    FascicoloSiepModel  lFasMod = new FascicoloSiepModel((FascicoloSiepModel)getSessionAttribute("fascicolo"));
    SentenzaModel       lSenMod = new SentenzaModel((SentenzaModel)getSessionAttribute("sentenza"));
    UtenteModel         lUteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

    //Model per conservare i campi inseriti all'interno della form
    RichiestaModel lDocMod = new RichiestaModel();

    lDocMod.setAutoritaDestinatario(getRequestStringParameter(ICostantiRichiesta.AUTORITA_DESTINATARIO));
    lDocMod.setAutoritaSede(getRequestStringParameter(ICostantiRichiesta.AUTORITA_SEDE));
    lDocMod.setCampoLibero(getRequestStringParameter(ICostantiRichiesta.CAMPO_LIBERO));
    lDocMod.setSedeSiep(lUteMod.getUfficioUtente().getDescrComune());
    lDocMod.setDataEmissione( getRequestDateParameter( ICostantiRichiesta.CAMPO_DATA_AAAA_EMISSIONE,
                                                       ICostantiRichiesta.CAMPO_DATA_MM_EMISSIONE  ,
                                                       ICostantiRichiesta.CAMPO_DATA_GG_EMISSIONE ) );

    TreeModel lTree  = new TreeModel(lDocMod);
    TreeModel lTree1 = new TreeModel(lSogMod );
    TreeModel lTree2 = new TreeModel(lSenMod);
    TreeModel lTree3 = new TreeModel(lFasMod);


    //Stabilisco le gerarchie
    lTree.add(lTree1);
    lTree.add(lTree2);
    lTree.add(lTree3);

    //RichiestaController lCtrl = new RichiestaController();
    IRichiesta lCtrl = SIEPLookupRemote.getRichiestaRemote();
    ByteArrayOutputStream lReport = (ByteArrayOutputStream)lCtrl.ExStampaRichiesta(lTree, ReportGenerator.RTF);

    /*
      String lReport = "";
      FascicoloSiepController lController = new FascicoloSiepController();
      FascicoloSiepModel lModel = new FascicoloSiepModel();
      lModel.setSogChiaveSoggetto(new BigDecimal("1"));
      lReport = lController.ExStampaFascicolo(lModel);
    */

    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}
