package siap.siep.modulocumulo.action;

import java.util.Iterator;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IBeneficioCumulo;
import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.BeneficioPenaAccessoria_CumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaBeneficiCumulo extends ActionModuloCumulo implements ICostantiBeneficiCumulo {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		// ==============================================================
		// Effettuo la ricerca dei benefici collegati al Cumulo
		// ==============================================================
		BeneficioCumuloModel lBenMod = new BeneficioCumuloModel();
		lBenMod.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));

		if (!isRequestParameterNullObj(CAMPO_COD_NATURA_BENEFICIO))
			lBenMod.setCodNaturaBeneficio(getRequestStringParameter(CAMPO_COD_NATURA_BENEFICIO));
		else
			lBenMod.setCodNaturaBeneficio("C");

		IBeneficioCumulo lCtrl = SIEPLookupRemote.getBeneficioCumuloRemote();
		Vector lVect = lCtrl.ExRicercaBeneficioCumulo(lBenMod);

		ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();

		setRequestAttribute("ListaBenefici", lVect);

		Vector lPenBenVec = new Vector();

		if (lVect != null) {
			Iterator itx = lVect.iterator();
			while (itx.hasNext()) {
				BeneficioCumuloModel lBenCumMod = (BeneficioCumuloModel) itx.next();

				BeneficioPenaAccessoria_CumuloModel lBenPenMod = new BeneficioPenaAccessoria_CumuloModel();
				lBenPenMod.setBeneficioCumulo(lBenCumMod);

				if (lBenCumMod != null && lBenCumMod.getIdBeneficioCumulo() != null) {
					// Cerco Eventuali Pene-Accessorie
					PenaAccessoriaCumuloModel lPenMod = new PenaAccessoriaCumuloModel();
					lPenMod.setTitIdTitoloCumulato(lBenCumMod.getTitIdTitoloCumulato());
					lPenMod.setBenIdBeneficioCumulo(lBenCumMod.getIdBeneficioCumulo());

					IPenaAccessoriaCumulo lCtrlPen = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
					Vector lVectPenaAcc = lCtrlPen.ExRicercaPenaAccessoriaCumulo(lPenMod);

					if (lVectPenaAcc != null && !lVectPenaAcc.isEmpty()) {
						// Prendo il Primo Rec. Pena Accessoria
						lBenPenMod.setPenaAccessoriaCumulo((PenaAccessoriaCumuloModel) lVectPenaAcc.get(0));
						lBenPenMod.setPresenzaPenaAccessoriaCumulo(true);
						// Tutte le Pene_Accessorie Trovate vanno nella Lista
						lBenPenMod.setListaPACumulo(lVectPenaAcc);

					}

					// Cerco Eventuali REVOCHE attraverso ilTitolo_Collegato al Beneficio Concesso
					if (lBenCumMod.getTitIdTitoloCumulatoCollegato() != null) {
						// Cerca i dati della REVOCA
						TitoloCumulatoModel lTitoloRevoca = lCtrlT
								.ExRicercaTitoloCumulatoById(lBenCumMod.getTitIdTitoloCumulatoCollegato());
						if (lTitoloRevoca != null && lTitoloRevoca.getIdTitoloCumulato() != null) {
							lBenPenMod.setTitoloCumulatoRevocante(lTitoloRevoca);
						}
					}

				}

				lPenBenVec.add(lBenPenMod);
			}
		}

		setRequestAttribute("beneficiopenaaccessoria", lPenBenVec);

		return PG_ELENCO_BENEFICI_CUMULO;
	}

}