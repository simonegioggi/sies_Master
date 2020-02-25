package siap.siep.avvocato.action;

import java.util.Vector;

//import siap.sico.ufficio.model.UfficioModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActModificaDifensore
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Avvocato
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
public class ActVisualizzaDifensoreDaModificare extends ActionSiap implements ICostantiAvvocato {

	/**
	 * Azione di Inserimento del Avvocato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("difensore", getRequestStringParameter(CAMPO_ID_AVVOCATO),
				getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il " + lck.getEntity()
					+ " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}
		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector avvocati = new Vector();

		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
			AvvocatoModel lmModelAppo = new AvvocatoModel();
			lmModelAppo
					.setIdAvvocato(this.getRequestBigDecimalParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO));
			avvocati = lCtrl.ExRicercaAvvocatoPerInserimento(lmModelAppo);
			setRequestAttribute("avvocati", avvocati);
		} else {
			AvvocatoModel lAvvModRic = new AvvocatoModel();
			if (!this.isRequestParameterNullObj(CAMPO_COGNOME))
				lAvvModRic.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
			if (!this.isRequestParameterNullObj(CAMPO_NOME))
				lAvvModRic.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());
			if (!this.isRequestParameterNullObj(CAMPO_FORO))
				lAvvModRic.setForo(getRequestStringParameter(ICostantiAvvocato.CAMPO_FORO).toUpperCase());
			lAvvModRic.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());
			if (!this.isRequestParameterNullObj(CAMPO_ANNO_DATA_NASCITA)
					&& (!this.isRequestParameterNullObj(CAMPO_MESE_DATA_NASCITA) && (!this
							.isRequestParameterNullObj(CAMPO_GIORNO_DATA_NASCITA))))
				lAvvModRic.setDataNascita(getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA,
						ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA,
						ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA));

			avvocati = lCtrl.ExRicercaAvvocatoPerInserimento(lAvvModRic);

			if (avvocati.size() > 1) {
				if ((this.getRequestStringParameter(CAMPO_ANNO_DATA_NASCITA).equals("")
						&& this.getRequestStringParameter(CAMPO_MESE_DATA_NASCITA).equals("") && this
						.getRequestStringParameter(CAMPO_GIORNO_DATA_NASCITA).equals(""))
						&& (this.getRequestStringParameter(CAMPO_FORO).equals(""))) {
					throw new F3BException(F3BException.USER_MESSAGE,
							"Dettagliare meglio con data di nascita o Foro la ricerca");
				} else {
					throw new F3BException(F3BException.USER_MESSAGE,
							"Attenzione: esistono più avvocati con gli stessi dati. Richiamare la funzione di ricerca per aggiornare");
				}
			}
			this.setRequestAttribute("avvocati", avvocati);

		}
		AvvocatoModel lAvv = new AvvocatoModel();
		lAvv = (AvvocatoModel) avvocati.get(0);
		Option lOption = new Option(DecodificheManager.getInstance().getListaAttivitaAvvocato(),
				lAvv.getCodNonAttivita());
		setRequestAttribute("autoritaAvvocato", "" + lOption);

		this.setRequestAttribute("modalita", "M");

		// 19/03/2010 Nuova gestione Combo per Foro avvocato.
		// IAvvocato lCtrl1 = SIEPLookupRemote.getAvvocatoRemote();
		// Vector lVect = lCtrl1.ExRicercaForiDisponibili();
		// this.setRequestAttribute("foro", lVect);

		// 19/03/2010 Nuova gestione Combo per Foro avvocato.
		// UfficioModel lUffUte = this.getUfficioUtenteConnesso();
		// String lDescrComune = lUffUte.getDescrComune();

		/*
		 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato
		 * Numero MEV : SIES v10
		 * Autore : gioggi
		 * Data : 28/gen/2016
		 * Branch : MEV_SIES v10
		 */
		// String lDescrComune = lAvv.getForo();
		// lOption = new Option(DecodificheManager.getInstance().getForo(), lDescrComune.toUpperCase().trim(),
		// Option.NO_BLANK_ITEM);
		// ***** FINE INTERVENTO MEV_SIES v10 *****//

		String lStatoForo = DecodificheUtils.getCodAltebyCode(DecodificheManager.getInstance().getForoAll(),
				lAvv.getForo());

		if ("SOPPRESSO".equals(lStatoForo)) {
			lOption = new Option(DecodificheManager.getInstance().getForo(), lAvv.getForo(), true);
		} else {
			lOption = new Option(DecodificheManager.getInstance().getForo(), lAvv.getForo().toUpperCase()
					.trim(), Option.NO_BLANK_ITEM);
		}

		setRequestAttribute("foro", "" + lOption);

		// MEV 15 - Revisione SIGE
		// Aggiunto parametro per identificare la funzione che richiama la maschera
		// di Modifica Difensore.
		// Quando viene richiamata da SIGE sulla maschera viene inserito
		// il Calendario in corrispondenza di ogni campo data
		String codFunzione = getCodFunMenuVerticale();
		setRequestAttribute("codFunzione", codFunzione);

		return PG_DIFENSORE_DA_MODIFICARE;
	}

}