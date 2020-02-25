<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.web.Action" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaFascicoliModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="soggettifascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="RequestForPaging" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui" 		scope="request" class="java.lang.String" />
<jsp:useBean id="AzioneChiamante" 	scope="request" class="java.lang.String" />
<jsp:useBean id="NumerazioneManualeMisureProvvFS"    scope="request" class="java.lang.String"/>

<%@page import="siap.sico.soggetto.model.SoggettoFascicoliModel"%>

<%
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//siesLogger.debug(" --XX-- RicercaSoggettoIscrizioneMisuraProvvisoria - NumerazioneManualeMisureProvvFS = "+NumerazioneManualeMisureProvvFS);
%>

<!-- 		 RicercaSoggettoIscrizioneMisuraProvvisoria		-->
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Soggetto - Misura Sicurezza Provvisoria </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
var node;
 function soggetto(id)
 {
		//alert(">>>>>>>> IdSoggetto = "+id)
	  document.elenco.<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>.disabled=false;
	  document.elenco.<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>.value=id;
 }

 function nuovo()
 {
	  document.elenco.<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>.disabled=true;
 }
</script>
  </head>

  <BODY class="corpo" onLoad="Javascript:nuovo()">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActLoadIscrizioneProcApplicazioneMisuraProvvisoria">
  <input type="hidden" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="">
  <input type="HIDDEN" name="<%=ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS%>" value="<%=NumerazioneManualeMisureProvvFS %>"> 
  
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
         <font class="label">Funzione :</font>&nbsp;
         <font class="campo">Iscrizione Procedimento Esecuzione Misura Sicurezza Applicazione Provvisoria - Elenco Soggetti</font>
        </td>
				<!-- BOTTONE DI RITORNO -->
				<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
			</tr>

    </table>
    <br>
 
<%if (!(RequestForPaging.equals("NO"))) {%>
	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<%}%>

<br>
<br>
<%
  String wCol1="25%";
  String wCol2="14%";
  String wCol3="18%";
  String wCol4="14%";
  String wCol5="14%";
  String wCol6="8%";
  String wCol7="5%";
  String wCol8="5%";


%>
    <table cellspacing=2 cellpadding=2 width="100%">
    <tr>
        <td class="l" colspan="5">Inserire un Nuovo Soggetto</td>
        <td class="c">          
           <input type="radio" title="Inserire un Nuovo Soggetto" checked name="radioins" onClick ="Javascript:nuovo()">
        </td>
    </tr>
    <tr>
      <td class="int" width="<%=wCol1%>">Cognome e Nome</td>
      <td class="int" width="<%=wCol2%>">Data di nascita</td>
      <td class="int" width="<%=wCol3%>">Luogo Nascita</td>
      <td class="int" width="<%=wCol4%>">Paternità</td>
      <td class="int" width="<%=wCol5%>">Maternità</td>
      <td class="int" width="<%=wCol6%>">Cod. CUI</td>
      <td class="int" width="<%=wCol7%>" Title="Dettaglio Soggetto">Dett.</td>
      <td class="int" width="<%=wCol8%>" Title="Selezione">Sel.</td>
    </tr>
    </table>
 
<%
	String modificabile = "NO";
    int jPA =0;
    Vector lFascicoli = new Vector();
    Iterator itx = soggettifascicoli.iterator();
    while ( itx.hasNext())
    {
    	FascicoloSiepModel lFasc = (FascicoloSiepModel)itx.next(); 
    	SoggettoModel soggetto = lFasc.getSoggetto();

%>
     	<table cellspacing=2 cellpadding=2 width="100%">
   			<tr>
   			<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
       		<td class=C width="<%=wCol1%>">
    				<%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%></font></td>
       		</td>
       		<td class=C width="<%=wCol2%>">
<%      		if ((soggetto.getDataNascita())==null || soggetto.getDataNascita().equals(""))
        		{%>-<%}
        		else 
        		{%>
          			<%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%>
        <%		}%>
       		</td>
       		<td class=C width="<%=wCol3%>">
      		<%if (soggetto.getDescrComuneNascita().compareTo("-")==0){
      				if(soggetto.getDescComuneNascitaEstero().length()>0) 
      				{%>
        				<%=soggetto.getDescComuneNascitaEstero()%>&nbsp;
        	    	<%if(soggetto.getDescrStatoNascita().length()>1) 
              		{%>
        		  		(<%=soggetto.getDescrStatoNascita().toUpperCase()%>)		
        				<%}%>
        	  <%}%>&nbsp; 
      		<%}else {%>
        		<%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)&nbsp;
      		<%}%>
       		</td>
       		<td class=C width="<%=wCol4%>">
						<%=soggetto.getPaternita()%>&nbsp;
       		</td>
       		<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
       		<td class=C width="<%=wCol5%>">
						<%=soggetto.getCognomeMadre()%>&nbsp;<%=soggetto.getNomeMadre()%>&nbsp;
       		</td>
       		<td class=C width="<%=wCol6%>">
						<%=StringUtils.toStringJSP(soggetto.getCodAfis())%>&nbsp;
       		</td>
       		<td class=C width="<%=wCol7%>">
			<jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
	           <jsp:param name="CampoAzioneChiamante" value="NomeAzione" />
	           <jsp:param name="ValoreAzioneChiamante" value="<%=AzioneChiamante%>" />
	           <jsp:param name="CampoIdEntita" value="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" />
	           <jsp:param name="ValoreIdEntita" value="<%=soggetto.getIdSoggetto()%>" />
	           <jsp:param name="Modificabile" value="<%=modificabile%>" />
        	</jsp:include>      		

     		</td>
     			<td class=C width="<%=wCol8%>">
         		<input type="radio" title="Selezione del Soggetto" name="radioins"  onClick ="Javascript:soggetto('<%=soggetto.getIdSoggetto() %>')">   
     			</td>
    		</tr>

	 	</table>
<% 	}%>
<br>
  <table>
     <tr>
      <td>
        <input class="bottone" type="submit" name="Avanti" value="Avanti >>>">
      </td>
    </tr>
  </table>
  </FORM>
  <br>

</body>
</html>