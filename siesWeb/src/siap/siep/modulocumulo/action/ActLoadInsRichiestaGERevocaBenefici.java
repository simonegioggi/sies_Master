package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IBeneficioCumulo;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings("rawtypes")
public class ActLoadInsRichiestaGERevocaBenefici extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		IstruttoriaCumuloModel lIstrCumulo = super.getDatiIstruttoria();

		String lCodMotivo = "";
		String lPage = "";
		String lModalita = "I"; // default inserimento

		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		if ("I".equals(lModalita)) {
			// Inserimento
			this.getTitoliBeneficiScelti(lIstrCumulo);
			lPage = PG_INS_RICH_GE_REV_BENEFICI;

			// siesLogger.debug("--XX-- Inserimento - lpage = "+lPage);
		} else if ("M".equals(lModalita)) {
			// Modifica cerco la Richiesta da Modificare
			BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
			IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
			RichiestePmInCumuloModel lRicMod = lCtrlRich.ExRicercaRichiestePmInCumuloById(aIdRich);

			setRequestAttribute("RichiestaAlGE", lRicMod);

			// Ricerca dei Titoli collegati alla Richiesta: Titolo a cui appartiene il Beneficio e Titolo di
			// Riferimento della Revoca
			ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();
			TitoloCumulatoModel lTitoloMod = lCtrlT
					.ExRicercaTitoloCumulatoById(lRicMod.getTitIdTitoloCumulato());
			TitoloCumulatoModel lTitoloRevocante = lCtrlT
					.ExRicercaTitoloCumulatoById(lRicMod.getTitIdTitoloCumulatoRef());

			// Ricerca dei Benefici (dati in Sentenza e dati con Provvedimento) Revocati
			TitoloCumulatoModel lTitoloBenefici = lCtrlRich.ExRicercaRichPMBeneficioCum(lTitoloMod,
					lRicMod.getIdRichiestePmInCumulo());

			setRequestAttribute("TitoloRichiesta", lTitoloBenefici);
			setRequestAttribute("TitoloRiferimento", lTitoloRevocante);

			if (lRicMod.getCodMotivo() != null)
				lCodMotivo = lRicMod.getCodMotivo();

			lPage = PG_MOD_RICH_GE_REV_BENEFICI;

			// siesLogger.debug("--XX-- Modifica - lpage = "+lPage);
		} else {
			// Rilanciare Eccezione - Operazione non supportata
		}

		// ================================
		// Recupero i dati delle combo
		// ================================
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlternativo("REVOCA");
		Collection lColMotivo = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
		setRequestAttribute("oggetto", lColMotivo);

		// Solo in caso di Modifica Richiesta Revoca di Sospensione/Non Mensione
		if (!lCodMotivo.equals("")) {
			Iterator itxOggetto = lColMotivo.iterator();
			while (itxOggetto.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) itxOggetto.next();
				if (lDecMod.getCode().equals(lCodMotivo)) {
					setRequestAttribute("articolo", lDecMod.getFiltro());
					setRequestAttribute("motivazione", lDecMod.getDescription());
					break;
				}
			}
		}

		setRequestAttribute("modalita", lModalita);
		return lPage;

	} // Chiude processRequest()

	private void getTitoliBeneficiScelti(IstruttoriaCumuloModel aIstruttoriaModel) throws F3BException {

		siesLogger.debug("--XX-- Inserimento -  Start getTitoliBeneficiScelti");

		ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();
		TitoloCumulatoModel lTitolo = null;

		// Lista dei check totali della form, Selezionati e NON Selezionati dall'Utente:
		// String[] lIdTitoliSelezionati = null;
		// if (!isRequestParameterNullObj(CAMPO_ID_TITOLO_SELEZIONATO))
		// lIdTitoliSelezionati = getRequestStringParameters(CAMPO_ID_TITOLO_SELEZIONATO);

		// siesLogger.debug("--XX-- lIdTitoliSelezionati = "+lIdTitoliSelezionati.length);

		String lIdTitolo = "";
		String lIdBeneficio = "";
		String lIdStatoEsec = "";
		String lIdComputo = "";

		String lIdBeneficio2 = "";

		// check Selezionati dall'Utente: ( nel caso siano + di 1 check )===
		String[] lIdSelezionatos = null;
		if (!isRequestParameterNullObj(CAMPO_ID_TITOLO_BEN_STATOESEC_COMP))
			lIdSelezionatos = getRequestStringParameters(CAMPO_ID_TITOLO_BEN_STATOESEC_COMP);

		if (lIdSelezionatos != null && lIdSelezionatos.length > 0) {
			if (lIdSelezionatos.length > 1) {
				String EleSelez0 = lIdSelezionatos[0];
				String[] EleSpl0 = EleSelez0.split(";");

				lIdTitolo = EleSpl0[0];
				lIdBeneficio = EleSpl0[1];
				lIdStatoEsec = EleSpl0[2];
				lIdComputo = EleSpl0[3];

				String EleSelez1 = lIdSelezionatos[1];
				String[] EleSpl1 = EleSelez1.split(";");

				// String nulllIdTitolo = EleSpl1[0];
				lIdBeneficio2 = EleSpl1[1];
				// String nulllIdStatoEsec = EleSpl1[2];
				// String nulllIdComputo = EleSpl1[3];

			} else {
				String EleSelez0 = lIdSelezionatos[0];
				String[] EleSpl0 = EleSelez0.split(";");

				lIdTitolo = EleSpl0[0];
				lIdBeneficio = EleSpl0[1];
				lIdStatoEsec = EleSpl0[2];
				lIdComputo = EleSpl0[3];

			}
		}

		lTitolo = lCtrlT.ExRicercaTitoloCumulatoById(new BigDecimal(lIdTitolo));

		BeneficioCumuloModel lBeneMod = null;
		BeneficioCumuloModel lBeneMod2 = null;
		IBeneficioCumulo lCtrlB = SIEPLookupRemote.getBeneficioCumuloRemote();
		Vector<BeneficioCumuloModel> VecBen = new Vector<>();

		if (!lIdBeneficio.equals("") && !lIdBeneficio.equals("-")) {
			lBeneMod = lCtrlB.ExRicercaBeneficioCumuloByKey(new BigDecimal(lIdBeneficio));
			if (lBeneMod != null && lBeneMod.getIdBeneficioCumulo() != null) {
				setRequestAttribute("lQuantiBenefici", "1");
				if (!lIdBeneficio2.equals("") && !lIdBeneficio2.equals("-")) { // richiesta revoca per 2
																				// Benefici
					VecBen.addElement(lBeneMod);

					lBeneMod2 = lCtrlB.ExRicercaBeneficioCumuloByKey(new BigDecimal(lIdBeneficio2));
					if (lBeneMod2 != null && lBeneMod2.getIdBeneficioCumulo() != null) {
						setRequestAttribute("lQuantiBenefici", "2");
						VecBen.addElement(lBeneMod2);
					}

					if (VecBen != null && VecBen.size() > 0) {
						lTitolo.setBeneficiCumulo(VecBen);
					}

				} else {
					lTitolo.setBeneficioCumulato(lBeneMod);
				}
			}
		}

		// Se Esiste StatoEsecTitolo trattasi di Beneficio con Provvedimento
		if (lIdStatoEsec != null && !lIdStatoEsec.equals("-")) {
			StatoEsecTitoloCumulatoModel lStatoEseMod = null;
			IStatoEsecTitoloCumulato lctrlS = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
			lStatoEseMod = lctrlS.ExRicercaStatoEsecTitoloCumulatoById(new BigDecimal(lIdStatoEsec));

			setRequestAttribute("lQuantiBenefici", "1");

			Vector<ComputiCumuloModel> lVec = new Vector<>();
			if (lIdComputo != null && !lIdComputo.equals("-")) {
				ComputiCumuloModel lComputo = null;
				IComputiCumulo lctrlC = SIEPLookupRemote.getComputiCumuloRemote();
				lComputo = lctrlC.ExRicercaComputiCumuloById(new BigDecimal(lIdComputo));

				lVec.addElement(lComputo);

			}

			if (lVec != null && lVec.size() > 0)
				lStatoEseMod.setListaComputi(lVec);

			lTitolo.setStatoEsecTitoloCumulato(lStatoEseMod);

		}

		setRequestAttribute("Titolo", lTitolo);
	} // Chiude getElencoTitoliReato()

} // End Action