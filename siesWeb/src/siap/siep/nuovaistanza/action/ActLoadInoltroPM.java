/**
 *
 */
package siap.siep.nuovaistanza.action;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.EventoController;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.NuovaIstanzaController;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActLoadInoltroPM
 * 
 * @author Giselda De Vita
 *
 */
public class ActLoadInoltroPM extends ActionSiap implements ICostantiNuovaIstanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/* Controllo se un fascicolo e' presente in sessione altrimenti lo faccio selezionare */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (isSessionAttributeNullObj("fascicolo")) {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
					+ "siap.siep.nuovaistanza.action.ActLoadInoltroPM";
			return lPage;
		}

		isFascicoloSiepDiCompetenza();

		// ricerca Istanza per il fascicolo in sessione
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		NuovaIstanzaController lIstController = new NuovaIstanzaController();
		EventoController lEveController = new EventoController();

		// Per visualizzare l'inoltro in caso di registro istanze oppure solo istanza
		Collection<NuovaIstanzaModel> lVect1 = new Vector<>();
		Vector lVectEventiInoltro = null;
		EventoModel evemod = new EventoModel();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("MAPPA DELLA REQUEST=" + this.getRequestParameterMap().toString());

		if (isRequestParameterNullObj(ICostantiNuovaIstanza.CAMPO_ID_NUOVA_ISTANZA)) {
			lVect1 = lIstController.ExRicercaNuovaIstanzaByIdFascicolo(lFascMod.getIdFascicoloSiep());
			evemod.setCodMotivo("1001");
			evemod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			try {
				lVectEventiInoltro = lEveController.ExRicercaEvento(evemod);
			} catch (F3BException fe) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info(fe);
			}
		} else {
			java.math.BigDecimal idista = getRequestBigDecimalParameter(
					ICostantiNuovaIstanza.CAMPO_ID_NUOVA_ISTANZA);
			NuovaIstanzaModel lModel = lIstController.ExRicercaNuovaIstanzaById(idista);
			lVect1.add(lModel);
			evemod.setCodMotivo("1001");
			evemod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			evemod.setEveIdEvento(lModel.getEveIdEvento());
			try {
				lVectEventiInoltro = lEveController.ExRicercaEvento(evemod);
			} catch (F3BException fe) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info(fe);
			}
		}
		this.setRequestAttribute("listaeventi", lVectEventiInoltro);
		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.info("SIZE DEGLI EVENTI="+lVectEventiInoltro.size());

		Collection<NuovaIstanzaModel> lVect = new Vector<>();
		if (lVect1 != null && lVect1.size() > 0) {
			java.util.Iterator<NuovaIstanzaModel> lItx = lVect1.iterator();
			while (lItx.hasNext()) {
				NuovaIstanzaModel lModel = lItx.next();
				if (lModel.getDataInoltroPM() != null) {
					String IsInoltroValidato = lIstController
							.ExRicercaFlagValNuovaIstanza(lModel.getEveIdEvento(), "I");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("IsInoltroValidato=" + IsInoltroValidato + "<<<");
					lModel.setCodEsito(IsInoltroValidato.substring(2));
					if (IsInoltroValidato.charAt(0) == 'N') {
						// Appoggio i due dati nel model solo per poterli gestire nella jsp
						// ma NON perchè devo cambiarli!!
						lModel.setDescrStatoIstanza("Inoltro da Validare");
					}
				}
				// 14/03/2011 Lettura dell'Avvocato.
				if (lModel.getAvvIdAvvocato() != null) {
					AvvocatoModel lAvvMod = new AvvocatoModel();
					lAvvMod.setIdAvvocato(lModel.getAvvIdAvvocato());

					IAvvocato lCtrlAvv = SIEPLookupRemote.getAvvocatoRemote();
					AvvocatoModel lAvv = lCtrlAvv.ExRicercaAvvocatoByKey(lModel.getAvvIdAvvocato());
					lModel.setAvvocato(lAvv);
				}

				lVect.add(lModel);
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("SIZE DELLe istanze ancora non inoltrate=" + lVect.size());
		if (lVect == null || (lVect != null && lVect.size() == 0)) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Nessuna Istanza da inoltrare sul procedimento " + lFascMod.getChiaveAnno() + "/"
							+ lFascMod.getChiaveProgr() + "<br>Impossibile procedere.");
			setRequestAttribute(IWebConstants.GOTO_PAGE,
					"/jsp/Main.jsp?Action=siap.sico.security.action.ActLoadOrizontalMenu&IdFunzione=21120240");

			return IWebConstants.PG_MESSAGE;
		}

		this.setRequestAttribute("listaIstanze", lVect);

		// Inserire Eventuali ComboBOX
		Option lOption = new Option(DecodificheManager.getInstance().getTipoContenutoIstanza());
		setRequestAttribute("oggettoIstanza", "" + lOption);

		if (lVect.size() == 1) {
			java.util.Iterator<NuovaIstanzaModel> lItx = lVect.iterator();
			NuovaIstanzaModel lModel = lItx.next();
			if (lModel.getDataInoltroPM() != null && lVectEventiInoltro.size() == 0) {
				String lPage = "";
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza";
				lPage += "&" + "IdEvento" + "=" + lModel.getCodEsito();
				lPage += "&" + "TipoVis=Inoltro";
				return lPage;
			} else
				return PG_LOAD_INSERISCI_INOLTRO_PM;
		} else
			return PG_LOAD_INSERISCI_INOLTRO_PM;

	}

}