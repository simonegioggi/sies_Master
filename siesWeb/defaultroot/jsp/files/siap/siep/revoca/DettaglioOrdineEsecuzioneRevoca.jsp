<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.richiesta.action.ICostantiRichiesta" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel"%>

<jsp:useBean id="eventonotifica"   scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"   scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<jsp:useBean id="autorita" scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="listaAvvSiep" scope="request" class="java.util.ArrayList"/>

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
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Dettaglio Revoca Sospensione</font>
    </td>
<%
    if (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null)
      if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0)
      {
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--td class="LBG">
  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.revoca.action.ActStampaOrdineEsecuzioneRevoca&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
    <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
  </a>
</td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.revoca.action.ActStampaOrdineEsecuzioneRevoca&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   </jsp:include>
<%
      }

      if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
      {
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--td class="LBG">
  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.revoca.action.ActStampaOrdineEsecuzioneRevoca&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
    <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
  </a>
</td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.revoca.action.ActStampaOrdineEsecuzioneRevoca&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   </jsp:include>
<%
      }
%>
    </tr>
  </table>
  <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=3>
        <font class="campo">
<%
          if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
          {
%>
            DETENUTO PER ALTRA CAUSA
<%
          }
          else
          {
%>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
          }
%>
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
<%
             //  if(lAltraCausa.getDescrLuogoIstituto()!=null)
              // {
%>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
<%
               //}
%>
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
<%
             // if(lLuogoDetenzione.getDescrLuogo()!=null)
             // {
%>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
             // }
%>
            </td>
          </tr>
<%
        }
%>
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
<%
    // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
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
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
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
          <td class="L">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;
            </font>
          </td>
<!--
        </tr>
-->
<%
       }

/*
       if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFinePresunta() != null)
       {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<tr>
  <td class="l">Data Fine Pena Automatica</td>
  <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%> &nbsp;</font></td>
</tr>
--%>
<%
/*
       }
*/
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
        else if(penaresidua.getFlagErgastolo().equals("D"))
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
<!--
      </tr>
      <tr>
-->
<%
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine()!=null)
        {
          String lClassTd="l";
          String lClassFont="campo";
          if( penaresidua.getDataFine() != null && !penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()) )
          {
            lClassTd="lRosso";
            lClassFont="lRosso";
          }
%>
          <td class="l">Data Fine Pena <!--Manuale--></td>
          <td class="<%=lClassTd%>">
            <font class="<%=lClassFont%>">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;
            </font>
          </td>
<%
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
<%
          if(penaresidua.getImportoMulta().compareTo(new BigDecimal(0))!=0)
          {
%>
            <td class="l">Multa</td>
            <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
          }
        }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}
    else
    {
%>
      <td class="l" >Arresto</td>
      <td class="l" >
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
<%
      if(penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
      {
%>
        <td class="l">Ammenda</td>
        <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
    }
  }
%>
      <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
      </tr>
      <tr>
<%
        if(eventonotifica.getEvento().getDataEmissione()!= null)
        {
%>
          <td class="l">Data Emissione</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%></font>
          </td>
<%
        }

        // SE NON ESISTE NESSUNA NOTIFICA FALLISCE
        if(eventonotifica.getNotifiche()[0].getDataInvio()!= null)
        {
%>
          <td class="l">Data Trasmissione</td>
          <td class="L">
            <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>
          </td>
<%
        }
%>
      </tr>
<%
  if(magistrato != null)
  {
%>
    <tr>
     <td class="l">Magistrato Assegnatario
     <td class="L">
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
     </td>
    </tr>
<%
  }

  if(autorita.getIdNotifica() != null)
  {
%>
    <tr>
      <td class="l">Autorita Notifica</td>
      <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP( autorita.getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
      di
      <font class="campo"><%=StringUtils.toStringJSP( autorita.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(autorita.getNote())%></font>&nbsp;</td>
    </tr>
<%
  }

  // Avvocati Siep
  for(int i=0; i<listaAvvSiep.size(); i++)
  {
    NotificaModel lAvvocatoSiep = (NotificaModel)listaAvvSiep.get(i);
    if(lAvvocatoSiep.getAvvSiep() == null)
    {
      lAvvocatoSiep.setAvvSiep( new AvvocatoSiepModel() );
    }
%>
      <tr>
       <td class="l">Avvocato per  Notifica</td>
       <td class="L" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(lAvvocatoSiep.getAvvSiep().getAvvocato().getCognome()) +" "+ StringUtils.toStringJSP(lAvvocatoSiep.getAvvSiep().getAvvocato().getNome())%></font>&nbsp;
        &nbsp;Foro di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvocatoSiep.getAvvSiep().getAvvocato().getForo())%>
        </font>
        &nbsp;Difensore di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvocatoSiep.getAvvSiep().getAvvocato().getDescrTipo())%>
        </font>
       </td>
      </tr>
<%
      if(lAvvocatoSiep.getAutoritaEsterna() == null)
      {
        lAvvocatoSiep.setAutoritaEsterna( new AutoritaEsternaModel() );
      }
%>
        <tr>
          <td class="l">Autorita Notifica</td>
          <td class="L" colspan=2>
            <font class="campo"><%=StringUtils.toStringJSP( lAvvocatoSiep.getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
            di
            <font class="campo"><%=StringUtils.toStringJSP( lAvvocatoSiep.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
          </td>
        </tr>
        <tr>
          <td class="l">Note</td>
          <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lAvvocatoSiep.getNote())%></font>&nbsp;</td>
        </tr>
<%
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
            <input class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.revoca.action.ActUploadOrdineEsecuzioneRevoca">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.revoca.action.ActLoadDettaglioOrdineEsecuzioneRevoca">
          </td>
        </tr>
      </table>
</form>
</div>
<br>
<br>
</body>
</html>