package siap.siep.modulocumulo.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;

public class ActLoadRicercaTitoloDaRichiedere extends ActionModuloCumulo implements ICostantiModuloCumulo {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		IstruttoriaCumuloModel lIstruttoria = super.getDatiIstruttoria();

		if (!ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstruttoria.getFlagStato())) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"L'istruttoria corrente non risulta Aperta. Non è possibile effettuare richieste atti.");
		}

		// Ticket#202607240122 — Allert in
		// Si sta per inserire un evento. Anche se da CUMULO deve comunque verificare 
		// che non esistano eventi non validati
		this.isEventoNonValidato();
		// Ticket#202607240122 - FINE
		
		//
		Option lComboAutorità = new Option(DecodificheManager.getInstance().getTipoUfficioSIEP());
		setRequestAttribute("tipoAutorita", "" + lComboAutorità);

		// ==========================================================================
		// Caricamento dati per gestione uffici accorpati
		// ==========================================================================
		IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
		Vector lUffAccTotali = lUffCtrl.ListaUfficiAccorpati("PM", null);
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);

		return PG_LOAD_RICERCA_TITOLO_DA_RICHIEDERE;
	}

}