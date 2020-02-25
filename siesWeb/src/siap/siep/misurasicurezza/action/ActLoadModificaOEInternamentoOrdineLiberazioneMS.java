package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.EventoVerbaleModel;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaOEInternamentoOrdineLiberazioneMS
 * </p>
 * <p>
 * Description: Classe Action per la load Modifica dei provvedimenti di
 * </p>
 * <p>
 * Ordine di Esecuzione per Internamento, esecuzione MS e
 * </p>
 * <p>
 * Ordine Scarcerazione per Liberazione, esecuzione MS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: IntersistemiItalia spa
 * </p>
 * 
 * @author AMBROSINO
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadModificaOEInternamentoOrdineLiberazioneMS extends ActionSiap implements
		ICostantiMisuraSicurezza, ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("evento", getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO),
				getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "La " + lck.getEntity()
					+ " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		// EventoNotifica
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);
		setRequestAttribute("eventonotifica", lEveMod);

		// Posizione Giuridica
		PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
//		String lCodPosGiu = lPos.getCodPosizioneGiuridica();

		setRequestAttribute("posizione", lPos);

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

		// Misure Sicurezza
		// Ricerca Misure sicurezza già presenti nel fascicolo : Sono ORDINATE per DATA_INSERIMENTO
		List lListMis = new ArrayList();
		MisuraSicurezzaModel lMisSicMod = null;
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
		// prendo solo l'ultima MISURA (28/10/2014 se esiste)
		if (lListMis.size() > 0)
			lMisSicMod = (MisuraSicurezzaModel) lListMis.get(lListMis.size() - 1);

		setRequestAttribute("listaMisure", lListMis);
		setRequestAttribute("MisuraModel", lMisSicMod);

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
						&& ("E").equals(lEveMod.getNotifiche()[i].getCodTipoNotifica())) {
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
		
		
		// MEV_39: DEVO RECUPERARE LA STRUTTURA DESIGNATA se esiste un evento di DESIGNAZIONTE ISTITUTO
		EventoModel lEveDaRicercare = new EventoModel();
		lEveDaRicercare.setCodTipoEvento("01");	
		lEveDaRicercare.setCodTipoProvvedimento("25");
		lEveDaRicercare.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel eventoDesignazioneIStituto = lCtrlEvento
				.ExRicercaEventoPerMotivo(new String[] { "1130" }, lEveDaRicercare);
		
		if(eventoDesignazioneIStituto!= null &&  !"A".equals(eventoDesignazioneIStituto.getFlagDocumentoRegistrato()) ){
			// EventoVerbale DesignazioneIStituto
			EventoVerbaleModel lEveVerMod = new EventoVerbaleModel();
			IEvento lCtrlDes = SICOLookupRemote.getEventoRemote();
			lEveVerMod = lCtrlDes.ExRicercaEventoVerbaleByIdEve(eventoDesignazioneIStituto.getIdEvento());			
			IstitutoDetenzioneModel strutturaDesignata = new IstitutoDetenzioneModel();
			if (lEveVerMod != null && lEveVerMod.getVerbale() != null
					&& lEveVerMod.getVerbale().getIstDetIdIstitutoDetenzione() != null
					&& !lEveVerMod.getVerbale().getIstDetIdIstitutoDetenzione().equals("-")) {
				IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
				strutturaDesignata = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lEveVerMod.getVerbale()
						.getIstDetIdIstitutoDetenzione());
			}
			setRequestAttribute("strutturaDesignataModel", strutturaDesignata);
		}

		return PG_MODIFICA_OE_INTERNAMENTO_ORDINE_LIBERAZIONR_MS;

	} // chiude processRequest

} // Chiude Classe