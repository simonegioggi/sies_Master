package siap.jms.jmscode.controller;

import java.util.Vector;

import siap.jms.jmscode.model.JmsCodeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: JmsCodeController
 * </p>
 * <p>
 * Description: Classe Controller per JmsCode
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
public interface IJmsCode {

	public Vector ExRicercaAllBDI() throws F3BException;

	public JmsCodeModel ExRicercaJmsCodeByKey(String aDominio, String aCodice) throws F3BException;

	public Vector ExRicercaPerDominio(String aDominio) throws F3BException;

	public Vector ExRicercaPerDominioEDescrizione(String aDominio, String aDescrizione) throws F3BException;

	public JmsCodeModel ExRicercaBDICodeByCodUff(String aCode) throws F3BException;

	public Vector<JmsCodeModel> ExRicercaJmsCode(JmsCodeModel jmsModel) throws F3BException;

}