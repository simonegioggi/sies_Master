<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeModel" %>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe" %>



<jsp:useBean id="FascicoloSiepeEsteso" scope="session" class="siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="FlagFasSiepe"     scope="request" class="java.lang.String"/>

<%
  SoggettoModel soggetto =FascicoloSiepeEsteso.getSoggetto();
  FascicoloSiepModel fascicolo = FascicoloSiepeEsteso.getFascicoloSiep();
  FascicoloGPModel fascicoloSiusGP = FascicoloSiepeEsteso.getFascicoloSius();
  FascicoloSiepeModel fascicoloSIEPE = FascicoloSiepeEsteso.getFascicoloSiepe();

  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>
  <table cellspacing=0 cellpadding=0 width=95%>
<% if (soggetto != null) { %>

    <tr>
      <td class="L" width=80%><font class="label">Soggetto:</font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%><%=retParam%>">
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
<% } %>

<% if (FlagFasSiepe.length()> 0) { %>
    <tr>
      <td class="L">
        <font class="label">Procedimento SIEPE N.</font>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siepe.fascicolo.action.ActLoadDettaglioFascicoloSiepe&<%=ICostantiFascicoloSiepe.CAMPO_ID_FASCICOLO_SIEPE%>=<%=fascicoloSIEPE.getIdFascicoloSiepe()%><%=retParam%>">
          <%=fascicoloSIEPE.getChiaveAnno()%>
          /
          <%=fascicoloSIEPE.getChiaveProgr()%>&nbsp;
        </a>
<%      if(!(UtenteConnesso.getUfficioUtente().getCodUfficio().compareTo(fascicoloSIEPE.getChiaveUfficio())==0))
        {
%>
          &nbsp;<%=fascicoloSIEPE.getDescrUfficioInserimento()%>&nbsp;
<%
        }
%>
        <font class="label"> incarico: </font>
        <font class="campo"><%=fascicoloSIEPE.getDescrIncarico()%></font>
       </td>
    </tr>
<% } %>


<%  if (fascicolo != null)
    {%>
    <tr>
      <td class="L">
        <font class="label">Procedimento SIEP </font>

        <font class="campo">
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%><%=retParam%>">
              <%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%>
            </a>
            &nbsp;&nbsp;<%=fascicolo.getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getDescrComuneUfficio()%>
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
<% if (fascicoloSiusGP != null) { %>
    <tr>
      <td class="L">
        <font class="label">Procedimento SIUS N.</font>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%><%=retParam%>">
          <%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnno()%>
          /
          <%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveProgr()%>&nbsp;
        </a>
<%      if(!(UtenteConnesso.getUfficioUtente().getCodUfficio().compareTo(fascicoloSiusGP.getFascicoloSiusModel().getChiaveUfficio())==0))
        {
%>
          &nbsp;<%=fascicoloSiusGP.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=fascicoloSiusGP.getFascicoloSiusModel().getDescrComuneUfficio()%>&nbsp;-&nbsp;
<%
        }
%>
        <font class="label"> relativo a: </font>
        <font class="campo"><%=fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font>
       </td>
    </tr>
<% } %>

  </table>