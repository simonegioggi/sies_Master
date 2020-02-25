package f3b.web.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

// STUB : 20030206 - Dove sta la libreria (commons-beans).
import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
// Nuova versione
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import f3b.util.F3BException;
import f3b.util.F3BProperties;

/**
 * <p>
 * Title: MultipartContent
 * </p>
 * <p>
 * Description: Gestisce la request con contenuto <code>MultipartContent</code>.
 * </p>
 * <p>
 * Copyright: Bull Italia S.p.A. Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class MultipartContent {

	private HttpServletRequest mRequest;
	// private FileUpload mFileUpload;
	private ServletFileUpload mFileUpload;
	private Hashtable<String, FileItem> mHash = new Hashtable<String, FileItem>();

	private int mSizeMax = 0;
	private int mSizeThreshold = 0;
	private String mTempPath;

	/**
	 * Costruttore, inizializza i parametri necessari per il parse della request.
	 * <p>
	 * 
	 * @throws F3BException
	 */
	public MultipartContent() throws F3BException {

		this.mSizeMax = F3BProperties.getIntProperty("MultiPartContent.maxSize", 1000000);
		this.mSizeThreshold = F3BProperties.getIntProperty("MultiPartContent.sizeThreshold", 4096);
		this.mTempPath = F3BProperties.getProperty("MultiPartContent.tempPath");
		this.mFileUpload = new ServletFileUpload();
	}

	/**
	 * Costruttore di classe con la request come parametro.
	 * <p>
	 * 
	 * @param aRequest
	 *            request da impostare.
	 * @throws F3BException
	 */
	public MultipartContent(HttpServletRequest aRequest) throws F3BException {
		this();
		this.mRequest = aRequest;

	}

	/**
	 * Imposta l'attributo di classe request con quella esterna.
	 * <p>
	 * 
	 * @param aRequest
	 *            valore della request.
	 */
	public void setRequest(HttpServletRequest aRequest) {
		this.mRequest = aRequest;
	}

	/**
	 * Imposta la dimensione massima del file in upload.
	 * <p>
	 * 
	 * @param aSizeMax
	 *            valore della dimensione massima del file.
	 */
	public void setSizeMax(int aSizeMax) {
		this.mSizeMax = aSizeMax;
	}

	/**
	 * Imposta il valore di soglia per il file in upload.
	 * <p>
	 * 
	 * @param aSizeThreshold
	 *            valore della soglia.
	 */
	public void setSizeThreshold(int aSizeThreshold) {
		this.mSizeThreshold = aSizeThreshold;
	}

	/**
	 * Imposta il path di appoggio del file.
	 * <p>
	 * 
	 * @param aPath
	 *            path di appoggio.
	 */
	public void setTempPath(String aPath) {
		this.mTempPath = aPath;
	}

	// MEV10-s3: aggiunto recupero di proprietà
	/**
	 * Ritorna il valore del path di appoggio del file.
	 * <p>
	 * 
	 * @return il valore del path di appoggio del file.
	 */
	public String getTempPath() {
		return this.mTempPath;
	}

	public ServletFileUpload getFileUpload() {
		return this.mFileUpload;
	}

	public Hashtable<String, FileItem> getHash() {
		return this.mHash;
	}

	// FINE MEV10-s3

	/**
	 * Ritorna il valore della dimensione massima del file.
	 * <p>
	 * 
	 * @return il valore della dimansione max del file.
	 */
	public int getSizeMax() {
		return this.mSizeMax;
	}

	/**
	 * Ritorna il valore della dimensione soglia.
	 * <p>
	 * 
	 * @return il valore della dimensione soglia.
	 */
	public int getSizeThreshold() {
		return this.mSizeThreshold;
	}

	/**
	 * Metodo statico che verifica se la request passata è una <code>MultipartContent</code>.
	 * <p>
	 * 
	 * @param aRequest
	 *            parametro della request.
	 * @return lo stato logico.
	 */
	public static boolean isMultipartContent(HttpServletRequest aRequest) {

		return ServletFileUpload.isMultipartContent(aRequest);
	}

	/**
	 * Effettua il parse della request per l'individuazione dei campi della form.
	 * <p>
	 * 
	 * @throws F3BException
	 *             rilancia l'errore di eccezione.
	 */
	public void parseRequest() throws F3BException {
		try {
			mFileUpload.setSizeMax(mSizeMax);
			// mFileUpload.setFileSizeMax(mSizeThreshold);
			mFileUpload.setFileItemFactory(new DiskFileItemFactory(mSizeThreshold, new File(mTempPath)));

			// List lItems = this.mFileUpload.parseRequest( this.mRequest, this.mSizeThreshold,
			// this.mSizeMax, this.mTempPath );
			List<?> lItems = this.mFileUpload.parseRequest(this.mRequest);

			Iterator lItx = lItems.iterator();

			while (lItx.hasNext()) {
				FileItem lFi = (FileItem) lItx.next();
				this.mHash.put(lFi.getFieldName(), lFi);
			}
		} catch (FileUploadException fuex) {
			fuex.printStackTrace();
			throw new F3BException(fuex);
		}
	}

	/**
	 * Ritorna il valore del campo, prelevato dalla request.
	 * <p>
	 * 
	 * @param aKey
	 *            campo chiave.
	 * @return il valore desiderato.
	 */
	public String getParameter(String aKey) {
		FileItem lFi = (FileItem) this.mHash.get(aKey);
		return (lFi != null && lFi.isFormField() ? lFi.getString() : null);
	}

	/**
	 * Ritorna *******************
	 * <p>
	 * 
	 * @param aKey
	 *            campo chiave.
	 * @return il valore desiderato.
	 */
	public Hashtable getParameters() {
		Hashtable lHash = new Hashtable();

		Set keys = this.mHash.keySet();

		Iterator itx = keys.iterator();
		while (itx.hasNext()) {
			String key = (String) itx.next();
			FileItem lFi = (FileItem) this.mHash.get(key);
			if (lFi != null && lFi.isFormField()) {
				lHash.put(key, lFi.getString());
			}
		}

		return lHash;
	}

	/**
	 * Ritorna il contenuto del file contenuto in una request multipart. 20100423 - tale metodo è stato
	 * modifica per ritornare, nel caso il file non esiste quindi la size è == 0, valore null.
	 * <p>
	 * 
	 * @param aKey
	 *            campo chiave.
	 * @return il contenuto del file.
	 * @throws F3BException
	 */
	public InputStream getFile(String aKey) throws F3BException {
		InputStream lIs = null;

		try {
			FileItem lFi = (FileItem) this.mHash.get(aKey);
			// Si verifica se il file esiste.
			// 20100423 - Aggiunta condizione : lFi.getSize()>0.
			if (lFi != null && !lFi.isFormField() && lFi.getSize() > 0)
				lIs = lFi.getInputStream();

		} catch (IOException ioex) {
			ioex.printStackTrace();
			throw new F3BException(ioex);
		}

		return lIs;
	}

	/**
	 * da eliminare ! Ritorna il contenuto del file contenuto in una request multipart.
	 * <p>
	 * 
	 * @param aKey
	 *            campo chiave.
	 * @return il contenuto del file.
	 * @throws F3BException
	 */
	/*
	 * public InputStream getInputStream( String aKey ) throws F3BException { InputStream lIs = null; try {
	 * FileItem lFi = (FileItem)this.mHash.get( aKey ); if( lFi != null && !lFi.isFormField() ) lIs =
	 * lFi.getInputStream(); } catch( IOException ioex ) { ioex.printStackTrace(); throw new F3BException(
	 * ioex ); }
	 * 
	 * return lIs; }
	 */
	/**
	 * Ritorna il contenuto del file contenuto in una request multipart. 20100423 - tale metodo è stato
	 * modifica per ritornare, nel caso il file non esiste quindi la size è == 0, valore null.
	 * <p>
	 * 
	 * @param aKey
	 *            campo chiave.
	 * @return il contenuto del file.
	 * @throws F3BException
	 */
	public byte[] getBytes(String aKey) throws F3BException {
		byte[] lbyte = null;
		try {
			FileItem lFi = (FileItem) this.mHash.get(aKey);
			// Si verifica se il file esiste.
			// 20100423 - Aggiunta condizione : lFi.getSize()>0.
			if (lFi != null && !lFi.isFormField() && lFi.getSize() > 0)
				lbyte = lFi.get();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new F3BException(ex);
		}

		return lbyte;
	}

	/**
	 * 
	 * Ritorna il nome del file contenuto in una request multipart.
	 * <p>
	 * 
	 * @param aKey
	 *            campo chiave.
	 * @return il nome del file.
	 * @throws F3BException
	 */
	public String getName(String aKey) throws F3BException {
		String lName = null;

		// try
		// {
		FileItem lFi = (FileItem) this.mHash.get(aKey);
		if (lFi != null && !lFi.isFormField())
			lName = lFi.getName();
		// }
		// catch( IOException ioex )
		// {
		// ioex.printStackTrace();
		// throw new F3BException( ioex );
		// }
		return lName;
	}

	/**
	 * Ritorna lìestensione del file contenuto in una request multipart.
	 * <p>
	 * 
	 * @param aKey
	 *            campo chiave.
	 * @return il nome del file.
	 * @throws F3BException
	 */
	public String getExtName(String aKey) throws F3BException {
		String lExtName = null;

		// try
		// {
		FileItem lFi = (FileItem) this.mHash.get(aKey);
		if (lFi != null && !lFi.isFormField())
			lExtName = lFi.getName().substring(lFi.getName().lastIndexOf(".") + 1);
		// }
		// catch( IOException ioex )
		// {
		// ioex.printStackTrace();
		// throw new F3BException( ioex );
		// }
		return lExtName;
	}

	/**
	 * Ritorna il contenuto del file come un ByteArrayInputStream.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave paramentro della request.
	 * @return il contenuto del file come ByteArrayInputStream, se vuoto ritorna null.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public ByteArrayInputStream getFileByteArrayInputStream(String aKey) throws F3BException {
		ByteArrayInputStream lByteArrayIS = null;

		try {
			InputStream lInput = this.getFile(aKey);

			// Se è diverso da null esegue la conversione.
			if (lInput != null) {
				byte[] lBuffer = new byte[lInput.available()]; // Inizializza l'array di Byte.
				lInput.read(lBuffer); // Legge i dati dallo Stream e riempe l'array di Byte.
				lByteArrayIS = new ByteArrayInputStream(lBuffer); // Crea il ByteArrayInputStream.
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
			throw new F3BException(ioex);
		}

		return lByteArrayIS;
	}
}