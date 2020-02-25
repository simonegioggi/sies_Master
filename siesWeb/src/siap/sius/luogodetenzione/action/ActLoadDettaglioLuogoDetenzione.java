package siap.sius.luogodetenzione.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

//import siap.siep.altracausa.model.AltraCausaModel;
//import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
//import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
//import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.log.LogF3B;
//import java.util.Collection;
//import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.util.SIEPLookupRemote;

/**
* <p>Title: ActLoadRicercaLuogoDetenzione</p>
* <p>Description: Classe Action per la load inserisci di Luogo Detenzione lato SIUS </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadDettaglioLuogoDetenzione extends ActionSius 
implements ICostantiLuogoDetenzione,ICostantiFascicoloSius
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
    
    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    
    BigDecimal lIdFascicolo       = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
    BigDecimal lIdFascicoloSIEP   = lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep();
        
    // Si esegue controllo lo stato del fascicolo.
    // Da come si evince non è possibile utilizzare la funzione se lo stato è :
    // COD_DEFINITO = 01
    // COD_UNIFICATO = 05
    if( lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().equals(COD_DEFINITO) || 
        lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().equals(COD_UNIFICATO) )
      throw new SIUSException( SIUSException.USER_MESSAGE, "Il fascicolo SIUS risulta Archiviato/Definito." );
    
   
    /* STUB 20080122 - Da Eliminare 
    if( ! lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().equals("02"))
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage( IWebConstants.PG_MAIN );
      lRedirigi.setAction("siap.sius.fascicolo.action.ActLoadDettaglioFascicolo" );
      lRedirigi.setParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS, lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString());
      setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
      throw new F3BException( F3BException.USER_MESSAGE, "Il fascicolo risulta Archiviato/Definito" );
    }
    */
    
    
    /* STUB 20080122 - Da Eliminare Inutile
    if(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null)
    {
      // Controlla se il fascicolo_SIEP appartiene ad un'altra BDI
      // ETC ETC Punto 4a-b
    }
    */
    
    // ** Seleziona l'ultima eventuale Posizione Giuridica associata al Fascicolo Siep **
    IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod = lCtrlPos.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicoloSius( lIdFascicoloSIEP, lIdFascicolo );

    if (lPosLuoAltMod.getLuogoDetenzione() == null ) //controlla se esiste un luogo det. in siep
      lPosLuoAltMod = lCtrlPos.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo( lIdFascicoloSIEP);
    
    /* STUB 20080122 - da Eliminare - non utilizzate
    PosizioneGiuridicaModel lPosGiu = lPosLuoAltMod.getPosizioneGiuridica();
    LuogoDetenzioneModel lLuogoDet  = lPosLuoAltMod.getLuogoDetenzione();
    AltraCausaModel lAltraCausa     = lPosLuoAltMod.getAltraCausa();
    */
    
    /* STUB 20080122 - Non Utilizzato - da Eliminare 
    // Controlla se per il fascicolo selezionato esiste un ordine di esecuzione (o legge simeone)
    IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
    boolean lFlagOrdineEsecuzione = lCtrl.ExEsisteOrdineEsecuzioneByFascicoloSiep(lIdFascicoloSIEP);
    */
    
    // Imposta Combo posizione Giuridica
    /* STUB 20080122 - Non Utilizzate - da eliminare
    Collection lTipoPosizioni = null;
    
    if(!lFlagOrdineEsecuzione)
      lTipoPosizioni = DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione();
    else
      lTipoPosizioni = DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione();
    */
    
    setRequestAttribute("PosizioneGiuridicaLuogoDetenzioneAltraCausaModel", lPosLuoAltMod);
    setRequestAttribute("modalita", "I");

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
    return PG_LOAD_DETTAGLIOLUOGODETENZIONE;
  }
}