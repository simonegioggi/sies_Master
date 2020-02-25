<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
  SoggettoModel soggetto = fascicoloSiusGP.getFascicoloSiusModel().getSoggetto();
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>
  <table cellspacing=0 cellpadding=0 width=95%>

    <tr>
      <td class="L" width=80%><font class="label">Soggetto:</font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>&nbsp;
<%
        if (soggetto.getSesso().compareTo("F")==0)
        {
%>
          <font class="label">nata il :</font>&nbsp;
<%
        }
        else
        {
%>
          <font class="label">nato il :</font>&nbsp;
<%
        }
%>
      <font class="campo"><%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
      <font class="label">in : </font>
      <font class="campo">
<%
      if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {
%>
        <%=soggetto.getDescrStatoNascita()%>
<%
      }
      else
      {
%>
        <%=soggetto.getDescrComuneNascita()+ "  ("+soggetto.getCodProvinciaNascita()+")" %>
<%
      }
%>
      </font>
     </td>
    </tr>
<%  if ((fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnnoSIEP())!=null)
    {%>
    <tr>
      <td class="L">
        <font class="label">Procedimento SIEP </font>

        <font class="campo">
<%        if ((fascicoloSiusGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep()!= null) )
          {
%>
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep()%>">
              <%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%>
            </a>

            &nbsp;&nbsp;<%=fascicolo.getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getDescrComuneUfficio()%>
<%
          }
          else
          {
            %>-&nbsp;<%}%>


        </font>&nbsp;
        <%
        if (fascicolo.getDataInserimento() != null)
        {%>
          <font class="label"> del </font>&nbsp;
          <font class="campo"><%=DateUtils.getDateToString(fascicolo.getDataInserimento(),"dd-MM-yyyy")%></font>&nbsp;
        <%}%>
      </td>
    </tr>
    <%}%>

    <tr>
      <td class="L">
        <font class="label">Data Udienza : </font>
        <font class="campo">
<%
        if (fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("10") == 0 )
        {%>
          <%=fascicoloSiusGP.getFascicoloSiusModel().getDescrStatoFascicolo()%>
<%      } else {%>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString( fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd-MM-yyyy"), "-" )%>
<%      }%>
        </font>
        <%if( fascicoloSiusGP.getUdiPro()  != null && fascicoloSiusGP.getUdiPro().getFlagRinviata() != null && fascicoloSiusGP.getUdiPro().getFlagRinviata().equalsIgnoreCase("P"))
         {
%>
          <font class="cRosso">  ( Prefissata ) </font>
<%
         }
         %>

      </td>
    </tr>

  </table>