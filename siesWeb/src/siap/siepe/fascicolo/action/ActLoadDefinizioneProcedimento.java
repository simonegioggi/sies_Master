package siap.siepe.fascicolo.action;
import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siepe.SIEPEException;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.siepe.util.SIEPELookupRemote;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.html.Option;


/**
 *
 * <p>Title: ActLoadDefinizioneProcedimento</p>
 * <p>Description: Azione adibita all'operazione di Definizione del Procedimento SIEPE.</p>
 * Se il Fascicolo risulta già Definito viene presentato il Dettaglio della Definizione.</p>
 * negli altri casi viene presentata la form di input per la Definizione.
 * @throws Exception
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Eunics S.p.A.- Bull Italia </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadDefinizioneProcedimento extends ActionSiap 
implements ICostantiFascicoloSiepe
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  //private FascicoloSiepModel mFasSIEP = null;
  
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
  
    this.gestioneRitorno();

    String lRetPage = null; // pagina di input
    FascicoloSiepeEstesoModel lFasSiepeEstesoMod = null;
    
    boolean fascicoloInSessione = false;

    if( ! isRequestParameterNullObj( CAMPO_CHIAVE_ANNO ) )
      lFasSiepeEstesoMod = ricercaFascicolo();
    else
    {
      // il Fascicolo è in sessione
      if(isSessionAttributeNullObj("FascicoloSiepeEsteso"))
        throw new SIEPEException(SIEPEException.USER_MESSAGE,"Dati del Fascicolo non in sessione!");
      lFasSiepeEstesoMod = (FascicoloSiepeEstesoModel)getSessionAttribute("FascicoloSiepeEsteso");
      fascicoloInSessione = true;
    }

    lRetPage = analisiStatoFascicolo(lFasSiepeEstesoMod);

    if(!fascicoloInSessione)
      setSessionAttribute("FascicoloSiepeEsteso", lFasSiepeEstesoMod);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );

    return lRetPage;
  }

  // Il Fascicolo viene cercato nel DB attraverso le chiavi ANNO e PROG
  private FascicoloSiepeEstesoModel ricercaFascicolo() throws Exception
  {
    if( isRequestParameterNullObj( CAMPO_CHIAVE_ANNO ) ||  isRequestParameterNullObj( CAMPO_CHIAVE_PROGR ))
      throw new SIEPEException(SIEPEException.USER_MESSAGE,"Assenti ANNO/PROG !");

    FascicoloSiepeEstesoModel lFasSiepeEstesoMod = new FascicoloSiepeEstesoModel();
    
    // Imposta i parametri di ricerca
    FascicoloSiepeModel lFasSiepeMod = new FascicoloSiepeModel();
    lFasSiepeMod.setChiaveAnno( getRequestBigDecimalParameter( CAMPO_CHIAVE_ANNO ));
    lFasSiepeMod.setChiaveProgr( getRequestBigDecimalParameter( CAMPO_CHIAVE_PROGR ));
    lFasSiepeMod.setChiaveUfficio( this.getCodUfficioUtenteConnesso() );
    
    // Recupera il fascicolo SIEPE e viene inserito nel fascicolo SIEPE Eeteso.
    IFascicoloSiepe lCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();
    lFasSiepeMod = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio( lFasSiepeMod );    
    lFasSiepeEstesoMod.setFascicoloSiepe(lFasSiepeMod);
    
    if( lFasSiepeEstesoMod.getFascicoloSiepe() == null )
      throw new SIEPEException(SIEPEException.USER_MESSAGE, "Fascicolo Siepe non trovato!");
    
    // Ricerca Soggetto
    if (lFasSiepeEstesoMod.getFascicoloSiepe().getSogIdSoggetto() != null)
    {
      ISoggetto lSoggCtrl = SICOLookupRemote.getSoggettoRemote();
      SoggettoModel lSoggModel = lSoggCtrl.ExRicercaSoggettoByKey(lFasSiepeEstesoMod.getFascicoloSiepe().getSogIdSoggetto());
      lFasSiepeEstesoMod.setSoggetto( lSoggModel );
    }
    
    // Ricerca Fascicolo SIUS
    if (lFasSiepeEstesoMod.getFascicoloSiepe().getFasSiuIdFascicoloSius() != null)
    {
      IFascicoloSius lFasSiusCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
      FascicoloGPModel lFasGPMod = lFasSiusCtrl.ExRicercaFascicoloByKey(lFasSiepeEstesoMod.getFascicoloSiepe().getFasSiuIdFascicoloSius());
      lFasSiepeEstesoMod.setFascicoloSiusGP( lFasGPMod );
    }
    
    // Ricerca Fascicolo SIEP
    if (lFasSiepeEstesoMod.getFascicoloSiepe().getFasSieIdFascicoloSiep() != null)
    {
      IFascicoloSiep lFasSiepCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
      FascicoloSiepModel lFasSiep = lFasSiepCtrl.ExRicercaFascicoloByKey(lFasSiepeEstesoMod.getFascicoloSiepe().getFasSieIdFascicoloSiep());
      lFasSiepeEstesoMod.setFascicoloSiep( lFasSiep );
    }

    return lFasSiepeEstesoMod;
  }
  
  /**
   * La funzione analizza il Fascicolo SIUS ed in base allo stato prepara la form da presentare.
   * I casi sono:
   * 1) STATO = COD_DEFINITO : il fascicolo è già in stato definito, viene visualizzato
   *    il dettaglio della definizione.
   * 3) Negli altri casi viene preparata la form di input per la definizione del procedimento.
   * @param aFasSiepeEstesoMod
   * @return String pagina di input o di dettaglio
   * @throws Exception propaga errore di eccezione
   */
  private String analisiStatoFascicolo(FascicoloSiepeEstesoModel aFasSiepeEstesoMod) throws Exception
  {
    String lRetPage = PG_LOAD_DEFINIZIONE_PROCEDIMENTO;
    String lcodTipoDefinizione = null, lmodalita = null;
    
    if (aFasSiepeEstesoMod == null || aFasSiepeEstesoMod.getFascicoloSiepe() == null)
      throw new SIEPEException(SIEPEException.USER_MESSAGE, "Fascicolo non trovato!");

    if (getCodUfficioUtenteConnesso().compareTo(aFasSiepeEstesoMod.getFascicoloSiepe().getChiaveUfficio()) !=0)
      throw new SIEPEException(SIEPEException.USER_MESSAGE, "Operazione non consentita per Procedimento di altro ufficio !");

    // Costruzione dell'Option filtrata dal Codice tipo Ufficio
    Option lOption = new Option(DecodificheUtils.getDecodificheFiltrateByCodAlt( DecodificheManager.getInstance().getTipoDefinzioneSiepe(),getUfficioUtenteConnesso().getCodTipoUfficio()));

    if (aFasSiepeEstesoMod.getFascicoloSiepe().getCodStatoFascicolo().equalsIgnoreCase(COD_DEFINITO))
    {
      lcodTipoDefinizione = aFasSiepeEstesoMod.getFascicoloSiepe().getTipoDefinizione();
      lOption.setSelected(lcodTipoDefinizione);
      lmodalita = "dettaglio";
      
      setRequestAttribute("descrizione", aFasSiepeEstesoMod.getFascicoloSiepe().getDescrDefinizione());
      setRequestAttribute("data_definizione", aFasSiepeEstesoMod.getFascicoloSiepe().getDataDefinizione());
    }
    else
    // possibile inserire Definizione Procedimento e quindi lock
    {
      // Lock per evitare più definizioni contemporanee del Fascicolo
      LockModel lck = LockController.lockIfNotLocked(getServletContext(),  "ProcedimentoSIEPE", aFasSiepeEstesoMod.getFascicoloSiepe().getIdFascicoloSiepe().toString(),  getCodUtenteConnesso(), getSession().getId());
      if (lck != null)
        throw new SIEPEException(SIEPEException.USER_MESSAGE, "Il  " + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
      lmodalita = "inserimento";
    }
    
    setRequestAttribute("FlagFasSiepe", "SI"); // Imposta il flag per abilitare la visualizzazione dei dati di sintesi SIEPE
    setRequestAttribute("TipoDefinizione", "" + lOption);
    setRequestAttribute("modalita", lmodalita);

    return lRetPage;
  }
}