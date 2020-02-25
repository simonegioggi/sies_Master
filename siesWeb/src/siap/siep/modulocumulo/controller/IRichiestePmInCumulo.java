package siap.siep.modulocumulo.controller;

import java.io.ByteArrayOutputStream;

/**
* <p>Title: RichiestePmInCumuloController</p>
* <p>Description: Classe Controller per RichiestePmInCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.RichPMMisSicCumModel;
import siap.siep.modulocumulo.model.RichPMTitoloCumModel;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;

@SuppressWarnings("rawtypes")
public interface IRichiestePmInCumulo {

	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo(
			RichiestePmInCumuloModel aRichiestePmInCumulo) throws F3BException;

	public String ExInserisciRichiestePmInCumuloWithoutSequence(Vector<RichiestePmInCumuloModel> VecRichPm,
			Connection lConn) throws F3BException;

	public Vector ExRicercaRichiestePmInCumulo(RichiestePmInCumuloModel aRichiestePmInCumulo)
			throws F3BException;

	public void ExModificaRichiestePmInCumulo(RichiestePmInCumuloModel aRichiestePmInCumulo)
			throws F3BException;

	public void ExModificaRichiestaEProvvPmInCumulo(RichiestePmInCumuloModel aRichiestePmInCumulo,
			String[] lIdMisSic, String[] lIdPenAcc) throws F3BException;

	public RichiestePmInCumuloModel ExModificaRichiestePmInCumuloERichPMTitoloCum(
			RichiestePmInCumuloModel aRichiestePmInCumulo) throws F3BException;

	public void ExCancellaRichiestePmInCumulo(RichiestePmInCumuloModel aRichiestePmInCumulo)
			throws F3BException;

	public void ExCancellaRichiestePmInCumuloFull(BigDecimal aIdRichiestaPmInCumulo) throws F3BException;

	public BigDecimal ExGetCountRichiestePmInCumulo(RichiestePmInCumuloModel aRichiestePmInCumulo)
			throws F3BException;

	public RichiestePmInCumuloModel ExRicercaRichiestePmInCumuloById(BigDecimal aIdRichiestePmInCumulo)
			throws F3BException;

	public Vector ExRicercaRichiestePmInCumuloPaged(RichiestePmInCumuloModel aRichiestePmInCumulo, int aPage)
			throws F3BException;

	// se QualiRichieste="tutte" --> Tutte le Richieste; se QualiRichieste="dainviare" --> solo le Richieste
	// ancora da inviare;
	public Vector<RichiestePmInCumuloModel> ExRicercaRichiestePmInCumuloByIdIstruttoria(
			BigDecimal aIdIstruttoria, String QualiRichieste) throws F3BException;

	public Vector<RichiestePmInCumuloModel> ExRicercaRichiestePmInCumuloByIdIstruttoriaTipoRichiesta(
			BigDecimal aIdIstruttoria, String aCodTipoRic, String QualiRichieste) throws F3BException;

	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_GE_ApplicazioneBenefici(
			RichiestePmInCumuloModel aRichPmInCum, Vector<TitoloCumulatoModel> VecTitoli) throws F3BException;

	// public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_GE_RevocaBenefici(
	// RichiestePmInCumuloModel aRichPmInCum, Vector<String> ListaIdBenefici) throws F3BException;

	public Vector<RichPMTitoloCumModel> ExRicercaRichPMTitoliCum(BigDecimal aRichIdRichiesta)
			throws F3BException;

	public Vector<RichPMMisSicCumModel> ExRicercaRichPMMisSicCum(BigDecimal aRichIdRichiesta)
			throws F3BException;

	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_GE_RevocaBenefici(
			RichiestePmInCumuloModel aRichPmInCum, Vector<String> ListaIdBenefici, BigDecimal lIdProvv)
			throws F3BException;

	public Vector<TitoloCumulatoModel> ExRicercaAltriDatiRichiestaGE(
			Vector<RichPMTitoloCumModel> VecRichTitoli) throws F3BException;

	public Vector<TitoloCumulatoModel> ExRicercaAggregatiAlTitolo(Vector<TitoloCumulatoModel> VecRichTitoli)
			throws F3BException;

	public Vector<TitoloCumulatoModel> ExCaricaLibAntDelTitolo(Vector<TitoloCumulatoModel> VecTitoliInput)
			throws F3BException;

	// RICHIESTE_INVIATE_CUM
	public String ExInserisciRichiesteInviateCumWithoutSequence(Vector<RichiesteInviateCumModel> VecRichInv,
			Connection lConn) throws F3BException;

	public RichiesteInviateCumModel ExInserisciRichiesteInviateCumulo(RichiesteInviateCumModel aRichInviate,
			String[] lIdRichCollegate) throws F3BException;

	public RichiesteInviateCumModel ExRicercaRichiesteInviateCumuloById(BigDecimal aIdRichiesteInviateCum)
			throws F3BException;

	public Vector<LibAnticipataCumuloModel> ExRicercaLibAntCumByTitoloCum(BigDecimal aTitoloKey)
			throws F3BException;

	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_SORV_RevocaLA(
			RichiestePmInCumuloModel aRichPmInCum, Vector<TitoloCumulatoModel> VecTitoli) throws F3BException;

	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_SORV_UnificaMS(
			RichiestePmInCumuloModel aRichPmInCum, String[] lListaTitoli, String[] lListaMisureSicurezza)
			throws F3BException;

	public Vector<TitoloCumulatoModel> ExRicercaTitoliDiLibAntPerRichiesta(
			Vector<RichPMTitoloCumModel> VecTitoliPerRichiesta) throws F3BException;

	public Vector<RichiesteInviateCumModel> ExRicercaRichiesteInviateCumuloByIdIstruttoria(
			BigDecimal aIdIstruttoria) throws F3BException;

	public Vector<RichiesteInviateCumModel> ExRicercaRichiesteInviateCumuloByIdIstruttoria(
			BigDecimal aIdIstruttoria, String aCodTipoRichiesta) throws F3BException;

	public void ExUpdateValidaRichiesteInviateCumulo(RichiesteInviateCumModel aRichiesteInv)
			throws F3BException;

	public void ExCancellaRichiesteInviateCumuloFull(BigDecimal aIdRichiestaInv, String CodUff, String CodOp)
			throws F3BException;

	public void ExCancellaDecisioneDellaRichiesta(BigDecimal aIdRich) throws F3BException;

	public ByteArrayOutputStream ExGetDocumento(RichiesteInviateCumModel aRichiesteInv) throws F3BException;

	// Ricerca dei Benefici per i quali è stata fatta una Richiesta di Revoca
	public TitoloCumulatoModel ExRicercaRichPMBeneficioCum(TitoloCumulatoModel aTitolo,
			BigDecimal aIdRichiesta) throws F3BException;

	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_GE_RevocaSS(
			RichiestePmInCumuloModel aRichPmInCum, String[] lIdTitoli, String[] lIdSanSostCum)
			throws F3BException;

	public Vector<TitoloCumulatoModel> ExRicercaTitoli_e_SSCumByRichiestaGE(BigDecimal aIdRichiesta)
			throws F3BException;

	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_GE_SostituzionePenaAccCum(
			RichiestePmInCumuloModel aRichPmInCum, BigDecimal aTitoloKey, String[] lIdPeneAcc)
			throws F3BException;

	public TitoloCumulatoModel ExRicercaTitolo_e_PeneAccCumByRichiestaGE(BigDecimal aIdRichiesta)
			throws F3BException;

	public Vector<TitoloCumulatoModel> ExRicercaTitoli_e_PeneAccCumByRichiestaGE(BigDecimal aIdRichiesta)
			throws F3BException;

	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_GE_RevocaPenaAccCum(
			RichiestePmInCumuloModel aRichPmInCum, String[] lIdTitoli_PeneAcc) throws F3BException;

	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_GE_ApplicazionePenaAccCum(
			RichiestePmInCumuloModel aRichPmInCum) throws F3BException;

	public TitoloCumulatoModel ExRicercaTitolo_ByRichiestaGE(BigDecimal aIdRichiesta) throws F3BException;

	// Inserimento Richiesta (Tipo = 014 Altre Richieste) del PM alla SORVEGLIANZA.
	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_RichPMTitoloCum_SORV(
			RichiestePmInCumuloModel aRichPmInCum) throws F3BException;

	// Inserimento Richiesta (Tipo = 031 Richieste Revoca M.A.) del PM alla SORVEGLIANZA.
	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_SORV_RevocaMisAlt(
			RichiestePmInCumuloModel aRichPmInCum, BigDecimal aIdStatoEsec) throws F3BException;

	public TitoloCumulatoModel ExRicercaTitolo_e_StatoEsecTitoloCumByRichiestaGE(BigDecimal aIdRichiesta)
			throws F3BException;

	public String ExinserisciTabellediRelazioneWithoutSequence(RichiestePmInCumuloModel aRichPmInCumMod,
			Connection lConn) throws F3BException;

}