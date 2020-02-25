package siap.sius.impugnazione.action;

/**
* <p>Title: ActRicercaFSPImpugnazione</p>
* <p>Description: Classe Action per la ricerca puntuale del Fascicolo SIUS finalizzata alla trasmissione atti</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.util.SIUSLookupRemote;

public class ActRicercaFSPImpugnazione extends ActRicercaFSPuntuale implements ICostantiImpugnazione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		super.processRequest();
		setLinkRitorno();

		// Recupero del FascicoloSiusGP. Se non in sessione solleva un errore di eccezione.
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Procedimento non selezionato");

		FascicoloGPModel fascicoloSiusGP = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Variabile ScadenzarioSiusModel.
		// ScadenzarioSiusModel lScaMod = new ScadenzarioSiusModel();

		// Verifica sullo Scadenzario se il procedimento è impugnabile.
		IScadenzarioSius lCtrl = SIUSLookupRemote.getScadenzarioRemote();
		/* lScaMod = */lCtrl.ExRicercaScadenzarioSiusByIdFascicoloTipo(
				fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius(),
				ICostantiImpugnazione.COD_TIPO_SCADENZARIO);

		// Verifica esistenza di un provvedimento impugnato per quel procedimento.
		// STUB 12/09/2003 Al momento il test è realizzato controllando il Campo FLAG_PIU_MENO, ma in futuro
		// sarà gestito COD_STATO_EVENTO
		// Se il procedimento non è impugnato si controlla lo scadenzario.
		IImpugnazione lCtrlImp = SIUSLookupRemote.getImpugnazioneRemote();
		// String flagImpugnato="N";
		/*
		 * 12/10/2007 Sostituita la Verifica con il Conteggio di Eventuali inpugnazioni preesistenti per il
		 * Fascicolo.
		 * 
		 * if (!lCtrlImp.ExVerificaImpugnazione(super.mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(),
		 * COD_EVENTO_PROVVEDIMENTO )) { // STUB 24/05/2004 A seguito di richieste dell'UTENTE, deve essere
		 * possibile impugnare un provvedimento anche se non sono state inserite le date di notifica. con
		 * conseguente attivazione dello scadenzario. // Commentati, pertanto i controlli bloccanti sollo
		 * scadenzario per l'Impugnazione. /* if(Utils.isNullObj(lScaMod)) throw new F3BException(
		 * F3BException.USER_MESSAGE, "Procedimento non Impugnabile: Date Notifica mancanti." );
		 * 
		 * if(Utils.isNullObj(lScaMod.getDataFineScadenza())) throw new F3BException(
		 * F3BException.USER_MESSAGE, "Procedimento non Impugnabile: data ricorso mancante. " ); else if
		 * (lScaMod.getDataFineScadenza().before( DateUtils.getSysDate()) ) throw new F3BException(
		 * F3BException.USER_MESSAGE, "Procedimento non Impugnabile: data ricorso scaduta. " ); / } else
		 * flagImpugnato = "S";
		 */

		// ==========================================================================
		// Recupera le Impugnazioni dell'ultimo evento (validato o meno) che presenta
		// un ricorso (flag_piu_meno = 'R')
		// n.b. solo dell'evento più recente che presenta un ricorso
		// ==========================================================================
		Option lOptTipoRicorso = new Option(DecodificheManager.getInstance().getTipoRicorso());
		String[] lFilterRicorso = { "01", "02", "03" };
		lOptTipoRicorso.setFilter(lFilterRicorso);

		Vector lImpugnazioni = lCtrlImp.ExRicercaImpugnazioni(
				super.mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(), COD_EVENTO_PROVVEDIMENTO,
				lOptTipoRicorso.getCodes(), null);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lImpugnazioni.size() = " + lImpugnazioni.size());

		// Recupero dei provvedimenti per il fascicolo.
		IEvento mCtrl = SICOLookupRemote.getEventoRemote();

		Vector lVect = mCtrl.ExRicercaEventoByFascicoloSius(
				super.mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(), COD_EVENTO_PROVVEDIMENTO);

		// Si eliminano i provvedimenti annullati
		/*
		 * 16/11/2007 Non si eliminano più i provvedimenti annullati Vector lVectValidi = new Vector();
		 * Iterator itx = lVect.iterator(); while ( itx.hasNext()) { EventoModel lProv =
		 * (EventoModel)itx.next(); if ( (lProv.getFlagDocumentoRegistrato() == null ||
		 * lProv.getFlagDocumentoRegistrato().equalsIgnoreCase("S")) && (!Utils.isNullObj(
		 * lProv.getDataTrasmissioneAtti() )) ) { lVectValidi.add(lProv); } }
		 */

		BigDecimal countImpugnazioni = new BigDecimal(lImpugnazioni.size());
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		setRequestAttribute("tipoUfficio", strCodTipoUfficio);

		setRequestAttribute("provvedimenti", lVect); // 16/11/2007
		setRequestAttribute("impugnazioni", lImpugnazioni); // 29/10/2007
		// setRequestAttribute("flag_Impugnato", flagImpugnato);
		setRequestAttribute("numero_Impugnazioni", countImpugnazioni.toString());
		setRequestAttribute("flag_valida", "NO");

		return PG_ELENCOPROVVEDIMENTI;
	}

}