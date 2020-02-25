package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.scambiosanzione.controller.IScambioSanzione;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


/**
 * <p>Title: ActInserisciAnnotazioneProvvedimento</p> 
 * <p>Description: Classe Action per l'inserimento di una Annotazione Provvedimento Sanzione Sostitutiva
 * viene variato lo stato di esecuzione
 * </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 * @version 3.0
 */

public class ActInserisciAnnotazioneProvvedimento extends ActionSiap implements ICostantiSanzioneSostitutiva
{
  public String processRequest() throws F3BException
  {
	  
	  FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
	  BigDecimal idScSanzione = getRequestBigDecimalParameter(CAMPO_ID_SANZIONE_SOSTITUTIVA);
	  
	  //PRENDO IL METODO DAL CONTROLLER DI SCAMBIOSANZIONE
	  IScambioSanzione lCtrl = SIEPLookupRemote.getScambioSanzionRemote();	   
	  ScambioSanzioneModel scSanzioneMod = lCtrl.ExRicercaScambioSanzioneById(idScSanzione);

	  setRequestAttribute("scambiosanzione", scSanzioneMod);
	  
	  //========= modifica dello stato del procedimento ==========
	  //preparo il model da passare al controller per l'aggiornamento
	  StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
	  
	  //Codifico lo stato del procedimento in base al codice esito
	  String codStatoProc = "";
	  if(scSanzioneMod.getCodNaturaSanzione().equals("AS")) codStatoProc = "0234";	  
	  else if(scSanzioneMod.getCodNaturaSanzione().equals("IR"))codStatoProc = "0242";
	  else if(scSanzioneMod.getCodNaturaSanzione().equals("IT"))codStatoProc = "0243";
	  else if(scSanzioneMod.getCodNaturaSanzione().equals("NP"))codStatoProc = "0244";
	  else if(scSanzioneMod.getCodNaturaSanzione().equals("RG"))codStatoProc = "0245";
	  	  
	  //imposto i valori nel model
      lStatoProcMod.setProgressivo				(new BigDecimal(1));
      lStatoProcMod.setFasSieIdFascicoloSiep   	(scSanzioneMod.getFasSieIdFascicoloSiep());
      lStatoProcMod.setData                 	(DateUtils.getSysDate());
      lStatoProcMod.setCodStatoProcedimento 	(codStatoProc);
      lStatoProcMod.setCodOperatoreInserimento 	(this.getCodUtenteConnesso());
      lStatoProcMod.setDataInserimento         	(DateUtils.getSysDate());
      lStatoProcMod.setCodUfficioInserimento   	(this.getCodUfficioUtenteConnesso());
      

	  //Posizione giuridica
	  PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
	  IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
	  lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

	
	  // Controllo Esistenza pena residua per quel fascicolo
	  PenaResiduaModel lPenaResMod = new PenaResiduaModel();
	  IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
	  lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());	  

	  
	  //evento
	  EventoModel lEveMod =  new EventoModel();
	  
	  lEveMod.setEveIdEvento(scSanzioneMod.getEveIdEvento());
	  lEveMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
	  lEveMod.setCodTipoEvento("01");
	  lEveMod.setCodTipoProvvedimento("04");
	  lEveMod.setCodMotivo(scSanzioneMod.getCodTipoSanzione());
	  lEveMod.setCodOperatoreInserimento 	(this.getCodUtenteConnesso());
	  lEveMod.setDataInserimento         	(DateUtils.getSysDate());
	  lEveMod.setCodUfficioInserimento   	(this.getCodUfficioUtenteConnesso());
	  lEveMod.setEveIdEvento				(scSanzioneMod.getEveIdEvento());
	  lEveMod.setCodEsito("-");
	  lEveMod.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
	  lEveMod.setCodLuogoEmittente(getCodComuneUtenteConnesso());  
    // 11/07/2011 La Data Emissione va inserita nel formato dd/MM/yyyy
 	 	//lEveMod.setDataEmissione(DateUtils.getSysDate());
 	 	lEveMod.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
      lEveMod.setFlagDocumentoRegistrato("S"); 
      lEveMod.setFlagStampaSiep("S"); 
      lEveMod.setFlagVideoSiep("S");
      lEveMod.setCodMagistrato("-");
      lEveMod.setCodLuogoDestinatario("-");
      lEveMod.setCodUfficioDestinatario("-");
      lEveMod.setCodTipoUfficioDestinatario("-");

      EventoModel lRetModel = lCtrl.ExInserisciEventoAggiornaStatoProcedimento(lEveMod,lPos.getPosizioneGiuridica(),lPenaResMod,lStatoProcMod);

	  String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.sanzionesostitutiva.action.ActDettaglioAnnotazioneProvvedimento&" +
		 ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getIdEvento();	  
	  
	  
	  return lPage;	  
  }
}