package siap.siep.posizione.controller;

/**
* <p>Title: PosizioneGiuridicaController</p>
* <p>Description: Classe Controller per PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Vector;

import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IPosizioneGiuridica {

	// Funzioni di inserimento
	public PosizioneGiuridicaModel ExInserisciPosizioneGiuridica(PosizioneGiuridicaModel aPosizioneGiuridica)
			throws F3BException;

	public PosizioneGiuridicaModel ExInserisciPosizioneGiuridicaModificaFascicoloSiepAssociato(
			PosizioneGiuridicaModel aPosizioneGiuridica, FascicoloSiepModel aFascicoloModel)
			throws F3BException;

	public PosizioneGiuridicaModel ExInserisciPosizioneLuogoDetenzioneAltraCausaModificaFascicolo(
			PosizioneGiuridicaModel aPosizioneGiuridica, FascicoloSiepModel aFascicoloModel,
			LuogoDetenzioneModel aLuogoDetenzione, AltraCausaModel aAltraCausa) throws F3BException;

	public PosizioneGiuridicaModel ExInserisciPosizioneLuogoDetenzioneAltraCausaModificaFascicolo(
			PosizioneGiuridicaModel aPosizioneGiuridica, FascicoloSiepModel aFascicoloModel,
			LuogoDetenzioneModel aLuogoDetenzione, AltraCausaModel aAltraCausa,
			MisuraCautelareModel aMisuraCautelare) throws F3BException;

	// Funzioni di modifica
	public PosizioneGiuridicaModel ExModificaPosizioneGiuridica(PosizioneGiuridicaModel aPosizioneGiuridica)
			throws F3BException;

	// Funzioni di modifica
	public PosizioneGiuridicaModel ExModificaPosizioneGiuridicaIdPosGiu(
			PosizioneGiuridicaModel aPosizioneGiuridica) throws F3BException;

	public PosizioneGiuridicaModel ExModificaPosizioneGiuridicaModificaFascicoloSiepAssociato(
			PosizioneGiuridicaModel aPosizioneGiuridica, FascicoloSiepModel aFascicoloModel)
			throws F3BException;

	public PosizioneGiuridicaModel ExModificaPosizioneGiuridicaModificaFascicoloSiepAssociatoLuogoDetenzione(
			PosizioneGiuridicaModel aPosizioneGiuridica, FascicoloSiepModel aFascicoloModel,
			LuogoDetenzioneModel aLuogoDetenzione) throws F3BException;

	// Funzioni di ricerca
	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
			BigDecimal aIdFascicolo) throws F3BException;

	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaMisuraCautelareCorrentiByIdFascicolo(
			BigDecimal aIdFascicolo) throws F3BException;

	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneLuogoDetAltraCausaByIdFascicoloDataFineNull(
			BigDecimal aIdFascicolo) throws F3BException;

	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByKey(
			BigDecimal aIdPosizione) throws F3BException;

	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByIdEvento(
			BigDecimal aIdEvento) throws F3BException;

	public Vector ExRicercaPosizioneGiuridica(PosizioneGiuridicaModel aPosizioneGiuridica)
			throws F3BException;

	public PosizioneGiuridicaModel ExRicercaPosizioneGiuridicaCorrente(
			PosizioneGiuridicaModel aPosizioneGiuridica) throws F3BException;

	public PosizioneGiuridicaModel ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(BigDecimal aIdFascicolo)
			throws F3BException;

	public PosizioneGiuridicaModel ExRicercaPosizioneGiuridicaCorrenteByIdFascicoloDataFineNull(
			BigDecimal aIdFascicolo) throws F3BException;

	public PosizioneGiuridicaModel ExRicercaPosizioneGiuridicaByKey(BigDecimal aKey) throws F3BException;

	public List ExRicercaPosizioneGiuridicaByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException;

	public List ExRicercaPosizioneGiuridicaByIdFascicoloNoError(BigDecimal aIdFascicolo) throws F3BException;

	public PosizioneGiuridicaLuogoDetenzioneModel ExRicercaPosizioneGiuridicaLuogoDetenzioneByKey(
			BigDecimal aKey) throws F3BException;

	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicoloSius(
			BigDecimal aIdFascicolo, BigDecimal aIdFascicoloSius) throws F3BException;

	public PosizioneGiuridicaModel ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(BigDecimal aKey)
			throws F3BException;

	public PosizioneGiuridicaModel ExInserisciPosizioneGiuridicaVerbaleSotto(PosizioneGiuridicaModel aPosMod,
			BigDecimal aKeyFasc) throws F3BException;

	// Funzioni di cancellazione
	public void ExCancellaPosizioneGiuridica(PosizioneGiuridicaModel aPosizioneGiuridica) throws F3BException;

	public String ExInserisciPosizioneGiuridicaWithoutSequence(PosizioneGiuridicaModel lPos, Connection lConn)
			throws F3BException;

	public PosizioneGiuridicaModel ExRicercaPosizioneGiuridicaByIdEvento(BigDecimal aIdEvento)
			throws F3BException;

	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo2(
			BigDecimal aIdFascicolo) throws F3BException;

}