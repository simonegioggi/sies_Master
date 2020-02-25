package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciUlterioriContinuazioniCumulo
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci Continuazione di PenaComplessiva
 * </p>
 * <p>
 * in ambito Cumulo (Pena_complessiva_Cumulo/Continuazione_Cumulo)
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadInserisciUlterioriContinuazioniCumulo extends ActionModuloCumulo
		implements ICostantiPenaComplessivaCumulo {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// ==========================================================================
		// Recupero i dati dei Istruttoria e Titolo Cumulato da passare alla form
		// ==========================================================================
		IstruttoriaCumuloModel lIstruttoriaCumMod = super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(), "-");
		setRequestAttribute("autoritaSentenza", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoContinuazione(), "-");
		setRequestAttribute("tipoContinuazione", "" + lOption);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		// FIXME recuperare il model
		BigDecimal lIdPena = getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA_CUM);
		setRequestAttribute("lIdPenaComplessivaCum", lIdPena);

		// combo Tipo Registro Generale
		Option tipoRegGen = new Option(DecodificheManager.getInstance().getTipoRegistroGenerale(), "-");
		setRequestAttribute("tipoRegGen", "" + tipoRegGen);

		// ==========================================================================
		// Recupero i titoli in istruttoria per poter gestire la continuazione
		// ==========================================================================
		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		Vector lListaTitoli = lIstrCtrl
				.ExRicercaTitoliByIstruttoria(lIstruttoriaCumMod.getIdIstruttoriaCumulo());
		setRequestAttribute("ListaTitoli", lListaTitoli);

		// n.b. i titoli già in continuazione vanno segnati

		return PG_LOAD_INSERISCI_ULTERIORI_CONTINUAZIONI_CUM;
	}
}
