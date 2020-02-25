package siap.sius.documentoallegato.action;

//import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaDocumentoAllegato
 * </p>
 * <p>
 * Description: Azione specializzazione per la ricerca dei documenti allegati ad un Provvedimento.
 * <p>
 * Copyright: Bull Italia S.p.A.Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaDocumentoAllegato extends ActionSiap implements ICostantiDocumentoAllegato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// String page = null;
		setLinkRitorno();
		// Id Evento di ricerca
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ricerca dell'evento
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoModel lEvento = lCtrlEve.ExRicercaEventoByKey(lIdEvento);
		// Si attiva la ricerca del documento allegato
		IDocumentoAllegato lCtrlDoc = SIUSLookupRemote.getDocumentoAllegatoRemote();
		Vector lDocs = lCtrlDoc.ExRicercaDocumentoAllegatoByIdEvento(lIdEvento);

		// Si prepara la pagina di destinazione
		setRequestAttribute("evento", lEvento);
		setRequestAttribute("allegati", lDocs);

		return PG_RICERCADOCUMENTOALLEGATO;
	}

}