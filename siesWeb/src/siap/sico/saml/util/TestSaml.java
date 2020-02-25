package siap.sico.saml.util;


public class TestSaml {

	// // NUOVA INFRASTRUTTURA: aggiunte variabili di classe
	// private SIAPPathProperties mPathProperties = SIAPPathProperties.getInstance();
	// private String mPath = null;
//	private static List<File> fileList;
//	int count = 0;
//	private static final String ext = ".RTF";
//	private static final String finalExt = ".rtf";
//	private static int occorrenze = 0;

	public static void main(String[] args) {

		// NUOVA INFRASTRUTTURA: cambiato il path per differenziare macchina windows da macchina UNIX
		// mPath = mPathProperties.getProperty("CONFIG");
		// System.setProperty("path.properties", mPath);
		// SamlModel lModel = new SamlModel("0011223344", "PM", "A1234", "10.0.20.30", "Rossi", "Mario",
		// "Torino", "SIEP");
		// SamlMaker lmaker = new SamlMaker();
		// String lSamkCriptata = lmaker.createSamlAssertion(lModel);
		// System.out.println("* * * * * * * * * * * * * * * * * * * * * ");
		// System.out.println(lSamkCriptata);
		// System.out.println("* * * * * * * * * * * * * * * * * * * * * ");

		// NUOVA INFRASTRUTTURA:
		// 1) trasformare da
		// c:\\template\\import\\variabili.rtf
		// a
		// /var/SIES/template/import/variabili.rtf
		// IN PRATICA:
		// prima sostituire: c:\\ --> /var/SIES/ [--> 0723 hits]
		// poi: \\ --> / [--> 1465 hits]
		// 2) sostituire .RTF (case sensitive) con .rtf [--> 0558 hits]
		// 3) sostituire variabili.rtf (case sensitive) con Variabili.rtf [--> 0540 hits]
		// 4) sostituire variabili (case sensitive) con Variabili [--> 0015 hits]
		// 5) aggiunta funzione ricorsiva per rinominare
		// le estensioni dei files nei template [--> 0645 hits]
//		try {
//			String path = "C:\\Users\\Gioggi\\Desktop\\MergeMevs\\AMBIENTE\\template";
//			File dir = new File(path);
//			System.out.println(dir.getPath());
//			String[] children = dir.list();
//			System.out.println("Nella directory ci sono: " + children.length + " oggetti!!!");
//			if (children == null || children.length == 0) {
//				System.out.println("Directory VUOTA!!!");
//			} else {
//				fileList = new ArrayList<File>();
//				searchFile(dir, ext);
//			}
//			System.out.println("In tutte le directories esistono " + count
//					+ " files con l'estensione richiesta!");
//			if (count > 0)
//				cambiaEstensioneFile(fileList);
//			System.out.println("### FINE DELLE OPERAZIONI ###");
//		} catch (Exception ex) {
//			System.out.println("ERRORE: " + ex.getMessage());
//			ex.printStackTrace();
//		}

		// CANCELLAZIONE RIGA DA FILE
//		System.out.println("########## START OF THE GAME! ##########");
//		lista(new File("D:\\DATA_RECOVERY\\Progetti\\SIES-MEV_39\\SiesWeb\\src"));
//		System.out.println("NUMERO DI OCCORRENZE FINALE: " + occorrenze);
//		System.out.println("########## END OF THE GAME! ##########");

		/* Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:siesto/siesto@10.5.207.201:1521:sies");
			String query = "select u.cognome || ' ' || u.nome as persona from utente u where u.cod_utente = 'A16641'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs.next())
				System.out.println(rs.getString("persona"));
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		} */

	}

	/**
	 * Metodo per il rename dell'estensione del file
	 * 
	 * @param fl
	 */
//	private static void cambiaEstensioneFile(List<File> fl) {
//
//		System.out.println("Cambio estensione a " + fl.size() + " files!!!");
//		for (int l = 0; l < fl.size(); l++) {
//			String pathName = fl.get(l).getName();
//			String path = fl.get(l).getPath();
//			int index = path.lastIndexOf("\\");
//			String subPath = path.substring(0, index + 1);
//			System.out.println("Manipolo il file: " + pathName + " nel percorso: " + subPath);
//			pathName = pathName.replace(ext, finalExt);
//			File newfile = new File(subPath + pathName);
//			fl.get(l).renameTo(newfile);
//			System.out.println("Nuovo nome del file: " + newfile.getName());
//		}
//	}

	/**
	 * Metodo ricorsivo che cerca i files nelle sub directories
	 * 
	 * @param pathFile
	 * @param estensione
	 */
//	private static void searchFile(File pathFile, String estensione) {
//
//		File listFile[] = pathFile.listFiles();
//		System.out.println("Nella sotto directory ci sono: " + listFile.length + " oggetti!!!");
//		if (listFile != null) {
//			for (int i = 0; i < listFile.length; i++) {
//				String filename = listFile[i].getName();
//				System.out.println(filename);
//				if (listFile[i].isDirectory()) {
//					searchFile(listFile[i], estensione);
//				} else {
//					if (estensione != null) {
//						if (listFile[i].getName().endsWith(estensione)) {
//							fileList.add(listFile[i]);
//							count++;
//							System.out.println("File di Import selezionato: [ " + listFile[i].getPath()
//									+ " ]");
//						}
//					}
//					// else {
//					// fileList.add(listFile[i].getPath());
//					// System.out.println("File di Import selezionato: [ " + listFile[i].getPath() + " ]");
//					// }
//				}
//			}
//		}
//	}

//	private static void lista(File partenza) {
//		
//		File[] list = partenza.listFiles();
//		int MAX = list.length;
//		for (int i = 0; i < MAX; i++) {
//			if (list[i].isDirectory())
//				lista(list[i]);
//			else {
//				if (list[i].getName().endsWith(".java")) {
//					System.out.println("OK! " + list[i].getName());
//					if (!"TestSaml.java".equals(list[i].getName()))
//						elaboraFile(list[i].getPath());
//				} else
//					System.out.println("KO! Non è un file .java!");
//			}
//		}
//	}

//	private static void elaboraFile(String path) {
//	
//		String toFind = "System.out.println(";
//		FileInputStream fstream = null;
//		DataInputStream in = null;
//		BufferedWriter out = null;
//	
//		// contatore
//		int cont = 0;
//		BufferedReader brc = null;
//		boolean elabora = false;
//		String s;
//		try {
//			FileReader reader = new FileReader(path);
//			brc = new BufferedReader(reader);
//			while ((s = brc.readLine()) != null) {
//				cont++;
//				s = s.trim();
//				if (s.contains("\t"))
//					s = s.replaceAll("\t", "");
//				if (s.startsWith(toFind) && s.endsWith(";")) {
//					String[] st = s.split(";");
//					if (st.length == 1)
//						elabora = true;
//				} else if (s.startsWith("//") && s.contains(toFind) && s.endsWith(";"))
//						elabora = true;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				brc.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	
//		if (elabora) {
//			int i = 0;
//			try {
//				// apro il file
//				fstream = new FileInputStream(path);
//				// prendo l'inputStream
//				in = new DataInputStream(fstream);
//				BufferedReader br = new BufferedReader(new InputStreamReader(in));
//				String strLine;
//				StringBuilder fileContent = new StringBuilder();
//				// Leggo il file riga per riga
//				while ((strLine = br.readLine()) != null) {
//					i++;
//					String copy = strLine;
//					copy = copy.trim();
//					if (copy.contains("\t"))
//						copy = copy.replaceAll("\t", "");
//					String[] st = copy.split(";");
//					if (copy.startsWith(toFind) && copy.endsWith(";") && st.length == 1) {
//						// se la riga è uguale a quella ricercata la tolgo
//						System.out.println("TROVATA RIGA DI INTERESSE: " + copy);
//						occorrenze++;
//					} else if (copy.startsWith("//") && copy.contains(toFind) && copy.endsWith(";")) {
//						// se la riga è uguale a quella ricercata la tolgo
//						System.out.println("TROVATA RIGA COMMENTATA DI INTERESSE: " + copy);
//						occorrenze++;
//					} else {
//						// altrimenti la riscrivo
////						System.out.println("TROVATA RIGA NON DI INTERESSE: " + copy);
//						fileContent.append(strLine);
//						if (i != cont)
//							fileContent.append(System.getProperty("line.separator"));
//					}
//				}
//				// Sovrascrivo il file con il nuovo contenuto (aggiornato)
//				FileWriter fstreamWrite = new FileWriter(path);
//				out = new BufferedWriter(fstreamWrite);
//				out.write(fileContent.toString());
//			} catch (Exception e) {
//				System.out.println("ERRORE: " + e.getMessage());
//				e.printStackTrace();
//			} finally {
//				// chiusura dell'output e dell'input
//				try {
//					fstream.close();
//					out.flush();
//					out.close();
//					in.close();
//				} catch (IOException e) {
//					System.out.println("ERRORE: " + e.getMessage());
//					e.printStackTrace();
//				}
//			}
//		}
//	}

}