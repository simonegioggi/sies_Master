<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="TornaQui"            scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>

<%
//Fascicolo SIGE
FascicoloSigeModel lFascicolo = FascicoloSigeEsteso.getFascicoloSige();
//Fascicolo SIEP
FascicoloSiepModel fascicoloSiep = FascicoloSigeEsteso.getFascicoloSiep();

String lDataUdienza = null;
if (FascicoloSigeEsteso.getUdienzaProcedimento()!=null && FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige()!=null)
{
	lDataUdienza = DateUtils.getDateToString(FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige(),"dd-MM-yyyy");
}else{
	lDataUdienza = "-";
}
	
// presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>
  <table cellspacing=0 cellpadding=0 width=95%>

    <tr>
      <td class="L">
        <font class="label">Procedimento N.</font>
         <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=lFascicolo.getIdFascicoloSige()%><%=retParam%>">
       		 <%=lFascicolo.getChiaveAnno() %>/<%=lFascicolo.getChiaveProgr()%>
        </a>
       		 <font class="campo"> &nbsp;&nbsp;<%=lFascicolo.getDescrUfficio()%> </font>
       </td>
    </tr>
    <tr>
      <td class="L"><font class="label">Soggetto: </font>
     	<jsp:include page="<%=ICostantiFascicoloSige.PG_INCLUDE_SOGGETTO%>"/>
      </td>
    </tr>

<%  if (fascicoloSiep != null)
    {%>
    <tr>
      <td class="L">
        <font class="label">Procedimento SIEP </font>

        <font class="campo">
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloSiep.getIdFascicoloSiep()%><%=retParam%>">
              <%=fascicoloSiep.getChiaveAnno()%>/<%=fascicoloSiep.getChiaveProgr()%>
            </a>
            &nbsp;&nbsp;<%=fascicoloSiep.getDescrTipoUfficio()%>&nbsp;<%=fascicoloSiep.getDescrComuneUfficio()%>
        </font>&nbsp;
        <%
        if (fascicoloSiep.getDataInserimento() != null)
        {%>
          <font class="label"> del </font>&nbsp;
          <font class="campo"><%=DateUtils.getDateToString(fascicoloSiep.getDataInserimento(),"dd-MM-yyyy")%></font>&nbsp;
        <%}%>
      </td>
    </tr>
    <%}%>
    
    <tr>
      <td class="L">
        <font class="label">Data Udienza : <%=lDataUdienza%> </font>
      </td>
    </tr>
  </table>
 <br>  