package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel;
import siap.siep.calcolopena.controller.ICalcoloPenaF5;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/*******************************************************************************
 * Classe action per la Load della jsp di visualizzazione del dettaglio delle annotazioni manuali inserite da
 * Decisioni del GE - Applicazione Benefici: - Amnistia/Indulto - Depenalizzazione - Incostituzionalità Questa
 * action viene invocata <b>SOLO</b> dopo la fase di inserimento. Per la load del dettaglio di annotazioni già
 * a sistema vengono invece utilizzate tre action distinte:
 *
 * - ActLoadDettaglioAnnotazioniAmnistiaIndulto - ActLoadDettaglioAnnotazioniDepenalizzazione -
 * ActLoadDettaglioAnnotazioniIncostituzionalita
 *
 * Tali action estendono la classe padre ActLoadDettaglioAnnotazioneManuale del package
 * siap.siep.annotazionemanuale.action
 *
 *
 */
public class ActLoadDettaglioAnnotazioniManuali extends ActionSiap implements ICostantiAnnotazioneManuale {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		String lMotivoProvvedimento = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);
		String lTipoAnnotazione = getRequestStringParameter(
				ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lMotivoProvvedimento = " + lMotivoProvvedimento);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lTipoAnnotazione = " + lTipoAnnotazione);

		setRequestAttribute("MotivoProvvedimento", lMotivoProvvedimento);

		// ==========================================================================
		// Recupero le annotazioni associate al fascicolo senza richiesta non ancora
		// validate
		// ==========================================================================
		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();

		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();
		lAnnMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lAnnMod.setCodTipoAnnotazione(lTipoAnnotazione);
		lAnnMod.setFlagAppProvvisoria("-"); // NON RICHIESTE
		lAnnMod.setFlagValidato("N");

		Vector lListAnnMan = lCtrlAnnMan.ExRicercaAnnotazioneManualeGenerico(lAnnMod);
		setRequestAttribute("ListaAnnotazioni", lListAnnMan);

		// ==========================================================================
		// Recupero i dati dell'annotazione appena inserita
		// ==========================================================================
		BigDecimal lIdAnnMan = getRequestBigDecimalParameter(CAMPO_ID_ANNOTAZIONE_MANUALE);

		AnnotazioneManualeModel lAnnManIns = lCtrlAnnMan.ExRicercaAnnotazioneManualeByKey(lIdAnnMan);

		setRequestAttribute("AnnotazioneManualeInserita", lAnnManIns);

		// ==========================================================================
		// Recupero i dati dell'ordinanza e dell'annotazione associata
		// ==========================================================================
		IEvento ICtrlEve = SICOLookupRemote.getEventoRemote();

		EventoModel lEveModPar = new EventoModel();
		lEveModPar.setCodTipoEvento("01");
		lEveModPar.setCodTipoProvvedimento("03");
		lEveModPar.setCodMotivo(lMotivoProvvedimento);
		lEveModPar.setFasSieIdFascicoloSiep(lIdFascicolo);

		EventoModel lEveMod = ICtrlEve.ExRicercaUltimoTipoEventoByIdFascicolo(lEveModPar);

		AnnotazioneManualeModel lAnnGE = null;
		if (lEveMod != null && lEveMod.getIdEvento() != null)
			lAnnGE = lCtrlAnnMan.ExRicercaAnnotazioniManualiByIdEvento(lEveMod.getIdEvento());

		AnnotazioneOrdinanzaModel lAnnOrdMod = new AnnotazioneOrdinanzaModel(lAnnGE, lEveMod);

		setRequestAttribute("AnnotazioneOrdinanza", lAnnOrdMod);

		// ==========================================================================
		// Provo a vedere se esiste una richiesta per la quale sia stata utilizzata
		// la data di scarcerazione nei calcoli della pena
		// ==========================================================================
		ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
		Date lDataScarcerazione = lCalcPenaF5.getDataScarcerazione(lIdFascicolo);
		setRequestAttribute("dataScarcerazione", lDataScarcerazione);

		setRequestAttribute("lPageGE", this.getRequestStringParameter("lFlagPage"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lFlagPage = " + this.getRequestStringParameter("lFlagPage"));

		return PG_LOAD_DETTAGLIO_ANN_MANUALI;
	}
}