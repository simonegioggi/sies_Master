package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Collection;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * Action per la Load inserimento del Sollecito di una risposta alla trasmissioni atti per competenza al fine
 * dell'esecuzione delle Misure di Sicurezza
 * 
 * @author d.fiorletta
 */
public class ActLoadInserisciSollecitoEsitoTrasmissione extends ActionSiap implements
		ICostantiMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lCodUfficioDaSollecitare = null;

		if (isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
			// Provengo dalla Griglia, devo avere il fascicolo in sessione
			// n.b. per ora non implementato il tasto in griglia
			if (this.isSessionAttributeNullObj("fascicolo")) {
				return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
			}
			// TODO in caso si prevedesse l'aggancio della funzione dalla griglia il
			// va implememntata opportunamente tale gestuone non essendo disponibili
			// i dati dalla tabella messaggio

		} else {
			// n.b. Il sollecito può essere effettuato o sul messaggio di Invio o su una
			// risposta di inoltro
			BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
			IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
			MessaggioModel lMessaggio = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

			if (ICostantiJMS.DELIVERY_MODE_INVIATO.equals(lMessaggio.getDeliveryMode())) {
				// Sto sollecitando il messaggio originario
				lCodUfficioDaSollecitare = lMessaggio.getCodUfficioDestinatario();
				setRequestAttribute("messaggioRich", lMessaggio);
			} else {
				// Sto sollecitando un inoltro. Il messaggio è il messaggio di risposta
				// dell'ufficio che ha inoltrato i dati.
				lCodUfficioDaSollecitare = lMessaggio.getCodUfficioInoltro();

				setRequestAttribute("messaggioInoltro", lMessaggio);

				MessaggioModel lMessaggioRichiesta = lCrtl.ExRicercaMessaggioByKey(new BigDecimal(lMessaggio
						.getJmsCorrelationIdMessage()));
				setRequestAttribute("messaggioRich", lMessaggioRichiesta);
			}

			// ========================================================================
			// Carico in sessione il fascicolo su cui registrare il sollecito
			// ========================================================================
			// Recuperao il fascicolo da mettere in sessione
			BigDecimal lChiaveAnno = lMessaggio.getChiaveAnnoSiep();
			BigDecimal lChiaveProgr = lMessaggio.getChiaveProgrSiep();
			FascicoloSiepModel lFasMod = new FascicoloSiepModel();

			lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());
			lFasMod.setChiaveProgr(lChiaveProgr);
			lFasMod.setChiaveAnno(lChiaveAnno);

			IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFasRet = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

			// Pulisco la sessione
			setSessionAttribute("fascicolo", null);
			setSessionAttribute("soggetto", null);
			setSessionAttribute("sentenza", null);

			setSessionAttribute("cumulowiz", null);
			setSessionAttribute("penaresidua", null);
			setSessionAttribute("reato", null);

			// Metto in sessione il fascicolo
			setSessionAttribute("fascicolo", lFasRet);
			setSessionAttribute("soggetto", lFasRet.getSoggetto());
			setSessionAttribute("sentenza", lFasRet.getSentenza());
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// ==========================================================================
		// Verifico se fascicolo di competenza
		// ==========================================================================
		this.isFascicoloSiepDiCompetenza();

		// ==========================================================================
		// Controllo Validazione Fascicolo
		// ==========================================================================
		if (this.isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;
		// ==========================================================================
		// Controllo Fascicolo definito
		// ==========================================================================
		if (this.isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;
		// ==========================================================================
		// Verifico se esistono eventi non validati
		// ==========================================================================
		this.isEventoNonValidato();

		// ========================
		// Dati per le combo
		// ========================
		UfficioModel lUfficio = getUfficioByCodUfficio(lCodUfficioDaSollecitare);
		setRequestAttribute("ufficioSollecito", lUfficio);

		// Oggetto evento di sollecito
		Collection lMotiviSollecto = DecodificheManager.getInstance().getMotivoSollecitoMisureSicurezza();
		DecodificheModel lOggettoProvvedimento = (DecodificheModel) lMotiviSollecto.toArray()[0];
		setRequestAttribute("oggettoProvvedimento", lOggettoProvvedimento);

		// MAGISTRATO COMPETENTE
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistratocompetente", lMagi);

		// ALTRO DESTINATARIO x la notifica
		Option lAEOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaN", "" + lAEOption);

		return PG_LOAD_INSERISCI_SOLLECITO_ESITO;
	}

}