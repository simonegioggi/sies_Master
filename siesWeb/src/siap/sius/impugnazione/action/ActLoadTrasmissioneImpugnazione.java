package siap.sius.impugnazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.cssa.controller.ICSSA;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadTrasmissioneImpugnazione
 * </p>
 * <p>
 * Description: Trasferisce l'ordinanza/decreto (compreso l'impugnazione) verso la Procura
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActLoadTrasmissioneImpugnazione extends ActionSiap implements ICostantiImpugnazione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Si prelevano dalla request i parametri del Ricorso appena iscritto/Letto.
//		BigDecimal lImpId = getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE);
		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		String lTipoProvvedimento = getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO);
		String lTipoUfficio = getUfficioUtenteConnesso().getCodUfficio();
		String lPage = "";

		// Fascicolo SIUS in sessione.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel(
				(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));

		// Recupero degli uffici Interessati all'Impugnazione.
		// Se l'impugnazione riguarda una Misura Alternativa o il Proc. SIUS è Unificante o esistono
		// "RIFASIEP", possono esistere più uffici interessati all'impugnazione!
		IUfficio lUctrl = SICOLookupRemote.getUfficioRemote();
		// MEV_39 03/01/2018 modificata firma metodo
		Vector lUfficiInteressati = lUctrl.ListaUfficiInteressatiProvvedimento(lFasGPMod
				.getFascicoloSiusModel().getIdFascicoloSius(), lFasGPMod.getFascicoloSiusModel()
				.getNumeroFascicoliUnificati(), null);
		setRequestAttribute("ufficiInteressati", lUfficiInteressati);

		if (lTipoProvvedimento.compareTo("03") == 0) { // ORDINANZA
			// DATI RELATIVI ALL'ORDINANZA
			OrdinanzaEventoTenoriPrescrizioniModel lOrdEveTenPreMod = new OrdinanzaEventoTenoriPrescrizioniModel();
			IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			lOrdEveTenPreMod = lCtrlDep.ExRicercaOrdinanzaEventoTenoriPrescrizioniByIdEvento(lEveId);

			setRequestAttribute("datiOrdinanza", lOrdEveTenPreMod);

			// Si Individua tra i Tenori Presenti quello che ha il campo CodEsitoTenore uguale a CodMotivo
			// dell'evento associato.
			TenoreModel[] lTenori = lOrdEveTenPreMod.getTenori();
			EventoModel lEve = lOrdEveTenPreMod.getEvento();
			if (lTenori != null && lEve != null) {
				for (int i = 0; i < lTenori.length; i++) {
					if (lTenori[i].getCodOggettoTenore().equals(lEve.getCodMotivo())) {
						setRequestAttribute("tenoreEsito", lTenori[i]);
						break;
					}
				}
			}

			// Si Cerca l'evenutale ufficio del Magistrato di Sorveglianza
			if (lOrdEveTenPreMod.getOrdinanza() != null
					&& lOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp() != null
					&& !lOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp().equals("")) {
				UfficioModel lUffMagComp = getUfficioByCodUfficio(lOrdEveTenPreMod.getOrdinanza()
						.getCodUfficioMagistratoComp());

				setRequestAttribute("ufficioMagistratoComp", lUffMagComp);

				// Cerca l'eventuale CSSA competente
				if (lOrdEveTenPreMod.getOrdinanza().getIdCssaComp() != null
						&& !lOrdEveTenPreMod.getOrdinanza().getIdCssaComp().equals(new BigDecimal(9999))) {
					ICSSA lCtrl = SICOLookupRemote.getCSSARemote();
					CSSAModel lCSSAModel = lCtrl
							.getCSSAByKey(lOrdEveTenPreMod.getOrdinanza().getIdCssaComp());

					setRequestAttribute("CSSA", lCSSAModel);
				}
			}

			// Insieme degli uffici destinatari
			Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSius());

			setRequestAttribute("uffici", "" + lOption);
			setRequestAttribute("IDEvento", lEveId.toString());

			lPage = ICostantiDepositoOrdinanzaPc.PG_LOAD_TRASFERISCI_ORDINANZA;
		} else if (lTipoProvvedimento.compareTo("02") == 0) { // DECRETO
			// PRELIEVO EVENTO
			EventoModel lEve = null;
			DepositoDecretoModel lDec = null;
			IEvento lCtrEve = SICOLookupRemote.getEventoRemote();
			lEve = lCtrEve.ExRicercaEventoByKey(lEveId);
			if (lEve == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Evento inesistente: " + lEveId);

			// PRELIEVO DEPOSITO DECRETO
			IDepositoDecreto lCtrDec = SIUSLookupRemote.getDepositoDecretoRemote();
			lDec = lCtrDec.ExRicercaDepositoDecretoByIdEvento(lEveId);
			if (lDec == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Deposito Decreto inesistente per evento: " + lEveId);

			// Insieme degli uffici destinatari.
			Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSius());

			setRequestAttribute("evento", lEve);
			setRequestAttribute("decreto", lDec);

			// MERGE v10: aggiunte opzioni per gli uffici minorenni
			String codTipoUff = getUfficioUtenteConnesso().getCodTipoUfficio();
			if ("TDSM".equalsIgnoreCase(codTipoUff) || "UDSM".equalsIgnoreCase(codTipoUff))
				lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiusTDSMUDSM());

			setRequestAttribute("uffici", "" + lOption);
			setRequestAttribute("IDEvento", lEveId.toString());
			lPage = ICostantiDepositoDecreto.PG_LOAD_TRASFERISCI_DECRETO;
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Provvedimento inesistente per evento: "
					+ lEveId);

		if (lTipoUfficio.compareTo("UDS") == 0)
			setRequestAttribute("postTitle", "Trasferimento Impugnazione Provvedimento");
		else
			setRequestAttribute("postTitle", "Trasferimento Ricorso Provvedimento");

		// STUB 26/03/2007 Destinatari UEPE.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiepe());
		setRequestAttribute("UEPE", "" + lOption);

		return lPage;
	}

}