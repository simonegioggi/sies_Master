<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"         	scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="penaresidua"        	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="eventonotifica" 		scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="ufficioge"       		scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficiouds"      		scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficiotds"         	scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficiopm"        		scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="istituto"         		scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="autoritaEsterna"		scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="noteautoritaEsterna"	scope="request" class="java.lang.String"/>
<jsp:useBean id="titolodettaglio"		scope="request" class="java.lang.String"/>
<%-- MEV_66: aggiunto campo in visualizzazione = uepe/ussm --%>
<jsp:useBean id="cssa"  				scope="request" class="siap.sico.cssa.model.CSSAModel"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");

PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();

LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
if(lPosizione == null)
  	lPosizione = new PosizioneGiuridicaModel();

if(lLuogoDetenzione == null)
  	lLuogoDetenzione = new LuogoDetenzioneModel();

if(lAltraCausa == null)
  	lAltraCausa = new AltraCausaModel();
%>

<!-- DettaglioRichiestaGenerica -->
<html>
<head>
  <title>[S.I.E.S.] - Gestione Richiesta </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

</head>
<body class="corpo">
	<table>
  		<tr>
  			<td class="LBG">
  				<a href="Javascript:window.print();">
  					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
  				</a>
  			</td>
  			<td class="LBG">
  				<font class="label">Funzione :</font>&nbsp;&nbsp;
<%
if (titolodettaglio != null) {
%>
  				<font class="campo">Dettaglio <%=titolodettaglio%></font>
<%
}
%>
			</td>
<!-- BOTTONE DI STAMPA -->
<%
if (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null) {
	if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
		if ("5440".equals(eventonotifica.getEvento().getCodMotivo())
				|| "5441".equals(eventonotifica.getEvento().getCodMotivo())) {
%>
			<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
				<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaTrasmissioneAttiExArt51Bis&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&codposizionegiuridica="+posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>"/>
			</jsp:include>
<%
		} else {
%>
			<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
				<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaRichiestaGenerica&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&codposizionegiuridica="+posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>"/>
			</jsp:include>
<%
		}
	}
}
if (eventonotifica.getEvento().getFlagDocumentoRegistrato() == null) {
	if ("5440".equals(eventonotifica.getEvento().getCodMotivo())
			|| "5441".equals(eventonotifica.getEvento().getCodMotivo())) {
%>
			<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
				<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaTrasmissioneAttiExArt51Bis&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&codposizionegiuridica="+posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>"/>
			</jsp:include>
<%
	} else {
%>
			<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
				<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaRichiestaGenerica&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&codposizionegiuridica="+posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>"/>
			</jsp:include>
<%
	}
}
%>

<!-- BOTTONE DI TRASFERIMENTO -->
<%-- MEV_39: aggiunto codice 5408 per la trasmissione --%>
<%
if (("5440".equals(eventonotifica.getEvento().getCodMotivo())
		|| "5441".equals(eventonotifica.getEvento().getCodMotivo())
		|| "5408".equals(eventonotifica.getEvento().getCodMotivo()))
		&& (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null)) {
	if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0) {
		String action = "siap.siep.richiesta.action.ActLoadTrasferisciRichiestaAttiExArt51Bis";
		if ("5408".equals(eventonotifica.getEvento().getCodMotivo()))
			action = "siap.siep.richiesta.action.ActLoadTrasferisciRichiestaGenerica";
%>
			<td class="LBG">
		    	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=action%>&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>">
		        	<img src="<%=IWebConstants.IMAGES_DIR%>net24.gif" alt="Trasferisci" width="24" height="24" border="0">
		      	</a>
		    </td>
<%
	}
}
%>

</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
      <font class="campo">
      <%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {%>
              DETENUTO PER ALTRA CAUSA
       <%
        }
        else
        {
%>
          <%=lPosizione.getDescrPosizioneGiuridica()%>

<%
         }%>
         </font>
   </td>
</tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null )
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
           </tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if(lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>

            </td>
          </tr>
<%
        }%>
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
  <% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getIstitutoDetenzione() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
            </tr>
<%
          }
        }
%>

<%
       if (penaresidua.getDataInizio() != null)
       {
%>
 <tr>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
 </tr>
<%
       }

       if (penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
        <tr>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
        </tr>
<%
        }
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
        <tr>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
        </tr>
<%
        }
       }
%>

    
<%
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine()!=null)
        {
          if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())){
%>
	<tr>
         <td class="l">Data Fine Pena</td>
         <td class="L" >
           <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
         </td>
     </tr>    
<%
        }else
        {%>
         <td class="l">Data Fine Pena</td>
         <td class="lRosso" >
           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
         </td>
<%     }
      }
%>
<tr>
<%
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
          <td class="l">Reclusione</td>
          <td class="l" >
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
            <%if(penaresidua.getImportoMulta().compareTo(new BigDecimal(0))!=0){ %>

          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
            } }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
%>
      <td class="l" >Arresto</td>
      <td class="l" >
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
            <%if(penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0))!=0){ %>

      <td class="l">Ammenda</td>
      <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
            }}
    }
%>

       </tr>
    </table>
    
 <table cellpadding="4" width="80%">    
	<tr>
       <%if(eventonotifica.getEvento().getDataEmissione()!= null){%>
        <td class="l" colspan=25%>Data Emissione</td>
        <td class="L" colspan=25%>
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>

         </td>
<%}%>
<%if(eventonotifica != null && eventonotifica.getNotifiche()!= null && eventonotifica.getNotifiche().length >0 && eventonotifica.getNotifiche()[0] != null && eventonotifica.getNotifiche()[0].getDataInvio()!= null)
  {%>
        <td class="l" colspan=25%>Data Trasmissione </td>
        <td class="L" colspan=25%>
             <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>

         </td>
        <%}%>
      </tr>
    </table>
    
 	<table cellpadding="3" width="80%" >
<%
if (magistrato != null) {
%>  
		<tr>
<%
	if (!eventonotifica.getEvento().getCodMagistrato().equalsIgnoreCase("-")) {
%>
			<td class="l" width=20%>Magistrato Firmatario
			<td class="L" width=60%>
			    <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
				<font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
			</td>
<%
	} else {
%>
  			<td class="l" width=20%>Funzionario Firmatario
			<td class="L" width=60%>
		    	<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getEvento().getCognomeSoggettoPresentante())%></font>
				<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getEvento().getNomeSoggettoPresentante())%></font>
			</td>
<%
	}
%>
		</tr>
<%
}

if (eventonotifica.getEvento() != null && eventonotifica.getEvento().getCodTipoProvvedimento() != null){
%>
 	<tr>
      	<td class="l" width=20%>Tipo Atto</td>
      	<td class="l" width=60%>
      		<font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrTipoProvvedimento())%></font>
     	</td>
	</tr>
<%
}

if (eventonotifica.getEvento() != null && eventonotifica.getEvento().getCodMotivo() != null) {
%>
	<tr>
		<td class="l" width=20>Oggetto Atto</td>
      	<td class="l" width=60%>
      		<font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%></font>
     	</td>
  	</tr>
<%
}

if (eventonotifica.getCampoNote() != null && eventonotifica.getCampoNote().length > 0 && eventonotifica.getCampoNote()[0] != null
		&& eventonotifica.getCampoNote()[0].getDescr() != null) {
%>
 	<tr>
		<td class="l" width=20%>Contenuto </td>
		<td class="l" width=60%>
			<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getCampoNote()[0].getDescr())%></font>
		</td>
  	</tr>
<%
}

if (ufficioge != null && ufficioge.getDescrTipoUfficio() != null && !ufficioge.getDescrTipoUfficio().equals("")) {
%>
	<tr>
		<td class="l">Ufficio Giudice dell'Esecuzione</td>
      	<td class="l">
			<font class="campo"> <%=StringUtils.toStringJSP(ufficioge.getDescrTipoUfficio())%></font>&nbsp;di
         	<font class="campo"><%=StringUtils.toStringJSP(ufficioge.getDescrComune())%></font>
       	</td>
	</tr>
<%
}

if (ufficiouds != null && ufficiouds.getDescrComune() != null && !ufficiouds.getDescrComune().equals("")) {
	// MEV_66: aggiunto campo in visualizzazione = tdsm + udsm + uepe
	String descrTipoUfficio = ufficiouds.getDescrTipoUfficio();
	if ("UDSM".equals(ufficiouds.getCodTipoUfficio()))
		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
%>
	<tr>
<%
	if (eventonotifica.getEvento().getCodMotivo().equals("5440")
			|| eventonotifica.getEvento().getCodMotivo().equals("5441")) {
%>
		<td class="l" width=30%>Destinatario: <%=descrTipoUfficio%></td>

<%
	} else {
%>	
		<td class="l"width=20%><%=descrTipoUfficio%></td>
<%
	}
%>
		<td class="l">
			di&nbsp;&nbsp;<font class="campo"><%=StringUtils.toStringJSP(ufficiouds.getDescrComune())%></font>
		</td>
	</tr>				
<%
}
  
if (ufficiotds != null && ufficiotds.getDescrComune() != null && !ufficiotds.getDescrComune().equals("")) {
	// MEV_66: aggiunto campo in visualizzazione = tdsm + udsm + uepe
	String descrTipoUfficio = ufficiotds.getDescrTipoUfficio();
%>
	<tr>
<%
	if (eventonotifica.getEvento().getCodMotivo().equals("5440")
			|| eventonotifica.getEvento().getCodMotivo().equals("5441")) {
%>   		
		<td class="l" width=30%>Destinatario: <%=descrTipoUfficio%></td>
<%
	} else {
%>   
		<td class="l"><%=descrTipoUfficio%></td>
<%
	}
%>			   		
		<td class="l">
			di&nbsp;&nbsp;<font class="campo"><%=StringUtils.toStringJSP(ufficiotds.getDescrComune())%></font>
		</td>
	</tr>
<%
}

// MEV_66: aggiunto campo in visualizzazione = tdsm + udsm + uepe
if (!"".equals(cssa.getComune())) {
	String descUff = "Ufficio Esecuzione Penale Esterna";
	if ("USSM".equals(cssa.getTipoDesc()))
		descUff = "Ufficio Servizi Sociali Minorili";
%>
	<tr>
		<td class="l" width=30%><%=StringUtils.toStringJSP(descUff)%></td>
  		<td class="l">
  			<font class="campo"><%=StringUtils.toStringJSP(cssa.getComune())%> - <%=StringUtils.toStringJSP(cssa.getIndirizzo())%></font>
		</td>
	</tr>
<%
}
  
if (ufficiopm != null && ufficiopm.getDescrTipoUfficio() != null && !ufficiopm.getDescrTipoUfficio().equals("")) {
%>
    <tr>
      	<td class="l" width=30%>Ufficio Pubblico Ministero</td>
      	<td class="l">
			<font class="campo"> <%=StringUtils.toStringJSP(ufficiopm.getDescrTipoUfficio())%></font>&nbsp;di
         	<font class="campo"><%=StringUtils.toStringJSP(ufficiopm.getDescrComune())%></font>
       	</td>
	</tr>
<%
}
if (istituto != null && istituto.getDescrTipoIstituto() != null && !istituto.getDescrTipoIstituto().equals("")) {
%>
    <tr>
      	<td class="l" width=20%>Istituto di detenzione</td>
      	<td class="l">
         	<font class="campo"> <%=StringUtils.toStringJSP(istituto.getDescrTipoIstituto())%></font>&nbsp;di
         	<font class="campo"><%=StringUtils.toStringJSP(istituto.getDescrComune())%></font>
       	</td>
	</tr>
<%
}

if (autoritaEsterna != null && autoritaEsterna.getCodTipoAutorita() != null && autoritaEsterna.getCodSede() != null) {
%>
	<tr>
    	<td class="l" width=20%>Autorità di Polizia</td>
    	<td class="L">
    		<font class="campo"><%=StringUtils.toStringJSP(autoritaEsterna.getDescrTipoAutorita())%></font>
<%
	if (autoritaEsterna != null && !autoritaEsterna.getDescrSede().equals("-")) {
%>
			&nbsp;di&nbsp;<font class="campo"><%=StringUtils.toStringJSP(autoritaEsterna.getDescrSede())%></font>
<%
	}
%>

		</td>
	</tr>
<%
	if (noteautoritaEsterna != null && !noteautoritaEsterna.equals("")) {
%>
	<tr>
		<td class="l" width=20%>Indirizzo</td>
    	<td class="l">
         	<font class="campo"><%=noteautoritaEsterna%>&nbsp;</font>
    	<td>
	</tr>
<%
	}
}
%>
</table>
<br>
<div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActUploadRichiestaGenerica">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.richiesta.action.ActDettaglioRichiestaGenerica">
          </td>
        </tr>
      </table>
</form>
</div>
  <br>
  <br>
</body>
</html>