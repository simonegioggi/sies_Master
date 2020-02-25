package siap.siep.continuazione.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.continuazione.model.ContinuazioneModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ContinuazioneController
 * </p>
 * <p>
 * Description: Classe Controller per Continuazione
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
public interface IContinuazione {

	public ContinuazioneModel ExInserisciContinuazione(ContinuazioneModel aContinuazione) throws F3BException;

	public Vector ExRicercaContinuazione(ContinuazioneModel aContinuazione) throws F3BException;

	public Vector ExRicercaContinuazioneByIDPenaComplessiva(BigDecimal aKey) throws F3BException;

	public ContinuazioneModel ExRicercaContinuazioneByKey(BigDecimal aKey) throws F3BException;

	public ContinuazioneModel ExModificaContinuazione(ContinuazioneModel aContinuazione) throws F3BException;

	public void ExCancellaContinuazione(ContinuazioneModel aContinuazione) throws F3BException;

}