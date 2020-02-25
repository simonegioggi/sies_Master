package siap.siep.modulocumulo.action;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;

/**
 * <p>
 * Title: ActLoadInserisciPenaComplessivaCumulo
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di PenaComplessiva
 * </p>
 * <p>
 * in ambito Cumulo (Pena_complessiva_Cumulo)
 * </p>
 */
public class ActLoadInserisciPenaComplessivaCumulo extends ActionModuloCumulo
		implements ICostantiPenaComplessivaCumulo {

	public String processRequest() throws Exception {

		// ==========================================================================
		// Recupero i dati dell'ISTRUTTORIA, TITOLO, da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		/* IstruttoriaCumuloModel lIstruttoriaCumMod = */super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		Option lOption = new Option(DecodificheManager.getInstance().getTipoPenaDetentivaErgastolo(), "-");
		setRequestAttribute("tipoPenaDetentiva", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getFlagLireEuro(), "EUR");
		setRequestAttribute("valute", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(), "-");
		setRequestAttribute("autoritaSentenza", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoSanzioneSostitutiva(), "-");
		setRequestAttribute("tipoSanzioneSostitutiva", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoContinuazione(), "-");
		setRequestAttribute("tipoContinuazione", "" + lOption);

		// combo Tipo Registro Generale
		Option tipoRegGen = new Option(DecodificheManager.getInstance().getTipoRegistroGenerale(), "-");
		setRequestAttribute("tipoRegGen", "" + tipoRegGen);

		// ==========================================================================
		// Recupero i titoli in istruttoria per poter gestire la continuazione
		// ==========================================================================
		/*
		 * IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote(); Vector lListaTitoli =
		 * lIstrCtrl.ExRicercaTitoliByIstruttoria (lIstruttoriaCumMod.getIdIstruttoriaCumulo());
		 * setRequestAttribute("ListaTitoli", lListaTitoli);
		 */

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		return PG_LOAD_INSERISCIPENACOMPLESSIVA_CUM;

	}

}