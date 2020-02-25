package siap.siep.richiesta.action;

import java.util.ArrayList;

import siap.sico.web.ActionSiap;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * Azione Helper di tutte le Action delle Misure Alternative.
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActRichiesta extends ActionSiap implements ICostantiMisuraAlternativa {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected NotificaModel[] setNotificheRichiesta() throws F3BException {

		ArrayList lNotifiche = new ArrayList();

		// Magistrato di Sorveglianza
		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_MAGISTRATO)) {
			NotificaModel lNotModTDS = new NotificaModel();
			String lMag = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_MAGISTRATO);
			String lNoteTDS = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_TDS);

			lNotModTDS.setNote(lNoteTDS);
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lNotModTDS.setCodTipoNotifica("C");
			lNotModTDS.setDataInvio(DateUtils.getSysDate());
			lNotModTDS.setUffCodUfficio(lMag);
			lNotifiche.add(lNotModTDS);
		}

		// Campo Note
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)) {
			NotificaModel lNotMod = new NotificaModel();

			String lNote = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE);

			lNotMod.setNote(lNote);
			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lNotMod.setCodTipoNotifica("N");
			lNotMod.setDataInvio(DateUtils.getSysDate());
			lNotifiche.add(lNotMod);
		}
		return (NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]);
	}

}