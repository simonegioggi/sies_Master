package siap.sius.sanzionesostitutiva.action;

import java.math.BigDecimal;

//import siap.sico.evento.model.EventoModel;
//import siap.sius.SIUSException;
//import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
//import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciInizioSanzioneSostitutivaUDS extends ActSanzioneSostitutiva 
implements  ICostantiSanzioneSostitutiva
{
	 public String processRequest() throws F3BException {
	    
			//recupero ID fascicolo e dati maschera
		 	PeriodoAltraSanzioneModel lPerMod = new PeriodoAltraSanzioneModel();
			lPerMod = recuperoDatiMaschera(lPerMod);
			BigDecimal lIdFasSius = recuperoIdFascicolo();
			BigDecimal lIdFasSiep = recuperoIdFascicoloSiep();
			lPerMod.setFasSiuIdFascicoloSius(lIdFasSius);
			lPerMod.setFasSieIdFascicoloSiep(lIdFasSiep);
			
			// 20260512-MEV_2026-124_SG: i quantum della pena sostitutiva devono essere valorizzati
			siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel lEssQ =
					siap.sius.util.SIUSLookupRemote.getEsecuzioneSSRemote().ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(lIdFasSius);
			if (lEssQ != null
					&& (lEssQ.getNumAnniSanzione() == null || lEssQ.getNumAnniSanzione().intValue() == 0)
					&& (lEssQ.getNumMesiSanzione() == null || lEssQ.getNumMesiSanzione().intValue() == 0)
					&& (lEssQ.getNumGiorniSanzione() == null || lEssQ.getNumGiorniSanzione().intValue() == 0))
				throw new siap.sius.SIUSException(siap.sius.SIUSException.USER_MESSAGE,
						"Attenzione! Valorizzare i quantum della pena sostitutiva (Modifica procedimento di Esecuzione) prima di procedere.");

			//setto i dati dell'utente che effettua l'inserimento
			lPerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPerMod.setDataInserimento(DateUtils.getSysDate());     

		    // Recupera il controller ed effettua l'inserimento inserisce periodo, modifica ESS
		    IPeriodoAltraSanzione lCtrl = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		    lPerMod = lCtrl.ExInserisciPeriodoAltraSanzione(lPerMod);
		    
		    //====================================================================== 
		    // Prepara la pagina di destinazione
		    // Viene restituita la pagina di dettaglio con i dati appena inseriti
		    //====================================================================== 	   
		    String lPage="";
		    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.sanzionesostitutiva.action.ActLoadValidaInizioSanzioneSostitutivaUDS";
		    lPage += "&" + CAMPO_ID_PERIODO_ALTRA_SANZIONE + "=" + lPerMod.getIdPeriodoAltraSanzione().toString();
		    return lPage;
		  }
}
