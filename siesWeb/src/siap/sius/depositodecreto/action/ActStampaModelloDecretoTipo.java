package siap.sius.depositodecreto.action;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.model.NotificaModel;
import siap.sius.SIUSException;
//import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

public class ActStampaModelloDecretoTipo extends ActionSiap implements ICostantiDepositoDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * <p>
	 * Title: ActStampaModDecretoTipo
	 * </p>
	 * <p>
	 * Description: Classe Azione responsabile della Stampa Modello Decreto Tipo
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2007
	 * </p>
	 * <p>
	 * Company:
	 * </p>
	 * 
	 * @author not attributable
	 * @version 1.0
	 */
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		EventoNotificaModel lEveMod = new EventoNotificaModel();
		// Imposta l'id del fascislo siep nell'evento.
		lEveMod.getEvento()
				.setFasSieIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

		UfficioModel lUff = new UfficioModel(this.getUfficioUtenteConnesso());
		// Imposta i parametrio ufficio nell'evento.
		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		// Costruisce l'array NotificaModel per evitare errori in fase di PrelevaDati
		lEveMod.setNotifiche(new NotificaModel[0]);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ufficio = " + lUff);

		// Setta il file RTF relativo al documento selezionato
		// Imposta il documento selezionato in funzione del tipo di Ufficio ( TDS / UDS ).
		// Ticket#20190805019 [SG]: aggiunti anche uffici minorili
		if ("TDS".equalsIgnoreCase(lUff.getCodTipoUfficio())
				|| "TDSM".equalsIgnoreCase(lUff.getCodTipoUfficio()))
			lEveMod.setNomeTemplate(TEMPLATE_MOD_DECRETO_TIPO);
		else if ("UDS".equalsIgnoreCase(lUff.getCodTipoUfficio())
				|| "UDSM".equalsIgnoreCase(lUff.getCodTipoUfficio()))
			lEveMod.setNomeTemplate(TEMPLATE_MOD_DECRETO_TIPO_UDS);
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Documento non disponibile per tipi uffici diversi da TDS, TDSM ed UDS, UDSM");

		// Esegue stampa del modello decreto tipo.

		// 2007-11-09
		// N.B.: Come si può notare si richiama lo stesso metodo utilizzato per il modello ordinanze
		// poichè il recupero dei dati è lo stesso.
		// Pertanto, in caso di ulteriori modifiche, si provvederà a generalizzare tale
		// metodo ( Ordinanza e Decreto ).
		IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumentoModello(lFasGPMod, lEveMod,
				super.getUtenteConnesso()); // setta la risposta nella request

		/*
		 * IDepositoDecreto lCtrl = SIUSLookupRemote.getDepositoDecretoRemote(); ByteArrayOutputStream lReport
		 * = lCtrl.ExStampEmissioneDecreto( lEveMod.getEvento(), lUff, super.getUtenteConnesso() );
		 */

		// Prepara la pagina di destinazione
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return IWebConstants.PG_DOWNLOAD;
	}

}