package siap.sico.libertaanticipata.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.action.ICostantiFungibilita;
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
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * Action per il caricamento della form dell'Ordine di scarcerazione per nuovo fine pena a seguito concessione
 * DL92 in caso di soggetto in espiazione
 * 
 * @author d.fiorletta
 * @since 10/2014
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciOSRimediRisarcitori extends ActionSiap implements ICostantiLicenzaLibanticipata {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciOSRimediRisarcitori    ");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// ==========================================================================
		// Recupero i dati del provvedimento della Sorveglianza da visualizzare
		// maschera
		// ==========================================================================
		// Recupero l'EVENTO
		BigDecimal lIdEvento = null;
		if (!this.isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO)) {
			lIdEvento = getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO);
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
		// Recupero i dati del Fascicolo da visualizzare
		// - Posizione Luogo Altra
		// - Pena Residua rideterminata
		// ==========================================================================
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
		// Recupero la Pena Residua rideterminata
		// n.b. è stata agganciata all'ordinanza/deceto in fase di calcolo (schifo)
		// ==========================================================================
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		PenaResiduaModel lPenaResidua = null;
		lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);

		setRequestAttribute("penaresidua", lPenaResidua);

		//
		String lIdFungibilita = null;
		if (!isRequestParameterNullObj(ICostantiFungibilita.CAMPO_ID_FUNGIBILITA))
			lIdFungibilita = getRequestStringParameter(ICostantiFungibilita.CAMPO_ID_FUNGIBILITA);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lIdFungibilita = " + lIdFungibilita);
		setRequestAttribute("IdFungibilita", lIdFungibilita);

		// ==========================================================================
		// Recupero i dati per le combo
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

		return PG_LOAD_INSERISCI_ORDINE_SCARCERAZIONE_DL92;

	}

}