package siap.siep.modulocumulo.controller;

/**
* <p>Title: PosizioneGiuridicaCumuloController</p>
* <p>Description: Classe Controller per PosizioneGiuridicaCumulo</p>
 *
 * @author Intersistemi S.p.A.
 *
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;

public interface IPosizioneGiuridicaCumulo {

	public PosizioneGiuridicaCumuloModel ExInserisciPosizioneGiuridicaCumulo(
			PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo) throws F3BException;

	public Vector<PosizioneGiuridicaCumuloModel> ExRicercaPosizioneGiuridicaCumulo(
			PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo) throws F3BException;

	public Vector<PosizioneGiuridicaCumuloModel> ExRicercaPosizioneGiuridicaCumulobyIdTitCum(
			BigDecimal aIdTitoloCumulato) throws F3BException;

	public void ExModificaPosizioneGiuridicaCumulo(PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo)
			throws F3BException;

	public void ExCancellaPosizioneGiuridicaCumuloBykey(BigDecimal aIdPosizioneGiuridicaCumulo)
			throws F3BException;

	public BigDecimal ExGetCountPosizioneGiuridicaCumulo(
			PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo) throws F3BException;

	public PosizioneGiuridicaCumuloModel ExRicercaPosizioneGiuridicaCumuloById(
			BigDecimal aIdPosizioneGiuridicaCumulo) throws F3BException;

	public Vector<PosizioneGiuridicaCumuloModel> ExRicercaPosizioneGiuridicaCumuloPaged(
			PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo, int aPage) throws F3BException;

	public Vector<PosizioneGiuridicaCumuloModel> ExRicercaPosizioneGiuridicaCumuloByIdIstruttoria(
			BigDecimal aIdIstruttoria, BigDecimal aIdDatiFinali) throws F3BException;

	public String ExInserisciPosizioneGiuridicaCumuloWithoutSequence(
			Vector<PosizioneGiuridicaCumuloModel> VecPosizioneGiuridicaCum, Connection lConn)
			throws F3BException;

	public Vector<PosizioneGiuridicaCumuloModel> ExRicercaPosizioniGiuridicheTitoliByIdIstruttoria(
			BigDecimal aIdIstruttoria) throws F3BException;

}