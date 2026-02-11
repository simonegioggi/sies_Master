package f3b.util;

import java.util.Vector;

/**
 * F3BCodeGenerator - Classe di utilità che genera le classi DAO e Model dal nome della tabella inserita in un
 * CreateDAO.properties
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes" })
public class F3BCodeGenerator {

	static Vector mNomiCampiDAO = new Vector();
	static Vector mNomiJavaCampiDAO = new Vector();
	static Vector mTipoCampiDAO = new Vector();
	static Vector mTipoJavaCampiDAO = new Vector();
	static Vector mTipoCampiModel = new Vector();
	static Vector mTipoJavaCampiModel = new Vector();
	static String mNomeTabella = null;
	static String mPackage = null;
	static String mPackageRoot = null;

	static String mContext = null;
	static String mKey = null;
	static String mKeyJava = null;
	static String mTypeKey = null;

	static String mDirectory = null;
	static String mDirectoryRoot = null;

	static boolean mForceOutput = false;
	static boolean mLookup = false;

	// public static void main(String args[]) {
	// String lDataSourceDriver = null;
	// String lDataSourceName = null;
	//
	// // Recupera i dati dell'Applicazione da un file Properties
	// try {
	// InputStream lInpStream = ClassLoader.getSystemResourceAsStream("F3BCodeGenerator.properties");
	//
	// if (lInpStream == null)
	// throw new Exception(
	// "Impossibile leggere il file F3BCodeGenerator.properties. Verificare che il file sia esistente! ");
	//
	// Properties lPr = new Properties();
	// lPr.load(lInpStream);
	//
	// // Recupero dal file di properties i parametri.
	// lDataSourceDriver = lPr.getProperty("datasource.driver");
	// if (lDataSourceDriver == null) {
	// return;
	// }
	//
	// lDataSourceName = lPr.getProperty("datasource.name");
	// if (lDataSourceName == null) {
	// return;
	// }
	//
	// mNomeTabella = lPr.getProperty("table.name");
	// if (mNomeTabella == null) {
	// return;
	// }
	//
	// mKey = lPr.getProperty("table.key");
	//
	// if (mKey == null) {
	// return;
	// }
	//
	// mPackage = lPr.getProperty("package.name");
	// mPackageRoot = lPr.getProperty("package.root");
	// mDirectory = lPr.getProperty("source.output.directory");
	// mDirectoryRoot = lPr.getProperty("source.output.root");
	// mContext = lPr.getProperty("source.context");
	//
	// if (lPr.getProperty("source.lookupRemote.exist").compareTo("YES") == 0)
	// mLookup = true;
	//
	// if (lPr.getProperty("source.create").compareTo("YES") == 0) {
	// BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	// String lConfirm;
	// lConfirm = stdIn.readLine();
	// if ((lConfirm != null) && (lConfirm.compareTo("s") != 0)) {
	// stdIn.close();
	// return;
	// }
	//
	// stdIn.close();
	// mForceOutput = true;
	//
	// // Creazione Directory
	// File lDir = new File(mDirectory);
	// if (!lDir.exists())
	// lDir.mkdir();
	//
	// lDir = new File(mDirectory + "\\dao");
	// if (!lDir.exists())
	// lDir.mkdir();
	//
	// lDir = new File(mDirectory + "\\model");
	// if (!lDir.exists())
	// lDir.mkdir();
	//
	// lDir = new File(mDirectory + "\\jsp");
	// if (!lDir.exists())
	// lDir.mkdir();
	//
	// lDir = new File(mDirectory + "\\controller");
	// if (!lDir.exists())
	// lDir.mkdir();
	//
	// lDir = new File(mDirectory + "\\action");
	// if (!lDir.exists())
	// lDir.mkdir();
	//
	// lDir = new File(mDirectoryRoot + "\\util");
	// if (!lDir.exists())
	// lDir.mkdir();
	// }
	//
	// // if (lDataSourceName == null)
	// // throw new Exception();
	//
	// } catch (Exception e) {
	// return;
	// }
	//
	// try {
	// Class.forName(lDataSourceDriver);
	// } catch (Exception e) {
	// return;
	// }
	//
	// Connection lCon = null;
	//
	// try {
	// lCon = DriverManager.getConnection(lDataSourceName);
	// } catch (Exception e) {
	// return;
	// }
	//
	// try {
	// // Riempio i vettori con i valori della Tabella
	// Statement lSelect = lCon.createStatement();
	// ResultSet lResult = lSelect.executeQuery("SELECT * FROM " + mNomeTabella);
	// ResultSetMetaData lMeta = lResult.getMetaData();
	//
	// int lColumns = lMeta.getColumnCount();
	//
	// for (int i = 1; i <= lColumns; i++) {
	// mNomiCampiDAO.insertElementAt(lMeta.getColumnLabel(i), i - 1);
	// mNomiJavaCampiDAO.insertElementAt(getJavaString(lMeta.getColumnLabel(i)), i - 1);
	//
	// if (lMeta.getColumnTypeName(i).startsWith("VARCHAR")) {
	// mTipoCampiDAO.add(i - 1, "STRING");
	// mTipoJavaCampiDAO.add(i - 1, "String");
	//
	// if (lMeta.getColumnLabel(i).compareTo(mKey) == 0)
	// mTypeKey = "String";
	// }
	//
	// if (lMeta.getColumnTypeName(i).startsWith("NUMBER")) {
	// mTipoCampiDAO.add(i - 1, "BIG_DECIMAL");
	// mTipoJavaCampiDAO.add(i - 1, "BigDecimal");
	// if (lMeta.getColumnLabel(i).compareTo(mKey) == 0)
	// mTypeKey = "BigDecimal";
	// }
	//
	// if (lMeta.getColumnTypeName(i).startsWith("DATE")) {
	// mTipoCampiDAO.add(i - 1, "DATE");
	// mTipoJavaCampiDAO.add(i - 1, "Date");
	// }
	//
	// if (lMeta.getColumnTypeName(i).startsWith("BLOB")) {
	// mTipoCampiDAO.add(i - 1, "BLOB");
	// mTipoJavaCampiDAO.add(i - 1, "Blob");
	// }
	// }
	//
	// // Riempio il vector di model che contiene anche le descrizioni
	// // for
	//
	// lSelect.close();
	// lCon.close();
	//
	// mNomeTabella = getJavaString(mNomeTabella);
	// mKeyJava = getJavaString(mKey);
	//
	// writeLookup();
	//
	// // Scrivo il Model
	// writeModel();
	//
	// // Scrivo il DAO
	// writeDAO();
	//
	// // Scrivo l' SQL DAO
	// writeSqlDAO();
	//
	// // Scrivo il Controller
	// writeController();
	//
	// // Scrivo l' Action
	// writeAction();
	//
	// // Scrivo la JSp
	// writeJsp();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * Scrittura FILE Lookup JAVA
	 */
	// private static void writeLookup() {
	//
	// try {
	// String lNomeFile = mDirectoryRoot;
	// FileWriter lFile;
	//
	// if (mForceOutput)
	// lNomeFile = mDirectoryRoot + "\\util\\";
	//
	// if (mLookup)// append
	// lFile = new FileWriter(lNomeFile + mContext + "LookupRemote.java", true);
	// else
	// lFile = new FileWriter(lNomeFile + mContext + "LookupRemote.java");
	//
	// PrintWriter lFileLookup = new PrintWriter(new BufferedWriter(lFile));
	//
	// if (!mLookup) {
	//
	// lFileLookup.write("package " + mPackageRoot + ".util;\n\n");
	//
	// // Write javadoc
	// lFileLookup.write("/**\n");
	// lFileLookup.write("* <p>Title: " + mContext + "LookupRemote</p>\n");
	// lFileLookup.write(
	// "* <p>Description: Classe DAO che rappresenta la tabella " + mNomeTabella + "</p>\n");
	// lFileLookup.write("* <p>Copyright: Copyright (c) 2008</p>\n");
	// lFileLookup.write("* <p>Company: Eutelia S.p.A.</p>\n");
	// lFileLookup.write("* @version 1.0\n");
	// lFileLookup.write("*/\n\n");
	// // write import
	// lFileLookup.write("import f3b.util.LookupClass;\nimport f3b.util.F3BException;\n");
	// lFileLookup.write("import " + mPackage + ".controller.I" + mNomeTabella + ";\n");
	// lFileLookup.write("import " + mPackage + ".controller." + mNomeTabella + "Controller;\n\n");
	// lFileLookup.write("\t public class " + mContext + "LookupRemote extends LookupClass\n{\n");
	//
	// } else {
	// lFileLookup.write("\n//Inserire gli import in testa a file\n");
	// lFileLookup.write("//import " + mPackage + ".controller.I" + mNomeTabella + ";\n");
	// lFileLookup.write("//import " + mPackage + ".controller." + mNomeTabella + "Controller;\n\n");
	// }
	//
	// lFileLookup.write("\tpublic static I" + mNomeTabella + " get" + mNomeTabella
	// + "Remote() throws F3BException\n");
	// lFileLookup.write("\t\t {\n");
	// lFileLookup.write("\t\t\t Object lRef;\n");
	// lFileLookup.write("\t\t\t I" + mNomeTabella + " lRemote;\n");
	//
	// lFileLookup.write("\t\t\t lRef = lookup(\"" + mPackage + ".controller." + mNomeTabella
	// + "Controller\");\n");
	// lFileLookup.write("\t\t\t lRemote = (I" + mNomeTabella + ")lRef;\n");
	// lFileLookup.write("\t\t\t return lRemote;\n");
	// lFileLookup.write("\t\t }\n\n");
	//
	// if (!mLookup)
	// lFileLookup.write("}");
	//
	// lFileLookup.flush();
	// lFileLookup.close();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	//
	// Scrittura FILE DAO JAVA
	//
	// private static void writeDAO() {
	// try {
	// String lNomeFile = mDirectory;
	//
	// if (mForceOutput)
	// lNomeFile = mDirectory + "\\dao\\";
	//
	// PrintWriter lFileDAO = new PrintWriter(
	// new BufferedWriter(new FileWriter(lNomeFile + mNomeTabella + "DAO.java")));
	//
	// lFileDAO.write("package " + mPackage + ".dao;\n\n");
	//
	// // Write javadoc
	// lFileDAO.write("/**\n");
	// lFileDAO.write("* <p>Title: " + mNomeTabella + "DAO</p>\n");
	// lFileDAO.write(
	// "* <p>Description: Classe DAO che rappresenta la tabella " + mNomeTabella + "</p>\n");
	// lFileDAO.write("* <p>Copyright: Copyright (c) 2008</p>\n");
	// lFileDAO.write("* <p>Company: Eutelia S.p.A.</p>\n");
	// lFileDAO.write("* @version 1.0\n");
	// lFileDAO.write("*/\n\n");
	// // write import
	// lFileDAO.write("import java.math.BigDecimal;\n");
	// lFileDAO.write("import java.util.Date;\n");
	// lFileDAO.write("import java.sql.Connection;\n\n");
	// lFileDAO.write("import f3b.dao.DAOException;\nimport f3b.dao.TableDAO;\n");
	// lFileDAO.write("import f3b.model.GenericModel;\n\n");
	//
	// lFileDAO.write("import " + mPackage + ".model." + mNomeTabella + "Model;\n\n");
	//
	// lFileDAO.write("public class " + mNomeTabella + "DAO extends TableDAO \n");
	// lFileDAO.write("{\n");
	// lFileDAO.write("\tpublic " + mNomeTabella + "DAO (Connection con) \n");
	// lFileDAO.write("\t{\n");
	// lFileDAO.write("\t\t\t super(con);\n");
	// lFileDAO.write("\t\t\t setTable(\"" + mNomeTabella.toUpperCase() + "\");\n\n");
	// lFileDAO.write("\t\t\t //Settare la Sequence e i campi chiave\n\n");
	//
	// // Scrivere i setField
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// lFileDAO.write("\t\t\t setField(\"" + mNomiCampiDAO.elementAt(i) + "\", "
	// + mTipoCampiDAO.elementAt(i) + ");\n");
	// }
	// lFileDAO.write("\t}\n");
	//
	// // Metodi GET
	// lFileDAO.write("\n\n //\n // METODI GET()\n //\n\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// String lTipo = mTipoJavaCampiDAO.elementAt(i).toString();
	// String lTab = " \t\t ";
	//
	// if (lTipo.compareTo("BigDecimal") == 0)
	// lTab = " \t\t ";
	// if (lTipo.compareTo("String") == 0)
	// lTab = " \t\t\t\t ";
	// if (lTipo.compareTo("Date") == 0)
	// lTab = " \t\t\t\t\t ";
	//
	// lFileDAO.write("\t\t\tpublic " + lTipo + lTab + "get" + mNomiJavaCampiDAO.elementAt(i)
	// + "() \t\tthrows DAOException\t { return get" + mTipoJavaCampiDAO.elementAt(i) + "(\""
	// + mNomiCampiDAO.elementAt(i) + "\"); } \n");
	// }
	//
	// // Metodi SET
	// lFileDAO.write("\n\n //\n // METODI SET()\n //\n\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// lFileDAO.write("\t\t\tpublic void \t set" + mNomiJavaCampiDAO.elementAt(i) + "("
	// + mTipoJavaCampiDAO.elementAt(i) + " aValore ) \t\t\t { set"
	// + mTipoJavaCampiDAO.elementAt(i) + "(\"" + mNomiCampiDAO.elementAt(i)
	// + "\", aValore); } \n");
	// }
	//
	// // Genera Get MOdel
	// lFileDAO.write("\n\n\tpublic GenericModel getModel() throws DAOException\n "
	// + " \t\t\t { \n \t\t\t\t return new " + mNomeTabella + "Model( \n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// if (i == mNomiCampiDAO.size() - 1) {
	// lFileDAO.write("\t\t\t\t\t\t\t\t get" + mNomiJavaCampiDAO.elementAt(i) + "() \n");
	// } else {
	// String lNomeCampo = mNomiJavaCampiDAO.elementAt(i).toString();
	//
	// lFileDAO.write("\t\t\t\t\t\t\t\t get" + lNomeCampo + "() , \n");
	//
	// if ((lNomeCampo.startsWith("Cod")) && (lNomeCampo.indexOf("Operatore") == -1))
	// lFileDAO.write("\t\t\t\t\t\t\t\t \"\",\n");
	// }
	// }
	//
	// lFileDAO.write("\t\t\t\t\t\t\t\t);\n");
	// lFileDAO.write("\t\t}\n");
	//
	// // Genera SetDAO from Model
	// lFileDAO.write("\n\n\t public void \t setDAOFromModel(" + mNomeTabella
	// + "Model aModel) throws DAOException\n " + " \t\t{\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// lFileDAO.write("\t\t\t\t set" + mNomiJavaCampiDAO.elementAt(i) + "( aModel.get"
	// + mNomiJavaCampiDAO.elementAt(i) + "() ); \n");
	// }
	//
	// lFileDAO.write("\t\t}\n");
	//
	// // Set Condizioni
	// lFileDAO.write("\n\n\tpublic void selCondizione(" + mNomeTabella + "Model aModel)\n");
	// lFileDAO.write("\t\t {\n");
	// lFileDAO.write("\t\t String lCondizioni = new String(); \n \t\t\n");
	// lFileDAO.write("\t\t boolean lInserito = false; \n ");
	// lFileDAO.write("\t\t if ( lInserito ) setCondition(lCondizioni); \n");
	// lFileDAO.write("\t\t }\n");
	// lFileDAO.write("\n\n\tpublic void selCondizioneUpdate(" + mTypeKey + " key)\n \t\t\t {}");
	// lFileDAO.write("\n}\n");
	//
	// lFileDAO.flush();
	// lFileDAO.close();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * Scrittura FILE Sql DAO JAVA
	 */
	// private static void writeSqlDAO() {
	//
	// try {
	// String lNomeFile = mDirectory;
	//
	// if (mForceOutput)
	// lNomeFile = mDirectory + "\\dao\\";
	//
	// PrintWriter lFileDAO = new PrintWriter(
	// new BufferedWriter(new FileWriter(lNomeFile + mNomeTabella + "SqlDAO.java")));
	//
	// lFileDAO.write("package " + mPackage + ".dao;\n\n");
	//
	// // Write javadoc
	// lFileDAO.write("/**\n");
	// lFileDAO.write("* <p>Title: " + mNomeTabella + "SqlDAO</p>\n");
	// lFileDAO.write(
	// "* <p>Description: Classe SqlDAO che rappresenta la tabella " + mNomeTabella + "</p>\n");
	// lFileDAO.write("* <p>Copyright: Copyright (c) 2008</p>\n");
	// lFileDAO.write("* <p>Company: Eutelia S.p.A.</p>\n");
	// lFileDAO.write("* @version 1.0\n");
	// lFileDAO.write("*/\n\n");
	//
	// lFileDAO.write("import java.math.BigDecimal;\n");
	// lFileDAO.write("import java.util.Date;\n");
	// lFileDAO.write("import java.sql.Connection;\n\n");
	// lFileDAO.write("import f3b.dao.DAOException;\nimport f3b.dao.SqlDAO;\n");
	// lFileDAO.write("import f3b.model.GenericModel;\n\n");
	//
	// lFileDAO.write("import " + mPackage + ".model." + mNomeTabella + "Model;\n\n");
	//
	// lFileDAO.write("public class " + mNomeTabella + "SqlDAO extends SqlDAO \n");
	// lFileDAO.write("{\n");
	// lFileDAO.write("\t public " + mNomeTabella + "SqlDAO (Connection con) \n");
	// lFileDAO.write("\t\t\t{\n");
	// lFileDAO.write("\t\t\t super(con);\n");
	// lFileDAO.write("\t\t\t}\n");
	//
	// // Metodo ricerca
	// lFileDAO.write("\n\n //\n // METODO RICERCA()\n //\n");
	//
	// lFileDAO.write(
	// "\n\n public void ricerca" + mNomeTabella + "( " + mNomeTabella + "Model aModel)");
	// lFileDAO.write("\t throws DAOException\n");
	// lFileDAO.write("\t\t{\n");
	// lFileDAO.write("\t\t\t String lStatement = new String(\"\");\n\n");
	// lFileDAO.write("\t\t\t lStatement += \" SELECT \" +\n");
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// if (i == mNomiCampiDAO.size() - 1) {
	// lFileDAO.write("\t\t\t\t \"" + mNomiCampiDAO.elementAt(i) + " \"; \n");
	// } else {
	// lFileDAO.write("\t\t\t\t \"" + mNomiCampiDAO.elementAt(i) + ", \"+ \n");
	// }
	// }
	//
	// lFileDAO.write("\t\t\t lStatement += \" FROM " + mNomeTabella.toUpperCase() + "\";\n");
	// lFileDAO.write("\t\t\t lStatement += \" WHERE \";\n");
	// lFileDAO.write("\n\t\t\tlStatement += \" \" + setCondizioni(aModel);\n");
	// lFileDAO.write("\t\t\t setStatement(lStatement);\n");
	// lFileDAO.write("\t\t}\n");
	//
	// // Metodo getModel
	// lFileDAO.write("\n\n //\n // METODO GETMODEL()\n //\n");
	// // Genera Get MOdel
	// lFileDAO.write(
	// "\n\n\t public GenericModel \t getModel() throws DAOException\n " + " \t\t{ \n\t\t\t\t "
	// + mNomeTabella + "Model aModel = new " + mNomeTabella + "Model(); \n");
	//
	// lFileDAO.write("\n//Inserire le opportune set delle descrizioni!\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// String lNomeCampo = mNomiJavaCampiDAO.elementAt(i).toString();
	//
	// lFileDAO.write("\t\t\t\t aModel.set" + lNomeCampo + "(get" + mTipoJavaCampiDAO.elementAt(i)
	// + "(\"" + mNomiCampiDAO.elementAt(i) + "\") ); \n");
	//
	// if ((lNomeCampo.startsWith("Cod")) && (lNomeCampo.indexOf("Operatore") == -1)) {
	// lFileDAO.write(
	// "\t\t\t\t aModel.setDescr" + lNomeCampo.substring(3) + "(getString(\"\") );\n");
	// }
	// }
	// lFileDAO.write("\t\t\t\t return aModel;\n");
	// // lFileDAO.write("\t\t\t\t);");
	// lFileDAO.write("\t\t}\n");
	//
	// // Set Condizioni
	// lFileDAO.write("\n\n\t public void selCondizione(" + mNomeTabella + "Model aModel)\n");
	// lFileDAO.write("\t\t{\n");
	// lFileDAO.write("\t\t String lCondizioni = new String(); \n \t\t\n");
	// lFileDAO.write("\t\t boolean lInserito = false; \n ");
	// lFileDAO.write("\t\t}\n");
	//
	// // Set Condizioni
	// lFileDAO.write("\n\n\t public String setCondizioni(" + mNomeTabella + "Model aModel)\n");
	// lFileDAO.write("\t\t{\n");
	// lFileDAO.write("\t\t String lCondizioni = new String(); \n \t\t\n");
	// lFileDAO.write("\t\t boolean lInserito = false; \n ");
	// lFileDAO.write("\t\t return lCondizioni; \n");
	// lFileDAO.write("\t\t}\n");
	//
	// lFileDAO.write("}\n");
	// lFileDAO.flush();
	// lFileDAO.close();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	//
	// MODEL
	//
	// private static void writeModel() {
	// try {
	//
	// String lNomeFile = mDirectory;
	//
	// if (mForceOutput)
	// lNomeFile = mDirectory + "\\model\\";
	//
	// PrintWriter lFileModel = new PrintWriter(
	// new BufferedWriter(new FileWriter(lNomeFile + mNomeTabella + "Model.java")));
	//
	// lFileModel.write("package " + mPackage + ".model;\n\n");
	// // Write javadoc
	// lFileModel.write("/**\n");
	// lFileModel.write("* <p>Title: " + mNomeTabella + "Model</p>\n");
	// lFileModel.write("* <p>Description: Classe Model che rappresenta il " + mNomeTabella + "</p>\n");
	// lFileModel.write("* <p>Copyright: Copyright (c) 2008</p>\n");
	// lFileModel.write("* <p>Company: Eutelia S.p.A.</p>\n");
	// lFileModel.write("* @version 1.0\n");
	// lFileModel.write("*/\n\n");
	//
	// lFileModel.write("import java.util.Date;\n");
	// lFileModel.write("import java.math.BigDecimal;\n\n");
	//
	// lFileModel.write("import f3b.model.GenericModel;\n\n");
	//
	// lFileModel.write("public class " + mNomeTabella + "Model extends GenericModel \n");
	// lFileModel.write("{\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// String lNomeCampo = mNomiJavaCampiDAO.elementAt(i).toString();
	//
	// lFileModel.write(
	// "\t\t\t private \t" + mTipoJavaCampiDAO.elementAt(i) + "\tm" + lNomeCampo + ";\n");
	//
	// // Controllo se il campo è un Codice
	// if ((lNomeCampo.startsWith("Cod")) && (lNomeCampo.indexOf("Operatore") == -1)) {
	// lFileModel.write("\t\t\t private \t" + mTipoJavaCampiDAO.elementAt(i) + "\tmDescr"
	// + lNomeCampo.substring(3) + ";\n");
	// }
	// }
	//
	// // Genera Costruttore di default
	// lFileModel.write("\n\n//COSTRUTTORE DI DEFAULT \n");
	// lFileModel.write("\tpublic " + mNomeTabella + "Model ()\n\t\t{\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// String lNomeCampo = mNomiJavaCampiDAO.elementAt(i).toString();
	//
	// if (mTipoJavaCampiDAO.elementAt(i).toString().compareTo("String") == 0)
	// lFileModel.write("\t\t\t this.m" + lNomeCampo + " = \"\";\n");
	// else
	// lFileModel.write("\t\t\t this.m" + lNomeCampo + " = null;\n");
	//
	// // Controllo se il campo è un Codice
	// if ((lNomeCampo.startsWith("Cod")) && (lNomeCampo.indexOf("Operatore") == -1)) {
	// lFileModel.write("\t\t\t this.mDescr" + lNomeCampo.substring(3) + " = \"\";\n");
	// }
	// }
	//
	// lFileModel.write("\t\t}\n\n");
	//
	// // Genera Costruttore di copia
	// lFileModel.write("\n\n//COSTRUTTORE DI COPIA \n");
	// lFileModel
	// .write("\tpublic " + mNomeTabella + "Model ( " + mNomeTabella + "Model aModel )\n\t\t{");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// String lNomeCampo = mNomiJavaCampiDAO.elementAt(i).toString();
	// lFileModel.write("\t\t\t this.m" + lNomeCampo + " = aModel.m" + lNomeCampo + ";\n");
	//
	// if ((lNomeCampo.startsWith("Cod")) && (lNomeCampo.indexOf("Operatore") == -1)) {
	// lFileModel.write("\t\t\t this.mDescr" + lNomeCampo.substring(3) + " = aModel.mDescr"
	// + lNomeCampo.substring(3) + ";\n");
	// }
	// }
	//
	// lFileModel.write("\t\t}\n\n");
	//
	// // Genera Costruttore da DAO
	// lFileModel.write("//COSTRUTTORE MODEL \n");
	// lFileModel.write("\tpublic " + mNomeTabella + "Model (\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// if (i != mNomiCampiDAO.size() - 1) {
	// String lNomeCampo = mNomiJavaCampiDAO.elementAt(i).toString();
	// lFileModel.write(
	// "\t\t\t\t " + mTipoJavaCampiDAO.elementAt(i) + "\t a" + lNomeCampo + ",\n");
	//
	// if ((lNomeCampo.startsWith("Cod")) && (lNomeCampo.indexOf("Operatore") == -1)) {
	// lFileModel.write("\t\t\t\t " + mTipoJavaCampiDAO.elementAt(i) + "\t aDescr"
	// + lNomeCampo.substring(3) + ",\n");
	// }
	// } else {
	// lFileModel.write("\t\t\t\t " + mTipoJavaCampiDAO.elementAt(i) + "\t a"
	// + mNomiJavaCampiDAO.elementAt(i) + ")\n");
	// }
	// }
	//
	// lFileModel.write("\t\t\t{\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// String lNomeCampo = mNomiJavaCampiDAO.elementAt(i).toString();
	//
	// lFileModel.write("\t\t\t\t this.m" + lNomeCampo + " = a" + lNomeCampo + ";\n");
	//
	// if ((lNomeCampo.startsWith("Cod")) && (lNomeCampo.indexOf("Operatore") == -1)) {
	// lFileModel.write("\t\t\t\t this.mDescr" + lNomeCampo.substring(3) + " = aDescr"
	// + lNomeCampo.substring(3) + ";\n");
	// }
	//
	// }
	//
	// lFileModel.write("\t\t\t}");
	// lFileModel.write("\n\n //\n // METODI GET()\n //\n\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// String lNomeCampo = mNomiJavaCampiDAO.elementAt(i).toString();
	//
	// String lTipo = mTipoJavaCampiDAO.elementAt(i).toString();
	// String lTab = " \t\t ";
	//
	// if (lTipo.compareTo("BigDecimal") == 0)
	// lTab = " \t ";
	// if (lTipo.compareTo("String") == 0)
	// lTab = " \t\t\t ";
	// if (lTipo.compareTo("Date") == 0)
	// lTab = " \t\t\t\t ";
	//
	// lFileModel.write("\t\t public " + mTipoJavaCampiDAO.elementAt(i) + lTab + "get" + lNomeCampo
	// + "() \t\t\t { return m" + lNomeCampo + "; } \n");
	//
	// if ((lNomeCampo.startsWith("Cod")) && (lNomeCampo.indexOf("Operatore") == -1)) {
	// lFileModel.write("\t\t public " + mTipoJavaCampiDAO.elementAt(i) + lTab + "getDescr"
	// + lNomeCampo.substring(3) + "() \t\t\t { return mDescr" + lNomeCampo.substring(3)
	// + "; } \n");
	// }
	// }
	//
	// lFileModel.write("\n\n //\n // METODI SET()\n //\n\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// String lNomeCampo = mNomiJavaCampiDAO.elementAt(i).toString();
	// lFileModel
	// .write("\t\t public void \t set" + lNomeCampo + "(" + mTipoJavaCampiDAO.elementAt(i)
	// + " aValore ) \t\t\t { m" + lNomeCampo + " = aValore; } \n");
	//
	// if ((lNomeCampo.startsWith("Cod")) && (lNomeCampo.indexOf("Operatore") == -1)) {
	// lFileModel.write("\t\t public void \t setDescr" + lNomeCampo.substring(3) + "("
	// + mTipoJavaCampiDAO.elementAt(i) + " aValore ) \t\t\t { mDescr"
	// + lNomeCampo.substring(3) + " = aValore; } \n");
	//
	// }
	//
	// }
	//
	// lFileModel.write("}\n");
	//
	// // lFileDAO.write("package " + mNomeTabella.toLowerCase() + ".dao;" + "\n");
	//
	// lFileModel.flush();
	// lFileModel.close();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	//
	// ACTION
	//
	// private static void writeAction() {
	//
	// String lNomeFile = mDirectory;
	//
	// if (mForceOutput)
	// lNomeFile = mDirectory + "\\action\\";
	//
	// try {
	//
	// String lLocalModel = "l" + mNomeTabella.substring(0, 3) + "Mod";
	// // --------------------------------------------------------------------------
	//
	// PrintWriter lFileAction = new PrintWriter(
	// new BufferedWriter(new FileWriter(lNomeFile + "ActInserisci" + mNomeTabella + ".java")));
	//
	// lFileAction.write("package " + mPackage + ".action;\n\n");
	//
	// // Write javadoc
	// lFileAction.write("\n/**\n");
	// lFileAction.write("* <p>Title: ActInserisci" + mNomeTabella + "</p>\n");
	// lFileAction
	// .write("* <p>Description: Classe Action per l'inserimento di " + mNomeTabella + "</p>\n");
	// lFileAction.write("* <p>Copyright: Copyright (c) 2008</p>\n");
	// lFileAction.write("* <p>Company: Eutelia S.p.A.</p>\n");
	// lFileAction.write("* @version 1.0\n");
	// lFileAction.write("*/\n\n");
	// lFileAction.write("import f3b.web.Action;\n");
	// lFileAction.write("import f3b.web.IWebConstants;\n");
	// lFileAction.write("import f3b.util.F3BException;\n");
	// lFileAction.write("import f3b.web.RedirectTo;\n");
	// lFileAction.write("import f3b.util.DateUtils;\n\n");
	// lFileAction.write("import " + mPackage + ".model." + mNomeTabella + "Model;\n");
	// lFileAction.write("import " + mContext.toLowerCase() + ".util." + mContext + "LookupRemote;\n");
	// lFileAction.write("import " + mPackage + ".controller.I" + mNomeTabella + ";\n\n");
	//
	// lFileAction.write("public class ActInserisci" + mNomeTabella
	// + " extends Action implements ICostanti" + mNomeTabella + "\n");
	// // METODI : processaRichiesta
	// lFileAction.write("{\n");
	//
	// lFileAction.write("/**\n");
	// lFileAction.write("* Azione di Inserimento del " + mNomeTabella + "\n");
	// lFileAction.write("* @return Nome della pagina JSP da visualizzare\n");
	// lFileAction.write("* al termine dell'elaborazione\n");
	// lFileAction.write("* @throws F3BException\n");
	// lFileAction.write("*/\n");
	//
	// lFileAction.write("public String processRequest() throws F3BException\n");
	// lFileAction.write(" \t\t {\n");
	// lFileAction.write(" \t\t " + mNomeTabella + "Model " + lLocalModel + " = new " + mNomeTabella
	// + "Model();\n");
	// lFileAction.write(" \t\t\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// String lNome = mNomiJavaCampiDAO.elementAt(i).toString();
	// String lTipo = mTipoJavaCampiDAO.elementAt(i).toString();
	// String lNomeDAO = mNomiCampiDAO.elementAt(i).toString();
	//
	// if (lTipo.compareTo("Date") == 0) {
	// lFileAction.write("\t\t " + lLocalModel + ".set" + lNome + "( getRequest" + lTipo
	// + "Parameter( CAMPO_ANNO_" + lNomeDAO + ",CAMPO_MESE_" + lNomeDAO
	// + ",CAMPO_GIORNO_" + lNomeDAO + ") );\n");
	// } else {
	// lFileAction.write("\t\t " + lLocalModel + ".set" + lNome + "( getRequest" + lTipo
	// + "Parameter( CAMPO_" + lNomeDAO + ") );\n");
	// }
	// }
	//
	// lFileAction.write("\n\t\t //---Aggiungere in " + mContext + "LookupRemote il metodo get"
	// + mNomeTabella + "Remote()\n");
	// lFileAction.write("\n\t\t I" + mNomeTabella + " lCtrl = " + mContext + "LookupRemote.get"
	// + mNomeTabella + "Remote();\n");
	// lFileAction.write("\t\t lCtrl.ExInserisci" + mNomeTabella + "(" + lLocalModel + ");");
	// lFileAction.write("\t\t // setta la risposta nella request");
	// lFileAction.write("\n\t\t setRequestAttribute(\"" + mNomeTabella.toLowerCase() + "\", "
	// + lLocalModel + ");\n");
	// lFileAction.write("\t\t //Prepara la pagina di destinazione\n");
	// lFileAction.write(
	// "\t\t setRequestAttribute(IWebConstants.MESSAGE_TEXT,\"Inserimento Avvenuto Correttamente!\");\n");
	// lFileAction.write("\n\t\t RedirectTo lRedirigi = new RedirectTo();\n");
	// lFileAction.write("\t\t lRedirigi.setPage( IWebConstants.PG_MAIN );\n");
	// lFileAction.write("\t\t lRedirigi.setParameter(\"modalita\",\"I\");\n");
	// lFileAction.write("\t\t lRedirigi.setParameter(ICostanti" + mNomeTabella + ".CAMPO_"
	// + mKey.toUpperCase() + ", " + lLocalModel + ".get" + mKeyJava + "().toString());\n");
	// lFileAction.write("\t\t lRedirigi.setAction(\"" + mPackage + ".action.ActLoadDettaglio"
	// + mNomeTabella + "\" );\n");
	// lFileAction.write("\t\t setRequestAttribute( IWebConstants.GOTO_PAGE, \"\" + lRedirigi );\n");
	// lFileAction.write("\t\t return IWebConstants.PG_MESSAGE;\n");
	// lFileAction.write("\t }\n");
	// lFileAction.write("\n\n\n}");
	//
	// lFileAction.close();
	// // --------------------------------------------------------------------------
	//
	// // ACTION MODIFICA
	// lFileAction = new PrintWriter(
	// new BufferedWriter(new FileWriter(lNomeFile + "ActModifica" + mNomeTabella + ".java")));
	//
	// lFileAction.write("package " + mPackage + ".action;\n\n");
	// // Write javadoc
	// lFileAction.write("\n/**\n");
	// lFileAction.write("* <p>Title: ActModifica" + mNomeTabella + "</p>\n");
	// lFileAction
	// .write("* <p>Description: Classe Action per la modifica di " + mNomeTabella + "</p>\n");
	// lFileAction.write("* <p>Copyright: Copyright (c) 2008</p>\n");
	// lFileAction.write("* <p>Company: Eutelia S.p.A.</p>\n");
	// lFileAction.write("* @version 1.0\n");
	// lFileAction.write("*/\n\n");
	//
	// lFileAction.write("import java.math.BigDecimal;\n\n");
	// lFileAction.write("import f3b.web.Action;\n");
	// lFileAction.write("import f3b.web.IWebConstants;\n");
	// lFileAction.write("import f3b.util.F3BException;\n");
	// lFileAction.write("import f3b.web.RedirectTo;\n");
	// lFileAction.write("import f3b.util.DateUtils;\n\n");
	// lFileAction.write("import " + mPackage + ".model." + mNomeTabella + "Model;\n");
	// lFileAction.write("import " + mContext.toLowerCase() + ".util." + mContext + "LookupRemote;\n");
	// lFileAction.write("import " + mPackage + ".controller.I" + mNomeTabella + ";\n\n");
	//
	// lFileAction.write("public class ActModifica" + mNomeTabella
	// + " extends Action implements ICostanti" + mNomeTabella + "\n");
	// // METODI : processaRichiesta
	// lFileAction.write("{\n");
	//
	// lFileAction.write("/**\n");
	// lFileAction.write("* Azione di Modifica del " + mNomeTabella + "\n");
	// lFileAction.write("* @return Nome della pagina JSP da visualizzare\n");
	// lFileAction.write("* al termine dell'elaborazione\n");
	// lFileAction.write("* @throws F3BException\n");
	// lFileAction.write("*/\n");
	//
	// lFileAction.write("public String processRequest() throws F3BException \n{\n\n");
	// lFileAction.write(
	// " \t\t String lId = getRequestStringParameter(CAMPO_" + mKey.toUpperCase() + ");\n");
	// lFileAction.write(" \t\t // riempie il model\n");
	// lFileAction.write(" \t\t " + mNomeTabella + "Model " + lLocalModel + " = new " + mNomeTabella
	// + "Model " + "();\n\n");
	//
	// if (mTypeKey.compareTo("BigDecimal") == 0)
	// lFileAction.write(" \t\t " + lLocalModel + ".set" + mKeyJava + "(new BigDecimal(lId));\n");
	// else
	// // la chiave è una Stringa
	// lFileAction.write(" \t\t " + lLocalModel + ".set" + mKeyJava + "(lId);\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// String lNome = mNomiJavaCampiDAO.elementAt(i).toString();
	// String lTipo = mTipoJavaCampiDAO.elementAt(i).toString();
	// String lNomeDAO = mNomiCampiDAO.elementAt(i).toString();
	//
	// if (mTipoJavaCampiDAO.elementAt(i).toString().compareTo("Date") == 0) {
	// lFileAction.write("\t\t " + lLocalModel + ".set" + lNome + "( getRequest" + lTipo
	// + "Parameter( CAMPO_GIORNO_" + lNomeDAO + ",CAMPO_MESE_" + lNomeDAO
	// + ",CAMPO_ANNO_" + lNomeDAO + ") );\n");
	// } else {
	// lFileAction.write("\t\t " + lLocalModel + ".set" + lNome + "( getRequest" + lTipo
	// + "Parameter( CAMPO_" + lNomeDAO + ") );\n");
	// }
	// }
	//
	// lFileAction.write("\n\t\t // chiama il controller\n");
	// lFileAction.write("\t\t I" + mNomeTabella + " lCtrl = " + mContext + "LookupRemote.get"
	// + mNomeTabella + "Remote();\n");
	// lFileAction.write("\t\t lCtrl.ExModifica" + mNomeTabella + "(" + lLocalModel + ");\n\n");
	// lFileAction.write("\t\t setRequestAttribute(\"modalita\", \"M\");\n");
	// lFileAction.write("\t\t setRequestAttribute(\"" + mNomeTabella.toLowerCase() + "\", "
	// + lLocalModel + ");\n\n");
	// lFileAction.write("\t\t return PG_LOAD_INSERISCI" + mNomeTabella.toUpperCase() + ";\n");
	// lFileAction.write("\t }\n");
	// lFileAction.write("\n\n\n}");
	// lFileAction.close();
	// // --------------------------------------------------------------------------
	//
	// // FILE ACTION LOAD RICERCA
	// lFileAction = new PrintWriter(new BufferedWriter(
	// new FileWriter(lNomeFile + "ActLoadRicerca" + mNomeTabella + ".java")));
	//
	// lFileAction.write("package " + mPackage + ".action;\n\n");
	//
	// // Write javadoc
	// lFileAction.write("\n/**\n");
	// lFileAction.write("* <p>Title: ActLoadRicerca" + mNomeTabella + "</p>\n");
	// lFileAction.write(
	// "* <p>Description: Classe Action per la load ricerca di " + mNomeTabella + "</p>\n");
	// lFileAction.write("* <p>Copyright: Copyright (c) 2008</p>\n");
	// lFileAction.write("* <p>Company: Eutelia S.p.A.</p>\n");
	// lFileAction.write("* @version 1.0\n");
	// lFileAction.write("*/\n\n");
	//
	// lFileAction.write("import f3b.util.F3BException;\nimport f3b.web.Action;\n\n");
	//
	// lFileAction.write("public class ActLoadRicerca" + mNomeTabella
	// + " extends Action implements ICostanti" + mNomeTabella + "\n");
	// lFileAction.write("{\n");
	//
	// lFileAction.write("\tpublic String processRequest() throws F3BException \n\t {\n\n");
	// lFileAction.write("\t\t return PG_LOAD_RICERCA" + mNomeTabella.toUpperCase()
	// + "; //restituisce la jsp di VIEW \n\n");
	// lFileAction.write("\t }\n");
	// lFileAction.write("\n}");
	// lFileAction.close();
	// // --------------------------------------------------------------------------
	//
	// // FILE ACTION LOAD INSERISCI
	// lFileAction = new PrintWriter(new BufferedWriter(
	// new FileWriter(lNomeFile + "ActLoadInserisci" + mNomeTabella + ".java")));
	//
	// lFileAction.write("package " + mPackage + ".action;\n\n");
	//
	// // Write javadoc
	// lFileAction.write("\n/**\n");
	// lFileAction.write("* <p>Title: ActLoadInserisci" + mNomeTabella + "</p>\n");
	// lFileAction.write(
	// "* <p>Description: Classe Action per la load inserisci di " + mNomeTabella + "</p>\n");
	// lFileAction.write("* <p>Copyright: Copyright (c) 2008</p>\n");
	// lFileAction.write("* <p>Company: Eutelia S.p.A.</p>\n");
	// lFileAction.write("* @version 1.0\n");
	// lFileAction.write("*/\n\n");
	//
	// lFileAction.write(
	// "import f3b.web.Action;\nimport f3b.util.F3BException;\n\n//import per le combo\n");
	// lFileAction.write(
	// "//import f3b.web.html.Option;\n//import decodifiche.controller.DecodificheManager;\n\n");
	//
	// lFileAction.write("public class ActLoadInserisci" + mNomeTabella
	// + " extends Action implements ICostanti" + mNomeTabella + "\n{\n");
	// lFileAction.write("\t public String processRequest() throws F3BException \n\t\t{\n\n");
	// lFileAction.write("\t // Inserire Eventuali ComboBOX\n");
	// lFileAction.write(
	// "\t // Option lOption = new Option( DecodificheManager.getInstance().get???());\n\n");
	// lFileAction.write("\t // setRequestAttribute(\"???\", \"\" + lOption );\n");
	// lFileAction.write("\t // Imposta Modalità.\n");
	// lFileAction.write("\t setRequestAttribute(\"modalita\", \"I\");\n");
	// lFileAction.write("\t\t return PG_LOAD_INSERISCI" + mNomeTabella.toUpperCase()
	// + "; //restituisce la jsp di VIEW \n\n");
	// lFileAction.write("\t\t}\n");
	// lFileAction.write("\n\n\n}");
	// lFileAction.close();
	// // --------------------------------------------------------------------------
	//
	// // FILE ACTION LOAD DETTAGLIO
	// lFileAction = new PrintWriter(new BufferedWriter(
	// new FileWriter(lNomeFile + "ActLoadDettaglio" + mNomeTabella + ".java")));
	//
	// lFileAction.write("package " + mPackage + ".action;\n\n");
	//
	// // Write javadoc
	// lFileAction.write("\n/**\n");
	// lFileAction.write("* <p>Title: ActLoadDettaglio" + mNomeTabella + "</p>\n");
	// lFileAction.write(
	// "* <p>Description: Classe Action per la load dettaglio di " + mNomeTabella + "</p>\n");
	// lFileAction.write("* <p>Copyright: Copyright (c) 2008</p>\n");
	// lFileAction.write("* <p>Company: Eutelia S.p.A.</p>\n");
	// lFileAction.write("* @version 1.0\n");
	// lFileAction.write("*/\n\n");
	//
	// lFileAction.write("import java.util.Vector;\n");
	// lFileAction.write("import java.math.BigDecimal;\n\n");
	//
	// lFileAction.write("import f3b.web.Action;\n");
	// lFileAction.write("import f3b.util.F3BException;\n\n");
	//
	// lFileAction.write("import " + mPackage + ".model." + mNomeTabella + "Model;\n");
	// lFileAction.write("import " + mContext.toLowerCase() + ".util." + mContext + "LookupRemote;\n");
	// lFileAction.write("import " + mPackage + ".controller.I" + mNomeTabella + ";\n\n");
	//
	// lFileAction.write("public class ActLoadDettaglio" + mNomeTabella
	// + " extends Action implements ICostanti" + mNomeTabella + "\n");
	// lFileAction.write("{\n");
	// lFileAction.write("public String processRequest() throws F3BException {\n\n");
	// lFileAction.write(
	// " \t\t String lId = getRequestStringParameter(CAMPO_" + mKey.toUpperCase() + ");\n");
	// lFileAction.write(" \t\t // riempie il model\n");
	// lFileAction.write(" \t\t " + mNomeTabella + "Model " + lLocalModel + " = new " + mNomeTabella
	// + "Model();\n");
	//
	// if (mTypeKey.compareTo("BigDecimal") == 0)
	// lFileAction.write(" \t\t " + lLocalModel + ".set" + mKeyJava + "(new BigDecimal(lId));\n");
	// else
	// lFileAction.write(" \t\t " + lLocalModel + ".set" + mKeyJava + "(lId);\n");
	//
	// lFileAction.write("\t\t // chiama il controller\n");
	// lFileAction.write("\n\t\t I" + mNomeTabella + " lCtrl = " + mContext + "LookupRemote.get"
	// + mNomeTabella + "Remote();\n");
	// lFileAction
	// .write("\t\t Vector lVect = lCtrl.ExRicerca" + mNomeTabella + "(" + lLocalModel + ");\n");
	// lFileAction.write("\t\t " + lLocalModel + " = new " + mNomeTabella + "Model( (" + mNomeTabella
	// + "Model)lVect.firstElement() );\n");
	// lFileAction.write("\t\t setRequestAttribute(\"" + mNomeTabella.toLowerCase() + "\", "
	// + lLocalModel + ");\n\n");
	//
	// lFileAction.write("\t\t return PG_LOAD_DETTAGLIO" + mNomeTabella.toUpperCase() + ";\n");
	// lFileAction.write("\t }\n");
	// lFileAction.write("\n\n\n}");
	// lFileAction.close();
	// // --------------------------------------------------------------------------
	//
	// // ACTION RICERCA
	// lFileAction = new PrintWriter(
	// new BufferedWriter(new FileWriter(lNomeFile + "ActRicerca" + mNomeTabella + ".java")));
	//
	// lFileAction.write("package " + mPackage + ".action;\n\n");
	// // Write javadoc
	// lFileAction.write("\n/**\n");
	// lFileAction.write("* <p>Title: ActRicerca" + mNomeTabella + "</p>\n");
	// lFileAction.write("* <p>Description: Classe Action per la ricerca di " + mNomeTabella + "</p>\n");
	// lFileAction.write("* <p>Copyright: Copyright (c) 2008</p>\n");
	// lFileAction.write("* <p>Company: Eutelia S.p.A.</p>\n");
	// lFileAction.write("* @version 1.0\n");
	// lFileAction.write("*/\n\n");
	//
	// lFileAction.write("import java.util.Vector;\n\n");
	// lFileAction.write("import f3b.util.F3BException;\n");
	// lFileAction.write("import f3b.web.Action;\n\n");
	// lFileAction.write("import " + mPackage + ".model." + mNomeTabella + "Model;\n");
	// lFileAction.write("import " + mContext.toLowerCase() + ".util." + mContext + "LookupRemote;\n");
	// lFileAction.write("import " + mPackage + ".controller.I" + mNomeTabella + ";\n\n");
	// lFileAction.write("public class ActRicerca" + mNomeTabella
	// + " extends Action implements ICostanti" + mNomeTabella + "\n");
	// lFileAction.write("{\n");
	// lFileAction.write("public String processRequest() throws F3BException {\n\n");
	// // METODI : ricerca
	// lFileAction.write(" \t\t\n");
	// lFileAction.write(" \t\t " + mNomeTabella + "Model " + lLocalModel + " = new " + mNomeTabella
	// + "Model() " + ";\n");
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	//
	// if (mTipoJavaCampiDAO.elementAt(i).toString().compareTo("Date") == 0)
	// lFileAction.write("\t\t " + lLocalModel + ".set" + mNomiJavaCampiDAO.elementAt(i)
	// + "( getRequest" + mTipoJavaCampiDAO.elementAt(i) + "Parameter( CAMPO_GIORNO_"
	// + mNomiCampiDAO.elementAt(i) + ",CAMPO_MESE_" + mNomiCampiDAO.elementAt(i)
	// + ",CAMPO_ANNO_" + mNomiCampiDAO.elementAt(i) + ") );\n");
	// else
	// lFileAction.write("\t\t " + lLocalModel + ".set" + mNomiJavaCampiDAO.elementAt(i)
	// + "( getRequest" + mTipoJavaCampiDAO.elementAt(i) + "Parameter( CAMPO_"
	// + mNomiCampiDAO.elementAt(i) + ") );\n");
	//
	// }
	// lFileAction.write("\n\t\t I" + mNomeTabella + " lCtrl = " + mContext + "LookupRemote.get"
	// + mNomeTabella + "Remote();\n");
	// lFileAction
	// .write("\t\t Vector lVect = lCtrl.ExRicerca" + mNomeTabella + "(" + lLocalModel + ");\n");
	// lFileAction.write("\t\t setRequestAttribute(\"" + mNomeTabella.toLowerCase() + "\", lVect);\n\n");
	// lFileAction.write("\t\t return PG_RICERCA" + mNomeTabella.toUpperCase() + ";\n");
	// lFileAction.write("\t }\n");
	// lFileAction.write("\n\n\n");
	// lFileAction.write("}\n");
	// lFileAction.flush();
	// lFileAction.close();
	// // --------------------------------------------------------------------------
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * Write Controller e interfaccia
	 */
	// private static void writeController() {
	// String lNomeFile = mDirectory;
	//
	// if (mForceOutput)
	// lNomeFile = mDirectory + "\\controller\\";
	//
	// try {
	// String lLocalDao = "l" + mNomeTabella.substring(0, 3) + "Dao";
	// // RandomAccessFile lFileDAO = new RandomAccessFile( mNomeTabella + "DAO.java","rw");
	//
	// PrintWriter lFileInterface = new PrintWriter(
	// new BufferedWriter(new FileWriter(lNomeFile + "I" + mNomeTabella + ".java")));
	//
	// lFileInterface.write("package " + mPackage + ".controller;\n\n");
	// // Write javadoc
	// lFileInterface.write("\n/**\n");
	// lFileInterface.write("* <p>Title: " + mNomeTabella + "Controller</p>\n");
	// lFileInterface.write("* <p>Description: Classe Controller per " + mNomeTabella + "</p>\n");
	// lFileInterface.write("* <p>Copyright: Copyright (c) 2008</p>\n");
	// lFileInterface.write("* <p>Company: Eutelia S.p.A.</p>\n");
	// lFileInterface.write("* @version 1.0\n");
	// lFileInterface.write("*/\n\n");
	//
	// lFileInterface.write("import java.math.BigDecimal;\n");
	// lFileInterface.write("import java.util.Vector;\n\n");
	//
	// lFileInterface.write("import f3b.util.F3BException;\n\n");
	//
	// lFileInterface.write("import " + mPackage + ".model." + mNomeTabella + "Model;\n");
	// lFileInterface.write("import " + mPackage + ".dao." + mNomeTabella + "DAO;\n");
	// lFileInterface.write("\n\n\n");
	//
	// lFileInterface.write("public interface I" + mNomeTabella + "\n");
	// lFileInterface.write("{\n");
	// lFileInterface.write("public void ExInserisci" + mNomeTabella + " (" + mNomeTabella + "Model a"
	// + mNomeTabella + " )\n");
	// lFileInterface.write(" \t\t throws F3BException\n;\n");
	// lFileInterface.write("public Vector ExRicerca" + mNomeTabella + " (" + mNomeTabella + "Model a"
	// + mNomeTabella + " )\n");
	// lFileInterface.write(" \t\t throws F3BException\n;\n");
	//
	// lFileInterface.write("public void ExModifica" + mNomeTabella + " (" + mNomeTabella + "Model a"
	// + mNomeTabella + " )\n");
	// lFileInterface.write(" \t\t throws F3BException\n;\n");
	//
	// lFileInterface.write("public void ExCancella" + mNomeTabella + " (" + mNomeTabella + "Model a"
	// + mNomeTabella + " )\n");
	// lFileInterface.write(" \t\t throws F3BException\n;\n");
	//
	// lFileInterface.write("}\n");
	//
	// lFileInterface.flush();
	//
	// lFileInterface.close();
	//
	// PrintWriter lFileController = new PrintWriter(
	// new BufferedWriter(new FileWriter(lNomeFile + mNomeTabella + "Controller.java")));
	//
	// lFileController.write("package " + mPackage + ".controller;\n\n");
	//
	// // Write javadoc
	// lFileController.write("\n/**\n");
	// lFileController.write("* <p>Title: " + mNomeTabella + "Controller</p>\n");
	// lFileController.write("* <p>Description: Classe Controller per " + mNomeTabella + "</p>\n");
	// lFileController.write("* <p>Copyright: Copyright (c) 2008</p>\n");
	// lFileController.write("* <p>Company: Eutelia S.p.A.</p>\n");
	// lFileController.write("* @version 1.0\n");
	// lFileController.write("*/\n\n");
	//
	// lFileController.write("import java.sql.Connection;\n");
	// lFileController.write("import java.sql.SQLException;\n");
	// lFileController.write("import java.math.BigDecimal;\n");
	// lFileController.write("import java.util.Vector;\n\n");
	//
	// lFileController.write("import f3b.controller.GenericController;\n");
	// lFileController.write("import f3b.util.F3BException;\n");
	// lFileController.write("import f3b.web.IWebConstants;\n");
	// lFileController.write("import f3b.dao.DAOException;\n\n");
	//
	// lFileController.write("import " + mPackage + ".model." + mNomeTabella + "Model;\n");
	// lFileController.write("import " + mPackage + ".dao." + mNomeTabella + "DAO;\n");
	// lFileController.write("\n\n\n");
	//
	// lFileController.write("public class " + mNomeTabella
	// + "Controller extends GenericController implements I" + mNomeTabella + "\n");
	// lFileController.write("{\n");
	//
	// // -EXINSERISCI-----------------------------
	//
	// lFileController.write("public void ExInserisci" + mNomeTabella + " (" + mNomeTabella + "Model a"
	// + mNomeTabella + " )\n");
	// lFileController.write(" \t\t throws F3BException\n{\n");
	// lFileController.write(" \t\t Connection lConn = null;\n");
	// lFileController.write(" \t\t " + mNomeTabella + "DAO " + lLocalDao + " = null;\n");
	// lFileController.write(" \t\t\n");
	// lFileController.write("\n\n try {\n");
	// lFileController.write("\t\t lConn = getDBConnection();\n");
	// lFileController.write("\t\t " + lLocalDao + " = new " + mNomeTabella + "DAO(lConn);\n");
	// /*
	// * for (int i=0;i<mNomiCampiDAO.size();i++) { lFileController.write("\t\t "+ lLocalDao +".set"+
	// * mNomiJavaCampiDAO.elementAt(i)+"( a" +
	// * mNomeTabella+".get"+mNomiJavaCampiDAO.elementAt(i)+"() );\n"); }
	// */
	//
	// lFileController.write("\t\t" + lLocalDao + ".setDAOFromModel(a" + mNomeTabella + " );\n");
	// lFileController.write("\t\t " + lLocalDao + ".insert();\n");
	// lFileController.write("\t\t commit(lConn);\n");
	// lFileController.write("\t\t}\n\t\tcatch (DAOException ex)\n\t\t{ \n");
	// lFileController.write("\t\trollback(lConn);\n");
	// lFileController.write("\t\t throw new F3BException(\"" + mNomeTabella
	// + "Controller.ExInserisci: Non posso inserire: \" + ex);\n }");
	// lFileController.write("\t\t catch (SQLException sqe)\n");
	// lFileController.write("\t\t {\n");
	// lFileController.write("\t\t rollback(lConn);\n");
	// lFileController
	// .write("\t\t throw new F3BException(\"" + mNomeTabella + "Controller.ExInserisci"
	// + mNomeTabella + ": Non posso inserire il soggetti : \" + sqe);\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\t\t finally\n");
	// lFileController.write("\t\t {\n");
	// lFileController.write("\t\t cleanup(" + lLocalDao + ");\n");
	// lFileController.write("\t\t cleanup(lConn);\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\t\t}\n");
	// lFileController.write("\n\n\n");
	//
	// // --EXRICERCA---------------------------------
	// lFileController.write("public Vector ExRicerca" + mNomeTabella + " (" + mNomeTabella + "Model a"
	// + mNomeTabella + " )\n");
	// lFileController.write(" \t\t throws F3BException\n{\n");
	// lFileController.write(" \t\t Connection lConn = null;\n");
	// lFileController.write(" \t\t Vector l" + mNomeTabella.substring(0, mNomeTabella.length() - 1)
	// + "i = new Vector();\n");
	// lFileController.write(" \t\t " + mNomeTabella + "DAO " + lLocalDao + " = null;\n");
	// lFileController.write(" \t\t\n");
	// lFileController.write("\n\n try {\n");
	// lFileController.write("\t\t lConn = getDBConnection();\n");
	// lFileController.write("\t\t " + lLocalDao + " = new " + mNomeTabella + "DAO(lConn);\n");
	// lFileController.write("\t\t " + lLocalDao + ".selCondizione(a" + mNomeTabella + ");\n");
	// lFileController.write("\t\t " + lLocalDao + ".start();\n");
	// lFileController.write("\t\t while ( " + lLocalDao + ".next() )\n");
	// lFileController.write("\t\t {\n");
	// lFileController.write("\t\t l" + mNomeTabella.substring(0, mNomeTabella.length() - 1) + "i.add( ("
	// + mNomeTabella + "Model)" + lLocalDao + ".getModel() );\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\t\t if ( l" + mNomeTabella.substring(0, mNomeTabella.length() - 1)
	// + "i.size() == 0 )\n");
	// lFileController.write("\t\t {\n");
	// lFileController.write(
	// "\t\t throw new F3BException(F3BException.USER_MESSAGE,\"Nessun Elemento trovato\");\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\t\t catch (DAOException daoEx)\n");
	// lFileController.write("\t\t {\n");
	// lFileController.write("\t\t throw new F3BException(\"" + mNomeTabella
	// + "Controller.ExRicerca" + mNomeTabella + ": Non posso leggere : \" + daoEx);\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\t\t catch (SQLException sqe)\n");
	// lFileController.write("\t\t {\n");
	// lFileController.write("\t\t throw new F3BException(\"" + mNomeTabella
	// + "Controller.ExRicerca" + mNomeTabella + ": Non posso leggere : \" + sqe);\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\t\t finally \n");
	// lFileController.write("\t\t {\n");
	// lFileController.write("\t\t cleanup(" + lLocalDao + ");\n");
	// lFileController.write("\t\t cleanup(lConn);\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write(
	// "\t\t return l" + mNomeTabella.substring(0, mNomeTabella.length() - 1) + "i;\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\n\n\n");
	//
	// // --EXMODIFICA---------------------------------
	// lFileController.write("public void ExModifica" + mNomeTabella + " (" + mNomeTabella + "Model a"
	// + mNomeTabella + " )\n");
	// lFileController.write(" \t\t throws F3BException\n{\n");
	// lFileController.write(" \t\t Connection lConn = null;\n");
	// lFileController.write(" \t\t Vector l" + mNomeTabella.substring(0, mNomeTabella.length() - 1)
	// + "i = new Vector();\n");
	// lFileController.write(" \t\t " + mNomeTabella + "DAO " + lLocalDao + " = null;\n");
	// lFileController.write(" \t\t\n");
	// lFileController.write("\n\ntry {\n");
	// lFileController.write("\t\t lConn = getDBConnection();\n");
	// lFileController.write("\t\t " + lLocalDao + " = new " + mNomeTabella + "DAO(lConn);\n");
	// /*
	// * for (int i=0;i<mNomiCampiDAO.size();i++) { lFileController.write("\t\t "+ lLocalDao +".set"+
	// * mNomiJavaCampiDAO.elementAt(i)+"( a" +
	// * mNomeTabella+".get"+mNomiJavaCampiDAO.elementAt(i)+"() );\n"); }
	// */
	//
	// lFileController.write("\t\t" + lLocalDao + ".setDAOFromModel(a" + mNomeTabella + " );\n");
	// lFileController.write("\t\t " + lLocalDao + ".update();\n");
	// lFileController.write("\t\t commit(lConn);\n");
	// lFileController.write("\t\t}\n\t\tcatch (DAOException ex)\n\t\t{ \n");
	// lFileController.write("\t\trollback(lConn);\n");
	// lFileController.write("\t\t throw new F3BException(\"" + mNomeTabella
	// + "Controller.ExModifica: Non posso inserire: \" + ex);\n }");
	// lFileController.write("\t\t catch (SQLException sqe)\n");
	// lFileController.write("\t\t {\n");
	// lFileController.write("\t\t rollback(lConn);\n");
	// lFileController
	// .write("\t\t throw new F3BException(\"" + mNomeTabella + "Controller.ExModifica"
	// + mNomeTabella + ": Non posso inserire il soggetti : \" + sqe);\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\t\t finally\n");
	// lFileController.write("\t\t {\n");
	// lFileController.write("\t\t cleanup(" + lLocalDao + ");\n");
	// lFileController.write("\t\t cleanup(lConn);\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\n\n\n");
	//
	// // --EXCANCELLA---------------------------------
	// lFileController.write("public void ExCancella" + mNomeTabella + " (" + mNomeTabella + "Model a"
	// + mNomeTabella + " )\n");
	// lFileController.write(" \t\t throws F3BException\n{\n");
	// lFileController.write(" \t\t Connection lConn = null;\n");
	// lFileController.write(" \t\t " + mNomeTabella + "DAO " + lLocalDao + " = null;\n");
	// lFileController.write("\n\n try {\n");
	// lFileController.write("\t\t lConn = getDBConnection();\n");
	// lFileController.write("\t\t " + lLocalDao + " = new " + mNomeTabella + "DAO(lConn);\n");
	// lFileController.write("\t\t " + lLocalDao + ".selCondizioneUpdate(a" + mNomeTabella + ".get"
	// + mKeyJava + "());\n");
	// lFileController.write("\t\t " + lLocalDao + ".delete();\n");
	// lFileController.write("\t\t commit(lConn);\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\t\t catch (DAOException daoEx)\n");
	// lFileController.write("\t\t {\n");
	// lFileController.write("\t\t throw new F3BException(\"" + mNomeTabella
	// + "Controller.ExCancella" + mNomeTabella + ": Non posso leggere : \" + daoEx);\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\t\t catch (SQLException sqe)\n");
	// lFileController.write("\t\t {\n");
	// lFileController.write("\t\t throw new F3BException(\"" + mNomeTabella
	// + "Controller.ExCancella" + mNomeTabella + ": Non posso leggere : \" + sqe);\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\t\t finally \n");
	// lFileController.write("\t\t {\n");
	// lFileController.write("\t\t cleanup(" + lLocalDao + ");\n");
	// lFileController.write("\t\t cleanup(lConn);\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\t\t }\n");
	// lFileController.write("\n\n\n");
	//
	// lFileController.write("}\n");
	//
	// lFileController.flush();
	//
	// lFileController.close();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	//
	// JSP E ICOSTANTI
	//
	// private static void writeJsp() {
	// try {
	//
	// String lNomeFile = mDirectory;
	//
	// if (mForceOutput)
	// lNomeFile = mDirectory + "\\action\\";
	//
	// PrintWriter lFileInterface = new PrintWriter(
	// new BufferedWriter(new FileWriter(lNomeFile + "ICostanti" + mNomeTabella + ".java")));
	//
	// lFileInterface.write("package " + mPackage + ".action;\n\n");
	//
	// // Write javadoc
	// lFileInterface.write("/**\n");
	// lFileInterface.write("* <p>Title: ICostanti" + mNomeTabella + "</p>\n");
	// lFileInterface.write("* <p>Description: Classe di costanti di " + mNomeTabella + "</p>\n");
	// lFileInterface.write("* <p>Copyright: Copyright (c) 2008</p>\n");
	// lFileInterface.write("* <p>Company: Eutelia S.p.A.</p>\n");
	// lFileInterface.write("* @version 1.0\n");
	// lFileInterface.write("*/\n\n");
	//
	// lFileInterface.write("import f3b.web.IWebConstants;\n");
	// lFileInterface.write("public interface ICostanti" + mNomeTabella + "\n{\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	//
	// if (mTipoJavaCampiDAO.elementAt(i).toString().compareTo("Date") == 0) {
	// lFileInterface.write(
	// "\t\t public static final String CAMPO_GIORNO_" + mNomiCampiDAO.elementAt(i)
	// + " = \"Giorno" + mNomiJavaCampiDAO.elementAt(i).toString() + "\"; \n");
	// lFileInterface
	// .write("\t\t public static final String CAMPO_MESE_" + mNomiCampiDAO.elementAt(i)
	// + " = \"Mese" + mNomiJavaCampiDAO.elementAt(i).toString() + "\"; \n");
	// lFileInterface
	// .write("\t\t public static final String CAMPO_ANNO_" + mNomiCampiDAO.elementAt(i)
	// + " = \"Anno" + mNomiJavaCampiDAO.elementAt(i).toString() + "\"; \n");
	// } else
	// lFileInterface.write("\t\t public static final String CAMPO_" + mNomiCampiDAO.elementAt(i)
	// + " = \"" + mNomiJavaCampiDAO.elementAt(i).toString() + "\"; \n");
	//
	// }
	// lFileInterface.write("\t\t public static final String PG_LOAD_RICERCA"
	// + mNomeTabella.toUpperCase() + " = IWebConstants.ROOT_DIR + \"jsp/files/"
	// + mPackage.replace(".".charAt(0), "/".charAt(0)) + "/LoadRicerca" + mNomeTabella
	// + ".jsp\";\n");
	// lFileInterface.write("\t\t public static final String PG_LOAD_DETTAGLIO"
	// + mNomeTabella.toUpperCase() + " = IWebConstants.ROOT_DIR + \"jsp/files/"
	// + mPackage.replace(".".charAt(0), "/".charAt(0)) + "/LoadRicerca" + mNomeTabella
	// + ".jsp\";\n");
	//
	// lFileInterface.write("\t\t public static final String PG_RICERCA" + mNomeTabella.toUpperCase()
	// + " = IWebConstants.ROOT_DIR + \"jsp/files/"
	// + mPackage.replace(".".charAt(0), "/".charAt(0)) + "/Ricerca" + mNomeTabella
	// + ".jsp\";\n");
	// lFileInterface.write("\t\t public static final String PG_LOAD_INSERISCI"
	// + mNomeTabella.toUpperCase() + " = IWebConstants.ROOT_DIR + \"jsp/files/"
	// + mPackage.replace(".".charAt(0), "/".charAt(0)) + "/LoadInserisci" + mNomeTabella
	// + ".jsp\";\n");
	//
	// lFileInterface.write("}");
	//
	// lFileInterface.flush();
	//
	// lFileInterface.close();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// try {
	// // File JSP
	//
	// String lNomeFile = mDirectory;
	//
	// if (mForceOutput)
	// lNomeFile = mDirectory + "\\jsp\\";
	//
	// PrintWriter lFileJSP = new PrintWriter(
	// new BufferedWriter(new FileWriter(lNomeFile + "Load" + mNomeTabella + ".jsp")));
	//
	// lFileJSP.write("<%@ page import=\"f3b.web.IWebConstants\"%>");
	// lFileJSP.write("<%@ page import=\"f3b.util.DateUtils\"%>");
	// lFileJSP.write("<%@ page import=\"" + mPackage + ".model." + mNomeTabella + "Model\"%>");
	// lFileJSP.write("<%@ page import=\"" + mPackage + ".action.ICostanti" + mNomeTabella + "\"%>\n");
	//
	// lFileJSP.write("<html>\n<head>\t\t\n");
	// lFileJSP.write("<title>[S.I.A.P.] - Gestione" + mNomeTabella + " </title>");
	// lFileJSP.write(
	// "\n<link rel=\"STYLESHEET\" type=\"text/css\" \n href=\"<%=IWebConstants.PG_STYLE%>\">");
	// lFileJSP.write("\n<script language=\"JavaScript\" src=\"/html/gen_validatorv2.js\"></script>");
	// lFileJSP.write("\n</head>\n\n");
	//
	// lFileJSP.write("\n\t\t<body class=\"corpo\">");
	//
	// lFileJSP.write("\t\t\t<table>\n\t\t\t<tr>");
	// lFileJSP.write("\t\t\t <td><font class=\"label\">Funzione :</font>&nbsp;&nbsp;\n");
	// lFileJSP.write(" <%\n");
	// lFileJSP.write("\t\t\t " + mNomeTabella + "Model lModel = new " + mNomeTabella + "Model();");
	// lFileJSP.write("\t\t\t String lAzione = new String();\n");
	// lFileJSP.write("\t\t\t if( modalita.equals(\"I\") )\n");
	// lFileJSP.write("\t\t\t {\n");
	// lFileJSP.write(
	// "\t\t\t lAzione = \"" + mPackage + ".action.ActInserisci" + mNomeTabella + "\";");
	// lFileJSP.write("\t\t\t \n%>\n");
	// lFileJSP.write("\n\t\t\t<font class=\"campo\">Inserimento di un " + mNomeTabella + "</font>\n");
	// lFileJSP.write("\t\t\t <%\n");
	// lFileJSP.write("\n\t\t\t }\n");
	// lFileJSP.write("\n\t\t\t else if( modalita.equals(\"M\") )\n");
	// lFileJSP.write("\n\t\t\t {\n");
	// lFileJSP.write(
	// "\n\t\t\t lAzione = \"" + mPackage + ".action.ActModifica" + mNomeTabella + "\";\n");
	// lFileJSP.write("\t\t\t lModel = " + mNomeTabella.toLowerCase() + ";\n");
	// lFileJSP.write("\t\t\t %>");
	// lFileJSP.write("\t\t\t <font class=\"campo\">Modifica di un " + mNomeTabella + "</font>\n");
	// lFileJSP.write("\t\t\t <%}%>");
	//
	// lFileJSP.write("\t\t\t </td>\n</tr>\n</table>");
	//
	// lFileJSP.write(
	// "\t\t<FORM method=\"POST\" action=\"<%= IWebConstants.PG_MAIN%>\" name=\"LoadInserisci"
	// + mNomeTabella + "\">");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// lFileJSP.write("\t\t<tr>\n");
	// lFileJSP.write("\t\t\t\t<td class=\"l\">" + mNomiJavaCampiDAO.elementAt(i) + "</td>\n");
	// lFileJSP.write("\t\t\t\t<td class=\"l\"><input value=\"<%=l" + mNomeTabella + ".get"
	// + mNomiJavaCampiDAO.elementAt(i) + "() %>\" type=\"text\" name=\"<%= ICostanti"
	// + mNomeTabella + ".CAMPO_" + mNomiCampiDAO.elementAt(i) + " %>\" >" + "</td>\n");
	// lFileJSP.write("\t\t</tr>\n");
	//
	// if (mTipoJavaCampiDAO.elementAt(i).toString().compareTo("Date") == 0) {
	// lFileJSP.write("\t\t<tr>\n");
	// lFileJSP.write("\t\t\t\t<td class=\"l\"><input value=\"<%=l" + mNomeTabella + ".getGiorno"
	// + mNomiJavaCampiDAO.elementAt(i)
	// + "() %>\" type=\"text\" size=\"2\" maxlength=\"2\" name=\"<%= ICostanti"
	// + mNomeTabella + ".CAMPO_GIORNO_" + mNomiCampiDAO.elementAt(i) + " %>\" >"
	// + "</td>\n");
	// lFileJSP.write("\t\t\t\t<td class=\"l\"><input value=\"<%=l" + mNomeTabella + ".getMese"
	// + mNomiJavaCampiDAO.elementAt(i)
	// + "() %>\" type=\"text\" size=\"2\" maxlength=\"2\" name=\"<%= ICostanti"
	// + mNomeTabella + ".CAMPO_MESE_" + mNomiCampiDAO.elementAt(i) + " %>\" >"
	// + "</td>\n");
	// lFileJSP.write("\t\t\t\t<td class=\"l\"><input value=\"<%=l" + mNomeTabella + ".getAnno"
	// + mNomiJavaCampiDAO.elementAt(i)
	// + "() %>\" type=\"text\" size=\"4\" maxlength=\"4\" name=\"<%= ICostanti"
	// + mNomeTabella + ".CAMPO_ANNO_" + mNomiCampiDAO.elementAt(i) + " %>\" >"
	// + "</td>\n");
	// lFileJSP.write("\t\t</tr>\n");
	// }
	// }
	//
	// lFileJSP.write("\t\t\t\n</table>\n\t\t</form>\n\t</body>\n</html>");
	// lFileJSP.flush();
	// lFileJSP.close();
	//
	// // --------------------------------------------------------------------
	// // --------------------------------------------------------------------
	//
	// lFileJSP = new PrintWriter(
	// new BufferedWriter(new FileWriter(lNomeFile + "Dettaglio" + mNomeTabella + ".jsp")));
	//
	// lFileJSP.write("<%@ page import=\"f3b.web.IWebConstants\"%>\n");
	// lFileJSP.write("<%@ page import=\"f3b.util.DateUtils\"%>\n");
	// lFileJSP.write("<%@ page import=\"" + mPackage + ".model." + mNomeTabella + "Model\"%>\n");
	// lFileJSP.write("<%@ page import=\"" + mPackage + ".action.ICostanti" + mNomeTabella + "\"%>\n");
	//
	// String lModel = mNomeTabella.toLowerCase();
	//
	// lFileJSP.write("<jsp:useBean id=\"" + mNomeTabella.toLowerCase() + "\" scope=\"request\" class=\""
	// + mPackage + ".model." + mNomeTabella + "Model\"/>\n");
	//
	// lFileJSP.write("<html>\n<head>\t\t\n");
	// lFileJSP.write("<title>[S.I.A.P.] - Dettaglio " + mNomeTabella + " </title>\n");
	// lFileJSP.write(
	// "\n<link rel=\"STYLESHEET\" type=\"text/css\" href=\"<%=IWebConstants.PG_STYLE%>\">\n");
	// lFileJSP.write("\n<script language=\"JavaScript\" src=\"/html/conferma.js\"></script>\n");
	// lFileJSP.write("\n</head>\n\n");
	//
	// lFileJSP.write("\n\t\t<body class=\"corpo\">\n");
	//
	// lFileJSP.write("<FORM name=\"comandi\" >\n");
	// lFileJSP.write(" <table>\n");
	// lFileJSP.write(" <tr>\n");
	// lFileJSP.write(" <td class=\"LBG\">\n");
	// lFileJSP.write(" <font class=\"label\">Funzione :</font>&nbsp;\n");
	// lFileJSP.write(" <font class=\"campo\">Dettaglio " + mNomeTabella + "</font>\n");
	// lFileJSP.write(" </td>\n");
	// lFileJSP.write(" <td class=\"LBG\">\n");
	// lFileJSP.write(" <jsp:include page=\"<%=IWebConstants.PG_TOOLBAR_HEADER%>\">\n");
	// lFileJSP.write(" <jsp:param name=\"CampoIdEntita\" value=\"<%=ICostanti" + mNomeTabella
	// + ".CAMPO_" + mKey + "%>\" />\n");
	// lFileJSP.write(" <jsp:param name=\"ValoreIdEntita\" value=\"<%="
	// + mNomeTabella.toLowerCase() + ".get" + mKeyJava + "()%>\" />\n");
	// lFileJSP.write(" </jsp:include>\n");
	// lFileJSP.write(" </td>\n");
	// lFileJSP.write(" </tr>\n");
	// lFileJSP.write(" </table>\n");
	// lFileJSP.write("</FORM>\n");
	// lFileJSP.write("\t\t <table cellspacing=4 cellpadding=4>\n");
	//
	// for (int i = 0; i < mNomiCampiDAO.size(); i++) {
	// if (mTipoJavaCampiDAO.elementAt(i).toString().compareTo("Date") == 0) {
	// lFileJSP.write("\t\t<tr>\n");
	// lFileJSP.write("\t\t\t\t<td class=\"l\">" + mNomiJavaCampiDAO.elementAt(i) + "</td>\n");
	// lFileJSP.write("\t\t\t\t<td class=\"l\">");
	// lFileJSP.write("<font class=\"campo\">");
	// lFileJSP.write("<%=DateUtils.getDateToString(" + lModel + ".get"
	// + mNomiJavaCampiDAO.elementAt(i) + "(),\"dd-MM-yyyy\")%> ");
	// lFileJSP.write("</font>");
	// lFileJSP.write("</td>\n");
	// lFileJSP.write("\t\t</tr>\n");
	// } else {
	// lFileJSP.write("\t\t<tr>\n");
	// lFileJSP.write("\t\t\t\t<td class=\"l\">" + mNomiJavaCampiDAO.elementAt(i) + "</td>\n");
	// lFileJSP.write("\t\t\t\t<td class=\"l\">");
	// lFileJSP.write("<font class=\"campo\">");
	// lFileJSP.write("<%=" + lModel + ".get" + mNomiJavaCampiDAO.elementAt(i) + "() %>");
	// lFileJSP.write("</font>");
	// lFileJSP.write("</td>\n");
	// lFileJSP.write("\t\t</tr>\n");
	// }
	//
	// }
	// lFileJSP.write("\t\t</table>\t</body>\n</html>\n");
	// lFileJSP.flush();
	// lFileJSP.close();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// Dalla Tabella mi ricavo i campi Java
	// private static String getJavaString(String aString) throws Exception {
	// try {
	// String lTemp = aString.toLowerCase();
	//
	// int indNewWord = 0;
	//
	// lTemp = lTemp.substring(0, 1).toUpperCase() + lTemp.substring(1);
	//
	// String lSt = new String(lTemp);
	//
	// int i = 0;
	//
	// while (indNewWord >= 0) {
	// indNewWord = lSt.indexOf('_', indNewWord + 1);
	//
	// if (indNewWord < 1)
	// break;
	//
	// lTemp = lTemp.substring(0, indNewWord - i)
	// + lSt.substring(indNewWord + 1, indNewWord + 2).toUpperCase()
	// + lSt.substring(indNewWord + 2);
	//
	// i++;
	// }
	// return lTemp;
	//
	// } catch (Exception e) {
	// throw e;
	// }
	// }

}