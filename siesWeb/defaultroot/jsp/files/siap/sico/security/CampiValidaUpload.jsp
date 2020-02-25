<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
  <%@ page import="siap.sico.evento.action.ICostantiEvento"%>

      <tr>
          <td class="L">Valida Documento</td>
          <td class="L">
            <input type=checkbox name="<%=ICostantiEvento.CAMPO_VALIDA%>" value=1>
          </td>
        </tr>
        <tr>
          <td class="l" rowspan=2>Indica il percorso locale del documento da salvare</td>
          <td class="L">
            <font class="campo">
            <input type=file size="35" name="<%=ICostantiEvento.CAMPO_BLOB%>"></font>
          </td>
        </tr>