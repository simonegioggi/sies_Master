package siap.jms;

/**
 * <p>
 * Title: MainTestSend
 * </p>
 * <p>
 * Description: Classe di Test per la spedizione Batch dei messaggi
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class MainTestSend implements ICostantiJMS {

	public MainTestSend() {
	}

	/**
	 * Main method.
	 *
	 * @param args
	 *            the topic used by the example
	 */
//	public static void main(String[] args) {
//		try {
//			/*
//			 * LOCAL_MITTENTE for (int x = 0; x < 10; x++) { TestSendThread lSend = new TestSendThread();
//			 * lSend.start(); }
//			 */
//
//			XModel lX = new XModel();
//			lX.setUfficio("ROMA");
//			TreeModel aTreeRoot = new TreeModel(lX);
//
//			SoggettoModel lSog = new SoggettoModel();
//			lSog.setNome("Nelluccio");
//			lSog.setCognome("Mio");
//			FascicoloSiepModel lFasc = new FascicoloSiepModel();
//			lFasc.setChiaveUfficio("2003");
//			lFasc.setAnnoFascicoloUnione("2000");
//			aTreeRoot.add(new TreeModel(lSog));
//			aTreeRoot.add(new TreeModel(lFasc));
//
//			SentenzaModel lSent = new SentenzaModel();
//			lSent.setCodLuogoEmittente("ROMA");
//			lSent.setDescrAltreSentenze("Sentenze bellissimissime!!!");
//			aTreeRoot.add(new TreeModel(lSent));
//
//			EventoNotificaModel lEve = new EventoNotificaModel();
//			lEve.getMagistrato().setCognome("PONZIOLO");
//			lEve.getMagistrato().setNome("MARICIOLO");
//			aTreeRoot.add(new TreeModel(lEve));
//
//			MessaggioModel lMessage = new MessaggioModel();
//			lMessage.setTreeModel(aTreeRoot);
//
//			lMessage.setCodBdiDestinataria(TORINO);
//			lMessage.setCodBdiMittente(LOCAL_MITTENTE);
//			lMessage.setCodTipoMessaggio(ESITO);
//
//			SIAPSender lSend = new SIAPSender();
//
//			// for (int i = 0; i < 10; i++)
//			lSend.send(lMessage);
//
//			lMessage.setCodTipoMessaggio(RICHIESTA);
//			lSend.send(lMessage);
//		} catch (Exception e) {
//		}
//	}

}