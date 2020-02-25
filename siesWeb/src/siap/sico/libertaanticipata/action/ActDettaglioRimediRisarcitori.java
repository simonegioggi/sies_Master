package siap.sico.libertaanticipata.action;

/**
 * <p>Title: ActDettaglioRimediRisarcitori</p>
 * <p>Description: Classe Action per il dettaglio dei dati del provvedimento SIUS
 *    di concessione Rimedi Risarcitori DL92/2014</p>
 *
 * @since 10/2014
 */

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

public class ActDettaglioRimediRisarcitori extends ActionSiap implements ICostantiLibertaAnticipata {
	/**
	 * Azione di Dettaglio del Liberazione Anticipata
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		// ==========================================================================
		// Recupera i dati del provvedimento della sorveglianza da visualizzare
		// - EVENTO
		// - DEPOSITO_ORDINANZA_PC o DEPOSITO_DECRETO
		// - TENORE (NO non contiene dati utili da visualizzare)
		// - LICENZA_LIBANTICIPATA
		// - PERIODO_LIBANTICIPATA
		// ==========================================================================
		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();

		// Recupero l'EVENTO
		BigDecimal lIdEvento = null;
		if (!this.isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)) {
			lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		} else {
			throw new F3BException(F3BException.USER_MESSAGE, "ID_EVENTO non passato ");
		}

		// EVENTO
		IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lEventoCtrl.ExRicercaEventoByKey(lIdEvento);
		setRequestAttribute("EventoSIUS", lEventoModel);

		// RICERCA DEPOSITO_DECRETO o DEPOSITO_ORDINANZA_PC
		if ("02".equals(lEventoModel.getCodTipoProvvedimento())) {
			IDepositoDecreto lCtrlDepDec = SIUSLookupRemote.getDepositoDecretoRemote();
			DepositoDecretoModel lDepDecMod = lCtrlDepDec.ExRicercaDepositoDecretoByEvento(lIdEvento);
			setRequestAttribute("DepositoDecreto", lDepDecMod);
		} else if ("03".equals(lEventoModel.getCodTipoProvvedimento())) {
			IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			DepositoOrdinanzaPcModel lDepOrdMod = lCtrlDep.ExRicercaDepositoOrdinanzaPcByEvento(lIdEvento);
			setRequestAttribute("DepositoOrdinanzaPc", lDepOrdMod);
		}

		// RICERCA LICENZA e PERIODI
		Vector<LicenzaPeriodiLibAnticipataModel> lLicenzePeriodi = lCtrlLib
				.ExRicercaLicenzeLibanticipataByEve(lIdEvento);

		// if(!lLicenzePeriodi.isEmpty())
		// {
		// LicenzaPeriodiLibAnticipataModel lLicenzaPeriodiModel =
		// (LicenzaPeriodiLibAnticipataModel)lLicenzePeriodi.firstElement();
		// String lCodUffEmi = lLicenzaPeriodiModel.getLicenza().getCodUfficioEmittente();
		//
		// UfficioModel lUffMod = this.getUfficioByCodUfficio(lCodUffEmi);
		//
		// lLicenzaPeriodiModel.getLicenza().setDescrUfficioEmittente(lUffMod.getDescrTipoUfficio());
		// }

		setRequestAttribute("LicenzePeriodi", lLicenzePeriodi);

		// ==========================================================================
		// Recupero gli altri dati relativi al fascicolo corrente da visualizzare
		// sul dettaglio
		// ==========================================================================
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// RICERCA POSIZIONE GURIDICA
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		lPos = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);
		setRequestAttribute("posizioneluogoaltra", lPos);

		// PENA RESIDUA
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
		setRequestAttribute("penaresidua", lPenaResidua);

		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		// PENA COMPLESSIVA
		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		String lFlagErgastolo = "N";
		// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		if (lPenComMod.getCodTipoPenaDetentiva() != null
				&& lPenComMod.getCodTipoPenaDetentiva() != ""
				&& (lPenComMod.getCodTipoPenaDetentiva().equals("03") || lPenComMod.getCodTipoPenaDetentiva()
						.equals("04"))) {
			lFlagErgastolo = "S";
		}

		setRequestAttribute("flagergastolo", lFlagErgastolo);

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UfficioModel lUfficioMod = this.getUfficioUtenteConnesso();
		String lCodTipoUfficio = lUfficioMod.getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		return PG_LOAD_DETTAGLIO_RIMEDI_RISARCIORI;
	}

}