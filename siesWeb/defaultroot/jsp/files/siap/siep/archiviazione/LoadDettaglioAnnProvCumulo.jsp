<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.archiviazione.model.ArchiviazioneModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>

<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="archiviazione"       scope="request" class="siap.siep.archiviazione.model.ArchiviazioneModel"/>
<jsp:useBean id="fascicolosiep"       scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>


<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  String lcodicePosizione =lPosizione.getCodPosizioneGiuridica();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  FascicoloSiepModel lFascMod = fascicolosiep;
  ArchiviazioneModel lArcMod = archiviazione;

//   if(lPosizione == null)
//     lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

//   if(lArcMod == null)
//     lArcMod = new ArchiviazioneModel();

//   if(lFascMod == null)
//     lFascMod = new FascicoloSiepModel();
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione Archiviazione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Annotazione dati cumulo</font>
    </td>

<%
    if (eventonotifica.getEvento().getFlagDocumentoRegistrato() == null ||
        (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null && eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0))
      {
%>
     <td class="LBG">
      <a  href="/jsp/Main.jsp?Action=siap.siep.archiviazione.action.ActUploadAnnProvCumulo&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.archiviazione.action.ActLoadDettaglioAnnProvCumulo&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
        <img  align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
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
        }

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
           </tr>
<%
          }
        }

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
		<tr>
          <td class="l">Reclusione</td>
          <td class="l" >
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
<%
          if(penaresidua.getImportoMulta() != null && penaresidua.getImportoMulta().compareTo(new BigDecimal(0))!=0)
          {
%>
            <td class="l">Multa</td>
            <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
           </tr>
<%
          }
        }
%>
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
      if(penaresidua.getImportoAmmenda() != null && penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
      {
%>
        <td class="l">Ammenda</td>
        <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
    }
  }
%>
      </tr>
<%
    if(penaresidua != null)
     {
%>
      <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l" width="25%">Pena Espiata dal</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
       }

       if ( penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
        }
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
       }

 if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
 {
    if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">al</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
             </td>
<%
            }else
            {
%>
              <td class="l">al</td>
              <td class="lRosso" colspan=2>
               <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              </td>
<%          }
        }
      }
%>

   </tr>
<%
  }
%>


<%
      if(lArcMod.getCodOggettoDefinizione()!= null)
      {
%>
       <tr>
          <td class="l">Oggetto Definizione</td>
        <td class="l">
          <font class="campo"> <%=StringUtils.toStringJSP(lArcMod.getDescrOggettoDefinizione())%></font>
        </td>
       </tr>
<%
      }

       if(lFascMod.getDataUnione()!= null)
        {
%>
         <tr>
          <td class="l">Data Provvedimento di cumulo</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascMod.getDataUnione(), "dd-MM-yyyy") )%></font>
          </td>
         </tr>
<%
        }

        if(lFascMod.getCodUfficioUnione()!= null && lFascMod.getDescrComuneUfficioUnione() != null)
        {
%>
         <tr>
          <td class="l">Ufficio che ha emesso il cumulo</td>
          <td class="L">
            <font class="campo"> <%=StringUtils.toStringJSP(lFascMod.getDescrTipoUfficioUnione())%></font>&nbsp;di
            <font class="campo"><%=StringUtils.toStringJSP(lFascMod.getDescrComuneUfficioUnione())%></font>
          </td>
         </tr>
<%
        }


       if(lFascMod.getNumFascicoloUnione() != null && lFascMod.getAnnoFascicoloUnione() != null)
        {
%>
        <tr>
         <td class="l">Numero Procedimento SIEP</td>
         <td class="l">
          <font class="campo"> <%=StringUtils.toStringJSP(lFascMod.getAnnoFascicoloUnione())%></font>/
          <font class="campo"><%=StringUtils.toStringJSP(lFascMod.getNumFascicoloUnione())%></font>
        </td>
       </tr>
<%
        }


       if(lArcMod.getNote()!= null)
        {
%>
         <tr>
          <td class="l">Note</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(lArcMod.getNote())%></font>
          </td>
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
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.archiviazione.action.ActUploadAnnProvCumulo">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.archiviazione.action.ActLoadDettaglioAnnProvCumulo">
          </td>
        </tr>
      </table>
</form>
</div>
<br>
<br>
</body>
</html>