package siap.sige.tenore.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.util.SICOLookupRemote;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.reato.model.ReatoSentenzaSigeModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

@SuppressWarnings("rawtypes")
public class ActLoadModificaOggettiRichiesta extends ActionSige implements ICostantiTenoreSige {

	public String processRequest() throws Exception {

		// ID Richiesta Sige
		BigDecimal lIdRichiesta = getRequestBigDecimalParameter(CAMPO_RIC_SIG_ID_RICHIESTA_SIGE);
		setRequestAttribute("idRichiesta", lIdRichiesta.toString());

		// [EC] 20190325 INTERVENTO PER 11.1.2
		String codContenuto = getRequestStringParameter(COD_CONTENUTO);
		if (codContenuto == null || "".equals(codContenuto)) {
			throw new F3BException(F3BException.USER_MESSAGE, "Contenuto Oggetto SIGE mancante !!");
		}
		// 20190508 [SG]: eliminata variabile non utilizzata
		// String descrContenutoSelected = "";

		// Carica la lista dei contenuti
		Option lOption = new Option(DecodificheManager.getInstance().getContenutiSigeTrattino());
		setRequestAttribute("contenuto", lOption.toString());

		ITenoreSige lTenSigeCtrl = SIGELookupRemote.getTenoreSigeRemote();
		// Ricerca Tenori
		Vector listaTenori = lTenSigeCtrl.ExRicercaTenoreEstesoByRichiesta(lIdRichiesta, codContenuto);
		setRequestAttribute("tenori", listaTenori);

		IDecodifiche lDecoCtrl = SICOLookupRemote.getDecodificheRemote();
		Collection lOggetti = lDecoCtrl.ExListaOggettiSige();
		setRequestAttribute("listaOggetti", lOggetti);

		IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		Vector lSentenze = lFasSenCtrl.ExRicercaSentenzeAssegnateFascicolo(
				getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());
		setRequestAttribute("sentenze", lSentenze);

		String codOggetto = "";
		String descOggetto = "";

		String idSentenza = "";
		String descSentenza = "";
		String idSentenzaAppo = "";

		String idReato = "";
		String descReato = "";

		Iterator iterTenori = listaTenori.iterator();
		while (iterTenori.hasNext()) {
			TenoreSigeEstesoModel tenoreSigeEsteso = (TenoreSigeEstesoModel) iterTenori.next();
			// Oggetti
			if (!codOggetto.contains(tenoreSigeEsteso.getTenoreSige().getCodOggettoSige())) {
				codOggetto += tenoreSigeEsteso.getTenoreSige().getCodOggettoSige().toString() + "|";
				descOggetto += tenoreSigeEsteso.getTenoreSige().getDescrOggettoSige() + "\n";
			}

			// // [EC] 20190325 INTERVENTO PER 11.1.2
			// //Contenuto
			// if(!codContenuto.contains(tenoreSigeEsteso.getTenoreSige().getCodContenutoSige())){
			// codContenuto +=tenoreSigeEsteso.getTenoreSige().getCodContenutoSige();
			// descrContenutoSelected = tenoreSigeEsteso.getTenoreSige().getDescrContenutoSige();
			// }

			// Titoli Esecutivi
			if (!idSentenza.contains(tenoreSigeEsteso.getSentenza().getIdSentenza().toString())) {
				idSentenza += tenoreSigeEsteso.getSentenza().getIdSentenza().toString() + "|";
				descSentenza += tenoreSigeEsteso.getSentenza().getLabelSentenza() + "\n";
				idSentenzaAppo = tenoreSigeEsteso.getSentenza().getIdSentenza().toString();
			}

			// Reati
			if (tenoreSigeEsteso.getReato() != null) {
				if (!idReato.contains(tenoreSigeEsteso.getReato().getIdReato().toString())) {
					BigDecimal idFasSigeSentenza = ricercaIdFasSigeSentenza(new BigDecimal(idSentenzaAppo));
					Vector listaReati = ricercaReatiByFasSen(idFasSigeSentenza);
					Iterator iterReati = listaReati.iterator();

					// Ciclo di Caricamento Reati.
					while (iterReati.hasNext()) {
						ReatoCircostanzaModel reatoCircostanza = (ReatoCircostanzaModel) iterReati.next();
						if (tenoreSigeEsteso.getReato().getIdReato()
								.equals(reatoCircostanza.getReato().getIdReato())) {
							if (!idReato.contains(tenoreSigeEsteso.getReato().getIdReato().toString())) {
								descReato += reatoCircostanza.getDescPopUp()[0] + "\n";
								idReato += reatoCircostanza.getReato().getIdReato().toString() + "|";
							}
						}
					}

				}
			}

		}

		setRequestAttribute("codOggetto", codOggetto);
		setRequestAttribute("descOggetto", descOggetto);

		setRequestAttribute("codSentenza", idSentenza);
		setRequestAttribute("descSentenza", descSentenza);

		setRequestAttribute("idReato", idReato);
		setRequestAttribute("descReato", descReato);

		// 25/03/2018 intervento per richieste 11.2.1
		lOption.setSelected(codContenuto); // questa cosa va rivista!!! si possono aggiungere due contenuti
											// diversi al procedmento???
		setRequestAttribute("contenuto", lOption.toString());
		setRequestAttribute("codContenuto", codContenuto);
		// setRequestAttribute("descrContenutoSelected", descrContenutoSelected);

		return IWebConstants.ROOT_DIR + "files/siap/sige/tenore/ModificaTenoriSige.jsp";
	}

	private BigDecimal ricercaIdFasSigeSentenza(BigDecimal lIdSentenza) throws F3BException {

		BigDecimal lIdFasSigeSentenza = null;

		// Preparazione del Model con il filtro di ricerca
		SentenzaSigeModel lFasSigeSen = new SentenzaSigeModel();
		lFasSigeSen.setFasIdFascicoloSige(
				getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());
		lFasSigeSen.setIdSentenza(lIdSentenza);

		// Ricerca
		IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		Vector lFasSigeSenLista = lFasSenCtrl.ExRicercaFasSigeSentenza(lFasSigeSen);
		if (lFasSigeSenLista != null && lFasSigeSenLista.size() > 0) {
			lFasSigeSen = (SentenzaSigeModel) lFasSigeSenLista.get(0);
			lIdFasSigeSentenza = lFasSigeSen.getIdFasSigeSentenza();
		}

		return lIdFasSigeSentenza;
	}

	private Vector ricercaReatiByFasSen(BigDecimal aKey) throws F3BException {

		Vector lVectRisultato = new Vector();
		IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
		ReatoSentenzaSigeModel lReatoSige = new ReatoSentenzaSigeModel();
		lReatoSige.setFasSigeSenId(aKey);
		lVectRisultato = lReaCtrl.ExRicercaReatoCircostanzaBySentenzaSige(aKey);

		return lVectRisultato;
	}

}