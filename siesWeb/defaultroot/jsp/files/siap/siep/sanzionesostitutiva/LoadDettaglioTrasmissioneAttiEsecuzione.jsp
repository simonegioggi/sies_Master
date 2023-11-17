<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel"%>

<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="lPenComSanSost"      scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"/>
<jsp:useBean id="misurecautelari"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="residenzaassociata"  scope="request" class="siap.sico.residenza.model.ResidenzaAssociataModel"/>

<%
  EventoNotificaModel lEve = evento;

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

  MagistratoModel lMagistrato = lEve.getMagistrato();
  if( lMagistrato == null)
    lMagistrato = new MagistratoModel();
  
  PenaComplessivaSanzioneSostitutivaModel lPenaComplessSSMod = lPenComSanSost;
//   if(lPenaComplessSSMod == null)
//   	lPenaComplessSSMod = new PenaComplessivaSanzioneSostitutivaModel();
%>

<html>
  <head>
    <title> [S.I.E.S.] - Dettaglio Evento- </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

    <script language="JavaScript">


    function conferma(azione)
    {
      document.comandi.<%=IWebConstants.ACTION_FIELD%>.value = azione;
      document.comandi.submit();
  
    }
  
 
  </script>

  </head>
  <BODY class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Trasmissione Atti per l'Esecuzione</font>
        </td>
 <%if (lEve.getEvento().getFlagDocumentoRegistrato()!=null)
 if (lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0)
{%>

<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActStampaTrasmissioneAttiEsecuzione&IdEvento="+lEve.getEvento().getIdEvento()%>"/>
   </jsp:include>
 <%}%>

<%if (lEve.getEvento().getFlagDocumentoRegistrato()==null)
 {%>

<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActStampaTrasmissioneAttiEsecuzione&IdEvento="+lEve.getEvento().getIdEvento()%>"/>
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
      <td class="L" colspan=5>
          <font class="campo">
<%     if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       {%>
              DETENUTO PER ALTRA CAUSA
<%     }
       else
       {%>
         <%=lPosizione.getDescrPosizioneGiuridica()%>
<%     }%>
         </font>
<%if(lPosizione.isLibero() && residenzaassociata != null && residenzaassociata.getResidenza()!= null){%>   
      Residenza 
          <font class="campo">

         <%=residenzaassociata.getResidenza().getIndirizzo()%>&nbsp;<%=residenzaassociata.getResidenza().getDescrComune()%>

         </font>

 <%} %>           
   </td>
</tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null)
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
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
        else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
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
 
<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getAltroLuogo() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
             </tr>
<%
          }
        }



      PenaComplessivaModel lPenCompMod=lPenaComplessSSMod.getPenaComplessiva();
      if(lPenCompMod!=null)
      {
      %>
		  <tr>
		    <td class="L">
          <font class="label">Pena irrogata in sentenza : </font></td>
         <td class="L" colspan="5">
          <%
          if (   (lPenCompMod.getNumAnniReclusione()!=null && lPenCompMod.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0)
        		  || (lPenCompMod.getNumMesiReclusione()!=null && lPenCompMod.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0)
        		  || (lPenCompMod.getNumGiorniReclusione()!=null && lPenCompMod.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0)
        		 )
          {%>
          <font class="campo">Reclusione</font>
          <font class="label">Anni</font>
          <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniReclusione(),"0")%></font>
          <font class="label">Mesi</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiReclusione(),"0")%></font>
          <font class="label">Giorni</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
          <%}%>

          <%if(lPenCompMod.getImportoMulta()!=null && lPenCompMod.getImportoMulta().compareTo(new BigDecimal(0))!=0){%>
          <font class="label">Multa </font>
          <font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
          <%}%>

          <%
          if (   (lPenCompMod.getNumAnniArresto()!=null && lPenCompMod.getNumAnniArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenCompMod.getNumMesiArresto()!=null && lPenCompMod.getNumMesiArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenCompMod.getNumGiorniArresto()!=null && lPenCompMod.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0)
             )
          {%>
          <font class="campo">Arresto</font>
          <font class="label">Anni</font>
          <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniArresto(),"0")%></font>
          <font class="label">Mesi</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiArresto(),"0")%></font>
          <font class="label">Giorni</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
          <%}%>

          <%if(lPenCompMod.getImportoAmmenda()!=null && lPenCompMod.getImportoAmmenda().compareTo(new BigDecimal(0))!=0) {%>
          <font class="label">Ammenda </font>
          <font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
          <%}%>

          <% if (lPenCompMod.getCodTipoPenaDetentiva().equals("03") || lPenCompMod.getCodTipoPenaDetentiva().equals("04")) {%>
          <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getDescrTipoPenaDetentiva())%></font>
            <%if(lPenCompMod.getCodTipoPenaDetentiva().equals("04")){%>
              <%if(lPenCompMod.getNumAnniIsolamentoDiurno()!=null){%>
              <font class="label">Anni</font>
              <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniIsolamentoDiurno(),"0")%></font>
              <%}%>

              <%if(lPenCompMod.getNumMesiIsolamentoDiurno()!=null){%>
              <font class="label">Mesi</font>
              <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiIsolamentoDiurno(),"0")%></font>
              <%}%>

              <%if(lPenCompMod.getNumGiorniIsolamentoDiurno()!=null){%>
              <font class="label">Giorni</font>
              <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniIsolamentoDiurno(),"0")%></font>
              <%}%>
            <%}%>
         <%}%>
      </td>
    </tr>
<%
  }



if(lPenaComplessSSMod!=null)
{
SanzioneSostitutivaModel lSanSos = lPenaComplessSSMod.getSanzioneSostitutiva();
if(lSanSos != null && lSanSos.getIdSanzioneSostitutiva() != null)
{
%>

<tr>
<td class="L"><font class="label">Sanzione Sostitutiva applicata: </font></td>
<td class="L" colspan="5">
<%
if((lSanSos.getNumAnni()!=null && lSanSos.getNumAnni().compareTo(new BigDecimal(0))!=0) || (lSanSos.getNumMesi()!=null && lSanSos.getNumMesi().compareTo(new BigDecimal(0))!=0) || (lSanSos.getNumGiorni()!=null && lSanSos.getNumGiorni().compareTo(new BigDecimal(0))!=0))
{
%>

<font class="campo"><%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%>&nbsp;</font>
<font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumAnni(), "0")%>&nbsp;</font>
<font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumMesi(), "0")%>&nbsp;</font>
<font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumGiorni(), "0")%></font>

<%
}

if(lSanSos.getSanzionePecuniariaMulta() != null && lSanSos.getSanzionePecuniariaMulta().intValue() != 0)
{
%>
<font class="label"> Sanz.Pec. Multa&nbsp;</font><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaMulta())%>&nbsp;</font>&euro;
<%
}

if(lSanSos.getSanzionePecuniariaAmmenda() != null && lSanSos.getSanzionePecuniariaAmmenda().intValue() != 0)
{
%>
<font class="label"> Sanz.Pec. Ammenda&nbsp;</font><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaAmmenda())%>&nbsp;</font>&euro;
<%
}
%>
</td>
</tr>
<%
}
}  

 if(misurecautelari != null && !misurecautelari.isEmpty())
 {
%>	 
    <tr>
      <td class="l">Misure Cautelari Computate:</td>
 <%
	 MisuraCautelareModel lMisCauMod = null;
     Iterator lItx = misurecautelari.iterator();
     while (lItx.hasNext())
     {
    	 lMisCauMod = (MisuraCautelareModel) lItx.next();
         if (lMisCauMod.getFlagComputabile().equals("S"))
         {
%>

 <td class="l" colspan="5">
   <font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getDescrTipoMisura())%>&nbsp;</font>
   <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumAnni(), "0")%>&nbsp;</font>
   <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumMesi(), "0")%>&nbsp;</font>
   <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumGiorni(), "0")%></font>
 </td>

<%
         } 

     }
%>   
     </tr>  
     
 <%}    
     
  if(penaresidua.getIdPenaResidua() != null)
    {
%>
<tr>
  <td class="l">Pena da espiare:</td>
<% 
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
          <td class="l" colspan=2>Reclusione
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
         Multa
          <font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
        }

    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
%>
      <td class="l" >Arresto
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
    
      Ammenda
      <font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
   
%>
</tr>
<%
     
    }
%>

<%
  if(penaresidua != null && penaresidua.getFlagSanzioneSostitutiva()!=null)
  {
%>
     <tr>
      <td class="l">Sanzione sostitutiva da espiare:</td>  
      <td class="L">
           <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%>&nbsp;</font>
           <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
           <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
           <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>
<% 
				 if(   (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0)
            || (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0)
           )
         {
%>
          <font class="label"> Sanz.Pec.&nbsp;</font>
          <% if (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0) { %>
          <font class="campo">Multa&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoMultaSS())%>&nbsp;</font>&euro;
          <% } %>
          <% if (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0) { %>
          <font class="campo">Ammenda&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoAmmendaSS())%>&nbsp;</font>&euro;
          <% } %>
<%
         }
%> 
      </td>
    </tr>

<%}      
%>
  </table>  
    <table>
       <tr>
        <td class="l">Data Emissione</td>
        <td class="L" colspan=1>
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getEvento().getDataEmissione(), "dd/MM/yyyy"))%>
          </font>
        </td>
        
        <td class="l">Data Trasmissione</td>  
         		<td class="L" colspan=1><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getNotifiche()[0].getDataInvio(),"dd/MM/yyyy"))%></font></td>
      </tr>

<%
  if(magistrato != null){%>
  <tr>
   <td class="l">Magistrato
   <td class="L">
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
   </td>
  </tr>
<%}%>
<%//modifica relativa al tipo istituto
 if(lEve.getNotifiche() != null && lEve.getNotifiche().length > 0 && lEve.getNotifiche()[0].getUfficio() != null){%>
     <tr>
      <td class="l">Autorità Destinazione</td>
      <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( lEve.getNotifiche()[0].getUfficio().getDescrTipoUfficio() )%></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP( lEve.getNotifiche()[0].getUfficio().getDescrComune())%></font>&nbsp;
      </td>
     </tr>
<%}%>
  </table>
  <br>

 <%if (lEve.getEvento().getFlagDocumentoRegistrato()== null || (lEve.getEvento().getFlagDocumentoRegistrato()!=null && 
	   lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0))
{%>


  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" action="<%= IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lEve.getEvento().getIdEvento() %>">
    <input type="HIDDEN" name="<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>" value="<%=lEve.getNotifiche()[0].getUfficio().getCodUfficio()%>">
  
        
      <table>
        <tr>
          <td class="l" rowspan=2>Documento da salvare</td>
          <td class="L">
            <font class="campo">
            <input type=file size="35" name="<%=ICostantiEvento.CAMPO_BLOB%>"></font>
          </td>
        </tr>
        <tr>
     <tr>
       <td class="lNoBord">
        <br><br><INPUT class="bottone" type="button" name="I" value="Conferma" onClick="javascript:return conferma('siap.siep.sanzionesostitutiva.action.ActUploadTrasmissioneAttiEsecuzione');">
       </td>
       <td class="lNoBord">
        <br><br><INPUT class="bottone" type="button" name="I" value="Conferma Trasmissione" onClick="javascript:return conferma('siap.siep.sanzionesostitutiva.action.ActConfermaTrasmissioneAttiEsecuzione');">
       </td>       
    </tr>        
        
      </table>
    </FORM>
  </div>
  
 <%
}

 if (lEve.getEvento().getFlagDocumentoRegistrato()!=null &&
     lEve.getEvento().getFlagDocumentoRegistrato().equals("S")&&
     lEve.getEvento().getCodUfficioDestinatario() != null &&
     lEve.getEvento().getCodUfficioDestinatario().equals("-"))
{%>
 <div align=left style="visibility:visible" id="upld">
    <FORM name="comandi"  enctype="multipart/form-data" method="post" action="<%= IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lEve.getEvento().getIdEvento() %>">
    <input type="HIDDEN" name="<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>" value="<%=lEve.getNotifiche()[0].getUfficio().getCodUfficio()%>">
  
    <table>
     <tr>
       <td class="lNoBord">
        <br><br><INPUT class="bottone" type="button" name="I" value="Conferma Trasmissione" onClick="javascript:return conferma('siap.siep.sanzionesostitutiva.action.ActConfermaTrasmissioneAttiEsecuzione');">
       </td>       
    </tr>  
      </table>
          </FORM>
  </div>
<%}%>
  <br>
  <br>
</body>

</html>