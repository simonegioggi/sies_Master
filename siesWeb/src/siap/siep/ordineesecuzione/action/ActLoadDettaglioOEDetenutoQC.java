package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioOEDetenutoQC
 * </p>
 * <p>
 * Description: Dettaglio dell'Ordine di Esecuzione detenuto Questa Causa
 * </p>
 * <p>
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class ActLoadDettaglioOEDetenutoQC extends ActSIESDettaglioProvvedimento
		implements ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {
		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		/*
		 * REWORK PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); IPosizioneGiuridica lPosCtrl =
		 * SIEPLookupRemote.getPosizioneGiuridicaRemote(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lEveMod.
		 * getEvento().getFasSieIdFascicoloSiep());
		 */
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lId,
						lEveMod.getEvento().getFasSieIdFascicoloSiep());
		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Al Procedimento non ha una Posizione Giuridica");

		setRequestAttribute("posizioneluogoaltra", lPos);

		/*
		 * REWORK IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel llPenMod =
		 * lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 */
		PenaResiduaModel llPenMod = this.getPenaResidua(lId, lEveMod.getEvento().getFasSieIdFascicoloSiep());

		lDataInizioPena = llPenMod.getDataInizio();
		// lDataFinePenaM = llPenMod.getDataFine();
		lDataFinePenaA = llPenMod.getDataFinePresunta();
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);

		setRequestAttribute("eventonotifica", lEveMod);

		return PG_LOAD_DETTAGLIO_OE_CONDANNATO_DETENUTO_QC;
	}

}