package siap.sius.documentoallegato.action;

import it.eng.giustizia.avvocatura.util.PropertyUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.avvocatura.action.ICostantiAvvisiAvvocato;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActUploadDocumentoAllegato
 * </p>
 * <p>
 * Description: Effettua Upload del file nella colonna BLOB della tabella DocumentoAllegato.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActUploadDocumentoAllegato extends ActionSiap implements ICostantiDocumentoAllegato {

	public String processRequest() throws Exception {

		// *********************************************
		// MEV_AVVOCATURA - INIZIO
		// **********************************************
		// Flag che segnala la necessità di inserire o meno un record nella tabella
		// AVVISI_AVVOCATO
		String lFlgAvvocatura = "";
		if (!isRequestParameterNullObj("FlagAvvocatura")) {
			lFlgAvvocatura = getRequestStringParameter("FlagAvvocatura");
		}
		// Parametro IdEvento
		BigDecimal IdEvento = null;
		if (!isRequestParameterNullObj("IdEvento")) {
			String rIdEvento = getRequestStringParameter("IdEvento");
			IdEvento = new BigDecimal(rIdEvento);
		}
		// Parametro IdFascicoloSius
		BigDecimal IdFascicoloSius = null;
		if (!isRequestParameterNullObj("IdFascicoloSius")) {
			String rIdFascicoloSius = getRequestStringParameter("IdFascicoloSius");
			IdFascicoloSius = new BigDecimal(rIdFascicoloSius);
		}
		// *********************************************
		// MEV_AVVOCATURA - FINE
		// **********************************************

		DocumentoAllegatoModel lModel = new DocumentoAllegatoModel();

		lModel.setIdDocumentoAllegato(this.getRequestBigDecimalParameter(CAMPO_ID_DOCUMENTO_ALLEGATO));

		InputStream lInput = getFile(CAMPO_BLOB);

		if (lInput != null) {
			byte[] lBuffer = new byte[lInput.available()];

			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);

			lModel.setDocBlobIn(lSt);
		}

		lModel.setDataAggiornamento(DateUtils.getSysDate());

		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());


		if (isRequestChecked(CAMPO_VALIDA))
			lModel.setFlagDocumentoRegistrato("S");
		else
			lModel.setFlagDocumentoRegistrato("N");

		IDocumentoAllegato lCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();

		// *********************************************
		// MEV_AVVOCATURA - INIZIO
		// **********************************************
		if (PropertyUtil.isPresent(lFlgAvvocatura)) {
			Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato = buildAvvisi(IdEvento, IdFascicoloSius,
					lFlgAvvocatura);
			lCtrl.ExUpdateDocument(lModel, lAvvvisiAvvocato);
		} else {
			lCtrl.ExUpdateDocument(lModel);
		}

		// *********************************************
		// MEV_AVVOCATURA - FINE
		// **********************************************

		// Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO)) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&"
					+ CAMPO_ID_DOCUMENTO_ALLEGATO + "="
					+ getRequestStringParameter(CAMPO_ID_DOCUMENTO_ALLEGATO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}

	/**
	 * @param aId
	 * @param idFascicoloSius
	 * @param ufficioUteConnesso
	 * @param lFlgAvvocatura
	 * @return
	 * @throws F3BException
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public Vector<AvvisiAvvocatoModel> buildAvvisi(BigDecimal aId, BigDecimal idFascicoloSius,
			String lFlgAvvocatura) throws Exception {

		Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato = null;

		try {
			if (PropertyUtil.isPresent(aId) && PropertyUtil.isPresent(idFascicoloSius)
					&& PropertyUtil.isPresent(lFlgAvvocatura)) {

				lAvvvisiAvvocato = new Vector<AvvisiAvvocatoModel>();

				String testoAvviso = "";
				if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.FISSAZIONE_UDIENZA)) {
					testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_FISSAZIONE_UDIENZA;
				}
				if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.RINVIO_UDIENZA)) {
					testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_RINVIO_UDIENZA;
				}
				if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.ORDINANZA_RINVIO_UDIENZA)) {
					testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_ORDINANZA_RINVIO_UDIENZA;
				}
				if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.EMISSIONE_ORDINANZA)) {
					testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_EMISSIONE_ORDINANZA;
				}
				if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.INSERIMENTO_RICORSO)) {
					testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_INSERIMENTO_RICORSO;
				}
				if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.INSERIMENTO_OPPOSIZIONE)) {
					testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_INSERIMENTO_OPPOSIZIONE;
				}
				if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.EMISSIONE_DECRETO)) {
					testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_EMISSIONE_DECRETO;
				}
				if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.DEPOSITO_DECRETO)) {
					testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_DEPOSITO_DECRETO;
				}
				if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.DEPOSITO_ORDINANZA)) {
					testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_DEPOSITO_ORDINANZA;
				}

				UtenteModel user = (UtenteModel) getSessionAttribute("UtenteConnesso");
				// Ufficio Emittente
				String ufficio = user.getUfficioUtente().getDescrTipoUfficio() + " di "
						+ user.getUfficioUtente().getDescrComune();

				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoModel eveMod = lCtrlEve.ExRicercaEventoByKey(aId);
				BigDecimal lIdEvento = eveMod.getIdEvento();

				// Recupero il Fascicolo Sius in sessione
				FascicoloGPModel lFasGPMod = null;
				if (isSessionAttributeNullObj("fascicoloSiusGP"))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Fascicolo SIUS non presente in sessione!");

				lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

				// Ricerca avvocati assegnati al fascicolo
				IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
				Vector<AvvocatoSiusModel> avvocati = null;
				avvocati = lAvvCtrl.ExRicercaAvvocatiByFascicoloNoError(lFasGPMod.getFascicoloSiusModel()
						.getIdFascicoloSius());

				// Recupero i dati del soggetto dalla sessione @emma 25/08/2016 - avvocatura
				String cognomeSoggetto = "";
				String nomeSoggetto = "";
				/* 
				 * ISSUE MEV : segnalazione Maffucci oggetto mail: SIUS Avvocati Di pre-esercizio - SIES MO di Roma:
				 * Eliminato recupero dalla session del soggetto che viene inserito nella tabella 
				 * degli avvisi_avvocato 
				 * Numero MEV : MEV_20
				 * Autore    : monica
				 * Data      : 13/mar/2020
				 * Branch    : MEV_20 
				 */
				/*if (!isSessionAttributeNullObj("soggetto")) {
					SoggettoModel datiSoggetto = (SoggettoModel) getSessionAttribute("soggetto");
					cognomeSoggetto = datiSoggetto.getCognome();
					nomeSoggetto = datiSoggetto.getNome();
				} else */
				//***** FINE INTERVENTO MEV_20  *****//
				if (lFasGPMod != null && lFasGPMod.getFascicoloSiusModel() != null) {
					// provo a verificare se è presente nell'oggetto FascicoloGPModel
					cognomeSoggetto = lFasGPMod.getFascicoloSiusModel().getSoggetto() != null ? lFasGPMod
							.getFascicoloSiusModel().getSoggetto().getCognome() : "";
					//EC@19/05/2017: correzione su nomeSoggetto. Inserivamo il nome sul cognome!		
					nomeSoggetto = lFasGPMod.getFascicoloSiusModel().getSoggetto() != null ? lFasGPMod
							.getFascicoloSiusModel().getSoggetto().getNome() : "";
				} else {
					// devo procedere con una ricerca del soggetto per chiave soggetto
					BigDecimal idSoggetto = lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto();
					ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
					SoggettoModel s = lSogCtrl.ExRicercaSoggettoByKey(idSoggetto);
					cognomeSoggetto = s.getCognome();
					nomeSoggetto = s.getNome();
				}

				AvvisiAvvocatoModel lAvvisiAvvModel = null;

				Iterator itxAvv = avvocati.iterator();
				while (itxAvv.hasNext()) {
					lAvvisiAvvModel = new AvvisiAvvocatoModel();

					AvvocatoSiusModel lAvv = (AvvocatoSiusModel) itxAvv.next();

					lAvvisiAvvModel.setIdAvvocato(lAvv.getAvvocato().getIdAvvocato());
					lAvvisiAvvModel.setCognomeSoggeto(cognomeSoggetto);
					lAvvisiAvvModel.setNomeSoggetto(nomeSoggetto);
					// lAvvisiAvvModel.setIdProvvedimento(lIdProvvedimento);
					// setto idEvento (emma 22/08/2016)
					lAvvisiAvvModel.setIdEvento(lIdEvento);
					lAvvisiAvvModel.setDescProvvedimento(eveMod.getDescrTipoProvvedimento());
					lAvvisiAvvModel.setUfficioEmittente(ufficio);
					lAvvisiAvvModel.setTestoAvviso(testoAvviso);
					lAvvisiAvvModel.setFlagVisualizzazione("N");
					lAvvisiAvvModel.setCodOperatoreInserimento(user.getUserId());
					lAvvisiAvvModel.setCodUfficioInserimento(user.getUfficioUtente().getCodUfficio());
					lAvvisiAvvModel.setDataInserimento(DateUtils.getSysDate());

					lAvvvisiAvvocato.add(lAvvisiAvvModel);
				}

			} else {
				throw new SIUSException(
						"AvvisiUtil.buildAvvisi: idEvento or idFascicoloSius or lFlgAvvocatura = null");
			}
		} catch (Exception e) {
			throw new SIUSException("AvvisiUtil.buildAvvisi: " + e);
		}
		return lAvvvisiAvvocato;
	}

}