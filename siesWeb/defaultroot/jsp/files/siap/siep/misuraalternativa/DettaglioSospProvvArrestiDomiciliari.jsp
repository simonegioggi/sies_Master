<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="UffTDS"       scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"       scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="flagmisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="lPosGiuModificata" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="NoteTDS"      scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteAutE"      scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="notificaE"      scope="request" class="java.lang.String"/>
<jsp:useBean id="UffUDS"       scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteUDS"      scope="request" class="java.lang.String"/>
<jsp:useBean id="UfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="CodMotivo"      scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunti useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

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
<html>
<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
	<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

</head>
<body class="corpo">

  <table>
  <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
  <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;

<%if(tipoMisura.equals("PROVVISORIAARRESTIDOMICILIARI")){%>
    <font class="campo">Sospensione provvisoria Arresti Domiciliari ex art. 656 comma 10</font>
<%}else if(tipoMisura.equals("PROVVISORIADOMICILIARI")){%>
    <font class="campo">Sospensione provvisoria Domiciliari ex art. 89 dpr 309/90</font>
<%}else if(tipoMisura.equals("PROVVISORIAPERMANENZA")){%>
    <font class="campo">Sospensione provvisoria Permanenza in Casa</font>
<%}else if(tipoMisura.equals("PROVVISORIACOLLOCAMENTO")){%>
    <font class="campo">Sospensione provvisoria Collocamento in Comunità</font>
<!-- Inizio MAC 2016/10/21 Gestione codice MOTIVO_PROVVEDIMENTO = 2291 -->
<!-- MAC 2017/04/01 Ripristinati i codici (2741,2742,2743,2756) -->

<!--// PEC m_dg.DOG07.01-10-2018.0030206.U (deve essere censito anche i codice 2291)  -->
<%} else if(tipoMisura.equals("PROVVISORIAARRESTIDOM")){%>
     <font class="campo">Sospensione provvisoria Arresti Domiciliari</font>
<%}%>
</td>
<%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
 if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) {%>
  <!-- BOTTONE DI UPLOAD -->
	<td class="LBG">
    	<a  href="#1" onclick="javascript:lookUpload();">
      <img  align="middle" src="/images/upload24.gif" alt="Upload Stampa" width="24" height="24" border="0">
    	</a>
	</td>
 	<!-- BOTTONE DI STAMPA -->
 	<!-- Inizio MAC 2016/10/21 
     	  Sostituita la action ActStampaSospProvvArrestiDomiciliari 
     	  poichè la classe non esiste
     -->
   	<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
		<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActStampaMAArrestiDomiciliari&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&notificaE="+notificaE+"&tipoMisura="+tipoMisura%>"/>
   	</jsp:include>
   	<!-- Fine MAC 2016/10/21 -->
<%}%>

<%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
 {%>
  <!-- BOTTONE DI UPLOAD -->
  <td class="LBG">
    <a  href="#1" onclick="javascript:lookUpload();">
      <img  align="middle" src="/images/upload24.gif" alt="Upload Stampa" width="24" height="24" border="0">
    </a>
	</td>
 <!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActStampaMAArrestiDomiciliari&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&notificaE="+notificaE+"&tipoMisura="+tipoMisura%>"/>
   </jsp:include>
<%}%>
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<table>
<%if(flagmisura.equals("N"))
{%>

  <tr><td><input type="HIDDEN" name="flagmisura" value="N"></td></tr>
 <%}else{%>
   <tr><td><input type="HIDDEN" name="flagmisura" value="S"></td></tr>
<%}%>
 <tr><td><input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>"></td></tr>

    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
      <font class="campo">
      <%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       {%>
              DETENUTO PER ALTRA CAUSA
       <%}
        else
        {
            if(lPosGiuModificata != null && lPosGiuModificata.getIdPosizioneGiuridica() != null)
            {
              %>
                   <%=lPosGiuModificata.getDescrPosizioneGiuridica()%>
            <%
             }else
              {
                %>
                     <%=lPosizione.getDescrPosizioneGiuridica()%>

             <%}}%>
         </font>
   </td>
</tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if(lAltraCausa.getIstitutoDetenzione() != null )
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
        else if(lLuogoDetenzione.getIstitutoDetenzione() != null )
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
           <%if(penaresidua.getImportoMulta().compareTo((new BigDecimal(0)))!=0){%>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>

<%
        }  }
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
           <%if(penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0)))!=0){%>

      <td class="l">Ammenda</td>
      <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
          } }
    }
%>
</tr>
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

       if ( penaresidua.getFlagErgastolo() != null)
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
      <tr>
<%
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine()!=null)
        {
          if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
         {
%>
         <td class="l">Data Fine Pena</td>
         <td class="L" >
           <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
         </td>
<%
        }else {%>
         <td class="l">Data Fine Pena</td>
         <td class="lRosso" >
           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
         </td>


<%            }
      }
%>
</tr>

        <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
       </tr>
<tr>
        <td class="l">Data Emissione</td>
        <td class="L" >
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>
         </td>
        <td class="l">Data Trasmissione</td>
        <td class="L" >
        	<%if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0)
        	  { %>
             	<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>
            <%} %>
         </td>
      </tr>

 <tr>
<%if(misuraalternativa.getChiaveAnnoFascicoloSius()!= null)
{%>
      <td class="l">Anno / N.Sius</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
      <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
     </td>
<%}
if(misuraalternativa.getAnnoRegistro()!= null){%>
     <td class="l"> Anno / Numero Decreto </td>
     <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
     <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
     </td>
<%}%>
  </tr>
 <tr>
	<td class="l">Ufficio che ha emesso il Decreto</td>
	<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
    <%
    	String descrTipoUfficio = StringUtils.toStringJSP(UfficioEmittente.getDescrTipoUfficio());
    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
    			"UDSM".equals(UfficioEmittente.getCodTipoUfficio())) {
    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
    	}
    %>
    <td class="l"> <font class="campo"><%=descrTipoUfficio%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%></font></td>
</tr>
 <tr>
   <td class="l">Oggetto Decreto </td>
   <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
   <td class="l">Data Emissione Decreto </td>
   <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%>

    </font>
   </td>
  </tr>

   <tr>
     <td class="l">Data Misura</td>
     <td class="l"><font class="campo">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%>
      </font></td>
  </tr>
<%if(misuraalternativa != null && misuraalternativa.getNote() != null){%>
<tr>
<td class="l">Note</td>
    <td class="l">
         <font class="campo"><%=misuraalternativa.getNote()%>&nbsp;</font>
    <td>
</tr>

<%}
  if(magistrato != null){%>
  <tr>
   <td class="l">Magistrato Firmatario
   <td class="L">
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
   </td>
  </tr>
<%}%>

<%if(notificaE.equals("autorita"))
{%>
   <tr>
    <td class="l">Destinatario per l'esecuzione</td>

  <td class="L"><font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaE.getDescrTipoAutorita())%></font>
 <%if(autoritaEsternaE !=null && !autoritaEsternaE.getDescrSede().equals("-"))
{%>
  di <font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaE.getDescrSede()) %></font>
<%}%>
     </td>
  </tr>
<%if(NoteAutE!= null && !NoteAutE.equals(""))
{%>
<tr>
<td class="l">Indirizzo</td>
    <td class="l">
         <font class="campo"><%=NoteAutE%>&nbsp;</font>
    <td>
</tr>
<%}
}else if(notificaE.equals("istituto")){
if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
{
int cont=0;
while(cont < eventonotifica.getNotifiche().length)
{
if(eventonotifica.getNotifiche()[cont].getCodTipoNotifica().equals("E"))
  {%>
   <tr>
    <td class="l">Istituto di detenzione</td>


      <td class="l">
         <font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di
          <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getIstitutoDetenzione().getDescrComune())%></font>
       </td>
  </tr>

 <%}
cont++;
}
}%>

<%}%>
</tr>

<% if (UffUDS != null && !UffUDS.equals("")) { %>
	<tr>
		<td class="l">Destinatario</td >
      	<td class="L"><%=StringUtils.toStringJSP(UffUDS)%></td>
	</tr>
	<% if (NoteUDS != null && !NoteUDS.equals("")) { %>
		<tr>
 			<td class="l">Note</td>
   			<td  class="L">
     			<font class="campo"><%=NoteUDS%>&nbsp;</font>
   			</td>
		</tr>
	<% }
} %>

<% if (UffTDS != null && !UffTDS.equals("")) { %>
	<tr>
		<td class="l">Destinatario</td >
     	<td class="L"><%=StringUtils.toStringJSP(UffTDS)%></td>
	</tr>
	<% if (NoteTDS != null && !NoteTDS.equals("")) { %>
		<tr>
	 		<td class="l">Note</td>
	  		<td class="L">
	      		<font class="campo"><%=NoteTDS%>&nbsp;</font>
	   		</td>
	   	</tr>
	<%}%>
<%}%>

</table>
 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActUploadSospProvvArrestiDomiciliari">
            <input type="HIDDEN" name="tipoMisura" value="<%=tipoMisura%>">
            <input type="HIDDEN" name="notificaE" value="<%=notificaE%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="IdPosizioneGiuridica" value="<%=lPosizione.getIdPosizioneGiuridica()%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misuraalternativa.action.ActDettaglioSospProvvArrestiDomiciliari">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%= CodMotivo %>">
          </td>
        </tr>
      </table>
</form>
</div>
  <br>
  <br>
</body>
</html>