package siap.sius.impugnazione.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciOpposizione
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Opposizione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Intersistemi
 * </p>
 * 
 * @since 01/06/2014
 */
public class ActLoadInserisciOpposizione extends ActionSius implements ICostantiImpugnazione {
	/**
	 * Action per la load per l'inserimento delle Opposizione TDS e UDS. La Action viene chiamata sia per
	 * l'inserimento che in caso di inserimento Ulteriore Opposizione
	 */
	public String processRequest() throws Exception {

		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Procedimento non selezionato");

		this.setLinkRitorno();

		// Recupero FascicoloSiusGP.
//		FascicoloGPModel fascicoloSiusGP = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// ==========================
		// Recupero dell'Evento.
		// ==========================
		IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrlEv
				.ExRicercaEventoByKey(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		setRequestAttribute("provvedimento", lEveMod);

		// ScadenzarioSiusModel lScaMod = null;
		// IScadenzarioSius lCtrl = SIUSLookupRemote.getScadenzarioRemote();
		// lScaMod = lCtrl.ExRicercaScadenzarioSiusByIdFascicoloTipo
		// (fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius(),
		// ICostantiImpugnazione.COD_TIPO_SCADENZARIO);

		// ==========================================================================
		// Recupero dell'Ordinanza/Decreto.
		// ==========================================================================
		String aNumOrdDec = "", aDataOrdDec = "", aDataDeposito = "";

		if (lEveMod.getCodTipoProvvedimento().compareTo("03") == 0) {
			IDepositoOrdinanzaPc lCtrlDO = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			DepositoOrdinanzaPcModel lDOMod = lCtrlDO.ExRicercaDepositoOrdinanzaPcByEvento(lEveMod
					.getIdEvento());
			aNumOrdDec = lDOMod.getAnnoS3().toString() + "/" + lDOMod.getNumS3().toString();
			aDataOrdDec = DateUtils.getDateToString(lEveMod.getDataEmissione(), "dd-MM-yyyy");
			aDataDeposito = DateUtils.getDateToString(lDOMod.getDataDeposito(), "dd-MM-yyyy");
		} else if (lEveMod.getCodTipoProvvedimento().compareTo("02") == 0) {
			IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
			DepositoDecretoModel lDDMod = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(lEveMod.getIdEvento());
			aNumOrdDec = lDDMod.getAnnoS72().toString() + "/" + lDDMod.getNumS72().toString();
			aDataOrdDec = DateUtils.getDateToString(lDDMod.getDataEmissione(), "dd-MM-yyyy");
			aDataDeposito = DateUtils.getDateToString(lDDMod.getDataDeposito(), "dd-MM-yyyy");
		}

		setRequestAttribute("numOrdDec", aNumOrdDec);
		setRequestAttribute("dataOrdDec", aDataOrdDec);
		setRequestAttribute("dataDeposito", aDataDeposito);

		// ===============================
		// Imposta ComboBOX Tipo Ricorso.
		// ===============================
		Option lOption = null;
		lOption = new Option(DecodificheManager.getInstance().getTipoRicorso());
		lOption.setFilter("04");
		setRequestAttribute("tipoRicorso", "" + lOption);

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		// =======================================
		// Imposta ComboBOX Soggetto Impugnante.
		// =======================================
		lOption = new Option(DecodificheManager.getInstance().getSoggettoImpugnante());

		if ("TDS".equals(strCodTipoUfficio)) {
			String[] lFilterSoggetto = { "01", "03", "05" };
			lOption.setFilter(lFilterSoggetto);
		}
		// MEV10-s3: aggiunte condizioni per gestire i minorenni
		else if ("TDSM".equals(strCodTipoUfficio) || "UDSM".equals(strCodTipoUfficio)) {
			String[] lFilterSoggetto = { "01", "05", "06" };
			lOption.setFilter(lFilterSoggetto);
		} else {
			String[] lFilterSoggetto = { "01", "02", "05" };
			lOption.setFilter(lFilterSoggetto);
		}
		setRequestAttribute("soggettoImpugnante", "" + lOption);

		// ============================================
		// Imposta ComboBOX Tenore Decisione Ricorso.
		// ============================================
		lOption = new Option(DecodificheManager.getInstance().getTenoreDecisioneRicorso());

		if ("TDS".equals(strCodTipoUfficio)) {
			String[] lFilterTDR = { "-", "05", "10", "11", "12" };
			lOption.setFilter(lFilterTDR);
		} else {
			String[] lFilterTDR = { "-", "05", "10", "11", "12", "13" }; // solo x UDS 13 - Converte in
																			// impugnazione al TDS
			lOption.setFilter(lFilterTDR);
		}

		setRequestAttribute("tenoreDecisioneRicorso", "" + lOption);

		setRequestAttribute("tipoUfficio", strCodTipoUfficio);

		return PG_LOAD_INSERISCI_OPPOSIZIONE;
	}

}
