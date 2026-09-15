<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione" %>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel" %>
<%@ page import="siap.sius.tenore.model.TenoreModel" %>

<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="aggregato"          scope="request" class="java.util.Vector"/>
<jsp:useBean id="dettagliofascicolo" scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel" />
<jsp:useBean id="AllMotivi"          scope="request" class="java.util.Vector"/>
<jsp:useBean id="codFunzione"        scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Stato Esecuzione </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
    function chiama(idEvento)
    {
      window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.ordineesecuzione.action.ActLoadCancellaProvvedimento&IdEvento="+idEvento,"Cancella_provvedimento", "top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
    }

    var node;
    function effettoTree(a)
    {
      node=document.getElementById("elenco"+a);
      node.style.display = (node.style.display == "none")? "block" : "none";
      document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>expand.gif" : "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
      return false;
    }

    //==========================================================================
    // Determina i quantum di pena residui alla data di systema
    //==========================================================================
    function CalcoloResiduoPena (){
      var calcoloURL = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActCalcolaPenaResiduaAl";

      desktop = window.open(calcoloURL, "Pena_Residua_Al", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=no, resizable=no, width=400, height=200, location=no");
    }
  </script>
</head>
<body class="corpo">
 <FORM method="POST" name="StatoEsecuzione" action="<%= IWebConstants.PG_MAIN%>">
 <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : </font><font class="campo">Stato Esecuzione </font>&nbsp;&nbsp;
     </td>
<%
	  // MEV 15 - Revisione SIGE
	  // Il pulsante Indietro viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Stato Esecuzione da Iscrizione Manuale
	  if( codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90010000)){
%>
	      <!-- BOTTONE DI RITORNO -->
	      <td class="LBG">
	        <a href="javascript:history.go(-1);">
	          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
	        </a>
	      </td>
<%		  
	  }
%>

    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenzaEsecuzione.jsp"/>
  <br>
  <div align="center">
    <table cellspacing=2 cellpadding=2 width='100%'>
      <tr><td>&nbsp;&nbsp;</td></tr>
<%if(aggregato!= null && aggregato.size()>0){
%>
      <tr><td class="Titolo"colspan=6>Stato Esecuzione</td></tr>
      <tr><td colspan=5>&nbsp;&nbsp;</td></tr>
    </table>

<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="int">Data Emissione</td>
        <td class="int">Provvedimento</td>
        <td class="int">Esito</td>
        <td class="int">Autorità</td>
        <td class="int">Documento<br>Validato</td>
        <td class="int">Azioni</td>
      </tr>
<%
  // Trova il Codice Ufficio dell'utente connesso
  UtenteModel lUteMod=(UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
  UfficioModel lUffMod= lUteMod.getUfficioUtente();
  String CodUff=new String(lUffMod.getCodTipoUfficio());

  MisuraAlternativaAggregatoModel lAggre = null;
  Iterator itx = aggregato.iterator();
  int jPA =0;
  Vector lTenori = new Vector();
  EventoModel lEvento = new EventoModel();
  while ( itx.hasNext())
  {
      lAggre = new MisuraAlternativaAggregatoModel();
      lAggre = (MisuraAlternativaAggregatoModel)itx.next();
      lEvento = lAggre.getEventoNotifica().getEvento();

      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.info("EVENTO "+lEvento.getDescrTipoProvvedimento()+"= " + lEvento.getCodTipoEvento() +" "+ lEvento.getCodTipoProvvedimento() +" "+ lEvento.getCodMotivo());

      // Se l'utente è un utente SIUS e il Provvedimento non è validato il Provvedimento non viene visualizzato
      if( ((CodUff.equals("TDS") || CodUff.equals("UDS")) && (lEvento.getFlagDocumentoRegistrato()!= null && !lEvento.getFlagDocumentoRegistrato().equals("N")))
          || (!CodUff.equals("TDS") && !CodUff.equals("UDS"))
        )
      {
%>
    <tr>
      <td class="l"  width="12%"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"dd-MM-yyyy"))%></td>
<%if ( lEvento.getCodMotivo().equals("1101")||
		lEvento.getCodMotivo().equals("1102")||
		lEvento.getCodMotivo().equals("1103")||
		lEvento.getCodMotivo().equals("1104")||
		lEvento.getCodMotivo().equals("1105")||
		lEvento.getCodMotivo().equals("1106")||
		lEvento.getCodMotivo().equals("1107")||
		lEvento.getCodMotivo().equals("1108")||
		lEvento.getCodMotivo().equals("1114")||
		lEvento.getCodMotivo().equals("1115")||
		lEvento.getCodMotivo().equals("1116")||
		lEvento.getCodMotivo().equals("1117")||
		lEvento.getCodMotivo().equals("1118")||
		lEvento.getCodMotivo().equals("1119")) {
	
		Iterator itxOggetto = AllMotivi.iterator();
		String strOggetto ="";
		while(itxOggetto.hasNext())
		{
		  DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();
		  if(lDecMod.getCode().equals(lEvento.getCodMotivo())){
		   		strOggetto += lEvento.getDescrTipoProvvedimento()+" ";
		   		strOggetto += lDecMod.getFiltro();
		   		break;
		  }
		}
//ANNA per PEne SOspese:per il momento non uso la descr sintetica ma lascio quella di dettaglio come 
//nel Vision, ma è da verificare!!! <%=StringUtils.toStringJSP(strOggetto)%->
		%> 
     <td class="l" width="36%"><%=StringUtils.toStringJSP(strOggetto)%>&nbsp;- <%=StringUtils.toStringJSP(lEvento.getDescrMotivo(),"-")%></td>
         <%}else{ %>
      <td class="l"  width="36%"><%=StringUtils.toStringJSP(lEvento.getDescrTipoProvvedimento())%>&nbsp;<%=StringUtils.toStringJSP(lEvento.getDescrMotivo(),"-")%></td>
         <%} %>
<%
    if(lAggre.getTenori() != null && lAggre.getTenori().length>0 && lAggre.getTenori()[0]!= null && lAggre.getTenori()[0].getCodEsitoTenore() != null &&
       !lAggre.getTenori()[0].getCodEsitoTenore().equals(""))
    {
%>
     <td class="c" width="12%"><%=StringUtils.toStringJSP(lAggre.getTenori()[0].getDescrEsitoTenore())%>
<%
     if(lAggre.getTenori().length>1)
     {
%>
         <a href="#1" onClick="return effettoTree(<%=jPA%>)"><img name="image<%=jPA%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Altri Esiti" ></a>
<%
     }
%>
    </td>
<%
    }else{
%>
     <td class="c" width="12%">-</td>
<%
    }
%>
     <td class="l"><%=lEvento.getDescrUfficioEmittente()+ " " + lEvento.getDescrLuogoEmittente()%>
<%
     if(lAggre != null && lAggre.getMisuraAlternativa() != null &&
        "P".equalsIgnoreCase(lAggre.getMisuraAlternativa().getFlagUfficioInserimento()))
      {
%>
        <br>(<font class="cRosso">-- iscritto da SIEP --</font>)
<%
      }
%>
</td>
      <td class="c" width="12%">&nbsp;
<%
      if (lEvento.getFlagDocumentoRegistrato()!=null)
      {
        if (lEvento.getFlagDocumentoRegistrato().compareTo("S")==0)
        {
%>
          <img src="/images/TickRed.gif">
<%
        }else if(lEvento.getFlagDocumentoRegistrato().compareTo("A")==0)
         {
%>
             <a class="cliccabile" href="javascript:chiama('<%=lEvento.getIdEvento()%>');" title="ANNULLAMENTO">
             <font class="cRosso">ANNULLATO</font></a>
<%
         }
      }
%>
      </td>
      <td class="c" width="7%">
<%
        String isBlob="SI";
        if(lEvento.getFlagDocumentoRegistrato() == null)
        {
          isBlob="NO";
        }
        if(lEvento.getDocBlobOut()==null || lEvento.getDocBlobOut().size()==0)
        {
           isBlob = "NO";
        }

        //Flag Documento Registrato
        String lDocReg = "N";
        if(  lEvento.getFlagDocumentoRegistrato() != null
          && lEvento.getFlagDocumentoRegistrato().equals("S"))
        {
          lDocReg = "S";
        }

        if(  lEvento.getFlagDocumentoRegistrato() != null
          && lEvento.getFlagDocumentoRegistrato().equals("A"))
        {
          lDocReg = "A";
        }

       if(  lEvento.getCodOperatoreInserimento() != null
          && lEvento.getCodOperatoreInserimento().startsWith("res-"))
        {

          lDocReg = "M";
        }

      String isValidato = "N";
      if (   lEvento.getFlagDocumentoRegistrato()!=null
          && lEvento.getFlagDocumentoRegistrato().compareTo("S")==0
         )
      {
        isValidato = "S";
      }

%>
        <jsp:include page="<%=ICostantiOrdineEsecuzione.PG_BUTTONS_ORDINANZE%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lEvento.getIdEvento()%>" />
          <jsp:param name="CampoIdEntitaProvv" value="campo" />
          <jsp:param name="ValoreIdEntitaProvv" value="<%=lEvento.getFlagDocumentoRegistrato()%>" />
          <jsp:param name="TipoProvvedimento" value="<%=lEvento.getCodTipoProvvedimento()%>" />
          <jsp:param name="MotivoEvento" value="<%=lEvento.getCodMotivo()%>" />
          <jsp:param name="TipoEvento" value="<%=lEvento.getCodTipoEvento()%>" />
          <jsp:param name="TemIdTemplate" value="<%=lEvento.getTemIdTemplate()%>" />
          <jsp:param name="modalita" value="R" />
          <jsp:param name="docRegistrato" value="<%=lDocReg%>" />
          <jsp:param name="Evento" value="<%=isBlob%>" />
          <jsp:param name="isValidato" value="<%=isValidato%>" />
        </jsp:include>
      </td>
    </tr>
<%

    //Caricamento Altri Esiti.
     if (lAggre!=  null && lAggre.getTenori() != null && lAggre.getTenori().length>1)
      {
       //il contatore parte da uno perchè il primo esito nn deve essere visualizzato nella lista
        for(int j=1;j<lAggre.getTenori().length;j++)
        {
          TenoreModel lTenMod = new TenoreModel(lAggre.getTenori()[j]);
          lTenori.add(lTenMod);

        }
%>
    		</table>
    			<div id="elenco<%=jPA%>" style="display:none; width:100%;">
      			<%@include file="/jsp/files/siap/sius/tenore/ListaTenori.jspf" %>
    			</div>
<%	    	lTenori.clear();
    			jPA++;
      %><table cellspacing=2 cellpadding=2><%
      }
    }
  }
}else{
%>
 </table>
 <table width="100%">
<tr><td class="Titolo"colspan=6>Stato Esecuzione</td></tr>
 </table>
<%}%>
    </table>
  </div>
  </form>
	</body>
</html>