package siap.sius.impugnazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
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
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadModificaImpugnazione
 * </p>
 * <p>
 * Description: Classe Action per la load Modifica di Impugnazione
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
public class ActLoadModificaImpugnazione extends ActionSiap implements ICostantiImpugnazione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Variabile ScadenzarioSiusModel.
		ScadenzarioSiusModel lScaMod = new ScadenzarioSiusModel();

		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Procedimento non selezionato");

		setLinkRitorno();

		// Recupero FascicoloSiusGP.
		// FascicoloGPModel fascicoloSiusGP = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Verifica se esiste già il record di Impugnazione per impostare la Modalità.
		IImpugnazione lCtrlImp = SIUSLookupRemote.getImpugnazioneRemote();
		// 16/11/2007 Prevista la lettura puntuale per chiave "IdImpugnazione".
		ImpugnazioneModel lImpMod = null;
		if (!isRequestParameterNullObj(CAMPO_ID_IMPUGNAZIONE)) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					CAMPO_ID_IMPUGNAZIONE + ": " + getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
			lImpMod = lCtrlImp
					.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
		} else {
			Option lOptTipoRicorso = new Option(DecodificheManager.getInstance().getTipoRicorso());
			String[] lFilterRicorso = { "01", "02", "03" };
			lOptTipoRicorso.setFilter(lFilterRicorso);
			lImpMod = lCtrlImp.ExRicercaImpugnazioneByIdEventoTipoProvv(
					getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO),
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO),
					lOptTipoRicorso.getCodes(), null);
		}
		/*
		 * Controllo di esistenza dell'impugnazione spostato sulla lista provvedimenti.
		 * if(Utils.isNullObj(lImpMod)) { // Verifica sullo Scadenzario se il procedimento è impugnabile.
		 * IScadenzarioSius lCtrl = SIUSLookupRemote.getScadenzarioRemote(); lScaMod =
		 * lCtrl.ExRicercaScadenzarioSiusByIdFascicoloTipo
		 * (fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius(),
		 * ICostantiImpugnazione.COD_TIPO_SCADENZARIO);
		 *
		 * if(Utils.isNullObj(lScaMod)) throw new F3BException( F3BException.USER_MESSAGE,
		 * "Procedimento non Impugnabile" );
		 *
		 * if(Utils.isNullObj(lScaMod.getDataFineScadenza())) throw new F3BException(
		 * F3BException.USER_MESSAGE, "Procedimento non Impugnabile" ); else if
		 * (lScaMod.getDataFineScadenza().before( DateUtils.getSysDate()) ) throw new F3BException(
		 * F3BException.USER_MESSAGE, "Procedimento non Impugnabile: data ricorso scaduta. " );
		 * setRequestAttribute("modalita", "I"); } else { // Si passa nella request l'impugnazione.
		 * setRequestAttribute("impugnazione", lImpMod); setRequestAttribute("modalita", "M"); }
		 */

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
		setRequestAttribute("modalita", "M");

		// Recupero dell'Evento.
		IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrlEv
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
		}

		else if (lEveMod.getCodTipoProvvedimento().compareTo("02") == 0) {
			lDDMod = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(lEveMod.getIdEvento());
			lIdProv = lDDMod.getIdDepositoDecreto();
		}

		// Ricerca eventuali impugnazioni annullate solo se l'impugnazione stessa non annullata
		if (lImpMod == null || lImpMod.getDataAnnullamento() == null) {
			Vector lImpAnnullate = lCtrlImp.ExRicercaImpugnazioniAnnullateByProv(lIdProv,
					lEveMod.getCodTipoProvvedimento());
			setRequestAttribute("InpAnnullate", lImpAnnullate);
		}

		// Si passa nella request lo scadenzario l'evento e il Decreto/DepositoOrdinanzaPc.
		setRequestAttribute("scadenzario", lScaMod);
		setRequestAttribute("provvedimento", lEveMod);
		String aNumOrdDec = "", aDataOrdDec = "", aDataDeposito = "";
		// String aIdDecreto = "", aIdOrdinanza = "";
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
		setRequestAttribute("numOrdDec", aNumOrdDec);
		setRequestAttribute("dataOrdDec", aDataOrdDec);
		setRequestAttribute("dataDeposito", aDataDeposito);

		// Imposta ComboBOX Tipo Ricorso.
		Option lOption = null;
		if (Utils.isNullObj(lImpMod))
			lOption = new Option(DecodificheManager.getInstance().getTipoRicorso());
		else
			lOption = new Option(DecodificheManager.getInstance().getTipoRicorso(),
					lImpMod.getCodTipoImpugnazione());

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		// Per il TDS/TDSM/UDSM si vuole filtrare per High_vALUE = "tds"
		// Nella combo viene visualizzato solo la descrizione "Ricorso"
		if (strCodTipoUfficio.compareTo("TDS") == 0 || strCodTipoUfficio.compareTo("TDSM") == 0
				|| strCodTipoUfficio.compareTo("UDSM") == 0) {
			lOption.setFilter("01");
		}

		setRequestAttribute("tipoRicorso", "" + lOption);

		// Imposta ComboBOX Soggetto Impugnante.
		if (Utils.isNullObj(lImpMod))
			lOption = new Option(DecodificheManager.getInstance().getSoggettoImpugnante());
		else
			lOption = new Option(DecodificheManager.getInstance().getSoggettoImpugnante(),
					lImpMod.getSoggettoImpugnante());
		String[] lFilterSogImp = null;
		// 20171002: [SG] aggiunta casistica UDS in or condition
		if ("TDS".compareTo(strCodTipoUfficio) == 0 || "UDS".equals(strCodTipoUfficio)
				|| "TDSM".compareTo(strCodTipoUfficio) == 0 || "UDSM".equals(strCodTipoUfficio)) {
			// MEV_65: aggiunto codice "09" --> Avvocatura Distrettuale dello Stato
			lFilterSogImp = new String[] { "01", "02", "03", "04", "05", "07", "08", "09" };
		} else {
			lFilterSogImp = new String[] { "01", "03", "04", "05", "06", "07", "08" };
		}
		lOption.setFilter(lFilterSogImp);
		setRequestAttribute("soggettoImpugnante", "" + lOption);

		// Imposta ComboBOX Tipo DecisioneCassazione.
		if (Utils.isNullObj(lImpMod))
			lOption = new Option(DecodificheManager.getInstance().getTenoreDecisioneRicorso());
		else
			lOption = new Option(DecodificheManager.getInstance().getTenoreDecisioneRicorso(),
					lImpMod.getCodTenoreDecisione());

		if (strCodTipoUfficio.compareTo("TDS") == 0) {
			String[] lFilterTDR = { "-", "01", "02", "03", "04", "05", "06", "08" };
			lOption.setFilter(lFilterTDR);
		}
		setRequestAttribute("tenoreDecisioneRicorso", "" + lOption);

		// Imposta ComboBOX Autorita Destinataria
		// 25/05/2009 Impostata in base al valore precedente.
		if (Utils.isNullObj(lImpMod))
			lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		else
			lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(),
					lImpMod.getCodAutoritaDestinataria());

		String[] lFilterTDS = { "CSS" };
		String[] lFilterUDS = { "CSS", "TDS" };
		if (strCodTipoUfficio.equals("TDS"))
			lOption.setFilter(lFilterTDS);
		else
			lOption.setFilter(lFilterUDS);
		setRequestAttribute("ListaUffici", "" + lOption);

		setRequestAttribute("tipoUfficio", strCodTipoUfficio);

		return PG_LOAD_MODIFICAIMPUGNAZIONE; // restituisce la jsp di VIEW
	}

}