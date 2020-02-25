package siap.siep.sanzionesostitutiva.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Action per il caricamento della pagina con la griglia dei bottoni per la
 * Gestione  Sanzioni Sostitutive
 *
 */
public class ActGestioneSanzioniSostitutive extends ActionSiap
                            implements ICostantiSanzioneSostitutiva
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      //setRequestAttribute("fascicoloNotInSession", "S");
    }

    this.isFascicoloSiepDiCompetenza();
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    if(isFascicoloNonValidato())
        return IWebConstants.PG_MESSAGE;

    this.isEventoNonValidato();    
    
    //==========================================================================
    // Recupero la Sanzione Sostitutiva In Sentenza 
    //==========================================================================
    IPenaComplessiva lCtrlPenaComp = SIEPLookupRemote.getPenaComplessivaRemote();
    PenaComplessivaSanzioneSostitutivaModel lPenaCompSanzModel = lCtrlPenaComp.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

    SanzioneSostitutivaModel lSanzSost = null;
    if(lPenaCompSanzModel!=null) {
    	lSanzSost = lPenaCompSanzModel.getSanzioneSostitutiva();
    	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    	siesLogger.debug("lSanzSost = "+lSanzSost);
    }
    
    
    if (lSanzSost==null || lSanzSost.getIdSanzioneSostitutiva()==null)
    {
    	setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                        "Per il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non risulta presente una Sanzione Sostitutiva!");
    	return IWebConstants.PG_MESSAGE;
    }
      
    
    setRequestAttribute("strFunzione", "Sanzioni Sostitutive");

    return PG_GRIGLIA_SANZIONI_SOSTITUTIVE;
  }
}