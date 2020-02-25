<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sige.fogliocomplementare.model.FoglioComplementareModel"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="siap.sige.documentoallegato.model.DocumentoAllegatoModel"%>
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
  <script type="text/javascript">
  function cancella(IdDocumentoAllegato)
  {
      window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fogliocomplementare.action.ActLoadMotivoAnnullamento&IdDocumentoAllegato="+IdDocumentoAllegato,"Cancella_provvedimento", "top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
  }
  </script>
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
      <td class="int" >Provv.<br />Validato</td>
      <td class="int" >Foglio. Comp.</td>
      <td class="int" >Azioni</td>
    </tr>
  </div>
<%
Iterator <FoglioComplementareModel>itx = provvedimenti.iterator();
while ( itx.hasNext()){
  FoglioComplementareModel foglio=itx.next();
  ProvvedimentoSigeEventoModel lProvEve = foglio.getProvvedimento();
  DocumentoAllegatoModel foglioComplementare = foglio.getFc();
  BigDecimal idEvento=lProvEve.getEventoNotifica().getEvento().getIdEvento();
  
  //String statoDocumentoRegistrato = foglioComplementare.getFlagDocumentoRegistrato();
  String azioniFC= this.getBottoniFoglioComplementare(foglioComplementare, idEvento);
  String statoDocumentoRegistrato = this.getStatoFoglioComplementare(foglioComplementare, idEvento);
  
  String tipoProvvedimento="&nbsp;";
  if(lProvEve.getProvvedimento().getCodTipoProvvedimentoSige()!=null) {
	  tipoProvvedimento=StringUtils.toStringJSP(lProvEve.getProvvedimento().getDescrTipoProvvedimentoSige(),"-");
  }
  
  Vector lTenori = lProvEve.getTenoriEstesi();
  String descrOggettoSige="&nbsp";
  Iterator itx2 = lTenori.iterator();
  String lDescrTenorePrecedente = "";
  String dataPrimoRicorso="-";
  String provvedimentoValidato="-";
  
  while ( itx2.hasNext()) {
		TenoreSigeModel lTenore = (TenoreSigeModel)itx2.next();
		if (lTenore != null &&  
			 !lTenore.getIdTenoreSige().toString().equalsIgnoreCase("") &&
			  lTenore.getDescrOggettoSige().compareTo(lDescrTenorePrecedente)!=0) 
		{
			lDescrTenorePrecedente = lTenore.getDescrOggettoSige();
			descrOggettoSige="-&nbsp;" + lTenore.getDescrOggettoSige() +"<br />";
		}
 }
  
  Iterator itx3 = impugnazioni.iterator();
	while (itx3.hasNext()) {
		ImpugnazioneSigeModel impSige = (ImpugnazioneSigeModel) itx3.next();
		
		if (impSige.getProvvIdProvvedimentoSige().intValue()  == lProvEve.getProvvedimento().getIdProvvedimentoSige().intValue() ) {
			dataPrimoRicorso = DateUtils.getDateToString( impSige.getDataRicorso(), "dd-MM-yyyy");	
			if(!StringUtils.toStringJSP(dataPrimoRicorso,"-").equals("-") ){
			    dataPrimoRicorso ="<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.sige.impugnazione.action.ActLoadDettaglioImpugnazioneSige&"+ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE+"="+impSige.getIdImpugnazioneSige()+"&TornaQui=0>"+StringUtils.toStringJSP(dataPrimoRicorso,"-")+"</a>";
		        break;
			}
		}
	}
	
	if ((lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato()!=null) && (lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("S")==0) ) {
 	   provvedimentoValidato="<img src=\"/images/TickRed.gif\">";
 	   // SVALIDAZIONE. Il decreto di Unificazione è sempre svalidabile !
        if  (lProvEve.getEventoNotifica().getEvento().getNumAllValidati() < 1 && lFunAnnullaValidaProvvedimento.length() > 1 && (modificabile  || (lProvEve.getEventoNotifica().getEvento().getCodEsito().compareToIgnoreCase("0600") == 0) )) {
     	   provvedimentoValidato="<a href=\"Javascript:annulla('Vuoi annullare la validazione del provvedimento? ','"+lFunAnnullaValidaProvvedimento+"' ,'"+ICostantiEvento.CAMPO_ID_EVENTO+"','"+lProvEve.getEventoNotifica().getEvento().getIdEvento()+"');\">" +
       			                 "<img src=\"/images/TickRed.gif\" alt = \"Annulla validazione provvedimento\"  border=\"0\"></a>";
       }
    }

    if(lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato()!=null && lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("A")) {
    	provvedimentoValidato="<a class=\"cliccabile\" href=\"javascript:cancella('"+idEvento+"');\" title=\"ANNULLAMENTO\">"+
                              "<font class=\"cRosso\">ANNULLATO</font></a>";
	}

%>		
		
    <tr>
      <td class="l">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvEve.getEventoNotifica().getEvento().getDataEmissione(),"dd-MM-yyyy"),"-") %>
      </td>
      <td class="l" >
      <%=tipoProvvedimento%>      
      </td>
      <td class="l">
          <font class="label">
						<%=descrOggettoSige %>
		  </font>
      </td>
      <td class="c" ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvEve.getProvvedimento().getDataDeposito(),"dd-MM-yyyy"),"-")%></td>
      <td class="c"><font class="campo">
       <font class="label">		
	         <%=dataPrimoRicorso %>
	   </font>
       </td>

      <td class="c">
          <%=provvedimentoValidato %>
       </td>
      <td class="c">
      <%=statoDocumentoRegistrato %>
       </td>
      
      <td class="l" >
        <%=azioniFC%>
      </td>
    </tr>
<%
   } // endwhile
%>
  </table>

    </FORM>
<%
  }  // endif provvedimenti.size()
%>
  </body>
</html>

<%!
public String getBottoniFoglioComplementare (DocumentoAllegatoModel fc, BigDecimal idEvento) {
	String html="<table> <tr>";
	if (fc==null) {
		html+="<td>";
		html+="<a href=\"/jsp/Main.jsp?Action=siap.sige.fogliocomplementare.action.ActLoadInserisciFoglioComplementare&IdEvento="+idEvento+"&TornaQui=20&Provenienza=DettaglioFC\">";
		html+="<img src=\"/images/dettaglioFC24.gif\" width=\"12\" height=\"12\" alt=\"Inserimento Foglio Complementare\" border=\"0\">";
		html+="</a>";
		html+="</td>";
	}
	
	if (fc != null && !fc.getFlagDocumentoRegistrato().equalsIgnoreCase("A")) {
		html+="<td>";
		html+="<a href=\"/jsp/Main.jsp?Action=siap.sige.fogliocomplementare.action.ActLoadDettaglioFCTastoFunzione&IdDocumentoAllegato="+fc.getIdDocumentoAllegato()+"&TornaQui=20&Provenienza=DettaglioFC\">";
		html+="<img src=\"/images/dettagli.gif\" width=\"12\" height=\"12\" alt=\"Dettaglio Foglio Complementare\" border=\"0\">";
		html+="</a>";
		html+="</td>";

		html+="<td>";
		html+="<a href=\"Javascript:confermaAnnullamento('siap.sige.fogliocomplementare.action.ActLoadAnnullaCFC','IdEvento','"+idEvento+"','CampoFill','fill');\">";
		html+="<img src=\"/images/delete.gif\" width=\"12\" height=\"12\" alt=\"Annulla Foglio Complementare\" border=\"0\">";
		html+="</a>";
		html+="</td>";

		html+="<td>";
		html+="<a href=\"/jsp/Main.jsp?Action=siap.sige.fogliocomplementare.action.ActLoadModificaCompFoglioComp&IdEvento="+idEvento+"&IdDocumentoAllegato="+fc.getIdDocumentoAllegato()+"&TornaQui=20&Provenienza=ModificaFCTastoFunzione\">";
		html+="<img src=\"/images/modifica.gif\" alt=\"Modifica Foglio Complementare\" width=\"12\" height=\"12\" border=\"0\">";
		html+="</a>";
		html+="</td>";
	}
	
	if (fc != null && fc.getFlagDocumentoRegistrato().equalsIgnoreCase("A")) {
		html+="<td>";
		html+="<a href=\"/jsp/Main.jsp?Action=siap.sige.fogliocomplementare.action.ActLoadDettaglioFCTastoFunzione&IdDocumentoAllegato="+fc.getIdDocumentoAllegato()+"&TornaQui=20&Provenienza=DettaglioFC\">";
		html+="<img src=\"/images/dettagli.gif\" width=\"12\" height=\"12\" alt=\"Dettaglio Foglio Complementare\" border=\"0\">";
		html+="</a>";
		html+="</td>";
	}
	
    html+="</tr></table>";
	return html;
}
%>
<%!
public String getStatoFoglioComplementare (DocumentoAllegatoModel fc, BigDecimal idEvento) {
	String html="<img src=\"/images/TickRed.gif\" alt = \"Foglio Complementare\"  border=\"0\">";
	if (fc==null) {
		html="";
	}
	
	if (fc!=null && fc.getFlagDocumentoRegistrato().equalsIgnoreCase("A")) {
		html="<a class=\"cliccabile\" href=\"javascript:cancella('"+fc.getIdDocumentoAllegato()+"');\" title=\"Foglio Complementare Annullato\">"+
                "<font class=\"cRosso\">ANNULLATO</font></a>";
	}
	
	return html;
	
}
%>