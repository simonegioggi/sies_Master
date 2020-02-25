package siap.siep.misuraalternativa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciSospProvvAffProva</p>
 * <p>Description: Classe Action per la load inserisci di Sospensione Provvisoria Affidamento in prova</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciSospProvvAffProva extends ActSospensioneProvvisoria
{
  public String processRequest() throws F3BException
  {
//tutti i controlli e la maggior parte delle request si trovano nel padre
    String lRitorno = getSospensioni();
    if(!lRitorno.equals(""))
      return lRitorno;

//ricerca esistenza almeno una  misura alternativa concessa
   FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
   IMisuraAlternativaIndultino lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
   MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
   String[] tipoMisura = {"0001","0002","0003","2006"};
   String[] natura = {"CO"};
   String[] decisione = {"03","02"};
   lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisione(lFascMod.getIdFascicoloSiep(),decisione,natura,tipoMisura);

  if (this.isRequestParameterNullObj("warning_2"))
  {
    if (lMisAlModConcessa == null || lMisAlModConcessa.getIdMisuraAlternativa() == null)
    {
      //set goto page set flag misura
      setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
      setRequestAttribute( IWebConstants.MESSAGE_TEXT, "Attenzione: è stata richiesta la sospensione provvisoria di una misura non concessa.Continuare?");

      return "/jsp/files/siap/siep/misuraalternativa/WarningMisura.jsp";
    }
  }

//setto il campo codice motivo
    Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoSospProvvMAffP());
    setRequestAttribute("motivoProvv", "" + lOption);

    setRequestAttribute("tipoSospensione", "AFFIDAMENTO");

	// MEV 10 - filtro sui minorenni
	setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());
	
    return PG_LOAD_INSERISCI_MA_DECRETO_SOSP;
  }
}