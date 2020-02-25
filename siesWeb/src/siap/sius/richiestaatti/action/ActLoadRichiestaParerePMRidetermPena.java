package siap.sius.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.xml.TreeModel;
import f3b.web.html.Option;
import siap.jms.util.ParserMessageRec;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.stampa.action.ICostantiStampaSius;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadRichiestaParerePMRidetermPena extends ActionSiap
		implements ICostantiRichiestaAtti, ICostantiFascicoloSius {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		BigDecimal aIdFascicoloSius = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);
		// Passo 3
		ParserMessageRec lParser = null;
		FascicoloSiepModel lFasSiepModel = null;
		SentenzaModel lSentenzaModel = null;
		UfficioModel lUfficioSiep = null;

		if (((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel()
				.getFasSieIdFascicoloSiep() != null) {
			// Passo 3a
			IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
			// Riempi l'Array contenente le tipologie di dati da prelevare
			int[] aTipoDati = { ICostantiStampaSius.TREE_FASCICOLOSIEP, ICostantiStampaSius.TREE_SENTENZA };
			// Crea il TreeModel con i dati che occorrono
			TreeModel lTreeDati = lCtrlSta.ExPrelevaDatiVideo(aIdFascicoloSius, aTipoDati);
			// Converte i dati ottenuti per utilizzarli come model
			lParser = new ParserMessageRec(lTreeDati);

			lFasSiepModel = lParser.getFascicolo();
			this.setRequestAttribute("fascicolo", lFasSiepModel);

			lSentenzaModel = lParser.getSentenza();
			this.setRequestAttribute("sentenza", lSentenzaModel);

			// Trova i dati dell'ufficio SIEP
			IUfficio lUff = null;
			lUff = SICOLookupRemote.getUfficioRemote();
			lUfficioSiep = lUff.getUfficioByKey(lFasSiepModel.getChiaveUfficio());
			this.setRequestAttribute("ufficioSiep", lUfficioSiep);
		}

		// Passo 4 - LISTA UFFICI
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoUfficio();

		if (lFasSiepModel != null && lFasSiepModel.getDescrTipoUfficio() != null) {
			String[] lStringFilter = { lUfficioSiep.getCodTipoUfficio() };
			Option lOption = new Option(lTipoIstituto);
			lOption.setFilter(lStringFilter);
			setRequestAttribute("TipiIstituti1", "" + lOption);
		} else {
			String[] lStringFilter = { "PM", "PGCAP" };
			Option lOption = new Option(lTipoIstituto);
			lOption.setFilter(lStringFilter);
			setRequestAttribute("TipiIstituti1", "" + lOption);
		}

		return PG_LOAD_RICHIESTARIDETERMPENA; // restituisce la jsp di VIEW
	}

}