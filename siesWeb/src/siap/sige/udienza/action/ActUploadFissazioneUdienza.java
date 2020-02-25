package siap.sige.udienza.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.action.ActUploadDocument;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActUploadFissazioneUdienza
 * </p>
 * <p>
 * Description: Classe Action per validare una Fissazione Udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActUploadFissazioneUdienza extends ActUploadDocument {

	FascicoloSigeEstesoModel getFascicoloSigeEstesoInSessione() throws F3BException {
		if (isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new F3BException(F3BException.USER_MESSAGE,
					"Dati del Procedimento SIGE non in sessione !!");

		FascicoloSigeEstesoModel lFascicoloEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		if (lFascicoloEsteso == null || lFascicoloEsteso.getFascicoloSige() == null
				|| lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige() == null)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Non è possibile recuperare dalla sessione i dati fascicolo");

		return lFascicoloEsteso;
	}

	void checkDataEmissione(UdienzaProcedimentoSigeModel upsm) throws Exception {
		// check data emissione
		BigDecimal lIdEvento = upsm.getEveIdEvento();
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel eventoNotificaModel = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		if (eventoNotificaModel.getEvento().getDataEmissione() == null) {
			// errore
			throw new F3BException(F3BException.USER_MESSAGE, "Errore");
		}
	}

	void checkDataUdienza(UdienzaProcedimentoSigeModel upsm) throws Exception {
		// check data udienza
		BigDecimal lIdUdienzaSige = upsm.getUdiIdUdienzaSige();
		IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
		UdienzaSigeModel lUdiMod = lCtrl.ExRicercaUdienzaSigeById(lIdUdienzaSige);
		if (lUdiMod.getDataUdienza() == null) {
			throw new F3BException(F3BException.USER_MESSAGE, "Errore");
		}
	}

	@SuppressWarnings("unchecked")
	void checkAvvocati(FascicoloSigeEstesoModel mFasEsteso) throws Exception {
		FascicoloSigeUtils lFasSigeUtils = new FascicoloSigeUtils();
		Vector<Object> lAvvocati = lFasSigeUtils
				.ricercaAvvocati(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
		if (lAvvocati.size() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE, "Errore");
		}
	}

	void checkMagistrato(FascicoloSigeEstesoModel mFasEsteso) throws Exception {
		MagistratoAssegnatarioModel lMagAss = mFasEsteso.getMagAssegnatario();
		if (lMagAss == null) {
			throw new F3BException(F3BException.USER_MESSAGE, "Errore");
		}
	}

	public String processRequest() throws Exception {

		// Controllo il Fascicolo Sige Esteso in sessione
		FascicoloSigeEstesoModel mFasEsteso = this.getFascicoloSigeEstesoInSessione();

		// Lettura ID Evento
		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_EVENTO);
		IUdienzaProcedimentoSige lUdiProCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		UdienzaProcedimentoSigeModel upsm = lUdiProCtrl.ExRicercaUdienzaProcedimentoByEve(lId);

		try {
			checkDataEmissione(upsm);
			checkDataUdienza(upsm);
			checkAvvocati(mFasEsteso);
			checkMagistrato(mFasEsteso);
		} catch (Exception e) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Prima di effettuare l'operazione di validazione, completare le informazioni mancanti: Data Emissione, Data Udienza, Magistrato e Difensore attraverso il tasto modifica");
		}

		return super.processRequest();
	}

}