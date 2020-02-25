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
 * Title: ActLoadDettaglioOELiberoSanSos
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Ordine Esecuzione Libero per Sanzione Sostitutiva
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
public class ActLoadDettaglioOELiberoSanSos extends ActSIESDettaglioProvvedimento
		implements ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento,
						lEveMod.getEvento().getFasSieIdFascicoloSiep());

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Al Procedimento non ha una Posizione Giuridica");

		setRequestAttribute("posizioneluogoaltra", lPos);

		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento,
				lEveMod.getEvento().getFasSieIdFascicoloSiep());

		if (llPenMod != null) {
			lDataInizioPena = llPenMod.getDataInizio();
			// lDataFinePenaM = llPenMod.getDataFine();
			lDataFinePenaA = llPenMod.getDataFinePresunta();

			setRequestAttribute("StrdataInizioPena",
					DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
			setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
			setRequestAttribute("penaresidua", llPenMod);
		}

		setRequestAttribute("eventonotifica", lEveMod);

		return PG_LOAD_DETTAGLIO_OE_CONDANNATO_LIBERO_SAN_SOS;
	}

}