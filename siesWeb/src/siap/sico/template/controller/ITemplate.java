package siap.sico.template.controller;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.template.model.TemplateModel;

/**
 * TemplateController - Classe Controller per Template
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

	// MEV_2024-092: aggiunto metodo di ricerca
	public TemplateModel ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplateCodOgg(String aTipoEvento,
			String aTipoProv, String aCodMotivo, String aFlagTemplate) throws F3BException;

}