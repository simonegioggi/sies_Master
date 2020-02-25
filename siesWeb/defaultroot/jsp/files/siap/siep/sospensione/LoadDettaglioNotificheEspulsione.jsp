<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<%
//  < jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
%>
<jsp:useBean id="flagergastolo"        scope="request" class="java.lang.String"/>
<jsp:useBean id="decretoordinanza"     scope="request" class="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"/>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="autorita"             scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>


<jsp:useBean id="sospensione" scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>

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

  //Inizializzazione campi se SOSPENSIONE non trovata
  //(per evitare eventuale NullPointerException)
  if(sospensione.getIdSospensione() == null)
    sospensione.setQuantumZero();
%>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Provvedimento Espulsione</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>

<body class="corpo">
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciSosp">

  <table>
    <tr>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <INPUT type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
      <font class="campo">Provvedimento Espulsione</font>
    </td>
<%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
 if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) {%>
 <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
     <%--td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaSospensioneOE&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
            <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
          </a>
        </td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaSospensioneOE&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   </jsp:include>
<%}%>

<%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
 {%>
 <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
     <%--td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaSospensioneOE&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
            <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
          </a>
        </td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaSospensioneOE&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   </jsp:include>
<%}%>

    </tr>
  </table>
  <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=7>
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

<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" 
	name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
</tr>
--%>
<%
	  if (  sospensione.getNumAnniPenaEspiata().intValue()!=0
	  	 || sospensione.getNumMesiPenaEspiata().intValue()!=0
	  	 || sospensione.getNumGiorniPenaEspiata().intValue()!=0 )
	  {
%>
       <tr>
				<td class="l">
					<font class="label">Pena Espiata</font>
				</td>
				<td class="l">
					<font class="label">Anni</font>
					<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaEspiata(), "0")%></font>
					<font class="label">Mesi</font>
					<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaEspiata(), "0")%></font>
					<font class="label">Giorni</font>
					<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaEspiata(), "0")%></font>
				</td>
			</tr>
<%
	  }
	  if (  flagergastolo.equals("N")
       && (sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
	  	 || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
	  	 || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0
       || sospensione.getNumAnniPenaResiduaArres().intValue()!=0
	  	 || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
	  	 || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0)
      )
	  {
%>
      <tr>
				<td class="l">
					<font class="label">Pena Residua</font>
				</td>
				<td class="l">
<%
          if (  flagergastolo.equals("N")
             && (sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
	  	       || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0)
             )
          {
%>
            <font class="label">Reclusione : </font>
            <font class="label">Anni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaReclus(), "0")%></font>
            <font class="label">Mesi</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaReclus(), "0")%></font>
            <font class="label">Giorni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaReclus(), "0")%></font>
<%
            if(sospensione.getMultaResidua()!=null && sospensione.getMultaResidua().compareTo(new BigDecimal(0))!=0)
            {
%>
              <font class="label">Multa </font>
              <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getMultaResidua())%></font>&nbsp;€&nbsp;
<%
            }
          }
          if (  flagergastolo.equals("N")
             && (sospensione.getNumAnniPenaResiduaArres().intValue()!=0
             || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0) )
          {
%>
            <font class="label"> Arresto : </font>
            <font class="label">Anni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaArres(), "0")%></font>
            <font class="label">Mesi</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaArres(), "0")%></font>
            <font class="label">Giorni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaArres(), "0")%></font>
<%
            if(sospensione.getAmmendaResidua()!=null && sospensione.getAmmendaResidua().compareTo(new BigDecimal(0))!=0)
            {
%>
              <font class="label">Ammenda </font>
              <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getAmmendaResidua())%></font>&nbsp;€&nbsp;
<%
            }
          }

          if (  flagergastolo.equals("N")
             && (sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
             || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0
             || sospensione.getNumAnniPenaResiduaArres().intValue()!=0
             || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0)
            )
          {
%>
              </td>
            </tr>
<%
          }
    }

    if(flagergastolo.equals("S"))
    {
%>
      <tr>
        <td class="l">
          <font class="label">Pena Complessiva</font>
        </td>
        <td class="l">
          <font class="campo">ERGASTOLO</font>
        </td>
      </tr>
<%
    }else if(flagergastolo.equals("D"))
     {
%>
      <tr>
        <td class="l">
          <font class="label">Pena Complessiva</font>
        </td>
        <td class="l">
          <font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>
        </td>
      </tr>
<%
     }
%>
    </table>
    <br>
   <table style="width: 95%;">
      <tr>
        <td colspan=6 class="titolo">Dati dell' Espulsione</td>
      </tr>


<%if(decretoordinanza.getDataEmissioneProvvedimento() != null ){%>
<tr>
      <td class="l">
        Data Emissione
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataEmissioneProvvedimento(),"dd-MM-yyyy"))%></font>&nbsp;
     </td>
 </tr>
<%}%>
<%if(decretoordinanza.getDataRicezioneProvvedimento() != null ){%>
<tr>
      <td class="l">
        Data Ricezione
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRicezioneProvvedimento(),"dd-MM-yyyy"))%></font>&nbsp;
     </td>
 </tr>
<%}%>
     <tr>
      <td class="l" width="18%">
       Autorità
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getAltraAutorita())%></font>&nbsp;
       </td>
 </tr>
 <tr>
      <td class="l">
       Luogo</TD>
    <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getAltroLuogo())%></font>&nbsp;
      </td>
    </tr>
<tr>
      <td class="l">
       Data Scarcerazione per espulsione
      </td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataEspulsione(),"dd-MM-yyyy"))%></font>&nbsp;
    </td>
  </tr>
  <tr>
    <td class="l">
      Magistrato Competente
    </td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%> &nbsp; <%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%></font>
    </td>
  </tr>

<%if(!autorita.getDescrTipoAutorita().equals("")){%>

 <tr>
   <td class="l">Autorità di Polizia</td>

      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(autorita.getDescrTipoAutorita())%>  </font>di
        <font class="campo"><%=StringUtils.toStringJSP(autorita.getDescrSede())%></font>
</td>

</tr>
<%}%>
<%int count=0;
while(count < eventonotifica.getNotifiche().length)
{
  NotificaModel lNotMod = eventonotifica.getNotifiche()[count];
 if(lNotMod.getCodTipoNotifica().equals("N") && lNotMod.getAutoritaEsterna()!=null && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null)
  {
     AvvocatoSiepModel lAvvMod = eventonotifica.getNotifiche()[count].getAvvSiep();

     AutoritaEsternaModel lAuMod = eventonotifica.getNotifiche()[count].getAutoritaEsterna();
    %>
     <tr>
       <td class="l">Avvocato per  Notifica</td>
       <td class="L" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome())+" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp;
        &nbsp;Foro di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%>
        </font>
        &nbsp;Difensore di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%>
        </font>
       </td>
      </tr>
   <tr>
	    <td class="l">Autorita Notifica</td>
            <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>&nbsp;
         di
        <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
      </td>
     </tr>
     <tr>
      <td class="l">Note</td>
      <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
     </tr>

<%
}
count++;
}
%>

</table>

</form>
 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table width="90%">
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActUploadEspulsione">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sospensione.action.ActLoadDettaglioNotificheDifferimento">
            <input type="HIDDEN" name="lFagAzione" value="E">

          </td>
        </tr>
      </table>
</form>
</div>
  <br>
  <br>

  </body>
</html>