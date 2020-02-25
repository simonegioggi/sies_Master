package siap.siep.istitutodetenzione.controller;

import java.util.Vector;

import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: IstitutoDetenzioneController
 * </p>
 * <p>
 * Description: Classe Controller per IstitutoDetenzione
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
public interface IIstitutoDetenzione {

	public Vector ExRicercaIstitutoDetenzione(IstitutoDetenzioneModel aIstitutoDetenzione)
			throws F3BException;

	public IstitutoDetenzioneModel ExRicercaIstitutoDetenzioneByKey(String aKey) throws F3BException;

	public Vector ExRicercaIstitutoDetenzionePerDistretto(IstitutoDetenzioneModel aIstitutoDetenzione)
			throws F3BException;

	public Vector ListaIstitutoDetenzione() throws F3BException;

}