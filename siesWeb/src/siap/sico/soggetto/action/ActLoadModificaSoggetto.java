package siap.sico.soggetto.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadModificaSoggetto</p>
 * <p>Description: Azione Load della Modifica del Soggetto </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class ActLoadModificaSoggetto extends ActionSiap implements ICostantiSoggetto {

	/**
	 * Azione di caricamento della form di modifica del Soggetto.
	 * Per modificare il soggetto vengono caricati tutti i dati del
	 * Soggetto.
	 * <p>
	 * @return Nome della pagina JSP su cui posizionarsi
	 * al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "soggetto", getRequestStringParameter(CAMPO_ID_SOGGETTO), getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il/la  " + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}
		// Popola il model.
		SoggettoModel lSogMod = new SoggettoModel();

		lSogMod.setIdSoggetto(getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO));


		// Chiama il controller.
		// SoggettoController lSogCtrl = new SoggettoController();
		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		SoggettoModel lSoggetto = lSogCtrl.ExRicercaSoggettoByKey(lSogMod.getIdSoggetto());

		// SoggettoModel lSoggetto = (SoggettoModel)lSoggetti.firstElement();

		// Imposta la combo nazioni
		Option lOption = new Option(DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"), lSoggetto.getCodStatoNascita());
		setRequestAttribute("nazioni", "" + lOption);

		// SuperSoggetto Ambrosino 04/2010
		// La Nazionalità diventa : Stato Cittadinanza
		// lOption = new Option(
		// DecodificheManager.getInstance().getNazionalita(),
		// lSoggetto.getNazionalita());
		// setRequestAttribute("nazionalita", "" + lOption );

		// paolo 02/09/2010 cambio lOption per aggiungere io "-" anche in modifica visto che è presente in inserimento
		// lOption = new Option(DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance().getStatoCittadinanza(),"-"),lSoggetto.getNazionalita());
		lOption = new Option(DecodificheManager.getInstance().getStatoCittadinanza(), lSoggetto.getNazionalita());
		setRequestAttribute("StatoCittadinanza", "" + lOption);
		//

		lOption = new Option(DecodificheManager.getInstance().getSesso(), lSoggetto.getSesso());
		setRequestAttribute("sesso", "" + lOption);

		// Flag Data Nascita Presunta
		lOption = new Option(DecodificheManager.getInstance().getFlagSNTrattino(), lSoggetto.getDataNascitaPresunta());
		setRequestAttribute("dataNascitaPresunta", "" + lOption);

		// Imposta la risposta nella request.
		setRequestAttribute("modalita", "M");
		setRequestAttribute("soggetto", lSoggetto);

		if (!this.isRequestAttributeNullObj("lTipoFunzione")) {
			// paramentro passato solo nel caso di iscrizione guidata
			this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
		}

		// MEV 15 - Revisione SIGE
		// Aggiunto parametro per identificare la funzione che richiama la maschera
		// di Iscrizione Soggetto da Iscrizione Manuale.
		// Quando viene richiamata da SIGE sulla maschera viene inserito
	    // il Calendario in corrispondenza di ogni campo data
	    String codFunzione = getCodFunMenuVerticale();
		setRequestAttribute("codFunzione", codFunzione);

		return PG_LOAD_INSERISCISOGGETTO; // restituisce la jsp di VIEW
	}

}