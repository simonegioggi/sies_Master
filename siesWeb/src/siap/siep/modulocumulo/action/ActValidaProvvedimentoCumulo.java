package siap.siep.modulocumulo.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActValidaProvvedimentoCumulo extends ActionModuloCumulo implements ICostantiModuloCumulo {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// 20170915: [SG] modifica per mancanza di parametro nella form
		// ==========================================================================
		// 15/09/2017 FIX su segnaazione errore di validazione se Action richiamata dalla
		// funzione di cambio magistrato
		// potrei provenire dalla validazione "cambio magistrato", l'unico dato sulla
		// request è l'IdEvento da validare
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lEveCtrl.ExRicercaEventoByKey(lIdEvento);
		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		Vector ListaTitoli = lIstrCtrl.ExRicercaTitoliByIstruttoria(lEventoModel
				.getIstruIdIstruttoriaCumulo());
		// Controllo che non ci siano Titoli Esclusi
		// BigDecimal lIdIstru =
		// getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		// Vector ListaTitoli = super.getListaTitoli();
		// END FIX
		// ==========================================================================

		boolean lEscluso = false;
		// MEV 16 CUMULO: aggiunto controllo preventivo
		if (Utils.isPresent(ListaTitoli)) {
			Iterator itx = ListaTitoli.iterator();
			while (itx.hasNext()) {
				TitoloCumulatoModel lTitoloModel = (TitoloCumulatoModel) itx.next();
				if (lTitoloModel != null && lTitoloModel.getIdTitoloCumulato() != null) {
					if (lTitoloModel.getFlagEscluso() != null
							&& lTitoloModel.getFlagEscluso().compareTo("S") == 0) {
						lEscluso = true;
					}
				}
			}
		}

		if (lEscluso) {
			throw new SIEPException(
					SIEPException.USER_MESSAGE,
					"Uno o più Titoli iscritti in istruttoria risulta momentaneamente escluso. Per procedere alla emissione del provvedimento di Cumulo è necessario prima provvedere ad escludere o includere definitivamente detto Titolo.");
		}

		// ==========================================================================
		// Recupero l'evento da Validare
		// ==========================================================================
		EventoModel lEveModel = new EventoModel();
		lEveModel.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		lEveModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		// ==========================================================================
		// Scarico l'eventuale report
		// ==========================================================================
		InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

		if (lInput != null) {
			byte[] lBuffer = new byte[lInput.available()];
			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lEveModel.setDocBlobIn(lSt);
		}

		// ==========================================================================
		// Imposto i dati dell'aggiornamento
		// ==========================================================================
		lEveModel.setDataAggiornamento(DateUtils.getSysDate());
		lEveModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lEveModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		// ==========================================================================
		// Validazione
		// ==========================================================================
		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA)) {
			lEveModel.setFlagDocumentoRegistrato("S");
			IDatiFinaliCumulo lCtrlDatFin = SIEPLookupRemote.getDatiFinaliCumuloRemote();
			lCtrlDatFin.ExUpdateValidaProvvedimentoCumulo(lEveModel, ListaTitoli);
			
			// Ticket [Ticket#20210430011] 
			// Devo ricaricare in sessione il fascicolo siep in quanto alcuni dati possono essere stati  
			// modificati in fase di validazione: FLAG_CUMULANTE FLAG_ALTRA_CAUSA
			IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFasRet = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFascMod);
			
			setSessionAttribute("fascicolo",lFasRet);			
			// FINE [Ticket#20210430011] 
		} else {
			lEveModel.setFlagDocumentoRegistrato("N");
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lCtrl.ExUpdateDocument(lEveModel);
		}

		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO)) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "="
					+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}

}