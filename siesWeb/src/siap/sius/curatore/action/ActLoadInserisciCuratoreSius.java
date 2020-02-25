package siap.sius.curatore.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sige.curatore.controller.ICuratore;
import siap.sige.curatore.model.CuratoreModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.curatore.controller.ICuratoreSius;
import siap.sius.curatore.model.CuratoreSiusModel;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.html.Option;

  /**
  * <p>Title: ActLoadInserisciCuratoreSius</p>
  * <p>Description: Classe Action per la load inserisci di Curatore SIUS</p>
  * <p>Copyright: Copyright (c) 2011</p>
  * <p>Company: </p>
  * @version 1.0
  */
public class ActLoadInserisciCuratoreSius extends ActRicercaFSPuntuale
implements ICostantiCuratoreSius
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    gestioneRitorno();
    BigDecimal lIdFasSius = null;
    FascicoloGPModel lFasGPMod = null;
    CuratoreSiusModel lCurSius = null;
    CuratoreModel lCuratore = null;
    String lactionDest = null;
    UtenteModel lUtenteMod = null;
    //Passa la action di destinazione

    if (!isRequestParameterNullObj("acdest"))
    {
        lactionDest = getRequestStringParameter("acdest");
    }
    else if( ! isRequestParameterNullObj( CAMPO_CHIAVE_ANNO ) )
    {
      // Invoca la process Request della superclasse se proviene dal menu'.
      super.processRequest();
    }

    // Fascicolo Sius
    lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

    //Utente
    lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

    // Curatore Sius
    ICuratoreSius lCurSiusCtrl = SIUSLookupRemote.getCuratoreSiusRemote();
    lCurSius = lCurSiusCtrl.ExRicercaCurSiusByFascicolo(lIdFasSius);

    if (lCurSius!=null)
    {
      // Curatore
      if (lCurSius.getCurIdCuratore()!=null)
      {
        ICuratore lCurCtrl = SIGELookupRemote.getCuratoreRemote();
        lCuratore = lCurCtrl.ExRicercaCuratoreByKey(lCurSius.getCurIdCuratore() );
      }

    }
    String lTipoAttuale = "-";
    if (lCurSius!=null)
    	lTipoAttuale=lCurSius.getDescrTipo();
    Option lOption = new Option(DecodificheManager.getInstance().getTipoCuratore(), lTipoAttuale.trim(), Option.NO_BLANK_ITEM);
    setRequestAttribute("tipoCuratore", ""+ lOption);
    
    if (lCurSius!=null && lCuratore !=null)
    lCurSius.setCuratore(lCuratore);
    setRequestAttribute("curatore", lCurSius);
    setRequestAttribute("modalita", "I");
    setRequestAttribute("acdest", lactionDest);
    setRequestAttribute("utente", lUtenteMod);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("ActLoadInserisciCuratore: fine");

    return PG_LOAD_INSERISCI_CURATORESIUS;  //restituisce la jsp di VIEW
  }
}