package siap.sius.impugnazione.action;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadModificaOpposizione
 * </p>
 * <p>
 * Description: Classe Action per la load Modifica di Opposizione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * 
 * @since 06/2014
 */
public class ActLoadModificaOpposizione extends ActionSiap implements ICostantiImpugnazione {

	public String processRequest() throws Exception {

		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Procedimento non selezionato");

		setLinkRitorno();

		// Recupero FascicoloSiusGP.
		// FascicoloGPModel fascicoloSiusGP = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");

		// Riecupera l'impugnazione
		IImpugnazione lCtrlImp = SIUSLookupRemote.getImpugnazioneRemote();
		ImpugnazioneModel lImpMod = null;
		lImpMod = lCtrlImp.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "Impugnazione",
				lImpMod.getIdImpugnazione().toString(), getCodUtenteConnesso(), getSession().getId());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il/la  " + lck.getEntity()
					+ " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// Si passa nella request l'impugnazione.
		setRequestAttribute("impugnazione", lImpMod);

		// Recupero dell'Evento.
		IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrlEv
				.ExRicercaEventoByKey(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		setRequestAttribute("provvedimento", lEveMod);

		// Recupero dell'Ordinanza/Decreto.
		String aNumOrdDec = "", aDataOrdDec = "", aDataDeposito = "";
		if (lEveMod.getCodTipoProvvedimento().compareTo("03") == 0) {
			IDepositoOrdinanzaPc lCtrlDO = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			DepositoOrdinanzaPcModel lDOMod = null;
			lDOMod = lCtrlDO.ExRicercaDepositoOrdinanzaPcByEvento(lEveMod.getIdEvento());

			aNumOrdDec = lDOMod.getAnnoS3().toString() + "/" + lDOMod.getNumS3().toString();
			aDataOrdDec = DateUtils.getDateToString(lEveMod.getDataEmissione(), "dd-MM-yyyy");
			aDataDeposito = DateUtils.getDateToString(lDOMod.getDataDeposito(), "dd-MM-yyyy");
		} else if (lEveMod.getCodTipoProvvedimento().compareTo("02") == 0) {
			IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
			DepositoDecretoModel lDDMod = null;
			lDDMod = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(lEveMod.getIdEvento());

			aNumOrdDec = lDDMod.getAnnoS72().toString() + "/" + lDDMod.getNumS72().toString();
			aDataOrdDec = DateUtils.getDateToString(lDDMod.getDataEmissione(), "dd-MM-yyyy");
			aDataDeposito = DateUtils.getDateToString(lDDMod.getDataDeposito(), "dd-MM-yyyy");
		}

		setRequestAttribute("numOrdDec", aNumOrdDec);
		setRequestAttribute("dataOrdDec", aDataOrdDec);
		setRequestAttribute("dataDeposito", aDataDeposito);

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		// =================================
		// Imposta ComboBOX Tipo Ricorso.
		// =================================
		Option lOption = null;
		lOption = new Option(DecodificheManager.getInstance().getTipoRicorso(),
				lImpMod.getCodTipoImpugnazione());
		lOption.setFilter("04");
		setRequestAttribute("tipoRicorso", "" + lOption);

		// ========================================
		// Imposta ComboBOX Soggetto Impugnante.
		// ========================================
		lOption = new Option(DecodificheManager.getInstance().getSoggettoImpugnante(),
				lImpMod.getSoggettoImpugnante());
		if ("TDS".equals(strCodTipoUfficio)) {
			String[] lFilterSoggetto = { "01", "03", "05" };
			lOption.setFilter(lFilterSoggetto);
		} else {
			String[] lFilterSoggetto = { "01", "02", "05" };
			lOption.setFilter(lFilterSoggetto);
		}
		setRequestAttribute("soggettoImpugnante", "" + lOption);

		// ================================
		// Imposta ComboBOX Tipo Tenore.
		// ================================
		lOption = new Option(DecodificheManager.getInstance().getTenoreDecisioneRicorso(),
				lImpMod.getCodTenoreDecisione());
		// String[] lFilterTDR = {"-","10","11","12","13"};
		// lOption.setFilter( lFilterTDR );
		if ("TDS".equals(strCodTipoUfficio)) {
			String[] lFilterTDR = { "-", "05", "10", "11", "12" };
			lOption.setFilter(lFilterTDR);
		} else {
			String[] lFilterTDR = { "-", "05", "10", "11", "12", "13" }; // solo x UDS 13 - Converte in
																			// impugnazione al TDS
			lOption.setFilter(lFilterTDR);
		}

		setRequestAttribute("tenoreDecisioneRicorso", "" + lOption);

		// setta la modalita: M = modifica, A = aggiornamento
		setRequestAttribute("modalita", "M");

		return PG_LOAD_MODIFICA_OPPOSIZIONE;
	}

}