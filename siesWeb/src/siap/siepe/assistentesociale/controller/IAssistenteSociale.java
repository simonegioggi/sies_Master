package siap.siepe.assistentesociale.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siepe.assistentesociale.model.AssistenteSocialeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: AssistenteSocialeController
 * </p>
 * <p>
 * Description: Classe Controller per l'Assistente Sociale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IAssistenteSociale {

	public AssistenteSocialeModel ExInserisciAssistenteSociale(AssistenteSocialeModel aAssistenteSociale)
			throws F3BException;

	public Vector ExRicercaAssistenteSociale(AssistenteSocialeModel aAssistenteSociale) throws F3BException;

	public AssistenteSocialeModel ExRicercaAssistenteSocialeByKey(BigDecimal aKey) throws F3BException;

	public AssistenteSocialeModel ExModificaAssistenteSociale(AssistenteSocialeModel aAssistenteSociale)
			throws F3BException;

	public void ExCancellaAssistenteSociale(BigDecimal IdAssistenteSociale) throws F3BException;

	public Vector ExElencoCbxAssistentiSocialiByCodUfficio(String aCodUfficio) throws F3BException;

	public Vector ExRicercaAssistenteSocialeByCodUfficio(String aCodUfficio) throws F3BException;

}