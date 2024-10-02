package siap.siep.annotazionemanuale.action;

/**
* <p>Title: ActLoadStampeAnnotazioni</p>
* <p>Description: Classe Action per l'inserimento della nuova Pena validata</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

public class ActLoadStampeAnnotazioni extends ActionSiap
{

/**
 * Azione di visualizzazione stampe per le annotazioni manuali
 * @return Nome della pagina JSP da visualizzare
 * al termine dell'elaborazione
 * @throws F3BException
 */
  protected String loadStampe() throws Exception
  {
//Controllo Presenza del Fascicolo in Sessione
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    IPenaResidua lCtrlPena = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel lPenRes = lCtrlPena.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);

    setRequestAttribute("PenaResidua", lPenRes);

/******************************* Pena Complessiva *****************************/
    IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
    PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);
    setRequestAttribute("PenaComplessivaSentenza", lPenComMod);

    String lFlagPage = "";
    if( !isRequestParameterNullObj("lFlagPage") )
    {
      lFlagPage = getRequestStringParameter("lFlagPage");
    }

    if( !isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE) )
    {
      String lIdAnnotazioneManuale = getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE);
      setRequestAttribute(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE, lIdAnnotazioneManuale);
    }

    setRequestAttribute("lFlagPage", lFlagPage);
    setRequestAttribute("lPageGE", lFlagPage);
    
    // MEV_2019-09 -- Devo passare la posozione Giuridica per non fare uscire O.S in caso di LIBERO nella form successiva
    // Esistenza posizione giuridica
    IPosizioneGiuridica lPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaModel PGMod = new PosizioneGiuridicaModel();
    PGMod.setFasSieIdFascicoloSiep(lIdFascicolo);
    PosizioneGiuridicaModel PGMod2 = lPG.ExRicercaPosizioneGiuridicaCorrente(PGMod);

    if(PGMod2==null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Rivedere Posizione Giuridica !");
    
    setRequestAttribute("CodPosizioneGiuridica", PGMod2.getCodPosizioneGiuridica());

    // Se i controlli sono andati a buon fine torna null
    return null;
  }
}