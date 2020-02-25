package siap.siep.modulocumulo.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IPosizioneGiuridicaCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaRidetPenaPMAltroCumulo extends ActionModuloCumulo implements ICostantiModuloCumulo {

	// Si Dichiara un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		TitoloCumulatoModel lTitoloCumulato = super.getDatiTitoloCumulato();

		// ==========================================================================
		// Effettuo la ricerca delle Posizioni Giuridiche collegate al Titolo
		// ==========================================================================
		IPosizioneGiuridicaCumulo lCtrlPosGiuridicaCumulo = SIEPLookupRemote
				.getPosizioneGiuridicaCumuloRemote();
		Vector<PosizioneGiuridicaCumuloModel> lListaEspiati = lCtrlPosGiuridicaCumulo
				.ExRicercaPosizioneGiuridicaCumulobyIdTitCum(lTitoloCumulato.getIdTitoloCumulato());

		setRequestAttribute("ListaEspiati", lListaEspiati);

		// ==========================================================================
		// Effettuo la ricerca delle Espiazioni Pregresse collegate al Titolo
		// ==========================================================================
		Vector<StatoEsecTitoloCumulatoModel> lListaRidetPena = new Vector<>();
		IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

		Vector<String> listaTipoProvv = new Vector<>();

		listaTipoProvv.add("25"); // Annotazione x 0270 estratti

		Vector<String> listaProvv = new Vector<>();

		// Provvedimenti d'ufficio
		listaProvv.add("0948"); // Computo pena ai fini estradizionali
		listaProvv.add("0951"); // Correzione errore materiale
		listaProvv.add("0950"); // Rettifica provvedimento determinazione pene concorrenti
		listaProvv.add("0949"); // Riconoscimento carcerazione sofferta ai fini estradizionali
		listaProvv.add("1000"); // Rideterminazione Pena

		// Provvedimenti d'Altra Autorità
		listaProvv.add("0953"); // Correzione errore materiale
		listaProvv.add("0955"); // Effetto Estensivo
		listaProvv.add("1005"); // Rideterminazione Pena

		// Provvedimenti del GE
		listaProvv.add("0959"); // Rideterminazione della pena a seguito di ordinanza ex art. 669 c.p.p.
		listaProvv.add("0956"); // Rideterminazione della pena a seguito di ordinanza ex. art. 671 c.p.p. e
								// art 81 c.p.
		listaProvv.add("0987"); // Rideterminazione della pena per estinzione delle pene della reclusione e
								// della multa per decorso del tempo ex art. 172 cp
		listaProvv.add("0988"); // Rideterminazione della pena per estinzione delle pene dell'arresto e
								// dell'ammenda per decorso del tempo ex art. 173 cp
		listaProvv.add("1006"); // Rideterminazione Pena

		// Provvedimenti della Sorveglianza
		listaProvv.add("0954"); // Rideterminazione della pena a seguito di conversione da sanzione
								// sostitutiva
		listaProvv.add("0958"); // Rideterminazione della pena a seguito di scomputo di permesso
		listaProvv.add("0957"); // Rideterminazione della pena per ridimensionamento beneficio Liberazione
								// Anticipata
		listaProvv.add("0994"); // Rideterminazione fine pena a seguito accoglimento reclamo scomputo permesso
		listaProvv.add("0999"); // Rideterminazione Pena

		// La ricerca dei provvedimenti per idTitolo e TipoProvvedimento richiede, nell'ordine, i parametri:
		// idTitolo, CodTipoEvento, CodTipoProvvedimento, CodMotivo.
		lListaRidetPena = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(
				lTitoloCumulato.getIdTitoloCumulato(), "01", listaTipoProvv, listaProvv);
		siesLogger.debug("lListaRidetPena.size() = " + lListaRidetPena.size());

		// Passo alla form i dati trovati
		setRequestAttribute("ListaRidetPena", lListaRidetPena);

		return PG_LOAD_ELENCO_RIDETPENA_ALTRO;
	}

}