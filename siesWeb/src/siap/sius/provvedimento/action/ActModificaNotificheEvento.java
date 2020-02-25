package siap.sius.provvedimento.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.curatore.controller.ICuratore;
import siap.sige.curatore.model.CuratoreModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.curatore.controller.ICuratoreSius;
import siap.sius.curatore.model.CuratoreSiusModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActModificaNotificheEvento
 * </p>
 * <p>
 * Description: Classe Action per la ricerca delle Notifiche legate ad un Evento
 * </p>
 * La ricerca delle notifiche viene effettuate per modificare le date di notifica.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class ActModificaNotificheEvento extends ActionSiap implements ICostantiProvvedimento {

	public String processRequest() throws F3BException {

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		INotifica lCtrl = SIEPLookupRemote.getNotificaRemote();
		Vector<NotificaModel> lVect = lCtrl.ExRicercaEstesaNotificaByKeyEvento(lIdEvento);
		UfficioModel lUff = getUfficioUtenteConnesso();

		// MEV_65: filtro lo stato notifiche solo per:
		// 1. Soggetto;
		// 2. Avvocato;
		// 3. Comunicazione ad altro destinatario;
		// 4. Procure generale sede (TDS) oppure Procura della Repubblica Sede (UDS)
		Iterator<NotificaModel> i = lVect.iterator();
		Vector<NotificaModel> v = new Vector<>();
		while (i.hasNext()) {
			NotificaModel nm = i.next();
			if (nm.getSogIdSoggetto() != null || nm.getAvvIdAvvocatoFascicoloSius() != null)
				v.add(nm);
			else if ("UDS".equals(lUff.getCodTipoUfficio()) && nm.getUfficio() != null
					&& nm.getUfficio().getCodTipoUfficio().startsWith("PM")
					&& !"per l'esecuzione".equals(nm.getNote()))
				v.add(nm);
			else if ("TDS".equals(lUff.getCodTipoUfficio()) && nm.getUfficio() != null
					&& nm.getUfficio().getCodTipoUfficio().startsWith("PG"))
				v.add(nm);
			else if (("TDSM".equals(lUff.getCodTipoUfficio()) || "UDSM".equals(lUff.getCodTipoUfficio()))
					&& nm.getUfficio() != null && "PMM".equals(nm.getUfficio().getCodTipoUfficio())
					&& "C".equals(nm.getCodTipoNotifica())
					&& !("per l'esecuzione".equals(nm.getNote()) || "per comunicazione".equals(nm.getNote())))
				v.add(nm);
			else if (nm.getAutEstIdAutoritaEsterna() != null && "-".equals(nm.getUffCodUfficio()))
				v.add(nm);
		}
		// setRequestAttribute("notifiche", lVect);
		setRequestAttribute("notifiche", v);
		setRequestAttribute("IdEvento", lIdEvento);
		setRequestAttribute("modalita", "M");

		// Eventuale Curatore Sius 23/05/2011
		FascicoloGPModel lFascicoloGPModel = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		CuratoreSiusModel lCuratore = null;
		if (lUff.getCodTipoUfficio().equals("UDS")) {
			ICuratoreSius lCurSiusCtrl = SIUSLookupRemote.getCuratoreSiusRemote();
			lCuratore = lCurSiusCtrl.ExRicercaCurSiusByFascicolo(
					lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
			if (lCuratore != null) {
				// Curatore
				if (lCuratore.getCurIdCuratore() != null) {
					ICuratore lCurCtrl = SIGELookupRemote.getCuratoreRemote();
					CuratoreModel lCurMod = lCurCtrl.ExRicercaCuratoreByKey(lCuratore.getCurIdCuratore());
					lCuratore.setCuratore(lCurMod);
				}
			}
			setRequestAttribute("curatore", lCuratore);
		}

		return PG_NOTIFICHE_EVENTO;
	}

}