package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.RichPMMisSicCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il Load della Form di Inserimento della decisione della SORV. a fronte di una Richiesta
 * del P.M. di Unificazione Mis.Sic.
 * 
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActLoadInserisciDecisioneDellaSorvUnificaMS extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		// cerco la Richiesta
		BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicMod = lCtrlRich.ExRicercaRichiestePmInCumuloById(aIdRich);

		setRequestAttribute("RichiestaSORV", lRicMod);

		// Lettura delle relazioni RICHPM_MISSICUR_CUM)
		Vector<RichPMMisSicCumModel> VecRicPM = new Vector<>();
		VecRicPM = lCtrlRich.ExRicercaRichPMMisSicCum(aIdRich);
		siesLogger.debug(">>>>>>>>>> aIdRich = " + aIdRich);

		// Lettura delle Misure di Sicurezza coinvolte.
		Vector<MisuraSicurezzaCumuloModel> VecMisSic = new Vector<>();
		IMisuraSicurezzaCumulo lCtrMSC = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
		for (int i = 0; i < VecRicPM.size(); i++) {
			RichPMMisSicCumModel lRicMSMod = VecRicPM.elementAt(i);
			MisuraSicurezzaCumuloModel lMisSicMod = lCtrMSC
					.ExRicercaMisuraSicurezzaCumuloById(lRicMSMod.getMisIdMisSicCumulo());
			VecMisSic.add(lMisSicMod);
		}

		// Lettura dei Titoli collegati alle Misure di Sicurezza.
		ITitoloCumulato lCtrlTitCum = SIEPLookupRemote.getTitoloCumulatoRemote();
		ArrayList<BigDecimal> lIdTitoli = new ArrayList<>();
		Vector<TitoloCumulatoModel> VecTitoli = new Vector<>();
		for (int j = 0; j < VecMisSic.size(); j++) {
			MisuraSicurezzaCumuloModel lMisSicMod = VecMisSic.elementAt(j);
			if (lIdTitoli.contains(lMisSicMod.getTitIdTitoloCumulato())) {
			} else {
				TitoloCumulatoModel lTitCumMod = lCtrlTitCum
						.ExRicercaTitoloCumulatoById(lMisSicMod.getTitIdTitoloCumulato());
				VecTitoli.add(lTitCumMod);
				lIdTitoli.add(lTitCumMod.getIdTitoloCumulato());
			}
		}
		siesLogger.debug("--XX-- i Titoli collegati alla Richiesta sono " + VecTitoli.size());

		// setRequestAttribute("MisureSicurezza", VecMisSic);
		setRequestAttribute("TitoliRichiesta", VecTitoli);

		// ----------------
		// Ricerca dei Titoli collegati alla Richiesta (tramite tabella di Relazione RICHPM_TITOLO_CUM)
		/// Vector<RichPMTitoloCumModel> VecRicPM = new Vector<RichPMTitoloCumModel>();
		/// VecRicPM = lCtrlRich.ExRicercaRichPMTitoliCum(aIdRich);

		// Ricerca completa dei dati Aggregati ai Titoli coinvolti nella Richiesta (Lib. Ant.)
		/// Vector<TitoloCumulatoModel> VecTitoli = new Vector<TitoloCumulatoModel>();
		/// VecTitoli = lCtrlRich.ExRicercaTitoliDiLibAntPerRichiesta(VecRicPM);

		siesLogger.debug("--XX-- i Titoli collegati alla Richiesta sono " + VecTitoli.size());
		setRequestAttribute("TitoliRichiesta", VecTitoli);
		setRequestAttribute("ProvvSORVCum", lRicMod.getDecisioneGeSorvCum());

		String lModalita = "I"; // default inserimento, ma se presente ProvvSORVCum si va in modifica
		if (lRicMod.getDecisioneGeSorvCum() != null
				&& lRicMod.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null)
			lModalita = "M";

		siesLogger.debug("--XX-- Inizio - Modalita = " + lModalita);
		setRequestAttribute("modalita", lModalita);

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

		// Tipo Misure di Sicurezza
		Option lOptionTipoMS = new Option(DecodificheManager.getInstance().getTipoMisuraSicurezza());
		if (lRicMod.getDecisioneGeSorvCum() != null && lRicMod.getDecisioneGeSorvCum().getCodTipoMsD() != null
				&& lRicMod.getDecisioneGeSorvCum().getCodTipoProvvedimento() != null)
			lOptionTipoMS.setSelected(lRicMod.getDecisioneGeSorvCum().getCodTipoMsD());

		setRequestAttribute("TipoMisuraSicurezza", "" + lOptionTipoMS);

		return PG_INSERIMENTO_DECISIONE_SORV;
	}
}
