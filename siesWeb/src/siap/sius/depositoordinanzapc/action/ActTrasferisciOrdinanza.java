package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * <p>Title: ActTrasferisciIstanza </p>
 * <p>Description: Trasferisce l'Istanza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActTrasferisciOrdinanza extends ActionSiap implements ICostantiDepositoOrdinanzaPc
{
	public String processRequest() throws Exception
  {
		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		String lTipoUff = getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
		String lSedeUff = getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO);

		String lCodiceUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lSedeUff);

		UfficioModel lLocal = this.getUfficioByCodUfficio(lCodiceUfficio);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrl.ExRicercaEventoByKey(lEveId);

		setRequestAttribute("evento", lEveMod);
		setRequestAttribute("UfficioDestinatario", lLocal);

    return PG_DETTAGLIO_TRASFERISCI_ORDINANZA;
  }
}