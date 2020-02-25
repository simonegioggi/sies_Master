package siap.siep.richiesta.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.controller.ITrasmissioneJMS;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActConfermaTrasmissioneRichiestaGenerica
 * </p>
 * <p>
 * Description: Classe Action per la conferma della trasmissione della richiesta generica
 * </p>
 * <p>
 * MEV_39
 * </p>
 * 
 * @author Gioggi
 * @version 1.0
 */
public class ActConfermaTrasmissioneRichiestaGenerica extends ActionSiap implements ICostantiRichiesta,
		ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		boolean inviato = false; // flag di controllo invio messaggio.

		// Si Prepara la trasmissione del Documento ai destinatari.
		String codTipoUfficioDestinatario = getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
		String codLuogoDestinatario = getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO);

		// Dati BDI mittente.
		UfficioModel umMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("BDI MIttente = " + umMittente);

		String codiceUfficio = new String();
		UfficioModel umLocal = new UfficioModel();
		UfficioModel umBDI = new UfficioModel();

		ITrasmissioneJMS itJMS = SIEPLookupRemote.getTrasmissioneJMS();
		MessaggioModel mm = itJMS.getMessageForProvvedimento(idEvento, fsm.getIdFascicoloSiep());

		if (codTipoUfficioDestinatario.trim().compareTo("-") != 0) {
			codiceUfficio = getCodUfficioByCodTipoUfficioDescrComune(codTipoUfficioDestinatario,
					codLuogoDestinatario);
			umLocal = getUfficioByCodUfficio(codiceUfficio);
			umBDI = getUfficioByCodUfficio(umLocal.getCodDistretto());

			mm.setDescrBdiDestinataria(umBDI.getDescrComune());
			mm.setCodBdiDestinataria(umBDI.getCodUfficio());
			mm.setCodBdiMittente(umMittente.getCodUfficio());
			mm.setDescrBdiMittente(umMittente.getDescrComune());
			mm.setCodUfficioDestinatario(codiceUfficio);
			mm.setCodUfficioMittente(getCodUfficioUtenteConnesso());
			mm.setCodTipoMessaggio(RICHIESTA);
			mm.setCodTipoOperazione(TRASFERIMENTO_PROVVEDIMENTO);
			mm.setCodiceUtenteMittente(getCodUtenteConnesso());
			mm.setDataInvio(DateUtils.getSysDate());
			// SETTA RIFERIMENTI FASCICOLO SIEP/SIUS
			mm.setChiaveAnnoSiep(fsm.getChiaveAnno());
			mm.setChiaveProgrSiep(fsm.getChiaveProgr());
			if (fsm.getSoggetto() != null) {
				if (fsm.getSoggetto().getNome() != null)
					mm.setNomeSoggetto(fsm.getSoggetto().getNome());
				if (fsm.getSoggetto().getCognome() != null)
					mm.setCognomeSoggetto(fsm.getSoggetto().getCognome());
				if (fsm.getSoggetto().getDataNascita() != null)
					mm.setDataNascita(fsm.getSoggetto().getDataNascita());
				if (fsm.getSoggetto().getCodComuneNascita() != null)
					mm.setCodComuneNascita(fsm.getSoggetto().getCodComuneNascita());
				if (fsm.getSoggetto().getCodStatoNascita() != null)
					mm.setCodStatoNascita(fsm.getSoggetto().getCodStatoNascita());
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("MESSAGGIO DA SPEDIRE A " + umBDI.getDescrComune());

			SIAPSender lSender = new SIAPSender();
			lSender.send(mm);
			inviato = true;
		}

		if (inviato == false)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione: selezionare almeno un destinatario!");

		setRequestAttribute("IdEvento", idEvento.toString());

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Documento sottomessa al Sistema!");

		// Prepara la "pagina" di destinAction.
		RedirectTo rt = new RedirectTo();
		rt.setPage(IWebConstants.PG_MAIN);
		rt.setParameter("IdEvento", idEvento.toString());
		if (isRequestParameterNullObj("codTipoOperazione")) {
			// setta la risposta nella request
			rt.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Documento sottomessa al Sistema!");
		}
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

		// valore di ritorno
		return IWebConstants.PG_MESSAGE;
	}

}