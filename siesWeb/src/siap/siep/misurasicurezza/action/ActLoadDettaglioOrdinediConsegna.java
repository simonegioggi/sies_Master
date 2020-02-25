package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.EventoVerbaleModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioOrdinediConsegna
 * </p>
 * <p>
 * Description: Classe per la load Dettaglio Ordine di Consegna del provvedimento SIEP
 * </p>
 * <p>
 * dopo Decisione del Magistrato di Sorveglianza sulla Misura di Sicurezza
 * </p>
 * <p>
 * e sull' Accertamento di pericolosità Sociale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Intersistemi Italia S.P.A.
 * </p>
 * 
 * @author AMBROSINO
 * @version 8.3
 */
@SuppressWarnings("rawtypes")
public class ActLoadDettaglioOrdinediConsegna extends ActSIESDettaglioProvvedimento implements
		ICostantiMisuraSicurezza, ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// EventoNotifica
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);
		setRequestAttribute("eventonotifica", lEveMod);

		// Cerco Isstituto Detenzione
		IstitutoDetenzioneModel lIstDetenzione = null;
		IIstitutoDetenzione ctrld = SIEPLookupRemote.getIstitutoDetenzioneRemote();

		for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
			if (lEveMod.getNotifiche()[i].getCodTipoNotifica().equals("E")) {
				if (lEveMod.getNotifiche()[i].getIstDetIdIstitutoDetenzione() != null) {
					lIstDetenzione = (IstitutoDetenzioneModel) ctrld.ExRicercaIstitutoDetenzioneByKey(lEveMod
							.getNotifiche()[i].getIstDetIdIstitutoDetenzione());
				}
			}
		}
		setRequestAttribute("istitutodetenzione", lIstDetenzione);

		// Posizione Giuridica
		PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizione", lPos);

		// MERGE v10: cancello codice come in Mev2-s2
		// Avvocato
		// IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		// Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		// setRequestAttribute("avvocati", lAvvocati);

		// Ricerca Misure Sicurezza presenti nel fascicolo : Sono ORDINATE per DATA_INSERIMENTO
		List lListMis = new ArrayList();
		MisuraSicurezzaModel lMisSicMod = null;
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
		// prendo solo l'ultima MISURA
		if (lListMis != null && lListMis.size() > 0) {
			lMisSicMod = (MisuraSicurezzaModel) lListMis.get(lListMis.size() - 1);
		}

		setRequestAttribute("MisuraModel", lMisSicMod);
		setRequestAttribute("listaMisure", lListMis);
		
		
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
			IEvento lCtrld = SICOLookupRemote.getEventoRemote();
			lEveVerMod = lCtrld.ExRicercaEventoVerbaleByIdEve(eventoDesignazioneIStituto.getIdEvento());			
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


		return PG_LOAD_DETTAGLIO_ORDINE_CONSEGNA;

	} // chiude processRequest

} // Chiude Classe