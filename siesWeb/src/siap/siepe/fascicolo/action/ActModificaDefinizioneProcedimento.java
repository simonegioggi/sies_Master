package siap.siepe.fascicolo.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siepe.SIEPEException;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 * <p>Title: ActModificaDefinizioneProcedimento</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActModificaDefinizioneProcedimento extends ActionSiap
implements ICostantiFascicoloSiepe
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    gestioneRitorno();

    String lRetPage = PG_LOAD_DEFINIZIONE_PROCEDIMENTO; // pagina di input
    FascicoloSiepeEstesoModel lFasSiepeEstesoMod = null;
    String lmodalita = "modifica";

    // il Fascicolo è in sessione
     if(isSessionAttributeNullObj("FascicoloSiepeEsteso"))
       throw new SIEPEException(SIEPEException.USER_MESSAGE,"Dati del Fascicolo non in sessione!");

     lFasSiepeEstesoMod = (FascicoloSiepeEstesoModel)getSessionAttribute("FascicoloSiepeEsteso");
     
     if (lFasSiepeEstesoMod == null || 
         lFasSiepeEstesoMod.getFascicoloSiepe() == null || 
         lFasSiepeEstesoMod.getFascicoloSius() == null )
       throw new SIEPEException(SIEPEException.USER_MESSAGE, "Errore nei dati del Fascicolo in sessione!");

    // Lock per evitare più definizioni contemporanee del Fascicolo
    LockModel lck = LockController.lockIfNotLocked(getServletContext(),  "ProcedimentoSIEPE", lFasSiepeEstesoMod.getFascicoloSiepe().getIdFascicoloSiepe().toString(),  getCodUtenteConnesso(), getSession().getId());
    if (lck != null)
      throw new SIEPEException(SIEPEException.USER_MESSAGE, "Il  " + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi !");

    IFascicoloSiepe lFasSiepeCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();
    FascicoloSiepeModel lFasSiepeModel = lFasSiepeCtrl.ExRicercaFascicoloSiepeByKey(lFasSiepeEstesoMod.getFascicoloSiepe().getIdFascicoloSiepe()); 
     
    // Costruzione dell'Option filtrata dal Codice tipo Ufficio (UDS o TDS)
    Option lOption = new Option(DecodificheUtils.getDecodificheFiltrateByCodAlt( DecodificheManager.getInstance().getTipoDefinzioneSiepe(),getUfficioUtenteConnesso().getCodTipoUfficio()));
    lOption.setSelected(lFasSiepeModel.getTipoDefinizione());
    
    // Valorizzazione dei dati nella request
    setRequestAttribute("FlagFasSiepe", "SI"); // Imposta il flag per abilitare la visualizzazione dei dati di sintesi SIEPE
    setRequestAttribute("TipoDefinizione", "" + lOption);
    setRequestAttribute("modalita", lmodalita);
    setRequestAttribute("descrizione", lFasSiepeModel.getDescrDefinizione());
    setRequestAttribute("data_definizione", lFasSiepeModel.getDataDefinizione());

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );

    return lRetPage;
  }
}