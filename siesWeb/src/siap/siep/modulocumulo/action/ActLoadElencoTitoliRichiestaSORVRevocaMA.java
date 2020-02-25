package siap.siep.modulocumulo.action;

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load Elenco dei Titoli possibili oggetto della Richieste alla SORV di Revoca Misura
 * Alternativa alla detenzione
 * 
 * @author Intersistemi Italia S.p.A.
 */

public class ActLoadElencoTitoliRichiestaSORVRevocaMA extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
		if (!ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstruttoriaModel.getFlagStato())) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"L'istruttoria risulta chiusa. Non è possibile procedere all'emissione di ulteriori richieste");
			return IWebConstants.PG_MESSAGE;
		}

		Vector<TitoloCumulatoModel> lListaTitoli = null;

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		siesLogger.debug("--XX-- Inizio - Modalita = " + lModalita);

		String lOrdinamento = lIstruttoriaModel.getOrdinamentoTitoli();
		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();

		// Cerco in CG_REF_CODES le M.A. che ci interessano
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlt4("CONCUM");
		Vector lVec = new Vector(lDecodifiche.ExRicercaDecodifiche(lModel));

		// Salvo qui i Codici delle M.A.
		Vector<String> lCodMA = new Vector<>();

		DecodificheModel lModelVec = null;
		Iterator Ite1 = lVec.iterator();
		while (Ite1.hasNext()) {
			lModelVec = (DecodificheModel) Ite1.next();
			lCodMA.add(lModelVec.getCode());
		}

		// La Query trova Titolo_Cumulato_Model in join con Stato_Esec_Titolo_Cumulato
		lListaTitoli = new Vector<>(
				lIstrCtrl.ExRicercaTitoliStatoEsecTitoloCumByIstruttoriaOrderBy(
						lIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento, lCodMA, null));

		setRequestAttribute("ListaTitoli", lListaTitoli);
		setRequestAttribute("modalita", lModalita);

		return PG_ELENCO_TITOLI_RICH_SORV_REVOCA_MA;
	}

} // Chiude Classe