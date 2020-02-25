package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * Action per il caricamento del dettaglio del porvvedimento di trasmissione per competenza
 * 
 * 
 * <p>
 * Title: ActLoadDettaglioTrasmissioneCompetenza
 * </p>
 * <p>
 * Description: ActLoadDettaglioTrasmissioneCompetenzas
 * </p>
 * 
 * @author d.f.
 * @version 1.0
 * 
 */
public class ActLoadDettaglioTrasmissioneCompetenza extends ActSIESDettaglioProvvedimento implements
		ICostantiMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		if (lIdEvento == null) {
			String lStrIdEvento = this.getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
			lIdEvento = new BigDecimal(lStrIdEvento);
		}

		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", llPenMod);

		// ricerca posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// Visualizza residenza ???
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		ResidenzaAssociataModel lResAss = lCtrl.ExRicercaResidenzaFascicoloSiepCorrente(lFascMod
				.getIdFascicoloSiep());
		if (lResAss != null && lResAss.getResidenza() != null)
			setRequestAttribute("residenza", lResAss.getResidenza());

		// ==================================================
		// Recupero le Misure di sicurezza da visualizzare
		// ==================================================
		if ("S".equalsIgnoreCase(lFascMod.getFlagCumulante())) {
			ICumulo lCtrlCum = SIEPLookupRemote.getCumuloRemote();
			Vector lCumuli = lCtrlCum
					.ExRicercaFascicoliCumulobyIdFascicoloSiepValidatoDataCumuloNotNull(lFascMod
							.getIdFascicoloSiep());

			PenaCumuloModel lPenCumMod = new PenaCumuloModel();

			if (lCumuli.size() > 0) {
				CumuloModel lCumMod = ((CumuloModel) (lCumuli).get(0));

				if (lCumMod != null && lCumMod.getIdCumulo() != null) {
					IPenaCumulo lCtrlPen = SIEPLookupRemote.getPenaCumuloRemote();
					lPenCumMod = lCtrlPen.ExRicercaPenaCumuloByIdCumulo(lCumMod.getIdCumulo());
				}
			}

			this.setRequestAttribute("penacumulo", lPenCumMod);
		} else {
			IMisuraSicurezza lCtrlMs = SIEPLookupRemote.getMisuraSicurezzaRemote();
			List lListMisure = lCtrlMs.ExRicercaMisuraSicurezzaByIdFascicolo(lFascMod.getIdFascicoloSiep());
			setRequestAttribute("listaMisure", lListMisure);
		}

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);

		// Ufficio Competente all'Emissione del Provvedimento
		UfficioModel lUffDestinataro = getUfficioByCodUfficio(lEveMod.getEvento().getCodUfficioDestinatario());
		setRequestAttribute("ufficioDestinatario", lUffDestinataro);

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagMod = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagMod);

		// Altra autorità [n.b. non previste per ora]
		setRequestAttribute("noteautoritaEsterna", lEveMod.getNotifiche()[0].getNote());

		return PG_DETTAGLIO_TRASMISSIONE_COMP;
	}

}