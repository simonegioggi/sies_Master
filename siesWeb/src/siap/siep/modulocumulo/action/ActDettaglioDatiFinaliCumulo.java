package siap.siep.modulocumulo.action;

import f3b.util.F3BException;

/**
 * Action di dettaglio dei dati finali cumulo. Prima form
 * 
 * @author d.fiorletta
 *
 */
public class ActDettaglioDatiFinaliCumulo extends ActionModuloCumulo implements ICostantiModuloCumulo {

	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		super.getDatiIstruttoria();
		/* DatiFinaliCumuloAggregatoModel lDatiAggregati = */super.getDatiFinaliCumuloAggregato();

		// super.getListaTitoli();

		// ==========================================================================
		// Recupero i dati. Se assenti, provengo dalla navigazione, carico la form
		// di inserimento.
		// n.b. i DatiFinaliCumulo sono sempre presenti. E' l'entry point
		// ==========================================================================

		return PG_LOAD_DETTAGLIO_DATI_FINALI;
	}

}