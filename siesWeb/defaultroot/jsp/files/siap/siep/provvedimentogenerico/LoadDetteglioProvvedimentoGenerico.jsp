<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="evento"              scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="camponota"    scope="request" class="siap.sico.camponota.model.CampoNotaModel" />
<jsp:useBean id="depositoordinanza"    scope="request" class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel" />
<jsp:useBean id="depositodecreto"    scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel" />
<jsp:useBean id="tenore"    scope="request" class="siap.sius.tenore.model.TenoreModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<%-- MEV10-s3: aggiunto useBean --%>
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
  <title>[S.I.E.S.] - Gestione Annotazione Provvedimento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>
      &nbsp;&nbsp;
      <font class="campo">Dettaglio <%=evento.getDescrTipoProvvedimento()%>&nbsp;<%=evento.getDescrMotivo()%></font>
    </td>
<%
    if (evento.getFlagDocumentoRegistrato() != null)
      if (evento.getFlagDocumentoRegistrato().compareTo("N")==0)
      {
%>
     <td class="LBG">
      <a  href="/jsp/Main.jsp?Action=siap.siep.provvedimentogenerico.action.ActUploadProvvedimentoGenerico&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.provvedimentogenerico.action.ActLoadDettaglioProvvedimentoGenerico&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
        <img  align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
      </a>
     </td>
<%
      }
    if (evento.getFlagDocumentoRegistrato() == null)
    {
%>
     <td class="LBG">
      <a  href="/jsp/Main.jsp?Action=siap.siep.provvedimentogenerico.action.ActUploadProvvedimentoGenerico&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.provvedimentogenerico.action.ActLoadDettaglioProvvedimentoGenerico&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
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
%>            DETENUTO PER ALTRA CAUSA
<%          }
          else
          {
%>            <%=lPosizione.getDescrPosizioneGiuridica()%>
<%          }
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
<%
          }%></tr><% 
        }
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}
    else
    {
%>   <tr>
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
      }%>   </tr><% 
    }
  }
%>
     
<%
        if(evento != null && evento.getDataEmissione()!= null)
        {
%>
          <tr>
          <td class="l">Data Emissione provvedimento</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(), "dd-MM-yyyy") )%></font>
          </td>
          </tr>
<%
        }

       if((depositodecreto != null && depositodecreto.getNumS72()!= null) ||
          (depositoordinanza != null && depositoordinanza.getNumS3() != null))
        {
%><tr>
          <td class="l">Anno/Numero Provvedimento</td>
          <td class="L">
<%
          if(evento != null && "02".equals(evento.getCodTipoProvvedimento()))
           {
%>
             <font class="campo"><%=StringUtils.toStringJSP(depositodecreto.getAnnoS72())%>/<%=StringUtils.toStringJSP(depositodecreto.getNumS72())%></font>

<%
           }else if(evento != null && "03".equals(evento.getCodTipoProvvedimento()))
           {
%>
             <font class="campo"><%=StringUtils.toStringJSP(depositoordinanza.getAnnoS3())%>/<%=StringUtils.toStringJSP(depositoordinanza.getNumS3())%></font>
<%
           }
%>
          </td>
<%
        }
%>
   </tr>
<%
       if(evento != null && evento.getCodTipoProvvedimento() != null)
      {
%>
       <tr>
        <td class="l">Tipo Provvedimento</td>
        <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(evento.getDescrTipoProvvedimento())%></font>
        </td>
       </tr>
<%
      }

       if(evento != null && evento.getDescrUfficioEmittente() != null && !evento.getCodUfficioEmittente().equals("-"))
      {
%>
       <tr>
        <td class="l">Autorità Emittente</td>
        <%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
	    <%
	    	String descrTipoUfficio = StringUtils.toStringJSP(evento.getDescrUfficioEmittente());
	    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
	    			"UDSM".equals(evento.getCodTipoUfficioEmittente())) {
	    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
	    	}
	    %>
        <td class="l">
           <font class="campo"> <%=descrTipoUfficio%></font>&nbsp; di
           <font class="campo"> <%=StringUtils.toStringJSP(evento.getDescrLuogoEmittente())%></font>
        </td>
       </tr>
<%
      }

       if(evento != null && evento.getCodMotivo() != null)
        {
%>
        <tr>
         <td class="l">Oggetto Decisione</td>
         <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(evento.getDescrMotivo())%></font>
          </td>
        </tr>

<%
      }

       if(tenore != null && tenore.getCodEsitoTenore()!= null && tenore.getCodEsitoTenore().length()>0)
        {
%>
         <tr>
          <td class="l">Esito</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(tenore.getDescrEsitoTenore())%></font>
          </td>
         </tr>
<%
        }

       if(camponota != null && camponota.getDescr()!= null &&  camponota.getDescr().length()>0)
        {
%>
         <tr>
          <td class="l">Note</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(camponota.getDescr())%></font>
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
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.provvedimentogenerico.action.ActUploadProvvedimentoGenerico">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= evento.getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.provvedimentogenerico.action.ActLoadDettaglioProvvedimentoGenerico">
          </td>
        </tr>
      </table>
</form>
</div>
<br>
<br>
</body>
</html>