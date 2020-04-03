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
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadCancellaProvvedimento
 * </p>
 * <p>
 * Description: Classe Action per la load ActLoadCancellaProvvedimento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
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
			IDepositoOrdinanzaPc lCtrOrd = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			DepositoOrdinanzaPcModel lOrdMod = lCtrOrd
					.ExRicercaDepositoOrdinanzaPcByEvento(this.getRequestBigDecimalParameter("IdEvento"));
			ILicenzaPeriodiLibAnticipata lCtrlDep = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
			Vector lLicenze = new Vector();
			try {
				lLicenze = lCtrlDep.ExRicercaLicenzeByEve(this.getRequestBigDecimalParameter("IdEvento"));
			} catch (F3BException F3BEx) {
				siesLogger.debug("F3BException: " + F3BEx);
			}

			if (lOrdMod != null && lOrdMod.getCodTipoOrdinanza().compareTo("LA") == 0
					&& checkStatoElaborazioneLA(lOrdMod, lLicenze))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Non è consentito annullare il provvedimento! La liberazione anticipata è già stata elaborata dalla procura!");
		}

		// 12/12/2019 - Ticket 201911260116 - Non si consente la cancellazione del provvedimento in caso di
		// presenza di Comunicazioni Cumulo afferenti non validate
		EventoModel lEveRic = new EventoModel();

		lEveRic.setCodTipoEvento("01");
		lEveRic.setCodTipoProvvedimento("12");
		lEveRic.setCodMotivo("0670");
		
		
		/* 
		 * ISSUE MAC : recupero l'id fascicolo e lo setto nell'evento 
		 * Numero MAC : 20200331014
		 * Autore    : monica
		 * Data      : 02/apr/2020
		 * Branch    : mac-otrs-20200331014
		 */
		BigDecimal idFascicoloSiep = null;
		if (!this.isSessionAttributeNullObj("fascicolo")) {
			idFascicoloSiep = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
			lEveRic.setFasSieIdFascicoloSiep(idFascicoloSiep); 
		}		
		//***** FINE INTERVENTO mac-otrs-20200331014 *****//


		lEveRic.setEveIdEvento(this.getRequestBigDecimalParameter("IdEvento"));
	
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoModel lEve = new EventoModel();
		// EventoNotificaModel lEveNot = new EventoNotificaModel();

		lEve = lCtrlEve.ExRicercaEventoNonRegistrato(lEveRic);
		if (lEve != null) {
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Non è consentito annullare il provvedimento! Sono presenti Comunicazioni Cumulo non validate!");
		}

		IOrdineEsecuzione lCtrlOrd = SIEPLookupRemote.getOrdineEsecuzioneRemote();

		CampoNotaModel lCampoMod = lCtrlOrd
				.ExRicercaEventoCampoNotaByIdEvento(this.getRequestBigDecimalParameter("IdEvento"));
		this.setRequestAttribute("camponota", lCampoMod);

		IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrlEv.ExRicercaEventoByKey(this.getRequestBigDecimalParameter("IdEvento"));
		this.setRequestAttribute("evento", lEveMod);

		this.setRequestAttribute("IdEvento", this.getRequestStringParameter("IdEvento"));
		// 10/04/2006 il parametro nextAction consente di prestabilire l'azione da eseguire
		// alla fine dell'attività di revoca/cancellazione provvedimento.
		if (!isRequestParameterNullObj("nextAction"))
			setRequestAttribute("nextAction", getRequestStringParameter("nextAction"));

		if (!isRequestParameterNullObj("lOrdinamento"))
			setRequestAttribute("lOrdinamento", getRequestStringParameter("lOrdinamento"));

		// 08/09/2015 L’annullamento dell’evento di Comunicazione Richiesta impossibilità esazione Pena
		// Pecuniaria
		// deve essere consentito solo se al procedimento SIEP non è ancora collegato un procedimento SIUS di
		// Conversione Pene Pecuniarie.
		if (lEveMod.getCodMotivo().compareTo("0942") == 0) {
			IFascicoloSius lCtrlFasSius = SIUSLookupRemote.getFascicoloSiusRemote();
			Vector lFascicoliPerNumeroSIEP = new Vector();
			try {
				lFascicoliPerNumeroSIEP = lCtrlFasSius
						.ExRicercaFascicoliPerNumeroSIEP(lEveMod.getFasSieIdFascicoloSiep());
			} catch (F3BException F3BEx) {
				siesLogger.debug("F3BException: " + F3BEx);
			}

			if (lFascicoliPerNumeroSIEP != null && lFascicoliPerNumeroSIEP.size() > 0) {

				Iterator itx1 = lFascicoliPerNumeroSIEP.iterator();
				while (itx1.hasNext()) {
					FascicoloGPModel fascicoloSIUS = (FascicoloGPModel) itx1.next();
					if (fascicoloSIUS.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.compareTo("U070") == 0)
						throw new SIUSException(SIUSException.USER_MESSAGE,
								"Non è consentito annullare il provvedimento! La Comunicazione di Richiesta impossibilità esazione Pena Pecuniaria è collegata ad un fascicolo SIUS di Conversione Pene Pecuniarie!");
				}
			}
		}

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