package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * Title: ActLoadCancellaProvvedimento 
 * Description: Classe Action per la load di cancellazione evento
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadCancellaProvvedimento extends ActionSiap implements ICostantiOrdineEsecuzione {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// Controllo non effettuato nel caso di provvedimento SIUS
		if (isRequestParameterNullObj("campoSIUS"))
			isFascicoloSiepDiCompetenza();
		else {
			setRequestAttribute("campoSIUS", getRequestStringParameter("campoSIUS"));
			// In SIUS
			// Per un'ordinanza di LA su cui sia già stato emesso un provvedimento della procura o ci siano
			// comunque licenze
			// con flag_elaborato ad "S" vanno bloccate le modifiche
			IDepositoOrdinanzaPc idop = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			DepositoOrdinanzaPcModel dopm = idop
					.ExRicercaDepositoOrdinanzaPcByEvento(getRequestBigDecimalParameter("IdEvento"));
			ILicenzaPeriodiLibAnticipata ilpla = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
			Vector licenze = new Vector();
			try {
				licenze = ilpla.ExRicercaLicenzeByEve(getRequestBigDecimalParameter("IdEvento"));
			} catch (F3BException F3BEx) {
				siesLogger.debug("F3BException: " + F3BEx);
			}

			if (dopm != null && dopm.getCodTipoOrdinanza().compareTo("LA") == 0
					&& checkStatoElaborazioneLA(dopm, licenze))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Non è consentito annullare il provvedimento! "
								+ "La liberazione anticipata è già stata elaborata dalla procura!");
		}

		// 12/12/2019 - Ticket 201911260116 - Non si consente la cancellazione del provvedimento in caso di
		// presenza di Comunicazioni Cumulo afferenti non validate
		EventoModel emRic = new EventoModel();

		emRic.setCodTipoEvento("01");
		emRic.setCodTipoProvvedimento("12");
		emRic.setCodMotivo("0670");

		/*
		 * ISSUE MAC : recupero l'id fascicolo e lo setto nell'evento 
		 * Numero MAC : 20200331014 
		 * Autore : monica
		 * Data : 02/apr/2020 
		 * Branch : mac-otrs-20200331014
		 */
		BigDecimal idFascicoloSiep = null;
		if (!isSessionAttributeNullObj("fascicolo")) {
			idFascicoloSiep = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
			emRic.setFasSieIdFascicoloSiep(idFascicoloSiep);
		}
		// ***** FINE INTERVENTO mac-otrs-20200331014 *****//

		emRic.setEveIdEvento(getRequestBigDecimalParameter("IdEvento"));

		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoModel em = new EventoModel();
		em = ie.ExRicercaEventoNonRegistrato(emRic);
		if (em != null) {
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Non è consentito annullare il provvedimento! "
							+ "Sono presenti Comunicazioni Cumulo non validate!");
		}

		IOrdineEsecuzione ioe = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		CampoNotaModel cnm = ioe
				.ExRicercaEventoCampoNotaByIdEvento(getRequestBigDecimalParameter("IdEvento"));
		setRequestAttribute("camponota", cnm);

		em = ie.ExRicercaEventoByKey(getRequestBigDecimalParameter("IdEvento"));
		setRequestAttribute("evento", em);

		setRequestAttribute("IdEvento", getRequestStringParameter("IdEvento"));
		// 10/04/2006 il parametro nextAction consente di prestabilire l'azione da eseguire
		// alla fine dell'attività di revoca/cancellazione provvedimento.
		if (!isRequestParameterNullObj("nextAction"))
			setRequestAttribute("nextAction", getRequestStringParameter("nextAction"));

		if (!isRequestParameterNullObj("lOrdinamento"))
			setRequestAttribute("lOrdinamento", getRequestStringParameter("lOrdinamento"));

		// 08/09/2015 L'’annullamento dell'’evento di Comunicazione Richiesta impossibilità esazione Pena
		// Pecuniaria deve essere consentito solo se al procedimento SIEP non è ancora collegato un
		// procedimento SIUS di Conversione Pene Pecuniarie
		if (em.getCodMotivo().compareTo("0942") == 0) {
			IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
			Vector fascicoliPerNumeroSIEP = new Vector();
			try {
				fascicoliPerNumeroSIEP = ifs.ExRicercaFascicoliPerNumeroSIEP(em.getFasSieIdFascicoloSiep());
			} catch (F3BException F3BEx) {
				siesLogger.debug("F3BException: " + F3BEx);
			}

			if (fascicoliPerNumeroSIEP != null && fascicoliPerNumeroSIEP.size() > 0) {
				Iterator itx1 = fascicoliPerNumeroSIEP.iterator();
				while (itx1.hasNext()) {
					FascicoloGPModel fgpm = (FascicoloGPModel) itx1.next();
					if (fgpm.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.compareTo("U070") == 0)
						throw new SIUSException(SIUSException.USER_MESSAGE,
								"Non è consentito annullare il provvedimento! "
										+ "La Comunicazione di Richiesta impossibilità esazione Pena Pecuniaria"
										+ " è collegata ad un fascicolo SIUS di Conversione Pene Pecuniarie!");
				}
			}
		}

		/*
		 * ISSUE MEV : In caso di Procedimento con Ordine di ingiunzione al pagamento emesso e validato e poi
		 * 				annullato, in quanto errato, al momento dell'annullamento dell'OI, il sistema
		 * 				storicizza i dati della modalità pagamento ed i bollettini generati, l'annullamento
		 * 				deve essere possibile solo se non risulta già pagato alcun bollettino!
		 * Numero MEV : 2023-33
		 * Autore : sgioggi 
		 * Data : 8 ago 2023 
		 * Branch : MEV_2023-33
		 */
		if (!"A".equals(em.getFlagDocumentoRegistrato()) && em.getCodMotivo().compareTo("0622") == 0) {
			// Ricerca lo stato dei pagamenti per id fascicolo
			IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
			Vector<BollettinoPagopaModel> elencoStatoPagamenti = ibp
					.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(em.getFasSieIdFascicoloSiep());
			Iterator<BollettinoPagopaModel> iterBPM = elencoStatoPagamenti.iterator();
			boolean esisteBollettinoPagato = false;
			while (iterBPM.hasNext()) {
				BollettinoPagopaModel bpm = iterBPM.next();
				if ("PA".equals(bpm.getStatoPagamento())) {
					esisteBollettinoPagato = true;
					break;
				}
			}
			if (esisteBollettinoPagato) {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Attenzione! Non è consentito annullare il provvedimento! "
						+ "Esistono dei bollettini già pagati!");
			}
		}
		// ***** FINE INTERVENTO MEV_2023-33 *****//

		// pagina di ritorno
		return PG_INSERICI_MOTIVAZIONI_EVENTO;
	}

	// Verifica lo stato del flag_elaborato per l'ordinanza di LA e per le relative licenze
	private boolean checkStatoElaborazioneLA(DepositoOrdinanzaPcModel ordinanzaLA, Vector licenzeLA)
			throws F3BException {

		if (ordinanzaLA.getFlagElaborato() != null && ordinanzaLA.getFlagElaborato().compareTo("S") == 0)
			return true;

		if (licenzeLA.size() > 0) {
			Iterator itx = licenzeLA.iterator();
			while (itx.hasNext()) {
				LicenzaLibAnticipataModel licLibAnt = (LicenzaLibAnticipataModel) itx.next();
				if (licLibAnt.getFlagElaborato() != null && licLibAnt.getFlagElaborato().compareTo("S") == 0)
					return true;
			}
		}

		return false;
	}

}