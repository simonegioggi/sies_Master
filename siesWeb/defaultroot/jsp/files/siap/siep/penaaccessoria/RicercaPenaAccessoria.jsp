<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.sico.evento.model.EventoModel" %>

<%@ page import="siap.siep.penaaccessoria.action.ICostantiPenaAccessoria" %>
<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.beneficio.model.BeneficioPenaAccessoriaModel" %>
<%@ page import="siap.siep.beneficio.model.BeneficioModel" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="beneficiopenaaccessoria" scope="request" class="java.util.Vector" />
<jsp:useBean id="eveCorrelati" scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="penaSigeModificabile"       scope="request" class="java.lang.String"/>

<%

	// Gestione funzione SIGE
	boolean modoSIGE = false;
	if (modo != null && modo.equalsIgnoreCase("SIGE"))
		modoSIGE = true;


  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Pena Accessoria</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  	<script language="JavaScript">
    	var node;
    	function effettoTree(a)
    	{
      	node=document.getElementById("elenco"+a);
      	node.style.display = (node.style.display == "none")? "block" : "none";
      	document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>expand.gif" : "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
      	return false;
    	}

			function confermaCancellazione(a_action, a_parameter, a_entityname, a_parameter2, a_entityname2, a_nextAction)
			{
				var documentoRegistrato = a_entityname2;
  			if (window.confirm('Confermi la cancellazione ?'))
  			{
    			if (documentoRegistrato=="S")
    			{
       			var  desktop = window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=" + a_action + "&" + a_parameter + "=" +a_entityname+"&nextAction="+a_nextAction, "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
         		window.parent.close();
   				}else{
      			str = "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&" +a_parameter +"=" + a_entityname+"&nextAction="+a_nextAction;
               window.location.href=str;
        	}
  			}
			}

  	</script>
  </head>
  <body class="corpo">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Pene Accessorie

<%
			if( modalita.equals("P") )
      {%>per Esecuzione PA<%
      }else	if( modalita.equals("G") )
      {%>per Richieste al GE
        <td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.penaaccessoria.action.ActLoadInserisciRichiestaGE&Aggiungi=yes<%=retParam%>">
            <img  align="middle" src="/images/new24.gif" alt="Iscrizione Richiesta GE" width="24" height="24" border="0">
          </a>
        </td>
      <%
      }else	if( modalita.equals("C") )
      {%>per Comunicazioni<%}%>
      </font></td>
  <% 
     if(modoSIGE)
     {
		String   lModificabile = (String)request.getAttribute("Modificabile");
		String   lCancellabile = (String)request.getAttribute("Cancellabile");
    	 
 %>     
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>"/>
          <jsp:param name="ValoreIdEntita" value="0"/>
          <jsp:param name="Modificabile" value="<%=lModificabile%>"/>
          <jsp:param name="Cancellabile" value="<%=lCancellabile%>"/>
        </jsp:include>
     </td> <%} %>     
      
 	 <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
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

  <div align=center>
<%
  // Elenco degli Eventi Correlati
  Vector eventiPA = new Vector();
  Iterator itxProc = eveCorrelati.iterator();
  int jPA =0;
  if (beneficiopenaaccessoria != null && beneficiopenaaccessoria.size() > 0 )
  {  
  	Iterator itx = beneficiopenaaccessoria.iterator();
		boolean primo = true;
  	while ( itx.hasNext())
  	{
%>
  		<table width="100%">
<%		if (primo) 
			{
				primo=false; %>	

		    <tr>
		      <td class="int" width=25%>Tipo</td>
		      <td class="int" width=12%>Tipo Durata</td>
		      <td class="int" width=10%>Stato</td>
		      <td class="int" width=13%>Data Fine Validità</td>
		      <td class="int" width=10%>Falsità di documenti</td>
		      <td class="int" width=10%>Tipo Beneficio</td>
		      <td class="int" width=10%>Provvedimento di concessione</td>
		      <td class="int" width=10%>Azioni</td>
		    </tr>
<%		}
  
		BeneficioPenaAccessoriaModel lBenPenMod = (BeneficioPenaAccessoriaModel)itx.next();
    PenaAccessoriaModel penacc = lBenPenMod.getPenaAccessoria();
    BeneficioModel lBenMod = lBenPenMod.getBeneficio();
    
		// Decodifica stato Pena Accessoria.
    String descrFlagCondonata = "-";
    if (penacc.getFlagCondonata().trim().compareTo("C")==0)
    	descrFlagCondonata = "Condonata";
    else if (penacc.getFlagCondonata().trim().compareTo("R")==0)
    	descrFlagCondonata = "Revocata";
    else if (penacc.getFlagCondonata().trim().compareTo("D")==0)
    	descrFlagCondonata = "Depenalizzata";
    else if (penacc.getFlagCondonata().trim().compareTo("S")==0)
    	descrFlagCondonata = "Sostituita";
    else if (penacc.getFlagCondonata().trim().compareTo("T")==0)
    	descrFlagCondonata = "Sost. e Condonata";
%>
    <tr>
      <td class=l width="25%" align="left">
<%      if (penacc.getNumeroEventiCorrelati() != null          &&
            penacc.getNumeroEventiCorrelati().intValue() > 0 )
        {%>
          <a href="#1" onClick="return effettoTree(<%=jPA%>)"><img name="image<%=jPA%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Elenco dei Provvedimenti Correlati" ></a>
      <%}%>
				<%=penacc.getDescrTipoPenaAccessoria()%>
			</td>
      <td class=c width="12%"><%=StringUtils.toStringJSP(penacc.getDescrDurata())%></td>
      <td class=c width="10%"><%=descrFlagCondonata%></td>
      <td class=c width="13%"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penacc.getDataFineValidita(),"dd-MM-yyyy"),"-") %></td>
      <td class=c width="10%"><%=StringUtils.toStringJSP(penacc.getFlagDichiarazioneFalsita())%></td>
      <%if(lBenMod != null && lBenMod.getIdBeneficio() != null) {%>
        <td class=c width="10%"><%=StringUtils.toStringJSP(lBenMod.getDescrTipoBeneficio(),"-")%></td>
        <td class=c width="10%"><%=StringUtils.toStringJSP(lBenMod.getDescrDpr(),"-")%></td>
      <%}else{ %>
         <td class=c width="10%">-</td>
         <td class=c width="10%">-</td>     
       <%}%>     
      <td class=c width="10%">
  <% 
  
     FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
     
     String modificabile = "";
     if(!modoSIGE)
     {
           if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(lFascicolo.getChiaveUfficio()))
              {modificabile = "SI";}
           else
              {modificabile = "NO";}
     }
     else 
    	 modificabile = penaSigeModificabile;
 %>
        <jsp:include page="<%=ICostantiPenaAccessoria.PG_BUTTONS_PENA_ACCESSORIA%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>" />
           <jsp:param name="ValoreIdEntita" value="<%=penacc.getIdPenaAccessoria()%>" />
           <jsp:param name="CodTipoPenaAccessoria" value="<%=penacc.getCodTipoPenaAccessoria()%>" />
           <jsp:param name="DescrTipoPenaAccessoria" value="<%=penacc.getDescrTipoPenaAccessoria()%>" />
           <jsp:param name="Modificabile" value="<%=modificabile%>" />
           <jsp:param name="Modalita" value="<%=modalita%>" />
           <jsp:param name="TornaQui" value="<%=TornaQui%>" />
        </jsp:include>
      </td>
    </tr>

<%    //Caricamento dei Provvedimenti Correlati.
      if (penacc.getNumeroEventiCorrelati() != null          &&
          penacc.getNumeroEventiCorrelati().intValue() > 0)
      {
        itxProc = eveCorrelati.iterator();
        while ( itxProc.hasNext())
        {
          EventoModel eventoCorrelato = (EventoModel)itxProc.next();

          if (eventoCorrelato.getPenAccIdPenaAccessoria() != null  &&
              penacc.getIdPenaAccessoria().compareTo(eventoCorrelato.getPenAccIdPenaAccessoria())== 0 )
          {
            eventiPA.add(eventoCorrelato);
          }
        }
%>
    		</table>
    			<div id="elenco<%=jPA%>" style="display:none; width:100%;">
      			<%@include file="/jsp/files/siap/siep/penaaccessoria/ListaProvvedimentiPA.jspf" %>
    			</div>
  		<table width="100%">
<%	    	eventiPA.clear();
    			jPA++;
      } // endif Provvedimenti Correlati   
      
    } // endwhile
		%></table><%
  }else { %>
    <table width="80%">
    <tr>
          <td class="int" align="left">Nessuna Pena Accessoria definita </td>
  </tr>
  <% }// endif  %>
  </form>
  </body>
</html>