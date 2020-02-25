<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.beneficio.action.ICostantiBeneficio"%>
<%@ page import="siap.siep.beneficio.model.BeneficioModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel" %>


<jsp:useBean id="beneficio" scope="request" class="siap.siep.beneficio.model.BeneficioModel"/>
<jsp:useBean id="peneaccessorie" scope="request" class="java.util.Vector"/>
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>
<jsp:useBean id="lPenaResMod"    	 scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>

<%
	// Gestione funzione SIGE
	boolean modoSIGE = false;
	if (modo != null && modo.equalsIgnoreCase("SIGE"))
		modoSIGE = true;

	FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
%>

<html>
<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<script language="JavaScript">
var aForm=null;
 function Verify()
  {
   alert("La funzione di Iscrizione Guidata è stata Interrotta");
   aForm=document.getElementById("Abbandona");
    Disabilita();
  }

 function DisabilitaM()
  {

   aForm=document.getElementById("Misura");
    Disabilita();
  }

 function Disabilita()
  {
    if (aForm==null)
       aForm=document.getElementById("Beneficio");

    document.Abbandona.A.disabled = true;
    document.Misura.M.disabled = true;
    document.Beneficio.B.disabled = true;

   aForm.submit();
  }
</script>
<%}%>
<head>
<title>[S.I.E.S.] - Dettaglio Beneficio Indulto</title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>


		<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Beneficio Indulto</font>
      </td>
  				<%    
 				if(modoSIGE)
				{
					String   lModificabile = (String)request.getAttribute("Modificabile");
					String   lCancellabile = (String)request.getAttribute("Cancellabile");
				 %>     
				      <td class="LBG">
				          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
	          				<jsp:param name="CampoIdEntita" value="<%=ICostantiBeneficio.CAMPO_ID_BENEFICIO%>" />
	          				<jsp:param name="ValoreIdEntita" value="<%=beneficio.getIdBeneficio()%>" />
				          <jsp:param name="Modificabile" value="<%=lModificabile%>"/>
				          <jsp:param name="Cancellabile" value="<%=lCancellabile%>"/>
				        </jsp:include>
				     </td> 

  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
				     
				<%} else { %>          
      <td class="LBG">
        <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_GESTIONE_FASCICOLO_VALIDATO%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiBeneficio.CAMPO_ID_BENEFICIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=beneficio.getIdBeneficio()%>" />
          <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
        </jsp:include>
      </td>
      <%} %>
    </tr>
  </table>

	<br>
  <%if(!modoSIGE){%>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <%} else {%>
	   	<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
		 	<jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <%}%>
  <br>

</FORM>
		 <table cellspacing=2 cellpadding=2>
		<tr>
				<td class="l">Natura Beneficio</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrNaturaBeneficio()) %></font></td>
		</tr>
		<tr>
				<td class="l">Tipologia Beneficio</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrTipoBeneficio()) %></font></td>
		</tr>
		<tr>
				<td class="l">Provvedimento di Concessione</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrDpr()) %></font></td>
		</tr>	
        <tr>
				<td class="l">Applicazione del beneficio</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrSottotipoBeneficio()) %></font></td>

		</tr>				
        <tr>
				<td class="l">Reclusione</td>
				<td class="l">
				<%
				if (beneficio.getNumAnniReclusione()!=null || beneficio.getNumMesiReclusione()!=null || beneficio.getNumGiorniReclusione()!=null ||
				    beneficio.getImportoMulta()!=null)
					
				{
				%>
					Anni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumAnniReclusione(),"0") %></font>&nbsp;
					Mesi <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumMesiReclusione(),"0") %></font>&nbsp;
					Giorni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumGiorniReclusione(),"0") %></font>
					Multa <font class="campo"><%=StringUtils.toEuroFormat(beneficio.getImportoMulta()) %></font>

				<%
				} else
				{ %>
					-
			  <%}%>
				</td>
		</tr>
        <tr>
				<td class="l">Arresto</td>
				<td class="l">
				<%
				if (beneficio.getNumAnniArresto()!=null || beneficio.getNumMesiArresto()!=null || beneficio.getNumGiorniArresto()!=null ||
				    beneficio.getImportoAmmenda()!=null)
				{
				%>
					Anni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumAnniArresto(),"0") %></font>&nbsp;
					Mesi <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumMesiArresto(),"0") %></font>&nbsp;
					Giorni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumGiorniArresto(),"0") %></font>
					Ammenda <font class="campo"><%=StringUtils.toEuroFormat(beneficio.getImportoAmmenda()) %></font>
				<%
				} else
				{ %>
					-
			  <%}%>
				</td>
		</tr>		
		<tr>
				<td class="l">Note</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getNote()) %></font></td>
		</tr>		
		


		</table>

<%
      if(peneaccessorie != null && !peneaccessorie.isEmpty())
      {
%>   	  
       <table width="90%">      
    	 <tr><td class="titolo" colspan=10>Pena Accessoria</td></tr>     	  
<%     
       Iterator iter = peneaccessorie.iterator();
       while (iter.hasNext()) 
       {
    	   PenaAccessoriaModel lPenAcMod = (PenaAccessoriaModel) iter.next();
%> 
        <tr>
          <td class="c">
            <%=StringUtils.toStringJSP(lPenAcMod.getDescrTipoPenaAccessoria(), "-")%>
          </td>
          <td class="l">
<%if(lPenAcMod.getDurata() != null && !lPenAcMod.getDurata().equals("-")) {%>          
            <%=StringUtils.toStringJSP(lPenAcMod.getDescrDurata(),"-")%> 
<%}else{ %>   
			Anni <font class="campo"><%=StringUtils.toStringJSP(lPenAcMod.getNumAnni(),"0") %></font>&nbsp;
			Mesi <font class="campo"><%=StringUtils.toStringJSP(lPenAcMod.getNumMesi(),"0") %></font>&nbsp;
			Giorni <font class="campo"><%=StringUtils.toStringJSP(lPenAcMod.getNumGiorni(),"0") %></font>

<%}%>         
          </td>
        </tr>     
<%
       }
%>       
       </table> 
<%
      }	

%>

 <%
    // richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena 
    // paolo cherubini lunedi 11/10/2010

     if (lPenaResMod!=null  && lPenaResMod.getFlagValidato().equals("N"))  {%>
    <FORM name="calcolopena" >   
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActLoadCalcoloPena">
	<table>
	<tr>
		<td class="lRosso">
			<font class="lRosso">
				Attenzione! Primo calcolo della pena già effettuato. Per computare i benefici modificati,
				è necessario procedere nuovamente con il calcolo della pena
			</font>
		</td>
	</tr>
	 <tr>
       <td>
        <INPUT  class="bottone" type="submit" name="CALCOLA" value="Calcola Fine Pena" onClick="">
       </td>
     </tr>
	</table>
	</FORM>
 <%   }
    // fine a9/rr/075 
    
 %>
 
<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<table>
<tr>
<td class="lNoBord">
<FORM method="POST" name="Beneficio" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActGrigliaIscrizioneBenefici&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="submit" name="B" value="Altro Beneficio concesso" onclick="Javascript:Disabilita();">
 </FORM>
</td>

<td class="lNoBord">
<FORM method="POST" name="Misura" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadInserisciMisuraSicurezza&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="submit" name="M" value="Misura Sicurezza" onclick="Javascript:DisabilitaM();">
 </FORM>
</td>

<td class="lNoBord">
<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActRicercaBeneficio&lTipoFunzione=ritornodettaglio">
      <br><INPUT class="bottone" type="submit" name="A" value="Abbandona" onclick="Javascript:return Verify();">
 </FORM>
</td>
</tr>
</table>
<%}%>             
                             
	</body>
</html>