package siap.sico.libertaanticipata.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.ordinescarcerazione.action.ICostantiOrdineScarcerazione;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioComunicazioneRimediRisarcitori
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Comunicazione concessione Rimedi Risarcitore DL 92/2014
 * </p>
 *
 * @version 1.0
 * @since 10/2014
 */
@SuppressWarnings("unchecked")
public class ActDettaglioOSRimediRisarcitori extends ActSIESDettaglioProvvedimento
		implements ICostantiOrdineScarcerazione, ICostantiLicenzaLibanticipata {

	public String processRequest() throws F3BException {

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);

		setRequestAttribute("eventonotifica", lEveNotMod);

		// ==========================================================================
		// Devo recuperare i dati del provvedimento delle Sorveglianza di Concessione
		// da visualizzare in maschera.
		// ==========================================================================

		// EVENTO (decreto/ordinanza)
		IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoSorvModel = lEventoCtrl
				.ExRicercaEventoByKey(lEveNotMod.getEvento().getEveIdEvento());
		setRequestAttribute("EventoSIUS", lEventoSorvModel);

		// RICERCA DEPOSITO_DECRETO o DEPOSITO_ORDINANZA_PC
		if ("02".equals(lEventoSorvModel.getCodTipoProvvedimento())) {
			IDepositoDecreto lCtrlDepDec = SIUSLookupRemote.getDepositoDecretoRemote();
			DepositoDecretoModel lDepDecMod = lCtrlDepDec
					.ExRicercaDepositoDecretoByEvento(lEventoSorvModel.getIdEvento());
			setRequestAttribute("DepositoDecreto", lDepDecMod);
		} else if ("03".equals(lEventoSorvModel.getCodTipoProvvedimento())) {
			IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			DepositoOrdinanzaPcModel lDepOrdMod = lCtrlDep
					.ExRicercaDepositoOrdinanzaPcByEvento(lEventoSorvModel.getIdEvento());
			setRequestAttribute("DepositoOrdinanzaPc", lDepOrdMod);
		}

		// RICERCA LICENZA e PERIODI
		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		Vector<LicenzaPeriodiLibAnticipataModel> lLicenzePeriodi = lCtrlLib
				.ExRicercaLicenzeLibanticipataByEve(lEventoSorvModel.getIdEvento());
		setRequestAttribute("LicenzePeriodi", lLicenzePeriodi);

		// ==========================================================================
		// Posizione Giuridica - Luogo di detenzione
		// ==========================================================================
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento,
						lEveNotMod.getEvento().getFasSieIdFascicoloSiep());
		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Al Procedimento non ha una Posizione Giuridica");

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ==========================================================================
		// Recupero la pena residua da visualizzare in maschera
		// ==========================================================================
		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento,
				lEveNotMod.getEvento().getFasSieIdFascicoloSiep());
		setRequestAttribute("penaresidua", llPenMod);

		//
		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveNotMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		return PG_DETTAGLIO_ORDINE_SCARCERAZIONE_DL92;
	}

}