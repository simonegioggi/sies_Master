package siap.sico.libertaanticipata.action;

/**
 * <p>Title: ActLoadInserisciComunicazioneRimediRisarcitori</p>
 * <p>Description: Classe Action per la load inserimento Comunicazione Rimedi Risarcitori
 *  DL 92/2014 Lierbo, Ergastolo, pena terminata (forse)</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
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
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciComunicazioneRimediRisarcitori extends ActionSiap
		implements ICostantiLicenzaLibanticipata {

	public String processRequest() throws Exception {

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		this.isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Controllo Validazione Fascicolo
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Controllo Fascicolo definito
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();

		// ==========================================================================
		// Posizione Giuridica
		// ==========================================================================
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosizione = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosizione = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (lPosizione == null || lPosizione.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("posizioneluogoaltra", lPosizione);

		// ==========================================================================
		// Pena Complessiva
		// ==========================================================================
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		String lFlagErgastolo = "N";
		// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		if (lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "") {
			if (lPenComMod.getCodTipoPenaDetentiva().equals("03")) {
				lFlagErgastolo = "S";
			} else if (lPenComMod.getCodTipoPenaDetentiva().equals("04")) {
				lFlagErgastolo = "D";
			}
		}

		setRequestAttribute("penacomplessiva", lPenComMod);
		setRequestAttribute("flagErgastolo", lFlagErgastolo);

		// ==============================================
		// Pena Residua
		// ==============================================
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		PenaResiduaModel lPenaResidua = null;
		if (!lPosizione.getPosizioneGiuridica().isLibero()) {
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
		} else {
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		}

		setRequestAttribute("penaresidua", lPenaResidua);

		// ==========================================================================
		// Verifico se è possibile effettare l'inserimento della comunicazione
		// ==========================================================================
		boolean lFlagLiberoDataFine = false;
		if (lPenaResidua != null && lPenaResidua.getDataFine() != null
				&& (DateUtils.isGreater(DateUtils.getSysDate(), lPenaResidua.getDataFine())
						&& !DateUtils.isEquals(lPenaResidua.getDataFine(), DateUtils.getSysDate()))) {
			lFlagLiberoDataFine = true;
		}

		if (lFlagErgastolo.equals("N") && !lPosizione.getPosizioneGiuridica().isLibero()
				&& !lFlagLiberoDataFine) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Il condannato non è in Ergastolo, non è Libero e non ha Pena scaduta. Impossibile eseguire la richiesta.");
		}

		// ==========================================================================
		// Devo recuperare i dati del provvedimento delle Sorveglianza di Concessione
		// da visualizzare in maschera.
		// ==========================================================================

		// Recupero l'EVENTO
		BigDecimal lIdEvento = null;
		if (!this.isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)) {
			lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		} else {
			throw new F3BException(F3BException.USER_MESSAGE, "ID_EVENTO non passato ");
		}

		// EVENTO (decreto/ordinanza)
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
		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		Vector<LicenzaPeriodiLibAnticipataModel> lLicenzePeriodi = lCtrlLib
				.ExRicercaLicenzeLibanticipataByEve(lIdEvento);
		setRequestAttribute("LicenzePeriodi", lLicenzePeriodi);

		UfficioModel lUfficioSIUS = this.getUfficioByCodUfficio(lEventoModel.getCodUfficioEmittente());
		setRequestAttribute("UfficioSIUS", lUfficioSIUS);

		// ==========================================================================
		// Dati per le combo
		// ==========================================================================
		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagiMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		if (lMagiMod != null)
			setRequestAttribute("magistratocompetente", lMagiMod);

		// Notifica per l'esecuzione
		Option lOptionE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaE", "" + lOptionE);

		// Notifica agli avvocati (22=UNEP)
		Option lOptionN = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOptionN);

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		return PG_LOAD_INSERISCI_COMUNICAZIONE_DL92_LIBERO;
	}

}