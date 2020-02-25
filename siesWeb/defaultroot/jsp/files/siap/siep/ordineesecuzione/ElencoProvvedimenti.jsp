<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<%-- MEV 16: aggiunti import di classi --%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="eventi" scope="request" class="java.util.Vector"/>
<jsp:useBean id="eventocancellareannullare" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="ordinamento"               scope="request" class="java.lang.String"/>
<jsp:useBean id="AllMotivi"                 scope="request" class="java.util.Vector"/>
<jsp:useBean id="codFunzione"               scope="request" class="java.lang.String"/>

<%
// Preparazione del bottone di riordino date
  String lRet = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.ordineesecuzione.action.ActRicercaProvvedimenti";
%>

<html>
  <head>
    <title>[S.I.E.S.] - Elenco Provvedimenti </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
function conferma(a_action, a_parameter, a_entityname, a_parameter2, a_entityname2, valoreordinamento, existFC) {
	// MEV 16: aggiunti controlli preventivi
	if (existFC == 'true') {
		alert("Attenzione! Per questo provvedimento esiste il foglio complementare. Cancellare prima il foglio complementare e poi il provvedimento.");
		a_action = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.ordineesecuzione.action.ActRicercaProvvedimenti";
	}
	if (existFC == 'false') {
	  	var documentoRegistrato = a_entityname2;
	  	if (window.confirm('Confermi la cancellazione ?')) {
	  		if (documentoRegistrato=="S") {
	  			var  desktop = window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=" + a_action + "&" + a_parameter + "=" +a_entityname +"&lOrdinamento="+valoreordinamento, "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
	         	window.parent.close();
	    	} else {
	    		str = "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&" +a_parameter +"=" + a_entityname+"&lOrdinamento=<%=ordinamento%>";
	    		window.location.href=str;
	    	}
	  	}
	}
}

function chiama(idEvento)
{
    window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.ordineesecuzione.action.ActLoadCancellaProvvedimento&IdEvento="+idEvento,"Cancella_provvedimento", "top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
}

function pr()
{
   sign = prompt("Confermi la cancellazione?");
}

</script>

  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : </font>
      <font class="campo">Elenco Provvedimenti PM</font>&nbsp;&nbsp;
	    <input type="HIDDEN" name="validato" value="">
	    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
     </td>
<%
	  // MEV 15 - Revisione SIGE
	  // Il pulsante Indietro viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Elenco Provvedimenti PM da Iscrizione Manuale
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
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<br>
  <div align="center">
  <table cellspacing=2 cellpadding=2>
    <tr>
<%
     if("EM".equals(ordinamento))
     {
%>
      <td class="int"><font class="cRosso">Data Emissione</font>
<%
     }else
     {%>
      <td class="int">Data Emissione
<%   }%>
        <a href="<%=lRet%>&lOrdinamento=EM">
         <img align="bottom" src="<%=IWebConstants.IMAGES_DIR%>/bottoni/modifica.gif" alt="ordina per" border="0">
        </a>&nbsp;&nbsp;
      </td>
<%
     if("IN".equals(ordinamento))
     {
%>
      <td class="int"><font class="cRosso">Data Inserimento</font>
<%
     }else
     {
%>
      <td class="int">Data Inserimento
<%
     }
%>
        <a href="<%=lRet%>&lOrdinamento=IN">
         <img align="bottom" src="<%=IWebConstants.IMAGES_DIR%>/bottoni/modifica.gif" alt="ordina per" border="0">
        </a>&nbsp;&nbsp;
      </td>
      <td class="int">Provvedimento</td>
      <td class="int">Autorità</td>
      <td class="int">Documento<br>Validato</td>
      <td class="int">Azioni</td>
    </tr>
<%
  Iterator itx = eventi.iterator();
  while ( itx.hasNext())
  {
    EventoModel lEvento = (EventoModel)itx.next();
	if(lEvento.getCodTipoProvvedimento().equals("57")&& lEvento.getCodTipoEvento().equals("01"))
		continue;

    // Se l'utente è un utente SIUS e il Provvedimento non è validato il Provvedimento non viene visualizzato
    if( ((UtenteConnesso.getUfficioUtente().getCodTipoUfficio().equals("TDS") || UtenteConnesso.getUfficioUtente().getCodTipoUfficio().equals("UDS")) && (lEvento.getFlagDocumentoRegistrato()!= null && !lEvento.getFlagDocumentoRegistrato().equals("N")))
        || (!UtenteConnesso.getUfficioUtente().getCodTipoUfficio().equals("TDS") && !UtenteConnesso.getUfficioUtente().getCodTipoUfficio().equals("UDS"))
      )
    {
%>
    <tr>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"dd-MM-yyyy"))%></td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataInserimento(),"dd-MM-yyyy"))%></td>
<%
Set<String> elencoMotivo = new HashSet<String>();
elencoMotivo.add("1101");
elencoMotivo.add("1102");
elencoMotivo.add("1103");
elencoMotivo.add("1104");
elencoMotivo.add("1105");
elencoMotivo.add("1106");
elencoMotivo.add("1107");
elencoMotivo.add("1108");
elencoMotivo.add("1114");
elencoMotivo.add("1115");
elencoMotivo.add("1116");
elencoMotivo.add("1117");
elencoMotivo.add("1118");
elencoMotivo.add("1119");

if ( elencoMotivo.contains(lEvento.getCodMotivo()) ) {
		Iterator itxOggetto = AllMotivi.iterator();
		String strOggetto ="";
		while(itxOggetto.hasNext()) {
		  DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();
		  if(lDecMod.getCode().equals(lEvento.getCodMotivo())){
		   		strOggetto += lEvento.getDescrTipoProvvedimento()+" ";
		   		strOggetto += lDecMod.getFiltro();
		   		break;
		  }
		}

		%> 
     <td class="l"><%=StringUtils.toStringJSP(strOggetto)%>
<% } else { %>
     <td class="l"><%=StringUtils.toStringJSP(lEvento.getDescrTipoProvvedimento())%>
           &nbsp;<%=StringUtils.toStringJSP(lEvento.getDescrMotivo(),"-")%>&nbsp;<br>
         
  <%  // AMBROS a8-rr-222 %>       
         
   <%      		if(lEvento.getCodTipoProvvedimento() != null && lEvento.getCodTipoProvvedimento().equals("04")
				&& ("0284").equals(lEvento.getCodMotivo()) )
			//	&& ("S").equals(lEvento.getFlagDocumentoRegistrato()) ) 
				{
	          		if(lEvento.getCodEsito().equals("-"))
					{
	%>					<font class="label">Esito : </font>
						<font class="campo">Richiesta Accolta </font>
	<%				}
	          		else if(lEvento.getCodEsito().equals("C"))
	          		{          			
	%>		
						<font class="label">Esito : </font>
						<font class="campo">Richiesta Accolta in Conformità</font>
	<%				}
	          		else if(lEvento.getCodEsito().equals("D"))
	          		{          			
	%>		
						<font class="label">Esito : </font>
						<font class="campo">Richiesta Accolta in Difformità</font>
	<%				}
	          		else if(lEvento.getCodEsito().equals("R"))
	          		{          			
	%>		
						<font class="label">Esito : </font>
						<font class="campo">Richiesta Rigettata </font>
	<%				}
	          		else if(lEvento.getCodEsito().equals("I"))
	          		{          			
	%>		
						<font class="label">Esito : </font>
						<font class="campo">Richiesta Inammissibile </font>
	<%				}
	          		else if(lEvento.getCodEsito().equals("U"))
	          		{          			
	%>		
						<font class="label">Esito : </font>
						<font class="campo">Riunisce</font>
	<%				}
					
				}
     %>    
         
  <%  // END AMBROS  %> 
        
<% } %>

      </td>
      <td class="l"><%= lEvento.getDescrUfficioEmittente()+ " " + lEvento.getDescrLuogoEmittente()%></td>
      <td class="c">&nbsp;
<%
      if (lEvento.getFlagDocumentoRegistrato()!=null)
      {
        if (lEvento.getFlagDocumentoRegistrato().compareTo("S")==0)
        {%>
          <img src="/images/TickRed.gif">
<%
        }
        else if(lEvento.getFlagDocumentoRegistrato().compareTo("A")==0)
        {
%>
             <a class="cliccabile" href="javascript:chiama('<%=lEvento.getIdEvento()%>');" title="ANNULLAMENTO">
             <font class="cRosso">ANNULLATO</font></a>
<%
         }
      }
%>
      </td>
      <td class="c">
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

 String modificabile = "";
   if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio()))
      {modificabile = "SI";}
   else
      {modificabile = "NO";}
%>
        <jsp:include page="<%=ICostantiOrdineEsecuzione.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita"  value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
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
          <jsp:param name="Modificabile" value="<%=modificabile%>" />
          <jsp:param name="EventoCancellareAnnullare" value="<%=eventocancellareannullare.getIdEvento()%>" />
          <jsp:param name="lOrdinamento" value="<%=ordinamento%>" />
        </jsp:include>
      </td>
    </tr>
<%
    }
   
  }  // Chiude Ciclo While
%>
  </table>
  </div>
</body>
</html>