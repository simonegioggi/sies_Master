package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.cssa.controller.ICSSA;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficiProvvedimentoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadTrasferisciOrdinanza
 * </p>
 * <p>
 * Description: Trasferisce l'ordinanza verso la Procura
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
public class ActLoadTrasferisciOrdinanza extends ActionSiap implements ICostantiDepositoOrdinanzaPc {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Fascicolo SIUS in sessione.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel(
				(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));

		// STUB 28/06/2004 Se l'ordinanza riguarda una Misura Alternativa o il Proc. SIUS è Unificante,
		// possono esistere
		// più uffici interessati all'ordinanza
		if ((lFasGPMod.getGeneraleProcedimentoModel().getCodTipoRegistro() != null
				&& (lFasGPMod.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S22") == 0))
				|| (lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null
						&& (lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue() > 0))
				|| lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
			IUfficio lUctrl = SICOLookupRemote.getUfficioRemote();
			// MEV_39 03/01/2018 passato ulteriore parametro al metodo
			Vector lUfficiInteressati = lUctrl.ListaUfficiInteressatiProvvedimento(
					lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(),
					lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati(),
					lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento());
			// MEV_39: aggiunto destinatario fisso per C036 & C029 & U077 & U082					
			if ("C036".equals(lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
					|| "C029".equals(lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
					|| "U077".equals(lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
					|| "U082".equals(lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
					// intervento post collaudo 11.3 (terza sessione) per risolvere anomalia 4
					|| "U023".equals(lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
					) {
				
				// Gasbarri segnala che deve essere visibile solo se ufficio TDS OPPURE TDSM
				String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
				if ("TDSM".equals(strCodTipoUfficio) || "TDS".equals(strCodTipoUfficio)){
					// aggiungo PGCAP
					UfficiProvvedimentoModel upmPGCAP = lUctrl.getUfficioPGCAPEsecDest("PGCAP",
							getCodComuneUtenteConnesso());
					if (upmPGCAP != null) {
						upmPGCAP.setChiaveAnnoSiep("");
						upmPGCAP.setChiaveProgrSiep("");
						upmPGCAP.setFasSiepOrigine("");
						lUfficiInteressati.add(upmPGCAP);
					}
				}
				// aggiungo eventualmente PM
				if (lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
					IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
					BigDecimal idFascicoloSiep = lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep();
					Vector v = ims.ExRicercaFascicoliCollegati(idFascicoloSiep);
					if (!v.isEmpty()) {
						FascMsToFascSiepModel fmtfsm = (FascMsToFascSiepModel) v.lastElement();
						BigDecimal idFascicoloSiepOrigine = fmtfsm.getFasSieIdFascicoloCollegato();
						UfficiProvvedimentoModel upmPM = null;
						boolean existUffPMInteressato = false;
						for (int i = 0; i < lUfficiInteressati.size(); i++) {
							UfficiProvvedimentoModel upm = (UfficiProvvedimentoModel) lUfficiInteressati
									.get(i);
							if (upm.getCodUfficio().equals(fmtfsm.getChiaveUfficioSiepCollegato())
									&& "PM".equals(upm.getCodTipoUfficio())) {
								existUffPMInteressato = true;
								break;
							}
						}
						if (!existUffPMInteressato)
							upmPM = lUctrl.getUfficioPMEsecDest(idFascicoloSiepOrigine, "PM",
									fmtfsm.getChiaveUfficioSiepCollegato());
						if (upmPM != null)
							lUfficiInteressati.add(upmPM);
					}
				}
			}
			setRequestAttribute("ufficiInteressati", lUfficiInteressati);
		}


		// DATI RELATIVI ALL'ORDINANZA
		OrdinanzaEventoTenoriPrescrizioniModel lOrdEveTenPreMod = new OrdinanzaEventoTenoriPrescrizioniModel();
		IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		lOrdEveTenPreMod = lCtrlDep.ExRicercaOrdinanzaEventoTenoriPrescrizioniByIdEvento(lEveId);

		setRequestAttribute("datiOrdinanza", lOrdEveTenPreMod);

		// Si Individua tra i Tenori Presenti quello che ha il campo CodEsitoTenore uguale a CodMotivo
		// dell'evento
		// associato.
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
			UfficioModel lUffMagComp = getUfficioByCodUfficio(
					lOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp());

			setRequestAttribute("ufficioMagistratoComp", lUffMagComp);

			// Cerca l'eventuale CSSA competente.
			if (lOrdEveTenPreMod.getOrdinanza().getIdCssaComp() != null
					&& !lOrdEveTenPreMod.getOrdinanza().getIdCssaComp().equals(new BigDecimal(9999))) {
				ICSSA lCtrl = SICOLookupRemote.getCSSARemote();
				CSSAModel lCSSAModel = lCtrl.getCSSAByKey(lOrdEveTenPreMod.getOrdinanza().getIdCssaComp());

				setRequestAttribute("CSSA", lCSSAModel);
			}
		}

		// Insieme degli uffici destinatari
		// 05/06/2008 La combo degli uffici destinatari non deve comprendere le Procure.
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSius());
		// MEV10-s3: aggiunte or condition per gestire trib. sorv. minori ed uff. sorv. minori
		// MEV_65: uguale sia per maggiorenni che minorenni
		// UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
		// ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		// UfficioModel lUfficioUtenteConnesso = lUtenteConnesso.getUfficioUtente();
		// String aCodTipoUfficio = lUfficioUtenteConnesso.getCodTipoUfficio();
		// if ("UDSM".equals(aCodTipoUfficio) || "TDSM".equals(aCodTipoUfficio))
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiusTDSMUDSM());
		// else
		// lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiusTrattino());
		setRequestAttribute("uffici", "" + lOption);

		// STUB 11/09/2006 Destinatari UEPE.
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiepe());
		setRequestAttribute("UEPE", "" + lOption);

		setRequestAttribute("IDEvento", lEveId.toString());

		return PG_LOAD_TRASFERISCI_ORDINANZA;
	}

}