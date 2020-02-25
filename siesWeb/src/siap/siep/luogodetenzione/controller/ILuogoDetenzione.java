package siap.siep.luogodetenzione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: LuogoDetenzioneController
 * </p>
 * <p>
 * Description: Classe Controller per LuogoDetenzione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface ILuogoDetenzione {

	public LuogoDetenzioneModel ExInserisciLuogoDetenzione(LuogoDetenzioneModel aLuogoDetenzione)
			throws F3BException;

	public Vector ExRicercaLuogoDetenzione(LuogoDetenzioneModel aLuogoDetenzione) throws F3BException;

	public LuogoDetenzioneModel ExRicercaLuogoDetenzioneByKey(BigDecimal aKey) throws F3BException;

	public LuogoDetenzioneModel ExModificaLuogoDetenzione(LuogoDetenzioneModel aLuogoDetenzione)
			throws F3BException;

	public void ExModificaDataFineLuogoDetenzione(LuogoDetenzioneModel aLuogoDetenzione) throws F3BException;

	public void ExCancellaLuogoDetenzione(LuogoDetenzioneModel aLuogoDetenzione) throws F3BException;

	public LuogoDetenzioneModel ExRicercaLuogoDetByFascicolo(BigDecimal aKey) throws F3BException;

	public LuogoDetenzioneModel ExRicercaLuogoDetenzioneCorrenteByFascicoloSiep(BigDecimal aKey)
			throws F3BException;

	public LuogoDetenzioneModel ExRicercaLuogoDetenzioneCorrenteByFascicoloSius(BigDecimal aKey)
			throws F3BException;

	public LuogoDetenzioneModel ExInserisciLuogoDetenzioneSius(LuogoDetenzioneModel aLuogoDetenzioneOld,
			LuogoDetenzioneModel aLuogoDetenzione, AltraCausaModel aAltraCausa) throws F3BException;

	public Vector ExElencoDatiLuogoDetenzioneSius(BigDecimal aKey) throws F3BException;

	public String ExInserisciLuogoDetenzioneWithoutSequence(LuogoDetenzioneModel lPars, Connection lConn)
			throws F3BException;

}