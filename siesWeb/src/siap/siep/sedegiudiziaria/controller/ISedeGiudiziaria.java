package siap.siep.sedegiudiziaria.controller;

import java.util.Vector;

import siap.siep.sedegiudiziaria.model.SedeGiudiziariaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: SedeGiudiziariaController
 * </p>
 * <p>
 * Description: Classe Controller per SedeGiudiziaria
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
public interface ISedeGiudiziaria {

	public Vector ExRicercaSedeGiudiziaria(SedeGiudiziariaModel aSedeGiudiziaria) throws F3BException;

	public SedeGiudiziariaModel ExRicercaSedeGiudiziariaByKey(String aKey) throws F3BException;

	public SedeGiudiziariaModel ExRicercaSedeGiudiziariaByDescrizione(String aDescrizione)
			throws F3BException;

}