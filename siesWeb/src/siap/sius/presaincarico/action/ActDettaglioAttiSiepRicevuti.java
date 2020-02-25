package siap.sius.presaincarico.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.sius.SIUSException;

/**
 * <p>
 * Title: ActDettaglioAttiSiepRicevuti
 * </p>
 * <p>
 * Description:
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
public class ActDettaglioAttiSiepRicevuti extends ActionSiap implements ICostantiPresaincarico {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("caricoistanza",
				getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO), getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"La presa in carico di questo Atto SIEP è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			// Prepara la "pagina" di destinazione
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.sius.presaincarico.action.ActLoadRicercaAttiSiep");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Questa classe presenta il dettaglio del MESSAGGIO selezionato.
		BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

		this.setRequestAttribute("Messaggio", lMess);

		ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

		if (lParser.getFascicolo() != null)
			this.setRequestAttribute("fascicolo", lParser.getFascicolo());
		else if (lParser.getDettaglioFascicoloSiep() != null) {
			this.setRequestAttribute("dettagliofascicolo", lParser.getDettaglioFascicoloSiep());
			this.setRequestAttribute("fascicolo", lParser.getDettaglioFascicoloSiep().getFascicoloSiep());
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Errore nella Ricezione del Procedimento SIEP. <BR>Rivolgersi all'amministratore di sistema!");

		if (lParser.getSoggetto() != null)
			this.setRequestAttribute("soggetto", lParser.getFascicolo().getSoggetto());
		else if (lParser.getDettaglioFascicoloSiep().getFascicoloSiep().getSoggetto() != null)
			this.setRequestAttribute("soggetto",
					lParser.getDettaglioFascicoloSiep().getFascicoloSiep().getSoggetto());
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Errore nella Ricezione del Soggetto. <BR>Rivolgersi all'amministratore di sistema!");

		if (lParser.getEvento() != null)
			this.setRequestAttribute("evento", lParser.getEvento());
		else if (lParser.getDettaglioFascicoloSiep().getEventi() != null)
			this.setRequestAttribute("eventi", lParser.getDettaglioFascicoloSiep().getEventi());
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Errore nella Ricezione dell'Evento. <BR>Rivolgersi all'amministratore di sistema!");

		/*
		 * 2010-04-30 - Sostituito per NuovaIstanza. if (lParser.getIstanza()!=null)
		 * this.setRequestAttribute("istanza", lParser.getIstanza());
		 */
		if (lParser.getNuovaIstanza() != null)
			this.setRequestAttribute("nuovaistanza", lParser.getNuovaIstanza());

		if (lParser.getDettaglioFascicoloSiep() != null
				&& lParser.getDettaglioFascicoloSiep().getFascicoloSiep() != null)
			ricercaPenaComplessivaSanzioneSostitutivaByIdFasSiep(lParser);

		// 16-03-2009 Ricerca Richiesta Conversione Pene Pecuniarie.
		if (lParser.getDettaglioFascicoloSiep() != null
				&& lParser.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
				&& lParser.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
						.getListRichiesteConversioniPP() != null)

			this.setRequestAttribute("richiesteConversioni", lParser.getDettaglioFascicoloSiep()
					.getDatiSiepPerTrasferimento().getListRichiesteConversioniPP());

		// ==========================================================================
		// List<MisuraSicurezzaModel> lElencoMisureSic = lParser.getDettaglioFascicoloSiep()
		// .getMisureSicurezza();

		// if (lElencoMisureSic!=null) {
		// for (int i=0; i<lElencoMisureSic.size();i++) {
		// MisuraSicurezzaModel lMisuraSicModel = lElencoMisureSic.get(i);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Misura: "+lMisuraSicModel.getIdMisuraSicurezza()
		// +" - "+lMisuraSicModel.getCodNatura()+" "+lMisuraSicModel.getDescrNatura()
		// +" - "+lMisuraSicModel.getCodTipo()+" "+lMisuraSicModel.getDescrTipo()
		// +" - "+lMisuraSicModel.getNumAnni()+" anni,"
		// +lMisuraSicModel.getNumMesi()+" mesi"
		// +lMisuraSicModel.getNumGiorni()+" giorni"
		// );
		// }
		// }

		// Eventuali misure di sicurezza
		if (lParser.getDettaglioFascicoloSiep().getMisureSicurezza() != null)
			this.setRequestAttribute("misureSicurezza",
					lParser.getDettaglioFascicoloSiep().getMisureSicurezza());

		return PG_DETTAGLIO_ATTOSIEP_RICEVUTO;
	}

	// Viene ricavata la Sanzione Residua o la Sanzione Sostitutiva e passata alla request
	private void ricercaPenaComplessivaSanzioneSostitutivaByIdFasSiep(ParserMessage aParser)
			throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaPenaComplessivaSanzioneSostitutivaByIdFasSiep: inizio");

		// BigDecimal aIdFascicoloSIEP =
		// aParser.getDettaglioFascicoloSiep().getFascicoloSiep().getIdFascicoloSiep();

		// Si cerca la SANZIONE RESIDUA
		SanzioneSostResiduaModel lSSResiduaModel = null;
		if (aParser.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
				&& aParser.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
						.getListSanzioneSostResidua() != null
				&& (aParser.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
						.getListSanzioneSostResidua().size() > 0))
			lSSResiduaModel = (SanzioneSostResiduaModel) aParser.getDettaglioFascicoloSiep()
					.getDatiSiepPerTrasferimento().getListSanzioneSostResidua().get(0);

		if (lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null) {
			// Se non è stata trovata la Sanzione Residua si ricerca la Sanzione Sostitutiva
			// Pena Complessiva e (al massimo 1 e al massimo 1)
			PenaComplessivaSanzioneSostitutivaModel lPenCompSanzSost = aParser.getDettaglioFascicoloSiep()
					.getPenaComplessivaSanzioneSostitutiva();
			// Se è stata trovata la Sanzione Sostitutiva si passa nella request
			if (lPenCompSanzSost != null && lPenCompSanzSost.getSanzioneSostitutiva() != null)
				setRequestAttribute("sanzione_sostitutiva", lPenCompSanzSost.getSanzioneSostitutiva());
		} else {
			// Se è stata trovata la Sanzione Residua si passa nella request
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lSSResiduaModel = " + lSSResiduaModel);
			setRequestAttribute("sanzione_residua", lSSResiduaModel);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaPenaComplessivaSanzioneSostitutivaByIdFasSiep: fine");
	}

}