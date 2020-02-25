package siap.sius.impugnazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActRicercaImpugnazioniDelProvvedimento
 * </p>
 * <p>
 * Description: Classe Action per la load dell'Elenco Impugnazioni
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRicercaImpugnazioniDelProvvedimento extends ActionSius implements ICostantiImpugnazione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		Vector lImpugnazioni = null;
		this.setLinkRitorno();

		IImpugnazione lCtrlImp = SIUSLookupRemote.getImpugnazioneRemote();
		IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
		ImpugnazioneModel lImpMod = null;
		EventoModel lEveMod = null;

		// Recupero dell'Evento.
		lEveMod = lCtrlEv
				.ExRicercaEventoByKey(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		// Recupero dell'Ordinanza/Decreto.
		IDepositoOrdinanzaPc lCtrlDO = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
		DepositoOrdinanzaPcModel lDOMod = null;
		DepositoDecretoModel lDDMod = null;
		BigDecimal lIdProv = null;

		if (lEveMod.getCodTipoProvvedimento().compareTo("03") == 0) {
			lDOMod = lCtrlDO.ExRicercaDepositoOrdinanzaPcByEvento(lEveMod.getIdEvento());
			lIdProv = lDOMod.getIdDepositoOrdinanzaPc();
		} else if (lEveMod.getCodTipoProvvedimento().compareTo("02") == 0) {
			lDDMod = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(lEveMod.getIdEvento());
			lIdProv = lDDMod.getIdDepositoDecreto();
		}

		// Ricerca eventuali impugnazioni.
		if (lImpMod == null || lImpMod.getDataAnnullamento() == null) {
			Option lOptTipoRicorso = new Option(DecodificheManager.getInstance().getTipoRicorso());
			String[] lFilterRicorso = { "01", "02", "03" };
			lOptTipoRicorso.setFilter(lFilterRicorso);

			lImpugnazioni = lCtrlImp.ExRicercaImpugnazioniDelProvvedimento(lIdProv,
					lEveMod.getCodTipoProvvedimento(), lOptTipoRicorso.getCodes(), null);

			setRequestAttribute("impugnazioni", lImpugnazioni);
		}

		BigDecimal countImpugnazioni = new BigDecimal(lImpugnazioni.size());
		setRequestAttribute("numero_Impugnazioni", countImpugnazioni.toString());

		// Si passa nella request lo scadenzario l'evento e il Decreto/DepositoOrdinanzaPc.
		// setRequestAttribute("scadenzario", lScaMod);
		setRequestAttribute("provvedimento", lEveMod);
		String aNumOrdDec = "", aDataOrdDec = "", aDataDeposito = "";
//		String aIdDecreto = "", aIdOrdinanza = "";
		if (lEveMod.getCodTipoProvvedimento().compareTo("02") == 0
				&& (lDDMod != null && lDDMod.getAnnoS72() != null && lDDMod.getNumS72() != null)) {
			aNumOrdDec = lDDMod.getAnnoS72().toString() + "/" + lDDMod.getNumS72().toString();
			aDataOrdDec = DateUtils.getDateToString(lDDMod.getDataEmissione(), "dd-MM-yyyy");
			aDataDeposito = DateUtils.getDateToString(lDDMod.getDataDeposito(), "dd-MM-yyyy");
//			aIdDecreto = lDDMod.getIdDepositoDecreto().toString();
		}
		if (lEveMod.getCodTipoProvvedimento().compareTo("03") == 0
				&& (lDOMod != null && lDOMod.getAnnoS3() != null && lDOMod.getNumS3() != null)) {
			aNumOrdDec = lDOMod.getAnnoS3().toString() + "/" + lDOMod.getNumS3().toString();
			aDataOrdDec = DateUtils.getDateToString(lEveMod.getDataEmissione(), "dd-MM-yyyy");
			aDataDeposito = DateUtils.getDateToString(lDOMod.getDataDeposito(), "dd-MM-yyyy");
//			aIdOrdinanza = lDOMod.getIdDepositoOrdinanzaPc().toString();
		}
		setRequestAttribute("numOrdDec", aNumOrdDec);
		setRequestAttribute("dataOrdDec", aDataOrdDec);
		setRequestAttribute("dataDeposito", aDataDeposito);

		ModificabileStampabile(lEveMod);

		// Imposta ComboBOX Tipo Ricorso.
		Option lOption = null;
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		if (Utils.isNullObj(lImpMod))
			lOption = new Option(DecodificheManager.getInstance().getTipoRicorso());
//		else
//			lOption = new Option(DecodificheManager.getInstance().getTipoRicorso(),
//					lImpMod.getCodTipoImpugnazione());

		// Per il TDS si vuole filtrare per High_Value = "tds"
		if (strCodTipoUfficio.equals("TDS"))
			lOption.setFilter("01");

		setRequestAttribute("tipoRicorso", "" + lOption);
		setRequestAttribute("tipoUfficio", "" + strCodTipoUfficio);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_ELENCOIMPUGNAZIONI;
	}

	// Funzione per la costruzione della combo con i template di stampa previsti
//	private void gestioneTemplate(BigDecimal alIdEvento) throws Exception {
//
//		Option lOptTemplate = null;
//		lOptTemplate = UtilTemplate.listaTemplateByCodProvv("15", "0");
//		setRequestAttribute("ElencoTemplate", "" + lOptTemplate);
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("ElencoTemplate -> " + lOptTemplate);
//		return;
//	}

	private void ModificabileStampabile(EventoModel lEvento) throws Exception {

		String lStampabile = "NO";
//		String lModificabile = "NO";

		if (IsFascicoloSiusModificabile()) {
//			lModificabile = "SI";
			if (lEvento.getFlagDocumentoRegistrato() == null
					|| lEvento.getFlagDocumentoRegistrato().compareTo("N") == 0)
				lStampabile = "SI";
		}
		// Modificabile coincide con stampabile !!
		setRequestAttribute("Modificabile", lStampabile);
		setRequestAttribute("Stampabile", lStampabile);
	}

}