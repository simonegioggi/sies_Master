package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * Action per il caricamento del dettaglio della trasmissione Comunicazioni alle procure Titolari dei
 * procedimenti cumulati.
 * 
 * @author Intersistemi Italia S.p.A.
 */
public class ActDettaglioTrasmissioneComunicazioniProcure extends ActSIESDettaglioProvvedimento
		implements ICostantiModuloCumulo {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);

		//// UfficioModel lUfficio = getUfficioByCodUfficio(mComp.getChiaveUfficio());
		//// setRequestAttribute("UfficioProcRichiesto", lUfficio);

		// Altri dati da visualizzare in maschera
		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", llPenMod);

		// Ricerca posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// Ricerca Magistrato Firmatario
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagMod = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagMod);

		// Da Verificare a cosa servono
		// FascicoloSiepModel mFascComp = new FascicoloSiepModel();
		// mFascComp.setChiaveUfficio(mComp.getChiaveUfficio());
		// mFascComp.setChiaveAnno(mComp.getChiaveAnno());
		// mFascComp.setChiaveProgr(mComp.getChiaveProgr());
		//
		// IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
		// FascicoloSiepModel findedFasc = null;
		// if(mComp.getFasSieIdFascicoloSiep()!=null){
		// findedFasc = lCtrlFas.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(mFascComp);
		// setRequestAttribute("fascCompetenza", findedFasc);
		// }

		return PG_LOAD_DETTAGLIO_RICHIESTA_TRASMISSIONE_TITOLO;
	}

}