package siap.siep.reato.action;

/**
 * <p>Title: ActOrganizzaReati</p>
 * <p>Description: Classe Action per l'organizzazione dei Reati</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.ReatoContinuazioneController;
import siap.siep.reato.controller.ReatoController;
import siap.siep.reato.model.ReatoModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActOrganizzaReati extends ActionSiap implements ICostantiReato {

	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = new FascicoloSiepModel();
		lFascMod.setIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());

		ReatoContinuazioneController lRCtrl = new ReatoContinuazioneController();
		Vector reatiDaOrganizzare = lRCtrl
				.ExRicercaReatiNoCircostanzaByFascicolo(lFascMod.getIdFascicoloSiep());

		String strReatiNorme = getRequestStringParameter("strreatinorme");
		String[] arrReatiNorme = strReatiNorme.split("=");

		Vector normePresenti = new Vector();

		ReatoModel lReato = null;

		ReatoModel lReatoCanc = null;
		Vector reatiCanc = new Vector();

		ReatoModel lReatoUpdate = null;
		Vector reatiUpdate = new Vector();

		ReatoModel lReatoOld = null;

		int contReato = 0;
		String progReato = "";
		String progReatoManuale = "";

		for (int i = 0; i < arrReatiNorme.length; i++) {

			String[] arrIdNorme = arrReatiNorme[i].split("-");

			// Controllo se nella stringa c'è più di un valore
			// se no non lo tratto perchè significa che c'è solo
			// il progressivo reato senza norme (reato cancellato)
			if (arrIdNorme.length > 1) {

				for (int y = 0; y < arrIdNorme.length; y++) {

					if (y != 0) {

						normePresenti.add(new BigDecimal(arrIdNorme[y]));

						lReatoUpdate = new ReatoModel();

						lReatoUpdate.setIdReato(new BigDecimal(arrIdNorme[y]));

						lReatoUpdate.setProgrReato(new BigDecimal(contReato));

						lReatoUpdate.setProgrCircostanza(new BigDecimal(y));

						// Se la norma trattata è in prima posizione
						if (y == 1) {

							// Viene recuperato il model della prima norma del
							// reato al quale appartiene la norma correntemente
							// trattata
							lReatoOld = getReatoModelbyId(reatiDaOrganizzare, new BigDecimal(arrIdNorme[y]));

							// Viene recuperato e mantenuto l'eventuale numero
							// manuale del reato
							progReatoManuale = getMyProgrNumeroManuale(reatiDaOrganizzare,
									new BigDecimal(progReato));
						}

						lReatoUpdate.setProgrNumeroManuale(progReatoManuale);

						// Vengono rimpiazzati i seguenti campi con i valori
						// della prima norma
						lReatoUpdate.setCodTipoReato(lReatoOld.getCodTipoReato());
						lReatoUpdate.setDataReato(lReatoOld.getDataReato());
						lReatoUpdate.setDataInizio(lReatoOld.getDataInizio());
						lReatoUpdate.setAnnoInizio(lReatoOld.getAnnoInizio());
						lReatoUpdate.setMeseInizio(lReatoOld.getMeseInizio());
						lReatoUpdate.setGiornoInizio(lReatoOld.getGiornoInizio());
						lReatoUpdate.setDataFine(lReatoOld.getDataFine());
						lReatoUpdate.setAnnoFine(lReatoOld.getAnnoFine());
						lReatoUpdate.setMeseFine(lReatoOld.getMeseFine());
						lReatoUpdate.setGiornoFine(lReatoOld.getGiornoFine());
						lReatoUpdate.setCodPeriodoConsumazione(lReatoOld.getCodPeriodoConsumazione());
						lReatoUpdate.setDescLuogo(lReatoOld.getDescLuogo());
						lReatoUpdate.setNote(lReatoOld.getNote());

						lReatoUpdate.setCodTipoPenaDetentiva(lReatoOld.getCodTipoPenaDetentiva());
						lReatoUpdate.setNumGiorni(lReatoOld.getNumGiorni());
						lReatoUpdate.setNumMesi(lReatoOld.getNumMesi());
						lReatoUpdate.setNumAnni(lReatoOld.getNumAnni());
						lReatoUpdate.setNumAnniIsolamentoDiurno(lReatoOld.getNumAnniIsolamentoDiurno());
						lReatoUpdate.setNumMesiIsolamentoDiurno(lReatoOld.getNumMesiIsolamentoDiurno());
						lReatoUpdate.setNumGiorniIsolamentoDiurno(lReatoOld.getNumGiorniIsolamentoDiurno());
						lReatoUpdate.setDataInizioIsolamentoDiurno(lReatoOld.getDataInizioIsolamentoDiurno());
						lReatoUpdate.setDataFineIsolamentoDiurno(lReatoOld.getDataFineIsolamentoDiurno());
						lReatoUpdate.setSanzionePecuniaria(lReatoOld.getSanzionePecuniaria());
						lReatoUpdate.setCodTipoSanzione(lReatoOld.getCodTipoSanzione());

						reatiUpdate.add(lReatoUpdate);

					} else {

						progReato = arrIdNorme[y];
						contReato++;
					}
				}
			}
		}

		Iterator itx = reatiDaOrganizzare.iterator();

		while (itx.hasNext()) {

			lReato = new ReatoModel();
			lReato = (ReatoModel) itx.next();

			if (!normePresenti.contains(lReato.getIdReato())) {

				lReatoCanc = new ReatoModel();

				lReatoCanc.setIdReato(lReato.getIdReato());
				reatiCanc.add(lReatoCanc);
			}

		}

		ReatoController lCtrl = new ReatoController();
		lCtrl.ExOrganizzaReati(reatiUpdate, reatiCanc);

		String lPage = "";

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.reato.action.ActRicercaReato";

		return lPage;
	}

	private ReatoModel getReatoModelbyId(Vector reatiOld, BigDecimal IdReato) {

		ReatoModel rmTemp = null;

		Iterator itx = reatiOld.iterator();

		while (itx.hasNext()) {

			ReatoModel rmAppo = new ReatoModel();
			rmAppo = (ReatoModel) itx.next();

			if (rmAppo.getProgrCircostanza().intValue() == 1) {

				rmTemp = new ReatoModel();
				rmTemp = rmAppo;
			}

			if (rmAppo.getIdReato().equals(IdReato))
				return rmTemp;
		}

		return null;
	}

	private String getMyProgrNumeroManuale(Vector reatiOld, BigDecimal progrReato) {

		Iterator itx = reatiOld.iterator();

		while (itx.hasNext()) {

			ReatoModel rmAppo = new ReatoModel();
			rmAppo = (ReatoModel) itx.next();

			if (rmAppo.getProgrReato().equals(progrReato) && rmAppo.getProgrCircostanza().intValue() == 1) {

				return rmAppo.getProgrNumeroManuale();
			}

		}

		return null;
	}
}