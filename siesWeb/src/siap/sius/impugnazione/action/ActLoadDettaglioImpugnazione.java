package siap.sius.impugnazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.util.UtilTemplate;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioImpugnazione
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Impugnazione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadDettaglioImpugnazione extends ActionSius implements ICostantiImpugnazione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		Vector lImpAnnullate = null;
		this.setLinkRitorno();

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		IImpugnazione lCtrl = SIUSLookupRemote.getImpugnazioneRemote();
		IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
		ImpugnazioneModel lImpMod = null;
		EventoModel lEveMod = null;

		if (!isRequestParameterNullObj(CAMPO_ID_IMPUGNAZIONE)) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					CAMPO_ID_IMPUGNAZIONE + ": " + getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
			lImpMod = lCtrl.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
		} else {
			Option lOptTipoRicorso = new Option(DecodificheManager.getInstance().getTipoRicorso());
			String[] lFilterRicorso = { "01", "02", "03" };
			lOptTipoRicorso.setFilter(lFilterRicorso);
			lImpMod = lCtrl.ExRicercaImpugnazioneByIdEventoTipoProvv(
					getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO),
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO),
					lOptTipoRicorso.getCodes(), null);
		}

		setRequestAttribute("impugnazione", lImpMod);

		// Recupero dell'Evento.
		lEveMod = lCtrlEv
				.ExRicercaEventoByKey(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		// Elaborazione dei template.
		gestioneTemplate(lEveMod.getIdEvento(), strCodTipoUfficio);

		// Recupero dell'Ordinanza/Decreto/Sentenza.
		IDepositoOrdinanzaPc lCtrlDO = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
		IDepositoSentenza lCtrlDS = SIUSLookupRemote.getDepositoSentenzaRemote();
		DepositoOrdinanzaPcModel lDOMod = null;
		DepositoDecretoModel lDDMod = null;
		DepositoSentenzaModel lDSMod = null;
		BigDecimal lIdProv = null;

		if (lEveMod.getCodTipoProvvedimento().compareTo("03") == 0) {
			lDOMod = lCtrlDO.ExRicercaDepositoOrdinanzaPcByEvento(lEveMod.getIdEvento());
			lIdProv = lDOMod.getIdDepositoOrdinanzaPc();
		} else if (lEveMod.getCodTipoProvvedimento().compareTo("02") == 0) {
			lDDMod = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(lEveMod.getIdEvento());
			lIdProv = lDDMod.getIdDepositoDecreto();
		} else if (lEveMod.getCodTipoProvvedimento().compareTo("01") == 0) {
			lDSMod = lCtrlDS.ExRicercaDepositoSentenzaByEvento(lEveMod.getIdEvento());
		}

		// Ricerca eventuali impugnazioni annullate solo se l'impugnazione stessa non annullata
		if (lImpMod == null || lImpMod.getDataAnnullamento() == null) {
			lImpAnnullate = lCtrl.ExRicercaImpugnazioniAnnullateByProv(lIdProv,
					lEveMod.getCodTipoProvvedimento());
			setRequestAttribute("InpAnnullate", lImpAnnullate);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Annulati");
		}

		// Si passa nella request lo scadenzario l'evento e il Decreto/DepositoOrdinanzaPc.
		// setRequestAttribute("scadenzario", lScaMod);
		setRequestAttribute("provvedimento", lEveMod);
		String aNumOrdDec = "", aDataOrdDec = "", aDataDeposito = "";
		// String aIdDecreto = "", aIdOrdinanza = "", aIdSentenza;
		if (lEveMod.getCodTipoProvvedimento().compareTo("02") == 0
				&& (lDDMod != null && lDDMod.getAnnoS72() != null && lDDMod.getNumS72() != null)) {
			aNumOrdDec = lDDMod.getAnnoS72().toString() + "/" + lDDMod.getNumS72().toString();
			aDataOrdDec = DateUtils.getDateToString(lDDMod.getDataEmissione(), "dd-MM-yyyy");
			aDataDeposito = DateUtils.getDateToString(lDDMod.getDataDeposito(), "dd-MM-yyyy");
			// aIdDecreto = lDDMod.getIdDepositoDecreto().toString();
		}
		if (lEveMod.getCodTipoProvvedimento().compareTo("03") == 0
				&& (lDOMod != null && lDOMod.getAnnoS3() != null && lDOMod.getNumS3() != null)) {
			aNumOrdDec = lDOMod.getAnnoS3().toString() + "/" + lDOMod.getNumS3().toString();
			aDataOrdDec = DateUtils.getDateToString(lEveMod.getDataEmissione(), "dd-MM-yyyy");
			aDataDeposito = DateUtils.getDateToString(lDOMod.getDataDeposito(), "dd-MM-yyyy");
			// aIdOrdinanza = lDOMod.getIdDepositoOrdinanzaPc().toString();
		}
		if (lEveMod.getCodTipoProvvedimento().compareTo("01") == 0
				&& (lDSMod != null && lDSMod.getAnnoSentenza() != null && lDSMod.getNumSentenza() != null)) {
			aNumOrdDec = lDSMod.getAnnoSentenza().toString() + "/" + lDSMod.getNumSentenza().toString();
			aDataOrdDec = DateUtils.getDateToString(lEveMod.getDataEmissione(), "dd-MM-yyyy");
			aDataDeposito = DateUtils.getDateToString(lDSMod.getDataDeposito(), "dd-MM-yyyy");
			// aIdSentenza = lDSMod.getIdDepositoSentenza().toString();
		}

		setRequestAttribute("numOrdDec", aNumOrdDec);
		setRequestAttribute("dataOrdDec", aDataOrdDec);
		setRequestAttribute("dataDeposito", aDataDeposito);

		ModificabileStampabile(lEveMod);

		// Imposta ComboBOX Tipo Ricorso.
		Option lOption = null;

		if (Utils.isNullObj(lImpMod))
			lOption = new Option(DecodificheManager.getInstance().getTipoRicorso());
		else
			lOption = new Option(DecodificheManager.getInstance().getTipoRicorso(),
					lImpMod.getCodTipoImpugnazione());

		// Per il TDS si vuole filtrare per High_vALUE = "tds"
		if (strCodTipoUfficio.equals("TDS"))
			lOption.setFilter("01");

		setRequestAttribute("tipoRicorso", "" + lOption);
		setRequestAttribute("tipoUfficio", "" + strCodTipoUfficio);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_LOAD_DETTAGLIOIMPUGNAZIONE;
	}

	// Funzione per la costruzione della combo con i template di stampa previsti
	private void gestioneTemplate(BigDecimal alIdEvento, String strCodTipoUfficio) throws Exception {
		Option lOptTemplate = null;
		if (strCodTipoUfficio.equals("TDSM") || strCodTipoUfficio.equals("UDSM")) {
			lOptTemplate = UtilTemplate.listaTemplateByCodProvv("15", "2");
		} else {
			lOptTemplate = UtilTemplate.listaTemplateByCodProvv("15", "0");
		}
		setRequestAttribute("ElencoTemplate", "" + lOptTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ElencoTemplate -> " + lOptTemplate);
		return;
	}

	private void ModificabileStampabile(EventoModel lEvento) throws Exception {

		String lStampabile = "NO";
		// String lModificabile = "NO";

		if (IsFascicoloSiusModificabile()) {
			// lModificabile = "SI";
			if (lEvento.getFlagDocumentoRegistrato() == null
					|| lEvento.getFlagDocumentoRegistrato().compareTo("N") == 0)
				lStampabile = "SI";
		}
		// Modificabile coincide con stampabile !!
		setRequestAttribute("Modificabile", lStampabile);
		setRequestAttribute("Stampabile", lStampabile);
	}

}