package siap.sius.scadenzario.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;

/**
 * IScadenzarioSius - Classe Interfaccia per Scadenzario Sius
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IScadenzarioSius {

	public ScadenzarioSiusModel ExInserisciScadenzarioSius(ScadenzarioSiusModel aScadenzarioSius)
			throws F3BException;

	public Vector ExRicercaScadenzarioSius(ScadenzarioSiusModel aScadenzarioSius) throws F3BException;

	public ScadenzarioSiusModel ExRicercaScadenzarioSiusByKey(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaScadenzarioSius(Date aData1, Date aData2) throws F3BException;

	// Ticket#202305250112 - aggiunto filtro per codice ufficio
	// public Vector ExRicercaScadenzarioSius(String aTipoScadenzario, Date aData1, Date aData2)
	// throws F3BException;
	public Vector ExRicercaScadenzarioSius(String aTipoScadenzario, Date aData1, Date aData2,
			String aCodUfficio) throws F3BException;
	// Ticket#202305250112 - FINE

	public ScadenzarioSiusModel ExModificaScadenzarioSius(ScadenzarioSiusModel aScadenzarioSius)
			throws F3BException;

	public void ExCancellaScadenzarioSius(ScadenzarioSiusModel aScadenzarioSius) throws F3BException;

	public ScadenzarioSiusModel ExRicercaScadenzarioSiusByIdFascicoloTipo(BigDecimal aIdFascicolo,
			String aTipo) throws F3BException;

	public boolean ExScadutoScadenzarioSiusByIdFascicoloTipo(BigDecimal aIdFascicolo, String aTipo)
			throws F3BException;

	public ScadenzarioSiusModel ExModificaScadenzario(ScadenzarioSiusModel aScadenzarioSius)
			throws F3BException;

	public void ExSetVistoScadenzario(ScadenzarioSiusModel aScadenzarioSius) throws F3BException;

	public Vector ExElencoTipiScadenzarioByTipoUfficio(String aTipoUfficio) throws F3BException;

	/**
	 * Metodi per la Ricerca Fine Pena Procedimenti Pendenti Paginata
	 *
	 * @param riferimento
	 * @param ai
	 * @param ni
	 * @param af
	 * @param nf
	 * @param dii
	 * @param dif
	 * @param dsi
	 * @param dsf
	 * @param codUfficio
	 * @param pagina
	 * @return Vector
	 * @throws F3BException
	 *
	 * @author sgioggi
	 * @since MEV_2026-1
	 */
	public Vector ExRicercaFinePenaProcedimentiPendentiPaginata(String riferimento, BigDecimal ai,
			BigDecimal ni, BigDecimal af, BigDecimal nf, Date dii, Date dif, Date dsi, Date dsf,
			String codUfficio, int pagina) throws F3BException;

	public BigDecimal ExGetNumRicercaFinePenaProcedimentiPendenti(String riferimento, BigDecimal ai,
			BigDecimal ni, BigDecimal af, BigDecimal nf, Date dii, Date dif, Date dsi, Date dsf,
			String codUfficio) throws F3BException;
	// FINE MEV_2026-1

}