package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficiProvvedimentoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadTrasferisciDecreto
 * </p>
 * <p>
 * Description: Trasferisce il decreto verso la Procura
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
public class ActLoadTrasferisciDecreto extends ActionSiap implements ICostantiDepositoDecreto {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		String page = PG_LOAD_TRASFERISCI_DECRETO;
		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		DepositoDecretoModel lDec = null;
		EventoModel lEve = null;

		// Recupero del Fascicolo SIUS dalla sessione.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel(
				(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));

		// STUB 28/06/2004 Se il decreto riguarda una Misura Alternativa o il Proc. SIUS è Unificante, possono
		// esistere più uffici interessati al decreto
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
					// intervento post collaudo 13.3 (terza sessione) per risolvere anomalia 4
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

		// PRELIEVO EVENTO
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

		// Insieme degli uffici destinatari
		// 05/06/2008 La combo degli uffici destinatari non deve comprendere le Procure.
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSius());
		// MEV_65: uguale sia per maggiorenni che minorenni
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiusTrattino());
		// MERGE v10: aggiunte opzioni per gli uffici minorenni
		// String codTipoUff = getUfficioUtenteConnesso().getCodTipoUfficio();
		// if ("TDSM".equalsIgnoreCase(codTipoUff) || "UDSM".equalsIgnoreCase(codTipoUff))
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiusTDSMUDSM());
		setRequestAttribute("uffici", "" + lOption);

		// STUB 11/09/2006 Destinatari UEPE.
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiepe());
		setRequestAttribute("UEPE", "" + lOption);

		setRequestAttribute("evento", lEve);
		setRequestAttribute("decreto", lDec);

		setRequestAttribute("IDEvento", lEveId.toString());

		return page;
	}

}