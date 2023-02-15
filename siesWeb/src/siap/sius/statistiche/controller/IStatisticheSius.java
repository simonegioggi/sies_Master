package siap.sius.statistiche.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.statistiche.model.EveFasGepSogCancModel;
import siap.sius.statistiche.model.EveFasGepSogDetModel;
import siap.sius.statistiche.model.EveFasGepSogModel;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.IspConteggioOggettiModel;
import siap.sius.statistiche.model.IspConteggioRelatoriMagistratiModel;
import siap.sius.statistiche.model.IspEstrazioneOggettiModel;
import siap.sius.statistiche.model.IspMotivoOggettoSelezionatiModel;
import siap.sius.statistiche.model.IspProcIntervalliModel;
import siap.sius.statistiche.model.ProcAggregatiCognomeElencoModel;
import siap.sius.statistiche.model.ProcAggregatiCognomeModel;
import siap.sius.statistiche.model.RicercaAggregatiCognomeModel;
import siap.sius.statistiche.model.RicercaOrdinanzaModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.statistiche.model.RicercaProvvedimentoModel;

/**
 * Title: IStatisticheSius 
 * Description: interface per il controller di Statistiche SIUS.
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IStatisticheSius {

	public Vector ExRicercaProcSiusXProvvedimentiPaginata(RicercaOrdinanzaModel aModel, int aPageNum)
			throws F3BException;

	public BigDecimal ExGetNumRicercaProcSiusXProvvedimenti(RicercaOrdinanzaModel aModel) throws F3BException;

	public EveFasGepSogProvModel ExRicercaImpugnazioneByAnnoNumUfficio(ImpugnazioneModel aModel)
			throws F3BException;

	public void ExStatoOggettiSiusStoredProcedure(String aCodUfficio, Date aDataInizio, Date aDataFine,
			String aCodCancAss, String aCollab, String aPosGiurid) throws F3BException;

	public void ExCreaStatisticaOggettiStoredProcedure(String aCodUfficio, String aCodMagistrato,
			Date aDataInizio, Date aDataFine) throws F3BException;

	public void ExCreaStatisticaRelatoriStoredProcedure(String aCodUfficio, Date aDataInizio, Date aDataFine)
			throws F3BException;

	public Vector ExRicercaMagistratiOggettiEstratti(String aCodUfficio) /* Mod. Michele 2/12/2008 */
			throws F3BException;

	public Vector ExRicercaStatisticaOggetti(String aCodUfficio)
			throws F3BException; /* Mod. Michele 4/12/2008 */

	public Vector ExRicercaStatisticaRelatori(String aCodUfficio) throws F3BException;

	public MagistratoModel ExRicercaMagistratoByCod(String aCodMag) throws F3BException;

	public EspertoModel ExRicercaEspertoByCod(String aCodMag) throws F3BException;

	public void ExEstraiProcedimentiDepositatiStProc(String aCodUfficio, Date aDataInizio, Date aDataFine,
			String aCodCancAss, String aCollab, String aPosGiu) throws F3BException;

	public void ExCreaStatisticaTempiStProc(String aCodUfficio, String aCodMag, Date aDataInizio,
			Date aDataFine) throws F3BException;

	public Vector ExRicercaStatisticaTempi(String aCodUfficio)
			throws F3BException; /* Mod. Michele 5/12/2008 */

	public Vector ExListaOggettiEstratti(String aNomeTabEstrazione) throws F3BException;

	public Vector ExListaOggettiEstratti(String aNomeTabEstrazione, String aCodUfficio)
			throws F3BException; /*
									 * Mod . Michele 5 / 12 / 2008
									 */

	public Vector ExRicercaOggettiEstratti(IspEstrazioneOggettiModel aModel) throws F3BException;

	public Vector ExRicercaOggettiEstrattiOrdinati(IspEstrazioneOggettiModel aModel) throws F3BException;

	public Vector ExRicercaTempiEstratti(IspProcIntervalliModel aModel) throws F3BException;

	public Vector ExRicercaFascicoliPendenti(String aCodUfficio, String aCodMag)
			throws F3BException; /*
									 * Mod. Michele 15 /12/2008
									 */

	public Vector ExRicercaOggettiPendenti(String aCodUfficio, String aCodMag) throws F3BException;

	public Vector ExRicercaFascicoliPriviDiRelatore(String aCodUfficio) throws F3BException;

	// public Vector ExRicercaOggettiCancellati (String aCodUfficio, String aCodMag) throws F3BException;
	public Vector ExRicercaOggettiCancellati(String aCodUfficio, String aCodMag, Date aDataInizio,
			Date aDataFine) throws F3BException;

	public Vector ExRicercaFascicoliUnificati(String aCodUfficio, String aCodMag) throws F3BException;

	// 20131215 - FR012
	public Collection<EveFasGepSogProvModel> ExRicercaProcFissatiNoDef(RicercaProcedimentoModel aModel)
			throws F3BException;

	public Collection<EveFasGepSogProvModel> ExRicercaProcFissatiNoDefPaginata(
			RicercaProcedimentoModel aModel, int aPagina) throws F3BException;

	public BigDecimal ExGetNumRicercaProcFissatiNoDef(RicercaProcedimentoModel aModel) throws F3BException;

	// 20131221 - FR015 - FA016
	public Collection<EveFasGepSogProvModel> ExRicercaProcPerProvvNoValidatiNoDeposito(
			RicercaProcedimentoModel aModel) throws F3BException;

	public Collection<EveFasGepSogProvModel> ExRicercaProcPerProvvNoValidatiNoDepositoPaginata(
			RicercaProcedimentoModel aModel, int aPagina) throws F3BException;

	public BigDecimal ExGetNumRicercaProcPerProvvNoValidatiNoDeposito(RicercaProcedimentoModel aModel)
			throws F3BException;

	// 20131229 - FR025 - FA026
	public Collection<ProcAggregatiCognomeModel> ExRicercaProcAggregati1Lettera(
			RicercaAggregatiCognomeModel aModel) throws F3BException;

	public Collection<ProcAggregatiCognomeModel> ExRicercaProcAggregati2Lettere(
			RicercaAggregatiCognomeModel aModel) throws F3BException;

	public Collection<ProcAggregatiCognomeElencoModel> ExRicercaProcAggregatiElenco(
			RicercaAggregatiCognomeModel aModel) throws F3BException;

	// 20131228 - FR027 - FA028
	public Collection<EveFasGepSogDetModel> ExRicercaProcSoggettiIstitutiDetenzione(
			RicercaProcedimentoModel aModel) throws F3BException;

	public Collection<EveFasGepSogDetModel> ExRicercaProcAggrIstitutiDetenzione(
			RicercaProcedimentoModel aModel) throws F3BException;

	// 20131228 - FR029 - FA030
	public Collection<EveFasGepSogModel> ExRicercaProcProcuraMittente(RicercaProcedimentoModel aModel)
			throws F3BException;

	public Collection<EveFasGepSogModel> ExRicercaProcTotaliProcuraMittente(RicercaProcedimentoModel aModel)
			throws F3BException;

	// 20140106 - FR017 - FA018
	public Collection<EveFasGepSogModel> ExRicercaProcFissatiNoDefNumGG(RicercaProcedimentoModel aModel)
			throws F3BException;

	public Collection<EveFasGepSogModel> ExRicercaProcFissatiNoDefNumGGPaginata(
			RicercaProcedimentoModel aModel, int aPagina) throws F3BException;

	public BigDecimal ExGetNumRicercaProcFissatiNoDefNumGG(RicercaProcedimentoModel aModel)
			throws F3BException;

	// 20140113 - FR019 - FA020
	public Collection<EveFasGepSogProvModel> ExRicercaProcProvvEmessiNoDepNumGG(
			RicercaProcedimentoModel aModel) throws F3BException;

	public Collection<EveFasGepSogProvModel> ExRicercaProcProvvEmessiNoDepNumGGPaginata(
			RicercaProcedimentoModel aModel, int aPagina) throws F3BException;

	public BigDecimal ExGetNumRicercaProcProvvEmessiNoDepNumGG(RicercaProcedimentoModel aModel)
			throws F3BException;

	// 20140122 - FR034 - FA035
	public BigDecimal ExGetNumRicercaProcPosizioneGiuridica(RicercaProcedimentoModel aRicerca)
			throws F3BException;

	public Collection<EveFasGepSogCancModel> ExRicercaProcPosizioneGiuridica(
			RicercaProcedimentoModel aRicerca) throws F3BException;

	public Collection<EveFasGepSogCancModel> ExRicercaProcPosizioneGiuridicaPaginata(
			RicercaProcedimentoModel aRicerca, int aPage) throws F3BException;

	// 20140129 - FI021 - FR022 - FA023 - FA024
	public Collection<IspConteggioRelatoriMagistratiModel> ExRicercaStatisticaComparataMagistrati(
			RicercaProcedimentoModel aRicerca) throws F3BException;

	public ArrayList<IspMotivoOggettoSelezionatiModel> ExListaMotiviOggettiSelezionati() throws F3BException;

	public Collection<IspConteggioOggettiModel> ExConteggioOggettiMagistrato(
			RicercaProcedimentoModel aRicerca) throws F3BException;

	public ArrayList<IspMotivoOggettoSelezionatiModel> ExListaMotivoOggettiSelezionati(String aCodUfficio)
			throws F3BException;

	public Collection<IspEstrazioneOggettiModel> ExRicercaProcedimentiEstrattiOggettiSelezionatiOrdinati(
			IspEstrazioneOggettiModel aModel) throws F3BException;

	// MEV10-s3: aggiunti metodi
	public Vector ExRicercaProcSiusXProvvedimentiPaginata(RicercaProvvedimentoModel mRicercaModel,
			int aPageNum) throws F3BException;

	public BigDecimal ExGetNumRicercaProcSiusXProvvedimenti(RicercaProvvedimentoModel aModel)
			throws F3BException;

	// 15032019 - MEV 73
	public Collection<EveFasGepSogModel> ExRicercaProcDlgs123_2018(UfficioModel aUfficioUtenteConnesso,
			RicercaProcedimentoModel aRicercaModel) throws F3BException;

	/**
	 * recupera la lista dei magistrati da visualizzare nella statistica per intervalli temporali
	 *
	 * @param aCodUfficio
	 * @return
	 * @throws F3BException
	 *
	 *             metodo introdotto per anomalia 7 del verbale collaudo sies 11.3 (terza sessione)
	 */
	public Vector ExRicercaMagistratiProcIntervalli(String aCodUfficio) throws F3BException;

	// MEV_9: aggiunti metodi per le statistiche di Misure Alternative
	public Collection<EveFasGepSogProvModel> ProcPerStatisticaMisureAlternative(RicercaProcedimentoModel rpm)
			throws F3BException;

	public Collection<EveFasGepSogProvModel> ExRicercaProcPerStatisticaMisureAlternative(
			RicercaProcedimentoModel rpm, int pagina) throws F3BException;

	public BigDecimal ExGetNumRicercaProcPerStatisticaMisureAlternative(RicercaProcedimentoModel rpm)
			throws F3BException;
	// FINE MEV_9

}