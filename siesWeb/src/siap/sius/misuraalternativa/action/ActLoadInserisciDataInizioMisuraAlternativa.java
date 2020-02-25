package siap.sius.misuraalternativa.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisuraalternativa.controller.IEsecuzioneMA;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

 /**
 * <p>Title: ActLoadInserisciDataInizioMisuraAlternativa</p>
 * <p>Description: Classe Action per la load inserisci di Data Deposito Decreto</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

 public class ActLoadInserisciDataInizioMisuraAlternativa extends ActionSius
 implements ICostantiVerbale, ICostantiMisuraAlternativa
 {
   public String processRequest() throws Exception
  {
      String lRetPage = PG_LOADINSERISCIDATAINIZIOMISURAALTERNATIVA;
      //  gestioneRitorno();
      BigDecimal lIdFasSius = null;
      FascicoloGPModel lFasGPMod = null;

      // Fascicolo Sius
      lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
      lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

      // Il sistema controlla che il fascicolo non sia definito
      if (this.IsFascicoloSiusModificabile()==false)
        throw new SIUSException(SIUSException.USER_MESSAGE,ICostantiFascicoloSius.MSG_NON_MODIFICABILE);

      // Controlla che sia stata emessa un'ordinanza della misura alternativa
      IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
      EventoModel lEve = lCtrlEvento.ExRicercaEventoMisuraAlternativaByIdFasSius(lIdFasSius);
      if (lEve==null)
        throw new F3BException(F3BException.USER_MESSAGE,"Per il fascicolo indicato non è stata emessa nessuna ordinanza di concessione della misura alternativa! ");

      // Controlla che non sia stata gia' inserita una data inizio misura alternativa
      IVerbale lCtrlVerbale = SIEPLookupRemote.getVerbaleRemote();
      VerbaleModel lVerbaleMod = null;
      lVerbaleMod = lCtrlVerbale.ExRicercaVerbaleObblighiByIdEvento(lEve.getIdEvento());
      if (lVerbaleMod !=null && lVerbaleMod.getIdVerbale() != null)
      {
        setRequestAttribute("verbale", lVerbaleMod);
        //Prepara la pagina di destinazione
        lRetPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.misuraalternativa.action.ActLoadDettaglioDataInizioMisuraAlternativa";
      }
      else
      {
       // Preleva i dati della misura alternativa
       IEsecuzioneMA lEMACtrl = SIUSLookupRemote.getEsecuzioneMARemote();
       EsecuzioneMisuraAlternativaModel lEMAModel = lEMACtrl.ExRicercaEsecuzioneMisuraAlternativaByIdFascicolo(lIdFasSius);

       // LISTA UFFICI
       Option lOptionAutorita = null;
       lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutoritaArresto());

       // Preleva elenco degli altri destinatari. (ist detenzione)
       Option lOptionAut = new Option();
       lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(),75);

       setRequestAttribute("idEvento", lEve.getIdEvento().toString() );
       setRequestAttribute("codTipoUfficioCSSA", "40");
       setRequestAttribute("lEsecMisuraAlternativa", lEMAModel);
       setRequestAttribute("tipoAutoritaArresto", "" + lOptionAutorita);
       setRequestAttribute("tipoAutorita", lOptionAut.toString());
      }
      return lRetPage; //restituisce la jsp di VIEW
    }
 }