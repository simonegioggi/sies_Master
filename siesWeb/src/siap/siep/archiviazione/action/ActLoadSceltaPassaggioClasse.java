package siap.siep.archiviazione.action;

import java.math.BigDecimal;

import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActLoadSceltaPassaggioClasse - Classe di caricamento scelte passaggio di classe
 *
 * @since MEV_2025-48
 * @author sgioggi
 * @version 1.0
 */
public class ActLoadSceltaPassaggioClasse extends ActionSiap {

	/**
	 * Action non inserisce nulla ma raccoglie i dati dalla request, li prepara per la pagina successiva e la
	 * richiama
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();
		DettaglioFascicoloModel dfm = null;
		IFascicoloSiep ifs = SIEPLookupRemote.getFascicoloSiepRemote();
		dfm = ifs.ExDettaglioFascicoloSiep(idFascicolo);
		setRequestAttribute("penaresidua", dfm.getPenaResidua());

		int chiaveProgr = fsm.getChiaveProgr().intValue();
		int progressivo = 0;
		if (chiaveProgr >= 0 && chiaveProgr < 20000)
			progressivo = 0;
		else if (chiaveProgr >= 20000 && chiaveProgr < 30000)
			progressivo = 1;
		else if (chiaveProgr >= 30000 && chiaveProgr < 40000)
			progressivo = 2;
		else if (chiaveProgr >= 40000 && chiaveProgr < 50000)
			progressivo = 3;
		else if (chiaveProgr >= 50000 && chiaveProgr < 60000)
			progressivo = 4;
		else if (chiaveProgr >= 60000 && chiaveProgr < 70000)
			progressivo = 5;
		else if (chiaveProgr >= 70000 && chiaveProgr < 80000)
			progressivo = 6;
		setRequestAttribute("progressivo", "" + progressivo);

		setRequestAttribute("dataEmissione",
				getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		setRequestAttribute("dataTrasmissione",
				getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
						ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
						ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));
		setRequestAttribute("dataDefinizione",
				getRequestDateParameter(ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE,
						ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE,
						ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE));
		setRequestAttribute("oggettoDefinizione",
				getRequestStringParameter(ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE));
		setRequestAttribute("note", getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E));
		setRequestAttribute("magistratoFirmatario", getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		setRequestAttribute("casellarioGiudiziale", getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS));

		// pagina di ritorno
		return IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadSceltaPassaggioClasse.jsp";
	}

}