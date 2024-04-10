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
public class ActGrigliaSemilibertaDetenzioneDomiciliare extends ActionSiap implements ICostantiSanzioneSostitutiva {

	public String processRequest() throws F3BException {

		if (isSessionAttributeNullObj("fascicolo"))
			setRequestAttribute("fascicoloNotInSession", "S");

		setRequestAttribute("strFunzione", "Gestione Pene Sostitutive: Semilibert&agrave;/Detenzione Domiciliare");

		return PG_GRIGLIA_SEMILIBERTA_DETENZIONE_DOMICILIARE;
	}

}