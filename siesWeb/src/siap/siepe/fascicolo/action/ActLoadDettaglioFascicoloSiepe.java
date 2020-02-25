package siap.siepe.fascicolo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.siepe.util.SIEPELookupRemote;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioFascicoloSiepe
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di FascicoloSiepe
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
public class ActLoadDettaglioFascicoloSiepe extends ActionSiap implements ICostantiFascicoloSiepe {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + "ActLoadDettaglioFascicoloSiepe: inizio");

		FascicoloSiepeEstesoModel lFascicoloEsteso = null;
		SoggettoModel lSoggetto = null;
		FascicoloGPModel lFasSius = null;
		FascicoloSiepModel lFasSiep = null;
		EventoModel lEvento = null;

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIEPE);

		// Ricerca Fascicolo
		IFascicoloSiepe lCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();
		FascicoloSiepeModel llFasMod = lCtrl.ExRicercaFascicoloSiepeByKey(lId);

		// Si ricava la descrizione dell'Ufficio Mittente
		if (llFasMod.getCodUfficioMittente() != null
				&& llFasMod.getCodUfficioMittente().trim().length() > 0) {
			UfficioModel lUff = UfficioUtils.getUfficioByCodUfficio(llFasMod.getCodUfficioMittente());
			llFasMod.setDescrUfficioMittente(lUff.getDescrTipoUfficio() + " " + lUff.getDescrComune());
		} else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Manca il cod Ufficio Mittente nel Fascicolo SIEPE !");

		// Si recupera la descrizione del tipo di definizione e la si imposta nel model.
		if (llFasMod.getTipoDefinizione() != null) {
			Collection lCol = DecodificheManager.getInstance().getTipoDefinzioneSiepe();
			llFasMod.setDescrTipoDefinizione(
					DecodificheUtils.getDescbyCode(lCol, llFasMod.getTipoDefinizione()));
		}

		setRequestAttribute("fascicolosiepe", llFasMod);

		// Ricerca Attività collegate
		IAttivita lAttCtrl = SIEPELookupRemote.getAttivitaRemote();
		AttivitaModel lAttivitaRicerca = new AttivitaModel();
		lAttivitaRicerca.setFasSieIdFasSiepe(llFasMod.getIdFascicoloSiepe());
		Vector lElencoAttivita = lAttCtrl.ExRicercaAttivita(lAttivitaRicerca);

		// Ricerca Soggetto
		if (llFasMod.getSogIdSoggetto() != null) {
			ISoggetto lSoggCtrl = SICOLookupRemote.getSoggettoRemote();
			lSoggetto = lSoggCtrl.ExRicercaSoggettoByKey(llFasMod.getSogIdSoggetto());
		}
		// Ricerca Fascicolo SIUS
		if (llFasMod.getFasSiuIdFascicoloSius() != null) {
			IFascicoloSius lFasSiusCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasSius = lFasSiusCtrl.ExRicercaFascicoloByKey(llFasMod.getFasSiuIdFascicoloSius());
		}
		// Ricerca Fascicolo SIEP
		if (llFasMod.getFasSieIdFascicoloSiep() != null) {
			IFascicoloSiep lFasSiepCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			lFasSiep = lFasSiepCtrl.ExRicercaFascicoloByKey(llFasMod.getFasSieIdFascicoloSiep());
		}
		// Ricerca Evento
		if (llFasMod.getEveIdEvento() != null) {
			IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
			lEvento = lEveCtrl.ExRicercaEventoByKey(llFasMod.getEveIdEvento());
		}

		// 22/06/2007 Si ricava la descrizione dell'Ufficio Proprietario del Fascicolo SIEPE
		UfficioModel lUffIns = UfficioUtils.getUfficioByCodUfficio(llFasMod.getCodUfficioInserimento());
		llFasMod.setDescrUfficioInserimento(lUffIns.getDescrTipoUfficio() + " " + lUffIns.getDescrComune());

		lFascicoloEsteso = new FascicoloSiepeEstesoModel(llFasMod, lSoggetto, lElencoAttivita, lFasSius,
				lFasSiep, lEvento);
		// setRequestAttribute("soggetto", lSoggetto);

		setSessionAttribute("FascicoloSiepeEsteso", lFascicoloEsteso);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Fascicolo Siepe: " + lFascicoloEsteso.getFascicoloSiepe());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Soggetto: " + lFascicoloEsteso.getSoggetto());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("N.ro attività collegate: " + lFascicoloEsteso.getElencoAttivita().size());
		// Bottone di ritorno
		setLinkRitorno();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + "ActLoadDettaglioFascicoloSiepe: fine");

		return PG_LOAD_DETTAGLIOFASCICOLOSIEPE;
	}

}