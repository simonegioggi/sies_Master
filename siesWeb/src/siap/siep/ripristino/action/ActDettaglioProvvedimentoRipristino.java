package siap.siep.ripristino.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioProvvedimentoRipristno
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio Provvedimento Ripristino
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
public class ActDettaglioProvvedimentoRipristino extends ActSIESDettaglioProvvedimento
		implements ICostantiRipristino {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// EVENTO
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		setRequestAttribute("eventonotifica", lEveMod);

		/*
		 * REWORK DETTAGLIO IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); PosizioneGiuridicaModel lPosGiuModificata = new
		 * PosizioneGiuridicaModel(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.
		 * getIdFascicoloSiep());
		 */

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lId, lFascMod.getIdFascicoloSiep());

		setRequestAttribute("posizioneluogoaltra", lPos);

		// MAGISTRATO
		MagistratoModel lMag = lEveMod.getMagistrato();

		setRequestAttribute("magistrato", lMag);

		// PENA RESIDUA
		// Date lDataInizioPena = null;
		// Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		/*
		 * REWORK DETTAGLIO IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel
		 * llPenMod =
		 * lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 */

		PenaResiduaModel llPenMod = this.getPenaResidua(lId, lFascMod.getIdFascicoloSiep());

		/*
		 * lDataInizioPena = llPenMod.getDataInizio(); lDataFinePenaM = llPenMod.getDataFine(); lDataFinePenaA
		 * = llPenMod.getDataFinePresunta();
		 */
		setRequestAttribute("penaresidua", llPenMod);

		// NOTIFICHE
		NotificaModel[] lNotifiche = lEveMod.getNotifiche();
		for (int i = 0; i < lNotifiche.length; i++) {
			/*
			 * // Autorita Esterne if( lNotifiche[i].getAutEstIdAutoritaEsterna() != null ) {
			 * setRequestAttribute("autorita", lNotifiche[i]); }
			 */
			if (lNotifiche[i].getIstDetIdIstitutoDetenzione() != null
					&& !lNotifiche[i].getIstDetIdIstitutoDetenzione().equals("")) {
				setRequestAttribute("istituto", lNotifiche[i]);
			}
			/*
			 * //Preleva gli uffici if( lNotifiche[i].getUffCodUfficio() != null ) {
			 * setRequestAttribute("ufficio", lNotifiche[i]); }
			 * 
			 * //Preleva il Cssa if( lNotifiche[i].getCssIdCssa() != null ) { setRequestAttribute("cssa",
			 * lNotifiche[i]); }
			 */
		}

		return PG_LOAD_DETTAGLIO_PROVVEDIMENTO_RIPRISTINO;
	}

}