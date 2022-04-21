package siap.siep.modulocumulo.action;

import f3b.util.F3BException;
import siap.siep.SIEPException;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;

public class ActLoadComunicazioniAltro extends ActionModuloCumulo implements ICostantiModuloCumulo {

	public String processRequest() throws F3BException {

		// Ticket#20211216018 - Aggiunto controllo se modificabile per evitare che utenti di altri uffici
		//                      abbiano accesso alla fuzione che crea una stampa "al volo" ma con i dati 
		//                      dell'ufficio dell'utente connesso
		super.isFascicoloSiepDiCompetenza();
		// Ticket#20211216018 - FINE
		
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