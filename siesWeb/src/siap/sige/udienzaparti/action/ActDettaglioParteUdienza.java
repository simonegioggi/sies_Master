package siap.sige.udienzaparti.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActDettaglioParteUdienza
 * </p>
 * <p>
 * Description: Classe Action per il Dettaglio Parte Udienza (Offesa/Civile - Fisica/Giuridica)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioParteUdienza extends ActionSiap implements ICostantiPartiUdienza {

	public String processRequest() throws Exception {

		// Gestione pulsante di ritorno
		gestioneRitorno();

		// Lettura dell'ID del Soggetto (Parte).
		String lIdSoggetto = "";
		if (!isRequestParameterNullObj(CAMPO_ID_SOGGETTO))
			lIdSoggetto = getRequestStringParameter(CAMPO_ID_SOGGETTO);

		// Identificativo Evento Udienza
		String idEventoUdienza = null;
		if (!isRequestParameterNullObj("IdEventoUdienza")) {
			idEventoUdienza = getRequestStringParameter("IdEventoUdienza");
		}

		// Identificativo Udienza SIGE
		String idUdienzaSige = null;
		if (!isRequestParameterNullObj("IdUdienzaSige")) {
			idUdienzaSige = getRequestStringParameter("IdUdienzaSige");
		}

		// Identificativo Udienza Procedimento SIGE
		String idUdienzaProcedimentoSige = null;
		if (!isRequestParameterNullObj("IdUdienzaProcedimentoSige")) {
			idUdienzaProcedimentoSige = getRequestStringParameter("IdUdienzaProcedimentoSige");
		}

		// Codice Tipo Parte (O=Offesa/C=Civile)
		String codTipoParte = null;
		if (!isRequestParameterNullObj("codTipoParte")) {
			codTipoParte = getRequestStringParameter("codTipoParte");
		}

		if (lIdSoggetto.compareTo("") == 0)
			throw new SIGEException(SIGEException.USER_MESSAGE, "Parte Udienza inesistente.");

		IPartiUdienza lCtrl = SIGELookupRemote.getPartiUdienzaRemote();

		AnagraficaPartiUdienzaModel anagParteUdienzaRet = lCtrl
				.ExRicercaParteUdienzaByKey(new BigDecimal(lIdSoggetto));

		// Difensori assegnati alla Parte.
		Vector lDifensori = lCtrl.ExRicercaDifensoreByIdSoggetto(new BigDecimal(lIdSoggetto));
		if (lDifensori.size() > 0)
			setRequestAttribute("difensori", lDifensori);

		// Lettura delle notifiche.
		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		Vector lVect = lCtrlNot.ExRicercaEstesaNotificaByIdParteUdienza(new BigDecimal(lIdSoggetto));
		setRequestAttribute("notifiche", lVect);

		setRequestAttribute("anagraficaParteUdienza", anagParteUdienzaRet);
		setRequestAttribute("idSoggetto", lIdSoggetto);
		setRequestAttribute("idEventoUdienza", idEventoUdienza);
		setRequestAttribute("idUdienzaSige", idUdienzaSige);
		setRequestAttribute("idUdienzaProcedimentoSige", idUdienzaProcedimentoSige);
		setRequestAttribute("codTipoParte", codTipoParte);

		return PG_DETTAGLIO_PARTEUDIENZA;
	}

}