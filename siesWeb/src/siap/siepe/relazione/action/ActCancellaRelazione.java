package siap.siepe.relazione.action;

import siap.sico.web.ActionSiap;
import siap.siepe.relazione.controller.IRelazione;
import siap.siepe.util.SIEPELookupRemote;

/**
 * <p>
 * Title: ActCancellaRelazione
 * </p>
 * <p>
 * Description: Classe Action per cancellare l'Attività
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActCancellaRelazione extends ActionSiap implements ICostantiRelazione {

	public String processRequest() throws Exception {

		// chiama il controller
		IRelazione lAttCtrl = SIEPELookupRemote.getRelazioneRemote();
		lAttCtrl.ExCancellaRelazione(getRequestBigDecimalParameter(CAMPO_ID_RELAZIONE));

		String retPage = null;

		retPage = ritornoDopoCancellazione("La relazione è stata cancellata!", null);
		return retPage;
	}

}