<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sige.fogliocomplementare.action.ICostantiFoglioComp" %>
<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato" %>
<%@ page import="siap.sige.impugnazione.model.ImpugnazioneSigeModel"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige" %>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sige.tenore.model.TenoreSigeModel"%>

<jsp:useBean id="provvedimenti"         scope="request" class="java.util.Vector"/>
<jsp:useBean id="FascicoloSigeEsteso" 	scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="flag_valida"           scope="request" class="java.lang.String"/>
<jsp:useBean id="isModificabile"        scope="request" class="java.lang.String"/>
<jsp:useBean id="impugnazioni"          scope="request" class="java.util.Vector"/>

<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

<html>
<head>
  <title>[S.I.E.S.] - Lista Provvedimenti - Compilazione Foglio Complementare</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">

  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Compilazione Foglio Complementare</font>
      </td>
        <!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
   </table>
<br>
<%
	boolean modificabile = true;
	if (isModificabile != null && isModificabile.equalsIgnoreCase("NO"))
	    modificabile = false;

	String lFunAnnullaValidaProvvedimento = "";
	String lFunAnnullaValidaAllegato = "";
	String isDepositato = "NO";

	if (flag_valida.equals(""))
    	flag_valida="SI";

	if (FascicoloSigeEsteso != null)
	{
%>
	 <table>
	    <tr>
	      <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
	    </tr>
	 </table>
<%
	} // endif fascicoloSige
%>
<br>
<%
  if ( provvedimenti.size() == 0 )
  {
%>
        <td class="LBG">
          <font class="label">Procedimento privo di Provvedimenti per i quali è possibile compilare il Foglio Complementare. </font>
        </td>
<%
  } else
  {
%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaProvvedimenti">
  <table width="96%">
  <div align=center>
    <tr>
      <td class="int" >Data emissione</td>
      <td class="int" >Tipo provvedimento</td>
      <td class="int" >Oggetti provvedimento</td>
      <td class="int" >Data Deposito</td>
      <td class="int" width="9%">Data Ricorso</td>
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
      	<td class="int" >Deposito<br>Validato</td>
		<%}%>
      <td class="int" >Azioni</td>
    </tr>
  </div>
<%
    Iterator itx = provvedimenti.iterator();
    int i=0;
    while ( itx.hasNext())
    {
      ProvvedimentoSigeEventoModel lProvEve = (ProvvedimentoSigeEventoModel)itx.next();
%>
    <tr>
      <td class="l">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvEve.getEventoNotifica().getEvento().getDataEmissione(),"dd-MM-yyyy"),"-") %>
      </td>
      <td class="l" >
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
			Vector lTenori = null;

			if (lProvEve.getProvvedimento().getCodTipoProvvedimentoSige()!= null 
					&& lProvEve.getProvvedimento().getCodTipoProvvedimentoSige().compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_SOSPENSIONE) == 0 ) {
				// Per l'Ordinanza di Sospensione il riferimento non è più ai tenori del provvedimento sospeso
				lTenori = lProvEve.getTenoriEstesi();
				
			}
			else {
				lTenori = lProvEve.getTenoriEstesi();
			}

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
%>   			<font class="label">
						-&nbsp;<%=lTenore.getDescrOggettoSige()%><br>
					</font>
<%			}
			}%>
      </td>
      <td class="c" ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvEve.getProvvedimento().getDataDeposito(),"dd-MM-yyyy"),"-")%></td>
      <td class="c"><font class="campo">
<%
			Iterator itx3 = impugnazioni.iterator();
			ImpugnazioneSigeModel impSigeProvv = new ImpugnazioneSigeModel();
			boolean provvImpugnato = false;
			while (itx3.hasNext()) {
				ImpugnazioneSigeModel impSige = (ImpugnazioneSigeModel) itx3.next();
				
				if (impSige.getProvvIdProvvedimentoSige().intValue()  == lProvEve.getProvvedimento().getIdProvvedimentoSige().intValue() ) {
					provvImpugnato = true;
					impSigeProvv = impSige;
					//break;
				}
			}

			if(provvImpugnato)
			{
				String dataPrimoRicorso = DateUtils.getDateToString( impSigeProvv.getDataRicorso(), "dd-MM-yyyy");

				if(!StringUtils.toStringJSP(dataPrimoRicorso,"-").equals("-") ){ %>
				<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.impugnazione.action.ActLoadDettaglioImpugnazioneSige&<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>=<%=impSigeProvv.getIdImpugnazioneSige()%>&TornaQui=<%=0%>"><%=StringUtils.toStringJSP(dataPrimoRicorso,"-")%></a>
			<% }else{%> - <%}
			}else{%> - <%}%>     		
			</font>
     	</td>
<%
      if (flag_valida.equals("SI") )
      {%>
        <td class="c">
        <% if ((lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato()!=null) && (lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("S")==0) )
        {
           // SVALIDAZIONE. Il decreto di Unificazione è sempre svalidabile !
           if  ( lProvEve.getEventoNotifica().getEvento().getNumAllValidati() < 1 && lFunAnnullaValidaProvvedimento.length() > 1 && (modificabile  || (lProvEve.getEventoNotifica().getEvento().getCodEsito().compareToIgnoreCase("0600") == 0) ))
           {%>
           		<a href="Javascript:annulla('Vuoi annullare la validazione del provvedimento? ','<%=lFunAnnullaValidaProvvedimento%>' ,'<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=lProvEve.getEventoNotifica().getEvento().getIdEvento()%>');">
          			<img src="/images/TickRed.gif" alt = "Annulla validazione provvedimento"  border="0">
          		</a>
         <%}else{%>
          <img src="/images/TickRed.gif">
         <%}
        // ANNULLATO
        }else if(lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato()!=null && lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("A")==0)
        {%>
           <a class="cliccabile" href="javascript:cancella('<%=lProvEve.getEventoNotifica().getEvento().getIdEvento()%>');" title="ANNULLAMENTO">
           <font class="cRosso">ANNULLATO</font></a>
			<%}else{%>-<%}%>
        </td>
      	<td class="c">
      	<%
      	isDepositato = "NO";
      	if (lProvEve.getEventoNotifica().getEvento().getNumAllValidati() > 0)
      	{
         	if (lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato()!=null && lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("S")==0)
         	{
           	isDepositato = "SI";
           	if (lFunAnnullaValidaAllegato.length() > 1 )
           	{%>
           		<a href="Javascript:annulla('Vuoi annullare la validazione del deposito? ','<%=lFunAnnullaValidaAllegato%>' ,'<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=lProvEve.getEventoNotifica().getEvento().getIdEvento()%>');">
          		<img src="/images/TickRed.gif" alt = "Annulla validazione deposito"  border="0">
          		</a>
         	<%}else{%>
          	<img src="/images/TickRed.gif">
        	<%}
        	} else if (lProvEve.getProvvedimento().getDataDeposito()!=null) {%>
          	<img src="/images/TickRed.gif">
        	<%}
      	}else{%>
          -
    	<%}%>
        </td>
  	<%}%>
      <td class="l" >
<%      String isBlob = "SI"; 
				if(lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() == null){isBlob="NO";}
        String isAllegato = "NO";  if(lProvEve.getEventoNotifica().getEvento().getNumAllegati() > 0){isAllegato="SI";}
 %>
<!--
    Le condizioni per abilitare la modifica di un provvedimento sono:
		- il Procedimento SIGE è modificabile;
    - provv. validato; (lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato()!=null && lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("S")==0) 
		- provv. depositato ma con deposito non validato;  (isDepositato.equalsIgnoreCase("NO") && lProvEve.getEventoNotifica().getEvento().getDataDeposito() != null)
		- provv. non appartiene ad uno dei seguenti tipi:  Unificazione (cod. Esito = 0600), Fissazione Udienza (cod. Esito = 0601), Irreperibilità (cod. Esito = 0602), Rinvio Udienza (cod. Esito = 0603);
-->
<%
	String lModificaProvvedimento = "NO";

	if (modificabile  && (lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato()!=null && 
	    									lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S")) &&  
	    									isDepositato.equalsIgnoreCase("NO") && 
	    									lProvEve.getProvvedimento().getDataDeposito() != null)
	{
    	if(	lProvEve.getEventoNotifica().getEvento().getCodEsito().compareTo("0600")!=0 && 
    	    lProvEve.getEventoNotifica().getEvento().getCodEsito().compareTo("0601")!=0 && 
    	    lProvEve.getEventoNotifica().getEvento().getCodEsito().compareTo("0602")!=0 && 
    	    lProvEve.getEventoNotifica().getEvento().getCodEsito().compareTo("0603")!=0 )
    	  lModificaProvvedimento = "SI";
 	}
      
    if (flag_valida.equals("SI"))
    {
%>
      <jsp:include page="<%=ICostantiFoglioComp.PG_BUTTONS_CFC%>">
        <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
        <jsp:param name="ValoreIdEntita" value="<%=lProvEve.getEventoNotifica().getEvento().getIdEvento()%>" />
        <jsp:param name="Allegato" value="<%=isAllegato%>" />
      </jsp:include>
<%     
	}
%>
       </td>
    </tr>
<%
     i++;
   } // endwhile
%>
  </table>

    </FORM>
<%
  }  // endif provvedimenti.size()
%>
  </body>
</html>