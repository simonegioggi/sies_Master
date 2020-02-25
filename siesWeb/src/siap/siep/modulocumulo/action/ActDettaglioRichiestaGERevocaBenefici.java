package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import f3b.util.F3BException;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il caricamento del dettaglio della richiesta di Revoca Benefici (gestione Cumulo)
 *
 * @author InterSistemi Itali S.p.A.
 *
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioRichiestaGERevocaBenefici extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	public String processRequest() throws F3BException {

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		// cerco la Richiesta
		BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicMod = lCtrlRich.ExRicercaRichiestePmInCumuloById(aIdRich);

		setRequestAttribute("RichiestaGE", lRicMod);

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

		return PG_DETT_RICH_GE_REV_BENEFICI;
	}

}