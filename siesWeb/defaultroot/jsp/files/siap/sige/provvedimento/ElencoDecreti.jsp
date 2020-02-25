<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeModel"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="decreti"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="FascicoloSigeEsteso" 	scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="flag_valida" scope="request" class="java.lang.String"/>
<jsp:useBean id="isModificabile" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Lista Decreti SIGE</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">

  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Ricerca Deposito Decreti</font>
      </td>
    </tr>
   </table>
<br>

<%
boolean modificabile = true;
if (isModificabile != null && isModificabile.equalsIgnoreCase("NO"))
    modificabile = false;

String lFunAnnullaValidaProvvedimento = "";
String lFunAnnullaValidaAllegato = "";

if (flag_valida.equals(""))
    flag_valida="SI";
String isDepositato = "NO";
%>

<%
  if (FascicoloSigeEsteso != null)
  {
%>
   <table>
      <tr>
        <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
      </tr>
   </table>
<%
  } // endif FascicoloSigeEsteso
%>
<br>
<%
  if ( decreti.size() == 0 )
  {
%>
        <td class="LBG">
          <font class="label"> Non ci sono decreti allegati al fascicolo. </font>
        </td>
<%
  }else{
%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaAtti">
  <table width="96%">
  <div align=center>
    <tr>
      <td class="int" >Data emissione</td>
      <td class="int" >Tipo provvedimento</td>
      <td class="int" >Oggetti provvedimento</td>
      <td class="int" >Data Deposito</td>
<%
      if (flag_valida.equals("SI"))
      {
       	// Estrazione funzioni annulla Validazione
      	Collection lFunzFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
      	if( (lFunzFiglie != null) && (lFunzFiglie.size() != 0) )
      	{
        	Iterator lIterFunz = lFunzFiglie.iterator();
        	FunctionModel lFunz = null;
        	while(lIterFunz.hasNext())
        	{
          	lFunz = (FunctionModel)lIterFunz.next();
          	if(lFunz.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_LINK)  && lFunz.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA))
          	{
             	if (lFunz.getVisualizationOrder().intValue() == 1)
                  lFunAnnullaValidaProvvedimento = lFunz.getNameAction();
             	else if (lFunz.getVisualizationOrder().intValue() == 2)
                 	lFunAnnullaValidaAllegato = lFunz.getNameAction();
          	}
        	}
				}
%>
      	<td class="int" >Provv.<br>Validato</td>
		<%}%>
      <td class="int" >Azioni</td>
    </tr>
  </div>
<%
    Iterator itx = decreti.iterator();
    while ( itx.hasNext())
    {
    	ProvvedimentoSigeEventoModel lProvEve = (ProvvedimentoSigeEventoModel)itx.next();
%>
    	<tr>
      <td class="l">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvEve.getProvvedimento().getDataEmissione(),"dd-MM-yyyy"),"-") %>
      </td>
      <td class ="l">
<%		if(lProvEve.getProvvedimento().getCodTipoProvvedimentoSige()!=null) { %>      
				<%=StringUtils.toStringJSP(lProvEve.getProvvedimento().getDescrTipoProvvedimentoSige(),"-")%>
<%		} else {	%>
      	<%=StringUtils.toStringJSP(lProvEve.getProvvedimento().getDescrTipoProvvedimento(),"-")%>
<%			if ( Utils.isNullObj(lProvEve.getEventoNotifica().getEvento()) || 
					   Utils.isNullObj(lProvEve.getEventoNotifica().getEvento().getCodEsito()) )
    		{%>&nbsp;<%
    		}	else {	%>  
					<%=lProvEve.getEventoNotifica().getEvento().getDescrEsito()%>
				<%}%>      
			<%}%>      
			</td>
      <td class="l">
<%
			Vector lTenori = lProvEve.getTenoriEstesi();
			if (lTenori != null) {
				Iterator itx2 = lTenori.iterator();
				String lDescrTenorePrecedente = "";
				String lIdTenore = "";

				while ( itx2.hasNext())
 				{
					TenoreSigeModel lTenore = (TenoreSigeModel)itx2.next();
					if (lTenore != null &&  
						 !lTenore.getIdTenoreSige().toString().equalsIgnoreCase(lIdTenore) &&
						  lTenore.getDescrOggettoSige().compareTo(lDescrTenorePrecedente)!=0) 
					{
						lDescrTenorePrecedente = lTenore.getDescrOggettoSige();
%>   				<font class="label">
							-&nbsp;<%=lTenore.getDescrOggettoSige()%><br>
						</font>
<%				}
 				}
			}else{%>
   			<font class="label">-</font>
		<%}%>
     	</td>
     	<td class="c" ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvEve.getProvvedimento().getDataDeposito(),"dd-MM-yyyy"),"-")%></td>
<%
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				//siesLogger.debug(" --------  FlagDocumentoRegistrato() = " + lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() );
      	if (flag_valida.equals("SI") )
      	{%>
        	<td class="c">
        	<% if ((lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato()!=null) && (lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("S")==0) )
        	{%>
          	<img src="/images/TickRed.gif">
        <%}
        // ANNULLATO
      }else if(lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato()!=null && lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("A")==0)
      {%>
         <font class="cRosso">ANNULLATO</font>
		<%}else{%>-<%}%>
        </td>

       <td class="l">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lProvEve.getProvvedimento().getIdProvvedimentoSige()%>" />
          <jsp:param name="ElencoDecreti" value="true" />
        </jsp:include>
       </td>
    </tr>
  <%
   } // endwhile
  %>
  </table>
  </FORM>
<%
  }  // endif decreti.size()>0
%>
  </body>
</html>