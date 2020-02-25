<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.siep.penasospesa.action.ICostantiPenaSospesa" %>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="IdFascCorrente" scope="request" class="java.lang.String" />
<jsp:useBean id="StrCodiceDistrettoUtente" scope="request" class="java.lang.String" />
<jsp:useBean id="ufficiAltroDistretto" scope="request" class="java.util.Vector" />
<jsp:useBean id="ufficiDistretto" scope="request" class="java.util.Vector" />
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="StrNomeSoggettoAlias" scope="request" class="java.lang.String" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="strTipoRicerca" scope="request" class="java.lang.String" />

<%
  String lCodTipoUfficio = UtenteConnesso.getUfficioUtente().getCodTipoUfficio();
  boolean isSIUS = (lCodTipoUfficio.compareToIgnoreCase("TDS") == 0 || lCodTipoUfficio.compareToIgnoreCase("UDS") == 0) ? true : false;

  Iterator itxControlla = fascicoli.iterator();
  String lprocAltreBDI = "N";
  String lproc = "S";

    while ( itxControlla.hasNext())
    {
         FascicoloSiepModel Contollafascicolo = (FascicoloSiepModel)itxControlla.next();
         if(!Contollafascicolo.getCodDistretto().equals(StrCodiceDistrettoUtente))
         {
           lprocAltreBDI="S";
         }
          else
         {
           lproc="N";

         }
    }
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Procedimento</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">

      function controlla()
      {
<%
        boolean esistonoDati = false;
        if( !fascicoli.isEmpty() )
          esistonoDati = true;
%>
        if(<%=!esistonoDati%>)
        {
          alert("Nessun dato presente");
          window.parent.close();
        }
      }

      function insertIT( GiornoAtto,MeseAtto,AnnoAtto,
      					 GiornoIrrevo,MeseIrrevo,AnnoIrrevo,
      					 AnnoRGNR,NumeroRGNR,
      					 AnnoRegGen,NumeroRegGen,
      					 GiornoSentenza,MeseSentenza,AnnoSentenza,
      					 AnnoSen,NumeroSen,
                         CodTipoAutoritaEmittente,CodLuogoEmittente,NumSezione)            						
      {
      	formname = '<%=request.getParameter("formname")%>';
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value=GiornoAtto;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value=MeseAtto;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_ARRIVO_ATTO%>.value=AnnoAtto;

        window.parent.opener.document.<%=request.getParameter("formname")%>.giornoIrrevocabilita.value=GiornoIrrevo;
        window.parent.opener.document.<%=request.getParameter("formname")%>.meseIrrevocabilita.value=MeseIrrevo;
        window.parent.opener.document.<%=request.getParameter("formname")%>.annoIrrevocabilita.value=AnnoIrrevo;

        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_NUMERO_RGNR_REVOCA%>.value=NumeroRGNR;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_ANNO_RGNR_REVOCA%>.value=AnnoRGNR;

        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_NUMERO_RegGen_REVOCA%>.value=NumeroRegGen;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_ANNO_RegGen_REVOCA%>.value=AnnoRegGen;

        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>.value=GiornoSentenza;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_SENTENZA_REVOCA%>.value=MeseSentenza;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_SENTENZA_REVOCA%>.value=AnnoSentenza;
        
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_ANNO_SENTENZA_REVOCA%>.value=AnnoSen;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_NUMERO_SENTENZA_REVOCA%>.value=NumeroSen;

        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO%>.value=CodTipoAutoritaEmittente;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_COD_LUOGO_SENTENZA_REVOCA%>.value=CodLuogoEmittente;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_SEZIONE_SENTENZA_REVOCA%>.value=NumSezione;

        window.parent.opener.document.<%=request.getParameter("formname")%>.flagDaLista.value='SI';
        window.parent.close();
    
      }     // chiude InsertIT                            
 
  	</script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Procedimenti di :</font></td>
      <td class="LBG">
        <a href="javascript:history.go(-1);">
         <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>
<table width ="100%">
<%

if(StrNomeSoggettoAlias.length() > 0)
 {%>
   <tr>
    	<td class="l" >Soggetto: &nbsp;
      		<font class="campo">
         		<%=StrNomeSoggettoAlias%>
      		</font>
    	</td>
    </tr>
    <tr>
    	<td class="l" >Alias di: &nbsp;
    		<font class="campo">
          		<%=soggetto.getCognome() +" " +soggetto.getNome()%>
       		</font>
    	</td>
   </tr>
<%}
  else
  {%>
   <tr>
    	<td class="l" >Soggetto: &nbsp;
      		<font class="campo">
          		<%=soggetto.getCognome() +" " +soggetto.getNome()%>
      		</font>
    	</td>
   </tr>
<%}%>
  
  <tr>
<%
  if(soggetto.getSesso().equals("M"))
  {
%>
    	<td class="l" >Nato il:  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%
  }
  else
  {
%>
    	<td class="l" >Nata il:  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
<%
  }
%>
  			<font class="campo"><%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%>
  			</font>&nbsp;&nbsp; in:  &nbsp;&nbsp;&nbsp;
<%
  if (soggetto.getDescrComuneNascita().compareTo("-")==0)
  {
%>
    		<font class="campo"><%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)&nbsp; 
			</font>
<%
  }
  else
  {
%>
    		<font class="campo"><%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)&nbsp;
			</font>
<%
  }
%>
		</td>	
  </tr>
</table>
<table cellspacing=2 cellpadding=2>
<%
  if((lproc.equals("N") ))
  {
%>
    <tr>
      <td class ="Titolo" colspan=8>
      	<%if(strTipoRicerca != null && strTipoRicerca.equals("ufficio")) 
        	{%>
      			Elenco Procedimenti associati al soggetto dell' ufficio
      	  <%}
      	    else if (strTipoRicerca != null && !strTipoRicerca.equals("ufficio"))
      	    {%>
      	    	Elenco Procedimenti associati al soggetto del distretto
      	  <%}%>  
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td>
        <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="int">Data Titolo Esecutivo</td>
      <td class="int">Autorità Titolo Esecutivo</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Numero SIEP</td>
      <td class="int">Ufficio Esecuzione</td>
      <td class="int">Data di Iscrizione</td>
      <td class="int">Stato del Procedimento</td>
      <td class="int">Azioni</td>
    </tr>
<%
  }
	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	//siesLogger.info("FASCICOLO SIZE-> " + fascicoli.size());

   Iterator itx = fascicoli.iterator();
   Iterator itxUffDistr = ufficiDistretto.iterator();
   String annoreggen=null;
   String numreggen=null;
   
   String reg = "";

    while ( itx.hasNext())
    {
      FascicoloSiepModel fascicolo = (FascicoloSiepModel)itx.next();
// Se utente SIUS non vengono visualizzati i fascicoli nello stato ISCRITTO (02)
	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.info("IdFascCorrente-> " + IdFascCorrente);
	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.info("Ifascicolo.getIdFascicoloSiep()-> " + fascicolo.getIdFascicoloSiep());
	  if(fascicolo.getIdFascicoloSiep().toString().equals(IdFascCorrente) )continue;
      if (!isSIUS || fascicolo.getCodStatoFascicolo().compareTo("02") != 0)
      {
        if(fascicolo.getCodDistretto().equals(StrCodiceDistrettoUtente))
        {
          UfficioModel lUfficio = (UfficioModel)itxUffDistr.next();
  			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  			//siesLogger.info("FASCICOLO -> " + fascicolo);
  			
          //SentenzaModel sent=fascicolo.getSentenza();



			if (fascicolo.getSentenza().getAnnoRegeCap() != null) {
				reg = "CAP";
				annoreggen = fascicolo.getSentenza().getAnnoRegeCap() + "";
				numreggen = fascicolo.getSentenza().getNumeroRegeCap() + "";
			}
			if (fascicolo.getSentenza().getAnnoRegeCas() != null) {
				reg = "CAS";
				annoreggen = fascicolo.getSentenza().getAnnoRegeCas() + "";
				numreggen = fascicolo.getSentenza().getNumeroRegeCas() + "";
			}
			if (fascicolo.getSentenza().getAnnoRegeDib() != null) {
				reg = "DIB";
				annoreggen = fascicolo.getSentenza().getAnnoRegeDib() + "";
				numreggen = fascicolo.getSentenza().getNumeroRegeDib() + "";
			}
			if (fascicolo.getSentenza().getAnnoRegeCasap() != null) {
				reg = "CASAP";
				annoreggen = fascicolo.getSentenza().getAnnoRegeCasap() + "";
				numreggen = fascicolo.getSentenza().getNumeroRegeCasap() + "";
			}
			if (fascicolo.getSentenza().getAnnoRegeGip() != null) {
				reg = "GIP";
				annoreggen = fascicolo.getSentenza().getAnnoRegeGip() + "";
				numreggen = fascicolo.getSentenza().getNumeroRegeGip() + "";
			}
			// MEV_66: aggiunte quattro nuove proprietà
			if (fascicolo.getSentenza().getAnnoRegeGup() != null) {
				reg = "GUP";
				annoreggen = fascicolo.getSentenza().getAnnoRegeGup() + "";
				numreggen = fascicolo.getSentenza().getNumeroRegeGup() + "";
			}
			if (fascicolo.getSentenza().getAnnoRegeCapsm() != null) {
				reg = "CAPSM";
				annoreggen = fascicolo.getSentenza().getAnnoRegeCapsm() + "";
				numreggen = fascicolo.getSentenza().getNumeroRegeCapsm() + "";
			}

			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("fascicolo data arrivo atto="+fascicolo.getDataArrivoAtto());
			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("fascicolo data irrevocabilità="+fascicolo.getDataIrrevocabilita());
%>
          <tr>
            <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td>
            <td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrTipoAutoritaEmittente()%></font> di <font class="label"><%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td>
            <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"dd-MM-yyyy")%></font></td>
            <td class="c"><font class="label"><%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%></font></td>
            <td class="c"><font class="label"><%=lUfficio.getCodTipoUfficio()%> di <%=lUfficio.getDescrComune()%> </font></td>
            <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
            <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getDescrStatoProcedimento())%>&nbsp;</font></td>
        		<td class="C">
            		<a href="Javascript:insertIT('<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataArrivoAtto(),"dd"))%>',
        		    							 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataArrivoAtto(),"MM"))%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataArrivoAtto(),"yyyy"))%>',
												 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"dd"))%>',
        		    							 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"MM"))%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"yyyy"))%>',            									 
            									 '<%=StringUtils.toStringJSP(fascicolo.getSentenza().getAnnoRegePm())%>',
            									 '<%=StringUtils.toStringJSP(fascicolo.getSentenza().getNumeroRegePm())%>',
            									 '<%=StringUtils.toStringJSP(annoreggen)%>',
            									 '<%=StringUtils.toStringJSP(numreggen)%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd"))%>',
        		    							 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"MM"))%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"yyyy"))%>',
            									 '<%=StringUtils.toStringJSP(fascicolo.getSentenza().getAnnoSentenza())%>',
            									 '<%=StringUtils.toStringJSP(fascicolo.getSentenza().getNumeroSentenza())%>',
            									 '<%=StringUtils.toStringJSP(fascicolo.getSentenza().getCodTipoAutoritaEmittente())%>',
            									 '<%=StringUtils.cStrForJS(fascicolo.getSentenza().getDescrLuogoEmittente())%>',
            									 '<%=StringUtils.toStringJSP(fascicolo.getSentenza().getNumSezioneAutoritaEmittente())%>');">
              		<img align="middle" src="/images/fileselected.gif" border=0>
            		</a>
        		</td>                                        
          </tr>
<%
        }
      }
    }
%>
  </table>
  </form>
  <br>
  </body>
</html>