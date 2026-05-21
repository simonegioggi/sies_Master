package siap.siepe.fascicolo.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siepe.SIEPEException;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.siepe.util.SIEPELookupRemote;
import siap.sius.ActionSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActRicercaFasSiepePuntuale - Classe Action per la ricerca puntuale del Fascicolo SIEPE
 *
 * @version 1.0
 */
public class ActRicercaFasSiepePuntuale extends ActionSius implements ICostantiFascicoloSiepe {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected FascicoloSiepeEstesoModel mFasSEMod = null;// occorre ereditarlo

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + "ActRicercaFasSiepePuntuale: inizio");

		String lRetPage = PG_LOAD_DETTAGLIOFASCICOLOSIEPE;

		FascicoloSiepeEstesoModel lFascicoloEsteso = null;
		SoggettoModel lSoggetto = null;
		FascicoloGPModel lFasSius = null;
		FascicoloSiepModel lFasSiep = null;

		FascicoloSiepeModel lFasMod = new FascicoloSiepeModel();
		lFasMod.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
		lFasMod.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));
		lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());

		// Ricerca Fascicolo
		IFascicoloSiepe lCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();

		lFasMod = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(lFasMod);

		if (lFasMod == null)
			throw new SIEPEException(SIEPEException.USER_MESSAGE, "Fascicolo non trovato !");

		// Si ricava la descrizione dell'Ufficio Mittente
		if (lFasMod.getCodUfficioMittente() != null && lFasMod.getCodUfficioMittente().trim().length() > 0) {
			UfficioModel lUff = UfficioUtils.getUfficioByCodUfficio(lFasMod.getCodUfficioMittente());
			lFasMod.setDescrUfficioMittente(lUff.getDescrTipoUfficio() + " " + lUff.getDescrComune());
		} else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Manca il cod Ufficio Mittente nel Fascicolo SIEPE !");

		setRequestAttribute("fascicolosiepe", lFasMod);

		// Ricerca Attività collegate
		IAttivita lAttCtrl = SIEPELookupRemote.getAttivitaRemote();
		AttivitaModel lAttivitaRicerca = new AttivitaModel();
		lAttivitaRicerca.setFasSieIdFasSiepe(lFasMod.getIdFascicoloSiepe());
		Vector lElencoAttivita = lAttCtrl.ExRicercaAttivita(lAttivitaRicerca);

		// Ricerca Soggetto
		if (lFasMod.getSogIdSoggetto() != null) {
			ISoggetto lSoggCtrl = SICOLookupRemote.getSoggettoRemote();
			lSoggetto = lSoggCtrl.ExRicercaSoggettoByKey(lFasMod.getSogIdSoggetto());
		}
		// Ricerca Fascicolo SIUS
		if (lFasMod.getFasSiuIdFascicoloSius() != null) {
			IFascicoloSius lFasSiusCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasSius = lFasSiusCtrl.ExRicercaFascicoloByKey(lFasMod.getFasSiuIdFascicoloSius());
		}
		// Ricerca Fascicolo SIEP
		if (lFasMod.getFasSieIdFascicoloSiep() != null) {
			IFascicoloSiep lFasSiepCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			lFasSiep = lFasSiepCtrl.ExRicercaFascicoloByKey(lFasMod.getFasSieIdFascicoloSiep());
		}

		lFascicoloEsteso = new FascicoloSiepeEstesoModel(lFasMod, lSoggetto, lElencoAttivita, lFasSius,
				lFasSiep);
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

		return lRetPage;
	}

}