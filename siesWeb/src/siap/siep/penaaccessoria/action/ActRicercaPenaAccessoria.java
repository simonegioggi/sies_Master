package siap.siep.penaaccessoria.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.beneficio.model.BeneficioPenaAccessoriaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaPenaAccessoria
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di PenaAccessoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActRicercaPenaAccessoria extends ActionSiap implements ICostantiPenaAccessoria {

	public String processRequest() throws Exception {

		// Gestione ritorno
		setLinkRitorno();

		PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
		lPenMod.setFasSieIdFascicoloSiep(
				getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP));

		ricercaPeneAccessorieBenefici(lPenMod);

		ricercaEventiCorrelati(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP));

		setRequestAttribute("modalita", "R");

		return PG_RICERCAPENAACCESSORIA;
	}

	protected void ricercaPeneAccessorieBenefici(PenaAccessoriaModel aPenMod) throws F3BException {
		IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
		Vector lVect = lCtrl.ExRicercaPenaAccessoria(aPenMod);

		Vector lPenBenVec = new Vector();
		BeneficioPenaAccessoriaModel lBenPenMod = null;
		if (lVect != null) {
			Iterator itx = lVect.iterator();
			while (itx.hasNext()) {
				lBenPenMod = new BeneficioPenaAccessoriaModel();
				PenaAccessoriaModel lPenModel = (PenaAccessoriaModel) itx.next();
				lBenPenMod.setPenaAccessoria(lPenModel);

				if (lPenModel != null && lPenModel.getBenIdBeneficio() != null) {
					IBeneficio lCtrlBen = SIEPLookupRemote.getBeneficioRemote();
					BeneficioModel lBenMod = lCtrlBen.ExRicercaBeneficioByKey(lPenModel.getBenIdBeneficio());
					lBenPenMod.setBeneficio(lBenMod);
				}
				lPenBenVec.add(lBenPenMod);
			}
		}
		setRequestAttribute("beneficiopenaaccessoria", lPenBenVec);
	}

	protected void ricercaEventiCorrelati(BigDecimal aIdFascicoloSiep) {
		Vector lEventi = new Vector();
		if (aIdFascicoloSiep != null) {
			try {
				// Si Invoca il controller per gli eventi di Pena Accessoria del fascicolo.
				IEvento lCtrl2 = SICOLookupRemote.getEventoRemote();
				String[] lTipoEvento = { "16", "17", "18" };
				lEventi = lCtrl2.ExRicercaEventoNotificaByFascicoloSiep(aIdFascicoloSiep, lTipoEvento);
			} catch (F3BException e) {
			}
		}

		setRequestAttribute("eveCorrelati", lEventi);
	}
}
