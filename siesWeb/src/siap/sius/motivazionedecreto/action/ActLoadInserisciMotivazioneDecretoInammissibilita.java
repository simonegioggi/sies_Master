package siap.sius.motivazionedecreto.action;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.motivazionedecreto.controller.IMotivazioneDecreto;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciMotivazioneDecretoInammissibilita
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di MotivazioneDecreto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciMotivazioneDecretoInammissibilita extends ActionSius
		implements ICostantiMotivazioneDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		setRequestAttribute("modalita", "I");

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO))
			setRequestAttribute("idEvento", getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		if (!isRequestParameterNullObj("idDepositoDecreto")) {
			setRequestAttribute("idDepositoDecreto", getRequestStringParameter("idDepositoDecreto"));
			listaMotivi();
			listaMotiviPresenti();
		}

		return PG_LOAD_INSERISCI_MOTIVAZIONE_DECRETO_INAMMISSIBILITA; // restituisce la jsp di VIEW
	}

	protected void listaMotivi() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotivi: inizio");
		String lTipoUff = getUfficioUtenteConnesso().getCodTipoUfficio().toUpperCase();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("tipo ufficio ->" + lTipoUff);

		// Chiamata Controller per la ricerca delle Decodifiche.
		IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();
		Vector lVect = new Vector(lDecCtrl.ExListaMotiviInammissibilita(lTipoUff));

		// Se si tratta di un procedimento di Remissione Debito, si aggiungono i
		// nuovi motivi di inammissibilità
		FascicoloGPModel lFasGPModMotivi = new FascicoloGPModel();
		lFasGPModMotivi = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		if (lFasGPModMotivi.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
				.compareTo("U011") == 0) {
			Vector lVectRd = new Vector(lDecCtrl.ExListaMotiviInammissibilitaRD(lTipoUff));
			lVect.addAll(lVectRd);
		}
		// Fine aggiunta motivi di inammissibilità per Remissione Debito

		setRequestAttribute("motivi", lVect);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotivi: fine");
	}

	// Ricerca delle Motivazioni Inammissibilità già inserite
	// da Richiesta Parere
	protected void listaMotiviPresenti() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotiviPresenti: inizio");
		// Lettura del Fascicolo in sessione
		FascicoloGPModel lFascicoloSius = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		if (lFascicoloSius == null || lFascicoloSius.getFascicoloSiusModel() == null
				|| lFascicoloSius.getFascicoloSiusModel().getIdFascicoloSius() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Dati del fascicolo SIUS assenti !");

		// Ricerca di eventuali richieste parere inammissibilità.
		IMotivazioneDecreto lEveCtrl = SIUSLookupRemote.getMotivazioneDecretoRemote();
		Vector listaMotivi = lEveCtrl.ExRicercaUltimeMotivazioniDecretoByIdFasSius(
				lFascicoloSius.getFascicoloSiusModel().getIdFascicoloSius());

		// Hash Table che conterrà Codice Tipo Motivo, Decrizione Motivazione ed Altra Motivazione
		Hashtable lTabellaMotiv = new Hashtable();

		Iterator lInd = listaMotivi.iterator();
		while (lInd.hasNext()) {
			MotivazioneDecretoModel lMotiv = (MotivazioneDecretoModel) lInd.next();
			String[] lCampi = new String[2];
			String lKey = lMotiv.getCodTipoMotivazione();
			lCampi[0] = lMotiv.getDescrMotivazione() != null ? lMotiv.getDescrMotivazione() : "";
			lCampi[1] = lMotiv.getAltraMotivazione() != null ? lMotiv.getAltraMotivazione() : "";
			lTabellaMotiv.put(lKey, lCampi);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Elemento in hasTable:");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("*** chiave ->" + lKey);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("*** Campo1 ->" + lCampi[0]);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("*** Campo2 ->" + lCampi[1]);
		}
		setRequestAttribute("motiviPresenti", lTabellaMotiv);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Numero di elementi della Tabella Motivi Presenti: " + lTabellaMotiv.size());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotiviPresenti: fine");
	}

}