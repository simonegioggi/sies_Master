package siap.sige.tenore.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.sige.datiprovsige.model.DatiProvvedimentoSigeModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.tenore.model.TenoreSentenzaReatoModel;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: TenoreSigeController
 * </p>
 * <p>
 * Description: Classe Controller per TenoreSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface ITenoreSige {

	public Vector<TenoreSigeModel> ExRicercaTenoreByRichiesta(BigDecimal aIdRichiesta) throws F3BException;

	public Vector<TenoreSigeEstesoModel> ExRicercaTenoreEstesoByRichiesta(BigDecimal aIdRichiesta)
			throws F3BException;

	// 02/12/2009
	public Vector<TenoreSigeEstesoModel> ExRicercaTenoreEstesoByRichiesta(BigDecimal aIdRichiesta,
			Connection lConn) throws F3BException;

	public Vector<TenoreSigeEstesoModel> ExRicercaTenoreEstesoById(BigDecimal aITenoreSige)
			throws F3BException;

	public void ExInserisciOggettoXRichiesta(TenoreSigeModel[] aTenori) throws F3BException;

	// 20190514 [SG]: aggiunto parametro di passaggio
	public void ExModificaOggettoXRichiesta(BigDecimal aIdRichiestaSige, TenoreSigeModel[] aTenori,
			String codContenutoOld) throws F3BException;

	public void ExCancellaTenoreSige(BigDecimal aIdRichiestaSige, BigDecimal aIdTenoreSige,
			BigDecimal aIdSentenza) throws F3BException;

	public void ExInserisciOggetto(TenoreSigeModel[] aTenori, Connection lConn) throws F3BException;

	public SentenzaModel ricercaSentenzaByKey(BigDecimal aSentenzaKey) throws F3BException;

	public void ExInserisciOggetti(Vector aTenori, BigDecimal aIdFascicoloSige, Connection aConn)
			throws F3BException;

	public Vector<TenoreSigeEstesoModel> ExRicercaTenoriEstesiAttivi(TenoreSigeModel aTenore)
			throws F3BException;

	public Vector<TenoreSigeModel> ExRicercaTenoriAttivi(TenoreSigeModel aTenore) throws F3BException;

	// @emma 23072018 intervento post COLLAUDO 11.2 (cambio firma del metodo aggiungendo il parametro idPrvvo
	// in input)
	public void ExInserisciEsitiOggetto(TenoreSigeModel aTenore, TenoreSentenzaReatoModel[] aTenori,
			DatiProvvedimentoSigeModel[] aDatiProv, AnnotazioneManualeModel aAnnotazioneManuale,
			Vector aistaRichieste, BigDecimal idProvv) throws F3BException;

	public void ExControlloDataIrrevocabilita(Vector aTenori) throws F3BException;

	public Vector<TenoreSigeModel> ExRicercaTenori(TenoreSigeModel aTenore) throws F3BException;

	public Vector ExRicercaTenoreByProvvedimento(BigDecimal aIdProvvedimento, Connection lConn)
			throws F3BException;

	public boolean ExAggiornaTenoriPerAnnullamento(ProvvedimentoSigeModel lProSige, Connection lConn)
			throws F3BException;

	public Vector<TenoreSigeEstesoModel> ExRicercaTenoriEstesiByIdProvvedimento(BigDecimal idProvvedimento)
			throws F3BException;

	public Vector<TenoreSigeEstesoModel> ExRicercaTenoriByCodOggettoSigeAndIdProvvedimento(String codOggetto,
			BigDecimal idProvvedimento) throws F3BException;

	public Vector<TenoreSigeEstesoModel> ExRicercaTenoreEstesoByRichiesta(BigDecimal aIdRichiesta,
			String codContenuto) throws F3BException;

}