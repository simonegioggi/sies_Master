package siap.sius.depositosentenza.action;

import java.math.BigDecimal;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.SentenzaEventoTenoriPrescrizioniModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadTrasferisciSentenza
 * </p>
 * <p>
 * Description: Trasferisce la Sentenza verso la Procura
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadTrasferisciSentenza extends ActionSiap implements ICostantiDepositoSentenza {

	public String processRequest() throws Exception {

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Fascicolo SIUS in sessione.
		// FascicoloGPModel lFasGPMod = new FascicoloGPModel ((FascicoloGPModel)
		// getSessionAttribute("fascicoloSiusGP"));

		// STUB 28/06/2004 Se l'ordinanza riguarda una Misura Alternativa o il Proc. SIUS è Unificante,
		// possono esistere più uffici interessati all'ordinanza
		/*
		 * if( (lFasGPMod.getGeneraleProcedimentoModel().getCodTipoRegistro() != null &&
		 * (lFasGPMod.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S22") == 0)) ||
		 * (lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null &&
		 * (lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue() > 0 )) ||
		 * lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null ) { IUfficio lUctrl =
		 * SICOLookupRemote.getUfficioRemote(); Vector lUfficiInteressati =
		 * lUctrl.ListaUfficiInteressatiProvvedimento(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(),
		 * lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati());
		 * setRequestAttribute("ufficiInteressati", lUfficiInteressati ); }
		 */
		// DATI RELATIVI ALLA SENTENZA
		SentenzaEventoTenoriPrescrizioniModel lSenEveTenPreMod = new SentenzaEventoTenoriPrescrizioniModel();
		IDepositoSentenza lCtrlDep = SIUSLookupRemote.getDepositoSentenzaRemote();
		lSenEveTenPreMod = lCtrlDep.ExRicercaSentenzaEventoTenoriPrescrizioniByIdEvento(lEveId);

		setRequestAttribute("datiSentenza", lSenEveTenPreMod);

		// Si Individua tra i Tenori Presenti quello che ha il campo CodEsitoTenore uguale a CodMotivo
		// dell'evento associato.
		TenoreModel[] lTenori = lSenEveTenPreMod.getTenori();
		EventoModel lEve = lSenEveTenPreMod.getEvento();
		if (lTenori != null && lEve != null) {
			for (int i = 0; i < lTenori.length; i++) {
				if (lTenori[i].getCodOggettoTenore().equals(lEve.getCodMotivo())) {
					setRequestAttribute("tenoreEsito", lTenori[i]);
					break;
				}
			}
		}
		/*
		 * //Si Cerca l'evenutale ufficio del Magistrato di Sorveglianza if( lOrdEveTenPreMod.getOrdinanza()
		 * != null && lOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp() != null &&
		 * !lOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp().equals("")) { UfficioModel
		 * lUffMagComp =
		 * getUfficioByCodUfficio(lOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp());
		 * 
		 * setRequestAttribute("ufficioMagistratoComp", lUffMagComp);
		 * 
		 * //Cerca l'eventuale CSSA competente. if (lOrdEveTenPreMod.getOrdinanza().getIdCssaComp() != null &&
		 * !lOrdEveTenPreMod.getOrdinanza().getIdCssaComp().equals(new BigDecimal(9999) ) ) { ICSSA lCtrl =
		 * SICOLookupRemote.getCSSARemote(); CSSAModel lCSSAModel =
		 * lCtrl.getCSSAByKey(lOrdEveTenPreMod.getOrdinanza().getIdCssaComp());
		 * 
		 * setRequestAttribute("CSSA", lCSSAModel); } }
		 */
		// Insieme degli uffici destinatari
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiusTDSMUDSM());
		String[] lCodes = new String[] { "-", "TDS", "UDS", "TDSM" };
		lOption.setFilter(lCodes);
		setRequestAttribute("uffici", "" + lOption);

		// Destinatari UEPE.
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiepe());
		setRequestAttribute("UEPE", "" + lOption);

		setRequestAttribute("IDEvento", lEveId.toString());

		return PG_LOAD_TRASFERISCI_SENTENZA;
	}

}