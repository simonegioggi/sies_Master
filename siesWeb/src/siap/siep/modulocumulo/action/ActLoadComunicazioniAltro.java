package siap.siep.modulocumulo.action;

import f3b.util.F3BException;
import siap.siep.SIEPException;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;

public class ActLoadComunicazioniAltro extends ActionModuloCumulo implements ICostantiModuloCumulo {

	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		/* IstruttoriaCumuloModel lIstruttoriaModel = */super.getDatiIstruttoria();
		DatiFinaliCumuloAggregatoModel lDatiFinaliCumulo = super.getDatiFinaliCumuloAggregato();

		if (lDatiFinaliCumulo.getProvvedimentoCumulo() == null
				|| lDatiFinaliCumulo.getProvvedimentoCumulo().getEvento() == null) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Nessuna Provvedimento di cumulo associato all'istruttoria corrente. Impossibile inserire le comunicazioni");
		} else if ("A".equals(
				lDatiFinaliCumulo.getProvvedimentoCumulo().getEvento().getFlagDocumentoRegistrato())) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Il Provvedimento di Cumulo risulta annullato. Impossibile inserire le comunicazioni");
		} else if (!"S".equals(
				lDatiFinaliCumulo.getProvvedimentoCumulo().getEvento().getFlagDocumentoRegistrato())) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Il Provvedimento di cumulo non risulta validato. Impossibile inserire le comunicazioni");
		}

		// if (lDatiFinaliCumulo.getProvvedimentoCumulo()!=null &&
		// lDatiFinaliCumulo.getProvvedimentoCumulo().getNotifiche()!=null) {
		// } else {
		// throw new SIEPException (SIEPException.USER_MESSAGE, "Nessuna Provvedimento presente. Impossibile
		// inserire comunicazioni");
		// }

		return PG_GRIGLIA_COMUNICAZIONI_ALTRE;
	}

}