<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sige.collegio.action.ICostantiCollegio" %>
<%@ page import="siap.sige.collegio.model.CollegioModel" %>

<jsp:useBean id="collegi" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Collegi</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">
  	<table>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();">
    				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
    			</a>
    		</td>
      	<td class="LBG">
      		<font class=label>Funzione :</font>
      		<font class=campo>Elenco Collegi</font> 
      	</td>
    	</tr>
  	</table>

  <br>

 	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
 
  <table>  
      <tr>
       <!--  <td class="int" width=5%>Numero</td> -->
        <td class="int" width=10%>Sezione</td>
        <td class="int" width=10%>Data Inizio Validità</td>
				<td class="int" width=10%>Data Fine Validità</td>
		<%-- 20190507 [SG]: aggiunto campo in estrazione --%>
		<td class="int" width=10%>Data Udienza</td>
				<td class="int" width=30%>Composizione</td>
        <td class="int" width=5%>Azioni</td>
      </tr>
<%
  Iterator itx = collegi.iterator();
while ( itx.hasNext()) {
    CollegioModel collegio = (CollegioModel)itx.next();
%>
    <tr>
		<%-- <td class=l>
      	<%=collegio.getCodCollegio()%>
      	</td> --%>
      <td class=l>
      	<%=StringUtils.toStringJSP(collegio.getSezione().getDescrizione(),"-")%>
      </td>
      <td class=l>
      		<%=StringUtils.toStringJSP(DateUtils.getDateToString(collegio.getDataInizioValidita(),"dd-MM-yyyy"),"-")%>
      </td>
      <td class=l>
      		<%=StringUtils.toStringJSP(DateUtils.getDateToString(collegio.getDataFineValidita(),"dd-MM-yyyy"),"-")%>
      </td>      
      	<%-- 20190507 [SG]: aggiunto campo in estrazione --%>
		<td class=l>
      		<%=StringUtils.toStringJSP(DateUtils.getDateToString(collegio.getDataUdienza(),"dd-MM-yyyy"),"-")%>
      	</td>
			<td class="L">      
<%
			if(collegio.getCollegioMagistrati()!= null){
				for(int i=0; i<collegio.getCollegioMagistrati().length; i++){
%>
						<%-- 20171004: [SG] aggiunto spazio tra nome e cognome magistrato --%>
						<%=i==0 ? "<font class=\"cVerde\">" : ""%>
						<%=collegio.getCollegioMagistrati()[i].getMagistrato().getCognome()%>&nbsp;
						<%=collegio.getCollegioMagistrati()[i].getMagistrato().getNome()%>
						<%=i==0 ? "</font>" : ""%>
						<br>
<%
				}
			} 
%>            
			</td>           	
		<td class=l>
       	<%--  <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=collegio.getIdCollegio()%>" />
          <jsp:param name="TornaQui" value="20" />
	        </jsp:include> --%>
		<!--  intervento sies 11.2.1 -->
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.collegio.action.ActLoadDettaglioCollegio&<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>=<%=collegio.getIdCollegio()%>&TornaQui=20&ElencoDecreti=false&IdEvento=null">
				<img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
			</a>
			<a href="Javascript:alert('La modifica deve essere fatta dalla funzione gestione udienza collegiale');">
				<img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
			</a>
			<a href="Javascript:alert('La cancellazione deve essere fatta dalla funzione gestione udienza collegiale');">
				<img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
			</a>
      </td>
    </tr>
<%
  }
%>
    </table>
  <br>
  </body>
</html>