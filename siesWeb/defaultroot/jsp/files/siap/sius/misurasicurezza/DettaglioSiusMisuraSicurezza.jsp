<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.rifasiep.action.ICostantiRifFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="misurasicurezza" scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="lTipoFunzione"   scope="request" class="java.lang.String"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<jsp:useBean id="riferimentoTitoloEsecutivo" scope="request" class="siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel" />
<jsp:useBean id="riferimentoTitoloEsecutivoPrincipale" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<%
	FascicoloGPModel lFascicolo = (FascicoloGPModel)session.getAttribute("fascicoloSiusGP");
%>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Misura Sicurezza </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
<%
	if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 	{
%>
		<script language="JavaScript">
			var aForm=null;
 			function DisabilitaFine()
  		{
    		aForm=document.getElementById("Fine");
    		Disabilita();
  		}

 			function Disabilita()
  		{
    		if (aForm==null)
       		aForm=document.getElementById("Misura");

    		document.Fine.F.disabled = true;
    		document.Misura.S.disabled = true;

   			aForm.submit();
  		}
			function conferma2Param(a_action, a_entityname, a_entityvalue, a_entityname2, a_entityvalue2, a_destnname, a_destvalue )
			{
				str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue + "&" + a_entityname2 + "=" +a_entityvalue2 +  "&" + a_destnname + "=" +a_destvalue;
    		if (window.confirm('Confermi la cancellazione ?'))
    		{
					window.location.href=str;
    		}
			}
 	  	
		</script>
<%
	}	
%>
</head>

<body class="corpo">
		<FORM name="comandi" >
	    	<table>
	      		<tr>
	      			<td class="LBG"><a href="Javascript:window.print();">
	      				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
	      			</td>
	        		<td class="LBG">
			        	<font class="label">Funzione :</font>&nbsp;
			        	<font class="campo">Dettaglio Misura Sicurezza</font>
			        </td>
<% 		
							if ( misurasicurezza.getCodUfficioInserimento().compareTo(UtenteConnesso.getUfficioUtente().getCodUfficio()) == 0)
  						{%>
								<!-- BOTTONE DI MODIFICA -->
								<td class="LBG">
									<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.misurasicurezza.action.ActLoadModificaSiusMisuraSicurezza&<%=ICostantiSiusMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>=<%=misurasicurezza.getIdMisuraSicurezza()%>&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=misurasicurezza.getFasSiuIdFascicoloSius()%>&TornaQui=<%=TornaQui%>" >
										<img  align="middle" src="/images/modifica24.gif" alt="Modifica Misura Sicurezza" width="24" height="24" border="0">
									</a>
								</td>

								<!--- BOTTONE DI CANCELLAZIONE -->
			    			<td class="LBG">
			      			<a href="Javascript:conferma('siap.sius.misurasicurezza.action.ActCancellaSiusMisuraSicurezza','<%=ICostantiSiusMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>','<%=misurasicurezza.getIdMisuraSicurezza()%>', 'TornaQui','<%=TornaQui%>');">
			        			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella Misura Sicurezza" width="24" height="24" border="0">
			      			</a>
			    			</td>
							<%}%>

      				<!-- BOTTONE DI RITORNO -->
        				<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
						</tr>
	    	</table>
		</FORM>

	<br>
	    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>
   <table cellspacing=2 cellpadding=2>
		<tr>
				<td class="l">Natura Misura</td>
				<td class="l"><font class="campo"><%=misurasicurezza.getDescrNatura() %></font></td>
		</tr>
		<tr>
				<td class="l">Tipo Misura</td>
				<td class="l"><font class="campo"><%=misurasicurezza.getDescrTipo() %></font></td>
		</tr>
		<tr>
      		<td class="l">Durata Misura</td>
				<td class="l">Anni
				<font class="campo"><%=StringUtils.toStringJSP(misurasicurezza.getNumAnni(),"0") %></font>
                		Mesi
				<font class="campo"><%=StringUtils.toStringJSP(misurasicurezza.getNumMesi(),"0") %></font>
		 		Giorni
				<font class="campo"><%=StringUtils.toStringJSP(misurasicurezza.getNumGiorni(),"0") %></font>
			</td>
		</tr>

<%  
	if (misurasicurezza.getFasSieIdFascicoloSiepRif()!=null && !misurasicurezza.getFasSieIdFascicoloSiepRif().equals("")) {
%>
    <tr>
      <td class="l">Riferimento Titolo Esecutivo</td>
	  <td class="l">
        <font class="campo">

           <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.rifasiep.action.ActLoadDettaglioRifFascicoloSiep&<%=ICostantiRifFascicoloSiep.CAMPO_ID_RIFERIMENTO_FASCICOLO_SIEP%>=<%=riferimentoTitoloEsecutivo.getIdRiferimentoFascicoloSiep()%><%=retParam%>" >

<% 
	if( riferimentoTitoloEsecutivo.getFlagMS() == null || riferimentoTitoloEsecutivo.getFlagMS().equals("") || riferimentoTitoloEsecutivo.getFlagMS().equals("N")) {
%>
            <%=riferimentoTitoloEsecutivo.getAnnoFascicoloSiep()%>
            /
            <%=riferimentoTitoloEsecutivo.getProgrFascicoloSiep()%>
<%
	} else {
		if (riferimentoTitoloEsecutivo.getAnnoFascicoloSiep() == null && riferimentoTitoloEsecutivo.getProgrFascicoloSiep() == null){
			if(riferimentoTitoloEsecutivo.getFlagMS().equals("EMS")){
%>		
				Es. Mis. Sic.	
<%		
			} else {
%>
				Es. Pene Pec.
<%				
			}
		} else {
			if(riferimentoTitoloEsecutivo.getFlagMS().equals("EMS")){
%>
				<%=riferimentoTitoloEsecutivo.getAnnoFascicoloSiep()%>
				/ 
				<%=riferimentoTitoloEsecutivo.getProgrFascicoloSiep()%> MS
<%
			} else {
%>
				<%=riferimentoTitoloEsecutivo.getAnnoFascicoloSiep()%>
				/ 
				<%=riferimentoTitoloEsecutivo.getProgrFascicoloSiep()%> PP
<%				
			}
		}
	}
%>

          </a>

			&nbsp;&nbsp;<%=riferimentoTitoloEsecutivo.getDescrTipoAutoritaEmittente()%>&nbsp;<%=riferimentoTitoloEsecutivo.getDescrLuogoEmittente()%>

        </font>&nbsp;
        <%
        if (riferimentoTitoloEsecutivo.getDataProvvedimento() != null)
        {%>
          <font class="label"> del </font>&nbsp;
          <font class="campo"><%=DateUtils.getDateToString(riferimentoTitoloEsecutivo.getDataProvvedimento(),"dd-MM-yyyy")%></font>&nbsp;
        <%}%>
      </td>
    </tr>
    
<%
	} else if (misurasicurezza.getSenIdSentenza()!=null && !misurasicurezza.getSenIdSentenza().equals("")) {
%>
    <tr>
      <td class="l">Riferimento Titolo Esecutivo</td>
	  <td class="l">
        <font class="campo">

           <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=riferimentoTitoloEsecutivoPrincipale.getIdFascicoloSiep()%><%=retParam%>" >
            <%=riferimentoTitoloEsecutivoPrincipale.getChiaveAnno()%>
            /
            <%=riferimentoTitoloEsecutivoPrincipale.getChiaveProgr()%>
          </a>

			&nbsp;&nbsp;<%=riferimentoTitoloEsecutivoPrincipale.getSentenza().getDescrTipoAutoritaEmittente()%>&nbsp;<%=riferimentoTitoloEsecutivoPrincipale.getSentenza().getDescrLuogoEmittente()%>

        </font>&nbsp;
        <%
        if (riferimentoTitoloEsecutivoPrincipale.getSentenza().getDataProvvedimento() != null)
        {%>
          <font class="label"> del </font>&nbsp;
          <font class="campo"><%=DateUtils.getDateToString(riferimentoTitoloEsecutivoPrincipale.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font>&nbsp;
        <%}%>
      </td>
    </tr>
    
<%
	} else {
%>

   <tr>
      <td class="l">Riferimento Titolo Esecutivo</td>
	  <td class="l">&nbsp;</td>
    </tr>

<%
	} 
%>

<%
		//lTipoFunzione per capire che si proviene da iscrizione guidata		
		if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  
 		{
%>
			<tr>
				<td class="lNoBord">
					<FORM method="POST" name="Misura" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadInserisciSiusMisuraSicurezza&lTipoFunzione=<%=lTipoFunzione%>">
	      		<br>
	      		<INPUT class="bottone" type="button" name="S" value="Altra Misura Sicurezza" onclick="Javascript:Disabilita();">
	 				</FORM>
				</td>
				<td class="lNoBord">
					<FORM method="POST" name="Fine" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=misurasicurezza.getFasSiuIdFascicoloSius()%>">
		      	<br>
		      	<INPUT class="bottone" type="button" name="F" value="  Fine  " onclick="Javascript:DisabilitaFine();">
				 </FORM>
				</td>
			</tr>
<%
		}
%>
  </table>
</body>
</html>