<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Vector"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.siep.notifica.model.NotificaFasSiusEveModel" %>
<%@ page import="siap.siep.notifica.model.RicercaNotificheSiusModel" %>


<jsp:useBean id="Notifiche" scope="request" class="java.util.Vector" />
<jsp:useBean id="InTesta" scope="request" class="java.lang.String" />
<jsp:useBean id="FiltoRicerca" scope="request" class="siap.siep.notifica.model.RicercaNotificheSiusModel" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<html>
<%
   // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  boolean debugmode = false; // Flag usato per debug
%>

  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Notifiche/Comunicazioni SIUS Esecutivo</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
 <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

 </head>

  <BODY class="corpo">

  <table >
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"> <font class=label>Funzione:</font>&nbsp; <font class="campo"><%=InTesta%></font> </td>
     <!-- BOTTONE DI STAMPA  -->
     <td class=l>
        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="N" />
          <jsp:param name="ValoreIdEntita" value="N" />
        </jsp:include>
     </td>

     <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

    </tr>
  </table>


  <table cellspacing=2 cellpadding=2>
  <% if (debugmode)
	  {%>
        <tr>
        <td class="lNoBord"><font class="cRosso">Modalità Debug </font></td>
      </tr>
 <%} %>
      <tr>
        <td class="lNoBord"><font class="label">Criteri di Ricerca selezionati:</font></td>
      </tr>

    <%
    String lTipoAtti = (FiltoRicerca.getCodTipoNotifica().equalsIgnoreCase("C")) ? "Comunicazioni " : "Notifiche ";
    String lDataIniziale = " " + DateUtils.getDateToString(FiltoRicerca.getDataIniziale(),"dd-MM-yyyy");
    String lDataFinale = " " + DateUtils.getDateToString(FiltoRicerca.getDataFinale(),"dd-MM-yyyy");


  	// Tipo prvvedimenti
  	String lTipoProvvedimenti = "Tutti";
  	if(FiltoRicerca.isDecretoCitazione())
  		lTipoProvvedimenti = "Decreti di Citazione";
  	else if (FiltoRicerca.isEsclusoDecret1Citazione())
  		lTipoProvvedimenti = "Tutti tranne i Decreti di Citazione";

  	// Tipo destinatari
  	String lDestinatari = "Tutti";


//  Caso UNEP
  	if(FiltoRicerca.isUNEP())
  	{
  		if (FiltoRicerca.isUNEPSede())
  			lDestinatari = "UNEP SEDE";
  		else if (FiltoRicerca.isUNEPEsclusoSede())
  			lDestinatari = "UNEP Altre Sedi";
  		else
  			lDestinatari = "UNEP";
  	}
  	else if (FiltoRicerca.isEsclusoUNEP() || FiltoRicerca.isIstitutoDiDetenzione())
  			lDestinatari = FiltoRicerca.getDescTipoDestinatario();


  	// Caso Ufficio
  	else if (FiltoRicerca.getUfficio() != null)
  	{
  		String lCodUfficio = FiltoRicerca.getUfficio().getCodTipoUfficio();
  		String lNomeUff = " ";
  		if (lCodUfficio != null)
  		{
  			if (lCodUfficio.equalsIgnoreCase("PM"))
  				lNomeUff = "Procura Repubblica c/o Tribunale Ordinario ";
  			else if (lCodUfficio.equalsIgnoreCase("PGCAP"))
  				lNomeUff = "Procura Generale ";
  			// Per più uffici si può usare la decodifica uffici
  		}
  		if (FiltoRicerca.getNoUff())
  			lDestinatari = "Tutti tranne " + lNomeUff;
  		else if (FiltoRicerca.getNoSedeUff())
  			lDestinatari = lNomeUff + "Altre Sedi";
  		else if (FiltoRicerca.getUfficio().getCodComune() != null && FiltoRicerca.getUfficio().getCodComune().trim().length() > 0)
  			lDestinatari = lNomeUff + "SEDE";
  		else
  			lDestinatari = lNomeUff;
  	}

  	lDestinatari = FiltoRicerca.getDescTipoDestinatario();


// Utente
  	String lCodUtente = FiltoRicerca.getCodOperatoreInserimento();

    %>
     <tr>
     	<td class="lVerdeNB"><%=lTipoAtti%> inserite dal <%=lDataIniziale%>  al  <%=lDataFinale%></td>
     </tr>
       <tr>
     	<td class="lVerdeNB"><%=lTipoAtti%> relative ai seguenti tipi di provvedimenti:  <%=lTipoProvvedimenti%></td>
     </tr>
       <tr>
     	<td class="lVerdeNB"><%=lTipoAtti%> da trasmettere ai seguenti tipi di destinatari:  <%=lDestinatari%></td>
     </tr>

      <%
      // Filtro su NOTE
      if (FiltoRicerca.getFiltroNote() != null && FiltoRicerca.getFiltroNote().trim().length() > 0)
 {
	 %>
     </tr>
       <tr>
     	<td class="lVerdeNB"><%=lTipoAtti%> annotate per:  <%=FiltoRicerca.getFiltroNote()%></td>
     </tr>
	 <%
 }
 if (lCodUtente != null && lCodUtente.trim().length() > 0)
 {
	 %>
     </tr>
       <tr>
     	<td class="lVerdeNB"><%=lTipoAtti%> inserite dall'utente:  <%=lCodUtente%></td>
     </tr>
	 <%
 }

 // Ordinamento
 if (FiltoRicerca.getDescOrdinamento() != null)
 {
	 %>
     </tr>
       <tr>
     	<td class="lVerdeNB"><%=lTipoAtti + "ordinate per " + FiltoRicerca.getDescOrdinamento()%> </td>
     	</tr>
 <%
 }
 %>
     </table>


  <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

 <table cellspacing=2 cellpadding=2 width=95%>
    <tr>
      <td class="int" width=10%>Numero SIUS</td>
      <td class="int" width=10%>Data Inserimento Notifica</td>
      <td class="int" width=20%>Cognome Nome</td>
       <td class="int" width=10%>Data Atto</td>
      <td class="int" width=20%>Contenuto Atto</td>
       <td class="int" width=10% >Data Trasmissione</td>
      <td class="int" width=20% >Destinatario</td>
    </tr>
<%

    Iterator itx = Notifiche.iterator();

    String sUfficio = "Procura";
    while ( itx.hasNext())
    {
    	NotificaFasSiusEveModel notifica = (NotificaFasSiusEveModel)itx.next();
    	String lCognome = "-";
    	String lNome = "-";
    	String lIdSoggetto = "-";
    	if (notifica.getFascicoloSius() != null && notifica.getFascicoloSius().getSoggetto() != null)
    	{
    		lCognome = notifica.getFascicoloSius().getSoggetto().getCognome();
    		lNome = notifica.getFascicoloSius().getSoggetto().getNome();
    		lIdSoggetto = notifica.getFascicoloSius().getSoggetto().getIdSoggetto().toString();
    	}
    	String lContenuto = StringUtils.toStringJSP(notifica.getEvento().getDescrTipoProvvedimento(), "-") + " <br>" + StringUtils.toStringJSP(notifica.getEvento().getDescrMotivo(), "-") + " <br> " + StringUtils.toStringJSP(notifica.getEvento().getDescrEsito(), "-");

    	%>
      <tr>
      	<td class="c"><font class="label">
      	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=notifica.getFascicoloSius().getIdFascicoloSius()%><%=retParam%>">
      	<%=notifica.getFascicoloSius().getChiaveAnno()%>/<%=notifica.getFascicoloSius().getChiaveProgr()%></font></td>
      	</a>
      	<td class="c"><font class="label"><%=DateUtils.getDateToString(notifica.getDataInserimento(),"dd-MM-yyyy")%></font></td>
       	<td class="c"><font class="label">
       	  <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=lIdSoggetto%>&TornaQui=<%=TornaQui%>">
         	<%=StringUtils.toStringJSP( lCognome, "-" )+ " " + StringUtils.toStringJSP(lNome, "-")%></font></a></td>
       	<td class="c"><font class="label"><%=DateUtils.getDateToString(notifica.getEvento().getDataEmissione(),"dd-MM-yyyy")%></font></td>
      	<td class="c"><font class="label"><%=StringUtils.toStringJSP(lContenuto, "-")%></font></td>
      	<td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(notifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy"), "-")%></font></td>
       	<td class="c">
      <%  	if( debugmode )
      {
    	  %>
    	  <font class="cRosso">
    	  id-Notifica: <%="" + notifica.getIdNotifica()%>
    <% 	  if (notifica.getUffCodUfficio()  != null && notifica.getUffCodUfficio().trim().length() > 0) {%>
    	  cod-Ufficio: <%=notifica.getUffCodUfficio()%>
    <%}
    	  if (notifica.getAutoritaEsterna()!= null) {%>
    	  id-Autorità: <%="" + notifica.getAutoritaEsterna().getIdAutoritaEsterna()%>
    <%}
       	  if (notifica.getIstitutoDetenzione()!= null) {%>
    	  id-IstDetenzione: <%=notifica.getIstitutoDetenzione().getIdIstitutoDetenzione() %>
   	<%}
    %>
    NOTE: <%=notifica.getNote()%>
      	</font>
      	<br>
      	<%
      }
      %>
       	<font class="label">
<%
    // UFFICIO
    if(notifica.getUfficio()!= null)
    {
%>
      <%=notifica.getUfficio().getDescrTipoUfficio()%>&nbsp;di&nbsp; <%=notifica.getUfficio().getDescrComune()%>
<%
    }
    // Autorità Esterna
    else if(notifica.getAutoritaEsterna()!= null)
    {
%>
       <%=notifica.getAutoritaEsterna().getDescrTipoAutorita()%>&nbsp;di&nbsp; <%=notifica.getAutoritaEsterna().getDescrSede()%>
<%
    }
    // Avvocato SIEP
    else if(notifica.getAvvIdAvvocatoFascicoloSiep()!=null)
    {
      if( notifica.getAvvSiep() !=null)
      {
%>
            Avv. &nbsp;<%=StringUtils.toStringJSP(notifica.getAvvSiep().getAvvocato().getCognome() +" "+notifica.getAvvSiep().getAvvocato().getNome())%>&nbsp;
<%
      }
    }
    // Avvocato SIUS
    else if(notifica.getAvvIdAvvocatoFascicoloSius()!=null)
    {
      if( notifica.getAvvSius() !=null)
      {
%>
            Avvocato &nbsp;<%=StringUtils.toStringJSP(notifica.getAvvSius().getAvvocato().getCognome() +" "+notifica.getAvvSius().getAvvocato().getNome())%>&nbsp;
<%
      }
    }
    // UEPE
    else if(notifica.getCssIdCssa()!=null)
    {
      if( notifica.getCSSA() !=null)
      {
%>
            UEPE &nbsp;<%=StringUtils.toStringJSP(notifica.getCSSA().getIndirizzo() +" "+notifica.getCSSA().getComune())%>&nbsp;
<%
      }
    }
// ISTITUTO DI DETENZIONE
    else if( notifica.getIstDetIdIstitutoDetenzione() != null && ! notifica.getIstDetIdIstitutoDetenzione().equals("") )
    {
      if( notifica.getIstitutoDetenzione() !=null)
      {
%>
        <%=StringUtils.toStringJSP(notifica.getIstitutoDetenzione().getDescrTipoIstituto(),"-")%>&nbsp;
        <%=StringUtils.toStringJSP(notifica.getIstitutoDetenzione().getIndirizzo(),"-")%>&nbsp;
        <%=StringUtils.toStringJSP(notifica.getIstitutoDetenzione().getDescrizione(),"-")%>&nbsp;
<%
      }
    }
%>
      </font></td>
      </tr>
<%
  }

%>
    </table>
  <br>
  </body>
</html>