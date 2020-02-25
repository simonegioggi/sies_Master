<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siepe.richiesta.action.ICostantiRichiesta" %>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe" %>
<%@ page import="siap.siepe.richiesta.model.RichiestaModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<%@ page import="siap.sico.decodifiche.util.DecodificheUtils" %>

<jsp:useBean id="richieste" scope="request" class="java.util.Vector"/>
<%--jsp:useBean id="flags" scope="request" class="java.util.Collection"/--%>

<%
	String lFunAnnullaValidazione = "siap.siepe.richiesta.action.ActAnnullaValidazioneRichiesta";
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco Richieste </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"> <font class=label>Funzione :</font>
      <font class=campo> Elenco Richieste  </font> </td>
    </tr>
  </table>

	<br>
		<jsp:include page="<%=ICostantiFascicoloSiepe.PG_SINTESI_SOGG_FASCICOLI%>"/>
	<br>

<% 
	if( richieste.size() == 0 )
	{
%>

  <table>
  	<td class="LBG">
    	<font class="label"> Non ci sono richieste riferite al procedimento indicato.</font>
    </td>
  </table>
<%
	}
	else
	{
%>
  	<table>
    	<div align="center">
      	<tr>
        	<td class="int" width=20%>Tipo Richiesta</td>
        	<td class="int" width=5%>Data Richiesta</td>
        	<td class="int" width=10%>Presentata da</td>
        	<td class="int" width=10%>Richiesta Validata</td>
        	<td class="int" width=5%>Azioni</td>
      	</tr>
    	<!--  </div>-->
<%
  		Iterator itx = richieste.iterator();
  		while ( itx.hasNext())
  		{
    		RichiestaModel richiesta = (RichiestaModel)itx.next();
%>
    		<tr>
    			<td class="l"><%=richiesta.getDescrTipoRichiesta()%></td>
					<td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiesta.getDataRichiesta(),"dd-MM-yyyy"))%></td>
					<td class="l"><%=richiesta.getDescrTipoRichiedente()%></td>
					<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      				<%--<td class="l"><%=StringUtils.toStringJSP(richiesta.getFlagDocumentoRegistrato())%></td>--%>
					<td class="c">
        	<%
					if ( (richiesta.getFlagDocumentoRegistrato()!=null) && (richiesta.getFlagDocumentoRegistrato().compareTo("S")==0 ) )
        	{
           	// SVALIDAZIONE
           	//if (richiesta.getNumAllValidati() < 1 && lFunAnnullaValidaProvvedimento.length() > 1 && modificabile)
           	//{
        	%>
           	<a href="Javascript:annulla('Vuoi annullare la validazione della richiesta ? ','<%=lFunAnnullaValidazione%>' ,'<%=ICostantiRichiesta.CAMPO_ID_RICHIESTA%>','<%=richiesta.getIdRichiesta()%>');">
          		<img src="/images/TickRed.gif" alt = "Annulla validazione richiesta"  border="0">
          	</a>
        	<% 	//}
							//else
          		//{ %>
          		<!--	<img src="/images/TickRed.gif"> -->
        	<%	//}
					}
      		else
      		{
					%>
          	-
        	<%
        	}
					%>
					</td>

					<td class="c">
        		<jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          		<jsp:param name="CampoIdEntita" value="<%=ICostantiRichiesta.CAMPO_ID_RICHIESTA%>" />
          		<jsp:param name="ValoreIdEntita" value="<%=richiesta.getIdRichiesta()%>" />
        		</jsp:include>
      		</td>
    	</tr>
<%
  		} // End while
	} // End if 
%>
    </table>
  <!--  </form> -->
  <br>
  </body>
</html>