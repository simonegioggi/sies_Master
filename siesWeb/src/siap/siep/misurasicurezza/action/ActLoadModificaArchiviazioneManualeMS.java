package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaArchiviazioneManualeMS
 * </p>
 * <p>
 * Description: classe per Load Modifica dei Provvedimenti
 * </p>
 * <p>
 * di Archiviazione manuale (Definizione MIS. SIC.)
 * </p>
 * <p>
 * ( tipo provv = Annotazione (25) )
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: IntersistemiItalia spa
 * </p>
 * 
 * @author
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadModificaArchiviazioneManualeMS extends ActionSiap implements ICostantiMisuraSicurezza,
		ICostantiOrdineEsecuzione {
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// EventoNotifica (Provvedimento di annotazione Inserito)
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);
		setRequestAttribute("eventonotifica", lEveMod);

		// Avvocati
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		try {
			Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
			setRequestAttribute("avvocati", lAvvocati);
		} catch (SIEPException e) {
			// NON Rilancio l'eccezione: Deve Passare anche se Fascicolo è PRIVO di Avvocato
		}

		// Magistrato
		IMagistrato lMCtrl = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagMod = lMCtrl.ExRicercaMagistratoByEvento(lId);
		setRequestAttribute("magistrato", lMagMod);

		// ARCHIVIAZIONE
		IArchiviazione lCtrlArc = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel lArcMod = new ArchiviazioneModel();
		lArcMod = lCtrlArc.ExRicercaArchiviazioneCssaIstitutoByIdEvento(lId);
		setRequestAttribute("archiviazione", lArcMod);

		// MOTIVO PROVVEDIMENTO
		Option lOptionT = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
		// OGGETTO DEFINIZIONE --> MEV_39: aggiunte 3 definizioni (1150, 1151, 1152) (order by rv_meaning)
		lOptionT.setFilter(new String[] { "1151", "0400", "0613", "0614", "1152", "2804", "1150", "0612" });
		if (lEveMod != null && lEveMod.getEvento() != null && !lEveMod.getEvento().getIdEvento().equals(null)) {
			lOptionT.setSelected(lEveMod.getEvento().getCodMotivo());
		}
				
		setRequestAttribute("tipoArchiviazioni", "" + lOptionT);

		// Misure Sicurezza
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		List lListMis = new ArrayList();
		lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("listaMisureSic", lListMis);

		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());
		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);

		// Autorità per Notifiche
		String lCodTipoAutorita1 = "-";
		String lCodTipoAutoritaND = "-";
		UfficioModel lUffSorvModel = new UfficioModel();
		String lCodUfficioTipo = "-";
		String lUfficioDescrSede = "";

		if (lEveMod.getNotifiche() != null && lEveMod.getNotifiche().length > 0) {
			for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
				if (lEveMod.getNotifiche()[i] != null
						&& lEveMod.getNotifiche()[i].getAutoritaEsterna() != null
						&& ("AA").equals(lEveMod.getNotifiche()[i].getCodTipoNotifica())) {
					lCodTipoAutorita1 = lEveMod.getNotifiche()[i].getAutoritaEsterna().getCodTipoAutorita();
				}

				if (lEveMod.getNotifiche()[i] != null
						&& ("MS").equals(lEveMod.getNotifiche()[i].getCodTipoNotifica())) {
					if (lEveMod.getNotifiche()[i].getUfficio() != null) {
						lUffSorvModel = lEveMod.getNotifiche()[i].getUfficio();
						lCodUfficioTipo = lUffSorvModel.getCodTipoUfficio();
						lUfficioDescrSede = lUffSorvModel.getDescrComune();
					}
				}

				if (lEveMod.getNotifiche()[i] != null
						&& ("ND").equals(lEveMod.getNotifiche()[i].getCodTipoNotifica())) {
					lCodTipoAutoritaND = lEveMod.getNotifiche()[i].getAutoritaEsterna().getCodTipoAutorita();
				}

			}
		}

		// Riempimento ComboBox Autorità (Prima)
		Option lOptionE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		if (lCodTipoAutorita1.compareTo("-") != 0) {
			lOptionE.setSelected(lCodTipoAutorita1);
		}
		setRequestAttribute("autoritaEsternaE", "" + lOptionE);

		// Aggiunto Desinatatio Sorveglianza
		Option lOptionSor = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOptionSor.setFilter(new String[] { "-", "TDS", "UDS", "UDSM" });
		if (lCodUfficioTipo.compareTo("-") != 0) {
			lOptionSor.setSelected(lCodUfficioTipo);
		}
		setRequestAttribute("tipoUDS", "" + lOptionSor);
		setRequestAttribute("comuneUDS", "" + lUfficioDescrSede);

		// Riempimento ComboBox notifica Difensore
		Option lOptionND = new Option(DecodificheManager.getInstance().getTipoAutorita());
		String[] lFiltroAvvo = { "22", "C0" };
		lOptionND.setFilter(lFiltroAvvo);
		if (lCodTipoAutoritaND.compareTo("-") != 0) {
			lOptionND.setSelected(lCodTipoAutoritaND);
		}
		setRequestAttribute("autoritaEsterna", "" + lOptionND);
		setRequestAttribute("NotificaAvvocati", "" + lCodTipoAutoritaND);

		return PG_MODIFICA_ARCHIVIAZIONE_MANUALE;

	} // chiude processRequest

} // Chiude Classe