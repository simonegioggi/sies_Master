package siap.siep.modulocumulo.action;

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

/**
 * <p>
 * Title: ActRicercaConcMisureAlternativeCumulo
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Attività della Sorveglianza - Concessione Misure Alternative
 * per un certo Titolo
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaConcMisureAlternativeCumulo extends ActionModuloCumulo
		implements ICostantiComputiCumulo {

	/*****************************************************************************************
	 * Azione di Ricerca Delle Attività della Sorveglianza - Concessione Misure Alternative.
	 * 
	 * Recupera i provvedimenti di Concessione Misure Alternative associati a un certo titolo.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************************/
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

		// ========================================================================================
		// Ricerca dei provvedimenti SORV. di Concessione Misure Alternative per titolo/istruttoria
		// ========================================================================================

		Vector<StatoEsecTitoloCumulatoModel> lVect = new Vector<StatoEsecTitoloCumulatoModel>();
		IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

		Vector<String> listaTipoProvv = new Vector<String>();

		listaTipoProvv.add("02"); // decreto
		listaTipoProvv.add("03"); // ordinanza

		Vector<String> listaProvv = new Vector<String>();

		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlt4("CONCUM");
		Collection lCodMotivi = lDecodifiche.ExRicercaDecodifiche(lModel);
		Iterator itx = lCodMotivi.iterator();

		while (itx.hasNext()) {
			DecodificheModel lDecodeModel = (DecodificheModel) itx.next();
			listaProvv.add(lDecodeModel.getCode());
		}

		// La ricerca dei provvedimenti per idTitolo e TipoProvvedimento richiede, nell'ordine, i parametri:
		// idTitolo, CodTipoEvento, CodTipoProvvedimento, CodMotivo.
		lVect = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(
				lTitolo.getIdTitoloCumulato(), "01", listaTipoProvv, listaProvv);
		siesLogger.debug("lVect.size() = " + lVect.size());

		// Passo alla form i dati trovati
		setRequestAttribute("ListaConcMisureAlt", lVect);

		return PG_ELENCO_CONC_MISUREALTERNATIVE_CUMULO;
	}

}