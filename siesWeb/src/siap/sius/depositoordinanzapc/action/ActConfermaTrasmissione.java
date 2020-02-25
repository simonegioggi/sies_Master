package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.jms.controller.ITrasmissioneJMS;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActConfermaTrasmissione
 * </p>
 * <p>
 * Description: L'Azione impacchetta i dati da inviare nel messaggio, poi attiva
 * l'invio del messaggio stesso ai destinatari.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: bull
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActConfermaTrasmissione extends ActionSiap implements
		ICostantiDepositoOrdinanzaPc, ICostantiJMS {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		boolean inviato = false; // flag di controllo invio messaggio.

		// Si Prepara la trasmissione dell'Ordinanza.
		// STUB 11/09/2006 I destinatari aggiuntivi sono di 2 tipi (aggiunto
		// l'UEPE)
		String[] lTipoUff = getRequestStringParameters(ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
		String[] lSedeUff = getRequestStringParameters(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO);
		String lTipoUfficio = getUfficioUtenteConnesso().getCodUfficio();

		// Fascicolo SIUS in sessione.
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Dati BDI mittente.
		UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso()
				.getCodDistretto());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("BDI MIttente = " + lBDIMittente);
		String lCodiceUfficio = new String();
		UfficioModel lLocal = new UfficioModel();
		UfficioModel lBDI = new UfficioModel();

		if (lTipoUff[0].trim().compareTo("-") != 0) {
			lCodiceUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(
					lTipoUff[0], lSedeUff[0]);
			lLocal = getUfficioByCodUfficio(lCodiceUfficio);
			lBDI = getUfficioByCodUfficio(lLocal.getCodDistretto());

			ITrasmissioneJMS lCtrlMess = SIUSLookupRemote.getTrasmissioneJMS();
			MessaggioModel lMessage = lCtrlMess.getMessageForOrdinanza(lEveId);

			lMessage.setDescrBdiDestinataria(lBDI.getDescrComune());
			lMessage.setCodBdiDestinataria(lBDI.getCodUfficio());
			lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
			lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
			lMessage.setCodUfficioDestinatario(lCodiceUfficio);
			lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
			lMessage.setCodTipoMessaggio(RICHIESTA);
			// STUB 23/09/2004 Se valorizzato il parametro codTipoOperazione, lo
			// si utilizza per il messaggio.
			if (!this.isRequestParameterNullObj("codTipoOperazione"))
				lMessage.setCodTipoOperazione(getRequestStringParameter("codTipoOperazione"));
			else
				lMessage.setCodTipoOperazione(TRASFERIMENTO_ORDINANZA);
			lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
			lMessage.setDataInvio(DateUtils.getSysDate());

			// SETTA RIFERIMENTI FASCICOLO SIEP/SIUS
			lMessage.setChiaveAnnoSiep(lFasGPMod.getFascicoloSiusModel()
					.getChiaveAnnoSIEP());
			lMessage.setChiaveProgrSiep(lFasGPMod.getFascicoloSiusModel()
					.getChiaveProgrSIEP());
			lMessage.setChiaveAnnoSius(lFasGPMod.getFascicoloSiusModel()
					.getChiaveAnno());
			lMessage.setChiaveProgrSius(lFasGPMod.getFascicoloSiusModel()
					.getChiaveProgr());

			// 27/06/2006 UEPE
			if (lFasGPMod.getFascicoloSiusModel().getSoggetto() != null) {
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto().getNome() != null)
					lMessage.setNomeSoggetto(lFasGPMod.getFascicoloSiusModel()
							.getSoggetto().getNome());
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto()
						.getCognome() != null)
					lMessage.setCognomeSoggetto(lFasGPMod
							.getFascicoloSiusModel().getSoggetto().getCognome());
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto()
						.getDataNascita() != null)
					lMessage.setDataNascita(lFasGPMod.getFascicoloSiusModel()
							.getSoggetto().getDataNascita());
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto()
						.getCodComuneNascita() != null)
					lMessage.setCodComuneNascita(lFasGPMod
							.getFascicoloSiusModel().getSoggetto()
							.getCodComuneNascita());
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto()
						.getCodStatoNascita() != null)
					lMessage.setCodStatoNascita(lFasGPMod
							.getFascicoloSiusModel().getSoggetto()
							.getCodStatoNascita());
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(
					"MESSAGGIO DA SPEDIRE A " + lBDI.getDescrComune() + " : "
							+ lMessage.toString());

			SIAPSender lSender = new SIAPSender();
			lSender.send(lMessage);
			inviato = true;
		}

		// STUB 11/09/2006 Aggiunta destinazioni UEPE.
		if (lTipoUff[1].trim().compareTo("-") != 0) {
			lCodiceUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(
					lTipoUff[1], lSedeUff[1]);
			lLocal = getUfficioByCodUfficio(lCodiceUfficio);
			lBDI = getUfficioByCodUfficio(lLocal.getCodDistretto());

			ITrasmissioneJMS lCtrlMess = SIUSLookupRemote.getTrasmissioneJMS();
			MessaggioModel lMessage = lCtrlMess.getMessageForOrdinanza(lEveId);

			lMessage.setDescrBdiDestinataria(lBDI.getDescrComune());
			lMessage.setCodBdiDestinataria(lBDI.getCodUfficio());
			lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
			lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
			lMessage.setCodUfficioDestinatario(lCodiceUfficio);
			lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
			lMessage.setCodTipoMessaggio(RICHIESTA);
			if (!this.isRequestParameterNullObj("codTipoOperazione"))
				lMessage.setCodTipoOperazione(getRequestStringParameter("codTipoOperazione"));
			else
				lMessage.setCodTipoOperazione(TRASFERIMENTO_ORDINANZA);
			lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
			lMessage.setDataInvio(DateUtils.getSysDate());

			// SETTA RIFERIMENTI FASCICOLO SIEP/SIUS
			lMessage.setChiaveAnnoSiep(lFasGPMod.getFascicoloSiusModel()
					.getChiaveAnnoSIEP());
			lMessage.setChiaveProgrSiep(lFasGPMod.getFascicoloSiusModel()
					.getChiaveProgrSIEP());
			lMessage.setChiaveAnnoSius(lFasGPMod.getFascicoloSiusModel()
					.getChiaveAnno());
			lMessage.setChiaveProgrSius(lFasGPMod.getFascicoloSiusModel()
					.getChiaveProgr());

			// UEPE
			if (lFasGPMod.getFascicoloSiusModel().getSoggetto() != null) {
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto().getNome() != null)
					lMessage.setNomeSoggetto(lFasGPMod.getFascicoloSiusModel()
							.getSoggetto().getNome());
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto()
						.getCognome() != null)
					lMessage.setCognomeSoggetto(lFasGPMod
							.getFascicoloSiusModel().getSoggetto().getCognome());
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto()
						.getDataNascita() != null)
					lMessage.setDataNascita(lFasGPMod.getFascicoloSiusModel()
							.getSoggetto().getDataNascita());
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto()
						.getCodComuneNascita() != null)
					lMessage.setCodComuneNascita(lFasGPMod
							.getFascicoloSiusModel().getSoggetto()
							.getCodComuneNascita());
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto()
						.getCodStatoNascita() != null)
					lMessage.setCodStatoNascita(lFasGPMod
							.getFascicoloSiusModel().getSoggetto()
							.getCodStatoNascita());
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(
					"MESSAGGIO DA SPEDIRE A " + lBDI.getDescrComune() + " : "
							+ lMessage.toString());

			SIAPSender lSender = new SIAPSender();
			lSender.send(lMessage);
			inviato = true;
		}

		// STUB 29/06/2004 Rework Destiniazioni multiple.
		// String[] lCheckUffici = null;
		// if (!isRequestParameterNullObj("lCheckUffici")) {
		// lCheckUffici = getRequestStringParameters("lCheckUffici");

		// MERGE v 10: aggiunti controlli preventivi
		String lCodUfficio[] = null;
		if (!isRequestParameterNullObj("lCodUfficio"))
			lCodUfficio = getRequestStringParameters("lCodUfficio");
		else
			lCodUfficio = new String[0];
		String lChiaveAnno[] = null;
		if (!isRequestParameterNullObj("lChiaveAnno"))
			lChiaveAnno = getRequestStringParameters("lChiaveAnno");
		else
			lChiaveAnno = new String[0];
		String lChiaveProgr[] = null;
		if (!isRequestParameterNullObj("lChiaveProgr"))
			lChiaveProgr = getRequestStringParameters("lChiaveProgr");
		else
			lChiaveProgr = new String[0];

		int lSize = lCodUfficio.length;
		String[] lCheckUffici = new String[lSize];
		boolean ufficiSelezionato = false;
		for (int x = 0; x < lSize; x++) {
			if (!isRequestParameterNullObj("lCheckUffici" + x)) { // Vector
																	// Uffici;
				lCheckUffici[x] = getRequestStringParameter("lCheckUffici" + x);
				ufficiSelezionato = true;
			} else {
				lCheckUffici[x] = null;
			}
		}
		if (ufficiSelezionato) {
			for (int x = 0; x < lSize; x++) {
				if (lCheckUffici[x] != null && lCheckUffici[x].equals("on")) {
					lCodiceUfficio = lCodUfficio[x];
					lLocal = getUfficioByCodUfficio(lCodiceUfficio);
					lBDI = getUfficioByCodUfficio(lLocal.getCodDistretto());

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("BDI Destinatario = " + lBDI);

					ITrasmissioneJMS lCtrlMess = SIUSLookupRemote
							.getTrasmissioneJMS();
					MessaggioModel lMessage = lCtrlMess
							.getMessageForOrdinanza(lEveId);

					lMessage.setDescrBdiDestinataria(lBDI.getDescrComune());
					lMessage.setCodBdiDestinataria(lBDI.getCodUfficio());
					lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
					lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
					lMessage.setCodUfficioDestinatario(lCodiceUfficio);
					lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
					lMessage.setCodTipoMessaggio(RICHIESTA);
					// STUB 23/09/2004 Se valorizzato il parametro
					// codTipoOperazione, lo si utilizza per il messaggio.
					if (!this.isRequestParameterNullObj("codTipoOperazione"))
						lMessage.setCodTipoOperazione(getRequestStringParameter("codTipoOperazione"));
					else
						lMessage.setCodTipoOperazione(TRASFERIMENTO_ORDINANZA);

					lMessage.setCodiceUtenteMittente(this
							.getCodUtenteConnesso());
					lMessage.setDataInvio(DateUtils.getSysDate());

					// SETTA RIFERIMENTI FASCICOLO SIEP/SIUS.
					if (!lChiaveAnno[x].equalsIgnoreCase("null")
							&& !lChiaveAnno[x].equalsIgnoreCase(""))
						lMessage.setChiaveAnnoSiep(new BigDecimal(
								lChiaveAnno[x]));
					if (!lChiaveProgr[x].equalsIgnoreCase("null")
							&& !lChiaveProgr[x].equalsIgnoreCase(""))
						lMessage.setChiaveProgrSiep(new BigDecimal(
								lChiaveProgr[x]));
					lMessage.setChiaveAnnoSius(lFasGPMod
							.getFascicoloSiusModel().getChiaveAnno());
					lMessage.setChiaveProgrSius(lFasGPMod
							.getFascicoloSiusModel().getChiaveProgr());

					// 27/06/2006 UEPE
					if (lFasGPMod.getFascicoloSiusModel().getSoggetto() != null) {
						if (lFasGPMod.getFascicoloSiusModel().getSoggetto()
								.getNome() != null)
							lMessage.setNomeSoggetto(lFasGPMod
									.getFascicoloSiusModel().getSoggetto()
									.getNome());
						if (lFasGPMod.getFascicoloSiusModel().getSoggetto()
								.getCognome() != null)
							lMessage.setCognomeSoggetto(lFasGPMod
									.getFascicoloSiusModel().getSoggetto()
									.getCognome());
						if (lFasGPMod.getFascicoloSiusModel().getSoggetto()
								.getDataNascita() != null)
							lMessage.setDataNascita(lFasGPMod
									.getFascicoloSiusModel().getSoggetto()
									.getDataNascita());
						if (lFasGPMod.getFascicoloSiusModel().getSoggetto()
								.getCodComuneNascita() != null)
							lMessage.setCodComuneNascita(lFasGPMod
									.getFascicoloSiusModel().getSoggetto()
									.getCodComuneNascita());
						if (lFasGPMod.getFascicoloSiusModel().getSoggetto()
								.getCodStatoNascita() != null)
							lMessage.setCodStatoNascita(lFasGPMod
									.getFascicoloSiusModel().getSoggetto()
									.getCodStatoNascita());
					}
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug(
							"MESSAGGIO DA SPEDIRE A " + lBDI.getDescrComune()
									+ " : " + lMessage.toString());
					SIAPSender lSender = new SIAPSender();
					lSender.send(lMessage);
					inviato = true;
				}
			}
		}
		if (inviato == false)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione: selezionare almeno un destinatario! ");

		setRequestAttribute("IdEvento", lEveId.toString());

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT,
				"Trasmissione Ordinanza sottomessa al Sistema!");

		// Prepara la "pagina" di destinAction.
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setParameter("IdEvento", lEveId.toString()); // 04/06/2004
		if (this.isRequestParameterNullObj("codTipoOperazione")) {
			// setta la risposta nella request
			lRedirigi
					.setAction("siap.sius.depositoordinanzapc.action.ActLoadTrasferisciOrdinanza");
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Trasmissione Ordinanza sottomessa al Sistema!");
		} else {
			lRedirigi.setParameter(
					ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO, "03");
			String strMessage = "";
			if (lTipoUfficio.compareTo("UDS") == 0)
				strMessage = "Trasmissione Impugnazione sottomessa al Sistema!";
			else
				strMessage = "Trasmissione Ricorso sottomessa al Sistema!";
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, strMessage);
			lRedirigi
					.setAction("siap.sius.impugnazione.action.ActLoadDettaglioImpugnazione");
		}

		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;
	}

}