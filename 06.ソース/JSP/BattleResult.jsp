<%@ page language="java" contentType="text/html; charset=Windows-31J"
    pageEncoding="Windows-31J"%>
<%@ page import="java.util.*" %>
<%@ page import="servlet.TrueOrFalseBean" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
    <title>○×ゲームの試合結果</title>
  </head>
  <body>
    <h1>○×ゲームの試合結果一覧表</h1>

    <form action="../servlet/servlet.BattleDetail" method="post">
    <% // 検索結果のデータリストを取得
       List<TrueOrFalseBean> list = (List<TrueOrFalseBean>)request.getAttribute("list");
       // 取得できなかった場合、メッセージ出力
       // 取得できた場合、データを出力
       if(list.isEmpty()) {
           out.print("該当のデータはありません。");
       }else{   %>

    <table border="1">
      <tr>
      	<th>選択</th>
        <th>試合ID</th>
        <th>先攻対戦者情報</th>
        <th>勝敗</th>
        <th>後攻対戦者情報</th>
        <th>勝敗</th>
        <th>試合日</th>

      </tr>

<!-- iが奇数の際に先攻ロジック情報を呼び出し -->
<!-- 偶数のときに後攻のロジック情報を呼び出すことで一つの表にまとめる -->

<% int i = 0; %>

      <% for(TrueOrFalseBean bn : list) { %>

<% i++; %>

<% if(i % 2 != 0){ %>

      <tr>
		<td><input type="radio" name="battleId" value="<%=bn.getBattle_id() %>" <%if(i == 1){ %>checked<% } %>></td>
        <td><%=bn.getBattle_id() %></td>

        <td><%=bn.getPFlogic_name() %>　<%=bn.getPFlogic_writer() %>　<%=bn.getPFlogic_ver() %></td>
        <td><%=bn.getResult() %></td>

         <% } %>

         <% if(i % 2 == 0){ %>

        <td><%=bn.getLogic_name() %>　<%=bn.getLogic_writer() %>　<%=bn.getLogic_ver() %></td>
        <td><%=bn.getResult() %></td>

        <td><%=bn.getYear() %>年<%=bn.getMonth() %>月<%=bn.getDay() %>日</td>
      </tr>

 <% } %>

	  <% } %>
    </table>

    <% } %>

    <input type="submit"  value="選択した試合の詳細を見る" />

    </form>
    <form action="../servlet/servlet.BattleResult" method="get">

    <input type="submit"  value="更新" />

    </form>
  </body>
</html>