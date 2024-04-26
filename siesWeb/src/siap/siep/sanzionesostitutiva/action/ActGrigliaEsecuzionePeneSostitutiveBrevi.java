package siap.siep.sanzionesostitutiva.action;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;

/**
 * Aggiunta classe per caricamento griglia
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActGrigliaEsecuzionePeneSostitutiveBrevi extends ActionSiap implements ICostantiSanzioneSostitutiva {

	public String processRequest() throws F3BException {

		if (isSessionAttributeNullObj("fascicolo"))
			setRequestAttribute("fascicoloNotInSession", "S");

		setRequestAttribute("strFunzione", "Gestione Esecuzione Pene Sostitutive Brevi");

		return PG_GRIGLIA_ESECUZIONE_PENE_SOSTITUTIVE_BREVI;
	}

}