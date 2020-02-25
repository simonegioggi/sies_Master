package siap.siep.modulocumulo.controller;

/**
* <p>Title: StatoEsecTitoloCumulatoController</p>
* <p>Description: Classe Controller per StatoEsecTitoloCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;

@SuppressWarnings("rawtypes")
public interface IStatoEsecTitoloCumulato {

	public StatoEsecTitoloCumulatoModel ExInserisciStatoEsecTitoloCumulato(
			StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato) throws F3BException;

	public Vector ExRicercaStatoEsecTitoloCumulato(StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato)
			throws F3BException;

	public void ExModificaStatoEsecTitoloCumulato(StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato)
			throws F3BException;

	public void ExCancellaStatoEsecTitoloCumulato(StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato)
			throws F3BException;

	public BigDecimal ExGetCountStatoEsecTitoloCumulato(StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato)
			throws F3BException;

	public StatoEsecTitoloCumulatoModel ExRicercaStatoEsecTitoloCumulatoById(
			BigDecimal aIdStatoEsecTitoloCumulato) throws F3BException;

	public StatoEsecTitoloCumulatoModel ExRicercaStatoEsecTitoloCumulatoByIdFull(
			BigDecimal aIdStatoEsecTitoloCumulato) throws F3BException;

	public Vector ExRicercaStatoEsecTitoloCumulatoPaged(StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato,
			int aPage) throws F3BException;

	public ByteArrayOutputStream ExStampaStatoEsecTitolo(FascicoloSiepModel aFasc, String lIdTemplate,
			UtenteModel aUtente) throws F3BException;

	public Vector<StatoEsecTitoloCumulatoModel> ExRicercaStatoEsecTitoloCumulatoByIdTitolo(
			BigDecimal aIdTitolo) throws F3BException;

	// public void ExAggiornaStatoEsecTitoloCumulatoByIdTitolo(Vector<BigDecimal> aListaEventiDaInserire,
	// Vector<StatoEsecTitoloCumulatoModel> aListaEventiDaRimuovere, BigDecimal aIdTitolo,
	// BigDecimal aIdIstruttoria, Connection aDBConnection, DatiOperazioneModel aDatiOper)
	// throws F3BException;

	public void ExCancellaStatoEsecTitoloCumulatoById(BigDecimal aStatoEsecTitoloCumulato, Connection aConn)
			throws F3BException;

	public String ExInserisciStatoEsecTitoloCumulatoFullWithoutSequence(
			Vector<StatoEsecTitoloCumulatoModel> aVecStatoEsec, Connection Conn) throws F3BException;

	public Vector<StatoEsecTitoloCumulatoModel> ExRicercaPresoffertiByIdTitolo(BigDecimal aIdTitolo)
			throws F3BException;

	// public void ExInserisciPresofferto (StatoEsecTitoloCumulatoModel aStatoEsecuz, ComputiCumuloModel
	// aComputoCumulo) throws F3BException ;
	public StatoEsecTitoloCumulatoModel ExInserisciStatoEsecComputoCumulo(
			StatoEsecTitoloCumulatoModel aStatoEsecuz, ComputiCumuloModel aComputoCumulo) throws F3BException;

	public void ExAggiornaStatoEsecTitoloCumulatoByIdTitolo(Vector<BigDecimal> aListaEventiDaInserire,
			Vector<StatoEsecTitoloCumulatoModel> aListaEventiDaRimuovere, BigDecimal aIdTitolo,
			BigDecimal aIdIstruttoria, Connection aDBConnection, DatiOperazioneModel aDatiOper,
			boolean aPresaInCarico) throws F3BException;

	// Sospensioni del PM
	public Vector<StatoEsecTitoloCumulatoModel> ExRicercaSospensioniDelPMByIdTitolo(BigDecimal aIdTitolo)
			throws F3BException;

	public void ExInserisciSospensioniDelPM(Vector<StatoEsecTitoloCumulatoModel> aListaProvvedimenti)
			throws F3BException;

	public void ExModificaStatoEsecTitoloCumulatoFull(StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato)
			throws F3BException;

	//
	public Vector<StatoEsecTitoloCumulatoModel> ExRicercaProvvedimentiCumuloByIdTitoloTipoProvv(
			BigDecimal aIdTitolo, String aCodTipoEvento, String aCodTipoProvvedimento, String aCodMotivo)
			throws F3BException;

	public StatoEsecTitoloCumulatoModel ExInserisciPagamentiPP(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			Vector<ComputiCumuloModel> aListaCumuli) throws F3BException;

	public void ExModificaStatoEsecComputoCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			ComputiCumuloModel aComputoCumulo) throws F3BException;

	public void ExModificaStatoEsecComputiCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			Vector<ComputiCumuloModel> aListaCumuli) throws F3BException;

	// Fungibilità
	public BigDecimal ExInserisciFungibilitaCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			ComputiCumuloModel aComputoCumulo) throws F3BException;

	public void ExInserisciPeriodoFungibilitaCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			ComputiCumuloModel aComputoCumulo) throws F3BException;

	public void ExModificaFungibilitaCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			ComputiCumuloModel aComputoCumulo) throws F3BException;

	public Vector<StatoEsecTitoloCumulatoModel> ExRicercaProvvedimentiCumuloByIdTitoloListaProvv(
			BigDecimal aIdTitolo, String aCodTipoEvento, String aCodTipoProvvedimento,
			Vector<String> listaProvv) throws F3BException;

	public Vector<StatoEsecTitoloCumulatoModel> ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(
			BigDecimal aIdTitolo, String aCodTipoEvento, Vector<String> listaTipoProvv,
			Vector<String> listaProvv) throws F3BException;

	// LIBERAZIONE ANTICIPATA CUMULO
	public BigDecimal ExInserisciLiberazioneAnticipataCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			LibAnticipataCumuloModel aLibAntCumLAModel, LibAnticipataCumuloModel aLibAntCumLASPEModel,
			LibAnticipataCumuloModel aLibAntCumLAINTModel) throws F3BException;

	public BigDecimal ExModificaLiberazioneAnticipataCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			LibAnticipataCumuloModel aLibAntCumLAModel, LibAnticipataCumuloModel aLibAntCumLASPEModel,
			LibAnticipataCumuloModel aLibAntCumLAINTModel) throws F3BException;

	public String ExInserisciLiberazioneAnticipataCumuloFullWithoutSequence(
			Vector<LibAnticipataCumuloModel> aVecLibAntCum, Connection Conn) throws F3BException;

	// RIMEDI RISARCITORI DL 2014/92

	public BigDecimal ExInserisciRimediRisarcitoriCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			Vector<LibAnticipataCumuloModel> VecLib) throws F3BException;

	public BigDecimal ExModificaRimediRisarcitoriCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			Vector<LibAnticipataCumuloModel> VecLib) throws F3BException;

//RIDETERMINAZIONE PENA PM ALTRO 01/03/2019
  public StatoEsecTitoloCumulatoModel ExInserisciStatoEsecComputiCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuzione, Vector<ComputiCumuloModel> aListaComputi) throws F3BException;
}