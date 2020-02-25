package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaStatoEsecuzione
 * </p>
 * <p>
 * Description: Classe Action per la ricerca per lo Stato Esecuzione
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
public class ActRicercaStatoEsecuzione extends ActionSiap implements ICostantiOrdineEsecuzione {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		if (isSessionAttributeNullObj("fascicolo")) {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
					+ "siap.siep.ordineesecuzione.action.ActRicercaStatoEsecuzione";
			return lPage;
		}

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal aId = lFascicoloModel.getIdFascicoloSiep();

		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		DettaglioFascicoloModel lDettaglio = null;
		lDettaglio = lCtrl.ExDettaglioFascicoloSiep(aId);

		if (lDettaglio == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non presente");

		FascicoloSiepModel lFasMod = lDettaglio.getFascicoloSiep();

		setRequestAttribute("dettagliofascicolo", lDettaglio);

		// Inserisce nella session il fascicolo (contenente Soggetto e Sentenza)
		setSessionAttribute("fascicolo", lFasMod);

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		IEventoSimeone lCtrlEve = SICOLookupRemote.getEventoSimeoneRemote();
		Vector lVect = lCtrlEve.ExRicercaEventoStatoEsecuzioneByFascicoloSiepPaged(
				lFascicoloModel.getIdFascicoloSiep(), Integer.parseInt(lPagina));

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrlEve.ExGetCountEventiPaged(lFascicoloModel.getIdFascicoloSiep());
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setRequestAttribute("aggregato", lVect);

		// ANNA per Pene Sospese
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		// lModel.setCodiceAlternativo("REVOCA");
		Collection lColMotivo = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
		setRequestAttribute("AllMotivi", lColMotivo);

		// MEV 15 - Revisione SIGE
		// Aggiunto parametro per identificare la funzione che richiama la maschera
		// di Stato Esecuzione da Iscrizione Manuale.
		// Quando viene richiamata da SIGE sulla maschera viene inserito
		// il pulsante Indietro
		String codFunzione = getCodFunMenuVerticale();
		setRequestAttribute("codFunzione", codFunzione);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return PG_RICERCA_STATO_ESECUZIONE;

	}
}