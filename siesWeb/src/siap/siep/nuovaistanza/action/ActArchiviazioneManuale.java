package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;

import siap.SIAPException;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
 * <p>Title: ActArchiviazioneManuale</p>
 * <p>Description: La Action esegue l'Archiviazione 
 * e l'Annullamento dell'archiviazione del Fascicolo SIEP.</p>
 * <p> l'annullamento viene eseguito dalla Action ereditata che valorizza 
 * il flag isAnnullamento a true. </p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: Agile</p>
 * @author Luigi
 * @version 1.0
 */

public class ActArchiviazioneManuale extends ActionSiap implements ICostantiFascicoloSiep
{
	protected  boolean isAnnullamento = false;
	
  public String processRequest() throws Exception
  {
	
	if( isSessionAttributeNullObj("fascicolo") )
    	  	throw new F3BException(F3BException.USER_MESSAGE, "Dati del Procedimento non in sessione !!");

	//ricerca Istanza per il fascicolo in sessione
	FascicoloSiepModel lFascMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");

    if(lFascMod == null || lFascMod.getIdFascicoloSiep() == null)
      throw new SIEPException(SIAPException.USER_MESSAGE,"Dati del Fascicolo assenti !!");
	
    isFascicoloSiepDiCompetenza();

    // Valorizzazione Fascicolo 
    lFascMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lFascMod.setDataAggiornamento(DateUtils.getSysDate());
    lFascMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    if (isAnnullamento)
    {
        lFascMod.setCodStatoFascicolo("02"); // Iscritto
        lFascMod.setCodMotivoArchiviazione(""); 
    	lFascMod.setDataArchiviazione( null);
    }
    else
    {
    	lFascMod.setCodStatoFascicolo("01"); // Archiviato
    	lFascMod.setCodMotivoArchiviazione("11"); // Archiviazione manuale
    	lFascMod.setDataArchiviazione( getRequestDateParameter( CAMPO_ANNO_ARCHIVIAZIONE, CAMPO_MESE_ARCHIVIAZIONE, CAMPO_GIORNO_ARCHIVIAZIONE) );
    }
    
    // Valorizzazione stato del procedimento
    StatoProcedimentoModel lStat = new StatoProcedimentoModel();
    lStat.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
    lStat.setProgressivo(new BigDecimal(1));
    lStat.setDataInserimento(DateUtils.getSysDate());
    lStat.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lStat.setCodOperatoreInserimento(getCodUtenteConnesso());
    if (isAnnullamento)
    {
        lStat.setCodStatoProcedimento("0436"); // Annullata Archiviazione 
        lStat.setData(DateUtils.getSysDate());
    }
    else
    {
        lStat.setCodStatoProcedimento("0076"); // Archiviato Definito 
        lStat.setData(lFascMod.getDataArchiviazione());    	
    }
     
    // Aggiornamento dati 
    IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
    lFascMod = lCtrl.ExArchiviazione(lFascMod,lStat);

    // Aggiornamento fascicolo in sessione
    setSessionAttribute("fascicolo", lFascMod);
    
    //Prepara la "pagina" di destinActione.
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage(IWebConstants.PG_MAIN);
    lRedirigi.setAction("siap.siep.nuovaistanza.action.ActLoadArchiviazioneManuale");
    if (! isRequestAttributeNullObj(IWebConstants.LINK_RITORNO))
    	lRedirigi.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO));   
    
    return lRedirigi.toString();
  }
}