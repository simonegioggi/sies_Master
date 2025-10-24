package siap.sico.template.controller;

import java.util.Vector;

import siap.sico.template.model.TemplateModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: TemplateController
 * </p>
 * <p>
 * Description: Classe Controller per Template
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
public interface ITemplate {

	public Vector ExRicercaTemplate(TemplateModel aTemplate) throws F3BException;

	public TemplateModel ExRicercaTemplateByKey(String aKey) throws F3BException;

	public Vector ExRicercaAllTemplate() throws F3BException;

	public TemplateModel ExRicercaTemplateByCodMotivo(String aKey) throws F3BException;

	public TemplateModel ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(String aTipoEvento,
			String aTipoProv, String aCodMotivo, String aFlagTemplate) throws F3BException;

	public Vector ExListaCbxTemplate(TemplateModel aTemplate) throws F3BException;

}