package siap.siep.archiviazione.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActUploadPassaggioClasse - Classe per l'upload della stampa della definizione procedimento per passaggio di
 * classe
 *
 * @since MEV_2025-48
 * @author sgioggi
 * @version 1.0
 */
public class ActUploadPassaggioClasse extends ActionSiap implements ICostantiEvento {

	public String processRequest() throws Exception {

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		EventoModel em = new EventoModel();
		IEvento ie = SICOLookupRemote.getEventoRemote();

		em = ie.ExRicercaEventoByKey(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		em.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		InputStream is = getFile(ICostantiEvento.CAMPO_BLOB);

		if (is != null) {
			byte[] bArray = new byte[is.available()];
			is.read(bArray);
			ByteArrayInputStream lSt = new ByteArrayInputStream(bArray);
			em.setDocBlobIn(lSt);
		}

		em.setDataAggiornamento(DateUtils.getSysDate());
		em.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		em.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA)) {
			em.setFlagDocumentoRegistrato("S");
			IArchiviazione ia = SIEPLookupRemote.getArchiviazioneRemote();
			ia.ExUpdateValidaArchiviazione(em, fsm);
		} else {
			em.setFlagDocumentoRegistrato("N");
			ie.ExUpdateDocument(em);
		}

		// Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		/*
		 * dopo aver validato l'evento e quindi archiviato il fascicolo si ha bisogno di una nuova ricerca del
		 * fascicolo per settare il nuovo model del fascicolo in sessione
		 */
		IFascicoloSiep ifs = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel fsmRic = ifs.ExRicercaFascicoloByKey(fsm.getIdFascicoloSiep());

		setSessionAttribute("fascicolo", fsmRic);

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO)) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			rt.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "="
					+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
		}

		// pagina di ritorno
		return IWebConstants.PG_MESSAGE;
	}

}