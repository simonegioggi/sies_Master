<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>

<jsp:useBean id="udienzasige" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>

<html>
	<head>
  	<title>Elenco Udienza Collegiale</title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
	</head>

<body class="corpo">
	<FORM method="POST" action="Main.jsp" name="RicercaUdienzaCollegialeSige">
  	<table>
    	<tr>
      	<td class="LBG">
					<a href="Javascript:window.print();">
						<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
					</a>
				</td>
      	<td class="LBG">
					<font class="label">Funzione :</font>&nbsp;&nbsp;
        	<font class="campo">Elenco Udienza Collegiale</font>
      	</td>
    	</tr>
  	</table>
  <% 
  	 //=============================================== 
     // Include della jsp che gestisce la paginazione 
     //=============================================== 
  %>
  <%
  if (tipo_ricerca.equals("paginata")) { %>
    <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <%}
  %>

<div>
	<br>
  <table align="center">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Data Udienza</td>
     <!--  ELIMINO IL NUMERO COLLEGIO PER RICHIESTA 11.2.1 -->
     <!--  <td class="int">Collegio</td> -->
      <td class="int">Sezione</td>
      <!--  aggiungo composizione del COLLEGIO PER RICHIESTA 11.2.1 -->
      <td class="int" width=30%>Composizione</td>
       <!--  aggiungo firmatario Udienza del COLLEGIO PER RICHIESTA 11.2.1 -->
      <td class="int" width=30%>Presidente Firmatario Udienza</td>
      <td class="int">Orario Inizio</td>
      <td class="int">Orario Fine</td>
      <td class="int">Azioni</td>
    </tr>
    <%
      Iterator itx = udienzasige.iterator();
      while ( itx.hasNext()) {
        UdienzaSigeModel lUdienzaSige = (UdienzaSigeModel)itx.next();
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class=c>&nbsp;
      	<%=StringUtils.toStringJSP(DateUtils.getDateToString(lUdienzaSige.getDataUdienza(),"dd-MM-yyyy"),"&nbsp;")%>
			</td>
<%--       <td class=c>&nbsp;
      	<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getCodCollegio(),"&nbsp;")%>
			</td> --%>
      <td class=c>&nbsp;
      
      	<%
			if(lUdienzaSige.getCollegio()!= null && lUdienzaSige.getCollegio().getSezione()!= null){
					   
	  	%>
	  	
      	<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getSezione().getDescrizione(),"&nbsp;")%>
		
		<%
				}
  
		%> 
			
			</td>
			
	  <!--  aggiungo composizione del COLLEGIO PER RICHIESTA 11.2.1 -->
      <td class=c>&nbsp;
	  	<%
			if(lUdienzaSige.getCollegio()!= null && lUdienzaSige.getCollegio().getCollegioMagistrati()!= null){
				for(int i=0; i<lUdienzaSige.getCollegio().getCollegioMagistrati().length; i++){				   
	  	%>
						
			<%=i==0 ? "<font class=\"cVerde\">" : ""%>
			<%=lUdienzaSige.getCollegio().getCollegioMagistrati()[i].getMagistrato().getCognome()%>&nbsp;
			<%=lUdienzaSige.getCollegio().getCollegioMagistrati()[i].getMagistrato().getNome()%>
			<%=i==0 ? "</font>" : ""%>
			<br>
		<%
				}
			} 
		%>            
	  </td>      
	  
	  <td class=c>&nbsp;
                
      	<%
			if(lUdienzaSige.getCollegio()!= null ){
					   
	  	%>
	  	
      	<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getDescrMagistratoPresidente(),"--")%>
		
		<%
		} 
		%>

	  </td>
					
      <td class=c>&nbsp;
      	<%=StringUtils.toStringJSP(lUdienzaSige.getOraInizio(),"--")%>:
      	<%=StringUtils.toStringJSP(lUdienzaSige.getMinInizio(),"--")%>
			</td>
			<td class=c>&nbsp;
				<%=StringUtils.toStringJSP(lUdienzaSige.getOraFine(),"--")%>:
				<%=StringUtils.toStringJSP(lUdienzaSige.getMinFine(),"--")%>
			</td>
      <td class=c>
      <%
       String modificabile = "SI";
       // inserire qui i criteri in base ai quali il campo è modificabile
      %>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lUdienzaSige.getIdUdienzaSige()%>" />
					<jsp:param name="TornaQui" value="0" />
        </jsp:include>
      </td>
    </tr>
    <% } // end while su iterator %>
  </table>
</div>
</FORM>
</body>
</html>