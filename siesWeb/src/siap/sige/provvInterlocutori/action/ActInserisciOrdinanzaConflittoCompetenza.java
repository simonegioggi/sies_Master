package siap.sige.provvInterlocutori.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoNotificaModel;
import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.action.ActInserisciEmissioneOrdinanza;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.tenore.util.TenoriSigeUtil;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActInserisciOrdinanzaConflittoCompetenza
 * </p>
 * <p>
 * Description: Classe Action per la memorizzazione dell' Ordinanza di Conflitto di Competenza.
 * </p>
 * <p>
 * Company:
 * </p>
 * <p>
 * Author:
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciOrdinanzaConflittoCompetenza extends ActInserisciEmissioneOrdinanza implements
		ICostantiProvvInterlocutoriSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciOrdinanzaConflittoCompetenza: inizio");

		// Valorizzazione Provvedimento ed Evento
		ProvvedimentoSigeEventoModel lProvEveModel = letturaDatiProvvedimento();

		if (isSessionAttributeNullObj("tenori"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Oggetti assenti !");

		// Si prelevano i tenori Sige in sessione.
		Vector lTenoriEstesi = (Vector) getSessionAttribute("tenori");

		if (lTenoriEstesi.size() == 0)
			throw new SIGEException(SIGEException.USER_MESSAGE, "Oggetti assenti !");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Dimensione lista Tenori Estesi :" + lTenoriEstesi.size());

		// In sessione c'è una lista di TenoreEstesoModel
		TenoriSigeUtil lTenUtil = new TenoriSigeUtil();
		Vector lTenori = lTenUtil.listaTenoriDaListaTenoriEstesi(lTenoriEstesi);
		if (lTenori != null)
			for (int i = 0; i < lTenori.size(); i++) {
				// Valorizzazione dell'Esito Automatico previsto per tale Ordinanza
				((TenoreSigeModel) lTenori.get(i)).setCodEsitoSige(COD_ESITO_CONFLITTO_COMPETENZA);
			}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Dimensione lista Tenori :" + lTenori.size());

		// Impostazione dati del Provvedimento specifici di questo tipo di ordinanza
		ProvvedimentoSigeModel lProv = lProvEveModel.getProvvedimento();

		lProv.setCodTipoProvvedimentoSige(COD_ORDINANZA_CONFLITTO_COMPETENZA);
		//@emma 26072018 intervento post COLLAUDO 11.2 (Nunzia segnala che con l'emissione di un ordinanza di Conflitto di competenza si chiude il fascicolo,
		// quindi l'ordinanza di ordinanza di Conflitto di competenza NONN DEVE ESSERE UN PROVVEDIMENTO INTERLOCUTORIO)
		lProv.setDefinitorio("S");
		lProv.setNote(getRequestStringParameter(CAMPO_NOTE));

		// Estrazione di EventoNotifica
		EventoNotificaModel lEveNotMod = lProvEveModel.getEventoNotifica();

		// Lettura Ufficio di Competenza Corte Suprema di Cassazione
		String lCodTipoUffCompCorteSuprema = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_UFF_COMP_CORTE_SUPREMA);
		String lDescComuneSedeUffCompCorteSuprema = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFF_COMP_CORTE_SUPREMA);
		String lCodUffCompCorteSuprema = null;
		if (lCodTipoUffCompCorteSuprema != null && !lCodTipoUffCompCorteSuprema.equals("-")
				&& !lCodTipoUffCompCorteSuprema.equals("")) {
			lCodUffCompCorteSuprema = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUffCompCorteSuprema,
					lDescComuneSedeUffCompCorteSuprema);
			lProv.setCodUffCompCorteSuprema(lCodUffCompCorteSuprema);
		}

		// Lettura Ufficio di Competenza
		String lCodTipoUfficioCompetenza = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO);
		String lDescComuneSede = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO);
		String lCodUffCompetenza = null;

		if (!lCodTipoUfficioCompetenza.equals("-") && !lDescComuneSede.equals("")) {
			lCodUffCompetenza = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficioCompetenza,
					lDescComuneSede);
			lProv.setCodUfficioDestinatario(lCodUffCompetenza);
		}

		// preleva i dati della udienza sige
		BigDecimal idUdienza = super
				.getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
		lProv.setUdiIdUdienzaSige(idUdienza);
		// lProv.getIdEventoGenerato();
		// loadDatiUdienzaSige();

		lProvEveModel.setProvvedimento(lProv);

		// Inserimento
		IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();
		lProvEveModel = lProvCtrl.ExInserisciProvvEveNotifica(lProvEveModel, lEveNotMod, lTenori,
				mTipoGiudizio);

		// Si Rilegge il fasciclo SIGE Esteso e lo si inserisce in SESSIONE.
		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
		IFascicoloSige lCtrlFas = SIGELookupRemote.getFascicoloSigeRemote();
		lFasEsteso = lCtrlFas.ExRicercaEstesaFascicoloSigeByKey(lFasEsteso.getFascicoloSige()
				.getIdFascicoloSige());
		setSessionAttribute("FascicoloSigeEsteso", lFasEsteso);

		// Rimozione dell'elenco Tenori dalla sessione
		removeSessionAttribute("tenori");

		// Prepara la "pagina" di destinazione
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sige.provvInterlocutori.action.ActDettaglioOrdinanzaConflittoCompetenza");
		lRedirigi.setParameter(CAMPO_ID_PROVVEDIMENTO_SIGE,
				(lProvEveModel.getProvvedimento().getIdProvvedimentoSige()).toString());
		lRedirigi.setParameter("modalita", "I");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciOrdinanzaConflittoCompetenza: fine");

		return lRedirigi.toString();

	}

}