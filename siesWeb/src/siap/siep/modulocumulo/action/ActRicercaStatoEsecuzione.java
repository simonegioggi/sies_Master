package siap.siep.modulocumulo.action;

import f3b.util.F3BException;

/**
 * Classe per il caricamento dell'opportuna popup di visualizzazione dello stato esecuzione per una specifica
 * "attività" PM, GE, SORV
 *
 * La Action centralizza le chiamate e smista l'opportuna ricerca
 *
 * @author d.fiorletta
 * @deprecated da eliminare
 */
public class ActRicercaStatoEsecuzione extends ActionModuloCumulo implements ICostantiStatoEsecuzioneCumulo {
	public String processRequest() throws F3BException {
		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		String lTipoAttivita = getRequestStringParameter(CAMPO_TIPO_ATTIVITA);
		// String lCodAttivita = getRequestStringParameter(CAMPO_COD_ATTIVITA);

		// ==========================================================================
		// Implementare qui la ricerca dei provvedimenti di Pagamento PP
		// ==========================================================================
		String lPopUp = "";
		if (lTipoAttivita.equals(CAMPO_TIPO_ATTIVITA_PM)) {
			// lPopUp = this.caricaPopupPM (lCodAttivita);
		} else if (lTipoAttivita.equals(CAMPO_TIPO_ATTIVITA_GE)) {
		} else if (lTipoAttivita.equals(CAMPO_TIPO_ATTIVITA_SORV)) {
		}

		return lPopUp;
	}
}
