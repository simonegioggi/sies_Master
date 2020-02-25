package siap.sius.magistratorelatore.action;

import java.math.BigDecimal;

import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
//import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.esperto.model.EspertoModel;
//import siap.sius.esperto.action.ICostantiEsperto;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
//import siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore;
import siap.sius.util.SIUSLookupRemote;

  /**
  * <p>Title: ActLoadInserisciMagistratoRelatore</p>
  * <p>Description: Classe Action per la load inserisci di MagistratoRelatore</p>
  * <p>Copyright: Copyright (c) 2002</p>
  * <p>Company: Bull</p>
  * @version 1.0
  */
public class ActLoadInserisciMagistratoRelatore extends ActRicercaFSPuntuale
implements ICostantiMagistratoRelatore
{
  public String processRequest() throws Exception
  {
    gestioneRitorno();
    BigDecimal lIdFasSius = null;
    FascicoloGPModel lFasGPMod = null;
    MagistratoRelatoreModel lMagRel = null;
    MagistratoModel lMagistrato = null;
    EspertoModel lEsperto = null;
    String lactionDest = null;
    UtenteModel lUtenteMod = null;
    //Passa la action di destinazione

    if (!isRequestParameterNullObj("acdest"))
    {
        lactionDest = getRequestStringParameter("acdest");
    }
    // STUB 08/03/2005.
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

    // Magistrato relatore
    IMagistratoRelatore lMagRelCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
    lMagRel = lMagRelCtrl.ExRicercaMagRelByFascicolo(lIdFasSius);

    if (lMagRel!=null)
    {
      // Magistrato
      if (lMagRel.getMagCodMagistrato()!=null)
      {
        IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
        lMagistrato = lMagCtrl.ExRicercaMagistratoByCod(lMagRel.getMagCodMagistrato());
      }

      // Esperto
      if (lMagRel.getEspIdEsperto()!=null)
      {
        IEsperto lEspCtrl = SIUSLookupRemote.getEspertoRemote();
        lEsperto = lEspCtrl.ExRicercaEspertoByKey(lMagRel.getEspIdEsperto());
      }
    }

    setRequestAttribute("magistrato", lMagistrato);
    setRequestAttribute("esperto", lEsperto);
    setRequestAttribute("modalita", "I");
    setRequestAttribute("acdest", lactionDest);
    setRequestAttribute("utente", lUtenteMod);

    return PG_LOAD_INSERISCI_MAGISTRATORELATORE;  //restituisce la jsp di VIEW
  }
}