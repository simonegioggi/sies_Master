package siap.siep.modulocumulo.action;

import java.util.Vector;

import f3b.web.IWebConstants;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load Elenco dei Titoli possibili oggetto della Richieste al GE di Sostituzione Pena
 * Accessoria / Richiesta Applicazione Pena Accessoria
 * 
 * @author Intersistemi Italia S.p.A.
 * 
 */

public class ActLoadElencoTitoliRichGESostituzionePenaAccCum extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	public String processRequest() throws Exception {
		IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();

		if (!ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstruttoriaModel.getFlagStato())) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"L'istruttoria risulta chiusa. Non è possibile procedere all'emissione di ulteriori richieste");
			return IWebConstants.PG_MESSAGE;
		}

		Vector<TitoloCumulatoModel> lListaTitoli = null;

		String lOrdinamento = lIstruttoriaModel.getOrdinamentoTitoli();
		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();

		// La ricerca trova Titolo_Cumulato in join con Pena_Accessoria_Cumulo e, in un secondo momento, il
		// relativo Procedimento_Cumulato Aggregato
		// In questa fase il terzo parametro è settato a NULL
		lListaTitoli = new Vector<>(
				lIstrCtrl.ExRicercaTitoliPenaAccCumByIstruttoriaOrderBy(
						lIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento, null));
		setRequestAttribute("ListaTitoli", lListaTitoli);

		return PG_ELENCO_TITOLI_RICH_GE_SOST_PENA_ACC_CUM;
	}
} // Chiude Classe
