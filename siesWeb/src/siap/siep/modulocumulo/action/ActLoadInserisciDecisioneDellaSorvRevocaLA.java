package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichPMTitoloCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il Load della Form di Inserimento della decisione della SORV. a fronte di una Richiesta
 * del P.M. d Revoca Lib.Ant.
 * 
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActLoadInserisciDecisioneDellaSorvRevocaLA extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		siesLogger.debug("--XX-- Inizio - Modalita = " + lModalita);
		setRequestAttribute("modalita", lModalita);

		// cerco la Richiesta
		BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicMod = lCtrlRich.ExRicercaRichiestePmInCumuloById(aIdRich);

		setRequestAttribute("RichiestaSORV", lRicMod);

		// Ricerca dei Titoli collegati alla Richiesta (tramite tabella di Relazione RICHPM_TITOLO_CUM)
		Vector<RichPMTitoloCumModel> VecRicPM = new Vector<>();
		VecRicPM = lCtrlRich.ExRicercaRichPMTitoliCum(aIdRich);

		// Ricerca completa dei dati Aggregati ai Titoli coinvolti nella Richiesta (Lib. Ant.)
		Vector<TitoloCumulatoModel> VecTitoli = new Vector<>();
		VecTitoli = lCtrlRich.ExRicercaTitoliDiLibAntPerRichiesta(VecRicPM);

		siesLogger.debug("--XX-- i Titoli collegati alla Richiesta sono " + VecTitoli.size());
		setRequestAttribute("TitoliRichiesta", VecTitoli);
		setRequestAttribute("ProvvSORVCum", lRicMod.getDecisioneGeSorvCum());

		// Combo Ufficio Emittente
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
		lOption.setFilter(new String[] { "TDS", "TDSM", "UDS", "UDSM", "-" });
		if (lRicMod != null && lRicMod.getIdRichiestePmInCumulo() != null) {
			if (lRicMod.getDecisioneGeSorvCum() != null
					&& lRicMod.getDecisioneGeSorvCum().getCodUfficioEmittente() != null) {
				UfficioModel lUffMod = this
						.getUfficioByCodUfficio(lRicMod.getDecisioneGeSorvCum().getCodUfficioEmittente());
				lOption.setSelected(lUffMod.getCodTipoUfficio());
			}
		}
		setRequestAttribute("UfficioEmittente", "" + lOption);

		// Tipo Provvedimento
		Option lOptionTipoProvv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza());
		if (lRicMod.getDecisioneGeSorvCum() != null
				&& lRicMod.getDecisioneGeSorvCum().getCodTipoProvvedimento() != null)
			lOptionTipoProvv.setSelected(lRicMod.getDecisioneGeSorvCum().getCodTipoProvvedimento());

		setRequestAttribute("TipoProvvedimento", "" + lOptionTipoProvv);

		return PG_INSERIMENTO_DECISIONE_SORV;
	}

}