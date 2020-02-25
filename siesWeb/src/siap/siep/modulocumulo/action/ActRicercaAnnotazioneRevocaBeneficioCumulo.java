package siap.siep.modulocumulo.action;

import java.util.Iterator;

/**
* <p>Title: ActRicercaAnnotazioneRevocaBeneficioCumulo</p>
* <p>Description: Classe Action per la ricerca di Annotazioni Decisioni G.E. Amnistia/Indulto per un
*                 certo Titolo</p>
* @version 1.0
*/

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IBeneficioCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaAnnotazioneRevocaBeneficioCumulo extends ActionModuloCumulo
		implements ICostantiComputiCumulo {

	/*****************************************************************************
	 * Azione di Ricerca Delle annotazioni di Revoca Sospensione Condizionale della Pena associati a un certo
	 * titolo
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

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

		// =====================================================================================
		// Ricerca dei provvedimenti G.E. di Sospensione/Non Menzione per titolo/istruttoria
		// =====================================================================================

		Vector<String> listaTipoProvv = new Vector<String>();
		listaTipoProvv.add("02"); // decreto
		listaTipoProvv.add("03"); // ordinanza

		Vector<String> listaCodMotivo = new Vector<String>();
		listaCodMotivo.add("0818"); // Revoca sospensione condizionale della pena
		listaCodMotivo.add("0822"); // Revoca non menzione

		Vector<StatoEsecTitoloCumulatoModel> lVect = new Vector<StatoEsecTitoloCumulatoModel>();
		IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

		// La ricerca dei provvedimenti per idTitolo e TipoProvvedimento richiede, nell'ordine, i parametri:
		// idTitolo, CodTipoEvento, CodTipoProvvedimento, CodMotivo.
		lVect = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(
				lTitolo.getIdTitoloCumulato(), "01", listaTipoProvv, listaCodMotivo);
		siesLogger.debug("lVect.size() = " + lVect.size());

		Vector<StatoEsecTitoloCumulatoModel> lVectRevoche = new Vector<StatoEsecTitoloCumulatoModel>();
		// ======================================================================
		// per non creare una nuova ricerca, filtro qui sotto SOLO LE REVOCHE
		if (lVect != null && lVect.size() > 0) {
			Iterator itx = lVect.iterator();
			while (itx.hasNext()) {
				StatoEsecTitoloCumulatoModel lmod = (StatoEsecTitoloCumulatoModel) itx.next();
				if (lmod.getCodEsitoTenore() != null && "0006".equals(lmod.getCodEsitoTenore())) {
					lVectRevoche.add(lmod);
				}
			}

		}
		// ========================================================================

		// Passo alla form la lista delle sole REVOCHE
		setRequestAttribute("ListaRevocheSospCond", lVectRevoche);

		// ==================================================================================
		// Controllo su : ESISTENZA di eventuali BENEFICI da Revocare per il Titolo in esame
		String lTipoSospensione = "";
		Vector<String> lTipoBen = new Vector<String>();
		lTipoBen.add("01"); // Sospensione Condizional
		lTipoBen.add("02"); // Non Menzione
		IBeneficioCumulo lctrlB = SIEPLookupRemote.getBeneficioCumuloRemote();

		lTipoSospensione = lctrlB.ExRicercaBeneficioCumuloByTipoNaturaTitoloCum(lTitolo.getIdTitoloCumulato(),
				"C", lTipoBen);

		setRequestAttribute("lTipopresenzaBenefici", lTipoSospensione);

		return PG_ELENCO_REVOCA_BENEFICIO;
	}

}