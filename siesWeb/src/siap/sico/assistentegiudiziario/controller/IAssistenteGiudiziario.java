package siap.sico.assistentegiudiziario.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: AssistenteGiudiziarioController
 * </p>
 * <p>
 * Description: Classe Controller per AssistenteGiudiziario
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
public interface IAssistenteGiudiziario {

	public AssistenteGiudiziarioModel ExInserisciAssistenteGiudiziario(
			AssistenteGiudiziarioModel aAssistenteGiudiziario) throws F3BException;

	public Vector ExRicercaAssistenteGiudiziario(AssistenteGiudiziarioModel aAssistenteGiudiziario)
			throws F3BException;

	public AssistenteGiudiziarioModel ExRicercaAssistenteGiudiziarioByKey(BigDecimal aKey)
			throws F3BException;

	public AssistenteGiudiziarioModel ExModificaAssistenteGiudiziario(
			AssistenteGiudiziarioModel aAssistenteGiudiziario) throws F3BException;

	public void ExCancellaAssistenteGiudiziario(AssistenteGiudiziarioModel aAssistenteGiudiziario)
			throws F3BException;

	public Vector ExElencoCbxAssistenteGiudiziarioByCodUfficio(String aCodUfficio) throws F3BException;

}