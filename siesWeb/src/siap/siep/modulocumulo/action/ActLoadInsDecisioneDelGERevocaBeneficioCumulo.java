package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il caricamento della form di Inserimento/Modifica di Decisione del G.E. su Richiesta
 * Revoca Benefici (gestione Cumulo)
 *
 * @author InterSistemi Itali S.p.A.
 *
 */
public class ActLoadInsDecisioneDelGERevocaBeneficioCumulo extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		// cerco la Richiesta
		BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicMod = lCtrlRich.ExRicercaRichiestePmInCumuloById(aIdRich);

		setRequestAttribute("RichiestaGE", lRicMod);

		if (lRicMod.getDecisioneGeSorvCum() != null
				&& lRicMod.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null)
			lModalita = "M";

		setRequestAttribute("modalita", lModalita);

		// Ufficio Emittente Decisione del GE
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
		lOption.setFilter(new String[] { "CAP", "CAS", "CASAP", "CSS", "GIP", "GIPM", "GUPM", "GUP", "TRIBSD",
				"CAPSM", "DIB", "DIBM", "-" });
		if (lRicMod != null && lRicMod.getDecisioneGeSorvCum() != null) {
			ProvvedimentoGeSorvCumModel lProvvMod = lRicMod.getDecisioneGeSorvCum();
			if (lProvvMod.getCodUfficioEmittente() != null) {
				UfficioModel lUffMod = this.getUfficioByCodUfficio(lProvvMod.getCodUfficioEmittente());
				lOption.setSelected(lUffMod.getCodTipoUfficio());
			}
		}

		setRequestAttribute("UfficioEmittente", "" + lOption);

		// Ricerca dei Titoli collegati alla Richiesta: Titolo a cui appartiene il Beneficio e Titolo di
		// Riferimento della Revoca
		ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();
		TitoloCumulatoModel lTitolo = lCtrlT.ExRicercaTitoloCumulatoById(lRicMod.getTitIdTitoloCumulato());
		TitoloCumulatoModel lTitoloRevocante = lCtrlT
				.ExRicercaTitoloCumulatoById(lRicMod.getTitIdTitoloCumulatoRef());

		// Ricerca dei Benefici (dati in Sentenza e dati con Provvedimento) Revocati
		TitoloCumulatoModel lTitoloBenefici = lCtrlRich.ExRicercaRichPMBeneficioCum(lTitolo,
				lRicMod.getIdRichiestePmInCumulo());

		setRequestAttribute("TitoloRichiesta", lTitoloBenefici);
		setRequestAttribute("TitoloRiferimento", lTitoloRevocante);

		// Ricerca di Articolo e Motivazione richiesta Revoca
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlternativo("REVOCA");
		Collection lColMotivo = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);

		Iterator itxOggetto = lColMotivo.iterator();
		while (itxOggetto.hasNext()) {
			DecodificheModel lDecMod = (DecodificheModel) itxOggetto.next();
			if (lDecMod.getCode().equals(lRicMod.getCodMotivo())) {
				setRequestAttribute("articolo", lDecMod.getFiltro());
				setRequestAttribute("motivazione", lDecMod.getDescription());
				break;
			}
		}

		return PG_INS_DECISIONE_GE_REVOCA_BEN;
	}

}