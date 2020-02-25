package siap.siep.richiesta.action;

import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActLoadInserisciRichiestaMSEstPenaLibCond   extends ActionSiap
                                              implements ICostantiRichiesta
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
   {
     return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
   }
   FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

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

    //ricerca esistenza almeno una  misura alternativa
     MisuraAlternativaModel lMisAlMod = new MisuraAlternativaModel();
     IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
     lMisAlMod = lMisAltCtrl.ExRicercaMisuraAlternativaByFascicoloOrdinanza(lFascMod.getIdFascicoloSiep());

//Controllo se è presente una misura alternativa con ultima data inserimento
     if(lMisAlMod==null || lMisAlMod.getIdMisuraAlternativa()==null)
     {
       throw new SIEPException(SIEPException.USER_MESSAGE, "Non è presente nessuna misura alternativa per questo fascicolo!");
     }
     else
     {
       //Sede Ufficio di Sorveglianza
      UfficioModel lUffEmiMod = new UfficioModel();
      IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
      lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lMisAlMod.getChiaveUfficioFascicoloSius());
      setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

      String lCodMag = lMisAlMod.getCodMagistrato();

      MagistratoModel lMagSorvMod = new MagistratoModel();
      IMagistrato lCtrlMagSorv = SICOLookupRemote.getMagistratoRemote();
      lMagSorvMod = lCtrlMagSorv.ExRicercaMagistratoByCod(lCodMag);
      setRequestAttribute("magistratosorveglianza", lMagSorvMod);
     }
     setRequestAttribute("misuraalternativa",lMisAlMod);

     return PG_LOAD_INSERISCI_RICHIESTA_MS_ESP_PENA_LIB_COND;
  }
}