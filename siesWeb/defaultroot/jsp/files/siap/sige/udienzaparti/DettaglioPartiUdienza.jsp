<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collection"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>

<jsp:useBean id="UtenteConnesso"           scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="partiUdienza"             scope="request" class="java.util.Vector"/>
<jsp:useBean id="FascicoloSigeEsteso"      scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="codTipoParte"              scope="request" class="java.lang.String"/>
<jsp:useBean id="modificabile"             scope="request" class="java.lang.String"/>
<jsp:useBean id="idEventoUdienza"          scope="request" class="java.lang.String"/>
<jsp:useBean id="idUdienzaSige"            scope="request" class="java.lang.String"/>
<jsp:useBean id="idUdienzaProcedimentoSige"            scope="request" class="java.lang.String"/>

<%
  // Variabile utilizzata per distinguere se trattasi di Parte Offesa oppure Parte Civile
  String tipoParte = "";
  if (codTipoParte != null && codTipoParte.equalsIgnoreCase("O")){
	  tipoParte = "Offese";
  } else {
	  tipoParte = "Civili";
  }
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Parti associate ad un'Udienza</title>
    <script language="JavaScript" src="/html/conferma.js"></script>

<script language="JavaScript">
function myConfirm(a_action, a_entityname, a_entityvalue, a_other ) {
	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue +  a_other;
	if (window.confirm('Confermi la cancellazione?')) {
		window.location.href=str;
	}
}
</script>

  </head>

  <body class="corpo">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class=label>Funzione :</font> <font class=campo> Gestione Parti <%=tipoParte%></font> </td>
          <td class="LBG">
	           <jsp:include page="<%=ICostantiPartiUdienza.PG_TOOLBAR_HEADER_PARTI%>">
           		<jsp:param name="CampoIdEventoUdienza" value="<%=ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA%>" />
           		<jsp:param name="ValoreIdEventoUdienza" value="<%=idEventoUdienza%>" />
           		<jsp:param name="CampoIdUdienzaSige" value="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>" />
           		<jsp:param name="ValoreIdUdienzaSige" value="<%=idUdienzaSige%>" />
           		<jsp:param name="CampoIdUdienzaProcedimentoSige" value="<%=ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE%>" />
           		<jsp:param name="ValoreIdUdienzaProcedimentoSige" value="<%=idUdienzaProcedimentoSige%>" />
            	<jsp:param name="CampoCodTipoParte" value="<%=ICostantiPartiUdienza.CAMPO_COD_TIPO_PART%>" />
            	<jsp:param name="ValoreCodTipoParte" value="<%=codTipoParte%>" />
	            <jsp:param name="Modificabile" value="<%=modificabile%>"/>
	           </jsp:include>
          </td>
 
         <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>

	<jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>

<% 
  int numParteF = 0;
  int numParteG = 0;

  if (partiUdienza != null && partiUdienza.size() > 0) { 

  Iterator itxCount = partiUdienza.iterator();
  while ( itxCount.hasNext())
  {
	  AnagraficaPartiUdienzaModel lParti = (AnagraficaPartiUdienzaModel)itxCount.next();
	  if(lParti.getCodParte() != null && lParti.getCodParte().equals("F")){
		  numParteF += 1; 
	  } else {
		  numParteG += 1; 
	  }
  }
  
  // Tabella Persona Fisica 
  if(numParteF > 0){
%>
	  <table>
     <div align=center>
        <tr><td class="Titolo" colspan="6">Persona Fisica</td></tr>
        <tr>
          <td class="int" width=20%>Nominativo</td>
          <td class="int" width=20%>Comune di Nascita</td>
          <td class="int" width=16%>Data di Nascita</td>
          <td class="int" width=20%>Residenza</td>
          <td class="int" width=20%>Difensore</td>
          <td class="int" width=5%>Azioni</td>
        </tr>
      </div>
<%
  Iterator itxF = partiUdienza.iterator();
  while ( itxF.hasNext())
  {
	  AnagraficaPartiUdienzaModel lParti = (AnagraficaPartiUdienzaModel)itxF.next();

	  if(lParti.getCodParte() != null && lParti.getCodParte().equals("F")){
%>
   	<tr>
		<td class=c><%=lParti.getCognome()%>&nbsp;&nbsp;<%=lParti.getNome()%> </td>
<%

	  if(lParti.getCodComuneNascita() != null && !lParti.getCodComuneNascita().equals("-")){
%>     
      	<td class=c><%=StringUtils.toStringJSP(lParti.getDescComuneNascita())%></td>
<%
	  } else {
%>
  		<td class=c><%=StringUtils.toStringJSP(lParti.getDescComuneNascitaEstero())%></td>
<%		  
	  }
%>
      <td class=c>
<%
		if(lParti.getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(lParti.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-" +StringUtils.toStringJSP(lParti.getDataNascita(), "**")%>&nbsp;
<%
        }
%>
      </td>

      <td class=c> 
<%
		if(lParti.getResidenza() != null && lParti.getResidenza().getIndirizzo() != null)
        {
%>
          <%=lParti.getResidenza().getIndirizzo()%>&nbsp;
<%
			if(lParti.getResidenza().getDescrComune() != null && !lParti.getResidenza().getDescrComune().equals("-")){
%>          
	       	-&nbsp;<%=lParti.getResidenza().getDescrComune()%>&nbsp;
<%
			} 
			if(lParti.getResidenza().getCodProvincia() != null && !lParti.getResidenza().getCodProvincia().equals("-") ){
%>	       	
		       	(<%=lParti.getResidenza().getCodProvincia()%>)&nbsp;
<%
			}
        }
        else
        {
%>
         &nbsp;
<%
        }
%>
      </td>

<%
	  List lDifensori = lParti.getDifensori();
	  if(lDifensori != null && lDifensori.size() != 0)
	  {
%>
		  <td class=c>
<%
		  Iterator lIter = lDifensori.iterator();
	      while (lIter.hasNext())
	      {
	    	  PartiUdienzaDifensoreModel lDifens = (PartiUdienzaDifensoreModel)lIter.next();
%>  
		      <%=lDifens.getAvvocato().getCognome()%>&nbsp;&nbsp;<%=lDifens.getAvvocato().getNome()%>
              <br>
<%
      	  }
%>
		  </td>
<%	      
	  } else {
%>
		  <td class=c>&nbsp;</td>
<%		  
	  }
%>
     <td class=c>
		<jsp:include page="<%=ICostantiPartiUdienza.PG_BUTTONS_MORE_PARAMETERS_PARTI%>">
	        <jsp:param name="CampoIdEventoUdienza" value="<%=ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA%>" />
	        <jsp:param name="ValoreIdEventoUdienza" value="<%=idEventoUdienza%>" />
			<jsp:param name="CampoIdUdienzaSige" value="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>"/>
	        <jsp:param name="ValoreIdUdienzaSige" value="<%=idUdienzaSige%>"/>
	        <jsp:param name="CampoIdUdienzaProcedimentoSige" value="<%=ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE%>" />
	        <jsp:param name="ValoreIdUdienzaProcedimentoSige" value="<%=idUdienzaProcedimentoSige%>" />
			<jsp:param name="CampoCodTipoParte" value="<%=ICostantiPartiUdienza.CAMPO_COD_TIPO_PART%>"/>
			<jsp:param name="ValoreCodTipoParte" value="<%=codTipoParte%>"/>
	        <jsp:param name="CampoIdEntita" value="<%=ICostantiPartiUdienza.CAMPO_ID_SOGGETTO%>" />
	        <jsp:param name="ValoreIdEntita" value="<%=lParti.getIdSoggetto()%>" />
		</jsp:include>
     </td>
    </tr>
<%
	  } // end if(lParti.getCodParte() != null && lParti.getCodParte().equals("F")){
  } // end while ( itxF.hasNext())
%>
    </table>
    <br>	
<%
  } // end if(numParteF > 0){
%>
<%
  // Tabella Persona Giuridica 
  if(numParteG > 0){
%>
  <table>
     <div align=center>
        <tr><td class="Titolo" colspan="7">Persona Giuridica</td></tr>
        <tr>
          <td class="int" width=20%>Denominazione</td>
          <td class="int" width=16%>Sede Legale</td>
          <td class="int" width=20%>Rappresentante Legale</td>
          <td class="int" width=16%>Comune di Nascita</td>
          <td class="int" width=16%>Data di Nascita</td>
          <td class="int" width=20%>Difensore</td>
          <td class="int" width=5%>Azioni</td>
        </tr>
      </div>
<%
  Iterator itxG = partiUdienza.iterator();
  while ( itxG.hasNext())
  {
	  AnagraficaPartiUdienzaModel lParti = (AnagraficaPartiUdienzaModel)itxG.next();

	  if(lParti.getCodParte() != null && lParti.getCodParte().equals("G")){
%>
    <tr>
		<td class=c><%=StringUtils.toStringJSP(lParti.getDenominazione())%>&nbsp;<%=StringUtils.toStringJSP(lParti.getRagSociale())%></td>
<%
	if(lParti.getIndSedeLegale() != null){
%>
      	<td class=c><%=StringUtils.toStringJSP(lParti.getIndSedeLegale())%></td>
<%
	} else {
%> 
 		<td class=c>&nbsp;</td>
<%
	}
%>      	
      	<td class=c><%=lParti.getCognome()%>&nbsp;&nbsp;<%=lParti.getNome()%></td>
<%	  
	if(lParti.getCodComuneNascita() != null && !lParti.getCodComuneNascita().equals("-")){
%>     
		<td class=c><%=StringUtils.toStringJSP(lParti.getDescComuneNascita())%></td>

<%
	} else {
%>
		<td class=c><%=StringUtils.toStringJSP(lParti.getDescComuneNascitaEstero())%></td>
<%		  
	}
%>

		<td class=c>
<%
  			if(lParti.getDataNascita() != null)
  			{
%>
    			<%=DateUtils.getDateToString(lParti.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
  			}
  			else
  			{
%>
    			<%="**-" +StringUtils.toStringJSP(lParti.getDataNascita(), "**")%>&nbsp;
<%
  			}
%>
		</td>
<%
	  List lDifensori = lParti.getDifensori();
	  if(lDifensori != null && lDifensori.size() != 0)
	  {
%>
		  <td class=c>
<%
		  Iterator lIter = lDifensori.iterator();
	      while (lIter.hasNext())
	      {
	    	  PartiUdienzaDifensoreModel lDifens = (PartiUdienzaDifensoreModel)lIter.next();
%>  
		      <%=lDifens.getAvvocato().getCognome()%>&nbsp;&nbsp;<%=lDifens.getAvvocato().getNome()%>
		      <br>
<%
      	  }
%>
		  </td>
<%	      
	  } else {
%>
		  <td class=c>&nbsp;</td>
<%		  
	  }
%>
     <td class=c>
		<jsp:include page="<%=ICostantiPartiUdienza.PG_BUTTONS_MORE_PARAMETERS_PARTI%>">
	        <jsp:param name="CampoIdEventoUdienza" value="<%=ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA%>" />
	        <jsp:param name="ValoreIdEventoUdienza" value="<%=idEventoUdienza%>" />
			<jsp:param name="CampoIdUdienzaSige" value="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>"/>
	        <jsp:param name="ValoreIdUdienzaSige" value="<%=idUdienzaSige%>"/>
	        <jsp:param name="CampoIdUdienzaProcedimentoSige" value="<%=ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE%>" />
	        <jsp:param name="ValoreIdUdienzaProcedimentoSige" value="<%=idUdienzaProcedimentoSige%>" />
			<jsp:param name="CampoCodTipoParte" value="<%=ICostantiPartiUdienza.CAMPO_COD_TIPO_PART%>"/>
			<jsp:param name="ValoreCodTipoParte" value="<%=codTipoParte%>"/>
	        <jsp:param name="CampoIdEntita" value="<%=ICostantiPartiUdienza.CAMPO_ID_SOGGETTO%>" />
	        <jsp:param name="ValoreIdEntita" value="<%=lParti.getIdSoggetto()%>" />
		</jsp:include>
     </td>
    </tr>
<%
	  } //if(lParti.getCodParte() != null && lParti.getCodParte().equals("G")){

  } // while ( itxG.hasNext())
%>
    </table>
<%
  } // end if(numParteG > 0){
%>    
    
<% } else { %>
  <br>
	<font class="campo"> Nessuna Parte associata all'Udienza.</font>
<% } %>

  </body>
</html>