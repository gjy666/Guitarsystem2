package myServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.Builder;
import dao.Type;
import dao.Wood;
import main.action.Inventory;
import main.domain.Guitar;
import main.domain.GuitarSpec;
import main.util.WrappingDatabase;

/**
 * Servlet implementation class GuitarMatched
 */
@WebServlet("/GuitarMatched")
public class GuitarMatched extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GuitarMatched() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter();
		JSONArray ja = new JSONArray();

		Inventory inventory = new Inventory();

		// ��ʼ��inventory
		String sql = "select * from Guitar";
		WrappingDatabase db = new WrappingDatabase();
		try {
			ResultSet rs = db.query(sql);
			while (rs.next()) {
				Builder builder = Builder.valueOf(rs.getString("builder").toUpperCase());
				String model = rs.getString("model");
				Type type = Type.valueOf(rs.getString("type").toUpperCase());
				Wood backWood = Wood.valueOf(rs.getString("backWood").toUpperCase());
				Wood topWood = Wood.valueOf(rs.getString("topWood").toUpperCase());
				GuitarSpec Spec = new GuitarSpec(builder, model, type,  backWood, topWood);
				inventory.addGuitar(rs.getString("serialNumber"), Double.parseDouble(rs.getString("price")), Spec);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ��ʼ���û���ѯ��GuitarSpec
		Builder _builder = Builder.valueOf(request.getParameter("Builder").toUpperCase());
		Type _type = Type.valueOf(request.getParameter("Type").toUpperCase());
		Wood _backWood = Wood.valueOf(request.getParameter("BackWood").toUpperCase());
		Wood _topWood = Wood.valueOf(request.getParameter("TopWood").toUpperCase());

		String _model = request.getParameter("Model");
		GuitarSpec userSpec = new GuitarSpec(_builder, _model, _type, _backWood, _topWood);

		// ��ѯƥ��
		List<Guitar> Guitars = inventory.search(userSpec);

		if (!Guitars.isEmpty()) {
			for (Iterator<Guitar> i = Guitars.iterator(); i.hasNext();) {
				Guitar guitar = (Guitar) i.next();
				GuitarSpec spec = guitar.getSpec();
				// д��json
				JSONObject jo = new JSONObject();
				jo.put("serialNumber", guitar.getSerialNumber());
				jo.put("price", guitar.getPrice());
				jo.put("builder", spec.getBuilder().toString());
				jo.put("model", spec.getModel());
				jo.put("type", spec.getType().toString());
				jo.put("backwood", spec.getBackWood().toString());
				jo.put("topwood", spec.getTopWood().toString());
				ja.put(jo);
			}
		}
		out.print(ja.toString());
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
