<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.udienza.model.UdienzaModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="udienze" scope="request" class="java.util.Vector"/>
<jsp:useBean id="TipoOrdinamento" scope="request" class="java.lang.String"/>
<jsp:useBean id="procuratore" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="assistente" scope="request" class="siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel"/>
<jsp:useBean id="presidente" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="giudiceR1" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="giudiceR2" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="esperto1" scope="request" class="siap.sius.esperto.model.EspertoModel"/>
<jsp:useBean id="esperto2" scope="request" class="siap.sius.esperto.model.EspertoModel"/>
<%-- MEV10-s3: aggiunto riferimento al codice tipo ufficio --%>
<jsp:useBean id="codTipoUfficio" scope="request" class="java.lang.String"/>

<% 
	Date[] campo_date = (Date[])request.getAttribute("campo_date");
%>



<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Udienza</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

 <%
 //Date data_ini =  (Date) range_date.get(0);
 //Date data_fine =  (Date) range_date.get(1);
 Date data_ini  = campo_date[0];
 Date data_fine = campo_date[1];
 %>
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
          <font class=label>Funzione :</font> <font class=campo> Elenco Udienze  </font>
      </td>
      <td class=c>
        <%if (data_ini != null) {%> dal <%=DateUtils.getDateToString(data_ini,"dd-MM-yyyy")%>   <%} if (data_fine != null) {%> fino al  <%=DateUtils.getDateToString(data_fine,"dd-MM-yyyy")%> <%}%></font>
      </td>
      <!--
         Gestione dellavisualizzazione in testa del tipo di ordinamento
         applicato:
         -> D-C ( Ordinamento per Data Udienza e Collegio ) default
         -> C-D ( Ordinamento per Collegio e Data Udienza )
      -->
      <td class=c>
        <%
          if (TipoOrdinamento != null)
          {
        %>
           Ordinate per :
        <%
           if( TipoOrdinamento.equalsIgnoreCase("D-C") )
           {
        %>
            Data Udienza e Collegio
        <%
           }
           else if( TipoOrdinamento.equalsIgnoreCase("C-D") )
           {
        %>
            Collegio e Data Udienza
        <%
           }
          }
        %>
      </font>
      </td>

    </tr></table>
    <table><tr>
    <td></td>
    <td class=c>
    	<%
          if (presidente != null || giudiceR1 != null || giudiceR2 != null || procuratore != null )
          {
        %>
           Ulteriori criteri di ricerca : Presidente : 
    </font>
    </td><td class=c>       
           <%=presidente.getCognome()%> &nbsp;<%=presidente.getNome() %>
     </font>
    </td>
    </tr> 
    <tr>
    <td></td>
    <td class=c>
    	   Giudice Relatore :   
    </font>
    </td><td class=c>
    	  <%=giudiceR1.getCognome()%>&nbsp;<%=giudiceR1.getNome()%>,&nbsp;&nbsp; <%=giudiceR2.getCognome() %>&nbsp;<%=giudiceR2.getNome() %>
    </td></tr>
    <tr>
    <td></td>
    <%-- MEV10-s3: cambiata etichetta in funzione dell'utenza collegata --%>
   	<% if ("TDSM".equals(codTipoUfficio)) { %>
   		<td class=c>Procuratore della Repubblica presso il Tribunale dei Minorenni :</td>
   	<% } else { %>
   		<td class=c>Procuratore Generale :</td>
   	<% } %>
    <td class=c>
    		<%=procuratore.getCognome() %>&nbsp;<%=procuratore.getNome() %>
    </td></tr>
    <tr>
    <td></td>
    <td class=c>
    		Esperti :   
    </font>
    </td><td class=c>
    		<%=esperto1.getCognome() %>&nbsp;<%=esperto1.getNome() %>,&nbsp;<%=esperto2.getCognome()%>&nbsp;<%=esperto2.getNome()%>
    </td></tr>
    <tr>
    <td></td>
    <td class=c>
    		Assistente Giudiziario :
    </font>
    </td>
    <td class=c>
    		<%=assistente.getCognome()%>&nbsp;<%=assistente.getNome() %>
    </td>
    </tr>   
         <%
           }  
    	 %>
    
    
    
  </table>

  <br>

  <table>
  	<div align=center>
    	<tr>
      	<td class="int" width=20%>Data Udienza</td>
      	<td class="int" width=30%>Presidente</td>
      	<td class="int" width=30%>Giudice Relatore</td>
      	<td class="int" width=30%>Esperti</td>
      	<td class="int" width=5%>Coll.</td>
      	<td class="int" width=10%>Orario Inizio</td>
      	<td class="int" width=10%>Orario Fine</td>
      	<td class="int" width=10%>Orario Fine Camera Consiglio</td>
      	<td class="int" width=10%>Azioni</td>
    	</tr>
		</div>
<%
  Iterator itx = udienze.iterator();
  while ( itx.hasNext())
  {
    UdienzaModel udienza = (UdienzaModel)itx.next();
%>
    <tr>
      <td class=c><%=DateUtils.getDateToString(udienza.getDataUdienza(),"dd-MM-yyyy")%></td>
      <td class=c><%=udienza.getDescrPresidente()%></td>
      <td class=c><%=udienza.getDescrGiudice1()%></td>
      <td class=c>
      	<table>
      		<tr>
      			<td class=c><%=udienza.getDescrIdEsperto1()%></td>
      		</tr>
      		<tr>
      			<td class=c><%=udienza.getDescrIdEsperto2()%></td>
      		</tr>
      	</table>
      </td>
      <td class=c><%=udienza.getNumCollegio()%></td>
      <td class=c><%=StringUtils.toStringJSP( udienza.getOraInizio(),"--")%>:<%=StringUtils.toStringJSP(udienza.getMinInizio(),"--")%></td>
			<td class=c><%=StringUtils.toStringJSP( udienza.getOraFine(),"--")%>:<%=StringUtils.toStringJSP( udienza.getMinFine(),"--")%></td>
			<td class=c><%=StringUtils.toStringJSP( udienza.getOraFineCC(),"--")%>:<%=StringUtils.toStringJSP( udienza.getMinFineCC(),"--")%></td>            
      <td class=c>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>" />
           <jsp:param name="ValoreIdEntita" value="<%=udienza.getIdUdienza()%>" />
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
  	</table>
  <br>
  </body>
</html>