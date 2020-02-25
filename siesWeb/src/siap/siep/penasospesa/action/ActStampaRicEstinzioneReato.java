package siap.siep.penasospesa.action;


import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.richiesta.action.ActStampaRichiestaGenerica;
import f3b.util.F3BException;

/**
 * <p>Title: ActStampaRicEstinzioneReato</p>
 * <p>Description: Produce il documento</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: Agile</p>
 * <p> @author Luigi</p>
 * @version 1.0
 */

public class ActStampaRicEstinzioneReato extends ActStampaRichiestaGenerica
{
  
	public String processRequest() throws F3BException
	{
		//==================================
		// Recupero l'evento  
		//==================================
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEve = null;
		lEve = lCtrlEvento.ExRicercaEventoByKey(getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO ));
		//========================================
		// Recupero il template
		//========================================
		String  flagTemplate ="0";

		TemplateModel lTemMod = new TemplateModel();
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lEve.getCodTipoEvento(), lEve.getCodTipoProvvedimento(), lEve.getCodMotivo(), flagTemplate);
		
		mNomeTemplate = lTemMod.getIdTemplate();
		return super.processRequest();
	}
}