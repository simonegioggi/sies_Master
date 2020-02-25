package siap.siep.modulocumulo.action;

/**
* <p>Title: ActRicercaSospMisureAlternativeCumulo</p>
* <p>Description: Classe Action per la ricerca di Attività della Sorveglianza - Revoca Misure Alternative per un
*                 certo Titolo</p>
* @version 1.0
*/

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaSospMisureAlternativeCumulo extends ActionModuloCumulo
		implements ICostantiComputiCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************************
	 * Azione di Ricerca Delle Attività della Sorveglianza - Revoca Misure Alternative.
	 * 
	 * Recupera i provvedimenti di Revoca Misure Alternative associati a un certo titolo.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		TitoloCumulatoModel lTitolo = super.getDatiTitoloCumulato();

		// ========================================================================================
		// Ricerca dei provvedimenti SORV. di Sospensione Misure Alternative per titolo/istruttoria
		// ========================================================================================

		Vector<StatoEsecTitoloCumulatoModel> lVect = new Vector<>();
		IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

		Vector<String> listaTipoProvv = new Vector<>();

		listaTipoProvv.add("02"); // decreto
		listaTipoProvv.add("03"); // ordinanza

		Vector<String> listaProvv = new Vector<>();

		// ======
		DecodificheModel lModel = new DecodificheModel();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlt4("SOSPCUM");

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		Collection lListaDecodifiche = lDecodifiche.ExRicercaDecodifiche(lModel);
		Iterator lIter = lListaDecodifiche.iterator();
		while (lIter.hasNext()) {
			DecodificheModel lDecode = (DecodificheModel) lIter.next();
			if (!"-".equals(lDecode.getCode()))
				listaProvv.add(lDecode.getCode());
		}
		// =========

		/*
		 * listaProvv.add("2146"); // Sospensione Provvisoria Affidamento al Servizio Sociale DATA INGRESSO IN
		 * CARCERE listaProvv.add("2147"); // Sospensione Provvisoria Affidamento art. 47 quater o.p. “
		 * listaProvv.add("2145"); // Sospensione Provvisoria Affidamento Servizio Sociale ex art. 94 DPR
		 * 309/90 “ listaProvv.add("2151"); // Sospensione Provvisoria Detenzione Domiciliare art. 47 quater
		 * o.p. listaProvv.add("2153"); // Sospensione Provvisoria Detenzione Domiciliare (Art.47 ter comma 1
		 * quater) listaProvv.add("2149"); // Sospensione Provvisoria Detenzione Domiciliare (Art. 47 Ter
		 * O.P.) listaProvv.add("2150"); // Sospensione Provvisoria Detenzione Domiciliare (Art. 47 Ter 1 Bis
		 * O.P.) listaProvv.add("2152"); // Sospensione Provvisoria Detenzione Domiciliare Speciale
		 * listaProvv.add("2293"); // Sospensione Provvisoria Differimento nelle forme della Detenzione
		 * Domiciliare listaProvv.add("2291"); // Sospensione Provvisoria Arresti Domiciliari
		 * listaProvv.add("2148"); // Sospensione Provvisoria Semilibertà listaProvv.add("2280"); //
		 * Inosservanza Obblighi/Prescrizioni Sospensione Condizionata della Pena listaProvv.add("2297"); //
		 * Sospensione provvisoria dell'esecuzione presso domicilio della pena detentiva
		 * 
		 * listaProvv.add("2741"); // listaProvv.add("2742"); // listaProvv.add("2743"); //
		 * listaProvv.add("2756"); //
		 */

		// La ricerca dei provvedimenti per idTitolo e TipoProvvedimento richiede, nell'ordine, i parametri:
		// idTitolo, CodTipoEvento, CodTipoProvvedimento, CodMotivo.
		lVect = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(
				lTitolo.getIdTitoloCumulato(), "01", listaTipoProvv, listaProvv);
		siesLogger.debug("lVect.size() = " + lVect.size());

		// Passo alla form i dati trovati
		setRequestAttribute("ListaSospMisureAlt", lVect);

		return PG_ELENCO_SOSP_MISUREALTERNATIVE_CUMULO;
	}

}