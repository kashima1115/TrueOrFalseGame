package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
*
* 試合詳細取得サーブレット
*
* @author arahari
* @version 1.0
*/

public class BattleDetail extends HttpServlet {

    /**
    *
    * 試合詳細取得メソッド
    *
    * データベースへの検索処理
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	 	 String battleID = request.getParameter("battleId");

        // データベースへの検索処理*****************************************************/
        try{
        	BattleDetailDBAccess bdd = new BattleDetailDBAccess();

            List<LocationBean> list = bdd.LocationBean(battleID);
            List<BattleDetailBean> list2 = bdd.BattleDetailBean(battleID);

            // listをrequestにセット
            request.setAttribute("LocationList", list);
            request.setAttribute("DetailList", list2);


        }catch(Exception e){
            e.printStackTrace();
        }

        // 次のJSPに遷移
        RequestDispatcher rd = request.getRequestDispatcher("/JSP/BattleDetail.jsp");
        rd.forward(request, response);
    }
}
