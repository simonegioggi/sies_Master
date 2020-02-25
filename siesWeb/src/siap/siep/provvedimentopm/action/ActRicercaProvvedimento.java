package siap.siep.provvedimentopm.action;

/**
* <p>Title: ActRicercaProvvedimento</p>
* <p>Description: Classe Action per la ricerca di Provvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import f3b.util.F3BException;
//import siap.siep.provvedimentopm.controller.ProvvedimentoController;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.provvedimentopm.controller.IProvvedimento;
import siap.siep.provvedimentopm.model.ProvvedimentoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaProvvedimento extends ActionSiap implements ICostantiProvvedimento {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		ProvvedimentoModel lProMod = new ProvvedimentoModel();

		if (isSessionAttributeNullObj("fascicolo"))
			throw new SIEPException(SIEPException.USER_MESSAGE, "Selezionare il fascicolo.");

		/*
		 * lProMod.setIdProvvedimento( getRequestBigDecimalParameter( CAMPO_ID_PROVVEDIMENTO) );
		 * lProMod.setCodTipo( getRequestStringParameter( CAMPO_COD_TIPO) ); lProMod.setCodMotivo(
		 * getRequestStringParameter( CAMPO_COD_MOTIVO) );
		 * 
		 * //lProMod.setData( getRequestDateParameter( CAMPO_GIORNO_DATA,CAMPO_MESE_DATA,CAMPO_ANNO_DATA) );
		 * lProMod.setData( getRequestDateParameter( CAMPO_ANNO_DATA, CAMPO_MESE_DATA, CAMPO_GIORNO_DATA) );
		 * 
		 * lProMod.setCodEsito( getRequestStringParameter( CAMPO_COD_ESITO) ); lProMod.setFlagPiuMeno(
		 * getRequestStringParameter( CAMPO_FLAG_PIU_MENO) ); lProMod.setDataTrasmissioneAtti(
		 * getRequestDateParameter( CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI,CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
		 * CAMPO_ANNO_DATA_TRASMISSIONE_ATTI) ); lProMod.setDataScadenza( getRequestDateParameter(
		 * CAMPO_GIORNO_DATA_SCADENZA,CAMPO_MESE_DATA_SCADENZA,CAMPO_ANNO_DATA_SCADENZA) );
		 * lProMod.setAnnoProtocollo( getRequestBigDecimalParameter( CAMPO_ANNO_PROTOCOLLO) );
		 * lProMod.setProgrProtocollo( getRequestBigDecimalParameter( CAMPO_PROGR_PROTOCOLLO) );
		 * lProMod.setCodOperatoreInserimento( getRequestStringParameter( CAMPO_COD_OPERATORE_INSERIMENTO) );
		 * lProMod.setDataInserimento( getRequestDateParameter(
		 * CAMPO_GIORNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_ANNO_DATA_INSERIMENTO) );
		 * lProMod.setCodOperatoreAggiornamento( getRequestStringParameter( CAMPO_COD_OPERATORE_AGGIORNAMENTO)
		 * ); lProMod.setDataAggiornamento( getRequestDateParameter(
		 * CAMPO_GIORNO_DATA_AGGIORNAMENTO,CAMPO_MESE_DATA_AGGIORNAMENTO,CAMPO_ANNO_DATA_AGGIORNAMENTO) );
		 */
		lProMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		// lProMod.setMagCodMagistrato( getRequestStringParameter( CAMPO_MAG_COD_MAGISTRATO) );

		// ProvvedimentoController lCtrl = new ProvvedimentoController();
		IProvvedimento lCtrl = SIEPLookupRemote.getProvvedimentoRemote();

		Vector lVect = lCtrl.ExRicercaProvvedimento(lProMod);
		setRequestAttribute("provvedimenti", lVect);

		return PG_RICERCAPROVVEDIMENTO;
	}

}