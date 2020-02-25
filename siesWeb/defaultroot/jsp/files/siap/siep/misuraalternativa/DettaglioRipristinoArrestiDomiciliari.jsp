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
<jsp:useBean id="autoritaEsternaC"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="UffTDS"       scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"       scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="daticssa"      scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="flagmisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="sedeUfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="lPosGiuModificata" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="NoteCssa"      scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteAutC"      scope="request" class="java.lang.String"/>
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

  String lFlagSemiAutC = "N";
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

<%if(tipoMisura.equals("REVPRO2748")){%>
    <font class="campo">Rigetto revoca  prosecuzione arresti domiciliari ex art. 656 comma 10</font>
<%}else if(tipoMisura.equals("REVPRO2749")){%>
    <font class="campo">Rigetto revoca  prosecuzione Arresti Domiciliari ex art 89 dpr 309/90</font>
<%}else if(tipoMisura.equals("REVPRO2758")){%>
    <font class="campo">Rigetto revoca  prosecuzione permanenza in casa  ex art. 656 comma 10</font>
<%}else if(tipoMisura.equals("REVPRO2759")){%>
    <font class="campo">Rigetto revoca  prosecuzione collocamento in comunità  ex art.  656 comma 10</font>
<%}else if(tipoMisura.equals("EFFSOS2752")){%>
    <font class="campo">Perdita di efficacia -  Sospensione provvisoria  prosecuzione arresti domiciliari ex art. 656 comma 10</font>
<%}else if(tipoMisura.equals("EFFSOS2753")){%>
    <font class="campo">Perdita di efficacia - Sospensione provvisoria  prosecuzione Arresti Domiciliari ex art 89 dpr 309/90</font>
<%}else if(tipoMisura.equals("EFFSOS2754")){%>
    <font class="campo">Perdita di efficacia - Sospensione provvisoria  prosecuzione permanenza in casa  ex art. 656 comma 10</font>
<%}else if(tipoMisura.equals("EFFSOS2755")){%>
      <font class="campo">Perdita di efficacia - Sospensione provvisoria  prosecuzione collocamento in comunità  ex art.  656 comma 10</font>
<%}%>

  <font class="campo">Dettaglio Ripristino</font>
</td>
<%if(eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
 if(eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) {%>
 <!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActStampaMAArrestiDomiciliari&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&notificaE="+notificaE+"&tipoMisura="+tipoMisura%>"/>
   </jsp:include>
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
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>
<%
        }else {%>
         <td class="l">Data Fine Pena</td>
         <td class="lRosso" >
           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>


<%            }
      }
%>
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
        	  {%>
            	<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>
			<%} %>
         </td>
</tr>
<tr>
<%if(misuraalternativa.getChiaveAnnoFascicoloSius()!= null){%>

      <td class="l">Anno / N.Sius</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
      <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
     </td>

<%}
if(misuraalternativa.getAnnoRegistro()!= null){%>

     <td class="l"> Anno / Numero Decisione </td>
     <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
     <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
     </td>
<%}%>
  </tr>
 <tr>
    <td class="l">Ufficio che ha emesso l'Ordinanza</td>
    <%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
    <%
    	String descrTipoUfficio = StringUtils.toStringJSP(sedeUfficioEmittente.getDescrTipoUfficio());
    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
    			"UDSM".equals(sedeUfficioEmittente.getCodTipoUfficio())) {
    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
    	}
    %>
    <td class="l"> <font class="campo"><%=descrTipoUfficio%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescProvincia())%></font></td>
 </tr>
 <tr>
   <td class="l">Oggetto Ordinanza </td>
   <td class="l"><font class="campo">
<%if(misuraalternativa.getCodNaturaDecisione().equals("RG")){%>
<%=StringUtils.toStringJSP(misuraalternativa.getDescrNaturaDecisione())%> &nbsp;
<%}%>
 <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%>
</font>&nbsp;</td>

   <td class="l">Data Emissione Ordinanza </td>
   <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%>
    </font>
   </td>
  </tr>
<%if(misuraalternativa.getDescrLuogoProva()!= null){%>

 <tr>
      <td class="l">Domicilio Imposto </td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%>&nbsp;</font>
      </td>
   </tr>
<%
  }
%>
  <tr>
<%
  if(  misuraalternativa.getCodTipoUfficioScarcerazione() != null
    && misuraalternativa.getCodTipoUfficioScarcerazione().equals("PROC")
    && misuraalternativa.getDataInizioMisura() != null  )
  {
%>
    <td class="l">Data Misura</td>
    <td class="l">
      <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%>
      </font>
    </td>
<%
  }

  if(misuraalternativa.getDataFineMisura() != null)
  {
%>
     <td class="l">Data Fine Misura</td>
     <td class="l">
       <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataFineMisura(),"dd-MM-yyyy"))%>
       </font>
     </td>
<%
  }
%>
</tr>
<%if(misuraalternativa.getDataScarcerazione()!= null){%>
 <tr>
     <td class="l">Data di Scarcerazione</td>
     <td class="l"><font class="campo">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataScarcerazione(),"dd-MM-yyyy"))%>
      &nbsp;</font></td>
  </tr>
<%}%>

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

<%if(notificaE != null && notificaE.equals("autorita"))
{%>
<tr>
    <td class="l">Autorità Competente per territorio</td>

  <td class="L"><font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaE.getDescrTipoAutorita())%></font>
<%if(autoritaEsternaE != null && !autoritaEsternaE.getDescrSede().equals("-"))
{%>
  di <font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaE.getDescrSede()) %>   </font>
<%}%>
 </td>
</tr>
<%if(NoteAutE != null && !NoteAutE.equals(""))
{%>
<tr>
<td class="l">Indirizzo</td>
    <td class="l">
         <font class="campo"><%=NoteAutE%>&nbsp;</font>
    <td>
</tr>

<%}
}else if(notificaE != null && notificaE.equals("istituto")){
if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
{
int cont=0;
while(cont < eventonotifica.getNotifiche().length)
{
if(eventonotifica.getNotifiche()[cont].getCodTipoNotifica().equals("E"))
  {%>
   <tr>
    <td class="l">Istituto di Detenzione</td>


      <td class="l">
         <font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di
          <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getIstitutoDetenzione().getDescrComune())%></font>
       </td>
  </tr>

 <%}
cont++;
}
}
%>

<%}%>
</tr>


<%if(tipoMisura.equals("SEMILIBERTA") && misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV"))
{
 lFlagSemiAutC = "S";
 if(autoritaEsternaC != null && autoritaEsternaC.getCodTipoAutorita()!= null && autoritaEsternaC.getCodSede()!= null){%>

 <tr>
    <td class="l">Autorità di Polizia Competente per territorio</td>
    <td class="L">
      <font class="campo">
        <%=StringUtils.toStringJSP(autoritaEsternaC.getDescrTipoAutorita())%></font>
 <%if(autoritaEsternaC !=null && !autoritaEsternaC.getDescrSede().equals("-"))
{%>
di <font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaC.getDescrSede()) %>
     </font>
<%}%>
     </td>
 </tr>

<%if(NoteAutC!= null && !NoteAutC.equals("")){%>

<tr>
<td class="l">Indirizzo</td>
     <td class="L">
         <font class="campo"><%=NoteAutC%>&nbsp;</font>
      </td>
</tr>
<%}
 }
}%>


<!--UEPE competente-->

  <tr>
      <td class="l"><%=StringUtils.toStringJSP(daticssa.getTipoDesc())%></td>
         <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(daticssa.getComune())%>-<%=StringUtils.toStringJSP(daticssa.getIndirizzo())%></font>
      </td>
  </tr>
<%if(NoteCssa!= null && !NoteCssa.equals(""))
{%>
<tr>
 <td class="l">Note</td>
   <td  class="L">
      <font class="campo"><%=NoteCssa%>&nbsp;</font>
   </td>
</tr>

<%}
if(UffUDS!= null )
{
%>
   <tr>
     <td class="l">Destinatario</td >
      <td class="L"><%=StringUtils.toStringJSP(UffUDS)%></td>
  </tr>
<%if(NoteUDS!= null && !NoteUDS.equals(""))
{%>
<tr>
 <td class="l">Note</td>
   <td  class="L">
     <font class="campo"><%=NoteUDS%>&nbsp;</font>
   </td>
</tr>
<%}
}%>

   <tr>
     <td class="l">Destinatario</td>
      <td class="L"><%=StringUtils.toStringJSP(UffTDS)%></td>
  </tr>
<%if(NoteTDS!= null && !NoteTDS.equals(""))
{%>
<tr>
 <td class="l">Note</td>
   <td  class="L">
      <font class="campo"><%=NoteTDS%>&nbsp;</font>
   </td>
</tr>
<%}%>

<%
if(lFlagSemiAutC.equals("N"))
{
if(autoritaEsternaC != null && autoritaEsternaC.getCodTipoAutorita()!= null && autoritaEsternaC.getCodSede()!= null){%>

 <tr>
    <td class="l">Autorità di Polizia Competente per territorio</td>
    <td class="L">
      <font class="campo">
        <%=StringUtils.toStringJSP(autoritaEsternaC.getDescrTipoAutorita())%></font>
 <%if(autoritaEsternaC !=null && !autoritaEsternaC.getDescrSede().equals("-"))
{%>
di <font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaC.getDescrSede()) %>
     </font>
<%}%>
     </td>
 </tr>
<%if(NoteAutC!= null && !NoteAutC.equals("")){%>

<tr>
<td class="l">Indirizzo</td>
     <td class="L">
         <font class="campo"><%=NoteAutC%>&nbsp;</font>
      </td>
</tr>
<%}
 }
}%>

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
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActUploadRipristinoArrestiDomiciliari">
            <input type="HIDDEN" name="tipoMisura" value="<%=tipoMisura%>">
            <input type="HIDDEN" name="notificaE" value="<%=notificaE%>">

            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="IdPosizioneGiuridica" value="<%=lPosizione.getIdPosizioneGiuridica()%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misuraalternativa.action.ActDettaglioRipristinoArrestiDomiciliari">
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