package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.scambiosanzione.controller.IScambioSanzione;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;


/**
 * <p>Title: ActDettaglioAnnotazioneProvvedimento</p>
 * <p>Description: Classe Action per il dettaglio del provveidmento sanzione sostitutiva</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 * @version 3.0
 */

public class ActDettaglioAnnotazioneProvvedimento extends ActSIESDettaglioProvvedimento 
													  implements ICostantiSanzioneSostitutiva
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

//Evento
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
    setRequestAttribute("evento", lEveMod);
    
 //scambio sanazione
	IScambioSanzione lCtrlSc= SIEPLookupRemote.getScambioSanzionRemote();	   
	ScambioSanzioneModel scSanzioneMod = lCtrlSc.ExRicercaScambioSanzioneByEveIdEvento(lEveMod.getEvento().getEveIdEvento());
	setRequestAttribute("scambiosanzione", scSanzioneMod);

//Posizione giuridica
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
    setRequestAttribute("posizioneluogoaltra", lPos);
	setRequestAttribute("lcodicePosizione", lPos.getPosizioneGiuridica().getCodPosizioneGiuridica());

//Penaresidua
    PenaResiduaModel lPenaResidua = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
 
    //ricerco le sanzione sostitutive
    ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
    SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(),"S");

    // Inserisco la SS residua nel model della PR
    if(lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null)
    {
        lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(),"N"); 	
    }	  
    
    lPenaResidua.setSanzSostResidua(lSSResiduaModel);   
 
    setRequestAttribute("penaresidua", lPenaResidua);
    
    //ricerca penacomplessiva e sanzione sostitutiva
    IPenaComplessiva lCtrlPena = SIEPLookupRemote.getPenaComplessivaRemote();
    PenaComplessivaSanzioneSostitutivaModel lPenSanMod = lCtrlPena.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
    setRequestAttribute("lPenComSanSost", lPenSanMod);	   
    
    
    return PG_DETTAGLIO_INSERISCI_ANNOTAZIONI_PROVVEDIMENTO;
  }
}