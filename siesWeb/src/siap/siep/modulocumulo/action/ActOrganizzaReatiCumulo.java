package siap.siep.modulocumulo.action;

/**
 * <p>Title: ActOrganizzaReatiCumulo</p>
 * <p>Description: Classe Action per l'organizzazione dei Reati</p>
 * <p>    in ambito Cumulo (legato al titolo Cumulato)</p>
 */

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActOrganizzaReatiCumulo extends ActionModuloCumulo implements ICostantiReatoCumulo {

	public String processRequest() throws Exception {

		BigDecimal lIdTitolo = getRequestBigDecimalParameter(
				ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

		// Recupera TUTTI i reati del Titolo
		ReatoCumuloModel lReaCumMod = new ReatoCumuloModel();
		lReaCumMod.setTitIdTitoloCumulato(lIdTitolo);
		IReatoCumulo lCtrl = SIEPLookupRemote.getReatoCumuloRemote();
		Vector reatiDaOrganizzare = lCtrl.ExRicercaReatoCumulo(lReaCumMod);

		// Recupera dalla form....
		String strReatiNorme = getRequestStringParameter("strreatinorme");
		String[] arrReatiNorme = strReatiNorme.split("=");

		Vector normePresenti = new Vector();
		ReatoCumuloModel lReato = null;

		ReatoCumuloModel lReatoUpdate = null;
		Vector reatiUpdate = new Vector();

		ReatoCumuloModel lReatoOld = null;

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

						lReatoUpdate = new ReatoCumuloModel();

						lReatoUpdate.setIdReatoCum(new BigDecimal(arrIdNorme[y]));
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
						lReatoUpdate.setCodTipoSanzione(lReatoOld.getCodTipoSanzione());
						lReatoUpdate.setSanzionePecuniaria(lReatoOld.getSanzionePecuniaria());

						reatiUpdate.add(lReatoUpdate);

					} else {
						progReato = arrIdNorme[y];
						contReato++;
					} // Chiude if Y!=0

				} // Chiude for (int y = 0

			} // Chiude if (arrIdNorme.length > 1

		} // Chiude for (int i = 0;

		// ==========================================================================
		//
		// ==========================================================================
		ReatoCumuloModel lReatoCanc = null;
		Vector reatiCanc = new Vector();
		Iterator itx = reatiDaOrganizzare.iterator();
		while (itx.hasNext()) {

			lReato = new ReatoCumuloModel();
			lReato = (ReatoCumuloModel) itx.next();

			if (!normePresenti.contains(lReato.getIdReatoCum())) {

				lReatoCanc = new ReatoCumuloModel();

				lReatoCanc.setIdReatoCum(lReato.getIdReatoCum());
				reatiCanc.add(lReatoCanc);
			}

		}

		// ==========================================================================
		//
		// ==========================================================================
		lCtrl.ExOrganizzaReatiCum(reatiUpdate, reatiCanc);

		String lPage = "";

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActRicercaReatoCumulo";

		return lPage;
	}

	/**
	 * 
	 * @param reatiOld
	 * @param IdReato
	 * @return
	 */
	private ReatoCumuloModel getReatoModelbyId(Vector reatiOld, BigDecimal IdReato) {

		ReatoCumuloModel rmTemp = null;

		Iterator itx = reatiOld.iterator();

		while (itx.hasNext()) {

			ReatoCumuloModel rmAppo = new ReatoCumuloModel();
			rmAppo = (ReatoCumuloModel) itx.next();

			if (rmAppo.getProgrCircostanza().intValue() == 1) {

				rmTemp = new ReatoCumuloModel();
				rmTemp = rmAppo;
			}

			if (rmAppo.getIdReatoCum().equals(IdReato))
				return rmTemp;
		}

		return null;
	}

	/**
	 * 
	 * @param reatiOld
	 * @param progrReato
	 * @return
	 */
	private String getMyProgrNumeroManuale(Vector reatiOld, BigDecimal progrReato) {

		Iterator itx = reatiOld.iterator();

		while (itx.hasNext()) {

			ReatoCumuloModel rmAppo = new ReatoCumuloModel();
			rmAppo = (ReatoCumuloModel) itx.next();

			if (rmAppo.getProgrReato().equals(progrReato) && rmAppo.getProgrCircostanza().intValue() == 1) {

				return rmAppo.getProgrNumeroManuale();
			}

		}

		return null;
	}

}