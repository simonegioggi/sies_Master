package siap.siep.scadenzario.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;


/**
 * <p>Title: ActLoadDettaglioScadenzarioOttemperaObblighi</p>
 * <p>Description: Classe Action per la load dettaglio di Scadenzario </p>
 * <p>Pene Sospese con i Termini Ottemperanza Obblighi in scadenza  </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadDettaglioScadenzarioOttemperaObblighi extends ActionSiap implements ICostantiScadenzario
{
  public String processRequest() throws Exception 
  {
    BigDecimal lIdScadenzario = null;
    BigDecimal lIdFascicoloSiep = null;
    FascicoloSiepModel lFascMod = null;
    
    if( !isRequestParameterNullObj(CAMPO_ID_SCADENZARIO) )
    {
      lIdScadenzario = getRequestBigDecimalParameter(CAMPO_ID_SCADENZARIO);
    }
    else
    {
      if (this.isSessionAttributeNullObj("fascicolo"))
        return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

      lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

      if(isFascicoloNonValidato())
        return IWebConstants.PG_MESSAGE;

      if (isFascicoloArchiviatoDefinito())
        return IWebConstants.PG_MESSAGE;
      
      lIdFascicoloSiep = lFascMod.getIdFascicoloSiep();
    }

    IScadenzario lCtrlScad = SIEPLookupRemote.getScadenzarioRemote();

    ScadenzarioModel llScaMod = null;
    // Se l'lIdScadenzario è null lo recupera attraverso l'lIdFascicoloSiep in sessione
    if( lIdScadenzario == null )
    {

      llScaMod = lCtrlScad.ExScadenzarioCorrenteByIdFascicoloTipoScadenzario(lIdFascicoloSiep, "16");
    }
    else
    {
      llScaMod = lCtrlScad.ExRicercaScadenzarioByKey( lIdScadenzario );
      
      // Cerca il fascicolo relativo allo scadenzario siep
      IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
      
      lFascMod = lCtrlFas.ExRicercaFascicoloByKey(llScaMod.getFasSieIdFascicoloSiep());
    }
    
    if( llScaMod == null )
    {
      throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun elemento trovato.");
    }
    else
    {
      lIdFascicoloSiep = llScaMod.getFasSieIdFascicoloSiep();
      llScaMod.setFascicoloModel(lFascMod);
    }

    setRequestAttribute("scadenzario", llScaMod);

    // Aggiorna lo scadenzario come visto
    llScaMod.setFlagVisto("S");
    llScaMod.setDataVisto(DateUtils.getSysDate());

    
    lCtrlScad.ExModificaScadenzario(llScaMod);

    return PG_LOAD_DETTAGLIOSCADENZARIO_OTTEMPERAOBBLIGHI;
  }
}