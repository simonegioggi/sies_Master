<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>


<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.magistratoassegnatario.action.ICostantiMagistratoAssegnatario"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeModel"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="f3b.web.RedirectTo"%>

<jsp:useBean id="TenoriSige" 	scope="session" class="java.util.Vector"/>
<jsp:useBean id="TornaQui" 		scope="request" class="java.lang.String" />
<jsp:useBean id="FascicoloSigeEsteso" 	scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="magistratoassegnatario" 	scope="request" class="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel" />
<jsp:useBean id="avvocato"		scope="request" class="java.util.Vector" />
<jsp:useBean id="ProvvedimentoEvento"		scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />
<jsp:useBean id="Modificabile"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="Stampabile"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="Cancellabile"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoTemplate" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"     		scope="request" class="java.lang.String"/>

<%
BigDecimal IdEvento = (BigDecimal) request.getAttribute("IdEvento");
%>

<%
	// Estrazione del provvedimento dal model strutturato
	ProvvedimentoSigeModel provvedimento = ProvvedimentoEvento.getProvvedimento();

	String isVALIGN = "top";
	String isBorder = "0";
	String lWidth = "96%";

	// Link alla Gestione Oggetti 
	RedirectTo lRedir = new RedirectTo();
	lRedir.setPage(IWebConstants.PG_MAIN);
	lRedir.setAction("siap.sige.tenore.action.ActLoadDettaglioOggettiProv");
	lRedir.setParameter("TornaQui", TornaQui );
	String lLinkOggettiSessione = lRedir.toString();

  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  boolean ulterioreDescrizione = false;
  
  // Flag per indicare la modalità di Modifica Decreto
  boolean modificaDecreto = false;
  if (modalita != null && modalita.trim().equalsIgnoreCase("M"))
	  modificaDecreto = true;
  String titolo = "Dettaglio Decreto";
  if (modificaDecreto)
	  titolo = "Modifica Decreto";

%>


<%@page import="siap.sige.motivazioneprovvedimento.model.MotivazioneProvvedimentoSigeModel"%>
<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Decreto Inammissibilità SIGE</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
        var desktop;

        function  Verifica() {
          var ritorno = true;

<%--           <% if (magistratoassegnatario == null )	{ %> --%>
//               ritorno = false;
<%--           <% } %> --%>

          if (! ritorno)
          	alert (" Magistrato non assegnato!");

          return ritorno;
        }
    </script>

    <script language="JavaScript">
    function Verify()
    {
        var ritorno = true;
 
     return ritorno;
    }
    </script>

  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>

  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :  </font>&nbsp;
          <font class="campo">Dettaglio Decreto Inammissibilità</font>
      </td>

<%
			if (Modificabile == null || Modificabile.trim().length() < 1)
    			Modificabile = "SI";
			if (Stampabile == null || Stampabile.trim().length() < 1)
    			Stampabile = "SI";
	
			// Nel caso di Modifica Decreto si elimina stampa e cancellazione
			if (modificaDecreto)
			{
				Modificabile = "NO";
				Stampabile = "NO";
			}

  		if (Stampabile.compareTo("SI") == 0)
  		{
    		// Deve esistere il template : da list o predefinito.
    		if ( ((ElencoTemplate != null) && (ElencoTemplate.trim().length() > 0)) || (ProvvedimentoEvento.getEventoNotifica().getEvento().getTemIdTemplate() != null && ProvvedimentoEvento.getEventoNotifica().getEvento().getTemIdTemplate().trim().length() > 1 ))
    		{
%>
  				<!-- BOTTONE DI STAMPA -->
    			<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIGE%>">
  					<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
    				<jsp:param name="ValoreIdEntita" value="<%=ProvvedimentoEvento.getEventoNotifica().getEvento().getIdEvento()%>"/>
  				</jsp:include>
<%
     		} /* endif esistenza ElencoTemplate */
  		}  /* endif Stampabile = SI */
  		
			// Nel caso di Evento già validato con stampa, si consente di riprodurre il documento.  		
		
			if (Stampabile.compareTo("NO") == 0 &&
					ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null &&
				  ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0	)
			{%>
				<!--- BOTTONE DI DOWNLOAD DOCUMENTO -->
      <td class="LBG">
       <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActLoadDocumento&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=ProvvedimentoEvento.getEventoNotifica().getEvento().getIdEvento()%>')">
        <img src="/images/print24.gif" alt="Visualizza Decreto" width="24" height="24" border="0">
        </a>
      </td> 
  	<%}
  		
  		if (Modificabile.compareTo("SI") == 0 )
  		{
%>
  		  <!-- BOTTONE DI MODIFICA -->
  		        <td class="LBG">
  		                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.provvedimento.action.ActLoadModificaDecretoInamissibilita&<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>=<%=provvedimento.getIdProvvedimentoSige()%>&ritorno=si&TornaQui=<%=TornaQui%>">
  		                <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
  		                </a>
  		              </td>
<%      } if (Cancellabile.compareTo("SI") == 0) { %>
				<!--- BOTTONE DI CANCELLAZIONE -->
    		<td class="LBG">
      		<a href="Javascript:conferma('siap.sige.provvedimento.action.ActCancellaProvvedimento','<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>','<%=provvedimento.getIdProvvedimentoSige()%>','TornaQui','<%=TornaQui%>');">
        		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
      		</a>
    		</td>
<%
  }
%>
  		<!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>

    <tr>
       <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    </tr>
    <tr>
<%  if (Modificabile.compareTo("SI")==0) {%> 
    	<jsp:include page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>">
        	<jsp:param name="MagAssRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneDecretoInammissibilita"/>
	    </jsp:include>
    </tr>
      <jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>">
      <jsp:param name="AvvRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneDecretoInammissibilita"/>
      </jsp:include>
		<%}else{%>
    	<jsp:include page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>"/>
    </tr>
      <jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>"/>
		<%}%>
      <jsp:include page="<%=ICostantiProvvedimentoSige.PG_INCLUDE_DATI_PROVVEDIMENTO%>"/>
  </table>
		<br>

  <table width="90%">
    <tr>
      <td class="Titolo">Oggetti</td>
      <td class="Titolo">Esito</td>
    </tr>

    <tr>
  			<div id="elenco1" style="width: 100%; display:block">
  				<jsp:include page="/jsp/files/siap/sige/tenore/ElencoDettaglioTenori.jsp"/>
  			</div>
	</tr>
  </table>

  <table cellspacing="2" cellpadding="2">
	  <tr>
	    <td  colspan=2> &nbsp;</td>
	  </tr>
	
	  <tr>
	    <td class="Titolo" colspan=2>Motivazioni</td>
	  </tr>
<%
    if( ProvvedimentoEvento.getMotiviProvvedSige() != null && ProvvedimentoEvento.getMotiviProvvedSige().size() > 0)
    {
      int lCount = ProvvedimentoEvento.getMotiviProvvedSige().size();
      for( int x=0; x<lCount; x++ )
      {
        // Stampa la descrizione motivazione se è != null
        MotivazioneProvvedimentoSigeModel lMotivazione  = (MotivazioneProvvedimentoSigeModel) ProvvedimentoEvento.getMotiviProvvedSige().get(x); 
        if( lMotivazione.getDescrMotivazione() != null )
        {
%>
          <tr>
            <td class="l" colspan=2><%=lMotivazione.getDescrMotivazione()%></td>
          </tr>
<%
        }
        // Stampa la descrizione altra motivazione se != null
        else if( lMotivazione.getAltraMotivazione() != null )
        {
%>
          <tr>
            <td class="l" colspan=2><%=lMotivazione.getAltraMotivazione()%></td>
          </tr>
<%
        }
      }
    }
    else if (Modificabile.compareTo("SI") == 0)
    {
 %>
    <tr>
      <td class="L">
        <font class="label">Motivazioni assenti</font>
      </td>
     </tr>
<%
    }
%>

<%
  	if (provvedimento.getDescrUfficioDestinatario()!= null  && provvedimento.getDescrUfficioDestinatario().length() > 2)
  	{
%>
	  	<tr>
	    	<td class="l">Ufficio Competente</td>
	    	<td class="l"><font class="campo"><%=provvedimento.getDescrUfficioDestinatario()%></font></td>
	  	</tr>
<% 
  	}
%>   
  </table>
   <form name="dettaglio"> 
<%  
  if(provvedimento.getCodTipoProvvedimento().trim().length() > 1 )
  {
  // Combo template di stampa solo sulle Emissioni di Decreti Nuovi
  // E se non esiste il template predefinito.
     if(ProvvedimentoEvento.getEventoNotifica().getEvento().getTemIdTemplate() == null || ProvvedimentoEvento.getEventoNotifica().getEvento().getTemIdTemplate().trim().length() < 2 )
     {
%>
      <jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
<%
     }
   } // endif cod tipo Decreto != null
%>
</form>

  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post">
    <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
            <input type="HIDDEN" name="IdEvento"  value="<%=IdEvento%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sige.provvedimento.action.ActDettaglioDecretoInammissibilita">
          </td>
        </tr>
        </table>
        </FORM>
      </div>
  
   </body>
</html>