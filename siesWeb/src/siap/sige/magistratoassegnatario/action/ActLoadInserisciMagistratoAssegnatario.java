package siap.sige.magistratoassegnatario.action;

import java.math.BigDecimal;

import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioMagistratoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.util.SIGELookupRemote;

  /**
  * <p>Title: ActLoadInserisciMagistratoAssegnatario</p>
  * <p>Description: Classe Action per la load inserisci di MagistratoAssegnatario</p>
  * <p>Copyright: Copyright (c) 2008</p>
  * <p>Company: Eutelia</p>
  * @version 1.0
  */
public class ActLoadInserisciMagistratoAssegnatario extends ActRicercaFSigePuntuale
implements ICostantiMagistratoAssegnatario
{
  public String processRequest() throws Exception
  {
    gestioneRitorno();
    BigDecimal lIdFasSige = null;
    FascicoloSigeEstesoModel lFasSigeEstMod = null;
    MagistratoAssegnatarioModel lMagAss = null;
    MagistratoModel lMagistrato = null;
    String lactionDest = null;
    UtenteModel lUtenteMod = null;

    //Passa la action di destinazione
    if (!isRequestParameterNullObj("acdest"))
    {
        lactionDest = getRequestStringParameter("acdest");
    }
    //
    else if( ! isRequestParameterNullObj( CAMPO_CHIAVE_ANNO ) )
    {
      // Invoca la process Request della superclasse se proviene dal menu'.
      super.processRequest();
    }

    // Fascicolo Sige
    lFasSigeEstMod = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");
    lIdFasSige = lFasSigeEstMod.getFascicoloSige().getIdFascicoloSige();

    //Utente
    lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

    // Magistrato assegnatario
    IMagistratoAssegnatario lMagAssCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
    lMagAss = lMagAssCtrl.ExRicercaMagAssCorrenteXFascicolo(lIdFasSige);

    if (lMagAss!=null)
    {
      // Magistrato
      if (lMagAss.getMagCodMagistrato()!=null)
      {
        IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
        lMagistrato = lMagCtrl.ExRicercaMagistratoByCod(lMagAss.getMagCodMagistrato());
      }
    }
    MagistratoAssegnatarioMagistratoModel lMagAssMag = new MagistratoAssegnatarioMagistratoModel();
    lMagAssMag.setMagistrato(lMagistrato);
    lMagAssMag.setMagistratoAssegnatario(lMagAss);
    setRequestAttribute("magistrato", lMagAssMag);
    setRequestAttribute("modalita", "I");
    setRequestAttribute("acdest", lactionDest);
    setRequestAttribute("utente", lUtenteMod);

	return PG_LOAD_INSERISCI_MAGISTRATOASSEGNATARIO;  //restituisce la jsp di VIEW
  }
}