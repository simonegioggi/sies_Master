package siap.sius.misuraalternativa.action;

import java.math.BigDecimal;

import siap.sico.cssa.controller.ICSSA;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadDettaglioDataInizioMisuraAlternativa</p>
* <p>Description: Classe Action per la load dettaglio di Data inizio Misura alternativa</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioDataInizioMisuraAlternativa extends ActionSiap
implements ICostantiVerbale, ICostantiMisuraAlternativa
{
public String processRequest() throws F3BException
  {
      String lRetPage = PG_LOADDETTAGLIODATAINIZIOMISURAALTERNATIVA;
      //  gestioneRitorno();
      BigDecimal lIdFasSius = null;
      FascicoloGPModel lFasGPMod = null;

      // Fascicolo Sius
      lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
      lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

      IVerbale lCtrlVerbale = SIEPLookupRemote.getVerbaleRemote();
      VerbaleModel lVerbaleMod = null;
    if(!this.isRequestParameterNullObj("ufficio"))
    {
      // Controlla che il contenuto del fascicolo sia del tipo "ISCRIZIONE MISURA ALTERNATIVA"
      if (!lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U004"))
        throw new F3BException(F3BException.USER_MESSAGE,"Il procedimento indicato non è di esecuzione di misura alternativa! ");

      // Controlla che non sia stata gia' inserita una data inizio misura alternativa
      lVerbaleMod = lCtrlVerbale.ExRicercaVerbaleByIdFascicolo(lIdFasSius);
      if (lVerbaleMod ==null)
        throw new F3BException(F3BException.USER_MESSAGE,"Per il fascicolo indicato non è stata inserita la data inizio misura alternativa! ");

    }else{

      // Controlla che sia stata emessa un'ordinanza della misura alternativa
      IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
      EventoModel lEve = lCtrlEvento.ExRicercaEventoMisuraAlternativaByIdFasSius(lIdFasSius);
      if (lEve==null)
        throw new F3BException(F3BException.USER_MESSAGE,"Per il fascicolo indicato non è stata emessa nessuna ordinanza di concessione della misura alternativa! ");

      // Controlla che sia stata gia' inserita una data inizio misura alternativa
      lVerbaleMod = lCtrlVerbale.ExRicercaVerbaleObblighiByIdEvento(lEve.getIdEvento());
      if (lVerbaleMod ==null)
        throw new F3BException(F3BException.USER_MESSAGE,"Per il fascicolo indicato non è stata inserita la data inizio misura alternativa! ");
    }


      //model cssa
      CSSAModel lCssaMod = null;
      if (lVerbaleMod != null && lVerbaleMod.getCssIdCssa() != null && lVerbaleMod.getCssIdCssa().compareTo(new BigDecimal(0)) != 0)
      {
	ICSSA lCtrlCssa = SICOLookupRemote.getCSSARemote();
	lCssaMod = lCtrlCssa.getCSSAByKey(lVerbaleMod.getCssIdCssa());
      }
      setRequestAttribute("cssa", lCssaMod);

      //model Istituto Detenzione
      IstitutoDetenzioneModel lIstMod = null;
      if (lVerbaleMod != null && lVerbaleMod.getIstDetIdIstitutoDetenzione() != null && !lVerbaleMod.getIstDetIdIstitutoDetenzione().equals("-"))
      {
	IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
	lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lVerbaleMod.getIstDetIdIstitutoDetenzione());
      }
      setRequestAttribute("istitutodetenzione", lIstMod);

/*
      //model misura alternativa
      MisuraAlternativaModel lMisAltMod = new MisuraAlternativaModel();
      IMisuraAlternativa lCtrlMisAlt = SICOLookupRemote.getMisuraAlternativaRemote();
      lMisAltMod = lCtrlMisAlt.ExRicercaMisuraAlternativaByIdEvento(lVerbaleMod.getEveIdEvento());
      setRequestAttribute("misuraalternativa", lMisAltMod);
*/
      setRequestAttribute("verbale", lVerbaleMod);

      return lRetPage; //restituisce la jsp di VIEW

  }

}